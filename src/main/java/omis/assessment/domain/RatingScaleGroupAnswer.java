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
package omis.assessment.domain;

import java.io.Serializable;

/**
 * Rating scale group answer.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Apr 3, 2018)
 * @since OMIS 3.0
 */
public interface RatingScaleGroupAnswer extends Serializable {

	/**
	 * Sets the ID of the rating scale group answer.
	 *
	 * @param id ID
	 */
	void setId(Long id);
	
	/**
	 * Return the ID of the rating scale group answer.
	 *
	 * @return ID
	 */
	Long getId();
	
	/**
	 * Sets the answer rating.
	 * 
	 * @param answerRating answer rating
	 */
	void setAnswerRating(AnswerRating answerRating);
	
	/**
	 * Returns the answer rating.
	 * 
	 * @return answer rating
	 */
	AnswerRating getAnswerRating();
	
	/**
	 * Sets the rating scale group.
	 *  
	 * @param scaleGroup rating scale group
	 */
	void setScaleGroup(RatingScaleGroup scaleGroup);
	
	/**
	 * Returns the rating scale group.
	 * 
	 * @return rating scale group
	 */
	RatingScaleGroup getScaleGroup();
}