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

import java.util.List;

import omis.assessment.domain.RatingCategory;
import omis.dao.GenericDao;
import omis.questionnaire.domain.QuestionnaireType;

/**
 * Data access object for rating category.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 26, 2018)
 * @since OMIS 3.0
 */
public interface RatingCategoryDao extends GenericDao<RatingCategory> {

	/**
	 * Returns the rating category for the specified parameters.
	 * 
	 * @param description description
	 * @param valid valid
	 * @return rating category
	 */
	RatingCategory find(String description, Boolean valid);
	
	/**
	 * Returns the rating category for the specified parameters excluding the 
	 * specified rating category.
	 * 
	 * @param description description
	 * @param valid valid
	 * @param excludedRatingCategory excluded rating category
	 * @return rating category
	 */
	RatingCategory findExcluding(String description, Boolean valid, 
			RatingCategory excludedRatingCategory);

	/**
	 * Returns a list of rating categories for the specified questionnaire type.
	 * 
	 * @param questionnaireType questionnaire type
	 * @return list of rating categories
	 */
	List<RatingCategory> findByQuestionnaireType(
			QuestionnaireType questionnaireType);
}