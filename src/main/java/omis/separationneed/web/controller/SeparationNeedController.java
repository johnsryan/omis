/*
 * OMIS - Offender Management Information System.
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
package omis.separationneed.web.controller;

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
import omis.content.RequestContentMapping;
import omis.content.RequestContentType;
import omis.exception.DateConflictException;
import omis.exception.DuplicateEntityFoundException;
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.person.domain.Person;
import omis.relationship.domain.Relationship;
import omis.relationship.exception.ReflexiveRelationshipException;
import omis.report.ReportFormat;
import omis.report.ReportRunner;
import omis.report.web.controller.delegate.ReportControllerDelegate;
import omis.separationneed.domain.SeparationNeed;
import omis.separationneed.domain.SeparationNeedNote;
import omis.separationneed.domain.SeparationNeedReason;
import omis.separationneed.domain.SeparationNeedReasonAssociation;
import omis.separationneed.domain.SeparationNeedRemoval;
import omis.separationneed.domain.SeparationNeedRemovalReason;
import omis.separationneed.report.SeparationNeedReportService;
import omis.separationneed.report.SeparationNeedSummary;
import omis.separationneed.report.delegate.LegacyLocationLookupDelegate;
import omis.separationneed.service.SeparationNeedService;
import omis.separationneed.validator.SeparationNeedFormValidator;
import omis.separationneed.web.form.SeparationNeedForm;
import omis.separationneed.web.form.SeparationNeedNoteItem;
import omis.separationneed.web.form.SeparationNeedNoteItemOperation;
import omis.web.controller.delegate.BusinessExceptionHandlerDelegate;

/**
 * Controller for separation need.
 * 
 * @author Joel Norris
 * @author Yidong Li 
 * @version 0.1.0 (May, 1 2018)
 * @since OMIS 3.0
 */
@Controller
@RequestMapping("/separationNeed")
@PreAuthorize("hasRole('USER')")
public class SeparationNeedController {

	/* Redirect URLs */
	
	private static final String LIST_REDIRECT = "redirect:list.html?offender=";
	
	/* View names */
	
	private static final String LIST_VIEW_NAME = "separationNeed/list";
	
	private static final String EDIT_VIEW_NAME = "separationNeed/edit";
	
	private static final String SEPARATION_NEEDS_ACTION_MENU_VIEW_NAME 
		= "separationNeed/includes/separationNeedsActionMenu";
	
	private static final String SEPARATION_NEED_ACTION_MENU_VIEW_NAME
		= "separationNeed/includes/separationNeedActionMenu";
	
	private static final String SEPARATION_NEEDS_ROW_ACTION_MENU_VIEW_NAME
		= "separationNeed/includes/separationNeedsRowActionMenu";
	
	private static final String SEPARATION_NEED_REASON_ITEMS_MENU_VIEW_NAME
		= "separationNeed/includes/separationNeedReasonItemsActionMenu";
	
	private static final String SEPARATION_NEED_NOTE_ITEMS_MENU_VIEW_NAME
		= "separationNeed/includes/separationNeedNoteItemsActionMenu";
	
	private static final String SEPARATION_NEED_NOTE_ITEM_ROW_VIEW_NAME
		= "separationNeed/includes/separationNeedNoteItemTableRow";
	
	private static final String
	SEPARATION_NEED_NOTE_ITEM_ROW_ACTION_MENU_VIEW_NAME
		= "separationNeed/includes/separationNeedNoteItemRowActionMenu";
	
	/* Model Keys */
	
	private static final String OFFENDER_NUMBER_MODEL_KEY = "offenderNumber";
	
	private static final String OFFENDER_MODEL_KEY = "offender";
	
	private static final String SEPARATION_NEED_FORM_MODEL_KEY = 
			"separationNeedForm";
	
	private static final String SEPARATION_NEED_MODEL_KEY  = "separationNeed";
	
	private static final String REASONS_MODEL_KEY = "reasons";
	
	private static final String SEPARATION_NEED_NOTE_ITEM_INDEX_MODEL_KEY
		= "separationNeedNoteItemIndex";
	
	private static final String SEPARATION_NEED_NOTE_ITEM_MODEL_KEY
		= "separationNeedNoteItem";
	
	private static final String SEPARATION_NEED_REMOVAL_REASONS_MODEL_KEY
		= "removalReasons";
	
