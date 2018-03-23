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
 * Sum rated category.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Mar 12, 2018)
 * @since OMIS 3.0
 */
public interface SumRatedCategory extends Serializable {

	/**
	 * Sets the ID of the sum rated category.
	 *
	 * @param id ID
	 */
	void setId(Long id);
	
	/**
	 * Return the ID of the sum rated category.
	 *
	 * @return ID
	 */
	Long getId();
	
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
	
	/**
	 * Sets whether valid.
	 * 
	 * @param valid valid
	 */
	void setValid(Boolean valid);
	
	/**
	 * Returns whether valid.
	 * 
	 * @return valid
	 */
	Boolean getValid();
}