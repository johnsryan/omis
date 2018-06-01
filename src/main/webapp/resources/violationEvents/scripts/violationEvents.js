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
 * Applies violation events screen behavior.
 *
 * @author: Annie Wahl
 * @author: Josh Divine
 * @version: 0.1.1 (May 17, 2018)
 * @since: OMIS 3.0
 */
window.onload = function() {
	applyActionMenu(document.getElementById("actionMenuLink"));
	var rows = document.getElementsByClassName('rowActionMenuItem');
	for(var i = 0; i < rows.length; i++){
		applyActionMenu(rows[i], function() {
			var removeLink = document.getElementsByClassName("removeLink");
			for (var y = 0; y < removeLink.length; y++) {
				removeLink[y].onclick = function() {
					var resolver = new common.MessageResolver("omis.violationevent.msgs.violationEvent");
					var message = resolver.getMessage("removeViolationEventConfirmation", null);
					if (confirm(message)) {
						return true;
					} else {
						return false;
					}
				};
			}
		});
		
	}
}