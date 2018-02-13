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
package omis.victim.service.impl;

import java.util.Date;
import java.util.List;

import omis.address.domain.Address;
import omis.address.domain.AddressUnitDesignator;
import omis.address.domain.BuildingCategory;
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
import omis.offender.domain.Offender;
import omis.person.domain.Person;
import omis.person.service.delegate.PersonDelegate;
import omis.person.service.delegate.SuffixDelegate;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.region.exception.CityExistsException;
import omis.region.service.delegate.CityDelegate;
import omis.region.service.delegate.StateDelegate;
import omis.relationship.domain.Relationship;
import omis.relationship.exception.ReflexiveRelationshipException;
import omis.relationship.service.delegate.RelationshipDelegate;
import omis.victim.domain.VictimAssociation;
import omis.victim.domain.VictimNote;
import omis.victim.domain.VictimNoteCategory;
import omis.victim.domain.component.VictimAssociationFlags;
import omis.victim.exception.VictimExistsException;
import omis.victim.exception.VictimNoteExistsException;
import omis.victim.service.VictimAssociationService;
import omis.victim.service.delegate.VictimAssociationDelegate;
import omis.victim.service.delegate.VictimNoteCategoryDelegate;
import omis.victim.service.delegate.VictimNoteDelegate;

/**
 * Implementation of service for victim associations.
 *
 * @author Stephen Abson
 * @author Sheronda Vaughn
 * @version 0.0.1 (May 26, 2015)
 * @since OMIS 3.0
 */