	private static final String SEPARATION_NEED_SUMMARIES_MODEL_KEY 
		= "separationNeedSummaries";
	
	/* Message keys */
	
	private static final String SEPARATION_NEED_DATE_CONFLICT_MESSAGE_KEY
		= "separationNeed.dateConflict";
	
	private static final String SEPARATION_NEED_OR_NOTE_EXISTS_MESSAGE_KEY
		= "separationNeed.needOrNoteExists";
	
	private static final String REFLECTIVE_RELATIONSHIP_MESSAGE_KEY
	= "separationNeed.personsEqual";
	
	/* Bundles */
	
	private static final String ERROR_BUNDLE_NAME
		= "omis.separationneed.msgs.form";
	
	/* Services */
	
	@Autowired
	private SeparationNeedService separationNeedService;
	
	@Autowired
	private CustomDateEditorFactory customDateEditorFactory;
	
	/* Report Services */
	
	@Autowired
	@Qualifier("separationNeedReportService")
	SeparationNeedReportService separationNeedReportService;
	
	/* Delegates */
	
	@Autowired
	@Qualifier("legacyLocationLookupDelegate")
	private LegacyLocationLookupDelegate legacyLocationLookupDelegate;
	
	/* Property editor factories */
	
	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory offenderPropertyEditorFactory;
	
	@Autowired
	@Qualifier("separationNeedPropertyEditorFactory")
	private PropertyEditorFactory separationNeedPropertyEditorFactory;
	
	@Autowired
	@Qualifier("separationNeedReasonPropertyEditorFactory")
	private PropertyEditorFactory separationNeedReasonPropertyEditorFactory;
	
	@Autowired
	@Qualifier("separationNeedReasonAssociationPropertyEditorFactory")
	private PropertyEditorFactory
	separationNeedReasonAssociationPropertyEditorFactory;
	
	@Autowired
	@Qualifier("separationNeedNotePropertyEditorFactory")
	private PropertyEditorFactory separationNeedNotePropertyEditorFactory;
	
	@Autowired
	@Qualifier("separationNeedRemovalReasonPropertyEditorFactory")
	private PropertyEditorFactory
	separationNeedRemovalReasonPropertyEditorFactory;
	
	@Autowired
	@Qualifier("personPropertyEditorFactory")
	private PropertyEditorFactory personPropertyEditorFactory;

	/* Report names. */
	
	private static final String SEPARATION_NEED_LISTING_REPORT_NAME 
		= "/Safety/SeparationNeeds/Separation_Needs_Listing";

	private static final String SEPARATION_NEED_DETAILS_REPORT_NAME 
		= "/Safety/SeparationNeeds/Separation_Needs_Details";

	/* Report parameter names. */
	
	private static final String SEPARATION_NEED_LISTING_ID_REPORT_PARAM_NAME 
		= "DOC_ID";

	private static final String SEPARATION_NEED_DETAILS_ID_REPORT_PARAM_NAME 
		= "SN_ID";
	
	/* Report runners. */
	
	@Autowired
	@Qualifier("reportRunner")
	private ReportRunner reportRunner;
	
	/* Controller delegates. */
	
	@Autowired
	@Qualifier("reportControllerDelegate")
	private ReportControllerDelegate reportControllerDelegate;

	/* Validators */
	
	@Autowired
	@Qualifier("separationNeedFormValidator")
	private SeparationNeedFormValidator separationNeedFormValidator;
	
	/* Helpers */
	
	@Autowired
	@Qualifier("businessExceptionHandlerDelegate")
	private BusinessExceptionHandlerDelegate businessExceptionHandlerDelegate;

	@Autowired
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;
	
	/**Instantiates a default instance of separation need controller. */
	public SeparationNeedController() {
		//Default constructor
	}
	
