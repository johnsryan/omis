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
package omis.boardhearing.web.controller;

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
import omis.boardhearing.domain.BoardHearingCategory;
import omis.boardhearing.domain.BoardHearingNote;
import omis.boardhearing.domain.BoardHearingParticipant;
import omis.boardhearing.domain.CancellationCategory;
import omis.boardhearing.service.BoardHearingService;
import omis.boardhearing.web.form.BoardHearingForm;
import omis.boardhearing.web.form.BoardHearingItemOperation;
import omis.boardhearing.web.form.BoardHearingNoteItem;
import omis.boardhearing.web.validator.BoardHearingFormValidator;
import omis.exception.DuplicateEntityFoundException;
import omis.facility.domain.Unit;
import omis.hearinganalysis.domain.HearingAnalysis;
import omis.location.domain.Location;
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.paroleboarditinerary.domain.BoardAttendee;
import omis.paroleboarditinerary.domain.BoardMeetingSite;
import omis.paroleboarditinerary.domain.ParoleBoardItinerary;
import omis.paroleboardmember.domain.ParoleBoardMember;
import omis.paroleeligibility.domain.ParoleEligibility;
import omis.web.controller.delegate.BusinessExceptionHandlerDelegate;

/**
 * Board Hearing Controller.
 * 
 * @author Annie Wahl 
 * @author Josh Divine
 * @version 0.1.2 (Apr 18, 2018)
 * @since OMIS 3.0
 */
@Controller
@RequestMapping("/boardHearing")
@PreAuthorize("hasRole('USER')")
public class BoardHearingController {
	
	/* View Names */
	
	private static final String EDIT_VIEW_NAME = "/boardHearing/edit";
	
	private static final String BOARD_HEARING_ACTION_MENU_VIEW_NAME =
			"/boardHearing/includes/boardHearingActionMenu";
	
	private static final String BOARD_HEARING_NOTE_ITEM_ROW_VIEW_NAME =
			"/boardHearing/includes/boardHearingNoteItemTableRow";
	
	private static final String BOARD_HEARING_NOTE_ITEMS_ACTION_MENU_VIEW_NAME =
			"/boardHearing/includes/boardHearingNoteItemsActionMenu";
	
	private static final String LIST_REDIRECT_VIEW_NAME =
			"redirect:/boardHearing/list.html?offender=%d";
	
	private static final String HEARING_LOCATION_OPTIONS_VIEW_NAME =
			"/boardHearing/includes/hearingLocationOptions";
	
	private static final String BOARD_MEMBERS_VIEW_NAME =
			"/boardHearing/includes/boardMembers";
	
	/* Model Keys */
	
	private static final String BOARD_HEARING_FORM_MODEL_KEY =
			"boardHearingForm";
	
	private static final String BOARD_HEARING_CATEGORIES_MODEL_KEY =
			"categories";
	
	private static final String BOARD_ATTENDEES_MODEL_KEY = "boardAttendees";
	
	private static final String BOARD_MEETING_SITES_MODEL_KEY =
			"boardMeetingSites";
	
	private static final String PAROLE_ITINERARIES_MODEL_KEY =
			"paroleBoardItineraries";
	
	private static final String CANCELLATION_CATEGORIES =
			"cancellationCategories";
	
	private static final String BOARD_HEARING_MODEL_KEY = "boardHearing";
	
	private static final String PAROLE_ELIGIBILITY_MODEL_KEY =
			"paroleEligibility";
	
	private static final String OFFENDER_MODEL_KEY = "offender";
	
	private static final String BOARD_HEARING_NOTE_ITEM_INDEX_MODEL_KEY =
			"boardHearingNoteItemIndex";
	
	private static final String BOARD_HEARING_NOTE_ITEM_MODEL_KEY =
			"boardHearingNoteItem";
	
	private static final String BOARD_MEMBER_ONE_MODEL_KEY = "boardMember1";
	
