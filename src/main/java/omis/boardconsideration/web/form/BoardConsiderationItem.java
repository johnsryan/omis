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

import omis.boardconsideration.domain.BoardConsideration;
import omis.boardconsideration.domain.BoardConsiderationCategory;

/**
 * Board consideration item.
 * 
 * @author Josh Divine
 * @version 0.1.0 (May 30, 2018)
 * @since OMIS 3.0
 */
public class BoardConsiderationItem implements Serializable {

	private static final long serialVersionUID = 1L;

	private BoardConsideration boardConsideration;
	
	private String title;
	
	private String description;
	
	private Boolean accepted;
	
	private BoardConsiderationCategory category;
	
	private ItemOperation operation;
	
	/**
	 * Instantiates a default board consideration item. 
	 */
	public BoardConsiderationItem() {
		// Default instantiation
	}

	/**
	 * Returns the board consideration.
	 *
	 * @return boardConsideration board consideration
	 */
	public BoardConsideration getBoardConsideration() {
		return boardConsideration;
	}

	/**
	 * Sets the board consideration.
	 *
	 * @param boardConsideration board consideration
	 */
	public void setBoardConsideration(
			final BoardConsideration boardConsideration) {
		this.boardConsideration = boardConsideration;
	}

	/**
	 * Returns the title.
	 *
	 * @return title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title.
	 *
	 * @param title title
	 */
	public void setTitle(final String title) {
		this.title = title;
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
	 * Returns whether the board consideration was accepted.
	 *
	 * @return accepted
	 */
	public Boolean getAccepted() {
		return accepted;
	}

	/**
	 * Sets whether the board consideration was accepted.
	 *
	 * @param accepted accepted
	 */
	public void setAccepted(final Boolean accepted) {
		this.accepted = accepted;
	}

	/**
	 * Returns the category.
	 *
	 * @return category
	 */
	public BoardConsiderationCategory getCategory() {
		return category;
	}

	/**
	 * Sets the category.
	 *
	 * @param category category
	 */
	public void setCategory(final BoardConsiderationCategory category) {
		this.category = category;
	}

	/**
	 * Returns the operation.
	 *
	 * @return operation
	 */
	public ItemOperation getOperation() {
		return operation;
	}

	/**
	 * Sets the operation.
	 *
	 * @param operation operation
	 */
	public void setOperation(final ItemOperation operation) {
		this.operation = operation;
	}
}