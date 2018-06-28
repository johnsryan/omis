package omis.employment.service.testng;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.annotations.Test;

import omis.address.domain.Address;
import omis.address.domain.ZipCode;
import omis.address.exception.ZipCodeExistsException;
import omis.address.service.delegate.AddressDelegate;
import omis.address.service.delegate.ZipCodeDelegate;
import omis.country.domain.Country;
import omis.country.service.delegate.CountryDelegate;
import omis.employment.domain.Employer;
import omis.employment.exception.EmployerExistsException;
import omis.employment.service.EmploymentService;
import omis.employment.service.delegate.EmployerDelegate;
import omis.exception.DuplicateEntityFoundException;
import omis.location.domain.Location;
import omis.location.exception.LocationExistsException;
import omis.location.service.delegate.LocationDelegate;
import omis.organization.domain.Organization;
import omis.organization.exception.OrganizationExistsException;
import omis.organization.service.delegate.OrganizationDelegate;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.region.exception.CityExistsException;
import omis.region.service.delegate.CityDelegate;
import omis.region.service.delegate.StateDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.util.PropertyValueAsserter;

/**
 * Tests for creating employers using employment service.
 * 
 * @author Josh Divine
 * @author Sheronda Vaughn
 * @version 0.0.1 (Dec 14, 2017)
 * @since OMIS 3.0
 */
public class EmploymentServiceCreateEmployerTests
	extends AbstractHibernateTransactionalTestNGSpringContextTests {

	/* Delegates. */

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
	@Qualifier("addressDelegate")
	private AddressDelegate addressDelegate;
	
	@Autowired
	@Qualifier("zipCodeDelegate")
	private ZipCodeDelegate zipCodeDelegate;
	
	@Autowired
	@Qualifier("employerDelegate")
	private EmployerDelegate employerDelegate;
	
	@Autowired
	@Qualifier("locationDelegate")
	private LocationDelegate locationDelegate;
	
	@Autowired
	@Qualifier("organizationDelegate")
	private OrganizationDelegate organizationDelegate;
	
	/* Services. */

	@Autowired
	@Qualifier("employmentService")
	private EmploymentService employmentService;

	/* Test methods. */

	/**
	 * Tests the creation of an employer.
	 * 
	 * @throws EmployerExistsException if duplicate entity exists
	 * @throws CityExistsException 
	 * @throws ZipCodeExistsException 
	 * @throws LocationExistsException 
	 * @throws OrganizationExistsException 
	 */
	@Test
	public void testCreateEmployer() throws EmployerExistsException, 
		CityExistsException, ZipCodeExistsException, 
		OrganizationExistsException, LocationExistsException {
		// Arrangements
		String name = "Employer";
		Long telephoneNumber = 1234567890L;
		Country country = this.countryDelegate.findOrCreate("Country", "C", 
				true);
		State state = this.stateDelegate.findOrCreate("State", "St", country, 
				true, true);
		City city = this.cityDelegate.create("City", true, state, country);
		ZipCode zipCode = this.zipCodeDelegate.create(city, "12345", null, 
				true);
		Address address = this.addressDelegate.findOrCreate("123 Easy St.", 
				null, null, null, zipCode);

		// Action
		Employer employer = this.employmentService.createEmployer(name, 
				telephoneNumber, address);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("location.organization.name", name)
			.addExpectedValue("telephoneNumber", telephoneNumber)
			.addExpectedValue("location.address", address)
			.performAssertions(employer);
	}

	/**
	 * Tests that {@code DuplicateEntityFoundException} is thrown.
	 * @throws CityExistsException 
	 * @throws ZipCodeExistsException 
	 * @throws OrganizationExistsException 
	 * @throws LocationExistsException 
	 *  
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	@Test(expectedExceptions = {EmployerExistsException.class})
	public void testEmployerExistsException() 
			throws EmployerExistsException, CityExistsException, 
			ZipCodeExistsException, OrganizationExistsException, 
			LocationExistsException {
		// Arrangements
		String name = "Employer";
		Long telephoneNumber = 1234567890L;
		Country country = this.countryDelegate.findOrCreate("Country", "C", 
				true);
		State state = this.stateDelegate.findOrCreate("State", "St", country, 
				true, true);
		City city = this.cityDelegate.create("City", true, state, country);
		ZipCode zipCode = this.zipCodeDelegate.create(city, "12345", null, 
				true);
		Address address = this.addressDelegate.findOrCreate("123 Easy St.", 
				null, null, null, zipCode);
		Organization organization = this.organizationDelegate.create(name, null, 
				null);
		Location location = this.locationDelegate.create(organization, null, 
				address);
		this.employerDelegate.create(location, telephoneNumber);

		// Action
		this.employmentService.createEmployer(name, telephoneNumber, address);
	}
}