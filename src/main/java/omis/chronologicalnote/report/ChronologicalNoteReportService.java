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
package omis.chronologicalnote.report;

import java.util.List;

import omis.chronologicalnote.domain.ChronologicalNoteCategory;
import omis.chronologicalnote.domain.ChronologicalNoteCategoryGroup;
import omis.offender.domain.Offender;

/**
 * Report service for chronologocal note.
 * 
 * @author Yidong Li
 * @version 0.1.0 (Jan 30, 2018)
 * @since OMIS 3.0
 */
public interface ChronologicalNoteReportService {
	/**
	 * Returns the list of chronologocal note summaries on offender.
	 * 
	 * @param offender offender
	 * @return a list of chronological note summaries
	 */
	List<ChronologicalNoteSummary> findByOffender(Offender offender);
	
	/**
	 * Returns the list of chronological note summaries.
	 * 
	 * @param offender offender
	 * @param categories a list of categories
	 * @return a list of chronological note summaries
	 */
	List<ChronologicalNoteSummary> findByOffenderAndCategories(
		Offender offender, List<ChronologicalNoteCategory> categories);
	
	/**
	 * Returns all chronological note categories.
	 * 
	 * @return all chronological note categories
	 */
	List<ChronologicalNoteCategory> findCategories();
	
	/**
	 * Returns all chronological note category groups.
	 * 
	 * @return list of chronological note category groups
	 */
	List<ChronologicalNoteCategoryGroup> findGroups();
}
