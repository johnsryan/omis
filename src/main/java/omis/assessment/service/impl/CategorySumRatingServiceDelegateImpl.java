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
package omis.assessment.service.impl;

import java.math.BigDecimal;
import java.util.List;

import omis.assessment.domain.AnswerRating;
import omis.assessment.domain.RatingCategory;
import omis.assessment.domain.SumRatedCategory;
import omis.assessment.service.AssessmentRatingServiceDelegateRegistry;
import omis.assessment.service.CategorySumRatingServiceDelegate;
import omis.assessment.service.delegate.AnswerRatingDelegate;
import omis.assessment.service.delegate.SumRatedCategoryDelegate;
import omis.questionnaire.domain.AdministeredQuestionnaire;

/**
 * Implementation of category sum rating service delegate.
 * 
 * @author Josh Divine
 * @version 0.1.2 (Apr 9, 2018)
 * @since OMIS 3.0
 */
public class CategorySumRatingServiceDelegateImpl 
		extends AbstractAssessmentRatingServiceDelegate
		implements CategorySumRatingServiceDelegate {

	/* Delegates. */
	
	private final AnswerRatingDelegate answerRatingDelegate;
	
	private final SumRatedCategoryDelegate sumRatedCategoryDelegate;
	
	/**
	 * Instantiates a category sum rating service delegate implementation with 
	 * the specified delegates.
	 * 
	 * @param answerRatingDelegate answer rating delegate
	 * @param sumRatedCategoryDelegate sum rated category delegate
	 * @param assessmentRatingServiceDelegateRegistry assessment rating service 
	 * delegate registry
	 */
	public CategorySumRatingServiceDelegateImpl(
			final AnswerRatingDelegate answerRatingDelegate,
			final SumRatedCategoryDelegate sumRatedCategoryDelegate,
			final AssessmentRatingServiceDelegateRegistry 
					assessmentRatingServiceDelegateRegistry) {
		super(assessmentRatingServiceDelegateRegistry);
		this.answerRatingDelegate = answerRatingDelegate;
		this.sumRatedCategoryDelegate = sumRatedCategoryDelegate;
	}
	
	/** {@inheritDoc} */
	@Override
	public Boolean isAssessable(
			final AdministeredQuestionnaire administeredQuestionnaire, 
			final RatingCategory ratingCategory) {
		return this.findSumRatedCategoryByRatingCategory(ratingCategory) != null;
	}

	/** {@inheritDoc} */
	@Override
	public BigDecimal assess(
			final AdministeredQuestionnaire administeredQuestionnaire, 
			final RatingCategory ratingCategory) {
		List<AnswerRating> answerRatings = this
				.findAnswerRatingsByRatingCategoryAndAdministeredQuestionnaire(
						ratingCategory, administeredQuestionnaire);
		BigDecimal total = new BigDecimal(0);
		for (AnswerRating answerRating : answerRatings) {
			total = total.add(answerRating.getValue().multiply(
					answerRating.getRatingCategory().getRatingFactor()));
		}
		return total;
	}

	/** {@inheritDoc} */
	@Override
	public List<AnswerRating> 
			findAnswerRatingsByRatingCategoryAndAdministeredQuestionnaire(
					final RatingCategory ratingCategory, 
					final AdministeredQuestionnaire administeredQuestionnaire) {
		return this.answerRatingDelegate
				.findByRatingCategoryAndAdministeredQuestionnaire(
						ratingCategory, administeredQuestionnaire);
	}

	/** {@inheritDoc} */
	@Override
	public SumRatedCategory findSumRatedCategoryByRatingCategory(
			final RatingCategory ratingCategory) {
		return this.sumRatedCategoryDelegate.findByRatingCategory(
				ratingCategory);
	}
}