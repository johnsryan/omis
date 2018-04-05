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
import omis.location.domain.Location;
import omis.locationterm.dao.AllowedLocationChangeDao;
import omis.locationterm.domain.AllowedLocationChange;
import omis.locationterm.domain.LocationTermChangeAction;
import omis.region.domain.State;
import omis.supervision.domain.CorrectionalStatus;

/**
 * Hibernate implementation of data access object for allowed location changes.
 *
 * @author Stephen Abson
 * @version 0.0.1 (Mar 6, 2015)
 * @since OMIS 3.0
 */
public class AllowedLocationChangeDaoHibernateImpl
		extends GenericHibernateDaoImpl<AllowedLocationChange>
		implements AllowedLocationChangeDao {

	/* Queries. */
	
	private static final String
	FIND_LOCATIONS_FOR_ACTION_FOR_CORRECTIONAL_STATUS_QUERY
		= "findLocationsForTermChangeActionForCorrectionalStatus";

	private static final String
	FIND_LOCATIONS_FOR_ACTION_FOR_CORRECTIONAL_STATUS_IN_STATE_QUERY
		= "findLocationsForTermChangeActionForCorrectionalStatusInState";
	
	private static final String FIND_LOCATIONS_FOR_ANY_CHANGE_QUERY_NAME
		= "findLocationsAllowedForAnyChange";
	
	private static final String
	FIND_LOCATIONS_FOR_ANY_CHANGE_IN_STATE_QUERY_NAME
		= "findLocationsAllowedForAnyChangeInState";
	
	/* Parameter names. */
	
	private static final String ACTION_PARAM_NAME = "action";
	
	private static final String CORRECTIONAL_STATUS_PARAM_NAME
		= "correctionalStatus";

	private static final String STATE_PARAM_NAME = "state";
	
	/* Constructors. */

	/**
	 * Instantiates Hibernate implementation of data access object for
	 * allowed location changes.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public AllowedLocationChangeDaoHibernateImpl(
			final SessionFactory sessionFactory, final String entityName) {
		super(sessionFactory, entityName);
	}

	/* Method implementations. */
	
	/** {@inheritDoc} */
	@Override
	public List<Location> findLocationsForActionForCorrectionalStatus(
			final LocationTermChangeAction action,
			final CorrectionalStatus correctionalStatus) {
		@SuppressWarnings("unchecked")
		List<Location> locations = this.getSessionFactory().getCurrentSession()
				.getNamedQuery(
						FIND_LOCATIONS_FOR_ACTION_FOR_CORRECTIONAL_STATUS_QUERY)
				.setParameter(ACTION_PARAM_NAME, action)
				.setParameter(CORRECTIONAL_STATUS_PARAM_NAME,
						correctionalStatus).list();
		return locations;
	}

	/** {@inheritDoc} */
	@Override
	public List<Location> findLocationsForActionForCorrectionalStatusInState(
			final LocationTermChangeAction action,
			final CorrectionalStatus correctionalStatus,
			final State state) {
		@SuppressWarnings("unchecked")
		List<Location> locations = this.getSessionFactory().getCurrentSession()
				.getNamedQuery(
			FIND_LOCATIONS_FOR_ACTION_FOR_CORRECTIONAL_STATUS_IN_STATE_QUERY)
				.setParameter(ACTION_PARAM_NAME, action)
				.setParameter(CORRECTIONAL_STATUS_PARAM_NAME,
						correctionalStatus)
				.setParameter(STATE_PARAM_NAME,  state).list();
		return locations;
	}

	/** {@inheritDoc} */
	@Override
	public List<Location> findLocationsAllowedForAnyChange() {
		@SuppressWarnings("unchecked")
		List<Location> locations = this.getSessionFactory().getCurrentSession()
			.getNamedQuery(FIND_LOCATIONS_FOR_ANY_CHANGE_QUERY_NAME)
			.list();
		return locations;
	}

	/** {@inheritDoc} */
	@Override
	public List<Location> findLocationsAllowedForAnyChangeInState(
			final State state) {
		@SuppressWarnings("unchecked")
		List<Location> locations = this.getSessionFactory().getCurrentSession()
			.getNamedQuery(FIND_LOCATIONS_FOR_ANY_CHANGE_IN_STATE_QUERY_NAME)
			.setParameter(STATE_PARAM_NAME, state)
			.list();
		return locations;
	}
}