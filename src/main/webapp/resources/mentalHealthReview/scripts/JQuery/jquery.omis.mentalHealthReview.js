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
 * Mental health review detail screen jquery behavior.
 * 
 * Author: Josh Divine
 * Version: 0.1.0 Feb 5, 2018
 * Since: OMIS 3.0
 */
function mentalHealthReviewDocumentAssociationItemsCreateOnClick() {
	$("#createMentalHealthReviewDocumentAssociationItemLink").click(function() {
		$.ajax(config.ServerConfig.getContextPath() + "/mentalHealthReview/createMentalHealthReviewDocumentAssociationItem.html",
		   {
				type: "GET",
				async: false,
				data: {documentAssociationItemIndex: currentDocumentAssociationItemIndex},
				success: function(data) {
					$("#mentalHealthReviewDocumentAssociationItems").append(data);
					mentalHealthReviewDocumentAssociationItemRowOnClick(currentDocumentAssociationItemIndex);
				},
				error: function(jqXHR, textStatus, errorThrown) {
					alert("Error - status: " + textStatus + "; error: "
						+ errorThrown);
					$("#mentalHealthReviewDocumentAssociationItems").html(jqXHR.responseText );
				}
			});
		currentDocumentAssociationItemIndex++;
		currentDocumentTagItemIndexes.push(0);
		return false;
	});
};

function mentalHealthReviewDocumentAssociationItemRowOnClick(mentalHealthReviewDocumentAssociationItemIndex) {
	assignDatePicker("mentalHealthReviewDocumentAssociationItems\\["+mentalHealthReviewDocumentAssociationItemIndex+"\\]\\.fileDate");
	applyFileExtensionNamer(mentalHealthReviewDocumentAssociationItemIndex);
	$("#createDocumentTagItemLink" + mentalHealthReviewDocumentAssociationItemIndex).click(function() {
		documentTagItemsCreateOnClick(mentalHealthReviewDocumentAssociationItemIndex);
	});
	$("#removeMentalHealthReviewDocumentAssociationLink" + mentalHealthReviewDocumentAssociationItemIndex).click(function() {
		if ($("#mentalHealthReviewDocumentAssociationOperation" + mentalHealthReviewDocumentAssociationItemIndex).val() == "UPDATE") {
			$("#mentalHealthReviewDocumentAssociationOperation" + mentalHealthReviewDocumentAssociationItemIndex).val("REMOVE");
			$("#mentalHealthReviewDocumentAssociationItem" + mentalHealthReviewDocumentAssociationItemIndex).addClass("removeContent");
		} else if($("#mentalHealthReviewDocumentAssociationOperation" + mentalHealthReviewDocumentAssociationItemIndex).val() == "REMOVE") {
			$("#mentalHealthReviewDocumentAssociationOperation" + mentalHealthReviewDocumentAssociationItemIndex).val("UPDATE");
			$("#mentalHealthReviewDocumentAssociationItem" +mentalHealthReviewDocumentAssociationItemIndex).removeClass("removeContent");
		} else {
			$("#mentalHealthReviewDocumentAssociationItem" + mentalHealthReviewDocumentAssociationItemIndex).remove();
		}
		return false;
	});
};

function documentTagItemsCreateOnClick(mentalHealthReviewDocumentAssociationItemIndex) {
	var documentTagItemIndex = currentDocumentTagItemIndexes[mentalHealthReviewDocumentAssociationItemIndex];
	$.ajax(config.ServerConfig.getContextPath() + "/mentalHealthReview/createDocumentTagItem.html",
	   {
			type: "GET",
			async: false,
			data: {
				documentAssociationItemIndex: mentalHealthReviewDocumentAssociationItemIndex,
				documentTagItemIndex: documentTagItemIndex},
			success: function(data) {
				$("#documentTagItems" + mentalHealthReviewDocumentAssociationItemIndex).append(data);
				documentTagItemRowOnClick(mentalHealthReviewDocumentAssociationItemIndex, documentTagItemIndex);
			},
			error: function(jqXHR, textStatus, errorThrown) {
				alert("Error - status: " + textStatus + "; error: "
					+ errorThrown);
				$("#documentTagItems" + mentalHealthReviewDocumentAssociationItemIndex).html(jqXHR.responseText );
			}
		});
	currentDocumentTagItemIndexes[mentalHealthReviewDocumentAssociationItemIndex]++;
	return false;
};

