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
import omis.location.exception.LocationNotAllowedException;
import omis.location.service.delegate.LocationDelegate;
import omis.organization.domain.Organization;
import omis.organization.service.delegate.OrganizationDelegate;
import omis.person.domain.Person;
import omis.person.service.delegate.PersonDelegate;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.region.service.delegate.CityDelegate;
import omis.region.service.delegate.StateDelegate;
import omis.residence.domain.NonResidenceTerm;
import omis.residence.domain.ResidenceStatus;
import omis.residence.exception.AllowedResidentialLocationRuleExistsException;
import omis.residence.exception.NonResidenceTermExistsException;
import omis.residence.exception.ResidenceStatusConflictException;
import omis.residence.service.ResidenceService;
import omis.residence.service.delegate.AllowedResidentialLocationRuleDelegate;
import omis.residence.service.delegate.NonResidenceTermDelegate;
import omis.residence.service.delegate.ResidenceTermDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.user.service.delegate.UserAccountDelegate;
import omis.util.PropertyValueAsserter;

/**
 * Tests method to update non-residence terms.
 *
 * @author Josh Divine
 * @version 0.0.1
 * @since OMIS 3.0
 */
@Test(groups = {"residence", "service"})
public class ResidenceServiceUpdateNonResidenceTermTests 
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
	 * Tests update of the date range for a non-residence term.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 * @throws ResidenceStatusConflictException if residence term conflict 
	 * exists
	 * @throws LocationNotAllowedException if location is not allowed
	 * @throws NonResidenceTermExistsException  nonResidence term exists
	 * @throws AllowedResidentialLocationRuleExistsException 
	 */
	@Test
	public void testUpdateNonResidenceTermDateRange() 
			throws DuplicateEntityFoundException, 
			ResidenceStatusConflictException, LocationNotAllowedException, 
			NonResidenceTermExistsException, AllowedResidentialLocationRuleExistsException {
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
		ResidenceStatus status = ResidenceStatus.GROUP_HOME;
		ZipCode zipCode = this.zipCodeDelegate.create(city, "00001", null, 
				true);
		Address address = this.addressDelegate.findOrCreate("1 A Street", null, 
				null, null, zipCode);
		Organization organization = this.organizationDelegate.create("Org", 
				null, null);
		Location location = this.locationDelegate.create(organization, 
				dateRange, address);
		this.allowedResidentialLocationRuleDelegate.create(location, status);
		Boolean confirmed = false;		
		VerificationMethod method = this.verificationMethodDelegate.create(
				"Method", (short) 1, true);
		VerificationSignature verificationSignature = new VerificationSignature(
				this.accountDelegate.findByUsername("AUDIT"), 
				this.parseDateText("02/01/2017"), true, method);
		NonResidenceTerm nonResidenceTerm = this.nonResidenceTermDelegate
				.createNonResidenceTerm(person, dateRange, status, location, 
						confirmed, notes, verificationSignature);
		DateRange newDateRange = new DateRange(this.parseDateText("03/01/2017"),
				this.parseDateText("06/01/2017"));
		
		// Action
		nonResidenceTerm = this.residenceService.updateNonResidenceTerm(
				nonResidenceTerm, newDateRange, status, location, confirmed, 
				notes, verificationSignature);
		
		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("person", person)
			.addExpectedValue("dateRange", newDateRange)
			.addExpectedValue("location", location)
			.addExpectedValue("notes", notes)
			.addExpectedValue("status", status)
			.addExpectedValue("confirmed", confirmed)
			.addExpectedValue("verificationSignature", verificationSignature)
			.performAssertions(nonResidenceTerm);
	}
	
	/**
	 * Tests update of the residence status for a non-residence term.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 * @throws ResidenceStatusConflictException if residence term conflict 
	 * exists
	 * @throws LocationNotAllowedException if location is not allowed
	 * @throws NonResidenceTermExistsException nonResidence term exists
	 * @throws AllowedResidentialLocationRuleExistsException 
	 */
	@Test
	public void testUpdateNonResidenceTermResidenceStatus() 
			throws DuplicateEntityFoundException, 
			ResidenceStatusConflictException, LocationNotAllowedException, 
			NonResidenceTermExistsException, AllowedResidentialLocationRuleExistsException {
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
		ResidenceStatus status = ResidenceStatus.GROUP_HOME;
		ZipCode zipCode = this.zipCodeDelegate.create(city, "00001", null, 
				true);
		Address address = this.addressDelegate.findOrCreate("1 A Street", null, 
				null, null, zipCode);
		Organization organization = this.organizationDelegate.create("Org", 
				null, null);
		Location location = this.locationDelegate.create(organization, 
				dateRange, address);
		this.allowedResidentialLocationRuleDelegate.create(location, status);
		Boolean confirmed = false;		
		VerificationMethod method = this.verificationMethodDelegate.create(
				"Method", (short) 1, true);
		VerificationSignature verificationSignature = new VerificationSignature(
				this.accountDelegate.findByUsername("AUDIT"), 
				this.parseDateText("02/01/2017"), true, method);
		NonResidenceTerm nonResidenceTerm = this.nonResidenceTermDelegate
				.createNonResidenceTerm(person, dateRange, status, location, 
						confirmed, notes, verificationSignature);
		ResidenceStatus newStatus = ResidenceStatus.FOSTER_CARE;
		this.allowedResidentialLocationRuleDelegate.create(location, newStatus);
		
		// Action
		nonResidenceTerm = this.residenceService.updateNonResidenceTerm(
				nonResidenceTerm, dateRange, newStatus, location, confirmed, 
				notes, verificationSignature);
		
		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("person", person)
			.addExpectedValue("dateRange", dateRange)
			.addExpectedValue("location", location)
			.addExpectedValue("notes", notes)
			.addExpectedValue("status", newStatus)
			.addExpectedValue("confirmed", confirmed)
			.addExpectedValue("verificationSignature", verificationSignature)
			.performAssertions(nonResidenceTerm);
	}
	
	/**
	 * Tests update of the location for a non-residence term.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 * @throws ResidenceStatusConflictException if residence term conflict 
	 * exists
	 * @throws LocationNotAllowedException if location is not allowed
	 * @throws NonResidenceTermExistsException nonResidence term exists
	 * @throws AllowedResidentialLocationRuleExistsException 
	 */
	@Test
	public void testUpdateNonResidenceTermLocation() 
			throws DuplicateEntityFoundException, 
			ResidenceStatusConflictException, LocationNotAllowedException, 
			NonResidenceTermExistsException, AllowedResidentialLocationRuleExistsException {
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
		ResidenceStatus status = ResidenceStatus.GROUP_HOME;
		ZipCode zipCode = this.zipCodeDelegate.create(city, "00001", null, 
				true);
		Address address = this.addressDelegate.findOrCreate("1 A Street", null, 
				null, null, zipCode);
		Organization organization = this.organizationDelegate.create("Org", 
				null, null);
		Location location = this.locationDelegate.create(organization, 
				dateRange, address);
		this.allowedResidentialLocationRuleDelegate.create(location, status);
		Boolean confirmed = false;		
		VerificationMethod method = this.verificationMethodDelegate.create(
				"Method", (short) 1, true);
		VerificationSignature verificationSignature = new VerificationSignature(
				this.accountDelegate.findByUsername("AUDIT"), 
				this.parseDateText("02/01/2017"), true, method);
		NonResidenceTerm nonResidenceTerm = this.nonResidenceTermDelegate
				.createNonResidenceTerm(person, dateRange, status, location, 
						confirmed, notes, verificationSignature);
		Address newAddress = this.addressDelegate.findOrCreate("1 B Street", 
				null, null, null, zipCode);
		Location newLocation = this.locationDelegate.create(organization, 
				dateRange, newAddress);
		this.allowedResidentialLocationRuleDelegate.create(newLocation, status);
		
		// Action
		nonResidenceTerm = this.residenceService.updateNonResidenceTerm(
				nonResidenceTerm, dateRange, status, newLocation, confirmed, 
				notes, verificationSignature);
		
		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("person", person)
			.addExpectedValue("dateRange", dateRange)
			.addExpectedValue("location", newLocation)
			.addExpectedValue("notes", notes)
			.addExpectedValue("status", status)
			.addExpectedValue("confirmed", confirmed)
			.addExpectedValue("verificationSignature", verificationSignature)
			.performAssertions(nonResidenceTerm);
	}
	
	/**
	 * Tests update of the confirmed status for a non-residence term.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 * @throws ResidenceStatusConflictException if residence term conflict 
	 * exists
	 * @throws LocationNotAllowedException if location is not allowed
	 * @throws NonResidenceTermExistsException nonResidence term exists
	 * @throws AllowedResidentialLocationRuleExistsException 
	 */
	@Test
	public void testUpdateNonResidenceTermConfirmed() 
			throws DuplicateEntityFoundException, 
			ResidenceStatusConflictException, LocationNotAllowedException, 
			NonResidenceTermExistsException, AllowedResidentialLocationRuleExistsException {
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
		ResidenceStatus status = ResidenceStatus.GROUP_HOME;
		ZipCode zipCode = this.zipCodeDelegate.create(city, "00001", null, 
				true);
		Address address = this.addressDelegate.findOrCreate("1 A Street", null, 
				null, null, zipCode);
		Organization organization = this.organizationDelegate.create("Org", 
				null, null);
		Location location = this.locationDelegate.create(organization, 
				dateRange, address);
		this.allowedResidentialLocationRuleDelegate.create(location, status);
		Boolean confirmed = false;		
		VerificationMethod method = this.verificationMethodDelegate.create(
				"Method", (short) 1, true);
		VerificationSignature verificationSignature = new VerificationSignature(
				this.accountDelegate.findByUsername("AUDIT"), 
				this.parseDateText("02/01/2017"), true, method);
		NonResidenceTerm nonResidenceTerm = this.nonResidenceTermDelegate
				.createNonResidenceTerm(person, dateRange, status, location, 
						confirmed, notes, verificationSignature);
		Boolean newConfirmed = true;		
		
		// Action
		nonResidenceTerm = this.residenceService.updateNonResidenceTerm(
				nonResidenceTerm, dateRange, status, location, newConfirmed, 
				notes, verificationSignature);
		
		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("person", person)
			.addExpectedValue("dateRange", dateRange)
			.addExpectedValue("location", location)
			.addExpectedValue("notes", notes)
			.addExpectedValue("status", status)
			.addExpectedValue("confirmed", newConfirmed)
			.addExpectedValue("verificationSignature", verificationSignature)
			.performAssertions(nonResidenceTerm);
	}
	
	/**
	 * Tests update of the notes for a non-residence term.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 * @throws ResidenceStatusConflictException if residence term conflict 
	 * exists
	 * @throws LocationNotAllowedException if location is not allowed
	 * @throws NonResidenceTermExistsException nonResidence term exists
	 * @throws AllowedResidentialLocationRuleExistsException 
	 */
	@Test
	public void testUpdateNonResidenceTermNotes() 
			throws DuplicateEntityFoundException, 
			ResidenceStatusConflictException, LocationNotAllowedException, 
			NonResidenceTermExistsException, AllowedResidentialLocationRuleExistsException {
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
		ResidenceStatus status = ResidenceStatus.GROUP_HOME;
		ZipCode zipCode = this.zipCodeDelegate.create(city, "00001", null, 
				true);
		Address address = this.addressDelegate.findOrCreate("1 A Street", null, 
				null, null, zipCode);
		Organization organization = this.organizationDelegate.create("Org", 
				null, null);
		Location location = this.locationDelegate.create(organization, 
				dateRange, address);
		this.allowedResidentialLocationRuleDelegate.create(location, status);
		Boolean confirmed = false;		
		VerificationMethod method = this.verificationMethodDelegate.create(
				"Method", (short) 1, true);
		VerificationSignature verificationSignature = new VerificationSignature(
				this.accountDelegate.findByUsername("AUDIT"), 
				this.parseDateText("02/01/2017"), true, method);
		NonResidenceTerm nonResidenceTerm = this.nonResidenceTermDelegate
				.createNonResidenceTerm(person, dateRange, status, location, 
						confirmed, notes, verificationSignature);
		String newNotes = "New notes";

		// Action
		nonResidenceTerm = this.residenceService.updateNonResidenceTerm(
				nonResidenceTerm, dateRange, status, location, confirmed, 
				newNotes, verificationSignature);
		
		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("person", person)
			.addExpectedValue("dateRange", dateRange)
			.addExpectedValue("location", location)
			.addExpectedValue("notes", newNotes)
			.addExpectedValue("status", status)
			.addExpectedValue("confirmed", confirmed)
			.addExpectedValue("verificationSignature", verificationSignature)
			.performAssertions(nonResidenceTerm);
	}
	
	/**
	 * Tests update of the verification signature for a non-residence term.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 * @throws ResidenceStatusConflictException if residence term conflict 
	 * exists
	 * @throws LocationNotAllowedException if location is not allowed
	 * @throws NonResidenceTermExistsException nonResidence term exists
	 * @throws AllowedResidentialLocationRuleExistsException 
	 */
	@Test
	public void testUpdateNonResidenceTermVerificationSignature() 
			throws DuplicateEntityFoundException, 
			ResidenceStatusConflictException, LocationNotAllowedException, 
			NonResidenceTermExistsException, AllowedResidentialLocationRuleExistsException {
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
		ResidenceStatus status = ResidenceStatus.GROUP_HOME;
		ZipCode zipCode = this.zipCodeDelegate.create(city, "00001", null, 
				true);
		Address address = this.addressDelegate.findOrCreate("1 A Street", null, 
				null, null, zipCode);
		Organization organization = this.organizationDelegate.create("Org", 
				null, null);
		Location location = this.locationDelegate.create(organization, 
				dateRange, address);
		this.allowedResidentialLocationRuleDelegate.create(location, status);
		Boolean confirmed = false;		
		VerificationMethod method = this.verificationMethodDelegate.create(
				"Method", (short) 1, true);
		VerificationSignature verificationSignature = new VerificationSignature(
				this.accountDelegate.findByUsername("AUDIT"), 
				this.parseDateText("02/01/2017"), true, method);
		NonResidenceTerm nonResidenceTerm = this.nonResidenceTermDelegate
				.createNonResidenceTerm(person, dateRange, status, location, 
						confirmed, notes, verificationSignature);
		VerificationSignature newVerificationSignature = 
				new VerificationSignature(
				this.accountDelegate.findByUsername("AUDIT"), 
				this.parseDateText("02/01/2017"), false, method);
		
		// Action
		nonResidenceTerm = this.residenceService.updateNonResidenceTerm(
				nonResidenceTerm, dateRange, status, location, confirmed, 
				notes, newVerificationSignature);
		
		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("person", person)
			.addExpectedValue("dateRange", dateRange)
			.addExpectedValue("location", location)
			.addExpectedValue("notes", notes)
			.addExpectedValue("status", status)
			.addExpectedValue("confirmed", confirmed)
			.addExpectedValue("verificationSignature", newVerificationSignature)
			.performAssertions(nonResidenceTerm);
	}
	
	/**
	 * Tests {@code DuplicateEntityFoundException} is thrown.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 * @throws ResidenceStatusConflictException if residence term conflict 
	 * exists
	 * @throws LocationNotAllowedException if location is not allowed
	 * @throws NonResidenceTermExistsException nonResidence term exists
	 * @throws AllowedResidentialLocationRuleExistsException 
	 */
	@Test(expectedExceptions = {NonResidenceTermExistsException.class})
	public void testDuplicateEntityFoundException() 
			throws DuplicateEntityFoundException, 
			ResidenceStatusConflictException, LocationNotAllowedException, 
			NonResidenceTermExistsException, AllowedResidentialLocationRuleExistsException {
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
		ResidenceStatus status = ResidenceStatus.GROUP_HOME;
		ZipCode zipCode = this.zipCodeDelegate.create(city, "00001", null, 
				true);
		Address address = this.addressDelegate.findOrCreate("1 A Street", null, 
				null, null, zipCode);
		Organization organization = this.organizationDelegate.create("Org", 
				null, null);
		Location location = this.locationDelegate.create(organization, 
				dateRange, address);
		this.allowedResidentialLocationRuleDelegate.create(location, status);
		Boolean confirmed = false;		
		VerificationMethod method = this.verificationMethodDelegate.create(
				"Method", (short) 1, true);
		VerificationSignature verificationSignature = new VerificationSignature(
				this.accountDelegate.findByUsername("AUDIT"), 
				this.parseDateText("02/01/2017"), true, method);
		this.nonResidenceTermDelegate.createNonResidenceTerm(person, dateRange, 
				status, location, confirmed, notes, verificationSignature);
		ResidenceStatus secondStatus = ResidenceStatus.GROUP_HOME;
		NonResidenceTerm nonResidenceTerm = this.nonResidenceTermDelegate
				.createNonResidenceTerm(person, dateRange, secondStatus, 
						location, confirmed, notes, verificationSignature);
		
		// Action
		this.residenceService.updateNonResidenceTerm(nonResidenceTerm, 
				dateRange, status, location, confirmed, notes, 
				verificationSignature);
	}
		
	/**
	 * Tests {@code LocationNotAllowedException} is thrown.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 * @throws ResidenceStatusConflictException if residence term conflict 
	 * exists
	 * @throws LocationNotAllowedException if location is not allowed
	 * @throws NonResidenceTermExistsException nonResidence term exists
	 */
	@Test(expectedExceptions = {LocationNotAllowedException.class})
	public void testLocationNotAllowedException() 
			throws ResidenceStatusConflictException, 
			LocationNotAllowedException, 
			NonResidenceTermExistsException, DuplicateEntityFoundException {
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
		ResidenceStatus status = ResidenceStatus.GROUP_HOME;
		ZipCode zipCode = this.zipCodeDelegate.create(city, "00001", null, 
				true);
		Address address = this.addressDelegate.findOrCreate("1 A Street", null, 
				null, null, zipCode);
		Organization organization = this.organizationDelegate.create("Org", 
				null, null);
		Location location = this.locationDelegate.create(organization, 
				dateRange, address);
		Boolean confirmed = false;		
		VerificationMethod method = this.verificationMethodDelegate.create(
				"Method", (short) 1, true);
		VerificationSignature verificationSignature = new VerificationSignature(
				this.accountDelegate.findByUsername("AUDIT"), 
				this.parseDateText("02/01/2017"), true, method);
		NonResidenceTerm nonResidenceTerm = this.nonResidenceTermDelegate
				.createNonResidenceTerm(person, dateRange, status, location, 
						confirmed, notes, verificationSignature);
		Address newAddress = this.addressDelegate.findOrCreate("1 B Street", 
				null, null, null, zipCode);
		Location newLocation = this.locationDelegate.create(organization, 
				dateRange, newAddress);
		
		// Action
		this.residenceService.updateNonResidenceTerm(nonResidenceTerm, 
				dateRange, status, newLocation, confirmed, notes, 
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
