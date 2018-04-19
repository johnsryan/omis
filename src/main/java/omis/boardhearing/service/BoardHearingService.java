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
package omis.boardhearing.service;

import java.util.Date;
import java.util.List;

import omis.boardhearing.domain.BoardHearing;
import omis.boardhearing.domain.BoardHearingCategory;
import omis.boardhearing.domain.BoardHearingNote;
import omis.boardhearing.domain.BoardHearingParticipant;
import omis.boardhearing.domain.CancellationCategory;
import omis.exception.DuplicateEntityFoundException;
import omis.hearinganalysis.domain.HearingAnalysis;
import omis.paroleboarditinerary.domain.BoardAttendee;
import omis.paroleboarditinerary.domain.BoardMeetingSite;
import omis.paroleboarditinerary.domain.ParoleBoardItinerary;
import omis.paroleboardmember.domain.ParoleBoardMember;
import omis.paroleeligibility.domain.AppearanceCategory;
import omis.paroleeligibility.domain.ParoleEligibility;

/**
 * Board Hearing Service.
 * 
 * @author Annie Wahl 
 * @author Josh Divine
 * @version 0.1.2 (Apr 18, 2018)
 * @since OMIS 3.0
 */
public interface BoardHearingService {
	
	/**
	 * Creates a Board Hearing with the specified properties.
	 * 
	 * @param itinerary - Parole Board Itinerary
	 * @param hearingDate - Date
	 * @param paroleEligibility - Parole Eligibility
	 * @param category - Board Hearing Category
	 * @param cancellation - Cancellation Category
	 * @param videoConference - Boolean
	 * @return Newly Created Board Hearing
	 * @throws DuplicateEntityFoundException - When a Board Hearing already
	 * exists with the specified Parole Eligibility
	 */
	BoardHearing createBoardHearing(ParoleBoardItinerary itinerary,
			Date hearingDate, ParoleEligibility paroleEligibility, 
			BoardHearingCategory category, CancellationCategory cancellation, 
			Boolean videoConference) throws DuplicateEntityFoundException;
	
	/**
	 * Updates the specified Board Hearing with the given properties.
	 * 
	 * @param boardHearing - Board Hearing to update
	 * @param itinerary - Parole Board Itinerary
	 * @param hearingDate - Date
	 * @param paroleEligibility - Parole Eligibility
	 * @param category - Board Hearing Category
	 * @param cancellation - Cancellation Category
	 * @param videoConference - Boolean
	 * @return Updated Board Hearing
	 * @throws DuplicateEntityFoundException - When a Board Hearing already
	 * exists with the specified Parole Eligibility
	 */
	BoardHearing updateBoardHearing(BoardHearing boardHearing,
			ParoleBoardItinerary itinerary, Date hearingDate, 
			ParoleEligibility paroleEligibility, BoardHearingCategory category, 
			CancellationCategory cancellation, Boolean videoConference)
				throws DuplicateEntityFoundException;
	
	/**
	 * Removes the specified Board Hearing.
	 * 
	 * @param boardHearing - Board Hearing to remove
	 */
	void removeBoardHearing(BoardHearing boardHearing);
	
	/**
	 * Creates a Board Hearing Note with the specified properties.
	 * 
	 * @param hearing - Board Hearing
	 * @param description - String Description
	 * @param date - Date
	 * @return Newly Created Board Hearing Note
	 * @throws DuplicateEntityFoundException - When a Board Hearing Note already
	 * exists with the given description and date for the specified
	 * Board Hearing.
	 */
	BoardHearingNote createBoardHearingNote(BoardHearing hearing,
			String description, Date date)
				throws DuplicateEntityFoundException;
	
	/**
	 * Updates the specified Board Hearing Note with the given properties.
	 * 
	 * @param boardHearingNote - Board Hearing Note to update
	 * @param description - String Description
	 * @param date - Date
	 * @return Updated Board Hearing Note
	 * @throws DuplicateEntityFoundException - When a Board Hearing Note already
	 * exists with the given description and date for the specified
	 * Board Hearing.
	 */
	BoardHearingNote updateBoardHearingNote(BoardHearingNote boardHearingNote,
			String description, Date date)
				throws DuplicateEntityFoundException;
	
