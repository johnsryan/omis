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
 * Parole review detail screen jquery behavior.
 * 
 * Author: Josh Divine
 * Version: 0.1.0 Jan 30, 2018
 * Since: OMIS 3.0
 */
function paroleReviewDocumentAssociationItemsCreateOnClick() {
	$("#createParoleReviewDocumentAssociationItemLink").click(function() {
		$.ajax(config.ServerConfig.getContextPath() + "/paroleReview/createParoleReviewDocumentAssociationItem.html",
		   {
				type: "GET",
				async: false,
				data: {documentAssociationItemIndex: currentDocumentAssociationItemIndex},
				success: function(data) {
					$("#paroleReviewDocumentAssociationItems").append(data);
					paroleReviewDocumentAssociationItemRowOnClick(currentDocumentAssociationItemIndex);
				},
				error: function(jqXHR, textStatus, errorThrown) {
					alert("Error - status: " + textStatus + "; error: "
						+ errorThrown);
					$("#paroleReviewDocumentAssociationItems").html(jqXHR.responseText );
				}
			});
		currentDocumentAssociationItemIndex++;
		currentDocumentTagItemIndexes.push(0);
		return false;
	});
};

function paroleReviewDocumentAssociationItemRowOnClick(paroleReviewDocumentAssociationItemIndex) {
	assignDatePicker("paroleReviewDocumentAssociationItems\\["+paroleReviewDocumentAssociationItemIndex+"\\]\\.fileDate");
	applyFileExtensionNamer(paroleReviewDocumentAssociationItemIndex);
	$("#createDocumentTagItemLink" + paroleReviewDocumentAssociationItemIndex).click(function() {
		documentTagItemsCreateOnClick(paroleReviewDocumentAssociationItemIndex);
	});
	$("#removeParoleReviewDocumentAssociationLink" + paroleReviewDocumentAssociationItemIndex).click(function() {
		if ($("#paroleReviewDocumentAssociationOperation" + paroleReviewDocumentAssociationItemIndex).val() == "UPDATE") {
			$("#paroleReviewDocumentAssociationOperation" + paroleReviewDocumentAssociationItemIndex).val("REMOVE");
			$("#paroleReviewDocumentAssociationItem" + paroleReviewDocumentAssociationItemIndex).addClass("removeContent");
		} else if($("#paroleReviewDocumentAssociationOperation" + paroleReviewDocumentAssociationItemIndex).val() == "REMOVE") {
			$("#paroleReviewDocumentAssociationOperation" + paroleReviewDocumentAssociationItemIndex).val("UPDATE");
			$("#paroleReviewDocumentAssociationItem" +paroleReviewDocumentAssociationItemIndex).removeClass("removeContent");
		} else {
			$("#paroleReviewDocumentAssociationItem" + paroleReviewDocumentAssociationItemIndex).remove();
		}
		return false;
	});
};

function documentTagItemsCreateOnClick(paroleReviewDocumentAssociationItemIndex) {
	var documentTagItemIndex = currentDocumentTagItemIndexes[paroleReviewDocumentAssociationItemIndex];
	$.ajax(config.ServerConfig.getContextPath() + "/paroleReview/createDocumentTagItem.html",
	   {
			type: "GET",
			async: false,
			data: {
				documentAssociationItemIndex: paroleReviewDocumentAssociationItemIndex,
				documentTagItemIndex: documentTagItemIndex},
			success: function(data) {
				$("#documentTagItems" + paroleReviewDocumentAssociationItemIndex).append(data);
				documentTagItemRowOnClick(paroleReviewDocumentAssociationItemIndex, documentTagItemIndex);
			},
			error: function(jqXHR, textStatus, errorThrown) {
				alert("Error - status: " + textStatus + "; error: "
					+ errorThrown);
				$("#documentTagItems" + paroleReviewDocumentAssociationItemIndex).html(jqXHR.responseText );
			}
		});
	currentDocumentTagItemIndexes[paroleReviewDocumentAssociationItemIndex]++;
	return false;
};

