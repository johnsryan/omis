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
package omis.parolereview.web.form;

import java.io.Serializable;

import omis.document.domain.DocumentTag;

/**
 * Document tag item.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Jan 29, 2018)
 * @since OMIS 3.0
 *
 */
public class DocumentTagItem implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private DocumentTag documentTag;
	
	private String name;
	
	private ParoleReviewItemOperation itemOperation;
	
	/**
	 * 
	 */
	public DocumentTagItem() {
	}

	/**
	 * Returns the document tag.
	 * 
	 * @return document tag
	 */
	public DocumentTag getDocumentTag() {
		return documentTag;
	}

	/**
	 * Sets the document tag.
	 * 
	 * @param documentTag document tag
	 */
	public void setDocumentTag(final DocumentTag documentTag) {
		this.documentTag = documentTag;
	}

	/**
	 * Returns the name.
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name name
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * Returns the item operation.
	 * 
	 * @return item operation
	 */
	public ParoleReviewItemOperation getItemOperation() {
		return itemOperation;
	}

	/**
	 * Sets the item operation.
	 * 
	 * @param itemOperation item operation
	 */
	public void setItemOperation(
			final ParoleReviewItemOperation itemOperation) {
		this.itemOperation = itemOperation;
	}
}