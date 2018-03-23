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

import omis.assessment.domain.AssessmentCategoryOverride;
import omis.assessment.domain.AssessmentCategoryOverrideNote;
import omis.assessment.domain.AssessmentCategoryOverrideReason;
import omis.assessment.domain.AssessmentCategoryScore;
import omis.assessment.domain.AssessmentRating;
import omis.assessment.domain.CategoryOverrideReason;
import omis.assessment.domain.RatingCategory;
import omis.exception.DuplicateEntityFoundException;
import omis.person.domain.Person;
import omis.questionnaire.domain.AdministeredQuestionnaire;
import omis.questionnaire.domain.QuestionnaireCategory;
import omis.questionnaire.domain.QuestionnaireType;
import omis.staff.domain.StaffAssignment;

/**
 * Assessment service.
 * 
 * @author Josh Divine
 * @version 0.1.1 (Mar 12, 2018)
 * @since OMIS 3.0
 */
public interface AssessmentService {

	/**
	 * Assesses the administered questionnaire.
	 * 
	 * @param administeredQuestionnaire administered questionnaire
	 * @return administered questionnaire
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	AdministeredQuestionnaire assess(
			AdministeredQuestionnaire administeredQuestionnaire) 
					throws DuplicateEntityFoundException;
	
	/**
	 * Creates a new assessment category override from the specified parameters.
	 * 
	 * @param assessmentCategoryScore assessment category score
	 * @param assessmentRating assessment rating
	 * @param notes notes
	 * @param approvedStaff approved staff
	 * @return assessment category override
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	AssessmentCategoryOverride createAssessmentCategoryOverride(
			AssessmentCategoryScore assessmentCategoryScore, 
			AssessmentRating assessmentRating, String notes, 
			StaffAssignment approvedStaff) throws DuplicateEntityFoundException;
	
	/**
	 * Updates an existing assessment category override from the specified 
	 * parameters.
	 * 
	 * @param assessmentCategoryOverride assessment category override
	 * @param assessmentCategoryScore assessment category score
	 * @param assessmentRating assessment rating
	 * @param notes notes
	 * @param approvedStaff approved staff
	 * @return assessment category override
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	AssessmentCategoryOverride updateAssessmentCategoryOverride(
			AssessmentCategoryOverride assessmentCategoryOverride, 
			AssessmentCategoryScore assessmentCategoryScore, 
			AssessmentRating assessmentRating, String notes, 
			StaffAssignment approvedStaff) throws DuplicateEntityFoundException;
	
	/**
	 * Creates a new assessment category override reason from the specified 
	 * parameters.
	 * 
	 * @param override assessment category override
	 * @param category category override reason
	 * @return assessment category override reason
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	AssessmentCategoryOverrideReason createAssessmentCategoryOverrideReason(
			AssessmentCategoryOverride override, 
			CategoryOverrideReason category) 
					throws DuplicateEntityFoundException;
	
	/**
	 * Returns a list of rating categories for the specified questionnaire type.
	 * 
	 * @param questionnaireType questionnaire type
	 * @return list of rating categories
	 */
	List<RatingCategory> findRatingCategoriesByQuestionnaireType(
			QuestionnaireType questionnaireType);
	
	/**
	 * Returns the assessment category score for the specified rating category 
	 * and administered questionnaire.
	 * 
	 * @param ratingCategory rating category
	 * @param administeredQuestionnaire administered questionnaire
	 * @return assessment category score
	 */
	AssessmentCategoryScore 
			findAssessmentCategoryScoreByRatingCategoryAndAdministeredQuestionnaire(
					RatingCategory ratingCategory, 
					AdministeredQuestionnaire administeredQuestionnaire);
	
	/**
	 * Returns the assessment category override for the specified assessment 
	 * category score.
	 * 
	 * @param assessmentCategoryScore assessment category score
	 * @return assessment category override
	 */
	AssessmentCategoryOverride 
			findAssessmentCategoryOverrideByAssessmentCategoryScore(
					AssessmentCategoryScore assessmentCategoryScore);
	
	/**
	 * Returns a list of assessment category override reasons for the specified 
	 * assessment category override.
	 * 
	 * @param assessmentCategoryOverride assessment category override
	 * @return list of assessment category override reasons
	 */
	List<AssessmentCategoryOverrideReason> 
			findAssessmentCategoryOverrideReasonsByAssessmentCategoryOverride(
					AssessmentCategoryOverride assessmentCategoryOverride);
	
	/**
	 * Returns a list of category override reasons.
	 * 
	 * @return list of category override reasons
	 */
	List<CategoryOverrideReason> findCategoryOverrideReasons();
	
	/**
	 * Returns a list of assessment ratings for the specified rating category.
	 * 
	 * @param ratingCategory rating category
	 * @return list of assessment ratings
	 */
	List<AssessmentRating> findAssessmentRatingsByCategory(
			RatingCategory ratingCategory);
	
	/**
	 * Creates an administered questionnaire.
	 * 
	 * @param answerer answerer
	 * @param draft draft 
	 * @param comments comments
	 * @param assessor assessor
	 * @param date date
	 * @param questionnaireType questionnaire type
	 * @return administered questionnaire
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	AdministeredQuestionnaire createAdministeredQuestionnaire(Person answerer, 
			Boolean draft, String comments, Person assessor, Date date, 
			QuestionnaireType questionnaireType) 
					throws DuplicateEntityFoundException;
	
	/**
	 * Returns a list of questionnaire types for the specified questionnaire 
	 * category.
	 * 
	 * @param questionnaireCategory questionnaire category
	 * @return list of questionnaire types
	 */
	List<QuestionnaireType> findQuestionaireTypesByQuestionnaireCategory(
			QuestionnaireCategory questionnaireCategory);
	
	/**
	 * Creates a new assessment category override note.
	 * 
	 * @param assessmentCategoryOverride assessment category override
	 * @param description description
	 * @param date date
	 * @return assessment category override note
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	AssessmentCategoryOverrideNote createAssessmentCategoryOverrideNote(
			AssessmentCategoryOverride assessmentCategoryOverride, 
			String description, Date date) throws DuplicateEntityFoundException;
	
	/**
	 * Updates an existing assessment category override note.
	 * 
	 * @param assessmentCategoryOverrideNote assessment category override note
	 * @param description description
	 * @param date date
	 * @return assessment category override note
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	AssessmentCategoryOverrideNote updateAssessmentCategoryOverrideNote(
			AssessmentCategoryOverrideNote assessmentCategoryOverrideNote, 
			String description, Date date) throws DuplicateEntityFoundException;
	
	/**
	 * Removes the specified assessment category override note.
	 * 
	 * @param assessmentCategoryOverrideNote assessment category override note
	 */
	void removeAssessmentCategoryOverrideNote(
			AssessmentCategoryOverrideNote assessmentCategoryOverrideNote);
	
	/**
	 * Returns a list of assessment category override notes for the specified 
	 * assessment category override.
	 * 
	 * @param assessmentCategoryOverride assessment category override
	 * @return list of assessment category override notes
	 */
	List<AssessmentCategoryOverrideNote> 
			findAssessmentCategoryOverrideNotesByAssessmentCategoryOverride(
					AssessmentCategoryOverride assessmentCategoryOverride);
}