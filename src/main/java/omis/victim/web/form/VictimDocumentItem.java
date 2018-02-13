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
package omis.victim.web.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import omis.docket.domain.Docket;
import omis.document.web.form.DocumentTagItem;
import omis.victim.domain.VictimDocumentAssociation;

/**
 * Victim document item.
 * 
 * @author Joel Norris
 * @author Josh Divine
 * @version 0.1.1 (Feb 12, 2018)
 * @since OMIS 3.0
 */
public class VictimDocumentItem implements Serializable {

	private static final long serialVersionUID = 1L;
	private VictimDocumentAssociation association;
	private byte[] documentData;
	private String title;
	private Date date;
	private Docket docket;
	private String fileExtension;
	private List<DocumentTagItem> tagItems = new ArrayList<DocumentTagItem>();
	private VictimDocumentItemOperation operation;
	
	/**
	 * Instantiates a default instance of victim document item.
	 */
	public VictimDocumentItem() {
		//Default constructor
	}
	
	/**
	 * Instantiates an instance of victim document item.
	 * 
	 * @param association association
	 * @param title title
	 * @param date date
	 * @param docket docket
	 * @param tagItems tag items
	 * @param operation operation
	 */
	public VictimDocumentItem(final VictimDocumentAssociation association, final String title, final Date date, final Docket docket,
			final List<DocumentTagItem> tagItems, final VictimDocumentItemOperation operation) {
			this.association = association;
			this.title = title;
			this.date = date;
			this.docket = docket;
			this.tagItems = tagItems;
			this.operation = operation;
	}

	/**
	 * Returns victim document association.
	 * 
	 * @return victim document association
	 */
	public VictimDocumentAssociation getAssociation() {
		return this.association;
	}

	/**
	 * Sets the victim document association.
	 * 
	 * @param association victim document association
	 */
	public void setAssociation(final VictimDocumentAssociation association) {
		this.association = association;
	}

	/**
	 * Returns the document data.
	 * 
	 * @return byte array of document
	 */
	public byte[] getDocumentData() {
		return this.documentData;
	}

	/**
	 * Sets the document data.
	 * 
	 * @param documentData document data byte array
	 */
	public void setDocumentData(final byte[] documentData) {
		this.documentData = documentData;
	}

	/**
	 * Returns the title.
	 * 
	 * @return title
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Sets the title.
	 * 
	 * @param title title
	 */
	public void setTitle(final String title) {
		this.title = title;
	}

	/**
	 * Returns the date.
	 * 
	 * @return date
	 */
	public Date getDate() {
		return this.date;
	}

	/**
	 * Sets the date.
	 * 
	 * @param date
	 */
	public void setDate(final Date date) {
		this.date = date;
	}

	/**
	 * Returns the docket.
	 * 
	 * @return docket
	 */
	public Docket getDocket() {
		return this.docket;
	}

	/**
	 * Sets the docket.
	 * 
	 * @param docket docket
	 */
	public void setDocket(final Docket docket) {
		this.docket = docket;
	}

	/**
	 * Returns the file extension.
	 * 
	 * @return file extension
	 */
	public String getFileExtension() {
		return this.fileExtension;
	}

	/**
	 * Sets the file extension.
	 * 
	 * @param fileExtension file extension
	 */
	public void setFileExtension(final String fileExtension) {
		this.fileExtension = fileExtension;
	}

	/**
	 * Returns the document tag items.
	 * 
	 * @return list of document tag items
	 */
	public List<DocumentTagItem> getTagItems() {
		return this.tagItems;
	}
	
	/**
	 * Sets the document tag items.
	 * 
	 * @param tagItems document tag items
	 */
	public void setTagItems(final List<DocumentTagItem> tagItems) {
		this.tagItems = tagItems;
	}

	/**
	 * Returns the victim document item operation.
	 * 
	 * @return victim document item operation
	 */
	public VictimDocumentItemOperation getOperation() {
		return this.operation;
	}

	/**
	 * Sets the victim document item operation.
	 * 
	 * @param operation victim document item operation
	 */
	public void setOperation(final VictimDocumentItemOperation operation) {
		this.operation = operation;
	}
}