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
package omis.mentalhealthreview.domain.impl;

import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.document.domain.Document;
import omis.mentalhealthreview.domain.MentalHealthReview;
import omis.mentalhealthreview.domain.MentalHealthReviewDocumentAssociation;

/**
 * Implementation of mental health review document association.
 *  
 * @author Josh Divine
 * @version 0.1.0 (Feb 1, 2018)
 * @since OMIS 3.0
 */
public class MentalHealthReviewDocumentAssociationImpl 
		implements MentalHealthReviewDocumentAssociation {

	private static final long serialVersionUID = 1L;

	private Long id;
	
	private Document document;
	
	private MentalHealthReview mentalHealthReview;
	
	private CreationSignature creationSignature;
	
	private UpdateSignature updateSignature;
	
	/** 
	 * Instantiates an implementation of mental health review document 
	 * association. 
	 */
	public MentalHealthReviewDocumentAssociationImpl() {
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
	public void setMentalHealthReview(
			final MentalHealthReview mentalHealthReview) {
		this.mentalHealthReview = mentalHealthReview;
	}

	/** {@inheritDoc} */
	@Override
	public MentalHealthReview getMentalHealthReview() {
		return mentalHealthReview;
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
		if (!(obj instanceof MentalHealthReviewDocumentAssociation)) {
			return false;
		}
		MentalHealthReviewDocumentAssociation that = 
				(MentalHealthReviewDocumentAssociation) obj;
		if (this.getDocument() == null) {
			throw new IllegalStateException("Document required");
		}
		if (!this.getDocument().equals(that.getDocument())) {
			return false;
		}
		if (this.getMentalHealthReview() == null) {
			throw new IllegalStateException("Mental health review required");
		}
		if (!this.getMentalHealthReview().equals(that.getMentalHealthReview())) {
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
		if (this.getMentalHealthReview() == null) {
			throw new IllegalStateException("Mental health review required");
		}
		int hashCode = 14;
		hashCode = 29 * hashCode + this.getDocument().hashCode();
		hashCode = 29 * hashCode + this.getMentalHealthReview().hashCode();
		
		return hashCode;
	}
}