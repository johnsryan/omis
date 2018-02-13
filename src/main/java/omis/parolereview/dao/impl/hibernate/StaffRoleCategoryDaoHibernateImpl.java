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
package omis.parolereview.dao.impl.hibernate;

import org.hibernate.SessionFactory;

import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.parolereview.dao.StaffRoleCategoryDao;
import omis.parolereview.domain.StaffRoleCategory;

/**
 * Hibernate implementation of the staff role category data access 
 * object.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 12, 2018)
 * @since OMIS 3.0
 */
public class StaffRoleCategoryDaoHibernateImpl 
		extends GenericHibernateDaoImpl<StaffRoleCategory>
		implements StaffRoleCategoryDao {

	/* Queries. */
	
	private static final String FIND_QUERY_NAME = 
			"findStaffRoleCategory";
	
	private static final String FIND_EXCLUDING_QUERY_NAME = 
			"findStaffRoleCategoryExcluding";
	
	/* Parameters. */
	
	private static final String NAME_PARAM_NAME = "name";
	
	private static final String EXCLUDED_PARAM_NAME = 
			"excludedStaffRoleCategory";
	
	/** Instantiates a hibernate implementation of the data access object for 
	 *  staff role category.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public StaffRoleCategoryDaoHibernateImpl(
			final SessionFactory sessionFactory, final String entityName) {
		super(sessionFactory, entityName);
	}

	/** {@inheritDoc} */
	@Override
	public StaffRoleCategory find(final String name) {
		StaffRoleCategory category = (StaffRoleCategory) this
				.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_QUERY_NAME)
				.setParameter(NAME_PARAM_NAME, name)
				.uniqueResult();
		return category;
	}

	/** {@inheritDoc} */
	@Override
	public StaffRoleCategory findExcluding(final String name,
			final StaffRoleCategory excludedStaffRoleCategory) {
		StaffRoleCategory category = (StaffRoleCategory) this
				.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_EXCLUDING_QUERY_NAME)
				.setParameter(NAME_PARAM_NAME, name)
				.setParameter(EXCLUDED_PARAM_NAME, excludedStaffRoleCategory)
				.uniqueResult();
		return category;
	}
}