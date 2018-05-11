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
package omis.paroleeligibility.web.controller;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import omis.beans.factory.PropertyEditorFactory;
import omis.beans.factory.spring.CustomDateEditorFactory;
import omis.exception.DuplicateEntityFoundException;
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.paroleeligibility.domain.AppearanceCategory;
import omis.paroleeligibility.domain.EligibilityStatusCategory;
import omis.paroleeligibility.domain.EligibilityStatusReason;
import omis.paroleeligibility.domain.ParoleEligibility;
import omis.paroleeligibility.domain.ParoleEligibilityNote;
import omis.paroleeligibility.report.ParoleEligibilityReportService;
import omis.paroleeligibility.service.ParoleEligibilityService;
import omis.paroleeligibility.web.form.ParoleEligibilityForm;
import omis.paroleeligibility.web.form.ParoleEligibilityNoteItem;
import omis.paroleeligibility.web.form.ParoleEligibilityNoteItemOperation;
import omis.paroleeligibility.web.validator.ParoleEligibilityFormValidator;

/**
 * Controller for managing parole eligibilities.
 *
 * @author Trevor Isles
 * @version 0.1.0 (Dec 7, 2017)
 * @since OMIS 3.0
 */
@Controller
@PreAuthorize("hasRole('USER')")
@RequestMapping("/paroleEligibility")
public class ManageParoleEligibilityController {
	
	/* View names. */
	
	private static final String EDIT_VIEW_NAME = "/paroleEligibility/edit";
	
	private static final String PAROLE_ELIGIBILITY_NOTE_ITEM_ROW_VIEW_NAME 
		= "/paroleEligibility/includes/paroleEligibilityNoteItemTableRow";
	
	private static final String PAROLE_ELIGIBILITY_ACTION_MENU_VIEW_NAME 
		= "/paroleEligibility/includes/paroleEligibilityActionMenu";
	
	private static final String
		PAROLE_ELIGIBILITY_NOTE_ITEMS_ACTION_MENU_VIEW_NAME 
		= "/paroleEligibility/includes/paroleEligibilityNoteItemsActionMenu";
	
	/* Redirects. */
	
	private static final String LIST_REDIRECT 
		= "redirect:/paroleEligibility/list.html?offender=%d";
	
	/* Model keys. */
	
	private static final String PAROLE_ELIGIBILITY_FORM_MODEL_KEY 
		= "paroleEligibilityForm";
	
	private static final String PAROLE_ELIGIBILITY_MODEL_KEY 
		= "paroleEligibility";
	
	private static final String PAROLE_ELIGIBILITY_NOTE_ITEM_MODEL_KEY 
		= "paroleEligibilityNoteItem";
	
	private static final String PAROLE_ELIGIBILITY_NOTE_ITEM_INDEX_MODEL_KEY 
		= "paroleEligibilityNoteItemIndex";
	
	private static final String OFFENDER_MODEL_KEY = "offender";
	
	private static final String APPEARANCE_CATEGORIES_MODEL_KEY 
		= "appearanceCategories";
	
	private static final String STATUS_REASONS_MODEL_KEY = "statusReasons";
	
	private static final String ELIGIBILITY_STATUSES_MODEL_KEY 
		= "eligibilityStatuses";
	
	private static final String BOARD_HEARING_MODEL_KEY = "boardHearing";
	
	/* Message keys. */
	
	/* Services. */
	
	@Autowired
	@Qualifier("paroleEligibilityService")
	private ParoleEligibilityService paroleEligibilityService;

	@Autowired
	@Qualifier("paroleEligibilityReportService")
	private ParoleEligibilityReportService paroleEligibilityReportService;
	
	/* Helpers. */
	
	@Autowired
	@Qualifier("offenderSummaryModelDelegate")
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;
	
	/* Property editor factories. */
	
	@Autowired
	@Qualifier("paroleEligibilityPropertyEditorFactory")
	private PropertyEditorFactory paroleEligibilityPropertyEditorFactory;
	
	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory offenderPropertyEditorFactory;
	
