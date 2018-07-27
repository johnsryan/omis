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
window.onload = function() {
	applyActionMenu(document.getElementById("actionMenuLink"));
	applyModifyLinks();
	if(document.getElementById("userAttendanceItemsActionMenuLink")) {
		applyActionMenu(document.getElementById("userAttendanceItemsActionMenuLink"), userAttendanceItemsCreateOnClick);
	}

	for (var index = 0; index < currentUserAttendanceItemIndex; index++) {
		userAttendanceItemRowOnClick(index);
	}

	var dateInputs = document.getElementsByClassName("date");
	for(var i = 0; i < dateInputs.length; i++){
		applyDatePicker(dateInputs[i]);
	}
	
	var dispositionInputs = document.getElementsByClassName("disposition");
	for(var i = 0; i < dispositionInputs.length; i++){
		dispositionInputs[i].onchange = function(event){
			if(event.target.name.indexOf('[') > -1){
				var j = event.target.name.split('[')[1].split(']')[0];
				if(event.target.value == "GUILTY" || event.target.value == "REDUCED"){
					document.getElementById("sanction"+j).className = document.getElementById("sanction"+j).className.replace(/\bhidden\b/g, "");
				}
				else{
					document.getElementById("sanction"+j).className +=" hidden";
					document.getElementById("violationItems"+j+".sanction").value = '';
				}
			}
			else{
				var j = 0;
				if(event.target.value == "GUILTY" || event.target.value == "REDUCED"){
					document.getElementById("sanction"+j).className = document.getElementById("sanction"+j).className.replace(/\bhidden\b/g, "");
				}
				else{
					document.getElementById("sanction"+j).className += " hidden";;
					document.getElementById("violationItem.sanction").value = '';
				}
			}
		}
	}
	var descriptions = [];
	var violations = [];
	var dupDescriptionCheck = [];
	var hideButtons = document.getElementsByClassName("hideOverflow");
	var showButtons = document.getElementsByClassName("showOverflow");
	var items = document.getElementsByClassName("violationItem");
	var firstDescription = items[0].getElementsByClassName('violationDescription')[0];
	//var firstViolation = items[0].getElementsByClassName('violation')[0];
	var groupDescription = document.getElementById('groupViolationDescription');
	var groupViolation = document.getElementById('groupViolations');
	var groupEdit = document.getElementsByName("groupEdit");
	
	for(var i = 0; i < items.length; i++){
		var descriptionText = items[i].getElementsByClassName('violationDescriptionNoOverflow')[0];
		if(descriptionText.scrollWidth > descriptionText.offsetWidth){
			showButtons[i].style.display = 'inline-block';
			hideButtons[i].style.display = 'inline-block';
		}
		
		violations[i] = items[i].getElementsByClassName('violation')[0].innerHTML;
		descriptions[i] = items[i].getElementsByClassName('violationDescription')[0].innerHTML;
		
		if(document.getElementById("violationItems["+i+"].authority") != null){
			applyStaffSearch(document.getElementById("authorityInput" + i),
					document.getElementById("violationItems["+i+"].authority"),
					document.getElementById("authorityDisplay" + i),
					null,
					document.getElementById("clearAuthority" + i));
		}
		else if(document.getElementById("violationItem.authority") != null){
			applyStaffSearch(document.getElementById("authorityInput"),
					document.getElementById("violationItem.authority"),
					document.getElementById("authorityDisplay"),
					null,
					document.getElementById("clearAuthority"));
		}
	}
	if(groupDescription != null){
		for(var i = 0; i < descriptions.length; i++){
			var notDuplicate = true;
			for(var j = 0; j < dupDescriptionCheck.length; j++){
				if(descriptions[i] == dupDescriptionCheck[j]){
					notDuplicate = false;
					break;
				}
			}
			if(notDuplicate == true){
				groupDescription.innerHTML += descriptions[i];
				dupDescriptionCheck.push(descriptions[i]);
			}
		}
		
		for(var i = 0; i < groupEdit.length; i++){
			groupEdit[i].onchange = function(event){
				for(var j = 0; j < items.length; j++){
					var modificationList;
					if (document.getElementById("violationItems"+j+".adjustedDisciplinaryCode")) {
						modificationList = document.getElementById("violationItems"+j+".adjustedDisciplinaryCode");
					} else if (document.getElementById("violationItems"+j+".adjustedCondition")) {
						modificationList = document.getElementById("violationItems"+j+".adjustedCondition");
					}
					
					if (modificationList) {
						for (var k = 0; k < modificationList.getElementsByTagName("option").length; k++) {
							if (modificationList.getElementsByTagName("option")[k].value == modificationList.value) {
								modificationList.getElementsByTagName("option")[k].setAttribute("selected", "selected");
							} else {
								modificationList.getElementsByTagName("option")[k].removeAttribute("selected");
							}
						
						}
					}
					
					if(event.target.value == 'true'){
						if (j > 0) {
							items[j].style.display = 'none';
						}
						groupViolation.innerHTML +=
							"<div id=\"violation" + j + "\" class=\"violation\">"
							+ items[j].getElementsByClassName('violation')[0].innerHTML
							+ "</div>";
						items[j].getElementsByClassName('violation')[0].innerHTML = '';
					}
					else{
						items[j].getElementsByClassName('violation')[0].innerHTML = groupViolation.getElementsByClassName('violation')[j].innerHTML;
						items[j].style.display = 'block';}
				}
				if(event.target.value == 'true'){
					firstDescription.style.display = 'none';
				//firstViolation.style.display = 'none';
					groupDescription.style.display = 'block';
					groupViolation.style.display = 'block';
				}
				else{
					groupDescription.style.display = 'none';
					groupViolation.style.display = 'none';
					firstDescription.style.display = 'block';
				//	firstViolation.style.display = 'block';
					groupViolation.innerHTML = '';
				}
				applyModifyLinks();
			}
			if(groupEdit[i].checked){
				if(groupEdit[i].value == 'true'){
					for(var j = 0; j < items.length; j++){
							items[j].style.display = 'none';
					}
					firstDescription.style.display = 'none';
					firstViolation.style.display = 'none';
					groupDescription.style.display = 'block';
					groupViolation.style.display = 'block';
				}
			}
		}
	}

	for(var i = 0; i < showButtons.length; i++){
		showButtons[i].onclick = function() {
			this.parentElement.getElementsByClassName('violationDescriptionNoOverflow')[0].className = "violationDescriptionShow";
			this.style.display = 'none';
		}
		hideButtons[i].onclick = function(){
			this.parentElement.className = "violationDescriptionNoOverflow";
			this.parentElement.parentElement.getElementsByClassName("showOverflow")[0].style.display = 'inline-block';
		}
	}
}

function applyModifyLinks() {
	var modifyViolationLinks = document.getElementsByClassName("modifyViolationLink");
	for (var i = 0; i < modifyViolationLinks.length; i++) {
		modifyViolationLinks[i].onclick = function(e) {
			e.preventDefault();
			if (document.getElementById("violationItems" + e.target.id.split("modifyViolationLink")[1] + ".adjusted").value == "true") {
				document.getElementById("violationItems" + e.target.id.split("modifyViolationLink")[1] + ".adjusted").value = "false";
				document.getElementById("violationModification" + e.target.id.split("modifyViolationLink")[1]).classList.add("hidden");
			} else {
				document.getElementById("violationItems" + e.target.id.split("modifyViolationLink")[1] + ".adjusted").value = "true";
				document.getElementById("violationModification" + e.target.id.split("modifyViolationLink")[1]).classList.remove("hidden");
			}
		}
	}
}