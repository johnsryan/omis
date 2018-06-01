/*
 *  OMIS - Offender Management Information System
 *  Copyright (C) 2011 - 2017 State of Montana
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package omis.travelpermit.domain.impl;

import java.util.Date;

import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.travelpermit.domain.TravelPermit;
import omis.travelpermit.domain.TravelPermitNote;

/**
 * Implementation of travel permit note.
 * @author Yidong Li
 * @version 0.1.0 (May 18, 2018)
 * @since OMIS 3.0 
 */
public class TravelPermitNoteImpl implements TravelPermitNote {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String value;
	private Date date;
	private TravelPermit permit;
	private UpdateSignature updateSignature;
	private CreationSignature creationSignature;
	
	/** Constructor. */
	public TravelPermitNoteImpl() {
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
	public String getValue() {
		return this.value;
	}

	/** {@inheritDoc} */
	@Override
	public void setValue(final String value) {
		this.value = value;
	}

	/** {@inheritDoc} */
	@Override
	public TravelPermit getPermit() {
		return this.permit;
	}
	
	/** {@inheritDoc} */
	@Override
	public void setPermit(final TravelPermit permit) {
		this.permit=permit;
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof TravelPermitNote)) {
			return false;
		}
		
		TravelPermitNote that = (TravelPermitNote) obj;
		
		if (this.getPermit() == null) {
			throw new IllegalStateException("Permit required.");
		}
		if (this.getDate() == null) {
			throw new IllegalStateException("Date required.");
		}
		if (this.getValue() == null) {
			throw new IllegalStateException("Value required.");
		}
	
		if (this.getPermit().equals(that.getPermit())) {
			return true;
		}
		if (this.getDate().equals(that.getDate())) {
			return true;
		}
		if (this.getValue().equals(that.getValue())) {
			return true;
		}
		return false;
	}
	
	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		if (this.getPermit() == null) {
			throw new IllegalStateException("Permit required.");
		}
		if (this.getDate() == null) {
			throw new IllegalStateException("Date required.");
		}
		if (this.getValue() == null) {
			throw new IllegalStateException("Value required.");
		}
		
		int hashCode = 14;
		
		hashCode += 29 * this.getPermit().hashCode();
		hashCode += 29 * this.getDate().hashCode();
		hashCode += 29 * this.getValue().hashCode();
		return hashCode;
	}

	@Override
	public void setCreationSignature(final CreationSignature creationSignature) {
		this.creationSignature = creationSignature;
		
	}

	@Override
	public CreationSignature getCreationSignature() {
		return this.creationSignature;
	}

	@Override
	public void setUpdateSignature(final UpdateSignature updateSignature) {
		this.updateSignature = updateSignature;
		
	}

	@Override
	public UpdateSignature getUpdateSignature() {
		return this.updateSignature;
	}
}