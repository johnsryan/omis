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

import omis.assessment.domain.RatingCategorySignificance;
import omis.assessment.domain.RatingCategory;

/**
 * Implementation of rating category.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 26, 2018)
 * @since OMIS 3.0
 */
public class RatingCategoryImpl implements RatingCategory {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private String description;
	
	private BigDecimal ratingFactor;
	
	private Boolean valid;
	
	private RatingCategorySignificance significance;
	
	/**
	 * Instantiates an implementation of rating category.
	 */
	public RatingCategoryImpl() {
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
	public void setDescription(final String description) {
		this.description = description;
	}

	/** {@inheritDoc} */
	@Override
	public String getDescription() {
		return description;
	}

	/** {@inheritDoc} */
	@Override
	public void setRatingFactor(final BigDecimal ratingFactor) {
		this.ratingFactor = ratingFactor;
	}

	/** {@inheritDoc} */
	@Override
	public BigDecimal getRatingFactor() {
		return ratingFactor;
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
	public void setSignificance(final RatingCategorySignificance significance) {
		this.significance = significance;
	}

	/** {@inheritDoc} */
	@Override
	public RatingCategorySignificance getSignificance() {
		return this.significance;
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof RatingCategory)) {
			return false;
		}
		RatingCategory that = (RatingCategory) obj;
		if (this.getDescription() == null ) {
			throw new IllegalStateException("Description required.");
		}
		if (!this.getDescription().equals(that.getDescription())) {
			return false;
		}
		if (this.getValid() == null ) {
			throw new IllegalStateException("Valid required.");
		}
		if (!this.getValid().equals(that.getValid())) {
			return false;
		}
		return true;
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		if (this.getDescription() == null ) {
			throw new IllegalStateException("Description required.");
		}
		if (this.getValid() == null ) {
			throw new IllegalStateException("Valid required.");
		}
		int hashCode = 14;
		hashCode = 29 * hashCode + this.getDescription().hashCode();
		hashCode = 29 * hashCode + this.getValid().hashCode();
		return hashCode;
	}
}