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
package omis.boardhearing.service.impl;

import java.util.Date;
import java.util.List;

import omis.boardhearing.domain.BoardHearing;
import omis.boardhearing.domain.BoardHearingCategory;
import omis.boardhearing.domain.BoardHearingNote;
import omis.boardhearing.domain.BoardHearingParticipant;
import omis.boardhearing.domain.CancellationCategory;
import omis.boardhearing.service.BoardHearingService;
import omis.boardhearing.service.delegate.BoardHearingCategoryDelegate;
import omis.boardhearing.service.delegate.BoardHearingDelegate;
import omis.boardhearing.service.delegate.BoardHearingNoteDelegate;
import omis.boardhearing.service.delegate.BoardHearingParticipantDelegate;
import omis.exception.DuplicateEntityFoundException;
import omis.hearinganalysis.domain.HearingAnalysis;
import omis.hearinganalysis.service.delegate.HearingAnalysisDelegate;
import omis.paroleboarditinerary.domain.BoardAttendee;
import omis.paroleboarditinerary.domain.BoardMeetingSite;
import omis.paroleboarditinerary.domain.ParoleBoardItinerary;
import omis.paroleboarditinerary.service.delegate.BoardAttendeeDelegate;
import omis.paroleboarditinerary.service.delegate.BoardMeetingSiteDelegate;
import omis.paroleboarditinerary.service.delegate.ParoleBoardItineraryDelegate;
import omis.paroleboardmember.domain.ParoleBoardMember;
import omis.paroleeligibility.domain.AppearanceCategory;
import omis.paroleeligibility.domain.ParoleEligibility;

/**
 * Board Hearing Service Implementation.
 * 
 * @author Annie Wahl 
 * @author Josh Divine
 * @version 0.1.2 (Apr 18, 2018)
 * @since OMIS 3.0
 */
public class BoardHearingServiceImpl implements BoardHearingService {
	
	private final BoardHearingDelegate boardHearingDelegate;
	
	private final BoardHearingNoteDelegate boardHearingNoteDelegate;
	
	private final BoardHearingParticipantDelegate
		boardHearingParticipantDelegate;
	
	private final BoardHearingCategoryDelegate boardHearingCategoryDelegate;
	
	private final BoardAttendeeDelegate boardAttendeeDelegate;
	
	private final BoardMeetingSiteDelegate boardMeetingSiteDelegate;
	
	private final HearingAnalysisDelegate hearingAnalysisDelegate;
	
	private final ParoleBoardItineraryDelegate paroleItineraryDelegate;
	
	/**
	 * @param boardHearingDelegate - Board Hearing Delegate
	 * @param boardHearingNoteDelegate - Board Hearing Note Delegate
	 * @param boardHearingParticipantDelegate - Board Hearing Participant
	 * Delegate
	 * @param boardHearingCategoryDelegate - Board Hearing Category Delegate
	 * @param boardAttendeeDelegate - Board Attendee Delegate
	 * @param boardMeetingSiteDelegate - Board Meeting Site Delegate
	 * @param hearingAnalysisDelegate - Hearing Analysis Delegate
	 * @param paroleBoardItineraryDelegate - Parole Board Itinerary Delegate
	 */
	public BoardHearingServiceImpl(
			final BoardHearingDelegate boardHearingDelegate,
			final BoardHearingNoteDelegate boardHearingNoteDelegate,
			final BoardHearingParticipantDelegate
				boardHearingParticipantDelegate,
			final BoardHearingCategoryDelegate boardHearingCategoryDelegate,
			final BoardAttendeeDelegate boardAttendeeDelegate,
			final BoardMeetingSiteDelegate boardMeetingSiteDelegate,
			final HearingAnalysisDelegate hearingAnalysisDelegate,
			final ParoleBoardItineraryDelegate paroleBoardItineraryDelegate) {
		this.boardHearingDelegate = boardHearingDelegate;
		this.boardHearingNoteDelegate = boardHearingNoteDelegate;
		this.boardHearingParticipantDelegate = boardHearingParticipantDelegate;
		this.boardHearingCategoryDelegate = boardHearingCategoryDelegate;
		this.boardAttendeeDelegate = boardAttendeeDelegate;
		this.boardMeetingSiteDelegate = boardMeetingSiteDelegate;
		this.hearingAnalysisDelegate = hearingAnalysisDelegate;
		this.paroleItineraryDelegate = paroleBoardItineraryDelegate;
	}
	
	/**{@inheritDoc} */
	@Override
	public BoardHearing createBoardHearing(final ParoleBoardItinerary itinerary,
			final Date hearingDate, final ParoleEligibility paroleEligibility,
			final BoardHearingCategory category,
			final CancellationCategory cancellation,
			final Boolean videoConference)
					throws DuplicateEntityFoundException {
		return this.boardHearingDelegate.create(itinerary, hearingDate, 
				paroleEligibility, category, cancellation, videoConference);
	}
	
