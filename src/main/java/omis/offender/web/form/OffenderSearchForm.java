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
import java.util.List;

import omis.address.domain.Address;
import omis.demographics.domain.Sex;
import omis.location.domain.Location;

/**
 * Form to search offenders.
 *
 * @author Sheronda Vaughn
 * @version 0.1.0 (Dec 02, 2016)
 * @since OMIS 3.0
 */
public class OffenderSearchForm implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private OffenderSearchType type;
	
	private Integer docIdNumber;
	
	private String lastName;
	
	private String firstName;
	
	private String middleName;
	
	private Sex sex;
	
	private Location location;
	
	private Address address;
	
	private Date birthDate;
	
	private Integer socialSecurityNumber;
	
	private Boolean searchActiveOffenders;
	
	private Boolean displayOffenderPhoto;
	
	private List<Sex> sexes;
	
	private List<Location> locations;
	
	/** Instantiates a default offender form. */
	public OffenderSearchForm() {
		//Default instantiation
	}
	
	/**
	 * Returns the offender search type.
	 * 
	 * @return the type
	 */
	public OffenderSearchType getType() {
		return this.type;
	}

	/**
	 * Sets the offender search type.
	 * 
	 * @param type type
	 */
	public void setType(final OffenderSearchType type) {
		this.type = type;
	}

	/**
	 *
	 * @return the docIdNumber
	 */
	public Integer getDocIdNumber() {
		return this.docIdNumber;
	}

	/**
	 * @param docIdNumber the docIdNumber to set
	 */
	public void setDocIdNumber(final Integer docIdNumber) {
		this.docIdNumber = docIdNumber;
	}

	/**
	 *
	 * @return the lastName
	 */
	public String getLastName() {
		return this.lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	/**
	 *
	 * @return the firstName
	 */
	public String getFirstName() {
		return this.firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	/**
	 *
	 * @return the middleName
	 */
	public String getMiddleName() {
		return this.middleName;
	}

	/**
	 * @param middleName the middleName to set
	 */
	public void setMiddleName(final String middleName) {
		this.middleName = middleName;
	}

	/**
	 *
	 * @return the sex
	 */
	public Sex getSex() {
		return this.sex;
	}

	/**
	 * @param sex the sex to set
	 */
	public void setSex(final Sex sex) {
		this.sex = sex;
	}

	/**
	 *
	 * @return the location
	 */
	public Location getLocation() {
		return this.location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(final Location location) {
		this.location = location;
	}

	/**
	 * @return the address
	 */
	public Address getAddress() {
		return this.address;
	}

	/**
	 * @param address address
	 */
	public void setAddress(final Address address) {
		this.address = address;
	}

	/**
	 *
	 * @return the birthDate
	 */
	public Date getBirthDate() {
		return this.birthDate;
	}

	/**
	 * @param birthDate the birthDate to set
	 */
	public void setBirthDate(final Date birthDate) {
		this.birthDate = birthDate;
	}

	/**
	 *
	 * @return the socialSecurityNumber
	 */
	public Integer getSocialSecurityNumber() {
		return this.socialSecurityNumber;
	}

	/**
	 * @param socialSecurityNumber the socialSecurityNumber to set
	 */
	public void setSocialSecurityNumber(final Integer socialSecurityNumber) {
		this.socialSecurityNumber = socialSecurityNumber;
	}

	/**
	 *
	 * @return the searchActiveOffenders
	 */
	public Boolean getSearchActiveOffenders() {
		return this.searchActiveOffenders;
	}

	/**
	 * @param searchActiveOffenders the searchActiveOffenders to set
	 */
	public void setSearchActiveOffenders(final Boolean searchActiveOffenders) {
		this.searchActiveOffenders = searchActiveOffenders;
	}

	/**
	 *
	 * @return the displayOffenderPhoto
	 */
	public Boolean getDisplayOffenderPhoto() {
		return this.displayOffenderPhoto;
	}

	/**
	 * @param displayOffenderPhoto the displayOffenderPhoto to set
	 */
	public void setDisplayOffenderPhoto(final Boolean displayOffenderPhoto) {
		this.displayOffenderPhoto = displayOffenderPhoto;
	}

	/**
	 * @return the sexes
	 */
	public List<Sex> getSexes() {
		return this.sexes;
	}

	/**
	 * @param sexes sexes
	 */
	public void setSexes(final List<Sex> sexes) {
		this.sexes = sexes;
	}

	/**
	 * @return the locations
	 */
	public List<Location> getLocations() {
		return this.locations;
	}

	/**
	 * @param locations locations
	 */
	public void setLocations(final List<Location> locations) {
		this.locations = locations;
	}	
}