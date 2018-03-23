window.onload = function() {
	applyActionMenu(document.getElementById("assessmentNoteItemsActionMenuLink"), assessmentNoteItemsCreateOnClick);
	for (var index = 0; index < currentAssessmentNoteItemIndex; index++) {
		assessmentNoteItemRowOnClick(index);
	}
}