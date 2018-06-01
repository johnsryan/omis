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

import omis.citizenship.domain.Citizenship;
import omis.country.domain.Country;
import omis.demographics.domain.Build;
import omis.demographics.domain.Complexion;
import omis.demographics.domain.DominantSide;
import omis.demographics.domain.EyeColor;
import omis.demographics.domain.HairColor;
import omis.demographics.domain.MaritalStatus;
import omis.demographics.domain.PersonDemographics;
import omis.demographics.domain.Race;
import omis.demographics.domain.Sex;
import omis.demographics.domain.Tribe;
import omis.demographics.domain.component.PersonAppearance;
import omis.demographics.domain.component.PersonPhysique;
import omis.exception.DateConflictException;
import omis.exception.DuplicateEntityFoundException;
import omis.exception.OperationNotAuthorizedException;
import omis.immigration.domain.AlienResidence;
import omis.offender.domain.Offender;
import omis.offender.exception.OffenderExistsException;
import omis.offenderflag.domain.OffenderFlag;
import omis.offenderflag.domain.OffenderFlagCategory;
import omis.offenderphoto.domain.OffenderPhotoAssociation;
import omis.person.domain.Person;
import omis.person.domain.Suffix;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.religion.domain.Religion;
import omis.religion.domain.ReligiousPreference;

/**
 * Create offender service.
 * 
 * @author Joel Norris
 * @author Stephen Abson
 * @version 0.1.1 (May 10, 2017)
 * @since OMIS 3.0
 */
public interface CreateOffenderService {

	/**
	 * Creates a new, unique, offender with the specified properties.
	 * 
	 * @param lastName last name
	 * @param firstName first name
	 * @param middleName middle name
	 * @param suffix suffix
	 * @param socialSecurityNumber social security number
	 * @param stateIdNumber state identification number
	 * @param birthDate birth date
	 * @param birthCountry birth country
	 * @param birthPlace birth city
	 * @param sex sex
	 * @return newly created offender
	 * @throws DuplicateEntityFoundException thrown when an offender is found
	 * with the same unique properties that were provided.
	 */
	Offender create(String lastName, String firstName, String middleName, 
			String suffix, Integer socialSecurityNumber, String stateIdNumber, 
			Date birthDate, Country birthCountry, City birthPlace, Sex sex) 
			throws DuplicateEntityFoundException;
	
	/**
	 * Converts person into offender.
	 * 
	 * @param person person to convert
	 * @param lastName last name
	 * @param firstName first name
	 * @param middleName middle name
	 * @param suffix suffix
	 * @param socialSecurityNumber social security number
	 * @param stateIdNumber state identification number
	 * @param birthDate birth date
	 * @param birthCountry birth country
	 * @param birthPlace birth city
	 * @param sex sex
	 * @return offender converted from person
	 * @throws OffenderExistsException thrown when person is already
	 * offender
	 */
	Offender convertPerson(Person person, String lastName, String firstName,
			String middleName, String suffix, Integer socialSecurityNumber,
			String stateIdNumber, Date birthDate, Country birthCountry,
			City birthPlace, Sex sex) throws OffenderExistsException;
	
	/**
	 * Adds the specified demographic information to the specified offender's
	 * demographics.
	 * 
	 * @param offender offender
	 * @param physique person physique
	 * @param appearance person appearance
	 * @param dominantSide dominant side
	 * @param race race
	 * @param hispanicEthnicity whether offender is of Hispanic ethnicity
	 * @param tribe tribe
	 * @param maritalStatus marital status
	 * @return person demographics
	 * @throws DuplicateEntityFoundException thrown when person demographics are
	 * found with the same unique information for the same offender
	 */
	PersonDemographics addDemographics(Offender offender, 
			PersonPhysique physique, PersonAppearance appearance, 
			DominantSide dominantSide, Race race, Boolean hispanicEthnicity,
			Tribe tribe, MaritalStatus maritalStatus)
					throws DuplicateEntityFoundException;
	
