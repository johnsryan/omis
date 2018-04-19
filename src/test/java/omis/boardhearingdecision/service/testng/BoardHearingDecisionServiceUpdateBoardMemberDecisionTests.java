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
package omis.boardhearingdecision.service.testng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.annotations.Test;

import omis.address.domain.Address;
import omis.address.domain.ZipCode;
import omis.address.service.delegate.AddressDelegate;
import omis.address.service.delegate.ZipCodeDelegate;
import omis.boardhearing.domain.BoardHearing;
import omis.boardhearing.domain.BoardHearingParticipant;
import omis.boardhearing.service.delegate.BoardHearingDelegate;
import omis.boardhearing.service.delegate.BoardHearingParticipantDelegate;
import omis.boardhearingdecision.domain.BoardHearingDecision;
import omis.boardhearingdecision.domain.BoardHearingDecisionCategory;
import omis.boardhearingdecision.domain.BoardMemberDecision;
import omis.boardhearingdecision.domain.DecisionCategory;
import omis.boardhearingdecision.domain.HearingDecisionReason;
import omis.boardhearingdecision.service.BoardHearingDecisionService;
import omis.boardhearingdecision.service.delegate.BoardHearingDecisionCategoryDelegate;
import omis.boardhearingdecision.service.delegate.BoardHearingDecisionDelegate;
import omis.boardhearingdecision.service.delegate.BoardMemberDecisionDelegate;
import omis.boardhearingdecision.service.delegate.HearingDecisionReasonDelegate;
import omis.country.domain.Country;
import omis.country.service.delegate.CountryDelegate;
import omis.datatype.DateRange;
import omis.exception.DateConflictException;
import omis.exception.DuplicateEntityFoundException;
import omis.location.domain.Location;
import omis.location.service.delegate.LocationDelegate;
import omis.offender.domain.Offender;
import omis.offender.service.delegate.OffenderDelegate;
import omis.organization.domain.Organization;
import omis.organization.service.delegate.OrganizationDelegate;
import omis.paroleboarditinerary.domain.ParoleBoardItinerary;
import omis.paroleboarditinerary.service.delegate.ParoleBoardItineraryDelegate;
import omis.paroleboardlocation.domain.ParoleBoardLocation;
import omis.paroleboardlocation.service.delegate.ParoleBoardLocationDelegate;
import omis.paroleboardmember.domain.ParoleBoardMember;
import omis.paroleboardmember.service.delegate.ParoleBoardMemberDelegate;
import omis.paroleeligibility.domain.ParoleEligibility;
import omis.paroleeligibility.service.delegate.ParoleEligibilityDelegate;
import omis.person.domain.Person;
import omis.person.service.delegate.PersonDelegate;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.region.service.delegate.CityDelegate;
import omis.region.service.delegate.StateDelegate;
import omis.staff.domain.StaffAssignment;
import omis.staff.domain.StaffTitle;
import omis.staff.service.delegate.StaffAssignmentDelegate;
import omis.staff.service.delegate.StaffTitleDelegate;
import omis.supervision.domain.SupervisoryOrganization;
import omis.supervision.service.delegate.SupervisoryOrganizationDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.util.PropertyValueAsserter;

/**
 * Tests method to update board member decisions.
 *
 * @author Josh Divine
 * @version 0.1.3 (Apr 18, 2018)
 * @since OMIS 3.0
 */
