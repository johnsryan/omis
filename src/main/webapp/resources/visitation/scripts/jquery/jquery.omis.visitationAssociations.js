/**
 * Visitation associations jQuery implementation.
 * 
 * Author: Joel Norris
 * Author: Sheronda Vaughn
 * Version: 0.1.0 (January 26th, 2016)
 * Since: OMIS 3.0
 */
function applyVisitationAssociationRowsOnClick() {
	$(".visitationAssociationSummary > td > .actionMenuItem").each(function () {
		applyActionMenu(document.getElementById($(this).attr("id")), showOnClick($(this), document.getElementById("visitorCheckInFormContainer")));		
	});
	$(".visitationAssociationSummary > td > .viewAlternativeNamesLink").each(function () {
		applyAlternateNamesMoreActionMenuOnClick(document.getElementById($(this).attr("id")));
	});
}

function applyVisitRowsOnClick() {
	$(".visitSummary > td > .actionMenuItem").each(function () {
		applyActionMenu(document.getElementById($(this).attr("id")));
	});
}

/**
 * Applies on click to show the target element upon on click of the
 * specified element.
 *  
 * @param ele element
 * @param target target element to show
 */
function showOnClick(ele, target) {

	return function (diffEle, diffTarget) {
//		$(ele.find(".visitationAssociationRemoveLink")).click(
//			function() {
//				ui.confirm("visitation", "visitationAssociationRemoveOnClick");
//		});
		var visitationAssociationValue = $(ele).attr("id").replace("visitationAssociationRowActionMenuLink", "");
		$("#visitorSummary" + visitationAssociationValue + "RemoveLink").click(
				function() {
					return ui.confirm("omis.visitation.msgs.visitation", "visitationRemoveConfirmationMessage");
				});
		$(diffTarget.find(".checkInLink")[0]).click(function () {
			$("#visitationAssociation").val(visitationAssociationValue);
			$(target).removeClass("hidden");
		});		
	};
}

/**
 * Applies on click functionality for the badge number form.
 */
function applyVisitorCheckInFormOnClick() {
	$("#modalFormCancelButton").click(function() {
		$("#visitorCheckInFormContainer").addClass("hidden");
	});
}

/**
 * Applies on click functionality for the visitor log form.
 */
function applyVisitLogFormOnClick() {
	assignDatePicker("date");
	assignDatePicker("startDate");
	assignDatePicker("endDate");
}

/*
 * Assign a date picker to the DOM element with the specified id.
 * 
 * @param elementId dom element id
 */
function assignDatePicker(elementId) {
	if ( $("#" + elementId).prop('type') != 'date' ) {
		$("#" + elementId).datepicker({
			changeMonth: true,
			changeYear: true
		});
	}
};

function applyAlternateNamesMoreActionMenuOnClick(moreAlternativeNames) {
	var visitationAssociationRow = $(moreAlternativeNames).closest("tr.visitationAssociationSummary");	
	$(moreAlternativeNames).click(function() {	
		$.ajax(moreAlternativeNames.href,				
				{async: false, type: "GET",
				cache: false,
				success: function(response) {		
					$(".altNameSummary").remove();
					$(response).insertAfter(visitationAssociationRow);						
				},
				error: function(jqXHR, textStatus, errorThrown) {
					alert("Error - status: " + textStatus + "; error: "
						+ errorThrown);
					$(visitationAssociationRow).html(jqXHR.responseText );
				}
			});		
		return false;
	});	
};