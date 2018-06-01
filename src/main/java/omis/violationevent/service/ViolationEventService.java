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
package omis.violationevent.service;

import java.util.Date;
import java.util.List;

import omis.condition.domain.Condition;
import omis.disciplinaryCode.domain.DisciplinaryCode;
import omis.document.domain.Document;
import omis.document.domain.DocumentTag;
import omis.exception.DuplicateEntityFoundException;
import omis.facility.domain.Unit;
import omis.hearing.domain.Hearing;
import omis.hearing.domain.HearingNote;
import omis.hearing.domain.HearingStatus;
import omis.hearing.domain.ImposedSanction;
import omis.hearing.domain.Infraction;
import omis.hearing.domain.UserAttendance;
import omis.offender.domain.Offender;
import omis.supervision.domain.SupervisoryOrganization;
import omis.violationevent.domain.ConditionViolation;
import omis.violationevent.domain.DisciplinaryCodeViolation;
import omis.violationevent.domain.ViolationEvent;
import omis.violationevent.domain.ViolationEventCategory;
import omis.violationevent.domain.ViolationEventDocument;
import omis.violationevent.domain.ViolationEventNote;

/**
 * Violation event service.
 * 
 * @author Annie Wahl 
 * @author Josh Divine
 * @version 0.1.1 (May 23, 2018)
 * @since OMIS 3.0
 */
public interface ViolationEventService {
	
	/**
	 * Creates a new ViolationEvent with the specified properties
	 * @param offender - Offender
	 * @param jurisdiction - SupervisoryOrganization
	 * @param unit unit
	 * @param date - Date
	 * @param details - String
	 * @param category - ViolationEventCategory
	 * @return Newly Created ViolationEvent
	 * @throws DuplicateEntityFoundException - when a ViolationEvent already
	 * exists with all of the given properties for the Offender
	 */
	public ViolationEvent createViolationEvent(Offender offender,
			SupervisoryOrganization jurisdiction, Unit unit, Date date, 
			String details, ViolationEventCategory category)
					throws DuplicateEntityFoundException;
	
	/**
	 * Updates a violation event with specified properties
	 * @param violationEvent - ViolationEvent to update
	 * @param jurisdiction - SupervisoryOrganization
	 * @param unit unit
	 * @param date - Date
	 * @param details - String
	 * @param category - ViolationEventCategory
	 * @return Updated ViolationEvent
	 * @throws DuplicateEntityFoundException - when a ViolationEvent already
	 * exists with all of the given properties for the Offender
	 */
	public ViolationEvent updateViolationEvent(ViolationEvent violationEvent,
			SupervisoryOrganization jurisdiction, Unit unit, Date date, 
			String details, ViolationEventCategory category)
					throws DuplicateEntityFoundException;
	
	/**
	 * Removes a specified violationEvent
	 * @param violationEvent - ViolationEvent to remove
	 */
	public void removeViolationEvent(ViolationEvent violationEvent);
	
	/**
	 * Finds and returns a list of ViolationEvents by specified offender
	 * @param offender - Offender 
	 * @return List of ViolationEvents by specified offender
	 */
	public List<ViolationEvent> findViolationEventsByOffender(
			Offender offender);
	
	/**
	 * Creates a DisciplinaryCodeViolation with specified properties
	 * @param disciplinaryCode - DisciplinaryCode
	 * @param violationEvent - ViolationEvent
	 * @return Newly Created DisciplinaryCodeViolation
	 * @throws DuplicateEntityFoundException - When a DisciplinaryCodeViolation
	 * already exists with specified DisciplinaryCode for given ViolationEvent
	 */
	public DisciplinaryCodeViolation createDisciplinaryCodeViolation(
			DisciplinaryCode disciplinaryCode,
			ViolationEvent violationEvent)
					throws DuplicateEntityFoundException;
	
