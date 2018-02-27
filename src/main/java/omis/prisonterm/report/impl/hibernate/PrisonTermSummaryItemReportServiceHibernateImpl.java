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
package omis.prisonterm.report.impl.hibernate;

import org.hibernate.SessionFactory;

import omis.offender.domain.Offender;
import omis.prisonterm.report.PrisonTermSummary;
import omis.prisonterm.report.PrisonTermSummaryItemReportService;

/**
 * PrisonTermSummaryItemReportServiceHibernateImpl.java
 * 
 * @author Annie Wahl
 * @author Josh Divine 
 * @version 0.1.1 (Feb 14, 2018)
 * @since OMIS 3.0
 */
public class PrisonTermSummaryItemReportServiceHibernateImpl
		implements PrisonTermSummaryItemReportService {
	
	private static final String
		FIND_PRISON_TERM_SUMMARY_BY_OFFENDER_AND_EFFECTIVE_DATE_QUERY_NAME =
			"findPrisonTermSummaryByOffenderAndEffectiveDate";
	
	private static final String OFFENDER_PARAM_NAME = "offender";
	
	private final SessionFactory sessionFactory;
	
	/**
	 * 
	 */
	public PrisonTermSummaryItemReportServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/**{@inheritDoc} */
	@Override
	public PrisonTermSummary findPrisonTermSummaryByOffender(
			final Offender offender) {
		PrisonTermSummary prisonTermSummary = (PrisonTermSummary)
				this.sessionFactory.getCurrentSession().getNamedQuery(
			FIND_PRISON_TERM_SUMMARY_BY_OFFENDER_AND_EFFECTIVE_DATE_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setReadOnly(true)
				.uniqueResult();
		
		return prisonTermSummary;
	}
}