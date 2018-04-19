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
import omis.facility.domain.Facility;
import omis.facility.domain.Unit;
import omis.facility.service.delegate.FacilityDelegate;
import omis.facility.service.delegate.UnitDelegate;
import omis.location.domain.Location;
import omis.location.service.delegate.LocationDelegate;
import omis.organization.domain.Organization;
import omis.organization.service.delegate.OrganizationDelegate;
import omis.paroleboarditinerary.domain.BoardMeetingSite;
import omis.paroleboarditinerary.domain.ParoleBoardItinerary;
import omis.paroleboarditinerary.service.ParoleBoardItineraryService;
import omis.paroleboarditinerary.service.delegate.BoardMeetingSiteDelegate;
import omis.paroleboarditinerary.service.delegate.ParoleBoardItineraryDelegate;
import omis.paroleboardlocation.domain.ParoleBoardLocation;
import omis.paroleboardlocation.service.delegate.ParoleBoardLocationDelegate;
import omis.region.domain.City;
import omis.region.service.delegate.CityDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.util.PropertyValueAsserter;

/**
 * Tests method to update board meeting sites.
 *
 * @author Josh Divine
 * @version 0.1.2 (Apr 18, 2018)
 * @since OMIS 3.0
 */
public class ParoleBoardItineraryServiceUpdateBoardMeetingSiteTests
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
	@Qualifier("boardMeetingSiteDelegate")
	private BoardMeetingSiteDelegate boardMeetingSiteDelegate;
	
	@Autowired
	@Qualifier("paroleBoardLocationDelegate")
	private ParoleBoardLocationDelegate paroleBoardLocationDelegate;

	@Autowired
	@Qualifier("facilityDelegate")
	private FacilityDelegate facilityDelegate;
	
	@Autowired
	@Qualifier("unitDelegate")
	private UnitDelegate unitDelegate;
	
	/* Services. */

	@Autowired
	@Qualifier("paroleBoardItineraryService")
	private ParoleBoardItineraryService paroleBoardItineraryService;

	/* Test methods. */

	/**
	 * Tests the update of the location for a board meeting site.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	@Test
	public void testUpdateBoardMeetingSiteLocation() 
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
		ParoleBoardItinerary boardItinerary = this.paroleBoardItineraryDelegate
				.create(paroleBoardLocation, true, startDate, endDate);
		Organization secondOrganization = this.organizationDelegate.create(
				"Org2", "O2", null);
		Location secondLocation = this.locationDelegate.create(
				secondOrganization, new DateRange(
						this.parseDateText("01/01/2000"), null), address);
		Date date = this.parseDateText("01/01/2005");
		Integer order = 1;
		Facility facility = this.facilityDelegate.create(secondLocation, 
				"Facility", "F", true, null);
		Unit unit = this.unitDelegate.create("Unit", "U", true, facility, null);
		BoardMeetingSite boardMeetingSite = this.boardMeetingSiteDelegate
				.create(boardItinerary, secondLocation, unit, date, order);
		Organization newOrganization = this.organizationDelegate.create(
				"Org3", "O3", null);
		Location newLocation = this.locationDelegate.create(newOrganization, 
				new DateRange(this.parseDateText("01/01/2000"), null), address);

		// Action
		boardMeetingSite = this.paroleBoardItineraryService
				.updateBoardMeetingSite(boardMeetingSite, newLocation, unit, 
						date, order);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("boardItinerary", boardItinerary)
			.addExpectedValue("location", newLocation)
			.addExpectedValue("unit", unit)
			.addExpectedValue("date", date)
			.addExpectedValue("order", order)
			.performAssertions(boardMeetingSite);
	}
	
	/**
	 * Tests the update of the unit for a board meeting site.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	@Test
	public void testUpdateBoardMeetingSiteUnit() 
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
		ParoleBoardItinerary boardItinerary = this.paroleBoardItineraryDelegate
				.create(paroleBoardLocation, true, startDate, endDate);
		Organization secondOrganization = this.organizationDelegate.create(
				"Org2", "O2", null);
		Location secondLocation = this.locationDelegate.create(
				secondOrganization, new DateRange(
						this.parseDateText("01/01/2000"), null), address);
		Date date = this.parseDateText("01/01/2005");
		Integer order = 1;
		Facility facility = this.facilityDelegate.create(secondLocation, 
				"Facility", "F", true, null);
		Unit unit = this.unitDelegate.create("Unit", "U", true, facility, null);
		BoardMeetingSite boardMeetingSite = this.boardMeetingSiteDelegate
				.create(boardItinerary, secondLocation, unit, date, order);
		Unit newUnit = this.unitDelegate.create("Unit 2", "U2", true, facility, 
				null);
		
		// Action
		boardMeetingSite = this.paroleBoardItineraryService
				.updateBoardMeetingSite(boardMeetingSite, secondLocation, 
						newUnit, date, order);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("boardItinerary", boardItinerary)
			.addExpectedValue("location", secondLocation)
			.addExpectedValue("unit", newUnit)
			.addExpectedValue("date", date)
			.addExpectedValue("order", order)
			.performAssertions(boardMeetingSite);
	}
	
	/**
	 * Tests the update of the date for a board meeting site.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	@Test
	public void testUpdateBoardMeetingSiteDate() 
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
		ParoleBoardItinerary boardItinerary = this.paroleBoardItineraryDelegate
				.create(paroleBoardLocation, true, startDate, endDate);
		Organization secondOrganization = this.organizationDelegate.create(
				"Org2", "O2", null);
		Location secondLocation = this.locationDelegate.create(
				secondOrganization, new DateRange(
						this.parseDateText("01/01/2000"), null), address);
		Date date = this.parseDateText("01/01/2005");
		Integer order = 1;
		Facility facility = this.facilityDelegate.create(secondLocation, 
				"Facility", "F", true, null);
		Unit unit = this.unitDelegate.create("Unit", "U", true, facility, null);
		BoardMeetingSite boardMeetingSite = this.boardMeetingSiteDelegate
				.create(boardItinerary, secondLocation, unit, date, order);
		Date newDate = this.parseDateText("01/02/2005");
		
		// Action
		boardMeetingSite = this.paroleBoardItineraryService
				.updateBoardMeetingSite(boardMeetingSite, secondLocation, unit,
						newDate, order);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("boardItinerary", boardItinerary)
			.addExpectedValue("location", secondLocation)
			.addExpectedValue("unit", unit)
			.addExpectedValue("date", newDate)
			.addExpectedValue("order", order)
			.performAssertions(boardMeetingSite);
	}

	/**
	 * Tests the update of the order for a board meeting site.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	@Test
	public void testUpdateBoardMeetingSiteOrder() 
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
		ParoleBoardItinerary boardItinerary = this.paroleBoardItineraryDelegate
				.create(paroleBoardLocation, true, startDate, endDate);
		Organization secondOrganization = this.organizationDelegate.create(
				"Org2", "O2", null);
		Location secondLocation = this.locationDelegate.create(
				secondOrganization, new DateRange(
						this.parseDateText("01/01/2000"), null), address);
		Date date = this.parseDateText("01/01/2005");
		Integer order = 1;
		Facility facility = this.facilityDelegate.create(secondLocation, 
				"Facility", "F", true, null);
		Unit unit = this.unitDelegate.create("Unit", "U", true, facility, null);
		BoardMeetingSite boardMeetingSite = this.boardMeetingSiteDelegate
				.create(boardItinerary, secondLocation, unit, date, order);
		Integer newOrder = 2;
		
		// Action
		boardMeetingSite = this.paroleBoardItineraryService
				.updateBoardMeetingSite(boardMeetingSite, secondLocation, unit, 
						date, newOrder);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("boardItinerary", boardItinerary)
			.addExpectedValue("location", secondLocation)
			.addExpectedValue("unit", unit)
			.addExpectedValue("date", date)
			.addExpectedValue("order", newOrder)
			.performAssertions(boardMeetingSite);
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
		ParoleBoardItinerary boardItinerary = this.paroleBoardItineraryDelegate
				.create(paroleBoardLocation, true, startDate, endDate);
		Organization secondOrganization = this.organizationDelegate.create(
				"Org2", "O2", null);
		Location secondLocation = this.locationDelegate.create(
				secondOrganization, new DateRange(
						this.parseDateText("01/01/2000"), null), address);
		Date date = this.parseDateText("01/01/2005");
		Integer order = 1;
		Facility facility = this.facilityDelegate.create(secondLocation, 
				"Facility", "F", true, null);
		Unit unit = this.unitDelegate.create("Unit", "U", true, facility, null);
		this.boardMeetingSiteDelegate.create(boardItinerary, secondLocation, 
				unit, date, order);
		Date secondDate = this.parseDateText("01/01/2005");
		BoardMeetingSite boardMeetingSite = this.boardMeetingSiteDelegate
				.create(boardItinerary, secondLocation, unit, secondDate, 
						order);

		// Action
		boardMeetingSite = this.paroleBoardItineraryService
				.updateBoardMeetingSite(boardMeetingSite, secondLocation, unit,
						date, order);
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