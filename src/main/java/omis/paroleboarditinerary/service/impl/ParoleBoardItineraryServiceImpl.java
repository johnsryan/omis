package omis.paroleboarditinerary.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import omis.exception.DuplicateEntityFoundException;
import omis.facility.domain.Facility;
import omis.facility.domain.Unit;
import omis.facility.service.delegate.FacilityDelegate;
import omis.facility.service.delegate.UnitDelegate;
import omis.location.domain.Location;
import omis.location.service.delegate.LocationDelegate;
import omis.paroleboarditinerary.domain.AttendeeRoleCategory;
import omis.paroleboarditinerary.domain.BoardAttendee;
import omis.paroleboarditinerary.domain.BoardMeetingSite;
import omis.paroleboarditinerary.domain.ParoleBoardItinerary;
import omis.paroleboarditinerary.domain.ParoleBoardItineraryNote;
import omis.paroleboarditinerary.service.ParoleBoardItineraryService;
import omis.paroleboarditinerary.service.delegate.BoardAttendeeDelegate;
import omis.paroleboarditinerary.service.delegate.BoardMeetingSiteDelegate;
import omis.paroleboarditinerary.service.delegate.ParoleBoardItineraryDelegate;
import omis.paroleboarditinerary.service.delegate.ParoleBoardItineraryNoteDelegate;
import omis.paroleboardlocation.domain.ParoleBoardLocation;
import omis.paroleboardlocation.service.delegate.ParoleBoardLocationDelegate;
import omis.paroleboardmember.domain.ParoleBoardMember;
import omis.paroleboardmember.service.delegate.ParoleBoardMemberDelegate;

/**
 * Implementation of service for parole board itinerary.
 * 
 * @author Josh Divine
 * @author Annie Wahl 
 * @version 0.1.3 (Apr 18, 2018)
 * @since OMIS 3.0
 */
