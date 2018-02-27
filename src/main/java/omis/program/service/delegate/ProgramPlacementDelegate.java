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
package omis.program.service.delegate;

import java.util.Date;
import java.util.List;

import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.datatype.DateRange;
import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;
import omis.locationterm.domain.LocationTerm;
import omis.offender.domain.Offender;
import omis.program.dao.ProgramPlacementDao;
import omis.program.domain.Program;
import omis.program.domain.ProgramPlacement;
import omis.program.exception.ProgramPlacementExistsException;
import omis.supervision.domain.PlacementTerm;

/**
 * Delegate for program placements.
 *
 * @author Stephen Abson
 * @author Sheronda Vaughn
 * @version 0.0.1 (Dec 14, 2015)
 * @since OMIS 3.0
 */
public class ProgramPlacementDelegate {

	/* Instance factories. */
	
	private final InstanceFactory<ProgramPlacement>
	programPlacementInstanceFactory;
	
	/* Data access objects. */
	
	private final ProgramPlacementDao programPlacementDao;
	
	/* Audit component retriever. */
	
	private final AuditComponentRetriever auditComponentRetriever;
	
	/* Constructors. */
	
	/**
	 * Delegate for program placements.
	 * 
	 * @param programPlacementInstanceFactory instance factory for program
	 * placements
	 * @param programPlacementDao data access object for program placements
	 * @param auditComponentRetriever audit component retriever
	 */
	public ProgramPlacementDelegate(
			final InstanceFactory<ProgramPlacement>
				programPlacementInstanceFactory,
			final ProgramPlacementDao programPlacementDao,
			final AuditComponentRetriever auditComponentRetriever) {
		this.programPlacementInstanceFactory
			= programPlacementInstanceFactory;
		this.programPlacementDao = programPlacementDao;
		this.auditComponentRetriever = auditComponentRetriever;
	}
	
	/* Methods. */
	
	/**
	 * Creates program placement.
	 * 
	 * @param offender offender
	 * @param dateRange date range
	 * @param program program
	 * @param placementTerm placement term
	 * @return created program placement
	 * @throws DuplicateEntityFoundException if program placement exists
	 */
	public ProgramPlacement create(
			final Offender offender,
			final DateRange dateRange,
			final Program program,
			final PlacementTerm placementTerm,
			final LocationTerm locationTerm)
				throws ProgramPlacementExistsException {
		if (this.programPlacementDao
				.find(offender, dateRange, program) != null) {
			throw new ProgramPlacementExistsException("Program placement exists");
		}
		ProgramPlacement placement = this.programPlacementInstanceFactory
				.createInstance();
		placement.setCreationSignature(new CreationSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
		placement.setOffender(offender);
		this.populatePlacement(
				placement, dateRange, program, placementTerm, locationTerm);
		return this.programPlacementDao.makePersistent(placement);
	}
	
	/**
	 * Updates program placement.
	 * 
	 * @param placement program placement
	 * @param dateRange date range
	 * @param program program
	 * @param placementTerm placement term
	 * @return updated program placement
	 * @throws DuplicateEntityFoundException if program placement exists
	 */
	public ProgramPlacement update(
			final ProgramPlacement placement,
			final DateRange dateRange,
			final Program program,
			final PlacementTerm placementTerm,
			final LocationTerm locationTerm)
				throws ProgramPlacementExistsException {
		if (this.programPlacementDao
				.findExcluding(placement.getOffender(), dateRange,
						program, placement) != null) {
			throw new ProgramPlacementExistsException("Program placement exists");
		}
		this.populatePlacement(
				placement, dateRange, program, placementTerm, locationTerm);
		return this.programPlacementDao.makePersistent(placement);
	}
	
	/**
	 * Returns program placements by offender.
	 * 
	 * @param offender offender
	 * @return program placements by offender
	 */
	public List<ProgramPlacement> findByOffender(
			final Offender offender) {
		return this.programPlacementDao.findByOffender(offender);
	}

	/**
	 * Returns program placement by offender on date.
	 * 
	 * @param offender offender
	 * @param effectiveDate effective date
	 * @return program placement by offender on date
	 */
	public ProgramPlacement findByOffenderOnDate(
			final Offender offender, final Date effectiveDate) {
		return this.programPlacementDao.findByOffenderOnDate(
				offender, effectiveDate);
	}

	/**
	 * Removes program placement.
	 * 
	 * @param programPlacement program placement
	 */
	public void remove(final ProgramPlacement programPlacement) {
		this.programPlacementDao.makeTransient(programPlacement);
	}
	
	/**
	 * Returns number of program placements for offender between dates that are 
	 * not excluded.
	 * 
	 * @param offender offender
	 * @param startDate start date
	 * @param endDate end date
	 * @param excludedProgramPlacements program placements to exclude
	 * @return number of program placements for offender between dates
	 */
	public long countExcluding(Offender offender, Date startDate, Date endDate,
			ProgramPlacement... excludedProgramPlacements) {
		return this.programPlacementDao.countExcluding(offender, startDate, 
				endDate, excludedProgramPlacements);
	}
	
	/**
	 * Returns number of program placements for an offender after the specified 
	 * date that are not excluded.
	 * 
	 * @param offender offender
	 * @param startDate start date
	 * @param excludedProgramPlacement excluded program placement
	 * @return number of program placements for an offender after the specified 
	 * date
	 */
	public long countAfterDateExcluding(Offender offender, Date startDate, 
			ProgramPlacement excludedProgramPlacement) {
		return this.programPlacementDao.countAfterDateExcluding(offender, 
				startDate, excludedProgramPlacement);
	}
	
	/**
	 * Returns program placement for the specified values.
	 * 
	 * @param offender offender
	 * @param dateRange date range
	 * @param program program
	 * @return program placement for the specified values
	 */
	public ProgramPlacement find(Offender offender, DateRange dateRange,
			Program program) {
		return this.programPlacementDao.find(offender, dateRange, 
				program);
	}
	
	/**
	 * Returns program placement other than those excluded.
	 * 
	 * @param offender offender
	 * @param dateRange date range
	 * @param program program
	 * @param excludedProgramPlacements program placements to exclude
	 * @return program placement other than those excluded
	 */
	public ProgramPlacement findExcluding(Offender offender, DateRange dateRange,
			Program program, ProgramPlacement... excludedProgramPlacements) {
		return this.programPlacementDao.findExcluding(offender, dateRange, 
				program, excludedProgramPlacements);
	}
	
	/* Helper methods. */
	
	// Populates placement
	private void populatePlacement(
			final ProgramPlacement programPlacement,
			final DateRange dateRange,
			final Program program,
			final PlacementTerm placementTerm,
			final LocationTerm locationTerm) {
		programPlacement.setUpdateSignature(new UpdateSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
		programPlacement.setDateRange(dateRange);
		programPlacement.setProgram(program);
		programPlacement.setPlacementTerm(placementTerm);
		programPlacement.setLocationTerm(locationTerm);
	}
}