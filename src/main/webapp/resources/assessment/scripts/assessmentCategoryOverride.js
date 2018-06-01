window.onload = function() {	
	applyActionMenu(document.getElementById("actionMenuLink"));
	applyDatePicker(document.getElementById("overrideDate"));
	applyStaffAssignmentSearch(document.getElementById("authorizedByInput"), 
			document.getElementById("authorizedBy"),
			document.getElementById("authorizedByDisplay"),
			document.getElementById("authorizedByCurrent"),
			document.getElementById("authorizedByClear"));
}