public class VictimAssociationServiceImpl
		implements VictimAssociationService {

	private final VictimAssociationDelegate victimAssociationDelegate;
	
	private final VictimNoteDelegate victimNoteDelegate;
	
	private final VictimNoteCategoryDelegate victimNoteCategoryDelegate;
	
	private final RelationshipDelegate relationshipDelegate;
	
	private final PersonDelegate personDelegate;
	
	private final AddressDelegate addressDelegate;
	
	private final StreetSuffixDelegate streetSuffixDelegate;
	
	private final AddressUnitDesignatorDelegate addressUnitDesignatorDelegate;
	
	private final CountryDelegate countryDelegate;
	
	private final StateDelegate stateDelegate;
	
	private final CityDelegate cityDelegate;
	
	private final SuffixDelegate suffixDelegate;
	
	private final ZipCodeDelegate zipCodeDelegate;
	
	private final ContactDelegate contactDelegate;
	
	private final TelephoneNumberDelegate telephoneNumberDelegate;
	
	private final OnlineAccountDelegate onlineAccountDelegate;
	
	private final OnlineAccountHostDelegate onlineAccountHostDelegate;
	
	/**
	 * Instantiates implementation of service for victim associations.
	 * 
	 * @param victimAssociationDelegate delegate for victim associations
	 * @param victimNoteDelegate delegate for victim notes
	 * @param victimNoteCategoryDelegate delegate for for victim note categories
	 * @param relationshipDelegate delegate for relationships
	 * @param personDelegate delegate for persons
	 * @param addressDelegate delegate for addresses
	 * @param streetSuffixDelegate delegate for street suffixes
	 * @param addressUnitDesignatorDelegate delegate for address unit
	 * designators
	 * @param countryDelegate delegate for countries
	 * @param stateDelegate delegate for States
	 * @param cityDelegate delegate for cities
	 * @param suffixDelegate delegate for suffixes
	 * @param zipCodeDelegate delegate for ZIP codes
	 * @param contactDelegate delegate for contacts
	 * @param telephoneNumberDelegate delegate for telephone numbers
	 * @param onlineAccountDelegate delegate for online accounts
	 * @param onlineAccountHostDelegate delegate for online account hosts
	 */
	public VictimAssociationServiceImpl(
			final VictimAssociationDelegate victimAssociationDelegate,
			final VictimNoteDelegate victimNoteDelegate,
			final VictimNoteCategoryDelegate victimNoteCategoryDelegate,
			final RelationshipDelegate relationshipDelegate,
			final PersonDelegate personDelegate,
			final AddressDelegate addressDelegate,
			final StreetSuffixDelegate streetSuffixDelegate,
			final AddressUnitDesignatorDelegate addressUnitDesignatorDelegate,
			final CountryDelegate countryDelegate,
			final StateDelegate stateDelegate,
			final CityDelegate cityDelegate,
			final SuffixDelegate suffixDelegate,
			final ZipCodeDelegate zipCodeDelegate,
			final ContactDelegate contactDelegate,
			final TelephoneNumberDelegate telephoneNumberDelegate,
			final OnlineAccountDelegate onlineAccountDelegate,
			final OnlineAccountHostDelegate onlineAccountHostDelegate) {
		this.victimAssociationDelegate = victimAssociationDelegate;
		this.victimNoteDelegate = victimNoteDelegate;
		this.victimNoteCategoryDelegate = victimNoteCategoryDelegate;
		this.relationshipDelegate = relationshipDelegate;
		this.personDelegate = personDelegate;
		this.addressDelegate = addressDelegate;
		this.streetSuffixDelegate = streetSuffixDelegate;
		this.addressUnitDesignatorDelegate = addressUnitDesignatorDelegate;
		this.countryDelegate = countryDelegate;
		this.stateDelegate = stateDelegate;
		this.cityDelegate = cityDelegate;
		this.suffixDelegate = suffixDelegate;
		this.zipCodeDelegate = zipCodeDelegate;
		this.contactDelegate = contactDelegate;
		this.telephoneNumberDelegate = telephoneNumberDelegate;
		this.onlineAccountDelegate = onlineAccountDelegate;
		this.onlineAccountHostDelegate = onlineAccountHostDelegate;
	}

	/** {@inheritDoc} */
	@Override
	public VictimAssociation associate(final Offender offender,
			final Person victim, final Date registeredDate,
			final Boolean packetSent, final Date packetSendDate,
			final VictimAssociationFlags flags)
					throws VictimExistsException,
						ReflexiveRelationshipException {
		Relationship relationship = relationshipDelegate.findOrCreate(
				offender, victim);
		return this.victimAssociationDelegate.create(relationship,
				registeredDate, packetSent, packetSendDate, flags);
	}

	/** {@inheritDoc} */
	@Override
	public VictimAssociation update(final VictimAssociation association,
			final Date registeredDate, final Boolean packetSent,
			final Date packetSendDate, final VictimAssociationFlags flags)
					throws VictimExistsException {
		return this.victimAssociationDelegate.update(association,
				registeredDate, packetSent, packetSendDate, flags);
	}

	/** {@inheritDoc} */
	@Override
	public void remove(final VictimAssociation association) {
		this.victimAssociationDelegate.remove(association);
	}

	/** {@inheritDoc} */
	@Override
	public Person createVictim(final String lastName, final String firstName,
			final String middleName, final String suffix,
			final Sex sex, final Date birthDate, final Country birthCountry,
			final State birthState, final City birthCity,
			final Integer socialSecurityNumber, final String stateId,
			final Boolean deceased, final Date deathDate) {
		if (sex != null || birthDate != null || birthCountry != null
				|| birthState != null || birthCity != null
				|| socialSecurityNumber != null || (stateId != null
					&& !stateId.isEmpty())
				|| deceased != null || deathDate != null) {
			return this.personDelegate.createWithIdentity(lastName, firstName,
					middleName, suffix, sex, birthDate, birthCountry,
					birthState, birthCity, socialSecurityNumber, stateId,
					deceased, deathDate);
		} else {
			return this.personDelegate.create(lastName, firstName, middleName,
					suffix);
		}
	}

	/** {@inheritDoc} */
	@Override
	public List<VictimAssociation> findByOffender(final Offender offender) {
		return this.victimAssociationDelegate.findByOffender(offender);
	}

	/** {@inheritDoc} */
	@Override
	public VictimNote addNote(final VictimAssociation association,
			final VictimNoteCategory category, final Date date,
			final String value)
					throws VictimNoteExistsException {
		return this.victimNoteDelegate.create(
				association.getRelationship().getSecondPerson(), category,
				association, date, value);
	}

	/** {@inheritDoc} */
	@Override
	public VictimNote updateNote(final VictimNote victimNote,
			final VictimNoteCategory category, final Date date,
			final String value)
					throws VictimNoteExistsException {
		return this.victimNoteDelegate.update(victimNote, category,
				victimNote.getAssociation(), date, value);
	}
	
	/** {@inheritDoc} */
	@Override
	public List<VictimNoteCategory> findNoteCategories() {
		return this.victimNoteCategoryDelegate.findAll();
	}

	/** {@inheritDoc} */
	@Override
	public void removeNote(final VictimNote victimNote) {
		this.victimNoteDelegate.remove(victimNote);
	}
	
	/** {@inheritDoc} */
	@Override
	public VictimNote disassociateNote(final VictimNote victimNote) {
		return this.victimNoteDelegate.disassociateFromVictimAssociation(
				victimNote);
	}

	/** {@inheritDoc} */
	@Override
	public List<VictimNote> findNotesByAssociation(
			final VictimAssociation association) {
		return this.victimNoteDelegate.findByAssociation(association);
	}

	/** {@inheritDoc} */
	@Override
	public void removeWithNotes(
			final VictimAssociation victimAssociation) {
		List<VictimNote> notes = this.victimNoteDelegate
				.findByAssociation(victimAssociation);
		for (int noteIndex = 0; noteIndex < notes.size(); noteIndex++) {
			VictimNote note = notes.get(noteIndex);
			this.victimNoteDelegate.remove(note);
		}
		this.victimAssociationDelegate.remove(victimAssociation);
	}
	
	/** {@inheritDoc} */
	@Override
	public Address createAddress(
			final String number, final ZipCode zipCode)
					throws AddressExistsException {
		return this.addressDelegate.findOrCreate(number, null, null,
				BuildingCategory.HOUSE, zipCode);
	}

	/** {@inheritDoc} */
	@Override
	public void removeAndDisassociateNotes(
			final VictimAssociation victimAssociation) {
		List<VictimNote> notes = this.victimNoteDelegate
				.findByAssociation(victimAssociation);
		for (int noteIndex = 0; noteIndex < notes.size(); noteIndex++) {
			VictimNote note = notes.get(noteIndex);
			this.victimNoteDelegate.disassociateFromVictimAssociation(note);
		}
		this.victimAssociationDelegate.remove(victimAssociation);
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
	public List<City> findCitiesByState(final State state) {
		return this.cityDelegate.findByState(state);
	}
	
	/** {@inheritDoc} */
	@Override
	public List<City> findCitiesByCountry(final Country country) {
		return this.cityDelegate.findByCountry(country);
	}

	/** {@inheritDoc} */
	@Override
	public List<City> findCitiesByCountryWithoutState(final Country country) {
		return this.cityDelegate.findByCountryWithoutState(country);
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean hasStates(final Country birthCountry) {
		return this.stateDelegate.countByCountry(birthCountry) > 0;
	}

	/** {@inheritDoc} */
	@Override
	public List<String> findSuffixNames() {
		return this.suffixDelegate.findSuffixNames();
	}

	/** {@inheritDoc} */
	@Override
	public Contact changeContact(
			final Person relation, final PoBox poBox,
			final Address mailingAddress) {
		Contact contact = this.contactDelegate.find(relation);
		if (contact != null) {
			try {
				return this.contactDelegate.update(
						contact, mailingAddress, poBox);
			} catch (ContactExistsException e) {
				throw new AssertionError("Duplicate contact exists", e);
			}
		} else {
			try {
				return this.contactDelegate.create(
						relation, mailingAddress, poBox);
			} catch (ContactExistsException e) {
				throw new AssertionError("Duplicate contact exists", e);
			}
		}
	}

	/** {@inheritDoc} */
	@Override
	public Address findMailingAddress(final Person relation) {
		Contact contact = this.contactDelegate.find(relation);
		if (contact != null) {
			return contact.getMailingAddress();
		} else {
			return null;
		}
	}

	/** {@inheritDoc} */
	@Override
	public PoBox findPoBox(final Person relation) {
		Contact contact = this.contactDelegate.find(relation);
		if (contact != null) {
			return contact.getPoBox();
		} else {
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
	public TelephoneNumber createTelephoneNumber(
			final Person relation, final Long value, final Integer extension,
			final Boolean primary, final Boolean active, 
			final TelephoneNumberCategory category)
					throws TelephoneNumberExistsException {
		Contact contact = this.contactDelegate.find(relation);
		if (contact == null) {
			try {
				contact = this.contactDelegate.create(relation, null, null);
			} catch (ContactExistsException e) {
				throw new AssertionError("Contact exists", e);
			}
		}
		return this.telephoneNumberDelegate.create(
				contact, value, extension, primary, active, category);
	}

	/** {@inheritDoc} */
	@Override
	public TelephoneNumber updateTelephoneNumber(
			final TelephoneNumber telephoneNumber, final Long value,
			final Integer extension, final Boolean primary, 
			final Boolean active, final TelephoneNumberCategory category)
					throws TelephoneNumberExistsException {
		return this.telephoneNumberDelegate.update(
				telephoneNumber, value, extension, primary, active, category);
	}

	/** {@inheritDoc} */
	@Override
	public void removeTelephoneNumber(final TelephoneNumber telephoneNumber) {
		this.telephoneNumberDelegate.remove(telephoneNumber);
	}

	/** {@inheritDoc} */
	@Override
	public OnlineAccount createOnlineAccount(
			final Person relation, final String name,
			final OnlineAccountHost host, final Boolean primary, 
			final Boolean active)
					throws OnlineAccountExistsException {
		Contact contact = this.contactDelegate.find(relation);
		if (contact == null) {
			try {
				contact = this.contactDelegate.create(relation, null, null);
			} catch (ContactExistsException e) {
				throw new AssertionError("Contact exists", e);
			}
		}
		return this.onlineAccountDelegate.create(contact, name, active, primary,
				host);
	}

	/** {@inheritDoc} */
	@Override
	public OnlineAccount updateOnlineAccount(
			final OnlineAccount onlineAccount, final String name,
			final OnlineAccountHost host, final Boolean primary, 
			final Boolean active)
					throws OnlineAccountExistsException {
		return this.onlineAccountDelegate.update(
				onlineAccount, name, active, primary, host);
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
	public City createCity(
			final String name, final State state, final Country country)
					throws CityExistsException {
		return this.cityDelegate.create(name, true, state, country);
	}

	/** {@inheritDoc} */
	@Override
	public ZipCode createZipCode(
			final String value, final String extension, final City city)
					throws ZipCodeExistsException {
		return this.zipCodeDelegate.create(city, value, extension, true);
	}

	/** {@inheritDoc} */
	@Override
	public List<ZipCode> findZipCodesByCity(final City city) {
		return this.zipCodeDelegate.findByCity(city);
	}

	/** {@inheritDoc} */
	@SuppressWarnings("deprecation")
	@Override
	public List<Address> findAddressesByQuery(final String query) {
		return this.addressDelegate.findAddressesByValue(query);
	}
}