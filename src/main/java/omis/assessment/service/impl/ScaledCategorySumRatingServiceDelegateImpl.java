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
import omis.assessment.domain.GroupSumRating;
import omis.assessment.domain.RatingCategory;
import omis.assessment.domain.RatingScaleGroup;
import omis.assessment.service.AssessmentRatingServiceDelegateRegistry;
import omis.assessment.service.ScaledCategorySumRatingServiceDelegate;
import omis.assessment.service.delegate.AnswerRatingDelegate;
import omis.assessment.service.delegate.GroupSumRatingDelegate;
import omis.assessment.service.delegate.RatingScaleGroupDelegate;
import omis.questionnaire.domain.AdministeredQuestionnaire;

/**
 * Implementation of scaled category sum rating service delegate.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Apr 5, 2018)
 * @since OMIS 3.0
 */
public class ScaledCategorySumRatingServiceDelegateImpl 
		extends AbstractAssessmentRatingServiceDelegate
		implements ScaledCategorySumRatingServiceDelegate {

	/* Delegates. */
	
	private final RatingScaleGroupDelegate ratingScaleGroupDelegate;
	
	private final AnswerRatingDelegate answerRatingDelegate;
	
	private final GroupSumRatingDelegate groupSumRatingDelegate;
	
	/**
	 * Instantiates a scaled category sum rating service delegate implementation 
	 * with the specified delegates.
	 * 
	 * @param assessmentRatingServiceDelegateRegistry assessment rating service 
	 * delegate registry
	 */
	public ScaledCategorySumRatingServiceDelegateImpl(
			final RatingScaleGroupDelegate ratingScaleGroupDelegate,
			final AnswerRatingDelegate answerRatingDelegate,
			final GroupSumRatingDelegate groupSumRatingDelegate,
			final AssessmentRatingServiceDelegateRegistry 
					assessmentRatingServiceDelegateRegistry) {
		super(assessmentRatingServiceDelegateRegistry);
		this.ratingScaleGroupDelegate = ratingScaleGroupDelegate;
		this.answerRatingDelegate = answerRatingDelegate;
		this.groupSumRatingDelegate = groupSumRatingDelegate;
	}

	/** {@inheritDoc} */
	@Override
	public Boolean isAssessable(
			final AdministeredQuestionnaire administeredQuestionnaire,
			final RatingCategory ratingCategory) {
		return (!this
				.findRatingScaleGroupsByAdministeredQuestionnaireAndRatingCategory(
						ratingCategory, administeredQuestionnaire).isEmpty());
	}

	/** {@inheritDoc} */
	@Override
	public BigDecimal assess(
			final AdministeredQuestionnaire administeredQuestionnaire, 
			final RatingCategory ratingCategory) {
		BigDecimal total = new BigDecimal(0);
		List<RatingScaleGroup> ratingScaleGroups = this
				.findRatingScaleGroupsByAdministeredQuestionnaireAndRatingCategory(
						ratingCategory, administeredQuestionnaire);
		for (RatingScaleGroup ratingScaleGroup : ratingScaleGroups) {
			List<AnswerRating> answerRatings = this
					.findAnswerRatingsByRatingScaleGroupAndAdministeredQuestionnaire(
							ratingScaleGroup, administeredQuestionnaire);
			for (AnswerRating answerRating : answerRatings) {
				BigDecimal value = answerRating.getValue().multiply(
						answerRating.getRatingCategory().getRatingFactor());
				GroupSumRating groupSumRating = this
						.findGroupSumRatingByRatingScaleGroupAndValue(
								ratingScaleGroup, value);
				total = total.add(groupSumRating.getValue());
			}
		}
		List<AnswerRating> answerRatings = this
				.findAnswerRatingsByRatingCategoryAndAdministeredQuestionnaireExcludingScaledGroups(
						ratingCategory, administeredQuestionnaire);
		for (AnswerRating answerRating : answerRatings) {
			total = total.add(answerRating.getValue().multiply(
					answerRating.getRatingCategory().getRatingFactor()));
		}
		return total;
	}

	/** {@inheritDoc} */
	@Override
	public List<RatingScaleGroup> 
			findRatingScaleGroupsByAdministeredQuestionnaireAndRatingCategory(
					final RatingCategory ratingCategory, 
					final AdministeredQuestionnaire administeredQuestionnaire) {
		return this.ratingScaleGroupDelegate
				.findByAdministeredQuestionnaireAndRatingCategory(
						administeredQuestionnaire, ratingCategory);
	}

	/** {@inheritDoc} */
	@Override
	public List<AnswerRating> 
			findAnswerRatingsByRatingScaleGroupAndAdministeredQuestionnaire(
					final RatingScaleGroup ratingScaleGroup, 
					final AdministeredQuestionnaire administeredQuestionnaire) {
		return this.answerRatingDelegate
				.findByRatingScaleGroupAndAdministeredQuestionnaire(
						ratingScaleGroup, administeredQuestionnaire);
	}

	/** {@inheritDoc} */
	@Override
	public GroupSumRating findGroupSumRatingByRatingScaleGroupAndValue(
			final RatingScaleGroup ratingScaleGroup, final BigDecimal value) {
		return this.groupSumRatingDelegate.findByRatingScaleGroupAndValue(
				ratingScaleGroup, value);
	}

	/** {@inheritDoc} */
	@Override
	public List<AnswerRating> 
			findAnswerRatingsByRatingCategoryAndAdministeredQuestionnaireExcludingScaledGroups(
					final RatingCategory ratingCategory, 
					final AdministeredQuestionnaire administeredQuestionnaire) {
		return this.answerRatingDelegate
				.findByRatingCategoryAndAdministeredQuestionnaireExcludingScaledGroups(
						ratingCategory, administeredQuestionnaire);
	}
}