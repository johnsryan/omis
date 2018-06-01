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
 * Author: Stephen Abson
 * Version: 0.1.0 (January 19, 2016)
 * Since: OMIS 3.0
 */
window.onload = function() {
	applyDatePicker(document.getElementById("familyAssociationFieldsStartDate"));
	applyDatePicker(document.getElementById("familyAssociationFieldsEndDate"));
	applyDatePicker(document.getElementById("familyAssociationFieldsMarriageDate"));
	applyDatePicker(document.getElementById("familyAssociationFieldsDivorceDate"));
	/*Victim fields*/
	applyDatePicker(document.getElementById("victimFieldsRegisterDate"));
	applyDatePicker(document.getElementById("victimFieldsPacketSendDate"));
	/* Visitation fields */
	applyDatePicker(document.getElementById("visitationAssociationFieldsDecisionDate"));
	applyDatePicker(document.getElementById("visitationAssociationFieldsStartDate"));
	applyDatePicker(document.getElementById("visitationAssociationFieldsEndDate"));
	applyActionMenu(document.getElementById("offenderRelationshipListActionMenuLink"));
	applyTextCounter(document.getElementById("visitationAssociationFieldsNotes"), document.getElementById("notesCharacterCounter"));
	applyTextCounter(document.getElementById("visitationAssociationFieldsGuardianship"), document.getElementById("guardianshipCharacterCounter"));
	
	var marriageDateContainer = document.getElementById("marriageDateContainer");
	var divorceDateContainer = document.getElementById("divorceDateContainer");
	var relationship = document.getElementById("relationship");
	
	if(!spauseStatus){
		marriageDateContainer.style.visibility="hidden";
		divorceDateContainer.style.visibility="hidden";
	} else {
		marriageDateContainer.style.visibility="visible";
		divorceDateContainer.style.visibility="visible";
	}
	
	relationship.onchange = function() {
		if($("#relationship").val()!=""){
		$.ajax({
			url: config.ServerConfig.getContextPath() + "/offenderRelationship/displayMarriageDivorceDate.json?category=" + $("#relationship").val(),
			dataType: "json",
			cache:false,
			success: function(data) {
				if(data==true){
					marriageDateContainer.style.visibility="visible";
					divorceDateContainer.style.visibility="visible";
				}
				else {
					marriageDateContainer.style.visibility="hidden";
					divorceDateContainer.style.visibility="hidden";
				}
			},
			error: function(jqXHR, textStatus, errorThrown) {
				alert("Error: " + textStatus + "; " + errorThrown);
			}})
		}	
	}
	
	if(newRelation){
		var queryInput = document.getElementById("offenderRelationshipSearchAddressQuery");
		var searchAddress = document.getElementById("searchAddress");
		applyActionMenu(document.getElementById("offenderRelationshipListActionMenuLink"));
		applyValueLabelAutoComplete(queryInput, searchAddress, config.ServerConfig.getContextPath() + "/offenderRelationship/findOffenderRelationshipAddress.json");
		applyAddressFieldsOnClick("addressFields", "addressFields/findStateOptions.html", "addressFields/findCityOptions.html", "addressFields/findZipCodeOptions.html");
		applyPersonFieldsOnClick("personFields", "personFields/findStateOptions.html", "personFields/findCityOptions.html");
		applyPoBoxFieldsOnClick("poBoxFields", "poBoxFields/findStateOptions.html", "poBoxFields/findCityOptions.html", "poBoxFields/findZipCodeOptions.html");
		
		applyActionMenu(document.getElementById("telephoneNumbersCreateActionMenuLink"), function() {
			var createTelephoneNumberLink = document.getElementById("addCreateOffenderRelationTelephoneNumberItemLink");
			createTelephoneNumberLink.onclick = function() {
			var url = createTelephoneNumberLink.getAttribute("href") + "?" + "&telephoneNumberItemIndex=" + offenderRelationshipTelephoneNumberIndex;
			var request = new XMLHttpRequest();
			request.open("GET", url, false);
			request.send();
			if (request.status == 200) {
				ui.appendHtml(document.getElementById("telephoneNumbersTableBody"), request.responseText);
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
				ui.appendHtml(document.getElementById("onlineAccountsTableBody"), request.responseText);
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
					applyRelationshipNoteRowBehavior(offenderRelationshipNoteItemIndex);
					offenderRelationshipNoteItemIndex++;
				} else {
					alert("Error - status: " + request.status + "; URL: " + url);
				}
				return false;
			};
		});
		
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
		
		var createScreenAddressContainer = document.getElementById("createScreenAddressContainer");
		var createScreenAddressFieldContainer = document.getElementById("createScreenAddressFieldContainer");
		var createScreenAddressCheckBox = document.getElementById("createScreenEnterAddress");
		
		if(newAddressRadioButton!=null&&existingAddressRadioButton!=null){
			newAddressRadioButton.onclick = function(){
				existingAddressInput.disabled = true;
				createScreenAddressFieldContainer.hidden = false;
			}
			existingAddressRadioButton.onclick = function(){
				existingAddressInput.disabled = false;
				createScreenAddressFieldContainer.hidden = true;
			}
		}
		createScreenAddressCheckBox.onclick = function(){
			if(createScreenAddressCheckBox.checked){
				createScreenAddressContainer.hidden = false;
			}
			else {
				createScreenAddressContainer.hidden = true;
			}
		}
		
		var poBoxFieldsContainer = document.getElementById("poBoxFieldsContainer");
		var poBoxCheckBox = document.getElementById("createScreenEnterPoBox");

		poBoxCheckBox.onclick = function(){
			if(poBoxCheckBox.checked){
				poBoxFieldsContainer.hidden = false;
			}
			else {
				poBoxFieldsContainer.hidden = true;
			}
		}
	}
	
	var createFamilyMember = document.getElementById("createFamilyMember");
	var familyMemberContainer = document.getElementById("familyMemberContainer");
	createFamilyMember.onchange = function() {
		if(createFamilyMember.checked){
			familyMemberContainer.hidden = false;
		} else {
			familyMemberContainer.hidden = true;
		}
	}
	
	var createVictim = document.getElementById("createVictim");
	var victimContainer = document.getElementById("victimContainer");
	createVictim.onchange = function() {
		if(createVictim.checked){
			victimContainer.hidden = false;
		} else {
			victimContainer.hidden = true;
		}
	}
	
	var createVisitor = document.getElementById("createVisitor");
	var visitorContainer = document.getElementById("visitorContainer");
	createVisitor.onchange = function() {
		if(createVisitor.checked){
			visitorContainer.hidden = false;
		} else {
			visitorContainer.hidden = true;
		}
	}
	
	for (var x = 0; x < offenderRelationshipTelephoneNumberIndex; x++) {
		applyTelephoneNumberRowBehavior(x);
	}
	
	for (var x = 0; x < offenderRelationshipOnlineAccountIndex; x++) {
		applyOnlineAccountRowBehavior(x);
	}
	
	for (var x = 0; x < offenderRelationshipNoteItemIndex; x++) {
		if(document.getElementById("noteItems[" + x + "].row") != null) {
			applyRelationshipNoteRowBehavior(x);
		}
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
	
	function applyRelationshipNoteRowBehavior(offenderRelationshipNoteItemIndex) {
		var noteItemDate = document.getElementById("noteItems[" + offenderRelationshipNoteItemIndex + "].fields.date");
		applyDatePicker(noteItemDate);
		var removeLink = document.getElementById("noteItems[" + offenderRelationshipNoteItemIndex + "].removeLink");
		removeLink.onclick = function() {
			var offenderRelationshipNoteItemIndex = this.getAttribute("id").replace("noteItems[", "").replace("].removeLink", "");
			var noteItemTableRow = document.getElementById("noteItems[" + offenderRelationshipNoteItemIndex + "].row");
			noteItemTableRow.parentNode.removeChild(noteItemTableRow);
			return false;
		};
	}
};