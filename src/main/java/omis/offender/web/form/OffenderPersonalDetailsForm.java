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

import java.util.Date;

import omis.country.domain.Country;
import omis.demographics.domain.Sex;
import omis.region.domain.City;
import omis.region.domain.State;

/**
 * Form for offender personal details.
 * 
 * @author Stephen Abson
 * @version 0.1.0 (Sep 11, 2013)
 * @since OMIS 3.0
 */
public class OffenderPersonalDetailsForm {

	private String lastName;
	
	private String firstName;
	
	private String middleName;
	
	private String suffix;
	
	private Date birthDate;
	
	private Country birthCountry;
	
	private State birthState;
	
	private City birthPlace;
	
	private Boolean createNewBirthPlace;
	
	private String birthPlaceName;
	
	private String socialSecurityNumber;
	
	private String stateIdNumber;
	
	private boolean validateSocialSecurityNumber;
	
	private Sex sex;
	
	private Boolean deceased;
	
	private Date deathDate;
	
	/** Instantiates a form for offender personal details. */
	public OffenderPersonalDetailsForm() {
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
	 * Returns the last name.
	 * 
	 * @return last name
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
	 * Sets the suffix
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
	 * @param birthCountry birth country
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
	 * Returns whether to validate social security number.
	 * 
	 * @return whether to validate social security number
	 */
	public boolean getValidateSocialSecurityNumber() {
		return this.validateSocialSecurityNumber;
	}

	/**
	 * Sets whether to validate social security number.
	 * 
	 * @param validateSocialSecurityNumber whether to validate social security
	 * number
	 */
	public void setValidateSocialSecurityNumber(
			final boolean validateSocialSecurityNumber) {
		this.validateSocialSecurityNumber = validateSocialSecurityNumber;
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
	 * Returns whether offender is deceased.
	 * 
	 * @return whether offender is deceased
	 */
	public Boolean getDeceased() {
		return this.deceased;
	}

	/**
	 * Sets whether offender is deceased.
	 * 
	 * @param deceased whether offender is deceased
	 */
	public void setDeceased(final Boolean deceased) {
		this.deceased = deceased;
	}

	/**
	 * Returns date of death.
	 * 
	 * @return date of death
	 */
	public Date getDeathDate() {
		return this.deathDate;
	}

	/**
	 * Sets date of death.
	 * 
	 * @param deathDate date of death
	 */
	public void setDeathDate(final Date deathDate) {
		this.deathDate = deathDate;
	}
}