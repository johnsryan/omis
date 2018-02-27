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
package omis.courtcase.report.impl.hibernate;

import java.util.List;

import org.hibernate.SessionFactory;

import omis.courtcase.report.ChargeSummary;
import omis.courtcase.report.ChargeSummaryReportService;
import omis.person.domain.Person;

/**
 * Implementation of report service for charge summary.
 * 
 * @author Josh Divine
 * @version 0.1.1 (Feb 15, 2018)
 * @since OMIS 3.0
 */
public class ChargeSummaryReportServiceImpl 
	implements ChargeSummaryReportService {

	/* Query names. */
	
	private static final String SUMMARIZE_PENDING_CHARGES_BY_PERSON_QUERY_NAME
		= "summarizePendingChargesByPerson";
	
	/* Parameters. */
	
	private static final String PERSON_PARAM_NAME = "person";
	
	/* Resources. */
	
	private final SessionFactory sessionFactory;
	
	/**
	 * Instantiates an instance of court case summary report service with the
	 * specified session factory.
	 * @param sessionFactory - session factory.
	 */
	ChargeSummaryReportServiceImpl(final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/** {@inheritDoc} */
	@Override
	public List<ChargeSummary> summarizePendingChargesByOffender(
			final Person person) {
		@SuppressWarnings("unchecked")
		List<ChargeSummary> charges = (List<ChargeSummary>) this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(SUMMARIZE_PENDING_CHARGES_BY_PERSON_QUERY_NAME)
				.setParameter(PERSON_PARAM_NAME, person)
				.setReadOnly(true)
				.list();
		return charges;
	}
}