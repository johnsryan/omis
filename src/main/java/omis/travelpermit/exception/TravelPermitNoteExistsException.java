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
package omis.travelpermit.exception;

import omis.exception.BusinessException;

/**
 * Thrown when travel permit note already exists.
 * 
 * @author Yidong Li
 * @version 0.1.0 (May 18, 2018)
 * @since  OMIS 3.0
 */
public class TravelPermitNoteExistsException extends BusinessException {

	private static final long serialVersionUID = 1L;
	
	/** Instantiates default residence term exists exception. */
	public TravelPermitNoteExistsException() {
		//Default constructor
	}

	/**
	 * Constructor.
	 * 
	 * @param message message
	 * @param cause cause
	 */
	public TravelPermitNoteExistsException(final String message,
			final Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param message message
	 */
	public TravelPermitNoteExistsException(final String message) {
		super(message);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param cause cause
	 */
	public TravelPermitNoteExistsException(final Throwable cause) {
		super(cause);
	}
}