	@Autowired
	@Qualifier("datePropertyEditorFactory")
	private CustomDateEditorFactory datePropertyEditorFactory;
	
	@Autowired
	@Qualifier("paroleEligibilityNotePropertyEditorFactory")
	private PropertyEditorFactory paroleEligibilityNotePropertyEditorFactory;
	
	@Autowired
	@Qualifier("appearanceCategoryPropertyEditorFactory")
	private PropertyEditorFactory appearanceCategoryPropertyEditorFactory;
	
	@Autowired
	@Qualifier("eligibilityStatusReasonPropertyEditorFactory")
	private PropertyEditorFactory eligibilityStatusReasonPropertyEditorFactory;
	
	/* Validators. */
	
	@Autowired
	@Qualifier("paroleEligibilityFormValidator")
	private ParoleEligibilityFormValidator paroleEligibilityFormValidator;
	
	/* Constructors. */
	
	/** Instantiates controller for parole eligibilities. */
	public ManageParoleEligibilityController() {
		// Default instantiation
	}
	
	/* URL invokable methods. */
	
	/**
	 * Displays form to create a new parole eligibility for offender.
	 * 
	 * @param offender offender
	 * @return screen to display form to create new parole eligibility for
	 * offender
	 */
	@PreAuthorize("hasRole('PAROLE_ELIGIBILITY_CREATE') or hasRole('ADMIN')")
	@RequestMapping(value = "/create.html", method = RequestMethod.GET)
	public ModelAndView create(
			@RequestParam(value = "offender", required = true)
			final Offender offender) {
		return prepareEditMav(offender, new ParoleEligibilityForm());
	}
	
	/**
	 * Saves a new parole eligibility for the offender.
	 * 
	 * @param offender offender
	 * @param paroleEligibilityForm form for the parole eligibility
	 * @param result bind result
	 * @return redirect to list parole eligibility by offender
	 * @throws DuplicateEntityFoundException thrown when a duplicate parole
	 * eligibility is found
	 */
	@PreAuthorize("hasRole('PAROLE_ELIGIBILITY_CREATE') or hasRole('ADMIN')")
	@RequestMapping(value = "/create.html", method = RequestMethod.POST)
	public ModelAndView save(
			@RequestParam(value = "offender", required = true)
			final Offender offender, 
			final ParoleEligibilityForm paroleEligibilityForm, 
			final BindingResult result) throws DuplicateEntityFoundException {
		this.paroleEligibilityFormValidator.validate(paroleEligibilityForm, 
				result);
		if (result.hasErrors()) {
			return prepareRedisplayEditMav(offender, paroleEligibilityForm, 
					result);
		}
		EligibilityStatusReason statusReason = null;
		String statusComment = null;
		Date reviewDate = null;
		if (EligibilityStatusCategory.INELIGIBLE.equals(paroleEligibilityForm
				.getEligibilityStatusCategory()) || EligibilityStatusCategory
				.WAIVED.equals(paroleEligibilityForm
						.getEligibilityStatusCategory())) {
			statusReason = paroleEligibilityForm.getEligibilityStatusReason();
			statusComment = paroleEligibilityForm.getStatusReasonComment();
			reviewDate = paroleEligibilityForm.getReviewDate();
		}
		ParoleEligibility eligibility = this.paroleEligibilityService.create(
				offender, paroleEligibilityForm.getHearingEligibilityDate(), 
				paroleEligibilityForm.getAppearanceCategory(), 
				paroleEligibilityForm.getEligibilityStatusCategory(), 
				paroleEligibilityForm.getParoleEligibilityStatusDate(), 
				statusReason, statusComment, reviewDate);
		this.processParoleEligibilityNoteItems(paroleEligibilityForm
				.getParoleEligibilityNoteItems(), eligibility);
		return new ModelAndView(String.format(LIST_REDIRECT, offender.getId()));
	}
	
