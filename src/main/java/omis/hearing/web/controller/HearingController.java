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
package omis.hearing.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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
import omis.exception.DuplicateEntityFoundException;
import omis.hearing.domain.Hearing;
import omis.hearing.domain.HearingCategory;
import omis.hearing.domain.HearingNote;
import omis.hearing.domain.HearingStatus;
import omis.hearing.domain.HearingStatusCategory;
import omis.hearing.domain.ImposedSanction;
import omis.hearing.domain.Infraction;
import omis.hearing.domain.LocationType;
import omis.hearing.domain.ResolutionClassificationCategory;
import omis.hearing.domain.UserAttendance;
import omis.hearing.domain.component.Resolution;
import omis.hearing.exception.HearingExistsException;
import omis.hearing.exception.HearingNoteExistsException;
import omis.hearing.exception.HearingStatusExistsException;
import omis.hearing.exception.InfractionExistsException;
import omis.hearing.exception.UserAttendanceExistsException;
import omis.hearing.service.HearingService;
import omis.hearing.web.form.GoToOption;
import omis.hearing.web.form.HearingForm;
import omis.hearing.web.form.HearingNoteItem;
import omis.hearing.web.form.InfractionItem;
import omis.hearing.web.form.ItemOperation;
import omis.hearing.web.form.ViolationSelectionItem;
import omis.hearing.web.form.ViolationsSelectForm;
import omis.hearing.web.validator.HearingFormValidator;
import omis.location.domain.Location;
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.report.ReportFormat;
import omis.report.ReportRunner;
import omis.report.web.controller.delegate.ReportControllerDelegate;
import omis.user.domain.UserAccount;
import omis.util.DateManipulator;
import omis.violationevent.domain.ConditionViolation;
import omis.violationevent.domain.DisciplinaryCodeViolation;
import omis.web.controller.delegate.BusinessExceptionHandlerDelegate;

/**
 * Hearing Controller.
 * 
 * @author Annie Wahl
 * @author Josh Divine
 * @version 0.1.4 (May 15, 2018)
 * @since OMIS 3.0
 */
@Controller
@RequestMapping("/hearing/")
@PreAuthorize("hasRole('USER')")
public class HearingController {
	
	/* View Names */
	
	private static final String EDIT_VIEW_NAME = "/hearing/edit";
	
	private static final String HEARING_NOTE_ITEM_ROW_VIEW_NAME =
			"/hearing/includes/hearingNoteItemTableRow";
	
	private static final String HEARING_ACTION_MENU_VIEW_NAME =
			"/hearing/includes/hearingActionMenu";
	
	private static final String HEARING_NOTE_ITEMS_ACTION_MENU_VIEW_NAME =
			"/hearing/includes/hearingNoteItemsActionMenu";
	
	
	private static final String LOCATION_OPTIONS_VIEW_NAME =
			"/hearing/includes/locationOptions";
	
	/* Model Keys */
	
	private static final String HEARING_MODEL_KEY = "hearing";
	
	private static final String OFFENDER_MODEL_KEY = "offender";
	
	private static final String HEARING_NOTE_ITEM_MODEL_KEY = "hearingNoteItem";
	
	private static final String HEARING_NOTE_ITEM_INDEX_MODEL_KEY =
			"hearingNoteItemIndex";
	
	private static final String HEARING_CATEGORIES_MODEL_KEY = "categories";
	
	private static final String LOCATION_TYPES_MODEL_KEY = "locationTypes";
	
	private static final String LOCATIONS_MODEL_KEY = "locations";
	
	private static final String HEARING_STATUSES_MODEL_KEY = "statuses";
	
	private static final String FORM_MODEL_KEY = "hearingForm";
	
	/* REDIRECT VIEW NAMES */
	
	private static final String LIST_REDIRECT =
			"redirect:/hearing/list.html?offender=%d";
	
	private static final String RESOLUTION_REDIRECT =
			"redirect:/hearing/resolution/create.html?"
			+ "offender=%d&hearing=%d&resolutionCategory=%s"
			+ "&violationCategory=%s";
	
	private static final String VIOLATIONS_SELECT_REDIRECT =
			"redirect:/hearing/violations/select.html?"
					+ "offender=%d&hearing=%d&resolutionCategory=%s"
					+ "&violationCategory=%s&goToOption=%s";
	
	/* Message Keys */
		
	private static final String ENTITY_EXISTS_MESSAGE_KEY =
			"entity.exists";
	
	private static final String USER_ATTENDANCE_EXISTS_MESSAGE_KEY
		= "userAttendance.exists";
	
	private static final String HEARING_EXISTS_MESSAGE_KEY
		= "hearing.exists";
	
