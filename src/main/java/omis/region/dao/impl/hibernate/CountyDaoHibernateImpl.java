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
package omis.region.dao.impl.hibernate;

import java.util.List;

import org.hibernate.SessionFactory;

import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.region.dao.CountyDao;
import omis.region.domain.County;
import omis.region.domain.State;

/**
 * Hibernate entity configurable implementation of data access object for
 * counties.
 * 
 * @author Stephen Abson
 * @version 0.1.0 (Feb 6, 2013)
 * @since OMIS 3.0
 */
public class CountyDaoHibernateImpl
		extends GenericHibernateDaoImpl<County>
		implements CountyDao {

	/* Query names. */
	
	private static final String FIND_QUERY_NAME = "findCounty";
	
	private static final String FIND_BY_STATE_QUERY_NAME
		= "findAllCountiesInState";
	
	private static final String FIND_ALL_QUERY_NAME = "findCounties";
	
	/* Parameter names. */
	
	private static final String NAME_PARAM_NAME = "name";
	
	private static final String STATE_PARAM_NAME = "state";

	/* Constructors. */
	
	/**
	 * Instantiates an Hibernate implementation of data access object for
	 * counties with the specified resources.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public CountyDaoHibernateImpl(final SessionFactory sessionFactory,
			final String entityName) {
		super(sessionFactory, entityName);
	}
	
	/** {@inheritDoc} */
	@Override
	public List<County> findAll() {
		@SuppressWarnings("unchecked")
		List<County> counties = getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_ALL_QUERY_NAME).list();
		return counties;
	}
	
	/* Method implementations. */
	
	/** {@inheritDoc} */
	@Override
	public List<County> findByState(final State state) {
		@SuppressWarnings("unchecked")
		List<County> counties = getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_BY_STATE_QUERY_NAME)
				.setParameter(STATE_PARAM_NAME, state)
				.list();
		return counties;
	}

	/** {@inheritDoc} */
	@Override
	public County find(final String name, final State state) {
		County county = (County)this.getSessionFactory()
				.getCurrentSession().getNamedQuery(FIND_QUERY_NAME)
				.setParameter(NAME_PARAM_NAME, name)
				.setParameter(STATE_PARAM_NAME, state)
				.uniqueResult();
		return county;
	}
}