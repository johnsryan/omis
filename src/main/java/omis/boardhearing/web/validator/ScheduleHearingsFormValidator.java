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
package omis.boardhearing.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import omis.boardhearing.web.form.BoardHearingItem;
import omis.boardhearing.web.form.ScheduleHearingsForm;

/**
 * Schedule Hearings Form Validator.
 * 
 *@author Annie Wahl 
 *@version 0.1.0 (Jun 29, 2018)
 *@since OMIS 3.0
 *
 */
public class ScheduleHearingsFormValidator implements Validator {
	
	private static final String DATE_REQUIRED_MSG_KEY = "date.required";
	
	private static final String BOARD_MEMBER_REQUIRED_MSG_KEY =
			"boardHearing.boardMember.required";
	
	/**{@inheritDoc} */
	@Override
	public boolean supports(Class<?> clazz) {
		return false;
	}

	/**{@inheritDoc} */
	@Override
	public void validate(Object target, Errors errors) {
		ScheduleHearingsForm form = (ScheduleHearingsForm) target;
		
		for (int i = 0; i < form.getBoardHearingItems().size(); i++) {
			BoardHearingItem item = form.getBoardHearingItems().get(i);
			
			if (item.getSelected()) {
				ValidationUtils.rejectIfEmpty(errors, "boardHearingItems[" + i 
								+ "].hearingDate",
						DATE_REQUIRED_MSG_KEY);
				if (item.getBoardMember1() != null
						|| item.getBoardMember2() != null
						|| item.getBoardMember3() != null) {
					ValidationUtils.rejectIfEmpty(errors, "boardHearingItems["
								+ i + "].boardMember1", 
							BOARD_MEMBER_REQUIRED_MSG_KEY);
					ValidationUtils.rejectIfEmpty(errors, "boardHearingItems["
								+ i + "].boardMember2", 
							BOARD_MEMBER_REQUIRED_MSG_KEY);
					ValidationUtils.rejectIfEmpty(errors, "boardHearingItems["
								+ i + "].boardMember3", 
							BOARD_MEMBER_REQUIRED_MSG_KEY);
				}
			}
		}
	}

}
