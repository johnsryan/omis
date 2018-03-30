/*
 * OMIS - Offender Management Information System
 * Copyright (C) 2011 - 2017 State of Montana
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package omis.supervision.web.controller;

import java.beans.PropertyEditor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import omis.beans.factory.PropertyEditorFactory;
import omis.beans.factory.spring.CustomDateEditorFactory;
import omis.datatype.DateRange;
import omis.exception.DuplicateEntityFoundException;
import omis.locationterm.domain.LocationTerm;
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.organization.domain.Organization;
import omis.region.domain.State;
import omis.report.ReportFormat;
import omis.report.ReportRunner;
import omis.report.web.controller.delegate.ReportControllerDelegate;
import omis.supervision.domain.CorrectionalStatus;
import omis.supervision.domain.PlacementStatus;
import omis.supervision.domain.PlacementTerm;
import omis.supervision.domain.PlacementTermChangeReason;
import omis.supervision.domain.PlacementTermNote;
import omis.supervision.domain.SupervisoryOrganization;
import omis.supervision.exception.CorrectionalStatusTermConflictException;
import omis.supervision.exception.OffenderNotUnderSupervisionException;
import omis.supervision.exception.PlacementTermConflictException;
import omis.supervision.exception.PlacementTermExistsAfterException;
import omis.supervision.exception.PlacementTermExistsBeforeException;
import omis.supervision.exception.PlacementTermExistsException;
import omis.supervision.exception.PlacementTermLockedException;
import omis.supervision.exception.PlacementTermNoteExistsException;
import omis.supervision.exception.SupervisoryOrganizationTermConflictException;
import omis.supervision.report.PlacementTermReportService;
import omis.supervision.report.PlacementTermSummary;
import omis.supervision.service.PlacementTermService;
import omis.supervision.web.form.PlacementTermForm;
import omis.supervision.web.form.PlacementTermNoteFields;
import omis.supervision.web.form.PlacementTermNoteItem;
import omis.supervision.web.form.PlacementTermNoteItemOperation;
import omis.supervision.web.validator.PlacementTermFormValidator;
import omis.util.DateManipulator;
import omis.web.controller.delegate.BusinessExceptionHandlerDelegate;
import omis.web.util.RedirectUrlRetriever;

/**
 * Controller for placement term.
 * 
 * @author Stephen Abson
 * @author Joshua Divine
 * @version 0.1.0 (Oct 4, 2013)
 * @since OMIS 3.0
 */
@Controller
@RequestMapping("/supervision/placementTerm")
@PreAuthorize("hasRole('USER')")
public class PlacementTermController {

	/* View names. */
	
	private static final String LIST_VIEW_NAME
			= "supervision/placementTerm/list";

	private static final String EDIT_FORM_VIEW_NAME
			= "supervision/placementTerm/edit";
	
	private static final String SUPERVISORY_ORGANIZATION_OPTIONS_VIEW_NAME
			= "supervision/supervisoryOrganization/includes/"
					+ "supervisoryOrganizationOptions";
	
	private static final String 
		PLACEMENT_TERM_START_CHANGE_REASON_OPTIONS_VIEW_NAME
			= "supervision/placementTerm/includes/"
					+ "placementTermStartChangeReasonOptions";

	private static final String 
		PLACEMENT_TERM_END_CHANGE_REASON_OPTIONS_VIEW_NAME
			= "supervision/placementTerm/includes/"
					+ "placementTermEndChangeReasonOptions";
	
	private static final String PLACEMENT_STATUS_OPTIONS_VIEW_NAME
			= "supervision/placementTerm/includes/placementStatusOptions";
	
	private static final String BOOLEAN_VALUE_VIEW_NAME
			= "common/json/booleanValue";
	
	/* Action menus. */
	
	private static final String PLACEMENT_TERM_ACTION_MENU_VIEW_NAME
			= "supervision/placementTerm/includes/placementTermActionMenu";

	private static final String PLACEMENT_TERMS_ACTION_MENU_VIEW_NAME
			= "supervision/placementTerm/includes/placementTermsActionMenu";

	private static final String PLACEMENT_TERM_TABLE_ROW_ACTION_MENU_VIEW_NAME
		= "supervision/placementTerm/includes/placementTermTableRowActionMenu";
	
	private static final String PLACEMENT_TERM_NOTES_ACTION_MENU_VIEW_NAME
		= "supervision/placementTerm/includes/placementTermNotesActionMenu";
	
	private static final String PLACEMENT_TERM_NOTE_TABLE_ROW_VIEW_NAME
		= "supervision/placementTerm/includes/placementTermNoteTableRow";
	
	/* URLs */
	
	private static final String LIST_BY_OFFENDER_URL
		= "/supervision/placementTerm/list.html?offender=%d";
	
	/* Redirects. */
	
	private static final String LIST_BY_OFFENDER_REDIRECT
		= "redirect:" + LIST_BY_OFFENDER_URL;
	
	private static final String CREATE_LOCATION_TERM_REDIRECT
			 = "redirect:/locationTerm/create.html?offender=%d&organization=%d"
					 + "&defaultStartDate=%s&defaultStartTime=%s"
					 + "&defaultEndDate=%s&defaultEndTime=%s";

	private static final String CREATE_LOCATION_TERM_WITHOUT_END_DATE_REDIRECT
	 		= "redirect:/locationTerm/create.html?offender=%d&organization=%d"
	 				+ "&defaultStartDate=%s&defaultStartTime=%s";
	
	private static final String EDIT_LOCATION_TERM_REDIRECT
			= "redirect:/locationTerm/edit.html?locationTerm=%d";
	
	private static final String
	EDIT_LOCATION_TERM_WITH_DEFAULT_END_DATE_REDIRECT
			= "redirect:/locationTerm/edit.html?locationTerm=%d"
				+ "&defaultEndDate=%s&defaultEndTime=%s";
	
	/* Model keys. */
	
	private static final String PLACEMENT_TERM_SUMMARIES_MODEL_KEY
			= "placementTermSummaries";
	
	private static final String CURRENT_DATE_MODEL_KEY = "currentDate";

	private static final String PLACEMENT_TERM_FORM_MODEL_KEY
			= "placementTermForm";

	private static final String OFFENDER_MODEL_KEY = "offender";

	private static final String CORRECTIONAL_STATUSES_MODEL_KEY
			= "correctionalStatuses";
	
	private static final String STATES_MODEL_KEY = "states";

	private static final String SUPERVISORY_ORGANIZATIONS_MODEL_KEY
			= "supervisoryOrganizations";

	private static final String PLACEMENT_TERM_START_CHANGE_REASONS_MODEL_KEY
			= "startChangeReasons";

	private static final String PLACEMENT_TERM_END_CHANGE_REASONS_MODEL_KEY
			= "endChangeReasons";
	
	private static final String FROM_CORRECTIONAL_STATUS_MODEL_KEY 
			= "fromCorrectionalStatus";
	
	private static final String BOOLEAN_VALUE_MODEL_KEY = "booleanValue";
	
	private static final String PLACEMENT_TERM_MODEL_KEY = "placementTerm";

	private static final String EFFECTIVE_PLACEMENT_TERM_MODEL_KEY
			= "effectivePlacementTerm";

