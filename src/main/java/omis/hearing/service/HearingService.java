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
package omis.hearing.service;

import java.util.Date;
import java.util.List;
import omis.condition.domain.Condition;
import omis.disciplinaryCode.domain.DisciplinaryCode;
import omis.exception.DuplicateEntityFoundException;
import omis.hearing.domain.Hearing;
import omis.hearing.domain.HearingCategory;
import omis.hearing.domain.HearingNote;
import omis.hearing.domain.HearingStatus;
import omis.hearing.domain.HearingStatusCategory;
import omis.hearing.domain.ImposedSanction;
import omis.hearing.domain.Infraction;
import omis.hearing.domain.InfractionPlea;
import omis.hearing.domain.UserAttendance;
import omis.hearing.domain.component.Resolution;
import omis.hearing.exception.HearingExistsException;
import omis.hearing.exception.HearingNoteExistsException;
import omis.hearing.exception.HearingStatusExistsException;
import omis.hearing.exception.InfractionExistsException;
import omis.hearing.exception.UserAttendanceExistsException;
import omis.location.domain.Location;
import omis.offender.domain.Offender;
import omis.supervision.domain.SupervisoryOrganization;
import omis.user.domain.UserAccount;
import omis.violationevent.domain.ConditionViolation;
import omis.violationevent.domain.DisciplinaryCodeViolation;
import omis.violationevent.domain.ViolationEvent;

/**
 * Hearing Service.
 * 
 * @author Annie Wahl
 * @author Josh Divine
 * @version 0.1.1 (May 3, 2018)
 * @since OMIS 3.0
 */
public interface HearingService {
	
	
	/**
	 * Creates a hearing with the specified parameters.
	 * @param location - Location
	 * @param offender - Offender
	 * @param inAttendance - Boolean
	 * @param date - Date
	 * @param category - HearingCategory
	 * @param officer user account
	 * @return Newly Created Hearing
	 * @throws HearingExistsException - when Hearing exists with
	 * specified offender, location, date, and officer
	 */
	Hearing createHearing(Location location, Offender offender,
			Boolean inAttendance, Date date, HearingCategory category,
			UserAccount officer)
			throws HearingExistsException;
	
	/**
	 * Updates a hearing with the specified parameters.
	 * @param hearing - Hearing to update
	 * @param location - Location
	 * @param inAttendance - Boolean
	 * @param date - Date
	 * @param category - HearingCategory
	 * @param officer user account
	 * @return Updated Hearing
	 * @throws HearingExistsException - when Hearing exists with
	 * specified offender, location, date, and officer
	 */
	Hearing updateHearing(Hearing hearing, Location location,
			Boolean inAttendance, Date date, HearingCategory category,
			UserAccount officer)
			throws HearingExistsException;
	
	/**
	 * Removes a hearing.
	 * @param hearing - Hearing to remove
	 */
	void removeHearing(Hearing hearing);
	
	/**
	 * Creates a hearing note with specified parameters.
	 * @param hearing - Hearing
	 * @param description - String
	 * @param date - Date
	 * @return newly created hearing note
	 * @throws HearingNoteExistsException - when Hearing Note already exists
	 * for specified hearing with given date and description
	 */
	HearingNote createHearingNote(Hearing hearing, String description,
			Date date) throws HearingNoteExistsException;
	
	/**
	 * Updates a hearing note with specified parameters.
	 * @param hearingNote - HearingNote to update
	 * @param description - String
	 * @param date - Date
	 * @return updated hearing note
	 * @throws HearingNoteExistsException - when Hearing Note already exists
	 * for specified hearing with given date and description
	 */
	HearingNote updateHearingNote(HearingNote hearingNote,
			String description, Date date)
					throws HearingNoteExistsException;
	
	/**
	 * Removes a HearingNote.
	 * @param hearingNote - HearingNote to remove
	 */
	void removeHearingNote(HearingNote hearingNote);
	
	/**
	 * Returns a list of all HearingNotes by hearing.
	 * @param hearing - Hearing
	 * @return list of all HearingNotes by hearing
	 */
	List<HearingNote> findHearingNotesByHearing(Hearing hearing);
	
