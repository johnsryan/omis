function medicalReviewNoteItemsCreateOnClick() {
	$("#createMedicalReviewNoteItemLink").click(function() {
		$.ajax(config.ServerConfig.getContextPath() + "/medicalReview/createMedicalReviewNoteItem.html",
		   {
				type: "GET",
				async: false,
				data: {medicalReviewNoteItemIndex: currentMedicalReviewNoteItemIndex},
				success: function(data) {
					$("#medicalReviewNoteTableBody").append(data);
					medicalReviewNoteItemRowOnClick(currentMedicalReviewNoteItemIndex);
				},
				error: function(jqXHR, textStatus, errorThrown) {
					alert("Error - status: " + textStatus + "; error: "
						+ errorThrown);
					$("#medicalReviewNoteTableBody").html(jqXHR.responseText );
				}
			});
		currentMedicalReviewNoteItemIndex++;
		return false;
	});
};

function medicalReviewNoteItemRowOnClick(medicalReviewNoteItemIndex) {
	assignDatePicker("medicalReviewNoteItemDate" + medicalReviewNoteItemIndex);
	$("#removeMedicalReviewNoteLink" + medicalReviewNoteItemIndex).click(function() {
		if ($("#medicalReviewNoteOperation" + medicalReviewNoteItemIndex).val() == "UPDATE") {
			$("#medicalReviewNoteOperation" + medicalReviewNoteItemIndex).val("REMOVE");
			$("#medicalReviewNoteItemRow" + medicalReviewNoteItemIndex).addClass("removeRow");
		} else if($("#medicalReviewNoteOperation" + medicalReviewNoteItemIndex).val() == "REMOVE") {
			$("#medicalReviewNoteOperation" + medicalReviewNoteItemIndex).val("UPDATE");
			$("#medicalReviewNoteItemRow" +medicalReviewNoteItemIndex).removeClass("removeRow");
		} else {
			$("#medicalReviewNoteItemRow" + medicalReviewNoteItemIndex).remove();
		}
		return false;
	});
};

function medicalReviewDocumentAssociationItemsCreateOnClick() {
	$("#createMedicalReviewDocumentAssociationItemLink").click(function() {
		$.ajax(config.ServerConfig.getContextPath() + "/medicalReview/createMedicalReviewDocumentAssociationItem.html",
		   {
				type: "GET",
				async: false,
				data: {
					medicalReviewDocumentAssociationItemIndex: currentMedicalReviewDocumentAssociationItemIndex
				},
				success: function(data) {
					$("#medicalReviewDocumentAssociationItems").append(data);
					medicalReviewDocumentAssociationItemRowOnClick(currentMedicalReviewDocumentAssociationItemIndex);
				},
				error: function(jqXHR, textStatus, errorThrown) {
					alert("Error - status: " + textStatus + "; error: "
						+ errorThrown);
					$("#medicalReviewDocumentAssociationItems").html(jqXHR.responseText );
				}
			});
		currentMedicalReviewDocumentAssociationItemIndex++;
		currentDocumentTagItemIndexes.push(0);
		return false;
	});
};

function medicalReviewDocumentAssociationItemRowOnClick(medicalReviewDocumentAssociationItemIndex) {
	assignDatePicker("medicalReviewDocumentAssociationItems\\[" + medicalReviewDocumentAssociationItemIndex + "\\]\\.date");
	applyFileExtensionNamer(medicalReviewDocumentAssociationItemIndex);
	$("#createDocumentTagItemLink" + medicalReviewDocumentAssociationItemIndex).click(function() {
		documentTagItemsCreateOnClick(medicalReviewDocumentAssociationItemIndex);
	});
	$("#removeMedicalReviewDocumentAssociationLink" + medicalReviewDocumentAssociationItemIndex).click(function() {
		if ($("#medicalReviewDocumentAssociationOperation" + medicalReviewDocumentAssociationItemIndex).val() == "UPDATE") {
			$("#medicalReviewDocumentAssociationOperation" + medicalReviewDocumentAssociationItemIndex).val("REMOVE");
			$("#medicalReviewDocumentAssociationItem" + medicalReviewDocumentAssociationItemIndex).addClass("removeContent");
		} else if($("#medicalReviewDocumentAssociationOperation" + medicalReviewDocumentAssociationItemIndex).val() == "REMOVE") {
			$("#medicalReviewDocumentAssociationOperation" + medicalReviewDocumentAssociationItemIndex).val("UPDATE");
			$("#medicalReviewDocumentAssociationItem" +medicalReviewDocumentAssociationItemIndex).removeClass("removeContent");
		} else {
			$("#medicalReviewDocumentAssociationItem" + medicalReviewDocumentAssociationItemIndex).remove();
		}
		return false;
	});
};

