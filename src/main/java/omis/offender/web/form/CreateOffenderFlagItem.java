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

import omis.offenderflag.domain.OffenderFlagCategory;

/**
 * Offender flag create form item.
 * 
 * @author Stephen Abson
 * @version 0.1.0 (Feb 10, 2014)
 * @since OMIS 3.0
 */
public class CreateOffenderFlagItem
		implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private OffenderFlagCategory category;
	
	private CreateOffenderFlagItemValue value;
	
	/** Instantiates an offender flag create form item. */
	public CreateOffenderFlagItem() {
		// Default instantiation
	}

	/**
	 * Returns the category.
	 * 
	 * @return category
	 */
	public OffenderFlagCategory getCategory() {
		return this.category;
	}

	/**
	 * Sets the category.
	 * 
	 * @param category category
	 */
	public void setCategory(final OffenderFlagCategory category) {
		this.category = category;
	}

	/**
	 * Returns the value.
	 * 
	 * @return value
	 */
	public CreateOffenderFlagItemValue getValue() {
		return this.value;
	}

	/**
	 * Sets the value.
	 * 
	 * @param value value
	 */
	public void setValue(final CreateOffenderFlagItemValue value) {
		this.value = value;
	}
}