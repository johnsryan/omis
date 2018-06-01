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
package omis.supervision.web.form;

import java.io.Serializable;
import java.util.Date;

import omis.supervision.domain.PlacementTermChangeReason;

public class EndPlacementTermForm implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Date endDate;
	
	private Date endTime;

	private boolean allowEndChangeReason;
	
	private PlacementTermChangeReason endChangeReason;

	/** Instantiates a default form for ending placement terms. */
	public EndPlacementTermForm() {
		// Default instantiation
	}
	
	/**
	 * Returns the end date.
	 * 
	 * @return end date
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * Sets the end date.
	 * 
	 * @param endDate end date
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * Returns the end time.
	 * 
	 * @return end time
	 */
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * Sets the end time.
	 * 
	 * @param endTime end time
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/**
	 * Returns whether end change reason is allowed.
	 * 
	 * @return whether end change reason is allowed
	 */
	public boolean getAllowEndChangeReason() {
		return this.allowEndChangeReason;
	}
	
	/**
	 * Sets whether end change reason is allowed.
	 * 
	 * @param allowEndChangeReason whether end change reason is allowed
	 */
	public void setAllowEndChangeReason(final boolean allowEndChangeReason) {
		this.allowEndChangeReason = allowEndChangeReason;
	}
	
	/**
	 * Returns the end change reason.
	 * 
	 * @return end change reason
	 */
	public PlacementTermChangeReason getEndChangeReason() {
		return endChangeReason;
	}

	/**
	 * Sets the end change reason.
	 * 
	 * @param endChangeReason end change reason
	 */
	public void setEndChangeReason(PlacementTermChangeReason endChangeReason) {
		this.endChangeReason = endChangeReason;
	}

}
