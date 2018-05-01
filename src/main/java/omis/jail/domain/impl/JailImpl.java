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

package omis.jail.domain.impl;

import omis.jail.domain.Jail;
import omis.jail.domain.JailCategory;
import omis.location.domain.Location;

/**
 * Implementation of jail.
 *
 * @author Stephen Abson
 * @author Yidong Li
 * @version 0.0.1 (Apr 19, 2018)
 * @since OMIS 3.0
 */
public class JailImpl
		implements Jail {
	
	private static final long serialVersionUID = 1L;
	
	private Long id;

	private String name;

	private Location location;

	private Boolean active;

	private JailCategory category;
	
	private Long telephoneNumber;
	
	/** Instantiates implementation of jail. */
	public JailImpl() {
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
	public void setName(final String name) {
		this.name = name;
	}

	/** {@inheritDoc} */
	@Override
	public String getName() {
		return this.name;
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
	public void setActive(final Boolean active) {
		this.active = active;
	}

	/** {@inheritDoc} */
	@Override
	public Boolean getActive() {
		return this.active;
	}
	
	/** {@inheritDoc} */
	@Override
	public void setCategory(final JailCategory category) {
		this.category = category;
	}

	@Override
	public JailCategory getCategory() {
		return this.category;
	}
	
	/** {@inheritDoc} */
	@Override
	public void setTelephoneNumber(final Long telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

	@Override
	public Long getTelephoneNumber() {
		return this.telephoneNumber;
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof Jail)) {
			return false;
		}
		Jail that = (Jail) obj;
		if (this.getName() == null) {
			throw new IllegalStateException("Name required");
		}
		if (!this.getName().equals(that.getName())) {
			return false;
		}
		if (this.getLocation() == null) {
			throw new IllegalStateException("Location required");
		}
		if (!this.getLocation().equals(that.getLocation())) {
			return false;
		}
		return true;
	}
	
	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		if (this.getName() == null) {
			throw new IllegalStateException("Name required");
		}
		if (this.getLocation() == null) {
			throw new IllegalStateException("Location required");
		}
		int hashCode = 14;
		hashCode = 29 * hashCode + this.getName().hashCode();
		hashCode = 29 * hashCode + this.getLocation().hashCode();
		return hashCode;
	}
}