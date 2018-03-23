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
package omis.assessment.report.impl.hibernate;

import java.util.List;
import omis.assessment.report.AssessmentDocumentSummary;
import omis.assessment.report.AssessmentDocumentSummaryReportService;
import omis.questionnaire.domain.AdministeredQuestionnaire;
import org.hibernate.SessionFactory;

/**
 * Assessment Document Summary Report Service Hibernate Implementation.
 * 
 *@author Annie Wahl 
 *@version 0.1.0 (Mar 8, 2018)
 *@since OMIS 3.0
 *
 */
public class AssessmentDocumentSummaryReportServiceHibernateImpl
		implements AssessmentDocumentSummaryReportService {
	
	private static final String
		FIND_SUMMARIES_BY_ADMINISTERED_QUESTIONNAIRE_QUERY_NAME =
			"findAssessmentDocumentSummariesByAdministeredQuestionnaire";
	
	private static final String ADMINISTERED_QUESTIONNAIRE_PARAM_NAME =
			"administeredQuestionnaire";
	
	private final SessionFactory sessionFactory;
	
	/**
	 * @param sessionFactory - Session Factory
	 */
	public AssessmentDocumentSummaryReportServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/**{@inheritDoc} */
	@Override
	public List<AssessmentDocumentSummary>
		findAssessmentDocumentSummariesByAdministeredQuestionnaire(
			final AdministeredQuestionnaire administeredQuestionnaire) {
		@SuppressWarnings("unchecked")
		List<AssessmentDocumentSummary> summaries = this.sessionFactory
				.getCurrentSession().getNamedQuery(
						FIND_SUMMARIES_BY_ADMINISTERED_QUESTIONNAIRE_QUERY_NAME)
				.setParameter(ADMINISTERED_QUESTIONNAIRE_PARAM_NAME,
						administeredQuestionnaire)
				.list();
		
		return summaries;
	}
}
