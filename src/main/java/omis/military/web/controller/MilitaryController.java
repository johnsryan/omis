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
package omis.military.web.controller;

import java.util.ArrayList;
import java.util.Date;
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
import omis.exception.DateConflictException;
import omis.exception.DuplicateEntityFoundException;
import omis.military.domain.MilitaryBranch;
import omis.military.domain.MilitaryDischargeStatus;
import omis.military.domain.MilitaryServiceTerm;
import omis.military.domain.MilitaryServiceTermNote;
import omis.military.report.MilitaryReportService;
import omis.military.service.MilitaryServiceTermService;
import omis.military.web.form.MilitaryServiceTermForm;
import omis.military.web.form.MilitaryServiceTermNoteItem;
import omis.military.web.form.MilitaryServiceTermNoteItemOperation;
import omis.military.web.validator.MilitaryServiceTermFormValidator;
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.report.OffenderReportService;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.report.ReportFormat;
import omis.report.ReportRunner;
import omis.report.web.controller.delegate.ReportControllerDelegate;
import omis.web.controller.delegate.BusinessExceptionHandlerDelegate;

/**
 * Military controller.
 * 
 * @author Joel Norris
 * @author Josh Divine
 * @version 0.1.1 (Feb 12, 2018)
 * @since OMIS 3.0
 */
@Controller
@RequestMapping("/military")
@PreAuthorize("hasRole('USER')")
public class MilitaryController {

	/* Redirect URLs. */
	
	private static final String LIST_REDIRECT_URL 
		= "redirect:list.html?offender=";
	
	/* View names. */
	
	private static final String LIST_VIEW_NAME = "military/list";
	
	private static final String EDIT_VIEW_NAME = "military/edit";
	
	private static final String SERVICE_TERMS_ACTION_MENU_VIEW_NAME
		= "military/includes/serviceTermsActionMenu";
	
	private static final String SERVICE_TERM_ACTION_MENU_VIEW_NAME
		= "military/includes/serviceTermActionMenu";
	
	private static final String SERVICE_TERM_NOTE_ITEMS_ACTION_MENU_VIEW_NAME
		= "military/includes/serviceTermNoteItemsActionMenu";
	
	private static final String SERVICE_TERM_NOTE_ITEM_ROW_VIEW_NAME
		= "military/includes/serviceTermNoteItemTableRow";
	
	private static final String SERVICE_TERM_ROW_ACTION_MENU_VIEW_NAME
		= "military/includes/serviceTermRowActionMenu";
	
	/* Model keys. */
	
	private static final String OFFENDER_MODEL_KEY = "offender";
	
	private static final String SERVICE_TERM_SUMMARIES_MODEL_KEY
		= "serviceTermSummaries";
	
	private static final String MILITARY_BRANCHES_MODEL_KEY = "branches";
	
	private static final String MILITARY_DISCHARGE_STATUSES_MODEL_KEY
		= "dischargeStatuses";
	
	private static final String MILITARY_SERVICE_TERM_FORM_MODEL_KEY
		= "militaryServiceTermForm";
	
	private static final String SERVICE_TERM_NOTE_ITEM_INDEX_MODEL_KEY
		= "serviceTermNoteItemIndex";
	
	private static final String SERVICE_TERM_NOTE_ITEM_MODEL_KEY
		= "serviceTermNoteItem";
	
	private static final String SERVICE_TERM_MODEL_KEY = "serviceTerm";
	
	/* Message keys. */
	
	private static final String MILITARY_SERVICE_TERM_EXISTS_MESSAGE_KEY
		= "serviceTerm.exists";
	
	private static final String MILITARY_SERVICE_TERM_DATE_CONFLICT_MESSAGE_KEY
		= "serviceTerm.dateConflict";
	
	/* Report names. */
	
	private static final String MILITARY_LISTING_REPORT_NAME 
		= "/BasicInformation/Military/Military_Listing";
	
	private static final String MILITARY_DETAILS_REPORT_NAME 
		= "/BasicInformation/Military/Military_Detail";
	
	/* Report parameter names. */
	
	private static final String MILITARY_LISTING_ID_REPORT_PARAM_NAME
		= "DOC_ID";
	
	private static final String MILITARY_DETAILS_ID_REPORT_PARAM_NAME
		= "MST_ID";

	/* Bundles. */
	
	private static final String ERROR_BUNDLE_NAME
		= "omis.military.msgs.form";
	
	/* Services. */
	
	@Autowired
	@Qualifier("militaryServiceTermService")
	private MilitaryServiceTermService militaryServiceTermService;
	
