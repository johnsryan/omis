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
package omis.probationterm.report.impl.hibernate;

import java.util.Date;

import org.hibernate.SessionFactory;

import omis.offender.domain.Offender;
import omis.probationterm.report.ProbationTermSummary;
import omis.probationterm.report.ProbationTermSummaryItemReportService;

/**
 * ProbationTermSummaryItemReportServiceHibernateImpl.java
 * 
 * @author Annie Wahl
 * @author Josh Divine 
 * @version 0.1.1 (Feb 14, 2018)
 * @since OMIS 3.0
 */
public class ProbationTermSummaryItemReportServiceHibernateImpl
		implements ProbationTermSummaryItemReportService {
	
	private static final String FIND_PROBATION_TERM_SUMMARY_BY_OFFENDER_QUERY =
			"findProbationTermSummaryItemByOffender";
	
	private static final String OFFENDER_PARAM_NAME = "offender";

	private static final String EFFECTIVE_DATE_PARAM_NAME = "effectiveDate";
	
	private final SessionFactory sessionFactory;
	
	/**
	 * @param sessionFactory
	 */
	public ProbationTermSummaryItemReportServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**{@inheritDoc} */
	@Override
	public ProbationTermSummary findMaxProbationTermExpirationDate(
			final Offender offender, final Date effectiveDate) {
		ProbationTermSummary probationTermSummary = (ProbationTermSummary)
				this.sessionFactory.getCurrentSession().getNamedQuery(
						FIND_PROBATION_TERM_SUMMARY_BY_OFFENDER_QUERY)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setDate(EFFECTIVE_DATE_PARAM_NAME, effectiveDate)
				.setReadOnly(true)
				.uniqueResult();
				
		return probationTermSummary;
	}
}