	private static final String BOARD_MEMBER_TWO_MODEL_KEY = "boardMember2";
	
	private static final String BOARD_MEMBER_THREE_MODEL_KEY = "boardMember3";
	
	private static final Long THREE = (long) 3;
	
	private static final Long TWO = (long) 2;
	
	private static final Long ONE = (long) 1;
	
	/* Message Keys */
	
	private static final String BOARD_HEARING_EXISTS_MESSAGE_KEY =
			"boardHearing.exists";
	
	/* Bundles */
	
	private static final String ERROR_BUNDLE_NAME =
			"omis.boardhearing.msgs.form";
	
	
	/* Property Editor Factories */
	
	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory offenderPropertyEditorFactory;
	
	@Autowired
	@Qualifier("boardHearingPropertyEditorFactory")
	private PropertyEditorFactory boardHearingPropertyEditorFactory;
	
	@Autowired
	@Qualifier("appearanceCategoryPropertyEditorFactory")
	private PropertyEditorFactory appearanceCategoryPropertyEditorFactory;
	
	@Autowired
	@Qualifier("paroleEligibilityPropertyEditorFactory")
	private PropertyEditorFactory paroleEligibilityPropertyEditorFactory;
	
	@Autowired
	@Qualifier("hearingAnalysisPropertyEditorFactory")
	private PropertyEditorFactory hearingAnalysisPropertyEditorFactory;
	
	@Autowired
	@Qualifier("paroleBoardItineraryPropertyEditorFactory")
	private PropertyEditorFactory paroleBoardItineraryPropertyEditorFactory;
	
	@Autowired
	@Qualifier("boardMeetingSitePropertyEditorFactory")
	private PropertyEditorFactory boardMeetingSitePropertyEditorFactory;
	
	@Autowired
	@Qualifier("boardAttendeePropertyEditorFactory")
	private PropertyEditorFactory boardAttendeePropertyEditorFactory;
	
	@Autowired
	@Qualifier("paroleBoardMemberPropertyEditorFactory")
	private PropertyEditorFactory paroleBoardMemberPropertyEditorFactory;
	
	@Autowired
	@Qualifier("boardHearingParticipantPropertyEditorFactory")
	private PropertyEditorFactory boardHearingParticipantPropertyEditorFactory;
	
	@Autowired
	@Qualifier("boardHearingNotePropertyEditorFactory")
	private PropertyEditorFactory boardHearingNotePropertyEditorFactory;
	
	@Autowired
	@Qualifier("boardHearingCategoryPropertyEditorFactory")
	private PropertyEditorFactory boardHearingCategoryPropertyEditorFactory;
	
	@Autowired
	@Qualifier("locationPropertyEditorFactory")
	private PropertyEditorFactory locationPropertyEditorFactory;
	
	@Autowired
	private CustomDateEditorFactory customDateEditorFactory;
	
	@Autowired
	@Qualifier("unitPropertyEditorFactory")
	private PropertyEditorFactory unitPropertyEditorFactory;
	
	/* Controller Model Delegates */
	
	@Autowired
	@Qualifier("offenderSummaryModelDelegate")
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;

	@Autowired
	@Qualifier("businessExceptionHandlerDelegate")
	private BusinessExceptionHandlerDelegate businessExceptionHandlerDelegate;
	
	/* Services. */
	
	@Autowired
	@Qualifier("boardHearingService")
	private BoardHearingService boardHearingService;
	

	/* Validator. */
	
	@Autowired
	@Qualifier("boardHearingFormValidator")
	private BoardHearingFormValidator boardHearingFormValidator;
	
	/**
	 * Default constructor for Board Hearing Controller. 
	 */
	public BoardHearingController() {
	}
	
