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
package omis.locationterm.service.delegate;

import java.util.Date;
import java.util.List;

import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.datatype.DateRange;
import omis.instance.factory.InstanceFactory;
import omis.locationterm.dao.LocationReasonTermDao;
import omis.locationterm.domain.LocationReason;
import omis.locationterm.domain.LocationReasonTerm;
import omis.locationterm.domain.LocationTerm;
import omis.locationterm.exception.LocationReasonTermExistsException;
import omis.offender.domain.Offender;

/**
 * Delegate for location reason terms.
 *
 * @author Stephen Abson
 * @version 0.0.1 (Nov 2, 2015)
 * @since OMIS 3.0
 */
public class LocationReasonTermDelegate {
	
	/* DAOs. */
	
	private final LocationReasonTermDao locationReasonTermDao;
	
	/* Instance factories. */
	
	private final InstanceFactory<LocationReasonTerm>
	locationReasonTermInstanceFactory;
	
	/* Audit component retriever. */
	
	private final AuditComponentRetriever auditComponentRetriever;

	/**
	 * Delegate for location reason terms.
	 * 
	 * @param locationReasonTermDao data access object for location reason
	 * terms
	 * @param locationReasonTermInstanceFactory instance factory for location
	 * reason terms
	 * @param auditComponentRetriever audit component retriever
	 */
	public LocationReasonTermDelegate(
			final LocationReasonTermDao locationReasonTermDao,
			final InstanceFactory<LocationReasonTerm>
				locationReasonTermInstanceFactory,
			final AuditComponentRetriever auditComponentRetriever) {
		this.locationReasonTermDao = locationReasonTermDao;
		this.locationReasonTermInstanceFactory
			= locationReasonTermInstanceFactory;
		this.auditComponentRetriever = auditComponentRetriever;
	}
	
