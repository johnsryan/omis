package omis.employment.service.testng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.annotations.Test;

import omis.address.domain.Address;
import omis.address.domain.ZipCode;
import omis.address.exception.ZipCodeExistsException;
import omis.address.service.delegate.AddressDelegate;
import omis.address.service.delegate.ZipCodeDelegate;
import omis.audit.domain.VerificationMethod;
import omis.audit.domain.VerificationSignature;
import omis.audit.service.delegate.VerificationMethodDelegate;
import omis.country.domain.Country;
import omis.country.service.delegate.CountryDelegate;
import omis.datatype.DateRange;
import omis.employment.domain.Employer;
import omis.employment.domain.EmploymentChangeReason;
import omis.employment.domain.EmploymentTerm;
import omis.employment.domain.component.Job;
import omis.employment.exception.EmploymentChangeReasonExistsException;
import omis.employment.exception.EmploymentExistsException;
import omis.employment.service.ChangeEmployerService;
import omis.employment.service.delegate.EmployerDelegate;
import omis.employment.service.delegate.EmploymentChangeReasonDelegate;
import omis.employment.service.delegate.EmploymentTermDelegate;
import omis.exception.DuplicateEntityFoundException;
import omis.location.domain.Location;
import omis.location.exception.LocationExistsException;
import omis.location.service.delegate.LocationDelegate;
import omis.offender.domain.Offender;
import omis.offender.service.delegate.OffenderDelegate;
import omis.organization.domain.Organization;
import omis.organization.exception.OrganizationExistsException;
import omis.organization.service.delegate.OrganizationDelegate;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.region.exception.CityExistsException;
import omis.region.service.delegate.CityDelegate;
import omis.region.service.delegate.StateDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.user.service.delegate.UserAccountDelegate;
import omis.util.PropertyValueAsserter;

/**
 * Tests for changing the employer of an employment term using the employment 
 * service.
 * 
 * @author Josh Divine
 * @author Sheronda Vaughn
 * @version 0.0.1 (Dec 14, 2017)
 * @since OMIS 3.0
 */
