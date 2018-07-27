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
package omis.hearing.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import omis.asrc.domain.AssessmentSanctionRevocationCenter;
import omis.asrc.service.delegate.AssessmentSanctionRevocationCenterDelegate;
import omis.communitysupervision.domain.CommunitySupervisionOffice;
import omis.communitysupervision.service.delegate.CommunitySupervisionOfficeDelegate;
import omis.condition.domain.Condition;
import omis.condition.service.delegate.ConditionDelegate;
import omis.disciplinaryCode.domain.DisciplinaryCode;
import omis.disciplinaryCode.service.delegate.DisciplinaryCodeDelegate;
import omis.facility.domain.Facility;
import omis.facility.service.delegate.FacilityDelegate;
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
import omis.hearing.service.HearingService;
import omis.hearing.service.delegate.HearingDelegate;
import omis.hearing.service.delegate.HearingNoteDelegate;
import omis.hearing.service.delegate.HearingStatusDelegate;
import omis.hearing.service.delegate.ImposedSanctionDelegate;
import omis.hearing.service.delegate.InfractionDelegate;
import omis.hearing.service.delegate.UserAttendanceDelegate;
import omis.jail.domain.Jail;
import omis.jail.service.delegate.JailDelegate;
import omis.location.domain.Location;
import omis.offender.domain.Offender;
import omis.prerelease.domain.PreReleaseCenter;
import omis.prerelease.service.delegate.PreReleaseCenterDelegate;
import omis.supervision.domain.SupervisoryOrganization;
import omis.supervision.service.delegate.SupervisoryOrganizationDelegate;
import omis.treatment.domain.TreatmentCenter;
import omis.treatment.service.delegate.TreatmentCenterDelegate;
import omis.user.domain.UserAccount;
import omis.violationevent.domain.ConditionViolation;
import omis.violationevent.domain.DisciplinaryCodeViolation;
import omis.violationevent.domain.ViolationEvent;
import omis.violationevent.service.delegate.ConditionViolationDelegate;
import omis.violationevent.service.delegate.DisciplinaryCodeViolationDelegate;
import omis.violationevent.service.delegate.ViolationEventDelegate;

/**
 * Hearing Service Implementation.
 * 
 * @author Annie Wahl 
 * @author Josh Divine
 * @version 0.1.5 (Jul 17, 2018)
 * @since OMIS 3.0
 */
public class HearingServiceImpl implements HearingService {
	
	private final HearingDelegate hearingDelegate;
	
	private final HearingNoteDelegate hearingNoteDelegate;
	
	private final UserAttendanceDelegate userAttendanceDelegate;
	
	private final HearingStatusDelegate hearingStatusDelegate;
	
	private final InfractionDelegate infractionDelegate;
	
	private final ImposedSanctionDelegate imposedSanctionDelegate;
	
	private final ViolationEventDelegate violationEventDelegate;
	
	private final ConditionViolationDelegate conditionViolationDelegate;
	
	private final DisciplinaryCodeViolationDelegate
		disciplinaryCodeViolationDelegate;
	
	private final JailDelegate jailDelegate;
	
	private final FacilityDelegate facilityDelegate;

	private final TreatmentCenterDelegate treatmentCenterDelegate;
	
	private final PreReleaseCenterDelegate preReleaseCenterDelegate;
	
	private final CommunitySupervisionOfficeDelegate 
			communitySupervisionOfficeDelegate;

	private final SupervisoryOrganizationDelegate 
			supervisoryOrganizationDelegate;
	
	private final AssessmentSanctionRevocationCenterDelegate 
			assessmentSanctionRevocationCenterDelegate;
	
	private final DisciplinaryCodeDelegate disciplinaryCodeDelegate;
	
	private final ConditionDelegate conditionDelegate;
	