	/**
	 * Lists the separation needs for the specified offender.
	 * 
	 * @param offender offender
	 * @return the model and view
	 */
	@RequestContentMapping(nameKey = "separationNeedListScreenName",
		descriptionKey = "separationNeedListScreenDescription",
		messageBundle = "omis.separationneed.msgs.separationNeed",
		screenType = RequestContentType.LISTING_SCREEN)
	@RequestMapping(value = "/list.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('SEPARATION_NEED_LIST') or hasRole('ADMIN')")
	public ModelAndView list(@RequestParam(value = "offender", 
		required = true) final Offender offender) {
		ModelMap map = new ModelMap();
		List<SeparationNeedSummary> separationNeedSummaries =
				this.separationNeedReportService.summarizeByOffenderOnDate(
						offender, new Date());
		map.addAttribute(SEPARATION_NEED_SUMMARIES_MODEL_KEY,
				separationNeedSummaries);
		map.addAttribute(OFFENDER_NUMBER_MODEL_KEY, String.valueOf(offender
				.getOffenderNumber()));
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		this.offenderSummaryModelDelegate.add(map, offender);
		return new ModelAndView(LIST_VIEW_NAME, map);
	}
	
	/**
	 * Displays the separation need form for viewing/editing a 
	 * specified separation need.
	 * 
	 * @param separationNeed separation need
	 * @return the model and view
	 */
	@RequestContentMapping(nameKey = "separationNeedEditScreenName",
			descriptionKey = "separationNeedEditScreenDescription",
			messageBundle = "omis.separationneed.msgs.separationNeed",
			screenType = RequestContentType.DETAIL_SCREEN)
	@RequestMapping(value = "/edit.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('SEPARATION_NEED_VIEW') or hasRole('ADMIN')")
	public ModelAndView edit(
			@RequestParam(value = "separationNeed", required = true) 
				final SeparationNeed separationNeed,
			@RequestParam(value = "offender", required = true)
				final Offender offender) {
		ModelMap map = new ModelMap();
		SeparationNeedForm form = new SeparationNeedForm();
		this.prepareSeparationNeedEditForm(form, separationNeed, offender);
		map.addAttribute(SEPARATION_NEED_MODEL_KEY, separationNeed);
		return this.prepareEditMav(map, form, offender);
	}
	
	/**
	 * Submits the update to a separation need from information in the edit 
	 * separation need screen.
	 * @param form form
	 * @param separationNeed separation need
	 * @param result result
	 * @return the model and view
	 * @throws DateConflictException thrown when a separation need that overlaps
	 * another separation need's date range
	 * 
	 * @throws DuplicateEntityFoundException 
	 */
	@RequestContentMapping(nameKey = "separationNeedEditName",
			descriptionKey = "separationNeedEditDescription",
			messageBundle = "omis.separationneed.msgs.separationNeed",
			screenType = RequestContentType.FORM_SUBMIT)
	@RequestMapping(value = "/edit.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('SEPARATION_NEED_EDIT') or hasRole('ADMIN')")
	public ModelAndView update(
		@RequestParam(value = "separationNeed", required = true)
			final SeparationNeed separationNeed,
			@RequestParam(value = "offender", required = true)
			final Offender offender, final SeparationNeedForm form,
			final BindingResult result)
		throws DuplicateEntityFoundException, DateConflictException {
		this.separationNeedFormValidator.validate(form, result);
		if (result.hasErrors()) {
			ModelMap map = new ModelMap();
			map.addAttribute(BindingResult.MODEL_KEY_PREFIX 
					+ SEPARATION_NEED_FORM_MODEL_KEY, result);
			map.addAttribute(SEPARATION_NEED_MODEL_KEY, separationNeed);
			return prepareEditMav(map, form, offender);
		}
		this.separationNeedService.update(separationNeed,
				separationNeed.getRelationship(), form.getDate(),
				form.getCreationComment(), form.getReportingStaff(),
				form.getRemovalDate(), form.getRemovalReason(),
				form.getRemovalComment());
		this.processNoteItems(form.getSeparationNeedNoteItems(),
				separationNeed);
		this.processReasons(form.getSeparationNeedReasons(),
				separationNeed);
		Long offenderId = offender.getId();
		return new ModelAndView(LIST_REDIRECT + offenderId);
	}
	
	/**
	 * Displays the separation need form for the creation of a new
	 * separation need.
	 * 
	 * @param offender offender
	 * @return the model and view
	 */
	@RequestContentMapping(nameKey = "separationNeedCreateScreenName",
			descriptionKey = "separationNeedCreateScreenDescription",
			messageBundle = "omis.separationneed.msgs.separationNeed",
			screenType = RequestContentType.DETAIL_SCREEN)
	@RequestMapping(value = "/create.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('SEPARATION_NEED_CREATE') or hasRole('ADMIN')")
	public ModelAndView create(@RequestParam(value = "offender", 
		required = true) final Offender offender) {
		ModelMap map = new ModelMap();
		SeparationNeedForm form = new SeparationNeedForm();
		return this.prepareEditMav(map, form, offender);
	}
	