	/**
	 * Returns the Model And View for creating a Board Hearing.
	 * 
	 * @param paroleEligibility - Parole Eligibility
	 * @return Model And View for creating a Board Hearing.
	 */
	@RequestMapping(value = "/create.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('BOARD_HEARING_CREATE') "
			+ "or hasRole('ADMIN')")
	public ModelAndView create(@RequestParam(
			value = "paroleEligibility", required = true)
		final ParoleEligibility paroleEligibility) {
		return this.prepareEditMav(paroleEligibility,
				this.prepareForm(paroleEligibility), new ModelMap());
	}
	
	/**
	 * Creates a Board Hearing and returns to the board hearing list screen.
	 * 
	 * @param paroleEligibility - Parole Eligibility
	 * @param form - Board Hearing Form
	 * @param bindingResult - Binding Result
	 * @return ModelAndView - redirects to the board hearing list screen on
	 * successful Board Hearing creation, or back to the creation screen on
	 * form error. 
	 * @throws DuplicateEntityFoundException - When a Board Hearing,
	 * Board Hearing Note, or Board Hearing Participant already exist with the
	 * given properties.
	 */
	@RequestMapping(value = "/create.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('BOARD_HEARING_CREATE') "
			+ "or hasRole('ADMIN')")
	public ModelAndView save(@RequestParam(
			value = "paroleEligibility", required = true)
		final ParoleEligibility paroleEligibility,
		final BoardHearingForm form, final BindingResult bindingResult)
				throws DuplicateEntityFoundException {
		
		this.boardHearingFormValidator.validate(form, bindingResult);
		
		if (bindingResult.hasErrors()) {
			return this.prepareEditMav(paroleEligibility, form, new ModelMap());
		} else {
			CancellationCategory reason = null;
			/*if (form.getCancelled()) {
				reason = form.getReason();
			}*/
			BoardHearing boardHearing = this.boardHearingService
					.createBoardHearing(form.getParoleBoardItinerary(),
							form.getHearingDate(), paroleEligibility,
							form.getBoardHearingCategory(), reason,
							form.getVideoConference());
			if (form.getBoardMember1() != null) {
				this.boardHearingService.createBoardHearingParticipant(
						boardHearing, form.getBoardMember1(), ONE);
			}
			if (form.getBoardMember2() != null) {
				this.boardHearingService.createBoardHearingParticipant(
						boardHearing, form.getBoardMember2(), TWO);
			}
			if (form.getBoardMember3() != null) {
				this.boardHearingService.createBoardHearingParticipant(
						boardHearing, form.getBoardMember3(), THREE);
			}
			this.processItems(form.getBoardHearingNoteItems(), boardHearing);
			
			return new ModelAndView(String.format(LIST_REDIRECT_VIEW_NAME,
					paroleEligibility.getOffender().getId()));
		}
	}
	
	/**
	 * Returns the model and view for editing a Board Hearing.
	 * 
	 * @param boardHearing - Board Hearing to edit
	 * @return Model And View for editing a Board Hearing
	 */
	@RequestMapping(value = "/edit.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('BOARD_HEARING_EDIT') "
			+ "or hasRole('ADMIN')")
	public ModelAndView edit(@RequestParam(
			value = "boardHearing", required = true)
		final BoardHearing boardHearing) {
		return this.prepareEditMav(boardHearing, this.prepareForm(boardHearing),
				new ModelMap());
	}
	