	/**
	 * @param hearingDelegate - hearing delegate
	 * @param hearingNoteDelegate - hearing note delegate
	 * @param userAttendanceDelegate user attendance delegate
	 * @param hearingStatusDelegate - hearing status delegate
	 * @param infractionDelegate - infraction delegate
	 * @param imposedSanctionDelegate - imposed sanction delegate
	 * @param violationEventDelegate - violation event delegate
	 * @param conditionViolationDelegate - condition violation delegate
	 * @param disciplinaryCodeViolationDelegate - disciplinary code violation
	 * delegate
	 * @param jailDelegate - jail delegate
	 * @param facilityDelegate - facility delegate
	 * @param treatmentCenterDelegate - treatment center delegate
	 * @param preReleaseCenterDelegate - prerelease center delegate
	 * @param communitySupervisionOfficeDelegate - community supervision
	 * office delegate
	 * @param supervisoryOrganizationDelegate - supervisory organization
	 * delegate
	 * @param assessmentSanctionRevocationCenterDelegate assessment sanction 
	 * revocation center delegate
	 * @param disciplinaryCodeDelegate disciplinary code delegate
	 * @param conditionDelegate condition delegate
	 */
	public HearingServiceImpl(final HearingDelegate hearingDelegate,
			final HearingNoteDelegate hearingNoteDelegate,
			final UserAttendanceDelegate userAttendanceDelegate,
			final HearingStatusDelegate hearingStatusDelegate,
			final InfractionDelegate infractionDelegate,
			final ImposedSanctionDelegate imposedSanctionDelegate,
			final ViolationEventDelegate violationEventDelegate,
			final ConditionViolationDelegate conditionViolationDelegate,
			final DisciplinaryCodeViolationDelegate
					disciplinaryCodeViolationDelegate,
			final JailDelegate jailDelegate,
			final FacilityDelegate facilityDelegate,
			final TreatmentCenterDelegate treatmentCenterDelegate,
			final PreReleaseCenterDelegate preReleaseCenterDelegate,
			final CommunitySupervisionOfficeDelegate 
					communitySupervisionOfficeDelegate,
			final SupervisoryOrganizationDelegate 
					supervisoryOrganizationDelegate,
			final AssessmentSanctionRevocationCenterDelegate 
					assessmentSanctionRevocationCenterDelegate,
			final DisciplinaryCodeDelegate disciplinaryCodeDelegate,
			final ConditionDelegate conditionDelegate) {
		this.hearingDelegate = hearingDelegate;
		this.hearingNoteDelegate = hearingNoteDelegate;
		this.userAttendanceDelegate = userAttendanceDelegate;
		this.hearingStatusDelegate = hearingStatusDelegate;
		this.infractionDelegate = infractionDelegate;
		this.imposedSanctionDelegate = imposedSanctionDelegate;
		this.violationEventDelegate = violationEventDelegate;
		this.conditionViolationDelegate = conditionViolationDelegate;
		this.disciplinaryCodeViolationDelegate =
				disciplinaryCodeViolationDelegate;
		this.jailDelegate = jailDelegate;
		this.facilityDelegate = facilityDelegate;
		this.treatmentCenterDelegate = treatmentCenterDelegate;
		this.preReleaseCenterDelegate = preReleaseCenterDelegate;
		this.communitySupervisionOfficeDelegate = 
				communitySupervisionOfficeDelegate;
		this.supervisoryOrganizationDelegate = supervisoryOrganizationDelegate;
		this.assessmentSanctionRevocationCenterDelegate = 
				assessmentSanctionRevocationCenterDelegate;
		this.disciplinaryCodeDelegate = disciplinaryCodeDelegate;
		this.conditionDelegate = conditionDelegate;
	}

	/**{@inheritDoc} */
	@Override
	public Hearing createHearing(final Location location,
			final Offender offender, final Boolean inAttendance,
			final Date date, final HearingCategory category,
			final UserAccount officer)
					throws HearingExistsException {
		return this.hearingDelegate.create(location, offender, inAttendance,
				date, category, officer);
	}

	/**{@inheritDoc} */
	@Override
	public Hearing updateHearing(final Hearing hearing, final Location location,
			final Boolean inAttendance, final Date date,
			final HearingCategory category, final UserAccount officer)
					throws HearingExistsException {
		return this.hearingDelegate.update(hearing, location, inAttendance,
				date, category, officer);
	}

	/**{@inheritDoc} */
	@Override
	public void removeHearing(final Hearing hearing) {
		this.hearingDelegate.remove(hearing);
	}

	/**{@inheritDoc} */
	@Override
	public HearingNote createHearingNote(final Hearing hearing,
			final String description, final Date date)
					throws HearingNoteExistsException {
		return this.hearingNoteDelegate.create(hearing, description, date);
	}

	/**{@inheritDoc} */
	@Override
	public HearingNote updateHearingNote(final HearingNote hearingNote,
			final String description, final Date date)
					throws HearingNoteExistsException {
		return this.hearingNoteDelegate.update(
				hearingNote, description, date);
	}

