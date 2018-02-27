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
package omis.paroleboardmember.report.impl.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;

import omis.paroleboardmember.report.ParoleBoardMemberSummary;
import omis.paroleboardmember.report.ParoleBoardMemberSummaryReportService;

/**
 * Hibernate implementation of the parole board member summary report service.
 * 
 * @author Josh Divine
 * @version 0.1.1 (Feb 14, 2018)
 * @since OMIS 3.0
 */
public class ParoleBoardMemberSummaryReportServiceHibernateImpl 
		implements ParoleBoardMemberSummaryReportService {

	/* Queries. */
	private static final String FIND_PAROLE_BOARD_MEMBERS_BY_DATE_QUERY_NAME = 
			"findParoleBoardMembersByDate";
	
	private static final String FIND_PAROLE_BOARD_MEMBERS_BY_DATES_QUERY_NAME = 
			"findParoleBoardMembersByDates";
	
	/* Parameters.*/ 
	private static final String EFFECTIVE_PARAMETER_NAME = "effectiveDate";
	
	private static final String START_PARAMETER_NAME = "startDate";
	
	private static final String END_PARAMETER_NAME = "endDate";
	
	/* Members. */
	private final SessionFactory sessionFactory;
	
	/**
	 * Constructor.
	 *
	 * @param sessionFactory session factory
	 */
	public ParoleBoardMemberSummaryReportServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/** {@inheritDoc} */
	@Override
	public List<ParoleBoardMemberSummary> findByDate(final Date effectiveDate) {
		@SuppressWarnings("unchecked")
		List<ParoleBoardMemberSummary> summaries = this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(FIND_PAROLE_BOARD_MEMBERS_BY_DATE_QUERY_NAME)
				.setTimestamp(EFFECTIVE_PARAMETER_NAME, effectiveDate)
				.setReadOnly(true)
				.list();
		return summaries;
	}

	/** {@inheritDoc} */
	@Override
	public List<ParoleBoardMemberSummary> findByDates(final Date startDate, 
			final Date endDate) {
		@SuppressWarnings("unchecked")
		List<ParoleBoardMemberSummary> summaries = this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(FIND_PAROLE_BOARD_MEMBERS_BY_DATES_QUERY_NAME)
				.setTimestamp(START_PARAMETER_NAME, startDate)
				.setTimestamp(END_PARAMETER_NAME, endDate)
				.setReadOnly(true)
				.list();
		return summaries;
	}
}