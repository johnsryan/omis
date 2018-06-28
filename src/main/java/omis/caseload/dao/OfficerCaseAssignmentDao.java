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
package omis.caseload.dao;

import java.util.Date;
import java.util.List;

import omis.caseload.domain.OfficerCaseAssignment;
import omis.dao.GenericDao;
import omis.offender.domain.Offender;
import omis.user.domain.UserAccount;

/**
 * Data access object for officer case assignment.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Jun 13, 2018)
 * @since OMIS 3.0
 */
public interface OfficerCaseAssignmentDao 
		extends GenericDao<OfficerCaseAssignment> {

	/**
	 * Returns the officer case assignment for the specified parameters.
	 * 
	 * @param offender offender
	 * @param officer officer
	 * @param startDate start date
	 * @param endDate end date
	 * @return officer case assignment
	 */
	OfficerCaseAssignment find(Offender offender, UserAccount officer, 
			Date startDate, Date endDate);
	
	/**
	 * Returns the officer case assignment for the specified parameters 
	 * excluding the specified officer case assignment.
	 * 
	 * @param offender offender
	 * @param officer officer
	 * @param startDate start date
	 * @param endDate end date
	 * @param excludedOfficerCaseAssignment excluded officer case assignment
	 * @return officer case assignment
	 */
	OfficerCaseAssignment findExcluding(Offender offender, UserAccount officer, 
			Date startDate, Date endDate, 
			OfficerCaseAssignment excludedOfficerCaseAssignment);
	
	/**
	 * Returns a list of officer case assignments for the specified offender 
	 * within the specified date range.
	 * 
	 * @param offender offender
	 * @param startDate start date
	 * @param endDate end date
	 * @return list of officer case assignments
	 */
	List<OfficerCaseAssignment> findWithinDateRange(Offender offender, 
			Date startDate, Date endDate);
	
	/**
	 * Returns a list of officer case assignments for the specified offender 
	 * within the specified date range excluding the specified officer case 
	 * assignment.
	 * 
	 * @param offender offender
	 * @param startDate start date
	 * @param endDate end date
	 * @param excludedOfficerCaseAssignment excluded officer case assignment
	 * @return list of officer case assignments
	 */
	List<OfficerCaseAssignment> findWithinDateRangeExcluding(Offender offender, 
			Date startDate, Date endDate, 
			OfficerCaseAssignment excludedOfficerCaseAssignment);
}