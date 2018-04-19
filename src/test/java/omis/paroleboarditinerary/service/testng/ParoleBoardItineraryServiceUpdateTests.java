package omis.paroleboarditinerary.service.testng;

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
import omis.location.domain.Location;
import omis.location.service.delegate.LocationDelegate;
import omis.organization.domain.Organization;
import omis.organization.service.delegate.OrganizationDelegate;
import omis.paroleboarditinerary.domain.ParoleBoardItinerary;
import omis.paroleboarditinerary.service.ParoleBoardItineraryService;
import omis.paroleboarditinerary.service.delegate.ParoleBoardItineraryDelegate;
import omis.paroleboardlocation.domain.ParoleBoardLocation;
import omis.paroleboardlocation.service.delegate.ParoleBoardLocationDelegate;
import omis.region.domain.City;
import omis.region.service.delegate.CityDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.util.PropertyValueAsserter;

/**
 * Tests method to update parole board itineraries.
 *
 * @author Josh Divine
 * @version 0.1.1 (Apr 18, 2018)
 * @since OMIS 3.0
 */
public class ParoleBoardItineraryServiceUpdateTests
	extends AbstractHibernateTransactionalTestNGSpringContextTests {

	/* Delegates. */

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
	@Qualifier("paroleBoardItineraryService")
	private ParoleBoardItineraryService paroleBoardItineraryService;

	/* Test methods. */

	/**
	 * Test the update of the location for a parole board itinerary.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	@Test
	public void testUpdateLocation() throws DuplicateEntityFoundException {
		// Arrangements
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
		Boolean onsite = true;
		ParoleBoardItinerary boardItinerary = this.paroleBoardItineraryDelegate
				.create(paroleBoardLocation, onsite, startDate, endDate);
		Organization newOrganization = this.organizationDelegate.create("Org2", 
				"O2", null);
		Location newLocation = this.locationDelegate.create(newOrganization, 
				new DateRange(this.parseDateText("01/01/2000"), null), address);
		ParoleBoardLocation newParoleBoardLocation = this
				.paroleBoardLocationDelegate.create(newLocation, true);
		
		// Action
		boardItinerary = this.paroleBoardItineraryService.update(boardItinerary,
				newParoleBoardLocation, onsite, startDate, endDate);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("paroleBoardLocation", newParoleBoardLocation)
			.addExpectedValue("onsite", onsite)
			.addExpectedValue("dateRange.startDate", startDate)
			.addExpectedValue("dateRange.endDate", endDate)
			.performAssertions(boardItinerary);
	}
	
	/**
	 * Test the update of the on-site for a parole board itinerary.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	@Test
	public void testUpdateOnsite() throws DuplicateEntityFoundException {
		// Arrangements
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
		Boolean onsite = true;
		ParoleBoardItinerary boardItinerary = this.paroleBoardItineraryDelegate
				.create(paroleBoardLocation, onsite, startDate, endDate);
		Boolean newOnsite = true;
		
		// Action
		boardItinerary = this.paroleBoardItineraryService.update(boardItinerary,
				paroleBoardLocation, newOnsite, startDate, endDate);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("paroleBoardLocation", paroleBoardLocation)
			.addExpectedValue("onsite", newOnsite)
			.addExpectedValue("dateRange.startDate", startDate)
			.addExpectedValue("dateRange.endDate", endDate)
			.performAssertions(boardItinerary);
	}
	
	/**
	 * Test the update of the start date for a parole board itinerary.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	@Test
	public void testUpdateStartDate() throws DuplicateEntityFoundException {
		// Arrangements
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
		Boolean onsite = true;
		ParoleBoardItinerary boardItinerary = this.paroleBoardItineraryDelegate
				.create(paroleBoardLocation, onsite, startDate, endDate);
		Date newStartDate = this.parseDateText("01/05/2017");
		
		// Action
		boardItinerary = this.paroleBoardItineraryService.update(boardItinerary,
				paroleBoardLocation, onsite, newStartDate, endDate);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("paroleBoardLocation", paroleBoardLocation)
			.addExpectedValue("onsite", onsite)
			.addExpectedValue("dateRange.startDate", newStartDate)
			.addExpectedValue("dateRange.endDate", endDate)
			.performAssertions(boardItinerary);
	}
	
	/**
	 * Test the update of the end date for a parole board itinerary.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	@Test
	public void testUpdateEndDate() throws DuplicateEntityFoundException {
		// Arrangements
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
		Boolean onsite = true;
		ParoleBoardItinerary boardItinerary = this.paroleBoardItineraryDelegate
				.create(paroleBoardLocation, onsite, startDate, endDate);
		Date newEndDate = this.parseDateText("12/25/2017");
		
		// Action
		boardItinerary = this.paroleBoardItineraryService.update(boardItinerary,
				paroleBoardLocation, onsite, startDate, newEndDate);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("paroleBoardLocation", paroleBoardLocation)
			.addExpectedValue("onsite", onsite)
			.addExpectedValue("dateRange.startDate", startDate)
			.addExpectedValue("dateRange.endDate", newEndDate)
			.performAssertions(boardItinerary);
	}

	/**
	 * Tests {@code DuplicateEntityFoundException} is thrown.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	@Test(expectedExceptions = {DuplicateEntityFoundException.class})
	public void testDuplicateEntityFoundException() 
			throws DuplicateEntityFoundException {
		// Arrangements
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
		Boolean onsite = true;
		this.paroleBoardItineraryDelegate.create(paroleBoardLocation, onsite, 
				startDate, endDate);
		Date secondStartDate = this.parseDateText("02/02/2017");
		ParoleBoardItinerary boardItinerary = this.paroleBoardItineraryDelegate
				.create(paroleBoardLocation, onsite, secondStartDate, endDate);

		// Action
		boardItinerary = this.paroleBoardItineraryService.update(boardItinerary,
				paroleBoardLocation, onsite, startDate, endDate);
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