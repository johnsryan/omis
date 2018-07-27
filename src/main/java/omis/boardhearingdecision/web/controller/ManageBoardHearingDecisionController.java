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
package omis.boardhearingdecision.web.controller;

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
import omis.boardhearing.domain.BoardHearing;
import omis.boardhearing.domain.BoardHearingParticipant;
import omis.boardhearingdecision.domain.BoardHearingDecision;
import omis.boardhearingdecision.domain.BoardHearingDecisionCategory;
import omis.boardhearingdecision.domain.BoardMemberDecision;
import omis.boardhearingdecision.domain.DecisionCategory;
import omis.boardhearingdecision.domain.HearingDecisionNote;
import omis.boardhearingdecision.domain.HearingDecisionReason;
import omis.boardhearingdecision.service.BoardHearingDecisionService;
import omis.boardhearingdecision.web.form.BoardHearingDecisionForm;
import omis.boardhearingdecision.web.form.BoardMemberDecisionItem;
import omis.boardhearingdecision.web.form.HearingDecisionNoteItem;
import omis.boardhearingdecision.web.form.HearingDecisionNoteItemOperation;
import omis.boardhearingdecision.web.validator.BoardHearingDecisionFormValidator;
import omis.exception.DuplicateEntityFoundException;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.web.controller.delegate.BusinessExceptionHandlerDelegate;

/**
 * Controller for managing board hearing decisions.
 * 
 * @author Josh Divine
 * @version 0.1.1 (Jul 26, 2018)
 * @since OMIS 3.0
 */
@Controller
@PreAuthorize("hasRole('USER')")
@RequestMapping("/boardHearingDecision")
public class ManageBoardHearingDecisionController {

	/* View names. */
	
	private static final String VIEW_NAME = "boardHearingDecision/edit";

	/* Table Rows. */
	
	private static final String HEARING_DECISION_NOTE_TABLE_ROW_VIEW_NAME =
			"boardHearingDecision/includes/hearingDecisionNoteTableRow";
	
	/* Partial Views. */
	
	private static final String HEARING_DECISON_REASON_OPTIONS_VIEW_NAME = 
			"boardHearingDecision/includes/hearingDecisionReasonOptions";
	
	private static final String 
			BOARD_HEARING_DECISON_CATEGORY_OPTIONS_VIEW_NAME = 
			"boardHearingDecision/includes/boardHearingDecisionCategoryOptions";
	
	/* Action menus. */

	private static final String ACTION_MENU_VIEW_NAME = 
			"boardHearingDecision/includes/boardHearingDecisionActionMenu";
	
	
	private static final String HEARING_DECISION_NOTES_ACTION_MENU_VIEW_NAME = 
			"boardHearingDecision/includes/hearingDecisionNotesActionMenu";
	
	/* Redirects. */
	
	private static final String REDIRECT_URL = 
			"redirect:/boardHearing/edit.html?boardHearing=%d";
	
	/* Model keys. */
	
	private static final String BOARD_HEARING_DECISION_FORM_MODEL_KEY = 
			"boardHearingDecisionForm";
	
	private static final String BOARD_HEARING_DECISION_MODEL_KEY = 
			"boardHearingDecision";
	
	private static final String HEARING_DECISION_NOTE_INDEX_MODEL_KEY = 
			"hearingDecisionNoteIndex";
	
	private static final String ITEM_OPERATION_MODEL_KEY = "operation";
	
	private static final String BOARD_HEARING_MODEL_KEY = "boardHearing";
	
	private static final String DECISION_CATEGORIES_MODEL_KEY = 
			"decisionCategories";
	
	private static final String HEARING_DECISION_REASONS_MODEL_KEY = 
			"hearingDecisionReasons";
	
	private static final String GRANT_HEARING_DECISION_REASONS_MODEL_KEY = 
			"grantHearingDecisionReasons";
	
	private static final String DENY_HEARING_DECISION_REASONS_MODEL_KEY = 
			"denyHearingDecisionReasons";
	
	private static final String BOARD_HEARING_DECISION_CATEGORIES_MODEL_KEY = 
			"boardHearingDecisionCategories";
	
	/* Services. */
	
	@Autowired
	@Qualifier("boardHearingDecisionService")
	private BoardHearingDecisionService boardHearingDecisionService;
	
	/* Property editor factories. */
	