	/**
	 * Removes the specified Board Hearing Note.
	 * 
	 * @param boardHearingNote - Board Hearing Note to remove.
	 */
	void removeBoardHearingNote(BoardHearingNote boardHearingNote);
	
	/**
	 * Creates a Board Hearing Participant with the specified properties.
	 * 
	 * @param hearing - Board Hearing
	 * @param boardMember - Parole Board Member
	 * @param number - Long
	 * @return Newly Created Board Hearing Participant
	 * @throws DuplicateEntityFoundException - When a Board Hearing Participant
	 * already exists with the given Board Member for the specified
	 * Board Hearing.
	 */
	BoardHearingParticipant createBoardHearingParticipant(BoardHearing hearing,
			ParoleBoardMember boardMember, Long number)
				throws DuplicateEntityFoundException;
	
	/**
	 * Updates the specified Board Hearing Participant with the
	 * given properties.
	 * 
	 * @param boardHearingParticipant - Board Hearing Participant to update
	 * @param hearing - Board Hearing
	 * @param boardMember - Parole Board Member
	 * @param number - Long
	 * @return Updated Board Hearing Participant
	 * @throws DuplicateEntityFoundException - When a Board Hearing Participant
	 * already exists with the given Board Member for the specified
	 * Board Hearing.
	 */
	BoardHearingParticipant updateBoardHearingParticipant(
			BoardHearingParticipant boardHearingParticipant,
			BoardHearing hearing,
			ParoleBoardMember boardMember, Long number)
				throws DuplicateEntityFoundException;
	
	/**
	 * Removes the specified Board Hearing Participant.
	 * 
	 * @param boardHearingParticipant - Board Hearing Participant to be removed
	 */
	void removeBoardHearingParticipant(
			BoardHearingParticipant boardHearingParticipant);
	
	/**
	 * Returns a list of Board Hearing Categories by specified Appearance
	 * Category.
	 * 
	 * @param appearanceCategory - Appearance Category.
	 * @return List of all Board Hearing Categories by specified Appearance
	 * Category.
	 * */
	List<BoardHearingCategory>
		findBoardHearingCategoriesByAppearanceCategory(
				AppearanceCategory appearanceCategory);
	
	/**
	 * Returns a list of Board Hearing Notes from the specified Board Hearing.
	 * 
	 * @param hearing - Board Hearing
	 * @return List of Board Hearing Notes from the specified Board Hearing.
	 */
	List<BoardHearingNote> findBoardHearingNotesByHearing(BoardHearing hearing);
	
	/**
	 * Returns a list of Board Hearing Participants for the specified
	 * Board Hearing.
	 * 
	 * @param hearing - Board Hearing
	 * @return List of Board Hearing Participants for the specified
	 * Board Hearing.
	 */
	List<BoardHearingParticipant> findBoardHearingParticipantsByHearing(
			BoardHearing hearing);
	
	/**
	 * Returns a list of board attendees that match the specified parole 
	 * board itinerary.
	 * 
	 * @param boardItinerary parole board itinerary
	 * @return list of board attendees
	 */
	List<BoardAttendee> findBoardAttendeesByBoardItinerary(
			ParoleBoardItinerary boardItinerary);
	
	/**
	 * Returns a list of board meeting sites that match the specified parole 
	 * board itinerary.
	 * 
	 * @param boardItinerary parole board itinerary
	 * @return list of board meeting sites
	 */
	List<BoardMeetingSite> findBoardMeetingSitesByBoardItinerary(
			ParoleBoardItinerary boardItinerary);
	
	/**
	 * Returns the hearing analysis for the specified parole eligibility.
	 * 
	 * @param eligibility parole eligibility
	 * @return hearing analysis
	 */
	HearingAnalysis findHearingAnalysisByParoleEligibility(
			ParoleEligibility eligibility);
	
	/**
	 * Returns a Board Hearing with the specified Parole Eligibility.
	 * 
	 * @param paroleEligibility - Parole Eligibility
	 * @return Board Hearing with the specified Parole Eligibility.
	 */
	BoardHearing findBoardHearingByParoleEligibility(
			ParoleEligibility paroleEligibility);
	
	/**
	 * Returns a list of parole board itineraries after the specified date.
	 * 
	 * @param effectiveDate effective date
	 * @return list of parole board itineraries
	 */
	List<ParoleBoardItinerary> findItinerariesByEffectiveDate(
			Date effectiveDate);
}