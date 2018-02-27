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

import org.hibernate.SessionFactory;

import omis.person.domain.Person;
import omis.user.report.UserAccountProfileItemReportService;

/** 
 * Hibernate implementation of user account profile item report service.
 * 
 * @author Ryan Johns
 * @author Josh Divine
 * @version 0.1.1 (Feb 14, 2018)
 * @since OMIS 3.0 
 */
public class UserAccountProfileItemReportServiceHibernateImpl 
	implements UserAccountProfileItemReportService {
	private static final String 
		FIND_USER_ACCOUNT_COUNT_BY_USER_QUERY_NAME
		= "findUserAccountCountByUser";
	private static final String USER_PARAM_NAME = "user";
	
	private final SessionFactory sessionFactory;
	
	/** Constructor.
	 * @param sessionFactory session factory. */
	public UserAccountProfileItemReportServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/** {@inheritDoc} */
	@Override
	public Integer findUserAccountCountByUser(final Person user) {
		return ((Long) this.sessionFactory.getCurrentSession()
				.getNamedQuery(FIND_USER_ACCOUNT_COUNT_BY_USER_QUERY_NAME)
				.setParameter(USER_PARAM_NAME, user)
				.setReadOnly(true)
				.uniqueResult()).intValue();
	}
}