	private static final String PLACEMENT_STATUSES_MODEL_KEY
			= "placementStatuses";
	
	private static final String PLACEMENT_TERM_NOTE_INDEX_MODEL_KEY
			= "placementTermNoteIndex";

	private static final String PLACEMENT_TERM_NOTE_ITEM_MODEL_KEY
			= "placementTermNoteItem";
	
	/* Error bundles. */
	
	private static final String ERROR_MESSAGE_BUNDLE_NAME
			= "omis.supervision.msgs.form";

	/* Message keys. */
	
	private static final String CORRECTIONAL_STATUS_TERM_CONFLICT_MESSAGE_KEY
			= "placementTerm.correctionalStatusTermConflicts";

	private static final String
	SUPERVISORY_ORGANIZATION_TERM_CONFLICT_MESSAGE_KEY
			= "placementTerm.supervisoryOrganizationTermConflicts";

	private static final String PLACEMENT_TERM_CONFLICT_MESSAGE_KEY
			= "placementTerm.conflicts";

	private static final String PLACEMENT_TERM_LOCKED_MESSAGE_KEY
			= "placementTerm.locked";
	
	private static final String PLACEMENT_TERM_EXISTS_AFTER_MESSAGE_KEY
			= "placementTerm.existsAfter";
	
	private static final String PLACEMENT_TERM_EXISTS_BEFORE_MESSAGE_KEY
			= "placementTerm.existsBefore";
	
	private static final String OFFENDER_NOT_UNDER_SUPERVISION_MESSAGE_KEY
			= "offender.notUnderSupervision";
	
	private static final String PLACEMENT_TERM_EXISTS_MESSAGE_KEY
			= "placementTerm.exists";

	private static final String PLACEMENT_TERM_NOTE_EXISTS_MESSAGE_KEY
			= "placementTermNote.exists";

	
	/* Report names. */
	
	private static final String PLACEMENT_LISTING_REPORT_NAME 
		= "/Placement/PlacementTerms/Placement_Listing";
	
	private static final String PLACEMENT_BY_SUP_ORG_LISTING_REPORT_NAME 
	    = "/Placement/PlacementTerms/Placement_Term_by_Supervisory_Organization_Listing";	
	
	private static final String PLACEMENT_DETAILS_REPORT_NAME 
		= "/Placement/PlacementTerms/Placement_Term_Details";	

	/* Report parameter names. */
	
	private static final String PLACEMENT_LISTING_ID_REPORT_PARAM_NAME 
		= "DOC_ID";
	
	private static final String PLACEMENT_TERM_DETAILS_ID_REPORT_PARAM_NAME 
		= "PLACEMENT_TERM_ID";
	
	/* Services. */
	
	@Autowired
	@Qualifier("placementTermService")
	private PlacementTermService placementTermService;
	
	/* Report services. */
	
	@Autowired
	@Qualifier("placementTermReportService")
	private PlacementTermReportService placementTermReportService;
	
	/* Property editor factories. */
	
	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory offenderPropertyEditorFactory;
	
	@Autowired
	@Qualifier("placementTermPropertyEditorFactory")
	private PropertyEditorFactory placementTermPropertyEditorFactory;
	
	@Autowired
	@Qualifier("placementTermNotePropertyEditorFactory")
	private PropertyEditorFactory placementTermNotePropertyEditorFactory;
	
	@Autowired
	@Qualifier("placementTermChangeReasonPropertyEditorFactory")
	private PropertyEditorFactory
			placementTermChangeReasonPropertyEditorFactory;
	
	@Autowired
	@Qualifier("supervisoryOrganizationPropertyEditorFactory")
	private PropertyEditorFactory
	supervisoryOrganizationPropertyEditorFactory;
	
	@Autowired
	@Qualifier("correctionalStatusPropertyEditorFactory")
	private PropertyEditorFactory correctionalStatusPropertyEditorFactory;
	
	@Autowired
	@Qualifier("statePropertyEditorFactory")
	private PropertyEditorFactory statePropertyEditorFactory;
	
	@Autowired
	@Qualifier("datePropertyEditorFactory")
	private CustomDateEditorFactory customDateEditorFactory;
	
	/* Validators. */
	
	@Autowired
	@Qualifier("placementTermFormValidator")
	private PlacementTermFormValidator placementTermFormValidator;
	
	/* Helpers. */
	
	@Autowired
	@Qualifier("businessExceptionHandlerDelegate")
	private BusinessExceptionHandlerDelegate businessExceptionHandlerDelegate;

	@Autowired
	@Qualifier("offenderSummaryModelDelegate")
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;

	/* Report runners. */
	
	@Autowired
	@Qualifier("reportRunner")
	private ReportRunner reportRunner;
	
	/* Controller delegates. */
	
	@Autowired
	@Qualifier("reportControllerDelegate")
	private ReportControllerDelegate reportControllerDelegate;
	/* Constructor. */
	
	/** Instantiates a default controller for placement terms. */
	public PlacementTermController() {
		// Default instantiation
	}
	
	/* Screens. */
	
	/**
	 * Displays a screen listing placement terms by offender.
	 * 
	 * @param offender offender
	 * @return screen listing placement terms by offender
	 */
	@RequestMapping(value = "/list.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ADMIN') or hasRole('PLACEMENT_TERM_LIST')")
	public ModelAndView list(
			@RequestParam(value = "offender", required = true)
				final Offender offender,
			@RequestParam(value = "effectiveDate", required = false)
				final Date effectiveDate,
			@RequestParam(value = "effectiveTime", required = false)
				final String effectiveTime) {
		
		// Compiles effective date/time from parameters or current date
		Date effectiveDateTime;
		if (effectiveDate != null) {
			effectiveDateTime = DateManipulator.getDateAtTimeOfDay(
						effectiveDate, this.parseTimeText(effectiveTime));
		} else {
			effectiveDateTime = new Date();
		}
		
		// Effective placement term is on effective date - add to model and view
		PlacementTerm effectivePlacementTerm = this.placementTermService
				.findPlacementTerm(offender, effectiveDateTime);
		List<PlacementTermSummary> placementTermSummaries
				= this.placementTermReportService.summarizeByOffender(
						offender, effectiveDateTime);
		ModelAndView mav = new ModelAndView(LIST_VIEW_NAME);
		mav.addObject(PLACEMENT_TERM_SUMMARIES_MODEL_KEY,
				placementTermSummaries);
		Date currentDate = new Date();
		addOffenderSummary(mav, offender, currentDate);
		mav.addObject(CURRENT_DATE_MODEL_KEY, currentDate);
		mav.addObject(OFFENDER_MODEL_KEY, offender);
		if (effectivePlacementTerm != null) {
			mav.addObject(EFFECTIVE_PLACEMENT_TERM_MODEL_KEY,
							effectivePlacementTerm);
		}
		return mav;
	}
	
