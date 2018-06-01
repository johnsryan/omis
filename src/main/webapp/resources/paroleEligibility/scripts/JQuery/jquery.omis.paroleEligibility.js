/**
 * Parole eligibility list screen.
 * 
 * Author: Trevor Isles
 * Author: Annie Wahl
 * Version: 0.1.1 (May 23, 2018)
 * Since: OMIS 3.0
 */

/**
 * Assign all on click functionality for elements found on initial page load.
 */
function assignOnClick() {
	for (var index = 0; index < currentParoleEligibilityNoteItemIndex; index++) {
		paroleEligibilityNoteItemRowOnClick(index);
	}
};

/**
 * Assigns on click functionality for the parole eligibility note items action
 * menu.
 */
function paroleEligibilityNoteItemsActionMenuOnClick() {
	$("#createParoleEligibilityNoteItemLink").click(function() {
		$.ajax(config.ServerConfig.getContextPath() + "/paroleEligibility/createParoleEligibilityNoteItem.html",
			{
				type: "GET",
				async: false,
				data: {paroleEligibilityNoteItemIndex: currentParoleEligibilityNoteItemIndex},
				success: function(data) {
					$("#paroleEligibilityNotesTableBody").append(data);
					paroleEligibilityNoteItemRowOnClick(currentParoleEligibilityNoteItemIndex);
				},
				error: function(jqXHR, textStatus, errorThrown) {
					alert("Error - status: " + textStatus + "; error: " + errorThrown);
					$("#paroleEligibilityNotesTableBody").html(jqXHR.responseText );
				}
			});
		currentParoleEligibilityNoteItemIndex++;
		return false;
	});
};

/**
 * Assign a date picker to the DOM element with the specified id.
 * 
 * @param elementId dom element id
 */
function assignDatePicker(elementId) {
	$("#" + elementId).datepicker({
		changeMonth: true,
		changeYear: true
	});
};


function paroleEligibilityNoteItemRowOnClick(paroleEligibilityNoteItemIndex) {
	assignDatePicker("paroleEligibilityNoteItemDate" + paroleEligibilityNoteItemIndex);
	$("#removeLink" + paroleEligibilityNoteItemIndex).click(function() {
		if ($("#paroleEligibilityNoteOperation" + paroleEligibilityNoteItemIndex).val() == "UPDATE") {
			$("#paroleEligibilityNoteOperation" + paroleEligibilityNoteItemIndex).val("REMOVE");
			$("#paroleEligibilityNoteItemRow" + paroleEligibilityNoteItemIndex).addClass("removeRow");
		} else if($("#paroleEligibilityNoteOperation" + paroleEligibilityNoteItemIndex).val() == "REMOVE") {
			$("#paroleEligibilityNoteOperation" + paroleEligibilityNoteItemIndex).val("UPDATE");
			$("#paroleEligibilityNoteItemRow" + paroleEligibilityNoteItemIndex).removeClass("removeRow");
		} else {
			$("#paroleEligibilityNoteItemRow" + paroleEligibilityNoteItemIndex).remove();
		}
		return false;
	});
}

function showStatusReasons(statusCategory) {
	$.ajax({
		type: "GET",
		async: false,
		url: "statusReasonOptions.html",
		data: {
			statusCategory: statusCategory
		},
		success: function(response) {
			$("#eligibilityStatusReason").html(response);
		},
		error: function(jqXHR, textStatus, errorThrown) {
			alert("Error - status: " + textStatus + "; error: "
					+ errorThrown);
			$("#eligibilityStatusReason").html(jqXHR.responseText );
		}
	});
}