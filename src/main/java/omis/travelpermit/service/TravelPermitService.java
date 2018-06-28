/*
 *  OMIS - Offender Management Information System
 *  Copyright (C) 2011 - 2017 State of Montana
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package omis.travelpermit.service;

import java.util.Date;
import java.util.List;





import omis.address.domain.Address;
//import omis.address.domain.Address;
import omis.address.domain.ZipCode;
import omis.address.exception.AddressExistsException;
//import omis.address.exception.AddressExistsException;
import omis.address.exception.ZipCodeExistsException;
import omis.country.domain.Country;
import omis.datatype.DateRange;
import omis.exception.DuplicateEntityFoundException;
import omis.offender.domain.Offender;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.region.exception.CityExistsException;
import omis.travelpermit.domain.TravelMethod;
import omis.travelpermit.domain.TravelPermit;
import omis.travelpermit.domain.TravelPermitNote;
import omis.travelpermit.domain.TravelPermitPeriodicity;
import omis.travelpermit.domain.component.OtherTravelers;
import omis.travelpermit.domain.component.TravelDestination;
import omis.travelpermit.domain.component.TravelPermitIssuance;
import omis.travelpermit.domain.component.TravelTransportation;
import omis.travelpermit.exception.TravelPermitExistsException;
import omis.travelpermit.exception.TravelPermitNoteExistsException;
import omis.user.domain.UserAccount;

/**
 * Service for travel permit.
 * 
 * @author Yidong Li
 * @version 0.1.1 (May 18, 2018)
 * @since OMIS 3.0
 */
public interface TravelPermitService {

	/**
	 * Create travel permit.
	 * 
	 * @param offender offender
	 * @param periodicity travel permit periodicity
	 * @param issuance travel permit issuance
	 * @param transportation travel transportation
	 * @param dateRange date range
	 * @param destination travel destination
	 * @param purpose purpose
	 * @param otherTravelers other travelers
	 * @return TravelPermit
	 * @throws TravelPermitExistException travel permit already exists
	 */
	TravelPermit create(Offender offender, String purpose, DateRange dateRange,
		TravelPermitPeriodicity periodicity,
		TravelPermitIssuance issuance, TravelTransportation transportation,
		TravelDestination destination, OtherTravelers otherTravelers)
			throws TravelPermitExistsException;
	
	/**
	 * Update travel permit.
	 * 
	 * @param travel permit travel permit
	 * @param periodicity travel permit periodicity
	 * @param issuance travel permit issuance
	 * @param transportation travel transportation
	 * @param destination travel destination
	 * @param dateRange date range
	 * @param purpose purpose
	 * @param otherTravelers other travelers
	 * @return TravelPermit
	 * @throws TravelPermitExistException travel permit already exists
	 * @return Updated travel permit
	 * @throws DuplicateEntityFoundException if work assignment already exists
	 */
	TravelPermit update(TravelPermit travelPermit, String purpose, DateRange dateRange,
			Offender offender, TravelPermitPeriodicity periodicity,
			TravelPermitIssuance issuance, TravelTransportation transportation,
			TravelDestination destination, OtherTravelers otherTravelers)
			throws TravelPermitExistsException;
	
	/**
	 * Remove travel permit
	 * @param travelPermit travel permit
	 * Returns void 
	 * @return void
	 */
	void remove(TravelPermit travelPermit);
	
	/**
	 * Create note.
	 * 
	 * @param travelPermit travel permit
	 * @param date date
	 * @param value value
	 * @return note
	 * @throws TravelPermitNoteExistException travel permit note already exists
	 */
	TravelPermitNote createNote(TravelPermit travelPermit, Date date, String value)
			throws TravelPermitNoteExistsException;
	
	/**
	 * Update travel permit note.
	 * 
	 * @param travelPermitNote travel permit note
	 * @param date date
	 * @param value value
	 * @return Travel permit note
	 * @throws TravelPermitNoteExistException travel permit note already exists
	 * @return Updated travel permit
	 */
	TravelPermitNote updateNote(TravelPermitNote travelPermitNote, Date date,
		String value) throws TravelPermitNoteExistsException;
	
	
	/**
	 * Remove travel permit note
	 * @param travelPermitNote travel permit note
	 * @Returns void 
	 * @return void
	 */
	void removeNote(TravelPermitNote travelPermitNote);
	
	/**
	 * Create address.
	 * 
	 * @param number number
	 * @param address zipcode
	 * @return Address address
	 * @throws AddressExistException address already exists
	 */
	 Address createAddress(String number, ZipCode address)
		throws AddressExistsException; 
	
	/**
	 * Create city.
	 * 
	 * @param state state
	 * @param name name
	 * @return country country
	 * @throws CityExistException city already exists
	 */
	City createCity(State state, String name, Country country)
		throws CityExistsException;
	
	/**
	 * Create zip code.
	 * 
	 * @param city city
	 * @param value value
	 * @param extension extension
	 * @return ZipCode zip code
	 * @throws ZipCodeExistException zip code already exists
	 */
	ZipCode createZipCode(City city, String value, String extension)
		throws ZipCodeExistsException;
	
	/**
	 * Find travel permit by offender.
	 * 
	 * @param offender offender
	 * @param extension extension
	 * @return A list of travel permit
	 *
	 */
	List<TravelPermit> findByOffender(Offender offender);
	
	/**
	 * Find travel permit notes by travel permit.
	 * 
	 * @param travelPermit travel permit
	 * @return A list of travelPermit
	 */
	List<TravelPermitNote> findNotes(TravelPermit travelPermit);
	
	/**
	 * Find travel method.
	 * 
	 * @return A list of travel methods
	 */
	List<TravelMethod> findTravelMethods();
	
	/**
	 * Find travel permit periodicity.
	 * 
	 * @return A list of travel permit periodicity
	 */
	List<TravelPermitPeriodicity> findPeriodicity();
	
	/**
	 * Find addresses.
	 * @param query query
	 * @return A list of addresses
	 */
	List<Address> findAddresses(String query); 
	
	/**
	 * Find zip codes.
	 * @param city city
	 * @return A list of zip code
	 */
	List<ZipCode> findZipCodes(City city);
	
	/**
	 * Find states.
	 *
	 * @return A list of states
	 */
	List<State> findStates();
	
	/**
	 * Check if country has states.
	 * @param country country
	 *
	 * @return true or false
	 */
	boolean hasStates(Country country);
	
	/**
	 * Find cities by state
	 * @param state state
	 *
	 * @return A list of cities
	 */
	List<City> findCitiesByState(State state);
	
	/**
	 * Find cities by country
	 * @param city city
	 *
	 * @return A list of cities
	 */
	List<City> findCitiesByCountry(Country country);
	
	/**
	 * Find countries//
	 *
	 * @return A list of countries
	 */
	List<Country> findCountries();
	
	/**
	 * Find home country
	 *
	 * @return home country
	 */
	Country findHomeCountry();
	
	/**
	 * Find states by country
	 *
	 * @return list of state
	 */
	List<State> findStatesByCountry(Country country);
	
	/**
	 * Find user accounts by input query 
	 *
	 * @return list of user accounts
	 */
	List<UserAccount> searchUserAccounts(String query);
	
	/**
	 * Find cities by country  
	 *
	 * @return list of user accounts
	 */
	List<City> findCitiesByCountryWithoutState(Country country);
}