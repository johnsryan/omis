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
package omis.health.report.impl.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;

import omis.facility.domain.Facility;
import omis.health.report.ProviderReportService;
import omis.health.report.ProviderSummary;

/** 
 * Hibernate implementation of the Health provider report service.
 * 
 * @author Ryan Johns
 * @author Josh Divine
 * @version 0.1.1 (Feb 14, 2018)
 * @since OMIS 3.0 
 */
public class ProviderReportServiceHibernateImpl
		implements ProviderReportService {
	
	/* Query names. */
	
	private static final String 
			FIND_HEALTH_PROVIDERS_BY_FACILITY_DATERANGE_QUERY =
					"findHealthProvidersByFacilityDateRange";

	private static final String
			FIND_HEALTH_PROVIDERS_BY_FACILITY_DATE_QUERY =
					"findHealthProvidersByFacilityDate";

	/* Parameter names. */
	
	private final static String FACILITY_PARAM_NAME = "facility";
	
	private final static String START_DATE_PARAM_NAME = "startDate";
	
	private final static String END_DATE_PARAM_NAME = "endDate";
	
	private final static String DATE_PARAM_NAME = "date";
	
	/* Resources. */
	
	private final SessionFactory sessionFactory;

	/** Constructor.
	 * @param sessionFactory session factory. */
	public ProviderReportServiceHibernateImpl(
			final SessionFactory sessionFactory)  {
		this.sessionFactory = sessionFactory;
	}

	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	@Override
	public List<ProviderSummary> findHealthProviders(final Facility facility,
			final Date startDate, final Date endDate) {
		return (List<ProviderSummary>) this.sessionFactory.getCurrentSession()
				.getNamedQuery(
						FIND_HEALTH_PROVIDERS_BY_FACILITY_DATERANGE_QUERY)
				.setParameter(FACILITY_PARAM_NAME, facility)
				.setTimestamp(START_DATE_PARAM_NAME, startDate)
				.setTimestamp(END_DATE_PARAM_NAME, endDate)
				.setReadOnly(true)
				.list();
	}

	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	@Override
	public List<ProviderSummary> findHealthProviders(final Facility facility,
			final Date date) {
		return (List<ProviderSummary>) this.sessionFactory.getCurrentSession()
				.getNamedQuery(FIND_HEALTH_PROVIDERS_BY_FACILITY_DATE_QUERY)
				.setParameter(FACILITY_PARAM_NAME, facility)
				.setTimestamp(DATE_PARAM_NAME, date)
				.list();
	}
}