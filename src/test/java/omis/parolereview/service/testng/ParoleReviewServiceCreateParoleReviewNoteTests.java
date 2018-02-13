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
import omis.parolereview.domain.ParoleReviewNote;
import omis.parolereview.domain.StaffRoleCategory;
import omis.parolereview.service.ParoleReviewService;
import omis.parolereview.service.delegate.ParoleEndorsementCategoryDelegate;
import omis.parolereview.service.delegate.ParoleReviewDelegate;
import omis.parolereview.service.delegate.ParoleReviewNoteDelegate;
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
 * Tests method to create parole review notes.
 *
 * @author Josh Divine
 * @version 0.0.1 (Feb 12, 2018)
 * @since OMIS 3.0
 */
@Test
public class ParoleReviewServiceCreateParoleReviewNoteTests
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
	
	@Autowired
	@Qualifier("paroleReviewNoteDelegate")
	private ParoleReviewNoteDelegate paroleReviewNoteDelegate;

	/* Services. */

	@Autowired
	@Qualifier("paroleReviewService")
	private ParoleReviewService paroleReviewService;

	/* Test methods. */

	/**
	 * Tests the creation of parole review notes.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	@Test
	public void testCreateParoleReviewNote() 
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
		String description = "Description";

		// Action
		ParoleReviewNote paroleReviewNote = this.paroleReviewService
				.createParoleReviewNote(paroleReview, description, date);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("paroleReview", paroleReview)
			.addExpectedValue("description", description)
			.addExpectedValue("date", date)
			.performAssertions(paroleReviewNote);
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
		ParoleReview paroleReview = this.paroleReviewDelegate.create(
				staffAssignment, date, text, offender, endorsement, staffRole);
		String description = "Description";
		this.paroleReviewNoteDelegate.create(paroleReview, date, description);
		
		// Action
		this.paroleReviewService.createParoleReviewNote(paroleReview, 
				description, date);
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