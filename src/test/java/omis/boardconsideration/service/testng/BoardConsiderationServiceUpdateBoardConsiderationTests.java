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
package omis.boardconsideration.service.testng;

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
import omis.boardconsideration.domain.BoardConsideration;
import omis.boardconsideration.domain.BoardConsiderationCategory;
import omis.boardconsideration.service.BoardConsiderationService;
import omis.boardconsideration.service.delegate.BoardConsiderationDelegate;
import omis.country.domain.Country;
import omis.country.service.delegate.CountryDelegate;
import omis.datatype.DateRange;
import omis.exception.DateConflictException;
import omis.exception.DuplicateEntityFoundException;
import omis.hearinganalysis.domain.HearingAnalysis;
import omis.hearinganalysis.domain.HearingAnalysisCategory;
import omis.hearinganalysis.service.delegate.HearingAnalysisCategoryDelegate;
import omis.hearinganalysis.service.delegate.HearingAnalysisDelegate;
import omis.location.domain.Location;
import omis.location.service.delegate.LocationDelegate;
import omis.offender.domain.Offender;
import omis.offender.service.delegate.OffenderDelegate;
import omis.organization.domain.Organization;
import omis.organization.service.delegate.OrganizationDelegate;
import omis.paroleboarditinerary.domain.AttendeeRoleCategory;
import omis.paroleboarditinerary.domain.BoardAttendee;
import omis.paroleboarditinerary.domain.ParoleBoardItinerary;
import omis.paroleboarditinerary.service.delegate.BoardAttendeeDelegate;
import omis.paroleboarditinerary.service.delegate.ParoleBoardItineraryDelegate;
import omis.paroleboardlocation.domain.ParoleBoardLocation;
import omis.paroleboardlocation.service.delegate.ParoleBoardLocationDelegate;
import omis.paroleboardmember.domain.ParoleBoardMember;
import omis.paroleboardmember.service.delegate.ParoleBoardMemberDelegate;
import omis.paroleeligibility.domain.AppearanceCategory;
import omis.paroleeligibility.domain.EligibilityStatusCategory;
import omis.paroleeligibility.domain.EligibilityStatusReason;
import omis.paroleeligibility.domain.ParoleEligibility;
import omis.paroleeligibility.domain.component.ParoleEligibilityStatus;
import omis.paroleeligibility.service.delegate.AppearanceCategoryDelegate;
import omis.paroleeligibility.service.delegate.EligibilityStatusReasonDelegate;
import omis.paroleeligibility.service.delegate.ParoleEligibilityDelegate;
import omis.person.domain.Person;
import omis.person.service.delegate.PersonDelegate;
import omis.region.domain.City;
import omis.region.service.delegate.CityDelegate;
import omis.staff.domain.StaffAssignment;
import omis.staff.domain.StaffTitle;
import omis.staff.service.delegate.StaffAssignmentDelegate;
import omis.staff.service.delegate.StaffTitleDelegate;
import omis.supervision.domain.SupervisoryOrganization;
import omis.supervision.service.delegate.SupervisoryOrganizationDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.util.PropertyValueAsserter;

/**
 * Tests method to update board considerations.
 *
 * @author Josh Divine
 * @version 0.1.0 (May 30, 2018)
 * @since OMIS 3.0
 */
