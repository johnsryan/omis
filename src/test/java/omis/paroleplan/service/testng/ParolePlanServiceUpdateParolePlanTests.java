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
package omis.paroleplan.service.testng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.annotations.Test;

import omis.datatype.DateRange;
import omis.exception.DuplicateEntityFoundException;
import omis.offender.domain.Offender;
import omis.offender.service.delegate.OffenderDelegate;
import omis.paroleeligibility.domain.AppearanceCategory;
import omis.paroleeligibility.domain.EligibilityStatusCategory;
import omis.paroleeligibility.domain.EligibilityStatusReason;
import omis.paroleeligibility.domain.ParoleEligibility;
import omis.paroleeligibility.domain.component.ParoleEligibilityStatus;
import omis.paroleeligibility.service.delegate.AppearanceCategoryDelegate;
import omis.paroleeligibility.service.delegate.EligibilityStatusReasonDelegate;
import omis.paroleeligibility.service.delegate.ParoleEligibilityDelegate;
import omis.paroleplan.domain.ParolePlan;
import omis.paroleplan.service.ParolePlanService;
import omis.paroleplan.service.delegate.ParolePlanDelegate;
import omis.person.domain.Person;
import omis.person.service.delegate.PersonDelegate;
import omis.staff.domain.StaffAssignment;
import omis.staff.domain.StaffTitle;
import omis.staff.service.delegate.StaffAssignmentDelegate;
import omis.staff.service.delegate.StaffTitleDelegate;
import omis.supervision.domain.SupervisoryOrganization;
import omis.supervision.service.delegate.SupervisoryOrganizationDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.util.PropertyValueAsserter;

/**
 * Tests method to update parole plans.
 *
 * @author Josh Divine
 * @version 0.0.1 (Feb 13, 2018)
 * @since OMIS 3.0
 */
