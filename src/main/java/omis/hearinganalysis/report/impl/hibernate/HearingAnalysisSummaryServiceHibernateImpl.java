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
package omis.hearinganalysis.report.impl.hibernate;

import org.hibernate.SessionFactory;

import omis.hearinganalysis.domain.HearingAnalysis;
import omis.hearinganalysis.report.HearingAnalysisSummary;
import omis.hearinganalysis.report.HearingAnalysisSummaryService;

/**
 * Hibernate implementation of the hearing analysis summary report service.
 * 
 * @author Josh Divine
 * @version 0.1.1 (Feb 14, 2018)
 * @since OMIS 3.0
 */
public class HearingAnalysisSummaryServiceHibernateImpl 
	implements HearingAnalysisSummaryService {
	
	/* Queries. */
	
	private static final String SUMMARIZE_QUERY_NAME = 
			"summarizeHearingAnalysis";
	
	/* Parameters.*/ 
	
	private static final String HEARING_ANALYSIS_PARAMETER_NAME = 
			"hearingAnalysis";
	
	/* Members. */
	
	private final SessionFactory sessionFactory;
	
	/**
	 * Constructor.
	 *
	 * @param sessionFactory session factory
	 */
	public HearingAnalysisSummaryServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/** {@inheritDoc} */
	@Override
	public HearingAnalysisSummary summarize(HearingAnalysis hearingAnalysis) {
		HearingAnalysisSummary summary = (HearingAnalysisSummary) this
				.sessionFactory.getCurrentSession()
				.getNamedQuery(SUMMARIZE_QUERY_NAME)
				.setParameter(HEARING_ANALYSIS_PARAMETER_NAME, hearingAnalysis)
				.setReadOnly(true)
				.uniqueResult();
		return summary;
	}
}