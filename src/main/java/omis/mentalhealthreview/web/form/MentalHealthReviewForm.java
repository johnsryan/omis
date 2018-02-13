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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Mental health review form.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 1, 2018)
 * @since OMIS 3.0
 */
public class MentalHealthReviewForm implements Serializable {

	private static final long serialVersionUID = 1L;

	private Date date;
	
	private String text;
	
	private List<MentalHealthReviewDocumentAssociationItem> 
			mentalHealthReviewDocumentAssociationItems = new ArrayList<>();
	
	private List<MentalHealthNoteItem> mentalHealthNoteItems = new ArrayList<>();
	
	/**
	 * Instantiates a default mental health review form. 
	 */
	public MentalHealthReviewForm() {
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
	 * Returns the text.
	 *
	 * @return text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets the text.
	 *
	 * @param text text
	 */
	public void setText(final String text) {
		this.text = text;
	}

	/**
	 * Returns the list of mental health review document association items.
	 *
	 * @return list of mental health review document association items
	 */
	public List<MentalHealthReviewDocumentAssociationItem> 
			getMentalHealthReviewDocumentAssociationItems() {
		return mentalHealthReviewDocumentAssociationItems;
	}

	/**
	 * Sets the list of mental health review document association items.
	 *
	 * @param mentalHealthReviewDocumentAssociationItems list of mental health 
	 * review document association items
	 */
	public void setMentalHealthReviewDocumentAssociationItems(
			final List<MentalHealthReviewDocumentAssociationItem>
					mentalHealthReviewDocumentAssociationItems) {
		this.mentalHealthReviewDocumentAssociationItems = 
				mentalHealthReviewDocumentAssociationItems;
	}

	/**
	 * Returns the mental health note items.
	 *
	 * @return mental health note items
	 */
	public List<MentalHealthNoteItem> getMentalHealthNoteItems() {
		return mentalHealthNoteItems;
	}

	/**
	 * Sets the mental health note items.
	 *
	 * @param mentalHealthNoteItems mental health note items
	 */
	public void setMentalHealthNoteItems(
			final List<MentalHealthNoteItem> mentalHealthNoteItems) {
		this.mentalHealthNoteItems = mentalHealthNoteItems;
	}
	
}
