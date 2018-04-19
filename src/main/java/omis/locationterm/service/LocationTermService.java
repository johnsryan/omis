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
package omis.locationterm.service;

import java.util.Date;
import java.util.List;

import omis.datatype.DateRange;
import omis.exception.DateRangeOutOfBoundsException;
import omis.location.domain.Location;
import omis.locationterm.domain.LocationReason;
import omis.locationterm.domain.LocationReasonTerm;
import omis.locationterm.domain.LocationTerm;
import omis.locationterm.domain.LocationTermChangeAction;
import omis.locationterm.exception.LocationReasonTermConflictException;
import omis.locationterm.exception.LocationReasonTermExistsAfterException;
import omis.locationterm.exception.LocationReasonTermExistsException;
import omis.locationterm.exception.LocationTermConflictException;
import omis.locationterm.exception.LocationTermExistsAfterException;
import omis.locationterm.exception.LocationTermExistsException;
import omis.locationterm.exception.LocationTermLockedException;
import omis.offender.domain.Offender;
import omis.organization.domain.Organization;
import omis.region.domain.State;
import omis.supervision.domain.CorrectionalStatus;
import omis.supervision.domain.PlacementTerm;
import omis.supervision.exception.OffenderNotUnderSupervisionException;

/**
 * Service for location terms.
 * 
 * @author Stephen Abson
 * @version 0.1.0 (Jan 15, 2014)
 * @since OMIS 3.0
 */
public interface LocationTermService {

	/**
	 * Returns location terms for offender.
	 * 
	 * @param offender offender
	 * @return location terms for offender
	 */
	List<LocationTerm> findByOffender(Offender offender);
	
	/**
	 * Returns locations.
	 * 
	 * <p>This method should be deprecated and replaced with one that returns
	 * only locations active on a given date.
	 * 
	 * @return locations
	 */
	List<Location> findLocations();
	
	/**
	 * Returns locations by organization.
	 * 
	 * <p>This method should be deprecated and replaced with one that returns
	 * only locations active on a given date.
	 * 
	 * @param organization organization
	 * @return locations by organization
	 */
	List<Location> findLocationsByOrganization(Organization organization);
	
	/**
	 * Creates location term.
	 * 
	 * <p>If not null, the start and end date of the date range are prevented
	 * from being equal by a {@code IllegalArgumentException} being thrown.
	 * 
	 * <p>Updates reason term active on start date to be ended on start date.
	 * 
	 * @param offender offender
	 * @param location location
	 * @param dateRange date range
	 * @return location term
	 * @throws LocationTermExistsException if location term exists
	 * @throws LocationTermConflictException if conflicting location terms exist
	 */
	LocationTerm create(Offender offender, Location location,
			DateRange dateRange)
					throws LocationTermExistsException,
						LocationTermConflictException,
						LocationTermExistsAfterException,
						OffenderNotUnderSupervisionException;
	
	/**
	 * Updates location term.
	 * 
	 * <p>If not null, the start and end date of the date range are prevented
	 * from being equal by a {@code IllegalArgumentException} being thrown.
	 * 
	 * @param locationTerm location term
	 * @param location location
	 * @param dateRange date range
	 * @return location term
	 * @throws LocationTermExistsException if location term exists
	 * @throws LocationTermConflictException if conflicting location terms exist
	 * @throws LocationTermExistsAfterException if existing location terms exist 
	 * after the start date when the end date is null
	 * @throws LocationTermLockedException if location term is locked
	 */
	LocationTerm update(LocationTerm locationTerm, Location location,
			DateRange dateRange)
					throws LocationTermExistsException,
						LocationTermConflictException,
						LocationTermExistsAfterException,
						LocationTermLockedException,
						OffenderNotUnderSupervisionException;
	
	/**
	 * Removes a location term.
	 * 
	 * <p>Reason terms associated with the location term are also removed.
	 * 
	 * @param locationTerm location term to remove
	 */
	void remove(LocationTerm locationTerm);
	
	/**
	 * Creates location reason term.
	 * 
	 * @param locationTerm location term
	 * @param dateRange date range
	 * @param reason reason 
	 * @return location reason term
	 * @throws LocationReasonTermExistsException if reason term exists
	 * @throws DateRangeOutOfBoundsException if date range exists out side of 
	 * location term date range
	 * @throws LocationReasonTermConflictException if overlapping location 
	 * reason terms
	 * @throws LocationReasonTermExistsAfterException date range is not ended 
	 * and exists location reason terms exist after the start date
	 */
	LocationReasonTerm createReasonTerm(LocationTerm locationTerm,
			DateRange dateRange, LocationReason reason)
					throws LocationReasonTermExistsException,
						LocationReasonTermConflictException,
						LocationReasonTermExistsAfterException,
						DateRangeOutOfBoundsException;
	