@Test
public class BoardHearingDecisionServiceUpdateBoardMemberDecisionTests
	extends AbstractHibernateTransactionalTestNGSpringContextTests {

	/* Delegates. */

	@Autowired
	@Qualifier("offenderDelegate")
	private OffenderDelegate offenderDelegate;
	
	@Autowired
	@Qualifier("paroleEligibilityDelegate")
	private ParoleEligibilityDelegate paroleEligibilityDelegate;
	
	@Autowired
	@Qualifier("boardHearingDelegate")
	private BoardHearingDelegate boardHearingDelegate;
	
	@Autowired
	@Qualifier("boardHearingDecisionCategoryDelegate")
	private BoardHearingDecisionCategoryDelegate 
			boardHearingDecisionCategoryDelegate;
	
	@Autowired
	@Qualifier("boardHearingDecisionDelegate")
	private BoardHearingDecisionDelegate boardHearingDecisionDelegate;
	
	@Autowired
	@Qualifier("paroleBoardMemberDelegate")
	private ParoleBoardMemberDelegate paroleBoardMemberDelegate;
	
	@Autowired
	@Qualifier("boardHearingParticipantDelegate")
	private BoardHearingParticipantDelegate boardHearingParticipantDelegate;

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
	@Qualifier("hearingDecisionReasonDelegate")
	private HearingDecisionReasonDelegate hearingDecisionReasonDelegate;
	
	@Autowired
	@Qualifier("boardMemberDecisionDelegate")
	private BoardMemberDecisionDelegate boardMemberDecisionDelegate;
	
	@Autowired
	private LocationDelegate locationDelegate;
	
	@Autowired
	@Qualifier("organizationDelegate")
	private OrganizationDelegate organizationDelegate;
	
	@Autowired
	@Qualifier("addressDelegate")
	private AddressDelegate addressDelegate;
	
	@Autowired
	private CountryDelegate countryDelegate;
	
	@Autowired
	private StateDelegate stateDelegate;
	
	@Autowired
	private CityDelegate cityDelegate;
	
	@Autowired
	private ZipCodeDelegate zipCodeDelegate;
	
	@Autowired
	private ParoleBoardItineraryDelegate paroleBoardItineraryDelegate;
	
	@Autowired
	private ParoleBoardLocationDelegate paroleBoardLocationDelegate;
	
	/* Services. */

	@Autowired
	@Qualifier("boardHearingDecisionService")
	private BoardHearingDecisionService boardHearingDecisionService;

	/* Test methods. */

	/**
	 * Tests the update of the board hearing participant for a board member 
	 * decision.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 * @throws DateConflictException if date conflict exists
	 */
	@Test
	public void testUpdateBoardMemberDecisionBoardHearingParticipant() 
			throws DuplicateEntityFoundException, DateConflictException {
		// Arrangements
		Offender offender = this.offenderDelegate.createWithoutIdentity("Smith",
				"John", "Jay", null);
		ParoleEligibility paroleEligibility = this.paroleEligibilityDelegate
				.create(offender, this.parseDateText("01/01/2018"), null, null,
						null);
		Organization organization = this.organizationDelegate.create(
				"Organization", "org", null);
		Country country = this.countryDelegate.create(
				"Country", "USA", true);
		State state = this.stateDelegate.create(
				"State", "ST", country, true, true);
		City city = this.cityDelegate.create(
				"City", true, state, country);
		ZipCode zipCode = this.zipCodeDelegate.create(
				city, "12345", null, true);
		Address address = this.addressDelegate.findOrCreate("123", "321",
				null, null, zipCode);
		Location location = this.locationDelegate.create(organization,
				new DateRange(this.parseDateText("01/01/2001"),
						this.parseDateText("01/01/2020")), address);
		ParoleBoardLocation paroleBoardLocation =
				this.paroleBoardLocationDelegate.create(location, true);
		ParoleBoardItinerary itinerary =
				this.paroleBoardItineraryDelegate.create(paroleBoardLocation,
						true, this.parseDateText("01/01/2015"), 
						this.parseDateText("01/01/2015"));
		BoardHearing hearing = this.boardHearingDelegate.create(itinerary, null, 
				paroleEligibility, null, null, false);
		BoardHearingDecisionCategory category = this
				.boardHearingDecisionCategoryDelegate.create("Category", 
						DecisionCategory.GRANT, true);
		BoardHearingDecision boardDecision = this.boardHearingDecisionDelegate
				.create(hearing, category);
		Date startDate = this.parseDateText("01/01/2017");
		Date endDate = null;
		Person staffMember = this.personDelegate.create("Smith", "John", "Jay", 
				null);
		SupervisoryOrganization supervisoryOrganization = this
				.supervisoryOrganizationDelegate.create("SupOrg", "SO", null);
		DateRange dateRange = new DateRange(startDate, endDate);
		StaffTitle title = this.staffTitleDelegate.create("Title", (short) 1, 
				true);
		StaffAssignment staffAssignment = this.staffAssignmentDelegate.create(
				staffMember, supervisoryOrganization, null, dateRange, title, 
				true, null, null, null, null, null);
		ParoleBoardMember boardMember = this.paroleBoardMemberDelegate.create(
				staffAssignment, 
				new DateRange(this.parseDateText("01/01/2017"), null));
		BoardHearingParticipant boardHearingParticipant = this
				.boardHearingParticipantDelegate.create(hearing, boardMember, 
						1L);
		HearingDecisionReason decisionReason = this
				.hearingDecisionReasonDelegate.create("Reason", 
						DecisionCategory.GRANT, true);
		String comments = "Comments";
		BoardMemberDecision boardMemberDecision = this
				.boardMemberDecisionDelegate.create(boardDecision, 
				boardHearingParticipant, decisionReason, comments);
		Person newStaffMember = this.personDelegate.create("Smith", "Jeff", 
				"Jay", null);
		StaffAssignment newStaffAssignment = this.staffAssignmentDelegate
				.create(newStaffMember, supervisoryOrganization, null, 
						dateRange, title, true, null, null, null, null, null);
		ParoleBoardMember newBoardMember = this.paroleBoardMemberDelegate
				.create(newStaffAssignment, 
				new DateRange(this.parseDateText("01/01/2017"), null));
		BoardHearingParticipant newBoardHearingParticipant = this
				.boardHearingParticipantDelegate.create(hearing, newBoardMember, 
						1L);
		
		// Action
		boardMemberDecision = this.boardHearingDecisionService
				.updateBoardMemberDecision(boardMemberDecision, 
						newBoardHearingParticipant, decisionReason, comments);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("boardDecision", boardDecision)
			.addExpectedValue("boardHearingParticipant", 
					newBoardHearingParticipant)
			.addExpectedValue("decisionReason", decisionReason)
			.addExpectedValue("comments", comments)
			.performAssertions(boardMemberDecision);
	}

	/**
	 * Tests the update of the decision reason for a board member decision.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 * @throws DateConflictException if date conflict exists
	 */
	@Test
	public void testUpdateBoardMemberDecisionDecisionReason() 
			throws DuplicateEntityFoundException, DateConflictException {
		// Arrangements
		Offender offender = this.offenderDelegate.createWithoutIdentity("Smith",
				"John", "Jay", null);
		ParoleEligibility paroleEligibility = this.paroleEligibilityDelegate
				.create(offender, this.parseDateText("01/01/2018"), null, null,
						null);
		Organization organization = this.organizationDelegate.create(
				"Organization", "org", null);
		Country country = this.countryDelegate.create(
				"Country", "USA", true);
		State state = this.stateDelegate.create(
				"State", "ST", country, true, true);
		City city = this.cityDelegate.create(
				"City", true, state, country);
		ZipCode zipCode = this.zipCodeDelegate.create(
				city, "12345", null, true);
		Address address = this.addressDelegate.findOrCreate("123", "321",
				null, null, zipCode);
		Location location = this.locationDelegate.create(organization,
				new DateRange(this.parseDateText("01/01/2001"),
						this.parseDateText("01/01/2020")), address);
		ParoleBoardLocation paroleBoardLocation =
				this.paroleBoardLocationDelegate.create(location, true);
		ParoleBoardItinerary itinerary =
				this.paroleBoardItineraryDelegate.create(paroleBoardLocation,
						true, this.parseDateText("01/01/2015"), 
						this.parseDateText("01/01/2015"));
		BoardHearing hearing = this.boardHearingDelegate.create(itinerary, null, 
				paroleEligibility, null, null, false);
		BoardHearingDecisionCategory category = this
				.boardHearingDecisionCategoryDelegate.create("Category", 
						DecisionCategory.GRANT, true);
		BoardHearingDecision boardDecision = this.boardHearingDecisionDelegate
				.create(hearing, category);
		Date startDate = this.parseDateText("01/01/2017");
		Date endDate = null;
		Person staffMember = this.personDelegate.create("Smith", "John", "Jay", 
				null);
		SupervisoryOrganization supervisoryOrganization = this
				.supervisoryOrganizationDelegate.create("SupOrg", "SO", null);
		DateRange dateRange = new DateRange(startDate, endDate);
		StaffTitle title = this.staffTitleDelegate.create("Title", (short) 1, 
				true);
		StaffAssignment staffAssignment = this.staffAssignmentDelegate.create(
				staffMember, supervisoryOrganization, null, dateRange, title, 
				true, null, null, null, null, null);
		ParoleBoardMember boardMember = this.paroleBoardMemberDelegate.create(
				staffAssignment, 
				new DateRange(this.parseDateText("01/01/2017"), null));
		BoardHearingParticipant boardHearingParticipant = this
				.boardHearingParticipantDelegate.create(hearing, boardMember, 
						1L);
		HearingDecisionReason decisionReason = this
				.hearingDecisionReasonDelegate.create("Reason", 
						DecisionCategory.GRANT, true);
		String comments = "Comments";
		BoardMemberDecision boardMemberDecision = this
				.boardMemberDecisionDelegate.create(boardDecision, 
				boardHearingParticipant, decisionReason, comments);
		HearingDecisionReason newDecisionReason = this
				.hearingDecisionReasonDelegate.create("New Reason", 
						DecisionCategory.GRANT, true);
		
		// Action
		boardMemberDecision = this.boardHearingDecisionService
				.updateBoardMemberDecision(boardMemberDecision, 
						boardHearingParticipant, newDecisionReason, comments);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("boardDecision", boardDecision)
			.addExpectedValue("boardHearingParticipant", 
					boardHearingParticipant)
			.addExpectedValue("decisionReason", newDecisionReason)
			.addExpectedValue("comments", comments)
			.performAssertions(boardMemberDecision);
	}
	
	/**
	 * Tests the update of the comments for a board member decision.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 * @throws DateConflictException if date conflict exists
	 */
	@Test
	public void testUpdateBoardMemberDecisionComments() 
			throws DuplicateEntityFoundException, DateConflictException {
		// Arrangements
		Offender offender = this.offenderDelegate.createWithoutIdentity("Smith",
				"John", "Jay", null);
		ParoleEligibility paroleEligibility = this.paroleEligibilityDelegate
				.create(offender, this.parseDateText("01/01/2018"), null, null,
						null);
		Organization organization = this.organizationDelegate.create(
				"Organization", "org", null);
		Country country = this.countryDelegate.create(
				"Country", "USA", true);
		State state = this.stateDelegate.create(
				"State", "ST", country, true, true);
		City city = this.cityDelegate.create(
				"City", true, state, country);
		ZipCode zipCode = this.zipCodeDelegate.create(
				city, "12345", null, true);
		Address address = this.addressDelegate.findOrCreate("123", "321",
				null, null, zipCode);
		Location location = this.locationDelegate.create(organization,
				new DateRange(this.parseDateText("01/01/2001"),
						this.parseDateText("01/01/2020")), address);
		ParoleBoardLocation paroleBoardLocation =
				this.paroleBoardLocationDelegate.create(location, true);
		ParoleBoardItinerary itinerary =
				this.paroleBoardItineraryDelegate.create(paroleBoardLocation,
						true, this.parseDateText("01/01/2015"), 
						this.parseDateText("01/01/2015"));
		BoardHearing hearing = this.boardHearingDelegate.create(itinerary, null, 
				paroleEligibility, null, null, false);
		BoardHearingDecisionCategory category = this
				.boardHearingDecisionCategoryDelegate.create("Category", 
						DecisionCategory.GRANT, true);
		BoardHearingDecision boardDecision = this.boardHearingDecisionDelegate
				.create(hearing, category);
		Date startDate = this.parseDateText("01/01/2017");
		Date endDate = null;
		Person staffMember = this.personDelegate.create("Smith", "John", "Jay", 
				null);
		SupervisoryOrganization supervisoryOrganization = this
				.supervisoryOrganizationDelegate.create("SupOrg", "SO", null);
		DateRange dateRange = new DateRange(startDate, endDate);
		StaffTitle title = this.staffTitleDelegate.create("Title", (short) 1, 
				true);
		StaffAssignment staffAssignment = this.staffAssignmentDelegate.create(
				staffMember, supervisoryOrganization, null, dateRange, title, 
				true, null, null, null, null, null);
		ParoleBoardMember boardMember = this.paroleBoardMemberDelegate.create(
				staffAssignment, 
				new DateRange(this.parseDateText("01/01/2017"), null));
		BoardHearingParticipant boardHearingParticipant = this
				.boardHearingParticipantDelegate.create(hearing, boardMember, 
						1L);
		HearingDecisionReason decisionReason = this
				.hearingDecisionReasonDelegate.create("Reason", 
						DecisionCategory.GRANT, true);
		String comments = "Comments";
		BoardMemberDecision boardMemberDecision = this
				.boardMemberDecisionDelegate.create(boardDecision, 
				boardHearingParticipant, decisionReason, comments);
		String newComments = "New comments";
		
		// Action
		boardMemberDecision = this.boardHearingDecisionService
				.updateBoardMemberDecision(boardMemberDecision, 
						boardHearingParticipant, decisionReason, newComments);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("boardDecision", boardDecision)
			.addExpectedValue("boardHearingParticipant", 
					boardHearingParticipant)
			.addExpectedValue("decisionReason", decisionReason)
			.addExpectedValue("comments", newComments)
			.performAssertions(boardMemberDecision);
	}
	
	/**
	 * Tests {@code DuplicateEntityFoundException} is thrown.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 * @throws DateConflictException if date conflict exists
	 */
	@Test(expectedExceptions = {DuplicateEntityFoundException.class})
	public void testDuplicateEntityFoundException() 
			throws DuplicateEntityFoundException, DateConflictException {
		// Arrangements
		Offender offender = this.offenderDelegate.createWithoutIdentity("Smith",
				"John", "Jay", null);
		ParoleEligibility paroleEligibility = this.paroleEligibilityDelegate
				.create(offender, this.parseDateText("01/01/2018"), null, null,
						null);
		Organization organization = this.organizationDelegate.create(
				"Organization", "org", null);
		Country country = this.countryDelegate.create(
				"Country", "USA", true);
		State state = this.stateDelegate.create(
				"State", "ST", country, true, true);
		City city = this.cityDelegate.create(
				"City", true, state, country);
		ZipCode zipCode = this.zipCodeDelegate.create(
				city, "12345", null, true);
		Address address = this.addressDelegate.findOrCreate("123", "321",
				null, null, zipCode);
		Location location = this.locationDelegate.create(organization,
				new DateRange(this.parseDateText("01/01/2001"),
						this.parseDateText("01/01/2020")), address);
		ParoleBoardLocation paroleBoardLocation =
				this.paroleBoardLocationDelegate.create(location, true);
		ParoleBoardItinerary itinerary =
				this.paroleBoardItineraryDelegate.create(paroleBoardLocation,
						true, this.parseDateText("01/01/2015"), 
						this.parseDateText("01/01/2015"));
		BoardHearing hearing = this.boardHearingDelegate.create(itinerary, null, 
				paroleEligibility, null, null, false);
		BoardHearingDecisionCategory category = this
				.boardHearingDecisionCategoryDelegate.create("Category", 
						DecisionCategory.GRANT, true);
		BoardHearingDecision boardDecision = this.boardHearingDecisionDelegate
				.create(hearing, category);
		Date startDate = this.parseDateText("01/01/2017");
		Date endDate = null;
		Person staffMember = this.personDelegate.create("Smith", "John", "Jay", 
				null);
		SupervisoryOrganization supervisoryOrganization = this
				.supervisoryOrganizationDelegate.create("SupOrg", "SO", null);
		DateRange dateRange = new DateRange(startDate, endDate);
		StaffTitle title = this.staffTitleDelegate.create("Title", (short) 1, 
				true);
		StaffAssignment staffAssignment = this.staffAssignmentDelegate.create(
				staffMember, supervisoryOrganization, null, dateRange, title, 
				true, null, null, null, null, null);
		ParoleBoardMember boardMember = this.paroleBoardMemberDelegate.create(
				staffAssignment, 
				new DateRange(this.parseDateText("01/01/2017"), null));
		BoardHearingParticipant boardHearingParticipant = this
				.boardHearingParticipantDelegate.create(hearing, boardMember, 
						1L);
		HearingDecisionReason decisionReason = this
				.hearingDecisionReasonDelegate.create("Reason", 
						DecisionCategory.GRANT, true);
		String comments = "Comments";
		this.boardMemberDecisionDelegate.create(boardDecision, 
				boardHearingParticipant, decisionReason, comments);
		HearingDecisionReason secondDecisionReason = this
				.hearingDecisionReasonDelegate.create("Reason 2", 
						DecisionCategory.GRANT, true);
		BoardMemberDecision boardMemberDecision = this
				.boardMemberDecisionDelegate.create(boardDecision, 
				boardHearingParticipant, secondDecisionReason, comments);
		
		// Action
		boardMemberDecision = this.boardHearingDecisionService
				.updateBoardMemberDecision(boardMemberDecision, 
						boardHearingParticipant, decisionReason, comments);
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