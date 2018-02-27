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
package omis.hearingparticipant.service.testng;

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
import omis.country.domain.Country;
import omis.country.service.delegate.CountryDelegate;
import omis.datatype.DateRange;
import omis.exception.DuplicateEntityFoundException;
import omis.hearingparticipant.dao.HearingParticipantDao;
import omis.hearingparticipant.domain.HearingParticipant;
import omis.hearingparticipant.domain.HearingParticipantCategory;
import omis.hearingparticipant.domain.HearingParticipantIntentCategory;
import omis.hearingparticipant.domain.HearingParticipantNote;
import omis.hearingparticipant.service.HearingParticipantService;
import omis.hearingparticipant.service.delegate.HearingParticipantDelegate;
import omis.hearingparticipant.service.delegate.HearingParticipantIntentCategoryDelegate;
import omis.hearingparticipant.service.delegate.HearingParticipantNoteDelegate;
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
import omis.person.domain.Person;
import omis.person.service.delegate.PersonDelegate;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.region.service.delegate.CityDelegate;
import omis.region.service.delegate.StateDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;

/**
 * Hearing Participant Service Remove Tests.
 * 
 *@author Annie Wahl 
 *@version 0.1.0 (Jan 29, 2018)
 *@since OMIS 3.0
 *
 */
public class HearingParticipantServiceRemoveTests
		extends AbstractHibernateTransactionalTestNGSpringContextTests {

	@Autowired
	@Qualifier("hearingParticipantService")
	private HearingParticipantService hearingParticipantService;
	
	@Autowired
	private ParoleEligibilityDelegate paroleEligibilityDelegate;
	
	@Autowired
	private ParoleBoardItineraryDelegate paroleBoardItineraryDelegate;
	
	@Autowired
	private ParoleBoardLocationDelegate paroleBoardLocationDelegate;
	
	@Autowired
	private BoardHearingDelegate boardHearingDelegate;
	
	@Autowired
	private HearingParticipantIntentCategoryDelegate
		hearingParticipantIntentCategoryDelegate;
	
	@Autowired
	private HearingParticipantDelegate hearingParticipantDelegate;
	
	@Autowired
	private HearingParticipantDao hearingParticipantDao;
	
	@Autowired
	private HearingParticipantNoteDelegate hearingParticipantNoteDelegate;
	
	@Autowired
	private OffenderDelegate offenderDelegate;

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
	private PersonDelegate personDelegate;
	
	/**
	 * Tests removing a Hearing Participant.
	 * @throws DuplicateEntityFoundException - When a duplicate entity it found
	 */
	@Test
	public void testHearingParticipantRemove()
			throws DuplicateEntityFoundException {
		final BoardHearing boardHearing = this.createBoardHearing();
		final Person person = this.personDelegate.create("McBeans", "Coffee",
				"Java", null);
		final HearingParticipantCategory category =
				HearingParticipantCategory.FAMILY;
		final Boolean boardApproved = true;
		final Boolean witness = false;
		final Boolean facilityApproved = true;
		final HearingParticipantIntentCategory intent =
				this.hearingParticipantIntentCategoryDelegate.create(
						"Intent", true);
		final String comments = "Participant Comments";
		final HearingParticipant hearingParticipant =
				this.hearingParticipantDelegate.create(
						boardHearing, person, category, boardApproved, witness,
						facilityApproved, intent, comments);
		
		this.hearingParticipantService.removeHearingParticipant(
				hearingParticipant);
		
		assert !this.hearingParticipantDao.findAll()
			.contains(hearingParticipant)
			: "Board Hearing Participant was not removed.";
	}
	
	/**
	 * Tests removing a Hearing Participant Note.
	 * @throws DuplicateEntityFoundException - When a duplicate entity it found
	 */
	@Test
	public void testHearingParticipantNoteRemove()
			throws DuplicateEntityFoundException {
		final BoardHearing boardHearing = this.createBoardHearing();
		final Person person = this.personDelegate.create("McBeans", "Coffee",
				"Java", null);
		final HearingParticipant hearingParticipant =
				this.hearingParticipantDelegate.create(
						boardHearing, person, HearingParticipantCategory.FAMILY,
						true, false, true, null, null);
		final String description = "Note Description";
		final Date date = this.parseDateText("01/01/2018");
		final HearingParticipantNote hearingParticipantNote =
				this.hearingParticipantNoteDelegate.create(
						hearingParticipant, description, date);
		
		this.hearingParticipantService.removeHearingParticipantNote(
				hearingParticipantNote);
		
		assert !this.hearingParticipantNoteDelegate
			.findByHearingParticipant(hearingParticipant)
			.contains(hearingParticipantNote)
			: "Hearing Participant Note was not removed.";
	}
	
	private BoardHearing createBoardHearing()
			throws DuplicateEntityFoundException {
		final Offender offender = this.offenderDelegate.createWithoutIdentity(
				"Wayne", "Bruce", "Alen", null);
		final ParoleEligibility paroleEligibility =
				this.paroleEligibilityDelegate.create(offender,
						this.parseDateText("01/01/2017"), null, null, null);
		final Organization organization = this.organizationDelegate.create(
				"Organization", "org", null);
		final Country country = this.countryDelegate.create(
				"Country", "USA", true);
		final State state = this.stateDelegate.create(
				"State", "ST", country, true, true);
		final City city = this.cityDelegate.create(
				"City", true, state, country);
		final ZipCode zipCode = this.zipCodeDelegate.create(
				city, "12345", null, true);
		final Address address = this.addressDelegate.findOrCreate("123", "321",
				null, null, zipCode);
		final Location location = this.locationDelegate.create(organization,
				new DateRange(this.parseDateText("01/01/2001"),
						this.parseDateText("01/01/2020")), address);
		final ParoleBoardLocation paroleBoardLocation =
				this.paroleBoardLocationDelegate.create(location, true);
		final ParoleBoardItinerary itinerary =
				this.paroleBoardItineraryDelegate.create(paroleBoardLocation,
						this.parseDateText("01/01/2015"), null);
		return this.boardHearingDelegate
				.create(itinerary, null, null, paroleEligibility, null, null,
						false);
	}
	
	private Date parseDateText(final String text) {
		try {
			return new SimpleDateFormat("MM/dd/yyyy").parse(text);
		} catch (ParseException e) {
			throw new RuntimeException("Parse error", e);
		}
	}
}
