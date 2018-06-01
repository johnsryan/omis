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
package omis.boardconsideration.dao;

import java.util.Date;
import java.util.List;

import omis.boardconsideration.domain.BoardConsiderationNote;
import omis.dao.GenericDao;
import omis.hearinganalysis.domain.HearingAnalysis;

/**
 * Board consideration note data access object.
 * 
 * @author Josh Divine
 * @version 0.1.0 (May 29, 2018)
 * @since OMIS 3.0
 */
public interface BoardConsiderationNoteDao 
		extends GenericDao<BoardConsiderationNote> {

	/**
	 * Returns a list of board consideration notes for the specified hearing 
	 * analysis.
	 * 
	 * @param hearingAnalysis hearing analysis
	 * @return list of board consideration notes
	 */
	List<BoardConsiderationNote> findByHearingAnalysis(
			HearingAnalysis hearingAnalysis);

	/**
	 * Returns the board consideration note with the specified parameters.
	 * 
	 * @param hearingAnalysis hearing analysis
	 * @param description description
	 * @param date date
	 * @return board consideration note
	 */
	BoardConsiderationNote find(HearingAnalysis hearingAnalysis, 
			String description, Date date);

	/**
	 * Returns the board consideration note with the specified parameters 
	 * excluding the specified board consideration note.
	 * 
	 * @param hearingAnalysis hearing analysis
	 * @param description description
	 * @param date date
	 * @param excludedBoardConsiderationNote excluded board consideration note
	 * @return board consideration
	 */
	BoardConsiderationNote findExcluding(HearingAnalysis hearingAnalysis, 
			String description, Date date,
			BoardConsiderationNote excludedBoardConsiderationNote);
}