@Test
public class BoardConsiderationServiceUpdateBoardConsiderationTests
	extends AbstractHibernateTransactionalTestNGSpringContextTests {

	/* Delegates. */

	@Autowired
	private BoardConsiderationDelegate boardConsiderationDelegate;

	@Autowired
	private HearingAnalysisDelegate hearingAnalysisDelegate;

	@Autowired
	private OffenderDelegate offenderDelegate;
	
	@Autowired
	private AppearanceCategoryDelegate appearanceCategoryDelegate;
	
	@Autowired
	private EligibilityStatusReasonDelegate eligibilityStatusReasonDelegate;
	
	@Autowired
	private ParoleEligibilityDelegate paroleEligibilityDelegate;
	
	@Autowired
	private OrganizationDelegate organizationDelegate;
	
	@Autowired
	private CountryDelegate countryDelegate;
	
	@Autowired
	private CityDelegate cityDelegate;
	
	@Autowired
	private ZipCodeDelegate zipCodeDelegate;
	
	@Autowired
	private AddressDelegate addressDelegate;
	
	@Autowired
	private LocationDelegate locationDelegate;
	
	@Autowired
	private ParoleBoardItineraryDelegate paroleBoardItineraryDelegate;
	
	@Autowired
	private StaffAssignmentDelegate staffAssignmentDelegate;
	
	@Autowired
	private PersonDelegate personDelegate;
	
	@Autowired
	private SupervisoryOrganizationDelegate supervisoryOrganizationDelegate;
	
	@Autowired
	private StaffTitleDelegate staffTitleDelegate;
	
	@Autowired
	private ParoleBoardMemberDelegate paroleBoardMemberDelegate;

	@Autowired
	private BoardAttendeeDelegate boardAttendeeDelegate;
	
	@Autowired
	private HearingAnalysisCategoryDelegate hearingAnalysisCategoryDelegate;
	
	@Autowired
	private ParoleBoardLocationDelegate paroleBoardLocationDelegate;
	
	/* Services. */

	@Autowired
	@Qualifier("boardConsiderationService")
	private BoardConsiderationService boardConsiderationService;

	/* Test methods. */

	/**
	 * Tests the update of the title for a board consideration.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 * @throws DateConflictException if parole board member date range not 
	 * within staff assignment date range
	 */
	@Test
	public void testUpdateBoardConsiderationTitle() 
			throws DuplicateEntityFoundException, DateConflictException {
		// Arrangements
		Offender offender = this.offenderDelegate.createWithoutIdentity("Smith",
				"John", "Jay", null);
		EligibilityStatusReason statusReason = eligibilityStatusReasonDelegate
				.create("Reason", EligibilityStatusCategory.APPEARING, true);
		ParoleEligibilityStatus paroleEligibilityStatus = 
				new ParoleEligibilityStatus(this.parseDateText("01/01/2017"), 
						"Comment", EligibilityStatusCategory.APPEARING, 
						statusReason);
		AppearanceCategory appearanceCategory = this.appearanceCategoryDelegate
				.create("Category", true);
		ParoleEligibility eligibility = this.paroleEligibilityDelegate.create(
				offender, this.parseDateText("01/01/2017"), 
				this.parseDateText("01/01/2017"), paroleEligibilityStatus, 
				appearanceCategory);
		Organization organization = this.organizationDelegate.create("Org", "O",
				null);
		Country country = this.countryDelegate.create("Country", "C", true);
		City city = this.cityDelegate.create("City", true, null, country);
		ZipCode zipCode = this.zipCodeDelegate.create(city, "12345", null, 
				true);
		Address address = this.addressDelegate.findOrCreate("123 Some St.", 
				null, null, null, zipCode);
		Location location = this.locationDelegate.create(organization, 
				new DateRange(this.parseDateText("01/01/2000"), null), address);
		ParoleBoardLocation paroleBoardLocation = this
				.paroleBoardLocationDelegate.create(location, true);
		ParoleBoardItinerary boardItinerary = this.paroleBoardItineraryDelegate
				.create(paroleBoardLocation, true, 
						this.parseDateText("01/01/2017"), 
						this.parseDateText("12/31/2017"));
		Person staffMember = this.personDelegate.create("Smith", "John", "Jay", 
				null);
		SupervisoryOrganization supervisoryOrganization = this
				.supervisoryOrganizationDelegate.create("SupOrg", "SO", null);
		StaffTitle staffTitle = this.staffTitleDelegate.create("Title", 
				(short) 1, true);
		StaffAssignment staffAssignment = this.staffAssignmentDelegate.create(
				staffMember, supervisoryOrganization, null, 
				new DateRange(this.parseDateText("01/01/2017"), null), 
				staffTitle, true, null, null, null, null, null);
		ParoleBoardMember boardMember = this.paroleBoardMemberDelegate.create(
				staffAssignment, 
				new DateRange(this.parseDateText("01/01/2017"), null));
		BoardAttendee analyst = this.boardAttendeeDelegate.create(
				boardItinerary, boardMember, 1L, AttendeeRoleCategory.PRIMARY);
		HearingAnalysisCategory analysisCategory = this
				.hearingAnalysisCategoryDelegate.create("Category", true);
		HearingAnalysis hearingAnalysis = this.hearingAnalysisDelegate.create(
				eligibility, boardItinerary, analysisCategory, analyst, null);
		String title = "Title";
		String description = "Description";
		BoardConsiderationCategory category = BoardConsiderationCategory.RISK;
		Boolean accepted = true;
		BoardConsideration boardConsideration = this.boardConsiderationDelegate
				.create(hearingAnalysis, title, description, category, accepted);
		String newTitle = "New Title";

		// Action
		boardConsideration = this.boardConsiderationService
				.updateBoardConsideration(boardConsideration, newTitle, 
						description, category, accepted);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("hearingAnalysis", hearingAnalysis)
			.addExpectedValue("title", newTitle)
			.addExpectedValue("description", description)
			.addExpectedValue("category", category)
			.addExpectedValue("accepted", accepted)
			.performAssertions(boardConsideration);
	}
	
	/**
	 * Tests the update of the description for a board consideration.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 * @throws DateConflictException if parole board member date range not 
	 * within staff assignment date range
	 */
	@Test
	public void testUpdateBoardConsiderationDescription() 
			throws DuplicateEntityFoundException, DateConflictException {
		// Arrangements
		Offender offender = this.offenderDelegate.createWithoutIdentity("Smith",
				"John", "Jay", null);
		EligibilityStatusReason statusReason = eligibilityStatusReasonDelegate
				.create("Reason", EligibilityStatusCategory.APPEARING, true);
		ParoleEligibilityStatus paroleEligibilityStatus = 
				new ParoleEligibilityStatus(this.parseDateText("01/01/2017"), 
						"Comment", EligibilityStatusCategory.APPEARING, 
						statusReason);
		AppearanceCategory appearanceCategory = this.appearanceCategoryDelegate
				.create("Category", true);
		ParoleEligibility eligibility = this.paroleEligibilityDelegate.create(
				offender, this.parseDateText("01/01/2017"), 
				this.parseDateText("01/01/2017"), paroleEligibilityStatus, 
				appearanceCategory);
		Organization organization = this.organizationDelegate.create("Org", "O",
				null);
		Country country = this.countryDelegate.create("Country", "C", true);
		City city = this.cityDelegate.create("City", true, null, country);
		ZipCode zipCode = this.zipCodeDelegate.create(city, "12345", null, 
				true);
		Address address = this.addressDelegate.findOrCreate("123 Some St.", 
				null, null, null, zipCode);
		Location location = this.locationDelegate.create(organization, 
				new DateRange(this.parseDateText("01/01/2000"), null), address);
		ParoleBoardLocation paroleBoardLocation = this
				.paroleBoardLocationDelegate.create(location, true);
		ParoleBoardItinerary boardItinerary = this.paroleBoardItineraryDelegate
				.create(paroleBoardLocation, true, 
						this.parseDateText("01/01/2017"), 
						this.parseDateText("12/31/2017"));
		Person staffMember = this.personDelegate.create("Smith", "John", "Jay", 
				null);
		SupervisoryOrganization supervisoryOrganization = this
				.supervisoryOrganizationDelegate.create("SupOrg", "SO", null);
		StaffTitle staffTitle = this.staffTitleDelegate.create("Title", 
				(short) 1, true);
		StaffAssignment staffAssignment = this.staffAssignmentDelegate.create(
				staffMember, supervisoryOrganization, null, 
				new DateRange(this.parseDateText("01/01/2017"), null), 
				staffTitle, true, null, null, null, null, null);
		ParoleBoardMember boardMember = this.paroleBoardMemberDelegate.create(
				staffAssignment, 
				new DateRange(this.parseDateText("01/01/2017"), null));
		BoardAttendee analyst = this.boardAttendeeDelegate.create(
				boardItinerary, boardMember, 1L, AttendeeRoleCategory.PRIMARY);
		HearingAnalysisCategory analysisCategory = this
				.hearingAnalysisCategoryDelegate.create("Category", true);
		HearingAnalysis hearingAnalysis = this.hearingAnalysisDelegate.create(
				eligibility, boardItinerary, analysisCategory, analyst, null);
		String title = "Title";
		String description = "Description";
		BoardConsiderationCategory category = BoardConsiderationCategory.RISK;
		Boolean accepted = true;
		BoardConsideration boardConsideration = this.boardConsiderationDelegate
				.create(hearingAnalysis, title, description, category, accepted);
		String newDescription = "New description";

		// Action
		boardConsideration = this.boardConsiderationService
				.updateBoardConsideration(boardConsideration, title, 
						newDescription, category, accepted);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("hearingAnalysis", hearingAnalysis)
			.addExpectedValue("title", title)
			.addExpectedValue("description", newDescription)
			.addExpectedValue("category", category)
			.addExpectedValue("accepted", accepted)
			.performAssertions(boardConsideration);
	}
	
	/**
	 * Tests the update of the category for a board consideration.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 * @throws DateConflictException if parole board member date range not 
	 * within staff assignment date range
	 */
	@Test
	public void testUpdateBoardConsiderationCategory() 
			throws DuplicateEntityFoundException, DateConflictException {
		// Arrangements
		Offender offender = this.offenderDelegate.createWithoutIdentity("Smith",
				"John", "Jay", null);
		EligibilityStatusReason statusReason = eligibilityStatusReasonDelegate
				.create("Reason", EligibilityStatusCategory.APPEARING, true);
		ParoleEligibilityStatus paroleEligibilityStatus = 
				new ParoleEligibilityStatus(this.parseDateText("01/01/2017"), 
						"Comment", EligibilityStatusCategory.APPEARING, 
						statusReason);
		AppearanceCategory appearanceCategory = this.appearanceCategoryDelegate
				.create("Category", true);
		ParoleEligibility eligibility = this.paroleEligibilityDelegate.create(
				offender, this.parseDateText("01/01/2017"), 
				this.parseDateText("01/01/2017"), paroleEligibilityStatus, 
				appearanceCategory);
		Organization organization = this.organizationDelegate.create("Org", "O",
				null);
		Country country = this.countryDelegate.create("Country", "C", true);
		City city = this.cityDelegate.create("City", true, null, country);
		ZipCode zipCode = this.zipCodeDelegate.create(city, "12345", null, 
				true);
		Address address = this.addressDelegate.findOrCreate("123 Some St.", 
				null, null, null, zipCode);
		Location location = this.locationDelegate.create(organization, 
				new DateRange(this.parseDateText("01/01/2000"), null), address);
		ParoleBoardLocation paroleBoardLocation = this
				.paroleBoardLocationDelegate.create(location, true);
		ParoleBoardItinerary boardItinerary = this.paroleBoardItineraryDelegate
				.create(paroleBoardLocation, true, 
						this.parseDateText("01/01/2017"), 
						this.parseDateText("12/31/2017"));
		Person staffMember = this.personDelegate.create("Smith", "John", "Jay", 
				null);
		SupervisoryOrganization supervisoryOrganization = this
				.supervisoryOrganizationDelegate.create("SupOrg", "SO", null);
		StaffTitle staffTitle = this.staffTitleDelegate.create("Title", 
				(short) 1, true);
		StaffAssignment staffAssignment = this.staffAssignmentDelegate.create(
				staffMember, supervisoryOrganization, null, 
				new DateRange(this.parseDateText("01/01/2017"), null), 
				staffTitle, true, null, null, null, null, null);
		ParoleBoardMember boardMember = this.paroleBoardMemberDelegate.create(
				staffAssignment, 
				new DateRange(this.parseDateText("01/01/2017"), null));
		BoardAttendee analyst = this.boardAttendeeDelegate.create(
				boardItinerary, boardMember, 1L, AttendeeRoleCategory.PRIMARY);
		HearingAnalysisCategory analysisCategory = this
				.hearingAnalysisCategoryDelegate.create("Category", true);
		HearingAnalysis hearingAnalysis = this.hearingAnalysisDelegate.create(
				eligibility, boardItinerary, analysisCategory, analyst, null);
		String title = "Title";
		String description = "Description";
		BoardConsiderationCategory category = BoardConsiderationCategory.RISK;
		Boolean accepted = true;
		BoardConsideration boardConsideration = this.boardConsiderationDelegate
				.create(hearingAnalysis, title, description, category, accepted);
		BoardConsiderationCategory newCategory = 
				BoardConsiderationCategory.STRENGTH;

		// Action
		boardConsideration = this.boardConsiderationService
				.updateBoardConsideration(boardConsideration, title, 
						description, newCategory, accepted);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("hearingAnalysis", hearingAnalysis)
			.addExpectedValue("title", title)
			.addExpectedValue("description", description)
			.addExpectedValue("category", newCategory)
			.addExpectedValue("accepted", accepted)
			.performAssertions(boardConsideration);
	}
	
	/**
	 * Tests the update of whether the plea was accepted for a board 
	 * consideration.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 * @throws DateConflictException if parole board member date range not 
	 * within staff assignment date range
	 */
	@Test
	public void testUpdateBoardConsiderationAccepted() 
			throws DuplicateEntityFoundException, DateConflictException {
		// Arrangements
		Offender offender = this.offenderDelegate.createWithoutIdentity("Smith",
				"John", "Jay", null);
		EligibilityStatusReason statusReason = eligibilityStatusReasonDelegate
				.create("Reason", EligibilityStatusCategory.APPEARING, true);
		ParoleEligibilityStatus paroleEligibilityStatus = 
				new ParoleEligibilityStatus(this.parseDateText("01/01/2017"), 
						"Comment", EligibilityStatusCategory.APPEARING, 
						statusReason);
		AppearanceCategory appearanceCategory = this.appearanceCategoryDelegate
				.create("Category", true);
		ParoleEligibility eligibility = this.paroleEligibilityDelegate.create(
				offender, this.parseDateText("01/01/2017"), 
				this.parseDateText("01/01/2017"), paroleEligibilityStatus, 
				appearanceCategory);
		Organization organization = this.organizationDelegate.create("Org", "O",
				null);
		Country country = this.countryDelegate.create("Country", "C", true);
		City city = this.cityDelegate.create("City", true, null, country);
		ZipCode zipCode = this.zipCodeDelegate.create(city, "12345", null, 
				true);
		Address address = this.addressDelegate.findOrCreate("123 Some St.", 
				null, null, null, zipCode);
		Location location = this.locationDelegate.create(organization, 
				new DateRange(this.parseDateText("01/01/2000"), null), address);
		ParoleBoardLocation paroleBoardLocation = this
				.paroleBoardLocationDelegate.create(location, true);
		ParoleBoardItinerary boardItinerary = this.paroleBoardItineraryDelegate
				.create(paroleBoardLocation, true, 
						this.parseDateText("01/01/2017"), 
						this.parseDateText("12/31/2017"));
		Person staffMember = this.personDelegate.create("Smith", "John", "Jay", 
				null);
		SupervisoryOrganization supervisoryOrganization = this
				.supervisoryOrganizationDelegate.create("SupOrg", "SO", null);
		StaffTitle staffTitle = this.staffTitleDelegate.create("Title", 
				(short) 1, true);
		StaffAssignment staffAssignment = this.staffAssignmentDelegate.create(
				staffMember, supervisoryOrganization, null, 
				new DateRange(this.parseDateText("01/01/2017"), null), 
				staffTitle, true, null, null, null, null, null);
		ParoleBoardMember boardMember = this.paroleBoardMemberDelegate.create(
				staffAssignment, 
				new DateRange(this.parseDateText("01/01/2017"), null));
		BoardAttendee analyst = this.boardAttendeeDelegate.create(
				boardItinerary, boardMember, 1L, AttendeeRoleCategory.PRIMARY);
		HearingAnalysisCategory analysisCategory = this
				.hearingAnalysisCategoryDelegate.create("Category", true);
		HearingAnalysis hearingAnalysis = this.hearingAnalysisDelegate.create(
				eligibility, boardItinerary, analysisCategory, analyst, null);
		String title = "Title";
		String description = "Description";
		BoardConsiderationCategory category = BoardConsiderationCategory.RISK;
		Boolean accepted = true;
		BoardConsideration boardConsideration = this.boardConsiderationDelegate
				.create(hearingAnalysis, title, description, category, accepted);
		Boolean newAccepted = false;

		// Action
		boardConsideration = this.boardConsiderationService
				.updateBoardConsideration(boardConsideration, title, 
						description, category, newAccepted);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("hearingAnalysis", hearingAnalysis)
			.addExpectedValue("title", title)
			.addExpectedValue("description", description)
			.addExpectedValue("category", category)
			.addExpectedValue("accepted", newAccepted)
			.performAssertions(boardConsideration);
	}

	/**
	 * Tests {@code DuplicateEntityFoundException} is thrown.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 * @throws DateConflictException if parole board member date range not 
	 * within staff assignment date range
	 */
	@Test(expectedExceptions = {DuplicateEntityFoundException.class})
	public void testDuplicateEntityFoundException() 
			throws DuplicateEntityFoundException, DateConflictException {
		// Arrangements
		Offender offender = this.offenderDelegate.createWithoutIdentity("Smith",
				"John", "Jay", null);
		EligibilityStatusReason statusReason = eligibilityStatusReasonDelegate
				.create("Reason", EligibilityStatusCategory.APPEARING, true);
		ParoleEligibilityStatus paroleEligibilityStatus = 
				new ParoleEligibilityStatus(this.parseDateText("01/01/2017"), 
						"Comment", EligibilityStatusCategory.APPEARING, 
						statusReason);
		AppearanceCategory appearanceCategory = this.appearanceCategoryDelegate
				.create("Category", true);
		ParoleEligibility eligibility = this.paroleEligibilityDelegate.create(
				offender, this.parseDateText("01/01/2017"), 
				this.parseDateText("01/01/2017"), paroleEligibilityStatus, 
				appearanceCategory);
		Organization organization = this.organizationDelegate.create("Org", "O",
				null);
		Country country = this.countryDelegate.create("Country", "C", true);
		City city = this.cityDelegate.create("City", true, null, country);
		ZipCode zipCode = this.zipCodeDelegate.create(city, "12345", null, 
				true);
		Address address = this.addressDelegate.findOrCreate("123 Some St.", 
				null, null, null, zipCode);
		Location location = this.locationDelegate.create(organization, 
				new DateRange(this.parseDateText("01/01/2000"), null), address);
		ParoleBoardLocation paroleBoardLocation = this
				.paroleBoardLocationDelegate.create(location, true);
		ParoleBoardItinerary boardItinerary = this.paroleBoardItineraryDelegate
				.create(paroleBoardLocation, true, 
						this.parseDateText("01/01/2017"), 
						this.parseDateText("12/31/2017"));
		Person staffMember = this.personDelegate.create("Smith", "John", "Jay", 
				null);
		SupervisoryOrganization supervisoryOrganization = this
				.supervisoryOrganizationDelegate.create("SupOrg", "SO", null);
		StaffTitle staffTitle = this.staffTitleDelegate.create("Title", 
				(short) 1, true);
		StaffAssignment staffAssignment = this.staffAssignmentDelegate.create(
				staffMember, supervisoryOrganization, null, 
				new DateRange(this.parseDateText("01/01/2017"), null), 
				staffTitle, true, null, null, null, null, null);
		ParoleBoardMember boardMember = this.paroleBoardMemberDelegate.create(
				staffAssignment, 
				new DateRange(this.parseDateText("01/01/2017"), null));
		BoardAttendee analyst = this.boardAttendeeDelegate.create(
				boardItinerary, boardMember, 1L, AttendeeRoleCategory.PRIMARY);
		HearingAnalysisCategory analysisCategory = this
				.hearingAnalysisCategoryDelegate.create("Category", true);
		HearingAnalysis hearingAnalysis = this.hearingAnalysisDelegate.create(
				eligibility, boardItinerary, analysisCategory, analyst, null);
		String title = "Title";
		String description = "Description";
		BoardConsiderationCategory category = BoardConsiderationCategory.RISK;
		Boolean accepted = true;
		this.boardConsiderationDelegate.create(hearingAnalysis, title, 
				description, category, accepted);
		String secondTitle = "Second Title";
		BoardConsideration boardConsideration = this.boardConsiderationDelegate
				.create(hearingAnalysis, secondTitle, description, category, 
						accepted);
		
		// Action
		boardConsideration = this.boardConsiderationService
				.updateBoardConsideration(boardConsideration, title, 
						description, category, accepted);
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