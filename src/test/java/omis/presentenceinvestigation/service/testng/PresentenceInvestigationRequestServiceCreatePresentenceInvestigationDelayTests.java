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
package omis.presentenceinvestigation.service.testng;

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
import omis.court.domain.Court;
import omis.court.domain.CourtCategory;
import omis.court.service.delegate.CourtDelegate;
import omis.datatype.DateRange;
import omis.docket.domain.Docket;
import omis.docket.exception.DocketExistsException;
import omis.docket.service.delegate.DocketDelegate;
import omis.exception.DuplicateEntityFoundException;
import omis.location.domain.Location;
import omis.location.service.delegate.LocationDelegate;
import omis.organization.domain.Organization;
import omis.organization.service.delegate.OrganizationDelegate;
import omis.person.domain.Person;
import omis.person.service.delegate.PersonDelegate;
import omis.presentenceinvestigation.domain.PresentenceInvestigationCategory;
import omis.presentenceinvestigation.domain.PresentenceInvestigationDelay;
import omis.presentenceinvestigation.domain.PresentenceInvestigationDelayCategory;
import omis.presentenceinvestigation.domain.PresentenceInvestigationRequest;
import omis.presentenceinvestigation.service.PresentenceInvestigationRequestService;
import omis.presentenceinvestigation.service.delegate.PresentenceInvestigationCategoryDelegate;
import omis.presentenceinvestigation.service.delegate.PresentenceInvestigationDelayCategoryDelegate;
import omis.presentenceinvestigation.service.delegate.PresentenceInvestigationDelayDelegate;
import omis.presentenceinvestigation.service.delegate.PresentenceInvestigationRequestDelegate;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.region.service.delegate.CityDelegate;
import omis.region.service.delegate.StateDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.user.domain.UserAccount;
import omis.user.service.delegate.UserAccountDelegate;
import omis.util.PropertyValueAsserter;

/**
 * Tests method to create presentence investigation delays.
 *
 * @author Josh Divine
 * @version 0.1.1 (May 9, 2018)
 * @since OMIS 3.0
 */
