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

import java.math.BigDecimal;
import java.util.Date;

import omis.assessment.domain.AssessmentRating;
import omis.dao.GenericDao;
import omis.demographics.domain.Sex;
import omis.questionnaire.domain.QuestionnaireType;

/**
 * Data access object for assessment rating.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 26, 2018)
 * @since OMIS 3.0
 */
public interface AssessmentRatingDao extends GenericDao<AssessmentRating> {

	/**
	 * Returns the assessment rating for the specified parameters.
	 * 
	 * @param questionnaireType questionnaire type
	 * @param sex sex
	 * @param min min
	 * @param max max
	 * @param startDate start date
	 * @param endDate end date
	 * @return assessment rating
	 */
	AssessmentRating find(QuestionnaireType questionnaireType, Sex sex, 
			BigDecimal min, BigDecimal max, Date startDate, Date endDate);
	
	/**
	 * Returns the assessment rating for the specified parameters excluding the 
	 * specified assessment rating.
	 * 
	 * @param questionnaireType questionnaire type
	 * @param sex sex
	 * @param min min
	 * @param max max
	 * @param startDate start date
	 * @param endDate end date
	 * @param excludedAssessmentRating excluded assessment rating
	 * @return assessment rating
	 */
	AssessmentRating findExcluding(QuestionnaireType questionnaireType, Sex sex, 
			BigDecimal min, BigDecimal max, Date startDate, Date endDate, 
			AssessmentRating excludedAssessmentRating);
}