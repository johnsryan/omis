window.onload = function() {
	applyActionMenu(document.getElementById("actionMenuLink"));
	applyActionMenu(document.getElementById("warrantCauseViolationItemsActionMenuLink"), violationToWitItemCreateLinkOnClick);
	applyActionMenu(document.getElementById("warrantNoteItemsActionMenuLink"), warrantNoteItemsCreateOnClick);
	applyDatePicker(document.getElementById("date"));
	applyDatePicker(document.getElementById("arrestDate"));
	applyDatePicker(document.getElementById("determinationDeadline"));
	
	for(var i = 0; i < currentWarrantNoteItemIndex; i++){
		warrantNoteItemRowOnClick(i);
	}
	for(var i = 0; i < currentViolationToWitItemIndex; i++){
		warrantCauseViolationItemRowOnClick(i);
		
	}
	var arrestedFields = document.getElementById('arrestedFields');
	if(document.getElementById('arrested').checked){
		arrestedFields.style.display = "block";
	}
	document.getElementById('arrested').onclick = function(){
		if(this.checked){
			arrestedFields.style.display = "block";
		}
		else{
			arrestedFields.style.display = "none";
		}
	}
	
	applyUserSearch(document.getElementById("issuedByInput"),
			document.getElementById("issuedBy"),
			document.getElementById("issuedByDisplay"),
			document.getElementById("currentIssuedBy"),
			document.getElementById("clearIssuedBy"));
	
}