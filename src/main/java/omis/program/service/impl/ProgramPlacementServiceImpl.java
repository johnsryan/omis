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
package omis.program.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import omis.datatype.DateRange;
import omis.location.domain.Location;
import omis.locationterm.domain.LocationTerm;
import omis.locationterm.service.delegate.LocationTermDelegate;
import omis.offender.domain.Offender;
import omis.program.domain.Program;
import omis.program.domain.ProgramPlacement;
import omis.program.exception.ProgramPlacementConflictException;
import omis.program.exception.ProgramPlacementExistsAfterException;
import omis.program.exception.ProgramPlacementExistsBeforeException;
import omis.program.exception.ProgramPlacementExistsException;
import omis.program.service.ProgramPlacementService;
import omis.program.service.delegate.ProgramDelegate;
import omis.program.service.delegate.ProgramPlacementDelegate;
import omis.supervision.domain.PlacementTerm;
import omis.supervision.domain.SupervisoryOrganization;
import omis.supervision.exception.OffenderNotUnderSupervisionException;
import omis.supervision.service.delegate.PlacementTermDelegate;

/**
 * Implementation of service for program placements.
 *
 * @author Stephen Abson
 * @author Sheronda Vaughn
 * @version 0.0.1
 * @since OMIS 3.0
 */
