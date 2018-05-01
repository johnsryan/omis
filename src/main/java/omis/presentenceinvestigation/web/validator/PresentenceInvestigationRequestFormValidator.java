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
package omis.presentenceinvestigation.web.validator;

import java.util.EnumSet;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import omis.presentenceinvestigation.web.form.PresentenceInvestigationItemOperation;
import omis.presentenceinvestigation.web.form.PresentenceInvestigationRequestForm;

/** 
 * Validator for presentence investigation request form.
 * 
 * @author Ryan Johns
 * @author Annie Wahl
 * @author Josh Divine
 * @version 0.1.3 (Apr 23, 2018)
 * @since OMIS 3.0
 */
public class PresentenceInvestigationRequestFormValidator
	implements Validator {
	
		private static final String DOCKET_VALUE_REQUIRED_MSG_KEY =
				"request.docket.value.empty";
		
		private static final String COURT_REQUIRED_MSG_KEY =
				"request.docket.court.empty";
		
		private static final String DATE_REQUIRED_MSG_KEY =
				"request.note.date.empty";
		
		private static final String DESCRIPTION_REQUIRED_MSG_KEY =
				"request.note.description.empty";
		
		private static final String REQUEST_DATE_REQUIRED_MSG_KEY =
				"request.requestDate.empty";
		
		private static final String ASSIGNED_USER_REQUIRED_MSG_KEY =
				"request.assignedUser.empty";
		
		private static final String CATEGORY_REQUIRED_MSG_KEY =
				"request.category.empty";
		
		private static final String PERSON_REQUIRED_MSG_KEY =
				"personFields.person.empty";
		
		private static final String LAST_NAME_REQUIRED_MSG_KEY =
				"personFields.lastName.empty";
		
		private static final String FIRST_NAME_REQUIRED_MSG_KEY =
				"personFields.firstName.empty";
		
		private static final String REASON_REQUIRED_MSG_KEY =
				"request.delay.reason.empty";
		
	/** {@inheritDoc} */	
	@Override
	public boolean supports(final Class<?> clazz) {
		return PresentenceInvestigationRequestForm.class.isAssignableFrom(clazz);
	}

	/** {@inheritDoc} */
	@Override
	public void validate(Object obj, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "docketValue",
				DOCKET_VALUE_REQUIRED_MSG_KEY);
		ValidationUtils.rejectIfEmpty(errors, "court",
				COURT_REQUIRED_MSG_KEY);
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "requestDate", 
				REQUEST_DATE_REQUIRED_MSG_KEY);
		ValidationUtils.rejectIfEmpty(errors, "assignedUserAccount", 
				ASSIGNED_USER_REQUIRED_MSG_KEY);
		ValidationUtils.rejectIfEmpty(errors, "category", 
				CATEGORY_REQUIRED_MSG_KEY);
		PresentenceInvestigationRequestForm form =
				(PresentenceInvestigationRequestForm) obj;
		if(form.getCreatePerson() != null && form.getCreatePerson() == true){
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName",
					LAST_NAME_REQUIRED_MSG_KEY);
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName",
					FIRST_NAME_REQUIRED_MSG_KEY);
		}
		else{
			ValidationUtils.rejectIfEmpty(errors, "person",
					PERSON_REQUIRED_MSG_KEY);
		}
		for(int i = 0; i <
				form.getPresentenceInvestigationRequestNoteItems().size(); i++){
			if(EnumSet.of(PresentenceInvestigationItemOperation.CREATE,
					PresentenceInvestigationItemOperation.UPDATE).contains(
					form.getPresentenceInvestigationRequestNoteItems().get(i)
					.getItemOperation())){
				ValidationUtils.rejectIfEmptyOrWhitespace(errors,
						"presentenceInvestigationRequestNoteItems[" + i + "]"
								+ ".date", DATE_REQUIRED_MSG_KEY);
				ValidationUtils.rejectIfEmptyOrWhitespace(errors,
						"presentenceInvestigationRequestNoteItems[" + i + "]"
								+ ".description", DESCRIPTION_REQUIRED_MSG_KEY);
			}
		}
		for(int i = 0; i <
				form.getPresentenceInvestigationDelayItems().size(); i++){
			if(EnumSet.of(PresentenceInvestigationItemOperation.CREATE,
					PresentenceInvestigationItemOperation.UPDATE).contains(
					form.getPresentenceInvestigationDelayItems().get(i)
					.getItemOperation())){
				ValidationUtils.rejectIfEmptyOrWhitespace(errors,
						"presentenceInvestigationDelayItems[" + i + "]"
								+ ".reason", REASON_REQUIRED_MSG_KEY);
			}
		}
	}
}