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
import omis.paroleboarditinerary.domain.ParoleBoardItineraryNote;
import omis.paroleboarditinerary.service.ParoleBoardItineraryService;
import omis.paroleboarditinerary.service.delegate.ParoleBoardItineraryDelegate;
import omis.paroleboarditinerary.service.delegate.ParoleBoardItineraryNoteDelegate;
import omis.paroleboardlocation.domain.ParoleBoardLocation;
import omis.paroleboardlocation.service.delegate.ParoleBoardLocationDelegate;
import omis.region.domain.City;
import omis.region.service.delegate.CityDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.util.PropertyValueAsserter;

/**
 * Tests method to update parole board itinerary notes.
 *
 * @author Josh Divine
 * @version 0.1.1 (Apr 18, 2018)
 * @since OMIS 3.0
 */
public class ParoleBoardItineraryServiceUpdateBoardItineraryNoteTests
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
	@Qualifier("paroleBoardItineraryNoteDelegate")
	private ParoleBoardItineraryNoteDelegate paroleBoardItineraryNoteDelegate;
	
	@Autowired
	@Qualifier("paroleBoardLocationDelegate")
	private ParoleBoardLocationDelegate paroleBoardLocationDelegate;
	
	/* Services. */

	@Autowired
	@Qualifier("paroleBoardItineraryService")
	private ParoleBoardItineraryService paroleBoardItineraryService;

	/* Test methods. */

	/**
	 * Tests the update of the description for a parole board itinerary note.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	@Test
	public void testUpdateBoardItineraryNoteDescription() 
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
		String description = "Description";
		Date date = this.parseDateText("01/05/2017");
		ParoleBoardItineraryNote itineraryNote = this
				.paroleBoardItineraryNoteDelegate.create(boardItinerary, 
				description, date);
		String newDescription = "New description";

		// Action
		itineraryNote = this.paroleBoardItineraryService
				.updateBoardItineraryNote(itineraryNote, newDescription, date);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("boardItinerary", boardItinerary)
			.addExpectedValue("description", newDescription)
			.addExpectedValue("date", date)
			.performAssertions(itineraryNote);
	}

	/**
	 * Tests the update of the date for a parole board itinerary note.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	@Test
	public void testUpdateBoardItineraryNoteDate() 
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
		String description = "Description";
		Date date = this.parseDateText("01/05/2017");
		ParoleBoardItineraryNote itineraryNote = this
				.paroleBoardItineraryNoteDelegate.create(boardItinerary, 
				description, date);
		Date newDate = this.parseDateText("01/06/2017");
		
		// Action
		itineraryNote = this.paroleBoardItineraryService
				.updateBoardItineraryNote(itineraryNote, description, newDate);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("boardItinerary", boardItinerary)
			.addExpectedValue("description", description)
			.addExpectedValue("date", newDate)
			.performAssertions(itineraryNote);
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
		String description = "Description";
		Date date = this.parseDateText("01/05/2017");
		this.paroleBoardItineraryNoteDelegate.create(boardItinerary, 
				description, date);
		Date secondDate = this.parseDateText("01/06/2017");
		ParoleBoardItineraryNote itineraryNote = this
				.paroleBoardItineraryNoteDelegate.create(boardItinerary, 
				description, secondDate);

		// Action
		itineraryNote = this.paroleBoardItineraryService
				.updateBoardItineraryNote(itineraryNote, description, date);
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