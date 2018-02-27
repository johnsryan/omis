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
package omis.substance.report.impl.hibernate;

import java.util.Collections;
import java.util.List;

import omis.offender.domain.Offender;
import omis.substance.report.SubstanceTestSummary;
import omis.substance.report.SubstanceSummaryService;

import org.hibernate.SessionFactory;

/**
 * Substance Summary Service Hibernate Impl.
 * 
 * @author Joel Norris
 * @author Josh Divine
 * @version 0.1.1 (Feb 14, 2018)
 * @since OMIS 3.0
 */
public class SubstanceSummaryServiceHibernateImpl 
	implements SubstanceSummaryService {

	/* Query names. */
	
	private static final String SUMMARIZE_BY_OFFENDER_QUERY_NAME 
		= "summarizeSubstanceByOffender";
	
	private static final String SUMMARIZE_REQUESTS_BY_OFFENDER_QUERY_NAME
		= "summarizeSubstanceSampleRequestsByOffender";
	
	private static final String FIND_NON_SAMPLE_SUMMARIES_QUERY_NAME 
		= "summarizeFulfilledRequestsWithoutSample";
	
	/* Parameter names. */
	
	private static final String OFFENDER_PARAM_NAME = "offender";
	
	/* Helpers */
	
	private SessionFactory sessionFactory;

	/**
	 * Instantiates a default instance of substance summary service
	 * hibernate implementation.
	 */
	public SubstanceSummaryServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	@Override
	public List<SubstanceTestSummary> summarizeSubstanceTestsByOffender(
			final Offender offender) {
		List<SubstanceTestSummary> summaries = this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(SUMMARIZE_BY_OFFENDER_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setReadOnly(true)
				.list();
		List<SubstanceTestSummary> requestSummaries = this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(SUMMARIZE_REQUESTS_BY_OFFENDER_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setReadOnly(true)
				.list();
		summaries.addAll(requestSummaries);
		List<SubstanceTestSummary> nonSampleSummaries = this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(FIND_NON_SAMPLE_SUMMARIES_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setReadOnly(true)
				.list();
		summaries.addAll(nonSampleSummaries);
		Collections.sort(summaries, Collections.reverseOrder());
		return summaries;
	}
}