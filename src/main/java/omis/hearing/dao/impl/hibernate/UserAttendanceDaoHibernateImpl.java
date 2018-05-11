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
package omis.hearing.dao.impl.hibernate;

import java.util.List;

import org.hibernate.SessionFactory;

import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.hearing.dao.UserAttendanceDao;
import omis.hearing.domain.Hearing;
import omis.hearing.domain.UserAttendance;
import omis.user.domain.UserAccount;

/**
 * Implementation of data access object for user attendance.
 * 
 * @author Josh Divine
 * @version 0.1.0 (May 3, 2018)
 * @since OMIS 3.0
 */
public class UserAttendanceDaoHibernateImpl
	extends GenericHibernateDaoImpl<UserAttendance>
		implements UserAttendanceDao {
	
	/* Query Names */
	
	private static final String FIND_USER_ATTENDANCE_QUERY_NAME =
			"findUserAttendance";
	
	private static final String FIND_USER_ATTENDANCE_EXCLUDING_QUERY_NAME =
			"findUserAttendanceExcluding";
	
	private static final String FIND_ALL_USER_ATTENDANCE_BY_HEARING_QUERY_NAME =
			"findUserAttendanceByHearing";
	
	/* Param Names */
	
	private static final String USER_ACCOUNT_PARAM_NAME = "userAccount";
	
	private static final String HEARING_PARAM_NAME = "hearing";
	
	private static final String EXCLUDED_USER_ATTENDANCE_PARAM_NAME = 
			"excludedUserAttendance";
	
	/** Instantiates a hibernate implementation of the data access object for 
	 *  user attendance.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	protected UserAttendanceDaoHibernateImpl(
			final SessionFactory sessionFactory, final String entityName) {
		super(sessionFactory, entityName);
	}

	/**{@inheritDoc} */
	@Override
	public UserAttendance find(final Hearing hearing,
			final UserAccount userAccount) {
		UserAttendance userAttendance = (UserAttendance) this
				.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_USER_ATTENDANCE_QUERY_NAME)
				.setParameter(HEARING_PARAM_NAME, hearing)
				.setParameter(USER_ACCOUNT_PARAM_NAME, userAccount)
				.uniqueResult();
		return userAttendance;
	}

	/**{@inheritDoc} */
	@Override
	public UserAttendance findExcluding(final Hearing hearing,
			final UserAccount userAccount, 
			final UserAttendance excludedUserAttendance){
		UserAttendance userAttendance = (UserAttendance) this
				.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_USER_ATTENDANCE_EXCLUDING_QUERY_NAME)
				.setParameter(HEARING_PARAM_NAME, hearing)
				.setParameter(USER_ACCOUNT_PARAM_NAME, userAccount)
				.setParameter(EXCLUDED_USER_ATTENDANCE_PARAM_NAME, 
						excludedUserAttendance)
				.uniqueResult();
		return userAttendance;
	}

	/**{@inheritDoc} */
	@Override
	public List<UserAttendance> findByHearing(final Hearing hearing) {
		@SuppressWarnings("unchecked")
		List<UserAttendance> userAttendances = this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_ALL_USER_ATTENDANCE_BY_HEARING_QUERY_NAME)
				.setParameter(HEARING_PARAM_NAME, hearing)
				.list();
		return userAttendances;
	}
}