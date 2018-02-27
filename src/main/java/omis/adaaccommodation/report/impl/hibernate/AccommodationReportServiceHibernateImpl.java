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
import omis.adaaccommodation.report.AccommodationReportService;
import omis.adaaccommodation.report.AccommodationSummary;
import omis.offender.domain.Offender;

import org.hibernate.SessionFactory;

/**
 * Hibernate implementation of the accommodation report service.
 *
 * @author Sheronda Vaughn
 * @author Josh Divine
 * @version 0.1.1 (Feb 15, 2018)
 * @since OMIS 3.0
 */
public class AccommodationReportServiceHibernateImpl 
	implements AccommodationReportService {

	/* Queries. */
		
	private static final String SUMMARIZE_ACCOMMODATION_QUERY_NAME
		= "summarizeAccommodations";
	
	private static final String FIND_BY_OFFENDER_ACCOMMODATION 
		= "findByOffenderAccommodation";
	
	private static final String COUNT_ISSUANCES_BY_ACCOMMODATION
		= "countIssuancesByAccommodation";
	
	/* Parameters. */
	
	private static final String ACCOMMODATION_PARAMETER_NAME = "accommodation";
	
	private static final String OFFENDER_PARAMETER_NAME = "offender";
	
	/* Memebers. */
	private final SessionFactory sessionFactory;
	
	/**
	 * Constructor.
	 * 
	 * @param sessionFactory session factory
	 */
	public AccommodationReportServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/** {@inheritDoc} */
	@Override
	public AccommodationSummary summarize(final Accommodation accommodation) {		
		AccommodationSummary accommodationSummary = (AccommodationSummary) 
				this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(SUMMARIZE_ACCOMMODATION_QUERY_NAME)
				.setParameter(ACCOMMODATION_PARAMETER_NAME, accommodation)
				.setReadOnly(true)
				.uniqueResult();
		return accommodationSummary;
	}

	/** {@inheritDoc} */
	@Override
	public List<AccommodationSummary> findByOffenderAccommodation(
			final Offender offender) {
		@SuppressWarnings("unchecked")
		List<AccommodationSummary> accommodationSummaries = this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(FIND_BY_OFFENDER_ACCOMMODATION)
				.setParameter(OFFENDER_PARAMETER_NAME, offender)
				.setReadOnly(true)
				.list();
		return accommodationSummaries;
	}

	/** {@inheritDoc} */
	@Override
	public Boolean hasIssuances(final Accommodation accommodation) {
		final Boolean query = (Boolean) this.sessionFactory.getCurrentSession()
		.getNamedQuery(COUNT_ISSUANCES_BY_ACCOMMODATION)
		.setParameter(ACCOMMODATION_PARAMETER_NAME, accommodation)
		.setReadOnly(true)
		.uniqueResult();
		return query;
	}
}