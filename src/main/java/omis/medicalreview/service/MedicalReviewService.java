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
package omis.medicalreview.service;

import java.util.Date;
import java.util.List;
import omis.document.domain.Document;
import omis.document.domain.DocumentTag;
import omis.exception.DuplicateEntityFoundException;
import omis.medicalreview.domain.MedicalHealthClassification;
import omis.medicalreview.domain.MedicalReview;
import omis.medicalreview.domain.MedicalReviewDocumentAssociation;
import omis.medicalreview.domain.MedicalReviewNote;
import omis.offender.domain.Offender;

/**
 * Medical Review Service.
 * 
 *@author Annie Wahl 
 *@version 0.1.0 (Jan 31, 2018)
 *@since OMIS 3.0
 *
 */
public interface MedicalReviewService {
	
	/**
	 * Creates a Medical Review with the given properties.
	 * 
	 * @param offender - Offender
	 * @param date - Date
	 * @param text - String text
	 * @param healthClassification - Medical Health Classification
	 * @return Newly created Medical Review
	 * @throws DuplicateEntityFoundException - When a Medical Review already
	 * exists with the given Date and Health Classification for the specified
	 * Offender.
	 */
	MedicalReview createMedicalReview(Offender offender,
			Date date, String text,
			MedicalHealthClassification healthClassification)
				throws DuplicateEntityFoundException;
	
	/**
	 * Update the specified Medical Review with the given properties.
	 * 
	 * @param medicalReview - Medical Review to update
	 * @param offender - Offender
	 * @param date - Date
	 * @param text - String text
	 * @param healthClassification - Medical Health Classification
	 * @return Updated Medical Review
	 * @throws DuplicateEntityFoundException - When a Medical Review already
	 * exists with the given Date and Health Classification for the specified
	 * Offender.
	 */
	MedicalReview updateMedicalReview(MedicalReview medicalReview,
			Offender offender, Date date, String text,
			MedicalHealthClassification healthClassification)
				throws DuplicateEntityFoundException;
	
	/**
	 * Removes the specified Medical Review.
	 * 
	 * @param medicalReview - Medical Review to remove
	 */
	void removeMedicalReview(MedicalReview medicalReview);
	
	/**
	 * Creates a Medical Review Note with the given properties.
	 * 
	 * @param medicalReview - Medical Review
	 * @param description - String description
	 * @param date - Date
	 * @return newly created Medical Review Note
	 * @throws DuplicateEntityFoundException - When a Medical Review Note
	 * already exists with the given Date and Description for the
	 * specified Medical Review.
	 */
	MedicalReviewNote createMedicalReviewNote(MedicalReview medicalReview,
			String description, Date date)
				throws DuplicateEntityFoundException;
	
	/**
	 * Updates the specified Medical Review Note with the given properties.
	 * 
	 * @param medicalReviewNote - Medical Review Note to update
	 * @param medicalReview - Medical Review
	 * @param description - String description
	 * @param date - Date
	 * @return Updated Medical Review Note
	 * @throws DuplicateEntityFoundException - When a Medical Review Note
	 * already exists with the given Date and Description for the
	 * specified Medical Review.
	 */
	MedicalReviewNote updateMedicalReviewNote(
			MedicalReviewNote medicalReviewNote, MedicalReview medicalReview,
			String description, Date date)
				throws DuplicateEntityFoundException;
	
	/**
	 * Removes the specified Medical Review Note.
	 * 
	 * @param medicalReviewNote - Medical Review Note to remove
	 */
	void removeMedicalReviewNote(MedicalReviewNote medicalReviewNote);
	
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
	MedicalReviewDocumentAssociation createMedicalReviewDocumentAssociation(
			MedicalReview medicalReview, Document document)
				throws DuplicateEntityFoundException;
	
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
	MedicalReviewDocumentAssociation updateMedicalReviewDocumentAssociation(
			MedicalReviewDocumentAssociation
				medicalReviewDocumentAssociation,
			MedicalReview medicalReview, Document document)
				throws DuplicateEntityFoundException;
	
	/**
	 * Removes the specified Medical Review Document Association.
	 * 
	 * @param medicalReviewDocumentAssociation - Medical Review Document
	 * Association to remove
	 */
	void removeMedicalReviewDocumentAssociation(MedicalReviewDocumentAssociation
			medicalReviewDocumentAssociation);
	
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
	
	/**
	 * Returns a list of Medical Review Notes for the specified Medical Review.
	 * 
	 * @param medicalReview - Medical Review
	 * @return List of Medical Review Notes for the specified Medical Review.
	 */
	List<MedicalReviewNote> findMedicalReviewNotesByMedicalReview(
			MedicalReview medicalReview);
	
	/**
	 * Returns a list of Medical Review Document Associations by the specified
	 * Medical Review.
	 * 
	 * @param medicalReview - Medical Review
	 * @return List of Medical Review Document Associations by the specified
	 * Medical Review.
	 */
	List<MedicalReviewDocumentAssociation>
		findReviewDocumentAssociationsByMedicalReview(
			MedicalReview medicalReview);
	
	/**
	 * Returns a list of all valid Medical Health Classifications.
	 * 
	 * @return List of all valid Medical Health Classifications
	 */
	List<MedicalHealthClassification> findMedicalHealthClassifications();
}
