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
 * Tests method to create non-residence terms.
 *
 * @author Josh Divine
 * @version 0.0.1
 * @since OMIS 3.0
 */
@Test(groups = {"residence", "service"})
public class ResidenceServiceCreateNonResidenceTermTests 
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
	 * Tests creation of a non-residence term.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 * @throws ResidenceStatusConflictException if residence term conflict 
	 * exists
	 * @throws LocationNotAllowedException if location is not allowed
	 * @throws NonResidenceTermExistsException nonResidence term exists
	 * @throws AllowedResidentialLocationRuleExistsException 
	 */
	@Test
	public void testCreateNonResidenceTerm() 
			throws ResidenceStatusConflictException, 
			LocationNotAllowedException, 
			NonResidenceTermExistsException, 
			AllowedResidentialLocationRuleExistsException, 
			DuplicateEntityFoundException {
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
		
		// Action
		NonResidenceTerm nonResidenceTerm = this.residenceService
				.createNonResidenceTerm(person, dateRange, status, location, 
						confirmed, notes, verificationSignature);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("person", person)
			.addExpectedValue("dateRange", dateRange)
			.addExpectedValue("location", location)
			.addExpectedValue("notes", notes)
			.addExpectedValue("status", status)
			.addExpectedValue("confirmed", confirmed)
			.addExpectedValue("verificationSignature", verificationSignature)
			.performAssertions(nonResidenceTerm);
	}
			
	/**
	 * Tests {@code LocationNotAllowedException} is thrown.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 * @throws ResidenceStatusConflictException if residence term conflict 
	 * exists
	 * @throws LocationNotAllowedException if location is not allowed
	 * @throws NonResidenceTermExistsException non residence term exists
	 */
	@Test(expectedExceptions = {LocationNotAllowedException.class})
	public void testLocationNotAllowedException() 
			throws DuplicateEntityFoundException, 
			ResidenceStatusConflictException, LocationNotAllowedException, 
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
		
		// Action
		this.residenceService.createNonResidenceTerm(person, dateRange, status, 
				location, confirmed, notes, verificationSignature);
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