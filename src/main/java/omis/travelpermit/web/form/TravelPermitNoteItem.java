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
import java.util.Date;

import omis.audit.domain.UpdateSignature;
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
	private String note;
	private Date date;
	private UserAccount userAccount;
	private TravelPermitNoteItemOperation operation;
	private UpdateSignature updateSignature;
	private TravelPermitNote travelPermitNote;
	
	/**
	 * Instantiates a default instance of travel permit notes.
	 */
	public TravelPermitNoteItem() {
		//Default constructor.
	}

	/**
	 * Gets the note.
	 *
	 * @return the note
	 */
	public String getNote() {
		return this.note;
	}

	/**
	 * Sets the note.
	 *
	 * @param note note
	 */
	public void setNote(final String note) {
		this.note = note;
	}
	
	/**
	 * Gets the date.
	 *
	 * @return the date
	 */
	public Date getDate() {
		return this.date;
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
	 * Gets the update signature.
	 *
	 * @return UpdateSignature UpdateSignature
	 */
	public UpdateSignature getUpdateSignature() {
		return this.updateSignature;
	}

	/**
	 * Sets the UpdateSignature Update Signature
	 *
	 * @param UpdateSignature UpdateSignature
	 */
	public void setUpdateSignature(final UpdateSignature updateSignature) {
		this.updateSignature = updateSignature;
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
	
	/**
	 * Gets the travel permit note.
	 *
	 * @return the travel permit note
	 */
	public TravelPermitNote getTravelPermitNote() {
		return this.travelPermitNote;
	}

	/**
	 * Sets the travel permit note.
	 *
	 * @param note travel permit note
	 */
	public void setTravelPermitNote(final TravelPermitNote note) {
		this.travelPermitNote = note;
	}
}