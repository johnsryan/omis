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
package omis.supervision.service;

import java.util.Date;
import java.util.List;

import omis.datatype.DateRange;
import omis.locationterm.domain.LocationTerm;
import omis.offender.domain.Offender;
import omis.region.domain.State;
import omis.supervision.domain.CorrectionalStatus;
import omis.supervision.domain.CorrectionalStatusTerm;
import omis.supervision.domain.PlacementStatus;
import omis.supervision.domain.PlacementTerm;
import omis.supervision.domain.PlacementTermChangeReason;
import omis.supervision.domain.PlacementTermNote;
import omis.supervision.domain.SupervisoryOrganization;
import omis.supervision.domain.SupervisoryOrganizationTerm;
import omis.supervision.exception.CorrectionalStatusTermConflictException;
import omis.supervision.exception.OffenderNotUnderSupervisionException;
import omis.supervision.exception.PlacementTermConflictException;
import omis.supervision.exception.PlacementTermExistsAfterException;
import omis.supervision.exception.PlacementTermExistsBeforeException;
import omis.supervision.exception.PlacementTermExistsException;
import omis.supervision.exception.PlacementTermLockedException;
import omis.supervision.exception.PlacementTermNoteExistsException;
import omis.supervision.exception.SupervisoryOrganizationTermConflictException;

/**
 * Service for placement terms.
 * 
 * @author Stephen Abson
 * @author Jason Nelson
 * @version 0.1.1 (Nov 12, 2014)
 * @since OMIS 3.0
 */
public interface PlacementTermService {
	
	/**
	 * Returns placement terms for the specified offender.
	 * 
	 * @param offender offender
	 * @return placement terms for offender
	 */
	List<PlacementTerm> findByOffender(Offender offender);
	
	/**
	 * Returns the placement term for the offender on the date.
	 * 
	 * @param offender offender
	 * @param date date
	 * @return placement term for offender on date
	 */
	PlacementTerm findByOffenderOnDate(Offender offender, Date date);
	
	/**
	 * Creates placement term.
	 * 
	 * <p>If not null, the start and end date of the date range are prevented
	 * from being equal by a {@code IllegalArgumentException} being thrown.
	 * 
	 * <p>New placement term uses existing correctional status term and/or
	 * supervisory organization term if the correction status and/or supervisory
	 * organization, respectively, and date ranges match. If the correctional
	 * status and/or supervisory organization does not match, existing
	 * correctional status terms and/or supervisory organization terms are ended
	 * and new ones began. If the correctional status and/or supervisory
	 * organization matches, the date ranges of the respective terms are
	 * lengthened for use by the placement term.
	 * 
	 * @param offender offender
	 * @param supervisoryOrganization of supervisory organization
	 * @param correctionalStatus of correctional status
	 * @param dateRange date range
	 * @param startChangeReason start change reason
	 * @param endChangeReason end change reason
	 * @return new placement term
	 * @throws PlacementTermExistsException if placement term exists
	 * @throws CorrectionalStatusTermConflictException if conflicting
	 * correctional status terms exist
	 * @throws SupervisoryOrganizationTermConflictException if conflicting
	 * supervisory organization terms exist
	 * @throws PlacementTermConflictException if the placement term
	 * conflicts with an existing placement term
	 */
	PlacementTerm create(Offender offender,
			SupervisoryOrganization supervisoryOrganization,
			CorrectionalStatus correctionalStatus,
			DateRange dateRange,
			PlacementTermChangeReason startChangeReason,
			PlacementTermChangeReason endChangeReason)
					throws PlacementTermExistsException,
						CorrectionalStatusTermConflictException,
						SupervisoryOrganizationTermConflictException,
						PlacementTermConflictException;
	
	/**
	 * Changes supervisory organization.
	 * 
	 * <p>If not null, the start and end date of the date range are prevented
	 * from being equal by a {@code IllegalArgumentException} being thrown.
	 * 
	 * @param offender offender
	 * @param supervisoryOrganization of supervisory organization
	 * @param dateRange date range
	 * @param startChangeReason start change reason
	 * @param endChangeReason end change reason
	 * @return new placement term
	 * @throws PlacementTermExistsException if placement term exists
	 * @throws SupervisoryOrganizationTermConflictException if conflicting
	 * supervisory organization terms exist
	 * @throws PlacementTermConflictException if the placement term
	 * conflicts with an existing placement term
	 * @throws OffenderNotUnderSupervisionException if offender not under 
	 * supervision
	 * @throws PlacementTermExistsAfterException if term exists after start date 
	 * and end date is null
	 * @throws PlacementTermExistsBeforeException if term exists and start date 
	 * is null
	 */
	PlacementTerm changeSupervisoryOrganization(Offender offender,
			SupervisoryOrganization supervisoryOrganization,
			DateRange dateRange,
			PlacementTermChangeReason startChangeReason,
			PlacementTermChangeReason endChangeReason)
					throws SupervisoryOrganizationTermConflictException,
						PlacementTermConflictException, 
						OffenderNotUnderSupervisionException,
						PlacementTermExistsAfterException,
						PlacementTermExistsBeforeException;
	