@Test
public class 
	PresentenceInvestigationRequestServiceCreatePresentenceInvestigationDelayTests
	extends AbstractHibernateTransactionalTestNGSpringContextTests {

	/* Delegates. */

	@Autowired
	private PresentenceInvestigationRequestDelegate 
			presentenceInvestigationRequestDelegate;

	@Autowired
	private PresentenceInvestigationDelayDelegate 
			presentenceInvestigationDelayDelegate;
	
	@Autowired
	private PresentenceInvestigationDelayCategoryDelegate 
			presentenceInvestigationDelayCategoryDelegate;

	@Autowired
	private PersonDelegate personDelegate;
	
	@Autowired
	private UserAccountDelegate userAccountDelegate;
	
	@Autowired
	private CourtDelegate courtDelegate;
	
	@Autowired
	private DocketDelegate docketDelegate;
	
	@Autowired
	private LocationDelegate locationDelegate;
	
	@Autowired
	private OrganizationDelegate organizationDelegate;
	
	@Autowired
	private AddressDelegate addressDelegate;
	
	@Autowired
	private CountryDelegate countryDelegate;
	
	@Autowired
	private StateDelegate stateDelegate;
	
	@Autowired
	private CityDelegate cityDelegate;
	
	@Autowired
	private ZipCodeDelegate zipCodeDelegate;
	
	@Autowired
	private PresentenceInvestigationCategoryDelegate
		presentenceInvestigationCategoryDelegate;
	
	/* Services. */

	@Autowired
	@Qualifier("presentenceInvestigationRequestService")
	private PresentenceInvestigationRequestService 
			presentenceInvestigationRequestService;

	/* Test methods. */

	/**
	 * Tests the creation of presentence investigation delays.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 * @throws DocketExistsException if duplicate docket exists
	 */
	@Test
	public void testCreatePresentenceInvestigationDelay() 
			throws DuplicateEntityFoundException, DocketExistsException {
		// Arrangements
		Person person = this.personDelegate.create("Wayne", "Bruce", "Alen", 
				null);
		Person user = this.personDelegate.create("Grayson", "Richard", "J", 
				null);
		UserAccount userAccount = this.userAccountDelegate.create(user, 
				"ROBIN34", "password1", this.parseDateText("12/12/2299"), 0, 
				true);
		Organization organization = this.organizationDelegate.create("Batcave", 
				"TBC", null);
		Country country = this.countryDelegate.create("Country", "USA", true);
		State state = this.stateDelegate.create("State", "ST", country, true, 
				true);
		City city = this.cityDelegate.create("City", true, state, country);
		ZipCode zipCode = this.zipCodeDelegate.create(city, "12345", null, true);
		Address address = this.addressDelegate.findOrCreate("123value", null,
				null, null, zipCode);
		Location location = this.locationDelegate.create(organization,
				new DateRange(this.parseDateText("01/01/2001"),
						this.parseDateText("01/01/2020")), address);
		Court court = this.courtDelegate.create("Court Of Justice!",
				CourtCategory.CITY, location);
		Docket docket = this.docketDelegate.create(person, court, "Docket");
		PresentenceInvestigationCategory category = this
				.presentenceInvestigationCategoryDelegate.create("PSI Category", 
						true);
		PresentenceInvestigationRequest presentenceInvestigationRequest = this
				.presentenceInvestigationRequestDelegate.create(
						userAccount, this.parseDateText("01/01/2016"),
						this.parseDateText("12/31/2017"), docket,  
						this.parseDateText("03/25/2015"), category, 
						this.parseDateText("04/01/2017"));
		Date date = this.parseDateText("11/30/2017");
		PresentenceInvestigationDelayCategory reason = this
				.presentenceInvestigationDelayCategoryDelegate.create(
						"Category", true);

		// Action
		PresentenceInvestigationDelay presentenceInvestigationDelay = this
				.presentenceInvestigationRequestService
				.createPresentenceInvestigationDelay(
						presentenceInvestigationRequest, date, reason);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("presentenceInvestigationRequest", 
					presentenceInvestigationRequest)
			.addExpectedValue("date", date)
			.addExpectedValue("reason", reason)
			.performAssertions(presentenceInvestigationDelay);
	}

	/**
	 * Tests {@code DuplicateEntityFoundException} is thrown.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 * @throws DocketExistsException if duplicate docket exists
	 */
	@Test(expectedExceptions = {DuplicateEntityFoundException.class})
	public void testDuplicateEntityFoundException() 
			throws DuplicateEntityFoundException, DocketExistsException {
		// Arrangements
		Person person = this.personDelegate.create("Wayne", "Bruce", "Alen", 
				null);
		Person user = this.personDelegate.create("Grayson", "Richard", "J", 
				null);
		UserAccount userAccount = this.userAccountDelegate.create(user, 
				"ROBIN34", "password1", this.parseDateText("12/12/2299"), 0, 
				true);
		Organization organization = this.organizationDelegate.create("Batcave", 
				"TBC", null);
		Country country = this.countryDelegate.create("Country", "USA", true);
		State state = this.stateDelegate.create("State", "ST", country, true, 
				true);
		City city = this.cityDelegate.create("City", true, state, country);
		ZipCode zipCode = this.zipCodeDelegate.create(city, "12345", null, true);
		Address address = this.addressDelegate.findOrCreate("123value", null,
				null, null, zipCode);
		Location location = this.locationDelegate.create(organization,
				new DateRange(this.parseDateText("01/01/2001"),
						this.parseDateText("01/01/2020")), address);
		Court court = this.courtDelegate.create("Court Of Justice!",
				CourtCategory.CITY, location);
		Docket docket = this.docketDelegate.create(person, court, "Docket");
		PresentenceInvestigationCategory category = this
				.presentenceInvestigationCategoryDelegate.create("PSI Category", 
						true);
		PresentenceInvestigationRequest presentenceInvestigationRequest = this
				.presentenceInvestigationRequestDelegate.create(
						userAccount, this.parseDateText("01/01/2016"),
						this.parseDateText("12/31/2017"), docket,  
						this.parseDateText("03/25/2015"), category, 
						this.parseDateText("04/01/2017"));
		Date date = this.parseDateText("11/30/2017");
		PresentenceInvestigationDelayCategory reason = this
				.presentenceInvestigationDelayCategoryDelegate.create(
						"Category", true);
		this.presentenceInvestigationDelayDelegate.create(
				presentenceInvestigationRequest, date, reason);
		
		// Action
		this.presentenceInvestigationRequestService
				.createPresentenceInvestigationDelay(
						presentenceInvestigationRequest, date, reason);
	}
	
	private Date parseDateText(final String dateText) {
		try {
			return new SimpleDateFormat("MM/dd/yyyy").parse(dateText);
		} catch (ParseException e) {
			throw new RuntimeException("Error parsing date", e);
		}
	}
}