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
package omis.chronologicalnote.domain.impl;

import omis.chronologicalnote.domain.ChronologicalNoteCategoryGroup;

/**
 * Implementation of group of categories for chronological notes.
 * 
 * @author Stephen Abson
 * @version 0.0.1 (Mar 2, 2018)
 * @since OMIS 3.0
 */
public class ChronologicalNoteCategoryGroupImpl
		implements ChronologicalNoteCategoryGroup {
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private String name;
	
	private Short sortOrder;

	private Boolean valid;
	
	/**
	 * Instantiates implementation of group of category of chronological note.
	 */
	public ChronologicalNoteCategoryGroupImpl() {
		// Default instantiation
	}
	
	/** {@inheritDoc} */
	@Override
	public void setId(final Long id) {
		this.id = id;
	}

	/** {@inheritDoc} */
	@Override
	public Long getId() {
		return this.id;
	}

	/** {@inheritDoc} */
	@Override
	public void setName(final String name) {
		this.name = name;
	}

	/** {@inheritDoc} */
	@Override
	public String getName() {
		return this.name;
	}

	/** {@inheritDoc} */
	@Override
	public void setSortOrder(final Short sortOrder) {
		this.sortOrder = sortOrder;
	}

	/** {@inheritDoc} */
	@Override
	public Short getSortOrder() {
		return this.sortOrder;
	}
	
	/** {@inheritDoc} */
	@Override
	public void setValid(final Boolean valid) {
		this.valid = valid;
	}
	
	/** {@inheritDoc} */
	@Override
	public Boolean getValid() {
		return this.valid;
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ChronologicalNoteCategoryGroup)) {
			return false;
		}
		ChronologicalNoteCategoryGroup that
			= (ChronologicalNoteCategoryGroup) obj;
		if (this.getName() == null) {
			throw new IllegalStateException("Name required");
		}
		if (!this.getName().equals(that.getName())) {
			return false;
		}
		return true;
	}
	
	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		if (this.getName() == null) {
			throw new IllegalStateException("Name required");
		}
		return (14 * 29) + this.getName().hashCode();
	}
	
	/**
	 * Returns a string representation of {@code this} including {@code name}.
	 * 
	 * @return string representation of {@code this} including {@code name}
	 */
	@Override
	public String toString() {
		return String.format("#%d: %s", this.getId(), this.getName());
	}
}