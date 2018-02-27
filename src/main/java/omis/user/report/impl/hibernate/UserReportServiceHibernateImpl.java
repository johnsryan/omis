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
package omis.user.report.impl.hibernate;

import java.util.List;

import org.hibernate.SessionFactory;

import omis.user.report.UserReportService;
import omis.user.report.UserSummary;

/**
 * Hibernate implementation of report service for users.
 *
 * @author Stephen Abson
 * @author Josh Divine
 * @version 0.0.2 (Feb 14, 2018)
 * @since OMIS 3.0
 */
public class UserReportServiceHibernateImpl
		implements UserReportService {
	
	/* Query names. */
	
	private static final String SUMMARIZE_BY_NAME_QUERY_NAME
		= "summarizeUsersByName";
	
	private static final String SUMMARIZE_BY_USERNAME_QUERY_NAME
		= "summarizeUsersByUsername";
	
	/* Parameter names. */
	
	private static final String LAST_NAME_PARAM_NAME = "lastName";
	
	private static final String FIRST_NAME_PARAM_NAME = "firstName";
	
	private static final String USERNAME_PARAM_NAME = "username";
	
	/* Resources. */
	
	private final SessionFactory sessionFactory;

	/* Constructors. */
	
	/**
	 * Hibernate implementation of report service for users.
	 * 
	 * @param sessionFactory session factory
	 */
	public UserReportServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/* Method implementations. */
	
	/** {@inheritDoc} */
	@Override
	public List<UserSummary> summarizeByName(
			final String lastName, final String firstName) {
		@SuppressWarnings("unchecked")
		List<UserSummary> summaries = this.sessionFactory
				.getCurrentSession().getNamedQuery(SUMMARIZE_BY_NAME_QUERY_NAME)
				.setParameter(LAST_NAME_PARAM_NAME, lastName)
				.setParameter(FIRST_NAME_PARAM_NAME, firstName)
				.setReadOnly(true)
				.list();
		return summaries;
	}

	/** {@inheritDoc} */
	@Override
	public List<UserSummary> summarizeByUsername(final String username) {
		@SuppressWarnings("unchecked")
		List<UserSummary> summaries = this.sessionFactory
				.getCurrentSession().getNamedQuery(
						SUMMARIZE_BY_USERNAME_QUERY_NAME)
				.setParameter(USERNAME_PARAM_NAME, username)
				.setReadOnly(true)
				.list();
		return summaries;
	}
}