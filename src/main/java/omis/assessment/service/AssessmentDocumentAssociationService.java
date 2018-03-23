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
package omis.assessment.service;

import java.util.Date;
import java.util.List;
import omis.assessment.domain.AssessmentDocumentAssociation;
import omis.document.domain.Document;
import omis.document.domain.DocumentTag;
import omis.exception.DuplicateEntityFoundException;
import omis.questionnaire.domain.AdministeredQuestionnaire;

/**
 * Assessment Document Association Service.
 * 
 *@author Annie Wahl 
 *@version 0.1.0 (Mar 8, 2018)
 *@since OMIS 3.0
 *
 */
public interface AssessmentDocumentAssociationService {
	
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
	AssessmentDocumentAssociation createAssessmentDocumentAssociation(
			Document document, Date date,
			AdministeredQuestionnaire administeredQuestionnaire)
				throws DuplicateEntityFoundException;
	
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
	AssessmentDocumentAssociation updateAssessmentDocumentAssociation(
			AssessmentDocumentAssociation assessmentDocumentAssociation,
			Document document, Date date,
			AdministeredQuestionnaire administeredQuestionnaire)
				throws DuplicateEntityFoundException;
	
	/**
	 * Removes the specified Assessment Document Association.
	 * 
	 * @param assessmentDocumentAssociation - Assessment Document Association
	 */
	void removeAssessmentDocumentAssociation(
			AssessmentDocumentAssociation assessmentDocumentAssociation);
	
	/**
	 * Create document.
	 * 
	 * @param documentDate - date of document.
	 * @param filename - file name.
	 * @param fileExtension - file extension. 
	 * @param title - title. 
	 * @return document entity.
	 * @throws DuplicateEntityFoundException - when document with existing file
	 * name exists. */
	Document createDocument(Date documentDate,
			String filename, String fileExtension, String title)
					throws DuplicateEntityFoundException;
	
	/**
	 * Updates document.
	 * 
	 * @param document - Document
	 * @param documentDate - Date
	 * @param title - String Title
	 * @return Document - updated document
	 * @throws DuplicateEntityFoundException */
	Document updateDocument(Document document, Date documentDate,
			String title)
					throws DuplicateEntityFoundException;

	/**
	 * Removes a specified document.
	 * 
	 * @param document - Document to remove
	 */
	void removeDocument(Document document);

	/**
	 * Tag document.
	 * 
	 * @param document - document.
	 * @param name - tag name. 
	 * @return document tag. 
	 * @throws DuplicateEntityFoundException - when document is already tagged
	 * with given name. */
	DocumentTag createDocumentTag(Document document, String name)
			throws DuplicateEntityFoundException;

	/**
	 * Update tag.
	 * 
	 * @param documentTag - document tag.
	 * @param name - name. 
	 * @return DocumentTag - updated Document Tag.
	 * @throws DuplicateEntityFoundException - when document is already tagged
	 * with given name. */
	DocumentTag updateDocumentTag(DocumentTag documentTag, String name)
			throws DuplicateEntityFoundException;

	/**
	 * Remove tag.
	 * 
	 * @param documentTag - document tag. */
	void removeDocumentTag(DocumentTag documentTag);
	

	/**
	 * Finds document tags by document.
	 * 
	 * @param document - document.
	 * @return list of document tags. */
	List<DocumentTag> findDocumentTagsByDocument(Document document);
}
