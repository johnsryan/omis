
function staffAttendanceItemsCreateOnClick() {
	$("#createStaffAttendanceItemLink").click(function() {
		$.ajax(config.ServerConfig.getContextPath() + "/hearing/resolution/createStaffAttendanceItem.html",
		   {
				type: "GET",
				async: false,
				data: {staffAttendanceItemIndex: currentStaffAttendanceItemIndex},
				success: function(data) {
					$("#staffAttendanceTableBody").append(data);
					staffAttendanceItemRowOnClick(currentStaffAttendanceItemIndex);
				},
				error: function(jqXHR, textStatus, errorThrown) {
					alert("Error - status: " + textStatus + "; error: "
						+ errorThrown);
					$("#staffAttendanceTableBody").html(jqXHR.responseText );
				}
			});
		currentStaffAttendanceItemIndex++;
		return false;
	});
};

function staffAttendanceItemRowOnClick(staffAttendanceItemIndex) {
	applyStaffAssignmentSearch(document.getElementById("staffAttendanceInput" + staffAttendanceItemIndex),
			document.getElementById("staffAttendanceItems[" + staffAttendanceItemIndex + "].staff"),
			document.getElementById("staffAttendanceDisplay" + staffAttendanceItemIndex),
			document.getElementById("clearStaffAttendance" + staffAttendanceItemIndex));
	$("#removeStaffAttendanceLink" + staffAttendanceItemIndex).click(function() {
		if ($("#staffAttendanceOperation" + staffAttendanceItemIndex).val() == "UPDATE") {
			$("#staffAttendanceOperation" + staffAttendanceItemIndex).val("REMOVE");
			$("#staffAttendanceItemRow" + staffAttendanceItemIndex).addClass("removeRow");
		} else if($("#staffAttendanceOperation" + staffAttendanceItemIndex).val() == "REMOVE") {
			$("#staffAttendanceOperation" + staffAttendanceItemIndex).val("UPDATE");
			$("#staffAttendanceItemRow" +staffAttendanceItemIndex).removeClass("removeRow");
		} else {
			$("#staffAttendanceItemRow" + staffAttendanceItemIndex).remove();
		}
		return false;
	});
};

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