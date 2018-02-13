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
 * Offender relationships edit screen java script.
 * 
 * Author: Yidong Li
 * Version: 0.1.0 (January 19, 2016)
 * Since: OMIS 3.0
 */
window.onload = function() {
	function applyExistingTelephoneNumberItemBehavior(x){
		var existingTelephoneNumberItemRemoveLink = document.getElementById("removeTelephoneNumber[" + x + "]");
		existingTelephoneNumberItemRemoveLink.onclick = function() {
			var existingTelephoneNumberItemOperation = document.getElementById(this.getAttribute("id").replace("removeTelephoneNumber", "telephoneNumberItemsOperation"));
			var existingTelephoneNumberItemRow = document.getElementById("telephoneNumberRow[" + x + "]");
			if(existingTelephoneNumberItemOperation.value == "UPDATE"){
				if (!ui.hasClass(existingTelephoneNumberItemRow, "removeRow")) {
					ui.addClass(existingTelephoneNumberItemRow, "removeRow");
					existingTelephoneNumberItemOperation.value = "REMOVE";
				} 
			} else if (existingTelephoneNumberItemOperation.value == "REMOVE"){
				if(ui.hasClass(existingTelephoneNumberItemRow, "removeRow")) {
					ui.removeClass(existingTelephoneNumberItemRow, "removeRow");
					existingTelephoneNumberItemOperation.value = "UPDATE";
				}
			}
			return false;
		}
	}
	
	function applyExistingOnlineAccountItemBehavior(x){
		var existingOnlineAccountItemRemoveLink = document.getElementById("removeOnlineAccount[" + x + "]");
		existingOnlineAccountItemRemoveLink.onclick = function() {
			var existingOnlineAccountItemOperation = document.getElementById(this.getAttribute("id").replace("removeOnlineAccount", "onlineAccountContactItemsOperation"));
			var existingOnlineAccountItemRow = document.getElementById("onlineAccountRow[" + x + "]");
			if(existingOnlineAccountItemOperation.value == "UPDATE"){
				if (!ui.hasClass(existingOnlineAccountItemRow, "removeRow")) {
					ui.addClass(existingOnlineAccountItemRow, "removeRow");
					existingOnlineAccountItemOperation.value = "REMOVE";
				} 
			} else if (existingOnlineAccountItemOperation.value == "REMOVE"){
				if(ui.hasClass(existingOnlineAccountItemRow, "removeRow")) {
					ui.removeClass(existingOnlineAccountItemRow, "removeRow");
					existingOnlineAccountItemOperation.value = "UPDATE";
				}
			}
			return false;
		}
	}
	
	function applyRelationshipNoteItemBehavior(offenderRelationshipNoteItemIndex) {
		var noteItemDate = document.getElementById("noteItems[" + offenderRelationshipNoteItemIndex + "].fields.date");
		applyDatePicker(noteItemDate);
		var removeLink = document.getElementById("noteItems[" + offenderRelationshipNoteItemIndex + "].removeLink");
		removeLink.onclick = function() {
			var itemIndex = this.getAttribute("id").replace("noteItems[", "").replace("].removeLink", "");
			var operation = document.getElementById("noteItems[" + itemIndex + "].operation");
			var row = document.getElementById("noteItems[" + itemIndex + "].row");
			if (operation.value == "CREATE") {
				row.parentNode.removeChild(row);
			} else if (operation.value == "UPDATE") {
				if (!ui.hasClass(row, "removeRow")) {
					ui.addClass(row, "removeRow");
				}
				operation.value = "REMOVE";
			} else if (operation.value == "REMOVE") {
				if (ui.hasClass(row, "removeRow")) {
					ui.removeClass(row, "removeRow");
				}
				operation.value = "UPDATE";
			} else {
				alert("Unsupported operation: " + operation.value);
			}
			return false;
		};
	}
	
	function applyTelephoneNumberRowBehavior(telephoneNumberItemIndex) {
		var newTelephoneNumberItemRemoveLink = document.getElementById("removeTelephoneNumber[" + telephoneNumberItemIndex + "]");
		newTelephoneNumberItemRemoveLink.onclick = function() {
			telephoneNumberTableRow = document.getElementById(this.getAttribute("id").replace("removeTelephoneNumber", "telephoneNumberRow"));
			telephoneNumberTableRow.parentNode.removeChild(telephoneNumberTableRow);
			return false;
		};
	}
	
	function applyOnlineAccountRowBehavior(onlineAcccountItemIndex) {
		var newOnlineAccountItemRemoveLink = document.getElementById("removeOnlineAccount[" + onlineAcccountItemIndex + "]");
		newOnlineAccountItemRemoveLink.onclick = function() {
			onlineAccountTableRow = document.getElementById(this.getAttribute("id").replace("removeOnlineAccount", "onlineAccountRow"));
			onlineAccountTableRow.parentNode.removeChild(onlineAccountTableRow);
			return false;
		};
	}
	
	for (var x = 0; x < offenderRelationshipTelephoneNumberIndex; x++) {
		var telephoneNumberItemOperation = document.getElementById("telephoneNumberItemsOperation[" + x + "]");
		var telephoneNumberItemRow = document.getElementById("telephoneNumberRow[" + x + "]");
		if(telephoneNumberItemOperation.value == "REMOVE"){
			if(!ui.hasClass(telephoneNumberItemRow, "removeRow")) {
				ui.addClass(telephoneNumberItemRow, "removeRow");
			}
		}
		if(telephoneNumberItemOperation.value == "REMOVE" || telephoneNumberItemOperation.value == "UPDATE"){
			applyExistingTelephoneNumberItemBehavior(x);
		}
		if(telephoneNumberItemOperation.value == "CREATE"){
			applyTelephoneNumberRowBehavior(x);
		}
	}
	
	for (var x = 0; x < offenderRelationshipOnlineAccountIndex; x++) {
		var onlineAccountItemOperation = document.getElementById("onlineAccountContactItemsOperation[" + x + "]");
		var onlineAccountItemRow = document.getElementById("onlineAccountRow[" + x + "]");
		if(onlineAccountItemOperation.value == "REMOVE"){
			if(!ui.hasClass(onlineAccountItemRow, "removeRow")) {
				ui.addClass(onlineAccountItemRow, "removeRow");
			}
		}
		if(onlineAccountItemOperation.value == "REMOVE" || onlineAccountItemOperation.value == "UPDATE"){
			applyExistingOnlineAccountItemBehavior(x);
		}
		if(onlineAccountItemOperation.value == "CREATE"){
			applyOnlineAccountRowBehavior(x);
		}
	}
	
	for (var currentIndex = 0; currentIndex < offenderRelationshipNoteItemIndex; currentIndex++) {
		if (document.getElementById("noteItems[" + currentIndex + "].row") != null) {
			applyRelationshipNoteItemBehavior(currentIndex);
		}
	}
	
	var personFields = "personFields";
	var queryInput = document.getElementById("offenderRelationshipSearchAddressQuery");
	var searchAddress = document.getElementById("searchAddress");
	applyActionMenu(document.getElementById("offenderRelationshipListActionMenuLink"));
	applyValueLabelAutoComplete(queryInput, searchAddress, config.ServerConfig.getContextPath() + "/offenderRelationship/update/findOffenderRelationshipAddress.json");
	applyPoBoxFieldsOnClick("poBoxFields", "poBoxFields/findStateOptions.html", "poBoxFields/findCityOptions.html", "poBoxFields/findZipCodeOptions.html");
	applyAddressFieldsOnClick("addressFields", "addressFields/findStateOptions.html", "addressFields/findCityOptions.html", "addressFields/findZipCodeOptions.html");
	applyPersonFieldsOnClick("personFields", "personFields/findStateOptions.html", "personFields/findCityOptions.html");
	
	applyActionMenu(document.getElementById("telephoneNumbersEditActionMenuLink"), function() {
		var createTelephoneNumberLink = document.getElementById("addOffenderRelationTelephoneNumberItemLink");
		createTelephoneNumberLink.onclick = function() {
		var url = createTelephoneNumberLink.getAttribute("href") + "?" + "&telephoneNumberItemIndex=" + offenderRelationshipTelephoneNumberIndex;
		var request = new XMLHttpRequest();
		request.open("GET", url, false);
		request.send();
		if (request.status == 200) {
			ui.appendHtml(document.getElementById("telephoneNumbersTable"), request.responseText);
			applyTelephoneNumberRowBehavior(offenderRelationshipTelephoneNumberIndex);
			offenderRelationshipTelephoneNumberIndex++;
		} else {
			alert("Error - status: " + request.status + "; URL: " + url);
		}
		return false;
		};
	});
	
	applyActionMenu(document.getElementById("onlineAccountsEditActionMenuLink"), function() {
		var createOnlineAccountLink = document.getElementById("addOffenderRelationshipOnlineAccountItemLink");
		createOnlineAccountLink.onclick = function() {
		var url = createOnlineAccountLink.getAttribute("href") + "?" + "&onlineAccountItemIndex=" + offenderRelationshipOnlineAccountIndex;
		var request = new XMLHttpRequest();
		request.open("GET", url, false);
		request.send();
		if (request.status == 200) {
			ui.appendHtml(document.getElementById("onlineAccountsTable"), request.responseText);
			applyOnlineAccountRowBehavior(offenderRelationshipOnlineAccountIndex);
			offenderRelationshipOnlineAccountIndex++;
		} else {
			alert("Error - status: " + request.status + "; URL: " + url);
		}
		return false;
		};
	});
	
	applyActionMenu(document.getElementById("noteItemsActionMenuLink"), function() {
		createRelationshipNoteItemLink = document.getElementById("createRelationshipNoteItemLink");
		createRelationshipNoteItemLink.onclick = function() {
			var url = createRelationshipNoteItemLink.getAttribute("href") + "?offenderRelationshipNoteItemIndex=" + offenderRelationshipNoteItemIndex;
			var request = new XMLHttpRequest();
			request.open("GET", url, false);
			request.send(null);
			if (request.status == 200) {
				ui.appendHtml(document.getElementById("relationshipNotesTableBody"), request.responseText);
				applyRelationshipNoteItemBehavior(offenderRelationshipNoteItemIndex);
				offenderRelationshipNoteItemIndex++;
			} else {
				alert("Error - status: " + request.status + "; URL: " + url);
			}
			return false;
		};
	});
	
	var currentAddressRadioButton = document.getElementById("currentAddress");
	var existingAddressRadioButton = document.getElementById("existingAddress");
	var newAddressRadioButton = document.getElementById("newAddress");
	var addressFieldsHouseNumber = document.getElementById("addressFieldsHouseNumber");
	var addressFieldsStreetName = document.getElementById("addressFieldsStreetName");
	var addressFieldsStreetSuffix = document.getElementById("addressFieldsStreetSuffix");
	var addressFieldsAddressUnitDesignator = document.getElementById("addressFieldsAddressUnitDesignator");
	var addressFieldsUnitName = document.getElementById("addressFieldsUnitName");
	var addressFieldsCountry = document.getElementById("addressFieldsCountry");
	var addressFieldsState = document.getElementById("addressFieldsState");
	var addressFieldsCity = document.getElementById("addressFieldsCity");
	var addressFieldsZipCode = document.getElementById("addressFieldsZipCode");
	var addressFieldsFalseNewCity = document.getElementById("addressFieldsFalseNewCity");
	var addressFieldsTrueNewCity = document.getElementById("addressFieldsTrueNewCity");
	var addressFieldsTrueNewZipCode = document.getElementById("addressFieldsTrueNewZipCode");
	var existingAddressInput = document.getElementById("offenderRelationshipSearchAddressQuery");
	var addressFieldsContainer = document.getElementById("addressFieldsContainer");
	
	if(currentAddressRadioButton!=null){
		currentAddressRadioButton.onclick = function(){
			existingAddressInput.disabled = true;
			addressFieldsContainer.hidden = true;
		}
		newAddressRadioButton.onclick = function(){
			existingAddressInput.disabled = true;
			addressFieldsContainer.hidden = false;
		}
		existingAddressRadioButton.onclick = function(){
			existingAddressInput.disabled = false;
			addressFieldsContainer.hidden = true;
		}
	}
	else {
		newAddressRadioButton.onclick = function(){
			existingAddressInput.disabled = true;
			addressFieldsContainer.hidden = false;
		}
		existingAddressRadioButton.onclick = function(){
			existingAddressInput.disabled = false;
			addressFieldsContainer.hidden = true;
		}
	}

	var addressContainer = document.getElementById("addressContainer");
	var addressCheckBox = document.getElementById("enterAddress");
	
	addressCheckBox.onclick = function(){
		if(addressCheckBox.checked){
			addressContainer.hidden = false;
		}
		else {
			addressContainer.hidden = true;
		}
	}
	
	var poBoxFieldsContainer = document.getElementById("poBoxFieldsContainer");
	var poBoxCheckBox = document.getElementById("enterPoBox");
	
	poBoxCheckBox.onclick = function(){
		if(poBoxCheckBox.checked){
			poBoxFieldsContainer.hidden = false;
		}
		else {
			poBoxFieldsContainer.hidden = true;
		}
	}
};