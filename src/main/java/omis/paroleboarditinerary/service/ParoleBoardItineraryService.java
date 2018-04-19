package omis.paroleboarditinerary.service;

import java.util.Date;
import java.util.List;

import omis.exception.DuplicateEntityFoundException;
import omis.facility.domain.Unit;
import omis.location.domain.Location;
import omis.paroleboarditinerary.domain.AttendeeRoleCategory;
import omis.paroleboarditinerary.domain.BoardAttendee;
import omis.paroleboarditinerary.domain.BoardMeetingSite;
import omis.paroleboarditinerary.domain.ParoleBoardItinerary;
import omis.paroleboarditinerary.domain.ParoleBoardItineraryNote;
import omis.paroleboardlocation.domain.ParoleBoardLocation;
import omis.paroleboardmember.domain.ParoleBoardMember;

/**
 * Service for parole board itinerary.
 * 
 * @author Josh Divine
 * @author Annie Wahl 
 * @version 0.1.3 (Apr 18, 2018)
 * @since OMIS 3.0
 */
public interface ParoleBoardItineraryService {

	/**
	 * Creates a new parole board itinerary.
	 * 
	 * @param paroleBoardLocation parole Board Location
	 * @param onsite on-site
	 * @param startDate start date
	 * @param endDate end date
	 * @return parole board itinerary
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	ParoleBoardItinerary create(ParoleBoardLocation paroleBoardLocation,
			Boolean onsite, Date startDate, Date endDate) 
					throws DuplicateEntityFoundException;
	
	/**
	 * Updates an existing parole board itinerary.
	 * 
	 * @param paroleBoardItinerary parole board itinerary
	 * @param paroleBoardLocation parole Board Location
	 * @param onsite on-site
	 * @param startDate start date
	 * @param endDate end date
	 * @return parole board itinerary
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	ParoleBoardItinerary update(ParoleBoardItinerary paroleBoardItinerary, 
			ParoleBoardLocation paroleBoardLocation, Boolean onsite,
			Date startDate, Date endDate) 
					throws DuplicateEntityFoundException;
	
	/**
	 * Removes the specified parole board itinerary.
	 * 
	 * @param paroleBoardItinerary parole board itinerary
	 */
	void remove(ParoleBoardItinerary paroleBoardItinerary);
	
	/**
	 * Returns a list of jail locations.
	 * 
	 * @return list of jail locations
	 */
	List<Location> findJailLocations();
	
	/**
	 * Returns a list of prerelease locations.
	 * 
	 * @return list of prerelease locations
	 */
	List<Location> findPrereleaseLocations();
	
	/**
	 * Returns a list of facility locations.
	 * 
	 * @return list of facility locations
	 */
	List<Location> findFacilityLocations();
	
	/**
	 * Returns a list of community supervision office locations.
	 * 
	 * @return list of community supervision office locations
	 */
	List<Location> findCommunitySupervisionOfficeLocations();
	
	/**
	 * Returns a list of treatment and sanction center locations.
	 * 
	 * @return list of treatment and sanction center locations
	 */
	List<Location> findTreatmentAndSactionCenterLocations();
	
	/**
	 * Returns a list of all board itinerary locations.
	 * 
	 * @return list of all board itinerary locations
	 */
	List<Location> findAllBoardItineraryLocations();
	
	/**
	 * Creates a new board attendee.
	 * 
	 * @param boardItinerary parole board itinerary
	 * @param boardMember parole board member
	 * @param number number
	 * @param role attendee role category 
	 * @return board attendee
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	BoardAttendee createAttendee(ParoleBoardItinerary boardItinerary, 
			ParoleBoardMember boardMember, Long number, 
			AttendeeRoleCategory role) 
					throws DuplicateEntityFoundException;
	
	/**
	 * Updates an existing board attendee.
	 * 
	 * @param boardAttendee board attendee
	 * @param boardMember parole board member
	 * @param role attendee role category
	 * @return board attendee
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	BoardAttendee updateAttendee(BoardAttendee boardAttendee, 
			ParoleBoardMember boardMember, AttendeeRoleCategory role) 
					throws DuplicateEntityFoundException;
	
	/**
	 * Removes the specified board attendee.
	 * 
	 * @param boardAttendee board attendee
	 */
	void removeAttendee(BoardAttendee boardAttendee);
	
