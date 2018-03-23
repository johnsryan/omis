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
package omis.assessment.service.testng;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.annotations.Test;

import omis.assessment.domain.AssessmentCategoryOverride;
import omis.assessment.domain.AssessmentCategoryOverrideReason;
import omis.assessment.domain.AssessmentCategoryScore;
import omis.assessment.domain.AssessmentRating;
import omis.assessment.domain.CategoryOverrideReason;
import omis.assessment.domain.RatingCategory;
import omis.assessment.domain.RatingRank;
import omis.assessment.service.AssessmentService;
import omis.assessment.service.delegate.AssessmentCategoryOverrideDelegate;
import omis.assessment.service.delegate.AssessmentCategoryOverrideReasonDelegate;
import omis.assessment.service.delegate.AssessmentCategoryScoreDelegate;
import omis.assessment.service.delegate.AssessmentRatingDelegate;
import omis.assessment.service.delegate.CategoryOverrideReasonDelegate;
import omis.assessment.service.delegate.RatingCategoryDelegate;
import omis.assessment.service.delegate.RatingRankDelegate;
import omis.datatype.DateRange;
import omis.demographics.domain.Sex;
import omis.exception.DuplicateEntityFoundException;
import omis.person.domain.Person;
import omis.person.service.delegate.PersonDelegate;
import omis.questionnaire.domain.AdministeredQuestionnaire;
import omis.questionnaire.domain.QuestionnaireCategory;
import omis.questionnaire.domain.QuestionnaireType;
import omis.questionnaire.service.delegate.AdministeredQuestionnaireDelegate;
import omis.questionnaire.service.delegate.QuestionnaireCategoryDelegate;
import omis.questionnaire.service.delegate.QuestionnaireTypeDelegate;
import omis.staff.domain.StaffAssignment;
import omis.staff.domain.StaffTitle;
import omis.staff.service.delegate.StaffAssignmentDelegate;
import omis.staff.service.delegate.StaffTitleDelegate;
import omis.supervision.domain.SupervisoryOrganization;
import omis.supervision.service.delegate.SupervisoryOrganizationDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.util.PropertyValueAsserter;

/**
 * Tests method to create assessment category override reasons.
 *
 * @author Josh Divine
 * @version 0.0.1 (Mar 6, 2018)
 * @since OMIS 3.0
 */
