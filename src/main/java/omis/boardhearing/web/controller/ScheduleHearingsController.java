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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
import omis.boardhearing.domain.BoardHearingParticipant;
import omis.boardhearing.exception.BoardHearingExistsException;
import omis.boardhearing.exception.BoardHearingParticipantExistsException;
import omis.boardhearing.service.ScheduleHearingService;
import omis.boardhearing.web.form.BoardHearingItem;
import omis.boardhearing.web.form.BoardHearingItemOperation;
import omis.boardhearing.web.form.ScheduleHearingsForm;
import omis.boardhearing.web.validator.ScheduleHearingsFormValidator;
import omis.hearinganalysis.domain.HearingAnalysis;
import omis.paroleboarditinerary.domain.BoardAttendee;
import omis.paroleboarditinerary.domain.ParoleBoardItinerary;
import omis.paroleboarditinerary.report.ParoleBoardItinerarySummaryReportService;
import omis.paroleboardmember.domain.ParoleBoardMember;
import omis.paroleeligibility.domain.ParoleEligibility;
import omis.paroleeligibility.report.ParoleEligibilityReportService;
import omis.paroleeligibility.report.ParoleEligibilitySummary;
import omis.web.controller.delegate.BusinessExceptionHandlerDelegate;

/**
 * Schedule Hearings Controller.
 * 
 *@author Annie Wahl 
 *@version 0.1.0 (Jun 28, 2018)
 *@since OMIS 3.0
 *
 */
@Controller
@RequestMapping("/boardHearing/schedule")
@PreAuthorize("hasRole('USER')")
public class ScheduleHearingsController {
	
	/* View Names */
	
	private static final String EDIT_VIEW_NAME = "/boardHearing/schedule/edit";
	
	private static final String SCHEDULE_HEARINGS_ACTION_MENU_VIEW_NAME =
			"/boardHearing/schedule/includes/scheduleHearingsActionMenu";
	
	private static final String ITINERARY_LIST_REDIRECT =
			"redirect:/paroleBoardItinerary/list.html?offender=%d";
	
	/* Model Keys */
	
	private static final String PAROLE_BOARD_ITINERARY_MODEL_KEY =
			"paroleBoardItinerary";
	
	private static final String ELIGIBILITY_SUMMARY_MAP_MODEL_KEY =
			"eligibilitySummaryMap";
	
	private static final String PAROLE_BOARD_ITINERARY_SUMMARY_MODEL_KEY =
			"paroleBoardItinerarySummary";

	private static final String BOARD_ATTENDEES_MODEL_KEY = "boardAttendees";
	
	private static final Long THREE = (long) 3;
	
	private static final Long TWO = (long) 2;
	
	private static final Long ONE = (long) 1;
	
	private static final String FORM_MODEL_KEY = "scheduleHearingsForm";
	
	/* Bundles */
	
	private static final String ERROR_BUNDLE_NAME =
			"omis.boardhearing.msgs.form";
	
	/* Message Keys */
	
	private static final String BOARD_HEARING_EXISTS_MESSAGE_KEY =
			"boardHearing.exists";

	private static final String BOARD_HEARING_PARTICPANT_EXISTS_MESSAGE_KEY =
			"boardHearingParticipant.exists";
	
	/* Property Editor Factories */
	
	@Autowired
	@Qualifier("boardHearingPropertyEditorFactory")
	private PropertyEditorFactory boardHearingPropertyEditorFactory;
	
	@Autowired
	@Qualifier("paroleBoardItineraryPropertyEditorFactory")
	private PropertyEditorFactory paroleBoardItineraryPropertyEditorFactory;
	
	@Autowired
	@Qualifier("paroleEligibilityPropertyEditorFactory")
	private PropertyEditorFactory paroleEligibilityPropertyEditorFactory;
	
	@Autowired
	@Qualifier("paroleBoardMemberPropertyEditorFactory")
	private PropertyEditorFactory paroleBoardMemberPropertyEditorFactory;
	
	@Autowired
	@Qualifier("boardHearingParticipantPropertyEditorFactory")
	private PropertyEditorFactory boardHearingParticipantPropertyEditorFactory;
	
	@Autowired
	@Qualifier("boardHearingCategoryPropertyEditorFactory")
	private PropertyEditorFactory boardHearingCategoryPropertyEditorFactory;

