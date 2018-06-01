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
package omis.assessment.report;

import java.util.List;

import omis.offender.domain.Offender;
import omis.questionnaire.domain.AdministeredQuestionnaire;
import omis.questionnaire.domain.QuestionnaireCategory;

/**
 * Assessment summary report service.
 * 
 * @author Josh Divine
 * @author Annie Wahl
 * @version 0.1.1 (Apr 18, 2018)
 * @since OMIS 3.0
 */
public interface AssessmentSummaryReportService {

	/**
	 * Returns a list of administered questionnaires for the specified offender.
	 * 
	 * @param offender offender
	 * @return list of administered questionnaires
	 */
	List<AdministeredQuestionnaire> findAssessmentQuestionnairesByOffender(
			Offender offender);
	
	/**
	 * Returns the assessment summary for the specified administered 
	 * questionnaire.
	 * 
	 * @param administeredQuestionnaire administered questionnaire
	 * @return assessment summary
	 */
	AssessmentSummary summarize(
			AdministeredQuestionnaire administeredQuestionnaire);
	
	/**
	 * Returns a list of assessment category score summaries for the specified 
	 * administered questionnaire.
	 * 
	 * @param administeredQuestionnaire administered questionnaire
	 * @return list of assessment category score summaries
	 */
	List<AssessmentCategoryScoreSummary> 
			findAssessmentCategoryScoreSummariesByAdministeredQuestionnaire(
					AdministeredQuestionnaire administeredQuestionnaire);
	
	/**
	 * Returns a list of questionnaire categories.
	 * 
	 * @return list of questionnaire categories
	 */
	List<QuestionnaireCategory> findAssessmentQuestionCategories();
	
	/**
	 * Returns a list of Assessment Rating Summaries by the specified
	 * Administered Questionnaire.
	 * 
	 * @param administeredQuestionnaire - Administered Questionnaire
	 * @return List of Assessment Rating Summaries by the specified
	 * Administered Questionnaire.
	 */
	List<AssessmentRatingSummary>
		findAssessmentRatingSummariesByAdministeredQuestionnaire(
				AdministeredQuestionnaire administeredQuestionnaire);
}