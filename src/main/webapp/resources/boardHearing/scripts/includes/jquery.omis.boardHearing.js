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
 * Applies jQuery screen behavior.
 *
 * @author: Annie Wahl
 * @author: Josh Divine
 */

function boardHearingNoteItemsCreateOnClick() {
	$("#createBoardHearingNoteItemLink").click(function() {
		$.ajax(config.ServerConfig.getContextPath() + "/boardHearing/createBoardHearingNoteItem.html",
		   {
				type: "GET",
				async: false,
				data: {boardHearingNoteItemIndex: currentBoardHearingNoteItemIndex},
				success: function(data) {
					$("#boardHearingNoteTableBody").append(data);
					boardHearingNoteItemRowOnClick(currentBoardHearingNoteItemIndex);
				},
				error: function(jqXHR, textStatus, errorThrown) {
					alert("Error - status: " + textStatus + "; error: "
						+ errorThrown);
					$("#boardHearingNoteTableBody").html(jqXHR.responseText );
				}
			});
		currentBoardHearingNoteItemIndex++;
		return false;
	});
};

function boardHearingNoteItemRowOnClick(boardHearingNoteItemIndex) {
	assignDatePicker("boardHearingNoteItemDate" + boardHearingNoteItemIndex);
	$("#removeBoardHearingNoteLink" + boardHearingNoteItemIndex).click(function() {
		if ($("#boardHearingNoteOperation" + boardHearingNoteItemIndex).val() == "UPDATE") {
			$("#boardHearingNoteOperation" + boardHearingNoteItemIndex).val("REMOVE");
			$("#boardHearingNoteItemRow" + boardHearingNoteItemIndex).addClass("removeRow");
		} else if($("#boardHearingNoteOperation" + boardHearingNoteItemIndex).val() == "REMOVE") {
			$("#boardHearingNoteOperation" + boardHearingNoteItemIndex).val("UPDATE");
			$("#boardHearingNoteItemRow" +boardHearingNoteItemIndex).removeClass("removeRow");
		} else {
			$("#boardHearingNoteItemRow" + boardHearingNoteItemIndex).remove();
		}
		return false;
	});
};

function applyParoleBoardLocationOnClick() {
	var itinerary = $("#paroleBoardItinerary");
	itinerary.change(function() {
		 if(itinerary.val()){
			 paroleBoardLocationChangeFunction();
		 }
		 else{
			 $("#boardMember1").html('<option value="">...</option>"');
			 $("#boardMember2").html('<option value="">...</option>"');
			 $("#boardMember3").html('<option value="">...</option>"');
		 }
	});
}

function paroleBoardLocationChangeFunction() {
	var itinerary = $("#paroleBoardItinerary");
	$.ajax({
		type: "GET",	
		async: false,
		url: "showBoardMemberOptions.html",
		data:{
			paroleBoardItinerary: itinerary.val()
		},
		success: function(response) {
			 $("#boardMembers").html(response);
		},
		error: function(jqXHR, textStatus, errorThrown) {
			alert("Error - status: " + textStatus + "; error: "
					+ errorThrown);
			 $("#boardMembers").html(jqXHR.responseText);
		}
	});
}

function assignDatePicker(elementId) {
	$("#" + elementId).datepicker({
		changeMonth: true,
		changeYear: true
	});
};