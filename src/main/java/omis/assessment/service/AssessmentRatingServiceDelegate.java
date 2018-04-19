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

import omis.assessment.domain.RatingCategory;
import omis.questionnaire.domain.AdministeredQuestionnaire;

/**
 * Assessment rating service delegate.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 26, 2018)
 * @since OMIS 3.0
 */
public interface AssessmentRatingServiceDelegate {

	/**
	 * Checks if the rating category is assessable.
	 * 
	 * @param administeredQuestionnaire administered questionnaire
	 * @param ratingCategory rating category
	 * @return if rating category is assessable
	 */
	Boolean isAssessable(AdministeredQuestionnaire administeredQuestionnaire, 
			RatingCategory ratingCategory);
	
	/**
	 * Assesses a score.
	 * 
	 * @param administeredQuestionnaire administered questionnaire
	 * @param ratingCategory rating category
	 * @return score
	 */
	BigDecimal assess(AdministeredQuestionnaire administeredQuestionnaire, 
			RatingCategory ratingCategory);
}