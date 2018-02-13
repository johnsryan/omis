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
package omis.criminalassociation.service.delegate;

import java.util.List;

import omis.criminalassociation.dao.CriminalAssociationCategoryDao;
import omis.criminalassociation.domain.CriminalAssociationCategory;
import omis.criminalassociation.exception.CriminalAssociationCategoryExistsException;
import omis.instance.factory.InstanceFactory;

/**
 * Criminal association category delegate.
 *
 * @author Sheronda Vaughn
 * @version 0.1.0 (Feb 5, 2018)
 * @since OMIS 3.0
 */
public class CriminalAssociationCategoryDelegate {
	
	/* Data access objects. */
	private CriminalAssociationCategoryDao criminalAssociationCategoryDao;

	/* Instance factories. */
	private InstanceFactory<CriminalAssociationCategory> 
		criminalAssociationCategoryInstanceFactory;
	
	/** Instantiates an implementation of CriminalAssociationCategoryDelegate */
	public CriminalAssociationCategoryDelegate(
			final CriminalAssociationCategoryDao criminalAssociationCategoryDao,
			final InstanceFactory<CriminalAssociationCategory> 
				criminalAssociationCategoryInstanceFactory) {
		this.criminalAssociationCategoryDao = criminalAssociationCategoryDao;
		this.criminalAssociationCategoryInstanceFactory 
			= criminalAssociationCategoryInstanceFactory;
	}
	
	/* Methods. */
	
	/**
	 * Create criminal association category.
	 *
	 *
	 * @param name name
	 * @param sortOrder sort order
	 * @param valid valid
	 * @return criminal association category
	 * @throws CriminalAssociationCategoryExistsException criminal association
	 * category exists
	 */
	public CriminalAssociationCategory create(final String name, 
			final Short sortOrder, final Boolean valid) 
					throws CriminalAssociationCategoryExistsException {
		if (this.criminalAssociationCategoryDao.find(name) != null) {
			throw new CriminalAssociationCategoryExistsException(
					"Duplicate criminal association category exists");
		}
		CriminalAssociationCategory category 
			= this.criminalAssociationCategoryInstanceFactory.createInstance();
		category.setName(name);
		category.setSortOrder(sortOrder);
		category.setValid(valid);
		return this.criminalAssociationCategoryDao.makePersistent(category);
	}
	
	/**
	 * Remove criminal association category.
	 *
	 *
	 * @param category category
	 */
	public void remove(final CriminalAssociationCategory category) {
		this.criminalAssociationCategoryDao.makeTransient(category);		
	}

	/**
	 * Find all criminal association categories.
	 *
	 * @return created criminal association
	 */
	public List<CriminalAssociationCategory> 
		findCriminalAssociationCategories() {
		return this.criminalAssociationCategoryDao.findAll();
	}
}