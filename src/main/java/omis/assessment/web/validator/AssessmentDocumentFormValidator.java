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
package omis.assessment.web.validator;

import java.util.List;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import omis.assessment.web.form.AssessmentDocumentForm;
import omis.document.validator.DocumentTagItemValidator;
import omis.document.web.form.DocumentTagItem;

/**
 * Assessment Document Form Validator.
 * 
 *@author Annie Wahl 
 *@version 0.1.0 (Mar 14, 2018)
 *@since OMIS 3.0
 *
 */
public class AssessmentDocumentFormValidator implements Validator {
	
	private static final String DATE_REQUIRED_MSG = "date.empty";
	
	private static final String TITLE_REQUIRED_MSG = "document.title.empty";
	
	private static final String DOCUMENT_REQUIRED_MSG = "document.required";
	
	private final DocumentTagItemValidator documentTagItemValidator =
			new DocumentTagItemValidator();
	
	
	/**{@inheritDoc} */
	@Override
	public boolean supports(final Class<?> clazz) {
		return AssessmentDocumentForm.class.isAssignableFrom(clazz);
	}

	/**{@inheritDoc} */
	@Override
	public void validate(final Object target, final Errors errors) {
		ValidationUtils.rejectIfEmpty(errors, "date", 
				DATE_REQUIRED_MSG);
		ValidationUtils.rejectIfEmpty(errors, "title", 
				TITLE_REQUIRED_MSG);
		ValidationUtils.rejectIfEmpty(errors, "data", 
				DOCUMENT_REQUIRED_MSG);
		AssessmentDocumentForm form = (AssessmentDocumentForm) target;
		List<DocumentTagItem> documentTagItems = form.getDocumentTagItems();
		if (documentTagItems != null) {
			this.documentTagItemValidator.validate(documentTagItems, errors);
		}
	}

}
