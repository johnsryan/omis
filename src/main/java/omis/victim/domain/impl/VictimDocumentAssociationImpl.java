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

import omis.audit.domain.CreationSignature;
import omis.docket.domain.Docket;
import omis.document.domain.Document;
import omis.person.domain.Person;
import omis.victim.domain.VictimDocumentAssociation;

/**
 * Victim document association implementation.
 * 
 * @author Joel Norris
 * @author Josh Divine
 * @version 0.1.1 (Feb 12, 2018)
 * @since OMIS 3.0
 */
public class VictimDocumentAssociationImpl 
		implements VictimDocumentAssociation {

	private static final long serialVersionUID = 1L;
	private Long id;
	private Person victim;
	private Document document;
	private Docket docket;
	private CreationSignature creationSignature;
	
	/**
	 * Instantiates a default instance of victim document association.
	 */
	public VictimDocumentAssociationImpl() {
		//Default constructor.
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
	public Person getVictim() {
		return this.victim;
	}

	/** {@inheritDoc} */
	@Override
	public void setVictim(final Person victim) {
		this.victim = victim;
	}

	/** {@inheritDoc} */
	@Override
	public Document getDocument() {
		return this.document;
	}

	/** {@inheritDoc} */
	@Override
	public void setDocument(final Document document) {
		this.document = document;
	}

	/** {@inheritDoc} */
	@Override
	public Docket getDocket() {
		return this.docket;
	}

	/** {@inheritDoc} */
	@Override
	public void setDocket(final Docket docket) {
		this.docket = docket;
	}

	/** {@inheritDoc} */
	@Override
	public CreationSignature getCreationSignature() {
		return this.creationSignature;
	}

	/** {@inheritDoc} */
	@Override
	public void setCreationSignature(final CreationSignature creationSignature) {
		this.creationSignature = creationSignature;
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}	
		if (!(o instanceof VictimDocumentAssociation)) {
			return false;
		}
		
		VictimDocumentAssociation that = (VictimDocumentAssociation) o;
		
		if (this.getVictim() == null) {
			throw new IllegalStateException("Victim required.");
		}
		if (!this.getVictim().equals(that.getVictim())) {
			return false;
		}
		if (this.getDocument() == null) {
			throw new IllegalStateException("Document required");
		}
		if (!this.getDocument().equals(that.getDocument())) {
			return false;
		}
		return true;
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		if (this.getVictim() == null) {
			throw new IllegalStateException("Victim required.");
		}
		if (this.getDocument() == null) {
			throw new IllegalStateException("Document required");
		}
		int hashCode = 14;
		hashCode = 29 * hashCode + this.getVictim().hashCode();
		hashCode = 29 * hashCode + this.getDocument().hashCode();
		
		return hashCode;
	}
}
