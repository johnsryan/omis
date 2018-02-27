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
package omis.program.report.impl.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;

import omis.offender.domain.Offender;
import omis.program.report.ProgramPlacementReportService;
import omis.program.report.ProgramPlacementSummary;

/**
 * Hibernate implementation of report service for program placements.
 *
 * @author Stephen Abson
 * @author Josh Divine
 * @version 0.0.2 (Feb 14, 2018)
 * @since OMIS 3.0
 */
public class ProgramPlacementReportServiceHibernateImpl
		implements ProgramPlacementReportService {

	/* Query names. */
	
	private final static String SUMMARIZE_BY_OFFENDER_QUERY_NAME
		= "summarizeProgramPlacementsByOffender";
	
	/* Parameter names. */
	
	private final static String OFFENDER_PARAM_NAME = "offender";
	
	private final static String EFFECTIVE_DATE_PARAM_NAME = "effectiveDate";
	
	/* Resources. */
	
	private final SessionFactory sessionFactory;
	
	/**
	 * Instantiates Hibernate implementation of report service for program
	 * placements.
	 * 
	 * @param sessionFactory session factory
	 */
	public ProgramPlacementReportServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/** {@inheritDoc} */
	@Override
	public List<ProgramPlacementSummary> summarizeByOffender(
			final Offender offender, final Date effectiveDate) {
		@SuppressWarnings("unchecked")
		List<ProgramPlacementSummary> summaries
			= this.sessionFactory.getCurrentSession()
				.getNamedQuery(SUMMARIZE_BY_OFFENDER_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setTimestamp(EFFECTIVE_DATE_PARAM_NAME, effectiveDate)
				.setReadOnly(true)
				.list();
		return summaries;
	}
}