	@Autowired
	@Qualifier("militaryReportService")
	private MilitaryReportService militaryReportService;
	
	@Autowired
	@Qualifier("offenderReportService")
    private OffenderReportService offenderReportService;

	@Autowired
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;
	
	/* Helpers. */
	
	@Autowired
	@Qualifier("businessExceptionHandlerDelegate")
	private BusinessExceptionHandlerDelegate businessExceptionHandlerDelegate;
	
	/* Validators. */
	
	@Autowired
	@Qualifier("militaryServiceTermFormValidator")
	private MilitaryServiceTermFormValidator militaryServiceTermFormValidator;
	
	/* Property Editor factories. */
	
	@Autowired
	@Qualifier("militaryServiceTermPropertyEditorFactory")
	private PropertyEditorFactory militaryServiceTermPropertyEditorFactory;
	
	@Autowired
	@Qualifier("militaryDischargeStatusPropertyEditorFactory")
	private PropertyEditorFactory militaryDischargeStatusPropertyEditorFactory;
	
	@Autowired
	@Qualifier("militaryBranchPropertyEditorFactory")
	private PropertyEditorFactory militaryBranchPropertyEditorFactory;
	
	@Autowired
	@Qualifier("militaryServiceTermNotePropertyEditorFactory")
	private PropertyEditorFactory militaryServiceTermNotePropertyEditorFactory;
	
	@Autowired
	private CustomDateEditorFactory customDateEditorFactory;
	
	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory offenderPropertyEditorFactory;
	
	/* Report runners. */
	
	@Autowired
	@Qualifier("reportRunner")
	private ReportRunner reportRunner;
	
	/* Controller delegates. */
	
	@Autowired
	@Qualifier("reportControllerDelegate")
	private ReportControllerDelegate reportControllerDelegate;
	
	/* Constructors. */
	
	/** Instantiates a default instance of military controller. */
	public MilitaryController() {
		//Default constructor.
	}
	
	/**
	 * Presents a model and view to list the specified offender's military
	 * service terms.
	 * 
	 * @param offender offender
	 * @return model and view for military service terms list
	 */
	@RequestMapping(value = "/list.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('ADMIN') or hasRole('MILITARY_LIST')")
	public ModelAndView list(@RequestParam(value = "offender", required = true)
			final Offender offender) {
		ModelMap map = new ModelMap();
		map.addAttribute(SERVICE_TERM_SUMMARIES_MODEL_KEY, 
				this.militaryReportService
				.findMilitaryServiceTermSummariesByOffender(offender));
		this.offenderSummaryModelDelegate.add(map, offender);
		
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		return new ModelAndView(LIST_VIEW_NAME, map);
	}
	
	/**
	 * Presents a model and view to create a new military service term for
	 * the specified offender.
	 * 
	 * @param offender offender
	 * @return model and view to create a military service term
	 */
	@RequestMapping(value = "/create.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('ADMIN') or hasRole('MILITARY_CREATE')")
	public ModelAndView create(@RequestParam(value = "offender",
		required = true) final Offender offender) {
		ModelMap map = new ModelMap();
		MilitaryServiceTermForm form = new MilitaryServiceTermForm();
		return this.prepareEditMav(map, offender, form);
	}
	
	/**
	 * Creates a new military service term for the specified offender from
	 * the specified military service term form properties.
	 * 
	 * @param offender offender
	 * @param form military service term form
	 * @param result binding result
	 * @return redirect to military service term list
	 * @throws DateConflictException thrown when a service term is found to
	 * overlap another service term for the specified offender
	 * @throws DuplicateEntityFoundException thrown when a duplicate service
	 * term is found
	 */
	@RequestMapping(value = "/create.html",
			method = RequestMethod.POST)
	@PreAuthorize("hasRole('ADMIN') or hasRole('MILITARY_CREATE')")
	public ModelAndView save(@RequestParam(value = "offender", required = true)
			final Offender offender, final MilitaryServiceTermForm form,
			final BindingResult result) 
		throws DuplicateEntityFoundException, DateConflictException {
		this.militaryServiceTermFormValidator.validate(form, result);
		if (result.hasErrors()) {
			ModelMap map = new ModelMap();
			map.addAttribute(BindingResult.MODEL_KEY_PREFIX 
					+ MILITARY_SERVICE_TERM_FORM_MODEL_KEY, result);
			return this.prepareEditMav(map, offender, form);
		}
		MilitaryServiceTerm serviceTerm = this.militaryServiceTermService
				.create(offender, form.getBranch(),
				form.getDischargeStatus(), form.getStartDate(), 
				form.getEndDate());
		this.processMilitaryServiceTermNoteItems(form.getServiceTermNoteItems(),
				serviceTerm);
		return new ModelAndView(LIST_REDIRECT_URL + offender.getId());
	}
	
