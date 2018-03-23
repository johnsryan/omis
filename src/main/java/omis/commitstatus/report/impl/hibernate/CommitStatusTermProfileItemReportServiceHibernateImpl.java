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
package omis.commitstatus.report.impl.hibernate;

import java.util.Date;

import org.hibernate.SessionFactory;

import omis.commitstatus.report.CommitStatusTermProfileItemReportService;
import omis.offender.domain.Offender;

/**
 * Hibernate implementation of report service for commit status term profile 
 * items.
 *
 * @author Yidong Li
 * @author Josh Divine
 * @version 0.1.2 (Mar 22, 2018)
 * @since OMIS 3.0
 */
public class CommitStatusTermProfileItemReportServiceHibernateImpl
		implements CommitStatusTermProfileItemReportService {
	
	/* Query names. */
	
	private static final String
	COUNT_BY_PERSON_AND_EFFECTIVE_DATE_QUERY_NAME
		= "countCommitStatusTermForPersonOnDate";
	
	/* Parameter names. */
	
	private static final String OFFENDER_PARAM_NAME = "person";
	
	private static final String DATE_PARAM_NAME = "date";
	
	/* Resources. */
	
	private final SessionFactory sessionFactory;
	
	/* Constructors. */
	
	/**
	 * Instantiates Hibernate implementation of report service for
	 * commit status term profile items.
	 * 
	 * @param sessionFactory session factory
	 */
	public CommitStatusTermProfileItemReportServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/* Method implementation. */
	
	/** {@inheritDoc} */
	@Override
	public Integer findCommitStatusTermCountByOffenderAndDate(
			final Offender offender, final Date effectiveDate) {
		Integer count = ((Long) this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(COUNT_BY_PERSON_AND_EFFECTIVE_DATE_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setTimestamp(DATE_PARAM_NAME, effectiveDate)
				.setReadOnly(true)
				.uniqueResult()).intValue();
		return count;
	}
}