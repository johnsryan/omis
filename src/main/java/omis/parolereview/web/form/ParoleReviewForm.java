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

import omis.parolereview.domain.ParoleEndorsementCategory;
import omis.parolereview.domain.StaffRoleCategory;
import omis.staff.domain.StaffAssignment;

/**
 * Parole review form.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Jan 29, 2018)
 * @since OMIS 3.0
 */
public class ParoleReviewForm implements Serializable {

	private static final long serialVersionUID = 1L;

	private StaffAssignment staffAssignment;
	
	private Date date;
	
	private String text;
	
	private ParoleEndorsementCategory endorsement;
	
	private StaffRoleCategory staffRole;
	
	private List<ParoleReviewDocumentAssociationItem> 
			paroleReviewDocumentAssociationItems = new ArrayList<>();
	
	private List<ParoleReviewNoteItem> paroleReviewNoteItems = new ArrayList<>();
	
	/**
	 * Instantiates a default parole review form. 
	 */
	public ParoleReviewForm() {
	}

	/**
	 * Returns the staff assignment.
	 *
	 * @return staff assignment
	 */
	public StaffAssignment getStaffAssignment() {
		return staffAssignment;
	}

	/**
	 * Sets the staff assignment.
	 *
	 * @param staffAssignment staff assignment
	 */
	public void setStaffAssignment(final StaffAssignment staffAssignment) {
		this.staffAssignment = staffAssignment;
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
	 * Returns the endorsement.
	 *
	 * @return endorsement
	 */
	public ParoleEndorsementCategory getEndorsement() {
		return endorsement;
	}

	/**
	 * Sets the endorsement.
	 *
	 * @param endorsement parole endorsement category
	 */
	public void setEndorsement(final ParoleEndorsementCategory endorsement) {
		this.endorsement = endorsement;
	}

	/**
	 * Returns the staff role category.
	 *
	 * @return staff role
	 */
	public StaffRoleCategory getStaffRole() {
		return staffRole;
	}

	/**
	 * Sets the staff role category.
	 *
	 * @param staffRole staff role category
	 */
	public void setStaffRole(final StaffRoleCategory staffRole) {
		this.staffRole = staffRole;
	}

	/**
	 * Returns the list of parole review document association items.
	 *
	 * @return list of parole review document association items
	 */
	public List<ParoleReviewDocumentAssociationItem> 
			getParoleReviewDocumentAssociationItems() {
		return paroleReviewDocumentAssociationItems;
	}

	/**
	 * Sets the list of parole review document association items.
	 *
	 * @param paroleReviewDocumentAssociationItems list of parole review 
	 * document association items
	 */
	public void setParoleReviewDocumentAssociationItems(
			final List<ParoleReviewDocumentAssociationItem>
					paroleReviewDocumentAssociationItems) {
		this.paroleReviewDocumentAssociationItems = 
				paroleReviewDocumentAssociationItems;
	}

	/**
	 * Returns the list of parole review note items.
	 *
	 * @return list of parole review note items
	 */
	public List<ParoleReviewNoteItem> getParoleReviewNoteItems() {
		return paroleReviewNoteItems;
	}

	/**
	 * Sets the list of parole review note items.
	 *
	 * @param paroleReviewNoteItems parole review note items
	 */
	public void setParoleReviewNoteItems(
			final List<ParoleReviewNoteItem> paroleReviewNoteItems) {
		this.paroleReviewNoteItems = paroleReviewNoteItems;
	}
}