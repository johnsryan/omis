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
package omis.residence.exception;

import omis.exception.BusinessException;
/**
 * Allowed residential location rule exists Exception
 *
 * @author Sheronda Vaughn
 * @version 0.1.0 (May 17, 2018)
 * @since OMIS 3.0
 */
public class AllowedResidentialLocationRuleExistsException 
	extends BusinessException {

	private static final long serialVersionUID = 1L;
	
	/**Instantiates an implementation of allowed residential location rule.*/
	public AllowedResidentialLocationRuleExistsException() {
		// Default constructor.
	}
	
	/**
	 * Constructor.
	 * 
	 * @param message message
	 * @param cause cause
	 */
	public AllowedResidentialLocationRuleExistsException(final String message,
			final Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param message message
	 */
	public AllowedResidentialLocationRuleExistsException(final String message) {
		super(message);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param cause cause
	 */
	public AllowedResidentialLocationRuleExistsException(
			final Throwable cause) {
		super(cause);
	}
}