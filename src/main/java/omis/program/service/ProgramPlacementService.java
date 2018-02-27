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
package omis.program.service;

import java.util.Date;
import java.util.List;

import omis.datatype.DateRange;
import omis.exception.DuplicateEntityFoundException;
import omis.location.domain.Location;
import omis.locationterm.domain.LocationTerm;
import omis.offender.domain.Offender;
import omis.program.domain.Program;
import omis.program.domain.ProgramPlacement;
import omis.program.exception.ProgramPlacementConflictException;
import omis.program.exception.ProgramPlacementExistsAfterException;
import omis.program.exception.ProgramPlacementExistsBeforeException;
import omis.program.exception.ProgramPlacementExistsException;
import omis.supervision.domain.PlacementTerm;
import omis.supervision.domain.SupervisoryOrganization;
import omis.supervision.exception.OffenderNotUnderSupervisionException;

/**
 * Service for program placements.
 *
 * @author Stephen Abson
 * @author Sheronda Vaughn
 * @version 0.0.1
 * @since OMIS 3.0
 */
public interface ProgramPlacementService {
	
	/**
	 * Places offender on program.
	 * 
	 * @param offender offender
	 * @param placementTerm placement term of offender
	 * @param locationTerm location term of offender if located
	 * @param dateRange date range
	 * @param program program
	 * @return program placement
	 * @throws DuplicateEntityFoundException if program placement exists
	 * @throws ProgramPlacementConflictException if conflicting program
	 * placement exists
	 * @throws ProgramPlacementExistsBeforeException if conflicting program
	 * placement exists before
	 * @throws ProgramPlacementExistsAfterException if conflicting program
	 * placement exists after
	 * @throws OffenderNotUnderSupervisionException if offender is not under
	 * supervision
	 */
	ProgramPlacement create(
			Offender offender, PlacementTerm placementTerm,
			LocationTerm locationTerm, DateRange dateRange, Program program)
					throws ProgramPlacementExistsException,
					 	ProgramPlacementConflictException,
					 	ProgramPlacementExistsBeforeException,
					 	ProgramPlacementExistsAfterException,
					 	OffenderNotUnderSupervisionException;
	
	/**
	 * Updates program placement.
	 * 
	 * @param programPlacement program placement to update
	 * @param dateRange date range
	 * @param program program
	 * @return updated program placement
	 * @throws DuplicateEntityFoundException if program placement exists
	 * @throws ProgramPlacementConflictException if conflicting program
	 * placement exists
	 * @throws ProgramPlacementExistsBeforeException if conflicting program
	 * placement exists before
	 * @throws ProgramPlacementExistsAfterException if conflicting program
	 * placement exists after
	 * @throws OffenderNotUnderSupervisionException if offender is not under
	 * supervision
	 */
	ProgramPlacement update(
			ProgramPlacement programPlacement, DateRange dateRange,
			Program program)
					throws ProgramPlacementExistsException,
						ProgramPlacementConflictException,
						ProgramPlacementExistsBeforeException,
						ProgramPlacementExistsAfterException,
					 	OffenderNotUnderSupervisionException;
	
	/**
	 * Removes program placement.
	 * 
	 * @param programPlacement program placement to remove
	 */
	void remove(ProgramPlacement programPlacement);

	/**
	 * Returns placement term.
	 * 
	 * @param offender offender
	 * @param effectiveDate effective date
	 * @return placement term
	 */
	PlacementTerm findPlacementTerm(Offender offender, Date effectiveDate);
	
	/**
	 * Returns location term.
	 * 
	 * @param offender offender
	 * @param effectiveDate effective date
	 * @return effective date
	 */
	LocationTerm findLocationTerm(Offender offender, Date effectiveDate);
	
	/**
	 * Returns programs offered by supervisory organization.
	 * 
	 * @param supervisoryOrganization supervisory organization
	 * @return programs offered by supervisory organization
	 */
	List<Program> findProgramsOfferedBySupervisoryOrganization(
			SupervisoryOrganization supervisoryOrganization);
	
	/**
	 * Returns programs offered at location.
	 * 
	 * @param location location
	 * @return programs offered at location
	 */
	List<Program> findProgramsOfferedAtLocation(Location location);
}