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
package omis.parolereview.report;

import java.io.Serializable;
import java.util.Date;

/**
 * Parole review summary.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Jan 29, 2018)
 * @since OMIS 3.0
 */
public class ParoleReviewSummary implements Serializable {

	private static final long serialVersionUID = 1L;

	private final Long id;
	
	private final String staffMemberLastName;
	
	private final String staffMemberFirstName;
	
	private final String staffMemberMiddleName;
	
	private final String staffTitle;
	
	private final Date date;
	
	private final String endorsementName;
	
	private final String staffCategoryName;
	
	/**
	 * Instantiates an implementation of parole review summary.
	 * 
	 * @param id parole review id
	 * @param staffMemberLastName staff member last name
	 * @param staffMemberFirstName staff member first name
	 * @param staffMemberMiddleName staff member middle name
	 * @param staffTitle staff title
	 * @param date date
	 */
	public ParoleReviewSummary(final Long id,
			final String staffMemberLastName,
			final String staffMemberFirstName,
			final String staffMemberMiddleName,
			final String staffTitle,
			final Date date,
			final String endorsementName,
			final String staffCategoryName) {
		this.id = id;
		this.staffMemberFirstName = staffMemberFirstName;
		this.staffMemberLastName = staffMemberLastName;
		this.staffMemberMiddleName = staffMemberMiddleName;
		this.staffTitle = staffTitle;
		this.date = date;
		this.endorsementName = endorsementName;
		this.staffCategoryName = staffCategoryName;
	}

	/**
	 * Returns the id.
	 *
	 * @return id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Returns the staff member last name.
	 *
	 * @return staff member last name
	 */
	public String getStaffMemberLastName() {
		return staffMemberLastName;
	}

	/**
	 * Returns the staff member first name.
	 *
	 * @return staff member first name
	 */
	public String getStaffMemberFirstName() {
		return staffMemberFirstName;
	}

	/**
	 * Returns the staff member middle name.
	 *
	 * @return staff member middle name
	 */
	public String getStaffMemberMiddleName() {
		return staffMemberMiddleName;
	}

	/**
	 * Returns the staff title.
	 *
	 * @return staff title
	 */
	public String getStaffTitle() {
		return staffTitle;
	}

	/**
	 * Returns the date.
	 *
	 * @return date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Returns the endorsement name.
	 *
	 * @return endorsement name
	 */
	public String getEndorsementName() {
		return endorsementName;
	}

	/**
	 * Returns the staff category name.
	 *
	 * @return staff category name
	 */
	public String getStaffCategoryName() {
		return staffCategoryName;
	}
}