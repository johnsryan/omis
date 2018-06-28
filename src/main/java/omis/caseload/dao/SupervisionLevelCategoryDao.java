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
package omis.caseload.dao;

import java.util.List;

import omis.caseload.domain.SupervisionLevelCategory;
import omis.dao.GenericDao;

/**
 * Data access object for supervision level category.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Jun 13, 2018)
 * @since OMIS 3.0
 */
public interface SupervisionLevelCategoryDao
		extends GenericDao<SupervisionLevelCategory>{

	/**
	 * Returns the supervision level category for the specified parameters.
	 * 
	 * @param description description
	 * @return supervision level category
	 */
	SupervisionLevelCategory find(String description);
	
	/**
	 * Returns the supervision level category for the specified parameters 
	 * excluding the specified supervision level category.
	 * 
	 * @param description description
	 * @param excludedSupervisionLevelCategory excluded supervision level 
	 * category
	 * @return supervision level category
	 */
	SupervisionLevelCategory findExcluding(String description, 
			SupervisionLevelCategory excludedSupervisionLevelCategory);

	/**
	 * Returns a list of supervision level categories.
	 * 
	 * @return list of supervision level categories
	 */
	List<SupervisionLevelCategory> findValid();
}