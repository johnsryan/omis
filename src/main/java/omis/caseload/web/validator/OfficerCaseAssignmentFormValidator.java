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
package omis.caseload.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import omis.caseload.web.form.OfficerCaseAssignmentForm;

/**
 * Officer case assignment form validator.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Jun 14, 2018)
 * @since OMIS 3.0
 */
public class OfficerCaseAssignmentFormValidator implements Validator {

	/** {@inheritDoc} */
	@Override
	public boolean supports(Class<?> clazz) {
		return OfficerCaseAssignmentForm.class.isAssignableFrom(clazz);
	}

	/** {@inheritDoc} */
	@Override
	public void validate(Object target, Errors errors) {
		OfficerCaseAssignmentForm form = (OfficerCaseAssignmentForm) target;
		if (form.getSelectedOffender() == null) {
			errors.rejectValue("selectedOffender", 
					"officerCaseAssignment.offender.empty");
		}
		if (form.getOfficer() == null) {
			errors.rejectValue("officer", 
					"officerCaseAssignment.userAccount.empty");
		}
		if (form.getStartDate() == null) {
			errors.rejectValue("startDate", 
					"officerCaseAssignment.startDate.empty");
		}
	}
}