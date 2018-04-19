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

import omis.assessment.domain.AnswerRating;
import omis.assessment.domain.RatingScaleGroup;
import omis.assessment.domain.RatingScaleGroupAnswer;
import omis.dao.GenericDao;

/**
 * Data access object for rating scale group answer.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Apr 4, 2018)
 * @since OMIS 3.0
 */
public interface RatingScaleGroupAnswerDao 
		extends GenericDao<RatingScaleGroupAnswer> {

	/**
	 * Finds the rating scale group answer for the specified parameters.
	 * 
	 * @param answerRating answer rating
	 * @param scaleGroup rating scale group
	 * @return rating scale group answer
	 */
	RatingScaleGroupAnswer find(AnswerRating answerRating, 
			RatingScaleGroup scaleGroup);

	/**
	 * Finds the rating scale group answer for the specified parameters 
	 * excluding the specified rating scale group answer.
	 * 
	 * @param answerRating answer rating
	 * @param scaleGroup rating scale group
	 * @param excludedRatingScaleGroupAnswer excluded rating scale group 
	 * answer
	 * @return rating scale group answer
	 */
	RatingScaleGroupAnswer findExcluding(AnswerRating answerRating, 
			RatingScaleGroup scaleGroup, 
			RatingScaleGroupAnswer excludedRatingScaleGroupAnswer);
}