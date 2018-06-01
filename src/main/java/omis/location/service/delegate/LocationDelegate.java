package omis.location.service.delegate;

import java.util.List;

import omis.address.domain.Address;
import omis.datatype.DateRange;
import omis.instance.factory.InstanceFactory;
import omis.location.dao.LocationDao;
import omis.location.domain.Location;
import omis.location.exception.LocationExistsException;
import omis.organization.domain.Organization;
import omis.region.domain.State;

/**
 * Delegate for locations.
 *
 * @author Stephen Abson
 * @author Josh Divine
 * @version 0.0.2 (Nov 28, 2017)
 * @since OMIS 3.0
 */
public class LocationDelegate {

	/* Data access object. */
	
	private final LocationDao locationDao;
	
	/* Instance factory. */
	
	private final InstanceFactory<Location> locationInstanceFactory;
	
	/* Constructors. */
	
	/**
	 * Instantiates delegate for locations.
	 * 
	 * @param locationDao data access object for locations
	 * @param locationInstanceFactory instance factory for locations
	 */
	public LocationDelegate(
			final LocationDao locationDao,
			final InstanceFactory<Location> locationInstanceFactory) {
		this.locationDao = locationDao;
		this.locationInstanceFactory = locationInstanceFactory;
	}
	
	/* Methods. */
	
	/**
	 * Creates location.
	 * 
	 * @param organization organization
	 * @param dateRange date range
	 * @param address address
	 * @return created location
	 * @throws LocationExistsException if location exists
	 */
	public Location create(final Organization organization,
			final DateRange dateRange, final Address address)
				throws LocationExistsException {
		Location location = this.locationInstanceFactory
				.createInstance();
		this.populate(location, organization, dateRange, address);
		return this.locationDao.makePersistent(location);
	}
	
	/**
	 * Updates location.
	 * 
	 * @param location location
	 * @param organization organization
	 * @param dateRange date range
	 * @param address address
	 * @return updated location
	 * @throws LocationExistsException if location exists
	 */
	public Location update(final Location location,
			final Organization organization, final DateRange dateRange,
			final Address address)
				throws LocationExistsException {
		this.populate(location, organization, dateRange, address);
		return this.locationDao.makePersistent(location);
	}
	
	/**
	 * Removes location.
	 * 
	 * @param location location
	 */
	public void remove(final Location location) {
		this.locationDao.makeTransient(location);
	}
	
	/**
	 * Returns locations by organization.
	 * 
	 * @param organization organization
	 * @return locations by organization
	 */
	public List<Location> findByOrganization(
			final Organization organization) {
		return this.locationDao.findByOrganization(organization);
	}

	/**
	 * Returns locations.
	 * 
	 * @return locations
	 */
	public List<Location> findAll() {
		return this.locationDao.findAll();
	}
	
	/* Helper methods */
	
	// Populates location
	private void populate(final Location location,
			final Organization organization, final DateRange dateRange,
			final Address address) {
		location.setOrganization(organization);
		location.setDateRange(dateRange);
		location.setAddress(address);
	}

	/**
	 * Returns locations by organizations.
	 * 
	 * @param organizations organizations
	 * @return locations by organizations
	 */
	public List<Location> findByOrganizations(
			final Organization... organizations) {
		return this.locationDao.findByOrganizations(organizations);
	}
	
	/**
	 * Returns locations by organizations in State.
	 * 
	 * @param state State
	 * @param organizations organizations
	 * @return locations by organizations
	 */
	public List<Location> findByOrganizationsInState(
			final State state, final Organization... organizations) {
		return this.locationDao.findByOrganizationsInState(
				state, organizations);
	}

	/**
	 * Returns locations associated with jails.
	 * 
	 * @return locations associated with jails
	 */
	public List<Location> findJailLocations() {
		return this.locationDao.findJailLocations();
	}

	/**
	 * Returns locations associated with prerelease locations.
	 * 
	 * @return locations associated with prerelease locations
	 */
	public List<Location> findPrereleaseLocations() {
		return this.locationDao.findPrereleaseLocations();
	}

	/**
	 * Returns locations associated with facilities.
	 * 
	 * @return locations associated with facilities
	 */
	public List<Location> findFacilityLocations() {
		return this.locationDao.findFacilityLocations();
	}

	/**
	 * Returns locations associated with community supervision offices.
	 * 
	 * @return locations associated with community supervision offices
	 */
	public List<Location> findCommunitySupervisionOfficeLocations() {
		return this.locationDao.findCommunitySupervisionOfficeLocations();
	}

	/**
	 * Returns locations associated with treatment and sanction centers.
	 * 
	 * @return locations associated with treatment and sanction centers
	 */
	public List<Location> findTreatmentAndSactionCenterLocations() {
		return this.locationDao.findTreatmentAndSactionCenterLocations();
	}

	/**
	 * Returns all board itinerary locations.
	 * 
	 * @return all board itinerary locations
	 */
	public List<Location> findAllBoardItineraryLocations() {
		return this.locationDao.findAllBoardItineraryLocations();
	}
}