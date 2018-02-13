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
package omis.residence.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import omis.residence.web.form.ResidenceSearchForm;

/**
 * Validator for form to search for residence term.
 *
 * @author Sheronda Vaughn
 * @author Josh Divine
 * @version 0.1.1 (Feb 12, 2018)
 * @since OMIS 3.0
 */

public class ResidenceSearchFormValidator implements Validator {

	/** Instantiates validator for form to search for residence term. */
	public ResidenceSearchFormValidator() {
		// Default instantiation
	}

	/** {@inheritDoc} */
	@Override
	public boolean supports(Class<?> clazz) {
		return ResidenceSearchForm.class.isAssignableFrom(clazz);
	}

	/** {@inheritDoc} */
	@Override
	public void validate(Object target, Errors errors) {
		ResidenceSearchForm residenceSearchForm = (ResidenceSearchForm) target;
		if (residenceSearchForm.getValue().isEmpty()
				&& residenceSearchForm.getState() == null
				&& residenceSearchForm.getCity() == null
				&& residenceSearchForm.getEffectiveDate() == null) {
			errors.reject("residence.search.fields.empty");
		}
	}
}