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
package omis.offenderrelationship.service.impl;

import java.util.Date;
import java.util.List;

import omis.address.domain.Address;
import omis.address.domain.AddressUnitDesignator;
import omis.address.domain.StreetSuffix;
import omis.address.domain.ZipCode;
import omis.address.exception.AddressExistsException;
import omis.address.exception.ZipCodeExistsException;
import omis.address.service.delegate.AddressDelegate;
import omis.address.service.delegate.AddressUnitDesignatorDelegate;
import omis.address.service.delegate.StreetSuffixDelegate;
import omis.address.service.delegate.ZipCodeDelegate;
import omis.contact.domain.Contact;
import omis.contact.domain.OnlineAccount;
import omis.contact.domain.OnlineAccountHost;
import omis.contact.domain.TelephoneNumber;
import omis.contact.domain.TelephoneNumberCategory;
import omis.contact.domain.component.PoBox;
import omis.contact.exception.ContactExistsException;
import omis.contact.exception.OnlineAccountExistsException;
import omis.contact.exception.TelephoneNumberExistsException;
import omis.contact.service.delegate.ContactDelegate;
import omis.contact.service.delegate.OnlineAccountDelegate;
import omis.contact.service.delegate.OnlineAccountHostDelegate;
import omis.contact.service.delegate.TelephoneNumberDelegate;
import omis.country.domain.Country;
import omis.country.service.delegate.CountryDelegate;
import omis.demographics.domain.Sex;
import omis.family.service.delegate.FamilyAssociationDelegate;
import omis.offender.domain.Offender;
import omis.offender.service.delegate.OffenderDelegate;
import omis.offenderrelationship.service.UpdateOffenderRelationService;
import omis.person.domain.Person;
import omis.person.domain.PersonIdentity;
import omis.person.domain.Suffix;
import omis.person.exception.PersonIdentityExistsException;
import omis.person.exception.PersonNameExistsException;
import omis.person.service.delegate.PersonDelegate;
import omis.person.service.delegate.PersonIdentityDelegate;
import omis.person.service.delegate.SuffixDelegate;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.region.exception.CityExistsException;
import omis.region.service.delegate.CityDelegate;
import omis.region.service.delegate.StateDelegate;
import omis.relationship.domain.Relationship;
import omis.relationship.domain.RelationshipNote;
import omis.relationship.domain.RelationshipNoteCategory;
import omis.relationship.exception.RelationshipNoteExistsException;
import omis.relationship.service.delegate.RelationshipDelegate;
import omis.relationship.service.delegate.RelationshipNoteCategoryDesignatorDelegate;
import omis.relationship.service.delegate.RelationshipNoteDelegate;
import omis.victim.service.delegate.VictimAssociationDelegate;
import omis.victim.service.delegate.VictimNoteDelegate;
import omis.visitation.service.delegate.VisitationAssociationDelegate;

/**
 * Update offender relation service implementation.
 * 
 * @author Joel Norris
 * @author Yidong Li
 * @author Stephen Abson
 * @author Sheronda Vaughn
 * @version 0.1.1 (Nov 8, 2017)
 * @since OMIS 3.0
 */
