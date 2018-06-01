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
package omis.boardconsideration.web.controller;

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
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import omis.beans.factory.PropertyEditorFactory;
import omis.beans.factory.spring.CustomDateEditorFactory;
import omis.boardconsideration.domain.BoardConsideration;
import omis.boardconsideration.domain.BoardConsiderationCategory;
import omis.boardconsideration.domain.BoardConsiderationNote;
import omis.boardconsideration.service.BoardConsiderationService;
import omis.boardconsideration.web.form.BoardConsiderationForm;
import omis.boardconsideration.web.form.BoardConsiderationItem;
import omis.boardconsideration.web.form.BoardConsiderationNoteItem;
import omis.boardconsideration.web.form.ItemOperation;
import omis.boardconsideration.web.validator.BoardConsiderationFormValidator;
import omis.exception.DuplicateEntityFoundException;
import omis.hearinganalysis.domain.HearingAnalysis;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.web.controller.delegate.BusinessExceptionHandlerDelegate;

/**
 * Controller for managing board consideration.
 * 
 * @author Josh Divine
 * @version 0.1.0 (May 30, 2018)
 * @since OMIS 3.0
 */
@Controller
@PreAuthorize("hasRole('USER')")
@RequestMapping("/boardConsideration")
public class ManageBoardConsiderationController {

	/* View names. */
	
	private static final String VIEW_NAME = "boardConsideration/edit";

	/* Table Rows. */
	
	private static final String BOARD_CONSIDERATION_TABLE_ROW_VIEW_NAME =
			"boardConsideration/includes/boardConsiderationTableRow";
	
	private static final String BOARD_CONSIDERATION_NOTE_TABLE_ROW_VIEW_NAME =
			"boardConsideration/includes/boardConsiderationNoteTableRow";
	
	/* Action menus. */

	private static final String BOARD_CONSIDERATIONS_ACTION_MENU_VIEW_NAME = 
			"boardConsideration/includes/boardConsiderationsActionMenu";
	
	
	private static final String BOARD_CONSIDERATION_NOTES_ACTION_MENU_VIEW_NAME = 
			"boardConsideration/includes/boardConsiderationNotesActionMenu";
	
	/* Redirects. */
	
	private static final String REDIRECT_URL = 
			"redirect:/boardConsideration/edit.html?hearingAnalysis=%d";
	
	/* Model keys. */
	
	private static final String BOARD_CONSIDERATION_FORM_MODEL_KEY = 
			"boardConsiderationForm";
	
	private static final String HEARING_ANALYSIS_MODEL_KEY = 
			"hearingAnalysis";
	
	private static final String BOARD_CONSIDERATION_INDEX_MODEL_KEY = 
			"boardConsiderationIndex";
	
	private static final String BOARD_CONSIDERATION_NOTE_INDEX_MODEL_KEY = 
			"boardConsiderationNoteIndex";
	
	private static final String ITEM_OPERATION_MODEL_KEY = "operation";
	
	private static final String BOARD_CONSIDERATION_CATEGORIES_MODEL_KEY = 
			"boardConsiderationCategories";
	
	/* Services. */
	
	@Autowired
	@Qualifier("boardConsiderationService")
	private BoardConsiderationService boardConsiderationService;
	
	/* Property editor factories. */
	
	@Autowired
	@Qualifier("boardConsiderationPropertyEditorFactory")
	private PropertyEditorFactory boardConsiderationPropertyEditorFactory;
	
	@Autowired
	@Qualifier("boardConsiderationNotePropertyEditorFactory")
	private PropertyEditorFactory boardConsiderationNotePropertyEditorFactory;
	
	@Autowired
	@Qualifier("hearingAnalysisPropertyEditorFactory")
	private PropertyEditorFactory hearingAnalysisPropertyEditorFactory;
	
	@Autowired
	@Qualifier("datePropertyEditorFactory")
	private CustomDateEditorFactory customDateEditorFactory;
	
	/* Validators. */

	@Autowired
	@Qualifier("boardConsiderationFormValidator")
	private BoardConsiderationFormValidator boardConsiderationFormValidator;
	
	/* Helpers. */

