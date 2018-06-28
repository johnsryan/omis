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
package omis.hearing.exception;

import omis.exception.DuplicateEntityFoundException;

/**
 * Thrown when the same user attendance exists more than once.
 *
 * @author Trevor Isles
 * @version 0.1.0 (Jun 15, 2018)
 * @since OMIS 3.0
 */
public class UserAttendanceExistsException 
		extends DuplicateEntityFoundException {

	private static final long serialVersionUID = -1L;
	
	/** Instantiates a default user attendance exists exception. */
	public UserAttendanceExistsException() {
		// Default instantiation
	}
	
	/**
	 * Instantiates with the specified message and cause.
	 * 
	 * @param message message
	 * @param cause cause
	 */
	public UserAttendanceExistsException(final String message, final Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * Instantiates with the specified message.
	 * 
	 * @param message message
	 */
	public UserAttendanceExistsException(final String message) {
		super(message);
	}
	
	/**
	 * Instantiates with the specified cause.
	 * 
	 * @param cause cause
	 */
	public UserAttendanceExistsException(final Throwable cause) {
		super(cause);
	}

}
