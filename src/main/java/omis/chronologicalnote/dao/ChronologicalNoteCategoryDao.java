/*
 *  OMIS - Offender Management Information System
 *  Copyright (C) 2011 - 2017 State of Montana
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package omis.chronologicalnote.dao;

import java.util.List;

import omis.chronologicalnote.domain.ChronologicalNoteCategory;
import omis.chronologicalnote.domain.ChronologicalNoteCategoryGroup;
import omis.dao.GenericDao;

/**
 * Chronological note category data access object.
 * 
 * @author Joel Norris
 * @author Yidong Li
 * @author Stephen Abson
 */
public interface ChronologicalNoteCategoryDao
		extends GenericDao<ChronologicalNoteCategory> {
	
	/**
	 * Returns chronological note categories.
	 * 
	 * @return categories
	 * @deprecated use findAll()
	 */
	@Deprecated
	List<ChronologicalNoteCategory> findCategories();
	
	/**
	 * Returns the chronological note category.
	 * @param name name
	 * @param group group
	 * @return chronological note category
	 */
	ChronologicalNoteCategory find(
			String name, ChronologicalNoteCategoryGroup group);
	
	/**
	 * Returns a list of chronological note categories with the specified group.
	 * @param group chronological note category group
	 * @return
	 */
	List<ChronologicalNoteCategory> findCategoriesByGroup(
		ChronologicalNoteCategoryGroup group);
}