	private static final String INFRACTION_EXISTS_MESSAGE_KEY
		= "infraction.exists";
	
	private static final String HEARING_STATUS_EXISTS_MESSAGE_KEY
		= "hearingStatus.exists";
	
	private static final String HEARING_NOTE_EXISTS_MESSAGE_KEY
		= "hearingNote.exists";
	
/* Report names. */
	
	private static final String HEARING_LISTING_REPORT_NAME 
		= "/Compliance/Hearings/Hearings_Listing";

	private static final String HEARING_DETAILS_REPORT_NAME 
		= "/Compliance/Hearings/Hearing_Details";
	
	private static final String ADJUDICATE_HEARING_REPORT_NAME 
		= "/Compliance/Hearings/Adjudicate_Hearing";

	/* Report parameter names. */
	
	private static final String HEARING_LISTING_ID_REPORT_PARAM_NAME 
		= "DOC_ID";

	private static final String HEARING_DETAILS_ID_REPORT_PARAM_NAME 
		= "HEARING_ID";

	private static final String ADJUDICATE_HEARING_ID_REPORT_PARAM_NAME 
		= "HEARING_ID";

	/* Bundles */
	
	private static final String ERROR_BUNDLE_NAME
		= "omis.hearing.msgs.form";
	
	/* Report runners. */
	
	@Autowired
	@Qualifier("reportRunner")
	private ReportRunner reportRunner;
	
	/* Controller delegates. */
	
	@Autowired
	@Qualifier("reportControllerDelegate")
	private ReportControllerDelegate reportControllerDelegate;
	
	
	/* Services */
	
	@Autowired
	@Qualifier("hearingService")
	private HearingService hearingService;
	
	/* Property Editor Factories */
	
	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory offenderPropertyEditorFactory;
	
	@Autowired
	private CustomDateEditorFactory customDateEditorFactory;
	
	@Autowired
	@Qualifier("hearingPropertyEditorFactory")
	private PropertyEditorFactory hearingPropertyEditorFactory;
	
	@Autowired
	@Qualifier("hearingNotePropertyEditorFactory")
	private PropertyEditorFactory hearingNotePropertyEditorFactory;
	
	@Autowired
	@Qualifier("locationPropertyEditorFactory")
	private PropertyEditorFactory locationPropertyEditorFactory;
	
	@Autowired
	@Qualifier("disciplinaryCodeViolationPropertyEditorFactory")
	private PropertyEditorFactory
		disciplinaryCodeViolationPropertyEditorFactory;

	@Autowired
	@Qualifier("conditionViolationPropertyEditorFactory")
	private PropertyEditorFactory conditionViolationPropertyEditorFactory;
	
	@Autowired
	@Qualifier("userAccountPropertyEditorFactory")
	private PropertyEditorFactory userAccountPropertyEditorFactory;
	
	/* Helpers. */
	
	@Autowired
	@Qualifier("businessExceptionHandlerDelegate")
	private BusinessExceptionHandlerDelegate businessExceptionHandlerDelegate;
	
	@Autowired
	@Qualifier("offenderSummaryModelDelegate")
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;
	
	/* Validator */
	
	@Autowired
	@Qualifier("hearingFormValidator")
	private HearingFormValidator hearingFormValidator;
	
	
	/**
	 * Default constructor for HearingController.
	 */
	public HearingController() {
	}
	
	/* Views */
	
	/**
	 * Returns a model and view for hearing creation.
	 * @param offender - Offender
	 * @param resolutionCategory - ResolutionClassificationCategory
	 * @param selectForm - ViolationsSelectForm, carried over from the
	 * Violations Select screen
	 * @return ModelAndView - model and view for hearing creation
	 */
	@RequestMapping(value = "/create.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('HEARING_CREATE') or hasRole('ADMIN')")
	public ModelAndView create(
			@RequestParam(value = "offender", required = true)
				final Offender offender,
			@RequestParam(value = "resolutionCategory", required = false)
				final ResolutionClassificationCategory resolutionCategory,
			final ViolationsSelectForm selectForm) {
		
		HearingForm form = new HearingForm();

		if (selectForm.getViolationSelectionItems() != null) {
			List<InfractionItem> infractionItems =
					new ArrayList<InfractionItem>();
			for (ViolationSelectionItem selectionItem
					: selectForm.getViolationSelectionItems()) {
				InfractionItem item = new InfractionItem();
				
				if (selectionItem.getSelected()) {
					if (selectionItem.getDisciplinaryCodeViolation() != null) {
						item.setDisciplinaryCodeViolation(
								selectionItem.getDisciplinaryCodeViolation());
						
						infractionItems.add(item);
					} else if (selectionItem.getConditionViolation() != null) {
						item.setConditionViolation(
								selectionItem.getConditionViolation());
						
						infractionItems.add(item);
					}
				}
			}
			form.setInfractionItems(infractionItems);
			form.setCategory(HearingCategory.valueOf(
					selectForm.getViolationCategory().name()));
		}
		ModelMap map = new ModelMap();
		List<HearingStatusCategory> hearingStatusCategories =
				new ArrayList<HearingStatusCategory>();
		hearingStatusCategories.add(HearingStatusCategory.HELD);
		hearingStatusCategories.add(HearingStatusCategory.DISMISSED);
		hearingStatusCategories.add(HearingStatusCategory.PENDING);
		hearingStatusCategories.add(HearingStatusCategory.DELAYED);
		
		map.addAttribute(HEARING_STATUSES_MODEL_KEY, hearingStatusCategories);
		
		return prepareEditMav(offender, form, map);
	}
	
