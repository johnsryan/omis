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
	
	applyValueLabelAutoComplete(document.getElementById("issuerInput"), 
			document.getElementById("issuer"), 
			config.ServerConfig.getContextPath() + "/travelPermit/searchUserAccounts.json");
	var queryInput = document.getElementById("travelPermitAddressQuery");
	var searchAddress = document.getElementById("searchAddress");
	applyValueLabelAutoComplete(queryInput, searchAddress, config.ServerConfig.getContextPath() + "/travelPermit/findOffenderRelationshipAddress.json");
	document.getElementById("issuerClear").onclick = function() {
		document.getElementById("issuer").value = "";
		document.getElementById("issuerInput").value = "";
		return false;
	};
	document.getElementById("useCurrentUserAccountForIssuerLink").onclick = function() {
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
	
	function applyNoteRowBehavior(noteItemIndex) {
		rowItem = document.getElementById("travelPermitNoteRows[" + travelPermitNoteItemIndex + "].row");
		var noteItemDate = document.getElementById("travelPermitNoteRow[" + travelPermitNoteItemIndex + "].date");
		applyDatePicker(noteItemDate);
		var removeLink = document.getElementById("removeNote[" + travelPermitNoteItemIndex + "].removeLink");
		removeLink.onclick = function() {
			var noteItemIndex = this.getAttribute("id").replace("removeNote[", "").replace("].removeLink", "");
			var noteItemTableRow = document.getElementById("travelPermitNoteRows[" + noteItemIndex + "].row");
			var noteItemTableRowOperation = document.getElementById("travelPermitNoteItems[" + noteItemIndex + "].operation");
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
	
	var travelMethod = document.getElementById("travelMethod");
	var airplane = document.getElementById("transportMethod2");
	var privateVehicle = document.getElementById("transportMethod1");
	var bus = document.getElementById("transportMethod3");
	var train = document.getElementById("transportMethod4");

	privateVehicle.onclick=function(){
		travelMethodCheck()
		var transportMethodLink = document.getElementById("transportMethodLink");
		var url = transportMethodLink.getAttribute("href") + "&travelMethod=" + privateVehicle.value;
		var request = new XMLHttpRequest();
		request.open("GET", url, false);
		request.send();
		if (request.status == 200) {
			ui.appendHtml(travelMethod, request.responseText);
		} else {
			alert("Error - status: " + request.status + "; URL: " + url);
		}
	}

	airplane.onclick=function(){
		travelMethodCheck()
		var transportMethodLink = document.getElementById("transportMethodLink");
		var url = transportMethodLink.getAttribute("href") + "&travelMethod=" + airplane.value;
		var request = new XMLHttpRequest();
		request.open("GET", url, false);
		request.send();
		if (request.status == 200) {
			ui.appendHtml(travelMethod, request.responseText);
		} else {
			alert("Error - status: " + request.status + "; URL: " + url);
		}
	}
	
	bus.onclick=function(){
		travelMethodCheck()
		var transportMethodLink = document.getElementById("transportMethodLink");
		var url = transportMethodLink.getAttribute("href") + "&travelMethod=" + bus.value;
		var request = new XMLHttpRequest();
		request.open("GET", url, false);
		request.send();
		if (request.status == 200) {
			ui.appendHtml(travelMethod, request.responseText);
		} else {
			alert("Error - status: " + request.status + "; URL: " + url);
		}
	}
	
	train.onclick=function(){
		travelMethodCheck()
		var transportMethodLink = document.getElementById("transportMethodLink");
		var url = transportMethodLink.getAttribute("href") + "&travelMethod=" + train.value;
		var request = new XMLHttpRequest();
		request.open("GET", url, false);
		request.send();
		if (request.status == 200) {
			ui.appendHtml(travelMethod, request.responseText);
		} else {
			alert("Error - status: " + request.status + "; URL: " + url);
		}
	}
	
	var fullAddressContainer = document.getElementById("fullAddressContainer");
	var partialAddressContainer = document.getElementById("partialAddressContainer");
	var fullAddress = document.getElementById("destinationOption1");
	var partialAddress = document.getElementById("destinationOption2");
	
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
	var existingAddress = document.getElementById("addressOption1");
	var newAddress = document.getElementById("addressOption2");
	
	existingAddress.onclick=function(){
		existingAddressContainer.hidden=false;
		newAddressContainer.hidden=true;
	}
	newAddress.onclick=function(){
		existingAddressContainer.hidden=true;
		newAddressContainer.hidden=false;
	}
	
	var newCityContainer = document.getElementById("newCityContainer");
	var newCityButton = document.getElementById("newCityName");
	var newCityValue="false";
	newCityButton.onclick=function(){
		if(newCityValue=="false"){
			newCityContainer.hidden=false;
			newCityValue="true";
		}
		else{
			newCityContainer.hidden=true;
			newCityValue="false";
		}
	}
	
	var newZipCodeContainer = document.getElementById("newZipCodeContainer");
	var newZipCodeButton = document.getElementById("newZipCodeName");
	var newZipCodeValue="false";
	newZipCodeButton.onclick=function(){
		if(newZipCodeValue=="false"){
			newZipCodeContainer.hidden=false;
			newZipCodeValue="true";
		}
		else{
			newZipCodeContainer.hidden=true;
			newZipCodeValue="false";
		}
	}
	
	function travelMethodCheck(){
		var legend = document.getElementById("legend");
		if(legend!=null){
			var div = document.getElementById("div");
			div.parentNode.removeChild(div);
		}
	}
}