	/**
	 * Submits the creation of a new separation need for the specified offender.
	 * 
	 * @param form separation need form
	 * @param offender offender
	 * @param result binding result
	 * @return the model and view
	 * @throws DateConflictException thrown when a separation need exists
	 * between the specified form's start date and end date
	 * @throws DuplicateEntityFoundException  thrown when a duplicate separation
	 * need is found
	 * @throws ReflexiveRelationshipException 
	 */
	@RequestContentMapping(nameKey = "separationNeedSaveName",
			descriptionKey = "separationNeedSaveDescription",
			messageBundle = "omis.separationneed.msgs.separationNeed",
			screenType = RequestContentType.FORM_SUBMIT)
	@RequestMapping(value = "/create.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('SEPARATION_NEED_CREATE') or hasRole('ADMIN')")
	public ModelAndView save(@RequestParam(value = "offender",
			required = true) final Offender offender,
			final SeparationNeedForm form, final BindingResult result)
		throws DuplicateEntityFoundException, DateConflictException, 
		ReflexiveRelationshipException {
		this.separationNeedFormValidator.validate(form, result);
		if (result.hasErrors()) {
			ModelMap map = new ModelMap();
			map.addAttribute(BindingResult.MODEL_KEY_PREFIX 
					+ SEPARATION_NEED_FORM_MODEL_KEY, result);
			return prepareEditMav(map, form, offender);
		}
		Relationship relationship = this.separationNeedService.findRelationship(
				offender, form.getTargetOffender());
		if (relationship == null) {
			relationship = this.separationNeedService.createRelationship(
					offender, form.getTargetOffender());
		}
		SeparationNeed separationNeed = this.separationNeedService.create(
				relationship, form.getDate(), form.getCreationComment(),
				form.getReportingStaff(), form.getRemovalDate(),
				form.getRemovalReason(), form.getRemovalComment());
		this.processNoteItems(form.getSeparationNeedNoteItems(),
				separationNeed);
		this.processReasons(form.getSeparationNeedReasons(), separationNeed);
		Long offenderId = offender.getId();
		return new ModelAndView(LIST_REDIRECT + offenderId);
	}
	
	/**
	 * Removes the specified separation need.
	 * @param separationNeed separation need
	 * @return redirect string
	 */
	@RequestContentMapping(nameKey = "separationNeedRemoveName",
			descriptionKey = "separationNeedRemoveDescription",
			messageBundle = "omis.separationneed.msgs.separationNeed",
			screenType = RequestContentType.FORM_SUBMIT)
	@RequestMapping(value = "/remove.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('SEPARATION_NEED_REMOVE') or hasRole('ADMIN')")
	public String remove(@RequestParam(value = "separationNeed", 
		required = true) final SeparationNeed separationNeed) {
		Long offenderId = separationNeed.getRelationship().getFirstPerson()
				.getId();
		List<SeparationNeedReasonAssociation> associations 
			= this.separationNeedService
			.findReasonAssociationsBySeparationNeed(separationNeed);
		for(SeparationNeedReasonAssociation association : associations) {
			this.separationNeedService.removeAssociatedReason(association);
		}
		List<SeparationNeedNote> notes = this.separationNeedService
				.findNotesBySeparationNeed(separationNeed);
		for(SeparationNeedNote note : notes) {
			this.separationNeedService.removeNote(note);
		}
		this.separationNeedService.remove(separationNeed);
		return LIST_REDIRECT + offenderId;
	}
	
	/**
	 * Displays the action menu for separation needs.
	 * 
	 * @return model and view for separation needs action menu
	 */
	@RequestMapping(value = "separationNeedsActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView displaySeparationNeedsActionMenu(
			@RequestParam(value = "offender", required = true)
			final Offender offender) {
		ModelMap map = new ModelMap();
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		return new ModelAndView(SEPARATION_NEEDS_ACTION_MENU_VIEW_NAME, map);
	}
	