	/**
	 * Attempts to save a hearing and return to the hearing list, or returns
	 * to the hearing creation screen on creation error.
	 * @param offender - Offender
	 * @param adjudicate - String, sent if "Save And Adjudicate" button is
	 * pressed, used to trigger the redirect to the Adjudication screen
	 * @param selectViolations - String, sent if "Save and Select Violations"
	 * button is pressed, used to trigger the redirect to the Violations Select
	 * screen
	 * @param form - hearingForm
	 * @param bindingResult - Binding Result
	 * @return ModelAndView - hearing list on successful hearing creation, or
	 * back to hearing creation on error
	 * @throws DuplicateEntityFoundException - when hearing, hearing note, or
	 * user attended already exist with provided parameters
	 */
	@RequestMapping(value = "/create.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('HEARING_CREATE') or hasRole('ADMIN')")
	public ModelAndView save(
			@RequestParam(value = "offender", required = true)
				final Offender offender,
			@RequestParam(value = "adjudicate", required = false) final
				String adjudicate,
			@RequestParam(value = "selectViolations", required = false) final
				String selectViolations,
			final HearingForm form, final BindingResult bindingResult)
					throws DuplicateEntityFoundException {
		
		this.hearingFormValidator.validate(form, bindingResult);
		
		if (bindingResult.hasErrors()) {
			ModelMap map = new ModelMap();
			List<HearingStatusCategory> hearingStatusCategories =
					new ArrayList<HearingStatusCategory>();
			hearingStatusCategories.add(HearingStatusCategory.HELD);
			hearingStatusCategories.add(HearingStatusCategory.DISMISSED);
			hearingStatusCategories.add(HearingStatusCategory.PENDING);
			hearingStatusCategories.add(HearingStatusCategory.DELAYED);
			
			map.addAttribute(HEARING_STATUSES_MODEL_KEY,
					hearingStatusCategories);
			return prepareEditMav(offender, form, map);
		} else {
			Hearing hearing = this.hearingService.createHearing(
					form.getLocation(), offender, null,
					DateManipulator.getDateAtTimeOfDay(form.getDate(), 
							form.getTime()), form.getCategory(),
					form.getOfficer());
			this.hearingService.createHearingStatus(hearing, null,
					new Date(), form.getStatus());
			if (HearingStatusCategory.DISMISSED.equals(form.getStatus())) {
				for (InfractionItem infractionItem
						: form.getInfractionItems()) {
					Resolution resolution = new Resolution();
					resolution.setCategory(
							ResolutionClassificationCategory.DISMISSED);
					
					this.hearingService.createInfraction(hearing,
						(infractionItem.getConditionViolation() != null ?
								infractionItem.getConditionViolation() : null),
						(infractionItem.getDisciplinaryCodeViolation() != null ?
								infractionItem.getDisciplinaryCodeViolation()
									: null), resolution, null);
				}
			} else {
				for (InfractionItem infractionItem
						: form.getInfractionItems()) {
					this.hearingService.createInfraction(hearing,
						(infractionItem.getConditionViolation() != null ?
							infractionItem.getConditionViolation() : null),
						(infractionItem.getDisciplinaryCodeViolation() != null
							? infractionItem.getDisciplinaryCodeViolation()
							: null), null, null);
				}
			}
			
			this.processHearingNoteItems(form.getHearingNoteItems(), hearing);
			
			if (adjudicate != null) {
				return new ModelAndView(String.format(
						RESOLUTION_REDIRECT,
						hearing.getSubject().getOffender().getId(),
						hearing.getId(), 
						ResolutionClassificationCategory.FORMAL,
						hearing.getCategory()));
			} else if (selectViolations != null) {
				return new ModelAndView(String.format(
						VIOLATIONS_SELECT_REDIRECT,
						hearing.getSubject().getOffender().getId(),
						hearing.getId(), 
						ResolutionClassificationCategory.FORMAL,
						hearing.getCategory(), GoToOption.HEARINGS_LIST));
			} else {
				return new ModelAndView(String.format(
						LIST_REDIRECT, offender.getId()));
			}
		}
	}
	
