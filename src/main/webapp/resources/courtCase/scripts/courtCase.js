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
 * Court case detail form behavior.
 * 
 * @author Stephen Abson
 * @author Joel Norris
 * @author Ryan Johns
 * @author Josh Divine
 * @version 0.1.4 (Feb 6, 2018)
 * @since OMIS 3.0
 */

/** Assign initial element behavior. */
window.addEventListener("load", function() {
	applyActionMenu(document.getElementById("actionMenuLink"));
	applyFormUpdateChecker(document.getElementById("courtCaseForm"));
	applyValueLabelAutoComplete(document.getElementById("judgeQuery"), document.getElementById("judge"), config.ServerConfig.getContextPath() + "/courtCase/searchJudges.json");
	applyClear(document.getElementById("judgeClear"), document.getElementById("judgeQuery"), document.getElementById("judge"), document.getElementById("judgeDisplay"));
	applyDatePicker(document.getElementById("pronouncementDate"));
	applyDatePicker(document.getElementById("sentenceReviewDate"));
	applyCourtCaseBehavior();
	applyActionMenu(document.getElementById("chargesActionMenuLink"), chargeActionMenuOnClick);
	applyActionMenu(document.getElementById("courtCaseNotesActionMenuLink"), courtCaseNoteActionMenuOnClick);
	var allowExistingDocket = document.getElementById("allowExistingDocket");
	var allowDocketFields = document.getElementById("allowDocket");
	if (allowExistingDocket.value && allowDocketFields.value) {
		document.getElementById("existingDocket").onchange = function() {
			var existingDocketValue = this.options[this.selectedIndex].value;
			var docketValue = document.getElementById("docketFields.value");
			var court = document.getElementById("docketFields.court");
			if (existingDocketValue != null && existingDocketValue != "") {
				docketValue.disabled = true;
				court.disabled = true;
			} else {
				docketValue.disabled = false;
				court.disabled = false;
			}
		};
	}
});

/**
 * Clears the data associated with the specified controls
 * @param clear button that on click will be added to
 * @param query query input control
 * @param value hidden value control
 * @param display display control
 */
function applyClear(clear, query, value, display) {
	clear.onclick = function() {
		query.value = "";
		value.value = "";
		display.innerHTML = "";
		return false;
	};
};

/**
 * Clears the data associated with the specified controls
 * @param clear button that on click will be added to
 * @param query query input control
 * @param value hidden value control
 */
function applyClear(clear, query, value) {
	clear.onclick = function() {
		query.value = "";
		value.value = "";
		return false;
	};
};