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
package omis.caseload.report.impl.hibernate;

import java.util.List;

import org.hibernate.SessionFactory;

import omis.caseload.report.OfficerCaseAssignmentSummary;
import omis.caseload.report.OfficerCaseAssignmentSummaryReportService;
import omis.offender.domain.Offender;
import omis.user.domain.UserAccount;

/**
 * Hibernate implementation of the officer case assignment summary report 
 * service.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Jun 14, 2018)
 * @since OMIS 3.0
 */
public class OfficerCaseAssignmentSummaryReportServiceHibernateImpl
		implements OfficerCaseAssignmentSummaryReportService {

	/* Queries. */
	
	private static final String FIND_BY_OFFENDER_QUERY_NAME = 
			"findOfficerCaseAssignmentSummariesByOffender";
	
	private static final String FIND_BY_USER_ACCOUNT_QUERY_NAME = 
			"findOfficerCaseAssignmentSummariesByUserAccount";
	
	/* Parameters. */

	private static final String OFFENDER_PARAM_NAME = "offender";
	
	private static final String USER_ACCOUNT_PARAM_NAME = "userAccount";
	
	/* Members. */
	
	private final SessionFactory sessionFactory;
	
	/**
	 * Initializes an implementation of officer case assignment summary report 
	 * service.
	 * 
	 * @param sessionFactory session factory
	 */
	public OfficerCaseAssignmentSummaryReportServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/** {@inheritDoc} */
	@Override
	public List<OfficerCaseAssignmentSummary> findByOffender(
			final Offender offender) {
		@SuppressWarnings("unchecked")
		List<OfficerCaseAssignmentSummary> summaries = this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(FIND_BY_OFFENDER_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.list();
		return summaries;
	}

	/** {@inheritDoc} */
	@Override
	public List<OfficerCaseAssignmentSummary> findByUser(
			final UserAccount userAccount) {
		@SuppressWarnings("unchecked")
		List<OfficerCaseAssignmentSummary> summaries = this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(FIND_BY_USER_ACCOUNT_QUERY_NAME)
				.setParameter(USER_ACCOUNT_PARAM_NAME, userAccount)
				.list();
		return summaries;
	}
}