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
package omis.hearinganalysis.service.impl;

import java.util.Date;
import java.util.List;

import omis.exception.DuplicateEntityFoundException;
import omis.hearinganalysis.domain.HearingAnalysis;
import omis.hearinganalysis.domain.HearingAnalysisCategory;
import omis.hearinganalysis.domain.HearingAnalysisNote;
import omis.hearinganalysis.service.HearingAnalysisService;
import omis.hearinganalysis.service.delegate.HearingAnalysisCategoryDelegate;
import omis.hearinganalysis.service.delegate.HearingAnalysisDelegate;
import omis.hearinganalysis.service.delegate.HearingAnalysisNoteDelegate;
import omis.paroleboarditinerary.domain.BoardAttendee;
import omis.paroleboarditinerary.domain.BoardMeetingSite;
import omis.paroleboarditinerary.domain.ParoleBoardItinerary;
import omis.paroleboarditinerary.service.delegate.BoardAttendeeDelegate;
import omis.paroleboarditinerary.service.delegate.BoardMeetingSiteDelegate;
import omis.paroleboarditinerary.service.delegate.ParoleBoardItineraryDelegate;
import omis.paroleeligibility.domain.ParoleEligibility;

/**
 * Implementation of service for hearing analysis.
 * 
 * @author Josh Divine
 * @author Annie Wahl
 * @version 0.1.3 (May 29, 2018)
 * @since OMIS 3.0
 */
public class HearingAnalysisServiceImpl implements HearingAnalysisService {

	/* Delegates. */
	
	private final HearingAnalysisDelegate hearingAnalysisDelegate;
	
	private final HearingAnalysisNoteDelegate hearingAnalysisNoteDelegate;
	
	private final ParoleBoardItineraryDelegate paroleBoardItineraryDelegate;
	
	private final BoardMeetingSiteDelegate boardMeetingSiteDelegate;
	
	private final BoardAttendeeDelegate boardAttendeeDelegate;
	
	private final HearingAnalysisCategoryDelegate 
			hearingAnalysisCategoryDelegate;
	
	/**
	 * Instantiates a hearing analysis service implementation with the specified 
	 * delegates.
	 * 
	 * @param hearingAnalysisDelegate hearing analysis delegate
	 * @param hearingAnalysisNoteDelegate hearing analysis note delegate
	 * @param paroleBoardItineraryDelegate parole board itinerary delegate
	 * @param boardMeetingSiteDelegate board meeting site delegate
	 * @param boardAttendeeDelegate board attendee delegate
	 */
	public HearingAnalysisServiceImpl(
			final HearingAnalysisDelegate hearingAnalysisDelegate,
			final HearingAnalysisNoteDelegate hearingAnalysisNoteDelegate,
			final ParoleBoardItineraryDelegate paroleBoardItineraryDelegate,
			final BoardMeetingSiteDelegate boardMeetingSiteDelegate,
			final BoardAttendeeDelegate boardAttendeeDelegate,
			final HearingAnalysisCategoryDelegate 
					hearingAnalysisCategoryDelegate) {
		this.hearingAnalysisDelegate = hearingAnalysisDelegate;
		this.hearingAnalysisNoteDelegate = hearingAnalysisNoteDelegate;
		this.paroleBoardItineraryDelegate = paroleBoardItineraryDelegate;
		this.boardMeetingSiteDelegate = boardMeetingSiteDelegate;
		this.boardAttendeeDelegate = boardAttendeeDelegate;
		this.hearingAnalysisCategoryDelegate = hearingAnalysisCategoryDelegate;
	}
	
	/** {@inheritDoc} */
	@Override
	public HearingAnalysis createHearingAnalysis(
			final ParoleEligibility eligibility, 
			final ParoleBoardItinerary paroleBoardItinerary, 
			final BoardAttendee analyst, final HearingAnalysisCategory category,
			final Date expectedCompletionDate)
					throws DuplicateEntityFoundException {
		return this.hearingAnalysisDelegate.create(eligibility,
				paroleBoardItinerary, category, analyst,
				expectedCompletionDate);
	}

	/** {@inheritDoc} */
	@Override
	public HearingAnalysis updateHearingAnalysis(
			final HearingAnalysis hearingAnalysis, 
			final ParoleBoardItinerary paroleBoardItinerary, 
			final BoardAttendee analyst, final HearingAnalysisCategory category,
			final Date expectedCompletionDate)
					throws DuplicateEntityFoundException {
		return this.hearingAnalysisDelegate.update(hearingAnalysis, 
				hearingAnalysis.getEligibility(), paroleBoardItinerary, 
				category, analyst, expectedCompletionDate);
	}

	/** {@inheritDoc} */
	@Override
	public void removeHearingAnalysis(final HearingAnalysis hearingAnalysis) {
		this.hearingAnalysisDelegate.remove(hearingAnalysis);
	}

	/** {@inheritDoc} */
	@Override
	public HearingAnalysisNote createHearingAnalysisNote(
			final HearingAnalysis hearingAnalysis, final String description, 
			final Date date) throws DuplicateEntityFoundException {
		return this.hearingAnalysisNoteDelegate.create(hearingAnalysis, 
				description, date);
	}

	/** {@inheritDoc} */
	@Override
	public HearingAnalysisNote updateHearingAnalysisNote(
			final HearingAnalysisNote hearingAnalysisNote, 
			final String description, final Date date) 
					throws DuplicateEntityFoundException {
		return this.hearingAnalysisNoteDelegate.update(hearingAnalysisNote, 
				description, date);
	}

	/** {@inheritDoc} */
	@Override
	public void removeHearingAnalysisNote(
			final HearingAnalysisNote hearingAnalysisNote) {
		this.hearingAnalysisNoteDelegate.remove(hearingAnalysisNote);
	}

	/** {@inheritDoc} */
	@Override
	public List<HearingAnalysisNote> findHearingAnalysisNotesByHearingAnalysis(
			final HearingAnalysis hearingAnalysis) {
		return this.hearingAnalysisNoteDelegate.findByHearingAnalysis(
				hearingAnalysis);
	}

	/** {@inheritDoc} */
	@Override
	public List<ParoleBoardItinerary> 
			findItinerariesByEffectiveDate(final Date effectiveDate) {
		return this.paroleBoardItineraryDelegate.findByEffectiveDate(
				effectiveDate);
	}
	
	/** {@inheritDoc} */
	@Override
	public List<BoardMeetingSite> findBoardMeetingSitesByItinerary(
			final ParoleBoardItinerary itinerary) {
		return this.boardMeetingSiteDelegate
				.findBoardMeetingSitesByBoardItinerary(itinerary);
	}

	/** {@inheritDoc} */
	@Override
	public List<BoardAttendee> findBoardAttendeesByItinerary(
			final ParoleBoardItinerary itinerary) {
		return this.boardAttendeeDelegate.findBoardAttendeesByBoardItinerary(
				itinerary);
	}

	/** {@inheritDoc} */
	@Override
	public HearingAnalysis findHearingAnalysisByParoleEligibility(
			final ParoleEligibility eligibility) {
		return this.hearingAnalysisDelegate.findByParoleEligibility(eligibility);
	}

	/** {@inheritDoc} */
	@Override
	public List<HearingAnalysisCategory> findHearingAnalysisCategories() {
		return this.hearingAnalysisCategoryDelegate.findAll();
	}

}
