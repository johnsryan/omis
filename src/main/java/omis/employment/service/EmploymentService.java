package omis.employment.service;

import java.util.Date;
import java.util.List;

import omis.address.domain.Address;
import omis.address.domain.ZipCode;
import omis.address.exception.AddressExistsException;
import omis.address.exception.ZipCodeExistsException;
import omis.audit.domain.VerificationMethod;
import omis.audit.domain.VerificationSignature;
import omis.country.domain.Country;
import omis.datatype.DateRange;
import omis.employment.domain.Employer;
import omis.employment.domain.EmploymentChangeReason;
import omis.employment.domain.EmploymentNote;
import omis.employment.domain.EmploymentTerm;
import omis.employment.domain.component.Job;
import omis.employment.exception.EmployerExistsException;
import omis.employment.exception.EmploymentExistsException;
import omis.employment.exception.EmploymentNoteExistsException;
import omis.exception.DuplicateEntityFoundException;
import omis.location.exception.LocationExistsException;
import omis.offender.domain.Offender;
import omis.organization.domain.Organization;
import omis.organization.exception.OrganizationExistsException;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.region.exception.CityExistsException;

/**
 * Employment Service.
 * 
 * @author: Yidong Li
 * @author Josh Divine
 * @version: 0.1.1 (Dec 14, 2017)
 * @since: OMIS 3.0
 */
public interface EmploymentService {

	/**
	 * Create an employment term.
	 * 
	 * @param offender offender
	 * @param employer employer
	 * @param dateRange date range
	 * @param job job
	 * @param convictedOfEmployerTheft "yes" or "no" to convictedOfEmployerTheft
	 * @param endReason reason of terminating employment
	 * @param verificationSignature verification signature
	 * @return employmentTerm, new created employment term 
	 * @throws EmploymentExistsException if duplicate employment exists
	 */
	EmploymentTerm create(Offender offender, Employer employer,
		DateRange dateRange, Job job, Boolean convictedOfEmployerTheft, 
		EmploymentChangeReason endReason, VerificationSignature
		verificationSignature) throws EmploymentExistsException;
	
	/** 
	 * Update an employment term.
	 * 
	 * @param employmentTerm employment term
	 * @param dateRange date range
	 * @param job job
	 * @param convictedOfEmployerTheft, "yes" or "no" to convicted of employment
	 * @param endReason reason of terminating employment
	 * @param verificationSignature verification signature
	 * @return employmentTerm, new updated employment term 
	 * @throws EmploymentExistsException if duplicate employment exists
	 */
	EmploymentTerm update(EmploymentTerm employmentTerm, DateRange dateRange,
		Job job, Boolean convictedOfEmployerTheft, 
		EmploymentChangeReason endReason, 
		VerificationSignature verificationSignature)
		throws EmploymentExistsException;

	/** 
	 * Add a note to an employment term.
	 * 
	 * @param term, employment term
	 * @param value value
	 * @param date date
	 * @return EmploymentNote employment note 
	 * @throws EmploymentNoteExistsException if duplicate note exists
	 */
	EmploymentNote addNote(EmploymentTerm term, String value, Date date)
		throws EmploymentNoteExistsException;
	
	/** 
	 * Update employment note.
	 * 
	 * @param note note  
	 * @param value value 
	 * @param date date
	 * @return EmploymentNote employment note
	 * @throws EmploymentNoteExistsException if duplicate note exists
	 */
	EmploymentNote updateNote(EmploymentNote note, String value, 
		Date date )	throws EmploymentNoteExistsException;
	
	/** 
	 * Remove employment note.
	 * 
     * @param note note
     */
	void removeNote(EmploymentNote note);

    /** 
     * Create an employer.
     * 
     * @param name employer name
     * @param telephoneNumber telephone number
     * @param address employer address
     * @throws EmployerExistsException if duplicate employer exists 
     * @throws OrganizationExistsException 
     * @throws LocationExistsException 
     */
	Employer createEmployer(String name, Long telephoneNumber, Address address) 
		throws EmployerExistsException, OrganizationExistsException, 
		LocationExistsException;
	
    /** 
     * Create an address.
     * 
     * @param value value
     * @param zipCode zip code
     * @throws AddressExistsException if duplicate address exists
     */
	Address createAddress(String value, ZipCode zipCode)
			throws AddressExistsException; 
    
	/** 
	 * Find notes by employment term.
	 * 
     * @param term employment term  
     * @return list of employment notes
     */
    List<EmploymentNote> findAdditionalNotes(EmploymentTerm term);
    
    /** 
     * Remove employment term.
     * 
     * @param employmentTerm employment term.
     */
    void remove(EmploymentTerm employmentTerm);
     
    /** 
     * Find employment term by offender.
     * 
     * @param offender offender
     * @return list of employment terms 
     */
    List<EmploymentTerm> findByOffender(Offender offender);
    
    /** 
     * Find employment change reason.
     * 
     * @return list of employment change reasons 
     */
	List<EmploymentChangeReason> findEmploymentChangeReasons( );
	
	/** 
	 * Find all verification methods.
	 * 
	 * @return a list of verification methods. 
	 */
	List<VerificationMethod> findVerificationMethods();
	
	/** 
	 * Find all countries.
	 * 
	 * @return a list of countries. 
	 */
	List<Country> findCountries();
	
	/** 
	 * Find all states in a specified country.
	 * 
	 * @param country country
	 * @return a list of states
	 */
	List<State> findStatesByCountry(Country country);
	
	/** 
	 * Find all cities in a specified country.
	 * 
	 * @param country country
	 * @return a list of cities
	 */
	List<City> findCitiesByCountry(Country country);
	
	/** 
	 * Check if there are states in a specified country.
	 * 
	 * @param country country
	 * @return true or false. 
	 */
	Boolean hasStates(Country country);
	
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
	 * @return list of zip codes. 
	 */
	List<ZipCode> findZipCodesByCity(City city);
	
	/** 
	 * Find cities in a specified state.
	 * 
	 * @param state state
	 * @return list of cities. 
	 */
	List<City> findCitiesByState(State state);
	
	/** 
	 * Find organizations by partial name.
	 * 
	 * @param partialName partial name
	 * @return list of organization. 
	 */
	List<Organization> findOrganizationByPartialName(String partialName);
	
	/** 
	 * Find addresses by entered query.
	 * 
	 * @param query partial name
	 * @return list of address. 
	 */
	List<Address> findAddresses(String query);
	
	/**
	 * Returns home State.
	 * 
	 * @return home State
	 */
	State findHomeState();	
}
