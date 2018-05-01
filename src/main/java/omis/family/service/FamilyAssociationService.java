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
package omis.family.service;

import java.util.Date;
import java.util.List;

import omis.address.domain.Address;
import omis.address.domain.BuildingCategory;
import omis.address.domain.ZipCode;
import omis.address.exception.AddressExistsException;
import omis.address.exception.ZipCodeExistsException;
import omis.audit.domain.VerificationSignature;
import omis.contact.domain.Contact;
import omis.contact.domain.OnlineAccount;
import omis.contact.domain.OnlineAccountHost;
import omis.contact.domain.TelephoneNumber;
import omis.contact.domain.TelephoneNumberCategory;
import omis.contact.domain.component.PoBox;
import omis.contact.exception.ContactExistsException;
import omis.contact.exception.OnlineAccountExistsException;
import omis.contact.exception.TelephoneNumberExistsException;
import omis.country.domain.Country;
import omis.datatype.DateRange;
import omis.demographics.domain.Sex;
import omis.family.domain.FamilyAssociation;
import omis.family.domain.FamilyAssociationCategory;
import omis.family.domain.component.FamilyAssociationFlags;
import omis.family.exception.FamilyAssociationConflictException;
import omis.family.exception.FamilyAssociationExistsException;
import omis.offender.domain.Offender;
import omis.person.domain.Person;
import omis.person.domain.Suffix;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.region.exception.CityExistsException;
import omis.relationship.domain.Relationship;
import omis.relationship.domain.RelationshipNote;
import omis.relationship.domain.RelationshipNoteCategory;
import omis.relationship.exception.ReflexiveRelationshipException;
import omis.relationship.exception.RelationshipNoteExistsException;
import omis.residence.domain.ResidenceTerm;
import omis.residence.exception.PrimaryResidenceExistsException;
import omis.residence.exception.ResidenceStatusConflictException;
import omis.residence.exception.ResidenceTermExistsException;

/**
 * Service for family association.
 * 
 * @author Joel Norris
 * @author Yidong Li
 * @author Sheronda Vaughn
 * @version 0.1.2 (June 2, 2015)
 * @since OMIS 3.0
 */
public interface FamilyAssociationService {

	/**
	 * Creates a family association between the specified first and second 
	 * person.
	 * 
	 * @param offender offender
	 * @param familyMember family member
	 * @param dateRange date range
	 * @param category family association category
	 * @param flags family association flags
	 * @param marriageDate marriage date
	 * @param divorceDate divorce date
	 * @return newly created family association
	 * @throws FamilyAssociationExistsException family association exists
	 * exception
	 * @throws ReflexiveRelationshipException reflexive relationship exception
	 * @throws FamilyAssociationConflictException 
	 * family association conflict exception
	 * @throws omis.family.exception.FamilyAssociationConflictException 
	 */
	FamilyAssociation associate(Offender offender, Person familyMember, 
		DateRange dateRange, FamilyAssociationCategory category,
		FamilyAssociationFlags flags, Date marriageDate, Date divorceDate)
		throws FamilyAssociationExistsException, ReflexiveRelationshipException,
		FamilyAssociationConflictException, 
		omis.family.exception.FamilyAssociationConflictException;
	
	/**
	 * Updates a family association.
	 * 
	 * @param familyAssociation family association being updated
	 * @param dateRange date range
	 * @param category family association category
	 * @param flags family association flags
	 * @param marriageDate marriage date
	 * @param divorceDate divorce date
	 * @return newly updated family association
	 * @throws FamilyAssociationExistsException family association exists
	 * exception
	 * @throws FamilyAssociationConflictException 
	 * family association conflict exception
	 */
	FamilyAssociation update(FamilyAssociation familyAssociation, 
			DateRange dateRange, FamilyAssociationCategory category, 
			FamilyAssociationFlags flags, Date marriageDate, Date divorceDate) 
				 throws FamilyAssociationExistsException,
		 FamilyAssociationConflictException;

	/**
	 * Removes the specified family association.
	 * @param familyAssociation family association
	 */
	void remove(FamilyAssociation familyAssociation);
	
	/**
	 * Find all family associations categories.
	 * @return list of family associations categories
	 */
	List<FamilyAssociationCategory> findCategories();
	
	/**
	 * Create family member.
	 * @param lastName last name
	 * @param firstName first name
	 * @param middleName middle name
	 * @param suffix suffix
	 * @param sex sex
	 * @param birthDate birth date
	 * @param birthState birth state
	 * @param birthCountrhy birth country
	 * @param birthCity birth city
	 * @param socialSecurityNumber social security number
	 * @param stateId state id
	 * @param deceased deceased or not
	 * @param deathDate death date
	 * @return person
	 */
	Person createFamilyMember(String lastName, String firstName, 
		String middleName, String suffix, Sex sex, Date birthDate, 
		Country birthCountrhy, State birthState, City birthCity, 
		Integer socialSecurityNumber, String stateId, Boolean deceased, 
		Date deathDate);
	
