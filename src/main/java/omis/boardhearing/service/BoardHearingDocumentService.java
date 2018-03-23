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
package omis.boardhearing.service;

import java.util.Date;
import java.util.List;

import omis.boardhearing.domain.BoardHearing;
import omis.boardhearing.domain.BoardHearingAssociableDocument;
import omis.document.domain.Document;
import omis.document.domain.DocumentTag;
import omis.exception.DuplicateEntityFoundException;

/**
 * Board hearing document service.
 *
 * @author Trevor Isles
 * @version 0.1.0 (Feb 7, 2018)
 * @since OMIS 3.0
 */
public interface BoardHearingDocumentService {

	/**
	 * Creates a board hearing document with the specified parameters.
	 * 
	 * @param date - Date
	 * @param title - String
	 * @param filename - String
	 * @param fileExtension - String
	 * @return Newly created board hearing document
	 * @throws DuplicateEntityFoundException - When a board hearing document
	 * already exists with the specified parameters
	 */
	Document createDocument(Date date, String title, String filename, 
			String fileExtension)
					throws DuplicateEntityFoundException;
	
	/**
	 * Updates a board hearing document with the given parameters.
	 * @param document - Document to update
	 * @param date - Date
	 * @param title - String
	 * @param filename - String
	 * @param fileExtension - String
	 * @return Updated board hearing document
	 * @throws DuplicateEntityFoundException - When a board hearing document
	 * already exists with the specified parameters
	 */
	Document updateDocument(Document document, Date date, String title, 
			String filename, String fileExtension)
					throws DuplicateEntityFoundException;
	
	/**
	 * Removes the specified board hearing document.
	 * 
	 * @param document - Document to remove
	 */
	void removeDocument(Document document);
	
	/**
	 * Creates a document tag for a board hearing document.
	 * 
	 * @param name - String
	 * @param document - Document
	 * @return newly created document tag
	 * @throws DuplicateEntityFoundException - When a board hearing document
	 * tag already exists with the given name for the specified board hearing
	 * document
	 */
	DocumentTag createDocumentTag(String name, Document document)
					throws DuplicateEntityFoundException;
	
	/**
	 * Updates the specified board hearing document tag with the given 
	 * parameters.
	 * 
	 * @param documentTag - Document tag to update
	 * @param name - String
	 * @param document - Document
	 * @return updated board hearing document tag
	 * @throws DuplicateEntityFoundException - When a board hearing document
	 * already exists with the given parameters for the specified board hearing
	 * document
	 */
	DocumentTag updateDocumentTag(DocumentTag documentTag, String name, 
			Document document)
					throws DuplicateEntityFoundException;
	
	/**
	 * Removes the specified board hearing document tag.
	 * 
	 * @param documentTag - Document tag to remove
	 */
	void removeDocumentTag(DocumentTag documentTag);
	
	/**
	 * Creates a new board hearing associable document.
	 * 
	 * @param boardHearing - Board Hearing
	 * @param document - Document
	 * @return Newly created board hearing associable document
	 * @throws DuplicateEntityFoundException - When a board hearing associable
	 * document already exists with the given parameters for the specified
	 * board hearing
	 */
	BoardHearingAssociableDocument createBoardHearingAssociableDocument(
			BoardHearing boardHearing, Document document)
					throws DuplicateEntityFoundException;
	
	/**
	 * Updates an existing board hearing associable document.
	 * 
	 * @param boardHearingAssociableDocument - Board Hearing Associable Document
	 * @param boardHearing - Board Hearing
	 * @param document - Document
	 * @return Updated board hearing associable document
	 * @throws DuplicateEntityFoundException - When a board hearing associable
	 * document already exists with the given parameters for the specified
	 * board hearing
	 */
	BoardHearingAssociableDocument updateBoardHearingAssociableDocument(
			BoardHearingAssociableDocument boardHearingAssociableDocument, 
			BoardHearing boardHearing, Document document)
					throws DuplicateEntityFoundException;
	
	/**
	 * Removes the specified board hearing associable document.
	 * 
	 * @param boardHearingAssociableDocument - Board Hearing Associable Document
	 * to be removed
	 */
	void removeBoardHearingAssociableDocument(
			BoardHearingAssociableDocument boardHearingAssociableDocument);
	
	/**
	 * Returns a list of document tags from the specified document.
	 * 
	 * @param document - Document
	 * @return List of document tags from the specified document
	 */
	List<DocumentTag> findDocumentTagsByDocument(Document document);
	
}
