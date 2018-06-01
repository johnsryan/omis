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
package omis.violationevent.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import omis.asrc.domain.AssessmentSanctionRevocationCenter;
import omis.asrc.service.delegate.AssessmentSanctionRevocationCenterDelegate;
import omis.communitysupervision.domain.CommunitySupervisionOffice;
import omis.communitysupervision.service.delegate.CommunitySupervisionOfficeDelegate;
import omis.condition.domain.Condition;
import omis.condition.service.delegate.ConditionDelegate;
import omis.disciplinaryCode.domain.DisciplinaryCode;
import omis.disciplinaryCode.service.delegate.DisciplinaryCodeDelegate;
import omis.document.domain.Document;
import omis.document.domain.DocumentTag;
import omis.document.service.delegate.DocumentDelegate;
import omis.document.service.delegate.DocumentTagDelegate;
import omis.exception.DuplicateEntityFoundException;
import omis.facility.domain.Facility;
import omis.facility.domain.Unit;
import omis.facility.service.delegate.FacilityDelegate;
import omis.facility.service.delegate.UnitDelegate;
import omis.hearing.domain.Hearing;
import omis.hearing.domain.HearingNote;
import omis.hearing.domain.HearingStatus;
import omis.hearing.domain.ImposedSanction;
import omis.hearing.domain.Infraction;
import omis.hearing.domain.UserAttendance;
import omis.hearing.service.delegate.HearingDelegate;
import omis.hearing.service.delegate.HearingNoteDelegate;
import omis.hearing.service.delegate.HearingStatusDelegate;
import omis.hearing.service.delegate.ImposedSanctionDelegate;
import omis.hearing.service.delegate.InfractionDelegate;
import omis.hearing.service.delegate.UserAttendanceDelegate;
import omis.offender.domain.Offender;
import omis.prerelease.domain.PreReleaseCenter;
import omis.prerelease.service.delegate.PreReleaseCenterDelegate;
import omis.supervision.domain.SupervisoryOrganization;
import omis.supervision.service.delegate.SupervisoryOrganizationDelegate;
import omis.treatment.domain.TreatmentCenter;
import omis.treatment.service.delegate.TreatmentCenterDelegate;
import omis.violationevent.domain.ConditionViolation;
import omis.violationevent.domain.DisciplinaryCodeViolation;
import omis.violationevent.domain.ViolationEvent;
import omis.violationevent.domain.ViolationEventCategory;
import omis.violationevent.domain.ViolationEventDocument;
import omis.violationevent.domain.ViolationEventNote;
import omis.violationevent.service.ViolationEventService;
import omis.violationevent.service.delegate.ConditionViolationDelegate;
import omis.violationevent.service.delegate.DisciplinaryCodeViolationDelegate;
import omis.violationevent.service.delegate.ViolationEventDelegate;
import omis.violationevent.service.delegate.ViolationEventDocumentDelegate;
import omis.violationevent.service.delegate.ViolationEventNoteDelegate;

/**
 * Implementation of service for violation event.
 * 
 * @author Annie Wahl
 * @author Josh Divine
 * @version 0.1.2 (May 23, 2018)
 * @since OMIS 3.0
 */
public class ViolationEventServiceImpl implements ViolationEventService {
	
	private final ViolationEventDelegate violationEventDelegate;
	
	private final DisciplinaryCodeViolationDelegate
			disciplinaryCodeViolationDelegate;
	
	private final ViolationEventDocumentDelegate violationEventDocumentDelegate;
	
	private final ViolationEventNoteDelegate violationEventNoteDelegate;
	
	private final ConditionViolationDelegate conditionViolationDelegate;
	
	private final DocumentDelegate documentDelegate;
	
	private final DocumentTagDelegate documentTagDelegate;
	
	private final SupervisoryOrganizationDelegate 
			supervisoryOrganizationDelegate;
	
	private final DisciplinaryCodeDelegate disciplinaryCodeDelegate;
	
	private final FacilityDelegate facilityDelegate;
	
	private final TreatmentCenterDelegate treatmentCenterDelegate;
	
