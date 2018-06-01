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

import omis.offender.web.form.OffenderPersonalDetailsForm;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Validator for offender personal details form.
 * 
 * @author Stephen Abson
 * @version 0.1.0 (Dec 16, 2013)
 * @since OMIS 3.0
 */
public class OffenderPersonalDetailsFormValidator
		implements Validator {

	/** Instantiates a default validator for offender personal details form. */
	public OffenderPersonalDetailsFormValidator() {
		// Default instantiation
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean supports(final Class<?> clazz) {
		return OffenderPersonalDetailsForm.class.isAssignableFrom(clazz);
	}

	/** {@inheritDoc} */
	@Override
	public void validate(final Object target, final Errors errors) {
		OffenderPersonalDetailsForm offenderPersonalDetailsForm
			= (OffenderPersonalDetailsForm) target;
		if (offenderPersonalDetailsForm.getLastName() == null
				|| offenderPersonalDetailsForm.getLastName().length() == 0) {
			errors.rejectValue("lastName", "lastName.empty");
		}
		if (offenderPersonalDetailsForm.getFirstName() == null
				|| offenderPersonalDetailsForm.getFirstName().length() == 0) {
			errors.rejectValue("firstName", "firstName.empty");
		}
		if (offenderPersonalDetailsForm.getBirthDate() == null) {
			errors.rejectValue("birthDate", "birthDate.empty");
		}
		if (offenderPersonalDetailsForm.getBirthCountry() == null) {
			errors.rejectValue("birthCountry", "birthCountry.empty");
		}
		if (offenderPersonalDetailsForm.getCreateNewBirthPlace() != null
				&& offenderPersonalDetailsForm.getCreateNewBirthPlace()
				&& (offenderPersonalDetailsForm.getBirthPlaceName() == null
					|| offenderPersonalDetailsForm.getBirthPlaceName()
						.isEmpty())) {
			errors.rejectValue("birthPlace", "city.nameEmpty");
		}
		if (offenderPersonalDetailsForm.getValidateSocialSecurityNumber()) {
			if (offenderPersonalDetailsForm.getSocialSecurityNumber() != null
					&& !"".equals(offenderPersonalDetailsForm
							.getSocialSecurityNumber())) {
				String socialSecurityNumber = offenderPersonalDetailsForm
						.getSocialSecurityNumber().replace("-", "");
				if (!socialSecurityNumber.matches("\\d+")) {
					errors.rejectValue("socialSecurityNumber",
							"socialSecurityNumber.invalid");
				} else if (socialSecurityNumber.length() != 9) {
					errors.rejectValue("socialSecurityNumber",
							"socialSecurityNumber.wrongLength");
				}
			}
		}
		if (offenderPersonalDetailsForm.getSex() == null) {
			errors.rejectValue("sex", "sex.empty");
		}
		if ((offenderPersonalDetailsForm.getDeceased() == null
				|| !offenderPersonalDetailsForm.getDeceased())
				&& offenderPersonalDetailsForm.getDeathDate() != null) {
			errors.rejectValue("deathDate", "deathDate.notAllowedUnlessDead");
		}
	}
}