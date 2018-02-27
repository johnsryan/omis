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
package omis.assessment.dao;

import omis.assessment.domain.AssessmentCategoryScore;
import omis.assessment.domain.RatingCategory;
import omis.dao.GenericDao;
import omis.questionnaire.domain.AdministeredQuestionnaire;

/**
 * Data access object for assessment category score.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 26, 2018)
 * @since OMIS 3.0
 */
public interface AssessmentCategoryScoreDao 
		extends GenericDao<AssessmentCategoryScore> {

	/**
	 * Returns the assessment category score for the specified parameters.
	 * 
	 * @param administeredQuestionnaire administered questionnaire
	 * @param ratingCategory rating category
	 * @return assessment category score
	 */
	AssessmentCategoryScore find(
			AdministeredQuestionnaire administeredQuestionnaire, 
			RatingCategory ratingCategory);
	
	/**
	 * Returns the assessment category score for the specified parameters.
	 * 
	 * @param administeredQuestionnaire administered questionnaire
	 * @param ratingCategory rating category
	 * @param excludedAssessmentCategoryScore excluded assessment category score
	 * @return assessment category score
	 */
	AssessmentCategoryScore findExcluding(
			AdministeredQuestionnaire administeredQuestionnaire, 
			RatingCategory ratingCategory,
			AssessmentCategoryScore excludedAssessmentCategoryScore);
}