	/**
	 * Returns a model and view for editing a hearing.
	 * @param hearing - hearing to edit
	 * @return ModelAndView - model and view for editing a hearing
	 */
	@RequestMapping(value = "/edit.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('HEARING_VIEW') or hasRole('ADMIN')")
	public ModelAndView edit(@RequestParam(
			value = "hearing", required = true) final Hearing hearing) {
		
		ModelMap map = new ModelMap();
		map.addAttribute(HEARING_MODEL_KEY, hearing);

		List<HearingStatusCategory> hearingStatusCategories =
				new ArrayList<HearingStatusCategory>();
		HearingStatusCategory latestStatus = this.hearingService
				.findLatestHearingStatus(hearing).getCategory();
		hearingStatusCategories.add(HearingStatusCategory.HELD);
		hearingStatusCategories.add(HearingStatusCategory.DISMISSED);
		if (!(latestStatus.getAdjudicated())) {
			hearingStatusCategories.add(HearingStatusCategory.PENDING);
			hearingStatusCategories.add(HearingStatusCategory.DELAYED);
		} else if (EnumSet.of(HearingStatusCategory.UPHELD,
				HearingStatusCategory.MODIFIED).contains(latestStatus)) {
			hearingStatusCategories.add(HearingStatusCategory.valueOf(
					latestStatus.getName()));
		}
		map.addAttribute(HEARING_STATUSES_MODEL_KEY, hearingStatusCategories);
		
		return prepareEditMav(hearing.getSubject().getOffender(),
				prepareEditForm(hearing), map);
	}
	
	/**
	 * Attempts to update a hearing and return to the hearing list, or returns
	 * to the hearing edit screen on creation error.
	 * @param hearing - hearing being edited
	 * @param adjudicate - String, sent if "Save And Adjudicate" button is
	 * pressed, used to trigger the redirect to the Adjudication screen
	 * @param selectViolations - String, sent if "Save and Select Violations"
	 * button is pressed, used to trigger the redirect to the Violations Select
	 * screen
	 * @param form - hearingForm
	 * @param bindingResult - BindingResult
	 * @return ModelAndView - hearing list on successful hearing update, or
	 * back to hearing editing on error
	 * @throws DuplicateEntityFoundException - when hearing, hearing note, or
	 * user attended already exist with provided parameters
	 */
	@RequestMapping(value = "/edit.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('HEARING_EDIT') or hasRole('ADMIN')")
	public ModelAndView update(
			@RequestParam(value = "hearing", required = true)
				final Hearing hearing,
			@RequestParam(value = "adjudicate", required = false) final
				String adjudicate,
			@RequestParam(value = "selectViolations", required = false) final
				String selectViolations,
			final HearingForm form, final BindingResult bindingResult)
					throws DuplicateEntityFoundException {
		
		this.hearingFormValidator.validate(form, bindingResult);
		
		if (bindingResult.hasErrors()) {
			ModelMap map = new ModelMap();
			List<HearingStatusCategory> hearingStatusCategories =
					new ArrayList<HearingStatusCategory>();
			HearingStatusCategory latestStatus = this.hearingService
					.findLatestHearingStatus(hearing).getCategory();
			hearingStatusCategories.add(HearingStatusCategory.HELD);
			hearingStatusCategories.add(HearingStatusCategory.DISMISSED);
			if (!(latestStatus.getAdjudicated())) {
				hearingStatusCategories.add(HearingStatusCategory.PENDING);
				hearingStatusCategories.add(HearingStatusCategory.DELAYED);
			} else if (EnumSet.of(HearingStatusCategory.UPHELD,
					HearingStatusCategory.MODIFIED).contains(latestStatus)) {
				hearingStatusCategories.add(HearingStatusCategory.valueOf(
						latestStatus.getName()));
			}
			map.addAttribute(HEARING_STATUSES_MODEL_KEY,
					hearingStatusCategories);
			
			map.addAttribute(HEARING_MODEL_KEY, hearing);
			
			return prepareEditMav(hearing.getSubject().getOffender(), form,
					map);
		} else {
			this.processHearingNoteItems(form.getHearingNoteItems(), hearing);
			if (!(form.getStatus().equals(this.hearingService
					.findLatestHearingStatus(hearing).getCategory()))) {
				this.hearingService.createHearingStatus(hearing, null,
						new Date(), form.getStatus());
			}
			this.hearingService.updateHearing(hearing,
					form.getLocation(), hearing.getSubject().getInAttendance(),
					DateManipulator.getDateAtTimeOfDay(form.getDate(), 
							form.getTime()), form.getCategory(),
					form.getOfficer());
			if (HearingStatusCategory.DISMISSED.equals(form.getStatus())) {
				for (Infraction infraction : this.hearingService
						.findInfractionsByHearing(hearing)) {
					Resolution resolution = new Resolution();
					resolution.setCategory(
							ResolutionClassificationCategory.DISMISSED);
					
					this.hearingService.updateInfraction(
						infraction,
						(infraction.getConditionViolation() != null ?
							infraction.getConditionViolation() : null),
						(infraction.getDisciplinaryCodeViolation() != null ?
							infraction.getDisciplinaryCodeViolation() : null),
						resolution, infraction.getPlea());
				}
			}
			if (adjudicate != null) {
				return new ModelAndView(String.format(
						RESOLUTION_REDIRECT,
						hearing.getSubject().getOffender().getId(),
						hearing.getId(),
						ResolutionClassificationCategory.FORMAL,
						hearing.getCategory()));
			} else if (selectViolations != null) {
				return new ModelAndView(String.format(
						VIOLATIONS_SELECT_REDIRECT,
						hearing.getSubject().getOffender().getId(),
						hearing.getId(), 
						ResolutionClassificationCategory.FORMAL,
						hearing.getCategory(), GoToOption.HEARINGS_LIST));
			} else {
				return new ModelAndView(String.format(
					LIST_REDIRECT, hearing.getSubject().getOffender().getId()));
			}
		}
	}
	
