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
package omis.victim.service;

import java.util.Date;
import java.util.List;

import omis.person.domain.Person;
import omis.victim.domain.VictimAssociation;
import omis.victim.domain.VictimNote;
import omis.victim.domain.VictimNoteCategory;
import omis.victim.exception.VictimNoteExistsException;

/**
 * Service for victim notes.
 *
 * @author Stephen Abson
 * @author Sheronda Vaughn
 * @version 0.0.1 (Jul 22, 2015)
 * @since OMIS 3.0
 */
public interface VictimNoteService {

	/**
	 * Creates victim note.
	 * 
	 * @param victim victim
	 * @param category category
	 * @param association association
	 * @param date date
	 * @param value value
	 * @return created victim note
	 * @throws VictimNoteExistsException if victim note exists
	 */
	VictimNote create(Person victim, VictimNoteCategory category,
			VictimAssociation association, Date date, String value)
					throws VictimNoteExistsException;
	
	/**
	 * Updates victim note.
	 * 
	 * @param victimNote victim note to update
	 * @param category category
	 * @param association association
	 * @param date date
	 * @param value value
	 * @return updated victim note
	 * @throws VictimNoteExistsException if victim note exists
	 */
	VictimNote update(VictimNote victimNote, VictimNoteCategory category,
			VictimAssociation association, Date date, String value)
					throws VictimNoteExistsException;
	
	/**
	 * Returns victim notes by victim.
	 * 
	 * @param victim victim
	 * @return victim notes by victim
	 */
	List<VictimNote> findByVictim(Person victim);
	
	/**
	 * Returns associations of victim.
	 * 
	 * @param victim victim
	 * @return associations of victim
	 */
	List<VictimAssociation> findAssociationsForVictim(Person victim);
	
	/**
	 * Returns categories.
	 * 
	 * @return categories
	 */
	List<VictimNoteCategory> findCategories();
	
	/**
	 * Removes victim note.
	 * 
	 * @param victimNote victim note to remove
	 */
	void remove(VictimNote victimNote);
}