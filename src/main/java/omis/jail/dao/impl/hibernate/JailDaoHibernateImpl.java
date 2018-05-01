/*
 *  OMIS - Offender Management Information System
 *  Copyright (C) 2011 - 2017 State of Montana
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package omis.jail.dao.impl.hibernate;

import java.util.List;

import org.hibernate.SessionFactory;

import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.jail.dao.JailDao;
import omis.jail.domain.Jail;
import omis.organization.domain.Organization;

/**
 * Hibernate implementation of data access object for jails.
 *
 * @author Stephen Abson
 * @version 0.0.1 (Apr 18, 2016)
 * @since OMIS 3.0
 */
public class JailDaoHibernateImpl
		extends GenericHibernateDaoImpl<Jail>
		implements JailDao {
	
	/* Query names. */
	
	private static final String FIND_ALL_QUERY_NAME = "findJails";
	
	private static final String FIND_BY_ORGANIZATION_QUERY_NAME
		= "findJailsByOrganization";
	
	/* Parameter names. */
	
	private static final String ORGANIZATION_PARAM_NAME = "organization";
	
	/* Constructors. */

	/**
	 * Instantiates Hibernate implementation of data access object for jails.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public JailDaoHibernateImpl(SessionFactory sessionFactory,
			String entityName) {
		super(sessionFactory, entityName);
	}
	
	/* Method implementations. */
	
	/** {@inheritDoc} */
	@Override
	public List<Jail> findAll() {
		@SuppressWarnings("unchecked")
		List<Jail> jails = this.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_ALL_QUERY_NAME).list();
		return jails;
	}
	
	/** {@inheritDoc} */
	@Override
	public List<Jail> findByOrganization(
			final Organization organization) {
		@SuppressWarnings("unchecked")
		List<Jail> jails = this.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_BY_ORGANIZATION_QUERY_NAME)
				.setParameter(ORGANIZATION_PARAM_NAME, organization)
				.list();
		return jails;
	}
}