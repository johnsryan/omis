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

package omis.offenderrelationship.service;

import java.util.Date;
import java.util.List;

import omis.address.domain.Address;
import omis.address.domain.AddressUnitDesignator;
import omis.address.domain.StreetSuffix;
import omis.address.domain.ZipCode;
import omis.address.exception.AddressExistsException;
import omis.address.exception.ZipCodeExistsException;
import omis.contact.domain.Contact;
import omis.contact.domain.OnlineAccount;
import omis.contact.domain.OnlineAccountHost;
import omis.contact.domain.TelephoneNumber;
import omis.contact.domain.TelephoneNumberCategory;
import omis.contact.domain.component.PoBox;
import omis.contact.exception.ContactExistsException;
import omis.country.domain.Country;
import omis.datatype.DateRange;
import omis.demographics.domain.Sex;
import omis.exception.DateConflictException;
import omis.exception.DuplicateEntityFoundException;
import omis.family.domain.FamilyAssociation;
import omis.family.domain.FamilyAssociationCategory;
import omis.family.domain.component.FamilyAssociationFlags;
import omis.family.exception.FamilyAssociationConflictException;
import omis.offender.domain.Offender;
import omis.person.domain.Person;
import omis.person.domain.Suffix;
import omis.person.exception.PersonExistsException;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.relationship.domain.RelationshipNote;
import omis.relationship.domain.RelationshipNoteCategory;
import omis.relationship.exception.ReflexiveRelationshipException;
import omis.relationship.exception.RelationshipNoteExistsException;
import omis.victim.domain.VictimAssociation;
import omis.victim.domain.component.VictimAssociationFlags;
import omis.victim.exception.VictimExistsException;
import omis.visitation.domain.VisitationApproval;
import omis.visitation.domain.VisitationAssociation;
import omis.visitation.domain.VisitationAssociationCategory;
import omis.visitation.domain.VisitationAssociationFlags;
import omis.visitation.exception.VisitationExistsException;


/**
 * Create relationships service.
 * 
 * @author Joel Norris
 * @author Yidong Li
 * @author Sheronda Vaughn
 * @author Stephen Abson
 * @version 0.1.2 (Nov 8, 2017)
 * @since OMIS 3.0
 */
public interface CreateRelationshipsService {
	
	/**
	 * Returns a list of all valid family association categories.
	 * 
	 * @return list of family association categories
	 */
	List<FamilyAssociationCategory> findFamilyAssociationCategories();
	
	/**
	 * Returns a list of all valid visitation association categories.
	 * 
	 * @return list of visitation association categories
	 */
	List<VisitationAssociationCategory> findVisitationAssociationCategories();
	
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
	 * Returns cities for the specified state.
	 *  
	 * @param state state
	 * @return list of cities
	 */
	List<City> findCitiesByState(State state);	
	
	/**
	 * Returns cities for the specified country.
	 * 
	 * @param country country
	 * @return list of cities
	 */
	List<City> findCitiesByCountry(Country country);
	
	/**
	 * Returns the home state.
	 * 
	 * @return home state
	 */
	State findHomeState();

	/**
	 * Returns a list of address unit designators.
	 * 
	 * @return list of address unit designators
	 */
	List<AddressUnitDesignator> findAddressUnitDesignators();

	/**
	 * Returns a list of zip codes for the specified city.
	 * 
	 * @param city city
	 * @return list of zip codes
	 */
	List<ZipCode> findZipCodes(City city);
	
	/**
	 * Creates a relation for the the specified person. 
	 * 
	 * @param lastName last name
	 * @param firstName first name
	 * @param middleName middle name
	 * @param suffix suffix
	 * @param sex sex
	 * @param birthDate birth day
	 * @param birthCountry birth country
	 * @param birthState birth state
	 * @param birthCity birth city
	 * @param socialSecurityNumber social security number
	 * @param stateId state id
	 * @param deceased deceased
	 * @param deathDate death date
	 * @return relation
	 * @throws PersonExistsException duplicate person found exception
	 */
	Person createRelation(String lastName, String firstName, String middleName, 
		String suffix, Sex sex, Date birthDate, Country birthCountry, 
		State birthState, City birthCity, Integer socialSecurityNumber, 
		String stateId,	Boolean deceased, Date deathDate) 
		throws PersonExistsException;

