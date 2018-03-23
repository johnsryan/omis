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
package omis.assessment.service.delegate;

import omis.assessment.dao.SumRatedCategoryDao;
import omis.assessment.domain.RatingCategory;
import omis.assessment.domain.SumRatedCategory;
import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;

/**
 * Sum rated category delegate.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Mar 12, 2018)
 * @since OMIS 3.0
 */
public class SumRatedCategoryDelegate {

	/* Data access objects. */
	
	private final SumRatedCategoryDao sumRatedCategoryDao;

	/* Instance factories. */
	
	private final InstanceFactory<SumRatedCategory> 
			sumRatedCategoryInstanceFactory;
	
	/**
	 * Instantiates an implementation of sum rated category delegate with the 
	 * specified date access object and instance factory.
	 * 
	 * @param sumRatedCategoryDao sum rated category data access object
	 * @param sumRatedCategoryInstanceFactory sum rated category instance 
	 * factory
	 */
	public SumRatedCategoryDelegate(
			final SumRatedCategoryDao sumRatedCategoryDao,
			final InstanceFactory<SumRatedCategory> 
					sumRatedCategoryInstanceFactory) {
		this.sumRatedCategoryDao = sumRatedCategoryDao;
		this.sumRatedCategoryInstanceFactory = sumRatedCategoryInstanceFactory;
	}
	
	/**
	 * Creates a new sum rated category.
	 * 
	 * @param ratingCategory rating category
	 * @param valid valid
	 * @return sum rated category
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	public SumRatedCategory create(final RatingCategory ratingCategory, 
			final Boolean valid) throws DuplicateEntityFoundException {
		if (this.sumRatedCategoryDao.find(ratingCategory, valid) != null) {
			throw new DuplicateEntityFoundException(
					"Sum rated category already exists.");
		}
		SumRatedCategory sumRatedCategory = this.sumRatedCategoryInstanceFactory
				.createInstance();
		populateSumRatedCategory(sumRatedCategory, ratingCategory, valid);
		return this.sumRatedCategoryDao.makePersistent(sumRatedCategory);
	}
	
	/**
	 * Updates an existing sum rated category.
	 * 
	 * @param sumRatedCategory sum rated category
	 * @param ratingCategory rating category
	 * @param valid valid
	 * @return sum rated category
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	public SumRatedCategory update(final SumRatedCategory sumRatedCategory,
			final RatingCategory ratingCategory, final Boolean valid) 
					throws DuplicateEntityFoundException {
		if (this.sumRatedCategoryDao.findExcluding(ratingCategory, valid, 
				sumRatedCategory) != null) {
			throw new DuplicateEntityFoundException(
					"Sum rated category already exists.");
		}
		populateSumRatedCategory(sumRatedCategory, ratingCategory, valid);
		return this.sumRatedCategoryDao.makePersistent(sumRatedCategory);
	}

	/**
	 * Removes the specified sum rated category.
	 * 
	 * @param sumRatedCategory sum rated category
	 */
	public void remove(final SumRatedCategory sumRatedCategory) {
		this.sumRatedCategoryDao.makeTransient(sumRatedCategory);
	}
	
	/**
	 * Returns the sum rated category for the specified rating category.
	 * 
	 * @param ratingCategory rating category
	 * @return sum rated category
	 */
	public SumRatedCategory findByRatingCategory(RatingCategory ratingCategory) {
		return this.sumRatedCategoryDao.findByRatingCategory(ratingCategory);
	}
	
	// Populates a sum rated category
	private void populateSumRatedCategory(
			final SumRatedCategory sumRatedCategory, 
			final RatingCategory ratingCategory, final Boolean valid) {
		sumRatedCategory.setRatingCategory(ratingCategory);
		sumRatedCategory.setValid(valid);
	}
}