	/**
	 * Updates a Board Hearing and returns to the board hearing list screen.
	 * 
	 * @param boardHearing - Board Hearing being updated
	 * @param form - Board Hearing Form
	 * @param bindingResult - Binding Result
	 * @return ModelAndView - redirects to the board hearing list screen on
	 * successful Board Hearing update, or back to the creation screen on
	 * form error.
	 * @throws DuplicateEntityFoundException - When a Board Hearing,
	 * Board Hearing Note, or Board Hearing Participant already exist with the
	 * given properties.
	 */
	@RequestMapping(value = "/edit.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('BOARD_HEARING_EDIT') "
			+ "or hasRole('ADMIN')")
	public ModelAndView update(@RequestParam(
			value = "boardHearing", required = true)
		final BoardHearing boardHearing,
		final BoardHearingForm form, final BindingResult bindingResult)
				throws DuplicateEntityFoundException {
		
		this.boardHearingFormValidator.validate(form, bindingResult);
		
		if (bindingResult.hasErrors()) {
			return this.prepareEditMav(boardHearing, form, new ModelMap());
		} else {
			CancellationCategory reason = null;
			/*if (form.getCancelled()) {
				reason = form.getReason();
			}*/
			this.boardHearingService
					.updateBoardHearing(boardHearing,
							form.getParoleBoardItinerary(),
							form.getHearingDate(),
							boardHearing.getParoleEligibility(),
							form.getBoardHearingCategory(), reason,
							form.getVideoConference());
			if (form.getBoardMember1() != null) {
				if (form.getBoardHearingParticipant1() != null) {
					this.boardHearingService.updateBoardHearingParticipant(
							form.getBoardHearingParticipant1(), boardHearing,
							form.getBoardMember1(), ONE);
				} else {
					this.boardHearingService.createBoardHearingParticipant(
							boardHearing, form.getBoardMember1(), ONE);
				}
			} else if (form.getBoardHearingParticipant1() != null) {
				this.boardHearingService.removeBoardHearingParticipant(
						form.getBoardHearingParticipant1());
			}
			if (form.getBoardMember2() != null) {
				if (form.getBoardHearingParticipant2() != null) {
					this.boardHearingService.updateBoardHearingParticipant(
							form.getBoardHearingParticipant2(), boardHearing,
							form.getBoardMember2(), TWO);
				} else {
					this.boardHearingService.createBoardHearingParticipant(
							boardHearing, form.getBoardMember2(), TWO);
				}
			} else if (form.getBoardHearingParticipant2() != null) {
				this.boardHearingService.removeBoardHearingParticipant(
						form.getBoardHearingParticipant2());
			}
			if (form.getBoardMember3() != null) {
				if (form.getBoardHearingParticipant3() != null) {
					this.boardHearingService.updateBoardHearingParticipant(
							form.getBoardHearingParticipant3(), boardHearing,
							form.getBoardMember3(), THREE);
				} else {
					this.boardHearingService.createBoardHearingParticipant(
							boardHearing, form.getBoardMember3(), THREE);
				}
			} else if (form.getBoardHearingParticipant3() != null) {
				this.boardHearingService.removeBoardHearingParticipant(
						form.getBoardHearingParticipant3());
			}
			this.processItems(form.getBoardHearingNoteItems(), boardHearing);
			
			return new ModelAndView(String.format(LIST_REDIRECT_VIEW_NAME,
					boardHearing.getParoleEligibility().getOffender().getId()));
		}
	}
	
	/**
	 * Removes the specified Board Hearing and redirects back to the Board
	 * Hearing list screen.
	 * 
	 * @param boardHearing - Board Hearing to remove.
	 * @return ModelAndView - redirects to the Board Hearing list screen.
	 */
	@RequestMapping(value = "/remove.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('BOARD_HEARING_EDIT') "
			+ "or hasRole('ADMIN')")
	public ModelAndView remove(@RequestParam(
			value = "boardHearing", required = true)
		final BoardHearing boardHearing) {
		for (BoardHearingNote note : this.boardHearingService
				.findBoardHearingNotesByHearing(boardHearing)) {
			this.boardHearingService.removeBoardHearingNote(note);
		}
		for (BoardHearingParticipant participant : this.boardHearingService
				.findBoardHearingParticipantsByHearing(boardHearing)) {
			this.boardHearingService.removeBoardHearingParticipant(participant);
		}
		this.boardHearingService.removeBoardHearing(boardHearing);
		return new ModelAndView(String.format(LIST_REDIRECT_VIEW_NAME,
				boardHearing.getParoleEligibility().getOffender().getId()));
	}
	
