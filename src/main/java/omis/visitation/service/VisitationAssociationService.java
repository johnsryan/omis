package omis.visitation.service;

import java.util.Date;
import java.util.List;

import omis.address.domain.Address;
import omis.address.domain.ZipCode;
import omis.contact.domain.Contact;
import omis.contact.domain.component.PoBox;
import omis.country.domain.Country;
import omis.demographics.domain.Sex;
import omis.exception.DateConflictException;
import omis.exception.DuplicateEntityFoundException;
import omis.offender.domain.Offender;
import omis.person.domain.Person;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.relationship.exception.ReflexiveRelationshipException;
import omis.residence.domain.ResidenceTerm;
import omis.residence.exception.PrimaryResidenceExistsException;
import omis.residence.exception.ResidenceStatusConflictException;
import omis.residence.exception.ResidenceTermExistsException;
import omis.visitation.domain.VisitationApproval;
import omis.visitation.domain.VisitationAssociation;
import omis.visitation.domain.VisitationAssociationCategory;
import omis.visitation.domain.VisitationAssociationFlags;

/**
 * Visitation association service.
 * 
 * @author Joel Norris
 * @author Yidong Li
 * @author Josh Divine
 * @version 0.1.1 (Aug 29, 2017)
 * @since OMIS 3.0
 */
public interface VisitationAssociationService {

	/**
	 * Associates the specified offender with the specified visitor.
	 * 
	 * @param offender offender
	 * @param visitor visitor
	 * @param category category
	 * @param approval approval
	 * @param startDate start date
	 * @param endDate end date
	 * @param flags flags
	 * @param notes notes
	 * @param guardianship guardianship
	 * @return newly created visitation association
	 * @throws DuplicateEntityFoundException thrown when a duplicate visitation
	 * association is found
	 * @throws DateConflictException thrown when a visitation association with
	 * a relationship containing the specified offender and visitor is found
	 * that overlaps the specified start date or end date
	 * @throws ReflexiveRelationshipException thrown when a relationship is 
	 * created with person one being the same as person two
	 */
	VisitationAssociation associate(Offender offender, Person visitor, 
			VisitationAssociationCategory category, VisitationApproval approval,
			Date startDate, Date endDate, VisitationAssociationFlags flags,
			String notes, String guardianship) 
					throws DuplicateEntityFoundException, DateConflictException, 
					ReflexiveRelationshipException;
	
	/**
	 * Updates the specified visitation association.
	 * 
	 * @param association visitation association
	 * @param category visitation association category
	 * @param approval visitation approval
	 * @param flags visitation association flags
	 * @param startDate start date
	 * @param endDate end date
	 * @param notes notes
	 * @param guardianship guardianship
	 * @return updated visitation association
	 * @throws DuplicateEntityFoundException thrown when a duplicate visitation
	 * association is found
	 * @throws DateConflictException thrown when a visitation association with
	 * a relationship containing the specified offender and visitor is found
	 * that overlaps the specified start date or end date, excluding the
	 * specified visitation association
	 */
	VisitationAssociation update(VisitationAssociation association, 
			VisitationAssociationCategory category, VisitationApproval approval,
			Date startDate, Date endDate, VisitationAssociationFlags flags,
			String notes, String guardianship) 
	throws DuplicateEntityFoundException, DateConflictException;
	
	/**
	 * Removes the specified visitation association.
	 * 
	 * @param association visitation association
	 */
	void remove(VisitationAssociation association);
	
	/**
	 * Returns a list of visitation association categories.
	 * 
	 * @return list of categories
	 */
	List<VisitationAssociationCategory> findCategories();
	
	/**
	 * Creates a new visitor ({@link Person}).
	 * 
	 * @param lastName last name
	 * @param firstName first name
	 * @param middleName middle name
	 * @param suffix suffix
	 * @param socialSecurityNumber social security number
	 * @param birthDate birth date
	 * @param birthCity birth city
	 * @param birthState birth state
	 * @param birthCountry birth country
	 * @param sex sex
	 * @param stateIdNumber state identification number
	 * @param deathDate deathDate
	 * @param deceased deceased
	 * @return newly created visitor {@code Person}
	 */
	Person createVisitor(String lastName, String firstName, 
			String middleName, String suffix, Integer socialSecurityNumber, 
			Date birthDate, City birthCity, State birthState,
			Country birthCountry, Sex sex, String stateIdNumber,
			Date deathDate, Boolean deceased);
	
