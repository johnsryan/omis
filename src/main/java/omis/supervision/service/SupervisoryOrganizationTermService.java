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
package omis.supervision.service;

import java.util.Date;
import java.util.List;

import omis.datatype.DateRange;
import omis.exception.DateRangeOutOfBoundsException;
import omis.offender.domain.Offender;
import omis.supervision.domain.SupervisoryOrganization;
import omis.supervision.domain.SupervisoryOrganizationTerm;
import omis.supervision.exception.PlacementTermExistsException;
import omis.supervision.exception.SupervisoryOrganizationTermConflictException;
import omis.supervision.exception.SupervisoryOrganizationTermExistsException;

/**
 * Service for supervisory organization terms.
 * 
 * @author Jason Nelson
 * @author Stephen Abson
 * @version 0.1.1 (Jan 10, 2013)
 * @since OMIS 3.0
 */
public interface SupervisoryOrganizationTermService {
	
	/**
	 * Returns all supervisory organization terms for the specified offender.
	 * 
	 * @param offender offender
	 * @return all supervisory organization terms for offender
	 */
	List<SupervisoryOrganizationTerm> findByOffender(Offender offender);
	
	/**
	 * Returns the supervisory organization term for the offender on the
	 * specified date.
	 * 
	 * @param offender offender
	 * @param date date
	 * @return supervisory organization term for offender on date
	 */
	SupervisoryOrganizationTerm findByOffenderOnDate(Offender offender,
			Date date);
	
	/**
	 * Creates a new supervisory organization term.
	 * 
	 * @param offender offender
	 * @param supervisoryOrganization supervisory organization
	 * @param dateRange date range
	 * @return newly created supervisory organization
	 * @throws SupervisoryOrganizationTermExistsException if the supervisory
	 * organization term exists
	 * @throws SupervisoryOrganizationTermConflictException if conflicting
	 * supervisory organization terms exist
	 */
	SupervisoryOrganizationTerm create(Offender offender,
			SupervisoryOrganization supervisoryOrganization,
			DateRange dateRange)
				throws SupervisoryOrganizationTermExistsException,
					SupervisoryOrganizationTermConflictException;

	/**
	 * Updates an existing supervisory organization term.
	 * 
	 * @param supervisoryOrganizationTerm supervisory organization term to
	 * update
	 * @param supervisoryOrganization supervisory organization
	 * @param dateRange date range
	 * @return supervisory organization term to update
	 * @throws SupervisoryOrganizationTermExistsException if the supervisory
	 * organization term exists
	 * @throws SupervisoryOrganizationTermConflictException if conflicting
	 * supervisory organization terms exist
	 * @throws DateRangeOutOfBoundsException if the associated placement terms
	 * are not within the date range
	 */
	SupervisoryOrganizationTerm update(
			SupervisoryOrganizationTerm supervisoryOrganizationTerm,
			SupervisoryOrganization supervisoryOrganization,
			DateRange dateRange)
				throws SupervisoryOrganizationTermExistsException,
					SupervisoryOrganizationTermConflictException,
					DateRangeOutOfBoundsException;
	
	/**
	 * Removes a supervisory organization term.
	 * 
	 * @param supervisoryOrganizationTerm supervisory organization term to
	 * remove
	 * @throws PlacementTermExistsException if a placement term uses the
	 * supervisory organization term
	 */
	void remove(SupervisoryOrganizationTerm supervisoryOrganizationTerm)
			throws PlacementTermExistsException;
	
	/**
	 * Returns supervisory organizations.
	 * 
	 * @return supervisory organizations
	 */
	List<SupervisoryOrganization> findSupervisoryOrganizations();
}