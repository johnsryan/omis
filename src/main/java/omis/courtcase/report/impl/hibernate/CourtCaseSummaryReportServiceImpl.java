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

import omis.courtcase.report.CourtCaseSummary;
import omis.courtcase.report.CourtCaseSummaryReportService;
import omis.person.domain.Person;

/**
 * Implementation of report service for court case summary.
 * 
 * @author Joel Norris
 * @author Josh Divine
 * @version 0.1.3 (Feb 15, 2018)
 * @since OMIS 3.0
 */
public class CourtCaseSummaryReportServiceImpl 
	implements CourtCaseSummaryReportService {

	/* Query names. */
	
	private static final String FIND_COURT_CASE_BY_DEFENDANT_QUERY_NAME
		= "findCourtCaseByDefendant";
	
	/* Parameters. */
	
	private static final String DEFENDANT_PARAM_NAME = "defendant";
	
	/* Resources. */
	
	private final SessionFactory sessionFactory;
	
	/**
	 * Instantiates an instance of court case summary report service with the
	 * specified session factory.
	 * @param sessionFactory - session factory.
	 */
	public CourtCaseSummaryReportServiceImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/** {@inheritDoc} */
	@Override
	public List<CourtCaseSummary> summarizeByPerson(final Person offender) {
		@SuppressWarnings("unchecked")
		List<CourtCaseSummary> summaries = this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(FIND_COURT_CASE_BY_DEFENDANT_QUERY_NAME)
				.setParameter(DEFENDANT_PARAM_NAME, offender)
				.setReadOnly(true)
				.list();
		return summaries;
	}
}