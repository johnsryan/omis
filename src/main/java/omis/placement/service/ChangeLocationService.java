package omis.placement.service;

import java.util.Date;
import java.util.List;

import omis.location.domain.Location;
import omis.locationterm.domain.LocationReason;
import omis.locationterm.domain.LocationTerm;
import omis.locationterm.domain.LocationTermChangeAction;
import omis.locationterm.exception.EndedLocationTermException;
import omis.locationterm.exception.LocationReasonTermConflictException;
import omis.locationterm.exception.LocationReasonTermExistsException;
import omis.locationterm.exception.LocationTermConflictException;
import omis.locationterm.exception.LocationTermExistsException;
import omis.offender.domain.Offender;
import omis.supervision.domain.CorrectionalStatus;

/**
 * Service to change locations.
 *
 * @author Stephen Abson
 * @version 0.0.1 (Mar 4, 2015)
 * @since OMIS 3.0
 */
public interface ChangeLocationService {
	
	/**
	 * Changes location term of offender on date.
	 * 
	 * <p>Existing location term of offender on date will be ended with
	 * {@code effectiveDate}.
	 * 
	 * @param offender offender
	 * @param effectiveDate effective date
	 * @param endDate end date
	 * @param location location
	 * @param reason location reason
	 * @return location term
	 * @throws LocationTermConflictException if conflicting location terms
	 * exist
	 * @throws EndedLocationTermException if location term exists in effective
	 * date that is ended
	 * @throws LocationReasonTermConflictException if conflicting reason terms
	 * exist
	 * @throws LocationReasonTermExistsException if reason term exists for
	 * changed location term
	 * @throws LocationTermExistsException if location term to change to exists
	 * @throws LocationReasonTermExistsException if reason term exists
	 * for location term
	 */
	LocationTerm change(Offender offender, Date effectiveDate, Date endDate,
			Location location, LocationReason reason)
				throws LocationTermConflictException,
					EndedLocationTermException,
					LocationReasonTermConflictException,
					LocationTermExistsException,
					LocationReasonTermExistsException;
	
	/**
	 * Returns locations allowed for action with correctional status.
	 * 
	 * @param action action
	 * @param correctionalStatus correctional status
	 * @return locations allowed for action with correctional status
	 */
	List<Location> findLocationsAllowedForActionWithCorrectionalStatus(
			LocationTermChangeAction action,
			CorrectionalStatus correctionalStatus);
	
	/**
	 * Returns reasons.
	 * 
	 * @return reasons
	 */
	List<LocationReason> findReasons();
	
	/**
	 * Returns reasons for change action.
	 * 
	 * @param changeAction change action
	 * @return reasons for change action
	 */
	List<LocationReason> findReasonsForChangeAction(
			LocationTermChangeAction changeAction);
}