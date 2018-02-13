/*
 *  OMIS - Offender Management Information System
 *  Copyright (C) 2011 - 2017 State of Montana
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package omis.chronologicalnote.web.form;

import java.io.Serializable;

import omis.chronologicalnote.domain.ChronologicalNoteCategory;

/**
 * Chronological note category item.
 * 
 * @author Joel Norris
 * @version 0.1.0 (February 7, 2018)
 * @since OMIS 3.0
 */
public class ChronologicalNoteCategoryItem implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private ChronologicalNoteCategory category;
	private ChronologicalNoteCategoryItemOperation operation;
	private String name;
	private Boolean associated;
	
	/**
	 * Instantiates a default instance of chronological note category item.
	 */
	public ChronologicalNoteCategoryItem() {
		//Default constructor.
	}
	
	/**
	 * Instantiates a chronological note category item with the specified chronological note category,
	 * chronological note category item operation, name and whether associated applies.
	 * 
	 * @param category chronological note category
	 * @param operation chronological note category item operation
	 * @param name name
	 * @param associated associated
	 */
	public ChronologicalNoteCategoryItem(final ChronologicalNoteCategory category, 
			final ChronologicalNoteCategoryItemOperation operation,
			final String name, final Boolean associated) {
		this.category = category;
		this.operation = operation;
		this.name = name;
		this.associated = associated;
	}

	/**
	 * Returns chronological note category.
	 * 
	 * @return chronological note category
	 */
	public ChronologicalNoteCategory getCategory() {
		return this.category;
	}

	/**
	 * Sets chronological note category.
	 * 
	 * @param category chronological note category
	 */
	public void setCategory(final ChronologicalNoteCategory category) {
		this.category = category;
	}

	/**
	 * Returns chronological note category item operation.
	 * 
	 * @return chronological note category item operation
	 */
	public ChronologicalNoteCategoryItemOperation getOperation() {
		return this.operation;
	}

	/**
	 * Sets chronological note category item operation.
	 * 
	 * @param operation chronological note category item operation
	 */
	public void setOperation(final ChronologicalNoteCategoryItemOperation operation) {
		this.operation = operation;
	}

	/**
	 * Returns name.
	 * 
	 * @return name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets name.
	 * 
	 * @param name name
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * Returns whether associated applies.
	 * 
	 * @return associated
	 */
	public Boolean getAssociated() {
		return this.associated;
	}

	/**
	 * Sets whether associated applies.
	 * 
	 * @param associated associated
	 */
	public void setAssociated(final Boolean associated) {
		this.associated = associated;
	}
}