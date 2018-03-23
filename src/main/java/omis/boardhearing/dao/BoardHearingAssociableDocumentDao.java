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
package omis.boardhearing.dao;

import omis.boardhearing.domain.BoardHearing;
import omis.boardhearing.domain.BoardHearingAssociableDocument;
import omis.dao.GenericDao;
import omis.document.domain.Document;

/**
 * Board hearing associable document data access object.
 *
 * @author Trevor Isles
 * @version 0.1.0 (Feb 7, 2018)
 * @since OMIS 3.0
 */
public interface BoardHearingAssociableDocumentDao 
		extends GenericDao<BoardHearingAssociableDocument> {
	
	/**
	 * Returns a board hearing associable document with the specified 
	 * properties.
	 * @param boardHearing - Board Hearing
	 * @param document - Document
	 * @return Board hearing associable document with the specified properties.
	 */
	BoardHearingAssociableDocument find(BoardHearing boardhearing,
			Document document);
	
	/**
	 * Returns a board hearing associable document with the specified properties
	 * excluding the given board hearing associable document.
	 * 
	 * @param document - Document
	 * @param boardHearingAssociableDocumentExcluded - Board Hearing Associable
	 * Document to exclude
	 * @return - Board hearing associable document with the specified properties
	 * excluding the given board hearing associable document.
	 */
	BoardHearingAssociableDocument findExcluding(BoardHearing boardHearing,
			Document document,
		BoardHearingAssociableDocument boardHearingAssociableDocumentExcluded);

}
