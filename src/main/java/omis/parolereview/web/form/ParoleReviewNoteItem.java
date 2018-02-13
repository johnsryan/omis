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
import java.util.Date;

import omis.parolereview.domain.ParoleReviewNote;

/**
 * Parole review note item.
 *
 * @author Josh Divine
 * @version 0.1.0 (Nov 29, 2017)
 * @since OMIS 3.0
 */
public class ParoleReviewNoteItem implements Serializable {

	private static final long serialVersionUID = 1L;

	private ParoleReviewNote paroleReviewNote;
	
	private Date date;
	
	private String value;
	
	private ParoleReviewItemOperation operation;
	
	/** Instantiates a default note on a parole review form. */
	public ParoleReviewNoteItem() {
		// Default instantiation
	}

	/**
	 * Returns the parole review note.
	 * 
	 * @return parole review note
	 */
	public ParoleReviewNote getParoleReviewNote() {
		return paroleReviewNote;
	}

	/**
	 * Sets the parole review note.
	 * 
	 * @param paroleReviewNote parole review note
	 */
	public void setParoleReviewNote(final ParoleReviewNote paroleReviewNote) {
		this.paroleReviewNote = paroleReviewNote;
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
	 * Returns the value.
	 * 
	 * @return value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the value.
	 * 
	 * @param value value
	 */
	public void setValue(final String value) {
		this.value = value;
	}

	/**
	 * Returns the parole review item operation.
	 * 
	 * @return parole review item operation
	 */
	public ParoleReviewItemOperation getOperation() {
		return operation;
	}

	/**
	 * Sets the parole review item operation.
	 * 
	 * @param operation parole review item operation
	 */
	public void setOperation(final ParoleReviewItemOperation operation) {
		this.operation = operation;
	}
}
