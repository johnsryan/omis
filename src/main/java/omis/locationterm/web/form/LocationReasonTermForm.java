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
package omis.locationterm.web.form;

import java.io.Serializable;
import java.util.Date;

import omis.locationterm.domain.LocationReason;
import omis.locationterm.domain.LocationTerm;

/**
 * Form for location reason term.
 * 
 * @author Stephen Abson
 * @version 0.1.0 (Jan 16, 2014)
 * @since OMIS 3.0
 */
public class LocationReasonTermForm
		implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private LocationTerm locationTerm;
	
	private LocationReason reason;
	
	private Date startDate;
	
	private Date startTime;
	
	private Date endDate;
	
	private Date endTime;
	
	/** Instantiates a form for location reason terms. */
	public LocationReasonTermForm() {
		// Default instantiation
	}

	/**
	 * Returns the location term.
	 * 
	 * @return location term
	 */
	public LocationTerm getLocationTerm() {
		return this.locationTerm;
	}

	/**
	 * Sets the location term.
	 * 
	 * @param locationTerm location term
	 */
	public void setLocationTerm(final LocationTerm locationTerm) {
		this.locationTerm = locationTerm;
	}

	/**
	 * Returns the reason.
	 * 
	 * @return reason
	 */
	public LocationReason getReason() {
		return this.reason;
	}

	/**
	 * Sets the reason.
	 * 
	 * @param reason reason
	 */
	public void setReason(final LocationReason reason) {
		this.reason = reason;
	}

	/**
	 * Returns the start date.
	 * 
	 * @return start date
	 */
	public Date getStartDate() {
		return this.startDate;
	}

	/**
	 * Sets the start date.
	 * 
	 * @param startDate start date
	 */
	public void setStartDate(final Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * Returns start time.
	 * 
	 * @return start time
	 */
	public Date getStartTime() {
		return this.startTime;
	}

	/**
	 * Sets start time.
	 * 
	 * @param startTime start time
	 */
	public void setStartTime(final Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * Returns the end date.
	 * 
	 * @return end date
	 */
	public Date getEndDate() {
		return this.endDate;
	}

	/**
	 * Sets the end date.
	 * 
	 * @param endDate end date
	 */
	public void setEndDate(final Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * Returns end time.
	 * 
	 * @return end time
	 */
	public Date getEndTime() {
		return this.endTime;
	}

	/**
	 * Sets end time.
	 * 
	 * @param endTime end time
	 */
	public void setEndTime(final Date endTime) {
		this.endTime = endTime;
	}
}