	/**
	 * Creates a user attendance with the specified properties.
	 * 
	 * @param hearing hearing
	 * @param userAccount user account
	 * @return user attendance
	 * @throws UserAttendanceExistsException if user attendance exists for
	 * specified hearing
	 */
	UserAttendance createUserAttendance(Hearing hearing,
			UserAccount userAccount)throws UserAttendanceExistsException;
	
	/**
	 * Updates a user attendance with the specified properties.
	 * 
	 * @param userAttendance user attendance
	 * @param userAccount user account
	 * @return user attendance
	 * @throws UserAttendanceExistsException if user attendance exists for
	 * specified hearing
	 */
	UserAttendance updateUserAttendance(UserAttendance userAttendance,
			UserAccount userAccount) throws UserAttendanceExistsException;
	
	/**
	 * Removes a user attendance.
	 * @param userAttendance user attendance
	 */
	void removeUserAttendance(UserAttendance userAttendance);
	
	/**
	 * Creates a HearingStatus with the specified properties.
	 * @param hearing - Hearing
	 * @param description - String
	 * @param date - Date
	 * @param category - HearingStatusCategory
	 * @return Newly Created HearingStatus
	 * @throws HearingStatusExistsException - When a HearingStatus already
	 * exists with specified Date and Category for given Hearing
	 */
	HearingStatus createHearingStatus(Hearing hearing, String description,
			Date date, HearingStatusCategory category)
					throws HearingStatusExistsException;
	
	/**
	 * Updates a HearingStatus with the specified properties.
	 * @param hearingStatus - HearingStatus to update
	 * @param description - String
	 * @param date - Date
	 * @param category - HearingStatusCategory
	 * @return Updated HearingStatus
	 * @throws HearingStatusExistsException - When a HearingStatus already
	 * exists with specified Date and Category for given Hearing
	 */
	HearingStatus updateHearingStatus(HearingStatus hearingStatus,
			String description, Date date,
			HearingStatusCategory category)
					throws HearingStatusExistsException;
	
	/**
	 * Removes a HearingStatus.
	 * @param hearingStatus - HearingStatus to remove
	 */
	void removeHearingStatus(HearingStatus hearingStatus);
	
	/**
	 * Creates a new Infraction with the specified properties.
	 * @param hearing - Hearing
	 * @param conditionViolation - ConditionViolation
	 * @param disciplinaryCodeViolation - DisciplinaryCodeViolation
	 * @param resolution - Resolution
	 * @param plea - Infraction Plea
	 * @return Newly created Infraction
	 * @throws DuplicateEntityFoundException - When an Infraction already exists
	 * with given Hearing, ConditionViolation, and DisciplinaryCodeViolation.
	 * @throws InfractionExistsException - When an Infraction already exists
	 * with given Hearing, Condition Violation, and Disciplinary Code Violation.
	 */
	Infraction createInfraction(Hearing hearing,
			ConditionViolation conditionViolation,
			DisciplinaryCodeViolation disciplinaryCodeViolation,
			Resolution resolution, InfractionPlea plea)
					throws InfractionExistsException;
	
	/**
	 * Updates an Infraction with the specified properties.
	 * @param infraction - Infraction to update
	 * @param conditionViolation - ConditionViolation
	 * @param disciplinaryCodeViolation - DisciplinaryCodeViolation
	 * @param resolution - Resolution
	 * @param plea - Infraction Plea
	 * @return Updated Infraction
	 * @throws InfractionExistsException - When an Infraction already exists
	 * with given Hearing, Condition Violation, and Disciplinary Code Violation.
	 */
	Infraction updateInfraction(Infraction infraction,
			ConditionViolation conditionViolation,
			DisciplinaryCodeViolation disciplinaryCodeViolation,
			Resolution resolution, InfractionPlea plea)
					throws InfractionExistsException;
	
	/**
	 * Removes an Infraction.
	 * @param infraction - Infraction to remove
	 */
	void removeInfraction(Infraction infraction);
	
	/**
	 * Removes an ImposedSanction.
	 * @param imposedSanction - ImposedSanction to remove
	 */
	void removeImposedSanction(ImposedSanction imposedSanction);
	
