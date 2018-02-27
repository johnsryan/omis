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
package omis.paroleplan.web.form;

import java.io.Serializable;
import java.util.Date;

import omis.paroleplan.domain.ParolePlanNote;

/**
 * Parole plan note item.
 *
 * @author Josh Divine
 * @version 0.1.0 (Fen 13, 2018)
 * @since OMIS 3.0
 */
public class ParolePlanNoteItem implements Serializable {

	private static final long serialVersionUID = 1L;

	private ParolePlanNote parolePlanNote;
	
	private Date date;
	
	private String value;
	
	private ParolePlanItemOperation operation;
	
	/** Instantiates a default note on a parole plan form. */
	public ParolePlanNoteItem() {
		// Default instantiation
	}

	/**
	 * Returns the parole plan note.
	 * 
	 * @return parole plan note
	 */
	public ParolePlanNote getParolePlanNote() {
		return parolePlanNote;
	}

	/**
	 * Sets the parole plan note.
	 * 
	 * @param parolePlanNote parole plan note
	 */
	public void setParolePlanNote(final ParolePlanNote parolePlanNote) {
		this.parolePlanNote = parolePlanNote;
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
	 * Returns the parole plan item operation.
	 * 
	 * @return parole plan item operation
	 */
	public ParolePlanItemOperation getOperation() {
		return operation;
	}

	/**
	 * Sets the parole plan item operation.
	 * 
	 * @param operation parole plan item operation
	 */
	public void setOperation(final ParolePlanItemOperation operation) {
		this.operation = operation;
	}
}
