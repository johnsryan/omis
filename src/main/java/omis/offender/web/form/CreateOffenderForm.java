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
package omis.offender.web.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import omis.country.domain.Country;
import omis.demographics.domain.Build;
import omis.demographics.domain.Complexion;
import omis.demographics.domain.DominantSide;
import omis.demographics.domain.EyeColor;
import omis.demographics.domain.HairColor;
import omis.demographics.domain.MaritalStatus;
import omis.demographics.domain.Race;
import omis.demographics.domain.Sex;
import omis.demographics.domain.Tribe;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.religion.domain.Religion;

/**
 * Form for offenders.
 * 
 * @author Stephen Abson
 * @version 0.1.2 (Sept 24, 2013)
 * @since OMIS 3.0
 */
public class CreateOffenderForm implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String lastName;
	
	private String firstName;
	
	private String middleName;
	
	private String suffix;
	
	private Date birthDate;
	
	private City birthPlace;
	
	private Boolean createNewBirthPlace;
	
	private String birthPlaceName;
	
	private State birthState;
	
	private Country birthCountry;
	
	private String socialSecurityNumber;
	
	private String stateIdNumber;
	
	private Sex sex;
	
	private EyeColor eyeColor;
	
	private HairColor hairColor;
	
	private Race race;
	
	private Boolean hispanicEthnicity;
	
	private Tribe tribe;
	
	private MaritalStatus maritalStatus;
	
	private Complexion complexion;
	
	private Religion religion;
	
	private Integer heightFeet;
	
	private Integer heightInches;
	
	private Integer weightPounds;
	
	private Build build;
	
	private DominantSide dominantSide;
	
	private Country countryOfCitizenship;
	
	private AlienResidenceLegality alienResidenceLegality;
	
	private byte[] photoData;

	private Date photoDate;
	
	private List<CreateOffenderFlagItem> flagItems
		= new ArrayList<CreateOffenderFlagItem>();
	
	/** Instantiates a default offender form. */
	public CreateOffenderForm() {
		// Default instantiation
	}
	
	/**
	 * Returns the last name.
	 * 
	 * @return last name
	 */
	public String getLastName() {
		return this.lastName;
	}

	/**
	 * Sets the last name.
	 * 
	 * @param lastName last name
	 */
	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Returns the first name.
	 * 
	 * @return first name
	 */
	public String getFirstName() {
		return this.firstName;
	}

	/**
	 * Sets the first name.
	 * 
	 * @param firstName first name
	 */
	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Returns the middle name.
	 * 
	 * @return middle name
	 */
	public String getMiddleName() {
		return this.middleName;
	}

	/**
	 * Sets the middle name.
	 * 
	 * @param middleName middle name
	 */
	public void setMiddleName(final String middleName) {
		this.middleName = middleName;
	}

	/**
	 * Returns the suffix.
	 * 
	 * @return suffix
	 */
	public String getSuffix() {
		return this.suffix;
	}

	/**
	 * Sets the suffix.
	 * 
	 * @param suffix suffix
	 */
	public void setSuffix(final String suffix) {
		this.suffix = suffix;
	}

	/**
	 * Returns the birth date.
	 * 
	 * @return birth date
	 */
	public Date getBirthDate() {
		return this.birthDate;
	}

	/**
	 * Sets the birth date.
	 * 
	 * @param birthDate birth date
	 */
	public void setBirthDate(final Date birthDate) {
		this.birthDate = birthDate;
	}

	/**
	 * Returns the birth place.
	 * 
	 * @return birth place
	 */
	public City getBirthPlace() {
		return this.birthPlace;
	}

	/**
	 * Sets the birth place.
	 * 
	 * @param birthPlace birth place
	 */
	public void setBirthPlace(final City birthPlace) {
		this.birthPlace = birthPlace;
	}

	/**
	 * Returns whether to create new birth place.
	 * 
	 * @return whether to create new birth place
	 */
	public Boolean getCreateNewBirthPlace() {
		return this.createNewBirthPlace;
	}
	
	/**
	 * Sets whether to create new birth place.
	 * 
	 * @param createNewBirthPlace whether to create new birth place
	 */
	public void setCreateNewBirthPlace(final Boolean createNewBirthPlace) {
		this.createNewBirthPlace = createNewBirthPlace;
	}
	
	/**
	 * Returns birth place name.
	 * 
	 * @return birth place name
	 */
	public String getBirthPlaceName() {
		return this.birthPlaceName;
	}
	
	/**
	 * Sets birth place name.
	 * 
	 * @param birthPlaceName birth place name
	 */
	public void setBirthPlaceName(final String birthPlaceName) {
		this.birthPlaceName = birthPlaceName;
	}
	
	/**
	 * Returns the birth country.
	 * 
	 * @return birth country
	 */
	public Country getBirthCountry() {
		return this.birthCountry;
	}

	/**
	 * Sets the birth country.
	 * 
	 * @param birthCountry country
	 */
	public void setBirthCountry(final Country birthCountry) {
		this.birthCountry = birthCountry;
	}

	/**
	 * Returns the birth State.
	 * 
	 * @return birth State
	 */
	public State getBirthState() {
		return this.birthState;
	}

	/**
	 * Sets the birth State.
	 * 
	 * @param birthState birth State
	 */
	public void setBirthState(final State birthState) {
		this.birthState = birthState;
	}

	/**
	 * Returns the social security number.
	 * 
	 * @return social security number
	 */
	public String getSocialSecurityNumber() {
		return this.socialSecurityNumber;
	}

	/**
	 * Sets the social security number.
	 * 
	 * @param socialSecurityNumber social security number
	 */
	public void setSocialSecurityNumber(
			final String socialSecurityNumber) {
		this.socialSecurityNumber = socialSecurityNumber;
	}

	/**
	 * Returns the State ID number.
	 * 
	 * @return State ID number
	 */
	public String getStateIdNumber() {
		return this.stateIdNumber;
	}

	/**
	 * Sets the State ID number.
	 * 
	 * @param stateIdNumber State ID number
	 */
	public void setStateIdNumber(final String stateIdNumber) {
		this.stateIdNumber = stateIdNumber;
	}

	/**
	 * Returns the sex.
	 * 
	 * @return sex
	 */
	public Sex getSex() {
		return this.sex;
	}

	/**
	 * Sets the sex.
	 * 
	 * @param sex sex
	 */
	public void setSex(final Sex sex) {
		this.sex = sex;
	}

	/**
	 * Returns the eye color.
	 * 
	 * @return eye color
	 */
	public EyeColor getEyeColor() {
		return this.eyeColor;
	}

	/**
	 * Sets the eye color.
	 * 
	 * @param eyeColor eye color
	 */
	public void setEyeColor(final EyeColor eyeColor) {
		this.eyeColor = eyeColor;
	}

	/**
	 * Returns the hair color.
	 * 
	 * @return hair color
	 */
	public HairColor getHairColor() {
		return this.hairColor;
	}

	/**
	 * Sets the hair color.
	 * 
	 * @param hairColor hair color
	 */
	public void setHairColor(final HairColor hairColor) {
		this.hairColor = hairColor;
	}

	/**
	 * Returns the race.
	 * 
	 * @return race
	 */
	public Race getRace() {
		return this.race;
	}

	/**
	 * Sets the race.
	 * 
	 * @param race race
	 */
	public void setRace(final Race race) {
		this.race = race;
	}

	/**
	 * Returns whether offender is of Hispanic ethnicity.
	 * 
	 * @return whether offender is of Hispanic ethnicity
	 */
	public Boolean getHispanicEthnicity() {
		return this.hispanicEthnicity;
	}
	
	/**
	 * Sets whether offender is of Hispanic ethnicity.
	 * 
	 * @param hispanicEthnicity whether offender is of Hispanic ethnicity
	 */
	public void setHispanicEthnicity(final Boolean hispanicEthnicity) {
		this.hispanicEthnicity = hispanicEthnicity;
	}
	
	/**
	 * Returns the tribe.
	 * 
	 * @return tribe
	 */
	public Tribe getTribe() {
		return this.tribe;
	}

	/**
	 * Sets the tribe.
	 * 
	 * @param tribe tribe
	 */
	public void setTribe(final Tribe tribe) {
		this.tribe = tribe;
	}

	/**
	 * Returns the marital status.
	 * 
	 * @return marital status
	 */
	public MaritalStatus getMaritalStatus() {
		return this.maritalStatus;
	}

	/**
	 * Sets the marital status.
	 * 
	 * @param maritalStatus marital status
	 */
	public void setMaritalStatus(final MaritalStatus maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	/**
	 * Returns the complexion.
	 * 
	 * @return complexion
	 */
	public Complexion getComplexion() {
		return this.complexion;
	}

	/**
	 * Sets the complexion.
	 * 
	 * @param complexion complexion
	 */
	public void setComplexion(final Complexion complexion) {
		this.complexion = complexion;
	}

	/**
	 * Returns the religion.
	 * 
	 * @return religion
	 */
	public Religion getReligion() {
		return this.religion;
	}

	/**
	 * Sets the religion.
	 * 
	 * @param religion religion
	 */
	public void setReligion(final Religion religion) {
		this.religion = religion;
	}

	/**
	 * Returns the height in feet.
	 * 
	 * @return height in feet
	 */
	public Integer getHeightFeet() {
		return this.heightFeet;
	}

	/**
	 * Sets the height in feet.
	 * 
	 * @param heightFeet height in feet
	 */
	public void setHeightFeet(final Integer heightFeet) {
		this.heightFeet = heightFeet;
	}

	/**
	 * Returns the height in inches.
	 * 
	 * @return height in inches
	 */
	public Integer getHeightInches() {
		return this.heightInches;
	}

	/**
	 * Sets the height in inches.
	 * 
	 * @param heightInches height in inches
	 */
	public void setHeightInches(final Integer heightInches) {
		this.heightInches = heightInches;
	}

	/**
	 * Returns the weight in pounds.
	 * 
	 * @return weight in pounds
	 */
	public Integer getWeightPounds() {
		return this.weightPounds;
	}

	/**
	 * Sets the weight in pounds.
	 * 
	 * @param weightPounds weight in pounds
	 */
	public void setWeightPounds(final Integer weightPounds) {
		this.weightPounds = weightPounds;
	}

	/**
	 * Returns the build.
	 * 
	 * @return build
	 */
	public Build getBuild() {
		return this.build;
	}

	/**
	 * Sets the build.
	 * 
	 * @param build build
	 */
	public void setBuild(final Build build) {
		this.build = build;
	}

	/**
	 * Sets the dominant side.
	 * 
	 * @return dominant side
	 */
	public DominantSide getDominantSide() {
		return this.dominantSide;
	}
	
	/**
	 * Returns the dominant side.
	 * 
	 * @param dominantSide dominant side
	 */
	public void setDominantSide(final DominantSide dominantSide) {
		this.dominantSide = dominantSide;
	}
	
	/**
	 * Returns the country of citizenship.
	 * 
	 * @return country of citizenship
	 */
	public Country getCountryOfCitizenship() {
		return this.countryOfCitizenship;
	}

	/**
	 * Sets the country of citizenship.
	 * 
	 * @param countryOfCitizenship country of citizenship
	 */
	public void setCountryOfCitizenship(final Country countryOfCitizenship) {
		this.countryOfCitizenship = countryOfCitizenship;
	}

	/**
	 * Returns alien residence legality.
	 * 
	 * @return alien residence legality
	 */
	public AlienResidenceLegality getAlienResidenceLegality() {
		return this.alienResidenceLegality;
	}

	/**
	 * Sets alien residence legality.
	 * 
	 * @param alienResidenceLegality alien residence legality
	 */
	public void setAlienResidenceLegality(
			final AlienResidenceLegality alienResidenceLegality) {
		this.alienResidenceLegality = alienResidenceLegality;
	}

	/**
	 * Returns the photo data.
	 * 
	 * @return photo data
	 */
	public byte[] getPhotoData() {
		return this.photoData;
	}

	/**
	 * Sets the photo data.
	 * 
	 * @param photoData photo data
	 */
	public void setPhotoData(final byte[] photoData) {
		this.photoData = photoData;
	}

	/**
	 * Returns the photo date.
	 * 
	 * @return photo date
	 */
	public Date getPhotoDate() {
		return this.photoDate;
	}
	
	/**
	 * Sets the photo date.
	 * 
	 * @param photoDate photo date
	 */
	public void setPhotoDate(final Date photoDate) {
		this.photoDate = photoDate;
	}

	/**
	 * Returns flag items.
	 * 
	 * @return flag items
	 */
	public List<CreateOffenderFlagItem> getFlagItems() {
		return this.flagItems;
	}

	/**
	 * Returns flag items.
	 * 
	 * @param flagItems flag items.
	 */
	public void setFlagItems(
			final List<CreateOffenderFlagItem> flagItems) {
		this.flagItems = flagItems;
	}
}