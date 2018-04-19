/**
 * 
 * @author Yidong Li
 * Date: June 19, 2015
 */
window.onload = function() {
	applyActionMenu(document.getElementById("noteItemsActionMenuLink"), function() {
		var createNoteLink = document.getElementById("addFamilyAssociationNoteItemLink");
		createNoteLink.onclick = function() {
		var url = createNoteLink.getAttribute("href") + "?" + "&noteItemIndex=" + offenderRelationshipNoteItemIndex;
		var request = new XMLHttpRequest();
		request.open("GET", url, false);
		request.send();
		if (request.status == 200) {
			ui.appendHtml(document.getElementById("relationshipNotesTableBody"), request.responseText);
			applyNoteRowBehavior(offenderRelationshipNoteItemIndex);
			offenderRelationshipNoteItemIndex++;
		} else {
			alert("Error - status: " + request.status + "; URL: " + url);
		}
		return false;
		};
	});
	if(existingFamilyMember==false){
		var queryInput = document.getElementById("familySearchAddressQuery");
		var searchAddress = document.getElementById("searchAddress");
		applyActionMenu(document.getElementById("familyEditActionMenuLink"));
		applyDatePicker(document.getElementById("birthDay"));
		applyDatePicker(document.getElementById("startDate"));
		applyDatePicker(document.getElementById("endDate"));
		applyDatePicker(document.getElementById("marriageDateInput"));
		applyDatePicker(document.getElementById("divorceDateInput"));
		
		applyActionMenu(document.getElementById("telephoneNumbersActionMenuLink"), function() {
			var createTelephoneNumberLink = document.getElementById("addFamilyAssociationTelephoneNumberItemLink");
			createTelephoneNumberLink.onclick = function() {
			var url = createTelephoneNumberLink.getAttribute("href") + "?" + "&telephoneNumberItemIndex=" + familyAssociationTelephoneNumberIndex;
			var request = new XMLHttpRequest();
			request.open("GET", url, false);
			request.send();
			if (request.status == 200) {
				ui.appendHtml(document.getElementById("telephoneNumbersBody"), request.responseText);
				applyTelephoneNumberRowBehavior(familyAssociationTelephoneNumberIndex);
				familyAssociationTelephoneNumberIndex++;
			} else {
				alert("Error - status: " + request.status + "; URL: " + url);
			}
			return false;
			};
		});
		
		applyActionMenu(document.getElementById("onlineAccountActionMenuLink"), function() {
			var createOnlineAccountLink = document.getElementById("addFamilyAssociationEmailItemLink");
			createOnlineAccountLink.onclick = function() {
			var url = createOnlineAccountLink.getAttribute("href") + "?" + "&emailItemIndex=" + familyAssociationOnlineAccountIndex;
			var request = new XMLHttpRequest();
			request.open("GET", url, false);
			request.send();
			if (request.status == 200) {
				ui.appendHtml(document.getElementById("emailBody"), request.responseText);
				applyOnlineAccountRowBehavior(familyAssociationOnlineAccountIndex);
				familyAssociationOnlineAccountIndex++;
			} else {
				alert("Error - status: " + request.status + "; URL: " + url);
			}
			return false;
			};
		});
		
		applyValueLabelAutoComplete(queryInput, searchAddress, config.ServerConfig.getContextPath() + "/family/findOffenderRelationshipAddress.json");
		var marriageDateContainer = document.getElementById("marriageDateContainer");
		var divorceDateContainer = document.getElementById("divorceDateContainer");
		var relationship = document.getElementById("relationship");
		applyPoBoxFieldsOnClick("poBoxFields", "poBoxFields/findStateOptions.html", "poBoxFields/findCityOptions.html", "poBoxFields/findZipCodeOptions.html");
		applyAddressFieldsOnClick("addressFields", "addressFields/findStateOptions.html", "addressFields/findCityOptions.html", "addressFields/findZipCodeOptions.html");
		applyPersonFieldsOnClick("personFields", "personFields/findStateOptions.html", "personFields/findCityOptions.html");
		applyLocationSearch(
			document.getElementById("existingAddress"),
			document.getElementById("location"),
			document.getElementById("addressDisplay"),
			document.getElementById("clearAddress")
		);
		
		for (var x = 0; x < offenderRelationshipNoteItemIndex; x++) {
			applyNoteRowBehavior(x);
			applyDatePicker(document.getElementById("noteDate"+x));
		}
		for (var x = 0; x < familyAssociationTelephoneNumberIndex; x++) {
			applyTelephoneNumberRowBehavior(x);
		}
		for (var x = 0; x < familyAssociationOnlineAccountIndex; x++) {
			applyOnlineAccountRowBehavior(x);
		}

		relationship.onchange = function() {
			if($("#relationship").val()!=""){
			$.ajax({
				url: config.ServerConfig.getContextPath() + "/family/displayMarriageDivorceDate.json?category=" + $("#relationship").val(),
				dataType: "json",
				cache:false,
				success: function(data) {
					if(data==true){
						$("#marriageDateContainer").removeClass("hidden");
						$("#divorceDateContainer").removeClass("hidden");
						$("#marriageDateContainer").addClass("show");
						$("#divorceDateContainer").addClass("show");
					}
					else {
						$("#marriageDateContainer").removeClass("show");
						$("#divorceDateContainer").removeClass("show");
						$("#marriageDateContainer").addClass("hidden");
						$("#divorceDateContainer").addClass("hidden");
					}
				},
				error: function(jqXHR, textStatus, errorThrown) {
					alert("Error: " + textStatus + "; " + errorThrown);
				}})
			}	
		}
		
		var createNewAddressRadioButton = document.getElementById("newAddressYes");
		var useExistingAddressRadioButton = document.getElementById("newAddressNo");
		var createNewAddressFields = document.getElementById("createAddressFields");
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
		var existingAddressInput = document.getElementById("familySearchAddressQuery");
		var addressContainer = document.getElementById("addressContainer");

		if(createNewAddressRadioButton!=null&&useExistingAddressRadioButton!=null){
			createNewAddressRadioButton.onclick = function(){
				existingAddressInput.disabled = true;
				addressContainer.hidden = false;
			}
			useExistingAddressRadioButton.onclick = function(){
				existingAddressInput.disabled = false;
				addressContainer.hidden = true;
			}
		}

		var poBoxFieldsContainer = document.getElementById("familyPoBoxFieldsContainer");
		var poBoxCheckBox = document.getElementById("familyCreateScreenEnterPoBox");
		poBoxCheckBox.onclick = function(){
			if(poBoxCheckBox.checked){
				poBoxFieldsContainer.hidden = false;
			}
			else {
				poBoxFieldsContainer.hidden = true;
			}
		}
		var existingAddressRadioButton = document.getElementById("newAddressNo");
		var newAddressRadioButton = document.getElementById("newAddressYes");
		var createScreenAddressContainer = document.getElementById("familyCreateScreenAddressContainer");
		var createScreenAddressFieldContainer = document.getElementById("addressContainer");
		var createScreenAddressCheckBox = document.getElementById("familyCreateScreenEnterAddress");
		if(newAddressRadioButton.checked){
			existingAddressInput.disabled = true;
			createScreenAddressFieldContainer.hidden = false;
		} else if(existingAddressRadioButton.checked){
			existingAddressInput.disabled = false;
			createScreenAddressFieldContainer.hidden = true;
		} 
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
		if(createScreenAddressCheckBox.checked){
			createScreenAddressContainer.hidden = false;
		}
		else {
			createScreenAddressContainer.hidden = true;
		}
		createScreenAddressCheckBox.onclick = function(){
			if(createScreenAddressCheckBox.checked){
				createScreenAddressContainer.hidden = false;
			}
			else {
				createScreenAddressContainer.hidden = true;
			}
		}
	}
	else {
		applyActionMenu(document.getElementById("familyEditActionMenuLink"));
		applyDatePicker(document.getElementById("startDate"));
		applyDatePicker(document.getElementById("endDate"));
		applyDatePicker(document.getElementById("marriageDateInput"));
		applyDatePicker(document.getElementById("divorceDateInput"));
		var marriageDateContainer = document.getElementById("marriageDateContainer");
		var divorceDateContainer = document.getElementById("divorceDateContainer");
		var relationship = document.getElementById("relationship");
		
		var categoryId = document.getElementById("categoryId").value;

		for (var x = 0; x < offenderRelationshipNoteItemIndex; x++) {
			applyNoteRowBehavior(x);
			applyDatePicker(document.getElementById("noteDate"+x));
		}
		
		relationship.onchange = function() {
			if($("#relationship").val()!=""){
			$.ajax({
				url: config.ServerConfig.getContextPath() + "/family/displayMarriageDivorceDate.json?category=" + $("#relationship").val(),
				dataType: "json",
				cache:false,
				success: function(data) {
					if(data==true){
						$("#marriageDateContainer").removeClass("hidden");
						$("#divorceDateContainer").removeClass("hidden");
						$("#marriageDateContainer").addClass("show");
						$("#divorceDateContainer").addClass("show");
					}
					else {
						$("#marriageDateContainer").removeClass("show");
						$("#divorceDateContainer").removeClass("show");
						$("#marriageDateContainer").addClass("hidden");
						$("#divorceDateContainer").addClass("hidden");
					}
				},
				error: function(jqXHR, textStatus, errorThrown) {
					alert("Error: " + textStatus + "; " + errorThrown);
				}})
			}
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
	
	function applyNoteRowBehavior(noteItemIndex) {
		rowItem = document.getElementById("familyAssociationNoteItems[" + noteItemIndex + "].row");
		var noteItemDate = document.getElementById("familyAssociationNoteItems[" + noteItemIndex + "].fields.date");
		applyDatePicker(noteItemDate);
		var removeLink = document.getElementById("familyAssociationNoteItems[" + noteItemIndex + "].removeLink");
		removeLink.onclick = function() {
			var noteItemIndex = this.getAttribute("id").replace("familyAssociationNoteItems[", "").replace("].removeLink", "");
			var noteItemTableRow = document.getElementById("familyAssociationNoteItems[" + noteItemIndex + "].row");
			var noteItemTableRowOperation = document.getElementById("familyAssociationNoteItems[" + noteItemIndex + "].operation");
			if(noteItemTableRowOperation.value=="UPDATE"){
				ui.addClass(noteItemTableRow, "removeRow");
				noteItemTableRowOperation.value="REMOVE"
			} else if(noteItemTableRowOperation.value=="REMOVE") {
				ui.removeClass(noteItemTableRow, "removeRow");
				noteItemTableRowOperation.value="UPDATE"
			} 
			else {
				noteItemTableRow.parentNode.removeChild(noteItemTableRow);
			}
			return false;
		}
	}
};