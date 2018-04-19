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
package omis.assessment.service.delegate;

import java.math.BigDecimal;

import omis.assessment.dao.GroupSumRatingDao;
import omis.assessment.domain.GroupSumRating;
import omis.assessment.domain.RatingScaleGroup;
import omis.datatype.DateRange;
import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;

/**
 * Group sum rating delegate.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Apr 4, 2018)
 * @since OMIS 3.0
 */
public class GroupSumRatingDelegate {

	/* Data access objects. */
	
	private final GroupSumRatingDao groupSumRatingDao;

	/* Instance factories. */
	
	private final InstanceFactory<GroupSumRating> groupSumRatingInstanceFactory;
	
	/**
	 * Instantiates an implementation of group sum rating delegate with the 
	 * specified date access object and instance factory.
	 * 
	 * @param groupSumRatingDao group sum rating data access object
	 * @param groupSumRatingInstanceFactory group sum rating instance factory
	 */
	public GroupSumRatingDelegate(final GroupSumRatingDao groupSumRatingDao,
			final InstanceFactory<GroupSumRating> 
					groupSumRatingInstanceFactory) {
		this.groupSumRatingDao = groupSumRatingDao;
		this.groupSumRatingInstanceFactory = groupSumRatingInstanceFactory;
	}
	
	/**
	 * Creates a new group sum rating.
	 * 
	 * @param min minimum
	 * @param max maximum
	 * @param valid valid
	 * @param dateRange date range
	 * @param value value
	 * @param group group
	 * @return group sum rating
	 * @throws DuplicateEntityFoundException if entity already exists
	 */
	public GroupSumRating create(final BigDecimal min, final BigDecimal max, 
			final Boolean valid, final DateRange dateRange, 
			final BigDecimal value, final RatingScaleGroup group) 
					throws DuplicateEntityFoundException {
		if (this.groupSumRatingDao.find(min, max, value, group) != null) {
			throw new DuplicateEntityFoundException(
					"Group sum rating already exists.");
		}
		GroupSumRating groupSumRating = this.groupSumRatingInstanceFactory
				.createInstance();
		populateGroupSumRating(groupSumRating, min, max, valid, dateRange, 
				value, group);
		return this.groupSumRatingDao.makePersistent(groupSumRating);
	}
	
	/**
	 * Updates an existing group sum rating.
	 * 
	 * @param min minimum
	 * @param max maximum
	 * @param valid valid
	 * @param dateRange date range
	 * @param value value
	 * @param group group
	 * @return group sum rating
	 * @throws DuplicateEntityFoundException if entity already exists
	 */
	public GroupSumRating update(final GroupSumRating groupSumRating, 
			final BigDecimal min, final BigDecimal max, final Boolean valid, 
			final DateRange dateRange, final BigDecimal value, 
			final RatingScaleGroup group) throws DuplicateEntityFoundException {
		if (this.groupSumRatingDao.findExcluding(min, max, value, group,
				groupSumRating) != null) {
			throw new DuplicateEntityFoundException(
					"Group sum rating already exists.");
		}
		populateGroupSumRating(groupSumRating, min, max, valid, dateRange, 
				value, group);
		return this.groupSumRatingDao.makePersistent(groupSumRating);
	}

	/**
	 * Removes the specified group sum rating.
	 * 
	 * @param groupSumRating group sum rating
	 */
	public void remove(final GroupSumRating groupSumRating) {
		this.groupSumRatingDao.makeTransient(groupSumRating);
	}

	/**
	 * Returns the group sum rating for the specified rating scale group and 
	 * summed value.
	 * 
	 * @param ratingScaleGroup rating scale group
	 * @param value summed value
	 * @return group sum rating
	 */
	public GroupSumRating findByRatingScaleGroupAndValue(
			final RatingScaleGroup ratingScaleGroup, final BigDecimal value) {
		return this.groupSumRatingDao.findByRatingScaleGroupAndValue(
				ratingScaleGroup, value);
	}
	
	// Populates a group sum rating
	private void populateGroupSumRating(final GroupSumRating groupSumRating, 
			final BigDecimal min, final BigDecimal max, final Boolean valid, 
			final DateRange dateRange, final BigDecimal value, 
			final RatingScaleGroup group) {
		groupSumRating.setMin(min);
		groupSumRating.setMax(max);
		groupSumRating.setValid(valid);
		groupSumRating.setDateRange(dateRange);
		groupSumRating.setValue(value);
		groupSumRating.setGroup(group);
	}
}