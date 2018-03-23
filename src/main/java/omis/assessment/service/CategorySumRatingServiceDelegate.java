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

import java.util.List;

import omis.assessment.domain.AnswerRating;
import omis.assessment.domain.RatingCategory;
import omis.assessment.domain.SumRatedCategory;
import omis.questionnaire.domain.AdministeredQuestionnaire;

/**
 * Category sum rating service delegate.
 * 
 * @author Josh Divine
 * @version 0.1.1 (Mar 12, 2018)
 * @since OMIS 3.0
 */
public interface CategorySumRatingServiceDelegate 
		extends AssessmentRatingServiceDelegate {

	/**
	 * Returns a list of answer ratings for the specified rating category and 
	 * administered questionnaire.
	 * 
	 * @param ratingCategory rating category
	 * @param administeredQuestionnaire administered questionnaire
	 * @return list of answer ratings
	 */
	List<AnswerRating> 
			findAnswerRatingsByRatingCategoryAndAdministeredQuestionnaire(
					RatingCategory ratingCategory, 
					AdministeredQuestionnaire administeredQuestionnaire);
	
	/**
	 * Returns the sum rated category for the specified rating category.
	 * 
	 * @param ratingCategory rating category
	 * @return list of sum rated categories
	 */
	SumRatedCategory findSumRatedCategoryByRatingCategory(
			RatingCategory ratingCategory);
}