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
package omis.parolereview.service.delegate;

import java.util.List;

import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;
import omis.parolereview.dao.StaffRoleCategoryDao;
import omis.parolereview.domain.StaffRoleCategory;

/**
 * Staff role category delegate.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 8, 2018)
 * @since OMIS 3.0
 */
public class StaffRoleCategoryDelegate {

	/* Data access objects. */
	
	private final StaffRoleCategoryDao staffRoleCategoryDao;

	/* Instance factories. */
	
	private final InstanceFactory<StaffRoleCategory> 
			staffRoleCategoryInstanceFactory;
	
	/** Instantiates an implementation of staff role category delegate with the 
	 * specified date access object and instance factory.
	 * 
	 * @param staffRoleCategoryDao staff role category data access object
	 * @param staffRoleCategoryInstanceFactory staff role category instance 
	 * factory
	 */
	public StaffRoleCategoryDelegate(
			final StaffRoleCategoryDao staffRoleCategoryDao,
			final InstanceFactory<StaffRoleCategory> 
					staffRoleCategoryInstanceFactory) {
		this.staffRoleCategoryDao = staffRoleCategoryDao;
		this.staffRoleCategoryInstanceFactory = 
				staffRoleCategoryInstanceFactory;
	}
	
	/**
	 * Creates a new staff role category.
	 * 
	 * @param name name
	 * @param valid valid
	 * @return staff role category
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	public StaffRoleCategory create(final String name, final Boolean valid) 
			throws DuplicateEntityFoundException {
		if (this.staffRoleCategoryDao.find(name) != null) {
			throw new DuplicateEntityFoundException(
					"Staff role category already exists.");
		}
		StaffRoleCategory staffRoleCategory = this
				.staffRoleCategoryInstanceFactory.createInstance();
		staffRoleCategory.setName(name);
		staffRoleCategory.setValid(valid);
		return this.staffRoleCategoryDao.makePersistent(staffRoleCategory);
	}
	
	/**
	 * Updates an existing staff role category.
	 * 
	 * @param staffRoleCategory staff role category
	 * @param name name
	 * @param valid valid
	 * @return staff role category
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	public StaffRoleCategory udpate(final StaffRoleCategory staffRoleCategory, 
			final String name, final Boolean valid) 
					throws DuplicateEntityFoundException {
		if (this.staffRoleCategoryDao.findExcluding(name, staffRoleCategory) != 
				null) {
			throw new DuplicateEntityFoundException(
					"Staff role category already exists.");
		}
		staffRoleCategory.setName(name);
		staffRoleCategory.setValid(valid);
		return this.staffRoleCategoryDao.makePersistent(staffRoleCategory);
	}
	
	/**
	 * Removes the specified staff role category.
	 * 
	 * @param staffRoleCategory staff role category
	 */
	public void remove(final StaffRoleCategory staffRoleCategory) {
		this.staffRoleCategoryDao.makeTransient(staffRoleCategory);
	}
	
	/**
	 * Returns a list of staff role categories.
	 * 
	 * @return list of staff role categories
	 */
	public List<StaffRoleCategory> findAll() {
		return this.staffRoleCategoryDao.findAll();
	}
}
