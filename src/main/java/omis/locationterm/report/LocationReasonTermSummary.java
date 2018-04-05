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
package omis.locationterm.report;

import java.io.Serializable;
import java.util.Date;

/**
 * Summary of location reason term.
 *
 * @author Stephen Abson
 * @version 0.0.1 (Feb 9, 2015)
 * @since OMIS 3.0
 */
public class LocationReasonTermSummary
		implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private final Long id;
	
	private final Long locationTermId;
	
	private final String offenderLastName;
	
	private final String offenderFirstName;
	
	private final String offenderMiddleName;
	
	private final Integer offenderNumber;
	
	private final String reasonName;
	
	private final Date startDate;
	
	private final Date endDate;
	
	private final Long dayCount;
	
	/**
	 * Instantiates a location reason term summary.
	 * 
	 * @param id ID
	 * @param locationTermId ID of location term
	 * @param offenderLastName last name of offender
	 * @param offenderFirstName first name of offender
	 * @param offenderMiddleName middle name of offender
	 * @param offenderNumber offender number
	 * @param reasonName reason name
	 * @param startDate start date
	 * @param endDate end date
	 * @param effectiveDate effective date
	 */
	public LocationReasonTermSummary(
			final Long id,
			final Long locationTermId,
			final String offenderLastName,
			final String offenderFirstName,
			final String offenderMiddleName,
			final Integer offenderNumber,
			final String reasonName,
			final Date startDate,
			final Date endDate,
			final Date effectiveDate) {
		this.id = id;
		this.locationTermId = locationTermId;
		this.offenderLastName = offenderLastName;
		this.offenderFirstName = offenderFirstName;
		this.offenderMiddleName = offenderMiddleName;
		this.offenderNumber = offenderNumber;
		this.reasonName = reasonName;
		this.startDate = startDate;
		this.endDate = endDate;
		final Date diffDate;
		final long dayLength = 24 * 60 * 60 * 1000;
		if (this.endDate != null) {
			diffDate = this.endDate;
		} else {
			diffDate = effectiveDate;
		}
		this.dayCount = (diffDate.getTime() / dayLength)
				- (this.startDate.getTime() / dayLength);
	}

	/**
	 * Returns ID of location reason term.
	 * 
	 * @return ID of location reason term
	 */
	public Long getId() {
		return this.id;
	}
	
	/**
	 * Returns ID of location term.
	 * 
	 * @return ID of location term
	 */
	public Long getLocationTermId() {
		return this.locationTermId;
	}
	
	/**
	 * Returns last name of offender.
	 * 
	 * @return last name of offender
	 */
	public String getOffenderLastName() {
		return this.offenderLastName;
	}
	
	/**
	 * Returns first name of offender.
	 * 
	 * @return first name of offender
	 */
	public String getOffenderFirstName() {
		return this.offenderFirstName;
	}
	
	/**
	 * Returns offender middle name.
	 * 
	 * @return offender middle name
	 */
	public String getOffenderMiddleName() {
		return this.offenderMiddleName;
	}
	
	/**
	 * Returns offender number.
	 * 
	 * @return offender number
	 */
	public Integer getOffenderNumber() {
		return this.offenderNumber;
	}
	
	/**
	 * Returns reason name.
	 * 
	 * @return reason name
	 */
	public String getReasonName() {
		return this.reasonName;
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
	 * Returns end date.
	 * 
	 * @return end date
	 */
	public Date getEndDate() {
		return this.endDate;
	}
	
	/**
	 * Returns day count.
	 * 
	 * @return day count
	 */
	public Long getDayCount() {
		return this.dayCount;
	}
}