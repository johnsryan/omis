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
package omis.medicalreview.web.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import omis.document.domain.Document;
import omis.document.web.form.DocumentTagItem;
import omis.medicalreview.domain.MedicalReviewDocumentAssociation;

/**
 * Medical Review Document Association Item.
 * 
 *@author Annie Wahl 
 *@version 0.1.0 (Feb 1, 2018)
 *@since OMIS 3.0
 *
 */
public class MedicalReviewDocumentAssociationItem implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private MedicalReviewDocumentAssociation medicalReviewDocumentAssociation;
	
	private Document document;
	
	private String title;
	
	private List<DocumentTagItem> tagItems = new ArrayList<DocumentTagItem>();
	
	private Date date;
	
	private String fileExtension;
	
	private byte[] data;
	
	private MedicalReviewItemOperation itemOperation;
	
	/**
	 * Default constructor for Medical Review Document Association Item.
	 */
	public MedicalReviewDocumentAssociationItem() {
	}

	/**
	 * Returns the medicalReviewDocumentAssociation.
	 * @return medicalReviewDocumentAssociation - Medical Review Document
	 * Association
	 */
	public MedicalReviewDocumentAssociation
			getMedicalReviewDocumentAssociation() {
		return this.medicalReviewDocumentAssociation;
	}

	/**
	 * Sets the medicalReviewDocumentAssociation.
	 * @param medicalReviewDocumentAssociation - Medical Review Document
	 * Association
	 */
	public void setMedicalReviewDocumentAssociation(
			final MedicalReviewDocumentAssociation
				medicalReviewDocumentAssociation) {
		this.medicalReviewDocumentAssociation =
				medicalReviewDocumentAssociation;
	}

	/**
	 * Returns the document.
	 * @return document - Document
	 */
	public Document getDocument() {
		return this.document;
	}

	/**
	 * Sets the document.
	 * @param document - Document
	 */
	public void setDocument(final Document document) {
		this.document = document;
	}

	/**
	 * Returns the title.
	 * @return title - String
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Sets the title.
	 * @param title - String
	 */
	public void setTitle(final String title) {
		this.title = title;
	}

	/**
	 * Returns the tagItems.
	 * @return tagItems - List of Document Tag Items
	 */
	public List<DocumentTagItem> getTagItems() {
		return this.tagItems;
	}

	/**
	 * Sets the tagItems.
	 * @param tagItems - List of Document Tag Items
	 */
	public void setTagItems(final List<DocumentTagItem> tagItems) {
		this.tagItems = tagItems;
	}

	/**
	 * Returns the date.
	 * @return date - Date
	 */
	public Date getDate() {
		return this.date;
	}

	/**
	 * Sets the date.
	 * @param date - Date
	 */
	public void setDate(final Date date) {
		this.date = date;
	}

	/**
	 * Returns the fileExtension.
	 * @return fileExtension - String
	 */
	public String getFileExtension() {
		return this.fileExtension;
	}

	/**
	 * Sets the fileExtension.
	 * @param fileExtension - String
	 */
	public void setFileExtension(final String fileExtension) {
		this.fileExtension = fileExtension;
	}

	/**
	 * Returns the data.
	 * @return data - byte[]
	 */
	public byte[] getData() {
		return this.data;
	}

	/**
	 * Sets the data.
	 * @param data - byte[]
	 */
	public void setData(final byte[] data) {
		this.data = data;
	}

	/**
	 * Returns the itemOperation.
	 * @return itemOperation - Medical Review Item Operation
	 */
	public MedicalReviewItemOperation getItemOperation() {
		return this.itemOperation;
	}

	/**
	 * Sets the itemOperation.
	 * @param itemOperation - Medical Review Item Operation
	 */
	public void setItemOperation(
			final MedicalReviewItemOperation itemOperation) {
		this.itemOperation = itemOperation;
	}
	
	
}
