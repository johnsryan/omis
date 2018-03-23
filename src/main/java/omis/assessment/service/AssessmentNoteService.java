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
package omis.assessment.service;

import java.util.Date;
import java.util.List;
import omis.assessment.domain.AssessmentNote;
import omis.exception.DuplicateEntityFoundException;
import omis.questionnaire.domain.AdministeredQuestionnaire;

/**
 * Assessment Note Service.
 * 
 *@author Annie Wahl 
 *@version 0.1.0 (Mar 15, 2018)
 *@since OMIS 3.0
 *
 */
public interface AssessmentNoteService {
	
	/**
	 * Creates the specified Assessment Note with the given properties.
	 * 
	 * @param administeredQuestionnaire - Administered Questionnaire
	 * @param description - String description
	 * @param date - Date
	 * @return Newly created Assessment Note
	 * @throws DuplicateEntityFoundException - When another Assessment Note
	 * already exists with the given properties.
	 */
	AssessmentNote createAssessmentNote(
			AdministeredQuestionnaire administeredQuestionnaire,
			String description, Date date)
				throws DuplicateEntityFoundException;
	
	/**
	 * Updates the specified Assessment Note with the given properties.
	 * 
	 * @param assessmentNote - Assessment Note to update.
	 * @param administeredQuestionnaire - Administered Questionnaire
	 * @param description - String description
	 * @param date - Date
	 * @return Updated Assessment Note
	 * @throws DuplicateEntityFoundException - When another Assessment Note
	 * already exists with the given properties.
	 */
	AssessmentNote updateAssessmentNote(AssessmentNote assessmentNote,
			AdministeredQuestionnaire administeredQuestionnaire,
			String description, Date date)
				throws DuplicateEntityFoundException;
	
	/**
	 * Removes the specified Assessment Note.
	 * 
	 * @param assessmentNote - Assessment Note to remove.
	 */
	void removeAssessmentNote(AssessmentNote assessmentNote);
	
	/**
	 * Returns a list a Assessment Notes for the specified Administered
	 * Questionnaire.
	 * 
	 * @param administeredQuestionnaire - Administered Questionnaire
	 * @return List a Assessment Notes for the specified Administered
	 * Questionnaire.
	 */
	List<AssessmentNote> findAssessmentNotesByAdministeredQuestionnaire(
			AdministeredQuestionnaire administeredQuestionnaire);
}
