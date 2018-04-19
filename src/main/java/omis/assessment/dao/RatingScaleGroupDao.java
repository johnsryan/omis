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
import omis.assessment.domain.RatingScaleGroup;
import omis.dao.GenericDao;
import omis.questionnaire.domain.AdministeredQuestionnaire;

/**
 * Data access object for rating scale group.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Apr 4, 2018)
 * @since OMIS 3.0
 */
public interface RatingScaleGroupDao extends GenericDao<RatingScaleGroup> {

	/**
	 * Finds the rating scale group with the specified name.
	 * 
	 * @param name name
	 * @return rating scale group
	 */
	RatingScaleGroup find(String name);

	/**
	 * Finds the rating scale group with the specified name excluding the 
	 * specified rating scale group.
	 * 
	 * @param name name
	 * @param excludedRatingScaleGroup excluded rating scale group.
	 * @return rating scale group
	 */
	RatingScaleGroup findExcluding(String name, 
			RatingScaleGroup excludedRatingScaleGroup);

	/**
	 * Returns a list of rating scale groups for the specified administered 
	 * questionnaire and rating category.
	 * 
	 * @param administeredQuestionnaire administered questionnaire
	 * @param ratingCategory rating category
	 * @return list of rating scale groups
	 */
	List<RatingScaleGroup> findByAdministeredQuestionnaireAndRatingCategory(
			AdministeredQuestionnaire administeredQuestionnaire, 
			RatingCategory ratingCategory);
}