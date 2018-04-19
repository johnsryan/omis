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

import omis.country.domain.Country;
import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.region.dao.CityDao;
import omis.region.domain.City;
import omis.region.domain.County;
import omis.region.domain.State;

/**
 * Hibernate entity configurable data access object for cities.
 * 
 * @author Stephen Abson
 * @author Yidong Li
 * @version 0.1.0 (June 23, 2015)
 * @since OMIS 3.0
 */
public class CityDaoHibernateImpl
		extends GenericHibernateDaoImpl<City>
		implements CityDao {

	/* Query names. */
	
	private static final String FIND_QUERY_NAME = "findCity";
	
	private static final String FIND_ALL_QUERY_NAME = "findCities";
	
	private static final String FIND_BY_STATE_QUERY_NAME = "findCitiesByState";
	
	private static final String FIND_BY_COUNTRY_QUERY_NAME
		= "findCitiesByCountry";
	
	private static final String FIND_BY_COUNTRY_WITHOUT_STATE_QUERY_NAME
		= "findCitiesByCountryWithoutState";
	
	/* Parameter names. */
	
	private static final String NAME_PARAM_NAME = "name";
	
	private static final String STATE_PARAM_NAME = "state";
	
	private static final String COUNTRY_PARAM_NAME = "country";

	/* Property names. */
	
	private static final String STATE_PROPERTY_NAME = "state";

	/* Constructors. */
	
	/**
	 * Instantiates an Hibernate implementation of data access object for
	 * cities with the specified resources.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public CityDaoHibernateImpl(final SessionFactory sessionFactory,
			final String entityName) {
		super(sessionFactory, entityName);
	}
	
	/* Method implementation. */
	
	/** {@inheritDoc} */
	@Override
	public List<City> findAll() {
		@SuppressWarnings("unchecked")
		List<City> cities = getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_ALL_QUERY_NAME).list();
		return cities;
	}
	
	/** {@inheritDoc} */
	@Override
	public List<City> findByState(final State state) {
		@SuppressWarnings("unchecked")
		List<City> cities = getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_BY_STATE_QUERY_NAME)
				.setParameter(STATE_PARAM_NAME, state)
				.list();
		return cities;
	}

	/** {@inheritDoc} */
	@Override
	public List<City> findByCounty(final County county) {
		// TODO Implement find cities by county
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/** {@inheritDoc} */
	@Override
	public City find(
			final String name, final State state, final Country country) {
		City city = (City) this.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_QUERY_NAME)
				.setParameter(NAME_PARAM_NAME, name)
				.setParameter(STATE_PARAM_NAME, state,
						this.getEntityPropertyType(STATE_PROPERTY_NAME))
				.setParameter(COUNTRY_PARAM_NAME, country)
				.uniqueResult();
		return city;
	}

	/** {@inheritDoc} */
	@Override
	public List<City> findByCountry(final Country country) {
		@SuppressWarnings("unchecked")
		List<City> cities = this.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_BY_COUNTRY_QUERY_NAME)
				.setParameter(COUNTRY_PARAM_NAME, country)
				.list();
		return cities;
	}

	/** {@inheritDoc} */
	@Override
	public List<City> findByCountryWithoutState(final Country country) {
		@SuppressWarnings("unchecked")
		List<City> cities = this.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_BY_COUNTRY_WITHOUT_STATE_QUERY_NAME)
				.setParameter(COUNTRY_PARAM_NAME, country)
				.list();
		return cities;
	}
}