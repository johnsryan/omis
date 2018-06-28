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
import omis.supervision.dao.PlacementTermDao;
import omis.supervision.dao.SupervisoryOrganizationDao;
import omis.supervision.dao.SupervisoryOrganizationTermDao;
import omis.supervision.domain.PlacementTerm;
import omis.supervision.domain.SupervisoryOrganization;
import omis.supervision.domain.SupervisoryOrganizationTerm;
import omis.supervision.exception.PlacementTermExistsException;
import omis.supervision.exception.SupervisoryOrganizationTermConflictException;
import omis.supervision.exception.SupervisoryOrganizationTermExistsException;
import omis.supervision.service.SupervisoryOrganizationTermService;

/**
 * Implementation of service for supervisory organization terms.
 * 
 * @author Jason Nelson
 * @author Stephen Abson
 * @version 0.1.1 (Jan 10, 2013)
 * @since OMIS 3.0
 */
public class SupervisoryOrganizationTermServiceImpl
		implements SupervisoryOrganizationTermService {

	private final InstanceFactory<SupervisoryOrganizationTerm>
	supervisoryOrganizationTermInstanceFactory;
	
	private final SupervisoryOrganizationTermDao supervisoryOrganizationTermDao;
	
	private final SupervisoryOrganizationDao supervisoryOrganizationDao;
	
	private final PlacementTermDao placementTermDao;
	
	private final AuditComponentRetriever auditComponentRetriever;
	
	/**
	 * Instantiates an implementation of a supervisory organization term with
	 * the specified resources.
	 * 
	 * @param supervisoryOrganizationTermInstanceFactory instance factory for
	 * supervisory organization terms
	 * @param supervisoryOrganizationTermDao data access object for supervisory
	 * organization terms
	 * @param supervisoryOrganizationDao data access object for supervisory
	 * organizations
	 * @param placementTermDao data access object for placement terms
	 * @param auditComponentRetriever audit component retriever
	 */
	public SupervisoryOrganizationTermServiceImpl(
			final InstanceFactory<SupervisoryOrganizationTerm>
				supervisoryOrganizationTermInstanceFactory,
			final SupervisoryOrganizationTermDao
				supervisoryOrganizationTermDao,
			final SupervisoryOrganizationDao supervisoryOrganizationDao,
			final PlacementTermDao placementTermDao,
			final AuditComponentRetriever auditComponentRetriever) {
		this.supervisoryOrganizationTermInstanceFactory
			= supervisoryOrganizationTermInstanceFactory;
		this.supervisoryOrganizationTermDao = supervisoryOrganizationTermDao;
		this.supervisoryOrganizationDao = supervisoryOrganizationDao;
		this.placementTermDao = placementTermDao;
		this.auditComponentRetriever = auditComponentRetriever;
	}

	/** {@inheritDoc} */
	@Override
	public List<SupervisoryOrganizationTerm> findByOffender(
			final Offender offender) {
		return this.supervisoryOrganizationTermDao.findByOffender(offender);
	}

	/** {@inheritDoc} */
	@Override
	public SupervisoryOrganizationTerm findByOffenderOnDate(
			final Offender offender, final Date date) {
		return this.supervisoryOrganizationTermDao
				.findForOffenderOnDate(offender, date);
	}

	/** {@inheritDoc} */
	@Override
	public SupervisoryOrganizationTerm create(final Offender offender,
			final SupervisoryOrganization supervisoryOrganization,
			final DateRange dateRange)
					throws SupervisoryOrganizationTermExistsException,
						SupervisoryOrganizationTermConflictException {
		
		// Throws exception if duplicate supervisory organization term exists
		if (this.supervisoryOrganizationTermDao.find(offender,
				supervisoryOrganization, DateRange.getStartDate(dateRange),
				DateRange.getEndDate(dateRange)) != null) {
			throw new SupervisoryOrganizationTermExistsException(
					"Supervisory organization term exists");
		}
		
		// Throws exception if conflicting supervisory organization terms exist
		long conflictingCount = this.supervisoryOrganizationTermDao
				.countForOffenderBetweenDates(offender,
						DateRange.getStartDate(dateRange),
						DateRange.getEndDate(dateRange));
		if (conflictingCount > 0) {
			throw new SupervisoryOrganizationTermConflictException(
					conflictingCount
					+ " conflicting supervisory organization term(s) exist");
		}
		
		// Creates new supervisory organization term
		SupervisoryOrganizationTerm supervisoryOrganizationTerm
			= this.supervisoryOrganizationTermInstanceFactory.createInstance();
		supervisoryOrganizationTerm.setCreationSignature(new CreationSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
		supervisoryOrganizationTerm.setOffender(offender);
		supervisoryOrganizationTerm.setUpdateSignature(new UpdateSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
		supervisoryOrganizationTerm.setSupervisoryOrganization(
				supervisoryOrganization);
		supervisoryOrganizationTerm.setDateRange(dateRange);
		return this.supervisoryOrganizationTermDao
				.makePersistent(supervisoryOrganizationTerm);
	}

	/** {@inheritDoc} */
	@Override
	public SupervisoryOrganizationTerm update(
			final SupervisoryOrganizationTerm supervisoryOrganizationTerm,
			final SupervisoryOrganization supervisoryOrganization,
			final DateRange dateRange)
					throws SupervisoryOrganizationTermExistsException,
						SupervisoryOrganizationTermConflictException,
						DateRangeOutOfBoundsException {
		
		// Throws exception if duplicate supervisory organization term exists
		if (this.supervisoryOrganizationTermDao.findExcluding(
				supervisoryOrganizationTerm.getOffender(),
				supervisoryOrganization, DateRange.getStartDate(dateRange),
				DateRange.getEndDate(dateRange), supervisoryOrganizationTerm)
					!= null) {
			throw new SupervisoryOrganizationTermExistsException(
					"Supervisory organization term exists");
		}
		
		// Throws exception if conflicting supervisory organization terms exist
		long conflictingCount = this.supervisoryOrganizationTermDao
				.countForOffenderBetweenDatesExcluding(
						supervisoryOrganizationTerm.getOffender(),
						DateRange.getStartDate(dateRange),
						DateRange.getEndDate(dateRange),
						supervisoryOrganizationTerm);
		if (conflictingCount > 0) {
			throw new SupervisoryOrganizationTermConflictException(
					conflictingCount
					+ " conflicting supervisory organization term(s) exist");
		}
		
		// Ensures that associated placement terms are within date range
		// of supervisory organization term
		List<PlacementTerm> placementTerms = this.placementTermDao
				.findBySupervisoryOrganizationTerm(supervisoryOrganizationTerm);
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
		
		// Updates supervisory organization term
		supervisoryOrganizationTerm.setUpdateSignature(new UpdateSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
		supervisoryOrganizationTerm.setSupervisoryOrganization(
				supervisoryOrganization);
		supervisoryOrganizationTerm.setDateRange(dateRange);
		return this.supervisoryOrganizationTermDao
				.makePersistent(supervisoryOrganizationTerm);
	}


	/** {@inheritDoc} */
	@Override
	public void remove(
			final SupervisoryOrganizationTerm supervisoryOrganizationTerm)
			throws PlacementTermExistsException {
		long placementTermCount = this.placementTermDao
			.countBySupervisoryOrganizationTerm(supervisoryOrganizationTerm);
		if (placementTermCount > 0) {
			throw new PlacementTermExistsException(placementTermCount
			+ " placement term(s) exist with supervisory organization term");
		}
		this.supervisoryOrganizationTermDao.makeTransient(
				supervisoryOrganizationTerm);
	}

	/** {@inheritDoc} */
	@Override
	public List<SupervisoryOrganization> findSupervisoryOrganizations() {
		return this.supervisoryOrganizationDao.findAll();
	}
}