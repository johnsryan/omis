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

import omis.confidentialoffender.domain.ConfidentialOffenderDesignator;
import omis.confidentialoffender.domain.ConfidentialOffenderDesignatorCategory;
import omis.offender.domain.Offender;

/**
 * Confidential offender designator implementation.
 *
 * @author Sheronda Vaughn
 * @version 0.1.0 (Apr 2, 2018)
 * @since OMIS 3.0
 */
public class ConfidentialOffenderDesignatorImpl 
	implements ConfidentialOffenderDesignator {

	private Offender offender;
	
	private Long id;
	
	private ConfidentialOffenderDesignatorCategory category;

	/** Instantiates an implementation of confidential offender designator. */
	public ConfidentialOffenderDesignatorImpl() {
		// Default constructor.
	}

	/** {@inheritDoc} */
	@Override
	public void setOffender(final Offender offender) {
		this.offender = offender;
	}

	/** {@inheritDoc} */
	@Override
	public Offender getOffender() {
		return this.offender;
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
	public void setCategory(
			final ConfidentialOffenderDesignatorCategory category) {
		this.category = category;
	}

	/** {@inheritDoc} */
	@Override
	public ConfidentialOffenderDesignatorCategory getCategory() {
		return this.category;
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ConfidentialOffenderDesignator)) {
			return false;
		}
		ConfidentialOffenderDesignator that 
			= (ConfidentialOffenderDesignator) obj;
		if (this.getOffender() == null) {
			throw new IllegalStateException("Offender required");
		}
		if (!this.getOffender().equals(that.getOffender())) {
			return false;
		}
		return true;
	}
	
	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		if (getOffender() == null) {
			throw new IllegalArgumentException("Offender required");
		}
		int hashCode = 14;
		hashCode = 29 * hashCode + this.getOffender().hashCode();
		return hashCode;
	}
}