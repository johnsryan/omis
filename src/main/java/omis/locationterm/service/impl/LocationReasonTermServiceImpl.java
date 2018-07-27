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
package omis.locationterm.service.impl;

import java.util.List;

import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.datatype.DateRange;
import omis.exception.DateRangeOutOfBoundsException;
import omis.instance.factory.InstanceFactory;
import omis.locationterm.dao.LocationReasonDao;
import omis.locationterm.dao.LocationReasonTermDao;
import omis.locationterm.dao.LocationTermDao;
import omis.locationterm.domain.LocationReason;
import omis.locationterm.domain.LocationReasonTerm;
import omis.locationterm.domain.LocationTerm;
import omis.locationterm.exception.LocationReasonTermConflictException;
import omis.locationterm.exception.LocationReasonTermExistsException;
import omis.locationterm.service.LocationReasonTermService;
import omis.offender.domain.Offender;
import omis.offender.exception.OffenderMismatchException;

/**
 * Implementation of service for location reason terms.
 * 
 * @author Stephen Abson
 * @author Sheronda Vaughn
 * @version 0.1.0 (Jan 16, 2014)
 * @since OMIS 3.0
 */
public class LocationReasonTermServiceImpl
		implements LocationReasonTermService {
	
	/* Resources. */
	
	private final LocationReasonTermDao locationReasonTermDao;
	
	private final InstanceFactory<LocationReasonTerm>
	locationReasonTermInstanceFactory;
	
	private final LocationTermDao locationTermDao;
	
	private final LocationReasonDao locationReasonDao;
	
	private final AuditComponentRetriever auditComponentRetriever;

	/* Constructors. */
	
	/**
	 * Instantiates an implementation for service for location reason term.
	 * 
	 * @param locationReasonTermDao data access object for location reason terms
	 * @param locationReasonTermInstanceFactory instance factory for location
	 * reason terms
	 * @param locationTermDao data access object for location terms
	 * @param locationReasonDao data access object for location reasons
	 * @param auditComponentRetriever audit component retriever
	 */
	public LocationReasonTermServiceImpl(
			final LocationReasonTermDao locationReasonTermDao,
			final InstanceFactory<LocationReasonTerm>
				locationReasonTermInstanceFactory,
			final LocationTermDao locationTermDao,
			final LocationReasonDao locationReasonDao,
			final AuditComponentRetriever auditComponentRetriever) {
		this.locationReasonTermDao = locationReasonTermDao;
		this.locationReasonTermInstanceFactory
			= locationReasonTermInstanceFactory;
		this.locationTermDao = locationTermDao;
		this.locationReasonDao = locationReasonDao;
		this.auditComponentRetriever = auditComponentRetriever;
	}

	/* Method implementations. */
	
	/** {@inheritDoc} */
	@Override
	public List<LocationReasonTerm> findByOffender(
			final Offender offender) {
		return this.locationReasonTermDao.findByOffender(offender);
	}

	/** {@inheritDoc} */
	@Override
	public List<LocationReason> findLocationReasons() {
		return this.locationReasonDao.findAll();
	}

	/** {@inheritDoc} */
	@Override
	public List<LocationTerm> findLocationTerms(
			final Offender offender) {
		return this.locationTermDao.findByOffender(offender);
	}

	/** {@inheritDoc} */
	@Override
	public void remove(
			final LocationReasonTerm locationReasonTerm) {
		this.locationReasonTermDao.makeTransient(locationReasonTerm);
	}

	/** {@inheritDoc} */
	@Override
	public LocationReasonTerm create(final Offender offender,
			final LocationTerm locationTerm,
			final LocationReason reason, final DateRange dateRange)
					throws LocationReasonTermExistsException,
						OffenderMismatchException,
						LocationReasonTermConflictException,
						DateRangeOutOfBoundsException {
		
		// Throws exception if duplicate location reason term exists
		if (this.locationReasonTermDao.find(offender, locationTerm,
				DateRange.getStartDate(dateRange),
				DateRange.getEndDate(dateRange)) != null) {
			throw new LocationReasonTermExistsException("Location term exists");
		}
		
		// Throws exception if offenders do not match
		if (!offender.equals(locationTerm.getOffender())) {
			throw new OffenderMismatchException(
					"Offender and offender of location term mismatch");
		}
		
		// Throws exception if conflicts exist
		long countConflicting = this.locationReasonTermDao.count(locationTerm,
				DateRange.getStartDate(dateRange),
				DateRange.getEndDate(dateRange));
		if (countConflicting > 0) {
			throw new LocationReasonTermConflictException(countConflicting
					+ " conflicting location reason term(s) exist");
		}
		
		// Throws exception if date range of reason term is outside of location
		// term
		if (!DateRange.occursWithin(dateRange, locationTerm.getDateRange())) {
			throw new DateRangeOutOfBoundsException(
					"Location reason term is outside of date range of"
					+ " location term");
		}
		
		// Creates new location reason term
		LocationReasonTerm locationReasonTerm
			= this.locationReasonTermInstanceFactory.createInstance();
		locationReasonTerm.setCreationSignature(new CreationSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
		locationReasonTerm.setUpdateSignature(new UpdateSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
		locationReasonTerm.setOffender(offender);
		locationReasonTerm.setLocationTerm(locationTerm);
		locationReasonTerm.setDateRange(dateRange);
		locationReasonTerm.setReason(reason);
		return this.locationReasonTermDao.makePersistent(locationReasonTerm);
	}

	/** {@inheritDoc} */
	@Override
	public LocationReasonTerm update(
			final LocationReasonTerm locationReasonTerm,
			final LocationTerm locationTerm,
			final LocationReason reason,
			final DateRange dateRange)
					throws LocationReasonTermExistsException,
						OffenderMismatchException,
						LocationReasonTermConflictException,
						DateRangeOutOfBoundsException {
		
		// Throws exception if duplicate location reason term exists
		if (this.locationReasonTermDao.findExcluding(
				locationReasonTerm.getOffender(), locationTerm,
				DateRange.getStartDate(dateRange),
				DateRange.getEndDate(dateRange), locationReasonTerm) != null) {
			throw new LocationReasonTermExistsException("Location term exists");
		}
		
		// Throws exception if offenders do not match
		if (!locationReasonTerm.getOffender().equals(
				locationTerm.getOffender())) {
			throw new OffenderMismatchException(
				"Offender of location term and location reason term mismatch");
		}
		
		// Throws exception if conflicts exist
		long countConflicting = this.locationReasonTermDao.countExcluding(
				locationTerm, DateRange.getStartDate(dateRange),
				DateRange.getEndDate(dateRange), locationReasonTerm);
		if (countConflicting > 0) {
			throw new LocationReasonTermConflictException(countConflicting
					+ " conflicting location reason term(s) exist");
		}
		
		// Throws exception if date range of reason term is outside of location
		// term
		if (!DateRange.occursWithin(dateRange, locationTerm.getDateRange())) {
			throw new DateRangeOutOfBoundsException(
					"Location reason term is outside of date range of"
					+ " location term");
		}
		
		// Updates existing location reason term
		locationReasonTerm.setUpdateSignature(new UpdateSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
		locationReasonTerm.setLocationTerm(locationTerm);
		locationReasonTerm.setDateRange(dateRange);
		locationReasonTerm.setReason(reason);
		return this.locationReasonTermDao.makePersistent(locationReasonTerm);
	}
}