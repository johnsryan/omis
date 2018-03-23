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
package omis.locationterm.report.impl.hibernate;

import java.util.Date;

import org.hibernate.SessionFactory;

import omis.locationterm.report.LocationTermProfileItemReportService;
import omis.offender.domain.Offender;

/**
 * Hibernate implementation of report service for profile item for location
 * term.
 *
 * @author Stephen Abson
 * @author Josh Divine
 * @version 0.0.2 (Feb 14, 2018)
 * @since OMIS 3.0
 */
public class LocationTermProfileItemReportServiceHibernateImpl
		implements LocationTermProfileItemReportService {

	/* Query names. */
	
	private final static String FIND_FOR_OFFENDER_ON_DATE_QUERY_NAME
		= "findLocationTermByOffenderOnDate";
	
	/* Parameter names. */
	
	private final static String OFFENDER_PARAM_NAME = "offender";
	
	private final static String DATE_PARAM_NAME = "date";
	
	/* Resources. */
	
	private final SessionFactory sessionFactory;
	
	/* Constructors. */
	
	/**
	 * Instantiates Hibernate implementation of report service for profile item
	 * for location term.
	 * 
	 * @param sessionFactory session factory
	 */
	public LocationTermProfileItemReportServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/* Method implementations. */
	
	/** {@inheritDoc} */
	@Override
	public boolean findLocationExistenceByOffenderAndDate(
			final Offender offender, final Date effectiveDate) {
		return !this.sessionFactory.getCurrentSession().getNamedQuery(
				FIND_FOR_OFFENDER_ON_DATE_QUERY_NAME)
			.setParameter(OFFENDER_PARAM_NAME, offender)
			.setTimestamp(DATE_PARAM_NAME, effectiveDate)
			.setReadOnly(true)
			.list().isEmpty();
	}
}