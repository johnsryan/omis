function assessmentNoteItemsCreateOnClick() {
	$("#createAssessmentNoteItemLink").click(function() {
		$.ajax(config.ServerConfig.getContextPath() + "/assessment/notes/createAssessmentNoteItem.html",
		   {
				type: "GET",
				async: false,
				data: {assessmentNoteItemIndex: currentAssessmentNoteItemIndex},
				success: function(data) {
					$("#assessmentNoteTableBody").append(data);
					assessmentNoteItemRowOnClick(currentAssessmentNoteItemIndex);
				},
				error: function(jqXHR, textStatus, errorThrown) {
					alert("Error - status: " + textStatus + "; error: "
						+ errorThrown);
					$("#assessmentNoteTableBody").html(jqXHR.responseText );
				}
			});
		currentAssessmentNoteItemIndex++;
		return false;
	});
};

function assessmentNoteItemRowOnClick(assessmentNoteItemIndex) {
	assignDatePicker("assessmentNoteItemDate" + assessmentNoteItemIndex);
	$("#removeAssessmentNoteLink" + assessmentNoteItemIndex).click(function() {
		if ($("#assessmentNoteOperation" + assessmentNoteItemIndex).val() == "UPDATE") {
			$("#assessmentNoteOperation" + assessmentNoteItemIndex).val("REMOVE");
			$("#assessmentNoteItemRow" + assessmentNoteItemIndex).addClass("removeRow");
		} else if($("#assessmentNoteOperation" + assessmentNoteItemIndex).val() == "REMOVE") {
			$("#assessmentNoteOperation" + assessmentNoteItemIndex).val("UPDATE");
			$("#assessmentNoteItemRow" +assessmentNoteItemIndex).removeClass("removeRow");
		} else {
			$("#assessmentNoteItemRow" + assessmentNoteItemIndex).remove();
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