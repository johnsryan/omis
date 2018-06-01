window.onload = function() {
	applyActionMenu(document.getElementById("actionMenuLink"));
	applyDatePicker(document.getElementById("hearingDate"));
	applyActionMenu(document.getElementById("boardHearingNoteItemsActionMenuLink"), boardHearingNoteItemsCreateOnClick);
	applyParoleBoardLocationOnClick();
	for (var index = 0; index < currentBoardHearingNoteItemIndex; index++) {
		boardHearingNoteItemRowOnClick(index);
	}
	
	/*document.getElementById("cancelled1").onclick = function(event) {
		if (event.target.checked) {
			document.getElementById("reasonFields").classList.remove("hidden");
		} else {
			document.getElementById("reasonFields").classList.add("hidden");
		}
	};*/
	
	document.getElementById("paroleBoardItinerary").onchange = function(event) {
		if (document.getElementById("videoConference" + event.target.value)) {
			if (document.getElementById("videoConference" + event.target.value).value == "true") {
				document.getElementById("videoConferenceFields").classList.remove("hidden");
			} else {
				document.getElementById("videoConferenceFields").classList.add("hidden");
				document.getElementsByName("videoConference")[0].checked = false;
			}
		} else {
			document.getElementById("videoConferenceFields").classList.add("hidden");
			document.getElementsByName("videoConference")[0].checked = false;
		}
	};
	
}