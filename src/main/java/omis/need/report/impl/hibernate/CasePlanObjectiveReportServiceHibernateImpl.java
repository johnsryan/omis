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

import omis.need.domain.NeedDomain;
import omis.need.report.CasePlanObjectiveReportService;
import omis.need.report.CasePlanObjectiveSummary;
import omis.offender.domain.Offender;

import org.hibernate.SessionFactory;

/**
 * Case plan objective report service hibernate implementation.
 * 
 * @author Joel Norris
 * @author Josh Divine
 * @version 0.1.1 (Feb 15, 2018)
 * @since OMIS 3.0
 */
public class CasePlanObjectiveReportServiceHibernateImpl
	implements CasePlanObjectiveReportService {
	
	/* Session factory. */
	
	private final SessionFactory sessionFactory;
	
	/* Parameter names. */
	
	private final String OFFENDER_PARAM_NAME = "offender";
	
	private final String NEED_DOMAIN_PARAM_NAME = "needDomain";
	
	/* Query names. */
	
	private static final String FIND_CASE_PLAN_OBJECTIVES_BY_DOMAIN_QUERY_NAME
		= "findCasePlanObjectivesByDomain";
	
	private static final String FIND_CASE_PLAN_OBJECTIVES_BY_OFFENDER_QUERY_NAME
		= "findCasePlanObjectivesByOffender";
	
	/**
	 * Instantiates a case plan objective report service with the specified
	 * session factory.
	 * 
	 * @param sessionFactory session factory
	 */
	public CasePlanObjectiveReportServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/* Method implementations. */
	
	/** {@inheritDoc} */
	@Override
	public List<CasePlanObjectiveSummary> findCasePlanObjectiveSummariesByDomain(
			final Offender offender, final NeedDomain needDomain) {
		@SuppressWarnings("unchecked")
		List<CasePlanObjectiveSummary> summaries = 
				this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(FIND_CASE_PLAN_OBJECTIVES_BY_DOMAIN_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setParameter(NEED_DOMAIN_PARAM_NAME, needDomain)
				.setReadOnly(true)
				.list();
		return summaries;
	}

	/** {@inheritDoc} */
	@Override
	public List<CasePlanObjectiveSummary> 
			findCasePlanObjectiveSummariesByOffender(final Offender offender) {
		@SuppressWarnings("unchecked")
		List<CasePlanObjectiveSummary> summaries = 
				this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(FIND_CASE_PLAN_OBJECTIVES_BY_OFFENDER_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setReadOnly(true)
				.list();
		return summaries;
	}
}