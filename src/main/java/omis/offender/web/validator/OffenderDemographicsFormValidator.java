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

import omis.offender.web.form.OffenderDemographicsForm;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Validator for form for offender demographics.
 * 
 * @author Stephen Abson
 * @version 0.1.0 (Feb 7, 2014)
 * @since OMIS 3.0
 */
public class OffenderDemographicsFormValidator
		implements Validator {

	/** Instantiates a default validator for form for offender demographics. */
	public OffenderDemographicsFormValidator() {
		// Default instantiation
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean supports(final Class<?> clazz) {
		return OffenderDemographicsForm.class.isAssignableFrom(clazz);
	}

	/** {@inheritDoc} */
	@Override
	public void validate(final Object target, final Errors errors) {
		OffenderDemographicsForm offenderDemographicsForm
			= (OffenderDemographicsForm) target;
		if (offenderDemographicsForm.getEyeColor() == null) {
			errors.rejectValue("eyeColor", "eyeColor.empty");
		}
		if (offenderDemographicsForm.getHairColor() == null) {
			errors.rejectValue("hairColor", "hairColor.empty");
		}
		if (offenderDemographicsForm.getRace() == null) {
			errors.rejectValue("race", "race.empty");
		}
		if (offenderDemographicsForm.getMaritalStatus() == null) {
			errors.rejectValue("maritalStatus", "maritalStatus.empty");
		}
	}
}