@Test
public class ParolePlanServiceUpdateParolePlanTests
	extends AbstractHibernateTransactionalTestNGSpringContextTests {

	/* Delegates. */

	@Autowired
	@Qualifier("staffAssignmentDelegate")
	private StaffAssignmentDelegate staffAssignmentDelegate;
	
	@Autowired
	@Qualifier("personDelegate")
	private PersonDelegate personDelegate;
	
	@Autowired
	@Qualifier("supervisoryOrganizationDelegate")
	private SupervisoryOrganizationDelegate supervisoryOrganizationDelegate;
	
	@Autowired
	@Qualifier("staffTitleDelegate")
	private StaffTitleDelegate staffTitleDelegate;
	
	@Autowired
	@Qualifier("offenderDelegate")
	private OffenderDelegate offenderDelegate;
	
	@Autowired
	@Qualifier("appearanceCategoryDelegate")
	private AppearanceCategoryDelegate appearanceCategoryDelegate;
	
	@Autowired
	@Qualifier("eligibilityStatusReasonDelegate")
	private EligibilityStatusReasonDelegate eligibilityStatusReasonDelegate;
	
	@Autowired
	@Qualifier("paroleEligibilityDelegate")
	private ParoleEligibilityDelegate paroleEligibilityDelegate;
	
	@Autowired
	@Qualifier("parolePlanDelegate")
	private ParolePlanDelegate parolePlanDelegate;
	
	/* Services. */

	@Autowired
	@Qualifier("parolePlanService")
	private ParolePlanService parolePlanService;

	/* Test methods. */

	/**
	 * Tests the update of the evaluator for a parole plan.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	@Test
	public void testUpdateParolePlanEvaluator() 
			throws DuplicateEntityFoundException {
		// Arrangements
		Person staffMember = this.personDelegate.create("Smith", "John", "Jay", 
				null);
		SupervisoryOrganization supervisoryOrganization = this
				.supervisoryOrganizationDelegate.create("SupOrg", "SO", null);
		StaffTitle title = this.staffTitleDelegate.create("Title", (short) 1, 
				true);
		StaffAssignment evaluator = this.staffAssignmentDelegate.create(
				staffMember, supervisoryOrganization, null, 
				new DateRange(this.parseDateText("01/01/2017"), null), title, 
				true, null, null, null, null, null);
		Offender offender = this.offenderDelegate.createWithoutIdentity("Smith",
				"John", "Jay", null);
		EligibilityStatusReason statusReason = this
				.eligibilityStatusReasonDelegate.create("Reason",
						EligibilityStatusCategory.APPEARING, true);
		AppearanceCategory appearanceCategory = this.appearanceCategoryDelegate
				.create("Category", true);
		ParoleEligibility paroleEligibility = this.paroleEligibilityDelegate
				.create(offender, this.parseDateText("01/01/2017"),
						this.parseDateText("01/01/2017"),
						new ParoleEligibilityStatus(
								this.parseDateText("01/01/2017"),
								"Comment", EligibilityStatusCategory.APPEARING,
								statusReason), appearanceCategory);
		String evaluationDescription = "Description";
		String vocationalPlan = "Vocational plan";
		String residencePlan = "Residence plan";
		String treatmentPlan = "Treatment plan";
		ParolePlan parolePlan = this.parolePlanDelegate.create(
				paroleEligibility, evaluator, evaluationDescription, 
				vocationalPlan, residencePlan, treatmentPlan);
		Person newStaffMember = this.personDelegate.create("Smith", "Tom", null, 
				null);
		StaffAssignment newEvaluator = this.staffAssignmentDelegate.create(
				newStaffMember, supervisoryOrganization, null, 
				new DateRange(this.parseDateText("01/01/2017"), null), title, 
				true, null, null, null, null, null);

		// Action
		parolePlan = this.parolePlanService.updateParolePlan(parolePlan, 
				newEvaluator, evaluationDescription, vocationalPlan, 
				residencePlan, treatmentPlan);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("paroleEligibility", paroleEligibility)
			.addExpectedValue("evaluation.staffAssignment", newEvaluator)
			.addExpectedValue("evaluation.description", evaluationDescription)
			.addExpectedValue("vocationalPlan", vocationalPlan)
			.addExpectedValue("residencePlan", residencePlan)
			.addExpectedValue("treatmentPlan", treatmentPlan)
			.performAssertions(parolePlan);
	}

	/**
	 * Tests the update of the evaluation description for a parole plan.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	@Test
	public void testUpdateParolePlanEvaluationDescription() 
			throws DuplicateEntityFoundException {
		// Arrangements
		Person staffMember = this.personDelegate.create("Smith", "John", "Jay", 
				null);
		SupervisoryOrganization supervisoryOrganization = this
				.supervisoryOrganizationDelegate.create("SupOrg", "SO", null);
		StaffTitle title = this.staffTitleDelegate.create("Title", (short) 1, 
				true);
		StaffAssignment evaluator = this.staffAssignmentDelegate.create(
				staffMember, supervisoryOrganization, null, 
				new DateRange(this.parseDateText("01/01/2017"), null), title, 
				true, null, null, null, null, null);
		Offender offender = this.offenderDelegate.createWithoutIdentity("Smith",
				"John", "Jay", null);
		EligibilityStatusReason statusReason = this
				.eligibilityStatusReasonDelegate.create("Reason",
						EligibilityStatusCategory.APPEARING, true);
		AppearanceCategory appearanceCategory = this.appearanceCategoryDelegate
				.create("Category", true);
		ParoleEligibility paroleEligibility = this.paroleEligibilityDelegate
				.create(offender, this.parseDateText("01/01/2017"),
						this.parseDateText("01/01/2017"),
						new ParoleEligibilityStatus(
								this.parseDateText("01/01/2017"),
								"Comment", EligibilityStatusCategory.APPEARING,
								statusReason), appearanceCategory);
		String evaluationDescription = "Description";
		String vocationalPlan = "Vocational plan";
		String residencePlan = "Residence plan";
		String treatmentPlan = "Treatment plan";
		ParolePlan parolePlan = this.parolePlanDelegate.create(
				paroleEligibility, evaluator, evaluationDescription, 
				vocationalPlan, residencePlan, treatmentPlan);
		String newEvaluationDescription = "New description";
		
		// Action
		parolePlan = this.parolePlanService.updateParolePlan(parolePlan, 
				evaluator, newEvaluationDescription, vocationalPlan, 
				residencePlan, treatmentPlan);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("paroleEligibility", paroleEligibility)
			.addExpectedValue("evaluation.staffAssignment", evaluator)
			.addExpectedValue("evaluation.description", newEvaluationDescription)
			.addExpectedValue("vocationalPlan", vocationalPlan)
			.addExpectedValue("residencePlan", residencePlan)
			.addExpectedValue("treatmentPlan", treatmentPlan)
			.performAssertions(parolePlan);
	}
	
	/**
	 * Tests the update of the vocational plan for a parole plan.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	@Test
	public void testUpdateParolePlanVocationalPlan() 
			throws DuplicateEntityFoundException {
		// Arrangements
		Person staffMember = this.personDelegate.create("Smith", "John", "Jay", 
				null);
		SupervisoryOrganization supervisoryOrganization = this
				.supervisoryOrganizationDelegate.create("SupOrg", "SO", null);
		StaffTitle title = this.staffTitleDelegate.create("Title", (short) 1, 
				true);
		StaffAssignment evaluator = this.staffAssignmentDelegate.create(
				staffMember, supervisoryOrganization, null, 
				new DateRange(this.parseDateText("01/01/2017"), null), title, 
				true, null, null, null, null, null);
		Offender offender = this.offenderDelegate.createWithoutIdentity("Smith",
				"John", "Jay", null);
		EligibilityStatusReason statusReason = this
				.eligibilityStatusReasonDelegate.create("Reason",
						EligibilityStatusCategory.APPEARING, true);
		AppearanceCategory appearanceCategory = this.appearanceCategoryDelegate
				.create("Category", true);
		ParoleEligibility paroleEligibility = this.paroleEligibilityDelegate
				.create(offender, this.parseDateText("01/01/2017"),
						this.parseDateText("01/01/2017"),
						new ParoleEligibilityStatus(
								this.parseDateText("01/01/2017"),
								"Comment", EligibilityStatusCategory.APPEARING,
								statusReason), appearanceCategory);
		String evaluationDescription = "Description";
		String vocationalPlan = "Vocational plan";
		String residencePlan = "Residence plan";
		String treatmentPlan = "Treatment plan";
		ParolePlan parolePlan = this.parolePlanDelegate.create(
				paroleEligibility, evaluator, evaluationDescription, 
				vocationalPlan, residencePlan, treatmentPlan);
		String newVocationalPlan = "New vocational plan";
		
		// Action
		parolePlan = this.parolePlanService.updateParolePlan(parolePlan, 
				evaluator, evaluationDescription, newVocationalPlan, 
				residencePlan, treatmentPlan);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("paroleEligibility", paroleEligibility)
			.addExpectedValue("evaluation.staffAssignment", evaluator)
			.addExpectedValue("evaluation.description", evaluationDescription)
			.addExpectedValue("vocationalPlan", newVocationalPlan)
			.addExpectedValue("residencePlan", residencePlan)
			.addExpectedValue("treatmentPlan", treatmentPlan)
			.performAssertions(parolePlan);
	}
	
	/**
	 * Tests the update of the residence plan for a parole plan.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	@Test
	public void testUpdateParolePlanResidencePlan() 
			throws DuplicateEntityFoundException {
		// Arrangements
		Person staffMember = this.personDelegate.create("Smith", "John", "Jay", 
				null);
		SupervisoryOrganization supervisoryOrganization = this
				.supervisoryOrganizationDelegate.create("SupOrg", "SO", null);
		StaffTitle title = this.staffTitleDelegate.create("Title", (short) 1, 
				true);
		StaffAssignment evaluator = this.staffAssignmentDelegate.create(
				staffMember, supervisoryOrganization, null, 
				new DateRange(this.parseDateText("01/01/2017"), null), title, 
				true, null, null, null, null, null);
		Offender offender = this.offenderDelegate.createWithoutIdentity("Smith",
				"John", "Jay", null);
		EligibilityStatusReason statusReason = this
				.eligibilityStatusReasonDelegate.create("Reason",
						EligibilityStatusCategory.APPEARING, true);
		AppearanceCategory appearanceCategory = this.appearanceCategoryDelegate
				.create("Category", true);
		ParoleEligibility paroleEligibility = this.paroleEligibilityDelegate
				.create(offender, this.parseDateText("01/01/2017"),
						this.parseDateText("01/01/2017"),
						new ParoleEligibilityStatus(
								this.parseDateText("01/01/2017"),
								"Comment", EligibilityStatusCategory.APPEARING,
								statusReason), appearanceCategory);
		String evaluationDescription = "Description";
		String vocationalPlan = "Vocational plan";
		String residencePlan = "Residence plan";
		String treatmentPlan = "Treatment plan";
		ParolePlan parolePlan = this.parolePlanDelegate.create(
				paroleEligibility, evaluator, evaluationDescription, 
				vocationalPlan, residencePlan, treatmentPlan);
		String newResidencePlan = "New residence plan";
		
		// Action
		parolePlan = this.parolePlanService.updateParolePlan(parolePlan, 
				evaluator, evaluationDescription, vocationalPlan, 
				newResidencePlan, treatmentPlan);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("paroleEligibility", paroleEligibility)
			.addExpectedValue("evaluation.staffAssignment", evaluator)
			.addExpectedValue("evaluation.description", evaluationDescription)
			.addExpectedValue("vocationalPlan", vocationalPlan)
			.addExpectedValue("residencePlan", newResidencePlan)
			.addExpectedValue("treatmentPlan", treatmentPlan)
			.performAssertions(parolePlan);
	}
	
	/**
	 * Tests the update of the treatment plan for a parole plan.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	@Test
	public void testUpdateParolePlanTreatmentPlan() 
			throws DuplicateEntityFoundException {
		// Arrangements
		Person staffMember = this.personDelegate.create("Smith", "John", "Jay", 
				null);
		SupervisoryOrganization supervisoryOrganization = this
				.supervisoryOrganizationDelegate.create("SupOrg", "SO", null);
		StaffTitle title = this.staffTitleDelegate.create("Title", (short) 1, 
				true);
		StaffAssignment evaluator = this.staffAssignmentDelegate.create(
				staffMember, supervisoryOrganization, null, 
				new DateRange(this.parseDateText("01/01/2017"), null), title, 
				true, null, null, null, null, null);
		Offender offender = this.offenderDelegate.createWithoutIdentity("Smith",
				"John", "Jay", null);
		EligibilityStatusReason statusReason = this
				.eligibilityStatusReasonDelegate.create("Reason",
						EligibilityStatusCategory.APPEARING, true);
		AppearanceCategory appearanceCategory = this.appearanceCategoryDelegate
				.create("Category", true);
		ParoleEligibility paroleEligibility = this.paroleEligibilityDelegate
				.create(offender, this.parseDateText("01/01/2017"),
						this.parseDateText("01/01/2017"),
						new ParoleEligibilityStatus(
								this.parseDateText("01/01/2017"),
								"Comment", EligibilityStatusCategory.APPEARING,
								statusReason), appearanceCategory);
		String evaluationDescription = "Description";
		String vocationalPlan = "Vocational plan";
		String residencePlan = "Residence plan";
		String treatmentPlan = "Treatment plan";
		ParolePlan parolePlan = this.parolePlanDelegate.create(
				paroleEligibility, evaluator, evaluationDescription, 
				vocationalPlan, residencePlan, treatmentPlan);
		String newTreatmentPlan = "New treatment plan";
		
		// Action
		parolePlan = this.parolePlanService.updateParolePlan(parolePlan, 
				evaluator, evaluationDescription, vocationalPlan, residencePlan, 
				newTreatmentPlan);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("paroleEligibility", paroleEligibility)
			.addExpectedValue("evaluation.staffAssignment", evaluator)
			.addExpectedValue("evaluation.description", evaluationDescription)
			.addExpectedValue("vocationalPlan", vocationalPlan)
			.addExpectedValue("residencePlan", residencePlan)
			.addExpectedValue("treatmentPlan", newTreatmentPlan)
			.performAssertions(parolePlan);
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