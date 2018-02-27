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
package omis.presentenceinvestigation.report.impl.hibernate;

import java.util.List;

import org.hibernate.SessionFactory;

import omis.person.domain.Person;
import omis.presentenceinvestigation.domain.PresentenceInvestigationRequest;
import omis.presentenceinvestigation.report.PresentenceInvestigationRequestSummary;
import omis.presentenceinvestigation.report.PresentenceInvestigationRequestSummaryReportService;
import omis.user.domain.UserAccount;

/**
 * Hibernate implementation of presentence investigation request summary report 
 * service. 
 * 
 * @author Annie Wahl
 * @author Josh Divine
 * @version 0.1.1 (Feb 14, 2018)
 * @since OMIS 3.0
 */
public class 
	PresentenceInvestigationRequestSummaryReportServiceHibernateImpl 
	implements PresentenceInvestigationRequestSummaryReportService {
	
	private static final String FIND_BY_USER_QUERY_NAME 
		= "findPresentenceInvestigationRequestSummariesByUser";
	
	private static final String USER_PARAM_NAME = "assignedUser";

	private static final String FIND_BY_OFFENDER_QUERY_NAME 
		= "findPresentenceInvestigationRequestSummariesByOffender";

	private static final String OFFENDER_PARAM_NAME = "offender";

	private static final String FIND_BY_REQUEST_QUERY_NAME 
		= "findPresentenceInvestigationRequestSummary";

	private static final String REQUEST_PARAM_NAME 
		= "presentenceInvestigationRequest";
	
	private final SessionFactory sessionFactory;
	
	/** Constructor. 
	 * @param sessionFactory - session factory. */
	public PresentenceInvestigationRequestSummaryReportServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	@Override
	public List<PresentenceInvestigationRequestSummary> 
		findPresentenceInvestigationRequestSummariesByUser(
			final UserAccount user) {
			return (List<PresentenceInvestigationRequestSummary>) this
					.sessionFactory.getCurrentSession()
					.getNamedQuery(FIND_BY_USER_QUERY_NAME)
					.setParameter(USER_PARAM_NAME, user)
					.setReadOnly(true)
					.list();
	}

	/**{@inheritDoc} */
	@SuppressWarnings("unchecked")
	@Override
	public List<PresentenceInvestigationRequestSummary> 
		findPresentenceInvestigationRequestSummariesByOffender(
			final Person offender) {
		return (List<PresentenceInvestigationRequestSummary>) this
				.sessionFactory.getCurrentSession()
				.getNamedQuery(FIND_BY_OFFENDER_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setReadOnly(true)
				.list();
	}

	/**{@inheritDoc} */
	@Override
	public PresentenceInvestigationRequestSummary summarize(
			PresentenceInvestigationRequest presentenceInvestigationRequest) {
		return ((PresentenceInvestigationRequestSummary) this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(FIND_BY_REQUEST_QUERY_NAME)
				.setParameter(REQUEST_PARAM_NAME, 
						presentenceInvestigationRequest)
				.setReadOnly(true)
				.uniqueResult());
	}
}