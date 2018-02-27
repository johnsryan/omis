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
import omis.assessment.domain.RatingRank;
import omis.dao.GenericDao;

/**
 * Data access object for rating rank.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 26, 2018)
 * @since OMIS 3.0
 */
public interface RatingRankDao extends GenericDao<RatingRank> {

	/**
	 * Returns the rating rank for the specified parameters.
	 * 
	 * @param name name
	 * @param ratingCategory ranking category
	 * @return ranking rate
	 */
	RatingRank find(String name, RatingCategory ratingCategory);
	
	/**
	 * Returns the rating rank for the specified parameters excluding the 
	 * specified rating rank.
	 * 
	 * @param name name
	 * @param ratingCategory ranking category
	 * @param excludedRatingRank excluded rating rank
	 * @return ranking rate
	 */
	RatingRank findExcluding(String name, RatingCategory ratingCategory, 
			RatingRank excludedRatingRank);
}