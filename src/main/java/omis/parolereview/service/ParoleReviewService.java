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
package omis.parolereview.service;

import java.util.Date;
import java.util.List;

import omis.document.domain.Document;
import omis.document.domain.DocumentTag;
import omis.exception.DuplicateEntityFoundException;
import omis.offender.domain.Offender;
import omis.parolereview.domain.ParoleEndorsementCategory;
import omis.parolereview.domain.ParoleReview;
import omis.parolereview.domain.ParoleReviewDocumentAssociation;
import omis.parolereview.domain.ParoleReviewNote;
import omis.parolereview.domain.StaffRoleCategory;
import omis.staff.domain.StaffAssignment;

/**
 * Service for parole review.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Jan 29, 2018)
 * @since OMIS 3.0
 */
public interface ParoleReviewService {

	/**
	 * Creates a new parole review with the specified parameters.
	 * 
	 * @param staffAssignment staff assignment
	 * @param date date
	 * @param text text
	 * @param offender offender
	 * @param endorsement parole endorsement category
	 * @param staffRole staff role category
	 * @return parole review
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	ParoleReview createParoleReview(StaffAssignment staffAssignment, 
			Date date, String text, Offender offender, 
			ParoleEndorsementCategory endorsement, StaffRoleCategory staffRole) 
					throws DuplicateEntityFoundException;
	
	/**
	 * Updates an existing parole review with the specified parameters.
	 * 
	 * @param paroleReview parole review
	 * @param staffAssignment staff assignment
	 * @param date date
	 * @param text text
	 * @param endorsement parole endorsement category
	 * @param staffRole staff role category
	 * @return parole review
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	ParoleReview updateParoleReview(
			ParoleReview paroleReview, 
			StaffAssignment staffAssignment, Date date, String text, 
			ParoleEndorsementCategory endorsement, StaffRoleCategory staffRole) 
					throws DuplicateEntityFoundException;
	
	/**
	 * Removes the specified parole review.
	 * 
	 * @param paroleReview parole review
	 */
	void removeParoleReview(ParoleReview paroleReview);
	
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
	 * Create a new parole review document association.
	 * 
	 * @param document document
	 * @param paroleReview parole review
	 * @return parole review document association
	 * @throws DuplicateEntityFoundException if entity already exists
	 */
	ParoleReviewDocumentAssociation createParoleReviewDocumentAssociation(
			Document document, ParoleReview paroleReview) 
					throws DuplicateEntityFoundException;
	
	/**
	 * Removes the specified parole review document association.
	 * 
	 * @param paroleReviewDocumentAssociation parole review document association
	 */
	void removeParoleReviewDocumentAssociation(
			ParoleReviewDocumentAssociation 
					paroleReviewDocumentAssociation);
	
	/**
	 * Returns a list of parole review document associations for the specified 
	 * parole review.
	 * 
	 * @param paroleReview parole review
	 * @return list of parole review document associations
	 */
	List<ParoleReviewDocumentAssociation> 
			findParoleReviewDocumentAssociationsByParoleReview(
					ParoleReview paroleReview);
	
	/**
	 * Creates a new parole review note.
	 * 
	 * @param paroleReview parole review
	 * @param date date
	 * @param description description
	 * @return parole review note
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	ParoleReviewNote createParoleReviewNote(ParoleReview paroleReview, 
			String description, Date date) throws DuplicateEntityFoundException;
	
	/**
	 * Updates an existing parole review note.
	 * 
	 * @param paroleReviewNote parole review note
	 * @param paroleReview parole review
	 * @param date date
	 * @param description description
	 * @return parole review note
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	ParoleReviewNote updateParoleReviewNote(ParoleReviewNote paroleReviewNote, 
			ParoleReview paroleReview, String description, Date date) 
					throws DuplicateEntityFoundException;
	
	/**
	 * Removes the specified parole review note.
	 * 
	 * @param paroleReviewNote parole review note
	 */
	void removeParoleReviewNote(ParoleReviewNote paroleReviewNote);
	
	/**
	 * Returns a list of parole endorsement categories.
	 * 
	 * @return list of parole endorsement categories
	 */
	List<ParoleEndorsementCategory> findParoleEndorsementCategories();
	
	/**
	 * Returns a list of staff role categories.
	 * 
	 * @return list of staff role categories
	 */
	List<StaffRoleCategory> findStaffRoleCategories();
	
	/**
	 * Returns a list of parole review notes for the specified parole review.
	 * 
	 * @param paroleReview parole review
	 * @return parole review note
	 */
	List<ParoleReviewNote> findParoleReviewNotesByParoleReview(
			ParoleReview paroleReview);
}