	/**
	 * Displays a screen for the creation of a new placement term.
	 * 
	 * @param offender offender for whom to create placement term
	 * @param allowCorrectionalStatusChange whether to allow correctional
	 * status change
	 * @return model and view to screen for creation of new placement term
	 */
	@RequestMapping(value = "/create.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ADMIN') or hasRole('PLACEMENT_TERM_CREATE')")
	public ModelAndView create(
			@RequestParam(value = "offender", required = true)
				final Offender offender,
			@RequestParam(
					value = "allowCorrectionalStatusChange",
					required = false)
				final Boolean allowCorrectionalStatusChange) {
		Date effectiveDate = new Date();
		PlacementTermForm placementTermForm = new PlacementTermForm();
		placementTermForm.setAllowStatusFields(false);
		placementTermForm.setAllowCorrectionalStatus(true);
		placementTermForm.setAllowSupervisoryOrganization(true);
		placementTermForm.setAllowState(true);
		placementTermForm.setAllowStartDate(true);
		placementTermForm.setAllowStartTime(true);
		placementTermForm.setStartDate(effectiveDate);
		placementTermForm.setStartTime(effectiveDate);
		if (allowCorrectionalStatusChange != null
				&& allowCorrectionalStatusChange) {
			placementTermForm.setAllowCorrectionalStatus(true);
		} else {
			
			// Use correctional status of placement term on effective date
			// if changing correctional status is not allowed when creating
			// This is to allow supervisory organizations and reasons for the
			// current correctional status rather than null
			// There is probably a better way to do this
			PlacementTerm placementTerm = this.placementTermService
					.findPlacementTerm(offender, effectiveDate);
			if (placementTerm != null) {
				placementTermForm.setCorrectionalStatus(
						placementTerm.getCorrectionalStatusTerm()
								.getCorrectionalStatus());
			}
			placementTermForm.setAllowCorrectionalStatus(false);
		}
		placementTermForm.setState(this.placementTermService
				.findHomeState());
		placementTermForm.setAllowSendToLocation(true);
		placementTermForm.setSendToLocation(true);
		return this.prepareEditMav(placementTermForm, offender,
				effectiveDate);
	}
	
	/**
	 * Displays a screen for the updating of an existing placement term.
	 * 
	 * @param placementTerm placement term to update
	 * @return model and view to screen for updating of existing placement
	 * term
	 */
	@RequestMapping(value = "/edit.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ADMIN') or hasRole('PLACEMENT_TERM_VIEW')")
	public ModelAndView edit(
			@RequestParam(value = "placementTerm", required = true)
				final PlacementTerm placementTerm) {
		PlacementTermForm placementTermForm = new PlacementTermForm();
		placementTermForm.setAllowStatusFields(true);
		placementTermForm.setAllowCorrectionalStatus(false);
		placementTermForm.setAllowSupervisoryOrganization(false);
		placementTermForm.setAllowState(false);
		placementTermForm.setAllowStartDate(false);
		placementTermForm.setAllowStartTime(false);
		placementTermForm.setCorrectionalStatus(
				placementTerm.getCorrectionalStatusTerm()
					.getCorrectionalStatus());
		placementTermForm.setSupervisoryOrganization(
				placementTerm.getSupervisoryOrganizationTerm()
					.getSupervisoryOrganization());
		if (placementTerm.getDateRange() != null) {
			placementTermForm.setStartDate(
					placementTerm.getDateRange().getStartDate());
			placementTermForm.setStartTime(
					placementTerm.getDateRange().getStartDate());
			placementTermForm.setEndDate(
					placementTerm.getDateRange().getEndDate());
			placementTermForm.setEndTime(
					placementTerm.getDateRange().getEndDate());
		}
		placementTermForm.setStartChangeReason(
				placementTerm.getStartChangeReason());
		placementTermForm.setEndChangeReason(
				placementTerm.getEndChangeReason());
		PlacementStatus status;
		Boolean returned;
		if (PlacementStatus.RETURNED_FROM_ESCAPE
				.equals(placementTerm.getStatus())) {
			status = PlacementStatus.ESCAPED;
			returned = true;
		} else if (PlacementStatus.RETURNED_FROM_ABSCOND
				.equals(placementTerm.getStatus())) {
			status = PlacementStatus.ABSCONDED;
			returned = true;
		} else {
			status = placementTerm.getStatus();
			returned = false;
		}
		placementTermForm.setStatus(status);
		placementTermForm.setStatusDate(DateRange.getStartDate(
				placementTerm.getStatusDateRange()));
		placementTermForm.setStatusTime(DateRange.getStartDate(
				placementTerm.getStatusDateRange()));
		placementTermForm.setReturned(returned);
		placementTermForm.setStatusReturnedDate(DateRange.getEndDate(
				placementTerm.getStatusDateRange()));
		placementTermForm.setStatusReturnedTime(DateRange.getEndDate(
				placementTerm.getStatusDateRange()));
		placementTermForm.setAllowSendToLocation(false);
		
		// Sets State to first at which State supervisory organization has
		// location - this might not be the State used to originally set the
		// supervisory organization. States must be ordered in order for the
		// State selected to not vary between requests - SA
		List<State> states = this.placementTermService
				.findStatesForSupervisoryOrganization(
						placementTerm.getSupervisoryOrganizationTerm()
							.getSupervisoryOrganization());
		if (states.size() > 0) {
			placementTermForm.setState(states.get(0));
		}
		
		// Adds note items
		List<PlacementTermNote> notes = this.placementTermService
				.findNotesByPlacementTerm(placementTerm);
		List<PlacementTermNoteItem> noteItems
			= new ArrayList<PlacementTermNoteItem>();
		for (PlacementTermNote note : notes) {
			PlacementTermNoteItem noteItem = new PlacementTermNoteItem();
			noteItem.setNote(note);
			noteItem.setOperation(PlacementTermNoteItemOperation.UPDATE);
			PlacementTermNoteFields fields = new PlacementTermNoteFields();
			fields.setDate(note.getDate());
			fields.setValue(note.getValue());
			noteItem.setFields(fields);
			noteItems.add(noteItem);
		}
		placementTermForm.setNoteItems(noteItems);
		
		// Prepares and returns model and view
		ModelAndView mav = this.prepareEditMav(
				placementTermForm, placementTerm.getOffender(), new Date());
		mav.addObject(PLACEMENT_TERM_MODEL_KEY, placementTerm);
		return mav;
	}
	
