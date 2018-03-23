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

import java.io.Serializable;
import java.util.Date;

/**
 * Assessment summary.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Mar 14, 2018)
 * @since OMIS 3.0
 */
public class AssessmentSummary implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private final Long administeredQuestionnaireId;
	
	private final String questionnaireTypeName;
	
	private final String questionnaireCategoryName;
	
	private final Date administeredQuestionnaireDate;
	
	private final String administeredQuestionnaireAssessorLastName;
	
	private final String administeredQuestionnaireAssessorFirstName;
	
	/**
	 * Instantiates an assessment summary with the specified parameters.
	 * 
	 * @param administeredQuestionnaireId administered questionnaire id
	 * @param questionnaireTypeName questionnaire type name
	 * @param questionnaireCategoryName questionnaire category name
	 * @param administeredQuestionnaireDate administered questionnaire date
	 * @param administeredQuestionnaireAssessorLastName administered 
	 * questionnaire assessor last name
	 * @param administeredQuestionnaireAssessorFirstName administered 
	 * questionnaire assessor first name
	 */
	public AssessmentSummary(final Long administeredQuestionnaireId,
			final String questionnaireTypeName,
			final String questionnaireCategoryName,
			final Date administeredQuestionnaireDate,
			final String administeredQuestionnaireAssessorLastName,
			final String administeredQuestionnaireAssessorFirstName) {
		this.administeredQuestionnaireId = administeredQuestionnaireId;
		this.questionnaireTypeName = questionnaireTypeName;
		this.questionnaireCategoryName = questionnaireCategoryName;
		this.administeredQuestionnaireDate = administeredQuestionnaireDate;
		this.administeredQuestionnaireAssessorLastName = 
				administeredQuestionnaireAssessorLastName;
		this.administeredQuestionnaireAssessorFirstName = 
				administeredQuestionnaireAssessorFirstName;
	}

	/**
	 * Returns the administered questionnaire id.
	 *
	 * @return administered questionnaire id
	 */
	public Long getAdministeredQuestionnaireId() {
		return administeredQuestionnaireId;
	}

	/**
	 * Returns the questionnaire type name.
	 *
	 * @return questionnaire type name
	 */
	public String getQuestionnaireTypeName() {
		return questionnaireTypeName;
	}

	/**
	 * Returns the questionnaire category name.
	 *
	 * @return questionnaire category name
	 */
	public String getQuestionnaireCategoryName() {
		return questionnaireCategoryName;
	}

	/**
	 * Returns the administered questionnaire date.
	 *
	 * @return administered questionnaire date
	 */
	public Date getAdministeredQuestionnaireDate() {
		return administeredQuestionnaireDate;
	}

	/**
	 * Returns the administered questionnaire assessor last name.
	 *
	 * @return administered questionnaire assessor last name
	 */
	public String getAdministeredQuestionnaireAssessorLastName() {
		return administeredQuestionnaireAssessorLastName;
	}

	/**
	 * Returns the administered questionnaire assessor first name.
	 *
	 * @return administered questionnaire assessor first name
	 */
	public String getAdministeredQuestionnaireAssessorFirstName() {
		return administeredQuestionnaireAssessorFirstName;
	}
}