	/**
	 * Updates reason term.
	 * 
	 * @param reasonTerm reason term to update
	 * @param dateRange date range
	 * @param reason reason
	 * @return updated reason term
	 * @throws LocationReasonTermExistsException if reason term exists
	 * @throws DateRangeOutOfBoundsException if date range exists out side of 
	 * location term date range
	 * @throws LocationReasonTermConflictException if overlapping location 
	 * reason terms
	 * @throws LocationReasonTermExistsAfterException date range is not ended 
	 * and exists location reason terms exist after the start date
	 */
	LocationReasonTerm updateReasonTerm(LocationReasonTerm reasonTerm,
			DateRange dateRange, LocationReason reason)
				throws LocationReasonTermExistsException,
					LocationReasonTermExistsAfterException,
					LocationReasonTermConflictException,
					DateRangeOutOfBoundsException;

	/**
	 * Removes reason term.
	 * 
	 * @param reasonTerm reason term to remove
	 */
	void removeReasonTerm(LocationReasonTerm reasonTerm);
	
	/**
	 * Returns reasons.
	 * 
	 * @return reasons
	 */
	List<LocationReason> findReasons();

	/**
	 * Returns reason terms for location term.
	 * 
	 * @param locationTerm location term
	 * @return reason terms for location term
	 */
	List<LocationReasonTerm> findReasonTerms(LocationTerm locationTerm);
	
	/**
	 * Returns placement term for offender on date.
	 * 
	 * @param offender offender
	 * @param effectiveDate effective date
	 * @return placement term for offender on date
	 */
	PlacementTerm findPlacementTermForOffenderOnDate(
			Offender offender, Date effectiveDate);

	/**
	 * Returns locations for correctional status.
	 * 
	 * @param correctionalStatus correctional status
	 * @return locations for correctional status
	 */
	List<Location> findLocationsForCorrectionalStatus(
			CorrectionalStatus correctionalStatus);

	/**
	 * Returns locations for correctional status in State.
	 * 
	 * @param correctionalStatus correctional status
	 * @param state State
	 * @return locations for correctional status
	 */
	List<Location> findLocationsForCorrectionalStatusInState(
			CorrectionalStatus correctionalStatus, State state);
	
	/**
	 * Returns location term for offender on date.
	 * 
	 * @param offender offender
	 * @param effectiveDate effective date
	 * @return location term for offender on date
	 */
	LocationTerm findForOffenderOnDate(
			Offender offender, Date effectiveDateTime);
	
	/**
	 * Returns home State.
	 * 
	 * @return home State
	 */
	State findHomeState();
	
	/**
	 * Returns home States.
	 * 
	 * @return home States
	 */
	List<State> findHomeStates();
	
	/**
	 * Returns location term change actions.
	 * 
	 * @return location term change actions
	 */
	List<LocationTermChangeAction> findChangeActions();
	
	/**
	 * Returns locations allowed for action while on correctional status.
	 * 
	 * @param action action
	 * @param correctionalStatus correctional status
	 * @return allowed locations
	 */
	List<Location> findLocationsForActionForCorrectionalStatus(
			LocationTermChangeAction action,
			CorrectionalStatus correctionalStatus);
	
	/**
	 * Returns locations allowed for action while on correctional status in
	 * State.
	 * 
	 * @param action action
	 * @param correctionalStatus correctional status
	 * @param state State
	 * @return allowed locations
	 */
	List<Location> findLocationsForActionForCorrectionalStatusInState(
			LocationTermChangeAction action,
			CorrectionalStatus correctionalStatus,
			State state);

	/**
	 * Returns location reasons for change action.
	 * 
	 * @param changeAction change action
	 * @return location reasons for change action
	 */
	List<LocationReason> findReasonsForChangeAction(
			LocationTermChangeAction changeAction);
	
	/**
	 * Returns reasons allowed for location.
	 * 
	 * @param location location
	 * @return reasons allowed for location
	 */
	List<LocationReason> findReasonsAllowedForLocation(Location location);
	
	/**
	 * Returns locations allowed for placement.
	 *  
	 * <p>Effectively, this methods returns any location at which an offender
	 * can be housed.
	 *  
	 * @return locations allowed for placement
	 */
	List<Location> findLocationsAllowedForPlacement();
	
	/**
	 * Returns locations allowed for placement in State.
	 *  
	 * <p>Effectively, this methods returns any location at which an offender
	 * can be housed in State.
	 * 
	 * @param state State
	 * @return locations allowed for placement in State
	 */
	List<Location> findLocationsAllowedForPlacementInState(State state);
}