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
package omis.medicalreview.service.delegate;

import java.util.List;
import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.document.domain.Document;
import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;
import omis.medicalreview.dao.MedicalReviewDocumentAssociationDao;
import omis.medicalreview.domain.MedicalReview;
import omis.medicalreview.domain.MedicalReviewDocumentAssociation;

/**
 * Medical Review Document Association Delegate.
 * 
 *@author Annie Wahl 
 *@version 0.1.0 (Jan 31, 2018)
 *@since OMIS 3.0
 *
 */
public class MedicalReviewDocumentAssociationDelegate {
	
	private static final String DUPLICATE_ENTITY_FOUND_MSG =
			"A Medical Review Document Association already exists with the "
			+ "specified Medical Review and Document.";
	
	private final MedicalReviewDocumentAssociationDao
		medicalReviewDocumentAssociationDao;
	
	private final InstanceFactory<MedicalReviewDocumentAssociation> 
		medicalReviewDocumentAssociationInstanceFactory;
	
	private final AuditComponentRetriever auditComponentRetriever;

	/**
	 * Constructor for MedicalReviewDocumentAssociationDelegate.
	 * @param medicalReviewDocumentAssociationDao - Medical Review Document
	 * Association DAO
	 * @param medicalReviewDocumentAssociationInstanceFactory - Medical Review
	 * Document Association Instance Factory
	 * @param auditComponentRetriever - Audit Component Retriever
	 */
	public MedicalReviewDocumentAssociationDelegate(
			final MedicalReviewDocumentAssociationDao
				medicalReviewDocumentAssociationDao,
			final InstanceFactory<MedicalReviewDocumentAssociation> 
				medicalReviewDocumentAssociationInstanceFactory,
			final AuditComponentRetriever auditComponentRetriever) {
		this.medicalReviewDocumentAssociationDao =
				medicalReviewDocumentAssociationDao;
		this.medicalReviewDocumentAssociationInstanceFactory =
				medicalReviewDocumentAssociationInstanceFactory;
		this.auditComponentRetriever = auditComponentRetriever;
	}
	
	/**
	 * Creates a Medical Review Document Association with the
	 * specified properties.
	 * 
	 * @param medicalReview - Medical Review
	 * @param document - Document
	 * @return Newly created Medical Review Document Association
	 * @throws DuplicateEntityFoundException - When a Medical Review Document
	 * Association already exists with the specified Medical Review
	 * and Document.
	 */
	public MedicalReviewDocumentAssociation create(
			final MedicalReview medicalReview, final Document document)
				throws DuplicateEntityFoundException {
		if (this.medicalReviewDocumentAssociationDao.find(
				medicalReview, document) != null) {
			throw new DuplicateEntityFoundException(DUPLICATE_ENTITY_FOUND_MSG);
		}
		
		MedicalReviewDocumentAssociation medicalReviewDocumentAssociation = 
				this.medicalReviewDocumentAssociationInstanceFactory
					.createInstance();
		
		medicalReviewDocumentAssociation.setMedicalReview(medicalReview);
		medicalReviewDocumentAssociation.setDocument(document);
		medicalReviewDocumentAssociation.setCreationSignature(
				new CreationSignature(
						this.auditComponentRetriever.retrieveUserAccount(), 
						this.auditComponentRetriever.retrieveDate()));
		medicalReviewDocumentAssociation.setUpdateSignature(
				new UpdateSignature(
						this.auditComponentRetriever.retrieveUserAccount(),
						this.auditComponentRetriever.retrieveDate()));
		
		return this.medicalReviewDocumentAssociationDao.makePersistent(
				medicalReviewDocumentAssociation);
	}
	
	/**
	 * Updates the specified Medical Review Document Association with the
	 * specified properties.
	 * 
	 * @param medicalReviewDocumentAssociation - Medical Review Document
	 * Association to update
	 * @param medicalReview - Medical Review
	 * @param document - Document
	 * @return Updated Medical Review Document Association
	 * @throws DuplicateEntityFoundException - When a Medical Review Document
	 * Association already exists with the specified Medical Review
	 * and Document.
	 */
	public MedicalReviewDocumentAssociation update(
			final MedicalReviewDocumentAssociation
				medicalReviewDocumentAssociation,
			final MedicalReview medicalReview, final Document document)
				throws DuplicateEntityFoundException {
		if (this.medicalReviewDocumentAssociationDao.findExcluding(
				medicalReview, document,
				medicalReviewDocumentAssociation) != null) {
			throw new DuplicateEntityFoundException(DUPLICATE_ENTITY_FOUND_MSG);
		}

		medicalReviewDocumentAssociation.setMedicalReview(medicalReview);
		medicalReviewDocumentAssociation.setDocument(document);
		medicalReviewDocumentAssociation.setUpdateSignature(
				new UpdateSignature(
						this.auditComponentRetriever.retrieveUserAccount(),
						this.auditComponentRetriever.retrieveDate()));
		
		return this.medicalReviewDocumentAssociationDao.makePersistent(
				medicalReviewDocumentAssociation);
	}
	
	/**
	 * Removes the specified Medical Review Document Association.
	 * 
	 * @param medicalReviewDocumentAssociation - Medical Review Document
	 * Association to remove
	 */
	public void remove(final MedicalReviewDocumentAssociation
			medicalReviewDocumentAssociation) {
		this.medicalReviewDocumentAssociationDao.makeTransient(
				medicalReviewDocumentAssociation);
	}
	
	/**
	 * Returns a list of Medical Review Document Associations by the specified
	 * Medical Review.
	 * 
	 * @param medicalReview - Medical Review
	 * @return List of Medical Review Document Associations by the specified
	 * Medical Review.
	 */
	public List<MedicalReviewDocumentAssociation> findByMedicalReview(
			final MedicalReview medicalReview) {
		return this.medicalReviewDocumentAssociationDao
				.findByMedicalReview(medicalReview);
	}
}
