function assignResidenceOnClick() {
	var originalState = $("#state").val();
	var stateSelector = $("#state");
	var citySelector = $("#city");
	var zipCodeSelector = $("#zipCode");
	var originalZip = $("#zipCode").val();
	//populating state selector
	stateSelector.change(function() {		
		//checking to see if the the value currently in state = the value on form load
		if (stateSelector.val() != originalState) {
			$.ajax({
				type: "GET",
				async: false,
				url: "showCityOptions.html",
				data: {state: stateSelector.val()},
				success: function(response) {
					citySelector.html(response);
					originalState = stateSelector.val();					
					allowedLocations(
							$("#statusOption").val(), 
							citySelector.val(), originalState);						
				},				
				error: function(jqXHR, textStatus, errorThrown)	{
					alert("Error - status: " + textStatus + "; error: "
							+ errorThrown);
					citySelector.html(jqXHR.responseText);
				}
			});
			$.ajax({
				type: "GET",
				async: false,
				url: "showZipCodeOptions.html", 
				data: {city: citySelector.val()},
				success: function(response) {
					zipCodeSelector.html(response);						
				},
				error: function(jqXHR, textStatus, errorThrown)	{
					alert("Error - status: " + textStatus + "; error: "
							+ errorThrown);
					zipCodeSelector.html(jqXHR.responseText);
				}				
			});			
		} else {
			$.ajax({
				type: "GET",
				async: false,
				url: "showZipCodeOptions.html", 
				data: {city: citySelector.val()},
				success: function(response) {
					zipCodeSelector.html(response);						
				},
				error: function(jqXHR, textStatus, errorThrown)	{
					alert("Error - status: " + textStatus + "; error: "
							+ errorThrown);
					zipCodeSelector.html(jqXHR.responseText);
				}				
			});			
		}
	
	});
	
	citySelector.change(function() {	
		if (citySelector.val() != '') {
			allowedLocations($("#statusOption").val(), 
					citySelector.val(), originalState);
			$.ajax({
				type: "GET",
				async: false,
				url: "showZipCodeOptions.html", 
				data: {city: citySelector.val()},
				success: function(response) {
					zipCodeSelector.html(response);						
				},
				error: function(jqXHR, textStatus, errorThrown)	{
					alert("Error - status: " + textStatus + "; error: "
							+ errorThrown);
					zipCodeSelector.html(jqXHR.responseText);
				}				
			});			
		} else {
			$.ajax({
				type: "GET",
				async: false,
				url: "showZipCodeOptions.html", 
				data: {city: citySelector.val()},
				success: function(response) {
					zipCodeSelector.html(response);						
				},
				error: function(jqXHR, textStatus, errorThrown)	{
					alert("Error - status: " + textStatus + "; error: "
							+ errorThrown);
					zipCodeSelector.html(jqXHR.responseText);
				}				
			});			
		}
	});	
};

function assignFormOptions() {
	$("#statusOptionGroup").click(function() {
		//checks to see if status option is checked
		var statusOptionChecked = ($('input[name="statusOption"]:checked', "residenceForm"));
		//gets the value of the checked option
		var statusOptionCheckedVal = ($("input[name=statusOption]:checked").val());
		//gets the length of the status options
		var statusOptionLen = $("#statusOptionGroup").length;
		var city = $("#city");
		var state = $("#state");
		//cycle thru the length of the status options and look for the selected option
		for(var i = 0; i < statusOptionLen; i++) {
			if(statusOptionChecked) {	
				checked = statusOptionCheckedVal;
				var homeless = "HOMELESS";
				var groupHome = "GROUP_HOME";
				var hotel = "HOTEL";
				var primary = "PRIMARY_RESIDENCE";
				var secondary = "SECONDARY_RESIDENCE";
				var fosterCare = "FOSTER_CARE";
				var groupHomeHotel = groupHome || hotel;
				var residenceTerm = primary || secondary || fosterCare;
				if (checked == homeless) {
					homelessRadioButtonChecked();
				} else if (checked == groupHome || checked == hotel) {
					allowedLocations(checked, city.val(), state.val());					
				} else if (checked == primary || checked == secondary || checked == fosterCare) {
					primarySecondaryFosterCareRadioButton();
				} else {
					alert("Error: No status option is checked.");
				}				
			} else {
				alert("Error: No option checked");
			}
		}
		return true;
	
	});
};

function allowedLocations(statusOption, city, state) {
	if (statusOption == "GROUP_HOME" || statusOption == "HOTEL") {
		if (city != '') {
			allowedCityLocations(statusOption, city);
		} else {
			allowedStateLocations(statusOption, state);		
		}
		createNewLocation();
	}
};

function allowedCityLocations(statusOption, city) {
	$.ajax({
		type:"GET",
		async: false,
		url: "showAllowedResidenceLocationOptions.html",
		data: {residenceStatus: statusOption, 
				city: city},
		success: function(response) {
			$("#location").html(response);
		},
		error: function(jqXHR, textStatus, errorThrown)	{
				alert("Error - status: " + textStatus + "; error: "
						+ errorThrown);
				$("#location").html(jqXHR.responseText );				
		}
	});	
};