	/**
	 * Displays form to update existing parole eligibility.
	 * 
	 * @param offender offender
	 * @param paroleEligibility parole eligibility to update
	 * @return screen for display form to update existing parole eligibility
	 */
	@PreAuthorize("hasRole('PAROLE_ELIGIBILITY_VIEW') or hasRole('ADMIN')")
	@RequestMapping(value = "/edit.html", method = RequestMethod.GET)
	public ModelAndView edit(
			@RequestParam(value = "eligibility", required = true)
				final ParoleEligibility paroleEligibility) {
		ParoleEligibilityForm paroleEligibilityForm 
				= new ParoleEligibilityForm();
		paroleEligibilityForm.setHearingEligibilityDate(paroleEligibility
				.getHearingEligibilityDate());
		paroleEligibilityForm.setAppearanceCategory(paroleEligibility
				.getAppearanceCategory());
		if (paroleEligibility.getStatus() != null) {
			paroleEligibilityForm.setEligibilityStatusCategory(paroleEligibility
				.getStatus().getCategory());
			paroleEligibilityForm.setEligibilityStatusReason(paroleEligibility
					.getStatus().getReason());
			paroleEligibilityForm.setStatusReasonComment(paroleEligibility
					.getStatus().getComment());
			paroleEligibilityForm.setParoleEligibilityStatusDate(paroleEligibility
					.getStatus().getDate());
			
		}
		paroleEligibilityForm.setReviewDate(paroleEligibility.getReviewDate());
		List<ParoleEligibilityNote> eligibilityNotes 
			= this.paroleEligibilityService
				.findParoleEligibilityNotesByParoleEligibility(
						paroleEligibility);
		List<ParoleEligibilityNoteItem> items
			= new ArrayList<ParoleEligibilityNoteItem>();
		for (ParoleEligibilityNote eligibilityNote : eligibilityNotes) {
			ParoleEligibilityNoteItem item = new ParoleEligibilityNoteItem();
			item.setNote(eligibilityNote);
			item.setDate(eligibilityNote.getDate());
			item.setDescription(eligibilityNote.getDescription());
			item.setOperation(ParoleEligibilityNoteItemOperation.UPDATE);
			items.add(item);
		}
		paroleEligibilityForm.setParoleEligibilityNoteItems(items);
		ModelAndView mav = this.prepareEditMav(paroleEligibility.getOffender(), 
				paroleEligibilityForm);
		mav.addObject(PAROLE_ELIGIBILITY_MODEL_KEY, paroleEligibility);
		return mav;
	}
	
	/**
	 * Updates an existing parole eligibility.
	 * 
	 * @param offender offender
	 * @param paroleEligibilityForm 
	 * @param result result
	 * @return an updated parole eligibility
	 * @throws DuplicateEntityFoundException thrown if a duplicate parole
	 * eligibility is found
	 */
	@PreAuthorize("hasRole('PAROLE_ELIGIBILITY_EDIT') or hasRole('ADMIN')")
	@RequestMapping(value = "/edit.html", method = RequestMethod.POST)
	public ModelAndView update(
			@RequestParam(value = "eligibility", required = true)
				final ParoleEligibility eligibility,
				final ParoleEligibilityForm paroleEligibilityForm,
				final BindingResult result) 
						throws DuplicateEntityFoundException {
		this.paroleEligibilityFormValidator.validate(paroleEligibilityForm,
				result);
		if (result.hasErrors()) {
			return prepareRedisplayEditMav(eligibility.getOffender(),
					paroleEligibilityForm, result);
		}
		EligibilityStatusReason statusReason = null;
		String statusComment = null;
		Date reviewDate = null;
		if (EligibilityStatusCategory.INELIGIBLE.equals(paroleEligibilityForm
				.getEligibilityStatusCategory()) || EligibilityStatusCategory
				.WAIVED.equals(paroleEligibilityForm
						.getEligibilityStatusCategory())) {
			statusReason = paroleEligibilityForm.getEligibilityStatusReason();
			statusComment = paroleEligibilityForm.getStatusReasonComment();
			reviewDate = paroleEligibilityForm.getReviewDate();
		}
		ParoleEligibility paroleEligibility = this.paroleEligibilityService
				.update(eligibility,
						paroleEligibilityForm.getHearingEligibilityDate(),
						paroleEligibilityForm.getAppearanceCategory(),
						paroleEligibilityForm.getEligibilityStatusCategory(),
						paroleEligibilityForm.getParoleEligibilityStatusDate(),
						statusReason, statusComment, reviewDate);
		this.processParoleEligibilityNoteItems(
				paroleEligibilityForm.getParoleEligibilityNoteItems(),
				paroleEligibility);
		return new ModelAndView(String.format(LIST_REDIRECT,
				eligibility.getOffender().getId()));
	}
	