	// Prepares edit form model and view
	private ModelAndView prepareEditMav(
			final PlacementTermForm placementTermForm,
			final Offender offender, final Date effectiveDate) {
		ModelAndView mav = new ModelAndView(EDIT_FORM_VIEW_NAME);
		mav.addObject(PLACEMENT_TERM_FORM_MODEL_KEY, placementTermForm);
		mav.addObject(OFFENDER_MODEL_KEY, offender);
		
		// Adds allowed statuses
		PlacementStatus[] placementStatuses;
		if (placementTermForm.getCorrectionalStatus() != null) {
			placementStatuses = this.findAllowedStatuses(
					placementTermForm.getCorrectionalStatus()
						.getLocationRequired());
		} else {
			placementStatuses = new PlacementStatus[] { };
		}
		mav.addObject(PLACEMENT_STATUSES_MODEL_KEY, placementStatuses);
		
		// Adds home States
		List<State> states = this.placementTermService.findHomeStates();
		mav.addObject(STATES_MODEL_KEY, states);
		
		// Effective placement term is on effective date
		PlacementTerm effectivePlacementTerm = this.placementTermService
				.findPlacementTerm(offender, effectiveDate);
		
		// Adds correctional statuses that can be changed to based off
		// correctional status on effective date
		// If there is no placement term on effective date, use null (initial
		// placement)
		List<CorrectionalStatus> correctionalStatuses;
		if (effectivePlacementTerm != null) {
				correctionalStatuses = this.placementTermService
					.findAllowedCorrectionalStatuses(effectivePlacementTerm
							.getCorrectionalStatusTerm()
							.getCorrectionalStatus());	
			
				// Not sure if this is required - SA
				mav.addObject(FROM_CORRECTIONAL_STATUS_MODEL_KEY,
						effectivePlacementTerm
							.getCorrectionalStatusTerm()
								.getCorrectionalStatus());
		} else {
				correctionalStatuses = this.placementTermService
						.findAllowedCorrectionalStatuses(null);
		}
		mav.addObject(CORRECTIONAL_STATUSES_MODEL_KEY,
				correctionalStatuses);
		
		// Adds supervisory organizations
		List<SupervisoryOrganization> supervisoryOrganizations;
		if ((placementTermForm.getAllowCorrectionalStatus() && 
				placementTermForm.getCorrectionalStatus() != null)
				|| (!placementTermForm.getAllowCorrectionalStatus() 
					&& placementTermForm.getCorrectionalStatus() != null)) {
			if (placementTermForm.getState() != null) {
				supervisoryOrganizations = this.placementTermService
					.findSupervisoryOrganizationsForCorrectionalStatusByState(
								placementTermForm.getCorrectionalStatus(),
								placementTermForm.getState());
			} else {
				supervisoryOrganizations = this.placementTermService
						.findSupervisoryOrganizationsForCorrectionalStatus(
								placementTermForm.getCorrectionalStatus());
			}
		} else {
			
			// Doing this does not allow releases from placement term screen
			// This may or may not be what is desired - SA
			supervisoryOrganizations = Collections.emptyList();
		}
		mav.addObject(SUPERVISORY_ORGANIZATIONS_MODEL_KEY,
				supervisoryOrganizations);
		
		// Adds start change reasons
		CorrectionalStatus fromCorrectionalStatus;
		if (effectivePlacementTerm != null) {
			PlacementTerm previousPlacementTerm = this.placementTermService
					.findPlacementTermForOffenderWithEndDate(offender,
							DateRange.getStartDate(effectivePlacementTerm
									.getDateRange()));
			if (previousPlacementTerm != null) {
				fromCorrectionalStatus = previousPlacementTerm
						.getCorrectionalStatusTerm()
							.getCorrectionalStatus();
			} else {
				fromCorrectionalStatus = null;
			}
		} else {
			fromCorrectionalStatus = null;
		}
		List<PlacementTermChangeReason> startChangeReasons
			= this.placementTermService.findAllowedStartChangeReasons(
					fromCorrectionalStatus,
					placementTermForm.getCorrectionalStatus());

		mav.addObject(PLACEMENT_TERM_START_CHANGE_REASONS_MODEL_KEY,
				startChangeReasons);
		
		// Adds end change reasons based on correctional status set on form
		List<PlacementTermChangeReason> endChangeReasons
			= this.placementTermService
					.findAllowedEndChangeReasons(
							placementTermForm.getCorrectionalStatus());
		mav.addObject(PLACEMENT_TERM_END_CHANGE_REASONS_MODEL_KEY,
				endChangeReasons);
		
		// Determines note item index for size of notes
		int noteItemIndex;
		if (placementTermForm.getNoteItems() != null) {
			noteItemIndex = placementTermForm.getNoteItems().size();
		} else {
			noteItemIndex = 0;
		}
		mav.addObject(PLACEMENT_TERM_NOTE_INDEX_MODEL_KEY, noteItemIndex);
		
		// Adds offender summary, returns model and view
		this.addOffenderSummary(mav, offender, effectiveDate);
		return mav;
	}
	
	// Adds offender summary to a model and view
	private void addOffenderSummary(
			final ModelAndView mav, final Offender offender, final Date date) {
		this.offenderSummaryModelDelegate.add(mav.getModelMap(), offender);
	}
	
	/**
	 * Creates a new placement term.
	 * 
	 * @param offender offender for whom to create placement term
	 * @param allowCorrectionalStatusChange whether to allow correctional
	 * status change
	 * @param placementTermForm placement term form
	 * @param result binding result
	 * @param session HTTP session
	 * @return model and view to redirect to placement term list screen
	 * @throws DuplicateEntityFoundException if the placement term already
	 * exists
	 * @throws CorrectionalStatusTermConflictException if conflicting
	 * correctional status terms exist
	 * @throws SupervisoryOrganizationTermConflictException if conflicting
	 * supervisory organization terms exist
	 * @throws PlacementTermConflictException if placement term
	 * conflicts with existing placement term
	 * @throws OffenderNotUnderSupervisionException if offender is not under 
	 * supervision
	 * @throws PlacementTermExistsBeforeException if start date is null and 
	 * terms exist before end date
	 * @throws PlacementTermExistsAfterException if end date is null and terms 
	 * exist after start date 
	 * @throws PlacementTermNoteExistsException if placement term note exists
	 */
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@PreAuthorize("hasRole('ADMIN') or hasRole('PLACEMENT_TERM_CREATE')")
	public ModelAndView save(
			@RequestParam(value = "offender", required = true)
				final Offender offender,
			@RequestParam(
					value = "allowCorrectionalStatusChange",
					required = false)
				final Boolean allowCorrectionalStatusChange,
			final PlacementTermForm placementTermForm,
			final BindingResult result,
			final HttpSession session) throws PlacementTermExistsException,
				CorrectionalStatusTermConflictException,
				SupervisoryOrganizationTermConflictException,
				PlacementTermConflictException, 
				OffenderNotUnderSupervisionException, 
				PlacementTermExistsAfterException, 
				PlacementTermExistsBeforeException,
				PlacementTermNoteExistsException {
		this.placementTermFormValidator.validate(placementTermForm, result);
		if (result.hasErrors()) {
			return this.prepareRedisplayMav(
					offender, placementTermForm, result);
		}
		DateRange dateRange = this.createDateRange(
				placementTermForm.getStartDate(),
				placementTermForm.getStartTime(),
				placementTermForm.getEndDate(),
				placementTermForm.getEndTime());
		PlacementTerm placementTerm;
		if (allowCorrectionalStatusChange != null
				&& allowCorrectionalStatusChange) {
			
			// TODO Remove status and statusDate parameters - SA
			placementTerm = this.placementTermService
				.create(offender,
						placementTermForm.getSupervisoryOrganization(),
						placementTermForm.getCorrectionalStatus(), dateRange,
						placementTermForm.getStartChangeReason(),
						placementTermForm.getEndChangeReason());
		} else {
			placementTerm = this.placementTermService
					.changeSupervisoryOrganization(offender,
							placementTermForm.getSupervisoryOrganization(),
							dateRange, placementTermForm.getStartChangeReason(),
							placementTermForm.getEndChangeReason());
		}
		
		// Saves placement term notes
		if (placementTermForm.getNoteItems() != null) {
			for (PlacementTermNoteItem noteItem
					: placementTermForm.getNoteItems()) {
				if (noteItem.getOperation() != null) {
					if (PlacementTermNoteItemOperation.CREATE.equals(
							noteItem.getOperation())) {
						this.placementTermService.createNote(placementTerm,
								noteItem.getFields().getDate(),
								noteItem.getFields().getValue());
					} else {
						throw new UnsupportedOperationException(
								String.format("Unsupported operation: %s",
										noteItem.getOperation()));
					}
				}
			}
		}
		
		// If sending to location is allowed and indicated, redirect to screen
		// to create new location term
		// On creation of new location term, existing location term, if
		// applicable will be ended
		if (placementTermForm.getAllowSendToLocation()
				&& placementTermForm.getSendToLocation() != null
				&& placementTermForm.getSendToLocation()
				&& placementTermForm.getCorrectionalStatus()
					.getLocationRequired()) {
			
			// Add redirect to return to listing screen for placement terms
			RedirectUrlRetriever redirectUrlRetriever
				= RedirectUrlRetriever.createInstance(session);
			redirectUrlRetriever.setOrReplace(String.format(
					LIST_BY_OFFENDER_URL, offender.getId()));
			
			// Returns redirect to send to location
			return this.prepareCreateLocationTermRedirect(offender,
					placementTermForm.getSupervisoryOrganization(),
					dateRange.getStartDate(), dateRange.getEndDate());
			
		// If location is required for existing but not new placement term,
		// find location term on start date of new placement and prompt to edit
		// if existing, otherwise redirect to placement term listing
		} else {
			PlacementTerm existingPlacementTerm = this.placementTermService
					.findPlacementTermForOffenderWithEndDate(
							offender, dateRange.getStartDate());
			if (existingPlacementTerm != null
					&& existingPlacementTerm.getCorrectionalStatusTerm()
						.getCorrectionalStatus().getLocationRequired()) {
				LocationTerm locationTerm = this.placementTermService
						.findLocationTermByOffenderOnDate(offender,
								dateRange.getStartDate());
				if (locationTerm != null) {
					return this.prepareEditLocationTermRedirect(
							locationTerm, dateRange.getStartDate());
				} else {
					return this.prepareListRedirect(
							placementTerm.getOffender());
				}
			} else {
				return this.prepareListRedirect(placementTerm.getOffender());
			}
		}
	}

