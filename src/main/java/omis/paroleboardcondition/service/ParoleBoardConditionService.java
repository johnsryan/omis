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
package omis.paroleboardcondition.service;

import java.util.Date;
import java.util.List;

import omis.boardhearing.domain.BoardHearing;
import omis.condition.domain.Agreement;
import omis.condition.domain.AgreementAssociableDocument;
import omis.condition.domain.AgreementCategory;
import omis.condition.domain.AgreementNote;
import omis.condition.domain.Condition;
import omis.condition.domain.ConditionCategory;
import omis.condition.domain.ConditionClause;
import omis.condition.domain.ConditionGroup;
import omis.condition.domain.ConditionTitle;
import omis.document.domain.Document;
import omis.document.domain.DocumentTag;
import omis.exception.DuplicateEntityFoundException;
import omis.hearinganalysis.domain.HearingAnalysis;
import omis.offender.domain.Offender;
import omis.paroleboardcondition.domain.ParoleBoardAgreement;
import omis.paroleboardcondition.domain.ParoleBoardAgreementCategory;

/**
 * Parole board condition service.
 * 
 * @author Annie Wahl 
 * @author Josh Divine
 * @version 0.1.1 (Feb 6, 2018)
 * @since OMIS 3.0
 *
 */
public interface ParoleBoardConditionService {
	
	/**
	 * Creates a Agreement.
	 * @param offender - offender.
	 * @param startDate - startDate.
	 * @param endDate - endDate.
	 * @param description - String description
	 * @param category - AgreementCategory
	 * @return newly created Agreement. 
	 * @throws DuplicateEntityFoundException - when Agreement already exists. */
	Agreement createAgreement(Offender offender, Date startDate,
			Date endDate, String description,
			AgreementCategory category)
					throws DuplicateEntityFoundException;
	
	/**
	 * Updates an existing Agreement.
	 * @param agreement - Agreement.
	 * @param startDate - startDate.
	 * @param endDate - endDate.
	 * @param description - String description
	 * @param category - AgreementCategory
	 * @return - updated Agreement
	 * @throws DuplicateEntityFoundException - when Agreement already exists. */
	Agreement updateAgreement(Agreement agreement, Date startDate,
			Date endDate, String description,
			AgreementCategory category)
					throws DuplicateEntityFoundException;
	
	/**
	 * Removes a Agreement.
	 * @param agreement - Agreement
	 */
	void removeAgreement(Agreement agreement);
	
	/**
	 * Creates a Parole Board Agreement with the specified properties.
	 * 
	 * @param agreement agreement
	 * @param boardHearing board hearing
	 * @param category parole board agreement category
	 * @return parole board agreement
	 * @throws DuplicateEntityFoundException When a parole board agreement
	 * already exists with the given agreement and category.
	 */
	ParoleBoardAgreement createParoleBoardAgreement(Agreement agreement,
			BoardHearing boardHearing, ParoleBoardAgreementCategory category)
					throws DuplicateEntityFoundException;
	
	/**
	 * Creates a Parole Board Agreement with the specified properties.
	 * 
	 * @param agreement agreement
	 * @param hearingAnalysis hearing analysis
	 * @param category parole board agreement category
	 * @return parole board agreement
	 * @throws DuplicateEntityFoundException When a parole board agreement
	 * already exists with the given agreement and category.
	 */
	ParoleBoardAgreement createParoleBoardAgreement(Agreement agreement,
			HearingAnalysis hearingAnalysis, 
			ParoleBoardAgreementCategory category)
					throws DuplicateEntityFoundException;
	
	/**
	 * Updates a Parole Board Agreement with the specified properties.
	 * @param paroleBoardAgreement - Parole Board Agreement to update
	 * @param category - Parole Board Agreement Category
	 * @return Updated Parole Board Agreement
	 * @throws DuplicateEntityFoundException - When a Parole Board Agreement
	 * already exists with the given Agreement and Category.
	 */
	ParoleBoardAgreement updateParoleBoardAgreement(
			ParoleBoardAgreement paroleBoardAgreement,
			ParoleBoardAgreementCategory category)
					throws DuplicateEntityFoundException;
	
	/**
	 * Removes the specified Parole Board Agreement.
	 * @param paroleBoardAgreement - Parole Board Agreement to remove;
	 */
	void removeParoleBoardAgreement(
			ParoleBoardAgreement paroleBoardAgreement);
	
	/** Creates an Agreement Note.
	 * @param date - Date
	 * @param agreement - agreement.
	 * @param description - Description.
	 * @return Agreement Note. 
	 * @throws DuplicateEntityFoundException - when Agreement Note
	 *  already exists. */
	AgreementNote createAgreementNote(Date date, Agreement agreement,
			String description)
				throws DuplicateEntityFoundException;
	
	/** Updates an existing Agreement Note.
	 * @param agreementNote - Agreement Note.
	 * @param description - description.
	 * @param date - Date
	 * @return Agreement Note. 
	 * @throws DuplicateEntityFoundException - when Agreement Note already
	 * exists.
	 */
	AgreementNote updateAgreementNote(AgreementNote agreementNote,
			String description, Date date)
				throws DuplicateEntityFoundException;
	
	/** Removes a Agreement Note.
	 * @param agreementNote - AgreementNote
	 */
	void removeAgreementNote(AgreementNote agreementNote);
	
