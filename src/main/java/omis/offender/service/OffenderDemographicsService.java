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

import java.util.List;

import javax.persistence.EntityNotFoundException;

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
import omis.demographics.domain.Tribe;
import omis.demographics.domain.component.PersonAppearance;
import omis.demographics.domain.component.PersonPhysique;
import omis.immigration.domain.AlienResidence;
import omis.offender.domain.Offender;
import omis.person.domain.Person;
import omis.religion.domain.Religion;

/**
 * Service for offender demographics.
 * 
 * @author Stephen Abson
 * @author Joel Norris
 * @version 0.1.1 (Oct 1, 2014)
 * @since OMIS 3.0
 */
public interface OffenderDemographicsService {

	/**
	 * Returns eye colors.
	 * 
	 * @return eye colors
	 */
	List<EyeColor> findEyeColors();
	
	/**
	 * Returns hair colors.
	 * 
	 * @return hair colors
	 */
	List<HairColor> findHairColors();
	
	/**
	 * Returns complexions.
	 * 
	 * @return complexion
	 */
	List<Complexion> findComplexions();
	
	/**
	 * Returns builds.
	 * 
	 * @return builds
	 */
	List<Build> findBuilds();
	
	/**
	 * Returns races.
	 * 
	 * @return races
	 */
	List<Race> findRaces();
	
	/**
	 * Returns tribes.
	 * 
	 * @return tribes
	 */
	List<Tribe> findTribes();
	
	/**
	 * Returns marital statuses.
	 * 
	 * @return marital statuses
	 */
	List<MaritalStatus> findMaritalStatuses();
	
	/**
	 * Returns religions.
	 * 
	 * @return religions
	 */
	List<Religion> findReligions();
	
	/**
	 * Update the demographic information for the person.
	 * 
	 * @param person person whose demographic information to save
	 * @param physique physique
	 * @param appearance appearance
	 * @param dominantSide dominant side
	 * @param race race
	 * @param hispanicEthnicity whether offender is of Hispanic ethnicity
	 * @param tribe tribe
	 * @param maritalStatus marital status
	 * @return person demographics
	 */
	PersonDemographics updateDemographics(Person person, 
			PersonPhysique physique, PersonAppearance appearance, 
			DominantSide dominantSide, Race race, Boolean hispanicEthnicity,
			Tribe tribe, MaritalStatus maritalStatus);
	
	/**
	 * Returns the demographics for the offender.
	 * 
	 * @param offender offender
	 * @return demographics for offender
	 */
	PersonDemographics findDemographics(Offender offender);
	
	/**
	 * Returns the country of citizenship for the specified offender.
	 *  
	 * @param offender offender
	 * @return country of citizenship
	 */
	Country findCountryOfCitizenship(Offender offender);
	
	/**
	 * Returns whether the specified offender has a legal residence status.
	 * 
	 * @param offender offender
	 * @return whether legal residence status exists
	 */
	Boolean hasLegalResidenceStatus(Offender offender);
	
	/**
	 * Returns the legal residence status for the specified offender.
	 * 
	 * @param offender offender
	 * @return legal residence status
	 * @throws EntityNotFoundException thrown when no alien residence is found
	 * for the specified offender
	 */
	Boolean findLegalResidenceStatus(Offender offender) 
			throws EntityNotFoundException;
	
	/**
	 * Returns countries.
	 * 
	 * @return countries
	 */
	List<Country> findCountries();

	/**
	 * Set the legality of the offender's residence status.
	 * 
	 * @param offender offender
	 * @param legal whether the residence is legal
	 * @return changed offenders alien residence status
	 */
	AlienResidence  changeLegalResidenceStatus(Offender offender, 
			Boolean legal);

	/**
	 * Removes the specified offender's alien residence if one exists.
	 * 
	 * @param offender offender
	 */
	void removeAlienResidence(Offender offender);

	/**
	 * Changes the specified offenders country of citizenship.
	 * 
	 * @param offender offender
	 * @param countryOfCitizenship country of citizenship
	 * @return changed offender citizenship
	 */
	Citizenship changeCountryOfCitizenship(Offender offender,
			Country countryOfCitizenship);

	/**
	 * Removes the country of citizenship for the specified offender.
	 * 
	 * @param offender offender
	 */
	void removeCountryOfCitizienship(Offender offender);

	/**
	 * Returns whether country is home country.
	 * 
	 * @param country country
	 * @return whether country is home country
	 */
	boolean isHomeCountry(Country country);
}