	/**
	 * Returns the model and view for a Board Hearing Note Item Row.
	 * 
	 * @param boardHearingNoteItemIndex - Integer Board Hearing Note Item Index
	 * @return ModelAndView - model and view for a Board Hearing Note Item Row.
	 */
	@RequestMapping(value = "createBoardHearingNoteItem.html",
			method = RequestMethod.GET)
	public ModelAndView displayBoardHearingNoteItemRow(@RequestParam(
			value = "boardHearingNoteItemIndex", required = true)
			final Integer boardHearingNoteItemIndex) {
		ModelMap map = new ModelMap();
		BoardHearingNoteItem noteItem = new BoardHearingNoteItem();
		noteItem.setItemOperation(BoardHearingItemOperation.CREATE);
		map.addAttribute(BOARD_HEARING_NOTE_ITEM_MODEL_KEY, noteItem);
		map.addAttribute(BOARD_HEARING_NOTE_ITEM_INDEX_MODEL_KEY,
				boardHearingNoteItemIndex);
		return new ModelAndView(BOARD_HEARING_NOTE_ITEM_ROW_VIEW_NAME, map);
	}
	
	/**
	 * Returns the model and view for board hearing action menu.
	 * 
	 * @param paroleEligibility - Parole Eligibility
	 * @return ModelAndView - model and view for board hearing action menu
	 */
	@RequestMapping(value = "/boardHearingActionMenu.html", 
			method = RequestMethod.GET)
	public ModelAndView displayBoardHearingActionMenu(@RequestParam(
			value = "paroleEligibility", required = true)
			final ParoleEligibility paroleEligibility, @RequestParam(
			value = "boardHearing", required = true)
			final BoardHearing boardHearing) {
		ModelMap map = new ModelMap();
		map.addAttribute(PAROLE_ELIGIBILITY_MODEL_KEY, paroleEligibility);
		map.addAttribute(BOARD_HEARING_MODEL_KEY, boardHearing);
		
		return new ModelAndView(BOARD_HEARING_ACTION_MENU_VIEW_NAME, map);
	}
	
	/**
	 * Returns a model and view for board hearing note items action menu.
	 * 
	 * @return ModelAndView - model and view for board hearing note items
	 * action menu
	 */
	@RequestMapping(value = "boardHearingNoteItemsActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView displayBoardHearingNoteItemsActionMenu() {
		ModelMap map = new ModelMap();	
		return new ModelAndView(
				BOARD_HEARING_NOTE_ITEMS_ACTION_MENU_VIEW_NAME, map);
	}
	
	/* AJAX views */
	
	/**
	 * Returns the Model And View for Board Meeting Site options for the
	 * selected Parole Board Itinerary. 
	 * 
	 * @param paroleBoardItinerary - Parole Board Itinerary.
	 * @return Model And View for Board Meeting Site options for the
	 * selected Parole Board Itinerary. 
	 */
	@RequestMapping(value = "/showMeetingSiteOptions.html",
			method = RequestMethod.GET)
	public ModelAndView showMeetingSiteOptions(
			@RequestParam(value = "paroleBoardItinerary", required = true)
			final ParoleBoardItinerary paroleBoardItinerary) {
		ModelMap map = new ModelMap();
		
		map.addAttribute(BOARD_MEETING_SITES_MODEL_KEY,
				this.boardHearingService
				.findBoardMeetingSitesByBoardItinerary(paroleBoardItinerary));
		
		return new ModelAndView(HEARING_LOCATION_OPTIONS_VIEW_NAME, map);
	}
	
