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
package omis.victim.domain.impl;

import java.util.Date;

import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.person.domain.Person;
import omis.victim.domain.VictimAssociation;
import omis.victim.domain.VictimNote;
import omis.victim.domain.VictimNoteCategory;

/**
 * Implementation of victim note.
 *
 * @author Stephen Abson
 * @author Sheronda Vaughn
 * @version 0.0.1 (Jul 23, 2015)
 * @since OMIS 3.0
 */
public class VictimNoteImpl
		implements VictimNote {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private CreationSignature creationSignature;
	
	private UpdateSignature updateSignature;
	
	private Person victim;
	
	private VictimNoteCategory category;
	
	private VictimAssociation association;
	
	private Date date;
	
	private String value;
	
	/**
	 * Instantiates default implementation of victim note.
	 */
	public VictimNoteImpl() {
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
	public void setCreationSignature(final CreationSignature creationSignature) {
		this.creationSignature = creationSignature;
	}

	/** {@inheritDoc} */
	@Override
	public CreationSignature getCreationSignature() {
		return this.creationSignature;
	}

	/** {@inheritDoc} */
	@Override
	public void setUpdateSignature(final UpdateSignature updateSignature) {
		this.updateSignature = updateSignature;
	}

	/** {@inheritDoc} */
	@Override
	public UpdateSignature getUpdateSignature() {
		return this.updateSignature;
	}

	/** {@inheritDoc} */
	@Override
	public void setCategory(final VictimNoteCategory category) {
		this.category = category;
	}

	/** {@inheritDoc} */
	@Override
	public VictimNoteCategory getCategory() {
		return this.category;
	}

	/** {@inheritDoc} */
	@Override
	public void setVictim(final Person victim) {
		this.victim = victim;
	}

	/** {@inheritDoc} */
	@Override
	public Person getVictim() {
		return this.victim;
	}

	/** {@inheritDoc} */
	@Override
	public void setAssociation(final VictimAssociation association) {
		this.association = association;
	}

	/** {@inheritDoc} */
	@Override
	public VictimAssociation getAssociation() {
		return this.association;
	}

	/** {@inheritDoc} */
	@Override
	public void setDate(final Date date) {
		this.date = date;
	}

	/** {@inheritDoc} */
	@Override
	public Date getDate() {
		return this.date;
	}

	/** {@inheritDoc} */
	@Override
	public void setValue(final String value) {
		this.value = value;
	}

	/** {@inheritDoc} */
	@Override
	public String getValue() {
		return this.value;
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof VictimNote)) {
			return false;
		}
		VictimNote that = (VictimNote) obj;
		if (this.getVictim() == null) {
			throw new IllegalStateException("Victim required");
		}
		if (!this.getVictim().equals(that.getVictim())) {
			return false;
		}
		if (this.getCategory() == null) {
			throw new IllegalStateException("Category required");
		}
		if (!this.getCategory().equals(that.getCategory())) {
			return false;
		}
		if (this.getDate() == null) {
			throw new IllegalStateException("Date required");
		}
		if (!this.getDate().equals(that.getDate())) {
			return false;
		}		
		if (this.getValue() == null) {
			throw new IllegalStateException("Value required");			
		} 
		if (!this.getValue().equals(that.getValue())) {
			return false;
		}
		return true;
	}
	
	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		if (this.getVictim() == null) {
			throw new IllegalStateException("Victim required");
		}
		if (this.getCategory() == null) {
			throw new IllegalStateException("Category required");
		}
		if (this.getDate() == null) {
			throw new IllegalStateException("Date required");
		}
		if (this.getValue() == null) {
			throw new IllegalStateException("Value required");			
		} 
		int hashCode = 14;
		hashCode = 29 * hashCode + this.getVictim().hashCode();
		hashCode = 29 * hashCode + this.getCategory().hashCode();
		hashCode = 29 * hashCode + this.getDate().hashCode();
		hashCode = 29 * hashCode + this.getValue().hashCode();
	
		return hashCode;
	}
}