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
import java.math.BigDecimal;

import omis.questionnaire.domain.AnswerValue;

/**
 * Answer rating.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 23, 2018)
 * @since OMIS 3.0
 */
public interface AnswerRating extends Serializable {

	/**
	 * Sets the ID of the answer rating.
	 *
	 * @param id ID
	 */
	void setId(Long id);
	
	/**
	 * Return the ID of the answer rating.
	 *
	 * @return ID
	 */
	Long getId();
	
	/**
	 * Sets the answer value.
	 * 
	 * @param answerValue answer value
	 */
	void setAnswerValue(AnswerValue answerValue);
	
	/**
	 * Returns the answer value.
	 * 
	 * @return answer value
	 */
	AnswerValue getAnswerValue();
	
	/**
	 * Sets the value.
	 * 
	 * @param value value
	 */
	void setValue(BigDecimal value);
	
	/**
	 * Returns the value.
	 * 
	 * @return value
	 */
	BigDecimal getValue();
	
	/**
	 * Sets the rating category.
	 * 
	 * @param ratingCategory rating category
	 */
	void setRatingCategory(RatingCategory ratingCategory);
	
	/**
	 * Returns the rating category.
	 * 
	 * @return rating category
	 */
	RatingCategory getRatingCategory();
}