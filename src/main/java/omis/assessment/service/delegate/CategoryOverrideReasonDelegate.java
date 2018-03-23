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

import java.util.List;

import omis.assessment.dao.CategoryOverrideReasonDao;
import omis.assessment.domain.CategoryOverrideReason;
import omis.assessment.domain.RatingCategory;
import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;

/**
 * Category override reason delegate.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 26, 2018)
 * @since OMIS 3.0
 */
public class CategoryOverrideReasonDelegate {

	/* Data access objects. */
	
	private final CategoryOverrideReasonDao categoryOverrideReasonDao;

	/* Instance factories. */
	
	private final InstanceFactory<CategoryOverrideReason> 
			categoryOverrideReasonInstanceFactory;
	
	/** 
	 * Instantiates an implementation of category override reason delegate 
	 * with the specified date access object and instance factory.
	 * 
	 * @param categoryOverrideReasonDao category override reason data access 
	 * object
	 * @param categoryOverrideReasonInstanceFactory category override reason 
	 * instance factory
	 * @param auditComponentRetriever audit component retriever
	 */
	public CategoryOverrideReasonDelegate(
			final CategoryOverrideReasonDao categoryOverrideReasonDao,
			final InstanceFactory<CategoryOverrideReason> 
					categoryOverrideReasonInstanceFactory) {
		this.categoryOverrideReasonDao = categoryOverrideReasonDao;
		this.categoryOverrideReasonInstanceFactory = 
				categoryOverrideReasonInstanceFactory;
	}
	
	/**
	 * Creates a new category override reason.
	 * 
	 * @param name name
	 * @param ratingCategory rating category
	 * @param valid valid
	 * @return category override reason
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	public CategoryOverrideReason create(final String name, 
			final RatingCategory ratingCategory, final Boolean valid) 
					throws DuplicateEntityFoundException {
		if (this.categoryOverrideReasonDao.find(name, ratingCategory) != null) {
			throw new DuplicateEntityFoundException(
					"Category override reason already exists.");
		}
		CategoryOverrideReason categoryOverrideReason = this
				.categoryOverrideReasonInstanceFactory.createInstance();
		populateCategoryOverrideReason(categoryOverrideReason, name, 
				ratingCategory, valid);
		return this.categoryOverrideReasonDao.makePersistent(
				categoryOverrideReason);
	}
	
	/**
	 * Updates an existing category override reason.
	 * 
	 * @param categoryOverrideReason category override reason
	 * @param name name
	 * @param ratingCategory rating category
	 * @param valid valid
	 * @return category override reason
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	public CategoryOverrideReason update(
			final CategoryOverrideReason categoryOverrideReason, 
			final String name, final RatingCategory ratingCategory, 
			final Boolean valid) throws DuplicateEntityFoundException {
		if (this.categoryOverrideReasonDao.findExcluding(name, ratingCategory, 
				categoryOverrideReason) != null) {
			throw new DuplicateEntityFoundException(
					"Category override reason already exists.");
		}
		populateCategoryOverrideReason(categoryOverrideReason, name, 
				ratingCategory, valid);
		return this.categoryOverrideReasonDao.makePersistent(
				categoryOverrideReason);
	}
	
	/**
	 * Removes the specified category override reason.
	 * 
	 * @param categoryOverrideReason category override reason
	 */
	public void remove(final CategoryOverrideReason categoryOverrideReason) {
		this.categoryOverrideReasonDao.makeTransient(categoryOverrideReason);
	}

	/**
	 * Returns a list of category override reasons.
	 * 
	 * @return list of category override reasons
	 */
	public List<CategoryOverrideReason> findAll() {
		return this.categoryOverrideReasonDao.findAll();
	}
	
	// Populates a category override reason
	private void populateCategoryOverrideReason(
			final CategoryOverrideReason categoryOverrideReason, 
			final String name, final RatingCategory ratingCategory, 
			final Boolean valid) {
		categoryOverrideReason.setName(name);
		categoryOverrideReason.setRatingCategory(ratingCategory);
		categoryOverrideReason.setValid(valid);
	}
}