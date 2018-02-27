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

import omis.assessment.domain.CategoryOverrideReason;
import omis.assessment.domain.RatingCategory;

/**
 * Implementation of category override reason.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 26, 2018)
 * @since OMIS 3.0
 */
public class CategoryOverrideReasonImpl implements CategoryOverrideReason {

	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String name;
	
	private RatingCategory ratingCategory;
	
	private Boolean valid;
	
	/**
	 * Instantiates an implementation of category override reason.
	 */
	public CategoryOverrideReasonImpl() {
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
	public void setName(final String name) {
		this.name = name;
	}

	/** {@inheritDoc} */
	@Override
	public String getName() {
		return name;
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
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof CategoryOverrideReason)) {
			return false;
		}
		CategoryOverrideReason that = (CategoryOverrideReason) obj;
		if (this.getName() == null) {
			throw new IllegalStateException("Name required.");
		}
		if (!this.getName().equals(that.getName())) {
			return false;
		}
		if (this.getRatingCategory() == null) {
			throw new IllegalStateException("Rating category required.");
		}
		if (!this.getRatingCategory().equals(that.getRatingCategory())) {
			return false;
		}
		return true;
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		if (this.getName() == null) {
			throw new IllegalStateException("Name required.");
		}
		if (this.getRatingCategory() == null) {
			throw new IllegalStateException("Rating category required.");
		}
		int hashCode = 14;
		hashCode = 29 * hashCode + this.getName().hashCode();
		hashCode = 29 * hashCode + this.getRatingCategory().hashCode();
		return hashCode;
	}
}