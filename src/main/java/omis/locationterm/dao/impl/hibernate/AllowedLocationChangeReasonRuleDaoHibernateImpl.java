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
import omis.locationterm.dao.AllowedLocationChangeReasonRuleDao;
import omis.locationterm.domain.AllowedLocationChangeReasonRule;
import omis.locationterm.domain.LocationReason;
import omis.locationterm.domain.LocationTermChangeAction;

/**
 * Hibernate implementation of data access object for allowed location change
 * reason rule.
 *
 * @author Stephen Abson
 * @version 0.0.1
 * @since OMIS 3.0
 */
public class AllowedLocationChangeReasonRuleDaoHibernateImpl
		extends GenericHibernateDaoImpl<AllowedLocationChangeReasonRule>
		implements AllowedLocationChangeReasonRuleDao {
	
	/* Queries. */

	private static final String
	FIND_LOCATION_REASONS_FOR_CHANGE_ACTION_QUERY_NAME
		= "findLocationReasonsForChangeAction";
	
	private static final String
	FIND_LOCATION_REASONS_ALLOWED_FOR_LOCATION_QUERY_NAME
		= "findLocationReasonsAllowedForLocation";
	
	/* Parameter names. */
	
	private static final String CHANGE_ACTION_PARAM_NAME = "changeAction";

	private static final String LOCATION_PARAM_NAME = "location";
	
	/* Constructors. */

	/**
	 * Instantiates Hibernate implementation of data access object for allowed
	 * location change.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public AllowedLocationChangeReasonRuleDaoHibernateImpl(
			final SessionFactory sessionFactory, final String entityName) {
		super(sessionFactory, entityName);
	}

	/* Method implementations. */
	
	/** {@inheritDoc} */
	@Override
	public List<LocationReason> findLocationReasonsForChangeAction(
			final LocationTermChangeAction changeAction) {
		@SuppressWarnings("unchecked")
		List<LocationReason> reasons = this.getSessionFactory()
				.getCurrentSession().getNamedQuery(
						FIND_LOCATION_REASONS_FOR_CHANGE_ACTION_QUERY_NAME)
				.setParameter(CHANGE_ACTION_PARAM_NAME, changeAction)
				.list();
		return reasons;
	}

	/** {@inheritDoc} */
	@Override
	public List<LocationReason> findLocationReasonsAllowedForLocation(
			final Location location) {
		@SuppressWarnings("unchecked")
		List<LocationReason> reasons = this.getSessionFactory()
				.getCurrentSession().getNamedQuery(
						FIND_LOCATION_REASONS_ALLOWED_FOR_LOCATION_QUERY_NAME)
				.setParameter(LOCATION_PARAM_NAME, location)
				.list();
		return reasons;
	}
}