	private final PreReleaseCenterDelegate preReleaseCenterDelegate;
	
	private final CommunitySupervisionOfficeDelegate 
			communitySupervisionOfficeDelegate;
	
	private final AssessmentSanctionRevocationCenterDelegate
			assessmentSanctionRevocationCenterDelegate;
	
	private final ConditionDelegate conditionDelegate;
	
	private final HearingDelegate hearingDelegate;
	
	private final HearingNoteDelegate hearingNoteDelegate;
	
	private final HearingStatusDelegate hearingStatusDelegate;
	
	private final ImposedSanctionDelegate imposedSanctionDelegate;
	
	private final InfractionDelegate infractionDelegate;
	
	private final UserAttendanceDelegate userAttendanceDelegate;
	
	private final UnitDelegate unitDelegate;
	
	/**
	 * Default Constructor for ViolationEventServiceImpl
	 * @param violationEventDelegate
	 * @param disciplinaryCodeViolationDelegate
	 * @param violationEventDocumentDelegate
	 * @param violationEventNoteDelegate
	 * @param conditionViolationDelegate
	 * @param documentDelegate
	 * @param documentTagDelegate
	 * @param supervisoryOrganizationDelegate
	 * @param disciplinaryCodeDelegate
	 * @param facilityDelegate
	 * @param treatmentCenterDelegate
	 * @param preReleaseCenterDelegate
	 * @param communitySupervisionOfficeDelegate
	 * @param assessmentSanctionRevocationCenterDelegate
	 * @param conditionDelegate
	 * @param hearingDelegate
	 * @param hearingNoteDelegate
	 * @param hearingStatusDelegate
	 * @param imposedSanctionDelegate
	 * @param infractionDelegate
	 * @param userAttendanceDelegate
	 * @param unitDelegate
	 */
	public ViolationEventServiceImpl(
			final ViolationEventDelegate violationEventDelegate,
			final DisciplinaryCodeViolationDelegate 
					disciplinaryCodeViolationDelegate,
			final ViolationEventDocumentDelegate violationEventDocumentDelegate,
			final ViolationEventNoteDelegate violationEventNoteDelegate,
			final ConditionViolationDelegate conditionViolationDelegate,
			final DocumentDelegate documentDelegate,
			final DocumentTagDelegate documentTagDelegate,
			final SupervisoryOrganizationDelegate 
					supervisoryOrganizationDelegate,
			final DisciplinaryCodeDelegate disciplinaryCodeDelegate,
			final FacilityDelegate facilityDelegate,
			final TreatmentCenterDelegate treatmentCenterDelegate,
			final PreReleaseCenterDelegate preReleaseCenterDelegate,
			final CommunitySupervisionOfficeDelegate 
					communitySupervisionOfficeDelegate,
			final AssessmentSanctionRevocationCenterDelegate
					assessmentSanctionRevocationCenterDelegate,
			final ConditionDelegate conditionDelegate,
			final HearingDelegate hearingDelegate,
			final HearingNoteDelegate hearingNoteDelegate,
			final HearingStatusDelegate hearingStatusDelegate,
			final ImposedSanctionDelegate imposedSanctionDelegate,
			final InfractionDelegate infractionDelegate,
			final UserAttendanceDelegate userAttendanceDelegate,
			final UnitDelegate unitDelegate) {
		this.violationEventDelegate = violationEventDelegate;
		this.disciplinaryCodeViolationDelegate =
				disciplinaryCodeViolationDelegate;
		this.violationEventDocumentDelegate = violationEventDocumentDelegate;
		this.violationEventNoteDelegate = violationEventNoteDelegate;
		this.conditionViolationDelegate = conditionViolationDelegate;
		this.documentDelegate = documentDelegate;
		this.documentTagDelegate = documentTagDelegate;
		this.supervisoryOrganizationDelegate = supervisoryOrganizationDelegate;
		this.disciplinaryCodeDelegate = disciplinaryCodeDelegate;
		this.facilityDelegate = facilityDelegate;
		this.treatmentCenterDelegate = treatmentCenterDelegate;
		this.preReleaseCenterDelegate = preReleaseCenterDelegate;
		this.communitySupervisionOfficeDelegate = 
				communitySupervisionOfficeDelegate;
		this.assessmentSanctionRevocationCenterDelegate =
				assessmentSanctionRevocationCenterDelegate;
		this.conditionDelegate = conditionDelegate;
		this.hearingDelegate = hearingDelegate;
		this.hearingNoteDelegate = hearingNoteDelegate;
		this.hearingStatusDelegate = hearingStatusDelegate;
		this.imposedSanctionDelegate = imposedSanctionDelegate;
		this.infractionDelegate = infractionDelegate;
		this.userAttendanceDelegate = userAttendanceDelegate;
		this.unitDelegate = unitDelegate;
	}
	
