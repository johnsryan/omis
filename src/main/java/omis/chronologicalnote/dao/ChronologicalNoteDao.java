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

import java.util.Date;
import java.util.List;

import omis.chronologicalnote.domain.ChronologicalNote;
import omis.dao.GenericDao;
import omis.offender.domain.Offender;

/**
 * Chronological note data access object.
 * 
 * @author Joel Norris
 * @author Sheronda Vaughn
 * @version 0.1.0 (January 29, 2018)
 * @since OMIS 3.0
 */
public interface ChronologicalNoteDao extends GenericDao<ChronologicalNote> {

	/**
	 * Returns list of chronological notes for the specified offender.
	 * 
	 * @param offender offender
	 * @return list of chronological notes
	 */
	List<ChronologicalNote> findByOffender(Offender offender);
	
	/**
	 * Returns chronological note with the specified date, offender, and title.
	 * 
	 * @param date date
	 * @param offender offender
	 * @param title title
	 * @return chronological note
	 */
	ChronologicalNote find(Date date, Offender offender, String title);
	
	/**
	 * Returns chronological note with the specified date, offender, and title excluding the specified
	 * chronological note.
	 * 
	 * @param note chronological note
	 * @param date date
	 * @param offender offender
	 * @param title title
	 * @return chronological note
	 */
	ChronologicalNote findExcluding(ChronologicalNote note, Date date, Offender offender, String title);
}