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
package omis.workassignment.report.impl.hibernate;

import java.util.Date;

import org.hibernate.SessionFactory;

import omis.offender.domain.Offender;
import omis.workassignment.report.WorkAssignmentProfileItemReportService;

/**
 * Hibernate implementation of report service for work assignment profile items.
 *
 * @author Stephen Abson
 * @author Josh Divine
 * @version 0.0.2 (Feb 14, 2018)
 * @since OMIS 3.0
 */
public class WorkAssignmentProfileItemReportServiceHibernateImpl
		implements WorkAssignmentProfileItemReportService {

	/* Query names. */
	
	private static final String COUNT_BY_OFFENDER_ON_DATE_QUERY_NAME
		= "countWorkAssignmentsByOffenderOnDate";
	
	/* Parameter names. */
	
	private static final String OFFENDER_PARAM_NAME = "offender";
	
	private static final String DATE_PARAM_NAME = "date";
	
	/* Resources. */
	
	private final SessionFactory sessionFactory;
	
	/* Constructors. */
	
	/**
	 * Instantiates Hibernate implementation of report service for work
	 * assignment profile items.
	 * 
	 * @param sessionFactory session factory
	 */
	public WorkAssignmentProfileItemReportServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/* Method implementations. */
	
	/** {@inheritDoc} */
	@Override
	public Integer findWorkAssignmentCountForOffenderOnDate(
			final Offender offender, final Date date) {
		Long count = (Long) this.sessionFactory.getCurrentSession()
				.getNamedQuery(COUNT_BY_OFFENDER_ON_DATE_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setTimestamp(DATE_PARAM_NAME, date)
				.setReadOnly(true)
				.uniqueResult();
		return count.intValue();
	}
}