	/**
	 * Updates an existing placement term.
	 * 
	 * @param placementTerm placement term to update
	 * @param placementTermForm placement term form
	 * @param result binding result
	 * @return model and view to redirect to placement term list screen
	 * @throws DuplicateEntityFoundException if the placement term already
	 * exists
	 * @throws CorrectionalStatusTermConflictException if conflicting
	 * correctional status terms exist
	 * @throws SupervisoryOrganizationTermConflictException if conflicting
	 * supervisory organization terms exist
	 * @throws PlacementTermLockedException if placement term is locked
	 */
	@RequestMapping(value = "/edit.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('ADMIN') or hasRole('PLACEMENT_TERM_EDIT')")
	public ModelAndView update(
			@RequestParam(value = "placementTerm", required = true)
				final PlacementTerm placementTerm,
			final PlacementTermForm placementTermForm,
			final BindingResult result)
					throws DuplicateEntityFoundException,
						CorrectionalStatusTermConflictException,
						SupervisoryOrganizationTermConflictException,
						PlacementTermLockedException {
		this.placementTermFormValidator.validate(placementTermForm, result);
		if (result.hasErrors()) {
			ModelAndView mav = prepareRedisplayMav(
					placementTerm.getOffender(), placementTermForm, result);
			mav.addObject(PLACEMENT_TERM_MODEL_KEY, placementTerm);
			return mav;
		}
		PlacementStatus status;
		DateRange statusDateRange;
		if (PlacementStatus.ESCAPED.equals(placementTermForm.getStatus())) {
			if (placementTermForm.getReturned() != null
					&& placementTermForm.getReturned()) {
				status = PlacementStatus.RETURNED_FROM_ESCAPE;
			} else {
				status = PlacementStatus.ESCAPED;
			}
			statusDateRange = this.createDateRange(
					placementTermForm.getStatusDate(), 
					placementTermForm.getStatusTime(), 
					placementTermForm.getStatusReturnedDate(), 
					placementTermForm.getStatusReturnedTime()); 
		} else if (PlacementStatus.ABSCONDED
				.equals(placementTermForm.getStatus())) {
			if (placementTermForm.getReturned() != null
					&& placementTermForm.getReturned()) {
				status = PlacementStatus.RETURNED_FROM_ABSCOND;
			} else {
				status = PlacementStatus.ABSCONDED;
			}
			statusDateRange = this.createDateRange(
					placementTermForm.getStatusDate(), 
					placementTermForm.getStatusTime(), 
					placementTermForm.getStatusReturnedDate(), 
					placementTermForm.getStatusReturnedTime()); 
		} else {
			status = placementTermForm.getStatus();
			statusDateRange = null;
		}
		this.placementTermService.update(placementTerm,
						this.calculateDateTime(
								placementTermForm.getEndDate(),
								placementTermForm.getEndTime()),
						status, statusDateRange,
						placementTermForm.getStartChangeReason(),
						placementTermForm.getEndChangeReason());
		
		// Creates, updates, removes placement term notes
		if (placementTermForm.getNoteItems() != null) {
			for (PlacementTermNoteItem noteItem : placementTermForm.getNoteItems()) {
				if (noteItem.getOperation() != null) {
					if (PlacementTermNoteItemOperation.CREATE.equals(
							noteItem.getOperation())) {
						this.placementTermService.createNote(placementTerm,
								noteItem.getFields().getDate(),
								noteItem.getFields().getValue());
					} else if (PlacementTermNoteItemOperation.UPDATE.equals(
								noteItem.getOperation())) {
						this.placementTermService.updateNote(
								noteItem.getNote(),
								noteItem.getFields().getDate(),
								noteItem.getFields().getValue());
					} else if (PlacementTermNoteItemOperation.REMOVE.equals(
								noteItem.getOperation())) {
						this.placementTermService.removeNote(noteItem.getNote());
					} else {
						throw new UnsupportedOperationException(
								String.format("Unsupported operation: %s",
										noteItem.getOperation()));
					}
				}
			}
		}
		return prepareListRedirect(placementTerm.getOffender());
	}
	
	// Returns a model and view to redisplay edit screen
	private ModelAndView prepareRedisplayMav(
			final Offender offender,
			final PlacementTermForm placementTermForm,
			final BindingResult result) {
		ModelAndView mav = this.prepareEditMav(
				placementTermForm, offender, new Date());
		mav.addObject(
			BindingResult.MODEL_KEY_PREFIX + PLACEMENT_TERM_FORM_MODEL_KEY,
			result);
		return mav;
	}
	
	// Returns a model and view to redirect to placement terms screen
	private ModelAndView prepareListRedirect(final Offender offender) {
		return new ModelAndView(String.format(LIST_BY_OFFENDER_REDIRECT,
				offender.getId()));
	}
	
	// Returns a model and view to redirect to create location term
	private ModelAndView prepareCreateLocationTermRedirect(
			final Offender offender,
			final Organization organization,
			final Date startDate,
			final Date endDate) {
		if (endDate != null) {
			return new ModelAndView(String.format(
				CREATE_LOCATION_TERM_REDIRECT,
				offender.getId(), organization.getId(),
				this.formatDate(startDate), this.formatTime(startDate),
				this.formatDate(endDate), this.formatTime(endDate)));
		} else {
			return new ModelAndView(String.format(
				CREATE_LOCATION_TERM_WITHOUT_END_DATE_REDIRECT,
				offender.getId(), organization.getId(),
				this.formatDate(startDate), this.formatTime(startDate)));			
		}
	}
	