	/**
	 * Updates a DisciplinaryCodeViolation
	 * @param disciplinaryCodeViolation - DisciplinaryCodeViolation to update
	 * @param disciplinaryCode - DisciplinaryCode
	 * @return Updated DisciplinaryCode
	 * @throws DuplicateEntityFoundException - when DisciplinaryCodeViolation
	 * already exists with given DisciplinaryCode for its ViolationEvent
	 */
	public DisciplinaryCodeViolation updateDisciplinaryCodeViolation(
			DisciplinaryCodeViolation disciplinaryCodeViolation,
			DisciplinaryCode disciplinaryCode)
					throws DuplicateEntityFoundException;
	
	/**
	 * Removes the specified DisciplinaryCodeViolation
	 * @param disciplinaryCodeViolation - DisciplinaryCodeViolation to remove
	 */
	public void removeDisciplinaryCodeViolation(
			DisciplinaryCodeViolation disciplinaryCodeViolation);
	
	/**
	 * Finds and returns a list of DisciplinaryCodeViolations found by 
	 * specified violationEvent
	 * @param violationEvent - ViolationEvent
	 * @return list of DisciplinaryCodeViolations found by 
	 * specified violationEvent
	 */
	public List<DisciplinaryCodeViolation>
		findDisciplinaryCodeViolationsByViolationEvent(
				ViolationEvent violationEvent);
	
	
	/**
	 * Creates a ViolationEventDocument with specified properties
	 * @param document - Document
	 * @param violationEvent - ViolationEvent
	 * @return Newly Created ViolationEventDocument
	 * @throws DuplicateEntityFoundException - when a ViolationEventDocument
	 * already exists with specified Document for given ViolationEvent
	 */
	public ViolationEventDocument createViolationEventDocument(Document document,
			ViolationEvent violationEvent)
					throws DuplicateEntityFoundException;
	
	/**
	 * Updates a ViolationEventDocument
	 * @param violationEventDocument - ViolationEventDocument to update
	 * @param document - Document
	 * @return Updated ViolationEventDocument
	 * @throws DuplicateEntityFoundException - When ViolationEventDocument 
	 * already exists with specified Document for its ViolationEvent
	 */
	public ViolationEventDocument updateViolationEventDocument(
			ViolationEventDocument violationEventDocument,
			Document document)
					throws DuplicateEntityFoundException;
	
	/**
	 * Removes a ViolationEventDocument
	 * @param violationEventDocument - ViolationEventDocument to remove
	 */
	public void removeViolationEventDocument(
			ViolationEventDocument violationEventDocument);
	
	/**
	 * Finds and returns a list of ViolationEventDocuments found by specified
	 * ViolationEvent
	 * @param violationEvent - ViolationEvent
	 * @return list of ViolationEventDocuments found by specified
	 * ViolationEvent
	 */
	public List<ViolationEventDocument>
		findViolationEventDocumentsByViolationEvent(
				ViolationEvent violationEvent);
	
	
	/**
	 * Creates a ViolationEventNote with specified properties
	 * @param date - Date
	 * @param description - String
	 * @param violationEvent - ViolationEvent
	 * @return Newly created ViolationEventNote
	 * @throws DuplicateEntityFoundException - When a ViolationEventNote already
	 * exists with specified date and description for given ViolationEvent
	 */
	public ViolationEventNote createViolationEventNote(Date date,
			String description, ViolationEvent violationEvent)
					throws DuplicateEntityFoundException;
	
	/**
	 * Updates a ViolationEventNote with specified properties
	 * @param violationEventNote - ViolationEventNote to update
	 * @param date - Date
	 * @param description - String 
	 * @return Updated ViolationEventNote
	 * @throws DuplicateEntityFoundExcepton - when a ViolationEventNote already
	 * exists with given date and description for its ViolationEvent
	 */
	public ViolationEventNote updateViolationEventNote(
			ViolationEventNote violationEventNote, Date date,
			String description)
					throws DuplicateEntityFoundException;
	
	/**
	 * Removes a ViolationEventNote
	 * @param violationEventNote - ViolationEventNote to remove
	 */
	public void removeViolationEventNote(ViolationEventNote violationEventNote);
	
