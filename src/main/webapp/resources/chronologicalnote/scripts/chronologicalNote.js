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
 * Chronological note edit screen java script.
 * 
 * Author: Joel Norris
 * Version: 0.1.0 (February 09, 2018)
 * Since: OMIS 3.0
 */

window.onload = function() {
	applyDatePicker(document.getElementById("date"), null);
	applyTextCounter(document.getElementById("narrative"), document.getElementById("narrativeCharacterCounterContainer"));
	var categoryItemCheckBoxes = document.getElementsByClassName("categoryItemCheckBox");
	for(var x = 0; x < categoryItemCheckBoxes.length; x++) {
		categoryItemCheckBoxes[x].addEventListener("click", function(){
			var index = this.id.replace("categoryItemCheckBox", "");
			var associated = document.getElementById("categoryItemAssociated" + index).value;
			var operationElt = document.getElementById("categoryItemOperation" + index);
			if (associated == "true") {
				if (this.checked) {
					operationElt.value = "";
				} else {
					operationElt.value = "DISSOCIATE";
				}
			} else {
				if (this.checked) {
					operationElt.value = "ASSOCIATE";
				} else {
					operationElt.value = "";
				}
			}
	    },false);
	}
	
}


/*
 * On click functionality for category item check box.
 * 
 * @param elt check box element
 */
function categoryCheckboxOnClick(elt) {
	var index = elt.id.replace("categoryItemCheckBox", "");
	var associated = document.getElementById("categoryItemAssociated").value;
	var operationElt = document.getElementById("categoryItemOperation");
	if (associated) {
		if (elt.checked) {
			operationElt.value = "";
		} else {
			operationElt.value = "DISSOCIATE";
		}
	} else {
		if (elt.checked) {
			operationElt.value = "ASSOCIATE";
		} else {
			operationElt.value = "";
		}
	}
	document.getElementById("categoryItemOperation" + index).value 
}