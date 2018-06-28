package omis.employment.service.impl;

import java.util.List;

import omis.address.domain.Address;
import omis.address.domain.ZipCode;
import omis.address.exception.AddressExistsException;
import omis.address.exception.ZipCodeExistsException;
import omis.address.service.delegate.AddressDelegate;
import omis.address.service.delegate.ZipCodeDelegate;
import omis.country.domain.Country;
import omis.country.service.delegate.CountryDelegate;
import omis.employment.domain.Employer;
import omis.employment.domain.EmploymentTerm;
import omis.employment.exception.EmployerExistsException;
import omis.employment.exception.EmploymentExistsException;
import omis.employment.service.ChangeEmployerService;
import omis.employment.service.delegate.EmployerDelegate;
import omis.employment.service.delegate.EmploymentTermDelegate;
import omis.location.domain.Location;
import omis.location.exception.LocationExistsException;
import omis.location.service.delegate.LocationDelegate;
import omis.organization.domain.Organization;
import omis.organization.exception.OrganizationExistsException;
import omis.organization.service.delegate.OrganizationDelegate;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.region.exception.CityExistsException;
import omis.region.service.delegate.CityDelegate;
import omis.region.service.delegate.StateDelegate;

/**
 * Implementation of change employer service.
 * 
 * @author: Yidong Li
 * @author Josh Divine
 * @version: 0.1.1 (Dec 14, 2017)
 * @since: OMIS 3.0
 */

public class ChangeEmployerServiceImpl implements ChangeEmployerService {
	
	/* Delegates. */
	
	private final EmploymentTermDelegate employmentTermDelegate;
	private final EmployerDelegate employerDelegate;
	private final OrganizationDelegate organizationDelegate;
	private final LocationDelegate locationDelegate;
	private final AddressDelegate addressDelegate;
	private final CountryDelegate countryDelegate;
	private final StateDelegate stateDelegate;
	private final CityDelegate cityDelegate;
	private final ZipCodeDelegate zipCodeDelegate;
	
	/**
	 * Instantiates an instance of change employer service.
	 * 
	 * @param employmentTermDelegate employment term data access object
	 * @param employerDelegate employer delegate
	 * @param organizationDelegate organization delegate
	 * @param locationDelegate location delegate
	 * @param addressDelegate address delegate
	 * @param countryDelegate country delegate
	 * @param stateDelegate state delegate
	 * @param cityDelegate city delegate
	 * @param zipCodeDelegate zip code delegate
	 */
	public ChangeEmployerServiceImpl(
			final EmploymentTermDelegate employmentTermDelegate,
			final EmployerDelegate employerDelegate,
			final OrganizationDelegate organizationDelegate,
			final LocationDelegate locationDelegate,
			final AddressDelegate addressDelegate,
			final CountryDelegate countryDelegate,
			final StateDelegate stateDelegate,
			final CityDelegate cityDelegate,
			final ZipCodeDelegate zipCodeDelegate) {
		this.employmentTermDelegate = employmentTermDelegate;
		this.employerDelegate = employerDelegate;
		this.organizationDelegate = organizationDelegate;
		this.locationDelegate = locationDelegate;
		this.addressDelegate = addressDelegate;
		this.countryDelegate = countryDelegate;
		this.stateDelegate = stateDelegate;
		this.cityDelegate = cityDelegate;
		this.zipCodeDelegate = zipCodeDelegate;
	}	
	
	/** {@inheritDoc} */
	@Override
	public EmploymentTerm change(final EmploymentTerm employmentTerm, 
		final Employer employer) throws EmploymentExistsException{
		return this.employmentTermDelegate.change(employmentTerm, employer);
	}
	
	/** {@inheritDoc} 
	 * @throws OrganizationExistsException 
	 * @throws LocationExistsException */
	@Override
	public Employer createEmployer(final String name, final Long telephoneNumber, 
		final Address address) throws EmployerExistsException, 
			OrganizationExistsException, LocationExistsException {
		if (this.employerDelegate.findByNameAndAddress(name, address) != null) {
			throw new EmployerExistsException("Employer Already Exist");
		}
		Organization organization = this.organizationDelegate.findByName(name);
		if(organization == null){
			organization = this.organizationDelegate.create(name, null, null);
		}
		Location location = this.locationDelegate.create(organization, null, 
				address);
		return this.employerDelegate.create(location, telephoneNumber);
	}
	
	/** {@inheritDoc} */
	@Override
	public Address createAddress(final String value, final ZipCode zipCode) 
		throws AddressExistsException {
		return this.addressDelegate.findOrCreate(value, null, null, null, 
			zipCode);
	}
	
	/** {@inheritDoc} */
	@Override
	public List<State> findStatesByCountry(final Country country){
		return this.stateDelegate.findByCountry(country);
	}
	
	/** {@inheritDoc} */
	@Override
	public List<City> findCitiesByCountry(final Country country){
		return this.cityDelegate.findByCountry(country);
	}
	
	/** {@inheritDoc} */
	@Override
	public Boolean hasStates(final Country country){
		if(this.stateDelegate.countByCountry(country)==0){
			return true;
		}
		else
			return false;
	}
	
	/** {@inheritDoc} */
	@Override
	public List<Country> findCountries(){
		return this.countryDelegate.findAll();
	}
	
	/** {@inheritDoc} */
	@Override
	public City createCity(final String name, final State state, 
		final Country country) throws CityExistsException{
		return this.cityDelegate.create(name, true, state, country);
	}
	
	/** {@inheritDoc} */
	@Override
	public ZipCode createZipCode(final String value, final String extension, 
		final City city) throws ZipCodeExistsException{
		return this.zipCodeDelegate.create(city, value, extension, true);
	}
	
	/** {@inheritDoc} */
	@Override
	public List<ZipCode> findZipCodesByCity(final City city){
		return this.zipCodeDelegate.findByCity(city);
	}
	
	/** {@inheritDoc} */
	@Override
	public List<City> findCitiesByState(final State state){
		return this.cityDelegate.findByState(state);
	}
	
	/** {@inheritDoc} */
	@Override
	public List<Organization> findOrganizationsByPartialName(
			final String partialName) {
		return this.organizationDelegate.findByPartialName(partialName);
	}
}