function documentTagItemRowOnClick(paroleReviewDocumentAssociationItemIndex, documentTagItemIndex) {
	$("#removeParoleReviewDocumentAssociation" + paroleReviewDocumentAssociationItemIndex + "documentTagLink" + documentTagItemIndex).click(function() {
		if ($("#paroleReviewDocumentAssociation" + paroleReviewDocumentAssociationItemIndex + "documentTagOperation" + documentTagItemIndex).val() == "UPDATE") {
			$("#paroleReviewDocumentAssociation" + paroleReviewDocumentAssociationItemIndex + "documentTagOperation" + documentTagItemIndex).val("REMOVE");
			$("#paroleReviewDocumentAssociation" + paroleReviewDocumentAssociationItemIndex + "documentTagItem" + documentTagItemIndex).addClass("removeContent");
		} else if($("#paroleReviewDocumentAssociation" + paroleReviewDocumentAssociationItemIndex + "documentTagOperation" + documentTagItemIndex).val() == "REMOVE") {
			$("#paroleReviewDocumentAssociation" + paroleReviewDocumentAssociationItemIndex + "documentTagOperation" + documentTagItemIndex).val("UPDATE");
			$("#paroleReviewDocumentAssociation" + paroleReviewDocumentAssociationItemIndex + "documentTagItem" +documentTagItemIndex).removeClass("removeContent");
		} else {
			$("#paroleReviewDocumentAssociation" + paroleReviewDocumentAssociationItemIndex + "documentTagItem" + documentTagItemIndex).remove();
		}
		return false;
	});
};

function applyFileExtensionNamer(paroleReviewDocumentAssociationIndex) {
	var documentData = document.getElementById("paroleReviewDocumentAssociationItems[" + paroleReviewDocumentAssociationIndex + "].data");
	var fileExtension = document.getElementById("paroleReviewDocumentAssociationItems[" + paroleReviewDocumentAssociationIndex + "].fileExtension");
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
/**
 * Assigns on click functionality for the parole review note item row with the 
 * specified parole review note index.
 * 
 * @param paroleReviewNoteIndex parole review note index
 */
function paroleReviewNoteRowOnClick(paroleReviewNoteIndex) {
	applyDatePicker("#paroleReviewNoteDate" + paroleReviewNoteIndex);
	$("#removeNoteLink" + paroleReviewNoteIndex).click(function() {
		if ($("#paroleReviewNoteOperation" + paroleReviewNoteIndex).val() == "UPDATE") {
			$("#paroleReviewNoteOperation" + paroleReviewNoteIndex).val("REMOVE");
			$("#paroleReviewNoteRow" + paroleReviewNoteIndex).addClass("removeRow");
		} else if($("#paroleReviewNoteOperation" + paroleReviewNoteIndex).val() == "REMOVE") {
			$("#paroleReviewNoteOperation" + paroleReviewNoteIndex).val("UPDATE");
			$("#paroleReviewNoteRow" + paroleReviewNoteIndex).removeClass("removeRow");
		} else {
			$("#paroleReviewNoteRow" + paroleReviewNoteIndex).remove();
		}
		return false;
	});
}

/**
 * Assigns on click functionality for the parole review note items action menu.
 */
function paroleReviewNoteActionMenuOnClick() {
	$("#createParoleReviewNoteLink").click(function() {
		$.ajax(config.ServerConfig.getContextPath() + "/paroleReview/createParoleReviewNote.html",
			   {
				   type: "GET",
				   async: false,
				   data: { paroleReviewNoteIndex: currentParoleReviewNoteIndex },
				   success: function(data) {
				   		$("#paroleReviewNotes").append(data);
				   		paroleReviewNoteRowOnClick(currentParoleReviewNoteIndex);
				   },
				   error: function(jqXHR, textStatus, errorThrown) {
						alert("Error - status: " + textStatus + "; error: "
								+ errorThrown);
							$("#paroleReviewNotes").html(jqXHR.responseText);
				   }
			   }
			);
			currentParoleReviewNoteIndex++;
   			return false;
	});
}