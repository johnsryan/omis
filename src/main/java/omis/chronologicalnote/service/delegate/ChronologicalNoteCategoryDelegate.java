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

import omis.chronologicalnote.dao.ChronologicalNoteCategoryDao;
import omis.chronologicalnote.domain.ChronologicalNoteCategory;
import omis.chronologicalnote.domain.ChronologicalNoteCategoryGroup;
import omis.chronologicalnote.exception.ChronologicalNoteCategoryExistsException;
import omis.instance.factory.InstanceFactory;

/**
 * Chronological note category delegate.
 * 
 * @author Joel Norris
 * @author Yidong Li
 * @version 0.1.0 (February 1, 2018)
 * @since OMIS 3.0
 */
public class ChronologicalNoteCategoryDelegate {
	
	/* Data access objects. */
	private ChronologicalNoteCategoryDao chronologicalNoteCategoryDao;
	
	/* Instance factories. */
	private InstanceFactory<ChronologicalNoteCategory> chronlogicalNoteCategoryInstanceFactory;
	
	/**
	 * Instantiates a chronological note category delegate with the specified data access object.
	 * 
	 * @param chronologicalNoteCategoryDao chronological note category data access object
	 */
	public ChronologicalNoteCategoryDelegate(final ChronologicalNoteCategoryDao chronologicalNoteCategoryDao,
			final InstanceFactory<ChronologicalNoteCategory> chronologicalNoteCategoryInstanceFactory) {
		this.chronologicalNoteCategoryDao = chronologicalNoteCategoryDao;
		this.chronlogicalNoteCategoryInstanceFactory = chronologicalNoteCategoryInstanceFactory;
	}
	
	/**
	 * Returns chronological note categories.
	 * 
	 * @return list of chronological note categories
	 */
	public List<ChronologicalNoteCategory> findAll() {
		return this.chronologicalNoteCategoryDao.findAll();
	}
	
	/**
	 * Creates chronological note category.
	 * 
	 * @param name name
	 * @param group group
	 * @param valid valid
	 * @return newly created chronological note category
	 * @throws ChronologicalNoteCategoryExistsException Thrown when a duplicate chronological note category
	 * with the specified name is found.
	 */
	public ChronologicalNoteCategory create(final String name,
			final ChronologicalNoteCategoryGroup group,
			final Boolean valid)
					throws ChronologicalNoteCategoryExistsException {
		if (this.chronologicalNoteCategoryDao.find(name, group) != null) {
			throw new ChronologicalNoteCategoryExistsException("Chronological note exists");
		}
		ChronologicalNoteCategory category = this.chronlogicalNoteCategoryInstanceFactory.createInstance();
		category.setName(name);
		category.setValid(valid);
		category.setGroup(group);
		return this.chronologicalNoteCategoryDao.makePersistent(category);
	}
	
	/**
	 * Creates chronological note category.
	 * 
	 * @param group chronological note category group
	 * @return A list of chronological note categories
	 *
	 */
	public List<ChronologicalNoteCategory> findCategoriesByGroup(
		final ChronologicalNoteCategoryGroup group) {
		return this.chronologicalNoteCategoryDao.findCategoriesByGroup(group);
	}
}