	/**
	 * Returns a list of name suffixes.
	 * 
	 * @return name suffixes
	 */
	List<Suffix> findNameSuffixes();
	
	/**
	 * Creates an address.
	 * 
	 * @param number number
	 * @param zipCode zip code
	 * @return address
	 * @throws AddressExistsException duplicate address found exception
	 */
	Address createAddress(String number, ZipCode zipCode)
			throws AddressExistsException;
	
	/**
	 * Returns a list of addresses.
	 * 
	 * @param addressQuery address query
	 * @return addresses
	 */
	List<Address> findAddresses(String addressQuery);
	
	/**
	 * Returns a list of street suffixes.
	 * 
	 * @return street suffixes
	 */
	List<StreetSuffix> findStreetSuffixes();
		
	/**
	 * Returns whether a country has states.
	 * 
	 * @param country country
	 * @return true or false
	 */
	Boolean hasStates(Country country);
	
	/**
	 * Creates a city for specified state with specified name.
	 * 
	 * @param state state
	 * @param name name
	 * @param country country
	 * @return city
	 * @throws DuplicateEntityFoundException duplicate entity found exception
	 */
	City createCity(State state, String name, Country country) 
			throws DuplicateEntityFoundException;
	
	/**
	 * Creates a ZIP code for the specified city with specified value and 
	 * their extension.
	 * 
	 * @param city city
	 * @param value value
	 * @param extension extension
	 * @return ZIP code
	 * @throws ZipCodeExistsException duplicate zip code exception
	 */
	ZipCode createZipCode(City city, String value, String extension)
			throws ZipCodeExistsException;
	
	/**
	 * Change a contacts relation, p o box and mailing address.
	 * @param relation relation
	 * @param poBox PO box
	 * @param mailingAddress mailing address
	 * @return contact
	 * @throws ContactExistsException duplicate contact found exception
	 */
	Contact changeContact(Person relation, PoBox poBox, Address mailingAddress)
			throws ContactExistsException;
	
	/**
	 * Find mailing address for specified relation.
	 * 
	 * @param relation relation
	 * @return mailing address
	 */
	Address findMailingAddress(Person relation);
	
	/**
	 * Find PO box for specified relation.
	 * 
	 * @param relation relation
	 * @return PO box
	 */
	PoBox findPoBox(Person relation);
	
	/**
	 * Find telephone numbers for the specified relation.
	 * 
	 * @param relation relation 
	 * @return list of telephone numbers
	 */
	List<TelephoneNumber> findTelephoneNumbers(Person relation);

	/**
	 * Find online accounts for specified relation.
	 * 
	 * @param relation relation
	 * @return list of online accounts
	 */
	List<OnlineAccount> findOnlineAccounts(Person relation);
	
	/**
	 * Create telephone number for the specified relation.
	 * 
	 * @param relation relation
	 * @param value value
	 * @param extension extension
	 * @param primary primary
	 * @param active active
	 * @param category category
	 * @return telephone number
	 * @throws DuplicateEntityFoundException duplicate entity found exception
	 */
	TelephoneNumber createTelephoneNumber(Person relation, Long value, 
		Integer extension, Boolean primary, Boolean active,
		TelephoneNumberCategory category)
			throws DuplicateEntityFoundException;
	
	/**
	 * Update telephone number for the specified relation.
	 * 
	 * @param telephoneNumber telephone number
	 * @param value value
	 * @param extension extension
	 * @param primary primary
	 * @param active active
	 * @param category category
	 * @return telephone number
	 * @throws DuplicateEntityFoundException duplicate entity found exception
	 */
	TelephoneNumber updateTelephoneNumber(TelephoneNumber telephoneNumber, 
		Long value, Integer extension, Boolean primary, Boolean active,
		TelephoneNumberCategory category)
			throws DuplicateEntityFoundException;
	
	/**
	 * Remove a telephone number.
	 * 
	 * @param telephoneNumber telephone number
	 */
	void removeTelephoneNumber(TelephoneNumber telephoneNumber);
	
	/**
	 * Create an online account for the specified relation.
	 * 
	 * @param relation relation
	 * @param name name
	 * @param host host
	 * @param primary primary
	 * @return online account
	 * @param active active
	 * @throws DuplicateEntityFoundException duplicate entity found exception
	 */
	OnlineAccount createOnlineAccount(Person relation, String name,
		Boolean primary, OnlineAccountHost host, Boolean active) 
			throws DuplicateEntityFoundException;
	
