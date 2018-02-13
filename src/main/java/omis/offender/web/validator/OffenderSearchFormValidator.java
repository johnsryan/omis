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

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import omis.offender.web.form.OffenderSearchForm;

/**
 * Validator for form to search offenders.
 *
 * @author Sheronda Vaughn
 * @author Josh Divine
 * @version 0.1.1 (Feb 12, 2018)
 * @since OMIS 3.0
 */
public class OffenderSearchFormValidator implements Validator {

	/** Instantiates validator for form to search offenders. */
	public OffenderSearchFormValidator() {
		// Default instantiation
	}

	/** {@inheritDoc} */
	@Override
	public boolean supports(final Class<?> clazz) {
		return OffenderSearchForm.class.isAssignableFrom(clazz);
	}

	/** {@inheritDoc} */
	@Override
	public void validate(final Object target, final Errors errors) {
		OffenderSearchForm offenderSearchForm = (OffenderSearchForm) target;
		if (offenderSearchForm.getDocIdNumber() == null 
				&& offenderSearchForm.getSocialSecurityNumber() == null 
				&& offenderSearchForm.getFirstName().isEmpty()
				&& offenderSearchForm.getMiddleName().isEmpty()
				&& offenderSearchForm.getLastName().isEmpty()
				&& this.numberOfCriteria(offenderSearchForm) < 2) {
			errors.reject("search.fields.minimum");
		}	
	}
	
	/* Counts number of criteria used among name, . */
	private int numberOfCriteria(final OffenderSearchForm offenderSearchForm) {
		int result = 0;
		if (!"".equals(offenderSearchForm.getFirstName())) {
			result++;
		}
		if (!"".equals(offenderSearchForm.getMiddleName())) {
			result++;
		}
		if (!"".equals(offenderSearchForm.getLastName())) {
			result++;
		}
		if (offenderSearchForm.getSex() != null) {
			result++;
		}
		if (offenderSearchForm.getBirthDate() != null) {
			result++;
		}
		if (offenderSearchForm.getSocialSecurityNumber() != null) {
			result++;
		}
		return result;
	}
}