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
package omis.caseload.service;

import java.util.Date;
import java.util.List;

import omis.caseload.domain.OfficerCaseAssignment;
import omis.caseload.domain.SupervisionLevelCategory;
import omis.exception.DateConflictException;
import omis.exception.DuplicateEntityFoundException;
import omis.location.domain.Location;
import omis.offender.domain.Offender;
import omis.user.domain.UserAccount;

/**
 * Officer case assignment service.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Jun 12, 2018)
 * @since OMIS 3.0
 */
public interface OfficerCaseAssignmentService {

	/**
	 * Creates a new officer case assignment.
	 * 
	 * @param offender offender
	 * @param officer officer
	 * @param startDate start date
	 * @param endDate end date
	 * @param supervisionOffice supervision office
	 * @param supervisionLevel supervision level category 
	 * @return officer case assignment
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 * @throws DateConflictException if date conflict exists
	 */
	OfficerCaseAssignment createOfficerCaseAssignment(Offender offender, 
			UserAccount officer, Date startDate, Date endDate, 
			Location supervisionOffice, 
			SupervisionLevelCategory supervisionLevel) 
					throws DuplicateEntityFoundException, DateConflictException;
	
	/**
	 * Updates an existing officer case assignment.
	 * 
	 * @param officerCaseAssignment officer case assignment
	 * @param officer officer
	 * @param startDate start date
	 * @param endDate end date
	 * @param supervisionOffice supervision office
	 * @param supervisionLevel supervision level category
	 * @return officer case assignment
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 * @throws DateConflictException if date conflict exists
	 */
	OfficerCaseAssignment updateOfficerCaseAssignment(
			OfficerCaseAssignment officerCaseAssignment, UserAccount officer, 
			Date startDate, Date endDate, Location supervisionOffice,
			SupervisionLevelCategory supervisionLevel) 
					throws DuplicateEntityFoundException, DateConflictException;
	
	/**
	 * Removes the specified officer case assignment.
	 * 
	 * @param officerCaseAssignment officer case assignment
	 */
	void removeOfficerCaseAssignment(
			OfficerCaseAssignment officerCaseAssignment);
	
	/**
	 * Returns a list of supervision level categories.
	 * 
	 * @return list of supervision level categories
	 */
	List<SupervisionLevelCategory> findSupervisionLevelCategories();
	
	/**
	 * Returns a list of supervisory organization locations.
	 * 
	 * @return list of supervisory organization locations
	 */
	List<Location> findSupervisoryOrganizationLocations();

	/**
	 * Returns the user account for the specified user name.
	 * 
	 * @param username user name
	 * @return user account
	 */
	UserAccount findUserAccountByUsername(String username);
}