	@Autowired
	@Qualifier("businessExceptionHandlerDelegate")
	private BusinessExceptionHandlerDelegate businessExceptionHandlerDelegate;
	
	@Autowired
	@Qualifier("offenderSummaryModelDelegate")
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;

	/* Constructors. */
	
	/** Instantiates controller for board consideration. */
	public ManageBoardConsiderationController() {
		// Default instantiation
	}
	
	/* URL invokable methods. */
	
	/**
	 * Shows screen to edit a board consideration.
	 * 
	 * @param hearingAnalysis hearing analysis
	 * @return screen to edit a board consideration
	 */
	@PreAuthorize("hasRole('BOARD_CONSIDERATION_VIEW') or hasRole('ADMIN')")
	@RequestMapping(value = "/edit.html", method = RequestMethod.GET)
	public ModelAndView edit(
			@RequestParam(value = "hearingAnalysis", required = true)
				final HearingAnalysis hearingAnalysis) {
		BoardConsiderationForm form = new BoardConsiderationForm();
		List<BoardConsiderationItem> considerationItems = new ArrayList<>();
		List<BoardConsiderationNoteItem> noteItems = new ArrayList<>();
		List<BoardConsideration> boardConsiderations = this
				.boardConsiderationService
				.findBoardConsiderationsByHearingAnalysis(hearingAnalysis);
		for (BoardConsideration boardConsideration : boardConsiderations) {
			BoardConsiderationItem considerationItem = 
					new BoardConsiderationItem();
			considerationItem.setBoardConsideration(boardConsideration);
			considerationItem.setTitle(boardConsideration.getTitle());
			considerationItem.setDescription(
					boardConsideration.getDescription());
			considerationItem.setCategory(boardConsideration.getCategory());
			considerationItem.setAccepted(boardConsideration.getAccepted());
			considerationItem.setOperation(ItemOperation.EDIT);
			considerationItems.add(considerationItem);
		}
		List<BoardConsiderationNote> notes = this.boardConsiderationService
				.findBoardConsiderationNotesByHearingAnalysis(hearingAnalysis);
		for (BoardConsiderationNote note : notes) {
			BoardConsiderationNoteItem noteItem = 
					new BoardConsiderationNoteItem();
			noteItem.setBoardConsiderationNote(note);
			noteItem.setDate(note.getDate());
			noteItem.setValue(note.getDescription());
			noteItem.setOperation(ItemOperation.EDIT);
			noteItems.add(noteItem);
		}
		form.setBoardConsiderationItems(considerationItems);
		form.setBoardConsiderationNoteItems(noteItems);
		return this.prepareMav(form, hearingAnalysis);
	}
	
	/**
	 * Updates a board consideration.
	 * 
	 * @param hearingAnalysis hearing analysis
	 * @param boardConsiderationForm board consideration form
	 * @param bindingResult binding result
	 * @return redirect to board hearing listing screen
	 * @throws DuplicateEntityFoundException if duplicate entity exists 
	 */
	@PreAuthorize("hasRole('BOARD_CONSIDERATION_EDIT') or hasRole('ADMIN')")
	@RequestMapping(value = "/edit.html", method = RequestMethod.POST)
	public ModelAndView update(
			@RequestParam(value = "hearingAnalysis", required = true)
				final HearingAnalysis hearingAnalysis,
			final BoardConsiderationForm boardConsiderationForm,
			final BindingResult bindingResult) 
					throws DuplicateEntityFoundException {
		this.boardConsiderationFormValidator.validate(
				boardConsiderationForm, bindingResult);
		if (bindingResult.hasErrors()) {
			ModelAndView mav = this.prepareRedisplay(boardConsiderationForm, 
					hearingAnalysis, bindingResult);
			return mav;
		}
		
		processBoardConsiderationItems(hearingAnalysis, 
				boardConsiderationForm.getBoardConsiderationItems());
		
		processBoardConsiderationNoteItems(hearingAnalysis, 
				boardConsiderationForm.getBoardConsiderationNoteItems());
		
		return new ModelAndView(String.format(REDIRECT_URL, 
				hearingAnalysis.getId()));
	}
	
	/* Invokable AJAX calls. */
	
