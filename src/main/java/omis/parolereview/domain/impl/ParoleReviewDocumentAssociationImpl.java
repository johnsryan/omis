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
package omis.parolereview.domain.impl;

import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.document.domain.Document;
import omis.parolereview.domain.ParoleReview;
import omis.parolereview.domain.ParoleReviewDocumentAssociation;

/**
 * Implementation of parole review document association.
 *  
 * @author Josh Divine
 * @version 0.1.0 (Jan 29, 2018)
 * @since OMIS 3.0
 */
public class ParoleReviewDocumentAssociationImpl 
		implements ParoleReviewDocumentAssociation {

	private static final long serialVersionUID = 1L;

	private Long id;
	
	private Document document;
	
	private ParoleReview paroleReview;
	
	private CreationSignature creationSignature;
	
	private UpdateSignature updateSignature;
	
	/** 
	 * Instantiates an implementation of parole review document 
	 * association. 
	 */
	public ParoleReviewDocumentAssociationImpl() {
		// Default constructor.
	}
	
	/** {@inheritDoc} */
	@Override
	public void setId(final Long id) {
		this.id = id;
	}

	/** {@inheritDoc} */
	@Override
	public Long getId() {
		return id;
	}

	/** {@inheritDoc} */
	@Override
	public void setDocument(final Document document) {
		this.document = document;
	}

	/** {@inheritDoc} */
	@Override
	public Document getDocument() {
		return document;
	}

	/** {@inheritDoc} */
	@Override
	public void setParoleReview(
			final ParoleReview paroleReview) {
		this.paroleReview = paroleReview;
	}

	/** {@inheritDoc} */
	@Override
	public ParoleReview getParoleReview() {
		return paroleReview;
	}

	/** {@inheritDoc} */
	@Override
	public void setCreationSignature(
			final CreationSignature creationSignature) {
		this.creationSignature = creationSignature;
	}

	/** {@inheritDoc} */
	@Override
	public CreationSignature getCreationSignature() {
		return creationSignature;
	}

	/** {@inheritDoc} */
	@Override
	public void setUpdateSignature(final UpdateSignature updateSignature) {
		this.updateSignature = updateSignature;
	}

	/** {@inheritDoc} */
	@Override
	public UpdateSignature getUpdateSignature() {
		return updateSignature;
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ParoleReviewDocumentAssociation)) {
			return false;
		}
		ParoleReviewDocumentAssociation that = 
				(ParoleReviewDocumentAssociation) obj;
		if (this.getDocument() == null) {
			throw new IllegalStateException("Document required");
		}
		if (!this.getDocument().equals(that.getDocument())) {
			return false;
		}
		if (this.getParoleReview() == null) {
			throw new IllegalStateException("Parole review required");
		}
		if (!this.getParoleReview().equals(that.getParoleReview())) {
			return false;
		}
		return true;
	}
	
	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		if (this.getDocument() == null) {
			throw new IllegalStateException("Document required");
		}
		if (this.getParoleReview() == null) {
			throw new IllegalStateException("Parole review required");
		}
		int hashCode = 14;
		hashCode = 29 * hashCode + this.getDocument().hashCode();
		hashCode = 29 * hashCode + this.getParoleReview().hashCode();
		
		return hashCode;
	}
}