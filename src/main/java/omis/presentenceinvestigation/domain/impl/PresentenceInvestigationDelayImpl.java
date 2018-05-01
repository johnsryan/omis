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
package omis.presentenceinvestigation.domain.impl;

import java.util.Date;

import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.presentenceinvestigation.domain.PresentenceInvestigationDelay;
import omis.presentenceinvestigation.domain.PresentenceInvestigationDelayCategory;
import omis.presentenceinvestigation.domain.PresentenceInvestigationRequest;

/**
 * Implementation of presentence investigation delay.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Apr 23, 2018)
 * @since OMIS 3.0
 */
public class PresentenceInvestigationDelayImpl 
		implements PresentenceInvestigationDelay {

	private static final long serialVersionUID = 1L;

	private Long id;
	
	private Date date;
	
	private PresentenceInvestigationRequest presentenceInvestigationRequest;
	
	private PresentenceInvestigationDelayCategory reason;
	
	private CreationSignature creationSignature;
	
	private UpdateSignature updateSignature;
	
	/** 
	 * Instantiates an implementation of presentence investigation delay. 
	 */
	public PresentenceInvestigationDelayImpl() {
		// Default constructor.
	}
	
	/** {@inheritDoc} */
	@Override
	public Long getId() {
		return id;
	}

	/** {@inheritDoc} */
	@Override
	public void setId(final Long id) {
		this.id = id;
	}

	/** {@inheritDoc} */
	@Override
	public Date getDate() {
		return date;
	}

	/** {@inheritDoc} */
	@Override
	public void setDate(final Date date) {
		this.date = date;
	}

	/** {@inheritDoc} */
	@Override
	public PresentenceInvestigationRequest 
			getPresentenceInvestigationRequest() {
		return presentenceInvestigationRequest;
	}

	/** {@inheritDoc} */
	@Override
	public void setPresentenceInvestigationRequest(
			final PresentenceInvestigationRequest 
					presentenceInvestigationRequest) {
		this.presentenceInvestigationRequest = presentenceInvestigationRequest;
	}

	/** {@inheritDoc} */
	@Override
	public PresentenceInvestigationDelayCategory getReason() {
		return reason;
	}

	/** {@inheritDoc} */
	@Override
	public void setReason(final PresentenceInvestigationDelayCategory reason) {
		this.reason = reason;
	}

	/** {@inheritDoc} */
	@Override
	public CreationSignature getCreationSignature() {
		return creationSignature;
	}
	
	/** {@inheritDoc} */
	@Override
	public void setCreationSignature(
			final CreationSignature creationSignature) {
		this.creationSignature = creationSignature;
	}

	/** {@inheritDoc} */
	@Override
	public UpdateSignature getUpdateSignature() {
		return updateSignature;
	}
	
	/** {@inheritDoc} */
	@Override
	public void setUpdateSignature(final UpdateSignature updateSignature) {
		this.updateSignature = updateSignature;
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof PresentenceInvestigationDelay)) {
			return false;
		}
		PresentenceInvestigationDelay that = 
				(PresentenceInvestigationDelay) obj;
		if (this.getDate() == null) {
			if (that.getDate() != null) {
				return false;
			} 
		} else {
			if (!this.getDate().equals(that.getDate())) {
				return false;
			}
		}
		if (this.getPresentenceInvestigationRequest() == null) {
			throw new IllegalStateException(
					"Presentence investigation request required");
		}
		if (!this.getPresentenceInvestigationRequest().equals(
				that.getPresentenceInvestigationRequest())) {
			return false;
		}
		if (this.getReason() == null) {
			throw new IllegalStateException("Reason required");
		}
		if (!this.getReason().equals(that.getReason())) {
			return false;
		}
		return true;
	}
	
	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		if (this.getPresentenceInvestigationRequest() == null) {
			throw new IllegalStateException(
					"Presentence investigation request required");
		}
		if (this.getReason() == null) {
			throw new IllegalStateException("Reason required");
		}
		int hashCode = 14;
		if (this.getDate() != null) {
			hashCode = 29 * hashCode + this.getDate().hashCode();
		}
		hashCode = 29 * hashCode + this.getPresentenceInvestigationRequest()
				.hashCode();
		hashCode = 29 * hashCode + this.getReason().hashCode();
		return hashCode;
	}
}