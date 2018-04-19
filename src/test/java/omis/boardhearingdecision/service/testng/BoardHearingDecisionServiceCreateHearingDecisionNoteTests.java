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
package omis.boardhearingdecision.service.testng;

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
import omis.boardhearing.domain.BoardHearing;
import omis.boardhearing.service.delegate.BoardHearingDelegate;
import omis.boardhearingdecision.domain.BoardHearingDecision;
import omis.boardhearingdecision.domain.BoardHearingDecisionCategory;
import omis.boardhearingdecision.domain.DecisionCategory;
import omis.boardhearingdecision.domain.HearingDecisionNote;
import omis.boardhearingdecision.service.BoardHearingDecisionService;
import omis.boardhearingdecision.service.delegate.BoardHearingDecisionCategoryDelegate;
import omis.boardhearingdecision.service.delegate.BoardHearingDecisionDelegate;
import omis.boardhearingdecision.service.delegate.HearingDecisionNoteDelegate;
import omis.country.domain.Country;
import omis.country.service.delegate.CountryDelegate;
import omis.datatype.DateRange;
import omis.exception.DuplicateEntityFoundException;
import omis.location.domain.Location;
import omis.location.service.delegate.LocationDelegate;
import omis.offender.domain.Offender;
import omis.offender.service.delegate.OffenderDelegate;
import omis.organization.domain.Organization;
import omis.organization.service.delegate.OrganizationDelegate;
import omis.paroleboarditinerary.domain.ParoleBoardItinerary;
import omis.paroleboarditinerary.service.delegate.ParoleBoardItineraryDelegate;
import omis.paroleboardlocation.domain.ParoleBoardLocation;
import omis.paroleboardlocation.service.delegate.ParoleBoardLocationDelegate;
import omis.paroleeligibility.domain.ParoleEligibility;
import omis.paroleeligibility.service.delegate.ParoleEligibilityDelegate;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.region.service.delegate.CityDelegate;
import omis.region.service.delegate.StateDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.util.PropertyValueAsserter;

/**
 * Tests method to create hearing decision notes.
 *
 * @author Josh Divine
 * @version 0.1.3 (Apr 18, 2018)
 * @since OMIS 3.0
 */
