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
package omis.paroleboardlocation.report;

import java.io.Serializable;

/**
 * Parole board location summary.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 20, 2018)
 * @since OMIS 3.0
 */
public class ParoleBoardLocationSummary implements Serializable {

	private static final long serialVersionUID = 1L;

	private final Long paroleBoardLocationId;
	
	private final String locationName;
	
	private final Boolean videoCapable;
	
	/**
	 * Instantiates an implementation of parole board location summary.
	 * 
	 * @param paroleBoardLocationId parole board location id
	 * @param locationName location name
	 * @param videoCapable video capable
	 */
	public ParoleBoardLocationSummary(final Long paroleBoardLocationId,
			final String locationName, final Boolean videoCapable) {
		this.paroleBoardLocationId = paroleBoardLocationId;
		this.locationName = locationName;
		this.videoCapable = videoCapable;
	}

	/**
	 * Returns the parole board location id.
	 *
	 * @return parole board location id
	 */
	public Long getParoleBoardLocationId() {
		return paroleBoardLocationId;
	}

	/**
	 * Returns the location name.
	 *
	 * @return location name
	 */
	public String getLocationName() {
		return locationName;
	}

	/**
	 * Returns the video capable.
	 *
	 * @return video capable
	 */
	public Boolean getVideoCapable() {
		return videoCapable;
	}
}