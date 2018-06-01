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
package omis.offender.web.validator.delegate;

import omis.offender.web.form.OffenderSearchFields;
import omis.offender.web.form.OffenderSearchType;

import org.springframework.validation.Errors;

/**
 * Delegate for validating offender search fields.
 *
 * @author Stephen Abson
 * @version 0.0.1 (Aug 11, 2015)
 * @since OMIS 3.0
 */
public class OffenderSearchFieldsValidatorDelegate {

	/* Constructors. */
	
	/** Instantiates default validator for offender search fields. */
	public OffenderSearchFieldsValidatorDelegate() {
		// Default instantiation
	}
	
	/* Methods. */
	
	/**
	 * Validates offender search fields.
	 * 
	 * @param offenderSearchFields fields to validate
	 * @param errors errors
	 */
	public void validate(
			final OffenderSearchFields offenderSearchFields,
			final Errors errors) {
		if (offenderSearchFields.getType() == null) {
			errors.rejectValue("offenderSearchFields.type",
					"offenderSearchType.empty");
		} else if (OffenderSearchType.NAME.equals(
				offenderSearchFields.getType())) {
			if ((offenderSearchFields.getLastName() == null
					|| offenderSearchFields.getLastName().isEmpty())
				&& (offenderSearchFields.getFirstName() == null
					|| offenderSearchFields.getFirstName().isEmpty())) {
				errors.rejectValue("offenderSearchFields.lastName",
						"offenderSearchName.empty");
			}
		} else if (OffenderSearchType.OFFENDER_NUMBER.equals(
				offenderSearchFields.getType())) {
			if (offenderSearchFields.getOffenderNumber() == null) {
				errors.rejectValue("offenderSearchFields.offenderNumber",
						"offenderSearchOffenderNumber.empty");
			}
		} else if (OffenderSearchType.SOCIAL_SECURITY_NUMBER.equals(
				offenderSearchFields.getType())) {
			if (offenderSearchFields.getSocialSecurityNumber() == null
				|| offenderSearchFields.getSocialSecurityNumber().isEmpty()) {
				errors.rejectValue("offenderSearchFields.socialSecurityNumber",
						"offenderSearchSocialSecurityNumber.empty");
			}
		} else if (OffenderSearchType.BIRTH_DATE.equals(
				offenderSearchFields.getType())) {
			if (offenderSearchFields.getBirthDate() == null) {
				errors.rejectValue("offenderSearchFields.birthDate",
						"offenderSearchBirthDate.empty");
			}
		} else {
			throw new UnsupportedOperationException(String.format(
					"Unsupported search type: %s",
					offenderSearchFields.getType()));
		}
	}
}