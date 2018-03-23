function hearingNoteItemsCreateOnClick() {
	$("#createHearingNoteItemLink").click(function() {
		$.ajax(config.ServerConfig.getContextPath() + "/hearing/createHearingNoteItem.html",
		   {
				type: "GET",
				async: false,
				data: {hearingNoteItemIndex: currentHearingNoteItemIndex},
				success: function(data) {
					$("#hearingNoteTableBody").append(data);
					hearingNoteItemRowOnClick(currentHearingNoteItemIndex);
				},
				error: function(jqXHR, textStatus, errorThrown) {
					alert("Error - status: " + textStatus + "; error: "
						+ errorThrown);
					$("#hearingNoteTableBody").html(jqXHR.responseText );
				}
			});
		currentHearingNoteItemIndex++;
		return false;
	});
};

function infractionItemsCreateOnClick() {
	var conditionViolation;
	var disciplinaryCodeViolation;
	var rows = document.getElementsByClassName("addInfraction");
	for(var index = 0; index < rows.length; index++){
		
		$("#createInfractionItemLink" + index).click(function() {
			var i = this.id.substr(24);
			if($("#conditionViolation" + i)){
				conditionViolation = $("#conditionViolation" + i).val();
			}
			if($("#disciplinaryCodeViolation" + i)){
				disciplinaryCodeViolation = $("#disciplinaryCodeViolation" + i).val();
			}
			
			$.ajax(config.ServerConfig.getContextPath() + "/hearing/createInfractionItem.html",
			   {
					type: "GET",
					async: false,
					data: {
						infractionItemIndex: currentInfractionItemIndex,
						conditionViolation: conditionViolation,
						disciplinaryCodeViolation: disciplinaryCodeViolation
					},
					success: function(data) {
						$("#infractionTableBody").append(data);
						infractionItemRowOnClick(currentInfractionItemIndex);
					},
					error: function(jqXHR, textStatus, errorThrown) {
						alert("Error - status: " + textStatus + "; error: "
							+ errorThrown);
						$("#infractionTableBody").html(jqXHR.responseText );
					}
				});
			currentInfractionItemIndex++;
			return false;
		});
	}
}

function hearingNoteItemRowOnClick(hearingNoteItemIndex) {
	assignDatePicker("hearingNoteItemDate" + hearingNoteItemIndex);
	$("#removeHearingNoteLink" + hearingNoteItemIndex).click(function() {
		if ($("#hearingNoteOperation" + hearingNoteItemIndex).val() == "UPDATE") {
			$("#hearingNoteOperation" + hearingNoteItemIndex).val("REMOVE");
			$("#hearingNoteItemRow" + hearingNoteItemIndex).addClass("removeRow");
		} else if($("#hearingNoteOperation" + hearingNoteItemIndex).val() == "REMOVE") {
			$("#hearingNoteOperation" + hearingNoteItemIndex).val("UPDATE");
			$("#hearingNoteItemRow" +hearingNoteItemIndex).removeClass("removeRow");
		} else {
			$("#hearingNoteItemRow" + hearingNoteItemIndex).remove();
		}
		return false;
	});
};

function infractionItemRowOnClick(infractionItemIndex) {
	$("#removeInfractionLink" + infractionItemIndex).click(function() {
		if ($("#infractionOperation" + infractionItemIndex).val() == "UPDATE") {
			$("#infractionOperation" + infractionItemIndex).val("REMOVE");
			$("#infractionItemRow" + infractionItemIndex).addClass("removeRow");
		} else if($("#infractionOperation" + infractionItemIndex).val() == "REMOVE") {
			$("#infractionOperation" + infractionItemIndex).val("UPDATE");
			$("#infractionItemRow" +infractionItemIndex).removeClass("removeRow");
		} else {
			$("#infractionItemRow" + infractionItemIndex).remove();
		}
		return false;
	});
}

function applyOnClickToItems() {
	for (var index = 0; index < currentHearingNoteItemIndex; index++) {
		hearingNoteItemRowOnClick(index);
	}
	//for (var index = 0; index < currentInfractionItemIndex; index++) {
		//infractionItemRowOnClick(index);
	//}
};

function assignDatePicker(elementId) {
	$("#" + elementId).datepicker({
		changeMonth: true,
		changeYear: true
	});
};



function applyLocationTypeOnClick() {
	var locationType = $("#locationType");
	locationType.change(function() {
		 if(locationType.val()){
			 locationTypeChangeFunction();
		 }
		 else{
			 $("#location").html('<option value="">...</option>"');
		 }
	});
}

function locationTypeChangeFunction() {
	var locationType = $("#locationType");
	var url = "showLocationOptions.html";
	$.ajax({
		type: "GET",	
		async: false,
		url: url,
		data:{
			locationType: locationType.val()
		},
		success: function(response) {
			 $("#location").html(response);
		},
		error: function(jqXHR, textStatus, errorThrown) {
			alert("Error - status: " + textStatus + "; error: "
					+ errorThrown);
			$("#location").html(jqXHR.responseText );
		}
	});
}

function applyStaffAssignmentSearch(input, target, targetLabel, clear, options) {
	var msg = new common.MessageResolver("omis.search.msgs.search");
	
	if (!$(input).hasClass("lookup")) {
		var settings = options;
		settings = $.extend({ 
			onSelect: function(event, ui) {
				$(target).val(ui.item.value);
				displaySelection(ui.item.label, input, targetLabel);
				return false;
			}}, options);
		$(input).autocomplete({
			autoFocus: true,
			minLength: 4,
			source: function(request, response) {
				$.ajax({
					url: config.ServerConfig.getContextPath() + "/staffSearch/searchByNonSpecified.json?searchCriteria="+request.term,
					dataType: "json",
					cache:false,
					success: function(data) {
						response($.map( data, function( item ) {
							return {
										label: item.lastName + ", " + item.firstName + " "+ item.titleName,
										value: item.staffId
							 		};}));},
					error: function() {
						displaySelection(msg.getMessage("noResults"), input, targetLabel);
					}});},
			select: settings.onSelect });
		$(input).addClass("lookup");
		
		if (typeof clear != 'undefined') {
			$(clear).on("click", function() {
				clearFields($(target), $(input), $(targetLabel));
				return false;
		});}
		return this;
	}};

