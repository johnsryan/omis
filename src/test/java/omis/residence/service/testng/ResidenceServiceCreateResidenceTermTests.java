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
import omis.location.domain.Location;
import omis.location.service.delegate.LocationDelegate;
import omis.organization.domain.Organization;
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
 * Tests method to create residence terms.
 *
 * @author Josh Divine
 * @author Yidong Li
 * @version 0.0.1
 * @since OMIS 3.0
 */
@Test(groups = {"residence", "service"})
public class ResidenceServiceCreateResidenceTermTests 
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
	 * Tests creation of a residence term.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 * @throws ResidenceStatusConflictException if residence term conflict 
	 * exists
	 * @throws PrimaryResidenceExistsException  if primary residence already 
	 * exists
	 * @throws ResidenceTermExistsException residence term exists
	 * @throws NonResidenceTermExistsException nonResidence term exists
	 */
	@Test
	public void testCreateResidenceTerm() 
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
		
		// Action
		ResidenceTerm residenceTerm = this.residenceService.createResidenceTerm(
				person, dateRange, primary, address, fosterCare, confirmed, 
				notes, verificationSignature);
		
		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("person", person)
			.addExpectedValue("dateRange", dateRange)
			.addExpectedValue("address", address)
			.addExpectedValue("notes", notes)
			.addExpectedValue("status", ResidenceStatus.RESIDENT)
			.addExpectedValue("category", ResidenceCategory.PRIMARY)
			.addExpectedValue("confirmed", confirmed)
			.addExpectedValue("verificationSignature", verificationSignature)
			.performAssertions(residenceTerm);
	}
	
	/**
	 * Tests {@code ResidenceTermExistsException} is thrown.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 * @throws ResidenceStatusConflictException if residence term conflict 
	 * exists
	 * @throws PrimaryResidenceExistsException if primary residence already 
	 * @throws ResidenceTermExistsException residence term exists exception
	 * exists
	 * @throws NonResidenceTermExistsException nonResidence term exists
	 */
	@Test(expectedExceptions = {ResidenceTermExistsException.class})
	public void testDuplicateEntityFoundException() 
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
		this.residenceTermDelegate.createResidenceTerm(person, dateRange, 
				ResidenceCategory.PRIMARY, address, ResidenceStatus.RESIDENT, 
				null, notes, null);
		ResidenceStatus status = ResidenceStatus.GROUP_HOME;
		Organization organization = this.organizationDelegate.create("Org", 
				null, null);
		Location location = this.locationDelegate.create(organization, 
				dateRange, address);
		this.nonResidenceTermDelegate.createNonResidenceTerm(person, dateRange, 
				status, location, null, notes, null);
		Boolean primary = false;
		Boolean fosterCare = false;
		Boolean confirmed = false;
		VerificationMethod method = this.verificationMethodDelegate.create(
				"Method", (short) 1, true);
		VerificationSignature verificationSignature = new VerificationSignature(
				this.accountDelegate.findByUsername("AUDIT"), 
				this.parseDateText("02/01/2017"), true, method);
		this.residenceTermDelegate.createResidenceTerm(person, dateRange, 
				ResidenceCategory.PRIMARY, address, ResidenceStatus.RESIDENT, 
				confirmed, notes, verificationSignature);
		
		// Action
		this.residenceService.createResidenceTerm(person, dateRange, primary, 
				address, fosterCare, confirmed, notes, verificationSignature);
	}
	
	/**
	 * Tests {@code ResidenceStatusConflictException} is thrown.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 * @throws ResidenceStatusConflictException if residence term conflict 
	 * exists
	 * @throws PrimaryResidenceExistsException if primary residence exists 
	 * @throws LocationNotAllowedException if location is not allowed
	 */
	/*
	@Test(expectedExceptions = {ResidenceStatusConflictException.class})
	public void testResidenceStatusConflictException() 
			throws DuplicateEntityFoundException, 
			ResidenceStatusConflictException, PrimaryResidenceExistsException, 
			LocationNotAllowedException {
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
		this.nonResidenceTermDelegate.createHomelessTerm(person, dateRange, 
				city, state, notes);
		Boolean primary = true;
		Boolean fosterCare = false;
		Boolean confirmed = false;
		VerificationMethod method = this.verificationMethodDelegate.create(
				"Method", (short) 1, true);
		VerificationSignature verificationSignature = new VerificationSignature(
				this.accountDelegate.findByUsername("AUDIT"), 
				this.parseDateText("02/01/2017"), true, method);
//		this.residenceTermDelegate.createResidenceTerm(person, dateRange, 
//				ResidenceCategory.PRIMARY, address, ResidenceStatus.RESIDENT, 
//				confirmed, notes, verificationSignature);
		
		// Action
		this.residenceService.createResidenceTerm(person, dateRange, primary, 
				address, fosterCare, confirmed, notes, verificationSignature);
	}*/
	
	/**
	 * Tests {@code ResidenceTermExistsException} is thrown.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 * @throws ResidenceStatusConflictException if residence term conflict 
	 * exists
	 * @throws PrimaryResidenceExistsException if primary residence already 
	 * @throws ResidenceTermExistsException residence term exists exception
	 * exists
	 * @throws NonResidenceTermExistsException nonResidence term exists
	 */
	@Test(expectedExceptions = {PrimaryResidenceExistsException.class})
	public void testPrimaryResidenceExistsException() 
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
		this.residenceTermDelegate.createResidenceTerm(person, dateRange, 
				ResidenceCategory.PRIMARY, address, ResidenceStatus.FOSTER_CARE,
				confirmed, notes, verificationSignature);
		
		// Action
		this.residenceService.createResidenceTerm(person, dateRange, primary, 
				address, fosterCare, confirmed, notes, verificationSignature);
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
