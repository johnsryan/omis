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

/**
 * Rating category.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 23, 2018)
 * @since OMIS 3.0
 */
public interface RatingCategory extends Serializable {
	
	/**
	 * Sets the ID of the rating category.
	 *
	 * @param id ID
	 */
	void setId(Long id);
	
	/**
	 * Return the ID of the rating category.
	 *
	 * @return ID
	 */
	Long getId();
	
	/**
	 * Sets the description.
	 * 
	 * @param description description
	 */
	void setDescription(String description);
	
	/**
	 * Returns the description.
	 * 
	 * @return description
	 */
	String getDescription();
	
	/**
	 * Sets the rating factor.
	 * 
	 * @param ratingFactor rating factor
	 */
	void setRatingFactor(BigDecimal ratingFactor);
	
	
	/**
	 * Returns the rating factor.
	 * 
	 * @return rating factor
	 */
	BigDecimal getRatingFactor();
	
	/**
	 * Sets whether the rating category is valid.
	 * 
	 * @param valid whether the rating category is valid
	 */
	void setValid(Boolean valid);
	
	/**
	 * Returns whether the rating category is valid.
	 * 
	 * @return whether the rating category is valid
	 */
	Boolean getValid();
	
	/**
	 * Sets the rating category significance.
	 * 
	 * @param significance rating category significance
	 */
	void setSignificance(RatingCategorySignificance significance);
	
	/**
	 * Returns the rating category significance.
	 * 
	 * @return rating category significance
	 */
	RatingCategorySignificance getSignificance();
}