	/**
	 * Presents a model and view to edit the specified military service term.
	 * 
	 * @param serviceTerm military service term
	 * @return model and view to edit a military service term
	 */
	@RequestMapping(value = "/edit.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('ADMIN') or hasRole('MILITARY_EDIT')"
			+ " or hasRole('MILITARY_VIEW')")
	public ModelAndView edit(@RequestParam(value = "serviceTerm") 
		final MilitaryServiceTerm serviceTerm) {
		ModelMap map = new ModelMap();
		MilitaryServiceTermForm form = new MilitaryServiceTermForm();
		this.populateEditForm(form, serviceTerm);
		List<MilitaryServiceTermNote> serviceTermNotes = 
				this.militaryServiceTermService
				.findServiceTermNotesByServiceTerm(serviceTerm);
		List<MilitaryServiceTermNoteItem> items 
			= new ArrayList<MilitaryServiceTermNoteItem>(); 
		for (MilitaryServiceTermNote serviceTermNote : serviceTermNotes) {
			MilitaryServiceTermNoteItem item = 
					new MilitaryServiceTermNoteItem();
			item.setDate(serviceTermNote.getDate());
			item.setNote(serviceTermNote.getNote());
			item.setServiceTermNote(serviceTermNote);
			item.setOperation(MilitaryServiceTermNoteItemOperation.UPDATE);
			items.add(item);
		}
		form.setServiceTermNoteItems(items);
		map.addAttribute(SERVICE_TERM_MODEL_KEY, serviceTerm);
		return this.prepareEditMav(map, serviceTerm.getOffender(), form);
	}
	
	/**
	 * Updates the specified military service term with values from the 
	 * specified military service term form.
	 * 
	 * @param serviceTerm military service term
	 * @param form military service term form
	 * @param result binding result
	 * @return redirect to military service term list screen
	 * @throws DateConflictException 
	 * @throws DuplicateEntityFoundException 
	 */
	@RequestMapping(value = "/edit.html",
			method = RequestMethod.POST)
	@PreAuthorize("hasRole('ADMIN') or hasRole('MILITARY_EDIT')")
	public ModelAndView update(@RequestParam(value = "serviceTerm") 
			final MilitaryServiceTerm serviceTerm, 
			final MilitaryServiceTermForm form, final BindingResult result) 
		throws DuplicateEntityFoundException, DateConflictException {
		this.militaryServiceTermFormValidator.validate(form, result);
		if (result.hasErrors()) {
			ModelMap map = new ModelMap();
			map.addAttribute(BindingResult.MODEL_KEY_PREFIX 
					+ MILITARY_SERVICE_TERM_FORM_MODEL_KEY, result);
			return this.prepareEditMav(map, serviceTerm.getOffender(), form);
		}
		this.militaryServiceTermService.update(serviceTerm, form.getBranch(),
				form.getDischargeStatus(), form.getStartDate(),
				form.getEndDate());
		this.processMilitaryServiceTermNoteItems(form.getServiceTermNoteItems(),
				serviceTerm);
		return new ModelAndView(LIST_REDIRECT_URL + serviceTerm.getOffender()
				.getId());
	}
	
	/**
	 * Removes the specified service term and redirects to the military
	 * service terms list screen for the offender of the specified service
	 * term.
	 * 
	 * @param serviceTerm military service term
	 * @return redirect to military service terms list screen
	 */
	@RequestMapping(value = "/remove.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('ADMIN') or hasRole('MILITARY_REMOVE')")
	public ModelAndView remove(@RequestParam(value = "serviceTerm",
		required = true) final MilitaryServiceTerm serviceTerm) {
		List<MilitaryServiceTermNote> serviceTermNotes = 
				this.militaryServiceTermService
				.findServiceTermNotesByServiceTerm(serviceTerm);
		for(MilitaryServiceTermNote serviceTermNote : serviceTermNotes) {
			this.militaryServiceTermService.removeNote(serviceTermNote);
		}
		this.militaryServiceTermService.remove(serviceTerm);	
		return new ModelAndView(LIST_REDIRECT_URL 
				+ serviceTerm.getOffender().getId());
	}
	
