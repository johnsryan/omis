package omis.residence.service;

import java.util.Date;
import java.util.List;

import omis.address.domain.Address;
import omis.address.domain.BuildingCategory;
import omis.address.domain.ZipCode;
import omis.audit.domain.VerificationMethod;
import omis.audit.domain.VerificationSignature;
import omis.datatype.DateRange;
import omis.exception.DuplicateEntityFoundException;
import omis.location.domain.Location;
import omis.location.exception.LocationNotAllowedException;
import omis.offender.domain.Offender;
import omis.organization.domain.Organization;
import omis.person.domain.Person;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.residence.domain.NonResidenceTerm;
import omis.residence.domain.ResidenceStatus;
import omis.residence.domain.ResidenceTerm;
import omis.residence.exception.PrimaryResidenceExistsException;
import omis.residence.exception.ResidenceStatusConflictException;
import omis.residence.exception.ResidenceTermExistsException;

/**
 * Service for residences. 
 *
 * @author Stephen Abson
 * @author Sheronda Vaughn
 * @author Josh Divine
 * @version 0.0.2 (Sept 19, 2017)
 * @since OMIS 3.0
 */
public interface ResidenceService {

	/**
	 * Create a new residence term.
	 * 
	 * @param person person
	 * @param dateRange date range
	 * @param primary primary
	 * @param address address
	 * @param fosterCare foster care
	 * @param confirmed confirmed
	 * @param notes notes
	 * @param verificationSignature verification signature
	 * @return new residence term
	 * @throws DuplicateEntityFoundException duplicate entity found exception
	 * @throws PrimaryResidenceExistsException 
	 * primary residence exists exception
	 * @throws ResidenceStatusConflictException 
	 * residence status conflict exception
	 * @throws ResidenceTermExistsException residence term exists exception
	 */
	ResidenceTerm createResidenceTerm(Person person, DateRange dateRange,
			Boolean primary, Address address, Boolean fosterCare, 
			Boolean confirmed, String notes, 
			VerificationSignature verificationSignature)
			throws DuplicateEntityFoundException,
			PrimaryResidenceExistsException, ResidenceStatusConflictException,
			ResidenceTermExistsException;
	
	/**
	 * Update a residence term.
	 * 
	 * @param residenceTerm residence term
	 * @param dateRange date range
	 * @param primary primary
	 * @param address address
	 * @param fosterCare foster care
	 * @param confirmed confirmed
	 * @param notes notes
	 * @param verificationSignature verification signature
	 * @return update residence term
	 * @throws DuplicateEntityFoundException duplicate entity found exception
	 * @throws PrimaryResidenceExistsException 
	 * primary residence exists exception
	 * @throws ResidenceStatusConflictException 
	 * residence status conflict exception
	 * @throws ResidenceTermExistsException residence term exists exception
	 */
	ResidenceTerm updateResidenceTerm(ResidenceTerm residenceTerm, 
			DateRange dateRange, Boolean primary, Address address, 
			Boolean fosterCare, Boolean confirmed, String notes, 
			VerificationSignature verificationSignature)
			throws DuplicateEntityFoundException, ResidenceTermExistsException,
			PrimaryResidenceExistsException;
	
	/**
	 * Create an address.
	 * 
	 * @param number number
	 * @param designator designator
	 * @param coordinates coordinates	 
	 * @param category category
	 * @param zipCode zip code
	 * @return new address
	 * @throws DuplicateEntityFoundException duplicate entity found exception
	 */
	Address createAddress(String number, String designator, String coordinates,
			BuildingCategory category, ZipCode zipCode)
			throws DuplicateEntityFoundException;
	
	/**
	 * Update an address.
	 * 
	 * @param address address
	 * @param number number
	 * @param designator designator
	 * @param coordinates coordinates
	 * @param category category
	 * @param zipCode zip code
	 * @return update address
	 * @throws DuplicateEntityFoundException duplicate entity found exception
	 */
	Address updateAddress(Address address, String number, String designator,
			String coordinates,
			BuildingCategory category, ZipCode zipCode)
			throws DuplicateEntityFoundException;
	
	/**
	 * Create a non residential term.
	 * 
	 * @param person person
	 * @param dateRange date range
	 * @param status status
	 * @param location location
	 * @param confirmed confirmed
	 * @param notes notes 
	 * @param verificationSignature verification signature
	 * @return new non residential term 
	 * @throws DuplicateEntityFoundException duplicate entity found exception
	 * @throws LocationNotAllowedException location not allowed exception
	 * @throws ResidenceStatusConflictException 
	 * residence status conflict exception
	 */
	NonResidenceTerm createNonResidenceTerm(Person person, 
			DateRange dateRange, ResidenceStatus status, Location location,
			Boolean confirmed, String notes, 
			VerificationSignature verificationSignature)
			throws DuplicateEntityFoundException,
			LocationNotAllowedException, ResidenceStatusConflictException;	
	
	/**
	 * Update a non residence term.
	 * 
	 * @param nonResidenceTerm non residence term
	 * @param dateRange date range
	 * @param status status
	 * @param location location
	 * @param confirmed confirmed
	 * @param notes notes 
	 * @param verificationSignature verification signature
	 * @return update non residence term
	 * @throws DuplicateEntityFoundException duplicate entity found exception
	 * @throws LocationNotAllowedException location not allowed exception
	 * @throws ResidenceStatusConflictException 
	 * residence status conflict exception
	 */
	NonResidenceTerm updateNonResidenceTerm(NonResidenceTerm nonResidenceTerm,
			DateRange dateRange, ResidenceStatus status, Location location,
			Boolean confirmed, String notes, 
			VerificationSignature verificationSignature)
			throws DuplicateEntityFoundException,
			LocationNotAllowedException, ResidenceStatusConflictException;
	
