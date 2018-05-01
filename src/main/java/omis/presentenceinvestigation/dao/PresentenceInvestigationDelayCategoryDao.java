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
package omis.presentenceinvestigation.dao;

import java.util.List;

import omis.dao.GenericDao;
import omis.presentenceinvestigation.domain.PresentenceInvestigationDelayCategory;

/**
 * Presentence investigation delay category data access object.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Apr 23, 2018)
 * @since OMIS 3.0
 */
public interface PresentenceInvestigationDelayCategoryDao
		extends GenericDao<PresentenceInvestigationDelayCategory> {
	
	/**
	 * Returns a presentence investigation delay category with the specified 
	 * name.
	 * 
	 * @param name name
	 * @return presentence investigation delay category
	 */
	public PresentenceInvestigationDelayCategory find(String name);
	
	/**
	 * Returns the presentence investigation delay category with the specified 
	 * name excluding the specified presentence investigation delay category.
	 * 
	 * @param name name
	 * @param presentenceInvestigationCategoryExcluded excluded presentence 
	 * investigation delay category
	 * @return presentence investigation delay category
	 */
	public PresentenceInvestigationDelayCategory findExcluding(String name,
			PresentenceInvestigationDelayCategory
				presentenceInvestigationDelayCategoryExcluded);
	
	/**
	 * Returns a list of all valid presentence investigation delay categories.
	 * 
	 * @return list of all valid presentence investigation delay categories
	 */
	public List<PresentenceInvestigationDelayCategory> 
			findPresentenceInvestigationDelayCategories();
}