	/**{@inheritDoc} */
	@Override
	public ViolationEvent createViolationEvent(final Offender offender,
			final SupervisoryOrganization jurisdiction, final Unit unit,
			final Date date, final String details,
			final ViolationEventCategory category)
					throws DuplicateEntityFoundException {
		return this.violationEventDelegate.create(offender, jurisdiction, unit,
				date, details, category);
	}

	/**{@inheritDoc} */
	@Override
	public ViolationEvent updateViolationEvent(
			final ViolationEvent violationEvent,
			final SupervisoryOrganization jurisdiction, final Unit unit,
			final Date date, final String details,
			final ViolationEventCategory category)
					throws DuplicateEntityFoundException {
		return this.violationEventDelegate.update(violationEvent, jurisdiction,
				unit, date, details, category);
	}

	/**{@inheritDoc} */
	@Override
	public void removeViolationEvent(final ViolationEvent violationEvent) {
		this.violationEventDelegate.remove(violationEvent);
	}

	/**{@inheritDoc} */
	@Override
	public List<ViolationEvent> findViolationEventsByOffender(
			final Offender offender) {
		return this.violationEventDelegate.findByOffender(offender);
	}

	/**{@inheritDoc} */
	@Override
	public DisciplinaryCodeViolation createDisciplinaryCodeViolation(
			final DisciplinaryCode disciplinaryCode,
			final ViolationEvent violationEvent)
					throws DuplicateEntityFoundException {
		return this.disciplinaryCodeViolationDelegate
				.create(disciplinaryCode, violationEvent);
	}

	/**{@inheritDoc} */
	@Override
	public DisciplinaryCodeViolation updateDisciplinaryCodeViolation(
			final DisciplinaryCodeViolation disciplinaryCodeViolation,
			final DisciplinaryCode disciplinaryCode)
					throws DuplicateEntityFoundException {
		return this.disciplinaryCodeViolationDelegate
				.update(disciplinaryCodeViolation, disciplinaryCode);
	}

	/**{@inheritDoc} */
	@Override
	public void removeDisciplinaryCodeViolation(
			final DisciplinaryCodeViolation disciplinaryCodeViolation) {
		this.disciplinaryCodeViolationDelegate.remove(disciplinaryCodeViolation);
	}

	/**{@inheritDoc} */
	@Override
	public List<DisciplinaryCodeViolation> 
		findDisciplinaryCodeViolationsByViolationEvent(
				final ViolationEvent violationEvent) {
		return this.disciplinaryCodeViolationDelegate
				.findByViolationEvent(violationEvent);
	}

	/**{@inheritDoc} */
	@Override
	public ViolationEventDocument createViolationEventDocument(
			final Document document, final ViolationEvent violationEvent)
					throws DuplicateEntityFoundException {
		return this.violationEventDocumentDelegate
				.create(document, violationEvent);
	}

	/**{@inheritDoc} */
	@Override
	public ViolationEventDocument updateViolationEventDocument(
			final ViolationEventDocument violationEventDocument,
			final Document document)
					throws DuplicateEntityFoundException {
		return this.violationEventDocumentDelegate
				.update(violationEventDocument, document);
	}

	/**{@inheritDoc} */
	@Override
	public void removeViolationEventDocument(
			final ViolationEventDocument violationEventDocument) {
		this.violationEventDocumentDelegate.remove(violationEventDocument);
	}

