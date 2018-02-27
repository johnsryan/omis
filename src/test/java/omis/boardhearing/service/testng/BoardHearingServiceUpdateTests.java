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
package omis.boardhearing.service.testng;

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
import omis.boardhearing.domain.BoardHearingCategory;
import omis.boardhearing.domain.BoardHearingNote;
import omis.boardhearing.domain.BoardHearingParticipant;
import omis.boardhearing.domain.CancellationCategory;
import omis.boardhearing.service.BoardHearingService;
import omis.boardhearing.service.delegate.BoardHearingCategoryDelegate;
import omis.boardhearing.service.delegate.BoardHearingDelegate;
import omis.boardhearing.service.delegate.BoardHearingNoteDelegate;
import omis.boardhearing.service.delegate.BoardHearingParticipantDelegate;
import omis.country.domain.Country;
import omis.country.service.delegate.CountryDelegate;
import omis.datatype.DateRange;
import omis.exception.DateConflictException;
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
import omis.paroleboardmember.domain.ParoleBoardMember;
import omis.paroleboardmember.service.delegate.ParoleBoardMemberDelegate;
import omis.paroleeligibility.domain.ParoleEligibility;
import omis.paroleeligibility.service.delegate.ParoleEligibilityDelegate;
import omis.person.domain.Person;
import omis.person.service.delegate.PersonDelegate;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.region.service.delegate.CityDelegate;
import omis.region.service.delegate.StateDelegate;
import omis.staff.domain.StaffAssignment;
import omis.staff.domain.StaffTitle;
import omis.staff.service.delegate.StaffAssignmentDelegate;
import omis.staff.service.delegate.StaffTitleDelegate;
import omis.supervision.domain.SupervisoryOrganization;
import omis.supervision.service.delegate.SupervisoryOrganizationDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.util.PropertyValueAsserter;

/**
 * Board Hearing Service Update Tests.
 * 
 *@author Annie Wahl 
 *@version 0.1.0 (Jan 12, 2018)
 *@since OMIS 3.0
 *
 */
