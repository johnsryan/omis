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
package omis.boardhearing.service.delegate;

import java.util.Date;

import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.boardhearing.dao.BoardHearingDao;
import omis.boardhearing.domain.BoardHearing;
import omis.boardhearing.domain.BoardHearingCategory;
import omis.boardhearing.domain.CancellationCategory;
import omis.boardhearing.exception.BoardHearingExistsException;
import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;
import omis.paroleboarditinerary.domain.ParoleBoardItinerary;
import omis.paroleeligibility.domain.ParoleEligibility;

/**
 * Board Hearing Delegate.
 * 
 * @author Annie Wahl 
 * @author Josh Divine
 * @version 0.1.2 (Apr 18, 2018)
 * @since OMIS 3.0
 */
public class BoardHearingDelegate {
	
	private static final String DUPLICATE_ENTITY_FOUND_MSG =
			"A Board Hearing already exists for the specified Parole "
			+ "Eligibility";
	
	private final BoardHearingDao boardHearingDao;
	
	private final InstanceFactory<BoardHearing> 
		boardHearingInstanceFactory;
	
	private final AuditComponentRetriever auditComponentRetriever;

	/**
	 * Constructor for BoardHearingDelegate.
	 * @param boardHearingDao - Board Hearing DAO
	 * @param boardHearingInstanceFactory - Board Hearing Instance Factory
	 * @param auditComponentRetriever - Audit Component Retriever
	 */
	public BoardHearingDelegate(
			final BoardHearingDao boardHearingDao,
			final InstanceFactory<BoardHearing> 
				boardHearingInstanceFactory,
			final AuditComponentRetriever auditComponentRetriever) {
		this.boardHearingDao = boardHearingDao;
		this.boardHearingInstanceFactory = boardHearingInstanceFactory;
		this.auditComponentRetriever = auditComponentRetriever;
	}
	
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
	public BoardHearing create(final ParoleBoardItinerary itinerary,
			final Date hearingDate, final ParoleEligibility paroleEligibility,
			final BoardHearingCategory category,
			final CancellationCategory cancellation,
			final Boolean videoConference)
				throws BoardHearingExistsException {
		if (this.boardHearingDao.find(paroleEligibility) != null) {
			throw new BoardHearingExistsException(DUPLICATE_ENTITY_FOUND_MSG);
		}
		
		BoardHearing boardHearing = 
				this.boardHearingInstanceFactory.createInstance();
		boardHearing.setCreationSignature(
				new CreationSignature(
						this.auditComponentRetriever.retrieveUserAccount(), 
						this.auditComponentRetriever.retrieveDate()));
		populateBoardHearing(boardHearing, itinerary, hearingDate, 
				paroleEligibility, category, cancellation, videoConference);
		
		return this.boardHearingDao.makePersistent(boardHearing);
	}
	
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
	public BoardHearing update(final BoardHearing boardHearing,
			final ParoleBoardItinerary itinerary, final Date hearingDate,
			final ParoleEligibility paroleEligibility,
			final BoardHearingCategory category,
			final CancellationCategory cancellation,
			final Boolean videoConference)
				throws BoardHearingExistsException {
		if (this.boardHearingDao.findExcluding(
				paroleEligibility, boardHearing) != null) {
			throw new BoardHearingExistsException(DUPLICATE_ENTITY_FOUND_MSG);
		}
		
		populateBoardHearing(boardHearing, itinerary, hearingDate, 
				paroleEligibility, category, cancellation, videoConference);
		
		return this.boardHearingDao.makePersistent(boardHearing);
	}

	/**
	 * Removes the specified Board Hearing.
	 * 
	 * @param boardHearing - Board Hearing to remove
	 */
	public void remove(final BoardHearing boardHearing) {
		this.boardHearingDao.makeTransient(boardHearing);
	}
	
	/**
	 * Returns a Board Hearing with the specified Parole Eligibility.
	 * 
	 * @param paroleEligibility - Parole Eligibility
	 * @return Board Hearing with the specified Parole Eligibility.
	 */
	public BoardHearing findByParoleEligibility(
			final ParoleEligibility paroleEligibility) {
		return this.boardHearingDao.find(paroleEligibility);
	}
	
	// Populates a board hearing
	private void populateBoardHearing(final BoardHearing boardHearing, 
			final ParoleBoardItinerary itinerary, final Date hearingDate, 
			final ParoleEligibility paroleEligibility,
			final BoardHearingCategory category, 
			final CancellationCategory cancellation, 
			final Boolean videoConference) {
		boardHearing.setItinerary(itinerary);
		boardHearing.setCategory(category);
		boardHearing.setCancellation(cancellation);
		boardHearing.setHearingDate(hearingDate);
		boardHearing.setParoleEligibility(paroleEligibility);
		boardHearing.setVideoConference(videoConference);
		boardHearing.setUpdateSignature(
				new UpdateSignature(
						this.auditComponentRetriever.retrieveUserAccount(),
						this.auditComponentRetriever.retrieveDate()));
	}
}