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

import omis.location.domain.Location;
import omis.locationterm.domain.AllowedLocationChange;
import omis.locationterm.domain.LocationTermChangeAction;
import omis.supervision.domain.CorrectionalStatus;

/**
 * Implementation of allowed location change.
 *
 * @author Stephen Abson
 * @version 0.0.1 (Mar 4, 2015)
 * @since OMIS 3.0
 */
public class AllowedLocationChangeImpl
		implements AllowedLocationChange {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private LocationTermChangeAction action;
	
	private CorrectionalStatus correctionalStatus;
	
	private Location location;
	
	/** Instantiates a default implementation of allowed location change. */
	public AllowedLocationChangeImpl() {
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
	public void setAction(final LocationTermChangeAction action) {
		this.action = action;
	}

	/** {@inheritDoc} */
	@Override
	public LocationTermChangeAction getAction() {
		return this.action;
	}

	/** {@inheritDoc} */
	@Override
	public void setCorrectionalStatus(
			final CorrectionalStatus correctionalStatus) {
		this.correctionalStatus = correctionalStatus;
	}

	/** {@inheritDoc} */
	@Override
	public CorrectionalStatus getCorrectionalStatus() {
		return this.correctionalStatus;
	}

	/** {@inheritDoc} */
	@Override
	public void setLocation(final Location location) {
		this.location = location;
	}

	/** {@inheritDoc} */
	@Override
	public Location getLocation() {
		return this.location;
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof AllowedLocationChange)) {
			return false;
		}
		AllowedLocationChange that = (AllowedLocationChange) obj;
		if (this.getAction() == null) {
			throw new IllegalStateException("Action required");
		}
		if (!this.getAction().equals(that.getAction())) {
			return false;
		}
		if (this.getCorrectionalStatus() == null) {
			throw new IllegalStateException("Correctional status required");
		}
		if (!this.getCorrectionalStatus().equals(
				that.getCorrectionalStatus())) {
			return false;
		}
		if (this.getLocation() != null) {
			if (!this.getLocation().equals(that.getLocation())) {
				return false;
			}
		} else if (that.getLocation() != null) {
			return false;
		}
		return true;
	}
	
	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		if (this.getAction() == null) {
			throw new IllegalStateException("Action required");
		}
		if (this.getCorrectionalStatus() == null) {
			throw new IllegalStateException("Correctional status required");
		}
		int hashCode = 14;
		hashCode = 29 * hashCode + this.getAction().hashCode();
		hashCode = 29 * hashCode + this.getCorrectionalStatus().hashCode();
		if (this.getLocation() != null) {
			hashCode = 29 * hashCode + this.getLocation().hashCode();
		}
		return hashCode;
	}
}