	/**
	 * Displays table row of board consideration inputs.
	 * 
	 * @param boardConsiderationIndex index of board consideration for which to 
	 * display row of inputs
	 * @return table row of board consideration inputs
	 */
	@RequestMapping(value = "/addBoardConsideration.html", 
			method = RequestMethod.GET)
	public ModelAndView addBoardConsideration(
			@RequestParam(value = "boardConsiderationIndex", required = true)
				final int boardConsiderationIndex) {
		ModelAndView mav = new ModelAndView(
				BOARD_CONSIDERATION_TABLE_ROW_VIEW_NAME);
		mav.addObject(BOARD_CONSIDERATION_INDEX_MODEL_KEY, 
				boardConsiderationIndex);
		mav.addObject(BOARD_CONSIDERATION_CATEGORIES_MODEL_KEY, 
				BoardConsiderationCategory.values());
		mav.addObject(ITEM_OPERATION_MODEL_KEY, ItemOperation.CREATE);
		return mav;
	}
	
	/**
	 * Displays table row of board consideration note inputs.
	 * 
	 * @param boardConsiderationNoteIndex index of board consideration note for 
	 * which to display row of inputs
	 * @return table row of board consideration note inputs
	 */
	@RequestMapping(value = "/addBoardConsiderationNote.html", 
			method = RequestMethod.GET)
	public ModelAndView addBoardConsiderationNote(
			@RequestParam(value = "boardConsiderationNoteIndex", required = true)
				final int boardConsiderationNoteIndex) {
		ModelAndView mav = new ModelAndView(
				BOARD_CONSIDERATION_NOTE_TABLE_ROW_VIEW_NAME);
		mav.addObject(BOARD_CONSIDERATION_NOTE_INDEX_MODEL_KEY, 
				boardConsiderationNoteIndex);
		mav.addObject(ITEM_OPERATION_MODEL_KEY, ItemOperation.CREATE);
		return mav;
	}
	
	/* Action menus. */
	
	/**
	 * Displays action menu for board considerations.
	 * 
	 * @param boardConsiderationIndex board consideration index
	 * @return action menu
	 */
	@RequestMapping(value = "/boardConsiderationsActionMenu.html", 
			method = RequestMethod.GET)
	public ModelAndView showActionMenu(
			@RequestParam(value = "boardConsiderationIndex", required = true) 
				final Long boardConsiderationIndex,
			@RequestParam(value = "hearingAnalysis", required = true)
				final HearingAnalysis hearingAnalysis) {
		final ModelMap modelMap = new ModelMap();
		modelMap.put(BOARD_CONSIDERATION_INDEX_MODEL_KEY, 
				boardConsiderationIndex);
		modelMap.put(HEARING_ANALYSIS_MODEL_KEY, hearingAnalysis);
		return new ModelAndView(BOARD_CONSIDERATIONS_ACTION_MENU_VIEW_NAME, 
				modelMap);
	}

	/** 
	 * Hearing decision note action menu.
	 * 
	 * @param boardConsiderationNoteIndex board consideration note index
	 * @return action menu
	 */
	@RequestMapping(value = "/boardConsiderationNotesActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView showConsiderationNotesActionMenu(
			@RequestParam(value = "boardConsiderationNoteIndex", required = true) 
				final Long boardConsiderationNoteIndex) {
		final ModelMap modelMap = new ModelMap();
		modelMap.put(BOARD_CONSIDERATION_NOTE_INDEX_MODEL_KEY, 
				boardConsiderationNoteIndex);
		return new ModelAndView(BOARD_CONSIDERATION_NOTES_ACTION_MENU_VIEW_NAME, 
				modelMap);
	}
	
	/* Helper methods. */
	