	/**{@inheritDoc} */
	@Override
	public List<ViolationEventDocument>
		findViolationEventDocumentsByViolationEvent(
				final ViolationEvent violationEvent) {
		return this.violationEventDocumentDelegate
				.findByViolationEvent(violationEvent);
	}

	/**{@inheritDoc} */
	@Override
	public ViolationEventNote createViolationEventNote(final Date date,
			final String description, final ViolationEvent violationEvent)
					throws DuplicateEntityFoundException {
		return this.violationEventNoteDelegate
				.create(date, description, violationEvent);
	}

	/**{@inheritDoc} */
	@Override
	public ViolationEventNote updateViolationEventNote(
			final ViolationEventNote violationEventNote, final Date date,
			final String description) throws DuplicateEntityFoundException {
		return this.violationEventNoteDelegate
				.update(violationEventNote, date, description);
	}

	/**{@inheritDoc} */
	@Override
	public void removeViolationEventNote(
			final ViolationEventNote violationEventNote) {
		this.violationEventNoteDelegate.remove(violationEventNote);
	}

	/**{@inheritDoc} */
	@Override
	public List<ViolationEventNote> findViolationEventNotesByViolationEvent(
			final ViolationEvent violationEvent) {
		return this.violationEventNoteDelegate
				.findByViolationEvent(violationEvent);
	}

	/**{@inheritDoc} */
	@Override
	public Document createDocument(final Date documentDate,
			final String filename, final String fileExtension,
			final String title) throws DuplicateEntityFoundException {
		return this.documentDelegate.create(documentDate, filename,
				fileExtension, title);
	}

	/**{@inheritDoc} */
	@Override
	public Document updateDocument(final Document document, final String title,
			final Date documentDate) throws DuplicateEntityFoundException {
		return this.documentDelegate.update(document, title, documentDate);
	}

	/**{@inheritDoc} */
	@Override
	public void removeDocument(final Document document) {
		this.documentDelegate.remove(document);
	}

	/**{@inheritDoc} */
	@Override
	public DocumentTag createDocumentTag(final Document document,
			final String name) throws DuplicateEntityFoundException {
		return this.documentTagDelegate.tagDocument(document, name);
	}

	/**{@inheritDoc} */
	@Override
	public DocumentTag updateDocumentTag(final DocumentTag documentTag,
			final String name) throws DuplicateEntityFoundException {
		return this.documentTagDelegate.update(documentTag, name);
	}

	/**{@inheritDoc} */
	@Override
	public void removeDocumentTag(final DocumentTag documentTag) {
		this.documentTagDelegate.removeTag(documentTag);
	}

	/**{@inheritDoc} */
	@Override
	public List<DisciplinaryCode>
		findDisciplinaryCodesByJurisdictionAndEventDate(
				final SupervisoryOrganization jurisdiction,
				final Date eventDate) {
		return this.disciplinaryCodeDelegate
				.findBySupervisoryOrganizationAndDate(jurisdiction, eventDate);
	}

	/**{@inheritDoc} */
	@Override
	public List<SupervisoryOrganization> findSupervisoryOrganizations() {
		return this.supervisoryOrganizationDelegate.findAll();
	}

	/**{@inheritDoc} */
	@Override
	public List<DocumentTag> findDocumentTagsByDocument(
			final Document document) {
		return this.documentTagDelegate.findByDocument(document);
	}

	/**{@inheritDoc} */
	@Override
	public List<SupervisoryOrganization> findFacilitySupervisoryOrganizations() {
		List<SupervisoryOrganization> facilitySupervisoryOrganizations =
				new ArrayList<SupervisoryOrganization>();
		
		List<Facility> facilities = this.facilityDelegate.findAll();
		List<SupervisoryOrganization> supervisoryOrganizations =
				this.supervisoryOrganizationDelegate.findAll();
		
		for(SupervisoryOrganization supervisoryOrganization :
			supervisoryOrganizations){
			for(Facility facility : facilities){
				if(facility.getLocation().getOrganization()
						.equals(supervisoryOrganization)){
					facilitySupervisoryOrganizations
						.add(supervisoryOrganization);
				}
			}
		}
		
		return facilitySupervisoryOrganizations;
	}

