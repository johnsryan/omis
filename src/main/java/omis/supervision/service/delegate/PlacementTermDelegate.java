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
package omis.supervision.service.delegate;

import java.util.Date;

import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.datatype.DateRange;
import omis.instance.factory.InstanceFactory;
import omis.offender.domain.Offender;
import omis.supervision.dao.PlacementTermDao;
import omis.supervision.domain.CorrectionalStatusTerm;
import omis.supervision.domain.PlacementStatus;
import omis.supervision.domain.PlacementTerm;
import omis.supervision.domain.PlacementTermChangeReason;
import omis.supervision.domain.SupervisoryOrganizationTerm;
import omis.supervision.exception.PlacementTermExistsException;

/**
 * Delegate for placement terms.
 *
 * @author Stephen Abson
 * @version 0.0.1 (May 9, 2016)
 * @since OMIS 3.0
 */
public class PlacementTermDelegate {

	/* Instance factories. */
	
	private final InstanceFactory<PlacementTerm>
	placementTermInstanceFactory;
	
	/* Data access objects. */
	
	private final PlacementTermDao placementTermDao;
	
	/* Audit component retrievers. */
	
	private final AuditComponentRetriever auditComponentRetriever;
	
	/* Constructors. */
	
	/**
	 * Instantiates delegate for placement terms.
	 * 
	 * @param placementTermInstanceFactory instance factory for placement
	 * terms
	 * @param placementTermDao data access object placement terms
	 * @param auditComponentRetriever audit component retriever
	 */
	public PlacementTermDelegate(
			final InstanceFactory<PlacementTerm>
				placementTermInstanceFactory,
			final PlacementTermDao placementTermDao,
			final AuditComponentRetriever auditComponentRetriever) {
		this.placementTermInstanceFactory = placementTermInstanceFactory;
		this.placementTermDao = placementTermDao;
		this.auditComponentRetriever = auditComponentRetriever;
	}
	
	/* Methods. */
	
	/**
	 * Returns placement term for offender on date.
	 * 
	 * @param offender offender
	 * @param date date
	 * @return placement term for offender on date
	 */
	public PlacementTerm findForOffenderOnDate(
			final Offender offender, final Date date) {
		return this.placementTermDao.findForOffenderOnDate(offender, date);
	}
	
