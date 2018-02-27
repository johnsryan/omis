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

import omis.audit.domain.Creatable;
import omis.audit.domain.Updatable;

/**
 * Rating rank.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 23, 2018)
 * @since OMIS 3.0
 */
public interface RatingRank extends Creatable, Updatable {

	/**
	 * Sets the ID of the rating rank.
	 *
	 * @param id ID
	 */
	void setId(Long id);
	
	/**
	 * Return the ID of the rating rank.
	 *
	 * @return ID
	 */
	Long getId();
	
	/**
	 * Sets the name.
	 * 
	 * @param name name
	 */
	void setName(String name);
	
	/**
	 * Returns the name.
	 * 
	 * @return name
	 */
	String getName();
	
	/**
	 * Sets whether the rating rank is valid.
	 * 
	 * @param valid whether the rating rank is valid
	 */
	void setValid(Boolean valid);
	
	/**
	 * Returns whether the rating rank is valid.
	 * 
	 * @return whether the rating rank is valid
	 */
	Boolean getValid();
	
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