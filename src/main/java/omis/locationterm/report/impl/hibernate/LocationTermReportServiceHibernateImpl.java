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
import java.util.List;

import org.hibernate.SessionFactory;

import omis.locationterm.report.LocationTermReportService;
import omis.locationterm.report.LocationTermSummary;
import omis.offender.domain.Offender;

/**
 * Hibernate implementation of location term report service.
 *
 * @author Stephen Abson
 * @author Josh Divine
 * @version 0.0.2 (Feb 15, 2018)
 * @since OMIS 3.0
 */
public class LocationTermReportServiceHibernateImpl
		implements LocationTermReportService {
	
	/* Query names. */
	
	private static final String SUMMARIZE_BY_OFFENDER_ON_DATE_QUERY_NAME
		= "summarizeLocationTermsByOffenderOnDate";
	
	/* Model keys. */
	
	private static final String OFFENDER_MODEL_KEY = "offender";
	
	private static final String EFFECTIVE_DATE_MODEL_KEY = "effectiveDate";
	
	/* Resources. */
	
	private final SessionFactory sessionFactory;

	/* Constructors. */
	
	/**
	 * Instantiates Hibernate implementation of location term report service.
	 * 
	 * @param sessionFactory session factory
	 */
	public LocationTermReportServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/* Method implementations. */
	
	/** {@inheritDoc} */
	@Override
	public List<LocationTermSummary> summarizeByOffenderOnDate(
			final Offender offender, final Date effectiveDate) {
		@SuppressWarnings("unchecked")
		List<LocationTermSummary> summaries
			= this.sessionFactory.getCurrentSession()
				.getNamedQuery(SUMMARIZE_BY_OFFENDER_ON_DATE_QUERY_NAME)
				.setParameter(OFFENDER_MODEL_KEY, offender)
				.setTimestamp(EFFECTIVE_DATE_MODEL_KEY, effectiveDate)
				.setReadOnly(true)
				.list();
		return summaries;
	}
}