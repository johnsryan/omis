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
 * Parole plan detail screen jquery behavior.
 * 
 * Author: Josh Divine
 * Version: 0.1.0 (Feb 13, 2018)
 * Since: OMIS 3.0
 */
function parolePlanDocumentAssociationItemsCreateOnClick() {
	$("#createParolePlanDocumentAssociationItemLink").click(function() {
		$.ajax(config.ServerConfig.getContextPath() + "/parolePlan/createParolePlanDocumentAssociationItem.html",
		   {
				type: "GET",
				async: false,
				data: {documentAssociationItemIndex: currentDocumentAssociationItemIndex},
				success: function(data) {
					$("#parolePlanDocumentAssociationItems").append(data);
					parolePlanDocumentAssociationItemRowOnClick(currentDocumentAssociationItemIndex);
				},
				error: function(jqXHR, textStatus, errorThrown) {
					alert("Error - status: " + textStatus + "; error: "
						+ errorThrown);
					$("#parolePlanDocumentAssociationItems").html(jqXHR.responseText );
				}
			});
		currentDocumentAssociationItemIndex++;
		currentDocumentTagItemIndexes.push(0);
		return false;
	});
};

function parolePlanDocumentAssociationItemRowOnClick(parolePlanDocumentAssociationItemIndex) {
	assignDatePicker("parolePlanDocumentAssociationItems\\["+parolePlanDocumentAssociationItemIndex+"\\]\\.fileDate");
	applyFileExtensionNamer(parolePlanDocumentAssociationItemIndex);
	$("#createDocumentTagItemLink" + parolePlanDocumentAssociationItemIndex).click(function() {
		documentTagItemsCreateOnClick(parolePlanDocumentAssociationItemIndex);
	});
	$("#removeParolePlanDocumentAssociationLink" + parolePlanDocumentAssociationItemIndex).click(function() {
		if ($("#parolePlanDocumentAssociationOperation" + parolePlanDocumentAssociationItemIndex).val() == "UPDATE") {
			$("#parolePlanDocumentAssociationOperation" + parolePlanDocumentAssociationItemIndex).val("REMOVE");
			$("#parolePlanDocumentAssociationItem" + parolePlanDocumentAssociationItemIndex).addClass("removeContent");
		} else if($("#parolePlanDocumentAssociationOperation" + parolePlanDocumentAssociationItemIndex).val() == "REMOVE") {
			$("#parolePlanDocumentAssociationOperation" + parolePlanDocumentAssociationItemIndex).val("UPDATE");
			$("#parolePlanDocumentAssociationItem" +parolePlanDocumentAssociationItemIndex).removeClass("removeContent");
		} else {
			$("#parolePlanDocumentAssociationItem" + parolePlanDocumentAssociationItemIndex).remove();
		}
		return false;
	});
};

function documentTagItemsCreateOnClick(parolePlanDocumentAssociationItemIndex) {
	var documentTagItemIndex = currentDocumentTagItemIndexes[parolePlanDocumentAssociationItemIndex];
	$.ajax(config.ServerConfig.getContextPath() + "/parolePlan/createDocumentTagItem.html",
	   {
			type: "GET",
			async: false,
			data: {
				documentAssociationItemIndex: parolePlanDocumentAssociationItemIndex,
				documentTagItemIndex: documentTagItemIndex},
			success: function(data) {
				$("#documentTagItems" + parolePlanDocumentAssociationItemIndex).append(data);
				documentTagItemRowOnClick(parolePlanDocumentAssociationItemIndex, documentTagItemIndex);
			},
			error: function(jqXHR, textStatus, errorThrown) {
				alert("Error - status: " + textStatus + "; error: "
					+ errorThrown);
				$("#documentTagItems" + parolePlanDocumentAssociationItemIndex).html(jqXHR.responseText );
			}
		});
	currentDocumentTagItemIndexes[parolePlanDocumentAssociationItemIndex]++;
	return false;
};

