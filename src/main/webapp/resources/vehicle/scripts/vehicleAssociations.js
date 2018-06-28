/**
 * Behavior to display vehicle model based on vehicle make selected.
 *
 * Author: Yidong Li.
 * 
 * Date: Sept.29, 2014
 */

window.onload = function() {
	applyActionMenu(document.getElementById("actionMenuLink"));
	// Fetch vehicle models for vehicle make on change
	applyDatePicker(document.getElementById("startDate"));
	applyDatePicker(document.getElementById("endDate"));
	applyFormUpdateChecker(document.getElementById("vehicleAssociationForm"));
	
	var vehicleMake = document.getElementById("vehicleMake");
	vehicleMake.onchange = function() {
		var request = new XMLHttpRequest();
		var url = config.ServerConfig.getContextPath() + "/vehicle/listModelsByMake.html";
		var params = "vehicleMake="+vehicleMake.options[vehicleMake.selectedIndex].value;
		request.open("GET", url + "?" + params, false);
		request.send();
		
		if (request.status == 200) 
		{
			var vehicleModels = document.getElementById("vehicleModel");
			if (vehicleModels != null) 
			{
				vehicleModels.innerHTML = request.responseText;
			} 
			else 
			{
				alert("No vehicle models to populate");
			}
		} 
		else 
		{
			alert("Error: " + request.status + " - " + request.statusText);
		} 
	}
	
	var plateNumber = document.getElementById("plateNumber");
	applyUppercaseChange(plateNumber); 
}; 