	/**{@inheritDoc} */
	@Override
	public ConditionViolation createConditionViolation(
			final Condition condition, final ViolationEvent violationEvent)
			throws DuplicateEntityFoundException {
		return this.conditionViolationDelegate.create(condition, violationEvent);
	}

	/**{@inheritDoc} */
	@Override
	public ConditionViolation updateConditionViolation(
			final ConditionViolation conditionViolation,
			final Condition condition)
					throws DuplicateEntityFoundException {
		return this.conditionViolationDelegate.update(conditionViolation,
				condition);
	}

	/**{@inheritDoc} */
	@Override
	public void removeConditionViolation(
			final ConditionViolation conditionViolation) {
		this.conditionViolationDelegate.remove(conditionViolation);
	}

	/**{@inheritDoc} */
	@Override
	public List<Condition> findConditionsByOffenderAndEventDate(
			final Offender offender, final  Date eventDate) {
		return this.conditionDelegate
				.findByOffenderAndEffectiveDate(offender, eventDate);
	}

	/**{@inheritDoc} */
	@Override
	public List<ConditionViolation> findConditionViolationsByViolationEvent(
			final ViolationEvent violationEvent) {
		return this.conditionViolationDelegate
				.findByViolationEvent(violationEvent);
	}

	/**{@inheritDoc} */
	@Override
	public List<SupervisoryOrganization>
			findTreatmentCenterSupervisoryOrganizations() {
		
		List<SupervisoryOrganization> treatmentCenterSupervisoryOrganizations =
				new ArrayList<SupervisoryOrganization>();
		
		List<TreatmentCenter> treatmentCenters =
				this.treatmentCenterDelegate.findAll();
		List<SupervisoryOrganization> supervisoryOrganizations =
				this.supervisoryOrganizationDelegate.findAll();
		
		for(SupervisoryOrganization supervisoryOrganization :
			supervisoryOrganizations){
			for(TreatmentCenter treatmentCenter : treatmentCenters){
				if(treatmentCenter.getLocation().getOrganization()
						.equals(supervisoryOrganization)){
					treatmentCenterSupervisoryOrganizations
						.add(supervisoryOrganization);
				}
			}
		}
		
		return treatmentCenterSupervisoryOrganizations;
	}

	/**{@inheritDoc} */
	@Override
	public List<SupervisoryOrganization>
			findPreReleaseCenterSupervisoryOrganizations() {
		List<SupervisoryOrganization> preReleaseCenterSupervisoryOrganizations =
				new ArrayList<SupervisoryOrganization>();
		
		List<PreReleaseCenter> preReleaseCenters =
				this.preReleaseCenterDelegate.findAll();
		List<SupervisoryOrganization> supervisoryOrganizations =
				this.supervisoryOrganizationDelegate.findAll();
		
		for(SupervisoryOrganization supervisoryOrganization :
			supervisoryOrganizations){
			for(PreReleaseCenter preReleaseCenter : preReleaseCenters){
				if(preReleaseCenter.getLocation().getOrganization()
						.equals(supervisoryOrganization)){
					preReleaseCenterSupervisoryOrganizations
						.add(supervisoryOrganization);
				}
			}
		}
		
		return preReleaseCenterSupervisoryOrganizations;
	}

	/**{@inheritDoc} */
	@Override
	public List<SupervisoryOrganization> 
			findCommunitySupervisionOfficeSupervisoryOrganizations() {
		List<SupervisoryOrganization>
			communitySupervisionOfficeSupervisoryOrganizations =
				new ArrayList<SupervisoryOrganization>();
		
		List<CommunitySupervisionOffice> communitySupervisionOffices =
				this.communitySupervisionOfficeDelegate.findAll();
		List<SupervisoryOrganization> supervisoryOrganizations =
				this.supervisoryOrganizationDelegate.findAll();
		
		for(SupervisoryOrganization supervisoryOrganization :
			supervisoryOrganizations){
			for(CommunitySupervisionOffice communitySupervisionOffice :
				communitySupervisionOffices){
				if(communitySupervisionOffice.getLocation().getOrganization()
						.equals(supervisoryOrganization)){
					communitySupervisionOfficeSupervisoryOrganizations
						.add(supervisoryOrganization);
				}
			}
		}
		
		return communitySupervisionOfficeSupervisoryOrganizations;
	}

