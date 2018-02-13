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
package omis.medicalreview.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import omis.document.web.form.DocumentTagItem;
import omis.document.web.form.DocumentTagOperation;
import omis.medicalreview.web.form.MedicalReviewDocumentAssociationItem;
import omis.medicalreview.web.form.MedicalReviewForm;
import omis.medicalreview.web.form.MedicalReviewItemOperation;
import omis.medicalreview.web.form.MedicalReviewNoteItem;

/**
 * Medical Review Form Validator.
 * 
 *@author Annie Wahl 
 *@version 0.1.0 (Jan 31, 2018)
 *@since OMIS 3.0
 *
 */
public class MedicalReviewFormValidator implements Validator {
	
	private static final String DATE_REQUIRED_MSG_KEY = "date.empty";
	
	private static final String DESCRIPTION_REQUIRED_MSG_KEY =
			"description.empty";
	
	private static final String CLASSIFICATION_REQUIRED_MSG_KEY =
			"medicalReview.classification.empty";
	
	private static final String TITLE_REQUIRED_MSG_KEY = "title.required";
	
	private static final String FILE_DATE_REQUIRED_MSG_KEY =
			"fileDate.required";
	
	private static final String DOCUMENT_REQUIRED_MSG_KEY = "document.required";
	
	private static final String TAG_REQUIRED_MSG_KEY = "tag.required";
	
	/**{@inheritDoc} */
	@Override
	public boolean supports(final Class<?> clazz) {
		return MedicalReviewForm.class.isAssignableFrom(clazz);
	}

	/**{@inheritDoc} */
	@Override
	public void validate(final Object target, final Errors errors) {
		MedicalReviewForm form = (MedicalReviewForm) target;
		
		ValidationUtils.rejectIfEmpty(errors, "date",
				DATE_REQUIRED_MSG_KEY);
		ValidationUtils.rejectIfEmpty(errors, "text",
				DESCRIPTION_REQUIRED_MSG_KEY);
		ValidationUtils.rejectIfEmpty(errors, "healthClassification",
				CLASSIFICATION_REQUIRED_MSG_KEY);
		
		int i = 0;
		for (MedicalReviewNoteItem item
				: form.getMedicalReviewNoteItems()) {
			if (MedicalReviewItemOperation.CREATE.equals(
						item.getItemOperation())
					|| MedicalReviewItemOperation.UPDATE.equals(
						item.getItemOperation())) {
				if (item.getDate() == null) {
					errors.rejectValue("medicalReviewNoteItems[" + i 
							+ "].date", DATE_REQUIRED_MSG_KEY);
				}
				if (item.getDate() == null) {
					errors.rejectValue(
							"medicalReviewNoteItems[" + i + "]"
									+ ".description",
							DESCRIPTION_REQUIRED_MSG_KEY);
				}
			}
			i++;
		}
		
		i = 0;
		for (MedicalReviewDocumentAssociationItem item 
				: form.getMedicalReviewDocumentAssociationItems()) {
			if (MedicalReviewItemOperation.CREATE
					.equals(item.getItemOperation())
					|| MedicalReviewItemOperation.UPDATE
					.equals(item.getItemOperation())) {
				
				ValidationUtils.rejectIfEmpty(errors,
						"medicalReviewDocumentAssociationItems[" + i + "]"
								+ ".title",
						TITLE_REQUIRED_MSG_KEY);
				ValidationUtils.rejectIfEmpty(errors,
						"medicalReviewDocumentAssociationItems[" + i + "].date",
						FILE_DATE_REQUIRED_MSG_KEY);
				ValidationUtils.rejectIfEmpty(errors,
						"medicalReviewDocumentAssociationItems[" + i + "]"
								+ ".data", 
						DOCUMENT_REQUIRED_MSG_KEY);
				
				if (item.getTagItems() != null) {
					int j = 0;
					for (DocumentTagItem tagItem : item.getTagItems()) {
						if (DocumentTagOperation.CREATE.equals(
								tagItem.getDocumentTagOperation())
								|| DocumentTagOperation.UPDATE.equals(
								tagItem.getDocumentTagOperation())) {
							ValidationUtils.rejectIfEmpty(errors,
									"medicalReviewDocumentAssociationItems["
											+ i + "].tagItems[" + j + "].name",
									TAG_REQUIRED_MSG_KEY);
						}
						j++;
					}
				}
			}
			i++;
		}
	}
}
