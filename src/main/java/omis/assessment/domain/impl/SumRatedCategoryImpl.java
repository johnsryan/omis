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

import omis.assessment.domain.RatingCategory;
import omis.assessment.domain.SumRatedCategory;

/**
 * Implementation of sum rated category.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Mar 12, 2018)
 * @since OMIS 3.0
 */
public class SumRatedCategoryImpl implements SumRatedCategory {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private RatingCategory ratingCategory;
	
	private Boolean valid;

	/**
	 * Instantiates an implementation of sum rated category.
	 */
	public SumRatedCategoryImpl() {
		// Default constructor
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
	public void setRatingCategory(final RatingCategory ratingCategory) {
		this.ratingCategory = ratingCategory;
	}

	/** {@inheritDoc} */
	@Override
	public RatingCategory getRatingCategory() {
		return ratingCategory;
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
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof SumRatedCategory)) {
			return false;
		}
		SumRatedCategory that = (SumRatedCategory) obj;
		if (this.getRatingCategory() == null) {
			throw new IllegalStateException("Rating category required.");
		}
		if (!this.getRatingCategory().equals(that.getRatingCategory())) {
			return false;
		}
		if (this.getValid() == null) {
			throw new IllegalStateException("Valid required");
		}
		if (!this.getValid().equals(that.getValid())) {
			return false;
		}
		return true;
	}
	
	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		if (this.getRatingCategory() == null) {
			throw new IllegalStateException("Rating category required.");
		}
		if (this.getValid() == null) {
			throw new IllegalStateException("Valid required");
		}
		int hashCode = 14;
		hashCode = 29 * hashCode + this.getRatingCategory().hashCode();
		hashCode = 29 * hashCode + this.getValid().hashCode();
		
		return hashCode;
	}
}