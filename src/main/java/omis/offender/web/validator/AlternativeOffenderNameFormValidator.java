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
package omis.offender.web.validator;

import omis.offender.web.form.AlternativeOffenderNameForm;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Validator for form for alternative offender names.
 * 
 * @author Stephen Abson
 * @version 0.1.0 (Dec 5, 2013)
 * @since OMIS 3.0
 */
public class AlternativeOffenderNameFormValidator
		implements Validator {
	
	/**
	 * Instantiates default validator for form for alternative offender names.
	 */
	public AlternativeOffenderNameFormValidator() {
		// Default instantiation
	}

	/** {@inheritDoc} */
	@Override
	public boolean supports(final Class<?> clazz) {
		return AlternativeOffenderNameForm.class.isAssignableFrom(clazz);
	}

	/** {@inheritDoc} */
	@Override
	public void validate(final Object target, final Errors errors) {
		AlternativeOffenderNameForm alternativeOffenderNameForm
				= (AlternativeOffenderNameForm) target;
		if (alternativeOffenderNameForm.getCategory() == null) {
			errors.rejectValue("category", "alternativeNameCategory.empty");
		}
		if (alternativeOffenderNameForm.getLastName() == null
				|| "".equals(alternativeOffenderNameForm.getLastName())) {
			errors.rejectValue("lastName", "lastName.empty");
		}
		if (alternativeOffenderNameForm.getFirstName() == null
				|| "".equals(alternativeOffenderNameForm.getFirstName())) {
			errors.rejectValue("firstName", "firstName.empty");
		}
		if (alternativeOffenderNameForm.getStartDate() != null
				&& alternativeOffenderNameForm.getEndDate() != null
				&& alternativeOffenderNameForm.getStartDate().getTime()
					> alternativeOffenderNameForm.getEndDate().getTime()) {
			errors.rejectValue("startDate",
					"dateRange.startDateGreaterThanEndDate");
		}
	}
}