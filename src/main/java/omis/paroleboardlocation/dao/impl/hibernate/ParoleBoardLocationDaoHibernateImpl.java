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
package omis.paroleboardlocation.dao.impl.hibernate;

import java.util.List;

import org.hibernate.SessionFactory;
import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.location.domain.Location;
import omis.paroleboardlocation.dao.ParoleBoardLocationDao;
import omis.paroleboardlocation.domain.ParoleBoardLocation;

/**
 * Parole Board Location DAO Hibernate Implementation.
 * 
 * @author Annie Wahl
 * @author Josh Divine 
 * @version 0.1.1 (Feb 20, 2018)
 * @since OMIS 3.0
 */
public class ParoleBoardLocationDaoHibernateImpl
		extends GenericHibernateDaoImpl<ParoleBoardLocation>
		implements ParoleBoardLocationDao {
	
	/* Queries. */
	
	private static final String FIND_QUERY_NAME = "findParoleBoardLocation";
	
	private static final String FIND_EXCLUDING_QUERY_NAME = 
			"findParoleBoardLocationExcluding";
	
	private static final String FIND_UNASSOCIATED_LOCATIONS_QUERY_NAME = 
			"findUnassociatedSupervisoryOrganizationLocations";
	
	/* Parameters. */
	
	private static final String LOCATION_PARAM_NAME = "location";
	
	private static final String EXCLUDED_PARAM_NAME = 
			"excludedParoleBoardLocation";
	
	/**
	 * Instantiates a hibernate implementation of the data access object for 
	 * parole board location.
	 *  
	 * @param sessionFactory - Session Factory
	 * @param entityName - String entity name
	 */
	public ParoleBoardLocationDaoHibernateImpl(
			final SessionFactory sessionFactory,
			final String entityName) {
		super(sessionFactory, entityName);
	}

	/** {@inheritDoc} */
	@Override
	public ParoleBoardLocation find(final Location location) {
		ParoleBoardLocation paroleBoardLocation = (ParoleBoardLocation) this
				.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_QUERY_NAME)
				.setParameter(LOCATION_PARAM_NAME, location)
				.uniqueResult();
		return paroleBoardLocation;
	}

	/** {@inheritDoc} */
	@Override
	public ParoleBoardLocation findExcluding(final Location location, 
			final ParoleBoardLocation excludedParoleBoardLocation) {
		ParoleBoardLocation paroleBoardLocation = (ParoleBoardLocation) this
				.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_EXCLUDING_QUERY_NAME)
				.setParameter(LOCATION_PARAM_NAME, location)
				.setParameter(EXCLUDED_PARAM_NAME, excludedParoleBoardLocation)
				.uniqueResult();
		return paroleBoardLocation;
	}

	/** {@inheritDoc} */
	@Override
	public List<Location> findUnassociatedSupervisoryOrganizationLocations(
			final ParoleBoardLocation paroleBoardLocation) {
		@SuppressWarnings("unchecked")
		List<Location> locations = this.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_UNASSOCIATED_LOCATIONS_QUERY_NAME)
				.setParameter(EXCLUDED_PARAM_NAME, paroleBoardLocation)
				.list();
		return locations;
	}
}