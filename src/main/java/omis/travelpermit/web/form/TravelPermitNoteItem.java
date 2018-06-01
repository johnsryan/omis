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
package omis.travelpermit.web.form;

import java.io.Serializable;

import omis.travelpermit.domain.TravelPermitNote;
import omis.user.domain.UserAccount;

/**
 * Travel permit note item.
 * 
 * @author Yidong Li
 * @version 0.1.1 (May 21, 2018)
 * @since OMIS 3.0
 */
public class TravelPermitNoteItem implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private TravelPermitNote note;
	private UserAccount userAccount;
	private TravelPermitNoteItemOperation operation;
	
	/**
	 * Instantiates a default instance of travel permit notes.
	 */
	public TravelPermitNoteItem() {
		//Default constructor.
	}

	/**
	 * Gets the ID.
	 *
	 * @return the id
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * Sets the ID.
	 *
	 * @param id id
	 */
	public void setId(final Long id) {
		this.id = id;
	}

	/**
	 * Gets the note.
	 *
	 * @return the note
	 */
	public TravelPermitNote getNote() {
		return this.note;
	}

	/**
	 * Sets the note.
	 *
	 * @param phoneNumber phone number
	 */
	public void setNote(final TravelPermitNote note) {
		this.note = note;
	}

	/**
	 * Gets the user account.
	 *
	 * @return the user account
	 */
	public UserAccount getUserAccount() {
		return this.userAccount;
	}

	/**
	 * Sets the user account.
	 *
	 * @param userAccount user account
	 */
	public void setUserAccount(final UserAccount userAccount) {
		this.userAccount = userAccount;
	}

	/**
	 * Gets the travel permit note item operation.
	 *
	 * @return the travel permit note item operation
	 */
	public TravelPermitNoteItemOperation getOperation() {
		return this.operation;
	}

	/**
	 * Sets the operation.
	 *
	 * @param operation operation
	 */
	public void setOperation(final TravelPermitNoteItemOperation operation) {
		this.operation = operation;
	}
}