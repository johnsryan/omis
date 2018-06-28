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
package omis.travelpermit.report.impl.hibernate;

import java.util.Date;

import org.hibernate.SessionFactory;

import omis.offender.domain.Offender;
import omis.travelpermit.report.TravelPermitProfileItemReportService;

/**
 * Travel permit profile item report service hibernate implementation.
 *
 * @author Sheronda Vaughn
 * @version 0.1.0 (Jun 6, 2018)
 * @since OMIS 3.0
 */
public class TravelPermitProfileItemReportServiceHibernateImpl 
	implements TravelPermitProfileItemReportService {
	
	private static final String 
		FIND_TRAVEL_PERMIT_COUNT_BY_OFFENDER_AND_DATE_QUERY_NAME
		= "findTravelPermitCountByOffenderAndDate";

	private static final String OFFENDER_PARAM_NAME = "offender";

	private static final String DATE_PARAM_NAME 
		= "date";
	private final SessionFactory sessionFactory;

	/** Instantiates an implementation of travel permit profile item report
	 * service hibernate impl. */
	public TravelPermitProfileItemReportServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/** {@inheritDoc} */
	@Override
	public Integer findTravePermitsCountByOffenderAndDate(
			final Offender offender, final Date date) {
		return ((Long) this.sessionFactory.getCurrentSession()
				.getNamedQuery(
						FIND_TRAVEL_PERMIT_COUNT_BY_OFFENDER_AND_DATE_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setDate(DATE_PARAM_NAME, date)
				.setReadOnly(true)
				.uniqueResult()).intValue();
	}
}