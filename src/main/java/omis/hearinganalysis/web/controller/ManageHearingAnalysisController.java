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
package omis.hearinganalysis.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import omis.boardhearing.domain.BoardHearing;
import omis.boardhearing.service.BoardHearingService;
import omis.exception.DuplicateEntityFoundException;
import omis.hearinganalysis.domain.HearingAnalysis;
import omis.hearinganalysis.domain.HearingAnalysisCategory;
import omis.hearinganalysis.domain.HearingAnalysisNote;
import omis.hearinganalysis.service.HearingAnalysisService;
import omis.hearinganalysis.web.form.HearingAnalysisForm;
import omis.hearinganalysis.web.form.HearingAnalysisNoteItem;
import omis.hearinganalysis.web.form.HearingAnalysisNoteItemOperation;
import omis.hearinganalysis.web.validator.HearingAnalysisFormValidator;
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.paroleboarditinerary.domain.BoardAttendee;
import omis.paroleboarditinerary.domain.ParoleBoardItinerary;
import omis.paroleeligibility.domain.ParoleEligibility;
import omis.paroleeligibility.report.ParoleEligibilityReportService;
import omis.util.DateManipulator;
import omis.web.controller.delegate.BusinessExceptionHandlerDelegate;

/**
 * Controller for managing hearing analyses.
 *
 * @author Josh Divine
 * @author Annie Wahl
 * @version 0.1.3 (May 29, 2018)
 * @since OMIS 3.0
 */
@Controller
@PreAuthorize("hasRole('USER')")
@RequestMapping("/hearingAnalysis")
public class ManageHearingAnalysisController {

	/* View names. */
	
	private static final String VIEW_NAME = "hearingAnalysis/edit";

	/* Table Rows. */
	
	private static final String HEARING_ANALYSIS_NOTE_TABLE_ROW_VIEW_NAME =
			"hearingAnalysis/includes/hearingAnalysisNoteTableRow";
	
	/* Partial Views. */
	
	private static final String BOARD_ATTENDEE_OPTIONS_VIEW_NAME = 
			"hearingAnalysis/includes/boardAttendeeOptions";
	
	/* Action menus. */

	private static final String ACTION_MENU_VIEW_NAME = 
			"hearingAnalysis/includes/hearingAnalysisActionMenu";
	
	
	private static final String HEARING_ANALYSIS_NOTES_ACTION_MENU_VIEW_NAME = 
			"hearingAnalysis/includes/hearingAnalysisNotesActionMenu";
	
	/* Redirects. */
	
	private static final String REDIRECT_URL = 
			"redirect:/paroleEligibility/list.html?offender=%d";
	
	/* Model keys. */
	
	private static final String HEARING_ANALYSIS_FORM_MODEL_KEY = 
			"hearingAnalysisForm";
	
	private static final String HEARING_ANALYSIS_MODEL_KEY = "hearingAnalysis";
	
	private static final String HEARING_ANALYSIS_NOTE_INDEX_MODEL_KEY = 
			"hearingAnalysisNoteIndex";
	
	private static final String ITEM_OPERATION_MODEL_KEY = "operation";
	
	private static final String BOARD_ATTENDEES_MODEL_KEY = "boardAttendees";

	private static final String ELIGIBILITY_MODEL_KEY = "eligibility";
	
	private static final String ITINERARIES_MODEL_KEY = "itineraries";
	
	private static final String HEARING_ANALYSIS_CATEGORIES_MODEL_KEY = 
			"categories";
	
	private static final String BOARD_HEARING_MODEL_KEY = "boardHearing";
	
	private static final String OFFENDER_MODEL_KEY = "offender";
	
	private static final String PAROLE_ELIGIBILITY_SUMMARY_MODEL_KEY =
			"eligibilitySummary";

	/* Message keys. */


	private static final String HEARING_ANALYSIS_EXISTS_MESSAGE_KEY = 
			"hearingAnalysis.exists";
	
	/* Bundles */
	
	private static final String ERROR_BUNDLE_NAME = 
			"omis.hearinganalysis.msgs.form";
	
	/* Services. */
	
