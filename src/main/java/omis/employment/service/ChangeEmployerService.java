package omis.employment.service;

import java.util.List;

import omis.address.domain.Address;
import omis.address.domain.ZipCode;
import omis.address.exception.AddressExistsException;
import omis.address.exception.ZipCodeExistsException;
import omis.country.domain.Country;
import omis.employment.domain.Employer;
import omis.employment.domain.EmploymentTerm;
import omis.employment.exception.EmployerExistsException;
import omis.employment.exception.EmploymentExistsException;
import omis.location.exception.LocationExistsException;
import omis.organization.domain.Organization;
import omis.organization.exception.OrganizationExistsException;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.region.exception.CityExistsException;


/**
 * Change Employer Service.
 * 
 * @author: Yidong Li
 * @author Josh Divine
 * @version: 0.1.1 (Dec 14, 2017)
 * @since: OMIS 3.0
 */

public interface ChangeEmployerService {
	
	/** 
	 * Change employer.
	 * 
	 * @param employmentTerm employment term 
	 * @param employer employer
	 * @return employment term
	 */
	EmploymentTerm change(EmploymentTerm employmentTerm, Employer employer) 
		throws EmploymentExistsException;
	
	/** 
	 * Create an employer.
	 * 
	 * @param name name 
	 * @param telephoneNumber telephone number
	 * @param address address
	 * @return employer 
	 * @throws OrganizationExistsException 
	 * @throws LocationExistsException 
	 */
	Employer createEmployer(String name, Long telephoneNumber, Address address) 
		throws EmployerExistsException, OrganizationExistsException, 
		LocationExistsException;
	
	/** 
	 * Create a new address.
	 * 
	 * @param value value 
	 * @param zipCode zip code
	 * @return new created address 
	 */
	Address createAddress(String value, ZipCode zipCode)
		throws AddressExistsException;
	
	/** 
	 * Find all states in a specified country.
	 * 
	 * @param country country
	 * @return list of states 
	 */
	List<State> findStatesByCountry(Country country);
	
	/** 
	 * Find all cities in a specified country.
	 * 
	 * @param country country
	 * @return  list of cities 
	 */
	List<City> findCitiesByCountry(Country country);
	
	/** 
	 * Check if there are states in a specified country.
	 * 
	 * @param country country
	 * @return boolean 
	 */
	Boolean hasStates(Country country);
	
	/** 
	 * Find all countries.
	 * 
	 * @return list of countries 
	 */
	List<Country> findCountries();
	
	/**
	 * Create a city.
	 * 
	 * @param country country
	 * @param state state
	 * @return city 
	 */
	City createCity(String name, State state, Country country)
		throws CityExistsException;
	
	/** 
	 * Create a zip code.
	 * 
	 * @param value value
	 * @param extension extension
	 * @param city city
	 * @return zip code
	 */
	ZipCode createZipCode(String value, String extension, City city)
		throws ZipCodeExistsException;
	
	/** 
	 * Find zip codes in a specified city.
	 * 
	 * @param city city
	 * @return list of zip codes 
	 */
	List<ZipCode> findZipCodesByCity(City city);
	
	/** 
	 * Find cities in a specified state.
	 * 
	 * @param state state
	 * @return list of cities 
	 */
	List<City> findCitiesByState(State state);
	
	/** 
	 * Find organizations by partial name.
	 * 
	 * @param partialName partial name
	 * @return list of organization.
	 */
	List<Organization> findOrganizationsByPartialName(String partialName);
}