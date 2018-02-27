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
package omis.paroleboardlocation.domain.impl;

import omis.location.domain.Location;
import omis.paroleboardlocation.domain.ParoleBoardLocation;

/**
 * Parole Board Location Implementation.
 * 
 * @author Annie Wahl
 * @author Josh Divine
 * @version 0.1.1 (Feb 20, 2018)
 * @since OMIS 3.0
 */
public class ParoleBoardLocationImpl implements ParoleBoardLocation {
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private Location location;
	
	private Boolean videoConferenceCapable;
	
	/**{@inheritDoc} */
	@Override
	public Long getId() {
		return this.id;
	}

	/**{@inheritDoc} */
	@Override
	public void setId(final Long id) {
		this.id = id;
	}

	/**{@inheritDoc} */
	@Override
	public Location getLocation() {
		return this.location;
	}

	/**{@inheritDoc} */
	@Override
	public void setLocation(final Location location) {
		this.location = location;
	}

	/**{@inheritDoc} */
	@Override
	public Boolean getVideoConferenceCapable() {
		return this.videoConferenceCapable;
	}

	/**{@inheritDoc} */
	@Override
	public void setVideoConferenceCapable(
			final Boolean videoConferenceCapable) {
		this.videoConferenceCapable = videoConferenceCapable;
	}
	
	/**{@inheritDoc}*/
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ParoleBoardLocation)) {
			return false;
		}
		
		ParoleBoardLocation that = (ParoleBoardLocation) obj;
		
		if (this.getLocation() == null) {
			throw new IllegalStateException("Location required.");
		}
		if (this.getVideoConferenceCapable() == null) {
			throw new IllegalStateException(
					"Video Conference Capable required.");
		}
		
		if (!this.getLocation().equals(that.getLocation())) {
			return false;
		}
		if (!this.getVideoConferenceCapable().equals(
				that.getVideoConferenceCapable())) {
			return false;
		}
		
		return true;
	}
	
	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		if (this.getLocation() == null) {
			throw new IllegalStateException("Location required.");
		}
		if (this.getVideoConferenceCapable() == null) {
			throw new IllegalStateException(
					"Video Conference Capable required.");
		}
		
		int hashCode = 14;
		hashCode = 29 * hashCode + this.getLocation().hashCode();
		hashCode = 29 * hashCode + this.getVideoConferenceCapable().hashCode();
		
		return hashCode;
	}
}