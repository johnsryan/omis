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
import omis.chronologicalnote.domain.ChronologicalNoteCategoryGroupTemplate;

/**
 * Implementation of template for group of categories for chronological notes.
 * 
 * @author Stephen Abson
 * @version 0.0.1 (Mar 2, 2018)
 * @since OMIS 3.0
 */
public class ChronologicalNoteCategoryGroupTemplateImpl
		implements ChronologicalNoteCategoryGroupTemplate {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private ChronologicalNoteCategoryGroup group;
	
	private String text;
	
	/**
	 * Instantiates implementation of template for group of categories
	 * for chronological notes.
	 */
	public ChronologicalNoteCategoryGroupTemplateImpl() {
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
	public void setGroup(final ChronologicalNoteCategoryGroup group) {
		this.group = group;
	}

	/** {@inheritDoc} */
	@Override
	public ChronologicalNoteCategoryGroup getGroup() {
		return this.group;
	}

	/** {@inheritDoc} */
	@Override
	public void setText(final String text) {
		this.text = text;
	}

	/** {@inheritDoc} */
	@Override
	public String getText() {
		return this.text;
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ChronologicalNoteCategoryGroupTemplate)) {
			return false;
		}
		ChronologicalNoteCategoryGroupTemplate that
			= (ChronologicalNoteCategoryGroupTemplate) obj;
		if (this.getGroup() == null) {
			throw new IllegalStateException("Group required");
		}
		if (!this.getGroup().equals(that.getGroup())) {
			return false;
		}
		if (this.getText() == null) {
			throw new IllegalStateException("Text required");
		}
		if (!this.getText().equals(that.getText())) {
			return false;
		}
		return true;
	}
	
	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		if (this.getGroup() == null) {
			throw new IllegalStateException("Group required");
		}
		if (this.getText() == null) {
			throw new IllegalStateException("Text required");
		}
		int hashCode = (14 * 29) + this.getGroup().hashCode();
		hashCode = 29 * hashCode + this.getText().hashCode();
		return hashCode;
	}
	
	/**
	 * Returns string representation of {@code this} including group and text.
	 * 
	 * @return string representation of {@code this} including group and text
	 */
	@Override
	public String toString() {
		return String.format("#%d: [%s] \"%s\"",
				this.getId(), this.getGroup(), this.getText());
	}
}
