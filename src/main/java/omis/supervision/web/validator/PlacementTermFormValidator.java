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

import java.util.Date;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import omis.datatype.DateRange;
import omis.supervision.domain.PlacementStatus;
import omis.supervision.web.form.PlacementTermForm;
import omis.supervision.web.form.PlacementTermNoteItem;
import omis.supervision.web.form.PlacementTermNoteItemOperation;
import omis.supervision.web.validator.delegate.PlacementTermNoteFieldsValidatorDelegate;
import omis.util.DateManipulator;

/**
 * Validator for form for placement terms.
 * 
 * @author Stephen Abson
 * @version 0.1.0 (Oct 16, 2013)
 * @since OMIS 3.0
 */
public class PlacementTermFormValidator
		implements Validator {
	
	/* Helpers. */
	
	private final PlacementTermNoteFieldsValidatorDelegate
	placementTermNoteFieldsValidatorDelegate;

	/* Constructor. */
	
	/** Instantiates a default validator for placement terms. */
	public PlacementTermFormValidator(
			final PlacementTermNoteFieldsValidatorDelegate
			placementTermNoteFieldsValidatorDelegate) {
		this.placementTermNoteFieldsValidatorDelegate
			= placementTermNoteFieldsValidatorDelegate;
	}
	
	/* Method implementations. */
	
	/** {@inheritDoc} */
	@Override
	public boolean supports(final Class<?> clazz) {
		return PlacementTermForm.class.isAssignableFrom(clazz);
	}

	/** {@inheritDoc} */
	@Override
	public void validate(final Object target, final Errors errors) {
		PlacementTermForm placementTermForm = (PlacementTermForm) target;
		if (placementTermForm.getAllowSupervisoryOrganization()
				&& placementTermForm.getSupervisoryOrganization() == null) {
			errors.rejectValue("supervisoryOrganization",
					"placementTermForm.supervisoryOrganization.empty");
		}
		if (placementTermForm.getAllowCorrectionalStatus() && 
				placementTermForm.getCorrectionalStatus() == null) {
			errors.rejectValue("correctionalStatus",
					"placementTermForm.correctionalStatus.empty");
		}
		if (placementTermForm.getAllowStartDate()
				&& placementTermForm.getStartDate() == null) {
			errors.rejectValue("startDate", "startDate.empty");
		}
		if (placementTermForm.getAllowStartTime()
				&& placementTermForm.getStartTime() == null) {
			errors.rejectValue("startTime", "startTime.empty");
		}
		if (placementTermForm.getStartDate() != null
				&& placementTermForm.getEndDate() != null) {
			if (DateManipulator.getDateAtTimeOfDay(
						placementTermForm.getEndDate(),
						placementTermForm.getEndTime()).getTime()
					< DateManipulator.getDateAtTimeOfDay(
							placementTermForm.getStartDate(),
							placementTermForm.getStartTime()).getTime()) {
				errors.rejectValue("endDate",
					"placementTermForm.dateRange.endBeforeStartDate");
			}
		}
		if (placementTermForm.getAllowStatusFields()) {
			if (placementTermForm.getStatus() == null) {
				errors.rejectValue("status", "placementTermForm.status.empty");
			}
			if (!PlacementStatus.PLACED.equals(placementTermForm.getStatus())
					&& placementTermForm.getStatus() != null
					&& placementTermForm.getStatusDate() == null) {
				errors.rejectValue(
						"statusDate", "placementTermForm.statusDate.empty");
			}
			if (!PlacementStatus.PLACED.equals(placementTermForm.getStatus())
					&& placementTermForm.getStatus() != null
					&& placementTermForm.getStatusTime() == null) {
				errors.rejectValue(
						"statusTime", "placementTermForm.statusTime.empty");
			}
			if (placementTermForm.getReturned() != null 
					&& placementTermForm.getReturned()) {
				if (placementTermForm.getStatusReturnedDate() == null) {
					errors.rejectValue("statusReturnedDate", 
							"placementTermForm.statusReturnedDate.empty");
				}
				if (placementTermForm.getStatusReturnedTime() == null) {
					errors.rejectValue("statusReturnedTime", 
							"placementTermForm.statusReturnedTime.empty");
				}
			}
			if (placementTermForm.getStatusDate() != null
					&& placementTermForm.getStatusReturnedDate() != null) {
				if (DateManipulator.getDateAtTimeOfDay(
							placementTermForm.getStatusReturnedDate(),
							placementTermForm.getStatusReturnedTime()).getTime()
						< DateManipulator.getDateAtTimeOfDay(
								placementTermForm.getStatusDate(),
								placementTermForm.getStatusTime()).getTime()) {
					errors.rejectValue("statusReturnedDate",
						"placementTermForm.statusDateRange.endBeforeStartDate");
				}
			}
			if (!PlacementStatus.PLACED.equals(placementTermForm.getStatus()) 
					&& placementTermForm.getStatusDate() != null) {
				DateRange dateRange = new DateRange();
				dateRange.setStartDate(DateManipulator.getDateAtTimeOfDay(
							placementTermForm.getStartDate(),
							placementTermForm.getStartTime()));
				if (placementTermForm.getEndDate() != null) {
					dateRange.setEndDate(DateManipulator.getDateAtTimeOfDay(
							placementTermForm.getEndDate(),
							placementTermForm.getEndTime()));
				}
				DateRange statusDateRange = new DateRange();
				statusDateRange.setStartDate(DateManipulator.getDateAtTimeOfDay(
								placementTermForm.getStatusDate(),
								placementTermForm.getStatusTime()));
				if (placementTermForm.getStatusReturnedDate() != null 
						&& DateManipulator.getDateAtTimeOfDay(
						placementTermForm.getStatusReturnedDate(),
						placementTermForm.getStatusReturnedTime()).getTime()
						> DateManipulator.getDateAtTimeOfDay(
							placementTermForm.getStatusDate(),
							placementTermForm.getStatusTime()).getTime()) {
					statusDateRange.setEndDate(
							DateManipulator.getDateAtTimeOfDay(
							placementTermForm.getStatusReturnedDate(),
							placementTermForm.getStatusReturnedTime()));
				}
				if (!statusDateRange.occursWithin(dateRange)) {
					errors.rejectValue("statusDate", 
							"placementTerm.statusDateRangeOutOfBounds");
				}
			}
		}
		if (placementTermForm.getEndDate() != null
				&& placementTermForm.getEndTime() == null) {
			errors.rejectValue("endTime", "endTime.empty");
		}
		if (placementTermForm.getEndDate() == null
				&& placementTermForm.getEndTime() != null) {
			errors.rejectValue("endDate", "endDate.empty");
		}
		if (this.isDateTimePairEqual(
				placementTermForm.getStartDate(),
				placementTermForm.getStartTime(),
				placementTermForm.getEndDate(),
				placementTermForm.getEndTime())) {
			errors.rejectValue("endDate", "endDateTime.equalsStartDateTime");
		}
		int noteItemIndex = 0;
		if (placementTermForm.getNoteItems() != null) {
			for (PlacementTermNoteItem noteItem
					: placementTermForm.getNoteItems()) {
				if (PlacementTermNoteItemOperation.CREATE
							.equals(noteItem.getOperation())
						|| PlacementTermNoteItemOperation.UPDATE
							.equals(noteItem.getOperation())) {
					this.placementTermNoteFieldsValidatorDelegate
						.validate(noteItem.getFields(),
								"noteItems[" + noteItemIndex + "].fields",
								errors);
				}
				noteItemIndex++;
			}
		}
	}
	
	// Returns whether date/time pair is equal
	private boolean isDateTimePairEqual(
			final Date date, final Date time,
			final Date otherDate, final Date otherTime) {
		if (date != null && time != null) {
			if (otherDate != null & otherTime != null) {
				if (DateManipulator.getDateAtTimeOfDay(date, time).getTime()
						== DateManipulator.getDateAtTimeOfDay(
								otherDate, otherTime).getTime()) {
					return true;
				}
			} else {
				return false;
			}
		} else if (otherDate == null && otherTime == null) {
			return true;
		}
		return false;
	}
}