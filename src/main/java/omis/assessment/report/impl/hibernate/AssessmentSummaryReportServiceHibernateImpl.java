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
import org.hibernate.SessionFactory;
import omis.assessment.report.AssessmentCategoryScoreSummary;
import omis.assessment.report.AssessmentRatingSummary;
import omis.assessment.report.AssessmentSummary;
import omis.assessment.report.AssessmentSummaryReportService;
import omis.offender.domain.Offender;
import omis.questionnaire.domain.AdministeredQuestionnaire;
import omis.questionnaire.domain.QuestionnaireCategory;

/**
 * Assessment summary report service hibernate implementation.
 * 
 * @author Josh Divine
 * @author Annie Wahl
 * @version 0.1.1 (Apr 18, 2018)
 * @since OMIS 3.0
 */
public class AssessmentSummaryReportServiceHibernateImpl 
		implements AssessmentSummaryReportService {

	/* Queries. */
	
	private static final String 
		FIND_ADMINISTERED_QUESTIONNAIRES_BY_OFFENDER_QUERY_NAME =
			"findAssessmentQuestionnairesByOffender";
	
	private static final String
		SUMMARIZE_BY_ADMINISTERED_QUESTIONNAIRE_QUERY_NAME =
			"findAssessmentSummaryByAdministeredQuestionnaire";
	
	private static final String 
		FIND_ASSESSMENT_CATEGORY_SCORE_SUMMARIES_BY_ADMINISTERED_QUESTIONNAIRE_QUERY_NAME =
			"findAssessmentCategoryScoreSummariesByAdministeredQuestionnaire";
	
	private static final String FIND_ASSESSMENT_QUESTION_CATEGORIES_QUERY_NAME =
		"findAssessmentQuestionCategories";
	
	private static final String
		FIND_ASSESSMENT_RATING_SUMMARIES_BY_ADMINISTERED_QUESTIONNAIRE_QUERY_NAME =
			"findAssessmentRatingSummariesByAdministeredQuestionnaire";
	
	/* Parameters. */
	
	private static final String OFFENDER_PARAM_NAME = "offender";
	
	private static final String ADMINISTERED_QUESTIONNAIRE_PARAM_NAME =
			"administeredQuestionnaire";
	
	private final SessionFactory sessionFactory;
	
	/**
	 * Instantiates an assessment summary report service hibernate 
	 * implementation with the specified session factory.
	 * 
	 * @param sessionFactory session factory
	 */
	public AssessmentSummaryReportServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/** {@inheritDoc} */
	@Override
	public List<AdministeredQuestionnaire> 
			findAssessmentQuestionnairesByOffender(final Offender offender) {
		@SuppressWarnings("unchecked")
		List<AdministeredQuestionnaire> questionnaires = this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(
						FIND_ADMINISTERED_QUESTIONNAIRES_BY_OFFENDER_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.list();
		return questionnaires;
	}

	/** {@inheritDoc} */
	@Override
	public AssessmentSummary summarize(
			final AdministeredQuestionnaire administeredQuestionnaire) {
		AssessmentSummary assessmentSummary = (AssessmentSummary) this
				.sessionFactory.getCurrentSession()
				.getNamedQuery(
						SUMMARIZE_BY_ADMINISTERED_QUESTIONNAIRE_QUERY_NAME)
				.setParameter(ADMINISTERED_QUESTIONNAIRE_PARAM_NAME, 
						administeredQuestionnaire)
				.uniqueResult();
		return assessmentSummary;
	}

	/** {@inheritDoc} */
	@Override
	public List<AssessmentCategoryScoreSummary> 
			findAssessmentCategoryScoreSummariesByAdministeredQuestionnaire(
					final AdministeredQuestionnaire administeredQuestionnaire) {
		@SuppressWarnings("unchecked")
		List<AssessmentCategoryScoreSummary> summaries = this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(
					FIND_ASSESSMENT_CATEGORY_SCORE_SUMMARIES_BY_ADMINISTERED_QUESTIONNAIRE_QUERY_NAME)
				.setParameter(ADMINISTERED_QUESTIONNAIRE_PARAM_NAME,
					administeredQuestionnaire)
				.list();
		return summaries;
	}

	/** {@inheritDoc} */
	@Override
	public List<QuestionnaireCategory> findAssessmentQuestionCategories() {
		@SuppressWarnings("unchecked")
		List<QuestionnaireCategory> questionnaireCategories = this
				.sessionFactory.getCurrentSession()
				.getNamedQuery(FIND_ASSESSMENT_QUESTION_CATEGORIES_QUERY_NAME)
				.list();
		return questionnaireCategories;
	}

	/**{@inheritDoc} */
	@Override
	public List<AssessmentRatingSummary>
		findAssessmentRatingSummariesByAdministeredQuestionnaire(
			final AdministeredQuestionnaire administeredQuestionnaire) {
		@SuppressWarnings("unchecked")
		List<AssessmentRatingSummary> summaries = this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(
					FIND_ASSESSMENT_RATING_SUMMARIES_BY_ADMINISTERED_QUESTIONNAIRE_QUERY_NAME)
				.setParameter(ADMINISTERED_QUESTIONNAIRE_PARAM_NAME,
					administeredQuestionnaire)
				.list();
		return summaries;
	}
}