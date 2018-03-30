package omis.facility.service.delegate;

import java.util.Date;
import java.util.List;

import omis.exception.DuplicateEntityFoundException;
import omis.facility.dao.FacilityDao;
import omis.facility.domain.Facility;
import omis.instance.factory.InstanceFactory;
import omis.location.domain.Location;
import omis.organization.domain.Organization;

/**
 * Delegate for facilities.
 *
 * @author Stephen Abson
 * @author Annie Jacques
 * @author Josh Divine
 * @author Sheronda Vaughn
 * @version 0.0.2 (Aug 2, 2017)
 * @since OMIS 3.0
 */
public class FacilityDelegate {

	/* Data access objects. */
	
	private final FacilityDao facilityDao;
	
	private final InstanceFactory<Facility> facilityInstanceFactory;
	
	/* Constructors. */
	
	/**
	 * Instantiates delegate for facilities.
	 * 
	 * @param facilityDao delegate for facilities
	 */
	public FacilityDelegate(final FacilityDao facilityDao,
			final InstanceFactory<Facility> facilityInstanceFactory) {
		this.facilityDao = facilityDao;
		this.facilityInstanceFactory = facilityInstanceFactory;
	}
	
	/* Methods. */
	
	/**
	 * Returns facility at location.
	 * 
	 * @param location location
	 * @return facility at location
	 */
	public Facility findByLocation(final Location location) {
		return this.facilityDao.findFacilityByLocation(location);
	}

	/**
	 * Returns facilities for organization between dates.
	 * 
	 * @param organization organization
	 * @param startDate start date
	 * @param endDate end date
	 * @return facilities for organization between dates
	 */
	public List<Facility> findByOrganizationBetweenDates(
			final Organization organization, final Date startDate,
			final Date endDate) {
		return this.facilityDao.findByOrganizationBetweenDates(
				organization, startDate, endDate);
	}
	
	/**
	 * Returns facilities.
	 * 
	 * @return facilities
	 */
	public List<Facility> findAll() {
		return this.facilityDao.findAll();
	}

	/**
	 * Returns facilities by organization.
	 * 
	 * @param organization organization
	 * @return facilities by organization
	 */
	public List<Facility> findByOrganization(
			final Organization organization) {
		return this.facilityDao.findByOrganization(organization);
	}
	
	/**
	 * Creates a new facility.
	 * 
	 * @param location location
	 * @param name name
	 * @param abbreviation abbreviation
	 * @param active active
	 * @return facility
	 * @throws DuplicateEntityFoundException if entity already exists
	 */
	public Facility create(final Location location, final String name,
			final String abbreviation, final Boolean active, 
			final Long telephoneNumber) 
					throws DuplicateEntityFoundException{
		if (this.facilityDao.find(name, location) != null) {
			throw new DuplicateEntityFoundException(
					"The facility already exists.");
		}
		Facility facility = this.facilityInstanceFactory.createInstance();
		populateFacility(facility, location, name, abbreviation, active, 
				telephoneNumber);
		return this.facilityDao.makePersistent(facility);
	}
	
	/**
	 * Updates an existing facility.
	 * 
	 * @param facility facility
	 * @param location location
	 * @param name name
	 * @param abbreviation abbreviation
	 * @param active active
	 * @return facility
	 * @throws DuplicateEntityFoundException if entity already exists
	 */
	public Facility update(final Facility facility, final Location location, 
			final String name, final String abbreviation, final Boolean active,
			final Long telephoneNumber) 
					throws DuplicateEntityFoundException {
		if (this.facilityDao.findExcluding(name, location, facility) != null) {
			throw new DuplicateEntityFoundException(
					"The facility already exists.");
		}
		populateFacility(facility, location, name, abbreviation, active, 
				telephoneNumber);
		return this.facilityDao.makePersistent(facility);
	}

	/**
	 * Removes the specified facility.
	 * 
	 * @param facility facility
	 */
	public void remove(final Facility facility) {
		this.facilityDao.makeTransient(facility);
	}
	
	// Populates a facility
	private void populateFacility(final Facility facility, 
			final Location location, final String name, 
			final String abbreviation, final Boolean active, 
			final Long telephoneNumber) {
		facility.setLocation(location);
		facility.setName(name);
		facility.setAbbreviation(abbreviation);
		facility.setActive(active);
		facility.setTelephoneNumber(telephoneNumber);
	}	
}