	@Autowired
	@Qualifier("hearingAnalysisService")
	private HearingAnalysisService hearingAnalysisService;
	
	@Autowired
	@Qualifier("boardHearingService")
	private BoardHearingService boardHearingService;
	
	@Autowired
	@Qualifier("paroleEligibilityReportService")
	private ParoleEligibilityReportService paroleEligibilityReportService;
	
	/* Property editor factories. */
	
	@Autowired
	@Qualifier("hearingAnalysisPropertyEditorFactory")
	private PropertyEditorFactory hearingAnalysisPropertyEditorFactory;
	
	@Autowired
	@Qualifier("hearingAnalysisNotePropertyEditorFactory")
	private PropertyEditorFactory hearingAnalysisNotePropertyEditorFactory;

	@Autowired
	@Qualifier("paroleEligibilityPropertyEditorFactory")
	private PropertyEditorFactory  paroleEligibilityPropertyEditorFactory;
	
	@Autowired
	@Qualifier("boardAttendeePropertyEditorFactory")
	private PropertyEditorFactory boardAttendeePropertyEditorFactory;
	
	@Autowired
	@Qualifier("paroleBoardItineraryPropertyEditorFactory")
	private PropertyEditorFactory paroleBoardItineraryPropertyEditorFactory;
	
	@Autowired
	@Qualifier("hearingAnalysisCategoryPropertyEditorFactory")
	private PropertyEditorFactory hearingAnalysisCategoryPropertyEditorFactory;
	
	@Autowired
	@Qualifier("datePropertyEditorFactory")
	private CustomDateEditorFactory customDateEditorFactory;
	
	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory offenderPropertyEditorFactory;
	
	/* Validators. */

	@Autowired
	@Qualifier("hearingAnalysisFormValidator")
	private HearingAnalysisFormValidator hearingAnalysisFormValidator;
	
	/* Helpers. */

	@Autowired
	@Qualifier("businessExceptionHandlerDelegate")
	private BusinessExceptionHandlerDelegate businessExceptionHandlerDelegate;
	
	@Autowired
	@Qualifier("offenderSummaryModelDelegate")
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;

	/* Constructors. */
	
	/** Instantiates controller for hearing analyses. */
	public ManageHearingAnalysisController() {
		// Default instantiation
	}
	
	/* URL invokable methods. */
	
	/**
	 * Shows screen to edit a hearing analysis.
	 * 
	 * @param eligibility parole eligibility
	 * @return screen to edit a hearing analysis
	 */
	@PreAuthorize("hasRole('HEARING_ANALYSIS_VIEW') or hasRole('ADMIN')")
	@RequestMapping(value = "/edit.html", method = RequestMethod.GET)
	public ModelAndView edit(
			@RequestParam(value = "eligibility", required = true)
				final ParoleEligibility eligibility) {
		HearingAnalysis hearingAnalysis = this.hearingAnalysisService
				.findHearingAnalysisByParoleEligibility(eligibility);
		HearingAnalysisForm hearingAnalysisForm = new HearingAnalysisForm();
		if (hearingAnalysis != null) {
			hearingAnalysisForm.setCategory(hearingAnalysis.getCategory());
			hearingAnalysisForm.setAnalyst(hearingAnalysis.getAnalyst());
			hearingAnalysisForm.setBoardItinerary(hearingAnalysis
					.getParoleBoardItinerary());
			hearingAnalysisForm.setDueDate(hearingAnalysis
					.getExpectedCompletionDate());
			List<HearingAnalysisNote> notes = this.hearingAnalysisService
					.findHearingAnalysisNotesByHearingAnalysis(hearingAnalysis);
			List<HearingAnalysisNoteItem> noteItems = 
					new ArrayList<HearingAnalysisNoteItem>();
			for (HearingAnalysisNote note : notes) {
				HearingAnalysisNoteItem noteItem = new HearingAnalysisNoteItem();
				noteItem.setHearingAnalysisNote(note);
				noteItem.setDate(note.getDate());
				noteItem.setValue(note.getDescription());
				noteItem.setOperation(HearingAnalysisNoteItemOperation.EDIT);
				noteItems.add(noteItem);
			}
			hearingAnalysisForm.setHearingAnalysisNoteItems(noteItems);
		}
		ModelAndView mav = this.prepareMav(hearingAnalysisForm, eligibility);
		mav.addObject(HEARING_ANALYSIS_MODEL_KEY, hearingAnalysis);
		return mav;
	}
	
