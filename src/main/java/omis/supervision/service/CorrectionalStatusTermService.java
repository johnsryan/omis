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
import omis.supervision.domain.CorrectionalStatus;
import omis.supervision.domain.CorrectionalStatusTerm;
import omis.supervision.exception.CorrectionalStatusTermConflictException;
import omis.supervision.exception.CorrectionalStatusTermExistsException;
import omis.supervision.exception.PlacementTermExistsException;

/**
 * Service for correctional status terms.
 * 
 * @author Jason Nelson
 * @author Stephen Abson
 * @version 0.1.1 (Jan 10, 2014)
 * @since OMIS 3.0
 */
public interface CorrectionalStatusTermService {
	
	/**
	 * Returns all correctional status terms for the specified offender.
	 * 
	 * @param offender offender
	 * @return all correctional status terms for offender
	 */
	List<CorrectionalStatusTerm> findByOffender(Offender offender);
	
	/**
	 * Returns the correctional status term for the offender on the specified
	 * date.
	 * 
	 * @param offender offender
	 * @param date date
	 * @return correctional status term for offender on date
	 */
	CorrectionalStatusTerm findByOffenderOnDate(Offender offender, Date date);

	/**
	 * Creates a new correctional status term.
	 * 
	 * @param offender offender
	 * @param correctionalStatus correctional status
	 * @param dateRange date range
	 * @return newly created correctional status term
	 * @throws CorrectionalStatusTermConflictException if conflicting
	 * correctional status terms exist
	 */
	CorrectionalStatusTerm create(Offender offender,
			CorrectionalStatus correctionalStatus, DateRange dateRange)
				throws CorrectionalStatusTermExistsException,
					CorrectionalStatusTermConflictException;
	
	/**
	 * Updates an existing correctional status term.
	 * 
	 * @param correctionalStatusTerm correctional status term to update
	 * @param correctionalStatus correctional status
	 * @param dateRange date range
	 * @return updated correctional status term
	 * @throws CorrectionalStatusTermExistsException if correctional status
	 * term exists
	 * @throws CorrectionalStatusTermConflictException if conflicting
	 * correctional status terms exist
	 * @throws DateRangeOutOfBoundsException if the associated placement terms
	 * are not within the date range
	 */
	CorrectionalStatusTerm update(CorrectionalStatusTerm correctionalStatusTerm,
			CorrectionalStatus correctionalStatus, DateRange dateRange)
				throws CorrectionalStatusTermExistsException,
					CorrectionalStatusTermConflictException,
					DateRangeOutOfBoundsException;	
	
	/**
	 * Removes a correctional status term.
	 * 
	 * @param correctionalStatusTerm correctional status term to remove
	 * @throws PlacementTermExistsException if a placement term uses the
	 * correctional status term
	 */
	void remove(CorrectionalStatusTerm correctionalStatusTerm)
			throws PlacementTermExistsException;
	
	/**
	 * Returns correctional statuses.
	 * 
	 * @return correctional statuses
	 */
	List<CorrectionalStatus> findCorrectionalStatuses();
}