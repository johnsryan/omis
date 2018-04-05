/*
 * Security threat group affiliation list screen.
 * 
 * Author: Stephen Abson
 */
window.onload = function() {
	applyRemoveLinkConfirmation();
	applyActionMenu(document.getElementById("actionMenuLink"));
	
	var activityActionMenuLink = document.getElementById("activityActionMenuLink");
	if (activityActionMenuLink != null) {
		applyActionMenu(activityActionMenuLink);
	}
	var affiliationsTableBody = document.getElementById("affiliations");
	var rowLinks = affiliationsTableBody.getElementsByTagName("a");
	for (var rowLinkIndex = 0; rowLinkIndex < rowLinks.length; rowLinkIndex++) {
		var rowLink = rowLinks[rowLinkIndex];
		if (rowLink.getAttribute("class") != null && rowLink.getAttribute("class").indexOf("actionMenuItem") > -1) {
			applyActionMenu(rowLink, function() {
				applyRemoveLinkConfirmation();
			});
		}
	}
	var activitiesTableBody = document.getElementById("activities");
	if (activitiesTableBody != null) {
		var activityRowLinks = activitiesTableBody.getElementsByTagName("a");
		for (var rowLinkIndex = 0; rowLinkIndex < activityRowLinks.length; rowLinkIndex++) {
			var rowLink = activityRowLinks[rowLinkIndex];
			if (rowLink.getAttribute("class") != null && rowLink.getAttribute("class").indexOf("actionMenuItem") > -1) {
				applyActionMenu(rowLink, function() {
					applyRemoveLinkConfirmation();
				});
			}
			if (rowLink.getAttribute("class") !=null && rowLink.getAttribute("class").indexOf("showLink") != -1) {
				rowLink.onclick = function() {
					listInvolvedOffenders(this);
					return false;
				};
			}
		}
	}
	return false;
}

function listInvolvedOffenders(elt) {
	var url = elt.getAttribute("href");
	var request = new XMLHttpRequest();
	var activityRow = $(elt).closest("tr.activityRow");
	request.open("get", url, false);
	request.send(null);
	if (request.status == 200) {
		var associatedInvolvedOffendersRows = document.getElementsByClassName("associatedInvolvedOffendersRow"); 
		for (count = 0; count < associatedInvolvedOffendersRows.length; count++) {
			$(associatedInvolvedOffendersRows[count]).remove();
		}
		$(request.responseText).insertAfter(activityRow);
	} else {
			alert("Error - " + request.status);
	}
}