	/**
	 * Updates a hearing analysis.
	 * 
	 * @param hearingAnalysis hearing analysis
	 * @param hearingAnalysisForm hearing analysis form
	 * @param bindingResult binding result
	 * @return redirect to hearing analysis listing screen
	 * @throws DuplicateEntityFoundException if duplicate entity exists 
	 */
	@PreAuthorize("hasRole('HEARING_ANALYSIS_EDIT') or hasRole('ADMIN')")
	@RequestMapping(value = "/edit.html", method = RequestMethod.POST)
	public ModelAndView update(
			@RequestParam(value = "eligibility", required = true)
				final ParoleEligibility eligibility,
			final HearingAnalysisForm hearingAnalysisForm,
			final BindingResult bindingResult) 
					throws DuplicateEntityFoundException {
		this.hearingAnalysisFormValidator.validate(
				hearingAnalysisForm, bindingResult);
		if (bindingResult.hasErrors()) {
			ModelAndView mav = this.prepareRedisplay(hearingAnalysisForm, 
					eligibility, bindingResult);
			return mav;
		}
		HearingAnalysis hearingAnalysis = this.hearingAnalysisService
				.findHearingAnalysisByParoleEligibility(eligibility);
		if (hearingAnalysis == null) {
			hearingAnalysis = this.hearingAnalysisService.createHearingAnalysis(
					eligibility, hearingAnalysisForm.getBoardItinerary(),
					hearingAnalysisForm.getAnalyst(),
					hearingAnalysisForm.getCategory(),
					hearingAnalysisForm.getDueDate());
		} else {
			hearingAnalysis = this.hearingAnalysisService.updateHearingAnalysis(
					hearingAnalysis, hearingAnalysisForm.getBoardItinerary(),
					hearingAnalysisForm.getAnalyst(),
					hearingAnalysisForm.getCategory(),
					hearingAnalysisForm.getDueDate());
		}
		processHearingAnalysisNoteItems(hearingAnalysis,
				hearingAnalysisForm.getHearingAnalysisNoteItems());
		
		return new ModelAndView(String.format(REDIRECT_URL, 
				eligibility.getOffender().getId()));
	}
	
	/**
	 * Removes a hearing analysis.
	 * 
	 * @param hearingAnalysis hearing analysis
	 * @return redirect to hearing analysis listing screen
	 */
	@PreAuthorize("hasRole('HEARING_ANALYSIS_REMOVE') or hasRole('ADMIN')")
	@RequestMapping(value = "/remove.html", method = RequestMethod.GET)
	public ModelAndView remove(
			@RequestParam(value = "hearingAnalysis", required = true)
				final HearingAnalysis hearingAnalysis) {
		Long offenderId = hearingAnalysis.getEligibility().getOffender().getId();
		this.hearingAnalysisService.removeHearingAnalysis(hearingAnalysis);
		return new ModelAndView(String.format(REDIRECT_URL, offenderId)); 
	}
	
	/* Invokable AJAX calls. */
	
	/**
	 * Displays table row of hearing analysis note inputs.
	 * 
	 * @param hearingAnalysisNoteIndex index of hearing analysis note for 
	 * which to display row of inputs
	 * @return table row of hearing analysis note inputs
	 */
	@RequestMapping(value = "/addHearingAnalysisNote.html", 
			method = RequestMethod.GET)
	public ModelAndView addHearingAnalysisNote(
			@RequestParam(value = "hearingAnalysisNoteIndex", required = true)
				final int hearingAnalysisNoteIndex) {
		ModelAndView mav = new ModelAndView(
				HEARING_ANALYSIS_NOTE_TABLE_ROW_VIEW_NAME);
		mav.addObject(HEARING_ANALYSIS_NOTE_INDEX_MODEL_KEY, 
				hearingAnalysisNoteIndex);
		mav.addObject(ITEM_OPERATION_MODEL_KEY, 
				HearingAnalysisNoteItemOperation.CREATE);
		return mav;
	}
	