	/**
	 * Displays the action menu for separation need.
	 * 
	 * @return model and view for separation need action menu
	 */
	@RequestMapping(value = "separationNeedActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView displaySeparationNeedActionMenu(
			@RequestParam(value = "offender", required = true)
			final Offender offender) {
		ModelMap map = new ModelMap();
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		return new ModelAndView(SEPARATION_NEED_ACTION_MENU_VIEW_NAME, map);
	}
	
	/**
	 * Displays the separation needs row action menu for the specified
	 * separation need.
	 * 
	 * @param separationNeed separation need
	 * @return model and view for separation needs row action menu
	 */
	@RequestMapping(value = "separationNeedsRowActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView displaySeparationNeedsRowActionMenu(
			@RequestParam(value = "separationNeed", required = true)
				final SeparationNeed separationNeed,
			@RequestParam(value = "offender", required = true)
				final Offender offender) {
		ModelMap map = new ModelMap();
		map.addAttribute(SEPARATION_NEED_MODEL_KEY, separationNeed);
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		return new ModelAndView(SEPARATION_NEEDS_ROW_ACTION_MENU_VIEW_NAME,
				map);
	}
	
	/**
	 * Displays the separation need reason items action menu.
	 * 
	 * @return model and view for separation need reason items action menu
	 */
	@RequestMapping(value = "separationNeedReasonItemsActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView displaySeparationNeedReasonItemsActionMenu() {
		return new ModelAndView(SEPARATION_NEED_REASON_ITEMS_MENU_VIEW_NAME);
	}
	
	/**
	 * Displays the separation need note items action menu.
	 * 
	 * @return model and view for separation need note item action menu
	 */
	@RequestMapping(value = "separationNeedNoteItemsActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView displaySeparationNeedNoteItemsActionMenu(
			@RequestParam("separationNeedNoteItemIndex") final Integer index) {
		ModelMap map = new ModelMap();
		map.addAttribute(SEPARATION_NEED_NOTE_ITEM_INDEX_MODEL_KEY, index);
		return new ModelAndView(SEPARATION_NEED_NOTE_ITEMS_MENU_VIEW_NAME, map);
	}
	
	/**
	 * Displays the separation need note item row action menu.
	 * 
	 * @param separationNeedNote separation need note
	 * @return model and view for separation need note item row action menu
	 */
	@RequestMapping(value = "separationNeedNoteItemRowActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView displaySeparationNeedNoteItemRowActionMenu(
			@RequestParam(value = "separationNeedNote", required = true)
			final SeparationNeedNote separationNeedNote) {
		ModelMap map = new ModelMap();
		return new ModelAndView(
				SEPARATION_NEED_NOTE_ITEM_ROW_ACTION_MENU_VIEW_NAME, map);
	}
	
	/**
	 * Displays a new separation need note item row.
	 * 
	 * @param index separation need note item index
	 * @return model and view for separation need note item row
	 */
	@RequestMapping(value = "displaySeparationNeedNoteItemRow.html",
			method = RequestMethod.GET)
	public ModelAndView displaySeparationNeedNoteItemRow(
			@RequestParam(value = "separationNeedNoteItemIndex",
			required = true)final int index) {
		ModelMap map = new ModelMap();
		SeparationNeedNoteItem item = new SeparationNeedNoteItem();
		item.setOperation(SeparationNeedNoteItemOperation.CREATE);
		map.addAttribute(SEPARATION_NEED_NOTE_ITEM_MODEL_KEY, item);
		map.addAttribute(SEPARATION_NEED_NOTE_ITEM_INDEX_MODEL_KEY, index);
		return new ModelAndView(SEPARATION_NEED_NOTE_ITEM_ROW_VIEW_NAME, map);
	}
	
