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
				if(event.target.value == "GUILTY"){
					document.getElementById("sanction"+j).className = document.getElementById("sanction"+j).className.replace(/\bhidden\b/g, "");
				}
				else{
					document.getElementById("sanction"+j).className +=" hidden";
					document.getElementById("violationItems"+j+".sanction").value = '';
				}
			}
			else{
				var j = 0;
				if(event.target.value == "GUILTY"){
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
	var firstViolation = items[0].getElementsByClassName('violation')[0];
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
			groupViolation.innerHTML += violations[i];
		}
		
		for(var i = 0; i < groupEdit.length; i++){
			groupEdit[i].onchange = function(event){
				for(var j = 1; j < items.length; j++){
					if(event.target.value == 'true'){
						items[j].style.display = 'none';
					}
					else{
						items[j].style.display = 'block';
					}
				}
				if(event.target.value == 'true'){
					firstDescription.style.display = 'none';
					firstViolation.style.display = 'none';
					groupDescription.style.display = 'block';
					groupViolation.style.display = 'block';
				}
				else{
					groupDescription.style.display = 'none';
					groupViolation.style.display = 'none';
					firstDescription.style.display = 'block';
					firstViolation.style.display = 'block';
				}
			}
			if(groupEdit[i].checked){
				if(groupEdit[i].value == 'true'){
					for(var j = 1; j < items.length; j++){
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