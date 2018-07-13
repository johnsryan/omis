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
package omis.locationterm.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.datatype.DateRange;
import omis.exception.DateRangeOutOfBoundsException;
import omis.instance.factory.InstanceFactory;
import omis.location.domain.Location;
import omis.location.service.delegate.LocationDelegate;
import omis.locationterm.dao.LocationTermDao;
import omis.locationterm.domain.LocationReason;
import omis.locationterm.domain.LocationReasonTerm;
import omis.locationterm.domain.LocationTerm;
import omis.locationterm.domain.LocationTermChangeAction;
import omis.locationterm.domain.LocationTermChangeActionAssociation;
import omis.locationterm.exception.LocationReasonTermConflictException;
import omis.locationterm.exception.LocationReasonTermExistsAfterException;
import omis.locationterm.exception.LocationReasonTermExistsException;
import omis.locationterm.exception.LocationTermChangeActionAssociationExistsException;
import omis.locationterm.exception.LocationTermConflictException;
import omis.locationterm.exception.LocationTermExistsAfterException;
import omis.locationterm.exception.LocationTermExistsException;
import omis.locationterm.exception.LocationTermLockedException;
import omis.locationterm.service.LocationTermService;
import omis.locationterm.service.delegate.AllowedLocationChangeDelegate;
import omis.locationterm.service.delegate.AllowedLocationChangeReasonRuleDelegate;
import omis.locationterm.service.delegate.LocationReasonDelegate;
import omis.locationterm.service.delegate.LocationReasonTermDelegate;
import omis.locationterm.service.delegate.LocationTermChangeActionAssociationDelegate;
import omis.locationterm.service.delegate.LocationTermChangeActionDelegate;
import omis.offender.domain.Offender;
import omis.organization.domain.Organization;
import omis.region.domain.State;
import omis.region.service.delegate.StateDelegate;
import omis.supervision.domain.CorrectionalStatus;
import omis.supervision.domain.PlacementTerm;
import omis.supervision.domain.SupervisoryOrganization;
import omis.supervision.exception.OffenderNotUnderSupervisionException;
import omis.supervision.service.delegate.PlacementTermDelegate;
import omis.supervision.service.delegate.SupervisoryOrganizationDelegate;

/**
 * Implementation of service for location terms.
 * 
 * @author Stephen Abson
 * @author Josh Divine
 * @version 0.1.1 (Mar 6, 2017)
 * @since OMIS 3.0
 */