	/**
	 * Creates a new religious preference for the specified offender with the
	 * specified religion.
	 * 
	 * @param offender offender
	 * @param religion religion
	 * @return religious preference
	 * @throws OperationNotAuthorizedException 
	 * @throws DateConflictException 
	 * @throws DuplicateEntityFoundException 
	 */
	ReligiousPreference addReligiousPreference(Offender offender, 
			Religion religion)
					throws DuplicateEntityFoundException,
					DateConflictException, OperationNotAuthorizedException;
	
	/**
	 * Sets the country of citizenship for the specified offender to the 
	 * specified country.
	 * 
	 * @param offender offender
	 * @param country country
	 * @return citizenship
	 */
	Citizenship setCountryOfCitizenship(Offender offender, Country country);
	
	/**
	 * Sets the legal residence status of the specified offender.
	 * 
	 * @param offender offender
	 * @param legal whether legal status applies
	 * @return alien residence
	 */
	AlienResidence setLegalResidenceStatus(Offender offender, Boolean legal);
	
	/**
	 * Sets the offender flag for the specified offender with the specified
	 * category, and states if the flag is valid.
	 * 
	 * @param offender offender
	 * @param category offender flag category
	 * @param value the value of this particular flag
	 * @return offender flag
	 * @throws DuplicateEntityFoundException 
	 */
	OffenderFlag setFlag(Offender offender, OffenderFlagCategory category, 
			Boolean value) throws DuplicateEntityFoundException;
	
	/**
	 * Associates the specified photo's filename with the specified offender
	 * as a profile photo.
	 * 
	 * @param offender offender
	 * @param filename file name
	 * @param photoDate photo date
	 * @return offender photo association
	 * @throws DuplicateEntityFoundException 
	 */
	OffenderPhotoAssociation associateProfilePhoto(Offender offender, 
			String filename, Date photoDate)
					throws DuplicateEntityFoundException;
	
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
	List<State> findStatesByCountry(Country country);
	
	/**
	 * Returns cities for the specified state.
	 * 
	 * @param state state
	 * @return list of cities
	 */
	List<City> findCitiesByState(State state);
	
	/**
	 * Returns cities by country.
	 * 
	 * @param country country
	 * @return cities by country
	 */
	List<City> findCitiesByCountry(Country country);
	
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
	 * @throws DuplicateEntityFoundException if city exists
	 */
	City createCity(String name, State state, Country country)
		throws DuplicateEntityFoundException;
	
	/**
	 * Returns builds.
	 * 
	 * @return list of builds
	 */
	List<Build> findBuilds();
	
	/**
	 * Returns complexions.
	 * 
	 * @return list of complexions
	 */
	List<Complexion> findComplexions();
	
	/**
	 * Returns eye colors.
	 * 
	 * @return list of eye colors.
	 */
	List<EyeColor> findEyeColors();
	
	/**
	 * Returns hair colors.
	 * 
	 * @return list of hair colors
	 */
	List<HairColor> findHairColors();
	
	/**
	 * Returns marital statuses.
	 * 
	 * @return list of marital statuses
	 */
	List<MaritalStatus> findMaritalStatuses();
	
	/**
	 * Returns races.
	 * 
	 * @return list of races
	 */
	List<Race> findRaces();
	
	/**
	 * Returns tribes.
	 * 
	 * @return list of tribes
	 */
	List<Tribe> findTribes();
	
	/**
	 * Returns suffixes.
	 * 
	 * @return list of suffixes
	 */
	List<Suffix> findSuffixes();
	
	/**
	 * Returns religions.
	 * 
	 * @return list of religions
	 */
	List<Religion> findReligions();
	
	/**
	 * Returns required categories.
	 * 
	 * @return required categories
	 */
	List<OffenderFlagCategory> findRequiredCategories();
	
	/**
	 * Returns home state.
	 * 
	 * @return home state
	 */
	State findHomeState();
	
	/**
	 * Returns whether country is home country.
	 * 
	 * @param country country
	 * @return whether country is home country
	 */
	boolean isHomeCountry(Country country);
	
	/**
	 * Returns whether the offender has the required flags.
	 * 
	 * @param offender offender
	 * @return whether offender has required flags
	 */
	boolean hasRequiredFlags(Offender offender);
}