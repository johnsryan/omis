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
package omis.mentalhealthreview.service;

import java.util.Date;
import java.util.List;

import omis.document.domain.Document;
import omis.document.domain.DocumentTag;
import omis.exception.DuplicateEntityFoundException;
import omis.mentalhealthreview.domain.MentalHealthNote;
import omis.mentalhealthreview.domain.MentalHealthReview;
import omis.mentalhealthreview.domain.MentalHealthReviewDocumentAssociation;
import omis.offender.domain.Offender;

/**
 * Service for mental health review.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 1, 2018)
 * @since OMIS 3.0
 */
public interface MentalHealthReviewService {

	/**
	 * Creates a new mental health review with the specified parameters.
	 * 
	 * @param date date
	 * @param text text
	 * @param offender offender
	 * @return mental health review
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	MentalHealthReview createMentalHealthReview(Date date, String text, 
			Offender offender) throws DuplicateEntityFoundException;
	
	/**
	 * Updates an existing mental health review with the specified parameters.
	 * 
	 * @param mentalHealthReview mental health review
	 * @param date date
	 * @param text text
	 * @return mental health review
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	MentalHealthReview updateMentalHealthReview(
			MentalHealthReview mentalHealthReview, Date date, String text) 
					throws DuplicateEntityFoundException;
	
	/**
	 * Removes the specified mental health review.
	 * 
	 * @param mentalHealthReview mental health review
	 */
	void removeMentalHealthReview(MentalHealthReview mentalHealthReview);
	
	/**
	 * Creates a new document.
	 * 
	 * @param date date
	 * @param title title
	 * @param filename filename
	 * @param fileExtension file extension
	 * @return document
	 * @throws DuplicateEntityFoundException if entity already exists
	 */
	Document createDocument(Date date, String title, String filename, 
			String fileExtension) throws DuplicateEntityFoundException;
	
	/**
	 * Updates an existing document.
	 * 
	 * @param document document
	 * @param date date
	 * @param title title
	 * @return document
	 * @throws DuplicateEntityFoundException if entity already exists
	 */
	Document updateDocument(Document document, Date date, String title) 
			throws DuplicateEntityFoundException;
	
	/**
	 * Removes the specified document.
	 * 
	 * @param document document
	 */
	void removeDocument(Document document);
	
	/**
	 * Creates a new document tag.
	 * 
	 * @param name name
	 * @param document document
	 * @return document tag
	 * @throws DuplicateEntityFoundException if entity already exists
	 */
	DocumentTag createDocumentTag(String name, Document document) 
			throws DuplicateEntityFoundException;
	
	/**
	 * Updates an existing document tag.
	 * 
	 * @param documentTag document tag
	 * @param name name
	 * @return document tag
	 * @throws DuplicateEntityFoundException if entity already exists
	 */
	DocumentTag updateDocumentTag(DocumentTag documentTag, String name) 
			throws DuplicateEntityFoundException;
	
	/**
	 * Removes the specified document tag.
	 * 
	 * @param documentTag document tag
	 */
	void removeDocumentTag(DocumentTag documentTag);
	
	/**
	 * Returns a list of document tags for the specified document.
	 * 
	 * @param document document
	 * @return list of document tags
	 */
	List<DocumentTag> findDocumentTagsByDocument(Document document);
	
	/**
	 * Create a new mental health review document association.
	 * 
	 * @param document document
	 * @param mentalHealthReview mental health review
	 * @return mental health review document association
	 * @throws DuplicateEntityFoundException if entity already exists
	 */
	MentalHealthReviewDocumentAssociation 
			createMentalHealthReviewDocumentAssociation(Document document, 
					MentalHealthReview mentalHealthReview) 
							throws DuplicateEntityFoundException;
	
	/**
	 * Removes the specified mental health review document association.
	 * 
	 * @param mentalHealthReviewDocumentAssociation mental health review 
	 * document association
	 */
	void removeMentalHealthReviewDocumentAssociation(
			MentalHealthReviewDocumentAssociation 
					mentalHealthReviewDocumentAssociation);
	
	/**
	 * Returns a list of mental health review document associations for the 
	 * specified mental health review.
	 * 
	 * @param mentalHealthReview mental health review
	 * @return list of mental health review document associations
	 */
	List<MentalHealthReviewDocumentAssociation> 
			findMentalHealthReviewDocumentAssociationsByMentalHealthReview(
					MentalHealthReview mentalHealthReview);
	
	/**
	 * Creates a new mental health note.
	 * 
	 * @param mentalHealthReview mental health review
	 * @param date date
	 * @param description description
	 * @return mental health note
	 * @throws DuplicateEntityFoundException if entity already exists
	 */
	MentalHealthNote createMentalHealthReviewNote(
			MentalHealthReview mentalHealthReview, String description, 
			Date date) throws DuplicateEntityFoundException;
	
	/**
	 * Updates the specified mental health note.
	 * 
	 * @param mentalHealthNote mental health note
	 * @param date date
	 * @param description description
	 * @return mental health note
	 * @throws DuplicateEntityFoundException if entity already exists
	 */
	MentalHealthNote updateMentalHealthReviewNote(
			MentalHealthNote mentalHealthNote, String description, Date date) 
					throws DuplicateEntityFoundException;
	
	/**
	 * Removes the specified mental health note.
	 * 
	 * @param mentalHealthNote mental health note
	 */
	void removeMentalHealthReviewNote(MentalHealthNote mentalHealthNote);
	
	/**
	 * Returns a list of mental health notes for the specified mental health 
	 * review.
	 * 
	 * @param mentalHealthReview mental health review
	 * @return list of mental health notes
	 */
	List<MentalHealthNote> findMentalHealthReviewNotesByMentalHealthReview(
			MentalHealthReview mentalHealthReview);
}
