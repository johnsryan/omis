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
package omis.assessment.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import omis.assessment.domain.AssessmentCategoryOverride;
import omis.assessment.domain.AssessmentCategoryOverrideNote;
import omis.assessment.domain.AssessmentCategoryOverrideReason;
import omis.assessment.domain.AssessmentCategoryScore;
import omis.assessment.domain.AssessmentRating;
import omis.assessment.domain.CategoryOverrideReason;
import omis.assessment.domain.RatingCategory;
import omis.assessment.service.AssessmentRatingServiceDelegate;
import omis.assessment.service.AssessmentRatingServiceDelegateRegistry;
import omis.assessment.service.AssessmentService;
import omis.assessment.service.delegate.AssessmentCategoryOverrideDelegate;
import omis.assessment.service.delegate.AssessmentCategoryOverrideNoteDelegate;
import omis.assessment.service.delegate.AssessmentCategoryOverrideReasonDelegate;
import omis.assessment.service.delegate.AssessmentCategoryScoreDelegate;
import omis.assessment.service.delegate.AssessmentRatingDelegate;
import omis.assessment.service.delegate.CategoryOverrideReasonDelegate;
import omis.assessment.service.delegate.RatingCategoryDelegate;
import omis.exception.DuplicateEntityFoundException;
import omis.person.domain.Person;
import omis.questionnaire.domain.AdministeredQuestionnaire;
import omis.questionnaire.domain.QuestionnaireCategory;
import omis.questionnaire.domain.QuestionnaireType;
import omis.questionnaire.service.delegate.AdministeredQuestionnaireDelegate;
import omis.questionnaire.service.delegate.QuestionnaireTypeDelegate;
import omis.staff.domain.StaffAssignment;

/**
 * Implementation of assessment service.
 * 
 * @author Josh Divine
 * @version 0.1.1 (Mar 12, 2018)
 * @since OMIS 3.0
 */
public class AssessmentServiceImpl implements AssessmentService {

	/* Delegates. */
	
	private final AssessmentCategoryOverrideDelegate 
			assessmentCategoryOverrideDelegate;
	
	private final AssessmentCategoryOverrideReasonDelegate 
			assessmentCategoryOverrideReasonDelegate;
	
	private final AssessmentCategoryScoreDelegate 
			assessmentCategoryScoreDelegate;
	
	private final CategoryOverrideReasonDelegate categoryOverrideReasonDelegate;
	
	private final AssessmentRatingDelegate assessmentRatingDelegate;
	
	private final RatingCategoryDelegate ratingCategoryDelegate;
	
	private final AssessmentRatingServiceDelegateRegistry 
			assessmentRatingServiceDelegateRegistry;
	
	private final AdministeredQuestionnaireDelegate 
			administeredQuestionnaireDelegate;
	
	private final QuestionnaireTypeDelegate questionnaireTypeDelegate;
	
	private final AssessmentCategoryOverrideNoteDelegate 
			assessmentCategoryOverrideNoteDelegate;
	
