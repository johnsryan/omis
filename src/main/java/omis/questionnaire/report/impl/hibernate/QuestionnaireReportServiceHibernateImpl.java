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

import omis.questionnaire.domain.AllowedQuestion;
import omis.questionnaire.domain.QuestionnaireCategory;
import omis.questionnaire.domain.QuestionnaireSection;
import omis.questionnaire.domain.QuestionnaireType;
import omis.questionnaire.report.AnswerValueSummary;
import omis.questionnaire.report.QuestionSummary;
import omis.questionnaire.report.QuestionnaireReportService;
import omis.questionnaire.report.QuestionnaireSectionSummary;
import omis.questionnaire.report.QuestionnaireTypeSummary;

/**
 * QuestionnaireReportServiceHibernateImpl.java
 * 
 * @author Annie Wahl
 * @author Josh Divine 
 * @version 0.1.1 (Feb 14, 2018)
 * @since OMIS 3.0
 */
public class QuestionnaireReportServiceHibernateImpl
		implements QuestionnaireReportService {
	
	/* Query Names */
	
	private static final String
		FIND_QUESTIONNAIRE_TYPE_SUMMARIES_BY_QUESTIONNAIRE_CATEGORY_QUERY_NAME
		= "findQuestionnaireTypeSummariesByQuestionnaireCategory";
	
	private static final String FIND_ALL_QUESTIONNAIRE_TYPE_SUMMARIES =
			"findAllQuestionnaireTypeSummaries";
	
	private static final String 
		FIND_QUESTIONNAIRE_SECTION_SUMMARIES_BY_QUESTIONNAIRE_TYPE
		= "findQuestionnaireSectionSummariesByQuestionnaireType";
	
	private static final String QUESTIONNAIRE_TYPE_SUMMARIZE 
		= "questionnaireTypeSummarize";
	
	private static final String QUESTION_SUMMARIZE_QUERY_NAME =
			"questionSummarize";
	
	private static final String 
		FIND_QUESTION_SUMMARIES_BY_QUESTIONNAIRE_SECTION_QUERY_NAME =
			"findQuestionSummariesByQuestionnaireSection";

	private static final String 
		FIND_ANSWER_VALUE_SUMMARIES_BY_ALLOWED_QUESTION_QUERY_NAME =
			"findAnswerValueSummariesByAllowedQuestion";
	
	private static final String
		FIND_ADMINISTERED_QUESTIONNAIRE_COUNT_BY_QUESTIONNAIRE_TYPE_QUERY_NAME =
			"findAdministeredQuestionnaireCountByQuestionnaireType";
	
	/* Param Names */
	
	private static final String QUESTIONNAIRE_CATEGORY_PARAM_NAME =
			"questionnaireCategory";
	
	private static final String QUESTIONNAIRE_TYPE_PARAM_NAME = 
			"questionnaireType";
	
	private static final String QUESTIONNAIRE_SECTION_PARAM_NAME =
			"questionnaireSection";
	
	private static final String ALLOWED_QUESTION_PARAM_NAME = "allowedQuestion";
	
	/* Helper */
	
	private final SessionFactory sessionFactory;
	
	/**
	 * Constructor for QuestionnaireReportServiceHibernateImpl
	 * @param sessionFactory
	 */
	public QuestionnaireReportServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**{@inheritDoc} */
	@Override
	public List<QuestionnaireTypeSummary>
		findQuestionnaireTypeSummariesByQuestionnaireCategory(
			final QuestionnaireCategory questionnaireCategory) {
		@SuppressWarnings("unchecked")
		List<QuestionnaireTypeSummary> summaries = this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(
		FIND_QUESTIONNAIRE_TYPE_SUMMARIES_BY_QUESTIONNAIRE_CATEGORY_QUERY_NAME)
				.setParameter(QUESTIONNAIRE_CATEGORY_PARAM_NAME,
						questionnaireCategory)
				.setReadOnly(true)
				.list();
		
		
		return summaries;
	}

	/**{@inheritDoc} */
	@Override
	public List<QuestionnaireSectionSummary>
		findQuestionnaireSectionSummariesByQuestionnaireType(
			final QuestionnaireType questionnaireType) {
		@SuppressWarnings("unchecked")
		List<QuestionnaireSectionSummary> summaries = this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(
					FIND_QUESTIONNAIRE_SECTION_SUMMARIES_BY_QUESTIONNAIRE_TYPE)
				.setParameter(QUESTIONNAIRE_TYPE_PARAM_NAME, questionnaireType)
				.setReadOnly(true)
				.list();
		
		return summaries;
	}

	/**{@inheritDoc} */
	@Override
	public QuestionnaireTypeSummary
		summarize(final QuestionnaireType questionnaireType) {
		QuestionnaireTypeSummary summary = 
				(QuestionnaireTypeSummary) this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(QUESTIONNAIRE_TYPE_SUMMARIZE)
				.setParameter(QUESTIONNAIRE_TYPE_PARAM_NAME, questionnaireType)
				.setReadOnly(true)
				.uniqueResult();
		
		return summary;
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
	public List<QuestionnaireTypeSummary> findAllQuestionnaireTypeSummaries() {
		@SuppressWarnings("unchecked")
		List<QuestionnaireTypeSummary> summaries = this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(FIND_ALL_QUESTIONNAIRE_TYPE_SUMMARIES)
				.setReadOnly(true)
				.list();
		
		return summaries;
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

	/**{@inheritDoc} */
	@Override
	public Integer findAdministeredQuestionnaireCountByQuestionnaireType(
			final QuestionnaireType questionnaireType) {
		Long count = (Long) this.sessionFactory.getCurrentSession()
				.getNamedQuery(
		FIND_ADMINISTERED_QUESTIONNAIRE_COUNT_BY_QUESTIONNAIRE_TYPE_QUERY_NAME)
				.setParameter(QUESTIONNAIRE_TYPE_PARAM_NAME, questionnaireType)
				.setReadOnly(true)
				.uniqueResult();
		return count.intValue();
	}
}