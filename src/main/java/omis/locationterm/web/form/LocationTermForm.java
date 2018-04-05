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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import omis.location.domain.Location;
import omis.locationterm.domain.LocationReason;
import omis.region.domain.State;

/**
 * Form for location terms.
 * 
 * @author Stephen Abson
 * @version 0.1.0 (Jan 15, 2014)
 * @since OMIS 3.0
 */
public class LocationTermForm
		implements Serializable {

	private static final long serialVersionUID = 1L;

	private Boolean allowState;
	
	private State state;
	
	private Boolean allowLocation;
	
	private Location location;
	
	private LocationReason reason;
	
	private Date startDate;
	
	private Date startTime;
	
	private Date endDate;
	
	private Date endTime;
	
	private Boolean allowMultipleReasonTerms;
	
	private Boolean allowSingleReasonTerm;
	
	private Boolean associateMultipleReasonTerms;
	
	private List<LocationReasonTermItem> reasonTermItems
		= new ArrayList<LocationReasonTermItem>();
	
	/** Instantiates a form for location terms. */
	public LocationTermForm() {
		// Default instantiation
	}
	
	/**
	 * Returns whether State is allowed.
	 * 
	 * @return whether State is allowed
	 */
	public Boolean getAllowState() {
		return this.allowState;
	}
	
	/**
	 * Sets whether State is allowed.
	 * 
	 * @param allowState whether State is allowed
	 */
	public void setAllowState(final Boolean allowState) {
		this.allowState = allowState;
	}
	
	/**
	 * Returns State.
	 * 
	 * @return State
	 */
	public State getState() {
		return this.state;
	}
	
	/**
	 * Sets State.
	 * 
	 * @param state State
	 */
	public void setState(final State state) {
		this.state = state;
	}
	
	/**
	 * Returns whether to allow location.
	 * 
	 * @return whether to allow location
	 */
	public Boolean getAllowLocation() {
		return this.allowLocation;
	}
	
	/**
	 * Sets whether to allow location.
	 * 
	 * @param allowLocation whether to allow location
	 */
	public void setAllowLocation(final Boolean allowLocation) {
		this.allowLocation = allowLocation;
	}
	
	/**
	 * Returns the location.
	 * 
	 * @return location
	 */
	public Location getLocation() {
		return this.location;
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
	 * Returns reason.
	 * 
	 * @return reason
	 */
	public LocationReason getReason() {
		return this.reason;
	}

	/**
	 * Sets reason.
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
	
	/**
	 * Returns whether multiple reason terms are allowed.
	 * 
	 * @return whether multiple reason terms are allowed
	 */
	public Boolean getAllowMultipleReasonTerms() {
		return this.allowMultipleReasonTerms;
	}

	/**
	 * Sets whether multiple reason terms are allowed.
	 * 
	 * @param allowMultipleReasonTerms whether multiple reason terms are allowed
	 */
	public void setAllowMultipleReasonTerms(
			final Boolean allowMultipleReasonTerms) {
		this.allowMultipleReasonTerms = allowMultipleReasonTerms;
	}
	
	/**
	 * Returns whether to allow single reason term.
	 * 
	 * @return whether to allow single reason term
	 */
	public Boolean getAllowSingleReasonTerm() {
		return this.allowSingleReasonTerm;
	}
	
	/**
	 * Sets whether to allow single reason term.
	 * 
	 * @param allowSingleReasonTerm whether to allow single reason term
	 */
	public void setAllowSingleReasonTerm(
			final Boolean allowSingleReasonTerm) {
		this.allowSingleReasonTerm = allowSingleReasonTerm;
	}
	
	/**
	 * Returns whether to associate multiple reason terms.
	 * 
	 * @return whether to associate multiple reason terms
	 */
	public Boolean getAssociateMultipleReasonTerms() {
		return this.associateMultipleReasonTerms;
	}

	/**
	 * Sets whether to associate multiple reason terms
	 * 
	 * @param associateMultipleReasonTerms whether to associate multiple reason
	 * terms
	 */
	public void setAssociateMultipleReasonTerms(
			final Boolean associateMultipleReasonTerms) {
		this.associateMultipleReasonTerms = associateMultipleReasonTerms;
	}

	/**
	 * Returns reason term items.
	 * 
	 * @return reason term items
	 */
	public List<LocationReasonTermItem> getReasonTermItems() {
		return this.reasonTermItems;
	}
	
	/**
	 * Sets reason term items.
	 * 
	 * @param reasonTermItems reason term items
	 */
	public void setReasonTermItems(
			final List<LocationReasonTermItem> reasonTermItems) {
		this.reasonTermItems = reasonTermItems;
	}
}