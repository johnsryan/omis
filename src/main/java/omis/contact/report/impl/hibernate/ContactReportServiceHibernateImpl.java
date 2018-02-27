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
package omis.contact.report.impl.hibernate;

import org.hibernate.SessionFactory;

import omis.contact.report.ContactReportService;
import omis.contact.report.ContactSummary;
import omis.person.domain.Person;

/**
 * Hibernate implementation of service to report contacts.
 *
 * @author Stephen Abson
 * @author Josh Divine
 * @version 0.0.2 (Feb 15, 2018)
 * @since OMIS 3.0
 */
public class ContactReportServiceHibernateImpl
		implements ContactReportService {

	/* Query names. */
	
	private static final String SUMMARIZE_BY_PERSON_QUERY_NAME
		= "summarizeContactByPerson";

	/* Parameter names. */
	
	private static final String PERSON_PARAM_NAME = "person";
	
	/* Session factories. */
	
	private final SessionFactory sessionFactory;
	
	/* Constructors. */
	
	/**
	 * Instantiates Hibernate implementation of service to report contacts.
	 * 
	 * @param sessionFactory session factory
	 */
	public ContactReportServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/* Method implementations. */
	
	/** {@inheritDoc} */
	@Override
	public ContactSummary summarizeByPerson(final Person person) {
		ContactSummary summary = (ContactSummary) this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(SUMMARIZE_BY_PERSON_QUERY_NAME)
				.setParameter(PERSON_PARAM_NAME, person)
				.setReadOnly(true)
				.uniqueResult();
		return summary;
	}
}