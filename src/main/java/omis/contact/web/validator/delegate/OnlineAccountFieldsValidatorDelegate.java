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
package omis.contact.web.validator.delegate;

import org.springframework.validation.Errors;

import omis.contact.web.form.OnlineAccountFields;

/**
 * Online account fields validator delegate.
 * 
 * @author Sheronda Vaughn
 * @author Yidong Li
 * @author Josh Divine
 * @version 0.1.1 (Feb 12, 2018)
 * @since OMIS 3.0
 */
public class OnlineAccountFieldsValidatorDelegate {

	/* Constructor. */
	
	/**
	 * Instantiates a default instance of online account fields validator.
	 */
	public OnlineAccountFieldsValidatorDelegate() {
		// Default constructor.
	}

	/* Validation method. */
	
	public Errors validateOnlineAccountFields(
			final OnlineAccountFields onlineAccountFields, 
			final String onlineAccountFieldsVariableName,
			final Errors errors) {
		if (onlineAccountFields.getName()==null
			||onlineAccountFields.getName().length() == 0) {
			errors.rejectValue(onlineAccountFieldsVariableName + ".name", 
					"onlineAccountFields.name.empty");
		}

		if (onlineAccountFields.getHost() == null) {
			errors.rejectValue(onlineAccountFieldsVariableName + ".host", 
					"onlineAccountFields.host.empty");
		}
		return errors;
	}
}