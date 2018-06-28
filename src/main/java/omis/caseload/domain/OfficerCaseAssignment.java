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
package omis.caseload.domain;

import omis.audit.domain.Creatable;
import omis.audit.domain.Updatable;
import omis.datatype.DateRange;
import omis.location.domain.Location;
import omis.offender.domain.Offender;
import omis.user.domain.UserAccount;

/**
 * Officer case assignment.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Jun 12, 2018)
 * @since OMIS 3.0
 */
public interface OfficerCaseAssignment extends Creatable, Updatable {

	/**
	 * Sets the ID of the officer case assignment.
	 * 
	 * @param id id
	 */
	void setId(Long id);
	
	/**
	 * Returns the ID of the officer case assignment.
	 * 
	 * @return ID
	 */
	Long getId();
	
	/**
	 * Sets the offender of the officer case assignment.
	 * 
	 * @param offender offender 
	 */
	void setOffender(Offender offender);
	
	/**
	 * Returns the offender of the officer case assignment.
	 * 
	 * @return offender
	 */
	Offender getOffender();
	
	/**
	 * Sets the officer of the officer case assignment.
	 * 
	 * @param officer officer
	 */
	void setOfficer(UserAccount officer);
	
	/**
	 * Returns the officer of the officer case assignment.
	 * 
	 * @return officer
	 */
	UserAccount getOfficer();
	
	/**
	 * Sets the date range of the officer case assignment.
	 * 
	 * @param dateRange date range
	 */
	void setDateRange(DateRange dateRange);
	
	/**
	 * Returns the date range of the officer case assignment.
	 * 
	 * @return date range
	 */
	DateRange getDateRange();
	
	/**
	 * Sets the supervision office of the officer case assignment.
	 * 
	 * @param supervisionOffice supervision office
	 */
	void setSupervisionOffice(Location supervisionOffice);
	
	/**
	 * Returns the supervision office of the officer case assignment.
	 * 
	 * @return supervision office
	 */
	Location getSupervisionOffice();
	
	/**
	 * Sets the supervision level of the officer case assignment.
	 * 
	 * @param supervisionLevel supervision level
	 */
	void setSupervisionLevel(SupervisionLevelCategory supervisionLevel);
	
	/**
	 * Returns the supervision level of the officer case assignment.
	 * 
	 * @return supervision level
	 */
	SupervisionLevelCategory getSupervisionLevel();
}