	/**
	 * Instantiates a assessment service implementation with the specified 
	 * delegates.
	 * 
	 * @param assessmentCategoryOverrideDelegate assessment category override 
	 * delegate
	 * @param assessmentCategoryOverrideReasonDelegate assessment category 
	 * override reason delegate
	 * @param assessmentCategoryScoreDelegate assessment category score delegate
	 * @param categoryOverrideReasonDelegate category override reason delegate
	 * @param assessmentRatingDelegate assessment rating delegate
	 * @param administeredQuestionnaireDelegate administered questionnaire 
	 * delegate
	 * @param questionnaireTypeDelegate questionnaire type delegate
	 * @param assessmentCategoryOverrideNoteDelegate assessment category 
	 * override note delegate
	 */
	public AssessmentServiceImpl(
			final AssessmentCategoryOverrideDelegate 
					assessmentCategoryOverrideDelegate,
			final AssessmentCategoryOverrideReasonDelegate 
					assessmentCategoryOverrideReasonDelegate,
			final AssessmentCategoryScoreDelegate 
					assessmentCategoryScoreDelegate,
			final CategoryOverrideReasonDelegate categoryOverrideReasonDelegate,
			final AssessmentRatingDelegate assessmentRatingDelegate,
			final RatingCategoryDelegate ratingCategoryDelegate,
			final AssessmentRatingServiceDelegateRegistry 
					assessmentRatingServiceDelegateRegistry,
			final AdministeredQuestionnaireDelegate 
					administeredQuestionnaireDelegate,
			final QuestionnaireTypeDelegate questionnaireTypeDelegate,
			final AssessmentCategoryOverrideNoteDelegate 
					assessmentCategoryOverrideNoteDelegate) {
		this.assessmentCategoryOverrideDelegate = 
				assessmentCategoryOverrideDelegate;
		this.assessmentCategoryOverrideReasonDelegate = 
				assessmentCategoryOverrideReasonDelegate;
		this.assessmentCategoryScoreDelegate = assessmentCategoryScoreDelegate;
		this.assessmentRatingDelegate = assessmentRatingDelegate;
		this.categoryOverrideReasonDelegate = categoryOverrideReasonDelegate;
		this.ratingCategoryDelegate = ratingCategoryDelegate;
		this.assessmentRatingServiceDelegateRegistry = 
				assessmentRatingServiceDelegateRegistry;
		this.administeredQuestionnaireDelegate = 
				administeredQuestionnaireDelegate;
		this.questionnaireTypeDelegate = questionnaireTypeDelegate;
		this.assessmentCategoryOverrideNoteDelegate = 
				assessmentCategoryOverrideNoteDelegate;
	}
	
	/** {@inheritDoc} */
	@Override
	public AdministeredQuestionnaire assess(
			final AdministeredQuestionnaire administeredQuestionnaire) 
					throws DuplicateEntityFoundException {
		List<AssessmentRatingServiceDelegate> items = this
				.assessmentRatingServiceDelegateRegistry.getItems();
		List<RatingCategory> ratingCategories = this
				.findRatingCategoriesByQuestionnaireType(
						administeredQuestionnaire.getQuestionnaireType());
		for (AssessmentRatingServiceDelegate item : items) {
			for (RatingCategory category : ratingCategories) {
				if (item.isAssessable(category)) {
					BigDecimal score = item.assess(administeredQuestionnaire, 
							category);
					AssessmentCategoryScore assessmentCategoryScore = this
							.findAssessmentCategoryScoreByRatingCategoryAndAdministeredQuestionnaire(
									category, administeredQuestionnaire);
					if (assessmentCategoryScore != null) {
						this.assessmentCategoryScoreDelegate.update(
								assessmentCategoryScore, 
								administeredQuestionnaire, category, score);
					} else {
						this.assessmentCategoryScoreDelegate.create(
								administeredQuestionnaire, category, score);
					}
				}
			}
		}
		return administeredQuestionnaire;
	}

	/** {@inheritDoc} */
	@Override
	public AssessmentCategoryOverride createAssessmentCategoryOverride(
			final AssessmentCategoryScore assessmentCategoryScore,
			final AssessmentRating assessmentRating, final String notes, 
			final StaffAssignment approvedStaff) 
					throws DuplicateEntityFoundException {
		return this.assessmentCategoryOverrideDelegate.create(
				assessmentCategoryScore, assessmentRating, notes, 
				approvedStaff);
	}

	/** {@inheritDoc} */
	@Override
	public AssessmentCategoryOverride updateAssessmentCategoryOverride(
			final AssessmentCategoryOverride assessmentCategoryOverride, 
			final AssessmentCategoryScore assessmentCategoryScore,
			final AssessmentRating assessmentRating, final String notes, 
			final StaffAssignment approvedStaff) 
					throws DuplicateEntityFoundException {
		return this.assessmentCategoryOverrideDelegate.update(
				assessmentCategoryOverride, assessmentCategoryScore, 
				assessmentRating, notes, approvedStaff);
	}

	/** {@inheritDoc} */
	@Override
	public AssessmentCategoryOverrideReason 
			createAssessmentCategoryOverrideReason(
					final AssessmentCategoryOverride override,
					final CategoryOverrideReason category) 
							throws DuplicateEntityFoundException {
		return this.assessmentCategoryOverrideReasonDelegate.create(override, 
				category);
	}

