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

import omis.assessment.dao.RatingScaleGroupAnswerDao;
import omis.assessment.domain.AnswerRating;
import omis.assessment.domain.RatingScaleGroup;
import omis.assessment.domain.RatingScaleGroupAnswer;
import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;

/**
 * Rating scale group answer delegate.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Apr 4, 2018)
 * @since OMIS 3.0
 */
public class RatingScaleGroupAnswerDelegate {

	/* Data access objects. */
	
	private final RatingScaleGroupAnswerDao ratingScaleGroupAnswerDao;

	/* Instance factories. */
	
	private final InstanceFactory<RatingScaleGroupAnswer> 
			ratingScaleGroupAnswerInstanceFactory;
	
	/**
	 * Instantiates an implementation of rating scale group answer delegate 
	 * with the specified date access object and instance factory.
	 * 
	 * @param ratingScaleGroupAnswerDao rating scale group answer data access 
	 * object
	 * @param ratingScaleGroupAnswerInstanceFactory rating scale group answer 
	 * instance factory
	 */
	public RatingScaleGroupAnswerDelegate(
			final RatingScaleGroupAnswerDao ratingScaleGroupAnswerDao,
			final InstanceFactory<RatingScaleGroupAnswer> 
					ratingScaleGroupAnswerInstanceFactory) {
		this.ratingScaleGroupAnswerDao = ratingScaleGroupAnswerDao;
		this.ratingScaleGroupAnswerInstanceFactory = 
				ratingScaleGroupAnswerInstanceFactory;
	}
	
	/**
	 * Creates a new rating scale group answer.
	 * 
	 * @param answerRating answer rating
	 * @param scaleGroup rating scale group
	 * @return rating scale group answer
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	public RatingScaleGroupAnswer create(final AnswerRating answerRating, 
			final RatingScaleGroup scaleGroup) 
					throws DuplicateEntityFoundException {
		if (this.ratingScaleGroupAnswerDao.find(answerRating, scaleGroup) 
				!= null) {
			throw new DuplicateEntityFoundException(
					"Rating scale group answer already exists.");
		}
		RatingScaleGroupAnswer ratingScaleGroupAnswers = this
				.ratingScaleGroupAnswerInstanceFactory.createInstance();
		populateRatingScaleGroupAnswers(ratingScaleGroupAnswers, answerRating, 
				scaleGroup);
		return this.ratingScaleGroupAnswerDao.makePersistent(
				ratingScaleGroupAnswers);
	}
	
	/**
	 * Updates an existing rating scale group answer.
	 * 
	 * @param answerRating answer rating
	 * @param scaleGroup rating scale group
	 * @return rating scale group answer
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	public RatingScaleGroupAnswer update(
			final RatingScaleGroupAnswer ratingScaleGroupAnswers, 
			final AnswerRating answerRating, final RatingScaleGroup scaleGroup) 
					throws DuplicateEntityFoundException {
		if (this.ratingScaleGroupAnswerDao.findExcluding(answerRating, 
				scaleGroup, ratingScaleGroupAnswers) != null) {
			throw new DuplicateEntityFoundException(
					"Rating scale group answer already exists.");
		}
		populateRatingScaleGroupAnswers(ratingScaleGroupAnswers, answerRating, 
				scaleGroup);
		return this.ratingScaleGroupAnswerDao.makePersistent(
				ratingScaleGroupAnswers);
	}
	
	/**
	 * Removes the specified rating scale group answer.
	 * 
	 * @param ratingScaleGroupAnswer rating scale group answer
	 */
	public void remove(final RatingScaleGroupAnswer ratingScaleGroupAnswer) {
		this.ratingScaleGroupAnswerDao.makeTransient(ratingScaleGroupAnswer);
	}

	// Populates a rating scale group answer
	private void populateRatingScaleGroupAnswers(
			final RatingScaleGroupAnswer ratingScaleGroupAnswers,
			final AnswerRating answerRating, 
			final RatingScaleGroup scaleGroup) {
		ratingScaleGroupAnswers.setAnswerRating(answerRating);
		ratingScaleGroupAnswers.setScaleGroup(scaleGroup);
	}
}