	/**
	 * Finds and returns a list of all ViolationEventNotes by ViolationEvent
	 * @param violationEvent - ViolationEvent
	 * @return List of violationEventNotes by specified ViolationEvent
	 */
	public List<ViolationEventNote> findViolationEventNotesByViolationEvent(
			ViolationEvent violationEvent);
	
	/**
	 * Creates a document
	 * @param documentDate - date of document
	 * @param filename - file name
	 * @param fileExtension - file extension
	 * @param title - title
	 * @return newly created Document
	 * @throws DuplicateEntityFoundException - when document with existing file
	 * name exists
	 */
	public Document createDocument(Date documentDate,
			String filename, String fileExtension, String title)
					throws DuplicateEntityFoundException;
	
	/**
	 * Updates document.
	 * @param document - document
	 * @param title - title
	 * @param date - date
	 */
	public Document updateDocument(Document document, String title,
			Date documentDate) throws DuplicateEntityFoundException;
	
	/**
	 * Removes Specified Document
	 * @param document - Document to remove
	 */
	public void removeDocument(Document document);
	
	/**
	 * Tag document
	 * @param document - document
	 * @param name - tag name
	 * @return newly created DocumentTag
	 * @throws DuplicateEntityFoundException - when document is already tagged
	 * with given name.
	 */
	public DocumentTag createDocumentTag(Document document, String name)
			throws DuplicateEntityFoundException;
	
	/**
	 * Update tag
	 * @param documentTag - document tag to update
	 * @param name - name
	 * @return updated DocumentTag
	 * @throws DuplicateEntityFoundException - when document is already tagged
	 * with given name
	 */
	public DocumentTag updateDocumentTag(DocumentTag documentTag, String name)
					throws DuplicateEntityFoundException;
	
	/**
	 * Remove tag
	 * @param documentTag - document tag to remove
	 */
	public void removeDocumentTag(DocumentTag documentTag);
	
	/**
	 * @param jurisdiction
	 * @param eventDate
	 * @return
	 */
	public List<DisciplinaryCode>
		findDisciplinaryCodesByJurisdictionAndEventDate(
				SupervisoryOrganization jurisdiction, Date eventDate);
	
	/**
	 * Returns a list of Supervisory Organizations
	 * @return List of Supervisory Organizations
	 */
	public List<SupervisoryOrganization> findSupervisoryOrganizations();
	
	/**
	 * Finds document tags by document
	 * @param document - document
	 * @return list of document tags 
	 */
	public List<DocumentTag> findDocumentTagsByDocument(Document document);
	
	/**
	 * Returns a list of Facility Supervisory Organizations
	 * @return List of Supervisory Organizations
	 */
	public List<SupervisoryOrganization> findFacilitySupervisoryOrganizations();
	
	/**
	 * Create a ConditionViolation with specified properties
	 * @param condition - Condition
	 * @param violationEvent - ViolationEvent
	 * @return Newly created ConditionViolation
	 * @throws DuplicateEntityFoundExcepton - when ConditionViolation already
	 * exists with given Condition and ViolationEvent
	 */
	public ConditionViolation createConditionViolation(Condition condition,
			ViolationEvent violationEvent) throws DuplicateEntityFoundException;
	
	/**
	 * Updates a ConditionViolation with specified properties
	 * @param conditionViolation - ConditionViolation to update
	 * @param condition - Condition
	 * @return Updated ConditionViolation
	 * @throws DuplicateEntityFoundExcepton - when ConditionViolation already
	 * exists with given Condition and ViolationEvent
	 */
	public ConditionViolation updateConditionViolation(
			ConditionViolation conditionViolation, Condition condition)
					throws DuplicateEntityFoundException;
	
	/**
	 * Removes a ConditionViolation
	 * @param conditionViolation - ConditionViolation to remove
	 */
	public void removeConditionViolation(ConditionViolation conditionViolation);
	
	/**
	 * Finds and returns a list of Conditions for specified Offender on specified
	 * Date
	 * @param offender - Offender
	 * @param eventDate - Date
	 * @return list of Conditions for specified Offender on specified Date
	 */
	public List<Condition> findConditionsByOffenderAndEventDate(
			Offender offender, Date eventDate);
	
