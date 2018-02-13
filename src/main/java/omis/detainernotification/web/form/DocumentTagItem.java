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
package omis.detainernotification.web.form;

import java.io.Serializable;

import omis.document.domain.DocumentTag;

/**
 * DocumentTagItem.java
 * 
 * @author Annie Jacques
 * @author Josh Divine 
 * @version 0.1.1 (Feb 12, 2018)
 * @since OMIS 3.0
 *
 */
public class DocumentTagItem implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private DocumentTag documentTag;
	
	private String name;
	
	private DetainerNotificationItemOperation itemOperation;
	
	/**
	 * 
	 */
	public DocumentTagItem() {
	}

	/**
	 * Returns the documentTag
	 * @return documentTag - DocumentTag
	 */
	public DocumentTag getDocumentTag() {
		return documentTag;
	}

	/**
	 * Sets the documentTag
	 * @param documentTag - DocumentTag
	 */
	public void setDocumentTag(final DocumentTag documentTag) {
		this.documentTag = documentTag;
	}

	/**
	 * Returns the name
	 * @return name - String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name
	 * @param name - String
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * Returns the itemOperation
	 * @return itemOperation - PresentenceInvestigationItemOperation
	 */
	public DetainerNotificationItemOperation getItemOperation() {
		return itemOperation;
	}

	/**
	 * Sets the itemOperation
	 * @param itemOperation - PresentenceInvestigationItemOperation
	 */
	public void setItemOperation(
			final DetainerNotificationItemOperation itemOperation) {
		this.itemOperation = itemOperation;
	}
	
	
	
	
}
