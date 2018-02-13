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
package omis.offenderrelationship.web.validator;

import org.springframework.validation.Errors;

import omis.offenderrelationship.web.form.OffenderRelationshipNoteFields;

/**
 * Validator delegate for offender relationship note fields.
 * 
 * @author Stephen Abson
 * @version 0.0.1 (Jan 10, 2018)
 * @since OMIS 3.0
 */
public class OffenderRelationshipNoteFieldsValidatorDelegate {

	/**
	 * Instantiates validator delegate for offender relationship note fields.
	 */
	public OffenderRelationshipNoteFieldsValidatorDelegate() {
		// Default instantiation
	}
	
	/**
	 * Validates fields.
	 * 
	 * @param fieldsPropertyName name of fields property
	 * @param fields fields
	 * @param errors errors
	 */
	public void validate(
			final String fieldsPropertyName,
			final OffenderRelationshipNoteFields fields,
			final Errors errors) {
		if (fields.getCategory() == null) {
			errors.rejectValue(fieldsPropertyName + ".category",
					"offenderRelationshipNote.category.empty");
		}
		if (fields.getDate() == null) {
			errors.rejectValue(fieldsPropertyName + ".date",
					"offenderRelationshipNote.date.empty");
		}
		if (fields.getValue() == null || fields.getValue().isEmpty()) {
			errors.rejectValue(fieldsPropertyName + ".value",
					"offenderRelationshipNote.value.empty");
		}
	}
}