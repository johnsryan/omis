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

import omis.person.domain.AlternativeNameCategory;
import omis.person.domain.PersonName;

/**
 * Form for alternative offender names.
 * 
 * @author Stephen Abson
 * @version 0.1.0 (Dec 4, 2013)
 * @since OMIS 3.0
 */
public class AlternativeOffenderNameForm
		implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String lastName;
	
	private String firstName;
	
	private String middleName;
	
	private String suffix;
	
	private PersonName name;
	
	private AlternativeNameCategory category;
	
	private Date startDate;
	
	private Date endDate;
	
	/** Instantiates a form for alternative offender names. */
	public AlternativeOffenderNameForm() {
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
	 * Sets the middle name
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
	 * Returns the name.
	 * 
	 * @return name
	 */
	public PersonName getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name name
	 */
	public void setName(PersonName name) {
		this.name = name;
	}

	/**
	 * Returns the category.
	 * 
	 * @return category
	 */
	public AlternativeNameCategory getCategory() {
		return this.category;
	}

	/**
	 * Sets the category.
	 * 
	 * @param category category
	 */
	public void setCategory(final AlternativeNameCategory category) {
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
}