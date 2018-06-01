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
package omis.presentenceinvestigation.service.testng;

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
import omis.court.domain.Court;
import omis.court.domain.CourtCategory;
import omis.court.service.delegate.CourtDelegate;
import omis.datatype.DateRange;
import omis.docket.domain.Docket;
import omis.docket.exception.DocketExistsException;
import omis.docket.service.delegate.DocketDelegate;
import omis.exception.DuplicateEntityFoundException;
import omis.location.domain.Location;
import omis.location.service.delegate.LocationDelegate;
import omis.organization.domain.Organization;
import omis.organization.service.delegate.OrganizationDelegate;
import omis.person.domain.Person;
import omis.person.service.delegate.PersonDelegate;
import omis.presentenceinvestigation.domain.PresentenceInvestigationCategory;
import omis.presentenceinvestigation.domain.PresentenceInvestigationRequest;
import omis.presentenceinvestigation.domain.PresentenceInvestigationTaskAssociation;
import omis.presentenceinvestigation.domain.PresentenceInvestigationTaskAssociationCategory;
import omis.presentenceinvestigation.domain.PresentenceInvestigationTaskAssociationUsageCategory;
import omis.presentenceinvestigation.domain.PresentenceInvestigationTaskSource;
import omis.presentenceinvestigation.service.PresentenceInvestigationTaskService;
import omis.presentenceinvestigation.service.delegate.PresentenceInvestigationCategoryDelegate;
import omis.presentenceinvestigation.service.delegate.PresentenceInvestigationRequestDelegate;
import omis.presentenceinvestigation.service.delegate.PresentenceInvestigationTaskSourceDelegate;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.region.service.delegate.CityDelegate;
import omis.region.service.delegate.StateDelegate;
import omis.task.domain.Task;
import omis.task.domain.TaskAssignment;
import omis.task.domain.TaskParameterValue;
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
 * PresentenceInvestigationTaskServiceCreateTests.java
 * 
 * @author Annie Wahl 
 * @author Josh Divine
 * @version 0.1.2 (May 9, 2018)
 * @since OMIS 3.0
 *
 */
