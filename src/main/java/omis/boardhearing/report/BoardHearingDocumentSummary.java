/*
\ * OMIS - Offender Management Information System
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
package omis.boardhearing.report;

import java.io.Serializable;
import java.util.Date;

/**
 * Board hearing document summary.
 *
 * @author Trevor Isles
 * @version 0.1.0 (Feb 12, 2018)
 * @since OMIS 3.0
 */
public class BoardHearingDocumentSummary implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private final Long boardHearingAssociableDocumentId;
	
	private final Long documentId;
	
	private final String documentTitle;
	
	private final String categoryName;
	
	private final Date documentDate;
	
	/**
	 * @param boardHearingDocumentId
	 * @param documentId
	 * @param documentTitle
	 * @param categoryName
	 * @param documentDate
	 */
	public BoardHearingDocumentSummary(
			final Long boardHearingAssociableDocumentId, 
			final Long documentId,
			final String documentTitle,
			final Date documentDate) {
		this.boardHearingAssociableDocumentId = boardHearingAssociableDocumentId;
		this.documentId = documentId;
		this.documentTitle = documentTitle;
		this.categoryName = null;
		this.documentDate = documentDate;
	}
	
	/**
	 * Returns the board hearing document id.
	 * @return boardHearingDocumentId - Long
	 */
	public Long getBoardHearingAssociableDocumentId() {
		return boardHearingAssociableDocumentId;
	}
	
	/**
	 * Returns the document id.
	 * @return documentId - Long
	 */
	public Long getDocumentId() {
		return documentId;
	}
	
	/**
	 * Returns the board hearing document title.
	 * @return documentTitle - String
	 */
	public String getDocumentTitle() {
		return documentTitle;
	}
	
	/**
	 * Returns the board hearing category name.
	 * @return categoryName - String
	 */
	public String getCategoryName() {
		return categoryName;
	}
	
	/**
	 * Returns the board hearing document date.
	 * @return documentDate - Date
	 */
	public Date getDocumentDate() {
		return documentDate;
	}

}