	/** {@inheritDoc} */
	@Override
	public List<RatingCategory> findRatingCategoriesByQuestionnaireType(
			final QuestionnaireType questionnaireType) {
		return this.ratingCategoryDelegate.findByQuestionnaireType(
				questionnaireType);
	}

	/** {@inheritDoc} */
	@Override
	public AssessmentCategoryScore 
			findAssessmentCategoryScoreByRatingCategoryAndAdministeredQuestionnaire(
					final RatingCategory ratingCategory, 
					final AdministeredQuestionnaire administeredQuestionnaire) {
		return this.assessmentCategoryScoreDelegate
				.findByRatingCategoryAndAdministeredQuestionnaire(
						ratingCategory, administeredQuestionnaire);
	}

	/** {@inheritDoc} */
	@Override
	public AssessmentCategoryOverride 
			findAssessmentCategoryOverrideByAssessmentCategoryScore(
					final AssessmentCategoryScore assessmentCategoryScore) {
		return this.assessmentCategoryOverrideDelegate
				.findByAssessmentCategoryScore(assessmentCategoryScore);
	}

	/** {@inheritDoc} */
	@Override
	public List<AssessmentCategoryOverrideReason> 
			findAssessmentCategoryOverrideReasonsByAssessmentCategoryOverride(
					final AssessmentCategoryOverride assessmentCategoryOverride) {
		return this.assessmentCategoryOverrideReasonDelegate
				.findByAssessmentCategoryOverride(assessmentCategoryOverride);
	}

	/** {@inheritDoc} */
	@Override
	public List<CategoryOverrideReason> findCategoryOverrideReasons() {
		return this.categoryOverrideReasonDelegate.findAll();
	}

	/** {@inheritDoc} */
	@Override
	public List<AssessmentRating> findAssessmentRatingsByCategory(
			final RatingCategory ratingCategory) {
		return this.assessmentRatingDelegate.findByRatingCategory(
				ratingCategory);
	}

	/** {@inheritDoc} */
	@Override
	public AdministeredQuestionnaire createAdministeredQuestionnaire(
			final Person answerer, final Boolean draft, final String comments,
			final Person assessor, final Date date, 
			final QuestionnaireType questionnaireType) 
					throws DuplicateEntityFoundException {
		return this.administeredQuestionnaireDelegate.create(answerer, draft, 
				comments, assessor, date, questionnaireType);
	}

	/** {@inheritDoc} */
	@Override
	public List<QuestionnaireType> findQuestionaireTypesByQuestionnaireCategory(
			final QuestionnaireCategory questionnaireCategory) {
		return this.questionnaireTypeDelegate.findAllByQuestionnaireCategory(
				questionnaireCategory);
	}

	/** {@inheritDoc} 
	 * @throws DuplicateEntityFoundException */
	@Override
	public AssessmentCategoryOverrideNote createAssessmentCategoryOverrideNote(
			final AssessmentCategoryOverride assessmentCategoryOverride, 
			final String description, final Date date) 
					throws DuplicateEntityFoundException {
		return this.assessmentCategoryOverrideNoteDelegate.create(
				assessmentCategoryOverride, description, date);
	}

	/** {@inheritDoc} */
	@Override
	public AssessmentCategoryOverrideNote updateAssessmentCategoryOverrideNote(
			final AssessmentCategoryOverrideNote assessmentCategoryOverrideNote,
			final String description, final Date date) 
					throws DuplicateEntityFoundException {
		return this.assessmentCategoryOverrideNoteDelegate.update(
				assessmentCategoryOverrideNote, description, date);
	}

	/** {@inheritDoc} */
	@Override
	public void removeAssessmentCategoryOverrideNote(
			final AssessmentCategoryOverrideNote assessmentCategoryOverrideNote) {
		this.assessmentCategoryOverrideNoteDelegate.remove(
				assessmentCategoryOverrideNote);
	}

	/** {@inheritDoc} */
	@Override
	public List<AssessmentCategoryOverrideNote> 
			findAssessmentCategoryOverrideNotesByAssessmentCategoryOverride(
					final AssessmentCategoryOverride assessmentCategoryOverride) {
		return this.assessmentCategoryOverrideNoteDelegate
				.findByAssessmentCategoryOverride(assessmentCategoryOverride);
	}
}