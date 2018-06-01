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
 * Applies violation event jquery screen behavior.
 *
 * @author: Annie Wahl
 * @author: Josh Divine
 * @version: 0.1.4 (May 23, 2018)
 * @since: OMIS 3.0
 */
function violationEventNoteItemsCreateOnClick() {
	$("#createViolationEventNoteItemLink").click(function() {
		$.ajax(config.ServerConfig.getContextPath() + "/violationEvent/createViolationEventNoteItem.html",
		   {
				type: "GET",
				async: false,
				data: {violationEventNoteItemIndex: currentViolationEventNoteItemIndex},
				success: function(data) {
					$("#violationEventNoteTableBody").append(data);
					violationEventNoteItemRowOnClick(currentViolationEventNoteItemIndex);
				},
				error: function(jqXHR, textStatus, errorThrown) {
					alert("Error - status: " + textStatus + "; error: "
						+ errorThrown);
					$("#violationEventNoteTableBody").html(jqXHR.responseText );
				}
			});
		currentViolationEventNoteItemIndex++;
		return false;
	});
};

function violationEventNoteItemRowOnClick(violationEventNoteItemIndex) {
	assignDatePicker("violationEventNoteItemDate" + violationEventNoteItemIndex);
	$("#removeViolationEventNoteLink" + violationEventNoteItemIndex).click(function() {
		if ($("#violationEventNoteOperation" + violationEventNoteItemIndex).val() == "UPDATE") {
			$("#violationEventNoteOperation" + violationEventNoteItemIndex).val("REMOVE");
			$("#violationEventNoteItemRow" + violationEventNoteItemIndex).addClass("removeRow");
		} else if($("#violationEventNoteOperation" + violationEventNoteItemIndex).val() == "REMOVE") {
			$("#violationEventNoteOperation" + violationEventNoteItemIndex).val("UPDATE");
			$("#violationEventNoteItemRow" +violationEventNoteItemIndex).removeClass("removeRow");
		} else {
			$("#violationEventNoteItemRow" + violationEventNoteItemIndex).remove();
		}
		return false;
	});
};


function disciplinaryCodeViolationItemsCreateOnClick() {
	$("#createDisciplinaryCodeViolationItemLink").click(function() {
		var jurisdiction = $("#jurisdiction");
		var eventDate = $("#eventDate");
		$.ajax(config.ServerConfig.getContextPath() + "/violationEvent/createDisciplinaryCodeViolationItem.html",
		   {
				type: "GET",
				async: false,
				data: {
					disciplinaryCodeViolationItemIndex: currentDisciplinaryCodeViolationItemIndex,
					jurisdiction: jurisdiction.val(),
					eventDate: eventDate.val()
					
				},
				success: function(data) {
					$("#disciplinaryCodeViolationTableBody").append(data);
					disciplinaryCodeViolationItemRowOnClick(currentDisciplinaryCodeViolationItemIndex);
				},
				error: function(jqXHR, textStatus, errorThrown) {
					alert("Error - status: " + textStatus + "; error: "
						+ errorThrown);
					$("#disciplinaryCodeViolationTableBody").html(jqXHR.responseText );
				}
			});
		currentDisciplinaryCodeViolationItemIndex++;
		return false;
	});
};

function disciplinaryCodeViolationItemRowOnClick(disciplinaryCodeViolationItemIndex) {
	/*var disciplinaryCode = $("#disciplinaryCodeViolationItems\\["+ disciplinaryCodeViolationItemIndex +"\\]\\.disciplinaryCode");
	disciplinaryCode.change(function() {
		if(disciplinaryCode.val()){
			 disciplinaryCodeChangeFunction(disciplinaryCodeViolationItemIndex);
		}
		else{
			$("#disciplinaryCodeViolationItemDescription" + disciplinaryCodeViolationItemIndex).html('');
		}
	});*/
	
	$("#removeDisciplinaryCodeViolationLink" + disciplinaryCodeViolationItemIndex).click(function() {
		if ($("#disciplinaryCodeViolationOperation" + disciplinaryCodeViolationItemIndex).val() == "UPDATE") {
			$("#disciplinaryCodeViolationOperation" + disciplinaryCodeViolationItemIndex).val("REMOVE");
			$("#disciplinaryCodeViolationItemRow" + disciplinaryCodeViolationItemIndex).addClass("removeRow");
		} else if($("#disciplinaryCodeViolationOperation" + disciplinaryCodeViolationItemIndex).val() == "REMOVE") {
			$("#disciplinaryCodeViolationOperation" + disciplinaryCodeViolationItemIndex).val("UPDATE");
			$("#disciplinaryCodeViolationItemRow" +disciplinaryCodeViolationItemIndex).removeClass("removeRow");
		} else {
			$("#disciplinaryCodeViolationItemRow" + disciplinaryCodeViolationItemIndex).remove();
		}
		return false;
	});
};


