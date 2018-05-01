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
package omis.presentenceinvestigation.service.delegate;

import java.util.List;

import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;
import omis.presentenceinvestigation.dao.PresentenceInvestigationDelayCategoryDao;
import omis.presentenceinvestigation.domain.PresentenceInvestigationDelayCategory;

/**
 * Presentence investigation delay category delegate.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Apr 23, 2018)
 * @since OMIS 3.0
 */
public class PresentenceInvestigationDelayCategoryDelegate {

	/* Data access objects. */
	
	private final PresentenceInvestigationDelayCategoryDao 
		presentenceInvestigationDelayCategoryDao;
	
	/* Instance factories. */
	
	private final InstanceFactory<PresentenceInvestigationDelayCategory> 
		presentenceInvestigationDelayCategoryInstanceFactory;
	
	/** 
	 * Instantiates an implementation of presentence investigation delay 
	 * category delegate with the specified data access object and instance 
	 * factory.
	 *  
	 * @param presentenceInvestigationDelayCategoryDao presentence investigation
	 * delay category data access object
	 * @param presentenceInvestigationDelayCategoryInstanceFactory presentence
	 * investigation delay category instance factory
	 */
	public PresentenceInvestigationDelayCategoryDelegate(
			final PresentenceInvestigationDelayCategoryDao 
				presentenceInvestigationDelayCategoryDao,
			final InstanceFactory<PresentenceInvestigationDelayCategory>
				presentenceInvestigationDelayCategoryInstanceFactory) {
		this.presentenceInvestigationDelayCategoryDao 
			= presentenceInvestigationDelayCategoryDao;
		this.presentenceInvestigationDelayCategoryInstanceFactory 
			= presentenceInvestigationDelayCategoryInstanceFactory;
	}
	
	/**
	 * Creates a new presentence investigation delay category.
	 * 
	 * @param name name
	 * @param valid valid
	 * @return presentence investigation delay category
	 * @throws DuplicateEntityFoundException if entity already exists
	 */
	public PresentenceInvestigationDelayCategory create(final String name, 
			final Boolean valid) throws DuplicateEntityFoundException {
		if (this.presentenceInvestigationDelayCategoryDao.find(name) != null) {
			throw new DuplicateEntityFoundException(
					"Presentence investigation delay category already exists.");
		}
		PresentenceInvestigationDelayCategory 
				presentenceInvestigationDelayCategory = this
				.presentenceInvestigationDelayCategoryInstanceFactory
				.createInstance();
		populatePresentenceInvestigationDelayCategory(
				presentenceInvestigationDelayCategory, name, valid);
		return this.presentenceInvestigationDelayCategoryDao.makePersistent(
				presentenceInvestigationDelayCategory);
	}
	
	/**
	 * Updates an existing presentence investigation delay category.
	 * 
	 * @param presentenceInvestigationDelayCategory presentence investigation 
	 * delay category
	 * @param name name
	 * @param valid valid
	 * @return presentence investigation delay category
	 * @throws DuplicateEntityFoundException if entity already exists
	 */
	public PresentenceInvestigationDelayCategory update(
			final PresentenceInvestigationDelayCategory 
					presentenceInvestigationDelayCategory, final String name, 
			final Boolean valid) throws DuplicateEntityFoundException {
		if (this.presentenceInvestigationDelayCategoryDao.findExcluding(name, 
				presentenceInvestigationDelayCategory) != null) {
			throw new DuplicateEntityFoundException(
					"Presentence investigation delay category already exists.");
		}
		populatePresentenceInvestigationDelayCategory(
				presentenceInvestigationDelayCategory, name, valid);
		return this.presentenceInvestigationDelayCategoryDao.makePersistent(
				presentenceInvestigationDelayCategory);
	}
	
	/**
	 * Removes the specified presentence investigation delay category.
	 * 
	 * @param presentenceInvestigationDelayCategory presentence investigation 
	 * delay category
	 */
	public void remove(final PresentenceInvestigationDelayCategory 
					presentenceInvestigationDelayCategory) {
		this.presentenceInvestigationDelayCategoryDao.makeTransient(
				presentenceInvestigationDelayCategory);
	}
	
	/**
	 * Returns a list of presentence investigation delay categories.
	 *  
	 * @return list of presentence investigation delay categories
	 */
	public List<PresentenceInvestigationDelayCategory> 
			findPresentenceInvestigationDelayCategories() {
		return this.presentenceInvestigationDelayCategoryDao
				.findPresentenceInvestigationDelayCategories();
	}

	// Populates a presentence investigation delay category
	private void populatePresentenceInvestigationDelayCategory(
			final PresentenceInvestigationDelayCategory 
					presentenceInvestigationDelayCategory, final String name,
			final Boolean valid) {
		presentenceInvestigationDelayCategory.setName(name);
		presentenceInvestigationDelayCategory.setValid(valid);
	}
}