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
import omis.locationterm.domain.LocationReasonTerm;

/**
 * Location reason term item.
 *
 * @author Stephen Abson
 * @version 0.0.1
 * @since OMIS 3.0
 */
public class LocationReasonTermItem
		implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private LocationReasonTermItemOperation operation;
	
	private LocationReasonTerm reasonTerm;
	
	private LocationReason reason;
	
	private Date startDate;
	
	private Date startTime;
	
	private Date endDate;
	
	private Date endTime;
	
	/** Instantiates location reason term item. */
	public LocationReasonTermItem() {
		// Default instantiation
	}
	
	/**
	 * Sets operation.
	 * 
	 * @param operation operation
	 */
	public void setOperation(final LocationReasonTermItemOperation operation) {
		this.operation = operation;
	}
	
	/**
	 * Returns operation.
	 * 
	 * @return operation
	 */
	public LocationReasonTermItemOperation getOperation() {
		return this.operation;
	}
	
	/**
	 * Sets reason term.
	 * 
	 * @param reasonTerm reason term
	 */
	public void setReasonTerm(final LocationReasonTerm reasonTerm) {
		this.reasonTerm = reasonTerm;
	}
	
	/**
	 * Returns reason term.
	 * 
	 * @return reason term
	 */
	public LocationReasonTerm getReasonTerm() {
		return this.reasonTerm;
	}
	
	/**
	 * Returns reason.
	 * 
	 * @param reason reason
	 */
	public void setReason(final LocationReason reason) {
		this.reason = reason;
	}
	
	/**
	 * Returns reason.
	 * 
	 * @return reason reason
	 */
	public LocationReason getReason() {
		return this.reason;
	}
	
	/**
	 * Sets start date.
	 * 
	 * @param startDate startDate
	 */
	public void setStartDate(final Date startDate) {
		this.startDate = startDate;
	}
	
	/**
	 * Returns start date.
	 * 
	 * @return start date
	 */
	public Date getStartDate() {
		return this.startDate;
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
	 * Returns start time.
	 * 
	 * @return start time
	 */
	public Date getStartTime() {
		return this.startTime;
	}
	
	/**
	 * Sets end date.
	 * 
	 * @param endDate end date
	 */
	public void setEndDate(final Date endDate) {
		this.endDate = endDate;
	}
	
	/**
	 * Returns end date.
	 * 
	 * @return end date
	 */
	public Date getEndDate() {
		return this.endDate;
	}
	
	/**
	 * Sets end time.
	 * 
	 * @param endTime end time
	 */
	public void setEndTime(final Date endTime) {
		this.endTime = endTime;
	}
	
	/**
	 * Returns end time.
	 * 
	 * @return end time
	 */
	public Date getEndTime() {
		return this.endTime;
	}
}