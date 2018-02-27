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
package omis.custody.report.impl.hibernate;

import java.util.List;

import omis.custody.report.CustodyReportService;
import omis.custody.report.CustodySummary;
import omis.offender.domain.Offender;

import org.hibernate.SessionFactory;

/**
 * Hibernate Implementation of report service for custody.
 * 
 * @author Joel Norris
 * @author Josh Divine 
 * @version 0.1.1 (Feb 15, 2018)
 * @since OMIS 3.0
 */
public class CustodyReportServiceHibernateImpl
		implements CustodyReportService {
	
	private static final String OFFENDER_PARAM = "offender";
	
	private static final String FIND_CUSTODY_SUMMARIES_QUERY_NAME
	 	= "summarizeCustodyByOffender";
	
	private SessionFactory sessionFactory;
	
	/**
	 * Instantiates an instance of custody report service with the specified
	 * session factory.
	 * @param sessionFactory session factory
	 */
	public CustodyReportServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/** {@inheritDoc} */
	@Override
	public final List<CustodySummary> findReviewSummaries(
			final Offender offender) {
		@SuppressWarnings("unchecked")
		List<CustodySummary> custodySummaries = (List<CustodySummary>) 
				this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(FIND_CUSTODY_SUMMARIES_QUERY_NAME)
				.setParameter(OFFENDER_PARAM, offender)
				.setReadOnly(true)
				.list();
		
		return custodySummaries;
	}
}