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
package omis.caseload.report;

import java.io.Serializable;
import java.util.Date;

/**
 * Officer case assignment summary.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Jun 14, 2018)
 * @since OMIS 3.0
 */
public class OfficerCaseAssignmentSummary implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private final Long officerCaseAssignmentId;
	
	private final Date startDate;
	
	private final Date endDate;
	
	private final String officerLastName;
	
	private final String officerFirstName;
	
	private final String officerMiddleName;
	
	private final String officerUsername;
	
	private final String offenderLastName;
	
	private final String offenderMiddleName;
	
	private final String offenderFirstName;
	
	private final Integer offenderNumber;
	
	private final String locationOrganizationName;
	
	private final String supervisionLevelCategoryDescription;

	/**
	 * Initializes a officer case assignment summary.
	 * 
	 * @param officerCaseAssignmentId officer case assignment id
	 * @param startDate start date
	 * @param endDate end date
	 * @param officerLastName officer last name
	 * @param officerFirstName officer first name
	 * @param officerMiddleName officer middle name
	 * @param officerUsername officer user name
	 * @param offenderLastName offender last name
	 * @param offenderMiddleName offender middle name
	 * @param offenderFirstName offender first name
	 * @param offenderNumber offender number
	 * @param locationOrganizationName location organization name
	 * @param supervisionLevelCategoryDescription supervision level category 
	 * description
	 */
	public OfficerCaseAssignmentSummary(final Long officerCaseAssignmentId, 
			final Date startDate, final Date endDate, 
			final String officerLastName, final String officerFirstName, 
			final String officerMiddleName, final String officerUsername, 
			final String offenderLastName, final String offenderMiddleName, 
			final String offenderFirstName, final Integer offenderNumber, 
			final String locationOrganizationName,
			final String supervisionLevelCategoryDescription) {
		this.officerCaseAssignmentId = officerCaseAssignmentId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.officerLastName = officerLastName;
		this.officerFirstName = officerFirstName;
		this.officerMiddleName = officerMiddleName;
		this.officerUsername = officerUsername;
		this.offenderLastName = offenderLastName;
		this.offenderMiddleName = offenderMiddleName;
		this.offenderFirstName = offenderFirstName;
		this.offenderNumber = offenderNumber;
		this.locationOrganizationName = locationOrganizationName;
		this.supervisionLevelCategoryDescription = 
				supervisionLevelCategoryDescription;
	}
	
	/**
	 * Returns the officer case assignment id.
	 *
	 * @return officer case assignment id
	 */
	public Long getOfficerCaseAssignmentId() {
		return officerCaseAssignmentId;
	}

	/**
	 * Returns the start date.
	 *
	 * @return start date
	 */
	public Date getStartDate() {
		return startDate;
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
	 * Returns the officer last name.
	 *
	 * @return officer last name
	 */
	public String getOfficerLastName() {
		return officerLastName;
	}

	/**
	 * Returns the officer first name.
	 *
	 * @return officer first name
	 */
	public String getOfficerFirstName() {
		return officerFirstName;
	}

	/**
	 * Returns the officer middle name.
	 *
	 * @return officer middle name
	 */
	public String getOfficerMiddleName() {
		return officerMiddleName;
	}

	/**
	 * Returns the officer user name.
	 *
	 * @return officer user name
	 */
	public String getOfficerUsername() {
		return officerUsername;
	}

	/**
	 * Returns the offender last name.
	 *
	 * @return offender last name
	 */
	public String getOffenderLastName() {
		return offenderLastName;
	}

	/**
	 * Returns the offender middle name.
	 *
	 * @return offender middle name
	 */
	public String getOffenderMiddleName() {
		return offenderMiddleName;
	}

	/**
	 * Returns the offender first name.
	 *
	 * @return offender first name
	 */
	public String getOffenderFirstName() {
		return offenderFirstName;
	}

	/**
	 * Returns the offender number.
	 *
	 * @return offender number
	 */
	public Integer getOffenderNumber() {
		return offenderNumber;
	}

	/**
	 * Returns the location organization name.
	 *
	 * @return location organization name
	 */
	public String getLocationOrganizationName() {
		return locationOrganizationName;
	}

	/**
	 * Returns the supervision level category description.
	 *
	 * @return supervision level category description
	 */
	public String getSupervisionLevelCategoryDescription() {
		return supervisionLevelCategoryDescription;
	}
}