function conditionViolationItemsCreateOnClick() {
	$("#createConditionViolationItemLink").click(function() {
		var eventDate = $("#eventDate");
		var offender = $("#offenderId");
		$.ajax(config.ServerConfig.getContextPath() + "/violationEvent/createConditionViolationItem.html",
		   {
				type: "GET",
				async: false,
				data: {
					conditionViolationItemIndex: currentConditionViolationItemIndex,
					offender: offender.val(),
					eventDate: eventDate.val()
					
				},
				success: function(data) {
					$("#conditionViolationTableBody").append(data);
					conditionViolationItemRowOnClick(currentConditionViolationItemIndex);
				},
				error: function(jqXHR, textStatus, errorThrown) {
					alert("Error - status: " + textStatus + "; error: "
						+ errorThrown);
					$("#conditionViolationTableBody").html(jqXHR.responseText );
				}
			});
		currentConditionViolationItemIndex++;
		return false;
	});
};

function conditionViolationItemRowOnClick(conditionViolationItemIndex) {
	var condition = $("#conditionViolationItems\\["+ conditionViolationItemIndex +"\\]\\.condition");
	condition.change(function() {
		if(condition.val()){
			 conditionChangeFunction(conditionViolationItemIndex);
		}
		else{
			$("#conditionViolationItemDescription" + conditionViolationItemIndex).html('');
		}
	});
	
	$("#removeConditionViolationLink" + conditionViolationItemIndex).click(function() {
		if ($("#conditionViolationOperation" + conditionViolationItemIndex).val() == "UPDATE") {
			$("#conditionViolationOperation" + conditionViolationItemIndex).val("REMOVE");
			$("#conditionViolationItemRow" + conditionViolationItemIndex).addClass("removeRow");
		} else if($("#conditionViolationOperation" + conditionViolationItemIndex).val() == "REMOVE") {
			$("#conditionViolationOperation" + conditionViolationItemIndex).val("UPDATE");
			$("#conditionViolationItemRow" +conditionViolationItemIndex).removeClass("removeRow");
		} else {
			$("#conditionViolationItemRow" + conditionViolationItemIndex).remove();
		}
		return false;
	});
};


function violationEventDocumentItemsCreateOnClick() {
	$("#createViolationEventDocumentItemLink").click(function() {
		$.ajax(config.ServerConfig.getContextPath() + "/violationEvent/createViolationEventDocumentItem.html",
		   {
				type: "GET",
				async: false,
				data: {violationEventDocumentItemIndex: currentViolationEventDocumentItemIndex},
				success: function(data) {
					$("#violationEventDocumentItems").append(data);
					violationEventDocumentItemRowOnClick(currentViolationEventDocumentItemIndex);
				},
				error: function(jqXHR, textStatus, errorThrown) {
					alert("Error - status: " + textStatus + "; error: "
						+ errorThrown);
					$("#violationEventDocumentItems").html(jqXHR.responseText );
				}
			});
		currentViolationEventDocumentItemIndex++;
		currentDocumentTagItemIndexes.push(0);
		return false;
	});
};

function violationEventDocumentItemRowOnClick(violationEventDocumentItemIndex) {
	assignDatePicker("violationEventDocumentItems\\["+violationEventDocumentItemIndex+"\\]\\.date");
	applyFileExtensionNamer(violationEventDocumentItemIndex);
	$("#createDocumentTagItemLink" + violationEventDocumentItemIndex).click(function() {
		documentTagItemsCreateOnClick(violationEventDocumentItemIndex);
	});
	$("#removeViolationEventDocumentLink" + violationEventDocumentItemIndex).click(function() {
		if ($("#violationEventDocumentOperation" + violationEventDocumentItemIndex).val() == "UPDATE") {
			$("#violationEventDocumentOperation" + violationEventDocumentItemIndex).val("REMOVE");
			$("#violationEventDocumentItem" + violationEventDocumentItemIndex).addClass("removeContent");
		} else if($("#violationEventDocumentOperation" + violationEventDocumentItemIndex).val() == "REMOVE") {
			$("#violationEventDocumentOperation" + violationEventDocumentItemIndex).val("UPDATE");
			$("#violationEventDocumentItem" +violationEventDocumentItemIndex).removeClass("removeContent");
		} else {
			$("#violationEventDocumentItem" + violationEventDocumentItemIndex).remove();
		}
		return false;
	});
};