	/**
	 * Displays the action menu for the specified offender's service terms 
	 * listing screen.
	 * 
	 * @param offender offender
	 * @return model and view for service terms action menu
	 */
	@RequestMapping(value = "/militaryServiceTermsActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView displayServiceTermsActionMenu(@RequestParam(
			value = "offender", required = true)
			final Offender offender) {
		ModelMap map = new ModelMap();
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		return new ModelAndView(SERVICE_TERMS_ACTION_MENU_VIEW_NAME, map);
	}
	
	/**
	 * Displays the action menu for the specified offender's service term
	 * edit screen.
	 *  
	 * @param offender offender
	 * @return model and view for service term action menu
	 */
	@RequestMapping(value = "/militaryServiceTermActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView displayServiceTermActionMenu(@RequestParam(
			value = "offender", required = true)
			final Offender offender) {
		ModelMap map = new ModelMap();
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		return new ModelAndView(SERVICE_TERM_ACTION_MENU_VIEW_NAME, map);
	}
	
	/**
	 * Displays the action menu for military service term note items.
	 * 
	 * @return model and view for service term note items action menu
	 */
	@RequestMapping(value = "/militaryServiceTermNoteItemsActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView displayServiceTermNoteItemsActionMenu() {
		ModelMap map = new ModelMap();	
		return new ModelAndView(
				SERVICE_TERM_NOTE_ITEMS_ACTION_MENU_VIEW_NAME, map);
	}
	
	/**
	 * Displays the action menu for the specified service term row.
	 * 
	 * @param term military service term
	 * @return model and view for military service term row action menu
	 */
	@RequestMapping(value = "/militaryServiceTermRowActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView displayServiceTermRowActionMenu(
			@RequestParam(value = "serviceTerm", required = true)
			final MilitaryServiceTerm term) {
		ModelMap map = new ModelMap();
		map.addAttribute(SERVICE_TERM_MODEL_KEY, term);
		return new ModelAndView(SERVICE_TERM_ROW_ACTION_MENU_VIEW_NAME, map);
	}
	
	/**
	 * Displays a service term note item row.
	 * 
	 * @return model and view for service term note item row
	 */
	@RequestMapping(value = "/createServiceTermNoteItem.html",
			method = RequestMethod.GET)
	public ModelAndView displayServiceTermNoteItemRow(@RequestParam(
			value = "serviceTermNoteItemIndex", required = true)
			final Integer serviceTermNoteItemIndex) {
		ModelMap map = new ModelMap();
		MilitaryServiceTermNoteItem serviceTermNoteItem
			= new MilitaryServiceTermNoteItem();
		serviceTermNoteItem.setOperation(
				MilitaryServiceTermNoteItemOperation.CREATE);
		map.addAttribute(SERVICE_TERM_NOTE_ITEM_MODEL_KEY, serviceTermNoteItem);
		map.addAttribute(SERVICE_TERM_NOTE_ITEM_INDEX_MODEL_KEY,
				serviceTermNoteItemIndex);
		return new ModelAndView(SERVICE_TERM_NOTE_ITEM_ROW_VIEW_NAME, map);
	}
	
	/* Exception handler methods. */
	
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
				MILITARY_SERVICE_TERM_EXISTS_MESSAGE_KEY, ERROR_BUNDLE_NAME, 
				exception);
	}
	
	/**
	 * Handles date conflict exceptions.
	 * 
	 * @param exception date conflict exception
	 * @return model and view for displaying exception explanation
	 */
	@ExceptionHandler(DateConflictException.class)
	public ModelAndView handleDateConflictException(
			final DateConflictException exception) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				MILITARY_SERVICE_TERM_DATE_CONFLICT_MESSAGE_KEY, 
				ERROR_BUNDLE_NAME, exception);
	}
	
	/**
	 * Returns the report for the specified offender.
	 * 
	 * @param offender offender
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/militaryServiceTermListingReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('MILITARY_LIST') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportMilitaryServiceTermListing(@RequestParam(
			value = "offender", required = true)
			final Offender offender,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(MILITARY_LISTING_ID_REPORT_PARAM_NAME,
				Long.toString(offender.getOffenderNumber()));
		byte[] doc = this.reportRunner.runReport(
				MILITARY_LISTING_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/**
	 * Returns the report for the specified military service term.
	 * 
	 * @param serviceTerm military service term
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/militaryServiceTermDetailsReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('MILITARY_LIST') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportMilitaryServiceTermDetails(@RequestParam(
			value = "serviceTerm", required = true)
			final MilitaryServiceTerm serviceTerm,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(MILITARY_DETAILS_ID_REPORT_PARAM_NAME,
				Long.toString(serviceTerm.getId()));
		byte[] doc = this.reportRunner.runReport(
				MILITARY_DETAILS_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/* Helper methods. */
	
	/*
	 * Prepares a model and view for editing a military service term.
	 * 
	 * @param map model map
	 * @param offender offender
	 * @return model and view to edit a military service term
	 */
	private ModelAndView prepareEditMav(final ModelMap map,
			final Offender offender, final MilitaryServiceTermForm form) {
		map.addAttribute(MILITARY_BRANCHES_MODEL_KEY,
				this.militaryServiceTermService.findAllMilitaryBranches());
		map.addAttribute(MILITARY_DISCHARGE_STATUSES_MODEL_KEY,
				this.militaryServiceTermService
				.findAllMilitaryDischargeStatus());
		this.offenderSummaryModelDelegate.add(map, offender);
		map.addAttribute(MILITARY_SERVICE_TERM_FORM_MODEL_KEY, form);
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		final Integer serviceTermNoteIndex;
		if (form.getServiceTermNoteItems() != null) {
			serviceTermNoteIndex = form.getServiceTermNoteItems().size(); 
		} else {
			serviceTermNoteIndex = 0;
		}
		map.addAttribute(SERVICE_TERM_NOTE_ITEM_INDEX_MODEL_KEY, 
				serviceTermNoteIndex);
		return new ModelAndView(EDIT_VIEW_NAME, map);
	}
	
	/*
	 * Populates the specified military service term form with values from the
	 * specified military service term.
	 * 
	 * @param form military service term form
	 * @param serviceTerm military service term
	 * @return populated military service term form
	 */
	private MilitaryServiceTermForm populateEditForm(
			final MilitaryServiceTermForm form, 
			final MilitaryServiceTerm serviceTerm) {
		form.setStartDate(serviceTerm.getDateRange().getStartDate());
		form.setEndDate(serviceTerm.getDateRange().getEndDate());
		form.setBranch(serviceTerm.getBranch());
		form.setDischargeStatus(serviceTerm.getDischargeStatus());
		return form;
	}
	
	/*
	 * Processes the specified list of military service term note items
	 * according to their specified operation values.
	 * 
	 * @param items military service term note items
	 * @param serviceTerm military service term
	 * @throws DuplicateEntityFoundException thrown when a duplicate military
	 * service term note is found
	 */
	private void processMilitaryServiceTermNoteItems(
			final List<MilitaryServiceTermNoteItem> items,
			final MilitaryServiceTerm serviceTerm)
		throws DuplicateEntityFoundException {
		if (items != null) {
			for (MilitaryServiceTermNoteItem item 
					: items) {
				if (MilitaryServiceTermNoteItemOperation.CREATE
						.equals(item.getOperation())) {
					this.militaryServiceTermService.createNote(serviceTerm, 
							item.getDate(), item.getNote());
				} else if (MilitaryServiceTermNoteItemOperation.UPDATE
						.equals(item.getOperation())) {
					if (this.isNoteChanged(
							item.getServiceTermNote(), item.getDate(), 
							item.getNote())) {
						this.militaryServiceTermService.updateNote(
								item.getServiceTermNote(), item.getDate(),
								item.getNote());
					}
				} else if (MilitaryServiceTermNoteItemOperation.REMOVE
						.equals(item.getOperation())) {
					this.militaryServiceTermService.removeNote(
							item.getServiceTermNote());
				}
			}
		}
	}
	
	/*
	 * Returns whether the military service term note has different
	 * values than the supplied value and date.
	 * 
	 * @param note military service term note
	 * @param date date
	 * @param value value
	 * @return whether note information is changed
	 */
	private boolean isNoteChanged(final MilitaryServiceTermNote note,
			final Date date, final String value) {
		if(!note.getNote().equals(value)) {
			return true;
		}
		if(!(note.getDate().getTime() == date.getTime())) {
			return true;
		}
		return false;
	}
	
	/**
	 * Sets up and registers property editors.
	 * 
	 * @param binder web binder
	 */
	@InitBinder
	protected void initBinder(final WebDataBinder binder) {
		binder.registerCustomEditor(
				Date.class,
				this.customDateEditorFactory
				.createCustomDateOnlyEditor(true));
		binder.registerCustomEditor(
				Offender.class,
				this.offenderPropertyEditorFactory
				.createOffenderPropertyEditor());
		binder.registerCustomEditor(
				MilitaryBranch.class,
				this.militaryBranchPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(
				MilitaryDischargeStatus.class,
				this.militaryDischargeStatusPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(
				MilitaryServiceTerm.class,
				this.militaryServiceTermPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(
				MilitaryServiceTermNote.class,
				this.militaryServiceTermNotePropertyEditorFactory
				.createPropertyEditor());
	}
}