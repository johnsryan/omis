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

import omis.media.domain.Photo;

/**
 * Photo item.
 * @author Joel Norris
 * @author Josh Divine
 * @version 0.1.1 (Feb 12, 2018)
 * @since OMIS 3.0
 */
public class PhotoItem implements Serializable {

	private static final long serialVersionUID = 1L;

	private Photo photo;
	
	private Date date;
	
	private byte[] photoData;
	
	private PhotoItemOperation operation;

	/**
	 * Instantiates an instance of photo item with the specified properties.
	 * 
	 * @param photo photo
	 * @param date date
	 * @param photoData photo data
	 * @param operation photo item operation
	 */
	public PhotoItem(final Photo photo, final Date date, final byte[] photoData,
			final PhotoItemOperation operation) {
		this.photo = photo;
		this.date = date;
		this.photoData = photoData;
		this.operation = operation;
	}
	
	/**
	 * Instantiates a default instance of photo item.
	 */
	public PhotoItem() {
		//Default constructor.
	}
	
	/**
	 * Return the photo of the photo item.
	 * @return photo
	 */
	public Photo getPhoto() {
		return this.photo;
	}

	/**
	 * Sets the photo of the photo item.
	 * @param photo photo
	 */
	public void setPhoto(final Photo photo) {
		this.photo = photo;
	}

	/**
	 * Returns the photo data of the photo item.
	 * @return photo data
	 */
	public byte[] getPhotoData() {
		return this.photoData;
	}

	/**
	 * Sets the photo data of the photo item.
	 * @param photoData photo data
	 */
	public void setPhotoData(final byte[] photoData) {
		this.photoData = photoData;
	}

	/**
	 * Returns the date of the photo item.
	 * @return date
	 */
	public Date getDate() {
		return this.date;
	}

	/**
	 * Sets the date of the photo item.
	 * @param date date
	 */
	public void setDate(final Date date) {
		this.date = date;
	}

	/**
	 * Returns the photo item operation.
	 * 
	 * @return operation photo item operation
	 */
	public PhotoItemOperation getOperation() {
		return this.operation;
	}

	/**
	 * Sets the photo item operation.
	 * 
	 * @param operation photo item operation
	 */
	public void setOperation(final PhotoItemOperation operation) {
		this.operation = operation;
	}
}