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
package omis.boardconsideration.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import omis.boardconsideration.web.form.BoardConsiderationForm;
import omis.boardconsideration.web.form.BoardConsiderationItem;
import omis.boardconsideration.web.form.BoardConsiderationNoteItem;
import omis.boardconsideration.web.form.ItemOperation;

/**
 * Board consideration form validator.
 * 
 * @author Josh Divine
 * @version 0.1.0 (May 30, 2018)
 * @since OMIS 3.0
 */
public class BoardConsiderationFormValidator implements Validator {

	/**{@inheritDoc} */
	@Override
	public boolean supports(Class<?> clazz) {
		return BoardConsiderationForm.class.isAssignableFrom(clazz);
	}

	/**{@inheritDoc} */
	@Override
	public void validate(Object target, Errors errors) {
		BoardConsiderationForm form = (BoardConsiderationForm) target;
		if (form.getBoardConsiderationItems() != null) {
			for (int i = 0; i < form.getBoardConsiderationItems().size(); i++) {
				BoardConsiderationItem boardConsiderationItem = 
						form.getBoardConsiderationItems().get(i);
				if (boardConsiderationItem.getCategory() == null) {
					errors.rejectValue("boardConsiderationItems[" + i + 
							"].category", "boardConsideration.category.empty");
				}
				if (boardConsiderationItem.getTitle() == null || 
						boardConsiderationItem.getTitle().isEmpty()) {
					errors.rejectValue("boardConsiderationItems[" + i + 
							"].title", "boardConsideration.title.empty");
				}
			}
		}
		if (form.getBoardConsiderationNoteItems() != null) {
			for (int i = 0; i < form.getBoardConsiderationNoteItems().size(); 
					i++) {
				BoardConsiderationNoteItem noteItem = 
						form.getBoardConsiderationNoteItems().get(i);
				if (noteItem.getOperation() != null && 
						!ItemOperation.REMOVE.equals(
								noteItem.getOperation())) {
					if (noteItem.getDate() == null) {
						errors.rejectValue("boardConsiderationNoteItems[" + i + 
								"].date", "boardConsiderationNote.date.empty");
					}
					if (noteItem.getValue() == null || 
							noteItem.getValue().isEmpty()) {
						errors.rejectValue("boardConsiderationNoteItems[" + i + 
								"].value", 
								"boardConsiderationNote.description.empty");
					}
				}
			}
		}
	}
}