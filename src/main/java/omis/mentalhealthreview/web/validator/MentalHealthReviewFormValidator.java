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
package omis.mentalhealthreview.web.validator;

import java.util.HashSet;
import java.util.List;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import omis.mentalhealthreview.web.form.DocumentTagItem;
import omis.mentalhealthreview.web.form.MentalHealthNoteItem;
import omis.mentalhealthreview.web.form.MentalHealthReviewDocumentAssociationItem;
import omis.mentalhealthreview.web.form.MentalHealthReviewForm;
import omis.mentalhealthreview.web.form.MentalHealthReviewItemOperation;

/**
 * Validator for mental health reviews.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 1, 2018)
 * @since OMIS 3.0
 */
public class MentalHealthReviewFormValidator implements Validator {

	/* Message Keys. */
	
	private static final String DESCRIPTION_REQUIRED_MSG_KEY =
			"description.required";
	
	private static final String DATE_REQUIRED_MSG_KEY = "date.required";
	
	private static final String TITLE_REQUIRED_MSG_KEY = "title.required";
	
	private static final String FILE_DATE_REQUIRED_MSG_KEY =
			"fileDate.required";
	
	private static final String DOCUMENT_REQUIRED_MSG_KEY = "document.required";
	
	private static final String NAME_REQUIRED_MSG = "tag.name.empty";
	
	private static final String NAME_DUPLICATED_MSG = "tag.name.duplicate";
	
	/* Field Names. */
	
	private static final String NAME_FIELD_NAME = "name";
	
	private static final String DOCUMENT_TAG_ITEM_FIELD_NAME = 
			"mentalHealthReviewDocumentAssociationItems[%d]"
											+ ".documentTagItems[%d]";
	
	/**
	 * Instantiates a default mental health review form validator.
	 */
	public MentalHealthReviewFormValidator() {
		// Default instantiation
	} 
	
	/** {@inheritDoc} */
	@Override
	public boolean supports(final Class<?> clazz) {
		return MentalHealthReviewForm.class.isAssignableFrom(clazz);
	}

	/** {@inheritDoc} */
	@Override
	public void validate(final Object target, final Errors errors) {
		MentalHealthReviewForm form = (MentalHealthReviewForm) target;
		
		if (form.getDate() == null) {
			errors.rejectValue("date", "mentalHealthReview.date.empty");
		}
		if (form.getText() == null || form.getText().isEmpty()) {
			errors.rejectValue("text", "mentalHealthReview.text.empty");
		}
		if (form.getMentalHealthNoteItems() != null) {
			int i = 0;
			for (MentalHealthNoteItem item : form.getMentalHealthNoteItems()) {
				if(MentalHealthReviewItemOperation.UPDATE.equals(
						item.getItemOperation())
				|| MentalHealthReviewItemOperation.CREATE.equals(
						item.getItemOperation())){
					ValidationUtils.rejectIfEmptyOrWhitespace(errors,
						"mentalHealthNoteItems[" + i + "].description",
						DESCRIPTION_REQUIRED_MSG_KEY);
					ValidationUtils.rejectIfEmpty(errors,
						"mentalHealthNoteItems[" + i + "].date",
						DATE_REQUIRED_MSG_KEY);
				}
				
				i++;
			}
		}
		if(form.getMentalHealthReviewDocumentAssociationItems() != null){
			int i = 0;
			for(MentalHealthReviewDocumentAssociationItem item :
					form.getMentalHealthReviewDocumentAssociationItems()){
				if(MentalHealthReviewItemOperation.CREATE.equals(
						item.getItemOperation()) ||
					MentalHealthReviewItemOperation.UPDATE.equals(
						item.getItemOperation())){
					
					ValidationUtils.rejectIfEmpty(errors,
						"mentalHealthReviewDocumentAssociationItems"
						+ "["+i+"].title",
							TITLE_REQUIRED_MSG_KEY);
					ValidationUtils.rejectIfEmpty(errors,
						"mentalHealthReviewDocumentAssociationItems"
						+ "["+i+"].fileDate",
							FILE_DATE_REQUIRED_MSG_KEY);
					ValidationUtils.rejectIfEmpty(errors,
							"mentalHealthReviewDocumentAssociationItems"
							+ "["+i+"].data", 
							DOCUMENT_REQUIRED_MSG_KEY);
					
					if(item.getDocumentTagItems() != null){
						List<DocumentTagItem> documentTags =
								item.getDocumentTagItems();
						HashSet<String> documentTagNames = 
								new HashSet<String>(documentTags.size());
						for (int x = 0; x < documentTags.size(); x++) {
							DocumentTagItem documentTagItem = documentTags.get(x);
							MentalHealthReviewItemOperation
								documentTagOperation = documentTagItem
									.getItemOperation();
							if (documentTagOperation ==
									MentalHealthReviewItemOperation.CREATE 
									|| documentTagOperation ==
									MentalHealthReviewItemOperation.UPDATE){
								errors.pushNestedPath(String.format(
										DOCUMENT_TAG_ITEM_FIELD_NAME, i, x));
								if (documentTagNames.add(documentTagItem
										.getName())) {
									ValidationUtils.rejectIfEmpty(
											errors, NAME_FIELD_NAME, 
											NAME_REQUIRED_MSG);
								}
								else {
									errors.rejectValue(NAME_FIELD_NAME, 
											NAME_DUPLICATED_MSG);
								}
								errors.popNestedPath();
							}
						}
					}
				}
				i++;
			}
		}
	}
}