	/**
	 * Creates location reason term.
	 * 
	 * @param locationTerm location term
	 * @param dateRange date range
	 * @param reason reason
	 * @return created location reason term
	 * @throws LocationReasonTermExistsException if location reason term exists
	 */
	public LocationReasonTerm create(
			final LocationTerm locationTerm, final DateRange dateRange,
			final LocationReason reason)
				throws LocationReasonTermExistsException {
		if (this.locationReasonTermDao.find(
				locationTerm.getOffender(), locationTerm,
				DateRange.getStartDate(dateRange),
				DateRange.getEndDate(dateRange)) != null) {
			throw new LocationReasonTermExistsException(
					"Location reason term exists");
		}
		LocationReasonTerm reasonTerm = this.locationReasonTermInstanceFactory
				.createInstance();
		reasonTerm.setOffender(locationTerm.getOffender());
		reasonTerm.setLocationTerm(locationTerm);
		reasonTerm.setDateRange(dateRange);
		reasonTerm.setReason(reason);
		reasonTerm.setCreationSignature(new CreationSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
		reasonTerm.setUpdateSignature(new UpdateSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
		return this.locationReasonTermDao.makePersistent(reasonTerm);
	}
	
	/**
	 * Updates location reason term.
	 * 
	 * @param reasonTerm location reason term to update
	 * @param dateRange date range
	 * @param reason reason
	 * @return updated location reason term
	 * @throws LocationReasonTermExistsException if location reason term exists
	 */
	public LocationReasonTerm update(
			final LocationReasonTerm reasonTerm, final DateRange dateRange,
			final LocationReason reason)
				throws LocationReasonTermExistsException {
		if (this.locationReasonTermDao.findExcluding(
				reasonTerm.getOffender(),
				reasonTerm.getLocationTerm(),
				DateRange.getStartDate(dateRange),
				DateRange.getEndDate(dateRange),
				reasonTerm) != null) {
			throw new LocationReasonTermExistsException(
					"Location reason term exists");
		}
		reasonTerm.setDateRange(dateRange);
		reasonTerm.setReason(reason);
		reasonTerm.setUpdateSignature(new UpdateSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
		return this.locationReasonTermDao.makePersistent(reasonTerm);
	}
	
	/**
	 * Removes location reason term.
	 * 
	 * @param reasonTerm location reason term
	 */
	public void remove(final LocationReasonTerm reasonTerm) {
		this.locationReasonTermDao.makeTransient(reasonTerm);
	}
	
	/**
	 * Returns location reason terms by offender.
	 * 
	 * @param offender offender
	 * @return location reason terms by offender
	 */
	public List<LocationReasonTerm> findByOffender(final Offender offender) {
		return this.locationReasonTermDao.findByOffender(offender);
	}
	
	/**
	 * Returns location reason term for location term on date.
	 * 
	 * @param locationTerm location term
	 * @param date date
	 * @return location reason term for location term on date
	 */
	public LocationReasonTerm findForLocationTermOnDate(
			final LocationTerm locationTerm, final Date date) {
		return this.locationReasonTermDao.findForLocationTermOnDate(
				locationTerm, date);
	}

	/**
	 * Returns location reason term for offender on date.
	 * 
	 * @param offender offender
	 * @param date date
	 * @return location reason term for offender on date
	 */
	public LocationReasonTerm findForOffenderOnDate(
			final Offender offender, final Date date) {
		return this.locationReasonTermDao.findForOffenderOnDate(offender, date);
	}

	/**
	 * Changes date range of location reason term.
	 * 
	 * @param reasonTerm location reason term to change 
	 * @param startDate start date
	 * @param endDate end date
	 * @return updated reason term
	 * @throws LocationReasonTermExistsException if location reason term with
	 * date rang exists for offender
	 */
	public LocationReasonTerm changeDateRange(
			final LocationReasonTerm reasonTerm, final Date startDate,
			final Date endDate)
					throws LocationReasonTermExistsException {
		if (this.locationReasonTermDao
				.find(reasonTerm.getOffender(),
					reasonTerm.getLocationTerm(),
					startDate, endDate) != null) {
			throw new LocationReasonTermExistsException(
					"Location reason term exists");
		}
		reasonTerm.setDateRange(new DateRange(startDate, endDate));
		reasonTerm.setUpdateSignature(new UpdateSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
		return this.locationReasonTermDao.makePersistent(reasonTerm);
	}

	/**
	 * Returns number of location reason terms for location term.
	 * 
	 * @param locationTerm location term
	 * @return number of location reason terms for location term
	 */
	public long countByLocationTerm(final LocationTerm locationTerm) {
		return this.locationReasonTermDao.countByLocationTerm(locationTerm);
	}

	/**
	 * Returns number of location reason terms that exist outside of start and
	 * end date range.
	 * 
	 * @param locationTerm location term
	 * @param startDate start date
	 * @param endDate end date
	 * @return number of location reason terms that exist outside of start and
	 * end date range
	 */
	public long countForLocationTermOutOfDateBounds(
			final LocationTerm locationTerm, final Date startDate,
			final Date endDate) {
		return this.locationReasonTermDao.countForLocationTermOutOfDateBounds(
				locationTerm, startDate, endDate);
	}

	/**
	 * Returns reason terms by location term.
	 * 
	 * @param locationTerm location term
	 * @return reason terms by location term
	 */
	public List<LocationReasonTerm> findByLocationTerm(
			final LocationTerm locationTerm) {
		return this.locationReasonTermDao.findByLocationTerm(locationTerm);
	}
	
	/**
	 * Returns number of conflicting location reason terms.
	 * 
	 * @param locationTerm location term
	 * @param startDate start date
	 * @param endDate end date
	 * @return number of conflicting location reason terms
	 */
	public long count(LocationTerm locationTerm, Date startDate, Date endDate) {
		return this.locationReasonTermDao.count(locationTerm, startDate, 
				endDate);
	}
	
	/**
	 * Returns number of conflicting location reason terms that are not
	 * excluded.
	 * 
	 * @param locationTerm location term
	 * @param startDate start date
	 * @param endDate end date
	 * @param excludedLocationReasonTerms location reason terms to exclude
	 * @return number of conflicting location reason terms
	 */
	public long countExcluding(LocationTerm locationTerm, Date startDate,
			Date endDate, LocationReasonTerm... excludedLocationReasonTerms) {
		return this.locationReasonTermDao.countExcluding(locationTerm, 
				startDate, endDate, excludedLocationReasonTerms);
	}

	/**
	 * Returns number of location reason terms for an offender after the 
	 * specified date that are not excluded.
	 * 
	 * @param offender offender
	 * @param startDate start date
	 * @param excludedLocationReasonTerm excluded location reason term
	 * @return number of location terms for an offender after the specified date
	 */
	public long countAfterDateExcluding(Offender offender, Date startDate, 
			LocationReasonTerm excludedLocationReasonTerm) {
		return this.locationReasonTermDao.countAfterDateExcluding(offender, 
				startDate, excludedLocationReasonTerm);
	}
	
	/**
	 * Returns location reason term.
	 * 
	 * @param offender offender
	 * @param locationTerm location term
	 * @param startDate start date
	 * @param endDate end date
	 * @return location reason term
	 */
	public LocationReasonTerm find(Offender offender, LocationTerm locationTerm,
			Date startDate, Date endDate) {
		return this.locationReasonTermDao.find(offender, locationTerm, 
				startDate, endDate);
	}
	
	/**
	 * Returns location reason term that is not one of the excluded location
	 * reason terms.
	 * 
	 * @param offender offender
	 * @param locationTerm location term
	 * @param startDate start date
	 * @param endDate end date
	 * @param excludedLocationReasonTerms location reason terms to exclude
	 * @return location reason term
	 */
	public LocationReasonTerm findExcluding(Offender offender,
			LocationTerm locationTerm, Date startDate, Date endDate,
			LocationReasonTerm... excludedLocationReasonTerms) {
		return this.locationReasonTermDao.findExcluding(offender, locationTerm, 
				startDate, endDate, excludedLocationReasonTerms);
	}

	/**
	 * Returns reason term with start date.
	 * 
	 * @param locationTerm location term
	 * @param startDate start date
	 * @return reason term with start date
	 */
	public LocationReasonTerm findWithStartDate(
			final LocationTerm locationTerm, final Date startDate) {
		return this.locationReasonTermDao.findWithStartDate(
				locationTerm, startDate);
	}
	
	/**
	 * Removes location reason terms by location term.
	 * 
	 * @param locationTerm location term by which to remove
	 * @return location reason terms removed
	 */
	public int removeByLocationTerm(final LocationTerm locationTerm) {
		return this.locationReasonTermDao.removeByLocationTerm(locationTerm);
	}
}