public class ChangeEmployerServiceChangeTests
	extends AbstractHibernateTransactionalTestNGSpringContextTests {

	/* Delegates. */

	@Autowired
	@Qualifier("offenderDelegate")
	private OffenderDelegate offenderDelegate;
	
	@Autowired
	@Qualifier("locationDelegate")
	private LocationDelegate locationDelegate;
	
	@Autowired
	@Qualifier("organizationDelegate")
	private OrganizationDelegate organizationDelegate;
	
	@Autowired
	@Qualifier("employerDelegate")
	private EmployerDelegate employerDelegate;
	
	@Autowired
	@Qualifier("employmentTermDelegate")
	private EmploymentTermDelegate employmentTermDelegate;
	
	@Autowired
	@Qualifier("employmentChangeReasonDelegate")
	private EmploymentChangeReasonDelegate employmentChangeReasonDelegate;
	
	@Autowired
	@Qualifier("addressDelegate")
	private AddressDelegate addressDelegate;
	
	@Autowired
	@Qualifier("zipCodeDelegate")
	private ZipCodeDelegate zipCodeDelegate;
	
	@Autowired
	@Qualifier("userAccountDelegate")
	private UserAccountDelegate userAccountDelegate;

	@Autowired
	@Qualifier("countryDelegate")
	private CountryDelegate countryDelegate;
	
	@Autowired
	@Qualifier("stateDelegate")
	private StateDelegate stateDelegate;
	
	@Autowired
	@Qualifier("cityDelegate")
	private CityDelegate cityDelegate;
	
	@Autowired
	@Qualifier("verificationMethodDelegate")
	private VerificationMethodDelegate verificationMethodDelegate;
	
	/* Services. */

	@Autowired
	@Qualifier("changeEmployerService")
	private ChangeEmployerService changeEmployerService;

	/* Test methods. */

	/**
	 * Test changing of an employer on an employment term.
	 * 
	 * @throws EmploymentChangeReasonExistsException if duplicate entity exists
	 * @throws CityExistsException 
	 * @throws ZipCodeExistsException 
	 * @throws OrganizationExistsException 
	 * @throws LocationExistsException 
	 * @throws EmploymentExistsException 
	 */
	@Test
	public void testChange() throws EmploymentChangeReasonExistsException, 
		CityExistsException, ZipCodeExistsException, 
		OrganizationExistsException, LocationExistsException, 
		EmploymentExistsException, DuplicateEntityFoundException {
		// Arrangements
		EmploymentChangeReason employmentChangeReason = this
				.employmentChangeReasonDelegate.create("Fired", (short) 1, 
						true);
		DateRange dateRange = new DateRange(this.parseDateText("08/12/2015"), 
				null);
		Offender offender = this.offenderDelegate.createWithoutIdentity(
				"Blofeld", "Ernst", "Stavro", null);
		Country country = this.countryDelegate.findOrCreate("United States", 
				"US", true);
		State state = this.stateDelegate.findOrCreate("Alabama", "AL", country, 
				true, true);
		Boolean convictedOfEmployerTheft = false;
		City city = this.cityDelegate.create("Alabama", true, state, country);
		ZipCode zipCode = this.zipCodeDelegate.create(city, "59601", "2444", 
				true);
		Address address = this.addressDelegate.findOrCreate("22", null, null, 
				null, zipCode);
		Organization organization = this.organizationDelegate.create(
				"Waffle Shack", "Shack", null);
		Location location = this.locationDelegate.create(organization, 
				dateRange, address);	
		Long telephoneNumber = 8885556666L;
		Employer employer = this.employerDelegate.create(location, 
				telephoneNumber);
		VerificationMethod verificationMethod = this.verificationMethodDelegate
				.create("Method", (short) 1, true);
		VerificationSignature verificationSignature = new VerificationSignature(
				this.userAccountDelegate.findByUsername("AUDIT"), 
				this.parseDateText("08/04/2016"), true, verificationMethod);
		Job job = new Job();
		job.setSupervisorName("John Doe");
		job.setJobTitle("Lackey");
		job.setPhoneExtension("245");
		job.setEmploymentLocation(location);
		job.setWage(null);
		job.setWorkShift(null);
		job.setEmployer(employer);
		EmploymentTerm employmentTerm = this.employmentTermDelegate.create(
				offender, dateRange, job, convictedOfEmployerTheft, 
				employmentChangeReason, verificationSignature);
		Long newTelephoneNumber = 8885556661L;
		Employer newEmployer = this.employerDelegate.create(location, 
				newTelephoneNumber);

		// Action
		employmentTerm = this.changeEmployerService.change(employmentTerm, 
				newEmployer);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("job.employer", newEmployer)
		.performAssertions(employmentTerm);
	}

	/**
	 * Tests that {@code DuplicateEntityFoundException} is thrown.
	 * @throws CityExistsException 
	 * @throws ZipCodeExistsException 
	 * @throws OrganizationExistsException 
	 * @throws LocationExistsException 
	 * @throws EmploymentExistsException 
	 *  
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	@Test(expectedExceptions = {EmploymentChangeReasonExistsException.class})
	public void testEmploymentChangeReasonExistsException() 
			throws EmploymentChangeReasonExistsException, CityExistsException, 
			ZipCodeExistsException, OrganizationExistsException, 
			LocationExistsException, EmploymentExistsException, 
			DuplicateEntityFoundException {
		// Arrangements
		EmploymentChangeReason employmentChangeReason = this
				.employmentChangeReasonDelegate.create("Fired", (short) 1, 
						true);
		DateRange dateRange = new DateRange(this.parseDateText("08/12/2015"), 
				null);
		Offender offender = this.offenderDelegate.createWithoutIdentity(
				"Blofeld", "Ernst", "Stavro", null);
		Country country = this.countryDelegate.findOrCreate("United States", 
				"US", true);
		State state = this.stateDelegate.findOrCreate("Alabama", "AL", country, 
				true, true);
		Boolean convictedOfEmployerTheft = false;
		City city = this.cityDelegate.create("Alabama", true, state, country);
		ZipCode zipCode = this.zipCodeDelegate.create(city, "59601", "2444", 
				true);
		Address address = this.addressDelegate.findOrCreate("22", null, null, 
				null, zipCode);
		Organization organization = this.organizationDelegate.create(
				"Waffle Shack", "Shack", null);
		Location location = this.locationDelegate.create(organization, 
				dateRange, address);	
		Long telephoneNumber = 8885556666L;
		Employer employer = this.employerDelegate.create(location, 
				telephoneNumber);
		VerificationMethod verificationMethod = this.verificationMethodDelegate
				.create("Method", (short) 1, true);
		VerificationSignature verificationSignature = new VerificationSignature(
				this.userAccountDelegate.findByUsername("AUDIT"), 
				this.parseDateText("08/04/2016"), true, verificationMethod);
		Job job = new Job();
		job.setSupervisorName("John Doe");
		job.setJobTitle("Lackey");
		job.setPhoneExtension("245");
		job.setEmploymentLocation(location);
		job.setWage(null);
		job.setWorkShift(null);
		job.setEmployer(employer);
		this.employmentTermDelegate.create(offender, dateRange, job, 
				convictedOfEmployerTheft, employmentChangeReason, 
				verificationSignature);
		Long newTelephoneNumber = 8885556661L;
		Organization newOrganization = this.organizationDelegate.create(
				"Waffle Shack2", "Shack2", null);
		Location newLocation = this.locationDelegate.create(newOrganization, 
				dateRange, address);
		Employer newEmployer = this.employerDelegate.create(newLocation, 
				newTelephoneNumber);
		job.setEmployer(newEmployer);
		EmploymentChangeReason employmentChangeReason2 = this
				.employmentChangeReasonDelegate.create("Fired", (short) 1, 
						true);
		EmploymentTerm employmentTerm = this.employmentTermDelegate.create(
				offender, dateRange, job, convictedOfEmployerTheft, 
				employmentChangeReason2, verificationSignature);

		// Action
		employmentTerm = this.changeEmployerService.change(employmentTerm, 
				newEmployer);
	}
	
	// Parses date text - returns result
	private Date parseDateText(final String dateText) {
		try {
			return new SimpleDateFormat("MM/dd/yyyy").parse(dateText);
		} catch (ParseException e) {
			throw new RuntimeException("Error parsing date", e);
		}
	}
}