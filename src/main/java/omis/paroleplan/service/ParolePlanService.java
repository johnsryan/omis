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
package omis.paroleplan.service;

import java.util.Date;
import java.util.List;

import omis.document.domain.Document;
import omis.document.domain.DocumentTag;
import omis.exception.DuplicateEntityFoundException;
import omis.paroleeligibility.domain.ParoleEligibility;
import omis.paroleplan.domain.ParolePlan;
import omis.paroleplan.domain.ParolePlanDocumentAssociation;
import omis.paroleplan.domain.ParolePlanNote;
import omis.staff.domain.StaffAssignment;

/**
 * Service for parole plan.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 13, 2018)
 * @since OMIS 3.0
 */
public interface ParolePlanService {

	/**
	 * Creates a new parole plan.
	 * 
	 * @param paroleEligibility parole eligibility
	 * @param evaluator evaluator
	 * @param evaluationDescription evaluation description
	 * @param vocationalPlan vocational plan
	 * @param residencePlan residence plan
	 * @param treatmentPlan treatment plan
	 * @return parole plan
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	ParolePlan createParolePlan(ParoleEligibility paroleEligibility, 
			StaffAssignment evaluator, String evaluationDescription, 
			String vocationalPlan, String residencePlan, String treatmentPlan) 
					throws DuplicateEntityFoundException;
	
	/**
	 * Updates the specified parole plan.
	 * 
	 * @param parolePlan parole plan
	 * @param evaluator evaluator
	 * @param evaluationDescription evaluation description
	 * @param vocationalPlan vocational plan
	 * @param residencePlan residence plan
	 * @param treatmentPlan treatment plan
	 * @return parole plan
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	ParolePlan updateParolePlan(ParolePlan parolePlan, 
			StaffAssignment evaluator, String evaluationDescription, 
			String vocationalPlan, String residencePlan, String treatmentPlan) 
					throws DuplicateEntityFoundException;
	
	/**
	 * Removes the specified parole plan.
	 * 
	 * @param parolePlan parole plan
	 */
	void removeParolePlan(ParolePlan parolePlan);
	
	/**
	 * Creates a new document.
	 * 
	 * @param date date
	 * @param title title
	 * @param filename filename
	 * @param fileExtension file extension
	 * @return document
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	Document createDocument(Date date, String title, String filename, 
			String fileExtension) throws DuplicateEntityFoundException;
	
	/**
	 * Updates the specified document.
	 * 
	 * @param document document
	 * @param date date
	 * @param title title
	 * @return document
	 * @throws DuplicateEntityFoundException if duplicate entity exists
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
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	DocumentTag createDocumentTag(String name, Document document) 
			throws DuplicateEntityFoundException;
	
	/**
	 * Updates the specified document tag.
	 * 
	 * @param documentTag document tag
	 * @param name name
	 * @return document tag
	 * @throws DuplicateEntityFoundException if duplicate entity exists
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
	 * Creates a new parole plan document association.
	 * 
	 * @param parolePlan parole plan
	 * @param document document
	 * @return parole plan document association
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	ParolePlanDocumentAssociation createParolePlanDocumentAssociation(
			ParolePlan parolePlan, Document document) 
					throws DuplicateEntityFoundException;
	
	/**
	 * Removes the specified parole plan document association.
	 * 
	 * @param parolePlanDocumentAssociation parole plan document association
	 */
	void removeParolePlanDocumentAssociation(
			ParolePlanDocumentAssociation parolePlanDocumentAssociation);
	
	/**
	 * Creates a new parole plan note.
	 * 
	 * @param parolePlan parole plan
	 * @param description description
	 * @param date date
	 * @return parole plan note
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	ParolePlanNote createParolePlanNote(ParolePlan parolePlan, 
			String description, Date date) throws DuplicateEntityFoundException;
	
	/**
	 * Updates the specified parole plan note.
	 * 
	 * @param parolePlanNote parole plan note
	 * @param description description
	 * @param date date
	 * @return parole plan note
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	ParolePlanNote updateParolePlanNote(ParolePlanNote parolePlanNote, 
			String description, Date date) throws DuplicateEntityFoundException;
	
	/**
	 * Removes the specified parole plan note.
	 * 
	 * @param parolePlanNote parole plan note
	 */
	void removeParolePlanNote(ParolePlanNote parolePlanNote);
	
	/**
	 * Returns a list of parole plan document associations for the specified 
	 * parole plan.
	 * 
	 * @param parolePlan parole plan
	 * @return list of parole plan document associations
	 */
	List<ParolePlanDocumentAssociation> 
			findParolePlanDocumentAssociationsByParolePlan(
					ParolePlan parolePlan);
	
	/**
	 * Returns a list of document tags for the specified document.
	 * 
	 * @param document document
	 * @return list of document tags
	 */
	List<DocumentTag> findDocumentTagsByDocument(Document document);
	
	/**
	 * Returns a list of parole plan notes for the specified parole plan.
	 * 
	 * @param parolePlan parole plan
	 * @return list of parole plan notes
	 */
	List<ParolePlanNote> findParolePlanNotesByParolePlan(
			ParolePlan parolePlan);

	/**
	 * Returns the parole plan for the specified parole eligibility.
	 * 
	 * @param paroleEligibility parole eligibility
	 * @return parole plan
	 */
	ParolePlan findParolePlanByParoleEligibility(
			ParoleEligibility paroleEligibility);
}
