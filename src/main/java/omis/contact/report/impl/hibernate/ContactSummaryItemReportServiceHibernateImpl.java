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

import omis.contact.report.ContactSummary;
import omis.contact.report.ContactSummaryItemReportService;
import omis.offender.domain.Offender;

/**
 * Contact Summary Item Report Service Hibernate Implementation.
 * 
 * @author Annie Wahl
 * @author Josh Divine 
 * @version 0.1.1 (Feb 14, 2018)
 * @since OMIS 3.0
 */
public class ContactSummaryItemReportServiceHibernateImpl
		implements ContactSummaryItemReportService {
	
	private static final String FIND_CONTACT_SUMMARY_QUERY_NAME =
			"summarizeContactByPerson";
	
	private static final String OFFENDER_PARAM_NAME = "person";
	
	private SessionFactory sessionFactory;
	
	/**
	 * @param sessionFactory - Session Factory
	 */
	public ContactSummaryItemReportServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/**{@inheritDoc} */
	@Override
	public ContactSummary findContactSummaryByOffender(
			final Offender offender) {
		ContactSummary contactSummary = (ContactSummary)
				this.sessionFactory.getCurrentSession()
				.getNamedQuery(FIND_CONTACT_SUMMARY_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setReadOnly(true)
				.uniqueResult();
		
		return contactSummary;
	}
}