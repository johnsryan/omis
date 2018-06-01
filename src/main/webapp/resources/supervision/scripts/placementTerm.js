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
 * Behavior for placement term detail screen.
 * 
 * Author: Stephen Abson
 */
window.onload = function() {
	var allowCorrectionalStatus = document.getElementById("allowCorrectionalStatus");
	var correctionalStatus = document.getElementById("correctionalStatus");
	var allowState = document.getElementById("allowState");
	var state;
	if (allowState.value) {
		 state = document.getElementById("state");
	} else {
		state = null;
	}
	var allowStatusFields = document.getElementById("allowStatusFields");
	if (allowCorrectionalStatus.value == "true") {
		correctionalStatus.onchange = function() {
			var elt = this;
			var allowSupervisoryOrganization = document.getElementById("allowSupervisoryOrganization");
			if (allowSupervisoryOrganization.value =="true" && allowState.value == "true") {
				populateSupervisoryOrganizations(elt.options[elt.selectedIndex].value, state.options[state.selectedIndex].value);
			}
			if (allowStatusFields.value == "true") {
				populatePlacementStatuses(elt.options[elt.selectedIndex].value);
				toggleStatusDateAndReturnedByStatus(status);
			}
			populateStartChangeReasons(fromCorrectionalStatus, elt.options[elt.selectedIndex].value);
			var allowEndChangeReason = document.getElementById("allowEndChangeReason");
			if (allowEndChangeReason.value == "true") {
				populateEndChangeReasons(elt.options[elt.selectedIndex].value);
			}
			var locationRequired;
			if (elt.options[elt.selectedIndex].value != null) {
				var url = config.ServerConfig.getContextPath() + "/supervision/placementTerm/isLocationRequiredForCorrectionalStatus.json?correctionalStatus=" + elt.options[elt.selectedIndex].value;
				var request = new XMLHttpRequest();
				request.open("get", url, false);
				request.send(null);
				if (request.status == 200) {
					locationRequired = eval(request.responseText);
				} else {
					alert("Error - status: " + request.status + "; url: " + url);
					return;
				}
			} else {
				locationRequired = false;
			}
			var allowSendToLocation = document.getElementById("allowSendToLocation");
			if (allowSendToLocation.value == "true") {
				var sendToLocationGroup = document.getElementById("sendToLocationGroup");
				if (locationRequired) {
					if (ui.hasClass(sendToLocationGroup, "hidden")) {
						ui.removeClass(sendToLocationGroup, "hidden");
					}
				} else {
					if (!ui.hasClass(sendToLocationGroup, "hidden")) {
						ui.addClass(sendToLocationGroup, "hidden");
					}
				}
			}
		};
	}
	if (allowState.value == "true") {
		state.onchange = function() {
			var elt = this;
			var correctionalStatusValue;
			if (allowCorrectionalStatus.value == "true") {
				correctionalStatusValue = correctionalStatus.options[correctionalStatus.selectedIndex].value;
			} else {
				correctionalStatusValue = correctionalStatus.value;
			}
			populateSupervisoryOrganizations(correctionalStatusValue, elt.options[elt.selectedIndex].value);
		};
	}
	if (allowStatusFields.value == "true") {
		var status = document.getElementById("status");
		status.onchange = function() {
			toggleStatusDateAndReturnedByStatus(status);
		};
		applyDatePicker(document.getElementById("statusDate"));
		applyTimePicker(document.getElementById("statusTime"));
		var returned = document.getElementById("returned");
		returned.onchange = function() {
			toggleStatusReturnedDate();
		};
		applyDatePicker(document.getElementById("statusReturnedDate"));
		applyTimePicker(document.getElementById("statusReturnedTime"));
	}
	var allowStartDate = document.getElementById("allowStartDate");
	if (allowStartDate.value) {
		applyDatePicker(document.getElementById("startDate"));
	}
	var allowStartTime = document.getElementById("startTime");
	if (allowStartTime.value) {
		applyTimePicker(document.getElementById("startTime"));
	}
	applyDatePicker(document.getElementById("endDate"));
	applyTimePicker(document.getElementById("endTime"));
	for (var currentNoteItemIndex = 0; currentNoteItemIndex < placementTermNoteIndex; currentNoteItemIndex++) {
		applyNoteRowBehavior(currentNoteItemIndex);
	}
	applyActionMenu(document.getElementById("actionMenuLink"));
	applyActionMenu(document.getElementById("placementTermNoteItemsActionMenu"), function() {
		var createPlacementTermNoteLink = document.getElementById("createPlacementTermNoteLink");
		createPlacementTermNoteLink.onclick = function() {
			var url = createPlacementTermNoteLink.getAttribute("href") + "?placementTermNoteIndex=" + placementTermNoteIndex;
			var request = new XMLHttpRequest();
			request.open("GET", url + "&timestamp=" + new Date().getTime(), false);
			request.send(null);
			if (request.status == 200) {
				ui.appendHtml(document.getElementById("placementTermNotesBody"), request.responseText);
				applyNoteRowBehavior(placementTermNoteIndex);
			} else {
				alert("Error - status: " + request.status + "; URL: " + url);
			}
			placementTermNoteIndex = placementTermNoteIndex + 1;
			return false;
		};
	});
};