@Test
public class BoardHearingDecisionServiceCreateHearingDecisionNoteTests
	extends AbstractHibernateTransactionalTestNGSpringContextTests {

	/* Delegates. */

	@Autowired
	@Qualifier("offenderDelegate")
	private OffenderDelegate offenderDelegate;
	
	@Autowired
	@Qualifier("paroleEligibilityDelegate")
	private ParoleEligibilityDelegate paroleEligibilityDelegate;
	
	@Autowired
	@Qualifier("boardHearingDelegate")
	private BoardHearingDelegate boardHearingDelegate;
	
	@Autowired
	@Qualifier("boardHearingDecisionCategoryDelegate")
	private BoardHearingDecisionCategoryDelegate 
			boardHearingDecisionCategoryDelegate;
	
	@Autowired
	@Qualifier("boardHearingDecisionDelegate")
	private BoardHearingDecisionDelegate boardHearingDecisionDelegate;
	
	@Autowired
	@Qualifier("hearingDecisionNoteDelegate")
	private HearingDecisionNoteDelegate hearingDecisionNoteDelegate;
	
	@Autowired
	private LocationDelegate locationDelegate;
	
	@Autowired
	@Qualifier("organizationDelegate")
	private OrganizationDelegate organizationDelegate;
	
	@Autowired
	@Qualifier("addressDelegate")
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
	private ParoleBoardItineraryDelegate paroleBoardItineraryDelegate;
	
	@Autowired
	private ParoleBoardLocationDelegate paroleBoardLocationDelegate;
	
	/* Services. */

	@Autowired
	@Qualifier("boardHearingDecisionService")
	private BoardHearingDecisionService boardHearingDecisionService;

	/* Test methods. */

	/**
	 * Tests the creation of hearing decision notes.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	@Test
	public void testCreateHearingDecisionNote() 
			throws DuplicateEntityFoundException {
		// Arrangements
		Offender offender = this.offenderDelegate.createWithoutIdentity("Smith",
				"John", "Jay", null);
		ParoleEligibility paroleEligibility = this.paroleEligibilityDelegate
				.create(offender, this.parseDateText("01/01/2018"), null, null,
						null);
		Organization organization = this.organizationDelegate.create(
				"Organization", "org", null);
		Country country = this.countryDelegate.create(
				"Country", "USA", true);
		State state = this.stateDelegate.create(
				"State", "ST", country, true, true);
		City city = this.cityDelegate.create(
				"City", true, state, country);
		ZipCode zipCode = this.zipCodeDelegate.create(
				city, "12345", null, true);
		Address address = this.addressDelegate.findOrCreate("123", "321",
				null, null, zipCode);
		Location location = this.locationDelegate.create(organization,
				new DateRange(this.parseDateText("01/01/2001"),
						this.parseDateText("01/01/2020")), address);
		ParoleBoardLocation paroleBoardLocation =
				this.paroleBoardLocationDelegate.create(location, true);
		ParoleBoardItinerary itinerary =
				this.paroleBoardItineraryDelegate.create(paroleBoardLocation,
						true, this.parseDateText("01/01/2015"), 
						this.parseDateText("01/01/2015"));
		BoardHearing hearing = this.boardHearingDelegate.create(itinerary, null, 
				paroleEligibility, null, null, false);
		BoardHearingDecisionCategory category = this
				.boardHearingDecisionCategoryDelegate.create("Category", 
						DecisionCategory.GRANT, true);
		BoardHearingDecision boardDecision = this.boardHearingDecisionDelegate
				.create(hearing, category);
		Date date = this.parseDateText("01/01/2018");
		String description = "Description";

		// Action
		HearingDecisionNote hearingDecisionNote = this
				.boardHearingDecisionService.createHearingDecisionNote(
						boardDecision, date, description);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("boardDecision", boardDecision)
			.addExpectedValue("date", date)
			.addExpectedValue("description", description)
			.performAssertions(hearingDecisionNote);
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
		Offender offender = this.offenderDelegate.createWithoutIdentity("Smith",
				"John", "Jay", null);
		ParoleEligibility paroleEligibility = this.paroleEligibilityDelegate
				.create(offender, this.parseDateText("01/01/2018"), null, null,
						null);
		Organization organization = this.organizationDelegate.create(
				"Organization", "org", null);
		Country country = this.countryDelegate.create(
				"Country", "USA", true);
		State state = this.stateDelegate.create(
				"State", "ST", country, true, true);
		City city = this.cityDelegate.create(
				"City", true, state, country);
		ZipCode zipCode = this.zipCodeDelegate.create(
				city, "12345", null, true);
		Address address = this.addressDelegate.findOrCreate("123", "321",
				null, null, zipCode);
		Location location = this.locationDelegate.create(organization,
				new DateRange(this.parseDateText("01/01/2001"),
						this.parseDateText("01/01/2020")), address);
		ParoleBoardLocation paroleBoardLocation =
				this.paroleBoardLocationDelegate.create(location, true);
		ParoleBoardItinerary itinerary =
				this.paroleBoardItineraryDelegate.create(paroleBoardLocation,
						true, this.parseDateText("01/01/2015"), 
						this.parseDateText("01/01/2015"));
		BoardHearing hearing = this.boardHearingDelegate.create(itinerary, null, 
				paroleEligibility, null, null, false);
		BoardHearingDecisionCategory category = this
				.boardHearingDecisionCategoryDelegate.create("Category", 
						DecisionCategory.GRANT, true);
		BoardHearingDecision boardDecision = this.boardHearingDecisionDelegate
				.create(hearing, category);
		Date date = this.parseDateText("01/01/2018");
		String description = "Description";
		this.hearingDecisionNoteDelegate.create(boardDecision, date, 
				description);
		
		// Action
		this.boardHearingDecisionService.createHearingDecisionNote(
						boardDecision, date, description);
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