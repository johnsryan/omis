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
 * Jquery implementation of functions for hearingAnalysis.js
 * 
 * @author: Josh Divine
 * @version: 0.1.0 (Dec 6, 2017)
 * @since: OMIS 3.0
 */
function applyHearingAnalysisBehavior() {
	for (var index = 0; index < currentHearingAnalysisNoteIndex; index++) {
		hearingAnalysisNoteRowOnClick(index);
	}
	
	boardItineraryOnChange();
}

function boardItineraryOnChange() {
	$("#boardItinerary").change(function() {
		$.ajax(config.ServerConfig.getContextPath() + "/hearingAnalysis/findBoardAttendeesForItinerary.html",
				   {
					   type: "GET",
					   async: false,
					   data: { itinerary: this.options[this.selectedIndex].value },
					   success: function(data) {
					   		$("#analyst").empty().append(data);
					   },
					   error: function(jqXHR, textStatus, errorThrown) {
							alert("Error - status: " + textStatus + "; error: "
									+ errorThrown);
					   }
					   
				   }
				);
	});
}

/**
 * Assigns on click functionality for the hearing analysis note items action 
 * menu.
 */
function hearingAnalysisNoteActionMenuOnClick() {
	$("#createHearingAnalysisNoteLink").click(function() {
		$.ajax(config.ServerConfig.getContextPath() + "/hearingAnalysis/addHearingAnalysisNote.html",
			   {
				   type: "GET",
				   async: false,
				   data: { hearingAnalysisNoteIndex: currentHearingAnalysisNoteIndex },
				   success: function(data) {
				   		$("#hearingAnalysisNotes").append(data);
				   		hearingAnalysisNoteRowOnClick(currentHearingAnalysisNoteIndex);
				   },
				   error: function(jqXHR, textStatus, errorThrown) {
						alert("Error - status: " + textStatus + "; error: "
								+ errorThrown);
							$("#hearingAnalysisNotes").html(jqXHR.responseText);
				   }
				   
			   }
			);
			currentHearingAnalysisNoteIndex++;
   			return false;
	});
}

/**
 * Assigns on click functionality for the hearing analysis note item row with 
 * the specified hearing analysis note index.
 * 
 * @param hearingAnalysisNoteIndex hearing analysis note index
 */
function hearingAnalysisNoteRowOnClick(hearingAnalysisNoteIndex) {
	applyDatePicker("#hearingAnalysisNoteDate" + hearingAnalysisNoteIndex);
	$("#removeNoteLink" + hearingAnalysisNoteIndex).click(function() {
		if ($("#hearingAnalysisNoteOperation" + hearingAnalysisNoteIndex).val() == "EDIT") {
			$("#hearingAnalysisNoteOperation" + hearingAnalysisNoteIndex).val("REMOVE");
			$("#hearingAnalysisNoteRow" + hearingAnalysisNoteIndex).addClass("removeRow");
		} else if($("#hearingAnalysisNoteOperation" + hearingAnalysisNoteIndex).val() == "REMOVE") {
			$("#hearingAnalysisNoteOperation" + hearingAnalysisNoteIndex).val("EDIT");
			$("#hearingAnalysisNoteRow" + hearingAnalysisNoteIndex).removeClass("removeRow");
		} else {
			$("#hearingAnalysisNoteRow" + hearingAnalysisNoteIndex).remove();
		}
		return false;
	});
}