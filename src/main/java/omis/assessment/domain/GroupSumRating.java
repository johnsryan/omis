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

import omis.datatype.DateRange;

/**
 * Group sum rating.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Apr 3, 2018)
 * @since OMIS 3.0
 */
public interface GroupSumRating extends Serializable {

	/**
	 * Sets the ID of the group sum rating.
	 *
	 * @param id ID
	 */
	void setId(Long id);
	
	/**
	 * Return the ID of the group sum rating.
	 *
	 * @return ID
	 */
	Long getId();
	
	/**
	 * Sets the minimum.
	 * 
	 * @param min minimum
	 */
	void setMin(BigDecimal min);
	
	/**
	 * Returns the minimum.
	 * 
	 * @return minimum
	 */
	BigDecimal getMin();
	
	/**
	 * Sets the maximum.
	 * 
	 * @param max maximum
	 */
	void setMax(BigDecimal max);
	
	/**
	 * Returns the maximum.
	 * 
	 * @return maximum
	 */
	BigDecimal getMax();
	
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
	
	/**
	 * Sets the date range.
	 * @param dateRange date range
	 */
	void setDateRange(DateRange dateRange);
	
	/**
	 * Returns the date range.
	 * 
	 * @return date range
	 */
	DateRange getDateRange();
	
	/**
	 * Sets the value.
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
	 * Sets the rating scale group.
	 * 
	 * @param group rating scale group
	 */
	void setGroup(RatingScaleGroup group);
	
	/**
	 * Returns the rating scale group.
	 * 
	 * @return rating scale group
	 */
	RatingScaleGroup getGroup();
	
}