	/**
	 * Returns the Model And View for Board Member options for the selected
	 * Parole Board Itinerary.
	 * 
	 * @param paroleBoardItinerary - Parole Board Itinerary
	 * @return Model And View for Board Member options for the selected
	 * Parole Board Itinerary.
	 */
	@RequestMapping(value = "/showBoardMemberOptions.html",
			method = RequestMethod.GET)
	public ModelAndView showBoardMemberOptions(
			@RequestParam(value = "paroleBoardItinerary", required = true)
			final ParoleBoardItinerary paroleBoardItinerary) {
		ModelMap map = new ModelMap();
		List<BoardAttendee> boardAttendees = this.boardHearingService
				.findBoardAttendeesByBoardItinerary(paroleBoardItinerary);
		
		for (BoardAttendee boardAttendee : boardAttendees) {
			if (boardAttendee.getNumber() != null) {
				if (boardAttendee.getNumber().equals(ONE)) {
					map.addAttribute(BOARD_MEMBER_ONE_MODEL_KEY,
							boardAttendee.getBoardMember());
				} else if (boardAttendee.getNumber().equals(TWO)) {
					map.addAttribute(BOARD_MEMBER_TWO_MODEL_KEY,
							boardAttendee.getBoardMember());
				} else if (boardAttendee.getNumber().equals(THREE)) {
					map.addAttribute(BOARD_MEMBER_THREE_MODEL_KEY,
							boardAttendee.getBoardMember());
				} else {
					System.out.println("something");
				}
			}
		}
		
		map.addAttribute(BOARD_ATTENDEES_MODEL_KEY,
				this.boardHearingService
				.findBoardAttendeesByBoardItinerary(paroleBoardItinerary));
		
		return new ModelAndView(BOARD_MEMBERS_VIEW_NAME, map);
	}
	
	/* Helper Methods */
	
	/**
	 * Processes Board Hearing Note items for creation, updating, or removal of
	 * Board Hearing Notes.
	 * 
	 * @param boardHearingNoteItems - List of Board Hearing Note Items to be
	 * processed
	 * @param boardHearing - Board Hearing associated with the Board Hearing
	 * Notes.
	 * @throws DuplicateEntityFoundException - When a board hearing note already
	 * exists with the given date and description for the specified board
	 * hearing. 
	 */
	private void processItems(
			final List<BoardHearingNoteItem> boardHearingNoteItems,
			final BoardHearing boardHearing)
					throws DuplicateEntityFoundException {
		if (boardHearingNoteItems != null) {
			for (BoardHearingNoteItem item : boardHearingNoteItems) {
				if (BoardHearingItemOperation.CREATE.equals(
						item.getItemOperation())) {
					this.boardHearingService.createBoardHearingNote(
							boardHearing, item.getDescription(),
							item.getDate());
				} else if (BoardHearingItemOperation.UPDATE.equals(
						item.getItemOperation())) {
					if (this.isNoteChanged(item.getBoardHearingNote(),
							item.getDate(), item.getDescription())) {
						this.boardHearingService.updateBoardHearingNote(
								item.getBoardHearingNote(),
								item.getDescription(), item.getDate());
					}
				} else if (BoardHearingItemOperation.REMOVE.equals(
						item.getItemOperation())) {
					this.boardHearingService.removeBoardHearingNote(
							item.getBoardHearingNote());
				}
			}
		}
	}
	