public class BoardHearingServiceUpdateTests
		extends AbstractHibernateTransactionalTestNGSpringContextTests {
	@Autowired
	@Qualifier("boardHearingService")
	private BoardHearingService boardHearingService;
	
	@Autowired
	private LocationDelegate locationDelegate;
	
	@Autowired
	private ParoleEligibilityDelegate paroleEligibilityDelegate;
	
	@Autowired
	@Qualifier("organizationDelegate")
	private OrganizationDelegate organizationDelegate;
	
	@Autowired
	@Qualifier("addressDelegate")
	private AddressDelegate addressDelegate;
	
	@Autowired
	private CountryDelegate countryDelegate;
	
	@Autowired
	private ParoleBoardItineraryDelegate paroleBoardItineraryDelegate;
	
	@Autowired
	private ParoleBoardLocationDelegate paroleBoardLocationDelegate;
	
	@Autowired
	private BoardHearingCategoryDelegate boardHearingCategoryDelegate;
	
	@Autowired
	private StateDelegate stateDelegate;
	
	@Autowired
	private CityDelegate cityDelegate;
	
	@Autowired
	private ZipCodeDelegate zipCodeDelegate;
	
	@Autowired
	private ParoleBoardMemberDelegate paroleBoardMemberDelegate;
	
	@Autowired
	@Qualifier("staffAssignmentDelegate")
	private StaffAssignmentDelegate staffAssignmentDelegate;
	
	@Autowired
	@Qualifier("personDelegate")
	private PersonDelegate personDelegate;
	
	@Autowired
	@Qualifier("supervisoryOrganizationDelegate")
	private SupervisoryOrganizationDelegate supervisoryOrganizationDelegate;
	
	@Autowired
	@Qualifier("staffTitleDelegate")
	private StaffTitleDelegate staffTitleDelegate;
	
	@Autowired
	@Qualifier("offenderDelegate")
	private OffenderDelegate offenderDelegate;
	
	@Autowired
	@Qualifier("boardHearingDelegate")
	private BoardHearingDelegate boardHearingDelegate;
	
	@Autowired
	@Qualifier("boardHearingNoteDelegate")
	private BoardHearingNoteDelegate boardHearingNoteDelegate;
	
	@Autowired
	@Qualifier("boardHearingParticipantDelegate")
	private BoardHearingParticipantDelegate boardHearingParticipantDelegate;
	
	/**
	 * Tests Board Hearing update.
	 * @throws DuplicateEntityFoundException - When a duplicate entity it found
	 */
	@Test
	public void testBoardHearingUpdate() throws DuplicateEntityFoundException {
		final Offender offender = this.offenderDelegate.createWithoutIdentity(
				"Wayne", "Bruce", "Alen", null);
		final ParoleEligibility paroleEligibility =
				this.paroleEligibilityDelegate.create(offender,
						this.parseDateText("01/01/2017"), null, null, null);
		final Organization organization = this.organizationDelegate.create(
				"Organization", "org", null);
		final Organization organization2 = this.organizationDelegate.create(
				"Organization2", "org2", null);
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
		final Location locationOld = this.locationDelegate.create(organization2,
						new DateRange(this.parseDateText("01/01/2001"),
								this.parseDateText("01/01/2020")), address);
		final ParoleBoardLocation paroleBoardLocationOld =
				this.paroleBoardLocationDelegate.create(locationOld, true);
		final ParoleBoardItinerary itineraryOld =
				this.paroleBoardItineraryDelegate.create(paroleBoardLocationOld,
						this.parseDateText("01/01/2015"), null);
		final BoardHearingCategory categoryOld =
				this.boardHearingCategoryDelegate
				.create("Hearing Category Ooooold", true);
		final Boolean videoConferenceOld = false;
		final Location location = this.locationDelegate.create(organization,
				new DateRange(this.parseDateText("01/01/2001"),
						this.parseDateText("01/01/2020")), address);
		final Date hearingDate = this.parseDateText("12/12/2020");
		final CancellationCategory cancellation = CancellationCategory.WAIVED;
		final BoardHearingCategory category = this.boardHearingCategoryDelegate
				.create("Hearing Category", true);
		final ParoleBoardLocation paroleBoardLocation =
				this.paroleBoardLocationDelegate.create(location, true);
		final ParoleBoardItinerary itinerary =
				this.paroleBoardItineraryDelegate.create(paroleBoardLocation,
						this.parseDateText("01/01/2015"), null);
		final Boolean videoConference = true;
		
		BoardHearing boardHearing = this.boardHearingDelegate.create(
				itineraryOld, locationOld, this.parseDateText("02/05/2015"),
				paroleEligibility, categoryOld, null, videoConferenceOld);
		
		boardHearing = this.boardHearingService
				.updateBoardHearing(boardHearing, itinerary, location,
						hearingDate, paroleEligibility, category,
						cancellation, videoConference);
		
		PropertyValueAsserter.create()
			.addExpectedValue("itinerary", itinerary)
			.addExpectedValue("location", location)
			.addExpectedValue("hearingDate", hearingDate)
			.addExpectedValue("paroleEligibility", paroleEligibility)
			.addExpectedValue("category", category)
			.addExpectedValue("cancellation", cancellation)
			.addExpectedValue("videoConference", videoConference)
			.performAssertions(boardHearing);
	}
	
	/**
	 * Tests Board Hearing Note update.
	 * @throws DuplicateEntityFoundException - When a duplicate entity it found
	 */
	@Test
	public void testBoardHearingNoteUpdate()
			throws DuplicateEntityFoundException {
		final BoardHearing hearing = this.createBoardHearing();
		final Date date = this.parseDateText("02/01/2018");
		final String description = "Note Description";
		BoardHearingNote boardHearingNote = this.boardHearingNoteDelegate
				.create(hearing, "Old Drescription",
						this.parseDateText("01/25/2017"));
		
		boardHearingNote = this.boardHearingService
				.updateBoardHearingNote(boardHearingNote, description, date);
		
		PropertyValueAsserter.create()
			.addExpectedValue("hearing", hearing)
			.addExpectedValue("date", date)
			.addExpectedValue("description", description)
			.performAssertions(boardHearingNote);
	}
	
	/**
	 * Tests Board Hearing Participant update.
	 * @throws DuplicateEntityFoundException - When a duplicate entity it found
	 * @throws DateConflictException - Date conflict exception.
	 */
	@Test
	public void testBoardHearingParticipantUpdate()
			throws DuplicateEntityFoundException, DateConflictException {
		final BoardHearing hearing = this.createBoardHearing();
		final Person staffMemberOld = this.personDelegate.create(
				"Hancock", "Alfred", "Michael", null);
		final Person staffMember = this.personDelegate.create("Smith", "John",
				"Jay", null);
		final SupervisoryOrganization supervisoryOrganization = this
				.supervisoryOrganizationDelegate.create("SupOrg", "SO", null);
		final StaffTitle title = this.staffTitleDelegate.create("Title",
				(short) 1, true);
		final StaffAssignment staffAssignmentOld = this.staffAssignmentDelegate
				.create(staffMemberOld, supervisoryOrganization, null,
				new DateRange(this.parseDateText("01/01/2015"), null), title,
				true, null, null, null, null, null);
		final ParoleBoardMember boardMemberOld = this.paroleBoardMemberDelegate
				.create(staffAssignmentOld, new DateRange(
						this.parseDateText("01/01/2015"), null));
		final StaffAssignment staffAssignment = this.staffAssignmentDelegate
				.create(staffMember, supervisoryOrganization, null,
				new DateRange(this.parseDateText("01/01/2015"), null), title,
				true, null, null, null, null, null);
		final ParoleBoardMember boardMember = this.paroleBoardMemberDelegate
				.create(staffAssignment, new DateRange(
						this.parseDateText("01/01/2015"), null));
		final Long number = (long) 1;
		BoardHearingParticipant boardHearingParticipant =
				this.boardHearingParticipantDelegate.create(
						hearing, boardMemberOld, (long) 2);
		
		boardHearingParticipant =
				this.boardHearingService.updateBoardHearingParticipant(
						boardHearingParticipant, hearing, boardMember, number);
		
		PropertyValueAsserter.create()
			.addExpectedValue("hearing", hearing)
			.addExpectedValue("boardMember", boardMember)
			.addExpectedValue("number", number)
			.performAssertions(boardHearingParticipant);
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
