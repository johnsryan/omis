package omis.placement.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import omis.datatype.DateRange;
import omis.location.domain.Location;
import omis.locationterm.domain.LocationReason;
import omis.locationterm.domain.LocationReasonTerm;
import omis.locationterm.domain.LocationTerm;
import omis.locationterm.domain.LocationTermChangeAction;
import omis.locationterm.exception.EndedLocationTermException;
import omis.locationterm.exception.LocationReasonTermConflictException;
import omis.locationterm.exception.LocationReasonTermExistsException;
import omis.locationterm.exception.LocationTermConflictException;
import omis.locationterm.exception.LocationTermExistsException;
import omis.locationterm.service.delegate.AllowedLocationChangeDelegate;
import omis.locationterm.service.delegate.AllowedLocationChangeReasonRuleDelegate;
import omis.locationterm.service.delegate.LocationReasonDelegate;
import omis.locationterm.service.delegate.LocationReasonTermDelegate;
import omis.locationterm.service.delegate.LocationTermDelegate;
import omis.offender.domain.Offender;
import omis.placement.service.ChangeLocationService;
import omis.supervision.domain.CorrectionalStatus;
import omis.util.DateManipulator;

/**
 * Implementation of service to change locations.
 * 
 * @author Stephen Abson
 * @version 0.0.1 (Mar 5, 2015)
 * @since OMIS 3.0
 */
