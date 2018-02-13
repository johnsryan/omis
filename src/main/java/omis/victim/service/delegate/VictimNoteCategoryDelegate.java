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
package omis.victim.service.delegate;

import java.util.List;

import omis.instance.factory.InstanceFactory;
import omis.victim.dao.VictimNoteCategoryDao;
import omis.victim.domain.VictimNoteCategory;
import omis.victim.exception.VictimNoteCategoryExistsException;

/**
 * Delegate for categories of victim note.
 *
 * @author Stephen Abson
 * @author Sheronda Vaughn
 * @version 0.0.1 (Sep 1, 2015)
 * @since OMIS 3.0
 */
public class VictimNoteCategoryDelegate {

	/* DAOs. */
	
	private final VictimNoteCategoryDao victimNoteCategoryDao;
	
	/* Instance factories. */
	private InstanceFactory<VictimNoteCategory> 
		victimNoteCategoryInstanceFactory;
	
	/* Constructors. */
	
	/**
	 * Instantiates delegate for victim note categories.
	 * 
	 * @param victimNoteCategoryDao data access object for victim notes
	 */
	public VictimNoteCategoryDelegate(
			final VictimNoteCategoryDao victimNoteCategoryDao,
			final InstanceFactory<VictimNoteCategory> 
				victimNoteCategoryInstanceFactory) {
		this.victimNoteCategoryDao = victimNoteCategoryDao;
		this.victimNoteCategoryInstanceFactory 
			= victimNoteCategoryInstanceFactory;
	}
	
	/**
	 * Returns victim note categories.
	 * 
	 * @return victim note categories
	 */
	public List<VictimNoteCategory> findAll() {
		return this.victimNoteCategoryDao.findAll();
	}
	
	/**
	 * Create victim note category.
	 *
	 *
	 * @param name name
	 * @param sortOrder sort order
	 * @param contact contact
	 * @param valid valid
	 * @return note category
	 * @throws VictimNoteCategoryExistsException victim note category exists
	 */
	public VictimNoteCategory create(final String name, final Short sortOrder, 
			final Boolean contact, final Boolean valid) 
					throws VictimNoteCategoryExistsException {
		if (this.victimNoteCategoryDao.find(name) != null) {
			throw new VictimNoteCategoryExistsException(
					"Duplicate victim note category found.");
		}
		VictimNoteCategory category 
			= this.victimNoteCategoryInstanceFactory.createInstance();
		category.setContact(contact);
		category.setName(name);
		category.setSortOrder(sortOrder);
		category.setValid(valid);
		return this.victimNoteCategoryDao.makePersistent(category);
	}
}