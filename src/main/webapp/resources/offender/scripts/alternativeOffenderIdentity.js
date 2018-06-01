/**
 * Behavior for editing alternative person identity.
 * 
 * @author Stephen Abson
 * @version 0.1.0 (Dec 10, 2013)
 * @since OMIS 3.0
 */
$(document).ready(function() {
	$("#birthDate").datepicker({
		changeMonth: true,
		changeYear: true
	});
	$("#deathDate").datepicker({
		changeMonth: true,
		changeYear: true
	});
	$("#startDate").datepicker({
		changeMonth: true,
		changeYear: true
	});
	$("#endDate").datepicker({
		changeMonth: true,
		changeYear: true
	});
	$("#birthCountry").change(function() {
		populateStates();
		populateCitiesByCountry();
	});
	$("#birthState").change(function() {
		if ($("#birthState").val() != null && $("#birthState").val() != '') {
			populateCitiesByState();
		} else {
			populateCitiesByCountry();
		}
	});
	$("#createNewBirthPlace").click(function() {
		if ($("#createNewBirthPlace").is(':checked')) {
			$("#birthPlaceName").show();
			$("#birthPlace").hide();
		} else {
			$("#birthPlace").show();
			$("#birthPlaceName").hide();
		}		
	});
	applyActionMenu(document.getElementById("actionMenuLink"));
});

/** Populates States drop down. */
function populateStates() {
	$.ajax(config.ServerConfig.getContextPath()
			+ "/offender/identity/alternative/findStatesByCountry.html?country=" + $("#birthCountry").val(),
			{
				async: true,
				cache: false,
				success: function(data) {
					$("#birthState").html(data);
					populateCitiesByState();
				},
				error: function(jqXHR, textStatus, errorThrown) {
					alert("Error: " + textStatus + "; " + errorThrown);
				},
				type: "GET"
			}
	);
}

/** Populates cities drop down by country. */
function populateCitiesByCountry() {
	$.ajax(config.ServerConfig.getContextPath()
			+ "/offender/identity/alternative/findCitiesByCountry.html?country=" + $("#birthCountry").val(),
			{
				async: true,
				cache: false,
				success: function(data) {
					$("#birthPlace").html(data);
				},
				error: function(jqXHR, textStatus, errorThrown) {
					alert("Error: " + textStatus + "; " + errorThrown);
				},
				type: "GET"
			}
	);
}

/** Populates cities drop down by State. */
function populateCitiesByState() {
	$.ajax(config.ServerConfig.getContextPath()
			+ "/offender/identity/alternative/findCitiesByState.html?state=" + $("#birthState").val(),
			{
				async: true,
				cache: false,
				success: function(data) {
					$("#birthPlace").html(data);
				},
				error: function(jqXHR, textStatus, errorThrown) {
					alert("Error: " + textStatus + "; " + errorThrown);
				},
				type: "GET"
			}
	);
}