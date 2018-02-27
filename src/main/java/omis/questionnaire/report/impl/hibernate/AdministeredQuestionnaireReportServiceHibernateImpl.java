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
package omis.questionnaire.report.impl.hibernate;

import java.util.List;

import org.hibernate.SessionFactory;

import omis.person.domain.Person;
import omis.questionnaire.domain.AdministeredQuestionnaire;
import omis.questionnaire.domain.AllowedQuestion;
import omis.questionnaire.domain.QuestionnaireSection;
import omis.questionnaire.domain.QuestionnaireType;
import omis.questionnaire.report.AdministeredQuestionnaireSummary;
import omis.questionnaire.report.AnswerValueSummary;
import omis.questionnaire.report.QuestionSummary;
import omis.questionnaire.report.AdministeredQuestionnaireReportService;
import omis.questionnaire.report.AdministeredQuestionnaireSectionSummary;

/**
 * QuestionnaireReportServiceHibernateImpl.java
 * 
 * @author Annie Wahl
 * @author Josh Divine
 * @version 0.1.1 (Feb 15, 2018)
 * @since OMIS 3.0
 *
 */
public class AdministeredQuestionnaireReportServiceHibernateImpl
	implements AdministeredQuestionnaireReportService {
	
	/* Query Names */
	
	private static final String 
		FIND_QUESTIONNAIRE_SUMMARIES_BY_ANSWERER_QUERY_NAME =
			"findAdministeredQuestionnaireSummariesByAnswerer";
	
	private static final String 
	FIND_QUESTIONNAIRE_SUMMARIES_BY_ANSWERER_AND_QUESTIONNAIRE_TYPE_QUERY_NAME =
		"findAdministeredQuestionnaireSummariesByAnswererAndQuestionnaireType";
	
	private static final String QUESTIONNAIRE_SUMMARIZE_QUERY_NAME =
			"administeredQuestionnaireSummarize";
	
	private static final String QUESTION_SUMMARIZE_QUERY_NAME =
			"questionSummarize";
	
	private static final String 
		FIND_QUESTIONNAIRE_SECTION_SUMMARIES_BY_QUESTIONNAIRE_QUERY_NAME =
			"findAdministeredQuestionnaireSectionSummariesByQuestionnaire";
	
	private static final String 
		FIND_QUESTION_SUMMARIES_BY_QUESTIONNAIRE_SECTION_QUERY_NAME =
			"findQuestionSummariesByQuestionnaireSection";
	
	private static final String 
		FIND_ANSWER_VALUE_SUMMARIES_BY_ALLOWED_QUESTION_QUERY_NAME =
			"findAnswerValueSummariesByAllowedQuestion";
	
	/* Parameter Names */
	
	private static final String ANSWERER_PARAM_NAME = "answerer";
	
	private static final String QUESTIONNAIRE_TYPE_PARAM_NAME =
			"questionnaireType";
	
	private static final String ADMINISTERED_QUESTIONNAIRE_PARAM_NAME =
			"administeredQuestionnaire";
	
	private static final String QUESTIONNAIRE_SECTION_PARAM_NAME =
			"questionnaireSection";
	
	private static final String ALLOWED_QUESTION_PARAM_NAME = "allowedQuestion";
	
	/* Members */
	
	private final SessionFactory sessionFactory;
	
	/* Constructor */
	
	/**
	 * Constructor for QuestionnaireReportServiceHibernateImpl
	 * @param sessionFactory
	 */
	public AdministeredQuestionnaireReportServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/* Report Service Methods */
	
	/**{@inheritDoc} */
	@Override
	public List<AdministeredQuestionnaireSummary> 
		findQuestionnairesByAnswerer(final Person answerer) {
		@SuppressWarnings("unchecked")
		List<AdministeredQuestionnaireSummary> 
			administeredQuestionnaireSummaries
			= this.sessionFactory.getCurrentSession()
			.getNamedQuery(FIND_QUESTIONNAIRE_SUMMARIES_BY_ANSWERER_QUERY_NAME)
			.setParameter(ANSWERER_PARAM_NAME, answerer)
			.setReadOnly(true)
			.list();
		
		return administeredQuestionnaireSummaries;
	}
	
	/**{@inheritDoc} */
	public List<AdministeredQuestionnaireSummary> 
	findQuestionnairesByAnswererAndQuestionnaireType(
		final Person answerer, final QuestionnaireType questionnaireType){
		@SuppressWarnings("unchecked")
		List<AdministeredQuestionnaireSummary> 
			administeredQuestionnaireSummaries
			= this.sessionFactory.getCurrentSession()
			.getNamedQuery(
	FIND_QUESTIONNAIRE_SUMMARIES_BY_ANSWERER_AND_QUESTIONNAIRE_TYPE_QUERY_NAME)
			.setParameter(ANSWERER_PARAM_NAME, answerer)
			.setParameter(QUESTIONNAIRE_TYPE_PARAM_NAME, questionnaireType)
			.setReadOnly(true)
			.list();
		
		return administeredQuestionnaireSummaries;
	}

	/**{@inheritDoc} */
	@Override
	public AdministeredQuestionnaireSummary summarize(
			final AdministeredQuestionnaire administeredQuestionnaire) {
		AdministeredQuestionnaireSummary administeredQuestionnaireSummary = 
			(AdministeredQuestionnaireSummary)this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(QUESTIONNAIRE_SUMMARIZE_QUERY_NAME)
				.setParameter(ADMINISTERED_QUESTIONNAIRE_PARAM_NAME, 
						administeredQuestionnaire)
				.setReadOnly(true)
				.uniqueResult();
			
		return administeredQuestionnaireSummary;
	}
	
	/**{@inheritDoc}*/
	@Override
	public QuestionSummary summarize(final AllowedQuestion allowedQuestion){
		QuestionSummary questionSummary = (QuestionSummary) this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(QUESTION_SUMMARIZE_QUERY_NAME)
				.setParameter(ALLOWED_QUESTION_PARAM_NAME, allowedQuestion)
				.setReadOnly(true)
				.uniqueResult();
		
		return questionSummary;
	}

	/**{@inheritDoc} */
	@Override
	public List<AdministeredQuestionnaireSectionSummary>
		findAdministeredQuestionnaireSectionSummariesByAdministeredQuestionnaire(
			final AdministeredQuestionnaire administeredQuestionnaire) {
		@SuppressWarnings("unchecked")
		List<AdministeredQuestionnaireSectionSummary> 
			questionnaireSectionSummaries =
			this.sessionFactory.getCurrentSession()
			.getNamedQuery(
					FIND_QUESTIONNAIRE_SECTION_SUMMARIES_BY_QUESTIONNAIRE_QUERY_NAME)
			.setParameter(ADMINISTERED_QUESTIONNAIRE_PARAM_NAME, 
					administeredQuestionnaire)
			.setReadOnly(true)
			.list();
		
		return questionnaireSectionSummaries;
	}

	/**{@inheritDoc} */
	@Override
	public List<QuestionSummary> findQuestionSummariesByQuestionnaireSection(
			final QuestionnaireSection questionnaireSection) {
		@SuppressWarnings("unchecked")
		List<QuestionSummary> questionSummaries =
			this.sessionFactory.getCurrentSession()
			.getNamedQuery(
					FIND_QUESTION_SUMMARIES_BY_QUESTIONNAIRE_SECTION_QUERY_NAME)
			.setParameter(QUESTIONNAIRE_SECTION_PARAM_NAME, questionnaireSection)
			.setReadOnly(true)
			.list();
		
		return questionSummaries;
	}

	/**{@inheritDoc} */
	@Override
	public List<AnswerValueSummary> findAnswerValueSummariesByAllowedQuestion(
			final AllowedQuestion allowedQuestion) {
		@SuppressWarnings("unchecked")
		List<AnswerValueSummary> answerValueSummaries =
			this.sessionFactory.getCurrentSession()
			.getNamedQuery(
					FIND_ANSWER_VALUE_SUMMARIES_BY_ALLOWED_QUESTION_QUERY_NAME)
			.setParameter(ALLOWED_QUESTION_PARAM_NAME, allowedQuestion)
			.setReadOnly(true)
			.list();
		
		return answerValueSummaries;
	}
}