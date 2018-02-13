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
package omis.parolereview.web.validator;

import java.util.HashSet;
import java.util.List;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import omis.parolereview.web.form.DocumentTagItem;
import omis.parolereview.web.form.ParoleReviewDocumentAssociationItem;
import omis.parolereview.web.form.ParoleReviewForm;
import omis.parolereview.web.form.ParoleReviewItemOperation;
import omis.parolereview.web.form.ParoleReviewNoteItem;

/**
 * Validator for parole reviews.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Jan 29, 2018)
 * @since OMIS 3.0
 */
public class ParoleReviewFormValidator implements Validator {

	/* Message Keys. */
	
	private static final String TITLE_REQUIRED_MSG_KEY = "title.required";
	
	private static final String FILE_DATE_REQUIRED_MSG_KEY =
			"fileDate.required";
	
	private static final String DOCUMENT_REQUIRED_MSG_KEY = "document.required";
	
	private static final String NAME_REQUIRED_MSG = "tag.name.empty";
	
	private static final String NAME_DUPLICATED_MSG = "tag.name.duplicate";
	
	/* Field Names. */
	
	private static final String NAME_FIELD_NAME = "name";
	
	private static final String DOCUMENT_TAG_ITEM_FIELD_NAME = 
			"paroleReviewDocumentAssociationItems[%d]"
											+ ".documentTagItems[%d]";
	
	/**
	 * Instantiates a default parole review form validator.
	 */
	public ParoleReviewFormValidator() {
		// Default instantiation
	} 
	
	/** {@inheritDoc} */
	@Override
	public boolean supports(final Class<?> clazz) {
		return ParoleReviewForm.class.isAssignableFrom(clazz);
	}

	/** {@inheritDoc} */
	@Override
	public void validate(final Object target, final Errors errors) {
		ParoleReviewForm form = (ParoleReviewForm) target;
		
		ValidationUtils.rejectIfEmpty(errors, "staffAssignment", 
				"paroleReview.staffAssignment.empty");
		ValidationUtils.rejectIfEmpty(errors, "date", 
				"paroleReview.date.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "text", 
				"paroleReview.text.empty");
		ValidationUtils.rejectIfEmpty(errors, "endorsement", 
				"paroleReview.endorsement.empty");
		ValidationUtils.rejectIfEmpty(errors, "staffRole", 
				"paroleReview.staffRole.empty");
		if (form.getParoleReviewNoteItems() != null) {
			int i = 0;
			for (ParoleReviewNoteItem item : form.getParoleReviewNoteItems()) {
				if (ParoleReviewItemOperation.CREATE.equals(item.getOperation()) 
						|| ParoleReviewItemOperation.UPDATE.equals(
								item.getOperation())) {
					ValidationUtils.rejectIfEmpty(errors, 
							"paroleReviewNoteItems[" + i + "].date", 
							"paroleReview.paroleReviewNote.date.empty");
					ValidationUtils.rejectIfEmpty(errors, 
							"paroleReviewNoteItems[" + i + "].value", 
							"paroleReview.paroleReviewNote.value.empty");
				}
			}
		}
		if(form.getParoleReviewDocumentAssociationItems() != null){
			int i = 0;
			for(ParoleReviewDocumentAssociationItem item :
					form.getParoleReviewDocumentAssociationItems()){
				if(ParoleReviewItemOperation.CREATE.equals(
						item.getItemOperation()) ||
					ParoleReviewItemOperation.UPDATE.equals(
						item.getItemOperation())){
					
					ValidationUtils.rejectIfEmpty(errors,
							"paroleReviewDocumentAssociationItems["+i+"].title",
							TITLE_REQUIRED_MSG_KEY);
					ValidationUtils.rejectIfEmpty(errors,
							"paroleReviewDocumentAssociationItems["+i+"].fileDate",
							FILE_DATE_REQUIRED_MSG_KEY);
					ValidationUtils.rejectIfEmpty(errors,
							"paroleReviewDocumentAssociationItems["+i+"].data", 
							DOCUMENT_REQUIRED_MSG_KEY);
					
					if(item.getDocumentTagItems() != null){
						List<DocumentTagItem> documentTags = 
								item.getDocumentTagItems();
						HashSet<String> documentTagNames = 
								new HashSet<String>(documentTags.size());
						for (int x = 0; x < documentTags.size(); x++) {
							DocumentTagItem documentTagItem = documentTags.get(x);
							ParoleReviewItemOperation documentTagOperation = 
									documentTagItem.getItemOperation();
							if (documentTagOperation ==
									ParoleReviewItemOperation.CREATE 
									|| documentTagOperation ==
									ParoleReviewItemOperation.UPDATE){
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