function documentTagItemRowOnClick(parolePlanDocumentAssociationItemIndex, documentTagItemIndex) {
	$("#removeParolePlanDocumentAssociation" + parolePlanDocumentAssociationItemIndex + "documentTagLink" + documentTagItemIndex).click(function() {
		if ($("#parolePlanDocumentAssociation" + parolePlanDocumentAssociationItemIndex + "documentTagOperation" + documentTagItemIndex).val() == "UPDATE") {
			$("#parolePlanDocumentAssociation" + parolePlanDocumentAssociationItemIndex + "documentTagOperation" + documentTagItemIndex).val("REMOVE");
			$("#parolePlanDocumentAssociation" + parolePlanDocumentAssociationItemIndex + "documentTagItem" + documentTagItemIndex).addClass("removeContent");
		} else if($("#parolePlanDocumentAssociation" + parolePlanDocumentAssociationItemIndex + "documentTagOperation" + documentTagItemIndex).val() == "REMOVE") {
			$("#parolePlanDocumentAssociation" + parolePlanDocumentAssociationItemIndex + "documentTagOperation" + documentTagItemIndex).val("UPDATE");
			$("#parolePlanDocumentAssociation" + parolePlanDocumentAssociationItemIndex + "documentTagItem" +documentTagItemIndex).removeClass("removeContent");
		} else {
			$("#parolePlanDocumentAssociation" + parolePlanDocumentAssociationItemIndex + "documentTagItem" + documentTagItemIndex).remove();
		}
		return false;
	});
};

function applyFileExtensionNamer(parolePlanDocumentAssociationIndex) {
	var documentData = document.getElementById("parolePlanDocumentAssociationItems[" + parolePlanDocumentAssociationIndex + "].data");
	var fileExtension = document.getElementById("parolePlanDocumentAssociationItems[" + parolePlanDocumentAssociationIndex + "].fileExtension");
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
 * Assigns on click functionality for the parole plan note item row with the 
 * specified parole plan note index.
 * 
 * @param parolePlanNoteIndex parole plan note index
 */
function parolePlanNoteRowOnClick(parolePlanNoteIndex) {
	applyDatePicker("#parolePlanNoteDate" + parolePlanNoteIndex);
	$("#removeNoteLink" + parolePlanNoteIndex).click(function() {
		if ($("#parolePlanNoteOperation" + parolePlanNoteIndex).val() == "UPDATE") {
			$("#parolePlanNoteOperation" + parolePlanNoteIndex).val("REMOVE");
			$("#parolePlanNoteRow" + parolePlanNoteIndex).addClass("removeRow");
		} else if($("#parolePlanNoteOperation" + parolePlanNoteIndex).val() == "REMOVE") {
			$("#parolePlanNoteOperation" + parolePlanNoteIndex).val("UPDATE");
			$("#parolePlanNoteRow" + parolePlanNoteIndex).removeClass("removeRow");
		} else {
			$("#parolePlanNoteRow" + parolePlanNoteIndex).remove();
		}
		return false;
	});
}

/**
 * Assigns on click functionality for the parole plan note items action menu.
 */
function parolePlanNoteActionMenuOnClick() {
	$("#createParolePlanNoteLink").click(function() {
		$.ajax(config.ServerConfig.getContextPath() + "/parolePlan/createParolePlanNote.html",
			   {
				   type: "GET",
				   async: false,
				   data: { parolePlanNoteIndex: currentParolePlanNoteIndex },
				   success: function(data) {
				   		$("#parolePlanNotes").append(data);
				   		parolePlanNoteRowOnClick(currentParolePlanNoteIndex);
				   },
				   error: function(jqXHR, textStatus, errorThrown) {
						alert("Error - status: " + textStatus + "; error: "
								+ errorThrown);
							$("#parolePlanNotes").html(jqXHR.responseText);
				   }
			   }
			);
			currentParolePlanNoteIndex++;
   			return false;
	});
}