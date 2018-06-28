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
 * Behavior for create/edit screen for historical offense terms.
 * 
 * @author Stephen Abson
 */
window.onload = function() {
	
	// Shows sentence term (prison, probation, deferred)
	function showSentenceTermFields(termName) {
		var offenseTerm = document.getElementById("sentenceFields." + termName + "Term");
		if (ui.hasClass(offenseTerm, "hidden")) {
			ui.removeClass(offenseTerm, "hidden");
		}
	}
	
	// Hides sentence term (prison, probation, deferred)
	function hideSentenceTermFields(termName) {
		var offenseTerm = document.getElementById("sentenceFields." + termName + "Term");
		if (!ui.hasClass(offenseTerm, "hidden")) {
			ui.addClass(offenseTerm, "hidden");
		}
	}
	
	// Applies term fields behavior
	function applyTermFieldsBehavior(termName) {
	
		// Action to fire when term fields are changed
		function onchangeTermDay(yearsElt, monthsElt, daysElt, totalDaysElt, totalDaysLabelElt) {
			var url = config.ServerConfig.getContextPath() + "/offenseTerm/calculateHistoricalTotalDays.json";
			var params = "years=" + yearsElt.value + "&months=" + monthsElt.value + "&days=" + daysElt.value;
			var request = new XMLHttpRequest();
			request.open("get", url + "?" + params, false);
			request.send(null);
			if (request.status == 200) {
				var totalDays = eval("(" + request.responseText + ")");
				totalDaysElt.value = totalDays;
				var messageResolver = new common.MessageResolver("omis.sentence.msgs.sentence");
				totalDaysLabelElt.innerHTML = messageResolver.getMessage("totalDaysLabel", {param0: totalDays});
			} else {
				alert("Error - status: " + request.status + "; url: " + url + "?" + params);
			}
		}
		
		// Applies change behavior to years field
		document.getElementById("sentenceFields." + termName + "Years").onchange = function() {
			var termName = this.getAttribute("id").replace("sentenceFields.", "").replace("Years", "");
			var monthsElt = document.getElementById("sentenceFields." + termName + "Months");
			var daysElt = document.getElementById("sentenceFields." + termName + "Days");
			var totalDaysElt = document.getElementById("sentenceFields." + termName + "TotalDays");
			var totalDaysLabelElt = document.getElementById("sentenceFields." + termName + "TotalDaysLabel");
			onchangeTermDay(this, monthsElt, daysElt, totalDaysElt, totalDaysLabelElt);
			return false;
		};
		
		// Applies change behavior to months field
		document.getElementById("sentenceFields." + termName + "Months").onchange = function() {
			var termName = this.getAttribute("id").replace("sentenceFields.", "").replace("Months", "");
			var yearsElt = document.getElementById("sentenceFields." + termName + "Years");
			var daysElt = document.getElementById("sentenceFields." + termName + "Days");
			var totalDaysElt = document.getElementById("sentenceFields." + termName + "TotalDays");
			var totalDaysLabelElt = document.getElementById("sentenceFields." + termName + "TotalDaysLabel");
			onchangeTermDay(yearsElt, this, daysElt, totalDaysElt, totalDaysLabelElt);
			return false;
		};
		
		// Applies change behavior to days field
		document.getElementById("sentenceFields." + termName + "Days").onchange = function() {
			var termName = this.getAttribute("id").replace("sentenceFields.", "").replace("Days", "");
			var yearsElt = document.getElementById("sentenceFields." + termName + "Years");
			var monthsElt = document.getElementById("sentenceFields." + termName + "Months");
			var totalDaysElt = document.getElementById("sentenceFields." + termName + "TotalDays");
			var totalDaysLabelElt = document.getElementById("sentenceFields." + termName + "TotalDaysLabel");
			onchangeTermDay(yearsElt, monthsElt, this, totalDaysElt, totalDaysLabelElt);
			return false;
		};
	}
	
	document.getElementById("sentenceFields.category").onchange = function() {
		if (this.selectedIndex > -1 && this.options[this.selectedIndex].value != null && this.options[this.selectedIndex].value != "") {
			var categoryId = this.options[this.selectedIndex].value;
			var request = new XMLHttpRequest();
			request.open("get", config.ServerConfig.getContextPath() + "/offenseTerm/findHistoricalSentenceCategory.json?sentenceCategory=" + categoryId, false);
			request.send(null);
			var category = eval("(" + request.responseText + ")");
			if (category.prisonRequirement == "REQUIRED" || category.prisonRequirement == "OPTIONAL") {
				showSentenceTermFields("prison");
			} else {
				hideSentenceTermFields("prison");	
			}
			if (category.probationRequirement == "REQUIRED" || category.probationRequirement == "OPTIONAL") {
				showSentenceTermFields("probation");
			} else {
				hideSentenceTermFields("probation");
			}
			if (category.deferredRequirement == "REQUIRED" || category.deferredRequirement == "OPTIONAL") {
				showSentenceTermFields("deferred");
			} else {
				hideSentenceTermFields("deferred");
			}
		} else {
			hideSentenceTermFields("prison");
			hideSentenceTermFields("probation");
			hideSentenceTermFields("deferred");
		}
	};
	document.getElementById("sentenceFields.calculateEffectiveDateLink").onclick = function() {
		var pronouncementDate = document.getElementById("sentenceFields.pronouncementDate").value;
		var jailTimeCredit = document.getElementById("sentenceFields.jailTimeCredit").value;
		var streetTimeCredit = document.getElementById("sentenceFields.streetTimeCredit").value;
		if (pronouncementDate != null && pronouncementDate != "" && jailTimeCredit != null && jailTimeCredit != "" && streetTimeCredit != null && streetTimeCredit != "") {
			var url = config.ServerConfig.getContextPath() + "/offenseTerm/calculateHistoricalSentenceEffectiveDate.json";
			var params = "pronouncementDate=" + pronouncementDate + "&jailTimeCredit=" + jailTimeCredit + "&streetTimeCredit=" + streetTimeCredit;
			var request = new XMLHttpRequest();
			request.open("get", url + "?" + params, false);
			request.send(null);
			if (request.status == 200) {
				document.getElementById("sentenceFields.effectiveDate").value = eval("(" + request.responseText + ")");
			} else {
				alert("Error - status: " + request.status + "; URL: " + url + "; params: " + params);
			}
		} else {
			var messageResolver = new common.MessageResolver("omis.offenseterm.msgs.offenseTerm");
			alert(messageResolver.getMessage("sentenceEffectiveDateCalculationRequirementsMessage"));
		}
		return false;
	};
	applyDatePicker(document.getElementById("sentenceFields.pronouncementDate"));
	applyDatePicker(document.getElementById("sentenceFields.effectiveDate"));
	applyDatePicker(document.getElementById("sentenceFields.turnSelfInDate"));
	applyTermFieldsBehavior("prison");
	applyTermFieldsBehavior("probation");
	applyTermFieldsBehavior("deferred");
	applyActionMenu(document.getElementById("actionMenuLink"));
};