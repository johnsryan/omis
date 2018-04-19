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

import org.hibernate.SessionFactory;

import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.region.dao.CityCountyAssociationDao;
import omis.region.domain.City;
import omis.region.domain.CityCountyAssociation;
import omis.region.domain.County;

/**
 * Hibernate implementation of data access object for association of city to
 * county.
 *
 * @author Stephen Abson
 * @version 0.0.1 (Jul 9, 2015)
 * @since OMIS 3.0
 */
public class CityCountyAssociationDaoHibernateImpl
		extends GenericHibernateDaoImpl<CityCountyAssociation>
		implements CityCountyAssociationDao {

	/* Query names. */
	
	private static final String FIND_QUERY_NAME = "findCityCountyAssociation";
	
	private static final String FIND_EXCLUDING_QUERY_NAME
		= "findCityCountyAssociationExcluding";
	
	/* Parameter names. */
	
	private static final String CITY_PARAM_NAME = "city";
	
	private static final String COUNTY_PARAM_NAME = "county";
	
	private static final String EXCLUDED_ASSOCIATIONS_PARAM_NAME
		= "excludedAssociations";
	
	/* Constructors. */
	
	/**
	 * Instantiates Hibernate implementation of data access object for
	 * association of city to county.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public CityCountyAssociationDaoHibernateImpl(
			final SessionFactory sessionFactory, final String entityName) {
		super(sessionFactory, entityName);
	}

	/* Method implementations. */
	
	/** {@inheritDoc} */
	@Override
	public CityCountyAssociation find(final City city, final County county) {
		CityCountyAssociation association = (CityCountyAssociation)
				this.getSessionFactory().getCurrentSession()
					.getNamedQuery(FIND_QUERY_NAME)
					.setParameter(CITY_PARAM_NAME, city)
					.setParameter(COUNTY_PARAM_NAME, county)
					.uniqueResult();
		return association;
	}

	/** {@inheritDoc} */
	@Override
	public CityCountyAssociation findExcluding(
			final City city, final County county,
			final CityCountyAssociation... excludedAssociations) {
		CityCountyAssociation association = (CityCountyAssociation)
				this.getSessionFactory().getCurrentSession()
					.getNamedQuery(FIND_EXCLUDING_QUERY_NAME)
					.setParameter(CITY_PARAM_NAME, city)
					.setParameter(COUNTY_PARAM_NAME, county)
					.setParameterList(EXCLUDED_ASSOCIATIONS_PARAM_NAME,
							excludedAssociations)
					.uniqueResult();
		return association;
	}
}