	/**
	 * Updates placement term.
	 * 
	 * <p>If not null, the start and end date of the date range are prevented
	 * from being equal by a {@code IllegalArgumentException} being thrown.
	 * 
	 * <p>If status is set to {@link PlacementStatus#ESCAPED}, the location
	 * on the status start date, if one exists, will be ended with the status
	 * start date.
	 * 
	 * @param placementTerm placement term to update
	 * @param endDate end date
	 * @param status status
	 * @param statusDateRange status date range
	 * @param startChangeReason start change reason
	 * @param endChangeReason end change reason
	 * @return updated placement term
	 * @throws CorrectionalStatusTermConflictException if conflicting
	 * correctional status terms exist
	 * @throws SupervisoryOrganizationTermConflictException of conflicting
	 * supervisory organization terms exist
	 * @throws PlacementTermLockedException if placement term is locked
	 */
	PlacementTerm update(PlacementTerm placementTerm,
			Date endDate,
			PlacementStatus status,
			DateRange statusDateRange,
			PlacementTermChangeReason startChangeReason,
			PlacementTermChangeReason endChangeReason)
					throws CorrectionalStatusTermConflictException,
						SupervisoryOrganizationTermConflictException,
						PlacementTermLockedException;
	
	/**
	 * Removes a placement term.
	 * 
	 * <p>Removes correctional status and supervisory organization terms if
	 * date ranges match that of {@code placementTerm}.
	 * 
	 * <p>If the correctional status and/or supervisory organization term
	 * run longer than the placement term, the correctional status and/or
	 * supervisory organization term will be adjusted to exclude the period
	 * of the removed placement term. In cases where the correctional status
	 * term and/or supervisory organization term finish later than the placement
	 * term, the start date of either or both of the latter two will be adjusted
	 * to that of the placement term. In cases where the correctional status
	 * term and/or supervisory organization term start earlier than the
	 * placement term, the end date of either or both of the latter two will be
	 * adjusted to the start date of the placement term.
	 * 
	 * @param placementTerm placement term to remove
	 */
	void remove(PlacementTerm placementTerm);
	
	/**
	 * Returns correctional statuses.
	 * 
	 * @return correctional statuses
	 */
	List<CorrectionalStatus> findCorrectionalStatuses();
	
	/**
	 * Creates note for placement term.
	 * 
	 * @param placementTerm placement term.
	 * @param date date
	 * @param value value
	 * @return note for placement term
	 * @throws PlacementTermNoteExistsException if note for placement term
	 * exists
	 */
	PlacementTermNote createNote(
			PlacementTerm placementTerm, Date date, String value)
				throws PlacementTermNoteExistsException;
	
	/**
	 * Updates note for placement term.
	 * 
	 * @param note note to update
	 * @param date date
	 * @param value value
	 * @return note for placement term to update
	 * @throws PlacementTermNoteExistsException if note for placement term
	 * exists
	 */
	PlacementTermNote updateNote(
			PlacementTermNote note, Date date, String value)
				throws PlacementTermNoteExistsException;
	
	/**
	 * Removes note for placement term.
	 * 
	 * @param note note for placement term to remove
	 */
	void removeNote(PlacementTermNote note);
	
	/**
	 * Returns notes by placement term.
	 * 
	 * @param placementTerm placement term
	 * @return notes by placement term
	 */
	List<PlacementTermNote> findNotesByPlacementTerm(
			PlacementTerm placementTerm);
	
	/**
	 * Returns supervisory organizations by State.
	 * 
	 * @deprecated Use
	 * {@code findSupervisoryOrganizationsForCorrectionalStatusByState()}
	 * @param state State
	 * @return supervisory organizations by State
	 */
	@Deprecated
	List<SupervisoryOrganization> findSupervisoryOrganizationsByState(
			State state);

