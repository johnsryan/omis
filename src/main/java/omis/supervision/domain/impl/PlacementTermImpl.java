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
package omis.supervision.domain.impl;

import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.datatype.DateRange;
import omis.offender.domain.Offender;
import omis.supervision.domain.CorrectionalStatusTerm;
import omis.supervision.domain.PlacementStatus;
import omis.supervision.domain.PlacementTerm;
import omis.supervision.domain.PlacementTermChangeReason;
import omis.supervision.domain.SupervisoryOrganizationTerm;

/**
 * Implementation of supervisory term.
 * 
 * @author Stephen Abson
 * @version 0.1.0 (Jul 31, 2013)
 * @since OMIS 3.0
 */
public class PlacementTermImpl
		implements PlacementTerm {
	
	private static final long serialVersionUID = 1L;
	
	private Long id;

	private CreationSignature creationSignature;

	private UpdateSignature updateSignature;

	private Offender offender;
	
	private PlacementStatus status;
	
	private DateRange statusDateRange;

	private SupervisoryOrganizationTerm supervisoryOrganizationTerm;

	private CorrectionalStatusTerm correctionalStatusTerm;

	private DateRange dateRange;

	private PlacementTermChangeReason startChangeReason;

	private PlacementTermChangeReason endChangeReason;

	private Boolean locked;
	
	/** Instantiates a default implementation of supervisory term. */
	public PlacementTermImpl() {
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
	public void setStatus(final PlacementStatus status) {
		this.status = status;
	}
	
	/** {@inheritDoc} */
	@Override
	public PlacementStatus getStatus() {
		return this.status;
	}
	
	/** {@inheritDoc} */
	@Override
	public void setStatusDateRange(final DateRange statusDateRange) {
		this.statusDateRange = statusDateRange;
	}
	
	/** {@inheritDoc} */
	@Override
	public DateRange getStatusDateRange() {
		return this.statusDateRange;
	}
	
	/** {@inheritDoc} */
	@Override
	public SupervisoryOrganizationTerm getSupervisoryOrganizationTerm() {
		return supervisoryOrganizationTerm;
	}

	/** {@inheritDoc} */
	@Override
	public void setSupervisoryOrganizationTerm(
			SupervisoryOrganizationTerm supervisoryOrganizationTerm) {
		this.supervisoryOrganizationTerm = supervisoryOrganizationTerm;
	}

	/** {@inheritDoc} */
	@Override
	public CorrectionalStatusTerm getCorrectionalStatusTerm() {
		return correctionalStatusTerm;
	}

	/** {@inheritDoc} */
	@Override
	public void setCorrectionalStatusTerm(
			CorrectionalStatusTerm correctionalStatusTerm) {
		this.correctionalStatusTerm = correctionalStatusTerm;
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
	public void setStartChangeReason(
			final PlacementTermChangeReason startChangeReason) {
		this.startChangeReason = startChangeReason;
	}

	/** {@inheritDoc} */
	@Override
	public PlacementTermChangeReason getStartChangeReason() {
		return this.startChangeReason;
	}

	/** {@inheritDoc} */
	@Override
	public void setEndChangeReason(
			final PlacementTermChangeReason endChangeReason) {
		this.endChangeReason = endChangeReason;
	}

	/** {@inheritDoc} */
	@Override
	public PlacementTermChangeReason getEndChangeReason() {
		return this.endChangeReason;
	}

	/** {@inheritDoc} */
	@Override
	public Boolean getLocked() {
		return locked;
	}

	/** {@inheritDoc} */
	@Override
	public void setLocked(final Boolean locked) {
		this.locked = locked;
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof PlacementTerm)) {
			return false;
		}
		PlacementTerm that = (PlacementTerm) obj;
		if (this.getOffender() == null) {
			throw new IllegalStateException("Offender required");
		}
		if (!this.getOffender().equals(that.getOffender())) {
			return false;
		}
		if (this.getDateRange() == null) {
			throw new IllegalStateException("Date range required");
		}
		if (this.getDateRange().getStartDate() == null) {
			throw new IllegalStateException("Start date required");
		}
		if (!this.getDateRange().equals(that.getDateRange())) {
			return false;
		}
		if (this.getSupervisoryOrganizationTerm() != null) {
			if (!this.getSupervisoryOrganizationTerm()
					.getSupervisoryOrganization()
						.equals(that.getSupervisoryOrganizationTerm()
							.getSupervisoryOrganization())) {
				return false;
			}
		} else if (that.getSupervisoryOrganizationTerm() != null){
			return false;
		}
		if (this.getCorrectionalStatusTerm() == null) {
			throw new IllegalStateException("Correctional status term required");
		}
		if (!this.getCorrectionalStatusTerm().getCorrectionalStatus()
				.equals(that.getCorrectionalStatusTerm()
						.getCorrectionalStatus())) {
			return false;
		}
		throw new UnsupportedOperationException("Not yet implemented");
	}
	
	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		if (this.getOffender() == null) {
			throw new IllegalStateException("Offender required");
		}
		if (this.getDateRange() == null) {
			throw new IllegalStateException("Date range required");
		}
		if (this.getDateRange().getStartDate() == null) {
			throw new IllegalStateException("Start date required");
		}
		if (this.getCorrectionalStatusTerm() == null) {
			throw new IllegalStateException(
					"Correctional status term required");
		}
		int hashCode = 14;
		hashCode = 29 * hashCode + this.getOffender().hashCode();
		hashCode = 29 * hashCode + this.getDateRange().hashCode();
		if (this.getSupervisoryOrganizationTerm() != null) {
			hashCode = 29 * hashCode + this.getSupervisoryOrganizationTerm()
					.getSupervisoryOrganization().hashCode();
		}
		hashCode = 29 * hashCode + this.getCorrectionalStatusTerm()
				.getCorrectionalStatus().hashCode();
		return hashCode;
	}
	
	/**
	 * Returns string representation of placement term including offender,
	 * correctional status, supervisory organization and date range information.
	 * 
	 * @return string representation of placement term
	 */
	@Override
	public String toString() {
		return String.format(
				"#%d: [%s] [%s] [%s] %s",
				this.getId(), this.getOffender(),
				this.getCorrectionalStatusTerm(),
				this.getSupervisoryOrganizationTerm(), this.getDateRange());
	}
}