/**
 * Retrieves supervisory organizations, populates supervisory organization drop 
 * down.
 * 
 * @param correctionalStatus correctional status
 */
function populateSupervisoryOrganizations(correctionalStatus, state) {
	var url = config.ServerConfig.getContextPath();
	url = url + "/supervision/placementTerm/findSupervisoryOrganizationsForCorrectionalStatus.html";
	var params = "correctionalStatus=" + correctionalStatus;
	if (state != null) {
		params = params + "&state=" + state;
	}
	var request = new XMLHttpRequest();
	request.open("get", url + "?" + params, false);
	request.send(null);
	if (request.status == 200) {
		var supervisoryOrganization = document.getElementById("supervisoryOrganization");
		supervisoryOrganization.innerHTML = request.responseText;
	} else {
		alert("Error - " + request.status);
	}
}

/**
 * Retrieves placement statuses, populates placement status drop down.
 * 
 * @param correctionalStatus correctional status
 */
function populatePlacementStatuses(correctionalStatus) {
	var url = config.ServerConfig.getContextPath();
	url = url + "/supervision/placementTerm/findPlacementStatusesForCorrectionalStatus.html";
	var params = "correctionalStatus=" + correctionalStatus;
	var request = new XMLHttpRequest();
	request.open("get", url + "?" + params, false);
	request.send(null);
	if (request.status == 200) {
		var status = document.getElementById("status");
		status.innerHTML = request.responseText;
	} else {
		alert("Error - " + request.status);
	}
}

/**
 * Retrieves start change reasons, populates start change reason drop down.
 * 
 * @param fromCorrectionalStatus current correctional status
 * @param toCorrectionalStatus correctional status transitioning to
 */
function populateStartChangeReasons(fromCorrectionalStatus, toCorrectionalStatus) {
	var url = config.ServerConfig.getContextPath();
	url = url + "/supervision/placementTerm/findStartChangeReasonsForCorrectionalStatuses.html";
	var params = "toCorrectionalStatus=" + toCorrectionalStatus;
	params = addParam(params, "fromCorrectionalStatus", fromCorrectionalStatus, true);
	var request = new XMLHttpRequest();
	request.open("get", url + "?" + params, false);
	request.send(null);
	if (request.status == 200) {
		var startChangeReason = document.getElementById("startChangeReason");
		startChangeReason.innerHTML = request.responseText;
	} else {
		alert("Error - " + request.status);
	}
}

/**
 * Retrieves end change reasons, populates end change reason drop down.
 * 
 * @param fromChangeReason correctional status that is being ended
 */
