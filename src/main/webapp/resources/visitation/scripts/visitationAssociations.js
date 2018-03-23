/**
 * Visitation associations java script.
 * 
 * Author: Joel Norris
 * Version: 0.1.0 (January 14, 2016)
 * Since: OMIS 3.0
 */
window.onload = function() {
	applyActionMenu(document.getElementById("visitationAssociationsActionMenuLink"));
	applyActionMenu(document.getElementById("visitsActionMenuLink"));
	applyVisitationAssociationRowsOnClick();
	applyVisitRowsOnClick();
	
	// Focuses input when label or radio box is clicked
	function applyFieldFocus(radioEltName, inputEltName) {
		var radioElt = document.getElementById(radioEltName);
		var inputElt = document.getElementById(inputEltName);
		radioElt.onfocus = function() {
			inputElt.focus();
		};
	}
	
	// Checks radio button when input is focused
	function applyInputFocus(inputEltName, radioEltName) {
		var inputElt = document.getElementById(inputEltName);
		var radioElt = document.getElementById(radioEltName);
		inputElt.onfocus = function() {
			radioElt.checked = true;
		};
	}
	
	applyFieldFocus("singleDateSearch", "date");
	applyInputFocus("date", "singleDateSearch");
	applyFieldFocus("dateRangeSearch", "startDate");
	applyInputFocus("startDate", "dateRangeSearch");
	applyInputFocus("endDate", "dateRangeSearch");
	applyVisitLogFormOnClick();
	/*function applyVisitationAssociationRowActionMenuOnClick(event, targetElement) {
		alert("!Here");
		var element = event.target;
		var visitationAssociationRow = $(element).closest(
				"visitationAssociationSummary");
		var viewVisitationAlternateNames = $(targetElement).find(".viewAlternativeNamesLink")[0];
		$(viewVisitationAlternateNames).click(function() {
			$.ajax(viewVisitationAlternateNames.href,
					{async: false, type: "GET",
					cache: false,
					success: function(response) {
						var alternateNameRows = document.getElementsByClassName("associatedAlternateNames");
						for (count = 0;  count < alternateNameRows.length; count++) {
							$(alternateNameRows[count]).remove();
						}
						$(response).insertAfter(visitationAssociationRow);					
					},
					error: function(jqXHR, textStatus, errorThrown) {
						alert("Error - status: " + textStatus + "; error: "
							+ errorThrown);
						$(visitationAssociationRow).html(jqXHR.responseText );
					}
				});
			return false;
		});	
	};*/
	applyVisitorCheckInFormOnClick();
	
}

function applyVisitationAssociationRowActionMenu() {
	var visitationAssociationRemoveLinks = document.getElementsByClassName("visitationAssociationRemoveLink");
	for (var i = 0; i < visitationAssociationRemoveLinks.length; i++) {
		
		visitationAssociaitonRemoveLink[i].onclick = function() {
			ui.confirm("visitation", "visitationAssociationRemoveOnClick");
		};
		alert("reached");
	}
}