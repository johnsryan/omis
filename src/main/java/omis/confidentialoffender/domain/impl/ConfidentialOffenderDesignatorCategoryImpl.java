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
package omis.confidentialoffender.domain.impl;

import omis.confidentialoffender.domain.ConfidentialOffenderDesignatorCategory;

/**
 * Implementation of confidential offender designator category.
 *
 * @author Sheronda Vaughn
 * @version 0.1.0 (Mar 29, 2018)
 * @since OMIS 3.0
 */
public class ConfidentialOffenderDesignatorCategoryImpl 
	implements ConfidentialOffenderDesignatorCategory {
		
	private Long id;
	
	private String description;
	
	private String instruction;
	
	private Boolean valid;
	

	/** Instantiates an implementation of confidential offender designator 
	 * category. */
	public ConfidentialOffenderDesignatorCategoryImpl() {
		// Default constructor.
	}

	/** {@inheritDoc} */
	@Override
	public void setDescription(final String description) {
		this.description = description;
	}

	/** {@inheritDoc} */
	@Override
	public String getDescription() {
		return this.description;
	}

	/** {@inheritDoc} */
	@Override
	public void setInstruction(final String instruction) {
		this.instruction = instruction;
	}

	/** {@inheritDoc} */
	@Override
	public String getInstruction() {
		return this.instruction;
	}

	/** {@inheritDoc} */
	@Override
	public void setValid(final Boolean valid) {
		this.valid = valid;
	}

	/** {@inheritDoc} */
	@Override
	public Boolean getValid() {
		return this.valid;
	}

	/** {@inheritDoc} */
	@Override
	public void setId(final Long id) {
		this.id = id;
	}

	/** {@inheritDoc} */
	@Override
	public Long getId() {
		return this.id;
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ConfidentialOffenderDesignatorCategory)) {
			return false;
		}
		ConfidentialOffenderDesignatorCategory that 
			= (ConfidentialOffenderDesignatorCategory) obj;
		if (this.getDescription() == null) {
			throw new IllegalStateException("Description required");
		}
		if (!this.getDescription().equals(that.getDescription())) {
			return false;
		}
		if (this.getInstruction() != null) {
			if (!this.getInstruction().equals(that.getInstruction())) {
				return false; 
			}
		} else if (that.getInstruction() != null) {
			return false;
		}		
			
		return true;
	}
	
	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		if (this.getDescription() == null) {
			throw new IllegalArgumentException("Description required");
		}	
		int hashCode = 14;
		hashCode = 29 * hashCode + this.getDescription().hashCode();
		if (this.getInstruction() != null) {
			hashCode = 29 * hashCode + this.getInstruction().hashCode();
		}
		return hashCode;
	}
}