	/**
	 * Removes a hearing and returns to the hearing list screen.
	 * @param hearing - hearing to remove
	 * @return ModelAndView - redirect to hearing list screen
	 */
	@RequestMapping(value = "/remove.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('HEARING_REMOVE') or hasRole('ADMIN')")
	public ModelAndView remove(@RequestParam(
			value = "hearing", required = true) final Hearing hearing) {
		
		List<HearingNote> hearingNotes = this.hearingService
				.findHearingNotesByHearing(hearing);
		List<UserAttendance> userAttended = this.hearingService
				.findUserAttendedByHearing(hearing);
		List<Infraction> infractions = this.hearingService
				.findInfractionsByHearing(hearing);
		
		for (HearingNote note : hearingNotes) {
			this.hearingService.removeHearingNote(note);
		}
		for (UserAttendance user : userAttended) {
			this.hearingService.removeUserAttendance(user);
		}
		for (Infraction infraction : infractions) {
			ImposedSanction imposedSanction = this.hearingService
					.findImposedSanctionByInfraction(infraction);
			if (imposedSanction != null) {
				this.hearingService.removeImposedSanction(imposedSanction);
			}
			this.hearingService.removeInfraction(infraction);
		}
		for (HearingStatus hearingStatus : this.hearingService
				.findHearingStatusesByHearing(hearing)) {
			this.hearingService.removeHearingStatus(hearingStatus);
		}
		
		this.hearingService.removeHearing(hearing);
		
		return new ModelAndView(String.format(
				LIST_REDIRECT, hearing.getSubject().getOffender().getId()));
	}
	
	
	/* Item Row Views */
	
	
	/**
	 * Displays a hearing note item row.
	 * @param hearingNoteItemIndex - Integer
	 * @return ModelAndView - view for a hearing note item row
	 */
	@RequestMapping(value = "createHearingNoteItem.html",
			method = RequestMethod.GET)
	public ModelAndView displayHearingNoteItemRow(@RequestParam(
			value = "hearingNoteItemIndex", required = true)
			final Integer hearingNoteItemIndex) {
		ModelMap map = new ModelMap();
		HearingNoteItem noteItem = new HearingNoteItem();
		noteItem.setItemOperation(ItemOperation.CREATE);
		map.addAttribute(HEARING_NOTE_ITEM_MODEL_KEY, noteItem);
		map.addAttribute(HEARING_NOTE_ITEM_INDEX_MODEL_KEY,
				hearingNoteItemIndex);
		return new ModelAndView(HEARING_NOTE_ITEM_ROW_VIEW_NAME, map);
	}
	
	/* Action Menu Views */
	
