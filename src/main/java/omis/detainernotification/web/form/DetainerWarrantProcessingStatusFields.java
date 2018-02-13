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
package omis.detainernotification.web.form;

import java.io.Serializable;
import java.util.Date;

import omis.detainernotification.domain.component.DetainerWarrantCancellation;
import omis.facility.domain.Complex;
import omis.facility.domain.Facility;
import omis.facility.domain.Unit;

/**
 * DetainerWarrantProcessingStatusFields.java
 * 
 * @author Annie Jacques 
 * @author Joel Norris
 * @author Josh Divine
 * @version 0.1.2 (Feb 12, 2018)
 * @since OMIS 3.0
 *
 */
public class DetainerWarrantProcessingStatusFields implements Serializable {
	private static final long serialVersionUID = 1L;
	private Date sentToFacilityDate;
	private Facility facility;
	private String facilityName;
	private Boolean otherFacility;
	private Unit unit;
	private Complex complex;
	private Date inmateServedDate;
	private Boolean refusedToSign;
	private Boolean waiverRequired;
	private DetainerWarrantCancellation cancellation;
	private String refusedToSignComment;
	private String waiverRequiredComment;
	
	/**
	 * Default constructor for detainer warrant processing status fields
	 */
	public DetainerWarrantProcessingStatusFields(){
		//nothing
	}
	
	
	/**
	 * Constructor for detainer warrant processing status fields
	 * @param sentToFacilityDate - sent to facility date
	 * @param facility - facility
	 * @param facilityName facility name
	 * @param otherFacility other facility
	 * @param unit - unit
	 * @param inmateServedDate - inmate served date
	 * @param refusedToSign - refused to sign
	 * @param waiverRequired - waiver required
	 * @param cancellation - cancellation
	 * @param comments - comments
	 */
	public DetainerWarrantProcessingStatusFields(
			final Date sentToFacilityDate, final Facility facility,
			final Unit unit, final Complex complex,
			final String facilityName, final Boolean otherFacility,
			final Date inmateServedDate,
			final Boolean refusedToSign, final Boolean waiverRequired,
			final DetainerWarrantCancellation cancellation,
			final String refusedToSignComment,
			final String waiverRequiredComment) {
		this.sentToFacilityDate = sentToFacilityDate;
		this.facility = facility;
		this.facilityName = facilityName;
		this.otherFacility = otherFacility;
		this.unit = unit;
		this.complex = complex;
		this.inmateServedDate = inmateServedDate;
		this.refusedToSign = refusedToSign;
		this.waiverRequired = waiverRequired;
		this.cancellation = cancellation;
		this.refusedToSignComment = refusedToSignComment;
		this.waiverRequiredComment = waiverRequiredComment;
	}


	/**
	 * Returns sent to facility date of detainer warrant processing fields
	 * @return the sentToFacilityDate
	 */
	public Date getSentToFacilityDate() {
		return this.sentToFacilityDate;
	}


	/**
	 * Sets sent to facility date of detainer warrant processing fields
	 * @param sentToFacilityDate the sentToFacilityDate to set
	 */
	public void setSentToFacilityDate(final Date sentToFacilityDate) {
		this.sentToFacilityDate = sentToFacilityDate;
	}


	/**
	 * Returns facility of detainer warrant processing fields
	 * @return the facility
	 */
	public Facility getFacility() {
		return this.facility;
	}


	/**
	 * Sets facility of detainer warrant processing fields
	 * @param facility the facility to set
	 */
	public void setFacility(final Facility facility) {
		this.facility = facility;
	}

	/**
	 * Returns the facility name.
	 * 
	 * @return facility name
	 */
	public String getFacilityName() {
		return this.facilityName;
	}

	/**
	 * Sets the facility name.
	 * 
	 * @param facilityName facility name
	 */
	public void setFacilityName(final String facilityName) {
		this.facilityName = facilityName;
	}

	/**
	 * Returns whether other facility applies.
	 * 
	 * @return other facility
	 */
	public Boolean getOtherFacility() {
		return this.otherFacility;
	}

	/**
	 * Sets whether other facility applies.
	 * 
	 * @param otherFacility other facility
	 */
	public void setOtherFacility(final Boolean otherFacility) {
		this.otherFacility = otherFacility;
	}

	/**
	 * Returns the unit.
	 * 
	 * @return unit
	 */
	public Unit getUnit() {
		return this.unit;
	}

	/**
	 * Sets the unit.
	 * 
	 * @param unit unit
	 */
	public void setUnit(final Unit unit) {
		this.unit = unit;
	}

	/**
	 * Returns the complex.
	 * 
	 * @return complex
	 */
	public Complex getComplex() {
		return this.complex;
	}

	/**
	 * Sets the complex.
	 * 
	 * @param complex complex
	 */
	public void setComplex(final Complex complex) {
		this.complex = complex;
	}


	/**
	 * Returns inmate served date of detainer warrant processing fields
	 * @return the inmateServedDate
	 */
	public Date getInmateServedDate() {
		return this.inmateServedDate;
	}


	/**
	 * Sets inmate served date of detainer warrant processing fields
	 * @param inmateServedDate the inmateServedDate to set
	 */
	public void setInmateServedDate(final Date inmateServedDate) {
		this.inmateServedDate = inmateServedDate;
	}


	/**
	 * Returns refused to sign of detainer warrant processing fields
	 * @return the refusedToSign - Boolean
	 */
	public Boolean getRefusedToSign() {
		return this.refusedToSign;
	}


	/**
	 * Sets refused to sign of detainer warrant processing fields
	 * @param refusedToSign the refusedToSign to set - Boolean
	 */
	public void setRefusedToSign(final Boolean refusedToSign) {
		this.refusedToSign = refusedToSign;
	}


	/**
	 * Returns waiver required of detainer warrant processing fields
	 * @return the waiverRequired - Boolean
	 */
	public Boolean getWaiverRequired() {
		return this.waiverRequired;
	}


	/**
	 * Sets waiver required of of detainer warrant processing fields
	 * @param waiverRequired the waiverRequired to set - Boolean
	 */
	public void setWaiverRequired(final Boolean waiverRequired) {
		this.waiverRequired = waiverRequired;
	}


	/**
	 * Returns cancellation of detainer warrant processing fields
	 * @return the cancellation
	 */
	public DetainerWarrantCancellation getCancellation() {
		return this.cancellation;
	}


	/**
	 * Sets cancellation of detainer warrant processing fields 
	 * @param cancellation the cancellation to set
	 */
	public void setCancellation(
			final DetainerWarrantCancellation cancellation) {
		this.cancellation = cancellation;
	}

	/**
	 * Returns the refused to sign comment.
	 * 
	 * @return refused to sign comment
	 */
	public String getRefusedToSignComment() {
		return this.refusedToSignComment;
	}


	/**
	 * Sets the refused to sign comment.
	 * 
	 * @param refusedToSignComment refused to sign comment
	 */
	public void setRefusedToSignComment(final String refusedToSignComment) {
		this.refusedToSignComment = refusedToSignComment;
	}

	/**
	 * Returns the waiver required comment.
	 * 
	 * @return waiver required comment
	 */
	public String getWaiverRequiredComment() {
		return this.waiverRequiredComment;
	}

	/**
	 * Sets the waiver required comment.
	 * 
	 * @param waiverRequiredComment waiver required comment
	 */
	public void setWaiverRequiredComment(final String waiverRequiredComment) {
		this.waiverRequiredComment = waiverRequiredComment;
	}
}