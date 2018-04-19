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
package omis.questionnaire.web.form;

import java.io.Serializable;
import omis.questionnaire.domain.AdministeredQuestionValue;
import omis.questionnaire.domain.AnswerValue;

/**
 * Administered Question Value Item.
 * 
 *@author Annie Wahl 
 *@version 0.1.0 (Mar 28, 2018)
 *@since OMIS 3.0
 *
 */
public class AdministeredQuestionValueItem implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private AdministeredQuestionValue administeredQuestionValue;
	
	private AnswerValue answerValue;
	
	/**
	 * Default Constructor for Administered Question Value Item.
	 */
	public AdministeredQuestionValueItem() {
	}

	/**
	 * Returns the administeredQuestionValue.
	 * @return administeredQuestionValue - AdministeredQuestionValue
	 */
	public AdministeredQuestionValue getAdministeredQuestionValue() {
		return this.administeredQuestionValue;
	}

	/**
	 * Sets the administeredQuestionValue.
	 * @param administeredQuestionValue - AdministeredQuestionValue
	 */
	public void setAdministeredQuestionValue(
			final AdministeredQuestionValue administeredQuestionValue) {
		this.administeredQuestionValue = administeredQuestionValue;
	}

	/**
	 * Returns the answerValue.
	 * @return answerValue - AnswerValue
	 */
	public AnswerValue getAnswerValue() {
		return this.answerValue;
	}

	/**
	 * Sets the answerValue.
	 * @param answerValue - AnswerValue
	 */
	public void setAnswerValue(final AnswerValue answerValue) {
		this.answerValue = answerValue;
	}
	
	
}
