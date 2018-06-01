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
package omis.hearinganalysis.service;

import java.util.Date;
import java.util.List;

import omis.exception.DuplicateEntityFoundException;
import omis.hearinganalysis.domain.HearingAnalysis;
import omis.hearinganalysis.domain.HearingAnalysisCategory;
import omis.hearinganalysis.domain.HearingAnalysisNote;
import omis.paroleboarditinerary.domain.BoardAttendee;
import omis.paroleboarditinerary.domain.BoardMeetingSite;
import omis.paroleboarditinerary.domain.ParoleBoardItinerary;
import omis.paroleeligibility.domain.ParoleEligibility;

/**
 * Service for hearing analysis.
 * 
 * @author Josh Divine
 * @author Annie Wahl
 * @version 0.1.3 (May 29, 2018)
 * @since OMIS 3.0
 */
public interface HearingAnalysisService {

	/**
	 * Creates a new hearing analysis.
	 * 
	 * @param eligibility parole eligibility
	 * @param paroleBoardItinerary parole board itinerary
	 * @param analyst board attendee
	 * @param category hearing analysis category
	 * @param expectedCompletionDate expected completion date
	 * @return hearing analysis
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	HearingAnalysis createHearingAnalysis(ParoleEligibility eligibility, 
			ParoleBoardItinerary paroleBoardItinerary, BoardAttendee analyst, 
			HearingAnalysisCategory category, Date expectedCompletionDate)
					throws DuplicateEntityFoundException;
	
	/**
	 * Updates an existing hearing analysis.
	 * 
	 * @param hearingAnalysis hearing analysis
	 * @param paroleBoardItinerary parole board itinerary
	 * @param analyst board attendee
	 * @param category hearing analysis category
	 * @param expectedCompletionDate expected completion date
	 * @return hearing analysis
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	HearingAnalysis updateHearingAnalysis(HearingAnalysis hearingAnalysis, 
			ParoleBoardItinerary paroleBoardItinerary, BoardAttendee analyst, 
			HearingAnalysisCategory category, Date expectedCompletionDate) 
					throws DuplicateEntityFoundException;
	
	/**
	 * Removes a hearing analysis.
	 * 
	 * @param hearingAnalysis hearing analysis
	 */
	void removeHearingAnalysis(HearingAnalysis hearingAnalysis);
	
	/**
	 * Creates a new hearing analysis note.
	 * 
	 * @param hearingAnalysis hearing analysis
	 * @param description description
	 * @param date date
	 * @return hearing analysis note
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	HearingAnalysisNote createHearingAnalysisNote(
			HearingAnalysis hearingAnalysis, String description, Date date) 
					throws DuplicateEntityFoundException;
	
	/**
	 * Updates an existing hearing analysis note.
	 * 
	 * @param hearingAnalysisNote hearing analysis note
	 * @param description description
	 * @param date date
	 * @return hearing analysis note
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	HearingAnalysisNote updateHearingAnalysisNote(
			HearingAnalysisNote hearingAnalysisNote, String description, 
			Date date) throws DuplicateEntityFoundException;
	
	/**
	 * Removes a hearing analysis note.
	 * 
	 * @param hearingAnalysisNote hearing analysis note
	 */
	void removeHearingAnalysisNote(HearingAnalysisNote hearingAnalysisNote);
	
	/**
	 * Returns a list of hearing analysis notes for the specified hearing 
	 * analysis.
	 * 
	 * @param hearingAnalysis hearing analysis
	 * @return list of hearing analysis notes
	 */
	List<HearingAnalysisNote> findHearingAnalysisNotesByHearingAnalysis(
			HearingAnalysis hearingAnalysis);
	
	/**
	 * Returns a list of parole board itineraries after the specified date.
	 * 
	 * @param effectiveDate effective date
	 * @return list of parole board itineraries
	 */
	List<ParoleBoardItinerary> findItinerariesByEffectiveDate(
			Date effectiveDate);
	
	/**
	 * Returns a list of board meeting sites for the specified parole board 
	 * itinerary.
	 * 
	 * @param itinerary parole board itinerary
	 * @return list of board meeting sites
	 */
	List<BoardMeetingSite> findBoardMeetingSitesByItinerary(
			ParoleBoardItinerary itinerary);

	/**
	 * Returns a list of board attendees for the specified parole board 
	 * itinerary.
	 * 
	 * @param itinerary parole board itinerary
	 * @return list of board attendees
	 */
	List<BoardAttendee> findBoardAttendeesByItinerary(
			ParoleBoardItinerary itinerary);

	/**
	 * Returns the hearing analysis for the specified parole eligibility.
	 * 
	 * @param eligibility parole eligibility
	 * @return hearing analysis
	 */
	HearingAnalysis findHearingAnalysisByParoleEligibility(
			ParoleEligibility eligibility);
	
	/**
	 * Returns a list of hearing analysis categories.
	 * 
	 * @return list of hearing analysis categories
	 */
	List<HearingAnalysisCategory> findHearingAnalysisCategories();
}
