/**
 * Behavior for the travel permit listing screen.
 * 
 * Author: Sheronda Vaughn
 */
window.onload = function() {
	applyActionMenu(document.getElementById("actionMenuLink"));	
	applyRemoveLinkConfirmation();
	
	var travelPermitRows = document.getElementsByClassName("rowActionMenuLinks");
	for(var x =0; x < travelPermitRows.length; x++) {
		applyActionMenu(travelPermitRows[x], function() {applyRemoveLinkConfirmation();});
	}
};