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
package omis.assessment.service;

import java.math.BigDecimal;
import java.util.List;

import omis.assessment.domain.AnswerRating;
import omis.assessment.domain.GroupSumRating;
import omis.assessment.domain.RatingCategory;
import omis.assessment.domain.RatingScaleGroup;
import omis.questionnaire.domain.AdministeredQuestionnaire;

/**
 * Scaled category sum rating service delegate.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Apr 5, 2018)
 * @since OMIS 3.0
 */
public interface ScaledCategorySumRatingServiceDelegate 
		extends AssessmentRatingServiceDelegate {

	/**
	 * Returns a list of rating scale groups for the specified administered 
	 * questionnaire and rating category.
	 * 
	 * @param ratingCategory rating category
	 * @param administeredQuestionnaire administered questionnaire
	 * @return list of rating scale groups
	 */
	List<RatingScaleGroup> 
			findRatingScaleGroupsByAdministeredQuestionnaireAndRatingCategory(
					RatingCategory ratingCategory, 
					AdministeredQuestionnaire administeredQuestionnaire);
	
	/**
	 * Returns a list of answer ratings for the specified scaled group and 
	 * administered questionnaire.
	 * 
	 * @param ratingScaleGroup rating scale group
	 * @param administeredQuestionnaire administered questionnaire
	 * @return list of answer ratings
	 */
	List<AnswerRating> 
			findAnswerRatingsByRatingScaleGroupAndAdministeredQuestionnaire(
					RatingScaleGroup ratingScaleGroup, 
					AdministeredQuestionnaire administeredQuestionnaire);
	
	/**
	 * Returns the group sum rating for the specified rating scale group and 
	 * summed value.
	 * 
	 * @param ratingScaleGroup rating scale group
	 * @param value summed value
	 * @return group sum rating
	 */
	GroupSumRating findGroupSumRatingByRatingScaleGroupAndValue(
			RatingScaleGroup ratingScaleGroup, BigDecimal value);
	
	/**
	 * Returns a list of answer ratings for the specified rating category and 
	 * administered questionnaire excluding already scaled groups.
	 * 
	 * @param ratingCategory rating category
	 * @param administeredQuestionnaire administered questionnaire
	 * @return list of answer ratings
	 */
	List<AnswerRating> 
			findAnswerRatingsByRatingCategoryAndAdministeredQuestionnaireExcludingScaledGroups(
					RatingCategory ratingCategory, 
					AdministeredQuestionnaire administeredQuestionnaire);
}