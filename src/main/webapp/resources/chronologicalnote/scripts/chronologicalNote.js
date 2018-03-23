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
 * Author: Stephen Abson
 * Version: 0.1.0 (February 09, 2018)
 * Since: OMIS 3.0
 */
window.onload = function() {
	
	const TEMPLATE_TEXT_PLACEHOLDER = "<<TEMPLATE>>";
	
	const TEMPLATE = "[" + TEMPLATE_TEXT_PLACEHOLDER + "]: ";
	
	const TEMPLATE_CLOSE = ";";
	
	// Builds template with field name
	function buildTemplate(fieldName) {
		return TEMPLATE.replace(TEMPLATE_TEXT_PLACEHOLDER, fieldName)
			.replace(TEMPLATE_TEXT_PLACEHOLDER, fieldName);
	}
	
	// Builds template with prompt and closing charater
	function buildTemplateWithPromptAndClose(fieldName) {
		return buildTemplate(fieldName) + fieldName + TEMPLATE_CLOSE;
	}
	
	applyActionMenu(document.getElementById("actionMenuLink"));
	applyDatePicker(document.getElementById("date"), null);
	applyTimePicker(document.getElementById("dateTime"));
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
					
					// Checks for category templates for category with "index"
					categoryId = document.getElementById("items" + index + ".category").value;
					var request = new XMLHttpRequest();
					var url = config.ServerConfig.getContextPath() + "/chronologicalNote/findTemplates.json";
					var params = "category=" + categoryId;
					request.open("GET", url + "?" + params + "&timestamp=" + new Date().getTime(), false);
					request.send(null);
					if (request.status == 200) {
						var result = eval("(" + request.responseText + ")");
						if (result != null) {
							var narrative = document.getElementById("narrative");
							var selectStart;
							var selectEnd;
							if (result.groupTemplates != null) {
								selectStart = null;
								selectEnd = null;
								for (var groupTemplateIndex = 0; groupTemplateIndex < result.groupTemplates.length; groupTemplateIndex++) {
									var groupTemplate = result.groupTemplates[groupTemplateIndex];
									if (narrative.value.indexOf(buildTemplate(groupTemplate)) == -1) {
										narrative.value = narrative.value + "\n" + buildTemplateWithPromptAndClose(groupTemplate);
										if (selectStart == null && selectEnd == null) {
											selectStart = narrative.value.lastIndexOf(groupTemplate);
											selectEnd = groupTemplate.length;
										}
									}
								}
							} else {
								selectStart = null;
								selectEnd = null;
							}
							if (result.categoryTemplate != null) {
								if (narrative.value.indexOf(buildTemplate(result.categoryTemplate)) == -1) {
									narrative.value = narrative.value + "\n" + buildTemplateWithPromptAndClose(result.categoryTemplate);
									if (selectStart == null && selectEnd == null) {
										selectStart = narrative.value.lastIndexOf(result.categoryTemplate);
										selectEnd = result.categoryTemplate.length;
									}
								}
							}
							if (selectStart != null && selectEnd != null) {
								narrative.selectionStart = selectStart;
								narrative.selectionEnd = selectStart + selectEnd;
								narrative.focus();
							}
						}
					} else {
						alert("Error - status: " + request.status + "; text: " + request.statusText + "; url: " + url + "; params: " + params);
					}
				} else {
					operationElt.value = "";
				}
			}
			
	    },false);
	}
	var narrative = document.getElementById("narrative");
	narrative.onfocus = function() {
		applySessionExtender(narrative, 
				config.ServerConfig.getContextPath() + "/chronologicalNote/extendSession.html", 
				300000);
	};
	var groupCategoryContainers = document.getElementsByClassName("groupCategoryContainer");
	var groupVisibilityLinks = document.getElementsByClassName("groupVisibilityLink");
	document.getElementById("groupsVisibilityLink").onclick = function() {
		if (document.getElementById("groupsVisibilityLink").classList.contains("expandLink")) {
			document.getElementById("groupsVisibilityLink").classList.add("collapseLink");
			document.getElementById("groupsVisibilityLink").classList.remove("expandLink");
			for (var i = 0; i < groupCategoryContainers.length; i++) {
				groupCategoryContainers[i].classList.remove("hidden");
				
			}
			for (var g = 0; g < groupVisibilityLinks.length; g++) {
				groupVisibilityLinks[g].classList.add("collapseLink");
				groupVisibilityLinks[g].classList.remove("expandLink");
			}
		} else {
			document.getElementById("groupsVisibilityLink").classList.remove("collapseLink");
			document.getElementById("groupsVisibilityLink").classList.add("expandLink");
			for (var t = 0; t < groupCategoryContainers.length; t++) {
				groupCategoryContainers[t].classList.add("hidden");
			}
			for (var g = 0; g < groupVisibilityLinks.length; g++) {
				groupVisibilityLinks[g].classList.remove("collapseLink");
				groupVisibilityLinks[g].classList.add("expandLink");
			}
		}
	}
	var groupVisibilityLinks = document.getElementsByClassName("groupVisibilityLink");
	for (var g = 0; g < groupVisibilityLinks.length; g++) {
		assignGroupVisibilityLinkOnClick(g);
	}
};

function assignGroupVisibilityLinkOnClick(index) {
	var link = document.getElementById("groupVisibilityLink" + index);
	link.onclick = function() {
		var groupContainer = document.getElementById("groupCategoryContainer" + index);
		toggleElementVisibility(groupContainer);
		link.classList.toggle("collapseLink");
		link.classList.toggle("expandLink");
	};
}

function toggleElementVisibility(elt) {
	elt.classList.toggle("hidden");
}