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
import omis.employment.service.EmploymentService;
import omis.employment.service.delegate.EmployerDelegate;
import omis.employment.service.delegate.EmploymentChangeReasonDelegate;
import omis.employment.service.delegate.EmploymentTermDelegate;
import omis.exception.DateConflictException;
import omis.exception.DuplicateEntityFoundException;
import omis.location.domain.Location;
import omis.location.exception.LocationExistsException;
import omis.location.service.delegate.LocationDelegate;
import omis.offender.domain.Offender;
import omis.offender.service.delegate.OffenderDelegate;
import omis.organization.domain.Organization;
import omis.organization.exception.OrganizationExistsException;
import omis.organization.service.delegate.OrganizationDelegate;
import omis.person.service.delegate.PersonDelegate;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.region.exception.CityExistsException;
import omis.region.service.delegate.CityDelegate;
import omis.region.service.delegate.StateDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.user.service.delegate.UserAccountDelegate;
import omis.util.PropertyValueAsserter;

/**
 * Tests for updating employment term using employment service.
 *
 * @author Trevor Isles
 * @author Josh Divine
 * @author Sheronda Vaughn
 * @version 0.0.2 (Dec 14, 2017)
 * @since OMIS 3.0
 */
@Test(groups = {"employment"})
public class EmploymentServiceUpdateTests 
	extends AbstractHibernateTransactionalTestNGSpringContextTests {

	/* Service delegates. */
	
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
	@Qualifier("personDelegate")
	private PersonDelegate personDelegate;
	
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
	
	/* Service to test. */
	
	@Autowired
	@Qualifier("employmentService")
	private EmploymentService employmentService;
	
	/* Tests. */
	
	/**
	 * Tests the update of the job for an employment term.
	 * @throws EmploymentChangeReasonExistsException 
	 * @throws CityExistsException 
	 * @throws ZipCodeExistsException 
	 * @throws OrganizationExistsException 
	 * @throws LocationExistsException 
	 * @throws EmploymentExistsException 
	 *  
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 * @throws DateConflictException if date conflict exists
	 */
	@Test
	public void testUpdateJob() throws EmploymentChangeReasonExistsException, 
		CityExistsException, ZipCodeExistsException, 
		OrganizationExistsException, LocationExistsException, 
		EmploymentExistsException, DuplicateEntityFoundException, 
		DateConflictException {
		// Arrangement
		EmploymentChangeReason employmentChangeReason = this
				.employmentChangeReasonDelegate.create("Fired", (short) 1, true);
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
		Job newJob = new Job();
		newJob.setSupervisorName("Jim Bob");
		newJob.setJobTitle("Job");
		newJob.setPhoneExtension("1234");
		newJob.setEmploymentLocation(location);
		newJob.setWage(null);
		newJob.setWorkShift(null);
		newJob.setEmployer(employer);
		
		// Action
		employmentTerm = this.employmentService.update(employmentTerm, 
				dateRange, newJob, convictedOfEmployerTheft, 
				employmentChangeReason, verificationSignature);
		
		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("offender", offender)
			.addExpectedValue("job", newJob)
			.addExpectedValue("dateRange", dateRange)
			.addExpectedValue("endReason", employmentChangeReason)
			.addExpectedValue("convictedOfEmployerTheft", 
					convictedOfEmployerTheft)
			.addExpectedValue("verificationSignature", verificationSignature)
			.performAssertions(employmentTerm);
	}

	/**
	 * Tests the update of the date range for an employment term.
	 *  
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	@Test
	public void testUpdateDateRange() 
			throws EmploymentChangeReasonExistsException, CityExistsException, 
			ZipCodeExistsException, OrganizationExistsException, 
			LocationExistsException, EmploymentExistsException, 
			DuplicateEntityFoundException, DateConflictException {
		// Arrangement
		EmploymentChangeReason employmentChangeReason = this
				.employmentChangeReasonDelegate.create("Fired", (short) 1, true);
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
		DateRange newDateRange = new DateRange(this.parseDateText("06/12/2015"), 
				null);
		
		// Action
		employmentTerm = this.employmentService.update(employmentTerm, 
				newDateRange, job, convictedOfEmployerTheft, 
				employmentChangeReason, verificationSignature);
		
		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("offender", offender)
			.addExpectedValue("job", job)
			.addExpectedValue("dateRange", newDateRange)
			.addExpectedValue("endReason", employmentChangeReason)
			.addExpectedValue("convictedOfEmployerTheft", 
					convictedOfEmployerTheft)
			.addExpectedValue("verificationSignature", verificationSignature)
			.performAssertions(employmentTerm);
	}
	
	/**
	 * Tests the update of the end reason for an employment term.
	 * @throws EmploymentChangeReasonExistsException 
	 * @throws CityExistsException 
	 * @throws ZipCodeExistsException 
	 * @throws OrganizationExistsException 
	 * @throws LocationExistsException 
	 * @throws EmploymentExistsException 
	 *  
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	@Test
	public void testUpdateEndReason() 
			throws EmploymentChangeReasonExistsException, CityExistsException, 
			ZipCodeExistsException, OrganizationExistsException, 
			LocationExistsException, EmploymentExistsException,
			DuplicateEntityFoundException {
		// Arrangement
		EmploymentChangeReason employmentChangeReason = this
				.employmentChangeReasonDelegate.create("Fired", (short) 1, true);
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
		EmploymentChangeReason newEmploymentChangeReason = this
				.employmentChangeReasonDelegate.create("Retired", (short) 2, 
						true);
		
		// Action
		employmentTerm = this.employmentService.update(employmentTerm, 
				dateRange, job, convictedOfEmployerTheft, 
				newEmploymentChangeReason, verificationSignature);
		
		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("offender", offender)
			.addExpectedValue("job", job)
			.addExpectedValue("dateRange", dateRange)
			.addExpectedValue("endReason", newEmploymentChangeReason)
			.addExpectedValue("convictedOfEmployerTheft", 
					convictedOfEmployerTheft)
			.addExpectedValue("verificationSignature", verificationSignature)
			.performAssertions(employmentTerm);
	}
	
	/**
	 * Tests the update of the convicted of employer theft for an employment 
	 * term.
	 * @throws EmploymentChangeReasonExistsException 
	 * @throws CityExistsException 
	 * @throws ZipCodeExistsException 
	 * @throws OrganizationExistsException 
	 * @throws LocationExistsException 
	 * @throws EmploymentExistsException 
	 *  
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	@Test
	public void testUpdateConvictedOfEmployerTheft() 
			throws EmploymentChangeReasonExistsException, CityExistsException, 
			ZipCodeExistsException, OrganizationExistsException, 
			LocationExistsException, EmploymentExistsException,
			DuplicateEntityFoundException {
		// Arrangement
		EmploymentChangeReason employmentChangeReason = this
				.employmentChangeReasonDelegate.create("Fired", (short) 1, true);
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
		Boolean newConvictedOfEmployerTheft = true;
		
		// Action
		employmentTerm = this.employmentService.update(employmentTerm, 
				dateRange, job, newConvictedOfEmployerTheft, 
				employmentChangeReason, verificationSignature);
		
		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("offender", offender)
			.addExpectedValue("job", job)
			.addExpectedValue("dateRange", dateRange)
			.addExpectedValue("endReason", employmentChangeReason)
			.addExpectedValue("convictedOfEmployerTheft", 
					newConvictedOfEmployerTheft)
			.addExpectedValue("verificationSignature", verificationSignature)
			.performAssertions(employmentTerm);
	}
	
	/**
	 * Tests the update of the verification signature for an employment term.
	 * @throws CityExistsException 
	 * @throws ZipCodeExistsException 
	 * @throws OrganizationExistsException 
	 * @throws LocationExistsException 
	 * @throws EmploymentExistsException 
	 * @throws EmploymentChangeReasonExistsException 
	 *  
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	@Test
	public void testUpdateVerificationSignature() throws CityExistsException, 
			ZipCodeExistsException, OrganizationExistsException, 
			LocationExistsException, EmploymentExistsException, 
			EmploymentChangeReasonExistsException, DuplicateEntityFoundException
			 {
		// Arrangement
		EmploymentChangeReason employmentChangeReason = this
				.employmentChangeReasonDelegate.create("Fired", (short) 1, true);
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
		VerificationSignature newVerificationSignature = 
				new VerificationSignature(
						this.userAccountDelegate.findByUsername("AUDIT"),
						this.parseDateText("08/06/2016"), true, 
						verificationMethod);
		
		// Action
		employmentTerm = this.employmentService.update(employmentTerm, 
				dateRange, job, convictedOfEmployerTheft, 
				employmentChangeReason, newVerificationSignature);
		
		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("offender", offender)
			.addExpectedValue("job", job)
			.addExpectedValue("dateRange", dateRange)
			.addExpectedValue("endReason", employmentChangeReason)
			.addExpectedValue("convictedOfEmployerTheft", 
					convictedOfEmployerTheft)
			.addExpectedValue("verificationSignature", newVerificationSignature)
			.performAssertions(employmentTerm);
	}
	
	/**
	 * Tests that {@code DuplicateEntityFoundException} is thrown.
	 * @throws EmploymentChangeReasonExistsException 
	 * @throws CityExistsException 
	 * @throws ZipCodeExistsException 
	 * @throws OrganizationExistsException 
	 * @throws LocationExistsException 
	 *  
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	@Test(expectedExceptions = {EmploymentExistsException.class})
	public void testEmploymentExistsException() 
			throws EmploymentExistsException, 
			EmploymentChangeReasonExistsException, CityExistsException, 
			ZipCodeExistsException, OrganizationExistsException, 
			LocationExistsException, DuplicateEntityFoundException {
		// Arrangements
		EmploymentChangeReason employmentChangeReason = this
				.employmentChangeReasonDelegate.create("Fired", (short) 1, true);
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
		DateRange secondDateRange = new DateRange(
				this.parseDateText("01/01/2015"), 
				this.parseDateText("08/11/2015"));
		EmploymentTerm employmentTerm = this.employmentTermDelegate.create(
				offender, secondDateRange, job, convictedOfEmployerTheft,
				employmentChangeReason, verificationSignature);
		
		// Action
		employmentTerm = this.employmentService.update(employmentTerm, 
				dateRange, job, convictedOfEmployerTheft, 
				employmentChangeReason, verificationSignature);
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