	@Autowired
	@Qualifier("boardHearingDecisionPropertyEditorFactory")
	private PropertyEditorFactory boardHearingDecisionPropertyEditorFactory;
	
	@Autowired
	@Qualifier("boardMemberDecisionPropertyEditorFactory")
	private PropertyEditorFactory boardMemberDecisionPropertyEditorFactory;
	
	@Autowired
	@Qualifier("hearingDecisionNotePropertyEditorFactory")
	private PropertyEditorFactory hearingDecisionNotePropertyEditorFactory;
	
	@Autowired
	@Qualifier("hearingDecisionReasonPropertyEditorFactory")
	private PropertyEditorFactory hearingDecisionReasonPropertyEditorFactory;
	
	@Autowired
	@Qualifier("boardHearingDecisionCategoryPropertyEditorFactory")
	private PropertyEditorFactory 
			boardHearingDecisionCategoryPropertyEditorFactory;
	
	@Autowired
	@Qualifier("datePropertyEditorFactory")
	private CustomDateEditorFactory customDateEditorFactory;
	
	@Autowired
	@Qualifier("boardHearingPropertyEditorFactory")
	private PropertyEditorFactory boardHearingPropertyEditorFactory;
	
	@Autowired
	@Qualifier("boardHearingParticipantPropertyEditorFactory")
	private PropertyEditorFactory boardHearingParticipantPropertyEditorFactory;
	
	/* Validators. */

	@Autowired
	@Qualifier("boardHearingDecisionFormValidator")
	private BoardHearingDecisionFormValidator boardHearingDecisionFormValidator;
	
	/* Helpers. */

	@Autowired
	@Qualifier("businessExceptionHandlerDelegate")
	private BusinessExceptionHandlerDelegate businessExceptionHandlerDelegate;
	
	@Autowired
	@Qualifier("offenderSummaryModelDelegate")
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;

	/* Constructors. */
	
	/** Instantiates controller for board hearing decisions. */
	public ManageBoardHearingDecisionController() {
		// Default instantiation
	}
	
	/* URL invokable methods. */
	
	/**
	 * Shows screen to edit a board hearing decision.
	 * 
	 * @param boardHearing board hearing
	 * @param redirectUrl URL to redirect to upon completion
	 * @return screen to edit a board hearing decision
	 */
	@PreAuthorize("hasRole('BOARD_HEARING_DECISION_VIEW') or hasRole('ADMIN')")
	@RequestMapping(value = "/edit.html", method = RequestMethod.GET)
	public ModelAndView edit(
			@RequestParam(value = "boardHearing", required = true)
				final BoardHearing boardHearing,
			@RequestParam(value = "redirectUrl", required = false)
				final String redirectUrl) {
		BoardHearingDecision boardHearingDecision = this
				.boardHearingDecisionService.findBoardHearingDecisionByHearing(
						boardHearing);
		BoardHearingDecisionForm form = new BoardHearingDecisionForm();
		List<BoardMemberDecisionItem> decisionItems = new ArrayList<>();
		List<HearingDecisionNoteItem> noteItems = new ArrayList<>();
		if (boardHearingDecision != null) {
			// populate form
			form.setCategory(boardHearingDecision.getCategory());
			List<BoardMemberDecision> memberDecisions = this
					.boardHearingDecisionService
					.findBoardMemberDecisionsByBoardHearingDecision(
							boardHearingDecision);
			for (BoardMemberDecision memberDecision : memberDecisions) {
				BoardMemberDecisionItem decisionItem = 
						new BoardMemberDecisionItem();
				decisionItem.setBoardMemberDecision(memberDecision);
				decisionItem.setBoardHearingParticipant(memberDecision
						.getBoardHearingParticipant());
				decisionItem.setCategory(memberDecision.getDecisionReason()
						.getCategory());
				decisionItem.setDecisionReason(memberDecision
						.getDecisionReason());
				decisionItem.setComments(memberDecision.getComments());
				decisionItems.add(decisionItem);
			}
			List<HearingDecisionNote> notes = this.boardHearingDecisionService
					.findHearingDecisionNotesByBoardHearingDecision(
							boardHearingDecision);
			for (HearingDecisionNote note : notes) {
				HearingDecisionNoteItem noteItem = new HearingDecisionNoteItem();
				noteItem.setHearingDecisionNote(note);
				noteItem.setDate(note.getDate());
				noteItem.setValue(note.getDescription());
				noteItem.setOperation(HearingDecisionNoteItemOperation.EDIT);
				noteItems.add(noteItem);
			}
		} else {
			List<BoardHearingParticipant> participants = this
					.boardHearingDecisionService
					.findBoardHearingParticipantsByHearing(boardHearing);
			for (BoardHearingParticipant participant : participants) {
				BoardMemberDecisionItem decisionItem = 
						new BoardMemberDecisionItem();
				decisionItem.setBoardHearingParticipant(participant);
				decisionItems.add(decisionItem);
			}
		}
		form.setBoardMemberDecisionItems(decisionItems);
		form.setHearingDecisionNoteItems(noteItems);
		ModelAndView mav = this.prepareMav(form, boardHearing);
		mav.addObject(BOARD_HEARING_DECISION_MODEL_KEY, boardHearingDecision);
		return mav;
	}
	
