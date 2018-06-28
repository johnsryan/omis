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
package omis.caseload.service.delegate;

import java.util.List;

import omis.caseload.dao.SupervisionLevelCategoryDao;
import omis.caseload.domain.SupervisionLevelCategory;
import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;

/**
 * Supervision level category delegate.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Jun 13, 2018)
 * @since OMIS 3.0
 */
public class SupervisionLevelCategoryDelegate {

	/* DAOs */
	private final SupervisionLevelCategoryDao supervisionLevelCategoryDao;
	
	/* Instance factory. */
	private final InstanceFactory<SupervisionLevelCategory> 
					supervisionLevelCategoryInstanceFactory;
	
	/**
	 * Instantiates an implementation of supervision level category delegate 
	 * with the specified data access object and instance factory.
	 * 
	 * @param boardHearingDecisionDao board hearing decision data access object
	 * @param boardHearingDecisionInstanceFactory board hearing decision 
	 * instance factory
	 */
	public SupervisionLevelCategoryDelegate(
			final SupervisionLevelCategoryDao supervisionLevelCategoryDao,
			final InstanceFactory<SupervisionLevelCategory> 
					supervisionLevelCategoryInstanceFactory) {
		this.supervisionLevelCategoryDao = supervisionLevelCategoryDao;
		this.supervisionLevelCategoryInstanceFactory = 
				supervisionLevelCategoryInstanceFactory;
	}
	
	/**
	 * Creates a new supervision level category.
	 * 
	 * @param description description
	 * @param valid valid
	 * @return supervision level category
	 * @throws DuplicateEntityFoundException if entity already exists
	 */
	public SupervisionLevelCategory create(final String description, 
			final Boolean valid) throws DuplicateEntityFoundException {
		if (this.supervisionLevelCategoryDao.find(description) != null) {
			throw new DuplicateEntityFoundException(
					"Supervision level category already exists.");
		}
		SupervisionLevelCategory supervisionLevelCategory = this
				.supervisionLevelCategoryInstanceFactory.createInstance();
		populateSupervisionLevelCategory(supervisionLevelCategory, description, 
				valid);
		return this.supervisionLevelCategoryDao.makePersistent(
				supervisionLevelCategory);
	}
	
	/**
	 * Updates an existing supervision level category.
	 * 
	 * @param supervisionLevelCategory supervision level category
	 * @param description description
	 * @param valid valid
	 * @return supervision level category
	 * @throws DuplicateEntityFoundException if entity already exists
	 */
	public SupervisionLevelCategory update(
			final SupervisionLevelCategory supervisionLevelCategory, 
			final String description, final Boolean valid) 
					throws DuplicateEntityFoundException {
		if (this.supervisionLevelCategoryDao.findExcluding(description, 
				supervisionLevelCategory) != null) {
			throw new DuplicateEntityFoundException(
					"Supervision level category already exists.");
		}
		populateSupervisionLevelCategory(supervisionLevelCategory, description, 
				valid);
		return this.supervisionLevelCategoryDao.makePersistent(
				supervisionLevelCategory);
	}

	/**
	 * Removes the specified supervision level category.
	 * 
	 * @param supervisionLevelCategory supervision level category
	 */
	public void remove(final SupervisionLevelCategory supervisionLevelCategory) {
		this.supervisionLevelCategoryDao.makeTransient(supervisionLevelCategory);
	}
	
	/**
	 * Returns a list of supervision level categories.
	 * 
	 * @return list of supervision level categories
	 */
	public List<SupervisionLevelCategory> findValid() {
		return this.supervisionLevelCategoryDao.findValid();
	}
	
	// Populates a supervision level category
	private void populateSupervisionLevelCategory(
			final SupervisionLevelCategory supervisionLevelCategory,
			final String description, final Boolean valid) {
		supervisionLevelCategory.setDescription(description);
		supervisionLevelCategory.setValid(valid);
	}
}