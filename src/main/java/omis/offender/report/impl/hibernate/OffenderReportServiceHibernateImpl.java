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
package omis.offender.report.impl.hibernate;

import java.util.Date;

import omis.offender.domain.Offender;
import omis.offender.report.OffenderReportService;
import omis.offender.report.OffenderSummary;
import omis.person.domain.Person;

import org.hibernate.SessionFactory;

/**
 * Hibernate implementation of report service for offenders.
 * 
 * @author Stephen Abson
 * @author Josh Divine
 * @version 0.1.1 (Feb 14, 2018)
 * @since OMIS 3.0
 */
public class OffenderReportServiceHibernateImpl
		implements OffenderReportService {

	private SessionFactory sessionFactory;
	
	/**
	 * Instantiates an implementation of report service for offenders with the
	 * specified resources.
	 * 
	 * @param sessionFactory session factory
	 */
	public OffenderReportServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/**
	 * Returns the session factory.
	 * 
	 * @return session factory
	 */
	protected final SessionFactory getSessionFactory() {
		return this.sessionFactory;
	}
	
	/** {@inheritDoc} */
	@Override
	public final OffenderSummary summarizeOffender(
			final Offender offender) {
		OffenderSummary report =
				(OffenderSummary) this.getSessionFactory()
					.getCurrentSession().getNamedQuery("summarizeOffender")
					.setParameter("offender", offender)
					.setReadOnly(true)
					.uniqueResult();
		return report;
	}
	
	/** {@inheritDoc} */
	@Override
	public final OffenderSummary summarizeIfOffender(
			final Person person) {
		OffenderSummary report =
				(OffenderSummary) this.getSessionFactory()
					.getCurrentSession().getNamedQuery("summarizeOffender")
					.setParameter("offender", person)
					.setReadOnly(true)
					.uniqueResult();
		return report;
	}

	/** {@inheritDoc} */
	@Override
	public OffenderSummary summarizeOffender(final Offender offender,
			final Date date) {
		
		// TODO Implement or remove - SA
		throw new UnsupportedOperationException("Not yet implemented");
	}
}