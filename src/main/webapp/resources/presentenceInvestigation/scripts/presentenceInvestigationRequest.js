/* Presentence Investigation Request form behavior.
 * Author: Ryan Johns
 * Author: Annie Jacques
 * Version: 0.1.1 (Nov 2, 2016) */
window.onload = function() {
	applyActionMenu(document.getElementById("actionMenuLink"));
	applyActionMenu(document.getElementById("presentenceInvestigationRequestNoteItemsActionMenuLink"), presentenceInvestigationRequestNoteItemsCreateOnClick);
	applyDatePicker(document.getElementById("expectedCompletionDate"));
	applyDatePicker(document.getElementById("requestDate"));
	applyDatePicker(document.getElementById("sentenceDate"));
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
};
	
	