	/**
	 * Returns the model and view for hearing action menu.
	 * @param offender - Offender
	 * @return ModelAndView - model and view for hearing action menu
	 */
	@RequestMapping(value = "/hearingActionMenu.html", 
			method = RequestMethod.GET)
	public ModelAndView displayHearingActionMenu(@RequestParam
			(value = "offender", required = true) final Offender offender) {
		ModelMap map = new ModelMap();
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		
		return new ModelAndView(HEARING_ACTION_MENU_VIEW_NAME, map);
	}
	
	/**
	 * Returns a model and view for hearing note items action menu.
	 * @return ModelAndView - model and view for hearing note items action menu
	 */
	@RequestMapping(value = "hearingNoteItemsActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView displayHearingNoteItemsActionMenu() {
		ModelMap map = new ModelMap();	
		return new ModelAndView(
				HEARING_NOTE_ITEMS_ACTION_MENU_VIEW_NAME, map);
	}
	
	/**
	 * Displays the location options.
	 * @param locationType - location type of locations to show
	 * @return ModelAndView - location options
	 */
	@RequestMapping(value = "/showLocationOptions.html",
			method = RequestMethod.GET)
	public ModelAndView showLocationOptions(
			@RequestParam(value = "locationType", required = true)
			final LocationType locationType) {
		ModelMap map = new ModelMap();
		List<Location> locations = new ArrayList<Location>();
		if (LocationType.FACILITIES.equals(locationType)) {
			locations = this.hearingService.findFacilityLocations();
		} else if (LocationType.JAILS.equals(locationType)) {
			locations = this.hearingService.findJailLocations();
		} else if (LocationType.COMMUNITY_SUPERVISION_OFFICES
				.equals(locationType)) {
			locations = this.hearingService
					.findCommunitySupervisionOfficeLocations();
		} else if (LocationType.PRERELEASES.equals(locationType)) {
			locations = this.hearingService.findPreReleaseCenterLocations();
		} else if (LocationType.TREATMENT_AND_SANCTION_CENTERS
				.equals(locationType)) {
			locations = this.hearingService.findTreatmentCenterLocations();
		} else {
			throw new UnsupportedOperationException(
					"Location Type Not Supported");
		}
		
		map.put(LOCATIONS_MODEL_KEY, locations);
		
		return new ModelAndView(
				LOCATION_OPTIONS_VIEW_NAME, map);
	}
	
	
	/**
	 * Returns the report for the specified offenders hearings.
	 * 
	 * @param offender offender
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/hearingListingReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('HEARING_VIEW') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportViolationEventListing(@RequestParam(
			value = "offender", required = true)
			final Offender offender,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(HEARING_LISTING_ID_REPORT_PARAM_NAME,
				Long.toString(offender.getOffenderNumber()));
		byte[] doc = this.reportRunner.runReport(
				HEARING_LISTING_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/**
	 * Returns the report for the specified hearing.
	 * 
	 * @param hearing hearing
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/hearingDetailsReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('HEARING_VIEW') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportHearingDetails(@RequestParam(
			value = "hearing", required = true)
			final Hearing hearing,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(HEARING_DETAILS_ID_REPORT_PARAM_NAME,
				Long.toString(hearing.getId()));
		byte[] doc = this.reportRunner.runReport(
				HEARING_DETAILS_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/**
	 * Returns the report for the specified adjudicate hearing.
	 * 
	 * @param hearing hearing
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/adjudicateHearingReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('HEARING_VIEW') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportAdjudicateHearing(@RequestParam(
			value = "hearing", required = true)
			final Hearing hearing,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(ADJUDICATE_HEARING_ID_REPORT_PARAM_NAME,
				Long.toString(hearing.getId()));
		byte[] doc = this.reportRunner.runReport(
				ADJUDICATE_HEARING_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/* Helper Methods */
	