	// Returns a model and view to redirect to edit location term
	// Allows optional default end date/time
	private ModelAndView prepareEditLocationTermRedirect(
			final LocationTerm locationTerm, final Date date) {
		if (date != null) {
			return new ModelAndView(String.format(
					EDIT_LOCATION_TERM_WITH_DEFAULT_END_DATE_REDIRECT,
						locationTerm.getId(),
					this.formatDate(date), this.formatTime(date)));
		} else {
			return new ModelAndView(String.format(
					EDIT_LOCATION_TERM_REDIRECT, locationTerm.getId()));
		}
	}
	
	// Formats date
	private String formatDate(final Date date) {
		if (date != null) {
			PropertyEditor propertyEditor = this.customDateEditorFactory
					.createCustomDateOnlyEditor(true);
			propertyEditor.setValue(date);
			return propertyEditor.getAsText().replace("/", "%2F");
		} else {
			return "";
		}
	}
	
	// Formats time
	private String formatTime(final Date time) {
		if (time != null) {
			PropertyEditor propertyEditor = this.customDateEditorFactory
					.createCustomTimeOnlyEditor(true);
			propertyEditor.setValue(time);
			return propertyEditor.getAsText()
					.replace(" ", "%20").replace(":", "%3A");
		} else {
			return "";
		}
	} 
	
	/**
	 * Removes a placement term.
	 * 
	 * @param placementTerm placement term to remove
	 * @return redirect to display placement terms for offender
	 */
	@RequestMapping(value = "/remove.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ADMIN') or hasRole('PLACEMENT_TERM_REMOVE')")
	public ModelAndView remove(
			@RequestParam(value = "placementTerm", required = true)
				final PlacementTerm placementTerm) {
		Offender offender = placementTerm.getOffender();
		this.placementTermService.remove(placementTerm);
		return this.prepareListRedirect(offender);
	}
	
	/* AJAX invokable requests. */
	
	/**
	 * Returns a list of supervisory organizations for correctional status
	 * options.
	 * 
	 * @param correctionalStatus correctional status
	 * @param state State
	 * @return model and view of list of supervisory organizations for
	 * correctional status options
	 */
	@RequestMapping(
			value = "findSupervisoryOrganizationsForCorrectionalStatus.html",
			method = RequestMethod.GET)
	public ModelAndView findSupervisoryOrganizationsForCorrectionalStatus(
			@RequestParam(value = "correctionalStatus", required = true)
				final CorrectionalStatus correctionalStatus,
			@RequestParam(value = "state", required = false)
				final State state) {
		List<SupervisoryOrganization> supervisoryOrganizations;
		if (state != null) {
			supervisoryOrganizations = this.placementTermService
					.findSupervisoryOrganizationsForCorrectionalStatusByState(
							correctionalStatus, state);
		} else {
			supervisoryOrganizations = this.placementTermService
					.findSupervisoryOrganizationsForCorrectionalStatus(
							correctionalStatus);
		}
		ModelAndView mav = new ModelAndView(
				SUPERVISORY_ORGANIZATION_OPTIONS_VIEW_NAME);
		mav.addObject(SUPERVISORY_ORGANIZATIONS_MODEL_KEY,
				supervisoryOrganizations);
		return mav;
	}
	
	/**
	 * Returns a list of placement term change reasons for correctional status 
	 * options.
	 * 
	 * @param fromCorrectionalStatus current correctional status
	 * @param toCorrectionalStatus correctional status transitioning to
	 * @return model and view of list of placement term change reasons for 
	 * correctional status options
	 */
	@RequestMapping(
			value = "findStartChangeReasonsForCorrectionalStatuses.html", 
			method = RequestMethod.GET)
	public ModelAndView findStartChangeReasonsForCorrectionalStatuses(
			@RequestParam(value = "fromCorrectionalStatus", required = false)
			final CorrectionalStatus fromCorrectionalStatus,
			@RequestParam(value = "toCorrectionalStatus", required = true)
			final CorrectionalStatus toCorrectionalStatus) {
		List<PlacementTermChangeReason> reasons = this.placementTermService
				.findAllowedStartChangeReasons(fromCorrectionalStatus, 
						toCorrectionalStatus);
		ModelAndView mav = new ModelAndView(
				PLACEMENT_TERM_START_CHANGE_REASON_OPTIONS_VIEW_NAME);
		mav.addObject(PLACEMENT_TERM_START_CHANGE_REASONS_MODEL_KEY, reasons);
		return mav;
	}
	
	/**
	 * Returns a list of placement term change reasons for correctional status 
	 * options.
	 * 
	 * @param fromCorrectionalStatus correctional status
	 * @return model and view of list of placement term change reasons for 
	 * correctional status options
	 */
	@RequestMapping(value = "findEndChangeReasonsForCorrectionalStatus.html", 
			method = RequestMethod.GET)
	public ModelAndView findEndChangeReasonsForCorrectionalStatus(
			@RequestParam(value = "fromCorrectionalStatus", required = true)
			final CorrectionalStatus fromCorrectionalStatus) {
		List<PlacementTermChangeReason> reasons;
		if (fromCorrectionalStatus != null) {
			reasons = this.placementTermService
				.findAllowedEndChangeReasons(fromCorrectionalStatus);
		} else {
			reasons = Collections.emptyList();
		}
		ModelAndView mav = new ModelAndView(
				PLACEMENT_TERM_END_CHANGE_REASON_OPTIONS_VIEW_NAME);
		mav.addObject(PLACEMENT_TERM_END_CHANGE_REASONS_MODEL_KEY, reasons);
		return mav;
	}
	
	/**
	 * Returns placement status options allowed for correctional status.
	 * 
	 * @param correctionalStatus correctional status
	 * @return placement status options allowed for correctional status
	 */
	@RequestMapping(
			value = "findPlacementStatusesForCorrectionalStatus.html",
			method = RequestMethod.GET)
	public ModelAndView findPlacementStatusesForCorrectionalStatus(
			@RequestParam(value = "correctionalStatus", required = false)
				final CorrectionalStatus correctionalStatus) {
		PlacementStatus[] statuses;
		if (correctionalStatus != null) {
			statuses = this.findAllowedStatuses(
					correctionalStatus.getLocationRequired());
		} else {
			statuses = new PlacementStatus[] { };
		}
		ModelAndView mav = new ModelAndView(PLACEMENT_STATUS_OPTIONS_VIEW_NAME);
		mav.addObject(PLACEMENT_STATUSES_MODEL_KEY, statuses);
		return mav;
	}
	
	/**
	 * Returns whether location is required for correctional status.
	 * 
	 * @param correctionalStatus correctional status
	 * @return whether location is required for correctional status
	 */
	@RequestMapping(
			value = "isLocationRequiredForCorrectionalStatus.json")
	public ModelAndView isLocationRequiredForCorrectionalStatus(
			@RequestParam(value = "correctionalStatus", required = false)
				final CorrectionalStatus correctionalStatus) {
		ModelAndView mav = new ModelAndView(BOOLEAN_VALUE_VIEW_NAME);
		mav.addObject(BOOLEAN_VALUE_MODEL_KEY,
				correctionalStatus.getLocationRequired());
		return mav;
	}
	
