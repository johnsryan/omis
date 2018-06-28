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

import java.util.List;
import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.boardhearing.dao.BoardHearingParticipantDao;
import omis.boardhearing.domain.BoardHearing;
import omis.boardhearing.domain.BoardHearingParticipant;
import omis.boardhearing.exception.BoardHearingParticipantExistsException;
import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;
import omis.paroleboardmember.domain.ParoleBoardMember;

/**
 * Board Hearing Participant Delegate.
 * 
 *@author Annie Wahl 
 *@version 0.1.0 (Dec 29, 2017)
 *@since OMIS 3.0
 *
 */
public class BoardHearingParticipantDelegate {
	
	private static final String DUPLICATE_ENTITY_FOUND_MSG =
			"A Board Hearing Participant already exists with the specified "
			+ "Parole Board Member for the given Board Hearing.";
	
	private final BoardHearingParticipantDao boardHearingParticipantDao;
	
	private final InstanceFactory<BoardHearingParticipant> 
		boardHearingParticipantInstanceFactory;
	
	private final AuditComponentRetriever auditComponentRetriever;

	/**
	 * Constructor for BoardHearingParticipantDelegate.
	 * @param boardHearingParticipantDao - Board Hearing Participant DAO
	 * @param boardHearingParticipantInstanceFactory - Board Hearing
	 * Participant Instance Factory
	 * @param auditComponentRetriever - Audit Component Retriever
	 */
	public BoardHearingParticipantDelegate(
			final BoardHearingParticipantDao boardHearingParticipantDao,
			final InstanceFactory<BoardHearingParticipant> 
				boardHearingParticipantInstanceFactory,
			final AuditComponentRetriever auditComponentRetriever) {
		this.boardHearingParticipantDao = boardHearingParticipantDao;
		this.boardHearingParticipantInstanceFactory =
				boardHearingParticipantInstanceFactory;
		this.auditComponentRetriever = auditComponentRetriever;
	}
	
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
	public BoardHearingParticipant create(final BoardHearing hearing,
			final ParoleBoardMember boardMember, final Long number)
				throws BoardHearingParticipantExistsException {
		if (this.boardHearingParticipantDao.find(
				boardMember, hearing) != null) {
			throw new BoardHearingParticipantExistsException(
					DUPLICATE_ENTITY_FOUND_MSG);
		}
		
		BoardHearingParticipant boardHearingParticipant = 
				this.boardHearingParticipantInstanceFactory.createInstance();
		
		boardHearingParticipant.setBoardMember(boardMember);
		boardHearingParticipant.setHearing(hearing);
		boardHearingParticipant.setNumber(number);
		boardHearingParticipant.setCreationSignature(
				new CreationSignature(
						this.auditComponentRetriever.retrieveUserAccount(), 
						this.auditComponentRetriever.retrieveDate()));
		boardHearingParticipant.setUpdateSignature(
				new UpdateSignature(
						this.auditComponentRetriever.retrieveUserAccount(),
						this.auditComponentRetriever.retrieveDate()));
		
		return this.boardHearingParticipantDao.makePersistent(
				boardHearingParticipant);
	}
	
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
	public BoardHearingParticipant update(
			final BoardHearingParticipant boardHearingParticipant,
			final BoardHearing hearing,
			final ParoleBoardMember boardMember, final Long number)
				throws BoardHearingParticipantExistsException {
		if (this.boardHearingParticipantDao.findExcluding(boardMember, hearing,
				boardHearingParticipant) != null) {
			throw new BoardHearingParticipantExistsException(
					DUPLICATE_ENTITY_FOUND_MSG);
		}
		
		boardHearingParticipant.setBoardMember(boardMember);
		boardHearingParticipant.setHearing(hearing);
		boardHearingParticipant.setNumber(number);
		boardHearingParticipant.setUpdateSignature(
				new UpdateSignature(
						this.auditComponentRetriever.retrieveUserAccount(),
						this.auditComponentRetriever.retrieveDate()));
		
		return this.boardHearingParticipantDao.makePersistent(
				boardHearingParticipant);
	}
	
	/**
	 * Removes the specified Board Hearing Participant.
	 * 
	 * @param boardHearingParticipant - Board Hearing Participant to be removed
	 */
	public void remove(final BoardHearingParticipant boardHearingParticipant) {
		this.boardHearingParticipantDao.makeTransient(boardHearingParticipant);
	}
	
	
	/**
	 * Returns a list of Board Hearing Participants for the specified
	 * Board Hearing.
	 * 
	 * @param hearing - Board Hearing
	 * @return List of Board Hearing Participants for the specified
	 * Board Hearing.
	 */
	public List<BoardHearingParticipant> findByHearing(
			final BoardHearing hearing) {
		return this.boardHearingParticipantDao.findByHearing(hearing);
	}
	
}
