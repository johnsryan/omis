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
package omis.boardhearing.service.impl;

import java.util.Date;
import java.util.List;

import omis.boardhearing.domain.BoardHearing;
import omis.boardhearing.domain.BoardHearingAssociableDocument;
import omis.boardhearing.service.BoardHearingDocumentService;
import omis.boardhearing.service.delegate.BoardHearingAssociableDocumentDelegate;
import omis.document.domain.Document;
import omis.document.domain.DocumentTag;
import omis.document.service.delegate.DocumentDelegate;
import omis.document.service.delegate.DocumentTagDelegate;
import omis.exception.DuplicateEntityFoundException;

/**
 * Implementation of board hearing document service.
 * 
 * @author Trevor Isles
 * @author Josh Divine
 * @version 0.1.1 (Mar 12, 2018)
 * @since OMIS 3.0
 */
public class BoardHearingDocumentServiceImpl 
	implements BoardHearingDocumentService {
	
	private final BoardHearingAssociableDocumentDelegate 
		boardHearingAssociableDocumentDelegate;
	
	private final DocumentDelegate documentDelegate;
	
	private final DocumentTagDelegate documentTagDelegate;
	
	public BoardHearingDocumentServiceImpl(
			final BoardHearingAssociableDocumentDelegate 
				boardHearingAssociableDocumentDelegate,
			final DocumentDelegate documentDelegate,
			final DocumentTagDelegate documentTagDelegate) {
		this.boardHearingAssociableDocumentDelegate 
			= boardHearingAssociableDocumentDelegate;
		this.documentDelegate = documentDelegate;
		this.documentTagDelegate = documentTagDelegate;
	}

	/**{@inheritDoc} */
	@Override
	public Document createDocument(
			final Date date, 
			final String title, 
			final String filename, 
			final String fileExtension)
			throws DuplicateEntityFoundException {
		return this.documentDelegate.create(date, filename, fileExtension, 
				title);
	}
	
	/**{@inheritDoc} */
	@Override
	public Document updateDocument(
			final Document document,
			final Date date,
			final String title,
			final String filename,
			final String fileExtension)
			throws DuplicateEntityFoundException {
		return this.documentDelegate.update(document, title, date);
	}

	/**{@inheritDoc} */
	@Override
	public void removeDocument(final Document document) {
		this.documentDelegate.remove(document); 
	}

	/**{@inheritDoc} */
	@Override
	public DocumentTag createDocumentTag(
			final String name,
			final Document document) throws DuplicateEntityFoundException {
		return this.documentTagDelegate.tagDocument(document, name);
	}

	/**{@inheritDoc} */
	@Override
	public DocumentTag updateDocumentTag(
			final DocumentTag documentTag,
			final String name,
			final Document document)
			throws DuplicateEntityFoundException {
		return this.documentTagDelegate.update(documentTag, name); 
	}

	/**{@inheritDoc} */
	@Override
	public void removeDocumentTag(final DocumentTag documentTag) {
		this.documentTagDelegate.removeTag(documentTag);
	}

	/**{@inheritDoc} */
	@Override
	public BoardHearingAssociableDocument createBoardHearingAssociableDocument(
			BoardHearing boardHearing, Document document) 
					throws DuplicateEntityFoundException {
		return this.boardHearingAssociableDocumentDelegate.create(
				boardHearing, document);
	}

	/**{@inheritDoc} */
	@Override
	public BoardHearingAssociableDocument updateBoardHearingAssociableDocument(
			BoardHearingAssociableDocument boardHearingAssociableDocument, 
			BoardHearing boardHearing, Document document)
					throws DuplicateEntityFoundException {
		return this.boardHearingAssociableDocumentDelegate.update(
				boardHearingAssociableDocument, boardHearing, document);
	}

	/**{@inheritDoc} */
	@Override
	public void removeBoardHearingAssociableDocument(
			final BoardHearingAssociableDocument boardHearingAssociableDocument){
		this.boardHearingAssociableDocumentDelegate.remove(
				boardHearingAssociableDocument);
	}

	/**{@inheritDoc} */
	@Override
	public List<DocumentTag> findDocumentTagsByDocument(
			final Document document) {
		return this.documentTagDelegate.findByDocument(document); 
	}

}
