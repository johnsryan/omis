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
/* 
 * Presentence Investigation Request form behavior.
 * 
 * Author: Ryan Johns
 * Author: Annie Wahl
 * Author: Josh Divine
 * Version: 0.1.2 (Apr 24, 2018) 
 */
window.onload = function() {
	applyActionMenu(document.getElementById("actionMenuLink"));
	applyActionMenu(document.getElementById("presentenceInvestigationRequestNoteItemsActionMenuLink"), presentenceInvestigationRequestNoteItemsCreateOnClick);
	applyActionMenu(document.getElementById("presentenceInvestigationDelayItemsActionMenuLink"), presentenceInvestigationDelayItemsCreateOnClick);
	applyDatePicker(document.getElementById("expectedCompletionDate"));
	applyDatePicker(document.getElementById("requestDate"));
	applyDatePicker(document.getElementById("sentenceDate"));
	applyDatePicker(document.getElementById("actualSentenceDate"));
	applyDatePicker(document.getElementById("submissionDate"));
	applySearchUserAccountsAutocomplete(
			document.getElementById("assignedUserInput"), 
			document.getElementById("assignedUserDisplay"), 
			document.getElementById("assignedUserAccount"), 
			document.getElementById("clearAssignedUser"),
			document.getElementById("currentAssignedUser"));
	
	if(document.getElementById("personInput") != null){
		applySearchOffendersOnChange();
		applyCreatePersonOnChange();
	}
	
	for(var i = 0; i < currentPresentenceInvestigationRequestNoteItemIndex; i++){
		presentenceInvestigationRequestNoteItemRowOnClick(i);
	}
	for(var i = 0; i < currentPresentenceInvestigationDelayItemIndex; i++){
		presentenceInvestigationDelayItemRowOnClick(i);
	}
};
	
	