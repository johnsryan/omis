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
 * Jquery implementation of functions for boardConsideration.js
 * 
 * @author: Josh Divine
 * @version: 0.1.0 (May 31, 2018)
 * @since: OMIS 3.0
 */
function boardConsiderationsCreateOnClick() {
	$("#createBoardConsiderationItemLink").click(function() {
		$.ajax(config.ServerConfig.getContextPath() + "/boardConsideration/addBoardConsideration.html",
		   {
				type: "GET",
				async: false,
				data: {boardConsiderationIndex: currentBoardConsiderationIndex},
				success: function(data) {
					$("#boardConsiderationTableBody").append(data);
					boardConsiderationItemRowOnClick(currentBoardConsiderationIndex);
				},
				error: function(jqXHR, textStatus, errorThrown) {
					alert("Error - status: " + textStatus + "; error: "
						+ errorThrown);
					$("#boardConsiderationTableBody").html(jqXHR.responseText );
				}
			});
		currentBoardConsiderationIndex++;
		return false;
	});
};

function boardConsiderationItemRowOnClick(boardConsiderationIndex) {
	$("#removeBoardConsiderationLink" + boardConsiderationIndex).click(function() {
		if ($("#boardConsiderationOperation" + boardConsiderationIndex).val() == "EDIT") {
			$("#boardConsiderationOperation" + boardConsiderationIndex).val("REMOVE");
			$("#boardConsiderationItemRow" + boardConsiderationIndex).addClass("removeRow");
		} else if($("#boardConsiderationOperation" + boardConsiderationIndex).val() == "REMOVE") {
			$("#boardConsiderationOperation" + boardConsiderationIndex).val("EDIT");
			$("#boardConsiderationItemRow" +boardConsiderationIndex).removeClass("removeRow");
		} else {
			$("#boardConsiderationItemRow" + boardConsiderationIndex).remove();
		}
		return false;
	});
};

function boardConsiderationNotesCreateOnClick() {
	$("#createBoardConsiderationNoteItemLink").click(function() {
		$.ajax(config.ServerConfig.getContextPath() + "/boardConsideration/addBoardConsiderationNote.html",
		   {
				type: "GET",
				async: false,
				data: {boardConsiderationNoteIndex: currentBoardConsiderationNoteIndex},
				success: function(data) {
					$("#boardConsiderationNoteTableBody").append(data);
					boardConsiderationNoteItemRowOnClick(currentBoardConsiderationNoteIndex);
				},
				error: function(jqXHR, textStatus, errorThrown) {
					alert("Error - status: " + textStatus + "; error: "
						+ errorThrown);
					$("#boardConsiderationNoteTableBody").html(jqXHR.responseText );
				}
			});
		currentBoardConsiderationNoteIndex++;
		return false;
	});
};

function boardConsiderationNoteItemRowOnClick(boardConsiderationNoteIndex) {
	assignDatePicker("boardConsiderationNoteItemDate" + boardConsiderationNoteIndex);
	$("#removeBoardConsiderationNoteLink" + boardConsiderationNoteIndex).click(function() {
		if ($("#boardConsiderationNoteOperation" + boardConsiderationNoteIndex).val() == "EDIT") {
			$("#boardConsiderationNoteOperation" + boardConsiderationNoteIndex).val("REMOVE");
			$("#boardConsiderationNoteItemRow" + boardConsiderationNoteIndex).addClass("removeRow");
		} else if($("#boardConsiderationNoteOperation" + boardConsiderationNoteIndex).val() == "REMOVE") {
			$("#boardConsiderationNoteOperation" + boardConsiderationNoteIndex).val("EDIT");
			$("#boardConsiderationNoteItemRow" +boardConsiderationNoteIndex).removeClass("removeRow");
		} else {
			$("#boardConsiderationNoteItemRow" + boardConsiderationNoteIndex).remove();
		}
		return false;
	});
};

function assignDatePicker(elementId) {
	$("#" + elementId).datepicker({
		changeMonth: true,
		changeYear: true
	});
};