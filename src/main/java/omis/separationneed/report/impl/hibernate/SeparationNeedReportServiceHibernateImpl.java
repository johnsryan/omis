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
package omis.separationneed.report.impl.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;

import omis.offender.domain.Offender;
import omis.separationneed.report.SeparationNeedReportService;
import omis.separationneed.report.SeparationNeedSummary;

/**
 * Separation need report service hibernate implementation.
 * 
 * @author Joel Norris
 * @author Josh Divine
 * @version 0.1.1 (Feb 14, 2018)
 * @since OMIS 3.0
 */
public class SeparationNeedReportServiceHibernateImpl 
	implements SeparationNeedReportService {
	
	private SessionFactory sessionFactory;
	
	/* Query names */
	
	private static final String
		SUMMARIZE_SEPARATION_NEEDS_BY_OFFENDER_ON_DATE_QUERY_NAME
		= "summarizeSeparationNeedsByOffenderOnDate";
	
	/* Parameter names */
	
	private static final String OFFENDER_PARAM_NAME = "offender";
	
	private static final String DATE_PARAM_NAME = "date";
	
	/* Constructors */
	
	/**
	 * Instantiates an instance of separation need report service.
	 * 
	 * @param sessionFactory session factory
	 */
	public SeparationNeedReportServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/* Method implementations */

	/** {@inheritDoc} */
	@Override
	public List<SeparationNeedSummary> summarizeByOffenderOnDate(
			final Offender offender, final Date date) {
		@SuppressWarnings("unchecked")
		List<SeparationNeedSummary> summaries = this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(
					SUMMARIZE_SEPARATION_NEEDS_BY_OFFENDER_ON_DATE_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setParameter(DATE_PARAM_NAME, date)
				.setReadOnly(true)
				.list();
		return summaries;
	}
}