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
import omis.document.domain.Document;
import omis.document.service.delegate.DocumentDelegate;
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
import omis.paroleplan.domain.ParolePlanDocumentAssociation;
import omis.paroleplan.service.ParolePlanService;
import omis.paroleplan.service.delegate.ParolePlanDelegate;
import omis.paroleplan.service.delegate.ParolePlanDocumentAssociationDelegate;
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
 * Tests method to create parole plan document associations.
 *
 * @author Josh Divine
 * @version 0.0.1 (Feb 13, 2018)
 * @since OMIS 3.0
 */
@Test
public class ParolePlanServiceCreateParolePlanDocumentAssociationTests
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
	
	@Autowired
	@Qualifier("parolePlanDocumentAssociationDelegate")
	private ParolePlanDocumentAssociationDelegate 
			parolePlanDocumentAssociationDelegate;
	
	@Autowired
	@Qualifier("documentDelegate")
	private DocumentDelegate documentDelegate;
	
	/* Services. */

	@Autowired
	@Qualifier("parolePlanService")
	private ParolePlanService parolePlanService;

	/* Test methods. */

	/**
	 * Tests the creation of parole plan document associations.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	@Test
	public void testCreateParolePlanDocumentAssociation() 
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
		ParolePlan parolePlan = this.parolePlanDelegate.create(
				paroleEligibility, evaluator, evaluationDescription, null, null, 
				null);
		Document document = this.documentDelegate.create(
				this.parseDateText("01/01/2018"), "filename", "123", "title");

		// Action
		ParolePlanDocumentAssociation parolePlanDocumentAssociation = this
				.parolePlanService.createParolePlanDocumentAssociation(
						parolePlan, document);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("parolePlan", parolePlan)
			.addExpectedValue("document", document)
			.performAssertions(parolePlanDocumentAssociation);
	}

	/**
	 * Tests {@code DuplicateEntityFoundException} is thrown.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	@Test(expectedExceptions = {DuplicateEntityFoundException.class})
	public void testDuplicateEntityFoundException() 
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
		ParolePlan parolePlan = this.parolePlanDelegate.create(
				paroleEligibility, evaluator, evaluationDescription, null, null, 
				null);
		Document document = this.documentDelegate.create(
				this.parseDateText("01/01/2018"), "filename", "123", "title");
		this.parolePlanDocumentAssociationDelegate.create(parolePlan, document);
		
		// Action
		this.parolePlanService.createParolePlanDocumentAssociation(parolePlan, 
				document);
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