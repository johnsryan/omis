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
package omis.supervision.exception;

import omis.exception.DuplicateEntityFoundException;

/**
 * Thrown when correctional status term exists.
 * 
 * @author Stephen Abson
 * @version 0.0.1 (Jun 6, 2018)
 * @since OMIS 3.0
 */
public class CorrectionalStatusTermExistsException
		extends DuplicateEntityFoundException {

	private static final long serialVersionUID = 1L;
	
	/** Instantiates exception thrown when correctional status term exists. */
	public CorrectionalStatusTermExistsException() {
		// Default instantiation
	}
	
	/**
	 * Instantiates exception thrown when correction status term exists.
	 * 
	 * @param message message
	 * @param cause cause
	 */
	public CorrectionalStatusTermExistsException(
			final String message, final Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * Instantiates exception thrown when correctional status term exists.
	 * 
	 * @param message message
	 */
	public CorrectionalStatusTermExistsException(
			final String message) {
		super(message);
	}
	
	/**
	 * Instantiates exception thrown when correctional status term exists.
	 * 
	 * @param cause cause
	 */
	public CorrectionalStatusTermExistsException(
			final Throwable cause) {
		super(cause);
	}
}