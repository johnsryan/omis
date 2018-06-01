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
package omis.boardconsideration.service.impl;

import java.util.Date;
import java.util.List;

import omis.boardconsideration.domain.BoardConsideration;
import omis.boardconsideration.domain.BoardConsiderationCategory;
import omis.boardconsideration.domain.BoardConsiderationNote;
import omis.boardconsideration.service.BoardConsiderationService;
import omis.boardconsideration.service.delegate.BoardConsiderationDelegate;
import omis.boardconsideration.service.delegate.BoardConsiderationNoteDelegate;
import omis.exception.DuplicateEntityFoundException;
import omis.hearinganalysis.domain.HearingAnalysis;

/**
 * Implementation of board consideration service.
 * 
 * @author Josh Divine
 * @version 0.1.0 (May 29, 2018)
 * @since OMIS 3.0
 */
public class BoardConsiderationServiceImpl 
		implements BoardConsiderationService {

	private final BoardConsiderationDelegate boardConsiderationDelegate;
	
	private final BoardConsiderationNoteDelegate boardConsiderationNoteDelegate;
	
	/**
	 * Instantiates a board consideration service implementation with the 
	 * specified delegates.
	 * 
	 * @param boardConsiderationDelegate board consideration delegate
	 * @param boardConsiderationNoteDelegate board consideration note delegate
	 */
	public BoardConsiderationServiceImpl(
			final BoardConsiderationDelegate boardConsiderationDelegate,
			final BoardConsiderationNoteDelegate boardConsiderationNoteDelegate) {
		this.boardConsiderationDelegate = boardConsiderationDelegate;
		this.boardConsiderationNoteDelegate = boardConsiderationNoteDelegate;
	}
	
	/** {@inheritDoc} */
	@Override
	public BoardConsideration createBoardConsideration(
			final HearingAnalysis hearingAnalysis, final String title,
			final String description, final BoardConsiderationCategory category,
			final Boolean accepted) throws DuplicateEntityFoundException {
		return this.boardConsiderationDelegate.create(hearingAnalysis, title, 
				description, category, accepted);
	}

	/** {@inheritDoc} */
	@Override
	public BoardConsideration updateBoardConsideration(
			final BoardConsideration boardConsideration, final String title, 
			final String description, final BoardConsiderationCategory category,
			final Boolean accepted) throws DuplicateEntityFoundException {
		return this.boardConsiderationDelegate.update(boardConsideration, 
				boardConsideration.getHearingAnalysis(), title, description, 
				category, accepted);
	}

	/** {@inheritDoc} */
	@Override
	public void removeBoardConsideration(
			final BoardConsideration boardConsideration) {
		this.boardConsiderationDelegate.remove(boardConsideration);
	}

	/** {@inheritDoc} */
	@Override
	public BoardConsiderationNote createBoardConsiderationNote(
			final HearingAnalysis hearingAnalysis, final String description,
			final Date date) throws DuplicateEntityFoundException {
		return this.boardConsiderationNoteDelegate.create(hearingAnalysis, 
				description, date);
	}

	/** {@inheritDoc} */
	@Override
	public BoardConsiderationNote updateBoardConsiderationNote(
			final BoardConsiderationNote boardConsiderationNote,
			final String description, final Date date) 
					throws DuplicateEntityFoundException {
		return this.boardConsiderationNoteDelegate.update(
				boardConsiderationNote, 
				boardConsiderationNote.getHearingAnalysis(), description, date);
	}

	/** {@inheritDoc} */
	@Override
	public void removeBoardConsiderationNote(
			final BoardConsiderationNote boardConsiderationNote) {
		this.boardConsiderationNoteDelegate.remove(boardConsiderationNote);
	}

	/** {@inheritDoc} */
	@Override
	public List<BoardConsideration> findBoardConsiderationsByHearingAnalysis(
			final HearingAnalysis hearingAnalysis) {
		return this.boardConsiderationDelegate.findByHearingAnalysis(
				hearingAnalysis);
	}

	/** {@inheritDoc} */
	@Override
	public List<BoardConsiderationNote> 
			findBoardConsiderationNotesByHearingAnalysis(
					final HearingAnalysis hearingAnalysis) {
		return this.boardConsiderationNoteDelegate.findByHearingAnalysis(
				hearingAnalysis);
	}
}