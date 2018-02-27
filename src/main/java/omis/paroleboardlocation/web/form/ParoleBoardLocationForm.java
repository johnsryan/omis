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
package omis.paroleboardlocation.web.form;

import java.io.Serializable;

import omis.location.domain.Location;

/**
 * Parole board location form.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 21, 2018)
 * @since OMIS 3.0
 */
public class ParoleBoardLocationForm implements Serializable {

	private static final long serialVersionUID = 1L;

	private Location location;
	
	private Boolean videoConferenceCapable;
	
	/**
	 * Instantiates a default parole board location form. 
	 */
	public ParoleBoardLocationForm() {
		// Default instantiation
	}

	/**
	 * Returns the location.
	 *
	 * @return location
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * Sets the location.
	 *
	 * @param location location
	 */
	public void setLocation(final Location location) {
		this.location = location;
	}

	/**
	 * Returns the video conference capable.
	 *
	 * @return video conference capable
	 */
	public Boolean getVideoConferenceCapable() {
		return videoConferenceCapable;
	}

	/**
	 * Sets the video conference capable.
	 *
	 * @param videoConferenceCapable video conference capable
	 */
	public void setVideoConferenceCapable(final Boolean videoConferenceCapable) {
		this.videoConferenceCapable = videoConferenceCapable;
	}
}