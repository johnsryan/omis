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
package omis.hearinganalysis.dao;

import java.util.List;

import omis.dao.GenericDao;
import omis.hearinganalysis.domain.HearingAnalysisCategory;

/**
 * Data access object for hearing analysis category.
 * 
 * @author Josh Divine
 * @author Annie Wahl
 * @version 0.1.1 (May 29, 2018)
 * @since OMIS 3.0
 */
public interface HearingAnalysisCategoryDao 
		extends GenericDao<HearingAnalysisCategory> {

	/**
	 * Returns the hearing analysis category that matches the specified name.
	 * 
	 * @param name name
	 * @return hearing analysis category
	 */
	HearingAnalysisCategory find(String name);

	/**
	 * Returns the hearing analysis category that matches the specified name 
	 * excluding the specified hearing analysis category.
	 * 
	 * @param name name
	 * @param excludedHearingAnalysisCategory excluded hearing analysis category
	 * @return hearing analysis category
	 */
	HearingAnalysisCategory findExcluding(String name, 
			HearingAnalysisCategory excludedHearingAnalysisCategory);
	
	/**
	 * Returns a list of all valid Hearing Analysis Categories.
	 * 
	 * @return List of all valid Hearing Analysis Categories.
	 */
	List<HearingAnalysisCategory> findAllHearingAnalysisCategories();

}
