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
package omis.person.report.impl.hibernate;

import omis.person.domain.Person;
import omis.person.report.PersonSummary;
import omis.person.report.PersonSummaryReportService;

import org.hibernate.SessionFactory;

/**
 * Hibernate implementation of report service for person summary.
 * 
 * @author Joel Norris
 * @author Josh Divine
 * @version 0.1.1 (Feb 14, 2018)
 * @since OMIS 3.0
 */
public class PersonSummaryReportServiceHibernateImpl 
	implements PersonSummaryReportService {
	
	/* Parameter names */
	
	private static final String PERSON_PARAM_NAME = "person";

	/* Query names */
	
	private static final String SUMMARIZE_PERSON_QUERY_NAME
		= "summarizePerson";
	
	/* Session Factory */
	
	private final SessionFactory sessionFactory;
	
	/* Constructor */
	
	/**
	 * Instantiates a person summary report service with the specified
	 * session factory.
	 * 
	 * @param sessionFactory session factory
	 */
	public PersonSummaryReportServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/* Method implementations */
	
	/** {@inheritDoc} */
	@Override
	public PersonSummary summarize(Person person) {
		PersonSummary summary = (PersonSummary) 
				this.sessionFactory.getCurrentSession()
				.getNamedQuery(SUMMARIZE_PERSON_QUERY_NAME)
				.setParameter(PERSON_PARAM_NAME, person)
				.setReadOnly(true)
				.uniqueResult();
		return summary;
	}
}