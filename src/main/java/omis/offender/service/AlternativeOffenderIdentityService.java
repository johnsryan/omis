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
import omis.datatype.DateRange;
import omis.demographics.domain.Sex;
import omis.offender.domain.Offender;
import omis.person.domain.AlternativeIdentityAssociation;
import omis.person.domain.AlternativeIdentityCategory;
import omis.person.domain.AlternativeNameAssociation;
import omis.person.domain.PersonIdentity;
import omis.person.exception.AlternativeIdentityAssociationExistsException;
import omis.person.exception.PersonIdentityExistsException;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.region.exception.CityExistsException;

/**
 * Service for alternative offender identities.
 * 
 * @author Stephen Abson
 * @author Sheronda Vaughn
 * @version 0.1.0 (Dec 10, 2013)
 * @since OMIS 3.0
 */
public interface AlternativeOffenderIdentityService {
	
	/**
	 * Creates a new alternative identity association for the specified
	 * offender.
	 * 
	 * @param offender offender
	 * @param alternativeNameAssociation alternative name association
	 * @param birthDate birth date
	 * @param birthCountry birth country
	 * @param birthPlace birth city
	 * @param socialSecurityNumber social security number
	 * @param stateIdNumber state ID number
	 * @param sex sex
	 * @param dateRange date range
	 * @param category alternative identity category
	 * @return alternative identity association
	 * @throws AlternativeIdentityAssociationExistsException thrown when alternative
	 * identity association exists
	 * @throws PersonIdentityExistsException thrown when identity exists
	 */
	AlternativeIdentityAssociation associate(Offender offender, 
			AlternativeNameAssociation alternativeNameAssociatino, 
			Date birthDate, Country birthCountry, City birthPlace, 
			Integer socialSecurityNumber, String stateIdNumber, Sex sex, 
			DateRange dateRange, AlternativeIdentityCategory category)
					throws AlternativeIdentityAssociationExistsException,
						PersonIdentityExistsException;
	
	/**
	 * Updates the specified alternative identity association.
	 * 
	 * @param association alternative identity association
	 * @param alternativeNameAssociation alternative name association
	 * @param birthDate birth date
	 * @param birthCountry birth country
	 * @param birthPlace birth city
	 * @param socialSecurityNumber social security number
	 * @param stateIdNumber state ID number
	 * @param sex sex
	 * @param dateRange date range
	 * @param category alternative identity category
	 * @return alternative identity association
	 * @throws AlternativeIdentityAssociationExistsException thrown when
	 * alternative identity association exists
	 * @throws PersonIdentityExistsException if identity exists
	 */
	AlternativeIdentityAssociation updateAssociation(
			AlternativeIdentityAssociation association, 
			AlternativeNameAssociation alternativeNameAssociation, 
			Date birthDate, Country birthCountry, City birthPlace, 
			Integer socialSecurityNumber, String stateIdNumber, Sex sex, 
			DateRange dateRange, AlternativeIdentityCategory category)
					throws AlternativeIdentityAssociationExistsException,
						PersonIdentityExistsException;
	
	/**
	 * Updates the specified alternative identity association without a 
	 * social security number.
	 * 
	 * @param association association
	 * @param alternativeNameAssociation alternative name association
	 * @param birthDate birth date
	 * @param birthCountry birth country
	 * @param birthPlace birth place
	 * @param stateIdNumber state ID number
	 * @param sex sex
	 * @param dateRange date range
	 * @param category category
	 * @return alternative identity association 
	 * @throws AlternativeIdentityAssociationExistsException thrown when 
	 * alternative identity association exists
	 * @throws PersonIdentityExistsException if identity exists
	 */
	AlternativeIdentityAssociation updateAssociationWithoutSsn(
			AlternativeIdentityAssociation association, 
			AlternativeNameAssociation alternativeNameAssociation, 
			Date birthDate, Country birthCountry, City birthPlace, 
			String stateIdNumber, Sex sex, DateRange dateRange, 
			AlternativeIdentityCategory category) 
					throws AlternativeIdentityAssociationExistsException,
						PersonIdentityExistsException;

	/**
	 * Removes an alternative offender identity.
	 * 
	 * @param alternativeIdentityAssociation association of alternative offender
	 * identity to remove
	 */
	void remove(AlternativeIdentityAssociation alternativeIdentityAssociation);
	
	/**
	 * Returns alternative identities for offender.
	 * 
	 * <p>Alternative offender identities are not equal to
	 * {@code offender.getIdentity()}.
	 * 
	 * @param offender offender
	 * @return alternative identities for offender
	 */
	List<PersonIdentity> findAlternativeIdentities(Offender offender);
	
	/**
	 * Returns alternative identity associations for offender.
	 * 
	 * @param offender offender
	 * @return alternative identity associations for offender
	 */
	List<AlternativeIdentityAssociation> findAssociations(Offender offender);
	
	/**
	 * Returns alternative identity categories.
	 * 
	 * @return alternative identity categories
	 */
	List<AlternativeIdentityCategory> findCategories();
	
	/**
	 * Returns countries.
	 * 
	 * @return countries
	 */
	List<Country> findCountries();
	
	/**
	 * Returns States by country.
	 * 
	 * @param country country
	 * @return States by country
	 */
	List<State> findStatesByCountry(Country country);
	
	/**
	 * Returns cities by State.
	 * 
	 * @param state State
	 * @return cities by State
	 */
	List<City> findCitiesByState(State state);
	
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
	
	/**
	 * Returns cities by countries.
	 * 
	 * @param country country
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
	 * Finds a list of alternative names by offender. 
	 * 
	 * @param offender offender
	 * @return alternative names
	 */
	List<AlternativeNameAssociation> findAlternativeNames(Offender offender);
	
	/**
	 * Returns whether country has States.
	 * 
	 * @param country country
	 * @return whether country has States
	 */
	boolean hasStates(Country country);

	/**
	 * Returns home State.
	 * 
	 * @return home State
	 */
	State findHomeState();	
}