	/**
	 * Updates the specified visitor ({@link Person}).
	 * 
	 * @param visitor visitor
	 * @param lastName last name
	 * @param firstName first name
	 * @param middleName middle name
	 * @param suffix suffix
	 * @param socialSecurityNumber social security number
	 * @param birthDate birth date
	 * @param birthCity birth city
	 * @param birthState birth state
	 * @param birthCountry birth country
	 * @param sex sex
	 * @param stateIdNumber state identification number
	 * @param deathDate deathDate
	 * @param deceased deceased
	 * @return updated person
	 * @throws DuplicateEntityFoundException thrown when a duplicate person
	 * is found
	 */
	Person updateVisitor(Person visitor, String lastName, String firstName,
			String middleName, String suffix, Integer socialSecurityNumber, 
			Date birthDate, City birthCity, State birthState,
			Country birthCountry, Sex sex, String stateIdNumber, Date deathDate,
			Boolean deceased)
		throws DuplicateEntityFoundException;
	
	/**
	 * Creates a new {@link Address}.
	 * 
	 * @param houseNumber valley
	 * @param zipCode zip code
	 * @return newly created address
	 */
	Address createAddress(String houseNumber, ZipCode zipCode);
	
	/**
	 * Creates a new residence term for the specified person with the specified
	 * address.
	 * 
	 * @param person person
	 * @param address address
	 * @return residence term
	 * @throws DuplicateEntityFoundException thrown when a duplicate residence
	 * term is found
	 * @throws ResidenceStatusConflictException thrown when a residence status
	 * conflicts
	 * @throws PrimaryResidenceExistsException thrown when a primary residence
	 * already exists for the specified person
	 * @throws ResidenceTermExistsException residence term exists exception
	 */
	ResidenceTerm createResidenceTerm(Person person, Address address)
		throws DuplicateEntityFoundException, ResidenceStatusConflictException,
		PrimaryResidenceExistsException, ResidenceTermExistsException;
	
	/**
	 * Creates a contact for the specified person.
	 * 
	 * @param person person
	 * @param telephoneNumber telephone number
	 * @param mail mail
	 * @return newly created contact
	 * @throws DuplicateEntityFoundException thrown when a duplicate contact
	 * is found
	 */
	Contact addContact(Person person, Integer telephoneNumber, PoBox mail)
		throws DuplicateEntityFoundException;
	
	/**
	 * Returns the primary residence term for the specified person on the
	 * specified date.
	 * 
	 * @param person person
	 * @param date date
	 * @return residence term
	 */
	ResidenceTerm findResidenceTerm(Person person, Date date);

	/**
	 * Returns countries.
	 * 
	 * @return list of countries
	 */
	List<Country> findCountries();

	/**
	 * Returns states for the specified country.
	 * 
	 * @param country country
	 * @return list of states
	 */
	List<State> findStates(Country country);

	/**
	 * Returns the home state.
	 * 
	 * @return state
	 */
	State findHomeState();

	/**
	 * Returns cities for the specified state.
	 * 
	 * @param state state
	 * @return list of cities
	 */
	List<City> findCities(State state);

	/**
	 * Returns cities for the specified country.
	 * 
	 * @param country country
	 * @return list of cities
	 */
	List<City> findCitiesByCountry(Country country);

	/**
	 * Returns suffix names.
	 * 
	 * @return list of suffix names
	 */
	List<String> findSuffixNames();

	/**
	 * Ends the specified visitation association on the specified date.
	 * 
	 * @param visitationAssociation visitation association 
	 * @param endDate end date
	 * @return dissociated visitation association
	 */
	VisitationAssociation dissociate(
			VisitationAssociation visitationAssociation, Date endDate);
	
	/**
	 * Returns whether the specified person is an offender.
	 * 
	 * @param person person
	 * @return whether person is offender
	 */
	Boolean isOffender(Person person);
	
	/**
	 * Returns the specified person as an offender.
	 * 
	 * @param person person
	 * @return offender
	 */
	Offender findAsOffender(Person person);
}