function documentTagItemsCreateOnClick(medicalReviewDocumentAssociationItemIndex) {
	var documentTagItemIndex = currentDocumentTagItemIndexes[medicalReviewDocumentAssociationItemIndex];
	$.ajax(config.ServerConfig.getContextPath() + "/medicalReview/createDocumentTagItem.html",
	   {
			type: "GET",
			async: false,
			data: {
				medicalReviewDocumentAssociationItemIndex: medicalReviewDocumentAssociationItemIndex,
				documentTagItemIndex: documentTagItemIndex
			},
			success: function(data) {
				$("#documentTagItems" + medicalReviewDocumentAssociationItemIndex).append(data);
				documentTagItemRowOnClick(medicalReviewDocumentAssociationItemIndex, documentTagItemIndex);
			},
			error: function(jqXHR, textStatus, errorThrown) {
				alert("Error - status: " + textStatus + "; error: "
					+ errorThrown);
				$("#documentTagItems" + medicalReviewDocumentAssociationItemIndex).html(jqXHR.responseText );
			}
		});
	currentDocumentTagItemIndexes[medicalReviewDocumentAssociationItemIndex]++;
	return false;
};

function documentTagItemRowOnClick(medicalReviewDocumentAssociationItemIndex, documentTagItemIndex) {
	$("#removeMedicalReviewDocumentAssociation" + medicalReviewDocumentAssociationItemIndex + "documentTagLink" + documentTagItemIndex).click(function() {
		if ($("#medicalReviewDocumentAssociation" + medicalReviewDocumentAssociationItemIndex + "documentTagOperation" + documentTagItemIndex).val() == "UPDATE") {
			$("#medicalReviewDocumentAssociation" + medicalReviewDocumentAssociationItemIndex + "documentTagOperation" + documentTagItemIndex).val("REMOVE");
			$("#medicalReviewDocumentAssociation" + medicalReviewDocumentAssociationItemIndex + "documentTagItem" + documentTagItemIndex).addClass("removeContent");
		} else if($("#medicalReviewDocumentAssociation" + medicalReviewDocumentAssociationItemIndex + "documentTagOperation" + documentTagItemIndex).val() == "REMOVE") {
			$("#medicalReviewDocumentAssociation" + medicalReviewDocumentAssociationItemIndex + "documentTagOperation" + documentTagItemIndex).val("UPDATE");
			$("#medicalReviewDocumentAssociation" + medicalReviewDocumentAssociationItemIndex + "documentTagItem" +documentTagItemIndex).removeClass("removeContent");
		} else {
			$("#medicalReviewDocumentAssociation" + medicalReviewDocumentAssociationItemIndex + "documentTagItem" + documentTagItemIndex).remove();
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

function applyFileExtensionNamer(medicalReviewDocumentAssociationIndex) {
	var documentData = document.getElementById("medicalReviewDocumentAssociationItems[" + medicalReviewDocumentAssociationIndex + "].data");
	var fileExtension = document.getElementById("medicalReviewDocumentAssociationItems[" + medicalReviewDocumentAssociationIndex + "].fileExtension");
	documentData.onchange = function() {
		var fileExtensionValue = documentData.value.substr(documentData.value.lastIndexOf('.')+1);
		fileExtension.value = fileExtensionValue;
	}
};