public class UpdateOffenderRelationServiceImpl
		implements UpdateOffenderRelationService {
	
	/* Service Delegates. */
	private final AddressDelegate addressDelegate;
	private final PersonDelegate personDelegate;
	private final PersonIdentityDelegate personIdentityDelegate;
	private final CountryDelegate countryDelegate;
	private final StateDelegate stateDelegate;
	private final CityDelegate cityDelegate;
	private final ZipCodeDelegate zipCodeDelegate;
	private final AddressUnitDesignatorDelegate addressUnitDesignatorDelegate;
	private final StreetSuffixDelegate streetSuffixDelegate;
	private final ContactDelegate contactDelegate;
	private final TelephoneNumberDelegate telephoneNumberDelegate;
	private final OnlineAccountDelegate onlineAccountDelegate;
	private final OnlineAccountHostDelegate onlineAccountHostDelegate;
	private final SuffixDelegate suffixDelegate;
	private final RelationshipDelegate relationshipDelegate;
	private final RelationshipNoteDelegate relationshipNoteDelegate;
	private final RelationshipNoteCategoryDesignatorDelegate
		relationshipNoteCategoryDesignatorDelegate;
	private final OffenderDelegate offenderDelegate;
	private final FamilyAssociationDelegate familyAssociationDelegate;
	private final VictimNoteDelegate victimNoteDelegate;
	private final VictimAssociationDelegate victimAssociationDelegate;
	private final VisitationAssociationDelegate visitationAssociationDelegate;
	
	/**
	 * Instantiates implementation of service to update offender relations.
	 * 
	 * @param addressDelegate delegate for addresses
	 * @param personDelegate delegate for person
	 * @param personIdentityDelegate delegate for person identities
	 * @param countryDelegate delegate for countries
	 * @param stateDelegate delegate for States
	 * @param cityDelegate delegate for cities
	 * @param zipCodeDelegate delegate for ZIP codes
	 * @param addressUnitDesignatorDelegate delegate for address unit designator
	 * @param streetSuffixDelegate delegate for street suffixes
	 * @param contactDelegate delegate for contacts
	 * @param telephoneNumberDelegate delegate for telephone numbers
	 * @param onlineAccountDelegate delegate for online accounts
	 * @param onlineAccountHostDelegate delegate for online account hosts
	 * @param suffixDelegate delegate for (name) suffixes
	 * @param relationshipDelegate delegate for relationships
	 * @param relationshipNoteDelegate delegate for relationship notes
	 * @param relationshipNoteCategoryDesignatorDelegate delegate for
	 * relationship note category designators
	 * @param offenderDelegate delegate for offenders
	 * @param familyAssociationDelegate delegate for family associations
	 * @param victimNoteDelegate delegate for victim notes
	 * @param victimAssociationDelegate delegate for victim associations
	 * @param visitationAssociationDelegate delegate for visitation associations
	 */
	public UpdateOffenderRelationServiceImpl(
			final AddressDelegate addressDelegate,
			final PersonDelegate personDelegate,
			final PersonIdentityDelegate personIdentityDelegate,
			final CountryDelegate countryDelegate,
			final StateDelegate stateDelegate,
			final CityDelegate cityDelegate,
			final ZipCodeDelegate zipCodeDelegate,
			final AddressUnitDesignatorDelegate addressUnitDesignatorDelegate,
			final StreetSuffixDelegate streetSuffixDelegate,
			final ContactDelegate contactDelegate,
			final TelephoneNumberDelegate telephoneNumberDelegate,
			final OnlineAccountDelegate onlineAccountDelegate,
			final OnlineAccountHostDelegate onlineAccountHostDelegate,
			final SuffixDelegate suffixDelegate,
			final RelationshipDelegate relationshipDelegate,
			final RelationshipNoteDelegate relationshipNoteDelegate,
			final RelationshipNoteCategoryDesignatorDelegate
			relationshipNoteCategoryDesignatorDelegate,
			final OffenderDelegate offenderDelegate,
			final FamilyAssociationDelegate familyAssociationDelegate,
			final VictimNoteDelegate victimNoteDelegate,
			final VictimAssociationDelegate victimAssociationDelegate,
			final VisitationAssociationDelegate visitationAssociationDelegate) {
		this.addressDelegate = addressDelegate;
		this.personDelegate = personDelegate;
		this.personIdentityDelegate = personIdentityDelegate;
		this.countryDelegate = countryDelegate;
		this.stateDelegate = stateDelegate;
		this.cityDelegate = cityDelegate;
		this.zipCodeDelegate = zipCodeDelegate;
		this.addressUnitDesignatorDelegate = addressUnitDesignatorDelegate;
		this.streetSuffixDelegate = streetSuffixDelegate;
		this.contactDelegate = contactDelegate;
		this.telephoneNumberDelegate = telephoneNumberDelegate;
		this.onlineAccountDelegate = onlineAccountDelegate;
		this.onlineAccountHostDelegate = onlineAccountHostDelegate;
		this.suffixDelegate = suffixDelegate;
		this.relationshipDelegate = relationshipDelegate;
		this.relationshipNoteDelegate = relationshipNoteDelegate;
		this.relationshipNoteCategoryDesignatorDelegate
				= relationshipNoteCategoryDesignatorDelegate;
		this.offenderDelegate = offenderDelegate;
		this.familyAssociationDelegate = familyAssociationDelegate;
		this.victimNoteDelegate = victimNoteDelegate;
		this.victimAssociationDelegate = victimAssociationDelegate;
		this.visitationAssociationDelegate = visitationAssociationDelegate;
	}
	
	/** {@inheritDoc} */
	@Override
	public Person updateRelation(final Person person, final String lastName, 
		final String firstName,	final String middleName, final String suffix, 
		final Sex sex, final Date birthDate, final Country birthCountry,
		final State birthState, final City birthCity, 
		final Integer socialSecurityNumber,	final String stateId, 
		final Boolean deceased, final Date deathDate) 
		throws PersonNameExistsException,
		PersonIdentityExistsException {
		this.checkIfRelationIsOffender(person);
		Person updatedPerson = this.personDelegate.update(
				person, lastName, firstName, middleName, suffix);
		if (person.getIdentity() != null) {
			this.personIdentityDelegate.update(person.getIdentity(), sex, 
				birthDate,	birthCountry, birthState, birthCity, 
				socialSecurityNumber, stateId, deceased, deathDate);
		} else {
			if (sex != null || birthDate != null || birthCountry != null 
					|| birthState != null || birthCity != null 
					|| socialSecurityNumber != null || stateId != null 
				|| deceased != null || deathDate != null) {
				PersonIdentity identity = this.personIdentityDelegate.create(
					person, sex, birthDate, birthCountry, birthState, 
					birthCity, 
					socialSecurityNumber, stateId, deceased, deathDate);
				person.setIdentity(identity);
			}
		}
		return updatedPerson;
	}
	
	/** {@inheritDoc} */
	@Override
	public Person updateRelationWithoutSsn(final Person person, final String lastName, 
		final String firstName,	final String middleName, final String suffix, 
		final Sex sex, final Date birthDate, final Country birthCountry,
		final State birthState, final City birthCity, final String stateId, 
		final Boolean deceased, final Date deathDate) 
		throws PersonNameExistsException,
		PersonIdentityExistsException {
		Person updatedPerson = this.personDelegate.update(
				person, lastName, firstName, middleName, suffix);
		this.checkIfRelationIsOffender(person);
		if (person.getIdentity() != null) {
			this.personIdentityDelegate.update(person.getIdentity(), sex, 
				birthDate,	birthCountry, birthState, birthCity, 
				person.getIdentity().getSocialSecurityNumber(), 
				stateId, deceased, deathDate);
		} else {
			if (sex != null || birthDate != null || birthCountry != null 
					|| birthState != null || birthCity != null 
					|| stateId != null || deceased != null || deathDate != null) {
				PersonIdentity identity = this.personIdentityDelegate.create(
					person, sex, birthDate, birthCountry, birthState, 
					birthCity, 
					null, stateId, deceased, deathDate);
				person.setIdentity(identity);
			}
		}
		return updatedPerson;
	}
	
	/** {@inheritDoc} */
	@Override
	public List<Suffix> findNameSuffixes() {
		return this.suffixDelegate.findAll();
	}
	
	/** {@inheritDoc} */
	@Override
	public Address createAddress(final String number, final ZipCode zipCode)
		throws AddressExistsException {
		return this.addressDelegate.findOrCreate(number, null, null, 
			null, zipCode);
	}

	/** {@inheritDoc} */
	@SuppressWarnings("deprecation")
	@Override
	public List<Address> findAddresses(String addressQuery) {
		return this.addressDelegate.findAddressesByValue(addressQuery);
	}

	/** {@inheritDoc} */
	@Override
	public List<StreetSuffix> findStreetSuffixes() {
		return this.streetSuffixDelegate.findAll();
	}

	/** {@inheritDoc} */
	@Override
	public List<AddressUnitDesignator> findAddressUnitDesignators() {
		return this.addressUnitDesignatorDelegate.findAll();
	}

	/** {@inheritDoc} */
	@Override
	public List<Country> findCountries() {
		return this.countryDelegate.findAll();
	}

	/** {@inheritDoc} */
	@Override
	public List<State> findStates(final Country country) {
		return this.stateDelegate.findByCountry(country);
	}

	/** {@inheritDoc} */
	@Override
	public Boolean hasStates(final Country country) {
		return this.stateDelegate.countByCountry(country) > 0;
	}

	/** {@inheritDoc} */
	@Override
	public List<City> findCitiesByCountry(final Country country) {
		return this.cityDelegate.findByCountry(country);
	}

	/** {@inheritDoc} */
	@Override
	public List<City> findCitiesByState(final State state) {
		return this.cityDelegate.findByState(state);
	}

	/** {@inheritDoc} */
	@Override
	public List<ZipCode> findZipCodes(final City city) {
		return this.zipCodeDelegate.findByCity(city);
	}

	/** {@inheritDoc} */
	@Override
	public City createCity(final String name, final State state, 
			final Country country) 
		throws CityExistsException {
		return this.cityDelegate.create(name, true, state, country);
	}

	/** {@inheritDoc} */
	@Override
	public ZipCode createZipCode(final String value, final String extension, 
		final City city) throws ZipCodeExistsException {
		return this.zipCodeDelegate.create(city, value, extension, true);
	}
	
	/** {@inheritDoc} */
	@Override
	public Contact changeContact(final Person relation, final PoBox poBox,
		final Address mailingAddress) throws ContactExistsException {
		Contact contact = this.contactDelegate.find(relation);
		if (contact != null) {			
			this.contactDelegate.update(contact, mailingAddress, 
					poBox);
		} else {
			return this.contactDelegate.create(relation, mailingAddress, 
					poBox);
		}
		return contact;
	}

	/** {@inheritDoc} */
	@Override
	public Address findMailingAddress(final Person relation) {
		Contact contact = this.contactDelegate.find(relation);
		if (contact != null) {
			Address address = contact.getMailingAddress();
			return address;
		} else {
			return null;
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public PoBox findPoBox(final Person relation) {
		Contact contact = this.contactDelegate.find(relation);
		if (contact != null) {
			PoBox poBox = contact.getPoBox();
			return poBox;
		} else  {
			return null;
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public List<TelephoneNumber> findTelephoneNumbers(final Person relation) {
		Contact contact = this.contactDelegate.find(relation);
		return this.telephoneNumberDelegate.findByContact(contact);
	}
	
	/** {@inheritDoc} */
	@Override
	public List<OnlineAccount> findOnlineAccounts(final Person relation) {
		Contact contact = this.contactDelegate.find(relation);
		return this.onlineAccountDelegate.findByContact(contact);
	}
	
	/** {@inheritDoc} */
	@Override
	public TelephoneNumber createTelephoneNumber(final Person relation, 
		final Long value, final Integer extension, final Boolean primary,
		final Boolean active, final TelephoneNumberCategory category)	
		throws TelephoneNumberExistsException {
		Contact contact = this.contactDelegate.find(relation);
		return this.telephoneNumberDelegate.create(contact, value, extension, 
			primary, active, category);
	}

	/** {@inheritDoc} */
	@Override
	public TelephoneNumber updateTelephoneNumber(
		final TelephoneNumber telephoneNumber, final Long value, 
		final Integer extension, final Boolean primary, final Boolean active, 
		final TelephoneNumberCategory category)
		throws TelephoneNumberExistsException {
		return this.telephoneNumberDelegate.update(telephoneNumber, value, 
			extension, primary, active, category);
	}
	
	/** {@inheritDoc} */
	@Override
	public void removeTelephoneNumber(final TelephoneNumber telephoneNumber) {
		this.telephoneNumberDelegate.remove(telephoneNumber);
	}

	/** {@inheritDoc} */
	@Override
	public OnlineAccount createOnlineAccount(final Person relation, 
		final String name, final OnlineAccountHost host, final Boolean primary,
		final Boolean active) 
		throws OnlineAccountExistsException {
		Contact contact = this.contactDelegate.find(relation);
		return this.onlineAccountDelegate.create(contact, name, 
				active, primary, host); 
	}
	
	/** {@inheritDoc} */
	@Override
	public OnlineAccount updateOnlineAccount(final OnlineAccount onlineAccount,
		final String name, final OnlineAccountHost host, final Boolean primary,
		final Boolean active)
		throws OnlineAccountExistsException {
		return this.onlineAccountDelegate.update(onlineAccount, name, active, 
			primary, host);
	}
	
	/** {@inheritDoc} */
	@Override
	public void removeOnlineAccount(final OnlineAccount onlineAccount) {
		this.onlineAccountDelegate.remove(onlineAccount);
	}
	
	/** {@inheritDoc} */
	@Override
	public List<OnlineAccountHost> findOnlineAccountHosts() {
		return this.onlineAccountHostDelegate.findAll();
	}
	
	/** {@inheritDoc} */
	@Override
	public List<String> findSuffixNames() {
		return this.suffixDelegate.findSuffixNames();
	}
	
	/** {@inheritDoc} */
	@Override
	public void removeRelationship(final Offender offender, 
			final Person relation) {
		Relationship relationship = this.relationshipDelegate.find(offender, 
				relation);
		this.familyAssociationDelegate.removeByRelationship(relationship);
		this.victimNoteDelegate.removeByRelationship(relationship);
		this.victimAssociationDelegate.removeByRelationship(relationship);
		this.visitationAssociationDelegate.removeByRelationship(relationship);
		this.relationshipNoteDelegate.removeByRelationship(relationship);
		this.relationshipDelegate.remove(relationship);
	}
	
	/** {@inheritDoc} */
	@Override
	public List<City> findCitiesByCountryWithoutState(
			final Country country) {
		return this.cityDelegate.findByCountryWithoutState(country);
	}
	
	/** {@inheritDoc} */
	@Override
	public State findHomeState() {
		return this.stateDelegate.findHomeState();
	}

	/** {@inheritDoc} */
	@Override
	public List<RelationshipNoteCategory> findDesignatedNoteCategories() {
		return this.relationshipNoteCategoryDesignatorDelegate
				.findDesignatedCategories();
	}

	/** {@inheritDoc} */
	@Override
	public RelationshipNote createNote(
			final Relationship relationship,
			final RelationshipNoteCategory category,
			final String value,
			final Date date)
					throws RelationshipNoteExistsException {
		return this.relationshipNoteDelegate
				.create(relationship, category, value, date);
	}

	/** {@inheritDoc} */
	@Override
	public RelationshipNote updateNote(
			final RelationshipNote relationshipNote,
			final RelationshipNoteCategory category,
			final String value, final Date date)
					throws RelationshipNoteExistsException {
		return this.relationshipNoteDelegate.update(
				relationshipNote, category, value, date);
	}

	/** {@inheritDoc} */
	@Override
	public void removeNote(final RelationshipNote relationshipNote) {
		this.relationshipNoteDelegate.remove(relationshipNote);
	}

	/** {@inheritDoc} */
	@Override
	public List<RelationshipNote> findNotes(final Relationship relationship) {
		return this.relationshipNoteDelegate.findByRelationship(relationship);
	}
	
	/* Helper methods. */
	
	// Throws exception if relation is an offender
	private void checkIfRelationIsOffender(final Person relation) {
		if (this.offenderDelegate.isOffender(relation)) {
			throw new IllegalArgumentException(
					"Cannot update if relation is an offender");
		}
	}
}