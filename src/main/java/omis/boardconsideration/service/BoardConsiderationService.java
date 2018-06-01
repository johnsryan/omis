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
package omis.boardconsideration.service;

import java.util.Date;
import java.util.List;

import omis.boardconsideration.domain.BoardConsideration;
import omis.boardconsideration.domain.BoardConsiderationCategory;
import omis.boardconsideration.domain.BoardConsiderationNote;
import omis.exception.DuplicateEntityFoundException;
import omis.hearinganalysis.domain.HearingAnalysis;

/**
 * Board consideration service.
 * 
 * @author Josh Divine
 * @version 0.1.0 (May 29, 2018)
 * @since OMIS 3.0
 */
public interface BoardConsiderationService {

	/**
	 * Creates a new board consideration.
	 * 
	 * @param hearingAnalysis hearing analysis
	 * @param title title
	 * @param description description
	 * @param category board consideration category
	 * @param accepted accepted
	 * @return board consideration
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	BoardConsideration createBoardConsideration(HearingAnalysis hearingAnalysis,
			String title, String description, 
			BoardConsiderationCategory category, Boolean accepted) 
					throws DuplicateEntityFoundException;
	
	/**
	 * Updates an existing board consideration.
	 * 
	 * @param boardConsideration board consideration
	 * @param title title
	 * @param description description
	 * @param category board consideration category
	 * @param accepted accepted
	 * @return board consideration
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	BoardConsideration updateBoardConsideration(
			BoardConsideration boardConsideration, 
			String title, String description, 
			BoardConsiderationCategory category, Boolean accepted) 
					throws DuplicateEntityFoundException;
	
	/**
	 * Removes the specified board consideration.
	 * 
	 * @param boardConsideration board consideration
	 */
	void removeBoardConsideration(BoardConsideration boardConsideration);
	
	/**
	 * Creates a new board consideration note.
	 * 
	 * @param hearingAnalysis hearing analysis
	 * @param description description
	 * @param date date
	 * @return board consideration note
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	BoardConsiderationNote createBoardConsiderationNote(
			HearingAnalysis hearingAnalysis, String description, Date date) 
					throws DuplicateEntityFoundException;
	
	/**
	 * Updates an existing board consideration note.
	 * 
	 * @param boardConsiderationNote board consideration note
	 * @param description description
	 * @param date date
	 * @return board consideration note
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	BoardConsiderationNote updateBoardConsiderationNote(
			BoardConsiderationNote boardConsiderationNote, String description, 
			Date date) 
					throws DuplicateEntityFoundException;
	
	/**
	 * Removes the specified board consideration note.
	 * 
	 * @param boardConsiderationNote board consideration note
	 */
	void removeBoardConsiderationNote(
			BoardConsiderationNote boardConsiderationNote);
	
	/**
	 * Returns a list of board considerations for the specified hearing 
	 * analysis.
	 * 
	 * @param hearingAnalysis hearing analysis
	 * @return list of board considerations
	 */
	List<BoardConsideration> findBoardConsiderationsByHearingAnalysis(
			HearingAnalysis hearingAnalysis);
	
	/**
	 * Returns a list of board consideration notes for the specified hearing 
	 * analysis.
	 * 
	 * @param hearingAnalysis hearing analysis
	 * @return list of board consideration notes
	 */
	List<BoardConsiderationNote> findBoardConsiderationNotesByHearingAnalysis(
			HearingAnalysis hearingAnalysis);
}