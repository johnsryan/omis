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
import omis.supervision.domain.SupervisoryOrganization;
import omis.supervision.domain.SupervisoryOrganizationTerm;

/**
 * Implementation of supervisory organization term. 
 *
 * @author Jason Nelson
 * @author Stephen Abson
 * @version 0.0.1 (Feb 2, 2015)
 * @since OMIS 3.0
 */
public class SupervisoryOrganizationTermImpl implements
		SupervisoryOrganizationTerm {
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private CreationSignature creationSignature;
	
	private UpdateSignature updateSignature;
	
	private Offender offender;
	
	private SupervisoryOrganization supervisoryOrganization;
	
	private DateRange dateRange;

	/** Instantiates implementation of supervisory organization term. */
	public SupervisoryOrganizationTermImpl() {
		// Default instantiation
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
	public void setSupervisoryOrganization(
			final SupervisoryOrganization supervisoryOrganization) {
		this.supervisoryOrganization = supervisoryOrganization;
	}

	/** {@inheritDoc} */
	@Override
	public SupervisoryOrganization getSupervisoryOrganization() {
		return this.supervisoryOrganization;
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
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof SupervisoryOrganizationTerm)) {
			return false;
		}
		SupervisoryOrganizationTerm that = (SupervisoryOrganizationTerm) obj;
		if (this.getOffender() == null) {
			throw new IllegalStateException("Offender required");
		}
		if (!this.getOffender().equals(that.getOffender())) {
			return false;
		}
		if (this.getSupervisoryOrganization() == null) {
			throw new IllegalStateException(
					"Supervisory organization required");
		}
		if (!this.getSupervisoryOrganization().equals(
				that.getSupervisoryOrganization())) {
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
		return true;
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
		if(this.getSupervisoryOrganization() == null) {
			throw new IllegalStateException(
					"Supervisory organization required");
		}
		int hashCode = 14;
		hashCode = 29 * hashCode + this.getOffender().hashCode();
		hashCode = 29 * hashCode + this.getDateRange().hashCode();
		hashCode = 29 * hashCode + this.getSupervisoryOrganization().hashCode();
		return hashCode;
	}
	
	/**
	 * Returns string representation of supervisory organization including
	 * offender, supervisory organization and date range.
	 * 
	 * @return string representation of supervisory organization
	 */
	@Override
	public String toString() {
		return String.format(
			"#%d: [%s] [%s] %s", this.getId(), this.getOffender(),
			this.getSupervisoryOrganization(), this.getDateRange());
	}
}