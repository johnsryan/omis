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
package omis.detainernotification.report.impl.hibernate;

import java.util.List;

import org.hibernate.SessionFactory;

import omis.detainernotification.domain.DetainerAgency;
import omis.detainernotification.report.DetainerAgencySummary;
import omis.detainernotification.report.DetainerSummary;
import omis.detainernotification.report.DetainerSummaryReportService;
import omis.offender.domain.Offender;

/**
 * Implementation of detainer summary report service.
 *
 * @author Sheronda Vaughn
 * @author Annie Wahl
 * @author Josh Divine
 * @version 0.1.2 (Feb 14, 2018)
 * @since OMIS 3.0
 */
public class DetainerSummaryReportServiceHibernateImpl 
	implements DetainerSummaryReportService {
	
	/* Queries. */
	
	private static final String FIND_DETAINERS_BY_OFFENDER =
			"findDetainersByOffender";
	
	private static final String SUMMARIZE_DETAINER_AGENCY_QUERY_NAME =
			"summarizeDetainerAgency";
	
	/* Parameters. */
	
	private static final String OFFENDER_PARAMETER_NAME = "offender";
	
	private static final String DETAINER_AGENCY_PARAM_NAME = "detainerAgency";
	
	/* Members. */
	
	private final SessionFactory sessionFactory;
	
	/**
	 * Constructor.
	 * 
	 * @param sessionFactory session factory
	 */
	public DetainerSummaryReportServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/** {@inheritDoc} */
	@Override
	public List<DetainerSummary> findByOffender(final Offender offender) {
		@SuppressWarnings("unchecked")
		List<DetainerSummary> detainerSummaries = this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(FIND_DETAINERS_BY_OFFENDER)
				.setParameter(OFFENDER_PARAMETER_NAME, offender)
				.setReadOnly(true)
				.list();
		return detainerSummaries;
	}

	/**{@inheritDoc} */
	@Override
	public DetainerAgencySummary summarizeDetainerAgency(
			final DetainerAgency detainerAgency) {
		DetainerAgencySummary detainerAgencySummary = (DetainerAgencySummary)
				this.sessionFactory.getCurrentSession().getNamedQuery(
						SUMMARIZE_DETAINER_AGENCY_QUERY_NAME)
				.setParameter(DETAINER_AGENCY_PARAM_NAME, detainerAgency)
				.setReadOnly(true)
				.uniqueResult();
		
		return detainerAgencySummary;
	}
}