	/**
	 * Updates a board hearing decision.
	 * 
	 * @param boardHearing board hearing
	 * @param boardHearingDecisionForm board hearing decision form
	 * @param bindingResult binding result
	 * @return redirect to board hearing listing screen
	 * @throws DuplicateEntityFoundException if duplicate entity exists 
	 */
	@PreAuthorize("hasRole('BOARD_HEARING_DECISION_EDIT') or hasRole('ADMIN')")
	@RequestMapping(value = "/edit.html", method = RequestMethod.POST)
	public ModelAndView update(
			@RequestParam(value = "boardHearing", required = true)
				final BoardHearing boardHearing,
			@RequestParam(value = "redirectUrl", required = false)
				final String redirectUrl,
			final BoardHearingDecisionForm boardHearingDecisionForm,
			final BindingResult bindingResult) 
					throws DuplicateEntityFoundException {
		this.boardHearingDecisionFormValidator.validate(
				boardHearingDecisionForm, bindingResult);
		if (bindingResult.hasErrors()) {
			ModelAndView mav = this.prepareRedisplay(boardHearingDecisionForm, 
					boardHearing, bindingResult);
			return mav;
		}
		BoardHearingDecision boardHearingDecision = this
				.boardHearingDecisionService.findBoardHearingDecisionByHearing(
						boardHearing);
		if (boardHearingDecision == null) {
			boardHearingDecision = this.boardHearingDecisionService
					.createBoardHearingDecision(boardHearing, 
							boardHearingDecisionForm.getCategory());
		} else {
			boardHearingDecision = this.boardHearingDecisionService
					.updateBoardHearingDecision(boardHearingDecision, 
							boardHearingDecisionForm.getCategory());
		}
		
		processBoardMemberDecisionItems(boardHearingDecision, 
				boardHearingDecisionForm.getBoardMemberDecisionItems());
		
		processHearingDecisionNoteItems(boardHearingDecision, 
				boardHearingDecisionForm.getHearingDecisionNoteItems());
		if (redirectUrl != null) {
			return new ModelAndView(redirectUrl);
		}
		return new ModelAndView(String.format(REDIRECT_URL, 
				boardHearing.getId()));
	}
	
	/* Invokable AJAX calls. */
	
	/**
	 * Displays table row of hearing decision note inputs.
	 * 
	 * @param hearingDecisionNoteIndex index of hearing decision note for which 
	 * to display row of inputs
	 * @return table row of hearing decision note inputs
	 */
	@RequestMapping(value = "/addHearingDecisionNote.html", 
			method = RequestMethod.GET)
	public ModelAndView addBoardHearingDecisionNote(
			@RequestParam(value = "hearingDecisionNoteIndex", required = true)
				final int hearingDecisionNoteIndex) {
		ModelAndView mav = new ModelAndView(
				HEARING_DECISION_NOTE_TABLE_ROW_VIEW_NAME);
		mav.addObject(HEARING_DECISION_NOTE_INDEX_MODEL_KEY, 
				hearingDecisionNoteIndex);
		mav.addObject(ITEM_OPERATION_MODEL_KEY, 
				HearingDecisionNoteItemOperation.CREATE);
		return mav;
	}
	
