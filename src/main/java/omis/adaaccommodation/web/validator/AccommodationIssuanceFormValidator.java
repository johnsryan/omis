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
package omis.adaaccommodation.web.validator;

import omis.adaaccommodation.web.form.AccommodationIssuanceForm;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Accommodation form validator.
 *
 * @author Sheronda Vaughn
 * @author Josh Divine
 * @version 0.1.0 (Feb 12, 2018)
 * @since OMIS 3.0
 */
public class AccommodationIssuanceFormValidator implements Validator {

	/** Instantiates a validator for accommodation issuance forms. */
	public AccommodationIssuanceFormValidator() {
		// Default constructor
	}

	/** {@inheritDoc} */
	@Override
	public boolean supports(final Class<?> clazz) {
		return AccommodationIssuanceForm.class.isAssignableFrom(clazz);
	}

	/** {@inheritDoc} */
	@Override
	public void validate(final Object target, final Errors errors) {
		AccommodationIssuanceForm form = (AccommodationIssuanceForm) target;

		if (form.getDay() == null) {
			errors.rejectValue("day", "accommodationIssuance.day.empty");
		}
		if (form.getTime() == null) {
			errors.rejectValue("time", 
					"accommodationIssuance.time.empty");
		}
		if (form.getText().isEmpty()) {
			errors.rejectValue("text", 
					"accommodationIssuance.description.empty");
		}
		if (form.getIssuer() == null) {
			errors.rejectValue("issuer", "accommodationIssuance.issuer.empty");
		}
	}	
}