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
 * Behavior for offense term list screen.
 * 
 * <p>Offense term list screen reports court cases, convictions and sentences.
 * 
 * @author: Stephen Abson
 * @author: Josh Divine
 */
window.onload = function() {
	applyRemoveLinkConfirmation();
	applyActionMenu(document.getElementById("actionMenuLink"));
	var courtCasesTableBody = document.getElementById("courtCases");
	var rowLinks = courtCasesTableBody.getElementsByTagName("a");
	for (var rowLinkIndex = 0; rowLinkIndex < rowLinks.length; rowLinkIndex++) {
		var rowLink = rowLinks[rowLinkIndex];
		if (rowLink.getAttribute("class") != null && rowLink.getAttribute("class").indexOf("actionMenuItem") > -1) {
			applyActionMenu(rowLink, function() {
				var dismissDocket = document.getElementsByClassName("dismissDocketLink");
				for (var x = 0; x < dismissDocket.length; x++) {
					dismissDocket[x].onclick = function() {
						var resolver = new common.MessageResolver("omis.offenseterm.msgs.offenseTerm");
						var message = resolver.getMessage("confirmDocketDismiss", null);
						if (confirm(message)) {
							return true;
						} else {
							return false;
						}
					};
				}
				applyRemoveLinkConfirmation();
			});
		}
	}
};