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
package omis.residence.service.testng;

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
import omis.audit.domain.VerificationMethod;
import omis.audit.domain.VerificationSignature;
import omis.audit.service.delegate.VerificationMethodDelegate;
import omis.country.domain.Country;
import omis.country.service.delegate.CountryDelegate;
import omis.datatype.DateRange;
import omis.exception.DuplicateEntityFoundException;
import omis.location.service.delegate.LocationDelegate;
import omis.organization.service.delegate.OrganizationDelegate;
import omis.person.domain.Person;
import omis.person.service.delegate.PersonDelegate;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.region.service.delegate.CityDelegate;
import omis.region.service.delegate.StateDelegate;
import omis.residence.domain.ResidenceCategory;
import omis.residence.domain.ResidenceStatus;
import omis.residence.domain.ResidenceTerm;
import omis.residence.exception.NonResidenceTermExistsException;
import omis.residence.exception.PrimaryResidenceExistsException;
import omis.residence.exception.ResidenceStatusConflictException;
import omis.residence.exception.ResidenceTermExistsException;
import omis.residence.service.ResidenceService;
import omis.residence.service.delegate.AllowedResidentialLocationRuleDelegate;
import omis.residence.service.delegate.NonResidenceTermDelegate;
import omis.residence.service.delegate.ResidenceTermDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.user.service.delegate.UserAccountDelegate;
import omis.util.PropertyValueAsserter;

/**
 * Tests method to update residence terms.
 *
 * @author Josh Divine
 * @author Yidong Li
 * @author Sheronda Vaughn
 * @version 0.0.1
 * @since OMIS 3.0
 */
