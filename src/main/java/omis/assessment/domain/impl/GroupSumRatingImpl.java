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
package omis.assessment.domain.impl;

import java.math.BigDecimal;

import omis.assessment.domain.GroupSumRating;
import omis.assessment.domain.RatingScaleGroup;
import omis.datatype.DateRange;

/**
 * 
 * 
 * @author Josh Divine
 * @version 0.1.0 (Apr 3, 2018)
 * @since OMIS 3.0
 */
public class GroupSumRatingImpl implements GroupSumRating {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private BigDecimal min;
	
	private BigDecimal max;
	
	private Boolean valid;
	
	private DateRange dateRange;
	
	private BigDecimal value;
	
	private RatingScaleGroup group;
	
	/**
	 * Instantiates an implementation of group sum rating.
	 */
	public GroupSumRatingImpl() {
		// Default instantiation
	}
	
	/** {@inheritDoc} */
	@Override
	public void setId(final Long id) {
		this.id = id;
	}

	/** {@inheritDoc} */
	@Override
	public Long getId() {
		return id;
	}

	/** {@inheritDoc} */
	@Override
	public void setMin(final BigDecimal min) {
		this.min = min;
	}

	/** {@inheritDoc} */
	@Override
	public BigDecimal getMin() {
		return min;
	}

	/** {@inheritDoc} */
	@Override
	public void setMax(final BigDecimal max) {
		this.max = max;
	}

	/** {@inheritDoc} */
	@Override
	public BigDecimal getMax() {
		return max;
	}

	/** {@inheritDoc} */
	@Override
	public void setValid(final Boolean valid) {
		this.valid = valid;
	}

	/** {@inheritDoc} */
	@Override
	public Boolean getValid() {
		return valid;
	}

	/** {@inheritDoc} */
	@Override
	public void setDateRange(final DateRange dateRange) {
		this.dateRange = dateRange;
	}

	/** {@inheritDoc} */
	@Override
	public DateRange getDateRange() {
		return dateRange;
	}

	/** {@inheritDoc} */
	@Override
	public void setValue(final BigDecimal value) {
		this.value = value;
	}

	/** {@inheritDoc} */
	@Override
	public BigDecimal getValue() {
		return value;
	}

	/** {@inheritDoc} */
	@Override
	public void setGroup(final RatingScaleGroup group) {
		this.group = group;
	}

	/** {@inheritDoc} */
	@Override
	public RatingScaleGroup getGroup() {
		return group;
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof GroupSumRating)) {
			return false;
		}
		GroupSumRating that = (GroupSumRating) obj;
		if (this.getMin() == null ) {
			throw new IllegalStateException("Minimum required.");
		}
		if (!this.getMin().equals(that.getMin())) {
			return false;
		}
		if (this.getMax() == null ) {
			throw new IllegalStateException("Maximum required.");
		}
		if (!this.getMax().equals(that.getMax())) {
			return false;
		}
		if (this.getValue() == null ) {
			throw new IllegalStateException("Value required.");
		}
		if (!this.getValue().equals(that.getValue())) {
			return false;
		}
		if (this.getGroup() == null ) {
			throw new IllegalStateException("Rating scale group required.");
		}
		if (!this.getGroup().equals(that.getGroup())) {
			return false;
		}
		return true;
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		if (this.getMin() == null ) {
			throw new IllegalStateException("Minimum required.");
		}
		if (this.getMax() == null ) {
			throw new IllegalStateException("Maximum required.");
		}
		if (this.getValue() == null ) {
			throw new IllegalStateException("Value required.");
		}
		if (this.getGroup() == null ) {
			throw new IllegalStateException("Rating scale group required.");
		}
		int hashCode = 14;
		hashCode = 29 * hashCode + this.getMin().hashCode();
		hashCode = 29 * hashCode + this.getMax().hashCode();
		hashCode = 29 * hashCode + this.getValue().hashCode();
		hashCode = 29 * hashCode + this.getGroup().hashCode();
		return hashCode;
	}
}