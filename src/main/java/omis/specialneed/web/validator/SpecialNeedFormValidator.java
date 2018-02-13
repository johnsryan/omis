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
package omis.specialneed.web.validator;

import omis.specialneed.web.form.SpecialNeedAssociableDocumentItemOperation;
import omis.specialneed.web.form.SpecialNeedForm;
import omis.specialneed.web.form.SpecialNeedNoteItem;
import omis.specialneed.web.form.SpecialNeedNoteItemOperation;
import omis.web.validator.StringLengthChecks;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Special Need Form Validator.
 * 
 * @author Joel Norris
 * @author Sheronda Vaughn
 * @author Josh Divine
 * @version 0.1.2 (Feb 12, 2018)
 * @since OMIS 3.0
 */
public class SpecialNeedFormValidator implements Validator {
	
	private StringLengthChecks stringLengthChecks;
	
	public SpecialNeedFormValidator(
			final StringLengthChecks stringLengthChecks) {
		this.stringLengthChecks = stringLengthChecks;
	}

	/** {@inheritDoc} */
	@Override
	public boolean supports(final Class<?> clazz) {
		return SpecialNeedForm.class.isAssignableFrom(clazz);
	}
	
	/** {@inheritDoc} */
	@Override
	public void validate(final Object target, final Errors errors) {
		SpecialNeedForm form = (SpecialNeedForm) target;
		
		if (form.getCategory() == null) {
			errors.rejectValue("category", "specialNeed.category.empty");
		}
		if (form.getStartDate() == null) {
			errors.rejectValue("startDate", 
					"specialNeed.startDate.empty");
		}
		if (form.getSource() == null) {
			errors.rejectValue("source", "specialNeed.source.empty");
		}
		if (form.getStartDate() != null 
				&& form.getEndDate() != null) {
			if (form.getStartDate().getTime() 
					> form.getEndDate().getTime()) {
				errors.rejectValue("startDate", 
						"specialNeed.startDate.afterEndDate");
			}
		}
		if (form.getComment() == null || form.getComment().isEmpty()) {
			errors.rejectValue("comment", "specialNeed.comment.empty");
		} else {
			this.stringLengthChecks.getHumongousCheck()
			.check("comment", form.getComment(), errors);
		}
		if (form.getSource() != null || !form.getComment().isEmpty()) {
			this.stringLengthChecks.getHumongousCheck()
			.check("comment", form.getSourceComment(), errors);
		}
		if (form.getSpecialNeedNotes() != null) {
			int noteCount = 0;
			for (SpecialNeedNoteItem item : form
					.getSpecialNeedNotes()) {
				if (item.getOperation() != null 
						&& !(SpecialNeedNoteItemOperation.REMOVE
								.equals(item.getOperation()))) {
					if (item.getValue().isEmpty()) {
						errors.rejectValue(
								"specialNeedNotes[" + noteCount + "].value",
								"specialNeedNote.value.empty");
					}
					if (item.getDate() == null) {
						errors.rejectValue(
								"specialNeedNotes[" + noteCount + "].date", 
								"specialNeedNote.date.empty");
					}					
				}
				noteCount ++;
			}			
		}
		if ((SpecialNeedAssociableDocumentItemOperation.CREATE.equals(
				form.getDocumentItemOperation()) && 
				(form.getDocumentCategory() != null ||
				(form.getTitle() != null && !form.getTitle().isEmpty()) ||
				form.getDate() != null || 
				(form.getData() != null && form.getData().length > 0))) ||
				SpecialNeedAssociableDocumentItemOperation.UPDATE.equals(
						form.getDocumentItemOperation())) {
			if (form.getDocumentCategory() == null) {
				errors.rejectValue("documentCategory",
						"specialNeedDocument.category.empty");
			}
			if (form.getTitle() != null && form.getTitle().isEmpty()) {
				errors.rejectValue("title",
						"specialNeedDocument.title.empty");
			}
			if (form.getDate() == null) {
				errors.rejectValue("date",
						"specialNeedDocument.date.empty");
			}
			if (SpecialNeedAssociableDocumentItemOperation.CREATE.equals(
					form.getDocumentItemOperation()) && 
					form.getData() != null && form.getData().length == 0) {
				errors.rejectValue("data",
						"specialNeedDocument.data.empty");
			}
		}
	}
}