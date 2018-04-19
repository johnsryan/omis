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
import omis.boardhearing.dao.BoardHearingDao;
import omis.boardhearing.domain.BoardHearing;
import omis.boardhearing.domain.BoardHearingNote;
import omis.boardhearing.domain.BoardHearingParticipant;
import omis.boardhearing.domain.CancellationCategory;
import omis.boardhearing.service.BoardHearingService;
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

/**
 * Board Hearing Service Remove tests.
 * 
 * @author Annie Wahl 
 * @author Josh Divine
 * @version 0.1.3 (Apr 18, 2018)
 * @since OMIS 3.0
 */
public class BoardHearingServiceRemoveTests
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
	private ParoleBoardItineraryDelegate paroleBoardItineraryDelegate;
	
	@Autowired
	private ParoleBoardLocationDelegate paroleBoardLocationDelegate;
	
	@Autowired
	@Qualifier("boardHearingDelegate")
	private BoardHearingDelegate boardHearingDelegate;
	
	@Autowired
	@Qualifier("boardHearingDao")
	private BoardHearingDao boardHearingDao;
	
	@Autowired
	@Qualifier("boardHearingNoteDelegate")
	private BoardHearingNoteDelegate boardHearingNoteDelegate;
	
	@Autowired
	@Qualifier("boardHearingParticipantDelegate")
	private BoardHearingParticipantDelegate boardHearingParticipantDelegate;
	
	/**
	 * Tests removing a board hearing.
	 * @throws DuplicateEntityFoundException - When a duplicate entity it found
	 */
	@Test
	public void testBoardHearingRemove() throws DuplicateEntityFoundException {
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
		final Date hearingDate = this.parseDateText("12/12/2020");
		final CancellationCategory cancellation = CancellationCategory.WAIVED;
		final ParoleBoardLocation paroleBoardLocation =
				this.paroleBoardLocationDelegate.create(location, true);
		final ParoleBoardItinerary itinerary =
				this.paroleBoardItineraryDelegate.create(paroleBoardLocation,
						true, this.parseDateText("01/01/2015"), 
						this.parseDateText("01/01/2015"));
		final Boolean videoConference = true;
		final BoardHearing boardHearing = this.boardHearingDelegate.create(
				itinerary, hearingDate, paroleEligibility, null, cancellation,
				videoConference);
			
		
		this.boardHearingService.removeBoardHearing(boardHearing);
		
		assert !this.boardHearingDao.findAll().contains(boardHearing)
			: "Board Hearing was not removed.";
	}
	
	/**
	 * Tests removing a Board Hearing Note.
	 * @throws DuplicateEntityFoundException - When a duplicate entity it found
	 */
	@Test
	public void testBoardHearingNoteRemove()
			throws DuplicateEntityFoundException {
		final BoardHearing hearing = this.createBoardHearing();
		final Date date = this.parseDateText("02/01/2018");
		final String description = "Note Description";
		final BoardHearingNote boardHearingNote = this.boardHearingNoteDelegate
				.create(hearing, description, date);
		
		this.boardHearingService.removeBoardHearingNote(boardHearingNote);
		
		assert !this.boardHearingNoteDelegate.findByHearing(hearing)
			.contains(boardHearingNote) : "Board Hearing Note was not removed.";
	}
	
	/**
	 * Tests removing board hearing participant.
	 * @throws DuplicateEntityFoundException - When a duplicate entity it found
	 * @throws DateConflictException - Date Conflict Exception
	 */
	@Test
	public void testBoardHearingParticipantRemove()
			throws DuplicateEntityFoundException, DateConflictException {
		final BoardHearing hearing = this.createBoardHearing();
		final Person staffMember = this.personDelegate.create("Smith", "John",
				"Jay", null);
		final SupervisoryOrganization supervisoryOrganization = this
				.supervisoryOrganizationDelegate.create("SupOrg", "SO", null);
		final StaffTitle title = this.staffTitleDelegate.create("Title",
				(short) 1, true);
		final StaffAssignment staffAssignment = this.staffAssignmentDelegate
				.create(staffMember, supervisoryOrganization, null,
				new DateRange(this.parseDateText("01/01/2015"), null), title,
				true, null, null, null, null, null);
		final ParoleBoardMember boardMember = this.paroleBoardMemberDelegate
				.create(staffAssignment, new DateRange(
						this.parseDateText("01/01/2015"), null));
		final Long number = (long) 1;
		final BoardHearingParticipant boardHearingParticipant =
				this.boardHearingParticipantDelegate.create(
						hearing, boardMember, number);
		
		this.boardHearingService.removeBoardHearingParticipant(
				boardHearingParticipant);
		
		assert !this.boardHearingParticipantDelegate.findByHearing(hearing)
			.contains(boardHearingParticipant)
			: "Board Hearing Participant was not removed.";
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
						true, this.parseDateText("01/01/2015"), 
						this.parseDateText("01/01/2015"));
		return this.boardHearingDelegate.create(itinerary, null, 
				paroleEligibility, null, null, false);
	}
	
	private Date parseDateText(final String text) {
		try {
			return new SimpleDateFormat("MM/dd/yyyy").parse(text);
		} catch (ParseException e) {
			throw new RuntimeException("Parse error", e);
		}
	}
}