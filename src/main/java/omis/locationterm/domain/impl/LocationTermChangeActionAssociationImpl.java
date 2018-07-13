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
import omis.locationterm.domain.LocationTerm;
import omis.locationterm.domain.LocationTermChangeAction;
import omis.locationterm.domain.LocationTermChangeActionAssociation;

/**
 * Association of location term to change action.
 * 
 * @author Stephen Abson
 * @version 0.0.1 (Jul 9, 2018)
 * @since OMIS 3.0
 */
public class LocationTermChangeActionAssociationImpl
		implements LocationTermChangeActionAssociation {
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private LocationTerm locationTerm;
	
	private LocationTermChangeAction changeAction;
	
	private CreationSignature creationSignature;
	
	private UpdateSignature updateSignature;

	/** Implementation of association of location term to change action. */
	public LocationTermChangeActionAssociationImpl() {
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
	public void setLocationTerm(final LocationTerm locationTerm) {
		this.locationTerm = locationTerm;
	}

	/** {@inheritDoc} */
	@Override
	public LocationTerm getLocationTerm() {
		return this.locationTerm;
	}

	/** {@inheritDoc} */
	@Override
	public void setChangeAction(final LocationTermChangeAction changeAction) {
		this.changeAction = changeAction;
	}

	/** {@inheritDoc} */
	@Override
	public LocationTermChangeAction getChangeAction() {
		return this.changeAction;
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
	public void setUpdateSignature(final UpdateSignature updateSignature) {
		this.updateSignature = updateSignature;
	}

	/** {@inheritDoc} */
	@Override
	public UpdateSignature getUpdateSignature() {
		return this.updateSignature;
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof LocationTermChangeActionAssociation)) {
			return false;
		}
		LocationTermChangeActionAssociation that
			= (LocationTermChangeActionAssociation) obj;
		if (this.getLocationTerm() == null) {
			throw new IllegalStateException("Location term required");
		}
		if (!this.getLocationTerm().equals(that.getLocationTerm())) {
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
		int hashCode = 14;
		hashCode = 29 * hashCode + this.getLocationTerm().hashCode();
		return hashCode;
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return String.format("#%d: [%s] [%s]", this.getId(),
				this.getLocationTerm(), this.getChangeAction());
	}
}