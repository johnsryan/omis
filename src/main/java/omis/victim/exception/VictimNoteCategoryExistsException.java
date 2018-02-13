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
package omis.victim.exception;

import omis.exception.DuplicateEntityFoundException;

/**
 * Thrown exception when victim note category exists.
 *
 * @author Sheronda Vaughn
 * @version 0.1.0 (Jan 31, 2018)
 * @since OMIS 3.0
 */
public class VictimNoteCategoryExistsException 
	extends DuplicateEntityFoundException {

	private static final long serialVersionUID = 1L;

	/** Instantiates exception when victim note category exists exception. */
	public VictimNoteCategoryExistsException() {
		// Default constructor.
	}

	/**
	 * Instantiates exception when victim note category exists exception.
	 * 
	 * @param message message
	 * @param cause cause
	 */
	public VictimNoteCategoryExistsException(final String message, 
			final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates exception when victim note category exists exception.
	 * 
	 * @param message message
	 */
	public VictimNoteCategoryExistsException(final String message) {
		super(message);
	}

	/**
	 * Instantiates exception when victim note category exists exception.
	 * 
	 * @param cause cause
	 */
	public VictimNoteCategoryExistsException(final Throwable cause) {
		super(cause);
	}
}