function documentTagItemRowOnClick(mentalHealthReviewDocumentAssociationItemIndex, documentTagItemIndex) {
	$("#removeMentalHealthReviewDocumentAssociation" + mentalHealthReviewDocumentAssociationItemIndex + "documentTagLink" + documentTagItemIndex).click(function() {
		if ($("#mentalHealthReviewDocumentAssociation" + mentalHealthReviewDocumentAssociationItemIndex + "documentTagOperation" + documentTagItemIndex).val() == "UPDATE") {
			$("#mentalHealthReviewDocumentAssociation" + mentalHealthReviewDocumentAssociationItemIndex + "documentTagOperation" + documentTagItemIndex).val("REMOVE");
			$("#mentalHealthReviewDocumentAssociation" + mentalHealthReviewDocumentAssociationItemIndex + "documentTagItem" + documentTagItemIndex).addClass("removeContent");
		} else if($("#mentalHealthReviewDocumentAssociation" + mentalHealthReviewDocumentAssociationItemIndex + "documentTagOperation" + documentTagItemIndex).val() == "REMOVE") {
			$("#mentalHealthReviewDocumentAssociation" + mentalHealthReviewDocumentAssociationItemIndex + "documentTagOperation" + documentTagItemIndex).val("UPDATE");
			$("#mentalHealthReviewDocumentAssociation" + mentalHealthReviewDocumentAssociationItemIndex + "documentTagItem" +documentTagItemIndex).removeClass("removeContent");
		} else {
			$("#mentalHealthReviewDocumentAssociation" + mentalHealthReviewDocumentAssociationItemIndex + "documentTagItem" + documentTagItemIndex).remove();
		}
		return false;
	});
};

function applyFileExtensionNamer(mentalHealthReviewDocumentAssociationIndex) {
	var documentData = document.getElementById("mentalHealthReviewDocumentAssociationItems[" + mentalHealthReviewDocumentAssociationIndex + "].data");
	var fileExtension = document.getElementById("mentalHealthReviewDocumentAssociationItems[" + mentalHealthReviewDocumentAssociationIndex + "].fileExtension");
	documentData.onchange = function() {
		var fileExtensionValue = documentData.value.substr(documentData.value.lastIndexOf('.')+1);
		fileExtension.value = fileExtensionValue;
	}
};

function assignDatePicker(elementId) {
	$("#" + elementId).datepicker({
		changeMonth: true,
		changeYear: true
	});
};

function mentalHealthNoteItemsCreateOnClick() {
	$("#createMentalHealthReviewNoteItemLink").click(function() {
		$.ajax(config.ServerConfig.getContextPath() + "/mentalHealthReview/createMentalHealthReviewNoteItem.html",
		   {
				type: "GET",
				async: false,
				data: {mentalHealthReviewNoteItemIndex: currentMentalHealthReviewNoteItemIndex},
				success: function(data) {
					$("#mentalHealthReviewNoteTableBody").append(data);
					mentalHealthNoteItemRowOnClick(currentMentalHealthReviewNoteItemIndex);
				},
				error: function(jqXHR, textStatus, errorThrown) {
					alert("Error - status: " + textStatus + "; error: "
						+ errorThrown);
					$("#mentalHealthReviewNoteTableBody").html(jqXHR.responseText );
				}
			});
		currentMentalHealthReviewNoteItemIndex++;
		return false;
	});
};

function mentalHealthNoteItemRowOnClick(mentalHealthNoteItemIndex) {
	assignDatePicker("mentalHealthNoteItemDate" + mentalHealthNoteItemIndex);
	$("#removeMentalHealthReviewNoteLink" + mentalHealthNoteItemIndex).click(function() {
		if ($("#mentalHealthNoteOperation" + mentalHealthNoteItemIndex).val() == "UPDATE") {
			$("#mentalHealthNoteOperation" + mentalHealthNoteItemIndex).val("REMOVE");
			$("#mentalHealthNoteItemRow" + mentalHealthNoteItemIndex).addClass("removeRow");
		} else if($("#mentalHealthNoteOperation" + mentalHealthNoteItemIndex).val() == "REMOVE") {
			$("#mentalHealthNoteOperation" + mentalHealthNoteItemIndex).val("UPDATE");
			$("#mentalHealthNoteItemRow" +mentalHealthNoteItemIndex).removeClass("removeRow");
		} else {
			$("#mentalHealthNoteItemRow" + mentalHealthNoteItemIndex).remove();
		}
		return false;
	});
};