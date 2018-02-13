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
import omis.medicalreview.domain.MedicalHealthClassification;

/**
 * Medical Review Form.
 * 
 *@author Annie Wahl 
 *@version 0.1.0 (Feb 1, 2018)
 *@since OMIS 3.0
 *
 */
public class MedicalReviewForm implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Date date;
	
	private String text;
	
	private MedicalHealthClassification healthClassification;
	
	private List<MedicalReviewNoteItem> medicalReviewNoteItems =
			new ArrayList<MedicalReviewNoteItem>();
	
	private List<MedicalReviewDocumentAssociationItem>
		medicalReviewDocumentAssociationItems =
		new ArrayList<MedicalReviewDocumentAssociationItem>();
	
	/**
	 * Default constructor for Medical Review Form. 
	 */
	public MedicalReviewForm() {
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
	 * Returns the text.
	 * @return text - String
	 */
	public String getText() {
		return this.text;
	}

	/**
	 * Sets the text.
	 * @param text - String
	 */
	public void setText(final String text) {
		this.text = text;
	}

	/**
	 * Returns the healthClassification.
	 * @return healthClassification - MedicalHealthClassification
	 */
	public MedicalHealthClassification getHealthClassification() {
		return this.healthClassification;
	}

	/**
	 * Sets the healthClassification.
	 * @param healthClassification - MedicalHealthClassification
	 */
	public void setHealthClassification(
			final MedicalHealthClassification healthClassification) {
		this.healthClassification = healthClassification;
	}

	/**
	 * Returns the medicalReviewNoteItems.
	 * @return medicalReviewNoteItems - List of MedicalReviewNoteItems
	 */
	public List<MedicalReviewNoteItem> getMedicalReviewNoteItems() {
		return this.medicalReviewNoteItems;
	}

	/**
	 * Sets the medicalReviewNoteItems.
	 * @param medicalReviewNoteItems - List of MedicalReviewNoteItems
	 */
	public void setMedicalReviewNoteItems(
			final List<MedicalReviewNoteItem> medicalReviewNoteItems) {
		this.medicalReviewNoteItems = medicalReviewNoteItems;
	}

	/**
	 * Returns the medicalReviewDocumentAssociationItems.
	 * @return medicalReviewDocumentAssociationItems - List of
	 * MedicalReviewDocumentAssociationItems
	 */
	public List<MedicalReviewDocumentAssociationItem>
			getMedicalReviewDocumentAssociationItems() {
		return this.medicalReviewDocumentAssociationItems;
	}

	/**
	 * Sets the medicalReviewDocumentAssociationItems.
	 * @param medicalReviewDocumentAssociationItems - List of
	 * MedicalReviewDocumentAssociationItems
	 */
	public void setMedicalReviewDocumentAssociationItems(
			final List<MedicalReviewDocumentAssociationItem>
				medicalReviewDocumentAssociationItems) {
		this.medicalReviewDocumentAssociationItems =
				medicalReviewDocumentAssociationItems;
	}
	
	
}
