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

import omis.datatype.DateRange;

/**
 * Summary of location term. 
 *
 * @author Stephen Abson
 * @version 0.0.1 (Nov 17, 2014)
 * @since OMIS 3.0
 */
public class LocationTermSummary
		implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private final Long id;
	
	private final String offenderLastName;
	
	private final String offenderFirstName;
	
	private final String offenderMiddleName;
	
	private final String offenderSuffix;
	
	private final Integer offenderNumber;
	
	private final String locationName;
	
	private final Date startDate;
	
	private final Date endDate;
	
	private final Long dayCount;
	
	private final Long reasonCount;
	
	private final String singleReasonName;
	
	/**
	 * Instantiates summary of location term.
	 * 
	 * @param id ID
	 * @param offenderLastName last name of offender
	 * @param offenderFirstName first name of offender
	 * @param offenderMiddleName middle name of offender
	 * @param offenderSuffix suffix
	 * @param offenderNumber offender number
	 * @param locationName name of location
	 * @param startDate start date of location term
	 * @param endDate end date of location term
	 * @param reasonCount reason count
	 * @param singleReasonName single reason name
	 * @param effectiveDate effective date
	 */
	public LocationTermSummary(
			final Long id,
			final String offenderLastName,
			final String offenderFirstName,
			final String offenderMiddleName,
			final String offenderSuffix,
			final Integer offenderNumber,
			final String locationName,
			final Date startDate,
			final Date endDate,
			final Long reasonCount,
			final String singleReasonName,
			final Date effectiveDate) {
		this.id = id;
		this.offenderLastName = offenderLastName;
		this.offenderFirstName = offenderFirstName;
		this.offenderMiddleName = offenderMiddleName;
		this.offenderSuffix = offenderSuffix;
		this.offenderNumber = offenderNumber;
		this.locationName = locationName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.reasonCount = reasonCount;
		this.singleReasonName = singleReasonName;
		final Date diffDate;
		if (this.endDate != null) {
			diffDate = this.endDate;
		} else {
			diffDate = effectiveDate;
		}
		this.dayCount = DateRange.countDaysInclusively(startDate, diffDate);
	}
	
	/**
	 * Returns ID.
	 * 
	 * @return ID
	 */
	public Long getId() {
		return this.id;
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
	 * Returns middle name of offender.
	 * 
	 * @return middle name of offender
	 */
	public String getOffenderMiddleName() {
		return this.offenderMiddleName;
	}
	
	/**
	 * Returns offender suffix.
	 * 
	 * @return offender suffix
	 */
	public String getSuffixName() {
		return this.offenderSuffix;
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
	 * Returns name of location.
	 * 
	 * @return name of location
	 */
	public String getLocationName() {
		return this.locationName;
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
	 * Returns count of days.
	 * 
	 * @return count of days
	 */
	public Long getDayCount() {
		return this.dayCount;
	}
	
	/**
	 * Returns count of reasons for location term.
	 * 
	 * @return count of reasons for location term
	 */
	public Long getReasonCount() {
		return this.reasonCount;
	}
	
	/**
	 * Returns name of single reason with matching date range.
	 * 
	 * <p>Returns {@code null} if there is anything other than a single reason
	 * for the location term with matching date range. Check
	 * {@code this#getReasonCount()} for number of reasons for the location
	 * term.
	 * 
	 * @return name of single reason with matching date range
	 */
	public String getSingleReasonName() {
		return this.singleReasonName;
	}
}