	/**
	 * Displays a parole eligibility note item row.
	 * 
	 * @param paroleEligibilityNoteItemIndex parole eligibility note item index
	 * @return model and view for parole eligibility note item row
	 */
	@RequestMapping(value = "/createParoleEligibilityNoteItem.html", 
			method = RequestMethod.GET)
	public ModelAndView displayParoleEligibilityNoteItemRow(@RequestParam(
			value = "paroleEligibilityNoteItemIndex", required = true)
				final Integer paroleEligibilityNoteItemIndex) {
		ModelMap map = new ModelMap();
		ParoleEligibilityNoteItem eligibilityNoteItem 
			= new ParoleEligibilityNoteItem();
		eligibilityNoteItem.setOperation(
				ParoleEligibilityNoteItemOperation.CREATE);
		map.addAttribute(PAROLE_ELIGIBILITY_NOTE_ITEM_MODEL_KEY, 
				eligibilityNoteItem);
		map.addAttribute(PAROLE_ELIGIBILITY_NOTE_ITEM_INDEX_MODEL_KEY, 
				paroleEligibilityNoteItemIndex);
		return new ModelAndView(PAROLE_ELIGIBILITY_NOTE_ITEM_ROW_VIEW_NAME, 
				map);
	}
	
	/* Action menus. */
	
	/**
	 * Returns the parole eligibility action menu.
	 * 
	 * @param offender offender
	 * @return parole eligibility action menu
	 */
	@RequestMapping(value = "/paroleEligibilityActionMenu.html", 
			method = RequestMethod.GET)
	public ModelAndView showParoleEligibilityActionMenu(
			@RequestParam(value = "offender", required = true) 
			final Offender offender,
			@RequestParam(value = "eligibility", required = false) 
			final ParoleEligibility eligibility) {
		ModelAndView mav = new ModelAndView(
				PAROLE_ELIGIBILITY_ACTION_MENU_VIEW_NAME);
		mav.addObject(OFFENDER_MODEL_KEY, offender);
		if (eligibility != null) {
			mav.addObject(PAROLE_ELIGIBILITY_MODEL_KEY, eligibility);
			mav.addObject(BOARD_HEARING_MODEL_KEY, 
					this.paroleEligibilityReportService
					.findBoardHearingByParoleEligibility(eligibility));
		}	
		return mav;
	}
	
	/**
	 * Displays the action menu for parole eligibility note items.
	 * 
	 * @return model and view for parole eligibility note items action menu
	 */
	@RequestMapping(value = "/paroleEligibilityNoteItemsActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView displayParoleEligibilityNoteItemsActionMenu(
			@RequestParam("paroleEligibilityNoteIndex") 
	final Long paroleEligibilityNoteIndex) {
		ModelMap map = new ModelMap();
		map.addAttribute(PAROLE_ELIGIBILITY_NOTE_ITEM_INDEX_MODEL_KEY, 
				paroleEligibilityNoteIndex);
		return new ModelAndView(
				PAROLE_ELIGIBILITY_NOTE_ITEMS_ACTION_MENU_VIEW_NAME, map);
	}
	
	/* Helper methods. */
	