	/**
	 * Returns the prepared model and view for editing a hearing.
	 * @param offender - Offender
	 * @param form - hearingForm
	 * @param map - ModelMap
	 * @return ModelAndView - prepared model and view for editing a hearing
	 */
	private ModelAndView prepareEditMav(final Offender offender,
			final HearingForm form, final ModelMap map) {
		
		//populate the Location list appropriately if there is a location
		if (form.getLocation() != null) {
			List<Location> facilityLocations = this.hearingService
					.findFacilityLocations();
			List<Location> jailLocations = this.hearingService
					.findJailLocations();
			List<Location> commSupervisionOffices = this.hearingService
					.findCommunitySupervisionOfficeLocations();
			List<Location> preReleaseCenters = this.hearingService
					.findPreReleaseCenterLocations();
			List<Location> trmtAndSnctCenters = this.hearingService
					.findTreatmentCenterLocations();
			if (facilityLocations.contains(form.getLocation())) {
				map.put(LOCATIONS_MODEL_KEY, facilityLocations);
				form.setLocationType(LocationType.FACILITIES);
			} else if (jailLocations.contains(form.getLocation())) {
				map.put(LOCATIONS_MODEL_KEY, jailLocations);
				form.setLocationType(LocationType.JAILS);
			} else if (commSupervisionOffices.contains(form.getLocation())) {
				map.put(LOCATIONS_MODEL_KEY, commSupervisionOffices);
				form.setLocationType(
						LocationType.COMMUNITY_SUPERVISION_OFFICES);
			} else if (preReleaseCenters.contains(form.getLocation())) {
				map.put(LOCATIONS_MODEL_KEY, preReleaseCenters);
				form.setLocationType(LocationType.PRERELEASES);
			} else if (trmtAndSnctCenters.contains(form.getLocation())) {
				map.put(LOCATIONS_MODEL_KEY, trmtAndSnctCenters);
				form.setLocationType(
						LocationType.TREATMENT_AND_SANCTION_CENTERS);
			}
		} else if (form.getLocationType() != null) {
			List<Location> locations = new ArrayList<Location>();
			if (LocationType.FACILITIES.equals(form.getLocationType())) {
				locations = this.hearingService.findFacilityLocations();
			} else if (LocationType.JAILS.equals(form.getLocationType())) {
				locations = this.hearingService.findJailLocations();
			} else if (LocationType.COMMUNITY_SUPERVISION_OFFICES.equals(
					form.getLocationType())) {
				locations = this.hearingService
						.findCommunitySupervisionOfficeLocations();
			} else if (LocationType.PRERELEASES.equals(
					form.getLocationType())) {
				locations = this.hearingService.findPreReleaseCenterLocations();
			} else if (LocationType.TREATMENT_AND_SANCTION_CENTERS.equals(
					form.getLocationType())) {
				locations = this.hearingService.findTreatmentCenterLocations();
			} else {
				throw new UnsupportedOperationException(
						"Location Type Not Supported");
			}
			map.put(LOCATIONS_MODEL_KEY, locations);
		}
		
		//populate the item indexes
		int hnIndex = 0;
			//hearing note item index
		
		if (form.getHearingNoteItems() != null) {
			hnIndex = form.getHearingNoteItems().size();
		}
		
		map.addAttribute(HEARING_NOTE_ITEM_INDEX_MODEL_KEY, hnIndex);
		map.addAttribute(HEARING_CATEGORIES_MODEL_KEY,
				HearingCategory.values());
		map.addAttribute(LOCATION_TYPES_MODEL_KEY, LocationType.values());
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		map.addAttribute(FORM_MODEL_KEY, form);
		this.offenderSummaryModelDelegate.add(map, offender);
		
		return new ModelAndView(EDIT_VIEW_NAME, map);
	}
	
	/**
	 * Prepares a hearingForm with a Hearing's values.
	 * @param hearing - Hearing used to populate the form
	 * @return HearingForm - populated HearingForm
	 */
	private HearingForm prepareEditForm(final Hearing hearing) {
		
		HearingForm form = new HearingForm();
		List<HearingNoteItem> hearingNoteItems =
				new ArrayList<HearingNoteItem>();
		List<InfractionItem> infractionItems = new ArrayList<InfractionItem>();
		
		List<HearingNote> hearingNotes =
				this.hearingService.findHearingNotesByHearing(hearing);
		for (HearingNote note : hearingNotes) {
			HearingNoteItem item = new HearingNoteItem();
			item.setHearingNote(note);
			item.setDate(note.getDate());
			item.setDescription(note.getDescription());
			item.setItemOperation(ItemOperation.UPDATE);
			
			hearingNoteItems.add(item);
		}
		List<Infraction> infractions = this.hearingService
				.findInfractionsByHearing(hearing);
		for (Infraction infraction : infractions) {
			InfractionItem item = new InfractionItem();
			item.setInfraction(infraction);
			item.setDisciplinaryCodeViolation(infraction
					.getDisciplinaryCodeViolation());
			item.setConditionViolation(infraction.getConditionViolation());
			
			infractionItems.add(item);
		}
		HearingStatusCategory status = this.hearingService
				.findLatestHearingStatus(hearing).getCategory();
		
		form.setLocation(hearing.getLocation());
		form.setCategory(hearing.getCategory());
		form.setDate(hearing.getDate());
		form.setTime(hearing.getDate());
		form.setOfficer(hearing.getOfficer());
		form.setStatus(status);
		form.setInfractionItems(infractionItems);
		form.setHearingNoteItems(hearingNoteItems);
		
		return form;
	}
	