public class ParoleBoardItineraryServiceImpl 
		implements ParoleBoardItineraryService {
	
	/* Delegates. */
	
	private final ParoleBoardItineraryDelegate paroleBoardItineraryDelegate;
	
	private final ParoleBoardItineraryNoteDelegate 
			paroleBoardItineraryNoteDelegate;
	
	private final BoardAttendeeDelegate boardAttendeeDelegate;
	
	private final BoardMeetingSiteDelegate boardMeetingSiteDelegate;

	private final LocationDelegate locationDelegate;
	
	private final ParoleBoardMemberDelegate paroleBoardMemberDelegate;
	
	private final ParoleBoardLocationDelegate paroleBoardLocationDelegate;
	
	private final FacilityDelegate facilityDelegate;
	
	private final UnitDelegate unitDelegate;
	
	/**
	 * Instantiates a parole board itinerary service implementation with the 
	 * specified delegates.
	 * 
	 * @param paroleBoardItineraryDelegate parole board itinerary delegate
	 * @param paroleBoardItineraryNoteDelegate parole board itinerary note 
	 * delegate
	 * @param boardAttendeeDelegate board attendee delegate
	 * @param boardMeetingSiteDelegate board meeting site delegate
	 * @param locationDelegate location delegate
	 * @param paroleBoardMemberDelegate parole board member delegate
	 * @param paroleBoardLocationDelegate parole board location delegate
	 */
	public ParoleBoardItineraryServiceImpl(
			final ParoleBoardItineraryDelegate paroleBoardItineraryDelegate,
			final ParoleBoardItineraryNoteDelegate 
					paroleBoardItineraryNoteDelegate,
			final BoardAttendeeDelegate boardAttendeeDelegate,
			final BoardMeetingSiteDelegate boardMeetingSiteDelegate,
			final LocationDelegate locationDelegate,
			final ParoleBoardMemberDelegate paroleBoardMemberDelegate,
			final ParoleBoardLocationDelegate paroleBoardLocationDelegate,
			final FacilityDelegate facilityDelegate,
			final UnitDelegate unitDelegate) {
		this.paroleBoardItineraryDelegate = paroleBoardItineraryDelegate;
		this.paroleBoardItineraryNoteDelegate = 
				paroleBoardItineraryNoteDelegate;
		this.boardAttendeeDelegate = boardAttendeeDelegate;
		this.boardMeetingSiteDelegate = boardMeetingSiteDelegate;
		this.locationDelegate = locationDelegate;
		this.paroleBoardMemberDelegate = paroleBoardMemberDelegate;
		this.paroleBoardLocationDelegate = paroleBoardLocationDelegate;
		this.facilityDelegate = facilityDelegate;
		this.unitDelegate = unitDelegate;
	}
	
	/** {@inheritDoc} */
	@Override
	public ParoleBoardItinerary create(
			final ParoleBoardLocation paroleBoardLocation, final Boolean onsite,
			final Date startDate, final Date endDate) 
					throws DuplicateEntityFoundException {
		return this.paroleBoardItineraryDelegate.create(
				paroleBoardLocation, onsite, startDate, endDate);
	}

	/** {@inheritDoc} */
	@Override
	public ParoleBoardItinerary update(
			final ParoleBoardItinerary paroleBoardItinerary, 
			final ParoleBoardLocation paroleBoardLocation, final Boolean onsite,
			final Date startDate, final Date endDate) 
					throws DuplicateEntityFoundException {
		return this.paroleBoardItineraryDelegate.update(paroleBoardItinerary, 
				paroleBoardLocation, onsite, startDate, endDate);
	}

	/** {@inheritDoc} */
	@Override
	public void remove(final ParoleBoardItinerary paroleBoardItinerary) {
		for (BoardAttendee boardAttendee
				: this.findBoardAttendeesByBoardItinerary(
						paroleBoardItinerary)) {
			this.removeAttendee(boardAttendee);
		}
		if (this.findBoardAlternateAttendeeByBoardItinerary(
				paroleBoardItinerary) != null) {
			this.removeAttendee(this.findBoardAlternateAttendeeByBoardItinerary(
					paroleBoardItinerary));
		}
		for (BoardMeetingSite boardMeetingSite
				: this.findBoardMeetingSitesByBoardItinerary(
						paroleBoardItinerary)) {
			this.removeBoardMeetingSite(boardMeetingSite);
		}
		this.paroleBoardItineraryDelegate.remove(paroleBoardItinerary);
	}

	/** {@inheritDoc} */
	@Override
	public List<Location> findJailLocations() {
		return this.locationDelegate.findJailLocations();
	}

	/** {@inheritDoc} */
	@Override
	public List<Location> findPrereleaseLocations() {
		return this.locationDelegate.findPrereleaseLocations();
	}

	/** {@inheritDoc} */
	@Override
	public List<Location> findFacilityLocations() {
		return this.locationDelegate.findFacilityLocations();
	}

	/** {@inheritDoc} */
	@Override
	public List<Location> findCommunitySupervisionOfficeLocations() {
		return this.locationDelegate.findCommunitySupervisionOfficeLocations();
	}

	/** {@inheritDoc} */
	@Override
	public List<Location> findTreatmentAndSactionCenterLocations() {
		return this.locationDelegate.findTreatmentAndSactionCenterLocations();
	}

	/** {@inheritDoc} */
	@Override
	public List<Location> findAllBoardItineraryLocations() {
		return this.locationDelegate.findAllBoardItineraryLocations();
	}

	/** {@inheritDoc} */
	@Override
	public BoardAttendee createAttendee(
			final ParoleBoardItinerary boardItinerary, 
			final ParoleBoardMember boardMember, final Long number,
			final AttendeeRoleCategory role) 
					throws DuplicateEntityFoundException {
		return this.boardAttendeeDelegate.create(boardItinerary, boardMember,
				number, role);
	}

	/** {@inheritDoc} */
	@Override
	public BoardAttendee updateAttendee(final BoardAttendee boardAttendee, 
			final ParoleBoardMember boardMember, 
			final AttendeeRoleCategory role) 
					throws DuplicateEntityFoundException {
		return this.boardAttendeeDelegate.update(boardAttendee, 
				boardAttendee.getBoardItinerary(), boardMember, 
				boardAttendee.getNumber(), role);
	}

	/** {@inheritDoc} */
	@Override
	public void removeAttendee(final BoardAttendee boardAttendee) {
		this.boardAttendeeDelegate.remove(boardAttendee);
	}

	/** {@inheritDoc} */
	@Override
	public BoardMeetingSite createBoardMeetingSite(
			final ParoleBoardItinerary boardItinerary, final Location location, 
			final Unit unit, final Date date, final Integer order) 
					throws DuplicateEntityFoundException {
		return this.boardMeetingSiteDelegate.create(boardItinerary, location,
				unit, date, order);
	}

	/** {@inheritDoc} */
	@Override
	public BoardMeetingSite updateBoardMeetingSite(
			final BoardMeetingSite boardMeetingSite, final Location location, 
			final Unit unit, final Date date, final Integer order) 
					throws DuplicateEntityFoundException {
		return this.boardMeetingSiteDelegate.update(boardMeetingSite, 
				boardMeetingSite.getBoardItinerary(), location, unit, date, 
				order);
	}

	/** {@inheritDoc} */
	@Override
	public void removeBoardMeetingSite(
			final BoardMeetingSite boardMeetingSite) {
		this.boardMeetingSiteDelegate.remove(boardMeetingSite);
	}

	/** {@inheritDoc} */
	@Override
	public ParoleBoardItineraryNote createBoardIteneraryNote(
			final ParoleBoardItinerary boardItinerary, final String description,
			final Date date) throws DuplicateEntityFoundException {
		return this.paroleBoardItineraryNoteDelegate.create(boardItinerary, 
				description, date);
	}

	/** {@inheritDoc} */
	@Override
	public ParoleBoardItineraryNote updateBoardItineraryNote(
			final ParoleBoardItineraryNote itineraryNote, 
			final String description, final Date date) 
					throws DuplicateEntityFoundException {
		return this.paroleBoardItineraryNoteDelegate.update(itineraryNote, 
				itineraryNote.getBoardItinerary(), description, date);
	}

	/** {@inheritDoc} */
	@Override
	public void removeBoardItineraryNote(
			final ParoleBoardItineraryNote itineraryNote) {
		this.paroleBoardItineraryNoteDelegate.remove(itineraryNote);
	}

	/** {@inheritDoc} */
	@Override
	public List<BoardAttendee> findBoardAttendeesByBoardItinerary(
			final ParoleBoardItinerary boardItinerary) {
		return this.boardAttendeeDelegate.findBoardAttendeesByBoardItinerary(
				boardItinerary);
	}

	/** {@inheritDoc} */
	@Override
	public BoardAttendee findBoardAlternateAttendeeByBoardItinerary(
			final ParoleBoardItinerary boardItinerary) {
		return this.boardAttendeeDelegate
				.findBoardAlternateAttendeeByBoardItinerary(boardItinerary);
	}

	/** {@inheritDoc} */
	@Override
	public List<BoardMeetingSite> findBoardMeetingSitesByBoardItinerary(
			final ParoleBoardItinerary boardItinerary) {
		return this.boardMeetingSiteDelegate
				.findBoardMeetingSitesByBoardItinerary(boardItinerary);
	}

	/** {@inheritDoc} */
	@Override
	public List<ParoleBoardItineraryNote> findItineraryNotesByBoardItinerary(
			final ParoleBoardItinerary boardItinerary) {
		return this.paroleBoardItineraryNoteDelegate
				.findItineraryNotesByBoardItinerary(boardItinerary);
	}

	/** {@inheritDoc} */
	@Override
	public List<ParoleBoardMember> findBoardMembersByDate(
			final Date effectiveDate) {
		return this.paroleBoardMemberDelegate
				.findBoardMembersByDate(effectiveDate);
	}
	
	/**{@inheritDoc} */
	@Override
	public List<ParoleBoardLocation> findParoleBoardLocations() {
		return this.paroleBoardLocationDelegate.findAll();
	}

	/** {@inheritDoc} */
	@Override
	public List<Unit> findUnitsByLocation(final Location location) {
		Facility facility = this.facilityDelegate.findByLocation(location);
		if (facility != null) {
			return this.unitDelegate.findByFacility(facility);
		} else {
			return new ArrayList<Unit>();
		}
	}
}