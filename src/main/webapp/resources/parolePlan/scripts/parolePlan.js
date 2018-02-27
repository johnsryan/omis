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
 * Parole plan detail screen behavior.
 * 
 * Author: Josh Divine
 * Version: 0.1.0 (Feb 13, 2018)
 * Since: OMIS 3.0
 */
window.onload = function() {	
	applyActionMenu(document.getElementById("actionMenuLink"));
	applyStaffAssignmentSearch(document.getElementById("evaluatorInput"), 
			document.getElementById("evaluator"),
			document.getElementById("evaluatorDisplay"),
			document.getElementById("evaluatorCurrent"),
			document.getElementById("evaluatorClear"));
	applyActionMenu(document.getElementById("parolePlanNotesActionMenuLink"), parolePlanNoteActionMenuOnClick);
	for (var index = 0; index < currentParolePlanNoteIndex; index++) {
		parolePlanNoteRowOnClick(index);
	}
	parolePlanDocumentAssociationItemsCreateOnClick();
	for(var i = 0; i < currentDocumentAssociationItemIndex; i++){
		parolePlanDocumentAssociationItemRowOnClick(i);
		for(var j = 0; j < currentDocumentTagItemIndexes[i]; j++){
			documentTagItemRowOnClick(i,j);
		}
	}
	applyFormUpdateChecker(document.getElementById("parolePlanForm"));
};