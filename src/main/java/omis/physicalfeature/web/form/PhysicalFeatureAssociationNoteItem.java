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
package omis.physicalfeature.web.form;

import java.io.Serializable;
import java.util.Date;

import omis.physicalfeature.domain.PhysicalFeatureAssociationNote;

/**
 * Physical feature association note item.
 * 
 * @author Joel Norris
 * @author Josh Divine
 * @version 0.1.1 (Feb 12, 2018)
 * @since OMIS 3.0
 */
public class PhysicalFeatureAssociationNoteItem implements Serializable {

	private static final long serialVersionUID = 1L;

	private PhysicalFeatureAssociationNote physicalFeatureAssociationNote;
	
	private String note;
	
	private Date date;
	
	private PhysicalFeatureAssociationNoteItemOperation operation;
	
	/**
	 * Instantiates a default instance of physical feature association 
	 * note item.
	 */
	public PhysicalFeatureAssociationNoteItem() {
		//Default constructor.
	}

	/**
	 * Returns the physical feature association note.
	 * 
	 * @return physical feature association note
	 */
	public PhysicalFeatureAssociationNote getPhysicalFeatureAssociationNote() {
		return this.physicalFeatureAssociationNote;
	}

	/**
	 * Sets the physical feature association note.
	 * 
	 * @param physicalFeatureAssociationNote physical feature association note
	 */
	public void setPhysicalFeatureAssociationNote(
			final PhysicalFeatureAssociationNote 
			physicalFeatureAssociationNote) {
		this.physicalFeatureAssociationNote = physicalFeatureAssociationNote;
	}

	/**
	 * Returns the note.
	 * 
	 * @return note
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
	 * Returns the date.
	 * 
	 * @return date
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
	 * Returns the operation.
	 * 
	 * @return physical feature association note item operation
	 */
	public PhysicalFeatureAssociationNoteItemOperation getOperation() {
		return this.operation;
	}

	/**
	 * Sets the operation.
	 * 
	 * @param operation physical feature association note item operation
	 */
	public void setOperation(
			final PhysicalFeatureAssociationNoteItemOperation operation) {
		this.operation = operation;
	}
}