	/**{@inheritDoc} */
	@Override
	public List<SupervisoryOrganization>
			findAssessmentSanctionRevocationCenterSupervisoryOrganizations() {
		List<SupervisoryOrganization>
				assessmentSanctionRevocationCenterSupervisoryOrganizations =
					new ArrayList<SupervisoryOrganization>();
		
		List<AssessmentSanctionRevocationCenter>
				assessmentSanctionRevocationCenters =
					this.assessmentSanctionRevocationCenterDelegate.findAll();
		List<SupervisoryOrganization> supervisoryOrganizations =
				this.supervisoryOrganizationDelegate.findAll();
		
		for(SupervisoryOrganization supervisoryOrganization :
			supervisoryOrganizations){
			for(AssessmentSanctionRevocationCenter
					assessmentSanctionRevocationCenter :
						assessmentSanctionRevocationCenters){
				if(assessmentSanctionRevocationCenter.getLocation()
						.getOrganization().equals(supervisoryOrganization)){
					assessmentSanctionRevocationCenterSupervisoryOrganizations
						.add(supervisoryOrganization);
				}
			}
		}
		
		return assessmentSanctionRevocationCenterSupervisoryOrganizations;
	}

	/** {@inheritDoc} */
	@Override
	public List<HearingNote> findHearingNotesByHearing(final Hearing hearing) {
		return this.hearingNoteDelegate.findAllByHearing(hearing);
	}

	/** {@inheritDoc} */
	@Override
	public List<HearingStatus> findHearingStatusesByHearing(
			final Hearing hearing) {
		return this.hearingStatusDelegate.findByHearing(hearing);
	}

	/** {@inheritDoc} */
	@Override
	public List<Hearing> findHearingsByViolationEvent(
			final ViolationEvent violationEvent) {
		return this.hearingDelegate.findByViolationEvent(violationEvent);
	}

	/** {@inheritDoc} */
	@Override
	public ImposedSanction findImposedSanctionByInfraction(
			final Infraction infraction) {
		return this.imposedSanctionDelegate.findByInfraction(infraction);
	}

	/** {@inheritDoc} */
	@Override
	public List<Infraction> findInfractionsByHearing(final Hearing hearing) {
		return this.infractionDelegate.findByHearing(hearing);
	}


	/** {@inheritDoc} */
	@Override
	public List<UserAttendance> findUserAttendedByHearing(
			final Hearing hearing) {
		return this.userAttendanceDelegate.findAllByHearing(hearing);
	}

	/** {@inheritDoc} */
	@Override
	public void removeHearing(final Hearing hearing) {
		this.hearingDelegate.remove(hearing);
	}

	/** {@inheritDoc} */
	@Override
	public void removeHearingNote(final HearingNote hearingNote) {
		this.hearingNoteDelegate.remove(hearingNote);
	}

	/** {@inheritDoc} */
	@Override
	public void removeHearingStatus(final HearingStatus hearingStatus) {
		this.hearingStatusDelegate.remove(hearingStatus);
	}

	/** {@inheritDoc} */
	@Override
	public void removeImposedSanction(final ImposedSanction imposedSanction) {
		this.imposedSanctionDelegate.remove(imposedSanction);
	}

	/** {@inheritDoc} */
	@Override
	public void removeInfraction(final Infraction infraction) {
		this.infractionDelegate.remove(infraction);
	}

	/** {@inheritDoc} */
	@Override
	public void removeUserAttended(final UserAttendance userAttendance) {
		this.userAttendanceDelegate.remove(userAttendance);
	}

	/** {@inheritDoc} */
	@Override
	public List<Unit> findUnitsBySupervisoryOrganization(
			final SupervisoryOrganization supervisoryOrganization) {
		return this.unitDelegate.findBySupervisoryOrganization(
				supervisoryOrganization);
	}
}