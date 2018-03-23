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
package omis.boardhearing.dao.impl.hibernate;

import org.hibernate.SessionFactory;

import omis.boardhearing.dao.BoardHearingAssociableDocumentDao;
import omis.boardhearing.domain.BoardHearing;
import omis.boardhearing.domain.BoardHearingAssociableDocument;
import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.document.domain.Document;

/**
 * Board hearing associable document data access object hibernate
 * implementation.
 *
 * @author Trevor Isles
 * @version 0.1.0 (Feb 12, 2018)
 * @since OMIS 3.0
 */
public class BoardHearingAssociableDocumentDaoHibernateImpl 
	extends GenericHibernateDaoImpl<BoardHearingAssociableDocument>
	implements BoardHearingAssociableDocumentDao {
	
	private static final String 
		FIND_BOARD_HEARING_ASSOCIABLE_DOCUMENT_QUERY_NAME 
			= "findBoardHearingAssociableDocument";
	
	private static final String
		FIND_BOARD_HEARING_ASSOCIABLE_DOCUMENT_EXCLUDING_QUERY_NAME 
			= "findBoardHearingAssociableDocumentExcluding";
	
	private static final String BOARD_HEARING_PARAM_NAME = "boardHearing";
	
	private static final String DOCUMENT_PARAM_NAME = "document";
	
	private static final String BOARD_HEARING_ASSOCIABLE_DOCUMENT_PARAM_NAME
		= "boardHearingAssociableDocument";
	
	public BoardHearingAssociableDocumentDaoHibernateImpl(
			final SessionFactory sessionFactory, final String entityName) {
		super(sessionFactory, entityName);
	}

	/**{@inheritDoc} */
	@Override
	public BoardHearingAssociableDocument find(final BoardHearing boardHearing,
			final Document document) {
		BoardHearingAssociableDocument boardHearingAssociableDocument
				= (BoardHearingAssociableDocument) this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_BOARD_HEARING_ASSOCIABLE_DOCUMENT_QUERY_NAME)
				.setParameter(BOARD_HEARING_PARAM_NAME, boardHearing)
				.setParameter(DOCUMENT_PARAM_NAME, document)
				.uniqueResult();
		
		return boardHearingAssociableDocument;
	}
	
	/**{@inheritDoc} */
	@Override
	public BoardHearingAssociableDocument findExcluding(
			final BoardHearing boardHearing,
			final Document document,
			final BoardHearingAssociableDocument 
				boardHearingAssociableDocumentExcluded) {
		BoardHearingAssociableDocument boardHearingAssociableDocument
				= (BoardHearingAssociableDocument) this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(
					FIND_BOARD_HEARING_ASSOCIABLE_DOCUMENT_EXCLUDING_QUERY_NAME)
				.setParameter(BOARD_HEARING_PARAM_NAME, boardHearing)
				.setParameter(DOCUMENT_PARAM_NAME, document)
				.setParameter(BOARD_HEARING_ASSOCIABLE_DOCUMENT_PARAM_NAME,
						boardHearingAssociableDocumentExcluded)
				.uniqueResult();
		
		return boardHearingAssociableDocument;
	}
	
}
