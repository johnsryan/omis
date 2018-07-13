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
package omis.locationterm.dao.impl.hibernate;

import java.util.List;

import org.hibernate.SessionFactory;

import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.locationterm.dao.LocationReasonDao;
import omis.locationterm.domain.LocationReason;

/**
 * Hibernate implementation of data access object for location reasons.
 * 
 * @author Stephen Abson
 * @version 0.1.0 (Nov 8, 2013)
 * @since OMIS 3.0
 */
public class LocationReasonDaoHibernateImpl
		extends GenericHibernateDaoImpl<LocationReason>
		implements LocationReasonDao {
	
	/* Query names. */
	
	private static final String FIND_QUERY_NAME = "findLocationReason";
	
	private static final String FIND_ALL_QUERY_NAME = "findLocationReasons";
	
	/* Parameter names. */
	
	private static final String NAME_PARAM_NAME = "name";
	
	/* Constructors. */
	
	/**
	 * Instantiates an Hibernate implementation of data access object for
	 * location reasons.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public LocationReasonDaoHibernateImpl(
			final SessionFactory sessionFactory,
			final String entityName) {
		super(sessionFactory, entityName);
	}

	/* Method implementations. */
	
	/** {@inheritDoc} */
	@Override
	public LocationReason find(final String name) {
		LocationReason reason = (LocationReason) this.getSessionFactory()
				.getCurrentSession().getNamedQuery(FIND_QUERY_NAME)
				.setParameter(NAME_PARAM_NAME, name)
				.uniqueResult();
		return reason;
	}
	
	/** {@inheritDoc} */
	@Override
	public List<LocationReason> findAll() {
		@SuppressWarnings("unchecked")
		List<LocationReason> reasons = this.getSessionFactory()
				.getCurrentSession().getNamedQuery(FIND_ALL_QUERY_NAME)
				.list();
		return reasons;
	}
}