	// Prepares model and view
	private ModelAndView prepareMav(
			final BoardConsiderationForm boardConsiderationForm,
			final HearingAnalysis hearingAnalysis) {
		ModelAndView mav = new ModelAndView(VIEW_NAME);
		mav.addObject(BOARD_CONSIDERATION_FORM_MODEL_KEY, 
				boardConsiderationForm);
		mav.addObject(HEARING_ANALYSIS_MODEL_KEY, hearingAnalysis);
		if (boardConsiderationForm.getBoardConsiderationItems() != null) {
			mav.addObject(BOARD_CONSIDERATION_INDEX_MODEL_KEY, 
					boardConsiderationForm.getBoardConsiderationItems().size());
		} else {
			mav.addObject(BOARD_CONSIDERATION_INDEX_MODEL_KEY, 0);
		}
		if (boardConsiderationForm.getBoardConsiderationNoteItems() != null) {
			mav.addObject(BOARD_CONSIDERATION_NOTE_INDEX_MODEL_KEY, 
					boardConsiderationForm.getBoardConsiderationNoteItems()
					.size());
		} else {
			mav.addObject(BOARD_CONSIDERATION_NOTE_INDEX_MODEL_KEY, 0);
		}
		mav.addObject(BOARD_CONSIDERATION_CATEGORIES_MODEL_KEY, 
				BoardConsiderationCategory.values());
		this.offenderSummaryModelDelegate.add(mav.getModel(), hearingAnalysis
				.getEligibility().getOffender());
		return mav;
	}
	
	// Prepares redisplay model and view
	private ModelAndView prepareRedisplay(
			final BoardConsiderationForm boardConsiderationForm,
			final HearingAnalysis hearingAnalysis,
			final BindingResult bindingResult) {
		ModelAndView mav = this.prepareMav(boardConsiderationForm, 
				hearingAnalysis);
		mav.addObject(BindingResult.MODEL_KEY_PREFIX
					+ BOARD_CONSIDERATION_FORM_MODEL_KEY, bindingResult);
		return mav;
	}
	
	// Process board consideration notes
	private void processBoardConsiderationNoteItems(
			final HearingAnalysis hearingAnalysis,
			final List<BoardConsiderationNoteItem> boardConsiderationNoteItems) 
					throws DuplicateEntityFoundException {
		if (boardConsiderationNoteItems != null) {
			for (BoardConsiderationNoteItem noteItem : 
					boardConsiderationNoteItems) {
				if (ItemOperation.CREATE.equals(
						noteItem.getOperation())) {
					this.boardConsiderationService.createBoardConsiderationNote(
							hearingAnalysis, noteItem.getValue(), 
							noteItem.getDate());
				} else if (ItemOperation.EDIT.equals(
						noteItem.getOperation())) {
					this.boardConsiderationService.updateBoardConsiderationNote(
							noteItem.getBoardConsiderationNote(), 
							noteItem.getValue(), noteItem.getDate());
				} else if (ItemOperation.REMOVE.equals(noteItem.getOperation())) {
					this.boardConsiderationService.removeBoardConsiderationNote(
							noteItem.getBoardConsiderationNote());
				}
			}
		}
	}
	
	// Process board considerations
	private void processBoardConsiderationItems(
			final HearingAnalysis hearingAnalysis,
			final List<BoardConsiderationItem> boardConsiderationItems) 
					throws DuplicateEntityFoundException {
		if (boardConsiderationItems != null) {
			for (BoardConsiderationItem considerationItem : 
				boardConsiderationItems) {
				if (considerationItem.getOperation().equals(
						ItemOperation.CREATE)) {
					this.boardConsiderationService.createBoardConsideration(
							hearingAnalysis, considerationItem.getTitle(), 
							considerationItem.getDescription(), 
							considerationItem.getCategory(), 
							considerationItem.getAccepted());
				} else if (considerationItem.getOperation().equals(
						ItemOperation.EDIT)) {
					this.boardConsiderationService.updateBoardConsideration(
							considerationItem.getBoardConsideration(), 
							considerationItem.getTitle(), 
							considerationItem.getDescription(), 
							considerationItem.getCategory(), 
							considerationItem.getAccepted());
				} else if (considerationItem.getOperation().equals(
						ItemOperation.REMOVE)) {
					this.boardConsiderationService.removeBoardConsideration(
							considerationItem.getBoardConsideration());
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
		binder.registerCustomEditor(BoardConsideration.class,
				this.boardConsiderationPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(BoardConsiderationNote.class,
				this.boardConsiderationNotePropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(HearingAnalysis.class,
				this.hearingAnalysisPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(Date.class,
				this.customDateEditorFactory.createCustomDateOnlyEditor(true));
	}
}