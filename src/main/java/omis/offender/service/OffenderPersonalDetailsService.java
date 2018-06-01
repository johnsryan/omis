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
package omis.offender.service;

import java.util.Date;
import java.util.List;

import omis.country.domain.Country;
import omis.demographics.domain.Sex;
import omis.exception.DuplicateEntityFoundException;
import omis.offender.domain.Offender;
import omis.person.domain.PersonIdentity;
import omis.person.domain.PersonName;
import omis.person.domain.Suffix;
import omis.person.exception.PersonIdentityExistsException;
import omis.person.exception.PersonNameExistsException;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.region.exception.CityExistsException;

/**
 * Offender personal details service.
 * 
 * @author Joel Norris
 * @author Stephen Abson
 * @version 0.1.0 (Sep 26, 2014)
 * @since OMIS 3.0
 */
public interface OffenderPersonalDetailsService {

	/**
	 * Updates the person name for the specified offender.
	 * 
	 * @param offender offender
	 * @param lastName last name
	 * @param firstName first name
	 * @param middleName middle name
	 * @param suffix suffix
	 * @return updated person name
	 * @throws PersonNameExistsException if name exists
	 */
	PersonName updateName(Offender offender, String lastName, String firstName, 
			String middleName, String suffix)
					throws PersonNameExistsException;
	
	/**
	 * Changes the identity of the specified offender with the specified
	 * properties.
	 * 
	 * @param offender offender
	 * @param birthDate birth date
	 * @param birthCountry birth country
	 * @param birthState birth State
	 * @param birthPlace birth city
	 * @param socialSecurityNumber social security number
	 * @param stateIdNumber state ID number
	 * @param sex sex
	 * @param deceased whether offender is deceased
	 * @param deathDate date of death
	 * @return changed, including SSN, person identity
	 * @throws PersonIdentityExistsException if person identity exists 
	 */
	PersonIdentity changeIdentity(Offender offender, Date birthDate, 
			Country birthCountry, State birthState, City birthPlace,
			Integer socialSecurityNumber, String stateIdNumber, Sex sex,
			Boolean deceased, Date deathDate) 
					throws PersonIdentityExistsException;
	
	/**
	 * Changes the identity of the specified offender with the specified
	 * properties (does not include social security number).
	 * 
	 * @param offender offender
	 * @param birthDate birth date
	 * @param birthCountry birth county
	 * @param birthState birth State
	 * @param birthPlace birth place
	 * @param stateIdNumber state id number
	 * @param sex sex
	 * @param deceased whether offender is deceased
	 * @param deathDate date of death
	 * @return changed, not including SSN, person identity
	 * @throws DuplicateEntityFoundException 
	 */
	PersonIdentity changeIdentityWithoutSsn(Offender offender, Date birthDate, 
			Country birthCountry, State birthState, City birthPlace,
			String stateIdNumber, Sex sex, Boolean deceased, Date deathDate)
					throws PersonIdentityExistsException;
	
	/**
	 * Returns a list of suffixes.
	 * 
	 * @return list of suffixes
	 */
	List<Suffix> findSuffixes();

	/**
	 * Returns a list of countries.
	 * 
	 * @return list of countries
	 */
	List<Country> findCountries();
	
	/**
	 * Returns a list of states.
	 * 
	 * @param country country
	 * @return list of states
	 */
	List<State> findStatesByCountry(Country country);
	
	/**
	 * Returns a list of cities.
	 * 
	 * @param state state
	 * @return list of cities
	 */
	List<City> findCitiesByState(State state);
	
	/**
	 * Returns cities by country.
	 * 
	 * @param country cities by country
	 * @return cities by country
	 */
	List<City> findCitiesByCountry(Country country);
	
	/**
	 * Returns cities by country without State.
	 * 
	 * @param country country without State
	 * @return cities by country without State
	 */
	List<City> findCitiesByCountryWithoutState(Country country);
	
	/**
	 * Returns whether country has States.
	 * 
	 * @param country country
	 * @return whether country has States
	 */
	boolean hasStates(Country country);
	
	/**
	 * Creates city.
	 * 
	 * @param name name
	 * @param state State
	 * @param country country
	 * @return created city
	 * @throws CityExistsException if city exists
	 */
	City createCity(String name, State state, Country country)
		throws CityExistsException;
}