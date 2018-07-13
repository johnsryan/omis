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
import omis.contact.exception.OnlineAccountExistsException;
import omis.contact.exception.TelephoneNumberExistsException;
import omis.country.domain.Country;
import omis.demographics.domain.Sex;
import omis.offender.domain.Offender;
import omis.person.domain.Person;
import omis.person.domain.Suffix;
import omis.person.exception.PersonIdentityExistsException;
import omis.person.exception.PersonNameExistsException;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.region.exception.CityExistsException;
import omis.relationship.domain.Relationship;
import omis.relationship.domain.RelationshipNote;
import omis.relationship.domain.RelationshipNoteCategory;
import omis.relationship.exception.RelationshipNoteExistsException;

/**
 * Service to update offender relationship.
 * 
 * @author Joel Norris
 * @author Yidong Li
 * @author Stephen Abson
 * @version 0.1.1 (Nov 8, 2017)
 * @since OMIS 3.0
 */
public interface UpdateOffenderRelationService {
	
	/**
	 * Updates relation.
	 * 
	 * @param person relation to update
	 * @param lastName last name
	 * @param firstName first name
	 * @param middleName middle name
	 * @param suffix suffix
	 * @param sex sex
	 * @param birthDate birth date
	 * @param birthCountry birth country
	 * @param birthState birth State
	 * @param birthCity birth city
	 * @param socialSecurityNumber social security number
	 * @param stateId State ID
	 * @param deceased whether relation is deceased
	 * @param deathDate death date
	 * @return updated relation
	 * @throws PersonNameExistsException if person name exists
	 * @throws PersonIdentityExistsException if person identity exists
	 */
	Person updateRelation(Person person, String lastName, String firstName, 
		String middleName, String suffix, Sex sex, Date birthDate, 
		Country birthCountry, State birthState, City birthCity,  
		Integer socialSecurityNumber, String stateId, Boolean deceased, 
		Date deathDate)	throws PersonNameExistsException,
		PersonIdentityExistsException;
	
	/**
	 * Updates relation.
	 * 
	 * @param person relation to update
	 * @param lastName last name
	 * @param firstName first name
	 * @param middleName middle name
	 * @param suffix suffix
	 * @param sex sex
	 * @param birthDate birth date
	 * @param birthCountry birth country
	 * @param birthState birth State
	 * @param birthCity birth city
	 * @param stateId State ID
	 * @param deceased whether relation is deceased
	 * @param deathDate death date
	 * @return updated relation
	 * @throws PersonNameExistsException if person name exists
	 * @throws PersonIdentityExistsException if person identity exists
	 */
	Person updateRelationWithoutSsn(Person person, String lastName, 
			String firstName, String middleName, String suffix, Sex sex, 
			Date birthDate, Country birthCountry, State birthState, 
			City birthCity, String stateId, Boolean deceased, Date deathDate) 
					throws PersonNameExistsException,  
					PersonIdentityExistsException;
	
	/**
	 * Returns name suffixes.
	 * 
	 * @return name suffixes
	 */
	List<Suffix> findNameSuffixes();
	
	/**
	 * Creates address.
	 * 
	 * @param number number
	 * @param zipCode ZIP code
	 * @return newly created address
	 * @throws AddressExistsException if address exists
	 */
	Address createAddress(String number, ZipCode zipCode)
		throws AddressExistsException;

	/**
	 * Returns addresses matching query.
	 * 
	 * @param addressQuery address query
	 * @return addresses matching query
	 */
	List<Address> findAddresses(String addressQuery);
	
	/**
	 * Returns street suffixes.
	 * 
	 * @return street suffix
	 */
	List<StreetSuffix> findStreetSuffixes();
	
	/**
	 * Returns address unit designators.
	 * 
	 * @return address unit designators
	 */
	List<AddressUnitDesignator> findAddressUnitDesignators();
	
	/**
	 * Returns countries.
	 * 
	 * @return countries
	 */
	List<Country> findCountries();
	
	/**
	 * Returns States by country.
	 * 
	 * @param country country by which to return States
	 * @return States by country
	 */
	List<State> findStates(Country country);
	
	/**
	 * Returns whether country has States.
	 * 
	 * @param country country
	 * @return whether country has States
	 */
	Boolean hasStates(Country country);
	
	/**
	 * Returns cities by country.
	 * 
	 * @param country country by which to return cities
	 * @return cities by country
	 */
	List<City> findCitiesByCountry(Country country);
	
	/**
	 * Returns cities by State.
	 * 
	 * @param state State by which to return cities
	 * @return list of city
	 */
	List<City> findCitiesByState(State state);
	
	/**
	 * Returns ZIP codes by city.
	 * 
	 * @param city city by which to return ZIP codes
	 * @return ZIP codes by city
	 */
	List<ZipCode> findZipCodes(City city);
	
	/**
	 * Creates city.
	 * 
	 * @param name name
	 * @param state State
	 * @param country country
	 * @return newly created city
	 * @throws CityExistsException if city exists
	 */
	City createCity(String name, State state, Country country) 
		throws CityExistsException;
	
	/**
	 * Creates ZIP code.
	 * 
	 * @param value value
	 * @param extension extension
	 * @param city city
	 * @return newly created ZIP code
	 * @throws ZipCodeExistsException if ZIP code exists
	 */
	ZipCode createZipCode(String value, String extension, City city)
		throws ZipCodeExistsException;
	
	/**
	 * Changes contact.
	 * 
	 * @param relation relation
	 * @param poBox PO box
	 * @param mailingAddress mailing address
	 * @return changed contact
	 * @throws ContactExistsException if contact exists
	 */
	Contact changeContact(Person relation, PoBox poBox, Address mailingAddress)
		throws ContactExistsException;
	
