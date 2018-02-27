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
package omis.paroleplan.domain.component;

import java.io.Serializable;

import omis.staff.domain.StaffAssignment;

/**
 * Evaluation component.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 13, 2018)
 * @since OMIS 3.0
 */
public class Evaluation implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private StaffAssignment staffAssignment;
	
	private String description;
	
	public Evaluation() {
		// Default constructor
	}
	
	public Evaluation(final StaffAssignment staffAssignment, 
			final String description) {
		this.staffAssignment = staffAssignment;
		this.description = description;
	}

	/**
	 * Returns the staffAssignment.
	 *
	 * @return staffAssignment
	 */
	public StaffAssignment getStaffAssignment() {
		return staffAssignment;
	}

	/**
	 * Sets the staff assignment.
	 *
	 * @param staffAssignment staff assignment
	 */
	public void setStaffAssignment(final StaffAssignment staffAssignment) {
		this.staffAssignment = staffAssignment;
	}

	/**
	 * Returns the description.
	 *
	 * @return description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description description
	 */
	public void setDescription(final String description) {
		this.description = description;
	}
}
