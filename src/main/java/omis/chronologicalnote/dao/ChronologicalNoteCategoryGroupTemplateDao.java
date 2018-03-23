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

import omis.chronologicalnote.domain.ChronologicalNoteCategoryGroup;
import omis.chronologicalnote.domain.ChronologicalNoteCategoryGroupTemplate;
import omis.dao.GenericDao;

/**
 * Chronological note category group template data access object.
 * 
 * @author Yidong Li
 * @author Stephen Abson
 * @version 0.1.0 (March 5, 2018)
 * 
 */
public interface ChronologicalNoteCategoryGroupTemplateDao
		extends GenericDao<ChronologicalNoteCategoryGroupTemplate> {
	
	/**
	 * Returns a chronological note category group template.
	 * 
	 * @param group chronological note category group
	 * @param text text
	 * @return a chronological note category group template
	 */
	ChronologicalNoteCategoryGroupTemplate
	findChronologicalNoteCategoryGroupTemplate(
		ChronologicalNoteCategoryGroup group,
		String text);

	/**
	 * Returns templates by group.
	 * 
	 * @param group group
	 * @return templates by group
	 */
	List<ChronologicalNoteCategoryGroupTemplate> findByGroup(
			ChronologicalNoteCategoryGroup group);
}