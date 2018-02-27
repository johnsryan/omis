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
package omis.commitstatus.report.impl.hibernate;

import java.util.List;
import java.util.ArrayList;

import omis.commitstatus.report.CommitStatusReportService;
import omis.commitstatus.report.CommitStatusTermSummary;
import omis.offender.domain.Offender;

import org.hibernate.SessionFactory;

/**
 * Hibernate implementation of commit status report service.
 * 
 * @author Yidong Li
 * @author Josh Divine
 * @version 0.1.1 (Feb 15, 2018)
 * @since OMIS 3.0
 */
public class CommitStatusReportServiceHibernateImpl
		implements CommitStatusReportService {

	/* Queries */
	private static final String 
			FIND_COMMIT_STATUS_SUMMARY_BY_OFFENDER_QUERY_NAME =
					"findCommitStatusSummaryByOffender";
	
	/* Parameters */
	private final static String OFFENDER_PARAM_NAME = "offender";
	
	private final SessionFactory sessionFactory;
		
	/* Constructors. */
	
	/**
	 * Instantiates an Hibernate implementation of commit status history 
	 * report service
	 * @param sessionFactory session factory
	 */

	public CommitStatusReportServiceHibernateImpl(final SessionFactory
		sessionFactory) {
			this.sessionFactory = sessionFactory;
	}
	
	/* Method implementations. */
	/** {@inheritDoc} */
	@Override 
	public List<CommitStatusTermSummary> summarizeByOffender(
			final Offender offender) {
		List<CommitStatusTermSummary> summaries
			= new ArrayList<CommitStatusTermSummary>();
		@SuppressWarnings("unchecked")
		List<CommitStatusTermSummary> internalSummaries = this.sessionFactory
			.getCurrentSession()
			.getNamedQuery(FIND_COMMIT_STATUS_SUMMARY_BY_OFFENDER_QUERY_NAME)
			.setParameter(OFFENDER_PARAM_NAME, offender)
			.setReadOnly(true)
			.list();
		summaries.addAll(internalSummaries); 
		return summaries;
	}
}