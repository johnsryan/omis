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
 * Chronological note list screen java script.
 * 
 * Author: Yidong Li
 * Version: 0.1.0 (Feb 6, 2018)
 * Since: OMIS 3.0
 */
window.onload = function() {
	applyActionMenu(document.getElementById("chronologicalNoteListActionMenuLink"));
	var chronologicalNoteRows = document.getElementsByClassName("chronologicalNoteListRowActionMenuItem");
	for(var x =0; x < chronologicalNoteRows.length; x++) {
		applyActionMenu(chronologicalNoteRows[x], function() {applyRemoveLinkConfirmation();});
	}
	var groupCategoryContainers = document.getElementsByClassName("groupCategoryContainer");
	var groupVisibilityLinks = document.getElementsByClassName("groupVisibilityLink");
	document.getElementById("groupsVisibilityLink").onclick = function() {
		if (document.getElementById("groupsVisibilityLink").classList.contains("expandAll")) {
			document.getElementById("groupsVisibilityLink").classList.add("collapseAll");
			document.getElementById("groupsVisibilityLink").classList.remove("expandAll");
			for (var i = 0; i < groupCategoryContainers.length; i++) {
				groupCategoryContainers[i].classList.remove("hidden");	
			}
		} else {
			document.getElementById("groupsVisibilityLink").classList.remove("collapseAll");
			document.getElementById("groupsVisibilityLink").classList.add("expandAll");
			for (var t = 0; t < groupCategoryContainers.length; t++) {
				groupCategoryContainers[t].classList.add("hidden");
			}
		}
	}
	var groupVisibilityLinks = document.getElementsByClassName("groupVisibilityLink");
	for (var g = 0; g < groupVisibilityLinks.length; g++) {
		assignGroupVisibilityLinkOnClick(g);
	}
}

function assignGroupVisibilityLinkOnClick(index) {
	var link = document.getElementById("groupVisibilityLink" + index);
	link.onclick = function() {
		var groupContainer = document.getElementById("groupCategoryContainer" + index);
		toggleElementVisibility(groupContainer);
	};
}

function toggleElementVisibility(elt) {
	elt.classList.toggle("hidden");
}