	/**{@inheritDoc} */
	@Override
	public void removeHearingNote(final HearingNote hearingNote) {
		this.hearingNoteDelegate.remove(hearingNote);
	}

	/**{@inheritDoc} */
	@Override
	public List<HearingNote> findHearingNotesByHearing(final Hearing hearing) {
		return this.hearingNoteDelegate.findAllByHearing(hearing);
	}

	/**{@inheritDoc} */
	@Override
	public UserAttendance createUserAttendance(final Hearing hearing,
			final UserAccount userAccount)
			throws UserAttendanceExistsException {
		return this.userAttendanceDelegate.create(hearing, userAccount);
	}

	/**{@inheritDoc} */
	@Override
	public UserAttendance updateUserAttendance(
			final UserAttendance userAttendance, final UserAccount userAccount)
					throws UserAttendanceExistsException {
		return this.userAttendanceDelegate.update(userAttendance, 
				userAttendance.getHearing(), userAccount);
	}

	/**{@inheritDoc} */
	@Override
	public void removeUserAttendance(final UserAttendance userAttendance) {
		this.userAttendanceDelegate.remove(userAttendance);
	}

	/**{@inheritDoc} */
	@Override
	public List<UserAttendance> findUserAttendedByHearing(
			final Hearing hearing) {
		return this.userAttendanceDelegate.findAllByHearing(hearing);
	}

	/**{@inheritDoc} */
	@Override
	public List<Location> findFacilityLocations() {
		List<Facility> facilities = this.facilityDelegate.findAll();
		List<Location> locations = new ArrayList<Location>();
		for (Facility facility : facilities) {
			locations.add(facility.getLocation());
		}
		return locations;
	}

	/**{@inheritDoc} */
	@Override
	public List<Location> findJailLocations() {
		List<Jail> jails = this.jailDelegate.findAll();
		List<Location> locations = new ArrayList<Location>();
		for (Jail jail : jails) {
			locations.add(jail.getLocation());
		}
		return locations;
	}

	/**{@inheritDoc} */
	@Override
	public HearingStatus createHearingStatus(final Hearing hearing,
			final String description, final Date date,
			final HearingStatusCategory category)
					throws HearingStatusExistsException {
		return this.hearingStatusDelegate.create(hearing, description, date,
				category);
	}

	/**{@inheritDoc} */
	@Override
	public HearingStatus updateHearingStatus(final HearingStatus hearingStatus,
			final String description, final Date date,
			final HearingStatusCategory category)
					throws HearingStatusExistsException {
		return this.hearingStatusDelegate.update(hearingStatus, description,
				date, category);
	}

	/**{@inheritDoc} */
	@Override
	public void removeHearingStatus(final HearingStatus hearingStatus) {
		this.hearingStatusDelegate.remove(hearingStatus);
	}

	/**{@inheritDoc} */
	@Override
	public Infraction createInfraction(final Hearing hearing,
			final ConditionViolation conditionViolation,
			final DisciplinaryCodeViolation disciplinaryCodeViolation,
			final Resolution resolution, final InfractionPlea plea)
					throws InfractionExistsException {
		return this.infractionDelegate.create(hearing, conditionViolation,
				disciplinaryCodeViolation, resolution, plea);
	}

	/**{@inheritDoc} */
	@Override
	public Infraction updateInfraction(final Infraction infraction,
			final ConditionViolation conditionViolation,
			final DisciplinaryCodeViolation disciplinaryCodeViolation,
			final Resolution resolution, final InfractionPlea plea)
					throws InfractionExistsException {
		return this.infractionDelegate.update(infraction, conditionViolation,
				disciplinaryCodeViolation, resolution, plea);
	}

	/**{@inheritDoc} */
	@Override
	public void removeInfraction(final Infraction infraction) {
		this.infractionDelegate.remove(infraction);
	}
	
	/**{@inheritDoc} */
	@Override
	public void removeImposedSanction(final ImposedSanction imposedSanction) {
		this.imposedSanctionDelegate.remove(imposedSanction);
	}

	/**{@inheritDoc} */
	@Override
	public List<Infraction> findInfractionsByHearing(final Hearing hearing) {
		return this.infractionDelegate.findByHearing(hearing);
	}
	