public class ProgramPlacementServiceImpl
		implements ProgramPlacementService {
	
	private final ProgramPlacementDelegate programPlacementDelegate;

	private final PlacementTermDelegate placementTermDelegate;
	
	private final LocationTermDelegate locationTermDelegate;
	
	private final ProgramDelegate programDelegate;
	
	/**
	 * Instantiates implementation of service for program placements.
	 * 
	 * @param programPlacementDelegate delegate for program placements
	 * @param placementTermDelegate delegate for placement terms
	 * @param locationTermDelegate delegate for location terms
	 * @param programDelegate delegate for programs
	 */
	public ProgramPlacementServiceImpl(
			final ProgramPlacementDelegate programPlacementDelegate,
			final PlacementTermDelegate placementTermDelegate,
			final LocationTermDelegate locationTermDelegate,
			final ProgramDelegate programDelegate) {
		this.programPlacementDelegate = programPlacementDelegate;
		this.placementTermDelegate = placementTermDelegate;
		this.locationTermDelegate = locationTermDelegate;
		this.programDelegate = programDelegate;
	}

	/** {@inheritDoc} */
	@Override
	public ProgramPlacement create(
			final Offender offender, final PlacementTerm placementTerm,
			final LocationTerm locationTerm, final DateRange dateRange,
			final Program program)
					throws ProgramPlacementExistsException,
						ProgramPlacementConflictException,
						ProgramPlacementExistsBeforeException,
						ProgramPlacementExistsAfterException, 
						OffenderNotUnderSupervisionException {
		if (this.programPlacementDelegate.find(offender, dateRange, program) 
				!= null) {
			throw new ProgramPlacementExistsException("Program placement already "
					+ "exists");
		}
		if (placementTerm == null) {
			throw new OffenderNotUnderSupervisionException("Offender is not "
					+ "supervised in the specified date range");
		}
		Date startDate = DateRange.getStartDate(dateRange);
		Date endDate = DateRange.getEndDate(dateRange);
		ProgramPlacement startProgramPlacement
			= this.programPlacementDelegate.findByOffenderOnDate(offender, 
					startDate);
		if (endDate == null) {
			long programPlacementCount 
				= this.programPlacementDelegate.countAfterDateExcluding(
						offender, startDate, startProgramPlacement);
			if (programPlacementCount > 0) {
				throw new ProgramPlacementExistsAfterException(
						programPlacementCount + " program placement(s) exist "
								+ "after the specified date range.");
			}
		}
		if (startProgramPlacement != null) {
			ProgramPlacement endProgramPlacement = null;			
			if (endDate != null) {
				endProgramPlacement = this.programPlacementDelegate
						.findByOffenderOnDate(offender, endDate);
				ArrayList<ProgramPlacement> programPlacements 
					= new ArrayList<ProgramPlacement>();
				programPlacements.add(startProgramPlacement);
				if (endProgramPlacement != null) {
					programPlacements.add(endProgramPlacement);
				}
				long existingCount 
					= this.programPlacementDelegate.countExcluding(offender, 
							startDate, endDate, 
							programPlacements.toArray(new ProgramPlacement[0]));
				if (existingCount > 0) {
					throw new ProgramPlacementConflictException("Date span "
							+ "covers " + existingCount + " existing program "
							+ "placements(s) that can not be adjusted "
							+ "automatically.");
				}
			} 
			
			this.programPlacementDelegate.update(startProgramPlacement, 
					new DateRange(DateRange.getStartDate(
							startProgramPlacement.getDateRange()), startDate), 
					startProgramPlacement.getProgram(),
					startProgramPlacement.getPlacementTerm(),
					startProgramPlacement.getLocationTerm());
			if (endProgramPlacement != null) {
				this.programPlacementDelegate.update(endProgramPlacement, 
						new DateRange(endDate, 
								DateRange.getEndDate(endProgramPlacement
										.getDateRange())), 
						endProgramPlacement.getProgram(),
						endProgramPlacement.getPlacementTerm(),
						endProgramPlacement.getLocationTerm());
			}
		}
		return this.programPlacementDelegate.create(offender, dateRange, 
				program, placementTerm, locationTerm);
	}

	/** {@inheritDoc} */
	@Override
	public ProgramPlacement update(
			final ProgramPlacement programPlacement, final DateRange dateRange,
			final Program program)
					throws ProgramPlacementExistsException,
						ProgramPlacementConflictException,
						ProgramPlacementExistsBeforeException,
						ProgramPlacementExistsAfterException,
						OffenderNotUnderSupervisionException {
		if (this.programPlacementDelegate.findExcluding(
				programPlacement.getOffender(), dateRange, program, 
				programPlacement) != null) {
			throw new ProgramPlacementExistsException(
					"Program placement already exists.");
		}
		long existingCount = this.programPlacementDelegate.countExcluding(
				programPlacement.getOffender(), 
				DateRange.getStartDate(dateRange), 
				DateRange.getEndDate(dateRange), programPlacement);
		if (existingCount > 0) {
			throw new ProgramPlacementConflictException("Program placement "
					+ "conflicts with " + existingCount + " existing program "
							+ "placement(s).");
		}
		if (DateRange.getEndDate(dateRange) == null) {
			long existingAfterCount 
				= this.programPlacementDelegate.countAfterDateExcluding(
						programPlacement.getOffender(), 
						DateRange.getStartDate(dateRange), programPlacement);
			if (existingAfterCount > 0) {
				throw new ProgramPlacementExistsAfterException(
					existingAfterCount + " conflicting program placement(s) "
							+ "exist after the updated program placement.");
			}
		}
		return this.programPlacementDelegate.update(programPlacement, dateRange,
				program, programPlacement.getPlacementTerm(), 
				programPlacement.getLocationTerm());
	}

	/** {@inheritDoc} */
	@Override
	public void remove(final ProgramPlacement programPlacement) {
		this.programPlacementDelegate.remove(programPlacement);
	}

	/** {@inheritDoc} */
	@Override
	public PlacementTerm findPlacementTerm(
			final Offender offender, final Date effectiveDate) {
		return this.placementTermDelegate
				.findForOffenderOnDate(offender, effectiveDate);
	}

	/** {@inheritDoc} */
	@Override
	public LocationTerm findLocationTerm(
			final Offender offender, final Date effectiveDate) {
		return this.locationTermDelegate
				.findByOffenderOnDate(offender, effectiveDate);
	}

	/** {@inheritDoc} */
	@Override
	public List<Program> findProgramsOfferedBySupervisoryOrganization(
			final SupervisoryOrganization supervisoryOrganization) {
		return this.programDelegate
				.findOfferedBySupervisoryOrganization(supervisoryOrganization);
	}

	/** {@inheritDoc} */
	@Override
	public List<Program> findProgramsOfferedAtLocation(
			final Location location) {
		return this.programDelegate.findOfferedByLocation(location);
	}
}