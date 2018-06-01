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
 * Parole eligibility edit screen javascript.
 * 
 * Author: Trevor Isles
 * Author: Annie Wahl
 * Version: 0.1.1 (May 23, 2018)
 * Since: OMIS 3.0
 */
window.onload = function() {
	document.getElementById("eligibilityStatusCategory").onchange = function(event){
		var fields = document.getElementById("statusFields");
		var showFields = document.getElementById("showStatusFields");
		if(event.target.value == "WAIVED" || event.target.value == "INELIGIBLE"){
			showStatusReasons(event.target.value);
			if(fields.classList.contains("hidden")) {
				fields.classList.remove("hidden");
				showFields.value = "true";
			}
		}  else {
			if(!fields.classList.contains("hidden")) {
				fields.classList.add("hidden");
				showFields.value = "false";
			}
		}
	}
	
	applyActionMenu(document.getElementById("actionMenuLink"));
	applyActionMenu(document.getElementById("paroleEligibilityNoteItemsActionMenuLink"), paroleEligibilityNoteItemsActionMenuOnClick);
	assignOnClick();
	applyDatePicker(document.getElementById("hearingEligibilityDate"));
	applyDatePicker(document.getElementById("paroleEligibilityStatusDate"));
	applyDatePicker(document.getElementById("reviewDate"));
	applyFormUpdateChecker(document.getElementById("paroleEligibilityForm"));
};