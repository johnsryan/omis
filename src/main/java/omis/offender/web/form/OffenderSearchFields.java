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

/**
 * Fields to search for offenders.
 *
 * @author Stephen Abson
 * @version 0.0.1 (Aug 10, 2015)
 * @since OMIS 3.0
 */
public class OffenderSearchFields
		implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private OffenderSearchType type;
	
	private String lastName;
	
	private String firstName;
	
	private Integer offenderNumber;
	
	private String socialSecurityNumber;
	
	private Date birthDate;
	
	/** Instantiates default fields for offender search. */
	public OffenderSearchFields() {
		// Default instantiation
	}

	/**
	 * Returns type.
	 * 
	 * @return type
	 */
	public OffenderSearchType getType() {
		return this.type;
	}

	/**
	 * Sets type.
	 * 
	 * @param type type
	 */
	public void setType(final OffenderSearchType type) {
		this.type = type;
	}

	/**
	 * Returns last name.
	 * 
	 * @return last name
	 */
	public String getLastName() {
		return this.lastName;
	}

	/**
	 * Sets last name.
	 * 
	 * @param lastName last name
	 */
	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Returns first name.
	 * 
	 * @return first name
	 */
	public String getFirstName() {
		return this.firstName;
	}

	/**
	 * Sets first name.
	 * 
	 * @param firstName first name
	 */
	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Returns offender number.
	 * 
	 * @return offender number
	 */
	public Integer getOffenderNumber() {
		return this.offenderNumber;
	}

	/**
	 * Sets offender number.
	 * 
	 * @param offenderNumber offender number
	 */
	public void setOffenderNumber(final Integer offenderNumber) {
		this.offenderNumber = offenderNumber;
	}

	/**
	 * Returns social security number.
	 * 
	 * @return social security number
	 */
	public String getSocialSecurityNumber() {
		return this.socialSecurityNumber;
	}

	/**
	 * Sets social security number.
	 * 
	 * @param socialSecurityNumber social security number
	 */
	public void setSocialSecurityNumber(
			final String socialSecurityNumber) {
		this.socialSecurityNumber = socialSecurityNumber;
	}

	/**
	 * Returns birth date.
	 * 
	 * @return birth date
	 */
	public Date getBirthDate() {
		return this.birthDate;
	}

	/**
	 * Sets birth date.
	 * 
	 * @param birthDate birth date
	 */
	public void setBirthDate(final Date birthDate) {
		this.birthDate = birthDate;
	}
}