public class PresentenceInvestigationTaskServiceCreateTests
		extends AbstractHibernateTransactionalTestNGSpringContextTests {
	
	/* Service being tested */
	
	@Autowired
	@Qualifier("presentenceInvestigationTaskService")
	private PresentenceInvestigationTaskService
		presentenceInvestigationTaskService;
	
	/* Helper delegates */
	
	@Autowired
	private PresentenceInvestigationRequestDelegate
		presentenceInvestigationRequestDelegate;
	
	@Autowired
	private PresentenceInvestigationCategoryDelegate
		presentenceInvestigationCategoryDelegate;
	
	@Autowired
	private PersonDelegate personDelegate;
	
	@Autowired
	private UserAccountDelegate userAccountDelegate;
	
	@Autowired
	private CourtDelegate courtDelegate;
	
	@Autowired
	private DocketDelegate docketDelegate;
	
	@Autowired
	private LocationDelegate locationDelegate;
	
	@Autowired
	private OrganizationDelegate organizationDelegate;
	
	@Autowired
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
	private TaskDelegate taskDelegate;
	
	@Autowired
	private PresentenceInvestigationTaskSourceDelegate
		presentenceInvestigationTaskSourceDelegate;
	
	@Autowired
	private TaskTemplateDelegate taskTemplateDelegate;
	
	@Autowired
	private TaskTemplateGroupDelegate taskTemplateGroupDelegate;
	
	@Test
	public void testPresentenceInvestigationTaskAssociationCreate()
			throws DuplicateEntityFoundException, DocketExistsException {
		
		final Person person = this.personDelegate.create(
				"Wayne", "Bruce", "Alen", null);
		final Person user = this.personDelegate.create(
				"Grayson", "Richard", "J", null);
		final UserAccount userAccount = this.userAccountDelegate.create(
				user, "ROBIN34", "password1", this.parseDateText("12/12/2299"),
				0, true);
		final Organization organization = this.organizationDelegate.create(
				"Batcave", "TBC", null);
		final Country country = this.countryDelegate.create(
				"Country", "USA", true);
		final State state = this.stateDelegate.create(
				"State", "ST", country, true, true);
		final City city = this.cityDelegate.create(
				"City", true, state, country);
		final ZipCode zipCode = this.zipCodeDelegate.create(
				city, "12345", null, true);
		final Address address = this.addressDelegate.findOrCreate("123value", null,
				null, null, zipCode);
		final Location location = this.locationDelegate.create(organization,
				new DateRange(this.parseDateText("01/01/2001"),
						this.parseDateText("01/01/2020")), address);
		final Court court = this.courtDelegate.create("Court Of Justice!",
				CourtCategory.CITY, location);
		final Docket docket = this.docketDelegate.create(person, court,
				"Docketty Doo");
		final PresentenceInvestigationCategory category =
				this.presentenceInvestigationCategoryDelegate
				.create("PSI Category", true);
		final PresentenceInvestigationRequest presentenceInvestigationRequest =
				this.presentenceInvestigationRequestDelegate.create(
						userAccount, this.parseDateText("01/01/2016"),
						this.parseDateText("12/31/2017"),
						docket, this.parseDateText("03/25/2015"), category, 
						this.parseDateText("04/01/2017"));
		final TaskTemplateGroup group = this.taskTemplateGroupDelegate.create(
				"TaskTemplateGroup");
		final TaskTemplate taskTemplate = this.taskTemplateDelegate.create(
				group, "Taskeroo", "taskyTaskController", "edit");
		final PresentenceInvestigationTaskSource taskSource =
				this.presentenceInvestigationTaskSourceDelegate.create(
					taskTemplate,
					PresentenceInvestigationTaskAssociationUsageCategory.SUMMARY,
					PresentenceInvestigationTaskAssociationCategory.LEGAL);
		final Task task = this.taskDelegate.create(
				taskTemplate.getControllerName(), taskTemplate.getMethodName(),
				taskTemplate.getName(), userAccount,
				this.parseDateText("01/01/2017"), null);
		
		final PresentenceInvestigationTaskAssociation taskAssociation =
				this.presentenceInvestigationTaskService
				.createPresentenceInvestigationTaskAssociation(
						task, presentenceInvestigationRequest, taskSource);
				
		PropertyValueAsserter.create()
		.addExpectedValue("task", task)
		.addExpectedValue("presentenceInvestigationRequest", 
				presentenceInvestigationRequest)
		.addExpectedValue("taskSource", taskSource)
		.performAssertions(taskAssociation);
	}
	
	@Test
	public void testTaskCreate()
			throws DuplicateEntityFoundException {
		final Person user = this.personDelegate.create(
				"Grayson", "Richard", "J", null);
		final UserAccount sourceAccount = this.userAccountDelegate.create(
				user, "ROBIN34", "password1", this.parseDateText("12/12/2299"),
				0, true);
		final TaskTemplateGroup group = this.taskTemplateGroupDelegate.create(
				"TaskTemplateGroup");
		final TaskTemplate taskTemplate = this.taskTemplateDelegate.create(
				group, "Taskeroo", "taskyTaskController", "edit");
		final Date originationDate = this.parseDateText("01/01/2017");
		final Date completionDate = this.parseDateText("11/03/2018");
		
		final Task task = this.presentenceInvestigationTaskService.createTask(
				taskTemplate.getControllerName(), taskTemplate.getMethodName(),
				taskTemplate.getName(), sourceAccount, originationDate, 
				completionDate);
		
		PropertyValueAsserter.create()
			.addExpectedValue("controllerName", taskTemplate.getControllerName())
			.addExpectedValue("methodName", taskTemplate.getMethodName())
			.addExpectedValue("description", taskTemplate.getName())
			.addExpectedValue("sourceAccount", sourceAccount)
			.addExpectedValue("originationDate", originationDate)
			.addExpectedValue("completionDate", completionDate)
			.performAssertions(task);
	}
	
	@Test
	public void testTaskAssignmentCreate()
			throws DuplicateEntityFoundException {
		final Person user = this.personDelegate.create(
				"Grayson", "Richard", "J", null);
		final UserAccount userAccount = this.userAccountDelegate.create(
				user, "ROBIN34", "password1", this.parseDateText("12/12/2299"),
				0, true);
		final TaskTemplateGroup group = this.taskTemplateGroupDelegate.create(
				"TaskTemplateGroup");
		final TaskTemplate taskTemplate = this.taskTemplateDelegate.create(
				group, "Taskeroo", "taskyTaskController", "edit");
		final Task task = this.taskDelegate.create(
				taskTemplate.getControllerName(), taskTemplate.getMethodName(),
				taskTemplate.getName(), userAccount,
				this.parseDateText("01/01/2017"), null);
		final Date assignedDate = this.parseDateText("07/07/2017");
		final Person assigneeUser = this.personDelegate.create(
				"Pennyworth", "Alfred", "J", null);
		final UserAccount assigneeAccount = this.userAccountDelegate.create(
				assigneeUser, "TheButler", "$e(ureP@$$\\/\\/0r|)",
				this.parseDateText("12/12/2299"), 0, true);
		
		final TaskAssignment taskAssignment =
				this.presentenceInvestigationTaskService.createTaskAssignment(
						task, assignedDate, assigneeAccount);
		
		PropertyValueAsserter.create()
			.addExpectedValue("task", task)
			.addExpectedValue("assignedDate", assignedDate)
			.addExpectedValue("assigneeAccount", assigneeAccount)
			.performAssertions(taskAssignment);
	}
	
	@Test
	public void testTaskParameterValueCreate()
			throws DuplicateEntityFoundException {
		final Person user = this.personDelegate.create(
				"Grayson", "Richard", "J", null);
		final UserAccount sourceAccount = this.userAccountDelegate.create(
				user, "ROBIN34", "password1", this.parseDateText("12/12/2299"),
				0, true);
		final TaskTemplateGroup group = this.taskTemplateGroupDelegate.create(
				"TaskTemplateGroup");
		final TaskTemplate taskTemplate = this.taskTemplateDelegate.create(
				group, "Taskeroo", "taskyTaskController", "edit");
		final Date originationDate = this.parseDateText("01/01/2017");
		final Date completionDate = this.parseDateText("11/03/2018");
		final Task task = this.taskDelegate.create(
				taskTemplate.getControllerName(), taskTemplate.getMethodName(),
				taskTemplate.getName(), sourceAccount, originationDate, completionDate);
		final String typeName =
				"omis.presentenceinvestigation.domain.PresentenceInvestigationRequest";
		final String instanceValue = "63";
		final Short order = 1;
		
		final TaskParameterValue taskParameterValue =
				this.presentenceInvestigationTaskService
				.createTaskParameterValue(
						task, typeName, instanceValue, order);
		
		PropertyValueAsserter.create()
			.addExpectedValue("task", task)
			.addExpectedValue("typeName", typeName)
			.addExpectedValue("instanceValue", instanceValue)
			.addExpectedValue("order", order)
			.performAssertions(taskParameterValue);
	}
	
	private Date parseDateText(final String dateText) {
		try {
			return new SimpleDateFormat("MM/dd/yyyy").parse(dateText);
		} catch (ParseException e) {
			throw new RuntimeException("Error parsing date", e);
		}
	}
}