	/**
	 * Find all name suffixes.
	 * @return a list of name suffixes
	 */
	List<Suffix> findNameSuffixes();
	
	/**
	 * Create address.
	 * @param value value
	 * @param designator designator
	 * @param buiildingCategory building category
	 * @param zipCode zip code
	 * @return address
	 * @throws FamilyAssociationExistsException family association exists
	 * exception
	 * @throws AddressExistsException 
	 */
	Address createAddress(String value, String designator, 
		BuildingCategory buiildingCategory,	ZipCode zipCode)
				throws AddressExistsException;
	
	/**
	 * Create residence term.
	 * @param person person
	 * @param address address
	 * @param verificationSignature verification signature	 
	 * @throws FamilyAssociationExistsException family association exists
	 * exception
	 * @throws PrimaryResidenceExistsException 
	 * primary residence exists exception
	 * @throws ResidenceStatusConflictException 
	 * residence status conflict exception
	 * @return residence term
	 * @throws ResidenceTermExistsException, residence term exists exception 
	 * @throws ResidenceStatusConflictException, residence status conflict
	 * exception
	 * @throws PrimaryResidenceExistsException  primary residence exists
	 * exception
	 * @throws ResidenceTermExistsException residence term exists exception
	 */
	ResidenceTerm createResidenceTerm(Person person, Address address, 
		VerificationSignature verificationSignature)
		throws PrimaryResidenceExistsException, 
		ResidenceStatusConflictException, ResidenceTermExistsException;
	
	/**
	 * Add contact.
	 * @param person person
	 * @param mailingAddress mailing address
	 * @param poBox P.O. box
	 * @return contact
	 * @throws FamilyAssociationExistsException family association exists
	 * exception
	 * @throws ContactExistsException contact exists exception 
	 */
	Contact addContact(Person person, Address mailingAddress, PoBox poBox)
		throws ContactExistsException;

	/**
	 * Find a contact by person.
	 * @param person person
	 * @return contact
	 */
	Contact findContactByPerson(Person person);
	
	/**
	 * Find telephone numbers by contact.
	 *
	 * @param contact contact
	 * @return list of telephone numbers
	 */
	List<TelephoneNumber> findTelephoneNumbersByContact(Contact contact);
	
	/**
	 * Find online accounts by contact.
	 * @param contact contact
	 * @return list of online accounts
	 */
	List<OnlineAccount> findOnlineAccountsByContact(Contact contact);
	
	/**
	 * Update contact.
	 * @param contact contact
	 * @param mailingAddress mailing address
	 * @param poBox P.O. box
	 * @return contact
	 * @throws FamilyAssociationExistsException family association exists
	 * exception
	 * @throws ContactExistsException contact exists exception
	 */
	Contact updateContact(Contact contact, Address mailingAddress, PoBox poBox)
			throws ContactExistsException;
	
	/**
	 * Add telephone number.
	 * @param contact contact
	 * @param value phone number
	 * @param extension phone extension
	 * @param primary primary phone or not
	 * @param active active phone
	 * @param category telephone number category
	 * @return telephone number
	 * @throws TelephoneNumberExistsException telephone number exists exception
	 */
	TelephoneNumber addTelephoneNumber(Contact contact, Long value, 
		Integer extension, Boolean primary, Boolean active, 
		TelephoneNumberCategory category)
				throws TelephoneNumberExistsException;
	
	/**
	 * Update a telephone number.
	 * @param telephoneNumber telephone number to be updated
	 * @param value phone number
	 * @param extension phone extension
	 * @param active active
	 * @param primary primary phone or not
	 * @param category telephone number category
	 * @return updated telephone number
	 * @throws TelephoneNumberExistsException telephone number exists exception
	 */
	TelephoneNumber updateTelephoneNumber(TelephoneNumber telephoneNumber, 
		Long value, Integer extension, Boolean primary, Boolean active,
		TelephoneNumberCategory category) 
				throws TelephoneNumberExistsException;
	
	/**
	 * Remove a telephone number.
	 * @param telephoneNumber telephone number to be removed
	 */
	void removeTelephoneNumber(TelephoneNumber telephoneNumber);
	
	/**
	 * Add online account.
	 * @param contact contact
	 * @param name name
	 * @param primary primary email or not
	 * @param host online account host
	 * @param active active account or not
	 * @return online account
	 * @throws OnlineAccountExistsException online account exists exception
	 */
	OnlineAccount addOnlineAccount(Contact contact, String name, 
			Boolean primary, OnlineAccountHost host, Boolean active)	
				throws OnlineAccountExistsException;
	
