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
package omis.boardconsideration.service.delegate;

import java.util.Date;
import java.util.List;

import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.boardconsideration.dao.BoardConsiderationNoteDao;
import omis.boardconsideration.domain.BoardConsiderationNote;
import omis.exception.DuplicateEntityFoundException;
import omis.hearinganalysis.domain.HearingAnalysis;
import omis.instance.factory.InstanceFactory;

/**
 * Board consideration note delegate.
 * 
 * @author Josh Divine
 * @version 0.1.0 (May 29, 2018)
 * @since OMIS 3.0
 */
public class BoardConsiderationNoteDelegate {

	/* Data access objects. */
	
	private final BoardConsiderationNoteDao boardConsiderationNoteDao;

	/* Instance factories. */
	
	private final InstanceFactory<BoardConsiderationNote> 
			boardConsiderationNoteInstanceFactory;
	
	/* Component Retrievers. */
	
	private final AuditComponentRetriever auditComponentRetriever;
	
	/**
	 * Instantiates an implementation of board consideration note delegate with 
	 * the specified data access object, instance factory and audit component 
	 * retriever.
	 * 
	 * @param boardConsiderationNoteDao board consideration note data access 
	 * object
	 * @param boardConsiderationNoteInstanceFactory board consideration note 
	 * instance factory
	 * @param auditComponentRetriever audit component retriever
	 */
	public BoardConsiderationNoteDelegate(
			final BoardConsiderationNoteDao boardConsiderationNoteDao,
			final InstanceFactory<BoardConsiderationNote> 
					boardConsiderationNoteInstanceFactory,
			final AuditComponentRetriever auditComponentRetriever) {
		this.boardConsiderationNoteDao = boardConsiderationNoteDao;
		this.boardConsiderationNoteInstanceFactory = 
				boardConsiderationNoteInstanceFactory;
		this.auditComponentRetriever = auditComponentRetriever;
	}
	
	/**
	 * Creates a new board consideration note.
	 * 
	 * @param hearingAnalysis hearing analysis
	 * @param description description
	 * @param date date
	 * @return board consideration note
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	public BoardConsiderationNote create(final HearingAnalysis hearingAnalysis,
			final String description, final Date date) 
					throws DuplicateEntityFoundException {
		if (this.boardConsiderationNoteDao.find(hearingAnalysis, description, 
				date) != null) {
			throw new DuplicateEntityFoundException(
					"Board consideration note already exists.");
		}
		BoardConsiderationNote boardConsiderationNote = this
				.boardConsiderationNoteInstanceFactory.createInstance();
		boardConsiderationNote.setCreationSignature(new CreationSignature(
				this.auditComponentRetriever.retrieveUserAccount(), 
				this.auditComponentRetriever.retrieveDate()));
		populateBoardConsiderationNote(boardConsiderationNote, hearingAnalysis, 
				description, date);
		return this.boardConsiderationNoteDao.makePersistent(
				boardConsiderationNote);
	}
	
	/**
	 * Updates an existing board consideration note.
	 * 
	 * @param boardConsiderationNote board consideration note
	 * @param hearingAnalysis hearing analysis
	 * @param description description
	 * @param date date
	 * @return board consideration note
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	public BoardConsiderationNote update(
			final BoardConsiderationNote boardConsiderationNote, 
			final HearingAnalysis hearingAnalysis, final String description, 
			final Date date) throws DuplicateEntityFoundException {
		if (this.boardConsiderationNoteDao.findExcluding(hearingAnalysis, 
				description, date, boardConsiderationNote) != null) {
			throw new DuplicateEntityFoundException(
					"Board consideration note already exists.");
		}
		populateBoardConsiderationNote(boardConsiderationNote, hearingAnalysis, 
				description, date);
		return this.boardConsiderationNoteDao.makePersistent(
				boardConsiderationNote);
	}

	/**
	 * Removes the specified board consideration note.
	 * 
	 * @param boardConsiderationNote board consideration note
	 */
	public void remove(
			final BoardConsiderationNote boardConsiderationNote) {
		this.boardConsiderationNoteDao.makeTransient(boardConsiderationNote);
	}
	
	/**
	 * Returns a list of board consideration notes for the specified hearing 
	 * analysis.
	 * 
	 * @param hearingAnalysis hearing analysis
	 * @return list of board consideration notes
	 */
	public List<BoardConsiderationNote> findByHearingAnalysis(
			final HearingAnalysis hearingAnalysis) {
		return this.boardConsiderationNoteDao.findByHearingAnalysis(
				hearingAnalysis);
	}
	
	// Populates a board consideration note
	private void populateBoardConsiderationNote(
			final BoardConsiderationNote boardConsiderationNote,
			final HearingAnalysis hearingAnalysis, final String description, 
			final Date date) {
		boardConsiderationNote.setHearingAnalysis(hearingAnalysis);
		boardConsiderationNote.setDescription(description);
		boardConsiderationNote.setDate(date);
		boardConsiderationNote.setUpdateSignature(new UpdateSignature(
				this.auditComponentRetriever.retrieveUserAccount(), 
				this.auditComponentRetriever.retrieveDate()));
	}
}