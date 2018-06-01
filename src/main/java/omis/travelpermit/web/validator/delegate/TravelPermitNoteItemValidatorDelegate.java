/*
 *  OMIS - Offender Management Information System
 *  Copyright (C) 2011 - 2017 State of Montana
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package omis.travelpermit.web.validator.delegate;

import org.springframework.validation.Errors;

import omis.travelpermit.web.form.TravelPermitNoteItem;
import omis.workassignment.web.form.WorkAssignmentNoteItem;

/**
 * Delegate to validator travel permit note items.
 *
 * @author Yidong Li
 * @version 0.0.1 (May 22, 2018)
 * @since OMIS 3.0
 */
public class TravelPermitNoteItemValidatorDelegate {
	
	/** Instantiates delegate to validate travel permit note items. */
	public TravelPermitNoteItemValidatorDelegate() {
		// Default instantiation
	}

	/**
	 * Validatates travel permit note item.
	 * 
	 * @param travelPermitNoteItem travel permit note item
	 * @param itemIndex index of travel permit note item 
	 * @param errors errors
	 */
	public void validate(final TravelPermitNoteItem travelPermitNoteItem,
			final int itemIndex, final Errors errors) {
		if (travelPermitNoteItem.getNote().getDate() == null) {
			errors.rejectValue("travelPermitNoteItems[" + itemIndex + "].date",
				"TravelPermitNote.date.empty");
		}
		/*if (workAssignmentNoteItem.getNote() == null ||workAssignmentNoteItem
			.getNote().isEmpty()) {
			errors.rejectValue("workAssignmentNoteItems[" + itemIndex + "].note",
				"WorkAssignmentNote.note.empty");
		}*/
	}
}