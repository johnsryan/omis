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
package omis.caseload.service.testng;

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
import omis.caseload.domain.OfficerCaseAssignment;
import omis.caseload.domain.SupervisionLevelCategory;
import omis.caseload.service.OfficerCaseAssignmentService;
import omis.caseload.service.delegate.OfficerCaseAssignmentDelegate;
import omis.caseload.service.delegate.SupervisionLevelCategoryDelegate;
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
import omis.person.domain.Person;
import omis.person.service.delegate.PersonDelegate;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.region.service.delegate.CityDelegate;
import omis.region.service.delegate.StateDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.user.domain.UserAccount;
import omis.user.service.delegate.UserAccountDelegate;
import omis.util.PropertyValueAsserter;

/**
 * Tests method to update officer case assignments.
 *
 * @author Josh Divine
 * @version 0.1.0 (Jun 19, 2018)
 * @since OMIS 3.0
 */
@Test
public class OfficerCaseAssignmentServiceUpdateOfficerCaseAssignmentTests
	extends AbstractHibernateTransactionalTestNGSpringContextTests {

	/* Delegates. */

	@Autowired
	private OfficerCaseAssignmentDelegate officerCaseAssignmentDelegate;

	@Autowired
	private OffenderDelegate offenderDelegate;
	
	@Autowired
	private UserAccountDelegate userAccountDelegate;

	@Autowired
	private LocationDelegate locationDelegate;

	@Autowired
	private SupervisionLevelCategoryDelegate supervisionLevelCategoryDelegate;

	@Autowired
	private PersonDelegate personDelegate;
	
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
	
	/* Services. */

	@Autowired
	@Qualifier("officerCaseAssignmentService")
	private OfficerCaseAssignmentService officerCaseAssignmentService;

	/* Test methods. */

	/**
	 * Tests the update of the officer for a officer case assignment.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 * @throws DateConflictException if date conflict exists
	 */
	@Test
	public void testUpdateOfficerCaseAssignmentOfficer() 
			throws DuplicateEntityFoundException, DateConflictException {
		// Arrangements
		Offender offender = this.offenderDelegate.createWithoutIdentity("Smith", 
				"John", null, null);
		Person user = this.personDelegate.create("Smith", "Ted", null, null);
		UserAccount officer = this.userAccountDelegate.create(user, "Username", 
				"", null, 0, true);
		Date startDate = this.parseDateText("01/01/2018");
		Date endDate = this.parseDateText("12/31/2018");
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
		Location supervisionOffice = this.locationDelegate.create(organization, 
				new DateRange(this.parseDateText("01/01/2000"), null), address);
		SupervisionLevelCategory supervisionLevel = this
				.supervisionLevelCategoryDelegate.create("Description", true);
		OfficerCaseAssignment officerCaseAssignment = this
				.officerCaseAssignmentDelegate.create(offender, officer, 
						startDate, endDate, supervisionOffice, supervisionLevel);
		Person newUser = this.personDelegate.create("Smith", "Tom", null, null);
		UserAccount newOfficer = this.userAccountDelegate.create(newUser, 
				"Username2", "", null, 0, true);
		
		// Action
		officerCaseAssignment = this.officerCaseAssignmentService
				.updateOfficerCaseAssignment(officerCaseAssignment, newOfficer, 
						startDate, endDate, supervisionOffice, supervisionLevel);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("offender", offender)
			.addExpectedValue("officer", newOfficer)
			.addExpectedValue("dateRange.startDate", startDate)
			.addExpectedValue("dateRange.endDate", endDate)
			.addExpectedValue("supervisionOffice", supervisionOffice)
			.addExpectedValue("supervisionLevel", supervisionLevel)
			.performAssertions(officerCaseAssignment);
	}
	
	/**
	 * Tests the update of the start date for a officer case assignment.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 * @throws DateConflictException if date conflict exists
	 */
	@Test
	public void testUpdateOfficerCaseAssignmentStartDate() 
			throws DuplicateEntityFoundException, DateConflictException {
		// Arrangements
		Offender offender = this.offenderDelegate.createWithoutIdentity("Smith", 
				"John", null, null);
		Person user = this.personDelegate.create("Smith", "Ted", null, null);
		UserAccount officer = this.userAccountDelegate.create(user, "Username", 
				"", null, 0, true);
		Date startDate = this.parseDateText("01/01/2018");
		Date endDate = this.parseDateText("12/31/2018");
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
		Location supervisionOffice = this.locationDelegate.create(organization, 
				new DateRange(this.parseDateText("01/01/2000"), null), address);
		SupervisionLevelCategory supervisionLevel = this
				.supervisionLevelCategoryDelegate.create("Description", true);
		OfficerCaseAssignment officerCaseAssignment = this
				.officerCaseAssignmentDelegate.create(offender, officer, 
						startDate, endDate, supervisionOffice, supervisionLevel);
		Date newStartDate = this.parseDateText("02/01/2018");
		
		// Action
		officerCaseAssignment = this.officerCaseAssignmentService
				.updateOfficerCaseAssignment(officerCaseAssignment, officer, 
						newStartDate, endDate, supervisionOffice, 
						supervisionLevel);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("offender", offender)
			.addExpectedValue("officer", officer)
			.addExpectedValue("dateRange.startDate", newStartDate)
			.addExpectedValue("dateRange.endDate", endDate)
			.addExpectedValue("supervisionOffice", supervisionOffice)
			.addExpectedValue("supervisionLevel", supervisionLevel)
			.performAssertions(officerCaseAssignment);
	}
	
	/**
	 * Tests the update of the end date for a officer case assignment.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 * @throws DateConflictException if date conflict exists
	 */
	@Test
	public void testUpdateOfficerCaseAssignmentEndDate() 
			throws DuplicateEntityFoundException, DateConflictException {
		// Arrangements
		Offender offender = this.offenderDelegate.createWithoutIdentity("Smith", 
				"John", null, null);
		Person user = this.personDelegate.create("Smith", "Ted", null, null);
		UserAccount officer = this.userAccountDelegate.create(user, "Username", 
				"", null, 0, true);
		Date startDate = this.parseDateText("01/01/2018");
		Date endDate = this.parseDateText("12/31/2018");
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
		Location supervisionOffice = this.locationDelegate.create(organization, 
				new DateRange(this.parseDateText("01/01/2000"), null), address);
		SupervisionLevelCategory supervisionLevel = this
				.supervisionLevelCategoryDelegate.create("Description", true);
		OfficerCaseAssignment officerCaseAssignment = this
				.officerCaseAssignmentDelegate.create(offender, officer, 
						startDate, endDate, supervisionOffice, supervisionLevel);
		Date newEndDate = null;
		
		// Action
		officerCaseAssignment = this.officerCaseAssignmentService
				.updateOfficerCaseAssignment(officerCaseAssignment, officer, 
						startDate, newEndDate, supervisionOffice, 
						supervisionLevel);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("offender", offender)
			.addExpectedValue("officer", officer)
			.addExpectedValue("dateRange.startDate", startDate)
			.addExpectedValue("dateRange.endDate", newEndDate)
			.addExpectedValue("supervisionOffice", supervisionOffice)
			.addExpectedValue("supervisionLevel", supervisionLevel)
			.performAssertions(officerCaseAssignment);
	}
	
	/**
	 * Tests the update of the supervision office for a officer case assignment.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 * @throws DateConflictException if date conflict exists
	 */
	@Test
	public void testUpdateOfficerCaseAssignmentSupervisionOffice() 
			throws DuplicateEntityFoundException, DateConflictException {
		// Arrangements
		Offender offender = this.offenderDelegate.createWithoutIdentity("Smith", 
				"John", null, null);
		Person user = this.personDelegate.create("Smith", "Ted", null, null);
		UserAccount officer = this.userAccountDelegate.create(user, "Username", 
				"", null, 0, true);
		Date startDate = this.parseDateText("01/01/2018");
		Date endDate = this.parseDateText("12/31/2018");
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
		Location supervisionOffice = this.locationDelegate.create(organization, 
				new DateRange(this.parseDateText("01/01/2000"), null), address);
		SupervisionLevelCategory supervisionLevel = this
				.supervisionLevelCategoryDelegate.create("Description", true);
		OfficerCaseAssignment officerCaseAssignment = this
				.officerCaseAssignmentDelegate.create(offender, officer, 
						startDate, endDate, supervisionOffice, supervisionLevel);
		Organization newOrganization = this.organizationDelegate.create(
				"Organization2", "org2", null);
		Location newSupervisionOffice = this.locationDelegate.create(
				newOrganization, 
				new DateRange(this.parseDateText("01/01/2000"), null), address);
		
		// Action
		officerCaseAssignment = this.officerCaseAssignmentService
				.updateOfficerCaseAssignment(officerCaseAssignment, officer, 
						startDate, endDate, newSupervisionOffice, 
						supervisionLevel);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("offender", offender)
			.addExpectedValue("officer", officer)
			.addExpectedValue("dateRange.startDate", startDate)
			.addExpectedValue("dateRange.endDate", endDate)
			.addExpectedValue("supervisionOffice", newSupervisionOffice)
			.addExpectedValue("supervisionLevel", supervisionLevel)
			.performAssertions(officerCaseAssignment);
	}
	
	/**
	 * Tests the update of the supervision level for a officer case assignment.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 * @throws DateConflictException if date conflict exists
	 */
	@Test
	public void testUpdateOfficerCaseAssignmentSupervisionLevel() 
			throws DuplicateEntityFoundException, DateConflictException {
		// Arrangements
		Offender offender = this.offenderDelegate.createWithoutIdentity("Smith", 
				"John", null, null);
		Person user = this.personDelegate.create("Smith", "Ted", null, null);
		UserAccount officer = this.userAccountDelegate.create(user, "Username", 
				"", null, 0, true);
		Date startDate = this.parseDateText("01/01/2018");
		Date endDate = this.parseDateText("12/31/2018");
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
		Location supervisionOffice = this.locationDelegate.create(organization, 
				new DateRange(this.parseDateText("01/01/2000"), null), address);
		SupervisionLevelCategory supervisionLevel = this
				.supervisionLevelCategoryDelegate.create("Description", true);
		OfficerCaseAssignment officerCaseAssignment = this
				.officerCaseAssignmentDelegate.create(offender, officer, 
						startDate, endDate, supervisionOffice, supervisionLevel);
		SupervisionLevelCategory newSupervisionLevel = this
				.supervisionLevelCategoryDelegate.create("Description2", true);
		
		// Action
		officerCaseAssignment = this.officerCaseAssignmentService
				.updateOfficerCaseAssignment(officerCaseAssignment, officer, 
						startDate, endDate, supervisionOffice, 
						newSupervisionLevel);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("offender", offender)
			.addExpectedValue("officer", officer)
			.addExpectedValue("dateRange.startDate", startDate)
			.addExpectedValue("dateRange.endDate", endDate)
			.addExpectedValue("supervisionOffice", supervisionOffice)
			.addExpectedValue("supervisionLevel", newSupervisionLevel)
			.performAssertions(officerCaseAssignment);
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
				"John", null, null);
		Person user = this.personDelegate.create("Smith", "Ted", null, null);
		UserAccount officer = this.userAccountDelegate.create(user, "Username", 
				"", null, 0, true);
		Date startDate = this.parseDateText("01/01/2018");
		Date endDate = this.parseDateText("12/31/2018");
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
		Location supervisionOffice = this.locationDelegate.create(organization, 
				new DateRange(this.parseDateText("01/01/2000"), null), address);
		SupervisionLevelCategory supervisionLevel = this
				.supervisionLevelCategoryDelegate.create("Description", true);
		this.officerCaseAssignmentDelegate.create(offender, officer, startDate, 
				endDate, supervisionOffice, supervisionLevel);
		OfficerCaseAssignment officerCaseAssignment = this
				.officerCaseAssignmentDelegate.create(offender, officer, 
						this.parseDateText("01/01/2019"), null, 
						supervisionOffice, supervisionLevel);

		// Action
		officerCaseAssignment = this.officerCaseAssignmentService
				.updateOfficerCaseAssignment(officerCaseAssignment, officer, 
						startDate, endDate, supervisionOffice, supervisionLevel);
	}

	/**
	 * Tests {@code DateConflictException} is thrown.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 * @throws DateConflictException if date conflict exists
	 */
	@Test(expectedExceptions = {DateConflictException.class})
	public void testDateConflictException() 
			throws DuplicateEntityFoundException, DateConflictException {
		// Arrangements
		Offender offender = this.offenderDelegate.createWithoutIdentity("Smith", 
				"John", null, null);
		Person user = this.personDelegate.create("Smith", "Ted", null, null);
		UserAccount officer = this.userAccountDelegate.create(user, "Username", 
				"", null, 0, true);
		Date startDate = this.parseDateText("01/01/2018");
		Date endDate = this.parseDateText("12/31/2018");
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
		Location supervisionOffice = this.locationDelegate.create(organization, 
				new DateRange(this.parseDateText("01/01/2000"), null), address);
		SupervisionLevelCategory supervisionLevel = this
				.supervisionLevelCategoryDelegate.create("Description", true);
		this.officerCaseAssignmentDelegate.create(offender, officer, startDate, 
				endDate, supervisionOffice, supervisionLevel);
		OfficerCaseAssignment officerCaseAssignment = this
				.officerCaseAssignmentDelegate.create(offender, officer, 
						endDate, null, supervisionOffice, supervisionLevel);

		// Action
		officerCaseAssignment = this.officerCaseAssignmentService
				.updateOfficerCaseAssignment(officerCaseAssignment, officer, 
						startDate, this.parseDateText("01/01/2019"), 
						supervisionOffice, supervisionLevel);
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