	/**
	 * Creates placement term.
	 * 
	 * @param offender offender
	 * @param status status
	 * @param statusDate status date
	 * @param dateRange date range
	 * @param supervisoryOrganizationTerm term supervised by organization
	 * @param correctionalStatusTerm term on correctional status
	 * @param startChangeReason start change reason
	 * @param endChangeReason end change reason
	 * @param locked whether placement term is locked
	 * @return created placement term
	 * @throws PlacementTermExistsException if placement term exists
	 */
	public PlacementTerm create(
			final Offender offender,
			final DateRange dateRange,
			final SupervisoryOrganizationTerm supervisoryOrganizationTerm,
			final CorrectionalStatusTerm correctionalStatusTerm,
			final PlacementTermChangeReason startChangeReason,
			final PlacementTermChangeReason endChangeReason,
			final Boolean locked)
				throws PlacementTermExistsException {
		if (this.placementTermDao.find(offender,
				DateRange.getStartDate(dateRange),
				DateRange.getEndDate(dateRange),
				correctionalStatusTerm,
				supervisoryOrganizationTerm)
					!= null) {
			throw new PlacementTermExistsException("Placement term exists");
		}
		PlacementTerm placementTerm = this.placementTermInstanceFactory
				.createInstance();
		placementTerm.setCreationSignature(new CreationSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
		placementTerm.setOffender(offender);
		this.populatePlacementTerm(placementTerm, PlacementStatus.PLACED, null,
				dateRange, supervisoryOrganizationTerm, correctionalStatusTerm,
				startChangeReason, endChangeReason, locked);
		return this.placementTermDao.makePersistent(placementTerm);
	}
	
	/**
	 * Updates placement term.
	 * 
	 * @param placementTerm placement term to update
	 * @param status status
	 * @param statusDateRange start date
	 * @param dateRange date range
	 * @param supervisoryOrganizationTerm term supervised by organization
	 * @param correctionalStatusTerm term on correctional status
	 * @param startChangeReason start change reason
	 * @param endChangeReason end change reason
	 * @return updated placement term
	 * @throws PlacementTermExistsException if placement term exists
	 */
	public PlacementTerm update(
			final PlacementTerm placementTerm,
			final PlacementStatus status,
			final DateRange statusDateRange,
			final DateRange dateRange,
			final SupervisoryOrganizationTerm supervisoryOrganizationTerm,
			final CorrectionalStatusTerm correctionalStatusTerm,
			final PlacementTermChangeReason startChangeReason,
			final PlacementTermChangeReason endChangeReason,
			final Boolean locked)
					throws PlacementTermExistsException {
		if (this.placementTermDao.findExcluding(
				placementTerm.getOffender(),
				DateRange.getStartDate(dateRange),
				DateRange.getEndDate(dateRange),
				correctionalStatusTerm,
				supervisoryOrganizationTerm,
				placementTerm) != null) {
			throw new PlacementTermExistsException("Placement term exists");
		}
		this.populatePlacementTerm(placementTerm, status, statusDateRange,
				dateRange, supervisoryOrganizationTerm, correctionalStatusTerm,
				startChangeReason, endChangeReason, locked);
		return this.placementTermDao.makePersistent(placementTerm);
	}
	
	/**
	 * Removes placement term.
	 * 
	 * @param placementTerm placement term to remove
	 */
	public void remove(final PlacementTerm placementTerm) {
		this.placementTermDao.makeTransient(placementTerm);
	}
	
	/**
	 * Counts placement terms for offender between dates.
	 * 
	 * @param offender offender
	 * @param startDate start date
	 * @param endDate end date
	 * @param excludedPlacementTerms placement terms to exclude
	 * @return count of placement terms for offender between dates
	 */
	public long countForOffenderBetweenDatesExcluding(
			final Offender offender,
			final Date startDate,
			final Date endDate,
			final PlacementTerm... excludedPlacementTerms) {
		return this.placementTermDao
				.countForOffenderBetweenDatesExcluding(
						offender, startDate, endDate, excludedPlacementTerms);
	}
	
	/**
	 * Counts placement terms for offender between dates.
	 * 
	 * @param offender offender
	 * @param startDate start date
	 * @param endDate end date
	 * @return count of placement terms for offender between dates
	 */
	public long countForOffenderBetweenDates(
			final Offender offender,
			final Date startDate,
			final Date endDate) {
		return this.placementTermDao.countForOffenderBetweenDates(
				offender, startDate, endDate);
	}
	
	/* Helper methods. */
	
	// Populates placement term
	private void populatePlacementTerm(
			final PlacementTerm placementTerm,
			final PlacementStatus status,
			final DateRange statusDateRange,
			final DateRange dateRange,
			final SupervisoryOrganizationTerm supervisoryOrganizationTerm,
			final CorrectionalStatusTerm correctionalStatusTerm,
			final PlacementTermChangeReason startChangeReason,
			final PlacementTermChangeReason endChangeReason,
			final Boolean locked) {
		placementTerm.setUpdateSignature(new UpdateSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
		placementTerm.setStatus(status);
		placementTerm.setStatusDateRange(statusDateRange);
		placementTerm.setDateRange(dateRange);
		placementTerm.setSupervisoryOrganizationTerm(
				supervisoryOrganizationTerm);
		placementTerm.setCorrectionalStatusTerm(correctionalStatusTerm);
		placementTerm.setStartChangeReason(startChangeReason);
		placementTerm.setEndChangeReason(endChangeReason);
		placementTerm.setLocked(locked);
	}
}