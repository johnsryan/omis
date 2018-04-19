/**
 * Jquery implementation of functions for paroleBoardItinerary.js
 * 
 * @author: Josh Divine
 * @version: 0.1.0 (Dec 6, 2017)
 * @since: OMIS 3.0
 */
function applyBoardItineraryBehavior() {
	for (var index = 0; index < currentBoardMeetingSiteIndex; index++) {
		boardMeetingSiteRowOnClick(index);
	}
	for (var index = 0; index < currentBoardItineraryNoteIndex; index++) {
		boardItineraryNoteRowOnClick(index);
	}
	
	startDateOnChange();
}

function startDateOnChange() {
	$("#startDate").change(function() {
		$.ajax(config.ServerConfig.getContextPath() + "/paroleBoardItinerary/findBoardMembersOnDate.html",
			   {
				   type: "GET",
				   async: false,
				   data: { date: this.value },
				   success: function(data) {
				   		$("#boardMember1").empty().append(data);
				   		$("#boardMember2").empty().append(data);
				   		$("#boardMember3").empty().append(data);
				   		$("#boardMemberAlternate").empty().append(data);
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
 * Assigns on click functionality for the parole board itinerary note items 
 * action menu.
 */
function boardItineraryNoteActionMenuOnClick() {
	$("#createBoardItineraryNoteLink").click(function() {
		$.ajax(config.ServerConfig.getContextPath() + "/paroleBoardItinerary/addBoardItineraryNote.html",
			   {
				   type: "GET",
				   async: false,
				   data: { boardItineraryNoteIndex: currentBoardItineraryNoteIndex },
				   success: function(data) {
				   		$("#boardItineraryNotes").append(data);
				   		boardItineraryNoteRowOnClick(currentBoardItineraryNoteIndex);
				   },
				   error: function(jqXHR, textStatus, errorThrown) {
						alert("Error - status: " + textStatus + "; error: "
								+ errorThrown);
							$("#boardItineraryNotes").html(jqXHR.responseText);
				   }
				   
			   }
			);
			currentBoardItineraryNoteIndex++;
   			return false;
	});
}

/**
 * Assigns on click functionality for the board meeting site items action menu.
 */
function boardMeetingSiteActionMenuOnClick() {
	boardMeetingSiteAddOnClick("#createBoardMeetingSiteSecureFacilityLink", "SECURE_FACILITY");
	boardMeetingSiteAddOnClick("#createBoardMeetingSiteJailLink", "JAIL");
	boardMeetingSiteAddOnClick("#createBoardMeetingSitePrereleaseLink", "PRERELEASE");
	boardMeetingSiteAddOnClick("#createBoardMeetingSiteCommunitySupervisionOfficeLink", "COMMUNITY_SUPERVISION_OFFICE");
	boardMeetingSiteAddOnClick("#createBoardMeetingSiteTreatementAndSanctionCenterLink", "TREATMENT_AND_SANCTION_CENTER");
}

/**
 * Assigns on click functionality for the specified link.
 * 
 * @param linkId link to add on click to
 * @param locationType enum value to pass
 */
function boardMeetingSiteAddOnClick(linkId, locationType) {
	$(linkId).click(function() {
		$.ajax(config.ServerConfig.getContextPath() + "/paroleBoardItinerary/addBoardMeetingSite.html",
			   {
				   type: "GET",
				   async: false,
				   data: { boardMeetingSiteIndex: currentBoardMeetingSiteIndex, boardMeetingSiteLocation: locationType },
				   success: function(data) {
				   		$("#boardMeetingSites").append(data);
				   		boardMeetingSiteRowOnClick(currentBoardMeetingSiteIndex);
				   },
				   error: function(jqXHR, textStatus, errorThrown) {
						alert("Error - status: " + textStatus + "; error: "
								+ errorThrown);
							$("#boardMeetingSites").html(jqXHR.responseText);
				   }
				   
			   }
			);
			currentBoardMeetingSiteIndex++;
   			return false;
	});
}

/**
 * Assigns on click functionality for the parole board itinerary note item row 
 * with the specified board itinerary note index.
 * 
 * @param boardItineraryNoteIndex board itinerary note index
 */
function boardItineraryNoteRowOnClick(boardItineraryNoteIndex) {
	applyDatePicker("#boardItineraryNoteDate" + boardItineraryNoteIndex);
	$("#removeNoteLink" + boardItineraryNoteIndex).click(function() {
		if ($("#boardItineraryNoteOperation" + boardItineraryNoteIndex).val() == "EDIT") {
			$("#boardItineraryNoteOperation" + boardItineraryNoteIndex).val("REMOVE");
			$("#boardItineraryNoteRow" + boardItineraryNoteIndex).addClass("removeRow");
		} else if($("#boardItineraryNoteOperation" + boardItineraryNoteIndex).val() == "REMOVE") {
			$("#boardItineraryNoteOperation" + boardItineraryNoteIndex).val("EDIT");
			$("#boardItineraryNoteRow" + boardItineraryNoteIndex).removeClass("removeRow");
		} else {
			$("#boardItineraryNoteRow" + boardItineraryNoteIndex).remove();
		}
		return false;
	});
}

/**
 * Assigns on click functionality for the board meeting site item row with the 
 * specified board meeting site index.
 * 
 * @param boardMeetingSiteIndex board meeting site index
 */
function boardMeetingSiteRowOnClick(boardMeetingSiteIndex) {
	applyDatePicker("#boardMeetingSite" + boardMeetingSiteIndex + "Date");
	$("#removeLink" + boardMeetingSiteIndex).click(function() {
		if ($("#boardMeetingSiteOperation" + boardMeetingSiteIndex).val() == "EDIT") {
			$("#boardMeetingSiteOperation" + boardMeetingSiteIndex).val("REMOVE");
			$("#boardMeetingSiteRow" + boardMeetingSiteIndex).addClass("removeRow");
		} else if($("#boardMeetingSiteOperation" + boardMeetingSiteIndex).val() == "REMOVE") {
			$("#boardMeetingSiteOperation" + boardMeetingSiteIndex).val("EDIT");
			$("#boardMeetingSiteRow" + boardMeetingSiteIndex).removeClass("removeRow");
		} else {
			$("#boardMeetingSiteRow" + boardMeetingSiteIndex).remove();
		}
		return false;
	});
	locationOnChange(boardMeetingSiteIndex);
}

function locationOnChange(boardMeetingSiteIndex) {
	$("#boardMeetingSiteItems" + boardMeetingSiteIndex + "Location").change(function() {
		$.ajax(config.ServerConfig.getContextPath() + "/paroleBoardItinerary/findUnitsByLocation.html",
		   {
			   type: "GET",
			   async: false,
			   data: { location: this.value },
			   success: function(data) {
			   		$("#boardMeetingSiteItems" + boardMeetingSiteIndex + "Unit").empty().append(data);
			   },
			   error: function(jqXHR, textStatus, errorThrown) {
					alert("Error - status: " + textStatus + "; error: "
							+ errorThrown);
			   }
			   
		   }
		);
	});
}