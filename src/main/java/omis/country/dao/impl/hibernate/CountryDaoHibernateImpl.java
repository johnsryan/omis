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
package omis.country.dao.impl.hibernate;

import java.util.List;

import omis.country.dao.CountryDao;
import omis.country.domain.Country;
import omis.dao.impl.hibernate.GenericHibernateDaoImpl;

import org.hibernate.SessionFactory;

/**
 * Hibernate entity configurable implementation of data access object for
 * countries.
 * 
 * @author Stephen Abson
 * @version 0.1.0 (Feb 6, 2013)
 * @since OMIS 3.0
 */
public class CountryDaoHibernateImpl
		extends GenericHibernateDaoImpl<Country>
		implements CountryDao {
	
	private static final String FIND_ALL_QUERY_NAME = "findAllContries";
	
	private static final String FIND_QUERY_NAME = "findCountry";
	
	private static final String NAME_PARAM_NAME = "name";

	/**
	 * Instantiates an Hibernate implementation of data access object for
	 * counties with the specified resources.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public CountryDaoHibernateImpl(final SessionFactory sessionFactory,
			final String entityName) {
		super(sessionFactory, entityName);
	}
	
	/** {@inheritDoc} */
	@Override
	public List<Country> findAll() {
		@SuppressWarnings("unchecked")
		List<Country> countries = this.getSessionFactory()
				.getCurrentSession().getNamedQuery(
						FIND_ALL_QUERY_NAME).list();
		return countries;
	}
	
	/** {@inheritDoc} */
	@Override
	public Country find(final String name) {
		Country country = (Country) this.getSessionFactory()
				.getCurrentSession().getNamedQuery(
						FIND_QUERY_NAME)
				.setParameter(NAME_PARAM_NAME, name)
				.uniqueResult();
		return country;
	}
}