function populateEndChangeReasons(fromChangeReason) {
	var url = config.ServerConfig.getContextPath();
	url = url + "/supervision/placementTerm/findEndChangeReasonsForCorrectionalStatus.html";
	var params = "fromCorrectionalStatus=" +  fromChangeReason;
	var request = new XMLHttpRequest();
	request.open("get", url + "?" + params, false);
	request.send(null);
	if (request.status == 200) {
		var endChangeReason = document.getElementById("endChangeReason");
		endChangeReason.innerHTML = request.responseText;
	} else {
		alert("Error - " + request.status);
	}
}

/**
 * Adds parameter to a URL.
 * 
 * @param url url
 * @param paramName parameter name
 * @param paramValue parameter value
 * @param optional whether parameter is optional
 * @returns url with parameter added
 */
function addParam(url, paramName, paramValue, optional) {
	if (!optional || (paramValue != null && paramValue != "")) {
		return url + "&" + paramName + "=" + paramValue;
	} else {
		return url;
	}
}

/**
 * Applies row behavior for notes.
 * 
 * @param noteItemIndex item index of row
 */
function applyNoteRowBehavior(noteItemIndex) {
	
	// Row behavior for notes
	var placementTermNoteItemRemoveLink = document.getElementById("placementTermNoteItemRemoveLink" + noteItemIndex);
	placementTermNoteItemRemoveLink.onclick = function() {
		var operation = document.getElementById(this.getAttribute("id").replace("placementTermNoteItemRemoveLink", "placementTermNoteItemOperation"));
		placementTermNoteItemTableRow = document.getElementById(this.getAttribute("id").replace("placementTermNoteItemRemoveLink", "placementTermNoteItemTableRow"));
		if (operation.value == "CREATE") {
			placementTermNoteItemTableRow.parentNode.removeChild(placementTermNoteItemTableRow);
		} else if (operation.value == "UPDATE") {
			if (!ui.hasClass(placementTermNoteItemTableRow, "removeRow")) {
				ui.addClass(placementTermNoteItemTableRow, "removeRow");
			}
			operation.value = "REMOVE";
		} else if (operation.value == "REMOVE") {
			operation.value = "UPDATE";
			if (ui.hasClass(placementTermNoteItemTableRow, "removeRow")) {
				ui.removeClass(placementTermNoteItemTableRow, "removeRow");
			}
		} else {
			alert("Unsupported operation form item: " + operation.value);
		}
		return false;
	};
	applyDatePicker(document.getElementById("placementTermNoteItemDate" + noteItemIndex));
}

/**
 * Toggles status date and returned field by status.
 * 
 * @param status status
 */
function toggleStatusDateAndReturnedByStatus(status) {
	if (status.value == 'PLACED' || status.value == '') {
		var statusDate = document.getElementById("statusDate");
		statusDate.setAttribute("disabled", "disabled");
		var statusTime = document.getElementById("statusTime");
		statusTime.setAttribute("disabled", "disabled");
		var returned = document.getElementById("returned");
		returned.setAttribute("disabled", "disabled");
		var statusReturnedDate = document.getElementById("statusReturnedDate");
		statusReturnedDate.setAttribute("disabled", "disabled");
		var statusReturnedTime = document.getElementById("statusReturnedTime");
		statusReturnedTime.setAttribute("disabled", "disabled");
	} else {
		var statusDate = document.getElementById("statusDate");
		statusDate.removeAttribute("disabled");
		var statusTime = document.getElementById("statusTime");
		statusTime.removeAttribute("disabled");
		var returned = document.getElementById("returned");
		returned.removeAttribute("disabled");
	}
}

/**
 * Toggles status return date.
 */
function toggleStatusReturnedDate() {
	if (document.getElementById("returned").checked) {
		var statusReturnedDate = document.getElementById("statusReturnedDate");
		statusReturnedDate.removeAttribute("disabled");
		var statusReturnedTime = document.getElementById("statusReturnedTime");
		statusReturnedTime.removeAttribute("disabled");
	} else {
		var statusReturnedDate = document.getElementById("statusReturnedDate");
		statusReturnedDate.setAttribute("disabled", "disabled");
		var statusReturnedTime = document.getElementById("statusReturnedTime");
		statusReturnedTime.setAttribute("disabled", "disabled");
	}
}