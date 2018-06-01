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
package omis.hearinganalysis.service.testng;

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
import omis.country.domain.Country;
import omis.country.service.delegate.CountryDelegate;
import omis.datatype.DateRange;
import omis.exception.DateConflictException;
import omis.exception.DuplicateEntityFoundException;
import omis.hearinganalysis.domain.HearingAnalysis;
import omis.hearinganalysis.domain.HearingAnalysisCategory;
import omis.hearinganalysis.domain.HearingAnalysisTaskAssociation;
import omis.hearinganalysis.domain.ParoleHearingAnalysisTaskSource;
import omis.hearinganalysis.domain.ParoleHearingTaskCategory;
import omis.hearinganalysis.service.HearingAnalysisTaskService;
import omis.hearinganalysis.service.delegate.HearingAnalysisCategoryDelegate;
import omis.hearinganalysis.service.delegate.HearingAnalysisDelegate;
import omis.hearinganalysis.service.delegate.HearingAnalysisTaskAssociationDelegate;
import omis.hearinganalysis.service.delegate.ParoleHearingAnalysisTaskSourceDelegate;
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
import omis.paroleeligibility.domain.ParoleEligibility;
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
import omis.task.domain.Task;
import omis.task.domain.TaskTemplate;
import omis.task.domain.TaskTemplateGroup;
import omis.task.service.delegate.TaskDelegate;
import omis.task.service.delegate.TaskTemplateDelegate;
import omis.task.service.delegate.TaskTemplateGroupDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.user.domain.UserAccount;
import omis.user.service.delegate.UserAccountDelegate;
import omis.util.PropertyValueAsserter;

/**
 * Tests method to create hearing analysis task associations.
 *
 * @author Josh Divine
 * @version 0.1.2 (Apr 18, 2018)
 * @since OMIS 3.0
 */