	/**
	 * Returns supervisory organizations for correctional status.
	 * 
	 * @param correctionalStatus correctional status
	 * @return supervisory organizations for correctional status
	 */
	List<SupervisoryOrganization>
	findSupervisoryOrganizationsForCorrectionalStatus(
			CorrectionalStatus correctionalStatus);
	
	/**
	 * Returns supervisory organizations for correctional status by State.
	 * 
	 * @param correctionalStatus correctional status
	 * @param state State
	 * @return supervisory organizations for correctional status by State
	 */
	List<SupervisoryOrganization>
	findSupervisoryOrganizationsForCorrectionalStatusByState(
			CorrectionalStatus correctionalStatus, State state);
	
	/**
	 * Returns States in country of home State.
	 * 
	 * @return States in country of home State.
	 */
	List<State> findHomeStates();
	
	/**
	 * Returns home State.
	 * 
	 * @return home State
	 */
	State findHomeState();
	
	/**
	 * Returns States for supervisory organization.
	 * 
	 * @param supervisoryOrganization supervisory organization
	 * @return States for supervisory organization
	 */
	List<State> findStatesForSupervisoryOrganization(
			SupervisoryOrganization supervisoryOrganization);
	
	/**
	 * Returns placement term change reasons.
	 * 
	 * @return placement term change reasons
	 */
	List<PlacementTermChangeReason> findChangeReasons();
	
	/**
	 * Returns correctional status terms for the specified offender.
	 * 
	 * @param offender offender
	 * @return correctional status terms for offender
	 */
	List<CorrectionalStatusTerm>
		findCorrectionalStatusTermsByOffender(Offender offender);
	
	/**
	 * Returns the correctional status term for the offender on the specified
	 * date.
	 * 
	 * @param offender offender
	 * @param date date
	 * @return correctional status term for offender on date
	 */
	CorrectionalStatusTerm findCorrectionalStatusTermByOffenderOnDate(
			Offender offender, Date date);
	
	/**
	 * Returns supervisory organization terms for the specified offender.
	 * 
	 * @param offender offender
	 * @return supervisory organization terms for offender
	 */
	List<SupervisoryOrganizationTerm>
	findSupervisoryOrganizationTermsByOffender(Offender offender);
	
	/**
	 * Returns the supervisory organization term for the offender on the
	 * specified date.
	 * 
	 * @param offender offender
	 * @param date date
	 * @return supervisory organization term for offender on date
	 */
	SupervisoryOrganizationTerm
	findSupervisoryOrganizationTermByOffenderOnDate(Offender offender,
			Date date);
	
	/**
	 * Returns the placement term for the offender on the specified date
	 * 
	 * @param offender offender
	 * @param effectiveDate effective date
	 * @return placement term for offender on date
	 */
	PlacementTerm findPlacementTerm(Offender offender, Date effectiveDate);
	
	/**
	 * Returns allowed correctional statuses from a correctional status
	 * 
	 * @param fromCorrectionalStatus status offender is leaving
	 * @return allowed correctional status from a correctional status
	 */
	List<CorrectionalStatus> findAllowedCorrectionalStatuses(
			CorrectionalStatus fromCorrectionalStatus);
	
	/**
	 * Returns allowed placement term start change reasons when offender moves 
	 * from a correctional status to another
	 * 
	 * @param fromCorrectionalStatus status offender is leaving
	 * @param toCorrectionalStatus status offender is entering
	 * @return allowed placement term start change reasons
	 */
	List<PlacementTermChangeReason> findAllowedStartChangeReasons(
			CorrectionalStatus fromCorrectionalStatus, 
			CorrectionalStatus toCorrectionalStatus);
	
	/**
	 * Returns allowed placement term end change reasons when offender leaves 
	 * the specified correctional status
	 * 
	 * @param fromCorrectionalStatus status offender is leaving
	 * @return allowed placement term end change reasons
	 */
	List<PlacementTermChangeReason> findAllowedEndChangeReasons(
			CorrectionalStatus fromCorrectionalStatus);
	
	/**
	 * Returns placement term for offender with end date.
	 * 
	 * @param offender offender
	 * @param endDate end date
	 * @return placemnet term for offender with end date
	 */
	PlacementTerm findPlacementTermForOffenderWithEndDate(
			Offender offender, Date endDate);

	/**
	 * Returns location term for offender on date.
	 * 
	 * @param offender offender
	 * @param effectiveDate effective date
	 * @return location term for offender on date
	 */
	LocationTerm findLocationTermByOffenderOnDate(
			Offender offender, Date effectiveDate);
}