	/**
	 * Displays board attendee options for the specified parole board itinerary.
	 * 
	 * @param itinerary parole board itinerary
	 * @return board attendee options
	 */
	@RequestMapping(value = "/findBoardAttendeesForItinerary.html", 
			method = RequestMethod.GET)
	public ModelAndView findBoardAttendeesForItinerary(
			@RequestParam(value = "itinerary", required = true) 
				final ParoleBoardItinerary itinerary) {
		ModelAndView mav = new ModelAndView(
				BOARD_ATTENDEE_OPTIONS_VIEW_NAME);
		mav.addObject(BOARD_ATTENDEES_MODEL_KEY, 
				this.hearingAnalysisService.findBoardAttendeesByItinerary(
						itinerary));
		return mav;
	}
	
	/* Action menus. */
	
	/**
	 * Displays action menu for screen to create/edit a hearing analysis.
	 * 
	 * @param paroleEligibility - Parole Eligibility
	 * @return action menu for screen to create/edit a hearing analysis
	 */
	@RequestMapping(value = "/hearingAnalysisActionMenu.html", 
			method = RequestMethod.GET)
	public ModelAndView showActionMenu(
			@RequestParam(value = "paroleEligibility", required = true)
				final ParoleEligibility paroleEligibility) {
		ModelAndView mav = new ModelAndView(ACTION_MENU_VIEW_NAME);
		HearingAnalysis hearingAnalysis = this.hearingAnalysisService
				.findHearingAnalysisByParoleEligibility(paroleEligibility);
		BoardHearing boardHearing = this.boardHearingService
				.findBoardHearingByParoleEligibility(paroleEligibility);
		mav.addObject(OFFENDER_MODEL_KEY, paroleEligibility.getOffender());
		mav.addObject(HEARING_ANALYSIS_MODEL_KEY, hearingAnalysis);
		mav.addObject(BOARD_HEARING_MODEL_KEY, boardHearing);
		mav.addObject(ELIGIBILITY_MODEL_KEY, paroleEligibility);
		return mav;
	}

	/** 
	 * Parole board itinerary note action menu.
	 * 
	 * @param hearingAnalysisNoteIndex hearing analysis note index.
	 * @return action menu. 
	 */
	@RequestMapping(value = "/hearingAnalysisNotesActionMenu.html", 
		method = RequestMethod.GET)
	public ModelAndView showHearingAnalysisNotesActionMenu(
			@RequestParam("hearingAnalysisNoteIndex") 
				final Long hearingAnalysisNoteIndex) {
		final ModelMap modelMap = new ModelMap();
		modelMap.put(HEARING_ANALYSIS_NOTE_INDEX_MODEL_KEY, 
				hearingAnalysisNoteIndex);
		return new ModelAndView(HEARING_ANALYSIS_NOTES_ACTION_MENU_VIEW_NAME, 
				modelMap);
	}
	
	/* Exception handler methods. */
	