	@Autowired
	private CustomDateEditorFactory customDateEditorFactory;
	
	/* Helpers */
	
	@Autowired
	@Qualifier("businessExceptionHandlerDelegate")
	private BusinessExceptionHandlerDelegate businessExceptionHandlerDelegate;
	
	/* Services. */
	
	@Autowired
	@Qualifier("scheduleHearingService")
	private ScheduleHearingService scheduleHearingService;
	
	@Autowired
	@Qualifier("paroleEligibilityReportService")
	private ParoleEligibilityReportService paroleEligibilityReportService;
	
	@Autowired
	@Qualifier("paroleBoardItinerarySummaryReportService")
	private ParoleBoardItinerarySummaryReportService
				paroleBoardItinerarySummaryReportService;

	/* Validator. */
	
	@Autowired
	@Qualifier("scheduleHearingsFormValidator")
	private ScheduleHearingsFormValidator scheduleHearingsFormValidator;
	
	/**
	 * Default constructor for Schedule Hearings Controller.
	 */
	public ScheduleHearingsController() {
		//Default controller.
	}
	
	/**
	 * Returns the Model and View for scheduling Board Hearings for the
	 * specified Parole Board Itinerary.
	 * 
	 * @param itinerary - Parole Board Itinerary
	 * @return Model and View for scheduling Board Hearings for the
	 * specified Parole Board Itinerary.
	 */
	@RequestMapping(value = "/edit.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('BOARD_HEARING_CREATE') "
			+ "or hasRole('ADMIN')")
	public ModelAndView edit(@RequestParam(value = "paroleBoardItinerary")
			final ParoleBoardItinerary itinerary) {
		return this.prepareEditMav(itinerary, this.prepareForm(itinerary));
	}
	
	/**
	 * Attempts to create/update the selected Board Hearings and related
	 * Board Hearing Participants and returns to the Parole Board Itinerary
	 * listing screen.
	 * 
	 * @param itinerary - Parole Board Itinerary
	 * @param form - Schedule Hearings Form
	 * @param bindingResult - Binding Result
	 * @return ModelAndView - Parole Board Itinerary listing screen after
	 * successful form processing, or returns to the edit screen on form error.
	 * @throws BoardHearingExistsException - When a Board Hearing already
	 * exists for its given Parole Eligibility.
	 * @throws BoardHearingParticipantExistsException - When a Board Hearing
	 * Participant with the given Board Member already exists for the given
	 * Board Hearing.
	 */
	@RequestMapping(value = "/edit.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('BOARD_HEARING_CREATE') "
			+ "or hasRole('ADMIN')")
	public ModelAndView update(@RequestParam(value = "paroleBoardItinerary")
			final ParoleBoardItinerary itinerary,
			final ScheduleHearingsForm form,
			final BindingResult bindingResult)
					throws BoardHearingExistsException,
					BoardHearingParticipantExistsException {
		this.scheduleHearingsFormValidator.validate(form, bindingResult);
		if (bindingResult.hasErrors()) {
			return this.prepareEditMav(itinerary, form);
		} else {
			for (BoardHearingItem item : form.getBoardHearingItems()) {
				if(item.getSelected()) {
					BoardHearing boardHearing = item.getBoardHearing();
					if (boardHearing == null) {
						boardHearing = this.scheduleHearingService
								.createBoardHearing(itinerary,
									item.getHearingDate(),
									item.getParoleEligibility(),
									item.getCategory(),
									item.getVideoConference());
						if (item.getBoardMember1() != null) {
							this.scheduleHearingService
								.createBoardHearingParticipant(boardHearing,
										item.getBoardMember1(), ONE);
						}
						if (item.getBoardMember2() != null) {
							this.scheduleHearingService
								.createBoardHearingParticipant(boardHearing,
										item.getBoardMember2(), TWO);
						}
						if (item.getBoardMember3() != null) {
							this.scheduleHearingService
								.createBoardHearingParticipant(boardHearing,
										item.getBoardMember3(), THREE);
						}
					} else {
						this.scheduleHearingService.updateBoardHearing(
								boardHearing, itinerary,
								item.getHearingDate(),
								item.getParoleEligibility(), item.getCategory(),
								item.getVideoConference());
						
						if (item.getBoardMember1() != null) {
							if (item.getBoardHearingParticipant1() != null) {
								this.scheduleHearingService
									.updateBoardHearingParticipant(
										item.getBoardHearingParticipant1(),
										boardHearing, item.getBoardMember1(),
										ONE);
							} else {
								this.scheduleHearingService
									.createBoardHearingParticipant(boardHearing,
											item.getBoardMember1(), ONE);
							}
						} else if (item.getBoardHearingParticipant1() != null) {
							this.scheduleHearingService
								.removeBoardHearingParticipant(
									item.getBoardHearingParticipant1());
						}
						if (item.getBoardMember2() != null) {
							if (item.getBoardHearingParticipant2() != null) {
								this.scheduleHearingService
									.updateBoardHearingParticipant(
										item.getBoardHearingParticipant2(),
										boardHearing, item.getBoardMember2(),
										TWO);
							} else {
								this.scheduleHearingService
									.createBoardHearingParticipant(boardHearing,
											item.getBoardMember2(),
											TWO);
							}
						} else if (item.getBoardHearingParticipant2() != null) {
							this.scheduleHearingService
								.removeBoardHearingParticipant(
									item.getBoardHearingParticipant2());
						}
						if (item.getBoardMember3() != null) {
							if (item.getBoardHearingParticipant3() != null) {
								this.scheduleHearingService
									.updateBoardHearingParticipant(
										item.getBoardHearingParticipant3(),
										boardHearing, item.getBoardMember3(),
										THREE);
							} else {
								this.scheduleHearingService
									.createBoardHearingParticipant(boardHearing,
											item.getBoardMember3(), THREE);
							}
						} else if (item.getBoardHearingParticipant3() != null) {
							this.scheduleHearingService
								.removeBoardHearingParticipant(
									item.getBoardHearingParticipant3());
						}
					}
				}
			}
		}
		return new ModelAndView(String.format(ITINERARY_LIST_REDIRECT,
				itinerary.getId()));
		
	}
	
