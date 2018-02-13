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
import java.util.Date;
import omis.medicalreview.domain.MedicalReviewNote;

/**
 * Medical Review Note Item.
 * 
 *@author Annie Wahl 
 *@version 0.1.0 (Feb 1, 2018)
 *@since OMIS 3.0
 *
 */
public class MedicalReviewNoteItem implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private MedicalReviewNote medicalReviewNote;
	
	private String description;
	
	private Date date;
	
	private MedicalReviewItemOperation itemOperation;
	
	/**
	 * Default constructor for Medical Review Note Item.
	 */
	public MedicalReviewNoteItem() {
	}

	/**
	 * Returns the medicalReviewNote.
	 * @return medicalReviewNote - Medical Review Note
	 */
	public MedicalReviewNote getMedicalReviewNote() {
		return this.medicalReviewNote;
	}

	/**
	 * Sets the medicalReviewNote.
	 * @param medicalReviewNote - Medical Review Note
	 */
	public void setMedicalReviewNote(
			final MedicalReviewNote medicalReviewNote) {
		this.medicalReviewNote = medicalReviewNote;
	}

	/**
	 * Returns the description.
	 * @return description - String
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Sets the description.
	 * @param description - String
	 */
	public void setDescription(final String description) {
		this.description = description;
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
