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

package omis.chronologicalnote.domain.impl;

import omis.chronologicalnote.domain.ChronologicalNoteCategory;
import omis.chronologicalnote.domain.ChronologicalNoteCategoryGroup;

/** Implementation of chronological note category.
 * @author Yidong Li
 * @author Stephen Abson
 * @version 0.1.0 (Jan 29, 2018)
 * @since OMIS 3.0 */
public class ChronologicalNoteCategoryImpl
	implements ChronologicalNoteCategory {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private ChronologicalNoteCategoryGroup group;
	private String shortName;
	private Boolean valid;
	
	/** Constructor. */
	public ChronologicalNoteCategoryImpl() {
		
	}
	
	/** {@inheritDoc} */
	@Override
	public Long getId() {
		return this.id;
	}

	/** {@inheritDoc} */
	@Override
	public void setId(final Long id) {
		this.id = id;
	}
	
	/** {@inheritDoc} */
	@Override
	public String getName() {
		return this.name;
	}

	/** {@inheritDoc} */
	@Override
	public void setName(final String name) {
		this.name = name;
	}
	
	/** {@inheritDoc} */
	@Override
	public ChronologicalNoteCategoryGroup getGroup() {
		return this.group;
	}
	
	/** {@inheritDoc} */
	@Override
	public void setGroup(final ChronologicalNoteCategoryGroup group) {
		this.group = group;
	}

	/** {@inheritDoc} */
	@Override
	public String getShortName() {
		return this.shortName;
	}
	
	/** {@inheritDoc} */
	@Override
	public void setShortName(final String shortName) {
		this.shortName = shortName;
	}
	
	/** {@inheritDoc} */
	@Override
	public Boolean getValid() {
		return this.valid;
	}
	
	/** {@inheritDoc} */
	@Override
	public void setValid(final Boolean valid) {
		this.valid = valid;
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ChronologicalNoteCategory)) {
			return false;
		}
		ChronologicalNoteCategory that = (ChronologicalNoteCategory) obj;
		if (this.getName() == null) {
			throw new IllegalStateException("Name required");
		}
		if (!this.getName().equals(that.getName())) {
			return false;
		}
		if (this.getGroup() == null) {
			throw new IllegalStateException("Group required");
		}
		if (!this.getGroup().equals(that.getGroup())) {
			return false;
		}
		return true;
	}
	
	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		final int number = 7;
		int hashCode = number;
		
		if (this.getName() == null) {
			throw new IllegalStateException("Name required");
		}
		if (this.getGroup() == null) {
			throw new IllegalStateException("Group required");
		}
		hashCode = 29 * hashCode + this.getName().hashCode();
		hashCode = 29 * hashCode + this.getGroup().hashCode();
		return hashCode;
	}
	
	/**
	 * Returns string representation of {@code this} including name and group.
	 * 
	 * @return string representation of {@code this} including name and group
	 */
	@Override
	public String toString() {
		return String.format("#%d: \"%s\"; [%s]",
				this.getId(), this.getName(), this.getGroup());
	}
}