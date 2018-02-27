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
import omis.person.domain.Person;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.region.exception.CityExistsException;
import omis.region.service.delegate.CityDelegate;
import omis.region.service.delegate.StateDelegate;
import omis.residence.domain.ResidenceCategory;
import omis.residence.domain.ResidenceStatus;
import omis.residence.domain.ResidenceTerm;
import omis.residence.exception.ResidenceTermExistsException;
import omis.residence.service.delegate.ResidenceTermDelegate;
import omis.victim.service.VictimContactService;

/**
 * Implementation of service for victim contacts.
 *
 * @author Stephen Abson
 * @author Yidong Li
 * @author Sheronda Vaughn
 * @version 0.0.1 (Jan 5, 2016)
 * @since OMIS 3.0
 */
public class VictimContactServiceImpl
		implements VictimContactService {
	
	private final AddressDelegate addressDelegate;
	
	private final ResidenceTermDelegate residenceTermDelegate;
	
	private final ContactDelegate contactDelegate;
	
	private final TelephoneNumberDelegate telephoneNumberDelegate;
	
	private final OnlineAccountDelegate onlineAccountDelegate;
	
	private final OnlineAccountHostDelegate onlineAccountHostDelegate;
	
	private final CountryDelegate countryDelegate;
	
	private final StateDelegate stateDelegate;
	
	private final CityDelegate cityDelegate;
	
	private final AddressUnitDesignatorDelegate addressUnitDesignatorDelegate;
	
	private final StreetSuffixDelegate streetSuffixDelegate;
	
	private final ZipCodeDelegate zipCodeDelegate;

	/**
	 * Instantiates implementation of service for victim contacts.
	 * 
	 * @param addressDelegate delegate for addresses
	 * @param residenceTermDelegate delegate for residence terms
	 * @param contactDelegate delegate for contacts
	 * @param telephoneNumberDelegate delegate for telephone numbers
	 * @param onlineAccountDelegate delegate for online accounts
	 * @param onlineAccountHostDelegate delegate for online account hosts
	 * @param countryDelegate delegate for countries
	 * @param stateDelegate delegate for States
	 * @param cityDelegate delegate for cities
	 * @param addressUnitDesignatorDelegate delegate for address unit
	 * designators
	 * @param streetSuffixDelegate delegate for street suffix
	 * @param zipCodeDelegate delegate for ZIP codes
	 */
	public VictimContactServiceImpl(
			final AddressDelegate addressDelegate,
			final ResidenceTermDelegate residenceTermDelegate,
			final ContactDelegate contactDelegate,
			final TelephoneNumberDelegate telephoneNumberDelegate,
			final OnlineAccountDelegate onlineAccountDelegate,
			final OnlineAccountHostDelegate onlineAccountHostDelegate,
			final CountryDelegate countryDelegate,
			final StateDelegate stateDelegate,
			final CityDelegate cityDelegate,
			final AddressUnitDesignatorDelegate addressUnitDesignatorDelegate,
			final StreetSuffixDelegate streetSuffixDelegate,
			final ZipCodeDelegate zipCodeDelegate) {
		this.addressDelegate = addressDelegate;
		this.residenceTermDelegate = residenceTermDelegate;
		this.contactDelegate = contactDelegate;
		this.telephoneNumberDelegate = telephoneNumberDelegate;
		this.onlineAccountDelegate = onlineAccountDelegate;
		this.onlineAccountHostDelegate = onlineAccountHostDelegate;
		this.countryDelegate = countryDelegate;
		this.stateDelegate = stateDelegate;
		this.cityDelegate = cityDelegate;
		this.addressUnitDesignatorDelegate = addressUnitDesignatorDelegate;
		this.streetSuffixDelegate = streetSuffixDelegate;
		this.zipCodeDelegate = zipCodeDelegate;
	}
	
	/** {@inheritDoc} */
	@Override
	public Address createAddress(
			final String houseNumber, final BuildingCategory buildingCategory,
			final ZipCode zipCode)
				throws AddressExistsException {
		return this.addressDelegate.findOrCreate(houseNumber, null, null,
				buildingCategory, zipCode);
	}

	/** {@inheritDoc} */
	@Override
	public Contact findContact(final Person victim) {
		return this.contactDelegate.find(victim);
	}

	/** {@inheritDoc} */
	@Override
	public Contact updateContact(
			final Person victim, final Address mailingAddress,
			final PoBox poBox) {
		Contact contact = this.contactDelegate.find(victim);
		if (contact != null) {
			try {
				contact = this.contactDelegate.update(
						contact, mailingAddress, poBox);
			} catch (ContactExistsException exception) {
				throw new AssertionError(exception.getMessage());
			}
		} else {
			try {
				contact = this.contactDelegate.create(
						victim, mailingAddress, poBox);
			} catch (ContactExistsException exception) {
				throw new AssertionError(exception.getMessage());
			}
		}
		return contact;
	}

	/** {@inheritDoc} */
	@Override
	public ResidenceTerm updateResidenceTerm(
			final Person victim, final Address address) 
					throws ResidenceTermExistsException {
		
		// Assumes on primary residence address per person
		// Do nothing if a residence term reflecting this exists
		ResidenceTerm residenceTerm = this.residenceTermDelegate
				.find(victim, null, null, address,
						ResidenceCategory.PRIMARY, ResidenceStatus.RESIDENT);
		if (residenceTerm == null) {
			try {
				residenceTerm = this.residenceTermDelegate
					.createResidenceTerm(victim, null,
							ResidenceCategory.PRIMARY, address,
							ResidenceStatus.RESIDENT, null, null, null);
			} catch (ResidenceTermExistsException exception) {
				throw new AssertionError(exception.getMessage());
			}
		}
		return residenceTerm;
	}

	/** {@inheritDoc} */
	@Override
	public TelephoneNumber addTelephoneNumber(
			final Contact contact, final Long value,
			final Integer extension, final Boolean primary,
			final TelephoneNumberCategory category)
					throws TelephoneNumberExistsException {
		return this.telephoneNumberDelegate.create(contact, value, extension,
				primary, true, category);
	}

	/** {@inheritDoc} */
	@Override
	public OnlineAccount addOnlineAccount(final Contact contact,
			final String name, final Boolean primary,
			final OnlineAccountHost host)
					throws OnlineAccountExistsException {
		return this.onlineAccountDelegate.create(
				contact, name, true, primary, host);
	}
	
	/** {@inheritDoc} */
	@Override
	public TelephoneNumber updateTelephoneNumber(
			final TelephoneNumber telephoneNumber, final Long value,
			final Integer extension, final Boolean primary,
			final TelephoneNumberCategory category)
					throws TelephoneNumberExistsException {
		return this.telephoneNumberDelegate.update(telephoneNumber, value,
				extension, primary, true, category);
	}

	/** {@inheritDoc} */
	@Override
	public OnlineAccount updateOnlineAccount(
			final OnlineAccount onlineAccount, final String name,
			final Boolean primary, final OnlineAccountHost host)
					throws OnlineAccountExistsException {
		return this.onlineAccountDelegate.update(
				onlineAccount, name, true, primary, host);
	}
	
	/** {@inheritDoc} */
	@Override
	public void removeTelephoneNumber(final TelephoneNumber telephoneNumber) {
		this.telephoneNumberDelegate.remove(telephoneNumber);
	}

	/** {@inheritDoc} */
	@Override
	public void removeOnlineAccount(final OnlineAccount onlineAccount) {
		this.onlineAccountDelegate.remove(onlineAccount);
	}
	
	/** {@inheritDoc} */
	@Override
	public List<TelephoneNumber> findTelephoneNumbers(
			final Contact victimContact) {
		return this.telephoneNumberDelegate.findByContact(victimContact);
	}
	
	/** {@inheritDoc} */
	@Override
	public List<OnlineAccount> findOnlineAccounts(final Contact contact) {
		return this.onlineAccountDelegate.findByContact(contact);
	}

	/** {@inheritDoc} */
	@Override
	public List<OnlineAccountHost> findOnlineAccountHosts() {
		return this.onlineAccountHostDelegate.findAll();
	}

	/** {@inheritDoc} */
	@Override
	public List<Country> findCountries() {
		return this.countryDelegate.findAll();
	}
	
	/** {@inheritDoc} */
	@Override
	public List<State> findStatesByCountry(final Country country) {
		return this.stateDelegate.findByCountry(country);
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean hasStates(final Country country) {
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
	public List<AddressUnitDesignator> findAddressUnitDesignators() {
		return this.addressUnitDesignatorDelegate.findAll();
	}
	
	/** {@inheritDoc} */
	@Override
	public List<StreetSuffix> findStreetSuffix() {
		return this.streetSuffixDelegate.findAll();
	}
	
	/** {@inheritDoc} */
	@Override
	public List<ZipCode> findZipCodesByCity(final City city) {
		return this.zipCodeDelegate.findByCity(city);
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
}