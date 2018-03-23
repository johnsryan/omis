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
package omis.boardhearing.service.delegate;

import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.boardhearing.dao.BoardHearingAssociableDocumentDao;
import omis.boardhearing.domain.BoardHearing;
import omis.boardhearing.domain.BoardHearingAssociableDocument;
import omis.document.domain.Document;
import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;

/**
 * Board hearing associable document delegate.
 *
 * @author Trevor Isles
 * @version 0.1.0 (Feb 12, 2018)
 * @since OMIS 3.0
 */
public class BoardHearingAssociableDocumentDelegate {
	
	private static final String DUPLICATE_ENTITY_FOUND_MSG = "Board hearing"
			+ "associable document already exists with given board hearing and"
			+ " document.";
	
	private final BoardHearingAssociableDocumentDao 
		boardHearingAssociableDocumentDao; 
	
	private final InstanceFactory<BoardHearingAssociableDocument>
		boardHearingAssociableDocumentInstanceFactory;
	
	private final AuditComponentRetriever auditComponentRetriever;
	
	public BoardHearingAssociableDocumentDelegate(
			final BoardHearingAssociableDocumentDao boardHearingAssociableDocumentDao,
			final InstanceFactory<BoardHearingAssociableDocument> boardHearingAssociableDocumentInstanceFactory,
			final AuditComponentRetriever auditComponentRetriever) {
		this.boardHearingAssociableDocumentDao = boardHearingAssociableDocumentDao;
		this.boardHearingAssociableDocumentInstanceFactory = boardHearingAssociableDocumentInstanceFactory;
		this.auditComponentRetriever = auditComponentRetriever;
	}

	public BoardHearingAssociableDocument create(
			final BoardHearing boardHearing, 
			final Document document) 
					throws DuplicateEntityFoundException {
		if(this.boardHearingAssociableDocumentDao.find(boardHearing, document) 
				!= null) {
			throw new DuplicateEntityFoundException(DUPLICATE_ENTITY_FOUND_MSG);
		}
		
		BoardHearingAssociableDocument boardHearingAssociableDocument =
				this.boardHearingAssociableDocumentInstanceFactory
				.createInstance();
		
		boardHearingAssociableDocument.setBoardHearing(boardHearing);
		boardHearingAssociableDocument.setDocument(document);
		boardHearingAssociableDocument.setCreationSignature(
				new CreationSignature(
						this.auditComponentRetriever.retrieveUserAccount(),
						this.auditComponentRetriever.retrieveDate()));
		boardHearingAssociableDocument.setUpdateSignature(
				new UpdateSignature(
						this.auditComponentRetriever.retrieveUserAccount(),
						this.auditComponentRetriever.retrieveDate()));
		
		return this.boardHearingAssociableDocumentDao.makePersistent(
				boardHearingAssociableDocument);
		
	}

	public BoardHearingAssociableDocument update(
			final BoardHearingAssociableDocument boardHearingAssociableDocument, 
			final BoardHearing boardHearing,
			final Document document) throws DuplicateEntityFoundException {
		if(this.boardHearingAssociableDocumentDao.findExcluding(boardHearing, 
				document, boardHearingAssociableDocument) != null) {
			throw new DuplicateEntityFoundException(DUPLICATE_ENTITY_FOUND_MSG);
		}
		
		boardHearingAssociableDocument.setBoardHearing(boardHearing);
		boardHearingAssociableDocument.setDocument(document);
		boardHearingAssociableDocument.setUpdateSignature(
				new UpdateSignature(
						this.auditComponentRetriever.retrieveUserAccount(),
						this.auditComponentRetriever.retrieveDate()));
		
		return this.boardHearingAssociableDocumentDao
				.makePersistent(boardHearingAssociableDocument);
		
	}

	public void remove(final BoardHearingAssociableDocument 
			boardHearingAssociableDocument) {
		this.boardHearingAssociableDocumentDao.makeTransient(
				boardHearingAssociableDocument);
	}

}
