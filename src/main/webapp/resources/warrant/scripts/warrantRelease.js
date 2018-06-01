window.onload = function() {
	applyActionMenu(document.getElementById("actionMenuLink"));
	applyDatePicker(document.getElementById("releaseDate"));
	applyDatePicker(document.getElementById("clearedByDate"));
	
	applyUserSearch(document.getElementById("clearedByInput"),
			document.getElementById("clearedBy"),
			document.getElementById("clearedByDisplay"),
			document.getElementById("currentClearedBy"),
			document.getElementById("clearClearedBy"));
}