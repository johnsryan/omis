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
 * Applies hearing analysis home screen behavior.
 *
 * @author: Josh Divine
 * Version: 0.1.1 (Feb 20, 2018)
 * Since: OMIS 3.0
 */
window.onload = function() {
	applyActionMenu(document.getElementById("actionMenuLink"));
	assignNewTabLinks();
	
	var taskCheckBoxes = document.getElementsByClassName("taskCheckBox");
	for(var i = 0, taskCheckBox; taskCheckBox = taskCheckBoxes[i]; i++){
		taskCheckBoxOnClick(taskCheckBox);
	}
	applyActionMenu(document.getElementById("hearingAnalysisNotesActionMenuLink"), hearingAnalysisNoteActionMenuOnClick);
	for (var index = 0; index < currentHearingAnalysisNoteIndex; index++) {
		hearingAnalysisNoteRowOnClick(index);
	}
};