	/**
	 * Returns the model and view for schedule hearings action menu.
	 * 
	 * @return ModelAndView - model and view for schedule hearings action menu
	 */
	@RequestMapping(value = "/scheduleHearingsActionMenu.html", 
			method = RequestMethod.GET)
	public ModelAndView displayScheduleHearingsActionMenu() {
		ModelMap map = new ModelMap();
		return new ModelAndView(SCHEDULE_HEARINGS_ACTION_MENU_VIEW_NAME, map);
	}
	
	/* Helper Methods */
	
	private ModelAndView prepareEditMav(final ParoleBoardItinerary itinerary,
			final ScheduleHearingsForm form) {
		ModelMap map = new ModelMap();

		Map<ParoleEligibilitySummary, List<BoardHearingCategory>>
			eligibilitySummaryMap = new LinkedHashMap<ParoleEligibilitySummary,
				List<BoardHearingCategory>>();
		for (int i = 0; i < form.getBoardHearingItems().size(); i++) {
			ParoleEligibility eligibility = form.getBoardHearingItems().get(i)
					.getParoleEligibility();
			if(eligibility != null) {
				ParoleEligibilitySummary summary =
						this.paroleEligibilityReportService
							.summarizeParoleEligibility(eligibility);
				eligibilitySummaryMap.put(summary, this.scheduleHearingService
						.findBoardHearingCategoriesByAppearanceCategory(
								eligibility.getAppearanceCategory()));
			}
		}

		map.addAttribute(ELIGIBILITY_SUMMARY_MAP_MODEL_KEY,
				eligibilitySummaryMap);
		map.addAttribute(BOARD_ATTENDEES_MODEL_KEY, this.scheduleHearingService
				.findBoardAttendeesByBoardItinerary(itinerary));
		map.addAttribute(FORM_MODEL_KEY, form);
		map.addAttribute(PAROLE_BOARD_ITINERARY_MODEL_KEY, itinerary);
		map.addAttribute(PAROLE_BOARD_ITINERARY_SUMMARY_MODEL_KEY,
				this.paroleBoardItinerarySummaryReportService
				.summarize(itinerary));
		
		return new ModelAndView(EDIT_VIEW_NAME, map);
	}
	
