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
package omis.locationterm.domain.impl;

import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.datatype.DateRange;
import omis.locationterm.domain.LocationReason;
import omis.locationterm.domain.LocationReasonTerm;
import omis.locationterm.domain.LocationTerm;
import omis.offender.domain.Offender;

/**
 * Implementation of location reason term.
 * 
 * @author Stephen Abson
 * @version 0.1.0 (Nov 8, 2013)
 * @since OMIS 3.0
 */
public class LocationReasonTermImpl
		implements LocationReasonTerm {

	private static final long serialVersionUID = 1L;
	
	private Long id;

	private Offender offender;

	private CreationSignature creationSignature;

	private UpdateSignature updateSignature;

	private LocationTerm locationTerm;

	private DateRange dateRange;

	private LocationReason reason;
	
	/** Instantiates a default implementation of location reason term. */
	public LocationReasonTermImpl() {
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
		return this.id;
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
	public void setCreationSignature(
			final CreationSignature creationSignature) {
		this.creationSignature = creationSignature;
	}

	/** {@inheritDoc} */
	@Override
	public CreationSignature getCreationSignature() {
		return this.creationSignature;
	}

	/** {@inheritDoc} */
	@Override
	public void setUpdateSignature(
			final UpdateSignature updateSignature) {
		this.updateSignature = updateSignature;
	}

	/** {@inheritDoc} */
	@Override
	public UpdateSignature getUpdateSignature() {
		return this.updateSignature;
	}

	/** {@inheritDoc} */
	@Override
	public void setLocationTerm(
			final LocationTerm locationTerm) {
		this.locationTerm = locationTerm;
	}

	/** {@inheritDoc} */
	@Override
	public LocationTerm getLocationTerm() {
		return this.locationTerm;
	}

	/** {@inheritDoc} */
	@Override
	public void setDateRange(final DateRange dateRange) {
		this.dateRange = dateRange;
	}

	/** {@inheritDoc} */
	@Override
	public DateRange getDateRange() {
		return this.dateRange;
	}
	
	/** {@inheritDoc} */
	@Override
	public void setReason(final LocationReason reason) {
		this.reason = reason;
	}

	/** {@inheritDoc} */
	@Override
	public LocationReason getReason() {
		return this.reason;
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof LocationReasonTerm)) {
			return false;
		}
		LocationReasonTerm that = (LocationReasonTerm) obj;
		if (this.getLocationTerm() == null) {
			throw new IllegalStateException("Location term required");
		}
		if (!this.getLocationTerm().equals(that.getLocationTerm())) {
			return false;
		}
		if (this.getDateRange() == null) {
			throw new IllegalStateException("Date range required");
		}
		if (!this.getDateRange().equals(that.getDateRange())) {
			return false;
		}
		return true;
	}
	
	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		if (this.getLocationTerm() == null) {
			throw new IllegalStateException("Location term required");
		}
		if (this.getDateRange() == null) {
			throw new IllegalStateException("Date range required");
		}
		int hashCode = 14;
		hashCode = 29 * hashCode + this.getLocationTerm().hashCode();
		hashCode = 29 * hashCode + this.getDateRange().hashCode();
		return hashCode;
	}
	
	/**
	 * Returns string representation of {@code this} including the reason,
	 * location term and date range.
	 * 
	 * @return string representation of {@code this}
	 */
	@Override
	public String toString() {
		return String.format(
				"#%d: [%s] [%s] %s", this.getId(),
				this.getLocationTerm(), this.getDateRange(),
				this.getReason());
	}
}