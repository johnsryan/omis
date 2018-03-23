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
package omis.assessment.dao;

import java.util.Date;
import java.util.List;
import omis.assessment.domain.AssessmentNote;
import omis.dao.GenericDao;
import omis.questionnaire.domain.AdministeredQuestionnaire;

/**
 * Assessment Note Data Access Object.
 * 
 *@author Annie Wahl 
 *@version 0.1.0 (Mar 15, 2018)
 *@since OMIS 3.0
 *
 */
public interface AssessmentNoteDao extends GenericDao<AssessmentNote> {
	
	/**
	 * Returns an Assessment Note found with the specified properties. Returns
	 * <code>null</code> if none are found.
	 * 
	 * @param administeredQuestionnaire - Administered Questionnaire
	 * @param description - String description
	 * @param date - Date
	 * @return Assessment Note found with the specified properties. Returns
	 * <code>null</code> if none are found.
	 */
	AssessmentNote find(
			AdministeredQuestionnaire administeredQuestionnaire,
			String description, Date date);
	
	/**
	 * Returns an Assessment Note found with the specified properties excluding
	 * the specified Assessment Note. Returns <code>null</code> if none are
	 * found.
	 * 
	 * @param administeredQuestionnaire - Administered Questionnaire
	 * @param description - String description
	 * @param date - Date
	 * @param assessmentNoteExcluding - Assessment Note to exclude from search
	 * @return Assessment Note found with the specified properties excluding
	 * the specified Assessment Note. Returns <code>null</code> if none are
	 * found.
	 */
	AssessmentNote findExcluding(
			AdministeredQuestionnaire administeredQuestionnaire,
			String description, Date date,
			AssessmentNote assessmentNoteExcluding);
	
	/**
	 * Returns a list a Assessment Notes for the specified Administered
	 * Questionnaire.
	 * 
	 * @param administeredQuestionnaire - Administered Questionnaire
	 * @return List a Assessment Notes for the specified Administered
	 * Questionnaire.
	 */
	List<AssessmentNote> findByAdministeredQuestionnaire(
			AdministeredQuestionnaire administeredQuestionnaire);
}
