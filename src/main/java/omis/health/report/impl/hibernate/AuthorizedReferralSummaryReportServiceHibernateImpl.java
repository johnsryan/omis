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
package omis.health.report.impl.hibernate;

import java.util.List;

import org.hibernate.SessionFactory;

import omis.facility.domain.Facility;
import omis.health.report.AuthorizedReferralSummary;
import omis.health.report.AuthorizedReferralSummaryReportService;
import omis.offender.domain.Offender;

/**
 * Hibernate implementation of report service for authorized referral summaries.
 * 
 * @author Stephen Abson
 * @author Josh Divine
 * @version 0.1.1 (Feb 14, 2018)
 * @since OMIS 3.0
 */
public class AuthorizedReferralSummaryReportServiceHibernateImpl
		implements AuthorizedReferralSummaryReportService {

	/* Query names. */
	
	private static final String FIND_UNSCHEDULED_BY_FACILITY_QUERY_NAME
		= "findUnscheduledAuthorizedExternalReferralSummariesByFacility";
	
	private static final String FIND_UNSCHEDULED_BY_OFFENDER_QUERY_NAME
	= "findUnscheduledAuthorizedExternalReferralSummariesByOffender";
	
	/* Parameters. */
	
	private static final String FACILITY_PARAM_NAME = "facility";

	private static final String OFFENDER_PARAM_NAME = "offender";
	
	/* Resources. */
	
	private final SessionFactory sessionFactory;
	
	/* Constructors. */
	
	/**
	 * Hibernate implementation of report service for authorized referral
	 * summaries.
	 * 
	 * @param sessionFactory session factory
	 */
	public AuthorizedReferralSummaryReportServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/* Method implementations. */
	
	/** {@inheritDoc} */
	@Override
	public List<AuthorizedReferralSummary> findUnscheduledByFacility(
			final Facility facility) {
		@SuppressWarnings("unchecked")
		List<AuthorizedReferralSummary> summaries
			= this.sessionFactory.getCurrentSession().getNamedQuery(
					FIND_UNSCHEDULED_BY_FACILITY_QUERY_NAME)
				.setParameter(FACILITY_PARAM_NAME, facility)
				.setReadOnly(true)
				.list();
		return summaries;
	}

	/** {@inheritDoc} */
	@Override
	public List<AuthorizedReferralSummary> findUnscheduledByOffender(
			final Offender offender) {
		@SuppressWarnings("unchecked")
		List<AuthorizedReferralSummary> summaries
			= this.sessionFactory.getCurrentSession().getNamedQuery(
					FIND_UNSCHEDULED_BY_OFFENDER_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setReadOnly(true)
				.list();
		return summaries;
	}
}