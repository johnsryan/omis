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
package omis.caseload.web.form;

import java.io.Serializable;
import java.util.Date;

import omis.caseload.domain.SupervisionLevelCategory;
import omis.location.domain.Location;
import omis.offender.domain.Offender;
import omis.user.domain.UserAccount;

/**
 * Officer case assignment form.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Jun 14, 2018)
 * @since OMIS 3.0
 */
public class OfficerCaseAssignmentForm implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Offender selectedOffender;
	
	private UserAccount officer;
	
	private Date startDate;
	
	private Date endDate;
	
	private SupervisionLevelCategory supervisionLevelCategory;
	
	private Location location;
	
	/**
	 * Instantiates a default officer case assignment form. 
	 */
	public OfficerCaseAssignmentForm() {
		//Default constructor.
	}

	/**
	 * Returns the offender.
	 *
	 * @return offender
	 */
	public Offender getSelectedOffender() {
		return selectedOffender;
	}

	/**
	 * Sets the offender.
	 *
	 * @param offender offender
	 */
	public void setSelectedOffender(final Offender offender) {
		this.selectedOffender = offender;
	}

	/**
	 * Returns the officer.
	 *
	 * @return officer
	 */
	public UserAccount getOfficer() {
		return officer;
	}

	/**
	 * Sets the officer.
	 *
	 * @param officer officer
	 */
	public void setOfficer(final UserAccount officer) {
		this.officer = officer;
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
	 * Sets the start date.
	 *
	 * @param startDate start date
	 */
	public void setStartDate(final Date startDate) {
		this.startDate = startDate;
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
	public void setEndDate(final Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * Returns the supervision level category.
	 *
	 * @return supervision level category
	 */
	public SupervisionLevelCategory getSupervisionLevelCategory() {
		return supervisionLevelCategory;
	}

	/**
	 * Sets the supervision level category.
	 *
	 * @param supervisionLevelCategory supervision level category
	 */
	public void setSupervisionLevelCategory(
			final SupervisionLevelCategory supervisionLevelCategory) {
		this.supervisionLevelCategory = supervisionLevelCategory;
	}

	/**
	 * Returns the location.
	 *
	 * @return location
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * Sets the location.
	 *
	 * @param location location
	 */
	public void setLocation(final Location location) {
		this.location = location;
	}

}