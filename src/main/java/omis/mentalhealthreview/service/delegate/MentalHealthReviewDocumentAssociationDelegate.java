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
package omis.mentalhealthreview.service.delegate;

import java.util.List;

import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.document.domain.Document;
import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;
import omis.mentalhealthreview.dao.MentalHealthReviewDocumentAssociationDao;
import omis.mentalhealthreview.domain.MentalHealthReview;
import omis.mentalhealthreview.domain.MentalHealthReviewDocumentAssociation;

/**
 * Mental health review document association delegate.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 1, 2018)
 * @since OMIS 3.0
 */
public class MentalHealthReviewDocumentAssociationDelegate {

	/* Data access objects. */
	
	private final MentalHealthReviewDocumentAssociationDao 
			mentalHealthReviewDocumentAssociationDao;

	/* Instance factories. */
	
	private final InstanceFactory<MentalHealthReviewDocumentAssociation> 
			mentalHealthReviewDocumentAssociationInstanceFactory;
	
	/* Component Retrievers. */
	
	private final AuditComponentRetriever auditComponentRetriever;

	/** Instantiates an implementation of mental health review document 
	 * association delegate with the specified date access object and instance 
	 * factory.
	 * 
	 * @param mentalHealthReviewDocumentAssociationDao mental health review 
	 * document association data access object
	 * @param mentalHealthReviewDocumentAssociationInstanceFactory mental health 
	 * review document association instance factory
	 * @param auditComponentRetriever audit component retriever
	 */
	public MentalHealthReviewDocumentAssociationDelegate(
			final MentalHealthReviewDocumentAssociationDao 
					mentalHealthReviewDocumentAssociationDao,
			final InstanceFactory<MentalHealthReviewDocumentAssociation> 
					mentalHealthReviewDocumentAssociationInstanceFactory,
			final AuditComponentRetriever auditComponentRetriever) {
		this.mentalHealthReviewDocumentAssociationDao = 
				mentalHealthReviewDocumentAssociationDao;
		this.mentalHealthReviewDocumentAssociationInstanceFactory = 
				mentalHealthReviewDocumentAssociationInstanceFactory;
		this.auditComponentRetriever = auditComponentRetriever;
	}

	/**
	 * Create a new mental health review document association.
	 * 
	 * @param document document
	 * @param mentalHealthReview mental health review
	 * @return mental health review document association
	 * @throws DuplicateEntityFoundException if entity already exists
	 */
	public MentalHealthReviewDocumentAssociation create(final Document document, 
			final MentalHealthReview mentalHealthReview) 
					throws DuplicateEntityFoundException {
		if (this.mentalHealthReviewDocumentAssociationDao.find(document, 
				mentalHealthReview) != null) {
			throw new DuplicateEntityFoundException(
					"Mental health review document association already exists.");
		}
		MentalHealthReviewDocumentAssociation documentAssociation = this
				.mentalHealthReviewDocumentAssociationInstanceFactory
				.createInstance();
		documentAssociation.setDocument(document);
		documentAssociation.setMentalHealthReview(mentalHealthReview);
		documentAssociation.setCreationSignature(new CreationSignature(
				this.auditComponentRetriever.retrieveUserAccount(), 
				this.auditComponentRetriever.retrieveDate()));
		documentAssociation.setUpdateSignature(new UpdateSignature(
				this.auditComponentRetriever.retrieveUserAccount(), 
				this.auditComponentRetriever.retrieveDate()));
		return this.mentalHealthReviewDocumentAssociationDao.makePersistent(
				documentAssociation);
	}
	
	/**
	 * Removes the specified mental health review document association.
	 * 
	 * @param mentalHealthReviewDocumentAssociation mental health review 
	 * document association
	 */
	public void remove(
			final MentalHealthReviewDocumentAssociation 
					mentalHealthReviewDocumentAssociation) {
		this.mentalHealthReviewDocumentAssociationDao.makeTransient(
				mentalHealthReviewDocumentAssociation);
	}

	/**
	 * Returns a list of mental health review document associations for the 
	 * specified mental health review.
	 * 
	 * @param mentalHealthReview mental health review
	 * @return list of mental health review document associations
	 */
	public List<MentalHealthReviewDocumentAssociation> findByMentalHealthReview(
			final MentalHealthReview mentalHealthReview) {
		return this.mentalHealthReviewDocumentAssociationDao
				.findByMentalHealthReview(mentalHealthReview);
	}
}