	/**
	 * Update the specified online account.
	 * 
	 * @param onlineAccount online account
	 * @param name name
	 * @param active active
	 * @param host host
	 * @param primary primary
	 * @return online account
	 * @throws DuplicateEntityFoundException duplicate entity found exception
	 */
	OnlineAccount updateOnlineAccount(OnlineAccount onlineAccount, String name, 
		Boolean primary, Boolean active, OnlineAccountHost host) 
			throws DuplicateEntityFoundException;
	
	/**
	 * Remove online account.
	 * 
	 * @param onlineAccount online account
	 */
	void removeOnlineAccount(OnlineAccount onlineAccount);
	
	/**
	 * Find a list of online account hosts.
	 * 
	 * @return online account host
	 */
	List<OnlineAccountHost> findOnlineAccountHosts();
	
	/**
	 * Associate a family member with the specified offender.
	 * 
	 * @param offender offender
	 * @param familyMember family member
	 * @param dateRange date range
	 * @param category category
	 * @param flags flags
	 * @param marriageDate marriage date
	 * @param divorceDate divorce date
	 * @return associated family member
	 * @throws DuplicateEntityFoundException duplicate entity found exception
	 * @throws DateConflictException date conflict exception
	 * @throws ReflexiveRelationshipException Reflexive Relationship Exception
	 * @throws FamilyAssociationConflictException Family Association Conflict
	 * Exception
	 */
	FamilyAssociation associateFamilyMember(Offender offender, 
		Person familyMember, DateRange dateRange, 
		FamilyAssociationCategory category, FamilyAssociationFlags flags, 
		Date marriageDate, Date divorceDate) 
		throws DuplicateEntityFoundException,
		ReflexiveRelationshipException, DateConflictException, 
		FamilyAssociationConflictException;
	
	/**
	 * Associates victim.
	 * 
	 * @param offender offender
	 * @param victim victim
	 * @param registerDate register date
	 * @param packetSent whether packet is sent
	 * @param packetSentDate packet sent date
	 * @param flags flags
	 * @return victim association
	 * @throws VictimExistsException if victim association exists
	 * @throws ReflexiveRelationshipException Reflexive Relationship Exception
	 */
	VictimAssociation associateVictim(Offender offender, Person victim, 
		Date registerDate, Boolean packetSent, 
		Date packetSentDate, VictimAssociationFlags flags) 
		throws VictimExistsException,
		ReflexiveRelationshipException;
	
	/**
	 * Associate a visitor for the specified offender.
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
	 * @return associated visitor
	 * @throws DateConflictException date conflict exception
	 * @throws VisitationExistsException if visitation association exists
	 * @throws ReflexiveRelationshipException Reflexive Relationship Exception
	 */
	VisitationAssociation associateVisitor(Offender offender, Person visitor, 
		VisitationAssociationCategory category, VisitationApproval approval,
		Date startDate, Date endDate, VisitationAssociationFlags flags, 
		String notes, String guardianship) throws VisitationExistsException,
		ReflexiveRelationshipException, DateConflictException;
	
	/**
	 * Returns name suffixes.
	 * 
	 * @return name suffixes
	 */
	List<String> findSuffixNames();
	
	/**
	 * Returns cities by country that do not have States.
	 * 
	 * @param country country
	 * @return cities by country that do not have States
	 */
	List<City> findCitiesByCountryWithoutState(Country country);
	
	/**
	 * Returns note categories designated for relationships.
	 * 
	 * @return note categories designated for relationships
	 */
	List<RelationshipNoteCategory> findDesignatedNoteCategories();
	
	/**
	 * Creates note.
	 * 
	 * @param offender offender
	 * @param relation relation
	 * @param category category
	 * @param value value
	 * @param date date
	 * @return newly created relationship note
	 * @throws RelationshipNoteExistsException if relationship note
	 * category exists
	 * @throws ReflexiveRelationshipException if offender and relation
	 * are the same person
	 */
	RelationshipNote createNote(Offender offender, Person relation,
			RelationshipNoteCategory category, String value, Date date)
				throws RelationshipNoteExistsException,
					ReflexiveRelationshipException;
}