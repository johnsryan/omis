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
package omis.adaaccommodation.report.impl.hibernate;

import java.util.List;

import omis.adaaccommodation.domain.Accommodation;
import omis.adaaccommodation.report.AccommodationIssuanceReportService;
import omis.adaaccommodation.report.AccommodationIssuanceSummary;

import org.hibernate.SessionFactory;

/**
 * Hibernate implementation of the accommodation issuance report service.
 *
 * @author Sheronda Vaughn
 * @author Josh Divine
 * @version 0.1.1 (Feb 15, 2018)
 * @since OMIS 3.0
 */
public class AccommodationIssuanceReportServiceHibernateImpl 
	implements AccommodationIssuanceReportService {

	/* Queries. */
	private static final String 
		FIND_ACCOMMODATION_ISSUANCES_BY_ACCOMMODATION_QUERY_NAME
		= "findIssuancesByAccommodation";
	
	/* Parameters. */
	private static final String ACCOMMODATION_PARAMETER_NAME = "accommodation";
	
	/* Memebers. */
	private final SessionFactory sessionFactory;
	
	/**
	 * Constructor.
	 * 
	 * @param sessionFactory session factory
	 */
	public AccommodationIssuanceReportServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/** {@inheritDoc} */
	@Override
	public  List<AccommodationIssuanceSummary> findByAccommodation(
			final Accommodation accommodation) {
		@SuppressWarnings("unchecked")
		List<AccommodationIssuanceSummary> issuanceSummaries
			= this.sessionFactory.getCurrentSession()
			.getNamedQuery(
					FIND_ACCOMMODATION_ISSUANCES_BY_ACCOMMODATION_QUERY_NAME)
			.setParameter(ACCOMMODATION_PARAMETER_NAME, accommodation)
			.setReadOnly(true)
			.list();
		return issuanceSummaries;
	}
}