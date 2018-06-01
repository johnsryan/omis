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
import omis.boardhearing.web.form.BoardHearingForm;
import omis.boardhearing.web.form.BoardHearingItemOperation;
import omis.boardhearing.web.form.BoardHearingNoteItem;

/**
 * Board Hearing Form Validator.
 * 
 * @author Annie Wahl 
 * @author Josh Divine
 * @version 0.1.2 (May 31, 2018)
 * @since OMIS 3.0
 */
public class BoardHearingFormValidator implements Validator {
	
	private static final String ITINERARY_REQUIRED_MSG_KEY =
			"boardHearing.itinerary.empty";
	
	private static final String DATE_REQUIRED_MSG_KEY = "date.required";
	
	private static final String DESCRIPTION_REQUIRED_MSG_KEY =
			"description.required";
	
	private static final String BOARD_MEMBER_REQUIRED_MSG_KEY =
			"boardHearing.boardMember.required";

	@SuppressWarnings("unused")
	private static final String REASON_REQUIRED_MSG_KEY =
			"boardHearing.reason.required";
	
	/**{@inheritDoc} */
	@Override
	public boolean supports(final Class<?> clazz) {
		return BoardHearingForm.class.isAssignableFrom(clazz);
	}

	/**{@inheritDoc} */
	@Override
	public void validate(final Object target, final Errors errors) {
		ValidationUtils.rejectIfEmpty(errors, "paroleBoardItinerary", 
				ITINERARY_REQUIRED_MSG_KEY);
		ValidationUtils.rejectIfEmpty(errors, "hearingDate",
				DATE_REQUIRED_MSG_KEY);
		
		BoardHearingForm form = (BoardHearingForm) target;
		
		/*if (form.getCancelled()) {
			ValidationUtils.rejectIfEmpty(errors, "reason", 
					REASON_REQUIRED_MSG_KEY);
		}*/
		
		if (form.getBoardHearingNoteItems() != null) {
			int i = 0;
			for (BoardHearingNoteItem item : form.getBoardHearingNoteItems()) {
				if (BoardHearingItemOperation.CREATE.equals(
							item.getItemOperation())
						|| BoardHearingItemOperation.UPDATE.equals(
							item.getItemOperation())) {
					if (item.getDate() == null) {
						errors.rejectValue("boardHearingNoteItems[" + i 
								+ "].date", DATE_REQUIRED_MSG_KEY);
					}
					if (item.getDate() == null) {
						errors.rejectValue(
								"boardHearingNoteItems[" + i + "].description",
								DESCRIPTION_REQUIRED_MSG_KEY);
					}
				}
				i++;
			}
		}
		
		if (form.getBoardMember1() != null || form.getBoardMember2() != null
				|| form.getBoardMember3() != null) {
			ValidationUtils.rejectIfEmpty(errors, "boardMember1", 
					BOARD_MEMBER_REQUIRED_MSG_KEY);
			ValidationUtils.rejectIfEmpty(errors, "boardMember2", 
					BOARD_MEMBER_REQUIRED_MSG_KEY);
			ValidationUtils.rejectIfEmpty(errors, "boardMember3", 
					BOARD_MEMBER_REQUIRED_MSG_KEY);
		}
		
	}
}