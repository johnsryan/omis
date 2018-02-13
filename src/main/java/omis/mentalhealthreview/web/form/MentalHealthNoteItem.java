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
package omis.mentalhealthreview.web.form;

import java.io.Serializable;
import java.util.Date;

import omis.mentalhealthreview.domain.MentalHealthNote;

/**
 * Mental health note item.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 1, 2018)
 * @since OMIS 3.0
 */
public class MentalHealthNoteItem implements Serializable {

	private static final long serialVersionUID = 1L;

	private MentalHealthNote mentalHealthNote;
	
	private Date date;
		
	private String description;
	
	private MentalHealthReviewItemOperation itemOperation;
	
	/**
	 * Instantiates a default mental health review note item.
	 */
	public MentalHealthNoteItem() {
	}

	/**
	 * Returns the mental health note.
	 *
	 * @return mental health note
	 */
	public MentalHealthNote getMentalHealthNote() {
		return mentalHealthNote;
	}

	/**
	 * Sets the mental health note.
	 *
	 * @param mentalHealthNote mental health note
	 */
	public void setMentalHealthNote(final MentalHealthNote mentalHealthNote) {
		this.mentalHealthNote = mentalHealthNote;
	}

	/**
	 * Returns the date.
	 *
	 * @return date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Sets the date.
	 *
	 * @param date date
	 */
	public void setDate(final Date date) {
		this.date = date;
	}

	/**
	 * Returns the description.
	 *
	 * @return description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description description
	 */
	public void setDescription(final String description) {
		this.description = description;
	}

	/**
	 * Returns the item operation.
	 *
	 * @return item operation
	 */
	public MentalHealthReviewItemOperation getItemOperation() {
		return itemOperation;
	}

	/**
	 * Sets the item operation.
	 *
	 * @param itemOperation item operation
	 */
	public void setItemOperation(
			final MentalHealthReviewItemOperation itemOperation) {
		this.itemOperation = itemOperation;
	}
}