	/**
	 * Returns view to create placement term note.
	 * 
	 * @param noteItemIndex item index of note
	 * @return view to create placement term note
	 */
	@RequestMapping(
			value = "createPlacementTermNote.html", method = RequestMethod.GET)
	public ModelAndView createPlacementTermNote(
			@RequestParam(value = "placementTermNoteIndex", required = true)
				final Integer placementTermNoteIndex) {
		PlacementTermNoteItem placementTermNoteItem
			= new PlacementTermNoteItem();
		placementTermNoteItem.setOperation(
				PlacementTermNoteItemOperation.CREATE);
		ModelAndView mav = new ModelAndView(
				PLACEMENT_TERM_NOTE_TABLE_ROW_VIEW_NAME);
		mav.addObject(PLACEMENT_TERM_NOTE_INDEX_MODEL_KEY,
				placementTermNoteIndex);
		mav.addObject(PLACEMENT_TERM_NOTE_ITEM_MODEL_KEY,
				placementTermNoteItem);
		return mav;
	}
	
	/* Helper methods. */
	
	// Creates and returns date range - never returns null
	private DateRange createDateRange(final Date startDate,
			final Date startTime, final Date endDate, final Date endTime) {
		return new DateRange(
				this.calculateDateTime(startDate, startTime),
				this.calculateDateTime(endDate, endTime));
	}
	
	// Adds time to date and returns date - if date is null return null (ignore
	// time) if time is null assume midnight
	private Date calculateDateTime(final Date date, final Date time) {
		if (date == null) {
			return null;
		}
		if (time != null) {
			return DateManipulator.getDateAtTimeOfDay(date, time);
		} else {
			return DateManipulator.getDateAtTimeOfDay(date, new Date(0L));
		}
	}
	
	// Parses time
	private Date parseTimeText(final String timeText) {
		if (timeText != null && !timeText.isEmpty()) {
			PropertyEditor propertyEditor
				= this.customDateEditorFactory.createCustomTimeOnlyEditor(true);
			propertyEditor.setAsText(timeText);
			return (Date) propertyEditor.getValue();
		} else {
			return null;
		}
	}
	
	// Returns allowed statuses
	private PlacementStatus[] findAllowedStatuses(
			final boolean locationRequired) {
		if (locationRequired) {
			return new PlacementStatus[] {
				PlacementStatus.PLACED, PlacementStatus.ESCAPED
			};
		} else {
			return new PlacementStatus[] {
				PlacementStatus.PLACED, PlacementStatus.ABSCONDED
			};
		}
	}
	

	/* Reports. */
	
	/**
	 * Returns the report for the specified offenders placements.
	 * 
	 * @param offender offender
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/placementListingReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('PLACEMENT_TERM_VIEW') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportPlacementListing(@RequestParam(
			value = "offender", required = true)
			final Offender offender,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(PLACEMENT_LISTING_ID_REPORT_PARAM_NAME,
				Long.toString(offender.getOffenderNumber()));
		byte[] doc = this.reportRunner.runReport(
				PLACEMENT_LISTING_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/**
	 * Returns the report for the specified offenders placements by sup org.
	 * 
	 * @param offender offender
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/placementSupOrgListingReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('PLACEMENT_TERM_VIEW') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportPlacementSupOrgListing(@RequestParam(
			value = "offender", required = true)
			final Offender offender,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(PLACEMENT_LISTING_ID_REPORT_PARAM_NAME,
				Long.toString(offender.getOffenderNumber()));
		byte[] doc = this.reportRunner.runReport(
				PLACEMENT_BY_SUP_ORG_LISTING_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}	
	
	/**
	 * Returns the report for the specified placement term.
	 * 
	 * @param placementTerm placement term
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/placementTermDetailReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('PLACEMENT_TERM_VIEW') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportPlacementTermDetails(@RequestParam(
			value = "placementTerm", required = true)
			final PlacementTerm placementTerm,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(PLACEMENT_TERM_DETAILS_ID_REPORT_PARAM_NAME,
				Long.toString(placementTerm.getId()));
		byte[] doc = this.reportRunner.runReport(
				PLACEMENT_DETAILS_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/* Action menus. */
	
	/**
	 * Shows action menu for placement term.
	 * 
	 * @param offender offender
	 * @return action menu for placement term
	 */
	@RequestMapping(
			value = "/placementTermActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView showPlacementTermActionMenu(
			@RequestParam(value = "offender", required = true)
				final Offender offender) {
		ModelAndView mav = new ModelAndView(
				PLACEMENT_TERM_ACTION_MENU_VIEW_NAME);
		mav.addObject(OFFENDER_MODEL_KEY, offender);
		return mav;
	}
	
	/**
	 * Shows action menu for placement terms.
	 * 
	 * @param offender offender
	 * @param effectivePlacementTerm effective placement term
	 * @return action menu for placement terms
	 */
	@RequestMapping(
			value = "/placementTermsActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView showPlacementTermsActionMenu(
			@RequestParam(value = "offender", required = true)
				final Offender offender,
			@RequestParam(value = "effectivePlacementTerm", required = false)
				final PlacementTerm effectivePlacementTerm) {
		ModelAndView mav = new ModelAndView(
				PLACEMENT_TERMS_ACTION_MENU_VIEW_NAME);
		mav.addObject(OFFENDER_MODEL_KEY, offender);
		mav.addObject(
				EFFECTIVE_PLACEMENT_TERM_MODEL_KEY, effectivePlacementTerm);
		return mav;
	}
	
	/**
	 * Shows action menu for placement term table row.
	 * 
	 * @param placementTerm placement term
	 * @return action menu for placement term table row
	 */
	@RequestMapping(
			value = "/placementTermTableRowActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView showPlacementTermTableRowActionMenu(
			@RequestParam(value = "placementTerm", required = false)
				final PlacementTerm placementTerm) {
		ModelAndView mav = new ModelAndView(
				PLACEMENT_TERM_TABLE_ROW_ACTION_MENU_VIEW_NAME);
		mav.addObject(PLACEMENT_TERM_MODEL_KEY, placementTerm);
		return mav;
	}
	
	/**
	 * Shows action menu for notes.
	 * 
	 * @return action menu for notes
	 */
	@RequestMapping(
			value = "/placementTermNoteItemsActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView showPlacementTermNotesActionMenu() {
		ModelAndView mav = new ModelAndView(
				PLACEMENT_TERM_NOTES_ACTION_MENU_VIEW_NAME);
		return mav;
	}
	
	/* Exception handlers. */
	
