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
import omis.paroleboarditinerary.domain.ParoleBoardItinerary;
import omis.paroleboarditinerary.service.delegate.ParoleBoardItineraryDelegate;
import omis.paroleboardlocation.domain.ParoleBoardLocation;
import omis.paroleboardlocation.service.delegate.ParoleBoardLocationDelegate;
import omis.paroleeligibility.domain.ParoleEligibility;
import omis.paroleeligibility.service.delegate.ParoleEligibilityDelegate;
import omis.person.domain.Person;
import omis.person.service.delegate.PersonDelegate;
import omis.region.domain.City;
import omis.region.service.delegate.CityDelegate;
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
	@Qualifier("userAccountDelegate")
	private UserAccountDelegate userAccountDelegate;
	
	@Autowired
	@Qualifier("personDelegate")
	private PersonDelegate personDelegate;
	
	@Autowired
	@Qualifier("taskDelegate")
	private TaskDelegate taskDelegate;
	
	@Autowired
	@Qualifier("hearingAnalysisDelegate")
	private HearingAnalysisDelegate hearingAnalysisDelegate;
	
	@Autowired
	@Qualifier("hearingAnalysisCategoryDelegate")
	private HearingAnalysisCategoryDelegate hearingAnalysisCategoryDelegate;
	
	@Autowired
	@Qualifier("paroleEligibilityDelegate")
	private ParoleEligibilityDelegate paroleEligibilityDelegate;
	
	@Autowired
	@Qualifier("offenderDelegate")
	private OffenderDelegate offenderDelegate;
	
	@Autowired
	@Qualifier("paroleHearingAnalysisTaskSourceDelegate")
	private ParoleHearingAnalysisTaskSourceDelegate 
			paroleHearingAnalysisTaskSourceDelegate;
	
	@Autowired
	@Qualifier("taskTemplateDelegate")
	private TaskTemplateDelegate taskTemplateDelegate;
	
	@Autowired
	@Qualifier("taskTemplateGroupDelegate")
	private TaskTemplateGroupDelegate taskTemplateGroupDelegate;
	
	@Autowired
	@Qualifier("hearingAnalysisTaskAssociationDelegate")
	private HearingAnalysisTaskAssociationDelegate 
			hearingAnalysisTaskAssociationDelegate;
	
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
	@Qualifier("paroleBoardLocationDelegate")
	private ParoleBoardLocationDelegate paroleBoardLocationDelegate;
	
	/* Services. */

	@Autowired
	@Qualifier("hearingAnalysisTaskService")
	private HearingAnalysisTaskService hearingAnalysisTaskService;

	/* Test methods. */

	/**
	 * Tests the creation of hearing analysis task associations.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	@Test
	public void testCreateHearingAnalysisTaskAssociation() 
			throws DuplicateEntityFoundException {
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
		HearingAnalysis hearingAnalysis = this.hearingAnalysisDelegate.create(
				eligibility, boardItinerary, category, null);
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
	 */
	@Test(expectedExceptions = {DuplicateEntityFoundException.class})
	public void testDuplicateEntityFoundException() 
			throws DuplicateEntityFoundException {
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
		HearingAnalysis hearingAnalysis = this.hearingAnalysisDelegate.create(
				eligibility, boardItinerary, category, null);
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