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
package omis.victim.dao;

import java.util.Date;
import java.util.List;

import omis.dao.GenericDao;
import omis.person.domain.Person;
import omis.relationship.domain.Relationship;
import omis.victim.domain.VictimAssociation;
import omis.victim.domain.VictimNote;
import omis.victim.domain.VictimNoteCategory;

/**
 * Data access object for victim notes.
 *
 * @author Stephen Abson
 * @author Sheronda Vaughn
 * @version 0.0.1 (Jul 22, 2015)
 * @since OMIS 3.0
 */
public interface VictimNoteDao
		extends GenericDao<VictimNote> {

	/**
	 * Finds victim note.
	 * 
	 * @param victim victim
	 * @param category category
	 * @param date date
	 * @param value value
	 * @return victim note
	 */
	VictimNote find(Person victim, VictimNoteCategory category, Date date, 
			String value);
	
	/**
	 * Finds victim note excluding notes.
	 * 
	 * @param victim victim
	 * @param category category
	 * @param date date
	 * @param value value
	 * @param excludedNotes notes to exclude
	 * @return victim note existing notes
	 */
	VictimNote findExcluding(Person victim, VictimNoteCategory category,
			Date date, String value, VictimNote... excludedNotes);

	/**
	 * Returns victim notes by victim.
	 * 
	 * @param victim victim
	 * @return victim notes by victim
	 */
	List<VictimNote> findByVictim(Person victim);

	/**
	 * Returns victim notes by association.
	 * 
	 * @param association association
	 * @return victim notes by association
	 */
	List<VictimNote> findByAssociation(VictimAssociation association);
	
	/**
	 * Returns count of notes by association.
	 * 
	 * @param association association
	 * @return count of notes by association
	 */
	long countByAssociation(VictimAssociation association);
	
	/**
	 * Removes notes by association.
	 * 
	 * @param association association
	 * @return number of notes removed by association
	 */
	int removeByAssociation(VictimAssociation association);

	/**
	 * Returns count of notes by victim.
	 * 
	 * @param victim victim
	 * @return count of note by victim
	 */
	long countByVictim(Person victim);

	/**
	 * Removes notes by relationship.
	 * 
	 * @param relationship relationship by which to remove notes
	 * @return number of notes removed
	 */
	int removeByRelationship(Relationship relationship);
}