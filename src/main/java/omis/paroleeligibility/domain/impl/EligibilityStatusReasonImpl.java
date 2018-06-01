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
package omis.paroleeligibility.domain.impl;

import omis.paroleeligibility.domain.EligibilityStatusCategory;
import omis.paroleeligibility.domain.EligibilityStatusReason;

/**
 * Eligibility status reason implementation.
 *
 * @author Trevor Isles
 * @author Annie Wahl
 * @version 0.1.1 (May 23, 2018)
 * @since OMIS 3.0
 */
public class EligibilityStatusReasonImpl implements EligibilityStatusReason {

	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String name;
	
	private Boolean valid;
	
	private EligibilityStatusCategory statusCategory;
	
	/**
	 * Instantiates an implementation of eligibility status reason.
	 */
	public EligibilityStatusReasonImpl() {
		// Default constructor
	}

	/** {@inheritDoc} */
	@Override
	public Long getId() {
		return this.id; 
	}

	/** {@inheritDoc} */
	@Override
	public void setId(Long id) {
		this.id = id; 
	}

	/** {@inheritDoc} */
	@Override
	public String getName() {
		return this.name; 
	}

	/** {@inheritDoc} */
	@Override
	public void setName(String name) {
		this.name = name; 
	}

	/** {@inheritDoc} */
	@Override
	public Boolean getValid() {
		return this.valid; 
	}

	/**{@inheritDoc} */
	@Override
	public EligibilityStatusCategory getStatusCategory() {
		return this.statusCategory;
	}

	/**{@inheritDoc} */
	@Override
	public void setStatusCategory(
			final EligibilityStatusCategory statusCategory) {
		this.statusCategory = statusCategory;
	}

	/** {@inheritDoc} */
	@Override
	public void setValid(Boolean valid) {
		this.valid = valid; 
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof EligibilityStatusReason)) {
			return false;
		}
		EligibilityStatusReason that = (EligibilityStatusReason) obj;
		
		if (this.getName() == null) {
			throw new IllegalStateException("Name required");
		}
		if (this.getValid() == null) {
			throw new IllegalStateException("Valid required");
		}
		if (this.getStatusCategory() == null) {
			throw new IllegalStateException("Status Category required");
		}
		
		if (!this.getName().equals(that.getName())) {
			return false;
		}
		if (!this.getStatusCategory().equals(that.getStatusCategory())) {
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
		if (this.getValid() == null) {
			throw new IllegalStateException("Valid required.");
		}
		if (this.getStatusCategory() == null) {
			throw new IllegalStateException("Status Category required");
		}
		
		int hashCode = 14;
		
		hashCode = 29 * hashCode + this.getName().hashCode();
		hashCode = 29 * hashCode + this.getStatusCategory().hashCode();
		return hashCode;
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return String.format(
				"Id: %s, Name: %s, Valid: %s",
				this.getId(),
				this.getName(),
				this.getValid());
	}
}
