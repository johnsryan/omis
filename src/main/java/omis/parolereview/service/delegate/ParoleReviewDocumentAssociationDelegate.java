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
package omis.parolereview.service.delegate;

import java.util.List;

import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.document.domain.Document;
import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;
import omis.parolereview.dao.ParoleReviewDocumentAssociationDao;
import omis.parolereview.domain.ParoleReview;
import omis.parolereview.domain.ParoleReviewDocumentAssociation;

/**
 * Parole review document association delegate.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Jan 29, 2018)
 * @since OMIS 3.0
 */
public class ParoleReviewDocumentAssociationDelegate {

	/* Data access objects. */
	
	private final ParoleReviewDocumentAssociationDao 
			paroleReviewDocumentAssociationDao;

	/* Instance factories. */
	
	private final InstanceFactory<ParoleReviewDocumentAssociation> 
			paroleReviewDocumentAssociationInstanceFactory;
	
	/* Component Retrievers. */
	
	private final AuditComponentRetriever auditComponentRetriever;

	/** Instantiates an implementation of parole review document 
	 * association delegate with the specified date access object and instance 
	 * factory.
	 * 
	 * @param paroleReviewDocumentAssociationDao parole review 
	 * document association data access object
	 * @param paroleReviewDocumentAssociationInstanceFactory parole 
	 * review document association instance factory
	 * @param auditComponentRetriever audit component retriever
	 */
	public ParoleReviewDocumentAssociationDelegate(
			final ParoleReviewDocumentAssociationDao 
					paroleReviewDocumentAssociationDao,
			final InstanceFactory<ParoleReviewDocumentAssociation> 
					paroleReviewDocumentAssociationInstanceFactory,
			final AuditComponentRetriever auditComponentRetriever) {
		this.paroleReviewDocumentAssociationDao = 
				paroleReviewDocumentAssociationDao;
		this.paroleReviewDocumentAssociationInstanceFactory = 
				paroleReviewDocumentAssociationInstanceFactory;
		this.auditComponentRetriever = auditComponentRetriever;
	}

	/**
	 * Create a new parole review document association.
	 * 
	 * @param document document
	 * @param paroleReview parole review
	 * @return parole review document association
	 * @throws DuplicateEntityFoundException if entity already exists
	 */
	public ParoleReviewDocumentAssociation create(final Document document, 
			final ParoleReview paroleReview) 
					throws DuplicateEntityFoundException {
		if (this.paroleReviewDocumentAssociationDao.find(document, 
				paroleReview) != null) {
			throw new DuplicateEntityFoundException(
					"Parole review document association already exists.");
		}
		ParoleReviewDocumentAssociation documentAssociation = this
				.paroleReviewDocumentAssociationInstanceFactory
				.createInstance();
		documentAssociation.setDocument(document);
		documentAssociation.setParoleReview(paroleReview);
		documentAssociation.setCreationSignature(new CreationSignature(
				this.auditComponentRetriever.retrieveUserAccount(), 
				this.auditComponentRetriever.retrieveDate()));
		documentAssociation.setUpdateSignature(new UpdateSignature(
				this.auditComponentRetriever.retrieveUserAccount(), 
				this.auditComponentRetriever.retrieveDate()));
		return this.paroleReviewDocumentAssociationDao.makePersistent(
				documentAssociation);
	}
	
	/**
	 * Removes the specified parole review document association.
	 * 
	 * @param paroleReviewDocumentAssociation parole review document 
	 * association
	 */
	public void remove(
			final ParoleReviewDocumentAssociation 
					paroleReviewDocumentAssociation) {
		this.paroleReviewDocumentAssociationDao.makeTransient(
				paroleReviewDocumentAssociation);
	}

	/**
	 * Returns a list of parole review document associations for the 
	 * specified parole review.
	 * 
	 * @param paroleReview parole review
	 * @return list of parole review document associations
	 */
	public List<ParoleReviewDocumentAssociation> findByParoleReview(
			final ParoleReview paroleReview) {
		return this.paroleReviewDocumentAssociationDao
				.findByParoleReview(paroleReview);
	}
}
