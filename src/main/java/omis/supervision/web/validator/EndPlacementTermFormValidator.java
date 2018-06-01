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
package omis.supervision.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import omis.supervision.web.form.EndPlacementTermForm;

/**
 * Validator for form for ending placement terms.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 16, 2017)
 * @since OMIS 3.0
 */
public class EndPlacementTermFormValidator implements Validator {

	/** Instantiates a default validator for ending placement terms. */
	public EndPlacementTermFormValidator() {
		// Default instantiation
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean supports(Class<?> clazz) {
		return EndPlacementTermForm.class.isAssignableFrom(clazz);
	}

	/** {@inheritDoc} */
	@Override
	public void validate(Object target, Errors errors) {
		EndPlacementTermForm endPlacementTermForm 
			= (EndPlacementTermForm) target;
		if (endPlacementTermForm.getEndDate() == null) {
			errors.rejectValue("endDate", "endPlacementTermForm.endDate.empty");
		}
		if (endPlacementTermForm.getEndTime() == null) {
			errors.rejectValue("endTime", "endPlacementTermForm.endTime.empty");
		}
		if (endPlacementTermForm.getAllowEndChangeReason()
				&& endPlacementTermForm.getEndChangeReason() == null) {
			errors.rejectValue("endChangeReason", 
					"endPlacementTermForm.endChangeReason.empty");
		}
	}

}