	/**
	 * Finds and returns a list of ConditionViolations by specified 
	 * ViolationEvent
	 * @param violationEvent - ViolationEvent
	 * @return List of ConditionViolations by specified ViolationEvent
	 */
	public List<ConditionViolation> findConditionViolationsByViolationEvent(
			ViolationEvent violationEvent);
	
	
	/**
	 * Returns a list of Treatment Center Supervisory Organizations
	 * @return List of Supervisory Organizations
	 */
	public List<SupervisoryOrganization>
			findTreatmentCenterSupervisoryOrganizations();
	
	/**
	 * Returns a list of PreRelease Center Supervisory Organizations
	 * @return List of Supervisory Organizations
	 */
	public List<SupervisoryOrganization>
			findPreReleaseCenterSupervisoryOrganizations();
	
	/**
	 * Returns a list of Community Supervision Office Supervisory Organizations
	 * @return List of Supervisory Organizations
	 */
	public List<SupervisoryOrganization>
			findCommunitySupervisionOfficeSupervisoryOrganizations();
	
	/**
	 * Returns a list of Assessment Sanction Revocation Center 
	 * Supervisory Organizations
	 * @return List of Supervisory Organizations
	 */
	public List<SupervisoryOrganization>
			findAssessmentSanctionRevocationCenterSupervisoryOrganizations();
	
	/**
	 * Returns a list of hearing notes for the specified hearing.
	 * 
	 * @param hearing hearing
	 * @return list of hearing notes
	 */
	List<HearingNote> findHearingNotesByHearing(Hearing hearing);
	
	/**
	 * Returns a list of hearing statuses for the specified hearing.
	 * 
	 * @param hearing hearing
	 * @return list of hearing statuses
	 */
	List<HearingStatus> findHearingStatusesByHearing(Hearing hearing);
	
	/**
	 * Returns a list of hearings for the specified violation event.
	 * 
	 * @param violationEvent violation event
	 * @return list of hearings
	 */
	List<Hearing> findHearingsByViolationEvent(ViolationEvent violationEvent);
	
	/**
	 * Returns the imposed sanction for the specified infraction.
	 * 
	 * @param infraction infraction
	 * @return imposed sanction
	 */
	ImposedSanction findImposedSanctionByInfraction(Infraction infraction);
	
	/**
	 * Returns a list of infractions for the specified hearing.
	 * 
	 * @param hearing hearing
	 * @return list of infractions
	 */
	List<Infraction> findInfractionsByHearing(Hearing hearing);
	
	/**
	 * Returns a list of user attendances for the specified hearing.
	 * 
	 * @param hearing hearing
	 * @return list of user attendances
	 */
	List<UserAttendance> findUserAttendedByHearing(Hearing hearing);
	
	/**
	 * Removes the specified hearing.
	 * 
	 * @param hearing hearing
	 */
	void removeHearing(Hearing hearing);
	
	/**
	 * Removes the specified hearing note.
	 * 
	 * @param hearingNote hearing note
	 */
	void removeHearingNote(HearingNote hearingNote);
	
	/**
	 * Removes the specified hearing status.
	 * 
	 * @param hearingStatus hearing status
	 */
	void removeHearingStatus(HearingStatus hearingStatus);
	
	/**
	 * Removes the specified imposed sanction.
	 * 
	 * @param imposedSanction imposed sanction
	 */
	void removeImposedSanction(ImposedSanction imposedSanction);
	
	/**
	 * Removes the specified infraction.
	 * 
	 * @param infraction infraction
	 */
	void removeInfraction(Infraction infraction);
	
	/**
	 * Removes the specified user attendance.
	 * @param userAttendance user attendance
	 */
	void removeUserAttended(UserAttendance userAttendance);
	
	/**
	 * Returns a list of units for the specified supervisory organization.
	 * 
	 * @param supervisoryOrganization supervisory organization
	 * @return list of units
	 */
	List<Unit> findUnitsBySupervisoryOrganization(
			SupervisoryOrganization supervisoryOrganization);
}