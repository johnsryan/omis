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
package omis.conviction.report.impl.hibernate;

import java.util.List;

import org.hibernate.SessionFactory;

import omis.conviction.report.ConvictionSummary;
import omis.conviction.report.ConvictionSummaryReportService;
import omis.person.domain.Person;

/**
 * Conviction summary report service implementation.
 * 
 * @author Josh Divine
 * @version 0.1.1 (Feb 15, 2018)
 * @since OMIS 3.0
 */
public class ConvictionSummaryReportServiceImpl 
	implements ConvictionSummaryReportService {

	/* Query names. */
	
	private static final String SUMMARIZE_CONVICTIONS_BY_PERSON_QUERY_NAME
		= "summarizeConvictionsByPerson";

	/* Parameters. */
	
	private static final String PERSON_PARAM_NAME = "person";
	
	/* Resources. */
	
	private final SessionFactory sessionFactory;
	
	/**
	 * Instantiates an instance of conviction summary report service with the
	 * specified session factory.
	 * @param sessionFactory session factory.
	 */
	public ConvictionSummaryReportServiceImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/** {@inheritDoc} */
	@Override
	public List<ConvictionSummary> summarizeByPerson(Person person) {
		@SuppressWarnings("unchecked")
		List<ConvictionSummary> convictions = this.sessionFactory
			.getCurrentSession()
			.getNamedQuery(SUMMARIZE_CONVICTIONS_BY_PERSON_QUERY_NAME)
			.setParameter(PERSON_PARAM_NAME, person)
			.setReadOnly(true)
			.list();
		return convictions;
	}
}