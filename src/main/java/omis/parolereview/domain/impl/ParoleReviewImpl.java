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
package omis.parolereview.domain.impl;

import java.util.Date;

import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.offender.domain.Offender;
import omis.parolereview.domain.ParoleEndorsementCategory;
import omis.parolereview.domain.ParoleReview;
import omis.parolereview.domain.StaffRoleCategory;
import omis.staff.domain.StaffAssignment;

/**
 * Implementation of parole review.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Jan 29, 2018)
 * @since OMIS 3.0
 */
public class ParoleReviewImpl implements ParoleReview {

	private static final long serialVersionUID = 1L;

	private Long id;
	
	private StaffAssignment staffAssignment;
	
	private Date date;
	
	private String text;
	
	private ParoleEndorsementCategory endorsement;
	
	private StaffRoleCategory staffRole;
	
	private Offender offender;
	
	private CreationSignature creationSignature;
	
	private UpdateSignature updateSignature;
	
	/** 
	 * Instantiates an implementation of parole review. 
	 */
	public ParoleReviewImpl() {
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
	public void setStaffAssignment(final StaffAssignment staffAssignment) {
		this.staffAssignment = staffAssignment;
	}

	/** {@inheritDoc} */
	@Override
	public StaffAssignment getStaffAssignment() {
		return staffAssignment;
	}

	/** {@inheritDoc} */
	@Override
	public void setDate(final Date date) {
		this.date = date;
	}

	/** {@inheritDoc} */
	@Override
	public Date getDate() {
		return date;
	}

	/** {@inheritDoc} */
	@Override
	public void setText(final String text) {
		this.text = text;
	}

	/** {@inheritDoc} */
	@Override
	public String getText() {
		return text;
	}

	/** {@inheritDoc} */
	@Override
	public void setEndorsement(final ParoleEndorsementCategory endorsement) {
		this.endorsement = endorsement;
	}

	/** {@inheritDoc} */
	@Override
	public ParoleEndorsementCategory getEndorsement() {
		return endorsement;
	}

	/** {@inheritDoc} */
	@Override
	public void setStaffRole(final StaffRoleCategory staffRole) {
		this.staffRole = staffRole;
	}

	/** {@inheritDoc} */
	@Override
	public StaffRoleCategory getStaffRole() {
		return staffRole;
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
		if (!(obj instanceof ParoleReview)) {
			return false;
		}
		ParoleReview that = (ParoleReview) obj;
		if (this.getStaffAssignment() == null) {
			throw new IllegalStateException("Staff assignment required");
		}
		if (!this.getStaffAssignment().equals(that.getStaffAssignment())) {
			return false;
		}
		if (this.getOffender() == null) {
			throw new IllegalStateException("Offender required");
		}
		if (!this.getOffender().equals(that.getOffender())) {
			return false;
		}
		if (this.getStaffRole() == null) {
			throw new IllegalStateException("Staff role required");
		}
		if (!this.getStaffRole().equals(that.getStaffRole())) {
			return false;
		}
		return true;
	}
	
	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		if (this.getStaffAssignment() == null) {
			throw new IllegalStateException("Staff assignment required");
		}
		if (this.getOffender() == null) {
			throw new IllegalStateException("Offender required");
		}
		if (this.getStaffRole() == null) {
			throw new IllegalStateException("Staff role required");
		}
		int hashCode = 14;
		hashCode = 29 * hashCode + this.getStaffAssignment().hashCode();
		hashCode = 29 * hashCode + this.getOffender().hashCode();
		hashCode = 29 * hashCode + this.getStaffRole().hashCode();
		
		return hashCode;
	}
}