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

import omis.assessment.domain.CategoryOverrideReason;
import omis.assessment.domain.RatingCategory;
import omis.dao.GenericDao;

/**
 * Data access object for category override reason.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 26, 2018)
 * @since OMIS 3.0
 */
public interface CategoryOverrideReasonDao
		extends GenericDao<CategoryOverrideReason>{

	/**
	 * Returns the category override reason for the specified parameters.
	 * 
	 * @param name name
	 * @param ratingCategory rating category
	 * @return category override reason
	 */
	CategoryOverrideReason find(String name, RatingCategory ratingCategory);
	
	/**
	 * Returns the category override reason for the specified parameters 
	 * excluding the specified category override reason.
	 * 
	 * @param name name
	 * @param ratingCategory rating category
	 * @param excludedCategoryOverrideReason excluded category override reason
	 * @return category override reason
	 */
	CategoryOverrideReason findExcluding(String name, RatingCategory ratingCategory,
			CategoryOverrideReason excludedCategoryOverrideReason);
}