	/**
	 * Checks if a Board Hearing Note has been changed and returns true if it
	 * has.
	 * 
	 * @param note - Board Hearing Note to check for change
	 * @param date - Note Date from Form
	 * @param value - Note Value from Form
	 * @return Boolean - true if either the form's note date or value is
	 * different from the original Note's, false otherwise.
	 */
	private boolean isNoteChanged(final BoardHearingNote note,
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
	 * Prepares the Model And View for creating/updating a Board Hearing.
	 * 
	 * @param eligibility parole eligibility
	 * @param boardHearingForm - Board Hearing Form
	 * @param map - Model Map
	 * @return Prepared Model And View for creating/updating a Board Hearing.
	 */
	private ModelAndView prepareEditMav(final ParoleEligibility eligibility, 
			final BoardHearingForm form, final ModelMap map) {
		ParoleBoardItinerary paroleBoardItinerary =
				form.getParoleBoardItinerary();
		map.addAttribute(BOARD_ATTENDEES_MODEL_KEY,
				this.boardHearingService
				.findBoardAttendeesByBoardItinerary(
						paroleBoardItinerary));
		map.addAttribute(PAROLE_ELIGIBILITY_MODEL_KEY, eligibility);
		map.addAttribute(BOARD_HEARING_NOTE_ITEM_INDEX_MODEL_KEY,
				form.getBoardHearingNoteItems().size());
		if (form.getHearingDate() != null) {
			map.addAttribute(PAROLE_ITINERARIES_MODEL_KEY, this
					.boardHearingService.findItinerariesByEffectiveDate(
							form.getHearingDate()));
		} else if (paroleBoardItinerary != null) {
			map.addAttribute(PAROLE_ITINERARIES_MODEL_KEY, this
					.boardHearingService.findItinerariesByEffectiveDate(
							paroleBoardItinerary.getDateRange().getStartDate()));
		}
		map.addAttribute(BOARD_HEARING_CATEGORIES_MODEL_KEY,
				 this.boardHearingService
				 .findBoardHearingCategoriesByAppearanceCategory(
						 eligibility.getAppearanceCategory()));
		map.addAttribute(CANCELLATION_CATEGORIES,
				CancellationCategory.values());
		if (paroleBoardItinerary != null) {
			if (paroleBoardItinerary.getParoleBoardLocation() != null) {
				form.setVideoConferenceCapable(paroleBoardItinerary
						.getParoleBoardLocation().getVideoConferenceCapable());
			}
		}
		map.addAttribute(BOARD_HEARING_FORM_MODEL_KEY, form);
		map.addAttribute(OFFENDER_MODEL_KEY, eligibility.getOffender());
		this.offenderSummaryModelDelegate.add(map, eligibility.getOffender());
		
		return new ModelAndView(EDIT_VIEW_NAME, map);
	}
	
	/**
	 * Returns a prepared model and view for editing a board hearing.
	 * 
	 * @param boardHearing - Board Hearing
	 * @param form - Board Hearing Form
	 * @param map - Model Map
	 * @return Prepared model and view for editing a board hearing.
	 */
	private ModelAndView prepareEditMav(final BoardHearing boardHearing,
			final BoardHearingForm form, final ModelMap map) {
		map.addAttribute(BOARD_HEARING_MODEL_KEY, boardHearing);
		return this.prepareEditMav(boardHearing.getParoleEligibility(), form, 
				map);
	}
	
	/**
	 * Populated a Board Hearing Form from the specified Board Hearing.
	 * 
	 * @param boardHearing - Board Hearing
	 * @return Populated Board Hearing Form
	 */
	private BoardHearingForm prepareForm(final BoardHearing boardHearing) {
		BoardHearingForm form = new BoardHearingForm();
		List<BoardHearingParticipant> boardHearingParticipants =
				this.boardHearingService.findBoardHearingParticipantsByHearing(
						boardHearing);
		for (BoardHearingParticipant boardHearingParticipant
				: boardHearingParticipants) {
			if (boardHearingParticipant.getNumber().equals(ONE)) {
				form.setBoardHearingParticipant1(boardHearingParticipant);
				form.setBoardMember1(boardHearingParticipant.getBoardMember());
			} else if (boardHearingParticipant.getNumber().equals(TWO)) {
				form.setBoardHearingParticipant2(boardHearingParticipant);
				form.setBoardMember2(boardHearingParticipant.getBoardMember());
			} else if (boardHearingParticipant.getNumber().equals(THREE)) {
				form.setBoardHearingParticipant3(boardHearingParticipant);
				form.setBoardMember3(boardHearingParticipant.getBoardMember());
			}
		}
		List<BoardHearingNote> boardHearingNotes = this.boardHearingService
				.findBoardHearingNotesByHearing(boardHearing);
		List<BoardHearingNoteItem> boardHearingNoteItems =
				new ArrayList<BoardHearingNoteItem>();
		for (BoardHearingNote note : boardHearingNotes) {
			BoardHearingNoteItem item = new BoardHearingNoteItem();
			item.setBoardHearingNote(note);
			item.setDate(note.getDate());
			item.setDescription(note.getDescription());
			item.setItemOperation(BoardHearingItemOperation.UPDATE);
			boardHearingNoteItems.add(item);
		}
		form.setBoardHearingNoteItems(boardHearingNoteItems);
		form.setBoardHearingCategory(boardHearing.getCategory());
		form.setVideoConference(boardHearing.getVideoConference());
		form.setParoleBoardItinerary(boardHearing.getItinerary());
		form.setHearingDate(boardHearing.getHearingDate());
		/*if (boardHearing.getCancellation() != null) {
			form.setCancelled(true);
			form.setReason(boardHearing.getCancellation());
		}*/
		return form;
	}
	
	/**
	 * Populates a Board Hearing Form from the specified Parole Eligibility.
	 * 
	 * @param paroleEligibility - Parole Eligibility
	 * @return Populated Board Hearing Form
	 */
	private BoardHearingForm prepareForm(
			final ParoleEligibility paroleEligibility) {
		BoardHearingForm form = new BoardHearingForm();
		HearingAnalysis hearingAnalysis = this.boardHearingService
				.findHearingAnalysisByParoleEligibility(paroleEligibility);
		if (hearingAnalysis != null) {
			ParoleBoardItinerary paroleBoardItinerary =
					hearingAnalysis.getParoleBoardItinerary();
			List<BoardAttendee> boardAttendees = this.boardHearingService
					.findBoardAttendeesByBoardItinerary(
							paroleBoardItinerary);
			for (BoardAttendee boardAttendee : boardAttendees) {
				if (boardAttendee.getNumber() != null) {
					if (boardAttendee.getNumber().equals(ONE)) {
						form.setBoardMember1(
								boardAttendee.getBoardMember());
					} else if (boardAttendee.getNumber().equals(TWO)) {
						form.setBoardMember2(
								boardAttendee.getBoardMember());
					} else if (boardAttendee.getNumber().equals(THREE)) {
						form.setBoardMember3(
								boardAttendee.getBoardMember());
					}
				}
			}
			form.setParoleBoardItinerary(paroleBoardItinerary);
			form.setHearingDate(paroleBoardItinerary.getDateRange()
					.getStartDate());
			form.setVideoConference(paroleBoardItinerary
					.getParoleBoardLocation().getVideoConferenceCapable());
		}
		return form;
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
				BOARD_HEARING_EXISTS_MESSAGE_KEY, ERROR_BUNDLE_NAME, 
					exception);
	}
	
