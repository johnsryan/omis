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

import java.math.BigDecimal;
import java.util.List;

import omis.assessment.dao.RatingCategoryDao;
import omis.assessment.domain.RatingCategory;
import omis.assessment.domain.RatingCategorySignificance;
import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;
import omis.questionnaire.domain.QuestionnaireType;

/**
 * Rating category delegate.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 26, 2018)
 * @since OMIS 3.0
 */
public class RatingCategoryDelegate {

	/* Data access objects. */
	
	private final RatingCategoryDao ratingCategoryDao;

	/* Instance factories. */
	
	private final InstanceFactory<RatingCategory> ratingCategoryInstanceFactory;
	
	/**
	 * Instantiates an implementation of rating category delegate with the 
	 * specified date access object and instance factory.
	 * 
	 * @param ratingCategoryDao rating category data access object
	 * @param ratingCategoryInstanceFactory rating category instance factory
	 */
	public RatingCategoryDelegate(final RatingCategoryDao ratingCategoryDao,
			final InstanceFactory<RatingCategory> ratingCategoryInstanceFactory) {
		this.ratingCategoryDao = ratingCategoryDao;
		this.ratingCategoryInstanceFactory = ratingCategoryInstanceFactory;
	}
	
	/**
	 * Creates a new rating category.
	 * 
	 * @param description description
	 * @param ratingFactor rating factor
	 * @param valid valid
	 * @return rating category
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	public RatingCategory create(final String description, 
			final BigDecimal ratingFactor, 
			final RatingCategorySignificance significance, final Boolean valid) 
					throws DuplicateEntityFoundException {
		if (this.ratingCategoryDao.find(description, valid) != null) {
			throw new DuplicateEntityFoundException(
					"Rating category already exists.");
		}
		RatingCategory ratingCategory = this.ratingCategoryInstanceFactory
				.createInstance();
		populateRatingCategory(ratingCategory, description, ratingFactor, 
				significance, valid);
		return this.ratingCategoryDao.makePersistent(ratingCategory);
	}
	
	/**
	 * Updates an existing rating category.
	 * 
	 * @param description description
	 * @param ratingFactor rating factor
	 * @param valid valid
	 * @return rating category
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	public RatingCategory update(final RatingCategory ratingCategory, 
			final String description, final BigDecimal ratingFactor, 
			final RatingCategorySignificance significance, final Boolean valid) 
					throws DuplicateEntityFoundException {
		if (this.ratingCategoryDao.findExcluding(description, valid, 
				ratingCategory) != null) {
			throw new DuplicateEntityFoundException(
					"Rating category already exists.");
		}
		populateRatingCategory(ratingCategory, description, ratingFactor, 
				significance, valid);
		return this.ratingCategoryDao.makePersistent(ratingCategory);
	}

	/**
	 * Removes the specified rating category.
	 * 
	 * @param ratingCategory rating category
	 */
	public void remove(final RatingCategory ratingCategory) {
		this.ratingCategoryDao.makeTransient(ratingCategory);
	}

	/**
	 * Returns a list of rating categories for the specified questionnaire type.
	 * 
	 * @param questionnaireType questionnaire type
	 * @return list of rating categories
	 */
	public List<RatingCategory> findByQuestionnaireType(
			final QuestionnaireType questionnaireType) {
		return this.ratingCategoryDao.findByQuestionnaireType(questionnaireType);
	}
	
	// Populates a rating category
	private void populateRatingCategory(final RatingCategory ratingCategory, 
			final String description, final BigDecimal ratingFactor, 
			final RatingCategorySignificance significance, 
			final Boolean valid) {
		ratingCategory.setDescription(description);
		ratingCategory.setRatingFactor(ratingFactor);
		ratingCategory.setSignificance(significance);
		ratingCategory.setValid(valid);
	}
}