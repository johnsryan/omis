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
package omis.vehicle.report.impl.hibernate;

import java.util.List;
import java.util.ArrayList;

import omis.vehicle.report.OffenderVehicleSummaryReportService;
import omis.vehicle.report.OffenderVehicleSummary;
import omis.offender.domain.Offender;

import org.hibernate.SessionFactory;

/**
 * Hibernate implementation of offender vehicle summary report service.
 * 
 * @author Yidong Li
 * @author Josh Divine
 * @version 0.1.1 (Feb 14, 2018)
 * @since OMIS 3.0
 */
public class OffenderVehicleSummaryReportServiceHibernateImpl
		implements OffenderVehicleSummaryReportService {

	/* Queries */
	private static final String FIND_VEHICLE_SUMMARIES_BY_OFFENDER_QUERY_NAME 
		= "findVehicleSummariesByOffender";
	
	/* Parameters */
	private final static String OFFENDER_PARAM_NAME = "offender";
	
	private SessionFactory sessionFactory;
		
	/* Constructors. */
	
	/**
	 * Instantiates an Hibernate implementation of offender vehicle summary 
	 * report service
	 * @param sessionFactory session factory
	 */

	public OffenderVehicleSummaryReportServiceHibernateImpl(final SessionFactory
		sessionFactory) {
			this.sessionFactory = sessionFactory;
	}
	
	/* Method implementations. */
	/** {@inheritDoc} */

	@Override 
	public List<OffenderVehicleSummary> findByOffender(
		final Offender offender) {
		List<OffenderVehicleSummary> summaries
			= new ArrayList<OffenderVehicleSummary>();
		@SuppressWarnings("unchecked")
		List<OffenderVehicleSummary> internalSummaries = this.sessionFactory
			.getCurrentSession()
			.getNamedQuery(FIND_VEHICLE_SUMMARIES_BY_OFFENDER_QUERY_NAME)
			.setParameter(OFFENDER_PARAM_NAME, offender)
			.setReadOnly(true)
			.list();
		summaries.addAll(internalSummaries); 
		return summaries;
	}
}