	/* Init binder. */
	
	/**
	 * Sets up init binder.
	 * 
	 * @param binder init binder
	 */
	@InitBinder
	protected void initBinder(final WebDataBinder binder) {
		binder.registerCustomEditor(Offender.class, 
				this.offenderPropertyEditorFactory
				.createOffenderPropertyEditor());
		binder.registerCustomEditor(BoardHearing.class,
				this.boardHearingPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(Location.class,
				this.locationPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(ParoleEligibility.class,
				this.paroleEligibilityPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(HearingAnalysis.class,
				this.hearingAnalysisPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(ParoleBoardItinerary.class,
				this.paroleBoardItineraryPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(BoardMeetingSite.class,
				this.boardMeetingSitePropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(BoardAttendee.class,
				this.boardAttendeePropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(ParoleBoardMember.class,
				this.paroleBoardMemberPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(BoardHearingNote.class,
				this.boardHearingNotePropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(BoardHearingParticipant.class,
				this.boardHearingParticipantPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(BoardHearingCategory.class,
				this.boardHearingCategoryPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(
				Date.class, this.customDateEditorFactory
				.createCustomDateOnlyEditor(true));
		binder.registerCustomEditor(Unit.class,
				this.unitPropertyEditorFactory.createPropertyEditor());
	}
}