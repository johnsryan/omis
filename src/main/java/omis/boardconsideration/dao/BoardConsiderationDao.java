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

import java.util.List;

import omis.boardconsideration.domain.BoardConsideration;
import omis.dao.GenericDao;
import omis.hearinganalysis.domain.HearingAnalysis;

/**
 * Board consideration data access object.
 * 
 * @author Josh Divine
 * @version 0.1.0 (May 29, 2018)
 * @since OMIS 3.0
 */
public interface BoardConsiderationDao extends GenericDao<BoardConsideration> {

	/**
	 * Returns a list of board considerations for the specified hearing 
	 * analysis.
	 * 
	 * @param hearingAnalysis hearing analysis
	 * @return list of board considerations
	 */
	List<BoardConsideration> findByHearingAnalysis(
			HearingAnalysis hearingAnalysis);

	/**
	 * Returns the board consideration with the specified parameters.
	 * 
	 * @param hearingAnalysis hearing analysis
	 * @param title title
	 * @return board consideration
	 */
	BoardConsideration find(HearingAnalysis hearingAnalysis, String title);

	/**
	 * Returns the board consideration with the specified parameters excluding 
	 * the specified board consideration.
	 * 
	 * @param hearingAnalysis hearing analysis
	 * @param title title
	 * @param excludedBoardConsideration excluded board consideration
	 * @return board consideration
	 */
	BoardConsideration findExcluding(HearingAnalysis hearingAnalysis, 
			String title, BoardConsideration excludedBoardConsideration);
}