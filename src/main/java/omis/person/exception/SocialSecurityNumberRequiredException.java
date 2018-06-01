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
package omis.person.exception;

import omis.exception.BusinessException;

/**
 * Thrown if a person does not have a social security when required.
 *
 * @author Stephen Abson
 * @version 0.0.1 (Apr 8, 2015)
 * @since OMIS 3.0
 */
public class SocialSecurityNumberRequiredException
		extends BusinessException {
	
	private static final long serialVersionUID = 1L;

	/** Instantiates default exception. */
	public SocialSecurityNumberRequiredException() {
		// Default instantiation
	}

	/**
	 * Instantiates with message and cause.
	 * 
	 * @param message message
	 * @param cause cause
	 */
	public SocialSecurityNumberRequiredException(
			final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates with message.
	 * 
	 * @param message message
	 */
	public SocialSecurityNumberRequiredException(final String message) {
		super(message);
	}

	/**
	 * Instantiates with cause.
	 * 
	 * @param cause cause
	 */
	public SocialSecurityNumberRequiredException(final Throwable cause) {
		super(cause);
	}
}