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
package omis.mentalhealthreview.report.impl.hibernate;

import java.util.List;

import org.hibernate.SessionFactory;

import omis.offender.domain.Offender;
import omis.mentalhealthreview.report.MentalHealthReviewSummary;
import omis.mentalhealthreview.report.MentalHealthReviewSummaryReportService;

/**
 * Hibernate implementation of the mental health review summary report service.
 * 
 * @author Josh Divine
 * @version 0.1.1 (Feb 14, 2018)
 * @since OMIS 3.0
 */
public class MentalHealthReviewSummaryReportServiceHibernateImpl 
		implements MentalHealthReviewSummaryReportService {

	/* Queries. */
	
	private static final String FIND_BY_OFFENDER_QUERY_NAME = 
			"findMentalHealthReviewSummariesByOffender";
	
	/* Parameters.*/ 
	
	private static final String OFFENDER_PARAMETER_NAME = "offender";
	
	/* Members. */
	
	private final SessionFactory sessionFactory;
	
	/**
	 * Constructor.
	 *
	 * @param sessionFactory session factory
	 */
	public MentalHealthReviewSummaryReportServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/** {@inheritDoc} */
	@Override
	public List<MentalHealthReviewSummary> 
			findMentalHealthReviewSummariesByOffender(final Offender offender) {
		@SuppressWarnings("unchecked")
		List<MentalHealthReviewSummary> summaries = this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(FIND_BY_OFFENDER_QUERY_NAME)
				.setParameter(OFFENDER_PARAMETER_NAME, offender)
				.setReadOnly(true)
				.list();
		return summaries;
	}
}