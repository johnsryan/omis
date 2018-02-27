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

import omis.user.report.UserAccountReportService;
import omis.user.report.UserAccountSearchResult;

/**
 * Implementation of report service for user accounts.
 * 
 * @author Stephen Abson
 * @author Josh Divine
 * @version 0.1.1 (Feb 14, 2018)
 * @since OMIS 3.0
 */
public class UserAccountReportServiceHibernateImpl
		implements UserAccountReportService {

	private static final String SEARCH_USER_ACCOUNT_BY_QUERY_QUERY_NAME
		= "searchUserAccountsByQuery";

	private static final String QUERY_PARAM_NAME = "query";
	
	private SessionFactory sessionFactory;

	/**
	 * Instantiates an implementation of report service for user accounts.
	 * 
	 * @param sessionFactory session factory
	 */
	public UserAccountReportServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/** {@inheritDoc} */
	@Override
	public List<UserAccountSearchResult> searchForUserAccount(
			final String query) {
		@SuppressWarnings("unchecked")
		List<UserAccountSearchResult> results = this.sessionFactory
				.getCurrentSession().getNamedQuery(
						SEARCH_USER_ACCOUNT_BY_QUERY_QUERY_NAME)
				.setParameter(QUERY_PARAM_NAME, query.toUpperCase())
				.setReadOnly(true)
				.list();
		return results;
	}
}