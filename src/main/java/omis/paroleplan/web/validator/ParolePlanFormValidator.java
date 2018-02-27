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
package omis.paroleplan.web.validator;

import java.util.HashSet;
import java.util.List;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import omis.paroleplan.web.form.DocumentTagItem;
import omis.paroleplan.web.form.ParolePlanDocumentAssociationItem;
import omis.paroleplan.web.form.ParolePlanForm;
import omis.paroleplan.web.form.ParolePlanItemOperation;
import omis.paroleplan.web.form.ParolePlanNoteItem;

/**
 * Validator for parole plans.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 13, 2018)
 * @since OMIS 3.0
 */
public class ParolePlanFormValidator implements Validator {

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
			"parolePlanDocumentAssociationItems[%d]"
											+ ".documentTagItems[%d]";
	
	/**
	 * Instantiates a default parole plan form validator.
	 */
	public ParolePlanFormValidator() {
		// Default instantiation
	} 
	
	/** {@inheritDoc} */
	@Override
	public boolean supports(final Class<?> clazz) {
		return ParolePlanForm.class.isAssignableFrom(clazz);
	}

	/** {@inheritDoc} */
	@Override
	public void validate(final Object target, final Errors errors) {
		ParolePlanForm form = (ParolePlanForm) target;
		
		ValidationUtils.rejectIfEmpty(errors, "evaluator", 
				"parolePlan.evaluator.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, 
				"evaluationDescription", 
				"parolePlan.evaluationDescription.empty");
		if (form.getParolePlanNoteItems() != null) {
			int i = 0;
			for (ParolePlanNoteItem item : form.getParolePlanNoteItems()) {
				if (ParolePlanItemOperation.CREATE.equals(item.getOperation()) 
						|| ParolePlanItemOperation.UPDATE.equals(
								item.getOperation())) {
					ValidationUtils.rejectIfEmpty(errors, 
							"parolePlanNoteItems[" + i + "].date", 
							"parolePlan.parolePlanNote.date.empty");
					ValidationUtils.rejectIfEmpty(errors, 
							"parolePlanNoteItems[" + i + "].value", 
							"parolePlan.parolePlanNote.value.empty");
				}
			}
		}
		if(form.getParolePlanDocumentAssociationItems() != null){
			int i = 0;
			for(ParolePlanDocumentAssociationItem item :
					form.getParolePlanDocumentAssociationItems()){
				if(ParolePlanItemOperation.CREATE.equals(
						item.getItemOperation()) ||
					ParolePlanItemOperation.UPDATE.equals(
						item.getItemOperation())){
					
					ValidationUtils.rejectIfEmpty(errors,
							"parolePlanDocumentAssociationItems["+i+"].title",
							TITLE_REQUIRED_MSG_KEY);
					ValidationUtils.rejectIfEmpty(errors,
							"parolePlanDocumentAssociationItems["+i+"].fileDate",
							FILE_DATE_REQUIRED_MSG_KEY);
					ValidationUtils.rejectIfEmpty(errors,
							"parolePlanDocumentAssociationItems["+i+"].data", 
							DOCUMENT_REQUIRED_MSG_KEY);
					
					if(item.getDocumentTagItems() != null){
						List<DocumentTagItem> documentTags = 
								item.getDocumentTagItems();
						HashSet<String> documentTagNames = 
								new HashSet<String>(documentTags.size());
						for (int x = 0; x < documentTags.size(); x++) {
							DocumentTagItem documentTagItem = documentTags.get(x);
							ParolePlanItemOperation documentTagOperation = 
									documentTagItem.getItemOperation();
							if (documentTagOperation ==
									ParolePlanItemOperation.CREATE 
									|| documentTagOperation ==
									ParolePlanItemOperation.UPDATE){
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