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
package omis.supervision.service.impl;

import java.util.Date;
import java.util.List;

import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.datatype.DateRange;
import omis.exception.DateRangeOutOfBoundsException;
import omis.instance.factory.InstanceFactory;
import omis.offender.domain.Offender;
import omis.supervision.dao.CorrectionalStatusDao;
import omis.supervision.dao.CorrectionalStatusTermDao;
import omis.supervision.dao.PlacementTermDao;
import omis.supervision.domain.CorrectionalStatus;
import omis.supervision.domain.CorrectionalStatusTerm;
import omis.supervision.domain.PlacementTerm;
import omis.supervision.exception.CorrectionalStatusTermConflictException;
import omis.supervision.exception.CorrectionalStatusTermExistsException;
import omis.supervision.exception.PlacementTermExistsException;
import omis.supervision.service.CorrectionalStatusTermService;

/**
 * Implementation of service for correctional status terms.
 * 
 * @author Jason Nelson
 * @author Stephen Abson
 * @version 0.1.1 (Jan 10, 2014)
 * @since OMIS 3.0
 */
public class CorrectionalStatusTermServiceImpl
	implements CorrectionalStatusTermService {

	private final InstanceFactory<CorrectionalStatusTerm>
	correctionalStatusTermInstanceFactory;
	
	private final CorrectionalStatusTermDao correctionalStatusTermDao;
	
	private final CorrectionalStatusDao correctionalStatusDao;
	
	private final PlacementTermDao placementTermDao;
	
	private final AuditComponentRetriever auditComponentRetriever;

	/**
	 * Instantiates an implementation of a correctional status term with the
	 * specified resources.
	 * 
	 * @param correctionalStatusTermInstanceFactory instance factory for
	 * correctional status terms
	 * @param correctionalStatusTermDao data access object for correctional
	 * status terms
	 * @param correctionalStatusDao data access object for correctional statuses
	 * @param placementTermDao data access object for placement terms
	 * @param auditComponentRetriever audit component retriever
	 */
	public CorrectionalStatusTermServiceImpl(
			final InstanceFactory<CorrectionalStatusTerm>
			correctionalStatusTermInstanceFactory,
			final CorrectionalStatusTermDao correctionalStatusTermDao,
			final CorrectionalStatusDao correctionalStatusDao,
			final PlacementTermDao placementTermDao,
			final AuditComponentRetriever auditComponentRetriever) {
		this.correctionalStatusTermInstanceFactory
			= correctionalStatusTermInstanceFactory;
		this.correctionalStatusTermDao = correctionalStatusTermDao;
		this.correctionalStatusDao = correctionalStatusDao;
		this.placementTermDao = placementTermDao;
		this.auditComponentRetriever = auditComponentRetriever;
	}

	/** {@inheritDoc} */
	@Override
	public List<CorrectionalStatusTerm> findByOffender(
			final Offender offender) {
		return this.correctionalStatusTermDao.findByOffender(offender);
	}

	/** {@inheritDoc} */
	@Override
	public CorrectionalStatusTerm findByOffenderOnDate(
			final Offender offender, final Date date) {
		return this.correctionalStatusTermDao.findForOffenderOnDate(
				offender, date);
	}

	/** {@inheritDoc} */
	@Override
	public CorrectionalStatusTerm create(final Offender offender,
			final CorrectionalStatus correctionalStatus,
			final DateRange dateRange)
					throws CorrectionalStatusTermConflictException,
						CorrectionalStatusTermExistsException{
		
		// Throws exception if duplicate is found
		if (this.correctionalStatusTermDao.find(offender, correctionalStatus,
				DateRange.getStartDate(dateRange),
				DateRange.getEndDate(dateRange)) != null) {
			throw new CorrectionalStatusTermExistsException(
					"Correctional status term exists");
		}
		
		// Throws exception if conflicts are found
		long conflictingCount = this.correctionalStatusTermDao
				.countForOffenderBetweenDates(offender,
						DateRange.getStartDate(dateRange),
						DateRange.getEndDate(dateRange));
		if (conflictingCount > 0) {
			throw new CorrectionalStatusTermConflictException(
					conflictingCount
					+ " conflicting correctional status term(s) exist");
		}
		
		// Creates new correctional status term
		CorrectionalStatusTerm correctionalStatusTerm
			= this.correctionalStatusTermInstanceFactory.createInstance();
		correctionalStatusTerm.setCreationSignature(new CreationSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
		correctionalStatusTerm.setOffender(offender);
		correctionalStatusTerm.setUpdateSignature(new UpdateSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
		correctionalStatusTerm.setCorrectionalStatus(correctionalStatus);
		correctionalStatusTerm.setDateRange(dateRange);
		return this.correctionalStatusTermDao
				.makePersistent(correctionalStatusTerm);
	}

	/** {@inheritDoc} */
	@Override
	public CorrectionalStatusTerm update(
			final CorrectionalStatusTerm correctionalStatusTerm,
			final CorrectionalStatus correctionalStatus,
			final DateRange dateRange)
					throws CorrectionalStatusTermExistsException,
						CorrectionalStatusTermConflictException,
						DateRangeOutOfBoundsException {
		
		// Throws exception if duplicate is found
		if (this.correctionalStatusTermDao.findExcluding(
				correctionalStatusTerm.getOffender(), correctionalStatus,
				DateRange.getStartDate(dateRange),
				DateRange.getEndDate(dateRange), correctionalStatusTerm)
					!= null) {
			throw new CorrectionalStatusTermExistsException(
					"Correctional status term exists");
		}
		
		// Throws excpetion if conflicts exist
		long conflictingCount = this.correctionalStatusTermDao
				.countForOffenderBetweenDatesExcluding(
						correctionalStatusTerm.getOffender(),
						DateRange.getStartDate(dateRange),
						DateRange.getEndDate(dateRange),
						correctionalStatusTerm);
		if (conflictingCount > 0) {
			throw new CorrectionalStatusTermConflictException(
					conflictingCount
					+ " conflicting correctional status term(s) exist");
		}
		
		// Ensures that associated placement terms are within date range
		// of correctional status term
		List<PlacementTerm> placementTerms = this.placementTermDao
				.findByCorrectionalStatusTerm(correctionalStatusTerm);
		for (PlacementTerm placementTerm : placementTerms) {
			if ((placementTerm.getDateRange() != null
					&& !placementTerm.getDateRange().occursWithin(dateRange))
				|| (placementTerm.getDateRange() == null
					&& (dateRange != null && ( dateRange.getStartDate() != null
						|| dateRange.getEndDate() != null)))) {
				throw new DateRangeOutOfBoundsException(
					"Associated placement term outside of date range");
			}
		}
		
		// Updates correctional status term
		correctionalStatusTerm.setUpdateSignature(new UpdateSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
		correctionalStatusTerm.setCorrectionalStatus(correctionalStatus);
		correctionalStatusTerm.setDateRange(dateRange);
		return this.correctionalStatusTermDao
				.makePersistent(correctionalStatusTerm);
	}

	/** {@inheritDoc} */
	@Override
	public void remove(final CorrectionalStatusTerm correctionalStatusTerm)
			throws PlacementTermExistsException {
		long placementTermCount = this.placementTermDao
				.countByCorrectionalStatusTerm(correctionalStatusTerm);
		if (placementTermCount > 0) {
			throw new PlacementTermExistsException(placementTermCount
				+ " placement term(s) exist with correctional status term");
		}
		this.correctionalStatusTermDao.makeTransient(correctionalStatusTerm);
	}

	/** {@inheritDoc} */
	@Override
	public List<CorrectionalStatus> findCorrectionalStatuses() {
		return this.correctionalStatusDao.findAll();
	}
}