	/**
	 * Creates a Condition with the specified properties.
	 * @param agreement - agreement.
	 * @param clause - clause.
	 * @param conditionClause - conditionClause.
	 * @param category - ConditionCategory
	 * @param waived - Boolean
	 * @return condition. 
	 * @throws DuplicateEntityFoundException - when condition already exists. */
	Condition createCondition(Agreement agreement, String clause,
			ConditionClause conditionClause,
			ConditionCategory category, Boolean waived)
					throws DuplicateEntityFoundException;
	
	/**
	 * Updates a Condition with the specified properties.
	 * @param condition - Condition to update
	 * @param conditionClause - ConditionClause
	 * @param clause - String
	 * @param category - ConditionCategory
	 * @param waived - Boolean
	 * @return Updated Condition
	 * @throws DuplicateEntityFoundException - when condition already exists
	 */
	Condition updateCondition(Condition condition,
			ConditionClause conditionClause,
			String clause,
			ConditionCategory category, Boolean waived)
					throws DuplicateEntityFoundException;
	
	/** Removes a condition.
	 * @param condition - Condition to remove.
	 */
	void removeCondition(Condition condition);
	
	/**
	 * Creates an Agreement Associable Document with the given properties.
	 * 
	 * @param agreement - Agreement
	 * @param document - Document
	 * @return Newly created Agreement
	 * @throws DuplicateEntityFoundException - When an Agreement Associable
	 * Document already exists with the given properties
	 */
	AgreementAssociableDocument createAgreementAssociableDocument(
			Agreement agreement, Document document)
					throws DuplicateEntityFoundException;
	
	/**
	 * Removes the specified Agreement Associable Document.
	 * 
	 * @param agreementAssociableDocument - AgreementAssociableDocument to
	 * remove
	 */
	void removeAgreementAssociableDocument(
			AgreementAssociableDocument agreementAssociableDocument);
	
	/** Create document.
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
	
	/** Updates document.
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
	 * @param document - Document to remove
	 */
	void removeDocument(Document document);

	/** Tag document.
	 * @param document - document.
	 * @param name - tag name. 
	 * @return document tag. 
	 * @throws DuplicateEntityFoundException - when document is already tagged
	 * with given name. */
	DocumentTag createDocumentTag(Document document, String name)
			throws DuplicateEntityFoundException;

	/** Update tag.
	 * @param documentTag - document tag.
	 * @param name - name. 
	 * @return DocumentTag - updated Document Tag.
	 * @throws DuplicateEntityFoundException - when document is already tagged
	 * with given name. */
	DocumentTag updateDocumentTag(DocumentTag documentTag, String name)
			throws DuplicateEntityFoundException;

	/** Remove tag.
	 * @param documentTag - document tag. */
	void removeDocumentTag(DocumentTag documentTag);
	

	/** Finds document tags by document.
	 * @param document - document.
	 * @return list of document tags. */
	List<DocumentTag> findDocumentTagsByDocument(Document document);
	
	/**
	 * Returns all ConditionClauses by specified ConditionGroup.
	 * @param conditionGroup - ConditionGroup
	 * @return List of ConditionClauses by specified ConditionGroup
	 */
	List<ConditionClause> findConditionClausesByConditionGroup(
			ConditionGroup conditionGroup);
	
	/**
	 * Returns a list of ConditionGroups that are not being used by the
	 * specified Agreement.
	 * @param agreement - Agreement
	 * @return List of ConditionGroups that are not being used by the specified
	 * Agreement
	 */
	List<ConditionGroup> findUnusedByAgreement(Agreement agreement);
	
	/**
	 * Returns a list of ConditionGroups that are being used by the specified
	 * Agreement.
	 * @param agreement - Agreement
	 * @return List of ConditionGroups that are being used by the specified
	 * Agreement
	 */
	List<ConditionGroup> findUsedByAgreement(Agreement agreement);
	
	/**
	 * Returns a Condition Clause found with the specified Condition Title.
	 * @param conditionTitle - Condition Title
	 * @return conditionClause - Condition Clause found with the specified
	 * Condition Title
	 */
	ConditionClause findConditionClauseByConditionTitle(
			ConditionTitle conditionTitle);
	
	/**
	 * Returns a list of Condition Clauses by the specified Parole Board
	 * Agreement Category.
	 * @param category - Parole Board Agreement Category
	 * @return List of Condition Clauses by the specified Parole Board
	 * Agreement Category.
	 */
	List<ConditionClause> findConditionClausesByCategory(
			ParoleBoardAgreementCategory category);
	
	/**
	 * Returns a list of all Parole Board Agreement Categories.
	 * @return List of all Parole Board Agreement Categories.
	 */
	List<ParoleBoardAgreementCategory> findAllParoleBoardAgreementCategories();
	
	/**
	 * Returns a List of Conditions found by specified Agreement.
	 * @param agreement - Agreement
	 * @return List of Conditions found by specified Agreement
	 */
	List<Condition> findConditionsByAgreement(Agreement agreement);
	
	/**
	 * Returns a List of AgreementNotes with the specified Agreement.
	 * @param agreement - Agreement
	 * @return List of AgreementNotes with the specified Agreement
	 */
	List<AgreementNote> findAgreementNotesByAgreement(Agreement agreement);
	
	/**
	 * Returns a list of Agreement Associable Documents by the specified 
	 * Agreement.
	 * 
	 * @param agreement - Agreement
	 * @return List of Agreement Associable Documents by the specified 
	 * Agreement
	 */
	List<AgreementAssociableDocument>
		findAgreementAssociableDocumentsByAgreement(Agreement agreement);
}
