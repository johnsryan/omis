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
import java.util.List;

/**
 * Board consideration form.
 * 
 * @author Josh Divine
 * @version 0.1.0 (May 30, 2018)
 * @since OMIS 3.0
 */
public class BoardConsiderationForm implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<BoardConsiderationItem> boardConsiderationItems;
	
	private List<BoardConsiderationNoteItem> boardConsiderationNoteItems;
	
	/**
	 * Instantiates a default board consideration form. 
	 */
	public BoardConsiderationForm() {
		// Default instantiation
	}

	/**
	 * Returns the board consideration items.
	 *
	 * @return board consideration items
	 */
	public List<BoardConsiderationItem> getBoardConsiderationItems() {
		return boardConsiderationItems;
	}

	/**
	 * Sets the board consideration items.
	 *
	 * @param boardConsiderationItems board consideration items
	 */
	public void setBoardConsiderationItems(
			final List<BoardConsiderationItem> boardConsiderationItems) {
		this.boardConsiderationItems = boardConsiderationItems;
	}

	/**
	 * Returns the board consideration note items.
	 *
	 * @return board consideration note items
	 */
	public List<BoardConsiderationNoteItem> getBoardConsiderationNoteItems() {
		return boardConsiderationNoteItems;
	}

	/**
	 * Sets the board consideration note items.
	 *
	 * @param boardConsiderationNoteItems board consideration note items
	 */
	public void setBoardConsiderationNoteItems(
			final List<BoardConsiderationNoteItem> boardConsiderationNoteItems) {
		this.boardConsiderationNoteItems = boardConsiderationNoteItems;
	}
}