	/**
	 * Process the specified list of parole eligibility note items according to
	 * their specified operation values.
	 * 
	 * @param items parole eligibility note items
	 * @param paroleEligibility parole eligibility
	 * @throws DuplicateEntityFoundException thrown when a duplicate parole
	 * eligibility not is found
	 */
	private void processParoleEligibilityNoteItems(
			final List<ParoleEligibilityNoteItem> items, 
			final ParoleEligibility paroleEligibility) 
					throws DuplicateEntityFoundException {
		if (items != null) {
			for (ParoleEligibilityNoteItem item : items) {
				if (ParoleEligibilityNoteItemOperation.CREATE.equals(item
						.getOperation())) {
					this.paroleEligibilityService.createParoleEligibilityNote(
							paroleEligibility, item.getDate(), 
							item.getDescription());					
				} else if (ParoleEligibilityNoteItemOperation.UPDATE.equals(
						item.getOperation())) {
					this.paroleEligibilityService.updateParoleEligibilityNote(
							item.getNote(), item.getDate(), 
							item.getDescription());
				} else if (ParoleEligibilityNoteItemOperation.REMOVE.equals(
						item.getOperation())) {
					this.paroleEligibilityService.removeParoleEligiblityNote(
							item.getNote());
				}
			}
		}
	}
	
	// Prepares edit model and view
	private ModelAndView prepareEditMav(final Offender offender, 
			final ParoleEligibilityForm paroleEligibilityForm) {
		ModelAndView mav = new ModelAndView(EDIT_VIEW_NAME);
		if (paroleEligibilityForm.getParoleEligibilityNoteItems() != null) {
			mav.addObject(PAROLE_ELIGIBILITY_NOTE_ITEM_INDEX_MODEL_KEY, 
					paroleEligibilityForm.getParoleEligibilityNoteItems()
					.size());
		} else {
			mav.addObject(PAROLE_ELIGIBILITY_NOTE_ITEM_INDEX_MODEL_KEY, 0);
		}
		if (paroleEligibilityForm.getEligibilityStatusCategory() != null){
			if (EligibilityStatusCategory.WAIVED.equals(paroleEligibilityForm
					.getEligibilityStatusCategory())
					|| EligibilityStatusCategory.INELIGIBLE.equals(
					paroleEligibilityForm.getEligibilityStatusCategory())) {
				paroleEligibilityForm.setShowStatusFields(true);
			} else {
				paroleEligibilityForm.setShowStatusFields(false);
			}
		}
		mav.addObject(APPEARANCE_CATEGORIES_MODEL_KEY, 
				this.paroleEligibilityService.findAppearanceCategories());
		mav.addObject(STATUS_REASONS_MODEL_KEY, 
				this.paroleEligibilityService.findEligibilityStatusReasons());
		mav.addObject(PAROLE_ELIGIBILITY_FORM_MODEL_KEY, paroleEligibilityForm);
		mav.addObject(ELIGIBILITY_STATUSES_MODEL_KEY, EligibilityStatusCategory
				.values());
		this.offenderSummaryModelDelegate.add(mav.getModelMap(), offender);
		return mav;
	}
	
	// Prepares redisplay edit model and view
	private ModelAndView prepareRedisplayEditMav(
			final Offender offender,
			final ParoleEligibilityForm paroleEligibilityForm,
			final BindingResult result) {
		ModelAndView mav = prepareEditMav(offender, paroleEligibilityForm);
		mav.addObject(BindingResult.MODEL_KEY_PREFIX 
				+ PAROLE_ELIGIBILITY_FORM_MODEL_KEY, result);
		return mav;
	}
	
	/* Init binders. */
	
	/**
	 * Sets up init binder.
	 * @param binder binder
	 */
	@InitBinder
	protected void initBinder(final WebDataBinder binder) {
		binder.registerCustomEditor(Offender.class, 
				this.offenderPropertyEditorFactory
					.createOffenderPropertyEditor());
		binder.registerCustomEditor(Date.class, 
				this.datePropertyEditorFactory
					.createCustomDateOnlyEditor(true));
		binder.registerCustomEditor(ParoleEligibility.class, 
				this.paroleEligibilityPropertyEditorFactory
					.createPropertyEditor());
		binder.registerCustomEditor(ParoleEligibilityNote.class, 
				this.paroleEligibilityNotePropertyEditorFactory
					.createPropertyEditor());
		binder.registerCustomEditor(AppearanceCategory.class, 
				this.appearanceCategoryPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(EligibilityStatusReason.class, 
				this.eligibilityStatusReasonPropertyEditorFactory
				.createPropertyEditor());
	}

}
