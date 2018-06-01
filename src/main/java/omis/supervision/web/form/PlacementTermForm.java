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
import java.util.List;

import omis.region.domain.State;
import omis.supervision.domain.CorrectionalStatus;
import omis.supervision.domain.PlacementStatus;
import omis.supervision.domain.PlacementTermChangeReason;
import omis.supervision.domain.SupervisoryOrganization;

/**
 * Form for capturing placement term information.
 * 
 * @author Stephen Abson
 * @author Jason Nelson
 * @version 0.1.0 (Oct 15, 2013)
 * @since OMIS 3.0
 */
public class PlacementTermForm
		implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<PlacementTermNoteItem> noteItems;
	
	private State state;
	
	private SupervisoryOrganization supervisoryOrganization;

	private CorrectionalStatus correctionalStatus;

	private Date startDate;
	
	private Date startTime;

	private PlacementTermChangeReason startChangeReason;
	
	private PlacementStatus status;
	
	private Date statusDate;
	
	private Date statusTime;
	
	private Boolean returned;
	
	private Date statusReturnedDate;
	
	private Date statusReturnedTime;

	private Date endDate;
	
	private Date endTime;

	private PlacementTermChangeReason endChangeReason;
	
	private Boolean sendToLocation;
	
	private boolean allowCorrectionalStatus;
	
	private boolean allowState;
	
	private boolean allowSupervisoryOrganization;
	
	private boolean allowStartDate;
	
	private boolean allowStartTime;
	
	private boolean allowStatusFields;
	
	private boolean allowEndChangeReason;
	
	private boolean allowSendToLocation;

	/** Instantiates a default form for placement terms. */
	public PlacementTermForm() {
		// Default instantiation
	}

	/**
	 * Sets note items.
	 * 
	 * @param noteItems note items
	 */
	public void setNoteItems(final List<PlacementTermNoteItem> noteItems) {
		this.noteItems = noteItems;
	}
	
	/**
	 * Returns note items.
	 * 
	 * @return note items
	 */
	public List<PlacementTermNoteItem> getNoteItems() {
		return this.noteItems;
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
	 * Returns the supervisory organization.
	 * 
	 * @return supervisory organization
	 */
	public SupervisoryOrganization getSupervisoryOrganization() {
		return this.supervisoryOrganization;
	}

	/**
	 * Sets the supervisory organization.
	 * 
	 * @param supervisoryOrganization supervisory organization
	 */
	public void setSupervisoryOrganization(
			final SupervisoryOrganization supervisoryOrganization) {
		this.supervisoryOrganization = supervisoryOrganization;
	}

	/**
	 * Returns the correctional status.
	 * 
	 * @return correctional status
	 */
	public CorrectionalStatus getCorrectionalStatus() {
		return this.correctionalStatus;
	}

	/**
	 * Sets the correctional status term.
	 * 
	 * @param correctionalStatus correctional status term
	 */
	public void setCorrectionalStatus(
			final CorrectionalStatus correctionalStatus) {
		this.correctionalStatus = correctionalStatus;
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
	 * Returns the start change reason.
	 * 
	 * @return start change reason
	 */
	public PlacementTermChangeReason getStartChangeReason() {
		return this.startChangeReason;
	}

	/**
	 * Sets the start change reason.
	 * 
	 * @param startChangeReason start change reason
	 */
	public void setStartChangeReason(
			final PlacementTermChangeReason startChangeReason) {
		this.startChangeReason = startChangeReason;
	}
	
	/**
	 * Returns status.
	 * 
	 * @return status
	 */
	public PlacementStatus getStatus() {
		return this.status;
	}
	
	/**
	 * Sets status.
	 * 
	 * @param status status
	 */
	public void setStatus(final PlacementStatus status) {
		this.status = status;
	}

	/**
	 * Returns status date.
	 * 
	 * @return status date
	 */
	public Date getStatusDate() {
		return this.statusDate;
	}
	
	/**
	 * Sets status date.
	 * 
	 * @param statusDate status date
	 */
	public void setStatusDate(final Date statusDate) {
		this.statusDate = statusDate;
	}
	
	/**
	 * Returns status time.
	 * 
	 * @return status time
	 */
	public Date getStatusTime() {
		return this.statusTime;
	}
	
	/**
	 * Sets status time.
	 * 
	 * @param statusTime status time
	 */
	public void setStatusTime(final Date statusTime) {
		this.statusTime = statusTime;
	}
	
	/**
	 * Sets whether returned from not placed status.
	 * 
	 * @return whether returned from not placed status
	 */
	public Boolean getReturned() {
		return this.returned;
	}
	
	/**
	 * Returns whether returned from not placed status.
	 * 
	 * @param returned whether returned from not placed status
	 */
	public void setReturned(final Boolean returned) {
		this.returned = returned;
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
	 * Returns the end change reason.
	 * 
	 * @return end change reason
	 */
	public PlacementTermChangeReason getEndChangeReason() {
		return this.endChangeReason;
	}

	/**
	 * Sets the end change reason.
	 * 
	 * @param endChangeReason end change reason
	 */
	public void setEndChangeReason(
			final PlacementTermChangeReason endChangeReason) {
		this.endChangeReason = endChangeReason;
	}

	/**
	 * Returns the status returned date.
	 * 
	 * @return status returned date
	 */
	public Date getStatusReturnedDate() {
		return statusReturnedDate;
	}

	/**
	 * Sets the status returned date.
	 * 
	 * @param statusReturnedDate status returned date
	 */
	public void setStatusReturnedDate(final Date statusReturnedDate) {
		this.statusReturnedDate = statusReturnedDate;
	}

	/**
	 * Returns the status returned time.
	 * 
	 * @return status returned time
	 */
	public Date getStatusReturnedTime() {
		return statusReturnedTime;
	}

	/**
	 * Sets the status returned time.
	 * 
	 * @param statusReturnedTime status returned time
	 */
	public void setStatusReturnedTime(Date statusReturnedTime) {
		this.statusReturnedTime = statusReturnedTime;
	}
	
	/**
	 * Returns whether to send to location.
	 * 
	 * @return whether to send to location
	 */
	public Boolean getSendToLocation() {
		return this.sendToLocation;
	}

	/**
	 * Sets whether to send to location.
	 * 
	 * @param sendToLocation whether to send to location
	 */
	public void setSendToLocation(final Boolean sendToLocation) {
		this.sendToLocation = sendToLocation;
	}
	
	/**
	 * Returns whether correctional status is allowed.
	 * 
	 * @return allow correctional status
	 */
	public boolean getAllowCorrectionalStatus() {
		return allowCorrectionalStatus;
	}

	/**
	 * Sets whether correctional status is allowed.
	 * 
	 * @param allowCorrectionalStatus allow correctional status
	 */
	public void setAllowCorrectionalStatus(boolean allowCorrectionalStatus) {
		this.allowCorrectionalStatus = allowCorrectionalStatus;
	}
	
	/**
	 * Returns whether State is allowed.
	 * 
	 * @return whether State is allowed
	 */
	public boolean getAllowState() {
		return this.allowState;
	}
	
	/**
	 * Sets whether State is allowed.
	 * 
	 * @param allowState whether State is allowed
	 */
	public void setAllowState(final boolean allowState) {
		this.allowState = allowState;
	}
	
	/**
	 * Returns whether supervisory organization is allowed.
	 * 
	 * @return whether supervisory organization is allowed
	 */
	public boolean getAllowSupervisoryOrganization() {
		return this.allowSupervisoryOrganization;
	}
	
	/**
	 * Sets whether supervisory organization is allowed.
	 * 
	 * @param allowSupervisoryOrganization whether supervisory organization
	 * is allowed
	 */
	public void setAllowSupervisoryOrganization(
			final boolean allowSupervisoryOrganization) {
		this.allowSupervisoryOrganization = allowSupervisoryOrganization;
	}
	
	/**
	 * Returns whether to allow start date.
	 * 
	 * @return whether to allow start date
	 */
	public boolean getAllowStartDate() {
		return this.allowStartDate;
	}
	
	/**
	 * Sets whether to allow start date.
	 * 
	 * @param allowStartDate whether to allow start date
	 */
	public void setAllowStartDate(final boolean allowStartDate) {
		this.allowStartDate = allowStartDate;
	}
	
	/**
	 * Returns whether to allow start time.
	 * 
	 * @return whether to allow start time
	 */
	public boolean getAllowStartTime() {
		return this.allowStartTime;
	}
	
	/**
	 * Sets whether to allow start time.
	 * 
	 * @param allowStartTime whether to allow start time
	 */
	public void setAllowStartTime(final boolean allowStartTime) {
		this.allowStartTime = allowStartTime;
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
	 * Returns whether status fields are allowed.
	 * 
	 * @return whether status fields are allowed
	 */
	public boolean getAllowStatusFields() {
		return this.allowStatusFields;
	}
	
	/**
	 * Sets whether status fields are allowed.
	 * 
	 * @param allowStatusFields whether status fields are allowed
	 */
	public void setAllowStatusFields(final boolean allowStatusFields) {
		this.allowStatusFields = allowStatusFields;
	}

	/**
	 * Returns whether sending to location is allowed.
	 * 
	 * @return whether sending to location is allowed
	 */
	public boolean getAllowSendToLocation() {
		return this.allowSendToLocation;
	}

	/**
	 * Sets whether sending to location is allowed.
	 * 
	 * @param allowSendToLocation whether sending to location is allowed
	 */
	public void setAllowSendToLocation(final boolean allowSendToLocation) {
		this.allowSendToLocation = allowSendToLocation;
	}
}