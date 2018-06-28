/**
 * Offender search detail screen behavior.
 * 
 * Author: Sheronda Vaughn, Joel Norris
 */
window.onload = function() {	
	document.getElementById("searchByDOCIdNumber").focus();
	applyActionMenu(document.getElementById("searchCriteriaActionMenu"));
	applyActionMenu(document.getElementById("searchResultsActionMenu"));
};