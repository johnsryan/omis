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

import omis.chronologicalnote.domain.ChronologicalNote;
import omis.chronologicalnote.domain.ChronologicalNoteCategory;
import omis.chronologicalnote.domain.ChronologicalNoteCategoryAssociation;
import omis.dao.GenericDao;

/**
 * Chronological note category association data access object.
 * 
 * @author Joel Norris
 * @version 0.1.0 (January 29, 2018)
 * @since OMIS 3.0
 */
public interface ChronologicalNoteCategoryAssociationDao extends GenericDao<ChronologicalNoteCategoryAssociation> {
	
	/**
	 * Returns the chronological note category association with the specified chronological note and chronological note category.
	 * 
	 * @param note chronological note
	 * @param category chronological note category
	 * @return chronological note category association
	 */
	@Deprecated
	ChronologicalNoteCategoryAssociation findByNoteAndCategory(ChronologicalNote note, ChronologicalNoteCategory category);
	
	/**
	 * Returns chronological note category associations for the specified chronological note.
	 * 
	 * @param note chronological note
	 * @return list of chronological note category association
	 */
	List<ChronologicalNoteCategoryAssociation> findChronologicalNoteCategoryAssociationsByNote(ChronologicalNote note);

	/**
	 * Returns the chronological note category association with the specified chronological note and chronological
	 * note category.
	 * 
	 * @param note chronological note
	 * @param category chronological note category
	 * @return chronological note category association
	 */
	ChronologicalNoteCategoryAssociation find(ChronologicalNote note, ChronologicalNoteCategory category);

	/**
	 * Returns associated chronological note categories for the specified chronological note.
	 * 
	 * @param note chronological note
	 * @return list of chronological note categories
	 */
	List<ChronologicalNoteCategory> findAssociatedCategories(ChronologicalNote note);
}