function documentTagItemsCreateOnClick(violationEventDocumentItemIndex) {
	var documentTagItemIndex = currentDocumentTagItemIndexes[violationEventDocumentItemIndex];
	$.ajax(config.ServerConfig.getContextPath() + "/violationEvent/createDocumentTagItem.html",
	   {
			type: "GET",
			async: false,
			data: {
				violationEventDocumentItemIndex: violationEventDocumentItemIndex,
				documentTagItemIndex: documentTagItemIndex},
			success: function(data) {
				$("#documentTagItems" + violationEventDocumentItemIndex).append(data);
				documentTagItemRowOnClick(violationEventDocumentItemIndex, documentTagItemIndex);
			},
			error: function(jqXHR, textStatus, errorThrown) {
				alert("Error - status: " + textStatus + "; error: "
					+ errorThrown);
				$("#documentTagItems" + violationEventDocumentItemIndex).html(jqXHR.responseText );
			}
		});
	currentDocumentTagItemIndexes[violationEventDocumentItemIndex]++;
	return false;
};

function documentTagItemRowOnClick(violationEventDocumentItemIndex, documentTagItemIndex) {
	$("#removeViolationEventDocument" + violationEventDocumentItemIndex + "documentTagLink" + documentTagItemIndex).click(function() {
		if ($("#violationEventDocument" + violationEventDocumentItemIndex + "documentTagOperation" + documentTagItemIndex).val() == "UPDATE") {
			$("#violationEventDocument" + violationEventDocumentItemIndex + "documentTagOperation" + documentTagItemIndex).val("REMOVE");
			$("#violationEventDocument" + violationEventDocumentItemIndex + "documentTagItem" + documentTagItemIndex).addClass("removeContent");
		} else if($("#violationEventDocument" + violationEventDocumentItemIndex + "documentTagOperation" + documentTagItemIndex).val() == "REMOVE") {
			$("#violationEventDocument" + violationEventDocumentItemIndex + "documentTagOperation" + documentTagItemIndex).val("UPDATE");
			$("#violationEventDocument" + violationEventDocumentItemIndex + "documentTagItem" +documentTagItemIndex).removeClass("removeContent");
		} else {
			$("#violationEventDocument" + violationEventDocumentItemIndex + "documentTagItem" + documentTagItemIndex).remove();
		}
		return false;
	});
};


function disciplinaryCodeChangeFunction(disciplinaryCodeViolationItemIndex) {
	var disciplinaryCode = $("#disciplinaryCodeViolationItems\\["+ disciplinaryCodeViolationItemIndex +"\\]\\.disciplinaryCode");
	var url = "showDisciplinaryCodeDescription.html";
	$.ajax({
		type: "GET",	
		async: false,
		url: url,
		data:{
			disciplinaryCode: disciplinaryCode.val()
		},
		success: function(response) {
			 $("#disciplinaryCodeViolationItemDescription" + disciplinaryCodeViolationItemIndex).html(response);
		},
		error: function(jqXHR, textStatus, errorThrown) {
			alert("Error - status: " + textStatus + "; error: "
					+ errorThrown);
			 $("#disciplinaryCodeViolationItemDescription" + disciplinaryCodeViolationItemIndex).html(jqXHR.responseText );
		}
	});
};

function conditionChangeFunction(conditionViolationItemIndex) {
	var condition = $("#conditionViolationItems\\["+ conditionViolationItemIndex +"\\]\\.condition");
	var url = "showConditionDescription.html";
	$.ajax({
		type: "GET",	
		async: false,
		url: url,
		data:{
			condition: condition.val()
		},
		success: function(response) {
			 $("#conditionViolationItemDescription" + conditionViolationItemIndex).html(response);
		},
		error: function(jqXHR, textStatus, errorThrown) {
			alert("Error - status: " + textStatus + "; error: "
					+ errorThrown);
			 $("#conditionViolationItemDescription" + conditionViolationItemIndex).html(jqXHR.responseText );
		}
	});
};


function applyJurisdictionAndEventDateOnClick() {
	var jurisdiction = $("#jurisdiction");
	var eventDate = $("#eventDate");
	var category = $("#category");
	jurisdiction.change(function(){
		$.ajax(config.ServerConfig.getContextPath() + "/violationEvent/showUnitOptions.html", {
			type: "GET",	
			async: false,
			data:{
				jurisdiction: this.value,
			},
			success: function(response) {
				 $("#unit").html(response);
			},
			error: function(jqXHR, textStatus, errorThrown) {
				alert("Error - status: " + textStatus + "; error: "
						+ errorThrown);
			}
		});
	});
	if(category.val() == "DISCIPLINARY"){
		jurisdiction.change(function(){
			jurisdictionOrEventDateDoOnChange(jurisdiction, eventDate, category);
		});
	}
	eventDate.change(function(){
		jurisdictionOrEventDateDoOnChange(jurisdiction, eventDate, category);
	});
};