public class HearingAnalysisTaskServiceCreateHearingAnalysisTaskAssociationTests
	extends AbstractHibernateTransactionalTestNGSpringContextTests {

	/* Delegates. */

	@Autowired
	private UserAccountDelegate userAccountDelegate;
	
	@Autowired
	private PersonDelegate personDelegate;
	
	@Autowired
	private TaskDelegate taskDelegate;
	
	@Autowired
	private HearingAnalysisDelegate hearingAnalysisDelegate;
	
	@Autowired
	private HearingAnalysisCategoryDelegate hearingAnalysisCategoryDelegate;
	
	@Autowired
	private ParoleEligibilityDelegate paroleEligibilityDelegate;
	
	@Autowired
	private OffenderDelegate offenderDelegate;
	
	@Autowired
	private ParoleHearingAnalysisTaskSourceDelegate 
			paroleHearingAnalysisTaskSourceDelegate;
	
	@Autowired
	private TaskTemplateDelegate taskTemplateDelegate;
	
	@Autowired
	private TaskTemplateGroupDelegate taskTemplateGroupDelegate;
	
	@Autowired
	private HearingAnalysisTaskAssociationDelegate 
			hearingAnalysisTaskAssociationDelegate;
	
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
	private ParoleBoardLocationDelegate paroleBoardLocationDelegate;
	
	@Autowired
	private StaffAssignmentDelegate staffAssignmentDelegate;
	
	@Autowired
	private SupervisoryOrganizationDelegate supervisoryOrganizationDelegate;
	
	@Autowired
	private StaffTitleDelegate staffTitleDelegate;
	
	@Autowired
	private ParoleBoardMemberDelegate paroleBoardMemberDelegate;

	@Autowired
	private BoardAttendeeDelegate boardAttendeeDelegate;
	
	/* Services. */

	@Autowired
	@Qualifier("hearingAnalysisTaskService")
	private HearingAnalysisTaskService hearingAnalysisTaskService;

	/* Test methods. */

	/**
	 * Tests the creation of hearing analysis task associations.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 * @throws DateConflictException if date conflicts
	 */
	@Test
	public void testCreateHearingAnalysisTaskAssociation() 
			throws DuplicateEntityFoundException, DateConflictException {
		// Arrangements
		Person user = this.personDelegate.create("Smith", "John", "Jay", null);
		UserAccount sourceAccount = this.userAccountDelegate.create(user, 
				"Username", "password", null, 0, true);
		Task task = this.taskDelegate.create("controllerName", "methodName", 
				"description", sourceAccount, this.parseDateText("01/01/2018"), 
				this.parseDateText("01/02/2018"));
		Offender offender = this.offenderDelegate.createWithoutIdentity("Jay", 
				"Ray", null, null);
		ParoleEligibility eligibility = this.paroleEligibilityDelegate.create(
				offender, this.parseDateText("01/01/2018"), null, null, null);
		HearingAnalysisCategory category = this.hearingAnalysisCategoryDelegate
				.create("Category", true);
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
				.create(paroleBoardLocation, true, startDate, endDate);
		Person staffMember = this.personDelegate.create("Smith", "John", "Jay", 
				null);
		SupervisoryOrganization supervisoryOrganization = this
				.supervisoryOrganizationDelegate.create("SupOrg", "SO", null);
		DateRange dateRange = new DateRange(this.parseDateText("01/01/2017"), 
				null);
		StaffTitle title = this.staffTitleDelegate.create("Title", (short) 1, 
				true);
		StaffAssignment staffAssignment = this.staffAssignmentDelegate.create(
				staffMember, supervisoryOrganization, null, dateRange, title, 
				true, null, null, null, null, null);
		ParoleBoardMember boardMember = this.paroleBoardMemberDelegate.create(
				staffAssignment, dateRange);
		BoardAttendee analyst = this.boardAttendeeDelegate.create(
				boardItinerary, boardMember, 1L, AttendeeRoleCategory.PRIMARY);
		HearingAnalysis hearingAnalysis = this.hearingAnalysisDelegate.create(
				eligibility, boardItinerary, category, analyst, null);
		TaskTemplateGroup group = this.taskTemplateGroupDelegate.create("Name");
		TaskTemplate taskTemplate = this.taskTemplateDelegate.create(group, 
				"Name", "controllerName", "methodName");
		ParoleHearingAnalysisTaskSource taskSource = this
				.paroleHearingAnalysisTaskSourceDelegate.create(taskTemplate, 
						ParoleHearingTaskCategory.ANALYSIS);

		// Action
		HearingAnalysisTaskAssociation hearingAnalysisTaskAssociation = this
				.hearingAnalysisTaskService
				.createHearingAnalysisTaskAssociation(task, hearingAnalysis, 
						taskSource);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("task", task)
			.addExpectedValue("hearingAnalysis", hearingAnalysis)
			.addExpectedValue("taskSource", taskSource)
			.performAssertions(hearingAnalysisTaskAssociation);
	}

	/**
	 * Test that {@code DuplicateEntityFoundException} is thrown.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 * @throws DateConflictException if date conflicts
	 */
	@Test(expectedExceptions = {DuplicateEntityFoundException.class})
	public void testDuplicateEntityFoundException() 
			throws DuplicateEntityFoundException, DateConflictException {
		// Arrangements
		Person user = this.personDelegate.create("Smith", "John", "Jay", null);
		UserAccount sourceAccount = this.userAccountDelegate.create(user, 
				"Username", "password", null, 0, true);
		Task task = this.taskDelegate.create("controllerName", "methodName", 
				"description", sourceAccount, this.parseDateText("01/01/2018"), 
				this.parseDateText("01/02/2018"));
		Offender offender = this.offenderDelegate.createWithoutIdentity("Jay", 
				"Ray", null, null);
		ParoleEligibility eligibility = this.paroleEligibilityDelegate.create(
				offender, this.parseDateText("01/01/2018"), null, null, null);
		HearingAnalysisCategory category = this.hearingAnalysisCategoryDelegate
				.create("Category", true);
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
				.create(paroleBoardLocation, true, startDate, endDate);
		Person staffMember = this.personDelegate.create("Smith", "John", "Jay", 
				null);
		SupervisoryOrganization supervisoryOrganization = this
				.supervisoryOrganizationDelegate.create("SupOrg", "SO", null);
		DateRange dateRange = new DateRange(this.parseDateText("01/01/2017"), 
				null);
		StaffTitle title = this.staffTitleDelegate.create("Title", (short) 1, 
				true);
		StaffAssignment staffAssignment = this.staffAssignmentDelegate.create(
				staffMember, supervisoryOrganization, null, dateRange, title, 
				true, null, null, null, null, null);
		ParoleBoardMember boardMember = this.paroleBoardMemberDelegate.create(
				staffAssignment, dateRange);
		BoardAttendee analyst = this.boardAttendeeDelegate.create(
				boardItinerary, boardMember, 1L, AttendeeRoleCategory.PRIMARY);
		HearingAnalysis hearingAnalysis = this.hearingAnalysisDelegate.create(
				eligibility, boardItinerary, category, analyst, null);
		TaskTemplateGroup group = this.taskTemplateGroupDelegate.create("Name");
		TaskTemplate taskTemplate = this.taskTemplateDelegate.create(group, 
				"Name", "controllerName", "methodName");
		ParoleHearingAnalysisTaskSource taskSource = this
				.paroleHearingAnalysisTaskSourceDelegate.create(taskTemplate, 
						ParoleHearingTaskCategory.ANALYSIS);
		this.hearingAnalysisTaskAssociationDelegate.create(task, 
				hearingAnalysis, taskSource);
		
		// Action
		this.hearingAnalysisTaskService.createHearingAnalysisTaskAssociation(
				task, hearingAnalysis, taskSource);
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