	/**
	 * Returns mailing address of relation.
	 * 
	 * @param relation relation
	 * @return mailing address of relation
	 */
	Address findMailingAddress(Person relation);
	
	/**
	 * Returns PO box of relation.
	 * 
	 * @param relation relation
	 * @return PO box of relation
	 */
	PoBox findPoBox(Person relation);
	
	/**
	 * Returns telephone number of relation. 
	 * 
	 * @param relation relation
	 * @return telephone number of relation
	 */
	List<TelephoneNumber> findTelephoneNumbers(Person relation);
	
	/**
	 * Returns online accounts of relation.
	 * 
	 * @param relation relation
	 * @return returns online accounts of relation
	 */
	List<OnlineAccount> findOnlineAccounts(Person relation);
	
	/**
	 * Creates telephone number for relation. 
	 * 
	 * @param relation relation for which to create telephone number
	 * @param value value
	 * @param extension extension
	 * @param primary whether primary
	 * @param active whether active
	 * @param category category
	 * @return newly created telephone number for relation
	 * @throws TelephoneNumberExistsException if telephone number exists
	 */
	TelephoneNumber createTelephoneNumber(Person relation, Long value,
			Integer extension, Boolean primary, Boolean active,
			TelephoneNumberCategory category)
		throws TelephoneNumberExistsException;
	
	/**
	 * Updates telephone number.
	 * 
	 * @param telephoneNumber telephone number to update
	 * @param value value
	 * @param extension extension
	 * @param primary whether primary
	 * @param active whether active
	 * @param category category
	 * @return updated telephone number
	 * @throws TelephoneNumberExistsException if telephone number exists
	 */
	TelephoneNumber updateTelephoneNumber(TelephoneNumber telephoneNumber,
			Long value, Integer extension, Boolean primary, Boolean active,
			TelephoneNumberCategory category)
		throws TelephoneNumberExistsException;
	
	/**
	 * Removes telephone number.
	 * 
	 * @param telephoneNumber telephone number to remove
	 */
	void removeTelephoneNumber(TelephoneNumber telephoneNumber);
	
	/**
	 * Creates online account for relation.
	 * 
	 * @param relation relation for which to create online account
	 * @param name name
	 * @param host host
	 * @param primary whether primary
	 * @param active whether active
	 * @return newly created online account
	 * @throws OnlineAccountExistsException if online account exists
	 */
	OnlineAccount createOnlineAccount(Person relation, String name,
		OnlineAccountHost host, Boolean primary, Boolean active) 
		throws OnlineAccountExistsException;
	
	/**
	 * Updates online account.
	 * 
	 * @param onlineAccount online account to update
	 * @param name name
	 * @param host host
	 * @param primary whether primary
	 * @param active whether active
	 * @return updated online account
	 * @throws OnlineAccountExistsException if online account exists
	 */
	OnlineAccount updateOnlineAccount(OnlineAccount onlineAccount, String name,
		OnlineAccountHost host, Boolean primary, Boolean active) 
		throws OnlineAccountExistsException;
	
	/**
	 * Removes online account.
	 * 
	 * @param onlineAccount online account to remove
	 */
	void removeOnlineAccount(OnlineAccount onlineAccount);
	
	/**
	 * Returns online account hosts.
	 * 
	 * @return online account hosts
	 */
	List<OnlineAccountHost> findOnlineAccountHosts();
	
	/**
	 * Returns suffix names.
	 * 
	 * @return suffix names
	 */
	List<String> findSuffixNames();
	
	/**
	 * Removes relationship of offender to relation.
	 * 
	 * <p>Removes associated notes, victim associations, victim notes,
	 * visitation associations and family associations.
	 * 
	 * @param offender offender
	 * @param relation relation
	 */
	void removeRelationship(Offender offender, Person relation);
	
	/**
	 * Returns cities by country without States.
	 * 
	 * @param country country but which to return cities without States
	 * @return cities without States
	 */
	List<City> findCitiesByCountryWithoutState(Country country);
	
	/**
	 * Returns home State.
	 * 
	 * @return home State
	 */
	State findHomeState();
	
	/**
	 * Returns designated note categories for relationships.
	 * 
	 * @return designated note categories for relationships
	 */
	List<RelationshipNoteCategory> findDesignatedNoteCategories();
	
	/**
	 * Creates relationship note.
	 * 
	 * @param relationship relationship for which to create note
	 * @param category category
	 * @param value value
	 * @param date date
	 * @return newly created relationship note
	 * @throws RelationshipNoteExistsException if relationship note exists
	 */
	RelationshipNote createNote(Relationship relationship,
			RelationshipNoteCategory category, String value, Date date)
					throws RelationshipNoteExistsException;
	
	/**
	 * Updates relationship note.
	 * 
	 * @param relationshipNote relationship note
	 * @param category category
	 * @param value value
	 * @param date date
	 * @return updated relationship note
	 * @throws RelationshipNoteExistsException if relationship note exists
	 */
	RelationshipNote updateNote(RelationshipNote relationshipNote,
			RelationshipNoteCategory category, String value, Date date)
					throws RelationshipNoteExistsException;
	
	/**
	 * Removes relationship note.
	 * 
	 * @param relationshipNote relationship note
	 */
	void removeNote(RelationshipNote relationshipNote);
	
	/**
	 * Returns notes.
	 * 
	 * @param relationship relationship
	 * @return notes
	 */
	List<RelationshipNote> findNotes(Relationship relationship);
}