	private ScheduleHearingsForm prepareForm(
			final ParoleBoardItinerary itinerary) {
		ScheduleHearingsForm form = new ScheduleHearingsForm();
		List<BoardHearingItem> items = new ArrayList<BoardHearingItem>();
		
		for(ParoleEligibility eligibility :
				this.scheduleHearingService
					.findUnscheduledParoleEligibilitiesIncluding(itinerary)) {
			BoardHearingItem item = new BoardHearingItem();
			item.setParoleEligibility(eligibility);
			BoardHearing boardHearing = this.scheduleHearingService
					.findBoardHearingByParoleEligibility(eligibility);
			if(boardHearing != null) {
				item.setBoardHearing(boardHearing);
				item.setCategory(boardHearing.getCategory());
				item.setHearingDate(boardHearing.getHearingDate());
				item.setVideoConference(boardHearing.getVideoConference());
				item.setSelected(true);
				item.setItemOperation(BoardHearingItemOperation.UPDATE);
				for (BoardHearingParticipant participant :
					this.scheduleHearingService
					.findBoardHearingParticipantsByHearing(boardHearing)) {
					if (participant.getNumber().equals(ONE)) {
						item.setBoardHearingParticipant1(participant);
						item.setBoardMember1(participant.getBoardMember());
					} else if (participant.getNumber().equals(TWO)) {
						item.setBoardHearingParticipant2(participant);
						item.setBoardMember2(participant.getBoardMember());
					} else if (participant.getNumber().equals(THREE)) {
						item.setBoardHearingParticipant3(participant);
						item.setBoardMember3(participant.getBoardMember());
					}
				}
			} else {
				HearingAnalysis hearingAnalysis = this.scheduleHearingService
						.findHearingAnalysisByParoleEligibility(eligibility);
				if (hearingAnalysis != null) {
					List<BoardAttendee> boardAttendees =
							this.scheduleHearingService
							.findBoardAttendeesByBoardItinerary(itinerary);
					for (BoardAttendee boardAttendee : boardAttendees) {
						if (boardAttendee.getNumber() != null) {
							if (boardAttendee.getNumber().equals(ONE)) {
								item.setBoardMember1(
										boardAttendee.getBoardMember());
							} else if (boardAttendee.getNumber().equals(TWO)) {
								item.setBoardMember2(
										boardAttendee.getBoardMember());
							} else if (
									boardAttendee.getNumber().equals(THREE)) {
								item.setBoardMember3(
										boardAttendee.getBoardMember());
							}
						}
					}
					item.setHearingDate(itinerary.getDateRange()
							.getStartDate());
					item.setVideoConference(itinerary.getParoleBoardLocation()
							.getVideoConferenceCapable());
				}
				item.setSelected(false);
				item.setItemOperation(BoardHearingItemOperation.CREATE);
			}
			items.add(item);
		}
		form.setBoardHearingItems(items);
		form.setScheduleAsGroup(false);
		return form;
	}
	
	/**
	 * Handles Board Hearing Exists Exceptions.
	 * 
	 * @param exception - Board Hearings Exists Exception
	 * @return Model and View for displaying the exception explanation.
	 */
	@ExceptionHandler(BoardHearingExistsException.class)
	public ModelAndView handleDuplicateEntityFoundException(
			final BoardHearingExistsException exception) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				BOARD_HEARING_EXISTS_MESSAGE_KEY, ERROR_BUNDLE_NAME, exception);
	}
	
	/**
	 * Handles Board Hearing Participant Exists Exceptions.
	 * 
	 * @param exception - Board Hearing Participant Exists Exception
	 * @return Model and View for displaying the exception explanation.
	 */
	@ExceptionHandler(BoardHearingParticipantExistsException.class)
	public ModelAndView handleDuplicateEntityFoundException(
			final BoardHearingParticipantExistsException exception) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				BOARD_HEARING_PARTICPANT_EXISTS_MESSAGE_KEY, ERROR_BUNDLE_NAME,
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
		binder.registerCustomEditor(BoardHearing.class,
				this.boardHearingPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(ParoleEligibility.class,
				this.paroleEligibilityPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(ParoleBoardItinerary.class,
				this.paroleBoardItineraryPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(ParoleBoardMember.class,
				this.paroleBoardMemberPropertyEditorFactory
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
	}
	
}
