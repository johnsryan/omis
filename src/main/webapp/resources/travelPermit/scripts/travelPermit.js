/*
 * OMIS - Offender Management Information System.
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
 * 
 * @author Yidong Li
 * Date: May 24, 2018
 */
window.onload = function() {
	applyActionMenu(document.getElementById("travelPermitEditActionMenuLink"));
	var startDate = document.getElementById("startDate");
	applyDatePicker(startDate);
	var endDate = document.getElementById("endDate");
	applyDatePicker(endDate);
	var issueDate = document.getElementById("issueDate");
	applyDatePicker(issueDate);
	for(var x = 0; x < travelPermitNoteItemIndex; x++){
		applyNoteRowBehavior(x);
	}
		
	applyValueLabelAutoComplete(document.getElementById("issuerInput"), 
			document.getElementById("issuer"), 
			config.ServerConfig.getContextPath() + "/travelPermit/searchUserAccounts.json");
	var queryInput = document.getElementById("travelPermitAddressQuery");
	var searchAddress = document.getElementById("searchAddress");
	applyValueLabelAutoComplete(queryInput, searchAddress, config.ServerConfig.getContextPath() + "/travelPermit/findAddress.json");
	/*document.getElementById("issuerClear").onclick = function() {
		document.getElementById("issuer").value = "";
		document.getElementById("issuerInput").value = "";
		return false;
	};*/
	document.getElementById("useCurrentUserAccountForIssuerLink").onclick = function() {
		var cc = document.getElementById("issuer");
		document.getElementById("issuer").value = config.SessionConfig.getUserAccountId();
		document.getElementById("issuerInput").value = config.SessionConfig.getUserAccountLabel();
		return false;
	};
	
	applyActionMenu(document.getElementById("noteActionMenuLink"), function() {
		var createNoteLink = document.getElementById("addTravelPermitNoteItemLink");
		createNoteLink.onclick = function() {
		var url = createNoteLink.getAttribute("href") + "?" + "&noteItemIndex=" + travelPermitNoteItemIndex;
		var request = new XMLHttpRequest();
		request.open("GET", url, false);
		request.send();
		if (request.status == 200) {
			ui.appendHtml(document.getElementById("noteTableBody"), request.responseText);
			applyNoteRowBehavior(travelPermitNoteItemIndex);
			travelPermitNoteItemIndex++;
		} else {
			alert("Error - status: " + request.status + "; URL: " + url);
		}
		return false;
		};
	});
	applyAddressFieldsOnClick("addressFields", "findStateOptions.html", "findCityOptions.html", "findZipCodeOptions.html");

	var state = document.getElementById("partialAddressStateID");
	state.onchange = function() {
		var request = new XMLHttpRequest();
		var url = config.ServerConfig.getContextPath() + "/travelPermit/listCitiesByState.html";
		var params = "state="+state.options[state.selectedIndex].value;
		request.open("GET", url + "?" + params, false);
		request.send();
		
		if (request.status == 200) 
		{
			var partialAddressCities = document.getElementById("partialAddressCityID");
			if (partialAddressCities != null) 
			{
				partialAddressCities.innerHTML = request.responseText;
			} 
			else 
			{
				alert("No cities to populate");
			}
		} 
		else 
		{
			alert("Error: " + request.status + " - " + request.statusText);
		} 
	}
	
	var city = document.getElementById("partialAddressCityID");
	city.onchange = function() {
		var request = new XMLHttpRequest();
		var url = config.ServerConfig.getContextPath() + "/travelPermit/listZipCodesByCity.html";
		var params = "city="+city.options[city.selectedIndex].value;
		request.open("GET", url + "?" + params, false);
		request.send();
		
		if (request.status == 200) 
		{
			var partialAddressZipCodes = document.getElementById("partialAddressZipCodeID");
			if (partialAddressZipCodes != null) 
			{
				partialAddressZipCodes.innerHTML = request.responseText;
			} 
			else 
			{
				alert("No zip codes to populate");
			}
		} 
		else 
		{
			alert("Error: " + request.status + " - " + request.statusText);
		} 
	}
	
	function applyNoteRowBehavior(index) {
		rowItem = document.getElementById("travelPermitNoteRows[" + index + "].row");
		var noteItemDate = document.getElementById("travelPermitNoteRow[" + index + "].date");
		applyDatePicker(noteItemDate);
		var removeLink = document.getElementById("removeNote[" + index + "].removeLink");
		if(createTravelPermit){
			if(removeLink != null){
				removeLink.onclick = function() {
					var noteItemIndex = this.getAttribute("id").replace("removeNote[", "").replace("].removeLink", "");
					var noteItemTableRow = document.getElementById("travelPermitNoteRows[" + noteItemIndex + "].row");
					var noteItemTableRowOperation = document.getElementById("travelPermitNoteItems[" + noteItemIndex + "].operation");
					if(noteItemTableRowOperation.value=="UPDATE"){
						ui.addClass(noteItemTableRow, "removeRow");
						noteItemTableRowOperation.value="REMOVE"
					} else if(noteItemTableRowOperation.value=="REMOVE") {
						ui.removeClass(noteItemTableRow, "removeRow");
						noteItemTableRowOperation.value="UPDATE";
					} 
					else {
						noteItemTableRow.parentNode.removeChild(noteItemTableRow);
					}
					return false;
				}
			}
		}
		if(!createTravelPermit){
			var noteItemTableRowOperation = document.getElementById("travelPermitNoteItems[" + index + "].operation");
			var noteItemTableRow = document.getElementById("travelPermitNoteRows[" + index + "].row");
			if(noteItemTableRowOperation.value == "REMOVE"){
				ui.addClass(noteItemTableRow, "removeRow");
			}
			if(removeLink != null){
				removeLink.onclick = function() {
					var noteItemIndex = this.getAttribute("id").replace("removeNote[", "").replace("].removeLink", "");
					var noteItemTableRow = document.getElementById("travelPermitNoteRows[" + noteItemIndex + "].row");
					var noteItemTableRowOperation = document.getElementById("travelPermitNoteItems[" + noteItemIndex + "].operation");
					if(noteItemTableRowOperation.value == "EDIT"){
						if (!ui.hasClass(noteItemTableRow, "removeRow")) {
							ui.addClass(noteItemTableRow, "removeRow");
							noteItemTableRowOperation.value = "REMOVE";
						} 
						return false;
					} 
					if (noteItemTableRowOperation.value == "REMOVE"){
						if(ui.hasClass(noteItemTableRow, "removeRow")) {
							ui.removeClass(noteItemTableRow, "removeRow");
							noteItemTableRowOperation.value = "UPDATE";
						}
						return false;
					}
					if (noteItemTableRowOperation.value == "CREATE"){
						noteItemTableRow.parentNode.removeChild(noteItemTableRow);
						return false;
					}
				}
			}
		}
	}
	
	var travelMethods = document.getElementById("travelMethods");
	var travelMethodsRadioButtons = document.getElementsByName("travelMethod");
	for (var index = 0; index < travelMethodsRadioButtons.length; index++) {
		var button = travelMethodsRadioButtons[index];
		button.onclick=function(){
			travelMethodCheck()
			var transportMethodLink = document.getElementById("transportMethodLink");
			var url = transportMethodLink.getAttribute("href") + "&travelMethod=" + this.value;
			var request = new XMLHttpRequest();
			request.open("GET", url, false);			request.send();
			if (request.status == 200) {
				ui.appendHtml(travelMethods, request.responseText);
			} else {
				alert("Error - status: " + request.status + "; URL: " + url);
			}
		}
	}

	var fullAddressContainer = document.getElementById("fullAddressContainer");
	var partialAddressContainer = document.getElementById("partialAddressContainer");
	var fullAddress = document.getElementById("fullAddress");
	var partialAddress = document.getElementById("partialAddress");
	
	fullAddress.onclick=function(){
		fullAddressContainer.hidden=false;
		partialAddressContainer.hidden=true;
	}
	partialAddress.onclick=function(){
		fullAddressContainer.hidden=true;
		partialAddressContainer.hidden=false;
	}
	
	var existingAddressContainer = document.getElementById("existingAddressContainer");
	var newAddressContainer = document.getElementById("newAddressContainer");
	var existingAddress = document.getElementById("useExistingAddress");
	var newAddress = document.getElementById("createNewAddress");
	
	existingAddress.onclick=function(){
		existingAddressContainer.hidden=false;
		newAddressContainer.hidden=true;
	}
	newAddress.onclick=function(){
		existingAddressContainer.hidden=true;
		newAddressContainer.hidden=false;
	}
	
	var falseNewCity = document.getElementById("newCityFalse");
	var trueNewCity = document.getElementById("newCityTrue");

	trueNewCity.onclick=function(){
		if (!ui.hasClass(document.getElementById("existingCityFieldGroup"),"hidden")) {
			ui.addClass(document.getElementById("existingCityFieldGroup"),"hidden");
		}
		if (ui.hasClass(document.getElementById("newCityFieldGroup"),"hidden")) {
			ui.removeClass(document.getElementById("newCityFieldGroup"),"hidden");
		}
		if (ui.hasClass(document.getElementById("newZipCodeFieldGroup"),"hidden")) {
			ui.removeClass(document.getElementById("newZipCodeFieldGroup"),"hidden");
			ui.addClass(document.getElementById("existingZipCodeFieldGroup"),"hidden");
		}
		if (ui.hasClass(document.getElementById("existingZipCodeFieldGroup"),"hidden")) {
			ui.addClass(document.getElementById("existingZipCodeFieldGroup"),"hidden");
		}

		document.getElementById("newZipCodeTrue").checked=true;
		document.getElementById("newZipCodeFalse").checked=false;
		document.getElementById("newZipCodeFalse").setAttribute("disabled", "disabled");
	};
	falseNewCity.onclick=function(){
		if (ui.hasClass(document.getElementById("existingCityFieldGroup"),"hidden")) {
			ui.removeClass(document.getElementById("existingCityFieldGroup"),"hidden");
		}
		if (ui.hasClass(document.getElementById("existingZipCodeFieldGroup"),"hidden")) {
			ui.removeClass(document.getElementById("existingZipCodeFieldGroup"),"hidden");
			ui.addClass(document.getElementById("newZipCodeFieldGroup"),"hidden");
			ui.addClass(document.getElementById("newCityFieldGroup"),"hidden");
		}
		document.getElementById("newZipCodeFalse").removeAttribute("disabled");
		document.getElementById("newZipCodeTrue").checked=false;
		document.getElementById("newZipCodeFalse").checked=true;
	};
		
	var falseNewZipCode = document.getElementById("newZipCodeFalse");
	var trueNewZipCode = document.getElementById("newZipCodeTrue");
	trueNewZipCode.onclick=function(){
		if (ui.hasClass(document.getElementById("newZipCodeFieldGroup"),"hidden")) {
			ui.removeClass(document.getElementById("newZipCodeFieldGroup"),"hidden");
		}
		if (!ui.hasClass(document.getElementById("existingZipCodeFieldGroup"),"hidden")) {
			ui.addClass(document.getElementById("existingZipCodeFieldGroup"),"hidden");
		}
	};
	falseNewZipCode.onclick=function(){
		if (!ui.hasClass(document.getElementById("newZipCodeFieldGroup"),"hidden")) {
			ui.addClass(document.getElementById("newZipCodeFieldGroup"),"hidden");
		}
		if (ui.hasClass(document.getElementById("existingZipCodeFieldGroup"),"hidden")) {
			ui.removeClass(document.getElementById("existingZipCodeFieldGroup"),"hidden");
		}
	};
	
	function travelMethodCheck(){
		var travelMethodDiv = document.getElementById("div");
		if(travelMethodDiv!=null){
			div.parentNode.removeChild(travelMethodDiv);
		}
	}
}