public class ChangeLocationServiceImpl
		implements ChangeLocationService {

	private final LocationReasonDelegate locationReasonDelegate;
	
	private final AllowedLocationChangeDelegate allowedLocationChangeDelegate;
	
	private final LocationTermDelegate locationTermDelegate;
	
	private final LocationReasonTermDelegate locationReasonTermDelegate;
	
	private final AllowedLocationChangeReasonRuleDelegate
	allowedLocationChangeReasonRuleDelegate;
	
	/**
	 * Instantiates an implementation of service to change locations.
	 * 
	 * @param locationReasonDelegate delegate for location reasons
	 * @param allowedLocationChangeDelegate delegate for allowed location
	 * changes
	 * @param locationTermDelegate delegate for location terms
	 * @param locationReasonTermDelegate delegate location reason terms
	 * @param allowedLocationChangeReasonRuleDelegate delegate for allowed
	 * location change reason rule
	 */
	public ChangeLocationServiceImpl(
			final LocationReasonDelegate locationReasonDelegate,
			final AllowedLocationChangeDelegate allowedLocationChangeDelegate,
			final LocationTermDelegate locationTermDelegate,
			final LocationReasonTermDelegate locationReasonTermDelegate,
			final AllowedLocationChangeReasonRuleDelegate
				allowedLocationChangeReasonRuleDelegate) {
		this.locationReasonDelegate = locationReasonDelegate;
		this.allowedLocationChangeDelegate = allowedLocationChangeDelegate;
		this.locationTermDelegate = locationTermDelegate;
		this.locationReasonTermDelegate = locationReasonTermDelegate;
		this.allowedLocationChangeReasonRuleDelegate
			= allowedLocationChangeReasonRuleDelegate;
	}

	/** {@inheritDoc} */
	@Override
	public LocationTerm change(final Offender offender,
			final Date effectiveDate, final Date endDate,
			final Location location, final LocationReason reason)
					throws LocationTermConflictException,
						EndedLocationTermException,
						LocationReasonTermConflictException,
						LocationTermExistsException,
						LocationReasonTermExistsException {
		
		// Checks for conflicts - throws exception if found
		LocationTerm locationTermOnStartDate = this.locationTermDelegate
				.findByOffenderOnDate(offender, effectiveDate);
		Date searchStartDate;
		List<LocationTerm> excludedLocationTerms
			= new ArrayList<LocationTerm>();
		if (locationTermOnStartDate != null) {
			excludedLocationTerms.add(locationTermOnStartDate);
			searchStartDate = DateManipulator.findEarliest(
					DateRange.getStartDate(
							locationTermOnStartDate.getDateRange()),
					effectiveDate);
		} else {
			searchStartDate = null;
		}
		long conflictCount;
		if (!excludedLocationTerms.isEmpty()) {
			conflictCount = this.locationTermDelegate
				.countExcluding(offender, searchStartDate, endDate,
						excludedLocationTerms.toArray(new LocationTerm[] { }));
		} else {
			conflictCount = this.locationTermDelegate
					.count(offender, searchStartDate, endDate);
		}
		if (conflictCount > 0) {
			throw new LocationTermConflictException(conflictCount
					+ " conflicting location term(s) exist");
		}
		LocationTerm locationTerm;
		if (locationTermOnStartDate != null
				&& location.equals(locationTermOnStartDate.getLocation())) {
			locationTerm = locationTermOnStartDate;
			Date locationTermEndDate = DateRange.getEndDate(
					locationTerm.getDateRange());
			if (locationTermEndDate != null && endDate != null
					&& endDate.compareTo(locationTermEndDate) > 0
					|| (locationTermEndDate != null && endDate == null)) {
				locationTerm = this.locationTermDelegate.changeDateRange(
						locationTerm, DateRange.getStartDate(
							locationTerm.getDateRange()), endDate);
			}
		} else {
			if (locationTermOnStartDate != null) {
				if (DateRange.getEndDate(
						locationTermOnStartDate.getDateRange()) == null) {
					locationTermOnStartDate = this.locationTermDelegate
						.changeDateRange(locationTermOnStartDate,
							DateRange.getStartDate(
									locationTermOnStartDate.getDateRange()),
							effectiveDate);
				} else {
					throw new EndedLocationTermException(
							"Location term on effective date ended");
				}
			}
			locationTerm = this.locationTermDelegate.create(
						offender, location, effectiveDate, endDate, false);
		}
		
		// Ends reason term on effective date if one exists and end date is null
		// Throws exception if end date is greater than start date
		LocationReasonTerm existingReasonTerm
			= this.locationReasonTermDelegate.findForOffenderOnDate(
					offender, effectiveDate);
		if (existingReasonTerm != null) {
			Date existingReasonTermEndDate = DateRange.getEndDate(
					existingReasonTerm.getDateRange());
			if (existingReasonTermEndDate == null) {
				try {
					existingReasonTerm = this.locationReasonTermDelegate
							.changeDateRange(existingReasonTerm,
									DateRange.getStartDate(
											existingReasonTerm.getDateRange()),
									effectiveDate);
				} catch (LocationReasonTermExistsException e) {

					// If two reason terms exist for the same location term
					// with the same start date, this is bad data - SA
					throw new AssertionError("Reason term exists", e);
				}	
			} else if (existingReasonTermEndDate.compareTo(effectiveDate) > 0) {
				throw new LocationReasonTermConflictException(
						"Conflicting reason terms exist");
			}
		}
		
		// Create new reason term if reason is specified
		if (reason != null) {
			LocationReasonTerm reasonTermOnStartDate
				= this.locationReasonTermDelegate.findForLocationTermOnDate(
						locationTerm, effectiveDate);
			if (reasonTermOnStartDate != null) {
				try {
					reasonTermOnStartDate = this.locationReasonTermDelegate
							.changeDateRange(reasonTermOnStartDate,
									DateRange.getStartDate(
										reasonTermOnStartDate.getDateRange()),
									effectiveDate);
				} catch (LocationReasonTermExistsException e) {

					// If two reason terms exist for the same location term
					// with the same start date, this is bad data - SA
					throw new AssertionError("Reason term exists", e);
				}
			}
			this.locationReasonTermDelegate.create(locationTerm,
					new DateRange(effectiveDate, endDate), reason);
		}
		
		// Returns location term
		return locationTerm;
	}

	/** {@inheritDoc} */
	@Override
	public List<LocationReason> findReasons() {
		return this.locationReasonDelegate.findAll();
	}

	/** {@inheritDoc} */
	@Override
	public List<Location> findLocationsAllowedForActionWithCorrectionalStatus(
			final LocationTermChangeAction action,
			final CorrectionalStatus correctionalStatus) {
		return this.allowedLocationChangeDelegate
				.findLocationsForActionForCorrectionalStatus(
						action, correctionalStatus);
	}

	/** {@inheritDoc} */
	@Override
	public List<LocationReason> findReasonsForChangeAction(
			final LocationTermChangeAction changeAction) {
		return this.allowedLocationChangeReasonRuleDelegate
				.findLocationReasonsForChangeAction(changeAction);
	}
}