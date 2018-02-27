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

import omis.assessment.dao.AnswerRatingDao;
import omis.assessment.domain.AnswerRating;
import omis.assessment.domain.RatingCategory;
import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;
import omis.questionnaire.domain.AnswerValue;

/**
 * Answer rating delegate.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 26, 2018)
 * @since OMIS 3.0
 */
public class AnswerRatingDelegate {

	/* Data access objects. */
	
	private final AnswerRatingDao answerRatingDao;

	/* Instance factories. */
	
	private final InstanceFactory<AnswerRating> answerRatingInstanceFactory;
	
	/**
	 * Instantiates an implementation of answer rating delegate with the 
	 * specified date access object and instance factory.
	 * 
	 * @param answerRatingDao answer rating data access object
	 * @param answerRatingInstanceFactory answer rating instance factory
	 */
	public AnswerRatingDelegate(final AnswerRatingDao answerRatingDao,
			final InstanceFactory<AnswerRating> answerRatingInstanceFactory) {
		this.answerRatingDao = answerRatingDao;
		this.answerRatingInstanceFactory = answerRatingInstanceFactory;
	}
	
	/**
	 * Creates a new answer value.
	 * 
	 * @param answerValue answer value
	 * @param ratingCategory rating category
	 * @param value value
	 * @return answer value
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	public AnswerRating create(final AnswerValue answerValue, 
			final RatingCategory ratingCategory, final BigDecimal value) 
					throws DuplicateEntityFoundException {
		if (this.answerRatingDao.find(answerValue, ratingCategory) != null) {
			throw new DuplicateEntityFoundException(
					"Answer rating already exists.");
		}
		AnswerRating answerRating = this.answerRatingInstanceFactory
				.createInstance();
		populateAnswerRating(answerRating, answerValue, ratingCategory, value);
		return this.answerRatingDao.makePersistent(answerRating);
	}
	
	/**
	 * Updates an existing answer value.
	 * 
	 * @param answerRating answer rating
	 * @param answerValue answer value
	 * @param ratingCategory rating category
	 * @param value value
	 * @return answer value
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	public AnswerRating update(final AnswerRating answerRating, 
			final AnswerValue answerValue, final RatingCategory ratingCategory, 
			final BigDecimal value) 
					throws DuplicateEntityFoundException {
		if (this.answerRatingDao.findExcluding(answerValue, ratingCategory, 
				answerRating) != null) {
			throw new DuplicateEntityFoundException(
					"Answer rating already exists.");
		}
		populateAnswerRating(answerRating, answerValue, ratingCategory, value);
		return this.answerRatingDao.makePersistent(answerRating);
	}

	/**
	 * Removes the specified answer rating.
	 * 
	 * @param answerRating answer rating
	 */
	public void remove(final AnswerRating answerRating) {
		this.answerRatingDao.makeTransient(answerRating);
	}
	
	// Populates an answer rating
	private void populateAnswerRating(final AnswerRating answerRating, 
			final AnswerValue answerValue, final RatingCategory ratingCategory, 
			final BigDecimal value) {
		answerRating.setAnswerValue(answerValue);
		answerRating.setRatingCategory(ratingCategory);
		answerRating.setValue(value);
	}
}