function jurisdictionOrEventDateDoOnChange(jurisdiction, eventDate, category){
	if((jurisdiction.val() && eventDate.val() && category.val() == "DISCIPLINARY") 
			|| (eventDate.val() && category.val() == "SUPERVISION")){
		jurisdictionOrEventDateChangeFunction(jurisdiction, eventDate, category);
	}
	else{
		if(category.val() == "DISCIPLINARY"){
			for (var index = 0; index < currentDisciplinaryCodeViolationItemIndex; index++) {
				 $("#disciplinaryCodeViolationItems\\["+ index +"\\]\\.disciplinaryCode").html('<option value="">...</option>"');
				 $("#disciplinaryCodeViolationItemDescription" + index).html('');
			}
		}
		else if(category.val() == "SUPERVISION"){
			for (var index = 0; index < currentConditionViolationItemIndex; index++) {
				$("#conditionViolationItems\\["+ index +"\\]\\.condition").html('<option value="">...</option>"');
				$("#conditionViolationItemDescription" + index).html('');
			}
		}
	}
};

function jurisdictionOrEventDateChangeFunction(jurisdiction, eventDate, category) {
	var data = [];
	var url;
	var currentIndex;
	var violationType;

	if(category.val() == "DISCIPLINARY"){
		url = "showDisciplinaryCodeOptions.html";
		currentIndex = currentDisciplinaryCodeViolationItemIndex;
		violationType = "disciplinaryCode";
		
		data = {
			jurisdiction: jurisdiction.val(),
			eventDate: eventDate.val()
		}
	}
	else if(category.val() == "SUPERVISION"){
		var offender = $("#offenderId");
		url = "showConditionOptions.html";
		currentIndex = currentConditionViolationItemIndex;
		violationType = "condition";
		
		data = {
				offender: offender.val(),
				eventDate: eventDate.val()
			}
	}
	
	var currentViolations = [];
	var currentDesc = [];
	for (var index = 0; index < currentIndex; index++) {
		currentViolations.push($("#" + violationType + "ViolationItems\\["+ index +"\\]\\." + violationType).val());
	}
	for (var index = 0; index < currentIndex; index++) {
		currentDesc.push($("#" + violationType + "ViolationItemDescription" + index).html());
	}
	
	$.ajax({
		type: "GET",	
		async: false,
		url: url,
		traditional: true,
		data: data,
		success: function(response) {
			for (var index = 0; index < currentIndex; index++) {
				 $("#" + violationType + "ViolationItems\\["+ index +"\\]\\." + violationType).html(response);
				 $("#" + violationType + "ViolationItems\\["+ index +"\\]\\." + violationType).val(currentViolations[index]);
				 $("#" + violationType + "ViolationItemDescription" + index).html(
						 ($("#" + violationType + "ViolationItems\\["+ index +"\\]\\." + violationType).val() !== '' ? currentDesc[index] : ''));
			}
		},
		error: function(jqXHR, textStatus, errorThrown) {
			alert("Error - status: " + textStatus + "; error: "
					+ errorThrown);
			for (var index = 0; index < currentIndex; index++) {
				 $("#" + violationType + "ViolationItems\\["+ index +"\\]\\." + violationType).html(jqXHR.responseText );
				 $("#" + violationType + "ViolationItemDescription" + index).html('');
			}
		}
	});
};

function applyJurisdictionFilterOnClick() {
	var filter = $("input[name=jurisdictionFilter]");
	filter.click(function() {
		jurisdictionFilterChangeFunction(this.value);
	});
};

function jurisdictionFilterChangeFunction(filter) {
	var url = "showJurisdictionOptions.html";
	var eventDate = $("#eventDate");
	var category = $("#category");
	var currentJurisdiction = $("#jurisdiction");
	$.ajax({
		type: "GET",	
		async: false,
		url: url,
		data:{
			jurisdictionFilter: filter,
			currentJurisdiction: currentJurisdiction.val()
		},
		success: function(response) {
			 $("#jurisdiction").html(response);
			 jurisdictionOrEventDateChangeFunction(currentJurisdiction, eventDate, category);
		},
		error: function(jqXHR, textStatus, errorThrown) {
			alert("Error - status: " + textStatus + "; error: "
					+ errorThrown);
			$("#jurisdiction").html(jqXHR.responseText );
			jurisdictionOrEventDateChangeFunction(currentJurisdiction, eventDate, category);
		}
	});
};


function assignDatePicker(elementId) {
	$("#" + elementId).datepicker({
		changeMonth: true,
		changeYear: true
	});
};

function applyFileExtensionNamer(violationEventDocumentIndex) {
	var documentData = document.getElementById("violationEventDocumentItems[" + violationEventDocumentIndex + "].data");
	var fileExtension = document.getElementById("violationEventDocumentItems[" + violationEventDocumentIndex + "].fileExtension");
	documentData.onchange = function() {
		var fileExtensionValue = documentData.value.substr(documentData.value.lastIndexOf('.')+1);
		fileExtension.value = fileExtensionValue;
	}
};