	/**
	 * Creates a new board meeting site.
	 * 
	 * @param boardItinerary parole board itinerary
	 * @param location location
	 * @param unit unit
	 * @param date date
	 * @param order order
	 * @return board meeting site
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	BoardMeetingSite createBoardMeetingSite(ParoleBoardItinerary boardItinerary,
			Location location, Unit unit, Date date, Integer order) 
					throws DuplicateEntityFoundException;
	
	/**
	 * Updates an existing board meeting site.
	 * 
	 * @param boardMeetingSite board meeting site
	 * @param location location
	 * @param unit unit
	 * @param date date
	 * @param order order
	 * @return board meeting site
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	BoardMeetingSite updateBoardMeetingSite(BoardMeetingSite boardMeetingSite, 
			Location location, Unit unit, Date date, Integer order) 
					throws DuplicateEntityFoundException;
	
	/**
	 * Removes the specified board meeting site.
	 * 
	 * @param boardMeetingSite board meeting site
	 */
	void removeBoardMeetingSite(BoardMeetingSite boardMeetingSite);
	
	/**
	 * Creates a new parole board itinerary note.
	 * 
	 * @param boardItinerary parole board itinerary
	 * @param description description
	 * @param date date
	 * @return parole board itinerary note
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	ParoleBoardItineraryNote createBoardIteneraryNote(
			ParoleBoardItinerary boardItinerary, String description, Date date) 
					throws DuplicateEntityFoundException;
	
	/**
	 * Updates an existing parole board itinerary.
	 * 
	 * @param itineraryNote parole board itinerary note
	 * @param description description
	 * @param date date
	 * @return parole board itinerary note
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	ParoleBoardItineraryNote updateBoardItineraryNote(
			ParoleBoardItineraryNote itineraryNote, String description, 
			Date date) throws DuplicateEntityFoundException;
	
	/**
	 * Removes the specified parole board itinerary note.
	 * 
	 * @param itineraryNote parole board itinerary note
	 */
	void removeBoardItineraryNote(ParoleBoardItineraryNote itineraryNote);
	
	/**
	 * Returns a list of board attendees that match the specified parole 
	 * board itinerary.
	 * 
	 * @param boardItinerary parole board itinerary
	 * @return list of board attendees
	 */
	List<BoardAttendee> findBoardAttendeesByBoardItinerary(
			ParoleBoardItinerary boardItinerary);
	
	/**
	 * Returns the alternate board attendee for the specified parole board 
	 * itinerary.
	 * 
	 * @param boardItinerary parole board itinerary
	 * @return board attendee
	 */
	BoardAttendee findBoardAlternateAttendeeByBoardItinerary(
			ParoleBoardItinerary boardItinerary);
	
	/**
	 * Returns a list of board meeting sites that match the specified parole 
	 * board itinerary.
	 * 
	 * @param boardItinerary parole board itinerary
	 * @return list of board meeting sites
	 */
	List<BoardMeetingSite> findBoardMeetingSitesByBoardItinerary(
			ParoleBoardItinerary boardItinerary);
	
	/**
	 * Returns list of notes for the specified parole board itinerary.
	 * 
	 * @param boardItinerary parole board itinerary
	 * @return list of parole board itinerary notes
	 */
	List<ParoleBoardItineraryNote> findItineraryNotesByBoardItinerary(
			ParoleBoardItinerary boardItinerary);

	/**
	 * Returns a list of parole board members on the effective date.
	 * 
	 * @param effectiveDate effective date
	 * @return list of all parole board members
	 */
	List<ParoleBoardMember> findBoardMembersByDate(Date effectiveDate);
	
	/**
	 * Returns a list of all Parole Board Locations.
	 * 
	 * @return List of all Parole Board Locations.
	 */
	List<ParoleBoardLocation> findParoleBoardLocations();
	
	/**
	 * Returns a list of units for the specified location.
	 * 
	 * @param location location
	 * @return list of units
	 */
	List<Unit> findUnitsByLocation(Location location);
}