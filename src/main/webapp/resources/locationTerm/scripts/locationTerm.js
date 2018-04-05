/*
 * OMIS - Offender Management Information System
 * Copyright (C) 2011 - 2017 State of Montana
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * Behavior for location term screen.
 *
 * @author: Stephen Abson
 */
window.onload = function() {
	
	// Returns whether offender is placed on start date (and time)
	function isPlacedOnStartDate() {
		var url = config.ServerConfig.getContextPath() + "/locationTerm/isPlaced.html?offender=" + offender;
		var startDate = document.getElementById("startDate");
		if (startDate.value != null && startDate.value != '') {
			url = url + "&effectiveDate=" + startDate.value;
			var startTime = document.getElementById("startTime");
			if (startTime.value != null && startTime.value != '') {
				url = url + "&effectiveTime=" + startTime.value;
			}
		}
		var request = new XMLHttpRequest();
		request.open("GET", url, false);
		request.send(null);
		if (request.status == 200) {
			return eval(request.responseText);
		} else {
			alert("Error - status: " + request.status + "; status text:" + request.statusText + "; URL: " + url);
			return false;
		}
	}
	
	// Removes offender not placed message if it exists
	function removeOffenderNotPlacedMessage() {
		var offenderNotPlacedMessage = document.getElementById("offenderNotPlacedMessage");
		if (offenderNotPlacedMessage != null) {
			offenderNotPlacedMessage.parentNode.removeChild(offenderNotPlacedMessage);
		}
	}
	
	// Updates locations
	function updateLocations() {
		if (isPlacedOnStartDate()) {
			var startDate = document.getElementById("startDate");
			var location = document.getElementById("location");
			var state = document.getElementById("state");
			var url = config.ServerConfig.getContextPath() + "/locationTerm/findLocations.html?offender=" + offender;
			if (locationTerm != null) {
				url = url + "&locationTerm=" + locationTerm;
			}
			if (startDate.value != null && startDate.value != '') {
				url = url + "&effectiveDate=" + startDate.value;
				var startTime = document.getElementById("startTime");
				if (startTime.value != null && startTime != '') {
					url = url + "&effectiveTime=" + startTime.value;
				}
				if (location.selectedIndex > -1) {
					url = url + "&defaultLocation=" + location.options[location.selectedIndex].value;
				}
				if (state.selectedIndex > -1) {
					url = url + "&state=" + state.options[state.selectedIndex].value;
				}
				if (changeAction != null) {
					url = url + "&changeAction=" + changeAction;
				}
			}
			var request = new XMLHttpRequest();
			request.open("GET", url, false);
			request.send(null);
			if (request.status == 200) {
				location.innerHTML = request.responseText;
			} else {
				alert("Error - status: " + request.status + "; status text:" + request.statusText + "; URL: " + url);
			}
		} else {
			var locationTermResolver = new common.MessageResolver("omis.locationterm.msgs.locationTerm");
			alert(locationTermResolver.getMessage("locationTermNotAllowedOffenderNotPlacedOnStartDateMessage"));
			var commonResolver = new common.MessageResolver("omis.msgs.common");
			var location = document.getElementById("location");
			location.innerHTML = '<option value="">' + commonResolver.getMessage("nullLabel") + '</option>';
		}
	}
	
	// Applies behavior
	var startDate = document.getElementById("startDate");
	var startTime = document.getElementById("startTime");
	applyDatePicker(startDate);
	applyTimePicker(startTime);
	var allowLocation = document.getElementById("allowLocation");
	var allowState = document.getElementById("allowState");
	if (allowLocation.value == "true" && allowState.value == "true") {
		startDate.onchange = function() {
			removeOffenderNotPlacedMessage();
			updateLocations();
		};
		startTime.onchange = function() {
			removeOffenderNotPlacedMessage();
			updateLocations();
		};
		var state = document.getElementById("state");
		state.onchange = function() {
			updateLocations();
		};
	}
	var allowSingleReasonTerm = document.getElementById("allowSingleReasonTerm");
	if (allowLocation.value == "true" && allowSingleReasonTerm.value == "true") {
		var location = document.getElementById("location");
		location.onchange = function() {
			var reason = document.getElementById("reason");
			var url = config.ServerConfig.getContextPath() + "/locationTerm/findAllowedReasons.html?location=" + location.value;
			if (reason.value != "" && reason.value != null) {
				url = url + "&defaultReason=" + reason.value;
			}
			var request = new XMLHttpRequest();
			request.open("GET", url, false);
			request.send(null);
			if (request.status == 200) {
				reason.innerHTML = request.responseText;
			} else {
				alert("Error - status: " + request.status + "; status text:" + request.statusText + "; URL: " + url);
			}
			return false;
		};
	}
	applyDatePicker(document.getElementById("endDate"));
	applyTimePicker(document.getElementById("endTime"));
	var allowMultipleReasonTerms = document.getElementById("allowMultipleReasonTerms");
	var allowSingleReasonTerm = document.getElementById("allowSingleReasonTerm");
	if (allowMultipleReasonTerms.value && allowSingleReasonTerm.value) {
		var associateMultipleReasonTerms = document.getElementById("associateMultipleReasonTerms");
		associateMultipleReasonTerms.onclick = function() {
			var reason = document.getElementById("reason");
			var reasonTermsContainer = document.getElementById("reasonTermsContainer");
			if (associateMultipleReasonTerms.checked) {
				reason.setAttribute("disabled", "disabled");
				if (ui.hasClass(reasonTermsContainer, "hidden")) {
					ui.removeClass(reasonTermsContainer, "hidden");
				}
			} else {
				reason.removeAttribute("disabled");
				if (!ui.hasClass(reasonTermsContainer, "hidden")) {
					ui.addClass(reasonTermsContainer, "hidden");
				}
			}
		};
	}
	for (var currentItemIndex = 0; currentItemIndex < locationReasonTermItemIndex; currentItemIndex++) {

		// Assign functionality
		var removeLink = document.getElementById("reasonTermItems[" + currentItemIndex + "].removeLink");
		if (removeLink != null) {
			removeLink.onclick = function() {
				
				// Gets element references
				var removeLink = this;
				var innerCurrentItemIndex = removeLink.getAttribute("id").replace("reasonTermItems[", "").replace("].removeLink", "");
				var operationElt = document.getElementById("reasonTermItems[" + innerCurrentItemIndex + "].operation");
				var tableRow = document.getElementById("reasonTermItems[" + innerCurrentItemIndex + "].row");
				
				// Handler to remove
				function removeLinkClick() {
					if (operationElt.value == "CREATE") {
						tableRow.parentNode.removeChild(tableRow);
					} else if (operationElt.value == "UPDATE") {
						operationElt.value = "REMOVE";
						if (!ui.hasClass(tableRow, "removeRow")) {
							ui.addClass(tableRow, "removeRow");
						}
						removeLink.onclick = function() {
							return unremoveLinkClick();
						};
					} else {
						
						// The only two valid operations are create and update
						// for a remove link to be invoked - SA
						alert("Invalid operation " + operationElt.value);
					}
					return false;
				}
				
				// Handler to unremove
				function unremoveLinkClick() {
					operationElt.value = "UPDATE";
					if (ui.hasClass(tableRow, "removeRow")) {
						ui.removeClass(tableRow, "removeRow");
					}
					removeLink.onclick = function() {
						return removeLinkClick();
					};
					return false;
				}
				
				// Removes link
				if (operationElt.value == "REMOVE") {
					return unremoveLinkClick();
				} else {
					return removeLinkClick();
				}
			};
		}
		applyDatePicker(document.getElementById("reasonTermItems[" + currentItemIndex + "].startDate"));
		applyTimePicker(document.getElementById("reasonTermItems[" + currentItemIndex + "].startTime"));
		applyDatePicker(document.getElementById("reasonTermItems[" + currentItemIndex + "].endDate"));
		applyTimePicker(document.getElementById("reasonTermItems[" + currentItemIndex + "].endTime"));
	}
	applyActionMenu(document.getElementById("actionMenuLink"));
	if (document.getElementById("locationReasonTermsActionMenuLink") != null) {
		applyActionMenu(document.getElementById("locationReasonTermsActionMenuLink"), function() {
			var createReasonTermLink = document.getElementById("createReasonTermLink");
			createReasonTermLink.onclick = function() {
				var url = createReasonTermLink.getAttribute("href") + "?itemIndex=" + locationReasonTermItemIndex;
				if (locationTermLocation != null) {
					url = url + "&location=" + locationTermLocation;
				}
				var request = new XMLHttpRequest();
				request.open("GET", url + "&timestamp=" + new Date().getTime(), false);
				request.send();
				if (request.status == 200) {
					ui.appendHtml(document.getElementById("reasonTermsBody"), request.responseText);
					var removeLink = document.getElementById("reasonTermItems[" + locationReasonTermItemIndex + "].removeLink");
					var tableRow = document.getElementById("reasonTermItems[" + locationReasonTermItemIndex + "].row");
					removeLink.onclick = function() {
						tableRow.parentNode.removeChild(tableRow);
						return false;
					};
					applyDatePicker(document.getElementById("reasonTermItems[" + locationReasonTermItemIndex + "].startDate"));
					applyTimePicker(document.getElementById("reasonTermItems[" + locationReasonTermItemIndex + "].startTime"));
					applyDatePicker(document.getElementById("reasonTermItems[" + locationReasonTermItemIndex + "].endDate"));
					applyTimePicker(document.getElementById("reasonTermItems[" + locationReasonTermItemIndex + "].endTime"));
					locationReasonTermItemIndex = locationReasonTermItemIndex + 1;
				} else {
					alert("Error - status: " + request.status + "; status text:" + request.statusText + "; URL: " + url);
				}
				return false;
			};
			return false;
		});
	}
};