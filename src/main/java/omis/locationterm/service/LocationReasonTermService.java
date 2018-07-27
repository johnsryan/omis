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
package omis.locationterm.service;

import java.util.List;

import omis.datatype.DateRange;
import omis.exception.DateRangeOutOfBoundsException;
import omis.locationterm.domain.LocationReason;
import omis.locationterm.domain.LocationReasonTerm;
import omis.locationterm.domain.LocationTerm;
import omis.locationterm.exception.LocationReasonTermConflictException;
import omis.locationterm.exception.LocationReasonTermExistsException;
import omis.offender.domain.Offender;
import omis.offender.exception.OffenderMismatchException;

/**
 * Service for location reason term.
 * 
 * @author Stephen Abson
 * @author Sheronda Vaughn
 * @version 0.1.0 (Jan 16, 2014)
 * @since OMIS 3.0
 */
public interface LocationReasonTermService {

	/**
	 * Returns location reason terms for offender.
	 * 
	 * @param offender offender
	 * @return location reason terms for offender
	 */
	List<LocationReasonTerm> findByOffender(Offender offender);
	
	/**
	 * Returns reasons an offender can be at a location.
	 * 
	 * @return reasons an offender can be at location
	 */
	List<LocationReason> findLocationReasons();
	
	/**
	 * Returns location terms for offender.
	 * 
	 * @param offender offender
	 * @return location terms for offender
	 */
	List<LocationTerm> findLocationTerms(Offender offender);
	
	/**
	 * Creates a location reason term.
	 * 
	 * @param offender offender
	 * @param locationTerm location term
	 * @param reason location reason
	 * @param dateRange date range
	 * @return location reason term
	 * @throws LocationReasonTermExistsException if location reason term exists
	 * @throws OffenderMismatchException if offender and offender of
	 * location term are not the same
	 * @throws LocationReasonTermConflictException if conflicting location
	 * reason terms exist
	 * @throws DateRangeOutOfBoundsException if the reason term is outside of
	 * the bounds of the location term
	 */
	LocationReasonTerm create(Offender offender, LocationTerm locationTerm,
			LocationReason reason, DateRange dateRange)
					throws LocationReasonTermExistsException,
						OffenderMismatchException,
						LocationReasonTermConflictException,
						DateRangeOutOfBoundsException;
	
	/**
	 * Updates a location reason term.
	 * 
	 * @param locationReasonTerm location reason term
	 * @param locationTerm location term
	 * @param reason location reason
	 * @param dateRange date range
	 * @return location reason term
	 * @throws LocationReasonTermExistsException if location reason term exists
	 * @throws OffenderMismatchException if offender of location reason term
	 * and offender of location term are not the same
	 * @throws LocationReasonTermConflictException if conflicting location
	 * reason terms exist
	 * @throws DateRangeOutOfBoundsException if the reason term is outside of
	 * the bounds of the location term
	 */
	LocationReasonTerm update(LocationReasonTerm locationReasonTerm,
			LocationTerm locationTerm, LocationReason reason,
			DateRange dateRange)
					throws LocationReasonTermExistsException,
						OffenderMismatchException,
						LocationReasonTermConflictException,
						DateRangeOutOfBoundsException;
	
	/**
	 * Removes location reason term.
	 * 
	 * @param locationReasonTerm location reason term to remove
	 */
	void remove(LocationReasonTerm locationReasonTerm);
}