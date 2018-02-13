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
package omis.need.domain.impl;

import java.util.Date;

import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.need.domain.CasePlanObjective;
import omis.need.domain.CasePlanReferral;
import omis.need.domain.CasePlanReferralResponse;
import omis.need.domain.ReferralCategory;
import omis.organization.domain.Organization;
import omis.organization.domain.OrganizationDepartment;

/**
 * Case plan referral implementation.
 * 
 * @author Joel Norris
 * @author Josh Divine
 * @version 0.1.1 (Feb 12, 2018)
 * @since OMIS 3.0
 */
public class CasePlanReferralImpl implements CasePlanReferral {

	private static final long serialVersionUID = 1L;

	private Long id;
	
	private Date date;
	
	private String comment;
	
	private CasePlanObjective objective;
	
	private Organization organization;
	
	private OrganizationDepartment department;
	
	private ReferralCategory category;
	
	private CasePlanReferralResponse response;
	
	private CreationSignature creationSignature;
	
	private UpdateSignature updateSignature;
	
	/**
	 * Instantiates a default instance of case plan referral.
	 */
	public CasePlanReferralImpl() {
		//Default constructor.
	}

	/** {@inheritDoc} */
	@Override
	public Long getId() {
		return this.id;
	}

	/** {@inheritDoc} */
	@Override
	public void setId(final Long id) {
		this.id = id;
	}

	/** {@inheritDoc} */
	@Override
	public Date getDate() {
		return this.date;
	}

	/** {@inheritDoc} */
	@Override
	public void setDate(final Date date) {
		this.date = date;
	}

	/** {@inheritDoc} */
	@Override
	public String getComment() {
		return this.comment;
	}

	/** {@inheritDoc} */
	@Override
	public void setComment(final String comment) {
		this.comment = comment;
	}

	/** {@inheritDoc} */
	@Override
	public CasePlanObjective getObjective() {
		return this.objective;
	}

	/** {@inheritDoc} */
	@Override
	public void setObjective(final CasePlanObjective objective) {
		this.objective = objective;
	}

	/** {@inheritDoc} */
	@Override
	public Organization getOrganization() {
		return this.organization;
	}

	/** {@inheritDoc} */
	@Override
	public void setOrganization(final Organization organization) {
		this.organization = organization;
	}

	/** {@inheritDoc} */
	@Override
	public OrganizationDepartment getDepartment() {
		return this.department;
	}

	/** {@inheritDoc} */
	@Override
	public void setDepartment(final OrganizationDepartment department) {
		this.department = department;
	}

	/** {@inheritDoc} */
	@Override
	public ReferralCategory getCategory() {
		return this.category;
	}

	/** {@inheritDoc} */
	@Override
	public void setCategory(final ReferralCategory category) {
		this.category = category;
	}

	/** {@inheritDoc} */
	@Override
	public CasePlanReferralResponse getResponse() {
		return this.response;
	}

	/** {@inheritDoc} */
	@Override
	public void setResponse(final CasePlanReferralResponse response) {
		this.response = response;
	}

	/** {@inheritDoc} */
	@Override
	public CreationSignature getCreationSignature() {
		return creationSignature;
	}

	/** {@inheritDoc} */
	@Override
	public void setCreationSignature(CreationSignature creationSiganture) {
		this.creationSignature = creationSiganture;
	}

	/** {@inheritDoc} */
	@Override
	public UpdateSignature getUpdateSignature() {
		return updateSignature;
	}

	/** {@inheritDoc} */
	@Override
	public void setUpdateSignature(UpdateSignature updateSignature) {
		this.updateSignature = updateSignature;
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}	
		if (!(o instanceof CasePlanReferral)) {
			return false;
		}
		
		CasePlanReferral that = (CasePlanReferral) o;
		
		if (this.getObjective() == null) {
			throw new IllegalStateException("Objective required.");
		}
		if (!this.getObjective().equals(that.getObjective())) {
			return false;
		}
		if (this.getDate() == null) {
			throw new IllegalStateException("Date required");
		}
		if (!this.getDate().equals(that.getDate())) {
			return false;
		}
		if (this.getOrganization() == null) {
			throw new IllegalStateException("Organization required");
		}
		if (!this.getOrganization().equals(that.getOrganization())) {
			return false;
		}
		if (this.getDepartment() != null) {
			if (!this.getDepartment().equals(that.getDepartment())) {
				return false;
			}
		} else if (that.getDepartment() != null) {
			return false;
		}
		
		return true;
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		if (this.getObjective() == null) {
			throw new IllegalStateException("Objective required.");
		}
		if (this.getDate() == null) {
			throw new IllegalStateException("Date required");
		}
		if (this.getOrganization() == null) {
			throw new IllegalStateException("Organization required");
		}
		int hashCode = 14;
		hashCode = 29 * hashCode + this.getObjective().hashCode();
		hashCode = 29 * hashCode + this.getDate().hashCode();
		hashCode = 29 * hashCode + this.getOrganization().hashCode();
		if (this.getDepartment() != null) {
			hashCode = 29 * hashCode + this.getDepartment().hashCode();
		}
		
		return hashCode;
	}
}