@Test
public class AssessmentServiceCreateAssessmentCategoryOverrideReasonTests
	extends AbstractHibernateTransactionalTestNGSpringContextTests {

	/* Delegates. */

	@Autowired
	private AssessmentCategoryOverrideDelegate 
			assessmentCategoryOverrideDelegate;

	@Autowired
	private AssessmentCategoryOverrideReasonDelegate 
			assessmentCategoryOverrideReasonDelegate;
	
	@Autowired
	private CategoryOverrideReasonDelegate categoryOverrideReasonDelegate;

	@Autowired
	private AssessmentCategoryScoreDelegate assessmentCategoryScoreDelegate;

	@Autowired
	private AssessmentRatingDelegate assessmentRatingDelegate;

	@Autowired
	private StaffAssignmentDelegate staffAssignmentDelegate;

	@Autowired
	private AdministeredQuestionnaireDelegate administeredQuestionnaireDelegate;
	
	@Autowired
	private RatingCategoryDelegate ratingCategoryDelegate;
	
	@Autowired
	private PersonDelegate personDelegate;
	
	@Autowired
	private QuestionnaireTypeDelegate questionnaireTypeDelegate;
	
	@Autowired
	private QuestionnaireCategoryDelegate questionnaireCategoryDelegate;
	
	@Autowired
	private SupervisoryOrganizationDelegate supervisoryOrganizationDelegate;
	
	@Autowired
	private StaffTitleDelegate staffTitleDelegate;

	@Autowired
	private RatingRankDelegate ratingRankDelegate;
	
	/* Services. */

	@Autowired
	@Qualifier("assessmentService")
	private AssessmentService assessmentService;

	/* Test methods. */

	/**
	 * Tests the creation of assessment category override reasons.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	@Test
	public void testCreateAssessmentCategoryOverrideReason() 
			throws DuplicateEntityFoundException {
		// Arrangements
		Person answerer = this.personDelegate.create("Answerer", "The", null, 
				null);
		Person assessor = this.personDelegate.create("Assessor", "The", null, 
				null);
		QuestionnaireCategory questionnaireCategory = this
				.questionnaireCategoryDelegate.create("Category", true, null);
		QuestionnaireType questionnaireType = this.questionnaireTypeDelegate
				.create("Title", "Title", 1, questionnaireCategory, true, 
						this.parseDateText("01/01/2000"), null, "Help");
		AdministeredQuestionnaire administeredQuestionnaire = this
				.administeredQuestionnaireDelegate.create(answerer, false, 
						"Comments", assessor, this.parseDateText("01/01/2018"), 
						questionnaireType);
		RatingCategory ratingCategory = this.ratingCategoryDelegate.create(
				"Category", new BigDecimal(100), true);
		AssessmentCategoryScore assessmentCategoryScore = this
				.assessmentCategoryScoreDelegate.create(
						administeredQuestionnaire, ratingCategory, 
						new BigDecimal(75));
		RatingRank ratingRank = this.ratingRankDelegate.create("Rank", 
				ratingCategory, true);
		AssessmentRating assessmentRating = this.assessmentRatingDelegate
				.create(questionnaireType, Sex.MALE, 
						new DateRange(this.parseDateText("01/01/2018"), null), 
						new BigDecimal(0), new BigDecimal(100), true, 
						ratingCategory, "Description", ratingRank);
		String notes = "Notes";
		Person staffMember = this.personDelegate.create("Smith", "John", "Jay", 
				null);
		SupervisoryOrganization supervisoryOrganization = this
				.supervisoryOrganizationDelegate.create("SupOrg", "SO", null);
		StaffTitle title = this.staffTitleDelegate.create("Title", (short) 1, 
				true);
		StaffAssignment approvedStaff = this.staffAssignmentDelegate.create(
				staffMember, supervisoryOrganization, null, 
				new DateRange(this.parseDateText("01/01/2000"), null), title, 
				true, null, null, null, null, null);
		AssessmentCategoryOverride override = this
				.assessmentCategoryOverrideDelegate.create(
						assessmentCategoryScore, assessmentRating, notes, 
						approvedStaff);
		CategoryOverrideReason category = this.categoryOverrideReasonDelegate
				.create("Name", ratingCategory, true);

		// Action
		AssessmentCategoryOverrideReason assessmentCategoryOverrideReason = this
				.assessmentService.createAssessmentCategoryOverrideReason(
						override, category);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("assessmentCategoryOverride", override)
			.addExpectedValue("categoryOverrideReason", category)
			.performAssertions(assessmentCategoryOverrideReason);
	}

	/**
	 * Test that {@code DuplicateEntityFoundException} is thrown.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	@Test(expectedExceptions = {DuplicateEntityFoundException.class})
	public void testDuplicateEntityFoundException() 
			throws DuplicateEntityFoundException {
		// Arrangements
		Person answerer = this.personDelegate.create("Answerer", "The", null, 
				null);
		Person assessor = this.personDelegate.create("Assessor", "The", null, 
				null);
		QuestionnaireCategory questionnaireCategory = this
				.questionnaireCategoryDelegate.create("Category", true, null);
		QuestionnaireType questionnaireType = this.questionnaireTypeDelegate
				.create("Title", "Title", 1, questionnaireCategory, true, 
						this.parseDateText("01/01/2000"), null, "Help");
		AdministeredQuestionnaire administeredQuestionnaire = this
				.administeredQuestionnaireDelegate.create(answerer, false, 
						"Comments", assessor, this.parseDateText("01/01/2018"), 
						questionnaireType);
		RatingCategory ratingCategory = this.ratingCategoryDelegate.create(
				"Category", new BigDecimal(100), true);
		AssessmentCategoryScore assessmentCategoryScore = this
				.assessmentCategoryScoreDelegate.create(
						administeredQuestionnaire, ratingCategory, 
						new BigDecimal(75));
		RatingRank ratingRank = this.ratingRankDelegate.create("Rank", 
				ratingCategory, true);
		AssessmentRating assessmentRating = this.assessmentRatingDelegate
				.create(questionnaireType, Sex.MALE, 
						new DateRange(this.parseDateText("01/01/2018"), null), 
						new BigDecimal(0), new BigDecimal(100), true, 
						ratingCategory, "Description", ratingRank);
		String notes = "Notes";
		Person staffMember = this.personDelegate.create("Smith", "John", "Jay", 
				null);
		SupervisoryOrganization supervisoryOrganization = this
				.supervisoryOrganizationDelegate.create("SupOrg", "SO", null);
		StaffTitle title = this.staffTitleDelegate.create("Title", (short) 1, 
				true);
		StaffAssignment approvedStaff = this.staffAssignmentDelegate.create(
				staffMember, supervisoryOrganization, null, 
				new DateRange(this.parseDateText("01/01/2000"), null), title, 
				true, null, null, null, null, null);
		AssessmentCategoryOverride override = this
				.assessmentCategoryOverrideDelegate.create(
						assessmentCategoryScore, assessmentRating, notes, 
						approvedStaff);
		CategoryOverrideReason category = this.categoryOverrideReasonDelegate
				.create("Name", ratingCategory, true);
		this.assessmentCategoryOverrideReasonDelegate.create(override, category);

		// Action
		this.assessmentService.createAssessmentCategoryOverrideReason(override, 
				category);
	}

	// Parses date text
	private Date parseDateText(final String text) {
		try {
			return new SimpleDateFormat("MM/dd/yyyy").parse(text);
		} catch (ParseException e) {
			throw new RuntimeException("Parse error", e);
		}
	}
}