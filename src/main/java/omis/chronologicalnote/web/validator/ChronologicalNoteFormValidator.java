/*
 *  OMIS - Offender Management Information System
 *  Copyright (C) 2011 - 2017 State of Montana
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package omis.chronologicalnote.web.validator;

import java.util.Date;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import omis.chronologicalnote.web.form.ChronologicalNoteCategoryItem;
import omis.chronologicalnote.web.form.ChronologicalNoteCategoryItemOperation;
import omis.chronologicalnote.web.form.ChronologicalNoteForm;
import omis.web.validator.StringLengthChecks;

/**
 * Chronological note from validator.
 * 
 * @author Joel Norris
 * @author Sheronda Vaughn
 * @version 0.1.0 (February 7, 2018)
 * @since OMIS 3.0
 */
public class ChronologicalNoteFormValidator implements Validator {

	/* Helpers. */
	
	/*
	 * TODO: Use String length checks object once database unique key issue 
	 * for chronological notes
	 * is resolved. - JN
	 */
	@SuppressWarnings("unused")
	private final StringLengthChecks stringLengthChecks;
	
	/* Manual logic value assignments */
	
	private final int NARRATIVE_MAX_LENGTH = 6000;
	
	/**
	 * Instantiates a chronological note form validator with the specified 
	 * string length checks.
	 * 
	 * @param stringLengthChecks string length checks
	 */
	public ChronologicalNoteFormValidator(final StringLengthChecks 
			stringLengthChecks) {
		this.stringLengthChecks = stringLengthChecks;
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean supports(final Class<?> clazz) {
		return ChronologicalNoteForm.class.isAssignableFrom(clazz);
	}

	/** {@inheritDoc} */
	@Override
	public void validate(final Object target, final Errors errors) {
		ChronologicalNoteForm form = (ChronologicalNoteForm) target;
		if (form.getTitle() == null || form.getTitle().isEmpty()) {
			errors.rejectValue("title", "chronologicalNote.title.empty");
		} 		
		if (form.getNarrative() == null || form.getNarrative().isEmpty()) {
			errors.rejectValue("narrative", 
					"chronologicalNote.narrative.empty");
		} else {		
			//FIXME: Use document check once data tier issue is resolved.
			if (form.getNarrative().length() > NARRATIVE_MAX_LENGTH) {
				errors.rejectValue("narrative", 
						"chronologicalNote.narrative.lengthExceeded");
			}
//			this.stringLengthChecks.getDocumentCheck().check(
//					"narrative", form.getNarrative(), errors);
		}
		if (form.getDate() == null) {
			errors.rejectValue("date", "chronologicalNote.date.empty");
		} else {
			if (form.getDate().getTime() > new Date().getTime()) {
				errors.rejectValue("date", "chronologicalNote.date.futureDate");
			}
		}
		if (form.getDateTime() == null) {
			errors.rejectValue("dateTime", "chronologicalNote.date.time.empty");
		} 
		boolean categorized = false;
		for (ChronologicalNoteCategoryItem item : form.getItems()) {
			if ((item.getAssociated() && !ChronologicalNoteCategoryItemOperation
					.DISSOCIATE.equals(item.getOperation())) 
					|| ChronologicalNoteCategoryItemOperation.ASSOCIATE.equals(
							item.getOperation())) {
				categorized = true;
			}
		}
		if (!categorized) {
			errors.rejectValue("items", "chronologicalNote.items.unassociated");
		}
		
	}
}