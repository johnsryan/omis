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
package omis.hearingparticipant.web.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import omis.beans.factory.PropertyEditorFactory;
import omis.boardhearing.domain.BoardHearing;
import omis.boardhearing.web.controller.delegate.BoardHearingSummaryModelDelegate;
import omis.hearingparticipant.domain.HearingParticipant;
import omis.hearingparticipant.report.HearingParticipantSummary;
import omis.hearingparticipant.report.HearingParticipantSummaryReportService;
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;

/**
 * Hearing Participant Report Controller.
 * 
 *@author Annie Wahl 
 *@version 0.1.0 (Jan 17, 2018)
 *@since OMIS 3.0
 *
 */
@Controller
@RequestMapping("/hearingParticipant/")
@PreAuthorize("hasRole('USER')")
public class HearingParticipantReportController {
	
/* View names */
	
	private static final String LIST_VIEW_NAME = "/hearingParticipant/list";
	
	private static final String HEARING_PARTICIPANTS_ACTION_MENU_VIEW_NAME =
			"/hearingParticipant/includes/hearingParticipantsActionMenu";
	
	private static final String HEARING_PARTICIPANTS_ROW_ACTION_MENU_VIEW_NAME =
			"/hearingParticipant/includes/hearingParticipantsRowActionMenu";
	
	/* Model Keys */
	
	private static final String OFFENDER_MODEL_KEY = "offender";

	private static final String HEARING_PARTICIPANT_SUMMARIES_MODEL_KEY =
			"hearingParticipantSummaries";
	
	private static final String HEARING_PARTICIPANT_MODEL_KEY =
			"hearingParticipant";
	
	private static final String BOARD_HEARING_MODEL_KEY = "boardHearing";
	
	/* Service */
	
	@Autowired
	@Qualifier("hearingParticipantSummaryReportService")
	private HearingParticipantSummaryReportService
				hearingParticipantSummaryReportService;
	
	/* Property editors */
	
	@Autowired
	@Qualifier("hearingParticipantPropertyEditorFactory")
	private PropertyEditorFactory hearingParticipantPropertyEditorFactory;
	
	@Autowired
	@Qualifier("boardHearingPropertyEditorFactory")
	private PropertyEditorFactory boardHearingPropertyEditorFactory;
	
	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory offenderPropertyEditorFactory;
	
	/* Delegates */
	
	@Autowired
	@Qualifier("offenderSummaryModelDelegate")
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;
	
	@Autowired
	@Qualifier("boardHearingSummaryModelDelegate")
	private BoardHearingSummaryModelDelegate boardHearingSummaryModelDelegate;
	
	/**
	 * Default constructor for Hearing Participant Report Controller.
	 */
	public HearingParticipantReportController() {
	}
	
	/**
	 * Returns model and view for hearing participants list.
	 * @param boardHearing - Board Hearing
	 * @return ModelAndView - model and view for hearing participants list
	 */
	@RequestMapping(value = "/list.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ADMIN') or hasRole ('HEARING_PARTICIPANT_LIST')")
	public ModelAndView list(@RequestParam(value = "boardHearing",
		required = true) final BoardHearing boardHearing) {
		
		ModelMap map = new ModelMap();
		
		List<HearingParticipantSummary> summaries =
				this.hearingParticipantSummaryReportService
				.findHearingParticipantSummariesByHearing(boardHearing);
		map.addAttribute(BOARD_HEARING_MODEL_KEY, boardHearing);
		map.addAttribute(HEARING_PARTICIPANT_SUMMARIES_MODEL_KEY, summaries);
		map.addAttribute(OFFENDER_MODEL_KEY,
				boardHearing.getParoleEligibility().getOffender());
		this.offenderSummaryModelDelegate.add(map,
				boardHearing.getParoleEligibility().getOffender());
		this.boardHearingSummaryModelDelegate.add(map, boardHearing);
		
		return new ModelAndView(LIST_VIEW_NAME, map);
	}
	
	/**
	 * Displays model and view for hearing participants action menu.
	 * @param boardHearing - Board Hearing
	 * @return ModelAndView - model and view for hearing participants
	 * action menu
	 */
	@RequestMapping(value = "/hearingParticipantsActionMenu.html", 
			method = RequestMethod.GET)
	public ModelAndView displayHearingParticipantsActionMenu(
			@RequestParam(value = "boardHearing", required = true)
			final BoardHearing boardHearing) {
		ModelMap map = new ModelMap();
		map.addAttribute(BOARD_HEARING_MODEL_KEY, boardHearing);
		
		return new ModelAndView(
				HEARING_PARTICIPANTS_ACTION_MENU_VIEW_NAME, map);
	}
		
	/**
	 * Displays model and view for hearing participants row action menu.
	 * @param hearingParticipant - Hearing Participant
	 * @return ModelAndView - model and view for hearings row action menu
	 */
	@RequestMapping(value = "/hearingParticipantsRowActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView displayHearingParticipantsRowActionMenu(
			@RequestParam(value = "hearingParticipant", required = true)
			final HearingParticipant hearingParticipant) {
		ModelMap map = new ModelMap();
		map.addAttribute(HEARING_PARTICIPANT_MODEL_KEY, hearingParticipant);
		
		return new ModelAndView(
				HEARING_PARTICIPANTS_ROW_ACTION_MENU_VIEW_NAME, map);
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
		binder.registerCustomEditor(HearingParticipant.class,
				this.hearingParticipantPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(BoardHearing.class,
				this.boardHearingPropertyEditorFactory
				.createPropertyEditor());
	}
}