	/**{@inheritDoc} */
	@Override
	public ImposedSanction findImposedSanctionByInfraction(
			final Infraction infraction) {
		return this.imposedSanctionDelegate.findByInfraction(infraction);
	}

	/**{@inheritDoc} */
	@Override
	public List<Location> findTreatmentCenterLocations() {
		List<Location> treatmentCenterLocations =
				new ArrayList<Location>();
		Set<Location> uniqueLocations = new HashSet<>();
		List<TreatmentCenter> treatmentCenters =
				this.treatmentCenterDelegate.findAll();
		List<AssessmentSanctionRevocationCenter> 
			assessmentSanctionRevocationCenters = this
				.assessmentSanctionRevocationCenterDelegate.findAll();
		for (TreatmentCenter treatmentCenter : treatmentCenters) {
			uniqueLocations.add(treatmentCenter.getLocation());
		}
		for (AssessmentSanctionRevocationCenter 
				assessmentSanctionRevocationCenter
				: assessmentSanctionRevocationCenters) {
			uniqueLocations.add(assessmentSanctionRevocationCenter
						.getLocation());
		}
		treatmentCenterLocations.addAll(uniqueLocations);
		return treatmentCenterLocations;
	}

	/**{@inheritDoc} */
	@Override
	public List<Location> findPreReleaseCenterLocations() {
		List<Location> preReleaseCenterLocations =
				new ArrayList<Location>();
		List<PreReleaseCenter> preReleaseCenters = this.preReleaseCenterDelegate
				.findAll();
		for (PreReleaseCenter preReleaseCenter : preReleaseCenters) {
			preReleaseCenterLocations.add(preReleaseCenter.getLocation());
		}
		return preReleaseCenterLocations;
	}

	/**{@inheritDoc} */
	@Override
	public List<Location> findCommunitySupervisionOfficeLocations() {
		List<Location> communitySupervisionOfficeLocations =
				new ArrayList<Location>();
		List<CommunitySupervisionOffice> communitySupervisionOffices =
				this.communitySupervisionOfficeDelegate.findAll();
		for (CommunitySupervisionOffice communitySupervisionOffice
				: communitySupervisionOffices) {
			communitySupervisionOfficeLocations.add(
						communitySupervisionOffice.getLocation());
		}
		return communitySupervisionOfficeLocations;
	}

	/**{@inheritDoc} */
	@Override
	public List<SupervisoryOrganization> findSupervisoryOrganization() {
		return this.supervisoryOrganizationDelegate.findAll();
	}

	/**{@inheritDoc} */
	@Override
	public HearingStatus findLatestHearingStatus(final Hearing hearing) {
		return this.hearingStatusDelegate.findLatestByHearing(hearing);
	}

	/**{@inheritDoc} */
	@Override
	public List<ViolationEvent> findUnresolvedViolationEventsByOffender(
			final Offender offender) {
		return this.violationEventDelegate.findUnresolvedByOffender(offender);
	}

	/**{@inheritDoc} */
	@Override
	public List<ConditionViolation>
		findUnresolvedConditionViolationsByViolationEvent(
				final ViolationEvent violationEvent) {
		return this.conditionViolationDelegate.findUnresolvedByViolationEvent(
				violationEvent);
	}

	/**{@inheritDoc} */
	@Override
	public List<DisciplinaryCodeViolation>
		findUnresolvedDisciplinaryCodeViolationsByViolationEvent(
				final ViolationEvent violationEvent) {
		return this.disciplinaryCodeViolationDelegate
				.findUnresolvedByViolationEvent(violationEvent);
	}

	/**{@inheritDoc} */
	@Override
	public List<HearingStatus> findHearingStatusesByHearing(
			final Hearing hearing) {
		return this.hearingStatusDelegate.findByHearing(hearing);
	}

	/**{@inheritDoc} */
	@Override
	public List<DisciplinaryCode>
		findDisciplinaryCodesByJurisdictionAndEventDate(
			final SupervisoryOrganization jurisdiction, final Date eventDate) {
		return this.disciplinaryCodeDelegate
				.findBySupervisoryOrganizationAndDate(jurisdiction, eventDate);
	}

	/**{@inheritDoc} */
	@Override
	public List<Condition> findConditionsByOffenderAndEffectiveDate(
			final Offender offender, final Date eventDate) {
		return this.conditionDelegate.findByOffenderAndEffectiveDate(
				offender, eventDate);
	}
}