public class LocationTermServiceImpl
		implements LocationTermService {
	
	/* Resources. */

	private final LocationTermDao locationTermDao;
	
	private final InstanceFactory<LocationTerm> locationTermInstanceFactory;
	
	private final LocationReasonTermDelegate locationReasonTermDelegate;
	
	private final LocationReasonDelegate locationReasonDelegate;
	
	private final LocationTermChangeActionDelegate
	locationTermChangeActionDelegate;
	
	private final AllowedLocationChangeDelegate allowedLocationChangeDelegate;
	
	private final AllowedLocationChangeReasonRuleDelegate
	allowedLocationChangeReasonRuleDelegate;
	
	private final LocationTermChangeActionAssociationDelegate
	locationTermChangeActionAssociationDelegate;
	
	private final PlacementTermDelegate placementTermDelegate;
	
	private final SupervisoryOrganizationDelegate
	supervisoryOrganizationDelegate;
	
	private final LocationDelegate locationDelegate;
	
	private final StateDelegate stateDelegate;
	
	private final AuditComponentRetriever auditComponentRetriever;

	/* Constructors. */
	
	/**
	 * Instantiates an implementation of service for location terms.
	 * 
	 * @param locationTermDao data access object for location terms
	 * @param locationTermInstanceFactory instance factory for location terms
	 * @param locationReasonTermDelegate delegate for location reason terms
	 * @param locationReasonDelegate delegate for location reasons
	 * @param locationTermChangeActionDelegate delegate for location term
	 * change actions
	 * @param allowedLocationChangeDelegate delegate for allowed location
	 * changes
	 * @param allowedLocationChangeReasonRuleDelegate delegate for allowed
	 * location change reason rules
	 * @param locationTermChangeActionAssociationDelegate delegate for
	 * association of location term to change action
	 * @param placementTermDelegate delegate for placement terms
	 * @param supervisoryOrganizationDelegate delegate for supervisory
	 * organizations
	 * @param locationDelegate delegate for locations
	 * @param stateDelegate delegate for States
	 * @param auditComponentRetriever audit component retriever
	 */
	public LocationTermServiceImpl(
			final LocationTermDao locationTermDao,
			final InstanceFactory<LocationTerm> locationTermInstanceFactory,
			final LocationReasonTermDelegate locationReasonTermDelegate,
			final LocationReasonDelegate locationReasonDelegate,
			final LocationTermChangeActionDelegate
			locationTermChangeActionDelegate,
			final AllowedLocationChangeDelegate allowedLocationChangeDelegate,
			final AllowedLocationChangeReasonRuleDelegate
				allowedLocationChangeReasonRuleDelegate,
			final LocationTermChangeActionAssociationDelegate
				locationTermChangeActionAssociationDelegate,
			final PlacementTermDelegate placementTermDelegate,
			final SupervisoryOrganizationDelegate
				supervisoryOrganizationDelegate,
			final LocationDelegate locationDelegate,
			final StateDelegate stateDelegate,
			final AuditComponentRetriever auditComponentRetriever) {
		this.locationTermDao = locationTermDao;
		this.locationTermInstanceFactory = locationTermInstanceFactory;
		this.locationReasonTermDelegate = locationReasonTermDelegate;
		this.locationReasonDelegate = locationReasonDelegate;
		this.locationTermChangeActionDelegate
			= locationTermChangeActionDelegate;
		this.allowedLocationChangeReasonRuleDelegate
			= allowedLocationChangeReasonRuleDelegate;
		this.allowedLocationChangeDelegate = allowedLocationChangeDelegate;
		this.locationTermChangeActionAssociationDelegate
			= locationTermChangeActionAssociationDelegate;
		this.placementTermDelegate = placementTermDelegate;
		this.supervisoryOrganizationDelegate = supervisoryOrganizationDelegate;
		this.locationDelegate = locationDelegate;
		this.stateDelegate = stateDelegate;
		this.auditComponentRetriever = auditComponentRetriever;
	}
	
	/* Method implementations. */
	
	/** {@inheritDoc} */
	@Override
	public List<LocationTerm> findByOffender(final Offender offender) {
		return this.locationTermDao.findByOffender(offender);
	}

	/** {@inheritDoc} */
	@Override
	public List<Location> findLocations() {
		
		// TODO: Remove - SA
		throw new UnsupportedOperationException(
				"Finding locations not supported");
	}
	
	/** {@inheritDoc} */
	@Override
	public List<Location> findLocationsByOrganization(
			final Organization organization) {
		return this.locationDelegate.findByOrganization(organization);
	}

	/** {@inheritDoc} */
	@Override
	public void remove(final LocationTerm locationTerm) {
		
		// Removes change action association
		this.locationTermChangeActionAssociationDelegate.removeByLocationTerm(
				locationTerm);
		
		// Removes term reasons
		this.locationReasonTermDelegate.removeByLocationTerm(locationTerm);
		
		// Removes location term
		this.locationTermDao.makeTransient(locationTerm);
	}

	/** {@inheritDoc} */
	@Override
	public LocationTerm create(
			final Offender offender,
			final Location location,
			final DateRange dateRange)
					throws LocationTermExistsException,
						LocationTermConflictException, 
						LocationTermExistsAfterException, 
						OffenderNotUnderSupervisionException {
		// Throws exception if duplicate location term exists
		if (this.locationTermDao.find(offender, 
				DateRange.getStartDate(dateRange),
				DateRange.getEndDate(dateRange)) != null) {
			throw new LocationTermExistsException("Location term exists");
		}
		
		// Prevents dates from being equal unless both are null
		Date startDate = DateRange.getStartDate(dateRange);
		Date endDate = DateRange.getEndDate(dateRange);
		if (startDate != null && startDate.equals(endDate)) {
			throw new IllegalArgumentException(
					"Start and end date cannot be equal");
		}
		
		// Throw exception if the offender does not have a current placement for
		// the specified date.
		if (startDate != null) {
			if(this.placementTermDelegate
					.findForOffenderOnDate(offender, startDate) == null) {
				throw new OffenderNotUnderSupervisionException("Offender is not"
						+ " under supervision on the specified date");
			}
		}
		
		LocationTerm startLocationTerm = this.locationTermDao
				.findByOffenderOnDate(offender, startDate);
		//Throws an exception if location terms exist after the start date and 
		//the end date is null
		if (endDate == null) {
			long locationCount = this.locationTermDao.countAfterDateExcluding(
					offender, startDate, startLocationTerm);
			if (locationCount > 0) {
				throw new LocationTermExistsAfterException(locationCount + 
					" location term(s) exist after the specified date range.");
			}
		}
		if (startLocationTerm != null) {
			//Throws an exception if an existing location term matches the term 
			//that is to be updated
			if (this.locationTermDao.findExcluding(offender, 
					DateRange.getStartDate(startLocationTerm.getDateRange()), 
					endDate, startLocationTerm) != null) {
				throw new LocationTermConflictException("Updated location term "
						+ "in conflict with existing location term.");
			}
			
			//Get end location term when possible
			LocationTerm endLocationTerm = null;
			if (endDate != null) {
				endLocationTerm = this.locationTermDao.findByOffenderOnDate(
						offender, endDate);
				ArrayList<LocationTerm> excludedLocationTerms 
					= new ArrayList<LocationTerm>();
				excludedLocationTerms.add(startLocationTerm);
				if (endLocationTerm != null) {
					excludedLocationTerms.add(endLocationTerm);
				}
				// Throws an exception if terms exist between the start and end
				// dates that can not be automatically adjusted
				long existingCount = this.locationTermDao.countExcluding(
						offender, startDate, endDate, 
						excludedLocationTerms.toArray(new LocationTerm[0]));
				if (existingCount > 0) {
					throw new LocationTermConflictException("Date span covers " 
							+ existingCount + " existing location term(s) that "
									+ "can not be adjusted automatically.");
				}
			}
			//Update the existing location term end date to the passed in start 
			//date
			startLocationTerm.setDateRange(new DateRange(
					DateRange.getStartDate(startLocationTerm.getDateRange()), 
					startDate));
			startLocationTerm.setUpdateSignature(new UpdateSignature(
					this.auditComponentRetriever.retrieveUserAccount(),
					this.auditComponentRetriever.retrieveDate()));
			this.locationTermDao.makePersistent(startLocationTerm);
			
			//Update the existing end location term if it exists
			if (endLocationTerm != null) {
				endLocationTerm.setDateRange(new DateRange(endDate, 
						DateRange.getEndDate(endLocationTerm.getDateRange())));
				endLocationTerm.setUpdateSignature(new UpdateSignature(
						this.auditComponentRetriever.retrieveUserAccount(),
						this.auditComponentRetriever.retrieveDate()));
			}
			
		}
		
		// Updates the existing location reason term if it exists
		LocationReasonTerm activeLocationReasonTerm 
		= this.locationReasonTermDelegate.findForOffenderOnDate(offender, 
				startDate);
		if (activeLocationReasonTerm != null) {
			try {
				this.locationReasonTermDelegate.update(activeLocationReasonTerm, 
						new DateRange(DateRange.getStartDate(
								activeLocationReasonTerm.getDateRange()), 
								startDate),
						activeLocationReasonTerm.getReason());
			} catch (LocationReasonTermExistsException e) {
				
				// It should not be possible for two reason terms for the same
				// location term to exist with the same start date
				// This would be bad data and unrecoverable in the application
				throw new AssertionError("Conflicting reason term exists", e);
			}
		}
		
		// Creates new location term
		LocationTerm locationTerm = this.locationTermInstanceFactory
				.createInstance();
		locationTerm.setCreationSignature(new CreationSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
		locationTerm.setUpdateSignature(new UpdateSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
		locationTerm.setOffender(offender);
		locationTerm.setLocation(location);
		locationTerm.setDateRange(dateRange);
		locationTerm.setLocked(false);
		return this.locationTermDao.makePersistent(locationTerm);
	}

	/** {@inheritDoc}  */
	@Override
	public LocationTerm update(final LocationTerm locationTerm,
			final Location location, final DateRange dateRange)
					throws LocationTermExistsException,
						LocationTermConflictException, 
						LocationTermExistsAfterException,
						LocationTermLockedException, 
						OffenderNotUnderSupervisionException {
		// Prevents update of locked location term
		if (locationTerm.getLocked()) {
			throw new LocationTermLockedException("Location term locked");
		}
		
		// Prevents dates from being equal unless both are null
		Date startDate = DateRange.getStartDate(dateRange);
		Date endDate = DateRange.getEndDate(dateRange);
		if (startDate != null && startDate.equals(endDate)) {
			throw new IllegalArgumentException(
					"Start and end date cannot be equal");
		}
		
		// Throw exception if the offender does not have a current placement for
		// the specified date.
		if (startDate != null) {
			if(this.placementTermDelegate.findForOffenderOnDate(
					locationTerm.getOffender(), startDate) == null) {
				throw new OffenderNotUnderSupervisionException("Offender is not"
						+ " under supervision on the specified date");
			}
		}
		
		// Throws exception if duplicate location term exists
		if (this.locationTermDao.findExcluding(locationTerm.getOffender(),
				DateRange.getStartDate(dateRange),
				DateRange.getEndDate(dateRange), locationTerm) != null) {
			throw new LocationTermExistsException("Location term exists");
		}
		
		// Throws exception if conflicting location terms exist (exclude
		// location terms that start on end date)
		LocationTerm locationTermStartingOnEndDate
			= this.locationTermDao.findWithStartDate(
					locationTerm.getOffender(),
					DateRange.getEndDate(dateRange));
		long conflictingCount;
		if (locationTermStartingOnEndDate != null) {
			conflictingCount = this.locationTermDao.countExcluding(
					locationTerm.getOffender(), DateRange.getStartDate(dateRange),
					DateRange.getEndDate(dateRange), locationTerm,
					locationTermStartingOnEndDate);
		} else {
			conflictingCount = this.locationTermDao.countExcluding(
					locationTerm.getOffender(), DateRange.getStartDate(dateRange),
					DateRange.getEndDate(dateRange), locationTerm);
		}
		if (conflictingCount > 0) {
			throw new LocationTermConflictException(
					conflictingCount + " conflicting location term(s) exist");
		}
		
		// Throws exception if location terms exist after
		if (endDate == null) {
			long existingTermsAfterDateCount = this.locationTermDao
					.countAfterDateExcluding(locationTerm.getOffender(), 
							DateRange.getStartDate(dateRange), locationTerm);
			if (existingTermsAfterDateCount > 0) {
				throw new LocationTermExistsAfterException(
						existingTermsAfterDateCount + " location term(s) exist "
								+ "after the specified date range.");
			}
		}
		
		// Updates existing location term
		locationTerm.setUpdateSignature(new UpdateSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
		locationTerm.setLocation(location);
		locationTerm.setDateRange(dateRange);
		return this.locationTermDao.makePersistent(locationTerm);
	}

	/** {@inheritDoc} */
	@Override
	public LocationTermChangeActionAssociation associateChangeAction(
				final LocationTerm locationTerm,
				final LocationTermChangeAction changeAction)
			throws LocationTermChangeActionAssociationExistsException {
		return this.locationTermChangeActionAssociationDelegate
				.create(locationTerm, changeAction);
	}
	
	/** {@inheritDoc} */
	@Override
	public LocationReasonTerm createReasonTerm(
			final LocationTerm locationTerm, final DateRange dateRange,
			final LocationReason reason)
				throws LocationReasonTermExistsException, 
				LocationReasonTermConflictException, 
				DateRangeOutOfBoundsException,
				LocationReasonTermExistsAfterException {
		// Throws exception if entity already exists
		if (this.locationReasonTermDelegate.find(
				locationTerm.getOffender(), locationTerm,
				DateRange.getStartDate(dateRange),
				DateRange.getEndDate(dateRange)) != null) {
			throw new LocationReasonTermExistsException(
					"Location reason term exists");
		}
		
		// Throws exception if end date is null and terms exist after the start
		// date
		if (DateRange.getEndDate(dateRange) == null) {
			long existingCount = this.locationReasonTermDelegate
					.countAfterDateExcluding(locationTerm.getOffender(), 
							DateRange.getStartDate(dateRange), null);
			if (existingCount > 0) {
				throw new LocationReasonTermExistsAfterException(existingCount 
						+ " location reason term(s) exist after the specified "
						+ "date range.");
			}
		}
		
		// Throws exception if conflicts exist
		long countConflicting = this.locationReasonTermDelegate.count(
				locationTerm, DateRange.getStartDate(dateRange),
				DateRange.getEndDate(dateRange));
		if (countConflicting > 0) {
			throw new LocationReasonTermConflictException(countConflicting
					+ " conflicting location reason term(s) exist");
		}
		
		// Throws exception if date range of reason term is outside of location
		// term
		if (!DateRange.occursWithin(dateRange, locationTerm.getDateRange())) {
			throw new DateRangeOutOfBoundsException(
					"Location reason term is outside of date range of"
					+ " location term");
		}
		
		return this.locationReasonTermDelegate.create(
				locationTerm, dateRange, reason);
	}
	
	/** {@inheritDoc} */
	@Override
	public LocationReasonTerm updateReasonTerm(
			final LocationReasonTerm reasonTerm, final DateRange dateRange,
			final LocationReason reason)
				throws LocationReasonTermExistsException, 
					LocationReasonTermConflictException, 
					DateRangeOutOfBoundsException, 
					LocationReasonTermExistsAfterException {
		
		// Throws an exception if entity already exists
		if (this.locationReasonTermDelegate.findExcluding(
				reasonTerm.getOffender(),
				reasonTerm.getLocationTerm(),
				DateRange.getStartDate(dateRange),
				DateRange.getEndDate(dateRange),
				reasonTerm) != null) {
			throw new LocationReasonTermExistsException(
					"Location reason term exists");
		}
	
		// Throws exception if end date is null and terms exist after the start
		// date
		if (DateRange.getEndDate(dateRange) == null) {
			long existingCount = this.locationReasonTermDelegate
					.countAfterDateExcluding(reasonTerm.getOffender(), 
							DateRange.getStartDate(dateRange), reasonTerm);
			if (existingCount > 0) {
				throw new LocationReasonTermExistsAfterException(existingCount 
						+ " location reason term(s) exist after the specified "
						+ "date range.");
			}
		}
		
		// Throws exception if conflicts exist - exclude reason term with
		// start date of end date
		LocationReasonTerm reasonTermStartingOnEndDate
			= this.locationReasonTermDelegate
				.findWithStartDate(reasonTerm.getLocationTerm(),
						DateRange.getEndDate(dateRange));
		LocationReasonTerm[] excludedReasonTerms;
		if (reasonTermStartingOnEndDate != null) {
			excludedReasonTerms = new LocationReasonTerm[] {
				reasonTerm, reasonTermStartingOnEndDate
			};
		} else {
			excludedReasonTerms = new LocationReasonTerm[] { reasonTerm };
		}
		long countConflicting = this.locationReasonTermDelegate.countExcluding(
				reasonTerm.getLocationTerm(), 
				DateRange.getStartDate(dateRange),
				DateRange.getEndDate(dateRange),
				excludedReasonTerms);
		if (countConflicting > 0) {
			throw new LocationReasonTermConflictException(countConflicting
					+ " conflicting location reason term(s) exist");
		}
		
		// Throws exception if date range of reason term is outside of location
		// term
		if (!DateRange.occursWithin(dateRange, reasonTerm.getLocationTerm()
				.getDateRange())) {
			throw new DateRangeOutOfBoundsException(
					"Location reason term is outside of date range of"
					+ " location term");
		}
		return this.locationReasonTermDelegate.update(
				reasonTerm, dateRange, reason);
	}

	/** {@inheritDoc} */
	@Override
	public void removeReasonTerm(final LocationReasonTerm reasonTerm) {
		this.locationReasonTermDelegate.remove(reasonTerm);
	}

	/** {@inheritDoc} */
	@Override
	public List<LocationReason> findReasons() {
		return this.locationReasonDelegate.findAll();
	}

	/** {@inheritDoc} */
	@Override
	public List<LocationReasonTerm> findReasonTerms(
			final LocationTerm locationTerm) {
		return this.locationReasonTermDelegate.findByLocationTerm(locationTerm);
	}

	/** {@inheritDoc} */
	@Override
	public PlacementTerm findPlacementTermForOffenderOnDate(
			final Offender offender, final Date effectiveDate) {
		return this.placementTermDelegate.findForOffenderOnDate(
				offender, effectiveDate);
	}

	/** {@inheritDoc} */
	@Override
	public List<Location> findLocationsForCorrectionalStatus(
			final CorrectionalStatus correctionalStatus) {
		List<SupervisoryOrganization> organizations
			= this.supervisoryOrganizationDelegate
				.findForCorrectionalStatus(correctionalStatus);
		if (organizations.size() > 0) {
			return this.locationDelegate
				.findByOrganizations(
						organizations.toArray(
								new SupervisoryOrganization[] { }));
		} else{
			return Collections.emptyList();
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public List<Location> findLocationsForCorrectionalStatusInState(
			final CorrectionalStatus correctionalStatus,
			final State state) {
		List<SupervisoryOrganization> organizations
		= this.supervisoryOrganizationDelegate
			.findForCorrectionalStatus(correctionalStatus);
		if (organizations.size() > 0) {
			return this.locationDelegate
				.findByOrganizationsInState(
						state,
						organizations.toArray(
								new SupervisoryOrganization[] { }));
		} else{
			return Collections.emptyList();
		}
	}

	/** {@inheritDoc} */
	@Override
	public LocationTerm findForOffenderOnDate(
			final Offender offender, final Date effectiveDateTime) {
		return this.locationTermDao.findByOffenderOnDate(
				offender, effectiveDateTime);
	}

	/** {@inheritDoc} */
	@Override
	public State findHomeState() {
		return this.stateDelegate.findHomeState();
	}

	/** {@inheritDoc} */
	@Override
	public List<State> findHomeStates() {
		return this.stateDelegate.findInHomeCountry();
	}

	/** {@inheritDoc} */
	@Override
	public List<LocationTermChangeAction> findChangeActions() {
		return this.locationTermChangeActionDelegate.findAll();
	}

	/** {@inheritDoc} */
	@Override
	public List<Location> findLocationsForActionForCorrectionalStatus(
			final LocationTermChangeAction action,
			final CorrectionalStatus correctionalStatus) {
		return this.allowedLocationChangeDelegate
				.findLocationsForActionForCorrectionalStatus(
						action, correctionalStatus);
	}

	/** {@inheritDoc} */
	@Override
	public List<Location> findLocationsForActionForCorrectionalStatusInState(
			final LocationTermChangeAction action,
			final CorrectionalStatus correctionalStatus,
			final State state) {
		return this.allowedLocationChangeDelegate
				.findLocationsForActionForCorrectionalStatusInState(
						action, correctionalStatus, state);
	}

	/** {@inheritDoc} */
	@Override
	public List<LocationReason> findReasonsForChangeAction(
			final LocationTermChangeAction changeAction) {
		return this.allowedLocationChangeReasonRuleDelegate
				.findLocationReasonsForChangeAction(changeAction);
	}
	
	/** {@inheritDoc} */
	@Override
	public List<LocationReason> findReasonsAllowedForLocation(
			final Location location) {
		return this.allowedLocationChangeReasonRuleDelegate
				.findLocationReasonsAllowedForLocation(location);
	}

	/** {@inheritDoc} */
	@Override
	public List<Location> findLocationsAllowedForPlacement() {
		List<SupervisoryOrganization> organizations
			= this.supervisoryOrganizationDelegate
				.findAllowedForPlacement();
		if (organizations.size() > 0) {
			return this.locationDelegate
				.findByOrganizations(
						organizations.toArray(
								new SupervisoryOrganization[] { }));
		} else{
			return Collections.emptyList();
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public List<Location> findLocationsAllowedForPlacementInState(
			final State state) {
		List<SupervisoryOrganization> organizations
			= this.supervisoryOrganizationDelegate
				.findAllowedForPlacementInState(state);
		if (organizations.size() > 0) {
			return this.locationDelegate
				.findByOrganizations(
						organizations.toArray(
								new SupervisoryOrganization[] { }));
		} else{
			return Collections.emptyList();
		}
	}
}