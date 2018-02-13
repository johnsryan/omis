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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import omis.document.domain.Document;
import omis.parolereview.domain.ParoleReviewDocumentAssociation;

/**
 * Parole review document association item.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Jan 29, 2018)
 * @since OMIS 3.0
 */
public class ParoleReviewDocumentAssociationItem implements Serializable {

	private static final long serialVersionUID = 1L;

	private ParoleReviewDocumentAssociation
			paroleReviewDocumentAssociation;
	
	private Document document;
	
	private String title;
	
	private String filename;
	
	private String fileExtension;
	
	private Date fileDate;
	
	private List<DocumentTagItem> documentTagItems =
			new ArrayList<DocumentTagItem>();
	
	private ParoleReviewItemOperation itemOperation;
	
	private byte[] data;
	
	/**
	 * 
	 */
	public ParoleReviewDocumentAssociationItem() {
	}

	/**
	 * Returns the parole review document association.
	 * 
	 * @return parole review document association
	 */
	public ParoleReviewDocumentAssociation
			getParoleReviewDocumentAssociation() {
		return paroleReviewDocumentAssociation;
	}

	/**
	 * Sets the parole review document association.
	 * 
	 * @param paroleReviewDocumentAssociation parole review document association
	 */
	public void setParoleReviewDocumentAssociation(
			final ParoleReviewDocumentAssociation
					paroleReviewDocumentAssociation) {
		this.paroleReviewDocumentAssociation =
				paroleReviewDocumentAssociation;
	}

	/**
	 * Returns the document.
	 * 
	 * @return document
	 */
	public Document getDocument() {
		return document;
	}

	/**
	 * Sets the document.
	 * 
	 * @param document document
	 */
	public void setDocument(final Document document) {
		this.document = document;
	}

	/**
	 * Returns the title.
	 * 
	 * @return title
	 */
	public String getTitle() {
		return title;
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
	 * Returns the filename.
	 * 
	 * @return filename
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * Sets the filename.
	 * 
	 * @param filename filename
	 */
	public void setFilename(final String filename) {
		this.filename = filename;
	}

	/**
	 * Returns the file extension.
	 * 
	 * @return file extension
	 */
	public String getFileExtension() {
		return fileExtension;
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
	 * Returns the file date.
	 * 
	 * @return file date
	 */
	public Date getFileDate() {
		return fileDate;
	}

	/**
	 * Sets the file date.
	 * 
	 * @param fileDate file date
	 */
	public void setFileDate(final Date fileDate) {
		this.fileDate = fileDate;
	}

	/**
	 * Returns the document tag items.
	 * 
	 * @return document tag items
	 */
	public List<DocumentTagItem> getDocumentTagItems() {
		return documentTagItems;
	}

	/**
	 * Sets the document tag items.
	 * 
	 * @param documentTagItems document tag items
	 */
	public void setDocumentTagItems(
			final List<DocumentTagItem> documentTagItems) {
		this.documentTagItems = documentTagItems;
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

	/**
	 * Returns the data.
	 * 
	 * @return data
	 */
	public byte[] getData() {
		return data;
	}

	/**
	 * Sets the data.
	 * 
	 * @param data data
	 */
	public void setData(final byte[] data) {
		this.data = data;
	}
}