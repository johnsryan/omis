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
package omis.family.service.delegate;

import java.util.List;

import omis.family.dao.FamilyAssociationCategoryDao;
import omis.family.domain.FamilyAssociationCategory;
import omis.family.domain.FamilyAssociationCategoryClassification;
import omis.family.exception.FamilyAssociationCategoryExistsException;
import omis.instance.factory.InstanceFactory;

/**
 * Family association category delegate.
 * 
 * @author Yidong Li
 * @author Sheronda Vaughn
 * @version 0.1.1 (Oct 6, 2015)
 * @since OMIS 3.0
 */
public class FamilyAssociationCategoryDelegate {

	/* Data access objects. */
	private FamilyAssociationCategoryDao familyAssociationCategoryDao;
	
	/* Instance factories. */
	private InstanceFactory<FamilyAssociationCategory> 
		familyAssociationCategoryInstanceFactory;

	/* Constructor. */

	/**
	 * Instantiates a family association category delegate with
	 * the specified data access object and instance factory.
	 * 
	 * @param familyAssociationCategoryDao family association category data 
	 * access object
	 * @param familyAssociationCategoryInstanceFactory family association 
	 * category instance factory
	 */
	public FamilyAssociationCategoryDelegate(
			final FamilyAssociationCategoryDao familyAssociationCategoryDao,
			final InstanceFactory<FamilyAssociationCategory> 
			familyAssociationCategoryInstanceFactory) {
		this.familyAssociationCategoryDao = familyAssociationCategoryDao;
		this.familyAssociationCategoryInstanceFactory 
			= familyAssociationCategoryInstanceFactory;
	}
	
	/* Management methods. */
	
	/**
	 * Find all family association categories.
	 * 
	 * @return created family association
	 */
	public List<FamilyAssociationCategory> findAll() {
		return this.familyAssociationCategoryDao.findAll();
	}
	
	/**
	 * Create a family association category.
	 * @param name name
	 * @param valid valid
	 * @param sortOrder sort order
	 * @param classification family association classification
	 * @return created family association category
	 * @throws FamilyAssociationCategoryExistsException  
	 * family association category exists exception.
	 */
	public FamilyAssociationCategory create(final String name, 
		final Boolean valid, final Short sortOrder, 
		final FamilyAssociationCategoryClassification classification) 
		throws FamilyAssociationCategoryExistsException {
		if (this.familyAssociationCategoryDao.find(name) != null) {
			throw new FamilyAssociationCategoryExistsException(
					"Duplicate family association category found");
		}
		FamilyAssociationCategory category 
			= this.familyAssociationCategoryInstanceFactory.createInstance();
		category.setClassification(classification);
		category.setName(name);
		category.setSortOrder(sortOrder);
		category.setValid(valid);
		return this.familyAssociationCategoryDao.makePersistent(category);
	}
	
	/**
	 * Remove a family association category.
	 * @param category family association category
	 * @throws FamilyAssociationCategoryExistsException family association 
	 * category exists exception
	 */
	public void remove(
		final FamilyAssociationCategory category) 
		throws FamilyAssociationCategoryExistsException {
		this.familyAssociationCategoryDao.makeTransient(category);
	}
}