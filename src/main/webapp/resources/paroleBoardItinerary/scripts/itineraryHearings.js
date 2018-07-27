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
 * Itinerary hearings listing screen behavior.
 * 
 * Author: Josh Divine
 * Version: 0.1.0 Jul 23, 2018
 * Since: OMIS 3.0
 */
window.onload = function()  {
	var actionMenu = document.getElementById("actionMenuLink");
	if (actionMenu != null) {
		applyActionMenu(actionMenu);
	}
	var tableBodies = document.getElementsByTagName("tbody");
	for (var tbIndex = 0; tbIndex < tableBodies.length; tbIndex++) {
		var rowLinks = tableBodies[tbIndex].getElementsByTagName("a");
		for (var rowLinkIndex = 0; rowLinkIndex < rowLinks.length; rowLinkIndex++) {
			var rowLink = rowLinks[rowLinkIndex];
			if (rowLink.getAttribute("class") != null && rowLink.getAttribute("class").indexOf("actionMenuItem") > -1) {
				applyActionMenu(rowLink, function() {
					applyRemoveLinkConfirmation();
				});
			}
		}
	}
};