	/**{@inheritDoc} */
	@Override
	public BoardHearing updateBoardHearing(final BoardHearing boardHearing,
			final ParoleBoardItinerary itinerary, final Date hearingDate, 
			final ParoleEligibility paroleEligibility,
			final BoardHearingCategory category, 
			final CancellationCategory cancellation,
			final Boolean videoConference)
					throws DuplicateEntityFoundException {
		return this.boardHearingDelegate.update(boardHearing, itinerary,
				hearingDate, paroleEligibility, category, cancellation, 
				videoConference);
	}
	
	/**{@inheritDoc} */
	@Override
	public void removeBoardHearing(final BoardHearing boardHearing) {
		this.boardHearingDelegate.remove(boardHearing);
	}
	
	/**{@inheritDoc} */
	@Override
	public BoardHearingNote createBoardHearingNote(final BoardHearing hearing,
			final String description, final Date date)
			throws DuplicateEntityFoundException {
		return this.boardHearingNoteDelegate.create(hearing, description, date);
	}
	
	/**{@inheritDoc} */
	@Override
	public BoardHearingNote updateBoardHearingNote(
			final BoardHearingNote boardHearingNote,
			final String description, final Date date)
			throws DuplicateEntityFoundException {
		return this.boardHearingNoteDelegate.update(boardHearingNote,
				description, date);
	}
	
	/**{@inheritDoc} */
	@Override
	public void removeBoardHearingNote(
			final BoardHearingNote boardHearingNote) {
		this.boardHearingNoteDelegate.remove(boardHearingNote);
	}
	
	/**{@inheritDoc} */
	@Override
	public BoardHearingParticipant createBoardHearingParticipant(
			final BoardHearing hearing, final ParoleBoardMember boardMember,
			final Long number)
			throws DuplicateEntityFoundException {
		return this.boardHearingParticipantDelegate.create(hearing, boardMember,
				number);
	}
	
	/**{@inheritDoc} */
	@Override
	public BoardHearingParticipant updateBoardHearingParticipant(
			final BoardHearingParticipant boardHearingParticipant,
			final BoardHearing hearing, final ParoleBoardMember boardMember,
			final Long number)
			throws DuplicateEntityFoundException {
		return this.boardHearingParticipantDelegate.update(
				boardHearingParticipant, hearing, boardMember, number);
	}
	
	/**{@inheritDoc} */
	@Override
	public void removeBoardHearingParticipant(
			final BoardHearingParticipant boardHearingParticipant) {
		this.boardHearingParticipantDelegate.remove(boardHearingParticipant);
	}
	
	/**{@inheritDoc} */
	@Override
	public List<BoardHearingNote> findBoardHearingNotesByHearing(
			final BoardHearing hearing) {
		return this.boardHearingNoteDelegate.findByHearing(hearing);
	}
	
	/**{@inheritDoc} */
	@Override
	public List<BoardHearingParticipant> findBoardHearingParticipantsByHearing(
			final BoardHearing hearing) {
		return this.boardHearingParticipantDelegate.findByHearing(hearing);
	}
	
	/**{@inheritDoc} */
	@Override
	public List<BoardAttendee> findBoardAttendeesByBoardItinerary(
			final ParoleBoardItinerary boardItinerary) {
		return this.boardAttendeeDelegate.findBoardAttendeesByBoardItinerary(
				boardItinerary);
	}
	
	/**{@inheritDoc} */
	@Override
	public List<BoardMeetingSite> findBoardMeetingSitesByBoardItinerary(
			final ParoleBoardItinerary boardItinerary) {
		return this.boardMeetingSiteDelegate
				.findBoardMeetingSitesByBoardItinerary(boardItinerary);
	}
	
	/**{@inheritDoc} */
	@Override
	public HearingAnalysis findHearingAnalysisByParoleEligibility(
			final ParoleEligibility eligibility) {
		return this.hearingAnalysisDelegate.findByParoleEligibility(
				eligibility);
	}
	
	/**{@inheritDoc} */
	@Override
	public List<ParoleBoardItinerary> 
			findItinerariesByEffectiveDate(final Date effectiveDate) {
		return this.paroleItineraryDelegate.findByEffectiveDate(effectiveDate);
	}
	
	/**{@inheritDoc} */
	@Override
	public List<BoardHearingCategory>
		findBoardHearingCategoriesByAppearanceCategory(
			final AppearanceCategory appearanceCategory) {
		return this.boardHearingCategoryDelegate.findByAppearanceCategory(
				appearanceCategory);
	}

	/**{@inheritDoc} */
	@Override
	public BoardHearing findBoardHearingByParoleEligibility(
			final ParoleEligibility paroleEligibility) {
		return this.boardHearingDelegate.findByParoleEligibility(
				paroleEligibility);
	}
}