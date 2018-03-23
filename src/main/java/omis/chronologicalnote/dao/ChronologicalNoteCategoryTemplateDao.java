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

import omis.chronologicalnote.domain.ChronologicalNoteCategory;
import omis.chronologicalnote.domain.ChronologicalNoteCategoryTemplate;
import omis.dao.GenericDao;

/**
 * Chronological note category template data access object.
 * 
 * @author Yidong Li
 * @version 0.1.0 (March 5, 2018)
 *
 */
public interface ChronologicalNoteCategoryTemplateDao
	extends GenericDao<ChronologicalNoteCategoryTemplate> {
	/**
	 * Returns the chronological note with the specified name.
	 * @param category chronological note category
	 * @return chronological note category template
	 */
	ChronologicalNoteCategoryTemplate find(ChronologicalNoteCategory
		category);
}