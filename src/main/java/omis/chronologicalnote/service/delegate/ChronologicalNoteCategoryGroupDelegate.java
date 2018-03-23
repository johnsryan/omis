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
package omis.chronologicalnote.service.delegate;

import java.util.List;

import omis.chronologicalnote.dao.ChronologicalNoteCategoryGroupDao;
import omis.chronologicalnote.domain.ChronologicalNoteCategoryGroup;
import omis.chronologicalnote.exception.ChronologicalNoteCategoryGroupExistsException;
import omis.instance.factory.InstanceFactory;

/**
 * Chronological note category group delegate.
 * 
 * @author Yidong Li
 * @version 0.1.0 (March 5, 2018)
 * @since OMIS 3.0
 */
public class ChronologicalNoteCategoryGroupDelegate {

	/* Data access objects. */
	private ChronologicalNoteCategoryGroupDao chronologicalNoteCategoryGroupDao;
	
	/*Instance factories. */
	private InstanceFactory<ChronologicalNoteCategoryGroup>
	chronologicalNoteCategoryGroupInstanceFactory;
	
	/**
	 * Instantiates a chronological note category group delegate with the
	 * specified data access object and instance factory.
	 * 
	 * @param chronologicalNoteCategoryGroupDao chronological note category
	 * group data access object
	 * @param chronologicalNoteCategoryGroupInstanceFactory chronological note
	 * category group instance factory
	 */
	public ChronologicalNoteCategoryGroupDelegate(
		final ChronologicalNoteCategoryGroupDao chronologicalNoteCategoryGroupDao,
		final InstanceFactory<ChronologicalNoteCategoryGroup>
			chronologicalNoteCategoryGroupInstanceFactory) {
		this.chronologicalNoteCategoryGroupDao
		= chronologicalNoteCategoryGroupDao;
		this.chronologicalNoteCategoryGroupInstanceFactory
			= chronologicalNoteCategoryGroupInstanceFactory;
	}
	
	/**
	 * Creates a new chronological note category group with the specified
	 * chronological note category name.
	 * 
	 * @param name chronological note category group name
	 * @return newly created chronological note category group
	 * @throws ChronologicalNoteCategoryGroupExistsException Thrown when a
	 * duplicate chronological note category group is found
	 */
	public ChronologicalNoteCategoryGroup create(
			final String name, final Short sortOrder, final Boolean valid)
					throws ChronologicalNoteCategoryGroupExistsException {
		if (this.chronologicalNoteCategoryGroupDao.findByName(name)
			!= null) {
			throw new ChronologicalNoteCategoryGroupExistsException(""
					+ "Chronological note category group already existse");
		}
		ChronologicalNoteCategoryGroup group
		= this.chronologicalNoteCategoryGroupInstanceFactory.createInstance();
		group.setName(name);
		group.setSortOrder(sortOrder);
		group.setValid(valid);
		return this.chronologicalNoteCategoryGroupDao.makePersistent(group);
	}
	
	/**
	 * Removes chronological note category group.
	 * 
	 * @param group chronological note category group
	 *
	 */
	public void remove(final ChronologicalNoteCategoryGroup group){
		this.chronologicalNoteCategoryGroupDao.makeTransient(group);
	}
	
	/**
	 * Find all chronological note category group.
	 * 
	 * @return all chronological note category groups
	 */
	public List<ChronologicalNoteCategoryGroup> findAll() {
		List<ChronologicalNoteCategoryGroup> groups
			= this.chronologicalNoteCategoryGroupDao.findAll();
		return groups;
	}

}