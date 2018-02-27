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
package omis.paroleboardcondition.service.testng;

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
import omis.condition.domain.Agreement;
import omis.condition.service.delegate.AgreementDelegate;
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
import omis.paroleboardcondition.domain.ParoleBoardAgreement;
import omis.paroleboardcondition.domain.ParoleBoardAgreementCategory;
import omis.paroleboardcondition.service.ParoleBoardConditionService;
import omis.paroleboardcondition.service.delegate.ParoleBoardAgreementCategoryDelegate;
import omis.paroleboardcondition.service.delegate.ParoleBoardAgreementDelegate;
import omis.paroleboarditinerary.domain.AttendeeRoleCategory;
import omis.paroleboarditinerary.domain.BoardAttendee;
import omis.paroleboarditinerary.domain.BoardMeetingSite;
import omis.paroleboarditinerary.domain.ParoleBoardItinerary;
import omis.paroleboarditinerary.service.delegate.BoardAttendeeDelegate;
import omis.paroleboarditinerary.service.delegate.BoardMeetingSiteDelegate;
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
 * Tests method to create parole board agreements from a hearing analysis.
 *
 * @author Josh Divine
 * @version 0.0.1
 * @since OMIS 3.0
 */
@Test
public class ParoleBoardConditionServiceCreateParoleBoardAgreementFromHearingAnalysisTests
	extends AbstractHibernateTransactionalTestNGSpringContextTests {

	/* Delegates. */

	@Autowired
	private ParoleBoardAgreementCategoryDelegate
		paroleBoardAgreementCategoryDelegate;
	
	@Autowired
	private OffenderDelegate offenderDelegate;

	@Autowired
	private AgreementDelegate agreementDelegate;
	
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
	@Qualifier("organizationDelegate")
	private OrganizationDelegate organizationDelegate;
	
	@Autowired
	@Qualifier("countryDelegate")
	private CountryDelegate countryDelegate;
	
	@Autowired
	@Qualifier("cityDelegate")
	private CityDelegate cityDelegate;
	
	@Autowired
	@Qualifier("zipCodeDelegate")
	private ZipCodeDelegate zipCodeDelegate;
	
	@Autowired
	@Qualifier("addressDelegate")
	private AddressDelegate addressDelegate;
	
	@Autowired
	@Qualifier("locationDelegate")
	private LocationDelegate locationDelegate;
	
	@Autowired
	@Qualifier("paroleBoardItineraryDelegate")
	private ParoleBoardItineraryDelegate paroleBoardItineraryDelegate;
	
	@Autowired
	@Qualifier("boardMeetingSiteDelegate")
	private BoardMeetingSiteDelegate boardMeetingSiteDelegate;
	
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
	@Qualifier("paroleBoardMemberDelegate")
	private ParoleBoardMemberDelegate paroleBoardMemberDelegate;

	@Autowired
	@Qualifier("boardAttendeeDelegate")
	private BoardAttendeeDelegate boardAttendeeDelegate;
	
	@Autowired
	@Qualifier("hearingAnalysisDelegate")
	private HearingAnalysisDelegate hearingAnalysisDelegate;
	
	@Autowired
	@Qualifier("hearingAnalysisCategoryDelegate")
	private HearingAnalysisCategoryDelegate hearingAnalysisCategoryDelegate;
	
	@Autowired
	@Qualifier("paroleBoardLocationDelegate")
	private ParoleBoardLocationDelegate paroleBoardLocationDelegate;
	
	@Autowired
	private ParoleBoardAgreementDelegate paroleBoardAgreementDelegate;
	
	/* Services. */

	@Autowired
	@Qualifier("paroleBoardConditionService")
	private ParoleBoardConditionService paroleBoardConditionService;

	/* Test methods. */

	/**
	 * Tests the creation of parole board agreements from a hearing analysis.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 * @throws DateConflictException if date conflicts
	 */
	@Test
	public void testCreateParoleBoardAgreement() 
			throws DuplicateEntityFoundException, DateConflictException {
		// Arrangements
		Offender offender = this.offenderDelegate.createWithoutIdentity("Wayne", 
				"Bruce", "Alen", null);
		Agreement agreement = this.agreementDelegate.create(offender,
				this.parseDateText("05/12/2017"),
				this.parseDateText("05/15/2019"), null, null);
		ParoleBoardAgreementCategory category =
				this.paroleBoardAgreementCategoryDelegate
				.create("Parole Category", false, true);
		Date hearingEligibilityDate = this.parseDateText("01/01/2017");
		Date reviewDate = this.parseDateText("01/01/2017");
		EligibilityStatusCategory statusCategory = 
				EligibilityStatusCategory.APPEARING;
		Date statusDate = this.parseDateText("01/01/2017");
		EligibilityStatusReason statusReason = eligibilityStatusReasonDelegate
				.create("Reason", true);
		String statusComment = "Comment";
		ParoleEligibilityStatus paroleEligibilityStatus = 
				new ParoleEligibilityStatus(statusDate, statusComment, 
						statusCategory, statusReason);
		AppearanceCategory appearanceCategory = this.appearanceCategoryDelegate
				.create("Category", true);
		ParoleEligibility eligibility = this.paroleEligibilityDelegate.create(
				offender, hearingEligibilityDate, reviewDate, 
				paroleEligibilityStatus, appearanceCategory);
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
		Date startDate = this.parseDateText("01/01/2017");
		Date endDate = this.parseDateText("12/31/2017");
		ParoleBoardItinerary boardItinerary = this.paroleBoardItineraryDelegate
				.create(paroleBoardLocation, startDate, endDate);
		Organization secondOrganization = this.organizationDelegate.create(
				"Org2", "O2", null);
		Location secondLocation = this.locationDelegate.create(
				secondOrganization, new DateRange(
						this.parseDateText("01/01/2000"), null), address);
		Date siteDate = this.parseDateText("01/01/2005");
		Integer order = 1;
		BoardMeetingSite meetingSite = this.boardMeetingSiteDelegate.create(
				boardItinerary, secondLocation, siteDate, order);
		Date memberStartDate = this.parseDateText("01/01/2017");
		Date memberEndDate = null;
		Person staffMember = this.personDelegate.create("Smith", "John", "Jay", 
				null);
		SupervisoryOrganization supervisoryOrganization = this
				.supervisoryOrganizationDelegate.create("SupOrg", "SO", null);
		DateRange dateRange = new DateRange(memberStartDate, memberEndDate);
		StaffTitle title = this.staffTitleDelegate.create("Title", (short) 1, 
				true);
		StaffAssignment staffAssignment = this.staffAssignmentDelegate.create(
				staffMember, supervisoryOrganization, null, dateRange, title, 
				true, null, null, null, null, null);
		ParoleBoardMember boardMember = this.paroleBoardMemberDelegate.create(
				staffAssignment, dateRange);
		Long number = 1L;
		AttendeeRoleCategory role = AttendeeRoleCategory.PRIMARY;
		BoardAttendee analyst = this.boardAttendeeDelegate.create(
				boardItinerary, boardMember, number, role);
		HearingAnalysisCategory analysisCategory = this
				.hearingAnalysisCategoryDelegate.create("Category", true);
		HearingAnalysis hearingAnalysis = this.hearingAnalysisDelegate.create(
				eligibility, meetingSite, analysisCategory, analyst);

		// Action
		ParoleBoardAgreement paroleBoardAgreement = this
				.paroleBoardConditionService.createParoleBoardAgreement(
						agreement, hearingAnalysis, category);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("agreement", agreement)
			.addExpectedValue("hearingAnalysis", hearingAnalysis)
			.addExpectedValue("category", category)
			.performAssertions(paroleBoardAgreement);
	}

	/**
	 * Tests {@code DuplicateEntityFoundException} is thrown.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 * @throws DateConflictException if date conflicts
	 */
	@Test(expectedExceptions = {DuplicateEntityFoundException.class})
	public void testDuplicateEntityFoundException() 
			throws DuplicateEntityFoundException, DateConflictException {
		// Arrangements
		Offender offender = this.offenderDelegate.createWithoutIdentity("Wayne", 
				"Bruce", "Alen", null);
		Agreement agreement = this.agreementDelegate.create(offender,
				this.parseDateText("05/12/2017"),
				this.parseDateText("05/15/2019"), null, null);
		ParoleBoardAgreementCategory category =
				this.paroleBoardAgreementCategoryDelegate
				.create("Parole Category", false, true);
		Date hearingEligibilityDate = this.parseDateText("01/01/2017");
		Date reviewDate = this.parseDateText("01/01/2017");
		EligibilityStatusCategory statusCategory = 
				EligibilityStatusCategory.APPEARING;
		Date statusDate = this.parseDateText("01/01/2017");
		EligibilityStatusReason statusReason = eligibilityStatusReasonDelegate
				.create("Reason", true);
		String statusComment = "Comment";
		ParoleEligibilityStatus paroleEligibilityStatus = 
				new ParoleEligibilityStatus(statusDate, statusComment, 
						statusCategory, statusReason);
		AppearanceCategory appearanceCategory = this.appearanceCategoryDelegate
				.create("Category", true);
		ParoleEligibility eligibility = this.paroleEligibilityDelegate.create(
				offender, hearingEligibilityDate, reviewDate, 
				paroleEligibilityStatus, appearanceCategory);
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
		Date startDate = this.parseDateText("01/01/2017");
		Date endDate = this.parseDateText("12/31/2017");
		ParoleBoardItinerary boardItinerary = this.paroleBoardItineraryDelegate
				.create(paroleBoardLocation, startDate, endDate);
		Organization secondOrganization = this.organizationDelegate.create(
				"Org2", "O2", null);
		Location secondLocation = this.locationDelegate.create(
				secondOrganization, new DateRange(
						this.parseDateText("01/01/2000"), null), address);
		Date siteDate = this.parseDateText("01/01/2005");
		Integer order = 1;
		BoardMeetingSite meetingSite = this.boardMeetingSiteDelegate.create(
				boardItinerary, secondLocation, siteDate, order);
		Date memberStartDate = this.parseDateText("01/01/2017");
		Date memberEndDate = null;
		Person staffMember = this.personDelegate.create("Smith", "John", "Jay", 
				null);
		SupervisoryOrganization supervisoryOrganization = this
				.supervisoryOrganizationDelegate.create("SupOrg", "SO", null);
		DateRange dateRange = new DateRange(memberStartDate, memberEndDate);
		StaffTitle title = this.staffTitleDelegate.create("Title", (short) 1, 
				true);
		StaffAssignment staffAssignment = this.staffAssignmentDelegate.create(
				staffMember, supervisoryOrganization, null, dateRange, title, 
				true, null, null, null, null, null);
		ParoleBoardMember boardMember = this.paroleBoardMemberDelegate.create(
				staffAssignment, dateRange);
		Long number = 1L;
		AttendeeRoleCategory role = AttendeeRoleCategory.PRIMARY;
		BoardAttendee analyst = this.boardAttendeeDelegate.create(
				boardItinerary, boardMember, number, role);
		HearingAnalysisCategory analysisCategory = this
				.hearingAnalysisCategoryDelegate.create("Category", true);
		HearingAnalysis hearingAnalysis = this.hearingAnalysisDelegate.create(
				eligibility, meetingSite, analysisCategory, analyst);
		this.paroleBoardAgreementDelegate.create(agreement, null, 
				hearingAnalysis, category);

		// Action
		this.paroleBoardConditionService.createParoleBoardAgreement(agreement, 
				hearingAnalysis, category);
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