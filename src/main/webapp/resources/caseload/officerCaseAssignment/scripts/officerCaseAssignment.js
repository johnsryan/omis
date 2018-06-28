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
 * Applies officer case assignment edit screen behavior.
 *
 * @author: Josh Divine
 */
window.onload = function() {
	applyActionMenu(document.getElementById("actionMenuLink"));
	applyDatePicker("startDate");
	applyDatePicker("endDate");
	applyUserSearch(document.getElementById("assignedTo"),
			document.getElementById("officer"),
			document.getElementById("userAccountCurrentLabel"),
			document.getElementById("currentUserAccountLink"), 
			document.getElementById("clearUserLink")); 
	applyOffenderSearch(document.getElementById("offenderSearch"),
			document.getElementById("selectedOffender"),
			document.getElementById("offenderCurrentLabel"),
			document.getElementById("clearOffenderLink"));
};