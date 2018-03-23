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

import omis.assessment.domain.RatingCategory;
import omis.assessment.domain.SumRatedCategory;
import omis.dao.GenericDao;

/**
 * Data access object for sum rated category.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Mar 12, 2018)
 * @since OMIS 3.0
 */
public interface SumRatedCategoryDao extends GenericDao<SumRatedCategory> {

	/**
	 * Finds the sum rated category for the specified rating category and valid 
	 * status.
	 * 
	 * @param ratingCategory rating category
	 * @param valid valid
	 * @return sum rated category
	 */
	SumRatedCategory find(RatingCategory ratingCategory, Boolean valid);

	/**
	 * Finds the sum rated category for the specified rating category and valid 
	 * status excluding the specified sum rated category.
	 * 
	 * @param ratingCategory rating category
	 * @param valid valid
	 * @param excludedSumRatedCategory excluded sum rated category
	 * @return sum rated category
	 */
	SumRatedCategory findExcluding(RatingCategory ratingCategory, Boolean valid, 
			SumRatedCategory excludedSumRatedCategory);

	/**
	 * Returns the sum rated category for the specified rating category.
	 * 
	 * @param ratingCategory rating category
	 * @return sum rated category
	 */
	SumRatedCategory findByRatingCategory(RatingCategory ratingCategory);
}