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
package omis.hearinganalysis.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import omis.hearinganalysis.web.form.HearingAnalysisHomeForm;
import omis.hearinganalysis.web.form.HearingAnalysisNoteItem;
import omis.hearinganalysis.web.form.HearingAnalysisNoteItemOperation;

/**
 * Validator for hearing analysis home.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 20, 2018)
 * @since OMIS 3.0
 */
public class HearingAnalysisHomeFormValidator implements Validator {

	/**
	 * Instantiates a default hearing analysis form validator.
	 */
	public HearingAnalysisHomeFormValidator() {
		// Default instantiation
	} 
	
	/** {@inheritDoc} */
	@Override
	public boolean supports(Class<?> clazz) {
		return HearingAnalysisHomeForm.class.isAssignableFrom(clazz);
	}

	/** {@inheritDoc} */
	@Override
	public void validate(Object target, Errors errors) {
		HearingAnalysisHomeForm form = (HearingAnalysisHomeForm) target;
		
		if (form.getHearingAnalysisNoteItems() != null && 
				form.getHearingAnalysisNoteItems().size() > 0) {
			for (int index = 0; index < 
					form.getHearingAnalysisNoteItems().size(); index++) {
				HearingAnalysisNoteItem noteItem = form
						.getHearingAnalysisNoteItems().get(index);
				if (noteItem.getOperation() != null && 
						!HearingAnalysisNoteItemOperation.REMOVE.equals(
								noteItem.getOperation())) {
					if (noteItem.getDate() == null) {
						errors.rejectValue("hearingAnalysisNoteItems[" + index +
								"].date", "hearingAnalysis.note.date.empty");
					}
					if (noteItem.getValue() == null || 
							noteItem.getValue().isEmpty()) {
						errors.rejectValue("hearingAnalysisNoteItems[" + index + 
								"].value", "hearingAnalysis.note.value.empty");
					}
				}
			}
		}
	}
}