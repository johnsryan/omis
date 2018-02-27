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

import omis.bedplacement.report.BedPlacementSummary;
import omis.bedplacement.report.BedPlacementSummaryReportService;
import omis.offender.domain.Offender;

/** 
 * Hibernate implementation of bed placement summary report service.
 * 
 * @author Ryan Johns
 * @author Josh Divine
 * @version 0.1.1 (Feb 14, 2018)
 * @since OMIS 3.0 
 */
public class BedPlacementSummaryReportServiceHibernateImpl 
				implements BedPlacementSummaryReportService {
	/* Query names. */
	
	private static final String 
			FIND_BED_PLACEMENT_BY_OFFENDER_AND_DATE_QUERY_NAME = 
					"findBedPlacementByOffenderAndDate";
	
	/* Parameters. */
	
	private static final String DATE_PARAM_NAME = "date";

	private static final String OFFENDER_PARAM_NAME = "offender";

	/* Resources. */
	
	private final SessionFactory sessionFactory;
	
	/** Constructor.
	 * @param sessionFactory - session factory. */
	public BedPlacementSummaryReportServiceHibernateImpl(
				final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/** {@inheritDoc} */
	@Override
	public BedPlacementSummary findBedPlacementSummaryByOffenderAndDate(
				final Offender offender, final Date date) {
		return (BedPlacementSummary) this.sessionFactory.getCurrentSession()
				.getNamedQuery(
						FIND_BED_PLACEMENT_BY_OFFENDER_AND_DATE_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setTimestamp(DATE_PARAM_NAME, date)
				.setReadOnly(true)
				.setMaxResults(1).uniqueResult();
	}
}