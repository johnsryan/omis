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
package omis.need.report.impl.hibernate;

import java.util.List;

import omis.need.report.NeedDomainSummary;
import omis.need.report.NeedDomainSummaryReportService;
import omis.offender.domain.Offender;

import org.hibernate.SessionFactory;

/**
 * Criminogenic domain summary report service hibernate implementation.
 * 
 * @author Joel Norris
 * @author Josh Divine
 * @version 0.1.1 (Feb 14, 2018)
 * @since OMIS 3.0
 */
public class NeedDomainSummaryReportServiceHibernateImpl
	implements NeedDomainSummaryReportService {

	/* Query names. */
	
	private static final String SUMMARIUZE_DOMAINS_BY_OFFENDER_QUERY_NAME
	= "summarizeNeedDomainsByOffender";
	
	/* Parameter names. */
	
	private static final String OFFENDER_PARAM_NAME = "offender";
	
	/* Session factory. */
	 
	private SessionFactory sessionFactory;
	
	/**
	 * Instantiates an instance of need domain summary report
	 * service with the specified session factory.
	 * 
	 * @param sessionFactory session factory
	 */
	public NeedDomainSummaryReportServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/** {@inheritDoc} */
	@Override
	public List<NeedDomainSummary> summarizeDomainsByOffender(
			final Offender offender) {
		@SuppressWarnings("unchecked")
		List<NeedDomainSummary> summaries = 
				this.sessionFactory.getCurrentSession()
				.getNamedQuery(SUMMARIUZE_DOMAINS_BY_OFFENDER_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setReadOnly(true)
				.list();
		return summaries;
	}
}