	/**
	 * Returns a list of Infractions by specified Hearing.
	 * @param hearing - Hearing
	 * @return List of Infractions by specified Hearing
	 */
	List<Infraction> findInfractionsByHearing(Hearing hearing);
	
	/**
	 * Returns an ImposedSanction found by specified Infraction.
	 * @param infraction - Infraction
	 * @return ImposedSanction found by specified Infraction
	 */
	ImposedSanction findImposedSanctionByInfraction(
			Infraction infraction);
	
	/**
	 * Returns a list of user attendances by specified Hearing.
	 * 
	 * @param hearing hearing
	 * @return list of user attendances
	 */
	List<UserAttendance> findUserAttendedByHearing(Hearing hearing);
	
	/**
	 * Returns a list of facility locations.
	 * @return list of facility locations
	 */
	List<Location> findFacilityLocations();
	
	/**
	 * Returns a list of jail locations.
	 * @return list of jail locations
	 */
	List<Location> findJailLocations();
	
	/**
	 * Returns a list of Treatment Center Locations.
	 * @return List of Locations
	 */
	List<Location>
			findTreatmentCenterLocations();
	
	/**
	 * Returns a list of PreRelease Center Locations.
	 * @return List of Locations
	 */
	List<Location>
			findPreReleaseCenterLocations();
	
	/**
	 * Returns a list of Community Supervision Office Locations.
	 * @return List of Locations
	 */
	List<Location>
			findCommunitySupervisionOfficeLocations();
	
	/**
	 * Returns a list of Supervisory Organizations.
	 * @return List of Supervisory Organizations
	 */
	List<SupervisoryOrganization> findSupervisoryOrganization();
	
	/**
	 * Returns the HearingStatus with the most recent Date for
	 * specified Hearing.
	 * @param hearing - Hearing
	 * @return HearingStatus with the most recent Date for specified Hearing
	 */
	HearingStatus findLatestHearingStatus(Hearing hearing);
	
	/**
	 * Returns a list of unresolved ViolationEvents by Offender.
	 * @param offender - Offender
	 * @return List of unresolved ViolationEvents by Offender
	 */
	List<ViolationEvent> findUnresolvedViolationEventsByOffender(
			Offender offender);
	
	/**
	 * Returns a list of unresolved ConditionViolations by
	 * specified ViolationEvent.
	 * @param violationEvent - ViolationEvent
	 * @return List of unresolved ConditionViolations by
	 * specified ViolationEvent
	 */
	List<ConditionViolation>
		findUnresolvedConditionViolationsByViolationEvent(
				ViolationEvent violationEvent);
	
	/**
	 * Returns a list of unresolved DisciplinaryCodeViolations by specified
	 * ViolationEvent.
	 * @param violationEvent - ViolationEvent
	 * @return List of unresolved DisciplinaryCodeViolations by specified
	 * ViolationEvent
	 */
	List<DisciplinaryCodeViolation>
		findUnresolvedDisciplinaryCodeViolationsByViolationEvent(
				ViolationEvent violationEvent);
	
	/**
	 * Returns a list of all HearingStatuses for specified Hearing.
	 * @param hearing - Hearing
	 * @return List of all HearingStatuses for specified Hearing
	 */
	List<HearingStatus> findHearingStatusesByHearing(Hearing hearing);
	
	/**
	 * Returns a list of disciplinary codes valid for the given jurisdiction and
	 * event date.
	 * @param jurisdiction - Jurisdiction
	 * @param eventDate - Event Date
	 * @return  list of disciplinary codes valid for specified Supervisory 
	 * Organization on specified date
	 */
	List<DisciplinaryCode> findDisciplinaryCodesByJurisdictionAndEventDate(
			SupervisoryOrganization jurisdiction,
			Date eventDate);
	
	/**
	 * Finds and returns a list of Conditions for an offender on specified date.
	 * @param offender - Offender
	 * @param eventDate - Event Date
	 * @return list of Conditions for an offender on specified date
	 */
	List<Condition> findConditionsByOffenderAndEffectiveDate(
			Offender offender, Date eventDate);
}
