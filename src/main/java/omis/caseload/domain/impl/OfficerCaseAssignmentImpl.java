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
package omis.caseload.domain.impl;

import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.caseload.domain.OfficerCaseAssignment;
import omis.caseload.domain.SupervisionLevelCategory;
import omis.datatype.DateRange;
import omis.location.domain.Location;
import omis.offender.domain.Offender;
import omis.user.domain.UserAccount;

/**
 * Implementation of officer case assignment.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Jun 12, 2018)
 * @since OMIS 3.0
 */
public class OfficerCaseAssignmentImpl implements OfficerCaseAssignment {

	private static final long serialVersionUID = 1L;

	private Long id;
	
	private Offender offender;
	
	private UserAccount officer;
	
	private DateRange dateRange;
	
	private Location supervisionOffice;
	
	private SupervisionLevelCategory supervisionLevel;
	
	private CreationSignature creationSignature;
	
	private UpdateSignature updateSignature;
	
	/**
	 * Instantiates an implementation of officer case assignment. 
	 */
	public OfficerCaseAssignmentImpl() {
		// Default constructor.
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
	public void setOffender(final Offender offender) {
		this.offender = offender;
	}

	/** {@inheritDoc} */
	@Override
	public Offender getOffender() {
		return offender;
	}

	/** {@inheritDoc} */
	@Override
	public void setOfficer(final UserAccount officer) {
		this.officer = officer;
	}

	/** {@inheritDoc} */
	@Override
	public UserAccount getOfficer() {
		return officer;
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
	public void setSupervisionOffice(final Location supervisionOffice) {
		this.supervisionOffice = supervisionOffice;
	}

	/** {@inheritDoc} */
	@Override
	public Location getSupervisionOffice() {
		return supervisionOffice;
	}

	/** {@inheritDoc} */
	@Override
	public void setSupervisionLevel(
			final SupervisionLevelCategory supervisionLevel) {
		this.supervisionLevel = supervisionLevel;
	}

	/** {@inheritDoc} */
	@Override
	public SupervisionLevelCategory getSupervisionLevel() {
		return supervisionLevel;
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
		return creationSignature;
	}

	/** {@inheritDoc} */
	@Override
	public void setUpdateSignature(final UpdateSignature updateSignature) {
		this.updateSignature = updateSignature;
	}

	/** {@inheritDoc} */
	@Override
	public UpdateSignature getUpdateSignature() {
		return updateSignature;
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof OfficerCaseAssignment)) {
			return false;
		}
		OfficerCaseAssignment that = (OfficerCaseAssignment) obj;
		if (this.getOffender() == null) {
			throw new IllegalStateException("Offender required.");
		}
		if (!this.getOffender().equals(that.getOffender())) {
			return false;
		}
		if (this.getOfficer() == null) {
			throw new IllegalStateException("Officer required.");
		}
		if (!this.getOfficer().equals(that.getOfficer())) {
			return false;
		}
		if (this.getDateRange().getStartDate() == null) {
			throw new IllegalStateException("Start date required.");
		}
		if (!this.getDateRange().getStartDate().equals(
				that.getDateRange().getStartDate())) {
			return false;
		}
		if (this.getDateRange().getEndDate() != null) {
			if (!this.getDateRange().getEndDate().equals(
					that.getDateRange().getEndDate())) {
				return false;
			}
		} else {
			if (that.getDateRange().getEndDate() != null) {
				return false;
			}
		}
		return true;
	}
	
	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		if (this.getOffender() == null) {
			throw new IllegalStateException("Offender required.");
		}
		if (this.getOfficer() == null) {
			throw new IllegalStateException("Officer required.");
		}
		if (this.getDateRange().getStartDate() == null) {
			throw new IllegalStateException("Start date required.");
		}
		int hashCode = 14;
		hashCode = 29 * hashCode + this.getOffender().hashCode();
		hashCode = 29 * hashCode + this.getOfficer().hashCode();
		hashCode = 29 * hashCode + 
				this.getDateRange().getStartDate().hashCode();
		if (this.getDateRange().getEndDate() != null) {
			hashCode = 29 * hashCode + 
					this.getDateRange().getEndDate().hashCode();
		}
		return hashCode;
	}
}