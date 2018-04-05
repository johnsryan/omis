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
package omis.locationterm.web.validator;

import omis.locationterm.web.form.LocationReasonTermForm;
import omis.util.DateManipulator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Validator for form for location reason terms.
 * 
 * @author Stephen Abson
 * @version 0.1.0 (Jan 16, 2014)
 * @since OMIS 3.0
 */
public class LocationReasonTermFormValidator
		implements Validator {
	
	/** Instantiates a validator for form for location reason terms. */
	public LocationReasonTermFormValidator() {
		// Default instantiation
	}

	/** {@inheritDoc} */
	@Override
	public boolean supports(final Class<?> clazz) {
		return LocationReasonTermForm.class.isAssignableFrom(clazz);
	}

	/** {@inheritDoc} */
	@Override
	public void validate(final Object target, final Errors errors) {
		LocationReasonTermForm locationReasonTermForm
			= (LocationReasonTermForm) target;
		if (locationReasonTermForm.getStartDate() != null
				&& locationReasonTermForm.getEndDate() != null
				&& DateManipulator.getDateAtTimeOfDay(
						locationReasonTermForm.getStartDate(),
						locationReasonTermForm.getStartTime()).getTime()
					> DateManipulator.getDateAtTimeOfDay(
							locationReasonTermForm.getEndDate(),
							locationReasonTermForm.getEndTime()).getTime()) {
			errors.rejectValue("startDate",
					"dateRange.startDateGreaterThanEndDate");
		}
		if (locationReasonTermForm.getStartDate() == null) {
			errors.rejectValue("startDate", "startDate.empty");
		}
		if (locationReasonTermForm.getStartTime() == null) {
			errors.rejectValue("startTime", "startTime.empty");
		}
		if (locationReasonTermForm.getEndDate() != null
				&& locationReasonTermForm.getEndTime() == null) {
			errors.rejectValue("endTime", "endTime.empty");
		}
		if (locationReasonTermForm.getEndDate() == null
				&& locationReasonTermForm.getEndTime() != null) {
			errors.rejectValue("endDate", "endDate.empty");
		}
		if (locationReasonTermForm.getLocationTerm() == null) {
			errors.rejectValue("locationTerm", "locationTerm.empty");
		}
		if (locationReasonTermForm.getReason() == null) {
			errors.rejectValue("reason", "locationReason.empty");
		}
	}
}