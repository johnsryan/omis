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
import omis.boardhearing.domain.BoardHearingParticipant;
import omis.boardhearing.exception.BoardHearingExistsException;
import omis.boardhearing.exception.BoardHearingParticipantExistsException;
import omis.boardhearing.service.ScheduleHearingService;
import omis.boardhearing.service.delegate.BoardHearingCategoryDelegate;
import omis.boardhearing.service.delegate.BoardHearingDelegate;
import omis.boardhearing.service.delegate.BoardHearingParticipantDelegate;
import omis.hearinganalysis.domain.HearingAnalysis;
import omis.hearinganalysis.service.delegate.HearingAnalysisDelegate;
import omis.paroleboarditinerary.domain.BoardAttendee;
import omis.paroleboarditinerary.domain.ParoleBoardItinerary;
import omis.paroleboarditinerary.service.delegate.BoardAttendeeDelegate;
import omis.paroleboardmember.domain.ParoleBoardMember;
import omis.paroleboardmember.service.delegate.ParoleBoardMemberDelegate;
import omis.paroleeligibility.domain.AppearanceCategory;
import omis.paroleeligibility.domain.ParoleEligibility;
import omis.paroleeligibility.service.delegate.ParoleEligibilityDelegate;

/**
 * Schedule Hearing Service Implementation.
 * 
 *@author Annie Wahl 
 *@version 0.1.0 (Jun 28, 2018)
 *@since OMIS 3.0
 *
 */
public class ScheduleHearingServiceImpl implements ScheduleHearingService {
	
	private final BoardHearingDelegate boardHearingDelegate;
	
	private final BoardHearingParticipantDelegate
			boardHearingParticipantDelegate;
	
	private final BoardHearingCategoryDelegate boardHearingCategoryDelegate;
	
	private final BoardAttendeeDelegate boardAttendeeDelegate;
	
	private final ParoleBoardMemberDelegate paroleBoardMemberDelegate;
	
	private final ParoleEligibilityDelegate paroleEligibilityDelegate;
	
	private final HearingAnalysisDelegate hearingAnalysisDelegate;
	
	/**
	 * @param boardHearingDelegate
	 * @param boardHearingParticipantDelegate
	 * @param boardHearingCategoryDelegate
	 * @param paroleBoardMemberDelegate
	 */
	public ScheduleHearingServiceImpl(
			final BoardHearingDelegate boardHearingDelegate,
			final ParoleEligibilityDelegate paroleEligibilityDelegate,
			final BoardHearingParticipantDelegate boardHearingParticipantDelegate,
			final BoardHearingCategoryDelegate boardHearingCategoryDelegate,
			final BoardAttendeeDelegate boardAttendeeDelegate,
			final ParoleBoardMemberDelegate paroleBoardMemberDelegate,
			final HearingAnalysisDelegate hearingAnalysisDelegate) {
		this.boardHearingDelegate = boardHearingDelegate;
		this.paroleEligibilityDelegate = paroleEligibilityDelegate;
		this.boardHearingParticipantDelegate = boardHearingParticipantDelegate;
		this.boardHearingCategoryDelegate = boardHearingCategoryDelegate;
		this.boardAttendeeDelegate = boardAttendeeDelegate;
		this.paroleBoardMemberDelegate = paroleBoardMemberDelegate;
		this.hearingAnalysisDelegate = hearingAnalysisDelegate;
	}
	
	/**{@inheritDoc} */
	@Override
	public BoardHearing createBoardHearing(final ParoleBoardItinerary itinerary,
			final Date hearingDate, final ParoleEligibility paroleEligibility,
			final BoardHearingCategory category, final Boolean videoConference)
					throws BoardHearingExistsException {
		return this.boardHearingDelegate.create(itinerary, hearingDate,
				paroleEligibility, category, null, videoConference);
	}

	/**{@inheritDoc} */
	@Override
	public BoardHearing updateBoardHearing(final BoardHearing boardHearing,
			final ParoleBoardItinerary itinerary, final Date hearingDate,
			final ParoleEligibility paroleEligibility,
			final BoardHearingCategory category, final Boolean videoConference)
					throws BoardHearingExistsException {
		return this.boardHearingDelegate.update(boardHearing, itinerary,
				hearingDate, paroleEligibility, category, null,
				videoConference);
	}

	/**{@inheritDoc} */
	@Override
	public void removeBoardHearing(final BoardHearing boardHearing) {
		this.boardHearingDelegate.remove(boardHearing);
	}

	/**{@inheritDoc} */
	@Override
	public BoardHearingParticipant createBoardHearingParticipant(
			final BoardHearing hearing, final ParoleBoardMember boardMember,
			final Long number)
					throws BoardHearingParticipantExistsException {
		return this.boardHearingParticipantDelegate.create(
				hearing, boardMember, number);
	}

	/**{@inheritDoc} */
	@Override
	public BoardHearingParticipant updateBoardHearingParticipant(
			final BoardHearingParticipant boardHearingParticipant,
			final BoardHearing hearing, final ParoleBoardMember boardMember,
			final Long number)
					throws BoardHearingParticipantExistsException {
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
	public BoardHearing findBoardHearingByParoleEligibility(
			final ParoleEligibility paroleEligibility) {
		return this.boardHearingDelegate.findByParoleEligibility(
				paroleEligibility);
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
	public List<BoardHearingParticipant> findBoardHearingParticipantsByHearing(
			final BoardHearing hearing) {
		return this.boardHearingParticipantDelegate.findByHearing(hearing);
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
	public List<ParoleBoardMember> findBoardMembersByDate(
			final Date effectiveDate) {
		return this.paroleBoardMemberDelegate.findBoardMembersByDate(
				effectiveDate);
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
	public List<ParoleEligibility> findParoleEligibilitiesByItinerary(
			final ParoleBoardItinerary itinerary) {
		return this.paroleEligibilityDelegate.findByItinerary(itinerary);
	}

	/**{@inheritDoc} */
	@Override
	public List<ParoleEligibility> findParoleEligibilitiesUnscheduled() {
		return this.paroleEligibilityDelegate.findUnscheduled();
	}

	/**{@inheritDoc} */
	@Override
	public List<ParoleEligibility> findUnscheduledParoleEligibilitiesIncluding(
			final ParoleBoardItinerary including) {
		return this.paroleEligibilityDelegate.findUnscheduledIncluding(
				including);
	}
}
