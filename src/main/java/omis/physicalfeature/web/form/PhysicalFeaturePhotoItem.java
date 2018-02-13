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

import java.util.Date;

import omis.media.domain.Photo;
import omis.physicalfeature.domain.PhysicalFeaturePhotoAssociation;

/**
 * Physical feature photo item.
 * 
 * @author Joel Norris
 * @author Josh Divine
 * @version 0.1.1 (Feb 12, 2018)
 * @since OMIS 3.0
 */
public class PhysicalFeaturePhotoItem extends PhotoItem {

	private static final long serialVersionUID = 1L;
	
	private PhysicalFeaturePhotoAssociation photoAssociation;

	/**
	 * Instantiates an instance of physical feature photo item using the photo
	 * item super constructor and a physical feature photo association.
	 * 
	 * @param photo photo
	 * @param date date
	 * @param photoData photo date
	 * @param operation photo item operation
	 * @param photoAssociation physical feature photo association
	 */
	public PhysicalFeaturePhotoItem(final Photo photo, final Date date, 
			final byte[] photoData, final PhotoItemOperation operation,
			final PhysicalFeaturePhotoAssociation photoAssociation) {
		super(photo, date, photoData, operation);
		this.photoAssociation = photoAssociation;
	}
	
	/**
	 * Instantiates a default instance of physical feature photo item.
	 */
	public PhysicalFeaturePhotoItem() {
		//Default constructor.
	}

	/**
	 * Returns the physical feature photo association.
	 * 
	 * @return physical feature photo association
	 */
	public PhysicalFeaturePhotoAssociation getPhotoAssociation() {
		return this.photoAssociation;
	}

	/**
	 * Sets the physical feature photo association.
	 * 
	 * @param photoAssociation physical feature photo association
	 */
	public void setPhotoAssociation(
			final PhysicalFeaturePhotoAssociation photoAssociation) {
		this.photoAssociation = photoAssociation;
	}
}