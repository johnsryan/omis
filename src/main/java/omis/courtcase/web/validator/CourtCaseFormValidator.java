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
package omis.courtcase.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import omis.courtcase.web.form.ChargeItem;
import omis.courtcase.web.form.ChargeItemOperation;
import omis.courtcase.web.form.CourtCaseForm;
import omis.courtcase.web.form.CourtCaseNoteItem;
import omis.courtcase.web.form.CourtCaseNoteItemOperation;
import omis.docket.web.validator.delegate.DocketFieldsValidatorDelegate;

/**
 * Validator for court case form.
 * 
 * @author Stephen Abson
 * @author Joel Norris
 * @author Josh Divine
 * @version 0.1.3 (Feb 6, 2018)
 * @since OMIS 3.0
 */
public class CourtCaseFormValidator
		implements Validator {

	private static final String DOCKET_FIELDS_PROPERTY_NAME = "docketFields";
	
	private final DocketFieldsValidatorDelegate docketFieldsValidatorDelegate;
	
	/** Instantiates a validator for court cases. */
	public CourtCaseFormValidator(
			final DocketFieldsValidatorDelegate docketFieldsValidatorDelegate) {
		this.docketFieldsValidatorDelegate = docketFieldsValidatorDelegate;
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean supports(final Class<?> clazz) {
		return CourtCaseForm.class.isAssignableFrom(clazz);
	}

	/** {@inheritDoc} */
	@Override
	public void validate(final Object target, final Errors errors) {
		CourtCaseForm courtCaseForm = (CourtCaseForm) target;
		if (courtCaseForm.getAllowDocket() != null
				&& courtCaseForm.getAllowDocket()
				&& courtCaseForm.getExistingDocket() == null) {
			this.docketFieldsValidatorDelegate.validate(
					courtCaseForm.getDocketFields(),
					DOCKET_FIELDS_PROPERTY_NAME,
					errors);
		}
		if (courtCaseForm.getJudge() == null) {
			errors.rejectValue("judge", "courtCase.judge.empty");
		}
		if (courtCaseForm.getCharges() == null 
				|| courtCaseForm.getCharges().size() < 1) {
			errors.rejectValue("charges", "courtCase.charges.empty");
		} else {
			Integer charges = 0;
			for (int index = 0; index < courtCaseForm.getCharges().size(); 
					index++) {
				ChargeItem chargeItem = courtCaseForm.getCharges().get(index);
				if (chargeItem.getOperation() != null) {
					if (ChargeItemOperation.CREATE.equals(
							chargeItem.getOperation()) || 
							ChargeItemOperation.EDIT.equals(
									chargeItem.getOperation())) {
						if (chargeItem.getDate() == null) {
							errors.rejectValue("charges[" + index + "].date", 
									"courtCase.charge.date.empty");
						}
						if (chargeItem.getFileDate() == null) {
							errors.rejectValue("charges[" + index + 
									"].fileDate", 
									"courtCase.charge.fileDate.empty");
						}
						if (chargeItem.getOffense() == null) {
							errors.rejectValue("charges[" + index + "].offense", 
									"courtCase.charge.offense.empty");
						}
						if (chargeItem.getCount() == null) {
							errors.rejectValue("charges[" + index + "].count", 
									"courtCase.charge.count.empty");
						}
						charges++;
					}
				}
			}
			if (charges == 0) {
				errors.rejectValue("charges", "courtCase.charges.empty");
			}
		}
		if (courtCaseForm.getNoteItems() != null) {
			for (int index = 0; index < courtCaseForm.getNoteItems().size(); 
					index++) {
				CourtCaseNoteItem noteItem 
					= courtCaseForm.getNoteItems().get(index);
				if (noteItem.getOperation() != null) {
					if (CourtCaseNoteItemOperation.CREATE.equals(
							noteItem.getOperation()) || 
							CourtCaseNoteItemOperation.EDIT.equals(
									noteItem.getOperation())) {
						if (noteItem.getDate() == null) {
							errors.rejectValue("noteItems[" + index + "].date", 
									"courtCase.note.date.empty");
						}
						if (noteItem.getValue() == null 
								|| noteItem.getValue().isEmpty()) {
							errors.rejectValue("noteItems[" + index + "].value", 
									"courtCase.note.value.empty");
						}
					}
				}
			}
		}
	}
}