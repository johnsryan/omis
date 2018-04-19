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

import java.math.BigDecimal;

import omis.assessment.domain.GroupSumRating;
import omis.assessment.domain.RatingScaleGroup;
import omis.dao.GenericDao;

/**
 * Data access object for group sum rating.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Apr 4, 2018)
 * @since OMIS 3.0
 */
public interface GroupSumRatingDao extends GenericDao<GroupSumRating> {

	/**
	 * Finds the group sum rating with the specified parameters.
	 * 
	 * @param min minimum
	 * @param max maximum
	 * @param value value
	 * @param group rating scale group
	 * @return group sum rating
	 */
	GroupSumRating find(BigDecimal min, BigDecimal max, BigDecimal value, 
			RatingScaleGroup group);

	/**
	 * Finds the group sum rating with the specified parameters excluding the 
	 * specified group sum rating.
	 * 
	 * @param min minimum
	 * @param max maximum
	 * @param value value
	 * @param group rating scale group
	 * @param excludedGroupSumRating excluded group sum rating
	 * @return group sum rating
	 */
	GroupSumRating findExcluding(BigDecimal min, BigDecimal max, 
			BigDecimal value, RatingScaleGroup group, 
			GroupSumRating excludedGroupSumRating);

	/**
	 * Returns the group sum rating for the specified rating scale group and 
	 * summed value.
	 * 
	 * @param ratingScaleGroup rating scale group
	 * @param value summed value
	 * @return group sum rating
	 */
	GroupSumRating findByRatingScaleGroupAndValue(
			RatingScaleGroup ratingScaleGroup, BigDecimal value);
}