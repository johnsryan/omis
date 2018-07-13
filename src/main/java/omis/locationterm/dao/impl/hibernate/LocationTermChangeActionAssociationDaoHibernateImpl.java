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

import org.hibernate.SessionFactory;

import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.locationterm.dao.LocationTermChangeActionAssociationDao;
import omis.locationterm.domain.LocationTerm;
import omis.locationterm.domain.LocationTermChangeActionAssociation;

/**
 * Hibernate implementation of data access object for association of location
 * term to change action.
 * 
 * @author Stephen Abson
 * @version 0.0.1 (Jul 9, 2018)
 * @since OMIS 3.0
 */
public class LocationTermChangeActionAssociationDaoHibernateImpl
		extends GenericHibernateDaoImpl<LocationTermChangeActionAssociation>
		implements LocationTermChangeActionAssociationDao {

	/* Query names. */
	
	private static final String FIND_QUERY_NAME
		= "findLocationTermChangeActionAssociation";
	
	private static final String FIND_EXCLUDING_QUERY_NAME
		= "findLocationTermChangeActionAssociationExcluding";
	
	private static final String DELETE_BY_LOCATION_TERM_QUERY_NAME
		= "deleteLocationTermChangeActionAssociationsByLocationTerm";
	
	/* Parameter names. */
	
	private static final String LOCATION_TERM_PARAM_NAME
		= "locationTerm";

	private static final String EXCLUDED_ASSOCIATIONS_PARAM_NAME
		= "excludedAssociations";
	
	/* Constructors. */

	/**
	 * Instantiates Hibernate implementation of data access object for
	 * association of location term to change action.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public LocationTermChangeActionAssociationDaoHibernateImpl(
			final SessionFactory sessionFactory, final String entityName) {
		super(sessionFactory, entityName);
	}

	/* Method implementations. */
	
	/** {@inheritDoc} */
	@Override
	public LocationTermChangeActionAssociation find(
			final LocationTerm locationTerm) {
		LocationTermChangeActionAssociation association
			= (LocationTermChangeActionAssociation)
				this.getSessionFactory().getCurrentSession()
					.getNamedQuery(FIND_QUERY_NAME)
					.setParameter(LOCATION_TERM_PARAM_NAME, locationTerm)
					.uniqueResult();
		return association;
	}

	/** {@inheritDoc} */
	@Override
	public LocationTermChangeActionAssociation findExcluding(
			final LocationTerm locationTerm,
			final LocationTermChangeActionAssociation... excludedAssociations) {
		LocationTermChangeActionAssociation association
			= (LocationTermChangeActionAssociation)
				this.getSessionFactory().getCurrentSession()
					.getNamedQuery(FIND_EXCLUDING_QUERY_NAME)
					.setParameter(LOCATION_TERM_PARAM_NAME, locationTerm)
					.setParameterList(EXCLUDED_ASSOCIATIONS_PARAM_NAME,
							excludedAssociations)
					.uniqueResult();
		return association;
	}

	/** {@inheritDoc} */
	@Override
	public int removeByLocationTerm(final LocationTerm locationTerm) {
		return this.getSessionFactory().getCurrentSession()
				.getNamedQuery(DELETE_BY_LOCATION_TERM_QUERY_NAME)
				.setParameter(LOCATION_TERM_PARAM_NAME, locationTerm)
				.executeUpdate();
	}
}