	/**
	 * Update an online account.
	 * @param onlineAccount online account to be updated
	 * @param name name
	 * @param primary primary email or not
	 * @param host online account host
	 * @param active active account or not
	 * @return updated online account
	 * @throws OnlineAccountExistsException online account exists exception
	 */
	OnlineAccount updateOnlineAccount(OnlineAccount onlineAccount, String name, 
		Boolean primary, OnlineAccountHost host, Boolean active)
				throws OnlineAccountExistsException;
	
	/**
	 * Remove an online account.
	 * @param onlineAccount online account
	 */
	void removeOnlineAccount(OnlineAccount onlineAccount);
	
	/**
	 * Find email hosts.
	 *
	 * @return email hosts
	 */
	List<OnlineAccountHost> findOnlineAccountHosts();
	
	/**
	 * Find notes by relationship.
	 * @param relationship relationship
	 * @return a list of family association notes
	 */
	List<RelationshipNote> findNotesByRelationship(Relationship relationship);
	
	/**
	 * Find contact.
	 * @param person person
	 * @return contact contact
	 */
	Contact findContact(Person person);
	
	/**
	 * Find residence term.
	 * @param person person
	 * @param effectiveDate effective date
	 * @return ResidenceTerm residence term
	 */
	ResidenceTerm findResidenceTerm(Person person, Date effectiveDate);
	
	/**
	 * Find countries.
	 * @return a list of countries
	 */
	List<Country> findCountries();
	
	/**
	 * Find zip codes by city.
	 * @param city city
	 * @return a list of zip codes
	 */
	List<ZipCode> findZipCodesByCity(City city);
	
	/**
	 * Find cities in state.
	 * @param state state
	 * @return a list of cities
	 */
	List<City> findCitiesByState(State state);
	
	/**
	 * Create zip code.
	 * @param value zip code value
	 * @param extension zip code extension
	 * @param city city
	 * @return zip code
	 * @throws ZipCodeExistsException zip code exists exception
	 * exception
	 */
	ZipCode createZipCode(String value, String extension, City city) 
		throws ZipCodeExistsException;
	
	/**
	 * Find states by country.
	 * @param country country
	 * @return a list of states
	 */
	List<State> findStatesByCountry(Country country);
	
	/**
	 * Find cities by country.
	 * @param country country
	 * @return a list of cities
	 */
	List<City> findCitiesByCountry(Country country);
	
	/**
	 * Check if the country has states.
	 * @param country country
	 * @return yes or no
	 */
	Boolean hasStates(Country country);
	
	/**
	 * Create city.
	 * @param name city name
	 * @param state state
	 * @param country country
	 * @return Created city
	 * @throws CityExistsException city exists exception
	 */
	City createCity(String name, State state, Country country) 
		throws CityExistsException;
	
	/**
	 * Find addresses.
	 * @param addressQuery address query
	 * @return a list of addresses
	 */
	List<Address> findAddresses(String addressQuery);
	
	/**
	 * Find name suffixes.
	 * @return a list of name suffixes
	 */
	List<String> findSuffixNames();
	
	/**
	 * Returns home State.
	 * 
	 * @return home State
	 */
	State findHomeState();	
	
	
	/**
	 * Returns a list of cities.
	 * @param country country
	 * @return a list of cities
	 */
	List<City> findCitiesByCountryWithoutState(Country country);
	
	/**
	 * Add a relationship note to family association.
	 * @param familyAssociation family association
	 * @param category relationship note category
	 * @param date date
	 * @param value value
	 * @exception RelationshipNoteExistsException thrown when relationship note
	 * already exists
	 * @return relationship note
	 */
	RelationshipNote addRelationshipNote(FamilyAssociation familyAssociation,
		RelationshipNoteCategory category, Date date, String value)
		throws RelationshipNoteExistsException;
	
	/**
	 * Update an existing  relationship note.
	 * @param relationshipNote relationship note
	 * @param category relationship note category
	 * @param date date
	 * @param value value
	 * @exception RelationshipNoteExistsException thrown when relationship note
	 * already exists
	 * @return relationship note
	 */
	RelationshipNote updateRelationshipNote(RelationshipNote relationshipNote,
		RelationshipNoteCategory category, Date date, String value)
		throws RelationshipNoteExistsException;
	
	/**
	 * Remove an existing  relationship note.
	 * @param relationshipNote relationship note
	 */
	void removeRelationshipNote(RelationshipNote relationshipNote);
	
	/**
	 * Find all existing  relationship note categories.
	 * @return a list of relationship note categories
	 */
	List<RelationshipNoteCategory> findDesignatedRelationshipNoteCategories();
	
	/**
	 * Find all relationship notes associated with a family association.
	 * @param familyAssociation family association
	 * @return a list of relationship notes
	 */
	@Deprecated
	List<RelationshipNote> findRelationshipNotes(FamilyAssociation
		familyAssociation);
}