function allowedStateLocations(statusOption, state) {
	$.ajax({
		type:"GET",
		async: false,
		url: "showAllowedResidenceLocationOptions.html",
		data: {residenceStatus: statusOption, 
				state: state},
		success: function(response) {
			$("#location").html(response);
		},
		error: function(jqXHR, textStatus, errorThrown)	{
				alert("Error - status: " + textStatus + "; error: "
						+ errorThrown);
				$("#location").html(jqXHR.responseText );				
		}
	});
};

function homelessRadioButtonChecked() {		
	$("#locationGroup").addClass("hidden");
	$("#houseNumberGroup").addClass("hidden");
	$("#streetNameGroup").addClass("hidden");
	$("#streetSuffixGroup").addClass("hidden");
	$("#unitValueGroup").addClass("hidden");
	$("#zipCodeGroup").addClass("hidden");
	$("#verificationFieldSet").addClass("hidden");
};

function groupHomeHotelRadioButtonChecked() {
	$("#location").removeClass("hidden");
	$("#locationName").addClass("hidden");
	$("#houseNumberGroup").addClass("hidden");
	$("#streetNameGroup").addClass("hidden");
	$("#streetSuffixGroup").addClass("hidden");
	$("#unitValueGroup").addClass("hidden");
	$("#zipCodeGroup").addClass("hidden");
	$("#locationGroup").removeClass("hidden");	
	$("#verificationFieldSet").removeClass("hidden");
};

function createNewLocationChecked() {
	$("#locationName").removeClass("hidden");	
	$("#location").addClass("hidden");
	$("#houseNumberGroup").removeClass("hidden");
	$("#streetNameGroup").removeClass("hidden");
	$("#streetSuffixGroup").removeClass("hidden");
	$("#unitValueGroup").removeClass("hidden");
	$("#zipCodeGroup").removeClass("hidden");
};

function primarySecondaryFosterCareRadioButton() {	
	$("#locationGroup").addClass("hidden");
	$("#houseNumberGroup").removeClass("hidden");
	$("#streetNameGroup").removeClass("hidden");
	$("#streetSuffixGroup").removeClass("hidden");
	$("#unitValueGroup").removeClass("hidden");
	$("#zipCodeGroup").removeClass("hidden");
	$("#verificationFieldSet").removeClass("hidden");
};

function applyVerifiedByUserSearch() {
	applySearchUserAccountsAutocomplete(document.getElementById("verifiedBy"),
			document.getElementById("verifiedCurrentLabel"),
			document.getElementById("verifiedByUserAccount"),
			document.getElementById("clearVerifiedByUserLink"),
			document.getElementById("verifiedByUser"));
};

function createNewLocation() {
	$("#createNewLocation").click(function() {
		var newLocationChecked = $("#createNewLocation").is(':checked');	
		if (newLocationChecked) {
			createNewLocationChecked();
		} else {	
			groupHomeHotelRadioButtonChecked();
		}
	});	
};


/**
 * Applies on click functionality for the new city radio button.
 *
 */
function applyNewCityOnClick() {
	var falseNewCity = $("#falseNewCity");
	var trueNewCity = $("#trueNewCity");
	if (trueNewCity != null) {
		trueNewCity.click(function() {
				if (!$("#existingCityGroup").hasClass("hidden")) {
					$("#existingCityGroup").addClass("hidden");
				}
				if ($("#newCityGroup").hasClass("hidden")) {
					$("#newCityGroup").removeClass("hidden");
				}
				newZipCodeDisplay();
				$("#trueNewZipCode").attr('checked', 'checked');
				$("#falseNewZipCode").prop("disabled", true);
		});
	}
	if (falseNewCity != null) {
		falseNewCity.click(function() {
				if ($("#existingCityGroup").hasClass("hidden")) {
					$("#existingCityGroup").removeClass("hidden");
				}
				if (!$("#newCityGroup").hasClass("hidden")) {
					$("#newCityGroup").addClass("hidden");
				}
				$("#falseNewZipCode").prop("disabled", false);
		});
	}
};

/**
 * Applies on click functionality for the new zip code radio button.
 *  
 */
function applyNewZipCodeOnClick() {
	var falseNewZipCode = $("#falseNewZipCode");
	var trueNewZipCode = $("#trueNewZipCode");
	trueNewZipCode.click(function() {
		newZipCodeDisplay();
		$("#newZipCode").val(true);
	});
	falseNewZipCode.click(function() {
		if (!$("#newZipCodeGroup").hasClass("hidden")) {
			$("#newZipCodeGroup").addClass("hidden");
		}
		if ($("#existingZipCodeGroup").hasClass("hidden")) {
			$("#existingZipCodeGroup").removeClass("hidden");
		}
		$("#newZipCode").val(false);
	});
};

/**
 * Display new zip code fields with the specified name.
 * 
 *
 */
function newZipCodeDisplay() {
	if ($("#newZipCodeGroup").hasClass("hidden")) {
		$("#newZipCodeGroup").removeClass("hidden");
	}
	if (!$("#existingZipCodeGroup").hasClass("hidden")) {
		$("#existingZipCodeGroup").addClass("hidden");
	}
};