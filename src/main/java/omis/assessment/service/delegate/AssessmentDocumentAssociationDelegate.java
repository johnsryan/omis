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
package omis.assessment.service.delegate;

import java.util.Date;
import java.util.List;
import omis.assessment.dao.AssessmentDocumentAssociationDao;
import omis.assessment.domain.AssessmentDocumentAssociation;
import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.document.domain.Document;
import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;
import omis.questionnaire.domain.AdministeredQuestionnaire;

/**
 * Assessment Document Association Delegate.
 * 
 *@author Annie Wahl 
 *@version 0.1.0 (Mar 8, 2018)
 *@since OMIS 3.0
 *
 */
public class AssessmentDocumentAssociationDelegate {
	
	private static final String DUPLICATE_ENTITY_FOUND_MSG =
			"Assessment Document Association already exists with the "
			+ "specified Document.";
	
	private final AssessmentDocumentAssociationDao
		assessmentDocumentAssociationDao;
	
	private final InstanceFactory<AssessmentDocumentAssociation> 
		assessmentDocumentAssociationInstanceFactory;
	
	private final AuditComponentRetriever auditComponentRetriever;

	/**
	 * Constructor for AssessmentDocumentAssociationDelegate.
	 * @param assessmentDocumentAssociationDao - Assessment Document
	 * Association DAO
	 * @param assessmentDocumentAssociationInstanceFactory - Assessment
	 * Document Association Instance Factory
	 * @param auditComponentRetriever - Audit Component Retriever
	 */
	public AssessmentDocumentAssociationDelegate(
			final AssessmentDocumentAssociationDao
				assessmentDocumentAssociationDao,
			final InstanceFactory<AssessmentDocumentAssociation> 
				assessmentDocumentAssociationInstanceFactory,
			final AuditComponentRetriever auditComponentRetriever) {
		this.assessmentDocumentAssociationDao =
				assessmentDocumentAssociationDao;
		this.assessmentDocumentAssociationInstanceFactory =
				assessmentDocumentAssociationInstanceFactory;
		this.auditComponentRetriever = auditComponentRetriever;
	}
	
	/**
	 * Creates aAssessment Document Association with the specified properties.
	 * 
	 * @param document - Document
	 * @param date - Date
	 * @param administeredQuestionnaire - Administered Questionnaire
	 * @return Newly created Assessment Document Association
	 * @throws DuplicateEntityFoundException - When a Assessment Document
	 * Association already exists with the specified properties.
	 */
	public AssessmentDocumentAssociation create(final Document document,
			final Date date,
			final AdministeredQuestionnaire administeredQuestionnaire)
				throws DuplicateEntityFoundException {
		if (this.assessmentDocumentAssociationDao.find(document) != null) {
			throw new DuplicateEntityFoundException(DUPLICATE_ENTITY_FOUND_MSG);
		}
		
		AssessmentDocumentAssociation assessmentDocumentAssociation = 
				this.assessmentDocumentAssociationInstanceFactory
				.createInstance();
		
		assessmentDocumentAssociation.setDocument(document);
		assessmentDocumentAssociation.setDate(date);
		assessmentDocumentAssociation.setAdministeredQuestionnaire(
				administeredQuestionnaire);
		assessmentDocumentAssociation.setCreationSignature(
				new CreationSignature(
						this.auditComponentRetriever.retrieveUserAccount(),
						this.auditComponentRetriever.retrieveDate()));
		assessmentDocumentAssociation.setUpdateSignature(
				new UpdateSignature(
						this.auditComponentRetriever.retrieveUserAccount(),
						this.auditComponentRetriever.retrieveDate()));
		
		return this.assessmentDocumentAssociationDao.makePersistent(
				assessmentDocumentAssociation);
	}
	
	/**
	 * Updates the specified Assessment Document Association with the
	 * specified properties.
	 * 
	 * @param assessmentDocumentAssociation - Assessment Document Association
	 * to update
	 * @param document - Document
	 * @param date - Date
	 * @param administeredQuestionnaire - Administered Questionnaire
	 * @return Updated Assessment Document Association
	 * @throws DuplicateEntityFoundException - When a Assessment Document
	 * Association already exists with the specified properties.
	 */
	public AssessmentDocumentAssociation update(
			final AssessmentDocumentAssociation assessmentDocumentAssociation,
			final Document document, final Date date,
			final AdministeredQuestionnaire administeredQuestionnaire)
				throws DuplicateEntityFoundException {
		if (this.assessmentDocumentAssociationDao.findExcluding(
				document, assessmentDocumentAssociation) != null) {
			throw new DuplicateEntityFoundException(DUPLICATE_ENTITY_FOUND_MSG);
		}

		assessmentDocumentAssociation.setDocument(document);
		assessmentDocumentAssociation.setDate(date);
		assessmentDocumentAssociation.setAdministeredQuestionnaire(
				administeredQuestionnaire);
		assessmentDocumentAssociation.setUpdateSignature(
				new UpdateSignature(
						this.auditComponentRetriever.retrieveUserAccount(),
						this.auditComponentRetriever.retrieveDate()));
		
		return this.assessmentDocumentAssociationDao.makePersistent(
				assessmentDocumentAssociation);
	}
	
	/**
	 * Removes the specified Assessment Document Association.
	 * 
	 * @param assessmentDocumentAssociation - Assessment Document Association
	 */
	public void remove(
			final AssessmentDocumentAssociation assessmentDocumentAssociation) {
		this.assessmentDocumentAssociationDao.makeTransient(
				assessmentDocumentAssociation);
	}
	
	/**
	 * Returns a list of Assessment Document Associations by the specified
	 * Administered Questionnaire.
	 * 
	 * @param administeredQuestionnaire - Administered Questionnaire
	 * @return List of Assessment Document Associations by the specified
	 * Administered Questionnaire.
	 */
	List<AssessmentDocumentAssociation> findByAdministeredQuestionnaire(
			final AdministeredQuestionnaire administeredQuestionnaire) {
		return this.assessmentDocumentAssociationDao
				.findByAdministeredQuestionnaire(administeredQuestionnaire);
	}
	
}
