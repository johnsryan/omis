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
package omis.parolereview.service.testng;

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
import omis.parolereview.domain.ParoleEndorsementCategory;
import omis.parolereview.domain.ParoleReview;
import omis.parolereview.domain.StaffRoleCategory;
import omis.parolereview.service.ParoleReviewService;
import omis.parolereview.service.delegate.ParoleEndorsementCategoryDelegate;
import omis.parolereview.service.delegate.ParoleReviewDelegate;
import omis.parolereview.service.delegate.StaffRoleCategoryDelegate;
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
 * Tests method to update parole reviews.
 *
 * @author Josh Divine
 * @version 0.0.1
 * @since OMIS 3.0
 */
@Test
public class ParoleReviewServiceUpdateParoleReviewTests
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
	@Qualifier("paroleReviewDelegate")
	private ParoleReviewDelegate paroleReviewDelegate;

	@Autowired
	@Qualifier("paroleEndorsementCategoryDelegate")
	private ParoleEndorsementCategoryDelegate paroleEndorsementCategoryDelegate;
	
	@Autowired
	@Qualifier("staffRoleCategoryDelegate")
	private StaffRoleCategoryDelegate staffRoleCategoryDelegate;
	
	/* Services. */

	@Autowired
	@Qualifier("paroleReviewService")
	private ParoleReviewService paroleReviewService;

	/* Test methods. */

	/**
	 * Tests the update of the staff assignment for a parole review.
	 * 
	 * @throws DuplicateEntityFoundException if entity already exists
	 */
	@Test
	public void testUpdateParoleReviewStaffAssignment() 
			throws DuplicateEntityFoundException {
		// Arrangements
		Person staffMember = this.personDelegate.create("Smith", "John", "Jay", 
				null);
		SupervisoryOrganization supervisoryOrganization = this
				.supervisoryOrganizationDelegate.create("SupOrg", "SO", null);
		StaffTitle title = this.staffTitleDelegate.create("Title", (short) 1, 
				true);
		StaffAssignment staffAssignment = this.staffAssignmentDelegate.create(
				staffMember, supervisoryOrganization, null, 
				new DateRange(this.parseDateText("01/01/2017"), null), title, 
				true, null, null, null, null, null);
		Date date = this.parseDateText("01/01/2018");
		String text = "Text";
		Offender offender = this.offenderDelegate.createWithoutIdentity("Smith",
				"Tom", "Harry", null);
		ParoleEndorsementCategory endorsement = this
				.paroleEndorsementCategoryDelegate.create("Name", true);
		StaffRoleCategory staffRole = this.staffRoleCategoryDelegate.create(
				"Name", true);
		ParoleReview paroleReview = this.paroleReviewDelegate.create(
				staffAssignment, date, text, offender, endorsement, staffRole);
		Person newStaffMember = this.personDelegate.create("Smith", "Jeff", 
				"Bob", null);
		StaffAssignment newStaffAssignment = this.staffAssignmentDelegate
				.create(newStaffMember, supervisoryOrganization, null, 
				new DateRange(this.parseDateText("01/01/2017"), null), title, 
				true, null, null, null, null, null);
		
		// Action
		paroleReview = this.paroleReviewService.updateParoleReview(paroleReview, 
				newStaffAssignment, date, text, endorsement, staffRole);

		// Assertions
		PropertyValueAsserter.create()
		.addExpectedValue("staffAssignment", newStaffAssignment)
		.addExpectedValue("date", date)
		.addExpectedValue("text", text)
		.addExpectedValue("offender", offender)
		.addExpectedValue("endorsement", endorsement)
		.addExpectedValue("staffRole", staffRole)
		.performAssertions(paroleReview);
	}
	
	/**
	 * Tests the update of the date for a parole review.
	 * 
	 * @throws DuplicateEntityFoundException if entity already exists
	 */
	@Test
	public void testUpdateParoleReviewDate() 
			throws DuplicateEntityFoundException {
		// Arrangements
		Person staffMember = this.personDelegate.create("Smith", "John", "Jay", 
				null);
		SupervisoryOrganization supervisoryOrganization = this
				.supervisoryOrganizationDelegate.create("SupOrg", "SO", null);
		StaffTitle title = this.staffTitleDelegate.create("Title", (short) 1, 
				true);
		StaffAssignment staffAssignment = this.staffAssignmentDelegate.create(
				staffMember, supervisoryOrganization, null, 
				new DateRange(this.parseDateText("01/01/2017"), null), title, 
				true, null, null, null, null, null);
		Date date = this.parseDateText("01/01/2018");
		String text = "Text";
		Offender offender = this.offenderDelegate.createWithoutIdentity("Smith",
				"Tom", "Harry", null);
		ParoleEndorsementCategory endorsement = this
				.paroleEndorsementCategoryDelegate.create("Name", true);
		StaffRoleCategory staffRole = this.staffRoleCategoryDelegate.create(
				"Name", true);
		ParoleReview paroleReview = this.paroleReviewDelegate.create(
				staffAssignment, date, text, offender, endorsement, staffRole);
		Date newDate = this.parseDateText("01/02/2018");
		
		// Action
		paroleReview = this.paroleReviewService.updateParoleReview(paroleReview,
				staffAssignment, newDate, text, endorsement, staffRole);

		// Assertions
		PropertyValueAsserter.create()
		.addExpectedValue("staffAssignment", staffAssignment)
		.addExpectedValue("date", newDate)
		.addExpectedValue("text", text)
		.addExpectedValue("offender", offender)
		.addExpectedValue("endorsement", endorsement)
		.addExpectedValue("staffRole", staffRole)
		.performAssertions(paroleReview);
	}

	/**
	 * Tests the update of the text for a parole review.
	 * 
	 * @throws DuplicateEntityFoundException if entity already exists
	 */
	@Test
	public void testUpdateParoleReviewText() 
			throws DuplicateEntityFoundException {
		// Arrangements
		Person staffMember = this.personDelegate.create("Smith", "John", "Jay", 
				null);
		SupervisoryOrganization supervisoryOrganization = this
				.supervisoryOrganizationDelegate.create("SupOrg", "SO", null);
		StaffTitle title = this.staffTitleDelegate.create("Title", (short) 1, 
				true);
		StaffAssignment staffAssignment = this.staffAssignmentDelegate.create(
				staffMember, supervisoryOrganization, null, 
				new DateRange(this.parseDateText("01/01/2017"), null), title, 
				true, null, null, null, null, null);
		Date date = this.parseDateText("01/01/2018");
		String text = "Text";
		Offender offender = this.offenderDelegate.createWithoutIdentity("Smith",
				"Tom", "Harry", null);
		ParoleEndorsementCategory endorsement = this
				.paroleEndorsementCategoryDelegate.create("Name", true);
		StaffRoleCategory staffRole = this.staffRoleCategoryDelegate.create(
				"Name", true);
		ParoleReview paroleReview = this.paroleReviewDelegate.create(
				staffAssignment, date, text, offender, endorsement, staffRole);
		String newText = "New text";
		
		// Action
		paroleReview = this.paroleReviewService.updateParoleReview(paroleReview, 
				staffAssignment, date, newText, endorsement, staffRole);

		// Assertions
		PropertyValueAsserter.create()
		.addExpectedValue("staffAssignment", staffAssignment)
		.addExpectedValue("date", date)
		.addExpectedValue("text", newText)
		.addExpectedValue("offender", offender)
		.addExpectedValue("endorsement", endorsement)
		.addExpectedValue("staffRole", staffRole)
		.performAssertions(paroleReview);
	}
	
	/**
	 * Tests the update of the endorsement for a parole review.
	 * 
	 * @throws DuplicateEntityFoundException if entity already exists
	 */
	@Test
	public void testUpdateParoleReviewEndorsement() 
			throws DuplicateEntityFoundException {
		// Arrangements
		Person staffMember = this.personDelegate.create("Smith", "John", "Jay", 
				null);
		SupervisoryOrganization supervisoryOrganization = this
				.supervisoryOrganizationDelegate.create("SupOrg", "SO", null);
		StaffTitle title = this.staffTitleDelegate.create("Title", (short) 1, 
				true);
		StaffAssignment staffAssignment = this.staffAssignmentDelegate.create(
				staffMember, supervisoryOrganization, null, 
				new DateRange(this.parseDateText("01/01/2017"), null), title, 
				true, null, null, null, null, null);
		Date date = this.parseDateText("01/01/2018");
		String text = "Text";
		Offender offender = this.offenderDelegate.createWithoutIdentity("Smith",
				"Tom", "Harry", null);
		ParoleEndorsementCategory endorsement = this
				.paroleEndorsementCategoryDelegate.create("Name", true);
		StaffRoleCategory staffRole = this.staffRoleCategoryDelegate.create(
				"Name", true);
		ParoleReview paroleReview = this.paroleReviewDelegate.create(
				staffAssignment, date, text, offender, endorsement, staffRole);
		ParoleEndorsementCategory newEndorsement = this
				.paroleEndorsementCategoryDelegate.create("New name", true);
		
		// Action
		paroleReview = this.paroleReviewService.updateParoleReview(paroleReview, 
				staffAssignment, date, text, newEndorsement, staffRole);

		// Assertions
		PropertyValueAsserter.create()
		.addExpectedValue("staffAssignment", staffAssignment)
		.addExpectedValue("date", date)
		.addExpectedValue("text", text)
		.addExpectedValue("offender", offender)
		.addExpectedValue("endorsement", newEndorsement)
		.addExpectedValue("staffRole", staffRole)
		.performAssertions(paroleReview);
	}
	
	/**
	 * Tests the update of the staff role for a parole review.
	 * 
	 * @throws DuplicateEntityFoundException if entity already exists
	 */
	@Test
	public void testUpdateParoleReviewStaffRole() 
			throws DuplicateEntityFoundException {
		// Arrangements
		Person staffMember = this.personDelegate.create("Smith", "John", "Jay", 
				null);
		SupervisoryOrganization supervisoryOrganization = this
				.supervisoryOrganizationDelegate.create("SupOrg", "SO", null);
		StaffTitle title = this.staffTitleDelegate.create("Title", (short) 1, 
				true);
		StaffAssignment staffAssignment = this.staffAssignmentDelegate.create(
				staffMember, supervisoryOrganization, null, 
				new DateRange(this.parseDateText("01/01/2017"), null), title, 
				true, null, null, null, null, null);
		Date date = this.parseDateText("01/01/2018");
		String text = "Text";
		Offender offender = this.offenderDelegate.createWithoutIdentity("Smith",
				"Tom", "Harry", null);
		ParoleEndorsementCategory endorsement = this
				.paroleEndorsementCategoryDelegate.create("Name", true);
		StaffRoleCategory staffRole = this.staffRoleCategoryDelegate.create(
				"Name", true);
		ParoleReview paroleReview = this.paroleReviewDelegate.create(
				staffAssignment, date, text, offender, endorsement, staffRole);
		StaffRoleCategory newStaffRole = this.staffRoleCategoryDelegate.create(
				"New name", true);
		
		// Action
		paroleReview = this.paroleReviewService.updateParoleReview(paroleReview, 
				staffAssignment, date, text, endorsement, newStaffRole);

		// Assertions
		PropertyValueAsserter.create()
		.addExpectedValue("staffAssignment", staffAssignment)
		.addExpectedValue("date", date)
		.addExpectedValue("text", text)
		.addExpectedValue("offender", offender)
		.addExpectedValue("endorsement", endorsement)
		.addExpectedValue("staffRole", newStaffRole)
		.performAssertions(paroleReview);
	}
	
	/**
	 * Tests that {@code DuplicateEntityFoundException} is thrown.
	 * 
	 * @throws DuplicateEntityFoundException if entity already exists
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
		StaffAssignment staffAssignment = this.staffAssignmentDelegate.create(
				staffMember, supervisoryOrganization, null, 
				new DateRange(this.parseDateText("01/01/2017"), null), title, 
				true, null, null, null, null, null);
		Date date = this.parseDateText("01/01/2018");
		String text = "Text";
		Offender offender = this.offenderDelegate.createWithoutIdentity("Smith",
				"Tom", "Harry", null);
		ParoleEndorsementCategory endorsement = this
				.paroleEndorsementCategoryDelegate.create("Name", true);
		StaffRoleCategory staffRole = this.staffRoleCategoryDelegate.create(
				"Name", true);
		this.paroleReviewDelegate.create(staffAssignment, date, text, offender, 
				endorsement, staffRole);
		Person secondStaffMember = this.personDelegate.create("Smith", "Jeff", 
				"Bob", null);
		StaffAssignment secondStaffAssignment = this.staffAssignmentDelegate
				.create(secondStaffMember, supervisoryOrganization, null, 
				new DateRange(this.parseDateText("01/01/2017"), null), title, 
				true, null, null, null, null, null);
		ParoleReview paroleReview = this.paroleReviewDelegate.create(
				secondStaffAssignment, date, text, offender, endorsement, 
				staffRole);
		
		// Action
		paroleReview = this.paroleReviewService.updateParoleReview(paroleReview, 
				staffAssignment, date, text, endorsement, staffRole);
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