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
package omis.assessment.web.form;

import java.io.Serializable;
import java.util.Date;

import omis.person.domain.Person;
import omis.questionnaire.domain.QuestionnaireType;

/**
 * Assessment form.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Mar 27, 2018)
 * @since OMIS 3.0
 */
public class AssessmentForm implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private QuestionnaireType questionnaireType;
	
	private Date assessDate;
	
	private Person assessor;
	
	private String comments;
	
	/**
	 * Default constructor for assessment form. 
	 */
	public AssessmentForm() {
	}

	/**
	 * Returns the questionnaire type.
	 *
	 * @return questionnaire type
	 */
	public QuestionnaireType getQuestionnaireType() {
		return questionnaireType;
	}

	/**
	 * Sets the questionnaire type.
	 *
	 * @param questionnaireType questionnaire type
	 */
	public void setQuestionnaireType(
			final QuestionnaireType questionnaireType) {
		this.questionnaireType = questionnaireType;
	}

	/**
	 * Returns the assess date.
	 *
	 * @return assess date
	 */
	public Date getAssessDate() {
		return assessDate;
	}

	/**
	 * Sets the assess date.
	 *
	 * @param assessDate assess date
	 */
	public void setAssessDate(final Date assessDate) {
		this.assessDate = assessDate;
	}

	/**
	 * Returns the assessor.
	 *
	 * @return assessor
	 */
	public Person getAssessor() {
		return assessor;
	}

	/**
	 * Sets the assessor.
	 *
	 * @param assessor assessor
	 */
	public void setAssessor(final Person assessor) {
		this.assessor = assessor;
	}

	/**
	 * Returns the comments.
	 *
	 * @return comments
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * Sets the comments.
	 *
	 * @param comments comments
	 */
	public void setComments(final String comments) {
		this.comments = comments;
	}
}