	/**
	 * Displays hearing decision reason options for the specified decision 
	 * category.
	 * 
	 * @param decision decision category
	 * @return hearing decision reason options
	 */
	@RequestMapping(value = "/findHearingDecisionReasonsForDecision.html", 
			method = RequestMethod.GET)
	public ModelAndView findHearingDecisionReasonsForDecision(
			@RequestParam(value = "decision", required = true) 
				final DecisionCategory decision) {
		ModelAndView mav = new ModelAndView(
				HEARING_DECISON_REASON_OPTIONS_VIEW_NAME);
		mav.addObject(HEARING_DECISION_REASONS_MODEL_KEY, 
				this.boardHearingDecisionService
				.findHearingDecisionReasonsByDecisionCategory(decision));
		return mav;
	}
	
	/**
	 * Displays board hearing decision category options for the specified 
	 * decision category.
	 * 
	 * @param decision decision category
	 * @return board hearing decision category options
	 */
	@RequestMapping(value = 
			"/findBoardHearingDecisionCategoriesForDecision.html", 
			method = RequestMethod.GET)
	public ModelAndView findBoardHearingDecisionCategoriesForDecision(
			@RequestParam(value = "decision", required = true) 
				final DecisionCategory decision) {
		ModelAndView mav = new ModelAndView(
				BOARD_HEARING_DECISON_CATEGORY_OPTIONS_VIEW_NAME);
		mav.addObject(BOARD_HEARING_DECISION_CATEGORIES_MODEL_KEY, 
				this.boardHearingDecisionService
				.findBoardHearingDecisionCategoriesByDecision(decision));
		return mav;
	}
	
	/* Action menus. */
	
	/**
	 * Displays action menu for screen to create/edit a board hearing decision.
	 * 
	 * @return action menu for screen to create/edit a board hearing decision
	 */
	@RequestMapping(value = "/boardHearingDecisionActionMenu.html", 
			method = RequestMethod.GET)
	public ModelAndView showActionMenu(
			@RequestParam(value = "boardHearing", required = true)
				final BoardHearing boardHearing) {
		ModelAndView mav = new ModelAndView(ACTION_MENU_VIEW_NAME);
		mav.addObject(BOARD_HEARING_MODEL_KEY, boardHearing);
		return mav;
	}

	/** 
	 * Hearing decision note action menu.
	 * 
	 * @param hearingDecisionNoteIndex hearing decision note index.
	 * @return action menu. 
	 */
	@RequestMapping(value = "/hearingDecisionNotesActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView showHearingDecisionNotesActionMenu(
			@RequestParam(value = "hearingDecisionNoteIndex", required = true) 
				final Long hearingDecisionNoteIndex) {
		final ModelMap modelMap = new ModelMap();
		modelMap.put(HEARING_DECISION_NOTE_INDEX_MODEL_KEY, 
				hearingDecisionNoteIndex);
		return new ModelAndView(HEARING_DECISION_NOTES_ACTION_MENU_VIEW_NAME, 
				modelMap);
	}
	
	/* Helper methods. */
	
	// Prepares model and view
	private ModelAndView prepareMav(
			final BoardHearingDecisionForm boardHearingDecisionForm,
			final BoardHearing boardHearing) {
		ModelAndView mav = new ModelAndView(VIEW_NAME);
		mav.addObject(BOARD_HEARING_DECISION_FORM_MODEL_KEY, 
				boardHearingDecisionForm);
		mav.addObject(BOARD_HEARING_MODEL_KEY, boardHearing);
		if (boardHearingDecisionForm.getHearingDecisionNoteItems() != null) {
			mav.addObject(HEARING_DECISION_NOTE_INDEX_MODEL_KEY, 
					boardHearingDecisionForm.getHearingDecisionNoteItems()
					.size());
		} else {
			mav.addObject(HEARING_DECISION_NOTE_INDEX_MODEL_KEY, 0);
		}
		mav.addObject(DECISION_CATEGORIES_MODEL_KEY, DecisionCategory.values());
		if (boardHearingDecisionForm.getCategory() != null) {
			mav.addObject(BOARD_HEARING_DECISION_CATEGORIES_MODEL_KEY, 
					this.boardHearingDecisionService
					.findBoardHearingDecisionCategoriesByDecision(
							boardHearingDecisionForm.getCategory()
							.getDecision()));
		}
		// Used to load hearing decision reasons for both grant and deny options
		// if they have been selected on the form already.
		boolean includeGrant = false;
		boolean includeDeny = false;
		for (BoardMemberDecisionItem decisionItem : 
			boardHearingDecisionForm.getBoardMemberDecisionItems()) {
			DecisionCategory category = null;
			if (decisionItem.getDecisionReason() != null) {
				category = decisionItem.getDecisionReason().getCategory();
			} else if (decisionItem.getCategory() != null) {
				category = decisionItem.getCategory();
			}
			if (category != null) {	
				if (DecisionCategory.GRANT.equals(category) && !includeGrant) {
					mav.addObject(GRANT_HEARING_DECISION_REASONS_MODEL_KEY, 
							this.boardHearingDecisionService
							.findHearingDecisionReasonsByDecisionCategory(
									category));
					includeGrant = true;
				} else if (DecisionCategory.DENY.equals(category) && 
						!includeDeny) {
					mav.addObject(DENY_HEARING_DECISION_REASONS_MODEL_KEY, 
							this.boardHearingDecisionService
							.findHearingDecisionReasonsByDecisionCategory(
									category));
					includeDeny = true;
				}
			}
			if (includeDeny && includeGrant) {
				// Exit loop early if both sets of reasons have been loaded
				break;
			}
		}
		this.offenderSummaryModelDelegate.add(mav.getModel(), boardHearing
				.getParoleEligibility().getOffender());
		return mav;
	}
	