	/**
	 * Creates, updates, and removes hearing notes based on their
	 * item operation.
	 * @param hearingNoteItems - list of hearingNoteItems to be processed
	 * @param hearing - hearing for which the items are being processed
	 * @throws DuplicateEntityFoundException - when a hearing note's date and 
	 * description already exist for the hearing
	 */
	private void processHearingNoteItems(
			final List<HearingNoteItem> hearingNoteItems,
			final Hearing hearing) throws DuplicateEntityFoundException {
		if (hearingNoteItems != null) {
			for (HearingNoteItem item : hearingNoteItems) {
				if (ItemOperation.CREATE.equals(item.getItemOperation())) {
					this.hearingService.createHearingNote(hearing,
							item.getDescription(), item.getDate());
				} else if (ItemOperation.UPDATE.equals(
						item.getItemOperation())) {
					if (this.isNoteChanged(item.getHearingNote(),
							item.getDate(), item.getDescription())) {
						this.hearingService.updateHearingNote(
								item.getHearingNote(),
								item.getDescription(), item.getDate());
					}
				} else if (ItemOperation.REMOVE.equals(
						item.getItemOperation())) {
					this.hearingService.removeHearingNote(
							item.getHearingNote());
				}
			}
		}
	}
	
	/**
	 * Checks if a Hearing Note has been changed and returns true if it has.
	 * @param note - Hearing Note to check for change
	 * @param date - Note Date from Form
	 * @param value - Note Value from Form
	 * @return Boolean - true if either the form's note date or value is
	 * different from the original Note's, false otherwise.
	 */
	private boolean isNoteChanged(final HearingNote note,
			final Date date, final String value) {
		if (!note.getDescription().equals(value)) {
			return true;
		}
		if (!(note.getDate().getTime() == date.getTime())) {
			return true;
		}
		return false;
	}
	
	/**
	 * Handles duplicate entity found exceptions.
	 * 
	 * @param exception duplicate entity found exception
	 * @return model and view for displaying exception explanation
	 */
	@ExceptionHandler(DuplicateEntityFoundException.class)
	public ModelAndView handleDuplicateEntityFoundException(
			final DuplicateEntityFoundException exception) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				ENTITY_EXISTS_MESSAGE_KEY, ERROR_BUNDLE_NAME,  exception);
	}
	
	@ExceptionHandler(HearingExistsException.class)
	public ModelAndView handleDuplicateEntityFoundException(
			final HearingExistsException exception) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				HEARING_EXISTS_MESSAGE_KEY, ERROR_BUNDLE_NAME,  exception);
	}
	
	@ExceptionHandler(HearingStatusExistsException.class)
	public ModelAndView handleDuplicateEntityFoundException(
			final HearingStatusExistsException exception) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				HEARING_STATUS_EXISTS_MESSAGE_KEY, ERROR_BUNDLE_NAME,  exception);
	}
	
	@ExceptionHandler(InfractionExistsException.class)
	public ModelAndView handleDuplicateEntityFoundException(
			final InfractionExistsException exception) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				INFRACTION_EXISTS_MESSAGE_KEY, ERROR_BUNDLE_NAME,  exception);
	}
	
	@ExceptionHandler(HearingNoteExistsException.class)
	public ModelAndView handleDuplicateEntityFoundException(
			final HearingNoteExistsException exception) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				HEARING_NOTE_EXISTS_MESSAGE_KEY, ERROR_BUNDLE_NAME,  exception);
	}
	
	@ExceptionHandler(UserAttendanceExistsException.class)
	public ModelAndView handleDuplicateEntityFoundException(
			final UserAttendanceExistsException exception) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				USER_ATTENDANCE_EXISTS_MESSAGE_KEY, ERROR_BUNDLE_NAME,  exception);
	}
	
	/* InitBinder */
	
	/**
	 * Sets up and registers property editors.
	 * 
	 * @param binder - web binder
	 */
	@InitBinder
	protected void initBinder(final WebDataBinder binder) {
		binder.registerCustomEditor(
				Offender.class,
				this.offenderPropertyEditorFactory
				.createOffenderPropertyEditor());
		binder.registerCustomEditor(Date.class, this.customDateEditorFactory
				.createCustomDateOnlyEditor(true));
		binder.registerCustomEditor(
				Hearing.class, this.hearingPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(
				HearingNote.class, this.hearingNotePropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(
				Location.class, this.locationPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(
				DisciplinaryCodeViolation.class,
				this.disciplinaryCodeViolationPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(
				ConditionViolation.class,
				this.conditionViolationPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(
				UserAccount.class,
				this.userAccountPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(
				Date.class, "time",
				this.customDateEditorFactory
				.createCustomTimeOnlyEditor(true));
	}
}