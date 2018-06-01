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
package omis.boardconsideration.web.form;

import java.io.Serializable;
import java.util.Date;

import omis.boardconsideration.domain.BoardConsiderationNote;

/**
 * Board consideration note item.
 * 
 * @author Josh Divine
 * @version 0.1.0 (May 30, 2018)
 * @since OMIS 3.0
 */
public class BoardConsiderationNoteItem implements Serializable {

	private static final long serialVersionUID = 1L;

	private BoardConsiderationNote boardConsiderationNote;
	
	private Date date;
	
	private String value;
	
	private ItemOperation operation;
	
	/** Instantiates a default note on a board consideration form. */
	public BoardConsiderationNoteItem() {
		// Default instantiation
	}

	/**
	 * Returns the board consideration note.
	 *
	 * @return board consideration note
	 */
	public BoardConsiderationNote getBoardConsiderationNote() {
		return boardConsiderationNote;
	}

	/**
	 * Sets the board consideration note.
	 *
	 * @param boardConsiderationNote board consideration note
	 */
	public void setBoardConsiderationNote(
			final BoardConsiderationNote boardConsiderationNote) {
		this.boardConsiderationNote = boardConsiderationNote;
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
	 * Returns the board consideration note item operation.
	 * 
	 * @return board consideration note item operation
	 */
	public ItemOperation getOperation() {
		return operation;
	}

	/**
	 * Sets the board consideration note item operation.
	 * 
	 * @param operation board consideration note item operation
	 */
	public void setOperation(final ItemOperation operation) {
		this.operation = operation;
	}
}