	/**
	 * Create a homeless non residence term.
	 * 
	 * @param person person
	 * @param dateRange date range
	 * @param city city
	 * @param state state
	 * @param notes notes
	 * @return new homeless non residence term
	 * @throws DuplicateEntityFoundException duplicate entity found exception
	 * @throws ResidenceStatusConflictException 
	 * residence status conflict exception
	 */
	NonResidenceTerm createHomelessTerm(Person person, DateRange dateRange, 
			City city, State state, String notes, Boolean confirmed)
			throws DuplicateEntityFoundException, 
			ResidenceStatusConflictException;
	
	/**
	 * Update a homeless non residence term.
	 * 
	 * @param nonResidenceTerm non residence term
	 * @param dateRange date range
	 * @param city city
	 * @param state state
	 * @param notes notes
	 * @return update homeless non residence term
	 * @throws DuplicateEntityFoundException duplicate entity found exception
	 * @throws ResidenceStatusConflictException 
	 * residence status conflict exception
	 */
	NonResidenceTerm updateHomelessTerm(NonResidenceTerm nonResidenceTerm,
			DateRange dateRange, City city, State state, String notes, 
			Boolean confirmed)
			throws DuplicateEntityFoundException, 
			ResidenceStatusConflictException;
	
	/**
	 * Returns a list of allowed locations.
	 * 
	 * @param residenceStatus residence status
	 * @return list of locations.
	 */
	List<Location> findAllowedLocations(ResidenceStatus residenceStatus);
	
	/**
	 * Returns a list of allowed locations within a state.
	 * 
	 * @param state state
	 * @param residenceStatus residence status
	 * @return list of locations.
	 */
	List<Location> findAllowedLocationsInState(State state, 
			ResidenceStatus residenceStatus);
	
	/**
	 * Returns a list of allowed locations within a city.
	 * 
	 * @param city city
	 * @param residenceStatus residence status
	 * @return list of locations.
	 */
	List<Location> findAllowedLocationsInCity(City city, 
			ResidenceStatus residenceStatus);
	
	/**
	 * Remove the non residence term.
	 * 
	 * @param nonResidenceTerm non residence term
	 */
	void removeNonResidenceTerm(NonResidenceTerm nonResidenceTerm);
	
	/**
	 * Remove the residence term.
	 * 
	 * @param residenceTerm residence term
	 */
	void removeResidenceTerm(ResidenceTerm residenceTerm);
	
	/**
	 * Returns a list of zip codes within a state.
	 * 
	 * @param state state
	 * @return list of zip codes.
	 */
	List<ZipCode> findZipCodesInState(State state);
	
	/**
	 * Returns a list of zip codes within a city.
	 * 
	 * @param city city
	 * @return list of zip codes.
	 */
	List<ZipCode> findZipCodesInCity(City city);
	
	/**
	 * Returns a list of states within home country.
	 * 
	 * @return list of states.
	 */
	List<State> findStatesInHomeCountry();
	
	/**
	 * Returns a list of cities within a state.
	 * 
	 * @param state state
	 * @return list of cities.
	 */
	List<City> findCitiesInState(State state);
	
	/**
	 * Returns a list of verification methods.
	 *
	 *
	 * @return list of verification methods
	 */
	List<VerificationMethod> findAllVerificationMethods();
	
	/**
	 * Returns home state.
	 * 
	 * @return home state
	 */
	State findHomeState();
	
	/**
	 * Creates a new location.
	 *
	 * @param organization organization
	 * @param dateRange date range
	 * @param address address
	 * @return new location
	 * @throws DuplicateEntityFoundException 
	 */
	Location createLocation(String organizationName, DateRange dateRange, 
			Address address, ResidenceStatus status) 
					throws DuplicateEntityFoundException;

	/**
	 * Updates an existing location.
	 *
	 * @param location location
	 * @param organization organization
	 * @param dateRange date range
	 * @param address address
	 * @return updated location
	 * @throws DuplicateEntityFoundException 
	 */
	Location updateLocation(Location location, Organization organization, 
			DateRange dateRange, Address address) 
					throws DuplicateEntityFoundException;

	/**
	 * Finds list of residence terms by offender and date.
	 *
	 * @param offender offender
	 * @param date date
	 * @return list of residence terms
	 */
	List<ResidenceTerm> findResidenceTermsByOffender(
			Offender offender, Date date);	
	
	/**
	 * Find the primary residence for the specified person on the specified date.
	 * 
	 * @param date effective date
	 * @param person person
	 * @return primary residence term
	 */
	ResidenceTerm findPrimaryResidence(Date date, Person person);
	
	/**
	 * Find all non residence terms for the specified person on the specified date.
	 * 
	 * @param date effective date
	 * @param person person
	 * @return list of non residence terms
	 */
	List<NonResidenceTerm> findNonResidenceTerms(Date date, Person person);

	/**
	 * Applies the specified date to the end date of the specified non residence term.
	 * 
	 * @param term non residence term
	 * @param endDate end date
	 * @return ended non residence term
	 * @throws DuplicateEntityFoundException thrown when a duplicate non residence term is found
	 */
	NonResidenceTerm endNonResidenceTerm(NonResidenceTerm term, Date endDate)
		throws DuplicateEntityFoundException;
}