	// Prepares redisplay model and view
	private ModelAndView prepareRedisplay(
			final BoardHearingDecisionForm boardHearingDecisionForm,
			final BoardHearing boardHearing,
			final BindingResult bindingResult) {
		ModelAndView mav = this.prepareMav(boardHearingDecisionForm, 
				boardHearing);
		mav.addObject(BindingResult.MODEL_KEY_PREFIX
					+ BOARD_HEARING_DECISION_FORM_MODEL_KEY, bindingResult);
		return mav;
	}
	
	// Process hearing decision notes
	private void processHearingDecisionNoteItems(
			final BoardHearingDecision boardHearingDecision,
			final List<HearingDecisionNoteItem> hearingDecisionNoteItems) 
					throws DuplicateEntityFoundException {
		if (hearingDecisionNoteItems != null) {
			for (HearingDecisionNoteItem noteItem : hearingDecisionNoteItems) {
				if (HearingDecisionNoteItemOperation.CREATE.equals(
						noteItem.getOperation())) {
					this.boardHearingDecisionService.createHearingDecisionNote(
							boardHearingDecision, noteItem.getDate(), 
							noteItem.getValue());
				} else if (HearingDecisionNoteItemOperation.EDIT.equals(
						noteItem.getOperation())) {
					this.boardHearingDecisionService.updateHearingDecisionNote(
							noteItem.getHearingDecisionNote(), 
							noteItem.getDate(), noteItem.getValue());
				} else if (HearingDecisionNoteItemOperation.REMOVE.equals(
						noteItem.getOperation())) {
					this.boardHearingDecisionService.removeHearingDecisionNote(
							noteItem.getHearingDecisionNote());
				}
			}
		}
	}
	
	// Process board member decisions
	private void processBoardMemberDecisionItems(
			final BoardHearingDecision boardHearingDecision,
			final List<BoardMemberDecisionItem> boardMemberDecisionItems) 
					throws DuplicateEntityFoundException {
		if (boardMemberDecisionItems != null) {
			for (BoardMemberDecisionItem decisionItem : 
				boardMemberDecisionItems) {
				if (decisionItem.getBoardMemberDecision() == null) {
					this.boardHearingDecisionService.createBoardMemberDecision(
							boardHearingDecision, 
							decisionItem.getBoardHearingParticipant(), 
							decisionItem.getDecisionReason(), 
							decisionItem.getComments());
				} else {
					this.boardHearingDecisionService.updateBoardMemberDecision(
							decisionItem.getBoardMemberDecision(), 
							decisionItem.getBoardHearingParticipant(), 
							decisionItem.getDecisionReason(), 
							decisionItem.getComments());
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
		binder.registerCustomEditor(BoardHearingDecision.class,
				this.boardHearingDecisionPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(BoardMemberDecision.class,
				this.boardMemberDecisionPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(BoardHearing.class,
				this.boardHearingPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(BoardHearingParticipant.class,
				this.boardHearingParticipantPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(HearingDecisionNote.class,
				this.hearingDecisionNotePropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(HearingDecisionReason.class,
				this.hearingDecisionReasonPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(BoardHearingDecisionCategory.class,
				this.boardHearingDecisionCategoryPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(Date.class,
				this.customDateEditorFactory.createCustomDateOnlyEditor(true));
	}
}