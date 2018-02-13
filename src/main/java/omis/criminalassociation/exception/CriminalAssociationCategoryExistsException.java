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
package omis.criminalassociation.exception;

import omis.exception.DuplicateEntityFoundException;

/**
 * Thrown when criminal association category exists.
 *
 * @author Sheronda Vaughn
 * @version 0.1.0 (Feb 5, 2018)
 * @since OMIS 3.0
 */
public class CriminalAssociationCategoryExistsException 
	extends DuplicateEntityFoundException {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Instantiates an implementation of criminalAssociation category 
	 * exists exception.
	 */
	public CriminalAssociationCategoryExistsException() {
		//Default constructor.
	}
	
	/**
	 * Instantiates an implementation of criminal association category 
	 * exists exception.
	 * 
	 * @param message message
	 * @param cause cause
	 */
	public CriminalAssociationCategoryExistsException(final String message, 
			final Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * Instantiates an implementation of criminal association category 
	 * exists exception.
	 * 
	 * @param message message
	 * @param cause cause
	 */
	public CriminalAssociationCategoryExistsException(final String message) {
		super(message);
	}
	
	/**
	 * Instantiates an implementation of criminal association category 
	 * exists exception.
	 * 
	 * @param message message
	 * @param cause cause
	 */
	public CriminalAssociationCategoryExistsException(final Throwable cause) {
		super(cause);
	}	
}