	/**
	 * Handles {@code PlacementTermExistsException}.
	 * 
	 * @param placementTermExistsException exception to handle
	 * @return model and view to handle {@code PlacementTermExistsException}.
	 */
	@ExceptionHandler(PlacementTermExistsException.class)
	public ModelAndView handlePlacementTermExistsException(
			final PlacementTermExistsException
				placementTermExistsException) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				PLACEMENT_TERM_EXISTS_MESSAGE_KEY,
				ERROR_MESSAGE_BUNDLE_NAME,
				placementTermExistsException);
	}
	
	/**
	 * Handles {@code PlacementTermNoteExistsException}.
	 * 
	 * @param placementTermNoteExistsException exception to handle
	 * @return model and view to handle {@code PlacementTermNoteExistsException}
	 */
	@ExceptionHandler(PlacementTermNoteExistsException.class)
	public ModelAndView handlePlacementTermNotExistsException(
			final PlacementTermNoteExistsException
				placementTermNoteExistsException) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				PLACEMENT_TERM_NOTE_EXISTS_MESSAGE_KEY,
				ERROR_MESSAGE_BUNDLE_NAME,
				placementTermNoteExistsException);
	}
	
	/**
	 * Handles {@code CorrectionalStatusTermConflictException}.
	 * 
	 * @param correctionalStatusTermConflictException exception thrown
	 * @return model and view to handle
	 * {@code CorrectionalStatusTermConflictException}
	 */
	@ExceptionHandler(CorrectionalStatusTermConflictException.class)
	public ModelAndView handleCorrectionalStatusTermConflictException(
			final CorrectionalStatusTermConflictException
				correctionalStatusTermConflictException) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				CORRECTIONAL_STATUS_TERM_CONFLICT_MESSAGE_KEY,
				ERROR_MESSAGE_BUNDLE_NAME,
				correctionalStatusTermConflictException);
	}
	
	/**
	 * Handles {@code SupervisoryOrganizationTermConflictException}.
	 * 
	 * @param supervisoryOrganizationTermConflictException exception thrown
	 * @return model and view to handle
	 * {@code SupervisoryOrganizationTermConflictException}
	 */
	@ExceptionHandler(SupervisoryOrganizationTermConflictException.class)
	public ModelAndView handleSupervisoryOrganizationTermConflictException(
			final SupervisoryOrganizationTermConflictException
				supervisoryOrganizationTermConflictException) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				SUPERVISORY_ORGANIZATION_TERM_CONFLICT_MESSAGE_KEY,
				ERROR_MESSAGE_BUNDLE_NAME,
				supervisoryOrganizationTermConflictException);
	}
	
	/**
	 * Handles {@code PlacementTermConflictException}.
	 * 
	 * @param placementTermConflictException exception thrown
	 * @return model and view to handle {@code PlacementTermConflictException}
	 */
	@ExceptionHandler(PlacementTermConflictException.class)
	public ModelAndView handlePlacementTermConflictException(
			final PlacementTermConflictException
				placementTermConflictException) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				PLACEMENT_TERM_CONFLICT_MESSAGE_KEY,
				ERROR_MESSAGE_BUNDLE_NAME,
				placementTermConflictException);
	}
	
	/**
	 * Handles {@code PlacementTermLockedException}.
	 * 
	 * @param placementTermLockedException exception thrown
	 * @return model and view to handle {@code PlacementTermLockedException}
	 */
	@ExceptionHandler(PlacementTermLockedException.class)
	public ModelAndView handlePlacementTermLockedException(
			final PlacementTermLockedException
				placementTermLockedException) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				PLACEMENT_TERM_LOCKED_MESSAGE_KEY,
				ERROR_MESSAGE_BUNDLE_NAME,
				placementTermLockedException);
	}
	
	/**
	 * Handles {@code PlacementTermExistsAfterException}.
	 * 
	 * @param placementTermExistsAfterException exception thrown
	 * @return model and view to handle {@code 
	 * PlacementTermExistsAfterException}
	 */
	@ExceptionHandler(PlacementTermExistsAfterException.class)
	public ModelAndView handlePlacementTermExistsAfterException(
			final PlacementTermExistsAfterException
				placementTermExistsAfterException) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				PLACEMENT_TERM_EXISTS_AFTER_MESSAGE_KEY,
				ERROR_MESSAGE_BUNDLE_NAME,
				placementTermExistsAfterException);
	}
	
	/**
	 * Handles {@code PlacementTermExistsBeforeException}.
	 * 
	 * @param placementTermExistsBeforeException exception thrown
	 * @return model and view to handle {@code 
	 * PlacementTermExistsBeforeException}
	 */
	@ExceptionHandler(PlacementTermExistsBeforeException.class)
	public ModelAndView handlePlacementTermExistsBeforeException(
			final PlacementTermExistsBeforeException
				placementTermExistsBeforeException) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				PLACEMENT_TERM_EXISTS_BEFORE_MESSAGE_KEY,
				ERROR_MESSAGE_BUNDLE_NAME,
				placementTermExistsBeforeException);
	}
	
	/**
	 * Handles {@code OffenderNotUnderSupervisionException}.
	 * 
	 * @param offenderNotUnderSupervisionException exception thrown
	 * @return model and view to handle {@code 
	 * OffenderNotUnderSupervisionException}
	 */
	@ExceptionHandler(OffenderNotUnderSupervisionException.class)
	public ModelAndView handleOffenderNotUnderSupervisionException(
			final OffenderNotUnderSupervisionException
				offenderNotUnderSupervisionException) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				OFFENDER_NOT_UNDER_SUPERVISION_MESSAGE_KEY,
				ERROR_MESSAGE_BUNDLE_NAME,
				offenderNotUnderSupervisionException);
	}
	
	/* Init binder. */
	
	/**
	 * Sets up and registers property editors.
	 * 
	 * @param binder web binder
	 */
	@InitBinder
	protected void initBinder(final WebDataBinder binder) {
		binder.registerCustomEditor(Offender.class,
			this.offenderPropertyEditorFactory.createOffenderPropertyEditor());
		binder.registerCustomEditor(PlacementTerm.class,
			this.placementTermPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(PlacementTermNote.class,
			this.placementTermNotePropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(PlacementTermChangeReason.class,
			this.placementTermChangeReasonPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(SupervisoryOrganization.class,
			this.supervisoryOrganizationPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(CorrectionalStatus.class,
			this.correctionalStatusPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(State.class,
				this.statePropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(Date.class, "startDate",
				this.customDateEditorFactory.createCustomDateOnlyEditor(true));
		binder.registerCustomEditor(Date.class, "startTime",
				this.customDateEditorFactory.createCustomTimeOnlyEditor(true));
		binder.registerCustomEditor(Date.class, "statusDate",
				this.customDateEditorFactory.createCustomDateOnlyEditor(true));
		binder.registerCustomEditor(Date.class, "statusTime",
				this.customDateEditorFactory.createCustomTimeOnlyEditor(true));
		binder.registerCustomEditor(Date.class, "statusReturnedDate",
				this.customDateEditorFactory.createCustomDateOnlyEditor(true));
		binder.registerCustomEditor(Date.class, "statusReturnedTime",
				this.customDateEditorFactory.createCustomTimeOnlyEditor(true));
		binder.registerCustomEditor(Date.class, "endDate",
				this.customDateEditorFactory.createCustomDateOnlyEditor(true));
		binder.registerCustomEditor(Date.class, "endTime",
				this.customDateEditorFactory.createCustomTimeOnlyEditor(true));
		binder.registerCustomEditor(Date.class, "noteItems.fields.date",
				this.customDateEditorFactory.createCustomDateOnlyEditor(true));
	}
}