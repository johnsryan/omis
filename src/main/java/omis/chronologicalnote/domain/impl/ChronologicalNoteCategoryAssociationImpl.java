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

import omis.audit.domain.CreationSignature;
import omis.chronologicalnote.domain.ChronologicalNote;
import omis.chronologicalnote.domain.ChronologicalNoteCategory;
import omis.chronologicalnote.domain.ChronologicalNoteCategoryAssociation;

/** Implementation of chronological note category association.
 * @author Yidong Li
 * @version 0.1.0 (Jan 29, 2018)
 * @since OMIS 3.0 */
public class ChronologicalNoteCategoryAssociationImpl
	implements ChronologicalNoteCategoryAssociation {
	private static final long serialVersionUID = 1L;
	private Long id;
	private ChronologicalNote note;
	private ChronologicalNoteCategory category;
	private CreationSignature creationSignature;
	
	/** Constructor. */
	public ChronologicalNoteCategoryAssociationImpl() {
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
	public ChronologicalNote getNote() {
		return this.note;
	}

	/** {@inheritDoc} */
	@Override
	public void setNote(final ChronologicalNote note) {
		this.note = note;
	}

	/** {@inheritDoc} */
	@Override
	public ChronologicalNoteCategory getCategory() {
		return this.category;
	}
	
	/** {@inheritDoc} */
	@Override
	public void setCategory(final ChronologicalNoteCategory category) {
		this.category = category;
	}

	/** {@inheritDoc} */
	@Override
	public void setCreationSignature(
			final CreationSignature creationSignature) {
		this.creationSignature = creationSignature;
	}
	
	@Override
	public CreationSignature getCreationSignature() {
		return this.creationSignature;
	}
	
	
	/** {@inheritDoc} */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ChronologicalNoteCategoryAssociation)) {
			return false;
		}
		ChronologicalNoteCategoryAssociation that
			= (ChronologicalNoteCategoryAssociation) obj;
		if (this.getNote() == null) {
			throw new IllegalStateException("Note required");
		}
		if (!this.getNote().equals(that.getNote())) {
			return false;
		}
		if (this.getCategory() == null) {
			throw new IllegalStateException("Category required");
		}
		if (!this.getCategory().equals(that.getCategory())) {
			return false;
		}
		return true;
	}
	
	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		final int number = 7;
		int hashCode = number;
		
		if (this.getNote() == null) {
			throw new IllegalStateException("Note required");
		}
		if (this.getCategory() == null) {
			throw new IllegalStateException("Category required");
		}
		hashCode += 29 * this.getCategory().hashCode();
		return hashCode;
	}
}