	/**
	 * Returns the report for the specified offenders separation needs.
	 * 
	 * @param offender offender
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/separationNeedListingReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('SEPARATION_NEED_VIEW') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportSeparationNeedListing(@RequestParam(
			value = "offender", required = true)
			final Offender offender,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(SEPARATION_NEED_LISTING_ID_REPORT_PARAM_NAME,
				Long.toString(offender.getOffenderNumber()));
		byte[] doc = this.reportRunner.runReport(
				SEPARATION_NEED_LISTING_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/**
	 * Returns the report for the specified separation need.
	 * 
	 * @param separationNeed separation need
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/separationNeedDetailsReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('SEPARATION_NEED_VIEW') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportSeparationNeedDetails(@RequestParam(
			value = "separationNeed", required = true)
			final SeparationNeed separationNeed,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(SEPARATION_NEED_DETAILS_ID_REPORT_PARAM_NAME,
				Long.toString(separationNeed.getId()));
		byte[] doc = this.reportRunner.runReport(
				SEPARATION_NEED_DETAILS_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/* Exception Handler methods. */
	
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
				SEPARATION_NEED_DATE_CONFLICT_MESSAGE_KEY, 
				ERROR_BUNDLE_NAME, exception);
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
				SEPARATION_NEED_OR_NOTE_EXISTS_MESSAGE_KEY, ERROR_BUNDLE_NAME, 
				exception);
	}
	
	/**
	 * Handles reflexive relationship exception.
	 * 
	 * @param exception reflexive relationship exception
	 * @return model and view for displaying exception explanation
	 */
	@ExceptionHandler(ReflexiveRelationshipException.class)
	public ModelAndView handleReflexiveRelationshipException(
			final ReflexiveRelationshipException exception) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
			REFLECTIVE_RELATIONSHIP_MESSAGE_KEY, ERROR_BUNDLE_NAME, 
			exception);
	}
	
	/* Helper methods. */
	
	/*
	 * Prepares the fields of the separation need form 
	 * with the values of the specified separation need.
	 */
	private void prepareSeparationNeedEditForm(final SeparationNeedForm form, 
		final SeparationNeed separationNeed, final Offender offender) {
		List<SeparationNeedReasonAssociation> reasonAssociations
			= this.separationNeedService
			.findReasonAssociationsBySeparationNeed(separationNeed);
		List<SeparationNeedReason> formReasons
			= new ArrayList<SeparationNeedReason>();
		for(SeparationNeedReasonAssociation association: reasonAssociations) {
			formReasons.add(association.getReason());
		}
		form.setSeparationNeedReasons(formReasons);
		List<SeparationNeedNoteItem> noteItems 
			= new ArrayList<SeparationNeedNoteItem>();
		List<SeparationNeedNote> notes = this.separationNeedService
				.findNotesBySeparationNeed(separationNeed);
		for (SeparationNeedNote note : notes) {
			SeparationNeedNoteItem noteItem = new SeparationNeedNoteItem(
					note.getValue(), note.getDate(), note,
					SeparationNeedNoteItemOperation.UPDATE);
			noteItems.add(noteItem);
		}
		form.setSeparationNeedNoteItems(noteItems);
		form.setDate(separationNeed.getDate());
		form.setReportingStaff(separationNeed.getReportingStaff());
		form.setCreationComment(separationNeed.getCreationComment());
		SeparationNeedRemoval removal = separationNeed.getRemoval();
		if (removal != null) {
			form.setRemovalDate(removal.getDate());
			form.setRemovalReason(removal.getReason());
			form.setRemovalComment(removal.getComment());
		}
		if(offender.equals(separationNeed.getRelationship().getSecondPerson())){
			form.setTargetOffender(((Offender)separationNeed.getRelationship()
			.getFirstPerson()));
		} else {
			form.setTargetOffender(((Offender)separationNeed.getRelationship()
			.getSecondPerson()));
		}
	}
	
	/*
	 * Prepares a model and view with values that it needs to present the edit
	 * view.
	 */
	private ModelAndView prepareEditMav(final ModelMap map,
			final SeparationNeedForm form, final Offender offender) {
		map.addAttribute(REASONS_MODEL_KEY, this.separationNeedService
				.findReasons());
		map.addAttribute(SEPARATION_NEED_FORM_MODEL_KEY, form);
		int noteItemIndex = form.getSeparationNeedNoteItems().size();
		map.addAttribute(SEPARATION_NEED_NOTE_ITEM_INDEX_MODEL_KEY,
				noteItemIndex);
		map.addAttribute(SEPARATION_NEED_REMOVAL_REASONS_MODEL_KEY,
				this.separationNeedService.findRemovalReasons());
		this.offenderSummaryModelDelegate.add(map, offender);
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		return new ModelAndView(EDIT_VIEW_NAME, map);
	}

	/*
	 * Processes the specified list of separation need note items according to
	 * their operation.
	 * 
	 * @param noteItems list of separation need note items
	 * @param separationNeed separation need
	 * @throws DuplicateEntityFoundException thrown when a duplicate separation
	 * need note item is found.
	 */
	private void processNoteItems(final List<SeparationNeedNoteItem> noteItems,
			final SeparationNeed separationNeed)
		throws DuplicateEntityFoundException {
		for (SeparationNeedNoteItem noteItem : noteItems) {
			if (SeparationNeedNoteItemOperation.CREATE.equals(
					noteItem.getOperation())) {
				this.separationNeedService.addNote(separationNeed,
						noteItem.getDate(), noteItem.getNote());
			} else if (SeparationNeedNoteItemOperation.UPDATE.equals(
					noteItem.getOperation())) {
				if (this.isNoteChanged(noteItem.getSeparationNeedNote(),
						noteItem.getDate(), noteItem.getNote())) {
					this.separationNeedService.updateNote(
							noteItem.getSeparationNeedNote(),
							noteItem.getDate(), noteItem.getNote());
				}
			} else if (SeparationNeedNoteItemOperation.REMOVE.equals(
					noteItem.getOperation())) {
				this.separationNeedService.removeNote(
						noteItem.getSeparationNeedNote());
			}
		}
	}
	
	/*
	 * Returns whether the note has different values than the supplied date
	 * and value.
	 * 
	 * @param note separation need note
	 * @param date date
	 * @param value value
	 * @return whether note information is changes
	 */
	private boolean isNoteChanged(final SeparationNeedNote note,
			final Date date, final String value) {
		if(!note.getValue().equals(value)) {
			return true;
		}
		if(!note.getDate().equals(date)) {
			return true;
		}
		return false;
	}
	
	/*
	 * Processes the specified list of separation need reason items according
	 * to their operation.
	 * 
	 * @param reasonItems separation need reason items
	 * @param separationNeed separation need
	 * @throws DuplicateEntityFoundException thrown when a duplicate separation
	 * need reason association is found
	 */
	private void processReasons(
			final List<SeparationNeedReason> reasons,
			final SeparationNeed separationNeed)
		throws DuplicateEntityFoundException {
		List<SeparationNeedReasonAssociation> reasonAssociations = 
				this.separationNeedService
				.findReasonAssociationsBySeparationNeed(separationNeed);
		for (SeparationNeedReason reason: reasons) {
			boolean found = false;
			for (SeparationNeedReasonAssociation association 
					: reasonAssociations) {
				if (reason.equals(association.getReason())) {
					found = true;
				}
			}
			if (!found) {
				SeparationNeedReasonAssociation newAssociation = 
						this.separationNeedService.associateReason(
						separationNeed, reason);
				reasonAssociations.add(newAssociation);
			}
		}
		for (SeparationNeedReasonAssociation association : reasonAssociations) {
			boolean found = false;
			for (SeparationNeedReason reason : reasons) {
				if (association.getReason().equals(reason)) {
					found = true;
				}	
			}
			if (!found) {
				this.separationNeedService
				.removeAssociatedReason(association);
			}
		}
	}
	
	/**
	 * Sets up and registers property editors.
	 * 
	 * @param binder web binder
	 */
	@InitBinder
	protected void initBinder(final WebDataBinder binder) {
		binder.registerCustomEditor(
				SeparationNeed.class, 
				this.separationNeedPropertyEditorFactory
				.createPropertyEditor());
		
		binder.registerCustomEditor(
				SeparationNeedReason.class, 
				this.separationNeedReasonPropertyEditorFactory
				.createPropertyEditor());
		
		binder.registerCustomEditor(
				Offender.class,
				this.offenderPropertyEditorFactory
				.createOffenderPropertyEditor());
		
		binder.registerCustomEditor(
				Date.class,
				this.customDateEditorFactory
				.createCustomDateOnlyEditor(true));
		
		binder.registerCustomEditor(
				SeparationNeedReasonAssociation.class, 
				this.separationNeedReasonAssociationPropertyEditorFactory
				.createPropertyEditor());
		
		binder.registerCustomEditor(
				SeparationNeedNote.class,
				this.separationNeedNotePropertyEditorFactory
				.createPropertyEditor());
		
		binder.registerCustomEditor(
				SeparationNeedRemovalReason.class,
				this.separationNeedRemovalReasonPropertyEditorFactory
				.createPropertyEditor());
		
		binder.registerCustomEditor(
				Person.class,
				this.personPropertyEditorFactory
				.createPropertyEditor());
	}			
}