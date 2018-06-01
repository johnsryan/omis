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

import omis.hearinganalysis.web.form.HearingAnalysisForm;
import omis.hearinganalysis.web.form.HearingAnalysisNoteItem;
import omis.hearinganalysis.web.form.HearingAnalysisNoteItemOperation;

/**
 * Validator for hearing analysis.
 * 
 * @author Josh Divine
 * @author Annie Wahl
 * @version 0.1.1 (May 29, 2018)
 * @since OMIS 3.0
 */
public class HearingAnalysisFormValidator implements Validator {

	/**
	 * Instantiates a default hearing analysis form validator.
	 */
	public HearingAnalysisFormValidator() {
		// Default instantiation
	} 
	
	/** {@inheritDoc} */
	@Override
	public boolean supports(Class<?> clazz) {
		return HearingAnalysisForm.class.isAssignableFrom(clazz);
	}

	/** {@inheritDoc} */
	@Override
	public void validate(Object target, Errors errors) {
		HearingAnalysisForm form = (HearingAnalysisForm) target;
		
		if (form.getCategory() == null) {
			errors.rejectValue("category", "hearingAnalysis.category.empty");
		}
		if (form.getBoardItinerary() == null) {
			errors.rejectValue("boardItinerary",
					"hearingAnalysis.boardItinerary.empty");
		}
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