	/**
	 * Handles duplicate entity exists exceptions.
	 * 
	 * @param exception duplicate entity exists exception
	 * @return model and view for displaying exception explanation
	 */
	@ExceptionHandler(DuplicateEntityFoundException.class)
	public ModelAndView handleHearingAnalysisExistsException(
			final DuplicateEntityFoundException exception) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				HEARING_ANALYSIS_EXISTS_MESSAGE_KEY, ERROR_BUNDLE_NAME, 
				exception);
	}
	
	/* Helper methods. */
	
	// Prepares model and view
	private ModelAndView prepareMav(
			final HearingAnalysisForm hearingAnalysisForm,
			final ParoleEligibility eligibility) {
		ModelAndView mav = new ModelAndView(VIEW_NAME);
		mav.addObject(HEARING_ANALYSIS_FORM_MODEL_KEY, hearingAnalysisForm);
		mav.addObject(ELIGIBILITY_MODEL_KEY, eligibility);
		DateManipulator dateManipulator = new DateManipulator(new Date());
		dateManipulator.setToEarliestTimeInDay();
		mav.addObject(ITINERARIES_MODEL_KEY, this.boardHearingService
				.findItinerariesByEffectiveDate(dateManipulator.getDate()));
		if (hearingAnalysisForm.getBoardItinerary() != null) {
			mav.addObject(BOARD_ATTENDEES_MODEL_KEY, this.hearingAnalysisService
					.findBoardAttendeesByItinerary(
							hearingAnalysisForm.getBoardItinerary()));
		}
		int boardHearingAnalysisNoteIndex = 0;
		if (hearingAnalysisForm.getHearingAnalysisNoteItems() != null) {
			boardHearingAnalysisNoteIndex = hearingAnalysisForm
					.getHearingAnalysisNoteItems().size();
		}
		mav.addObject(HEARING_ANALYSIS_NOTE_INDEX_MODEL_KEY, 
				boardHearingAnalysisNoteIndex);
		this.offenderSummaryModelDelegate.add(mav.getModelMap(), 
				eligibility.getOffender());
		mav.addObject(HEARING_ANALYSIS_CATEGORIES_MODEL_KEY, 
				this.hearingAnalysisService.findHearingAnalysisCategories());
		mav.addObject(PAROLE_ELIGIBILITY_SUMMARY_MODEL_KEY,
				this.paroleEligibilityReportService.summarizeParoleEligibility(
						eligibility));
		return mav;
	}
	
	// Prepares redisplay model and view
	private ModelAndView prepareRedisplay(
			final HearingAnalysisForm hearingAnalysisForm,
			final ParoleEligibility eligibility,
			final BindingResult bindingResult) {
		ModelAndView mav = this.prepareMav(hearingAnalysisForm, eligibility);
		mav.addObject(BindingResult.MODEL_KEY_PREFIX
					+ HEARING_ANALYSIS_FORM_MODEL_KEY, bindingResult);
		return mav;
	}
	
	// Process hearing analysis notes
	private void processHearingAnalysisNoteItems(
			final HearingAnalysis hearingAnalysis,
			final List<HearingAnalysisNoteItem> hearingAnalysisNoteItems) 
					throws DuplicateEntityFoundException {
		if (hearingAnalysisNoteItems != null) {
			for (HearingAnalysisNoteItem noteItem : hearingAnalysisNoteItems) {
				if (HearingAnalysisNoteItemOperation.CREATE.equals(
						noteItem.getOperation())) {
					this.hearingAnalysisService.createHearingAnalysisNote(
							hearingAnalysis, noteItem.getValue(), 
							noteItem.getDate());
				} else if (HearingAnalysisNoteItemOperation.EDIT.equals(
						noteItem.getOperation())) {
					this.hearingAnalysisService.updateHearingAnalysisNote(
							noteItem.getHearingAnalysisNote(), 
							noteItem.getValue(), noteItem.getDate());
				} else if (HearingAnalysisNoteItemOperation.REMOVE.equals(
						noteItem.getOperation())) {
					this.hearingAnalysisService.removeHearingAnalysisNote(
							noteItem.getHearingAnalysisNote());
				}
			}
		}
	}
	
	/* Init binders. */
	
	/**
	 * Registers custom editors.
	 * 
	 * @param binder web binder
	 */
	@InitBinder
	protected void registerCustomEditors(final WebDataBinder binder) {
		binder.registerCustomEditor(HearingAnalysis.class,
				this.hearingAnalysisPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(ParoleEligibility.class,
				this.paroleEligibilityPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(HearingAnalysisNote.class,
				this.hearingAnalysisNotePropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(BoardAttendee.class,
				this.boardAttendeePropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(ParoleBoardItinerary.class,
				this.paroleBoardItineraryPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(HearingAnalysisCategory.class,
				this.hearingAnalysisCategoryPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(Date.class,
				this.customDateEditorFactory.createCustomDateOnlyEditor(true));
		binder.registerCustomEditor(Offender.class, 
				this.offenderPropertyEditorFactory
				.createOffenderPropertyEditor());
	}
}