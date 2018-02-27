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
package omis.bedplacement.report.impl.hibernate;

import java.util.Date;

import org.hibernate.SessionFactory;

import omis.bedplacement.report.BedPlacementLookupDelegate;
import omis.offender.domain.Offender;

/**
 * Bed placement lookup delegate hibernate implementation.
 * 
 * @author Joel Norris
 * @author Josh Divine
 * @version 0.1.1 (Feb 15, 2018)
 * @since OMIS 3.0
 */
public class BedPlacementLookupDelegateHibernateImpl 
	implements BedPlacementLookupDelegate {

	/* Helpers. */
	
	private final SessionFactory sessionFactory;
	
	/* Query names. */
	
	private static final String FIND_HOUSING_UNIT_NAME_BY_OFFENDER_ON_DATE
		= "findHousingUnitNameByOffenderOnDate";
	
	/* Parameter names. */
	
	private static final String OFFENDER_PARAM_NAME = "offender";
	private static final String DATE_PARAM_NAME = "date";
	
	/* Constructor. */
	
	/**
	 * Instantiates an instance of bed placement lookup delegate with the 
	 * specified session factory.
	 * 
	 * @param sessionFactory session factory
	 */
	public BedPlacementLookupDelegateHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/* Getters. */
	
	/**
	 * Returns session factory.
	 * 
	 * @return session factory
	 */
	public SessionFactory getSessionFactory() {
		return this.sessionFactory;
	}
	
	/* Method implementations. */
	
	/** {@inheritDoc} */
	@Override
	public String findUnitNameByOffender(final Offender offender, 
			final Date date) {
		String unitName = (String) this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_HOUSING_UNIT_NAME_BY_OFFENDER_ON_DATE)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setDate(DATE_PARAM_NAME, date)
				.setReadOnly(true)
				.uniqueResult();
		return unitName;
	}
}