@Test(groups = {"residence", "service"})
public class ResidenceServiceUpdateResidenceTermTests 
	extends AbstractHibernateTransactionalTestNGSpringContextTests {

	/* Service delegates. */
	
	@Autowired
	@Qualifier("personDelegate")
	private PersonDelegate personDelegate;
	
	@Autowired
	@Qualifier("cityDelegate")
	private CityDelegate cityDelegate;
	
	@Autowired
	@Qualifier("stateDelegate")
	private StateDelegate stateDelegate;
	
	@Autowired
	@Qualifier("countryDelegate")
	private CountryDelegate countryDelegate;
	
	@Autowired
	@Qualifier("nonResidenceTermDelegate")
	private NonResidenceTermDelegate nonResidenceTermDelegate;
	
	@Autowired
	@Qualifier("addressDelegate")
	private AddressDelegate addressDelegate;
	
	@Autowired
	@Qualifier("zipCodeDelegate")
	private ZipCodeDelegate zipCodeDelegate;
	
	@Autowired
	@Qualifier("residenceTermDelegate")
	private ResidenceTermDelegate residenceTermDelegate;
	
	@Autowired
	@Qualifier("locationDelegate")
	private LocationDelegate locationDelegate;

	@Autowired
	@Qualifier("organizationDelegate")
	private OrganizationDelegate organizationDelegate;
	
	@Autowired
	@Qualifier("allowedResidentialLocationRuleDelegate")
	private AllowedResidentialLocationRuleDelegate 
			allowedResidentialLocationRuleDelegate;
	
	@Autowired
	@Qualifier("userAccountDelegate")
	private UserAccountDelegate accountDelegate;
	
	@Autowired
	@Qualifier("verificationMethodDelegate")
	private VerificationMethodDelegate verificationMethodDelegate;

	/* Service to test. */
	
	@Autowired
	@Qualifier("residenceService")
	private ResidenceService residenceService;
	
	/* Test methods. */
	
	/**
	 * Tests update of the date range for a residence term.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 * @throws ResidenceStatusConflictException if residence term conflict 
	 * exists
	 * @throws PrimaryResidenceExistsException  if primary residence already 
	 * exists
	 * @throws ResidenceTermExistsException residence term exists exception
	 * @throws NonResidenceTermExistsException nonResidence term exists
	 */
	@Test
	public void testUpdateResidenceTermDateRange() 
			throws DuplicateEntityFoundException, 
			ResidenceStatusConflictException, PrimaryResidenceExistsException,
			ResidenceTermExistsException, NonResidenceTermExistsException {
		// Arrangements
		Person person = this.personDelegate.create("Smith", "John", "Jay", 
				null);
		DateRange dateRange = new DateRange(this.parseDateText("01/01/2017"), 
				this.parseDateText("06/01/2017"));
		Country country = this.countryDelegate.create("Country", "C", true);
		State state = this.stateDelegate.create("State", "ST", country, true, 
				true);
		City city = this.cityDelegate.create("City", true, state, country);
		String notes = "Notes";
		ZipCode zipCode = this.zipCodeDelegate.create(city, "00001", null, 
				true);
		Address address = this.addressDelegate.findOrCreate("1 A Street", null, 
				null, null, zipCode);
		Boolean primary = true;
		Boolean fosterCare = false;
		Boolean confirmed = false;
		VerificationMethod method = this.verificationMethodDelegate.create(
				"Method", (short) 1, true);
		VerificationSignature verificationSignature = new VerificationSignature(
				this.accountDelegate.findByUsername("AUDIT"), 
				this.parseDateText("02/01/2017"), true, method);
		ResidenceCategory category = ResidenceCategory.PRIMARY;
		ResidenceStatus status = ResidenceStatus.RESIDENT;
		ResidenceTerm residenceTerm = this.residenceTermDelegate
				.createResidenceTerm(person, dateRange, category, address, 
						status, confirmed, notes, verificationSignature);
		DateRange newDateRange = new DateRange(this.parseDateText("02/01/2017"),
				this.parseDateText("06/01/2017"));
		
		// Action
		residenceTerm = this.residenceService.updateResidenceTerm(residenceTerm,
				newDateRange, primary, address, fosterCare, confirmed, notes, 
				verificationSignature);
		
		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("person", person)
			.addExpectedValue("dateRange", newDateRange)
			.addExpectedValue("address", address)
			.addExpectedValue("notes", notes)
			.addExpectedValue("status", status)
			.addExpectedValue("category", category)
			.addExpectedValue("confirmed", confirmed)
			.addExpectedValue("verificationSignature", verificationSignature)
			.performAssertions(residenceTerm);
	}
	
	/**
	 * Tests update of the residence category for a residence term.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 * @throws ResidenceStatusConflictException if residence term conflict 
	 * exists
	 * @throws PrimaryResidenceExistsException  if primary residence already 
	 * exists
	 * @throws ResidenceTermExistsException residence term exists exception
	 * @throws NonResidenceTermExistsException nonResidence term exists
	 */
	@Test
	public void testUpdateResidenceTermResidenceCategory() 
			throws DuplicateEntityFoundException, 
			ResidenceStatusConflictException, PrimaryResidenceExistsException,
			ResidenceTermExistsException, NonResidenceTermExistsException {
		// Arrangements
		Person person = this.personDelegate.create("Smith", "John", "Jay", 
				null);
		DateRange dateRange = new DateRange(this.parseDateText("01/01/2017"), 
				this.parseDateText("06/01/2017"));
		Country country = this.countryDelegate.create("Country", "C", true);
		State state = this.stateDelegate.create("State", "ST", country, true, 
				true);
		City city = this.cityDelegate.create("City", true, state, country);
		String notes = "Notes";
		ZipCode zipCode = this.zipCodeDelegate.create(city, "00001", null, 
				true);
		Address address = this.addressDelegate.findOrCreate("1 A Street", null, 
				null, null, zipCode);
		Boolean fosterCare = false;
		Boolean confirmed = false;
		VerificationMethod method = this.verificationMethodDelegate.create(
				"Method", (short) 1, true);
		VerificationSignature verificationSignature = new VerificationSignature(
				this.accountDelegate.findByUsername("AUDIT"), 
				this.parseDateText("02/01/2017"), true, method);
		ResidenceCategory category = ResidenceCategory.PRIMARY;
		ResidenceStatus status = ResidenceStatus.RESIDENT;
		ResidenceTerm residenceTerm = this.residenceTermDelegate
				.createResidenceTerm(person, dateRange, category, address, 
						status, confirmed, notes, verificationSignature);
		Boolean primary = false;
		ResidenceCategory newCategory = ResidenceCategory.SECONDARY;
		
		// Action
		residenceTerm = this.residenceService.updateResidenceTerm(residenceTerm,
				dateRange, primary, address, fosterCare, confirmed, notes, 
				verificationSignature);
		
		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("person", person)
			.addExpectedValue("dateRange", dateRange)
			.addExpectedValue("address", address)
			.addExpectedValue("notes", notes)
			.addExpectedValue("status", status)
			.addExpectedValue("category", newCategory)
			.addExpectedValue("confirmed", confirmed)
			.addExpectedValue("verificationSignature", verificationSignature)
			.performAssertions(residenceTerm);
	}
	
	/**
	 * Tests update of the address for a residence term.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 * @throws ResidenceStatusConflictException if residence term conflict 
	 * exists
	 * @throws PrimaryResidenceExistsException  if primary residence already 
	 * exists
	 * @throws ResidenceTermExistsException residence term exists exception
	 * @throws NonResidenceTermExistsException nonResidence term exists
	 */
	@Test
	public void testUpdateResidenceTermAddress() 
			throws DuplicateEntityFoundException, ResidenceTermExistsException,
			ResidenceStatusConflictException, PrimaryResidenceExistsException, 
			NonResidenceTermExistsException {
		// Arrangements
		Person person = this.personDelegate.create("Smith", "John", "Jay", 
				null);
		DateRange dateRange = new DateRange(this.parseDateText("01/01/2017"), 
				this.parseDateText("06/01/2017"));
		Country country = this.countryDelegate.create("Country", "C", true);
		State state = this.stateDelegate.create("State", "ST", country, true, 
				true);
		City city = this.cityDelegate.create("City", true, state, country);
		String notes = "Notes";
		ZipCode zipCode = this.zipCodeDelegate.create(city, "00001", null, 
				true);
		Address address = this.addressDelegate.findOrCreate("1 A Street", null, 
				null, null, zipCode);
		Boolean primary = true;
		Boolean fosterCare = false;
		Boolean confirmed = false;
		VerificationMethod method = this.verificationMethodDelegate.create(
				"Method", (short) 1, true);
		VerificationSignature verificationSignature = new VerificationSignature(
				this.accountDelegate.findByUsername("AUDIT"), 
				this.parseDateText("02/01/2017"), true, method);
		ResidenceCategory category = ResidenceCategory.PRIMARY;
		ResidenceStatus status = ResidenceStatus.RESIDENT;
		ResidenceTerm residenceTerm = this.residenceTermDelegate
				.createResidenceTerm(person, dateRange, category, address, 
						status, confirmed, notes, verificationSignature);
		Address newAddress = this.addressDelegate.findOrCreate("1 B Street", 
				null, null, null, zipCode);
		
		// Action
		residenceTerm = this.residenceService.updateResidenceTerm(residenceTerm,
				dateRange, primary, newAddress, fosterCare, confirmed, notes, 
				verificationSignature);
		
		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("person", person)
			.addExpectedValue("dateRange", dateRange)
			.addExpectedValue("address", newAddress)
			.addExpectedValue("notes", notes)
			.addExpectedValue("status", status)
			.addExpectedValue("category", category)
			.addExpectedValue("confirmed", confirmed)
			.addExpectedValue("verificationSignature", verificationSignature)
			.performAssertions(residenceTerm);
	}
	
	/**
	 * Tests update of the residence status for a residence term.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 * @throws ResidenceStatusConflictException if residence term conflict 
	 * exists
	 * @throws PrimaryResidenceExistsException  if primary residence already 
	 * exists
	 * @throws ResidenceTermExistsException residence term exists exception
	 * @throws NonResidenceTermExistsException nonResidence term exists
	 */
	@Test
	public void testUpdateResidenceTermResidenceStatus() 
			throws DuplicateEntityFoundException, ResidenceTermExistsException,
			ResidenceStatusConflictException, PrimaryResidenceExistsException, 
			NonResidenceTermExistsException {
		// Arrangements
		Person person = this.personDelegate.create("Smith", "John", "Jay", 
				null);
		DateRange dateRange = new DateRange(this.parseDateText("01/01/2017"), 
				this.parseDateText("06/01/2017"));
		Country country = this.countryDelegate.create("Country", "C", true);
		State state = this.stateDelegate.create("State", "ST", country, true, 
				true);
		City city = this.cityDelegate.create("City", true, state, country);
		String notes = "Notes";
		ZipCode zipCode = this.zipCodeDelegate.create(city, "00001", null, 
				true);
		Address address = this.addressDelegate.findOrCreate("1 A Street", null, 
				null, null, zipCode);
		Boolean primary = true;
		Boolean confirmed = false;
		VerificationMethod method = this.verificationMethodDelegate.create(
				"Method", (short) 1, true);
		VerificationSignature verificationSignature = new VerificationSignature(
				this.accountDelegate.findByUsername("AUDIT"), 
				this.parseDateText("02/01/2017"), true, method);
		ResidenceCategory category = ResidenceCategory.PRIMARY;
		ResidenceStatus status = ResidenceStatus.RESIDENT;
		ResidenceTerm residenceTerm = this.residenceTermDelegate
				.createResidenceTerm(person, dateRange, category, address, 
						status, confirmed, notes, verificationSignature);
		Boolean fosterCare = true;
		ResidenceStatus newStatus = ResidenceStatus.FOSTER_CARE;
		
		// Action
		residenceTerm = this.residenceService.updateResidenceTerm(residenceTerm,
				dateRange, primary, address, fosterCare, confirmed, notes, 
				verificationSignature);
		
		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("person", person)
			.addExpectedValue("dateRange", dateRange)
			.addExpectedValue("address", address)
			.addExpectedValue("notes", notes)
			.addExpectedValue("status", newStatus)
			.addExpectedValue("category", category)
			.addExpectedValue("confirmed", confirmed)
			.addExpectedValue("verificationSignature", verificationSignature)
			.performAssertions(residenceTerm);
	}
	
	/**
	 * Tests update of the confirmed status for a residence term.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 * @throws ResidenceStatusConflictException if residence term conflict 
	 * exists
	 * @throws PrimaryResidenceExistsException  if primary residence already 
	 * exists
	 * @throws ResidenceTermExistsException residence term exists exception
	 * @throws NonResidenceTermExistsException nonResidence term exists
	 */
	@Test
	public void testUpdateResidenceTermConfirmed() 
			throws DuplicateEntityFoundException, ResidenceTermExistsException,
			ResidenceStatusConflictException, PrimaryResidenceExistsException, 
			NonResidenceTermExistsException {
		// Arrangements
		Person person = this.personDelegate.create("Smith", "John", "Jay", 
				null);
		DateRange dateRange = new DateRange(this.parseDateText("01/01/2017"), 
				this.parseDateText("06/01/2017"));
		Country country = this.countryDelegate.create("Country", "C", true);
		State state = this.stateDelegate.create("State", "ST", country, true, 
				true);
		City city = this.cityDelegate.create("City", true, state, country);
		String notes = "Notes";
		ZipCode zipCode = this.zipCodeDelegate.create(city, "00001", null, 
				true);
		Address address = this.addressDelegate.findOrCreate("1 A Street", null, 
				null, null, zipCode);
		Boolean primary = true;
		Boolean fosterCare = false;
		Boolean confirmed = false;
		VerificationMethod method = this.verificationMethodDelegate.create(
				"Method", (short) 1, true);
		VerificationSignature verificationSignature = new VerificationSignature(
				this.accountDelegate.findByUsername("AUDIT"), 
				this.parseDateText("02/01/2017"), true, method);
		ResidenceCategory category = ResidenceCategory.PRIMARY;
		ResidenceStatus status = ResidenceStatus.RESIDENT;
		ResidenceTerm residenceTerm = this.residenceTermDelegate
				.createResidenceTerm(person, dateRange, category, address, 
						status, confirmed, notes, verificationSignature);
		Boolean newConfirmed = false;
		
		// Action
		residenceTerm = this.residenceService.updateResidenceTerm(residenceTerm,
				dateRange, primary, address, fosterCare, newConfirmed, notes, 
				verificationSignature);
		
		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("person", person)
			.addExpectedValue("dateRange", dateRange)
			.addExpectedValue("address", address)
			.addExpectedValue("notes", notes)
			.addExpectedValue("status", status)
			.addExpectedValue("category", category)
			.addExpectedValue("confirmed", newConfirmed)
			.addExpectedValue("verificationSignature", verificationSignature)
			.performAssertions(residenceTerm);
	}
	
	/**
	 * Tests update of the notes for a residence term.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 * @throws ResidenceStatusConflictException if residence term conflict 
	 * exists
	 * @throws PrimaryResidenceExistsException  if primary residence already 
	 * exists
	 * @throws ResidenceTermExistsException residence term exists exception
	 * @throws NonResidenceTermExistsException nonResidence term exists
	 */
	@Test
	public void testUpdateResidenceTermNotes() 
			throws DuplicateEntityFoundException, ResidenceTermExistsException,
			ResidenceStatusConflictException, PrimaryResidenceExistsException, 
			NonResidenceTermExistsException {
		// Arrangements
		Person person = this.personDelegate.create("Smith", "John", "Jay", 
				null);
		DateRange dateRange = new DateRange(this.parseDateText("01/01/2017"), 
				this.parseDateText("06/01/2017"));
		Country country = this.countryDelegate.create("Country", "C", true);
		State state = this.stateDelegate.create("State", "ST", country, true, 
				true);
		City city = this.cityDelegate.create("City", true, state, country);
		String notes = "Notes";
		ZipCode zipCode = this.zipCodeDelegate.create(city, "00001", null, 
				true);
		Address address = this.addressDelegate.findOrCreate("1 A Street", null, 
				null, null, zipCode);
		Boolean primary = true;
		Boolean fosterCare = false;
		Boolean confirmed = false;
		VerificationMethod method = this.verificationMethodDelegate.create(
				"Method", (short) 1, true);
		VerificationSignature verificationSignature = new VerificationSignature(
				this.accountDelegate.findByUsername("AUDIT"), 
				this.parseDateText("02/01/2017"), true, method);
		ResidenceCategory category = ResidenceCategory.PRIMARY;
		ResidenceStatus status = ResidenceStatus.RESIDENT;
		ResidenceTerm residenceTerm = this.residenceTermDelegate
				.createResidenceTerm(person, dateRange, category, address, 
						status, confirmed, notes, verificationSignature);
		String newNotes = "New notes";
		
		// Action
		residenceTerm = this.residenceService.updateResidenceTerm(residenceTerm,
				dateRange, primary, address, fosterCare, confirmed, newNotes, 
				verificationSignature);
		
		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("person", person)
			.addExpectedValue("dateRange", dateRange)
			.addExpectedValue("address", address)
			.addExpectedValue("notes", newNotes)
			.addExpectedValue("status", status)
			.addExpectedValue("category", category)
			.addExpectedValue("confirmed", confirmed)
			.addExpectedValue("verificationSignature", verificationSignature)
			.performAssertions(residenceTerm);
	}
	
	/**
	 * Tests update of the verification signature for a residence term.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 * @throws ResidenceStatusConflictException if residence term conflict 
	 * exists
	 * @throws PrimaryResidenceExistsException  if primary residence already 
	 * exists
	 * @throws ResidenceTermExistsException residence term exists exception
	 * @throws NonResidenceTermExistsException nonResidence terms exists
	 */
	@Test
	public void testUpdateResidenceTermVerificationSignature() 
			throws DuplicateEntityFoundException, ResidenceTermExistsException,
			ResidenceStatusConflictException, PrimaryResidenceExistsException, 
			NonResidenceTermExistsException {
		// Arrangements
		Person person = this.personDelegate.create("Smith", "John", "Jay", 
				null);
		DateRange dateRange = new DateRange(this.parseDateText("01/01/2017"), 
				this.parseDateText("06/01/2017"));
		Country country = this.countryDelegate.create("Country", "C", true);
		State state = this.stateDelegate.create("State", "ST", country, true, 
				true);
		City city = this.cityDelegate.create("City", true, state, country);
		String notes = "Notes";
		ZipCode zipCode = this.zipCodeDelegate.create(city, "00001", null, 
				true);
		Address address = this.addressDelegate.findOrCreate("1 A Street", null, 
				null, null, zipCode);
		Boolean primary = true;
		Boolean fosterCare = false;
		Boolean confirmed = false;
		VerificationMethod method = this.verificationMethodDelegate.create(
				"Method", (short) 1, true);
		VerificationSignature verificationSignature = new VerificationSignature(
				this.accountDelegate.findByUsername("AUDIT"), 
				this.parseDateText("02/01/2017"), true, method);
		ResidenceCategory category = ResidenceCategory.PRIMARY;
		ResidenceStatus status = ResidenceStatus.RESIDENT;
		ResidenceTerm residenceTerm = this.residenceTermDelegate
				.createResidenceTerm(person, dateRange, category, address, 
						status, confirmed, notes, verificationSignature);
		VerificationSignature newVerificationSignature = 
				new VerificationSignature(
						this.accountDelegate.findByUsername("AUDIT"),
						this.parseDateText("02/01/2017"), false, method);
		
		// Action
		residenceTerm = this.residenceService.updateResidenceTerm(residenceTerm,
				dateRange, primary, address, fosterCare, confirmed, notes, 
				newVerificationSignature);
		
		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("person", person)
			.addExpectedValue("dateRange", dateRange)
			.addExpectedValue("address", address)
			.addExpectedValue("notes", notes)
			.addExpectedValue("status", status)
			.addExpectedValue("category", category)
			.addExpectedValue("confirmed", confirmed)
			.addExpectedValue("verificationSignature", newVerificationSignature)
			.performAssertions(residenceTerm);
	}
	
	/**
	 * Tests {@code DuplicateEntityFoundException} is thrown.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 * @throws ResidenceStatusConflictException if residence term conflict 
	 * exists
	 * @throws PrimaryResidenceExistsException if primary residence already 
	 * exists
	 * @throws ResidenceTermExistsException residence term exists exception
	 * @throws NonResidenceTermExistsException nonResidence term exists
	 */
	@Test(expectedExceptions = {ResidenceTermExistsException.class})
	public void testDuplicateEntityFoundException() 
			throws DuplicateEntityFoundException, ResidenceTermExistsException,
			ResidenceStatusConflictException, PrimaryResidenceExistsException, 
			NonResidenceTermExistsException {
		// Arrangements
		Person person = this.personDelegate.create("Smith", "John", "Jay", 
				null);
		DateRange dateRange = new DateRange(this.parseDateText("01/01/2017"), 
				this.parseDateText("06/01/2017"));
		Country country = this.countryDelegate.create("Country", "C", true);
		State state = this.stateDelegate.create("State", "ST", country, true, 
				true);
		City city = this.cityDelegate.create("City", true, state, country);
		String notes = "Notes";
		ZipCode zipCode = this.zipCodeDelegate.create(city, "00001", null, 
				true);
		Address address = this.addressDelegate.findOrCreate("1 A Street", null, 
				null, null, zipCode);
		Boolean primary = false;
		Boolean fosterCare = false;
		Boolean confirmed = false;
		VerificationMethod method = this.verificationMethodDelegate.create(
				"Method", (short) 1, true);
		VerificationSignature verificationSignature = new VerificationSignature(
				this.accountDelegate.findByUsername("AUDIT"), 
				this.parseDateText("02/01/2017"), true, method);
		ResidenceCategory category = ResidenceCategory.SECONDARY;
		ResidenceStatus status = ResidenceStatus.RESIDENT;
		this.residenceTermDelegate.createResidenceTerm(person, dateRange, 
				category, address, status, confirmed, notes, 
				verificationSignature);
		ResidenceCategory secondCategory = ResidenceCategory.SECONDARY;
		Address secondAddress = this.addressDelegate.findOrCreate("1 B Street", 
				null, null, null, zipCode);
		DateRange secondDateRange = new DateRange(
				this.parseDateText("07/01/2017"), null);
		ResidenceTerm residenceTerm = this.residenceTermDelegate
				.createResidenceTerm(person, secondDateRange, secondCategory, 
						secondAddress, status, confirmed, notes, 
						verificationSignature);
		
		// Action
		this.residenceService.updateResidenceTerm(residenceTerm, dateRange, 
				primary, address, fosterCare, confirmed, notes, 
				verificationSignature);
	}
	
	/**
	 * Tests {@code PrimaryResidenceExistsException} is thrown.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 * @throws ResidenceStatusConflictException if residence term conflict 
	 * exists
	 * @throws PrimaryResidenceExistsException if primary residence already 
	 * exists
	 * @throws ResidenceTermExistsException residence term exists exception
	 * @throws NonResidenceTermExistsException nonResidence term exists
	 */
	@Test(expectedExceptions = {PrimaryResidenceExistsException.class})
	public void testPrimaryResidenceExistsException() 
			throws DuplicateEntityFoundException, ResidenceTermExistsException,
			ResidenceStatusConflictException, PrimaryResidenceExistsException, 
			NonResidenceTermExistsException {
		// Arrangements
		Person person = this.personDelegate.create("Smith", "John", "Jay", 
				null);
		DateRange dateRange = new DateRange(this.parseDateText("01/01/2017"), 
				this.parseDateText("06/01/2017"));
		Country country = this.countryDelegate.create("Country", "C", true);
		State state = this.stateDelegate.create("State", "ST", country, true, 
				true);
		City city = this.cityDelegate.create("City", true, state, country);
		String notes = "Notes";
		ZipCode zipCode = this.zipCodeDelegate.create(city, "00001", null, 
				true);
		Address address = this.addressDelegate.findOrCreate("1 A Street", null, 
				null, null, zipCode);
		Boolean primary = true;
		Boolean confirmed = false;
		VerificationMethod method = this.verificationMethodDelegate.create(
				"Method", (short) 1, true);
		VerificationSignature verificationSignature = new VerificationSignature(
				this.accountDelegate.findByUsername("AUDIT"), 
				this.parseDateText("02/01/2017"), true, method);
		ResidenceCategory category = ResidenceCategory.PRIMARY;
		ResidenceStatus status = ResidenceStatus.RESIDENT;
		this.residenceTermDelegate.createResidenceTerm(person, dateRange, 
				category, address, status, confirmed, notes, 
				verificationSignature);
		DateRange newDateRange = new DateRange(this.parseDateText("07/01/2017"), 
				this.parseDateText("08/01/2017"));
		ResidenceCategory secondCategory = ResidenceCategory.SECONDARY;
		ResidenceTerm residenceTerm = this.residenceTermDelegate
				.createResidenceTerm(person, newDateRange, secondCategory, 
						address, status, confirmed, notes, 
						verificationSignature);
		Boolean fosterCare = true;
		
		// Action
		this.residenceService.updateResidenceTerm(residenceTerm, dateRange, 
				primary, address, fosterCare, confirmed, notes, 
				verificationSignature);
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
