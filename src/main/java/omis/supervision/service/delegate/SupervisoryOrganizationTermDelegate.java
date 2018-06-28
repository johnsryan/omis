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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.datatype.DateRange;
import omis.instance.factory.InstanceFactory;
import omis.offender.domain.Offender;
import omis.supervision.dao.SupervisoryOrganizationTermDao;
import omis.supervision.domain.SupervisoryOrganization;
import omis.supervision.domain.SupervisoryOrganizationTerm;
import omis.supervision.exception.SupervisoryOrganizationTermExistsException;

/**
 * Delegate for supervisory organization terms.
 *
 * @author Stephen Abson
 * @version 0.0.1 (May 6, 2016)
 * @since OMIS 3.0
 */
public class SupervisoryOrganizationTermDelegate {
	
	/* Instance factories. */
	
	private final InstanceFactory<SupervisoryOrganizationTerm>
	supervisoryOrganizationTermInstanceFactory;

	/* Data access object. */
	
	private final SupervisoryOrganizationTermDao supervisoryOrganizationTermDao;
	
	/* Audit component retriever. */
	
	private final AuditComponentRetriever auditComponentRetriever;
	
	/* Constructors. */
	
	/**
	 * Instantiates delegate for supervisory organization terms.
	 * 
	 * @param supervisoryOrganizationTermInstanceFactory instance factory
	 * for supervisory organization terms
	 * @param supervisoryOrganizationTermDao data access object for supervisory
	 * organization terms
	 * @param auditComponentRetriever audit component retriever
	 */
	public SupervisoryOrganizationTermDelegate(
			final InstanceFactory<SupervisoryOrganizationTerm>
			supervisoryOrganizationTermInstanceFactory,
			final SupervisoryOrganizationTermDao
				supervisoryOrganizationTermDao,
			final AuditComponentRetriever auditComponentRetriever) {
		this.supervisoryOrganizationTermInstanceFactory
			= supervisoryOrganizationTermInstanceFactory;
		this.supervisoryOrganizationTermDao = supervisoryOrganizationTermDao;
		this.auditComponentRetriever = auditComponentRetriever;
	}
	
	/* Methods. */

	/**
	 * Returns supervisory organization term for offender on effective date.
	 * 
	 * @param offender offender
	 * @param effectiveDate effective date
	 * @return supervisory organization term for offender on effective date
	 */
	public SupervisoryOrganizationTerm findByOffenderOnEffectiveDate(
			final Offender offender, final Date effectiveDate) {
		return this.supervisoryOrganizationTermDao.findForOffenderOnDate(
				offender, effectiveDate);
	}
	
	/**
	 * Creates new supervisory organization term.
	 * 
	 * @param offender offender
	 * @param dateRange date range
	 * @param supervisoryOrganization supervisory organization
	 * @return newly created supervisory organization term
	 * @throws SupervisoryOrganizationTermExistsException if supervisory
	 * organization term exists
	 */
	public SupervisoryOrganizationTerm create(
			final Offender offender,
			final DateRange dateRange,
			final SupervisoryOrganization supervisoryOrganization) 
				throws SupervisoryOrganizationTermExistsException {
		if (this.supervisoryOrganizationTermDao.find(
				offender,
				supervisoryOrganization,
				DateRange.getStartDate(dateRange),
				DateRange.getEndDate(dateRange)) != null ) {
			throw new SupervisoryOrganizationTermExistsException(
					"Supervisory organization term exists");
		}
		SupervisoryOrganizationTerm supervisoryOrganizationTerm
			= this.supervisoryOrganizationTermInstanceFactory.createInstance();
		supervisoryOrganizationTerm.setCreationSignature(new CreationSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
		supervisoryOrganizationTerm.setOffender(offender);
		this.populateSupervisoryOrganizationTerm(
				supervisoryOrganizationTerm, dateRange,
				supervisoryOrganization);
		return this.supervisoryOrganizationTermDao.makePersistent(
				supervisoryOrganizationTerm);
	}
	
	/**
	 * Updates supervisory organization term.
	 * 
	 * @param supervisoryOrganizationTerm supervisory organization term
	 * @param dateRange date range
	 * @param supervisoryOrganization supervisory organization
	 * @return updated supervisory organization term
	 * @throws SupervisoryOrganizationTermExistsException if supervisory
	 * organization term exists
	 */
	public SupervisoryOrganizationTerm  update(
			final SupervisoryOrganizationTerm supervisoryOrganizationTerm,
			final DateRange dateRange,
			final SupervisoryOrganization supervisoryOrganization)
				throws SupervisoryOrganizationTermExistsException {
		if (this.supervisoryOrganizationTermDao.findExcluding(
				supervisoryOrganizationTerm.getOffender(),
				supervisoryOrganization,
				DateRange.getStartDate(dateRange),
				DateRange.getEndDate(dateRange),
				supervisoryOrganizationTerm) != null) {
			throw new SupervisoryOrganizationTermExistsException(
					"Supervisory organization term exists");
		}
		this.populateSupervisoryOrganizationTerm(
				supervisoryOrganizationTerm, dateRange,
				supervisoryOrganization);
		return this.supervisoryOrganizationTermDao.makePersistent(
				supervisoryOrganizationTerm);
	}
	
	/**
	 * Counts supervisory organization terms for offender between dates with
	 * option of excluding terms.
	 * 
	 * <p>Ignores excluded terms that are {@code null}.
	 * 
	 * @param offender offender
	 * @param startDate start date
	 * @param endDate end date
	 * @param excluded excluded terms; {@code null}s are ignored
	 * @return count of supervisory organization terms for offender between
	 * dates with option of excluding terms
	 */
	public long countForOffenderBetweenDatesExcluding(
			final Offender offender,
			final Date startDate, Date endDate,
			final SupervisoryOrganizationTerm... excluded) {
		if (excluded.length > 0) {
			List<SupervisoryOrganizationTerm> notNullTerms
				= new ArrayList<SupervisoryOrganizationTerm>();
			for (SupervisoryOrganizationTerm term : excluded) {
				if (term != null) {
					notNullTerms.add(term);
				}
			}
			if (notNullTerms.size() > 0) {
				return this.supervisoryOrganizationTermDao
					.countForOffenderBetweenDatesExcluding(
						offender, startDate, endDate,
						notNullTerms.toArray(
								new SupervisoryOrganizationTerm[] { }));
			} else {
				return this.supervisoryOrganizationTermDao
						.countForOffenderBetweenDates(
								offender, startDate, endDate);
			}
		} else {
			return this.supervisoryOrganizationTermDao
					.countForOffenderBetweenDates(offender, startDate, endDate);
		}
	}
	
	/* Helper methods. */
	
	// Populates supervisory organization term
	private void populateSupervisoryOrganizationTerm(
			final SupervisoryOrganizationTerm supervisoryOrganizationTerm,
			final DateRange dateRange,
			final SupervisoryOrganization supervisoryOrganization) {
		supervisoryOrganizationTerm.setUpdateSignature(new UpdateSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
		supervisoryOrganizationTerm.setDateRange(dateRange);
		supervisoryOrganizationTerm.setSupervisoryOrganization(
				supervisoryOrganization);
	}
}