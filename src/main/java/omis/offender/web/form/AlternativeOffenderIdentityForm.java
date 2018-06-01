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
import java.util.Date;

import omis.country.domain.Country;
import omis.demographics.domain.Sex;
import omis.person.domain.AlternativeIdentityCategory;
import omis.person.domain.AlternativeNameAssociation;
import omis.region.domain.City;
import omis.region.domain.State;

/**
 * Form for alternative offender identities.
 * 
 * @author Stephen Abson
 * @version 0.1.0 (Dec 10, 2013)
 * @since OMIS 3.0
 */
public class AlternativeOffenderIdentityForm
		implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String socialSecurityNumber;
	
	private String stateIdNumber;
	
	private Date birthDate;
	
	private Country birthCountry;
	
	private State birthState;
	
	private City birthPlace;
	
	private Boolean createNewBirthPlace;
	
	private String birthPlaceName;
	
	private Sex sex;
	
	private AlternativeIdentityCategory category;
	
	private Date startDate;
	
	private Date endDate;
	
	private transient Void offenderIdentityGroup;
	
	private AlternativeNameAssociation alternativeNameAssociation;
	
	/** Instantiates a form for alternative offender identities. */
	public AlternativeOffenderIdentityForm() {
		// Default instantiation
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
	public void setSocialSecurityNumber(final String socialSecurityNumber) {
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
	 * Returns the category.
	 * 
	 * @return category
	 */
	public AlternativeIdentityCategory getCategory() {
		return this.category;
	}

	/**
	 * Sets the category.
	 * 
	 * @param category category
	 */
	public void setCategory(final AlternativeIdentityCategory category) {
		this.category = category;
	}

	/**
	 * Returns the start date.
	 * 
	 * @return start date
	 */
	public Date getStartDate() {
		return this.startDate;
	}

	/**
	 * Sets the start date.
	 * 
	 * @param startDate start date
	 */
	public void setStartDate(final Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * Returns the end date.
	 * 
	 * @return end date
	 */
	public Date getEndDate() {
		return this.endDate;
	}

	/**
	 * Sets the end date.
	 * 
	 * @param endDate end date
	 */
	public void setEndDate(final Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * Returns place holder for identity validation messages.
	 * 
	 * @return place holder for identity validation messages
	 */
	public Void getOffenderIdentityGroup() {
		return this.offenderIdentityGroup;
	}

	/**
	 * Sets place holder for identity validation messages.
	 * 
	 * @param offenderIdentityGroup place holder for identity validation
	 * messages
	 */
	public void setOffenderIdentityGroup(final Void offenderIdentityGroup) {
		this.offenderIdentityGroup = offenderIdentityGroup;
	}

	/**
	 * Returns alternative name association.
	 * 
	 * @return the alternative name association
	 */
	public AlternativeNameAssociation getAlternativeNameAssociation() {
		return this.alternativeNameAssociation;
	}

	/**
	 * Sets alternative name association. 
	 * 
	 * @param alternativeNameAssociation alternative name association
	 */
	public void setAlternativeNameAssociation(
			final AlternativeNameAssociation alternativeNameAssociation) {
		this.alternativeNameAssociation = alternativeNameAssociation;
	}
}