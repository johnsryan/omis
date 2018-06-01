/**
 * Residence detail screen behavior.
 * 
 * Author: Sheronda Vaughn
 */
window.onload = function() {	
	applyActionMenu(document.getElementById("actionMenuLink"));
	applyDatePicker(document.getElementById("startDate"));
	applyDatePicker(document.getElementById("endDate"));
	applyDatePicker(document.getElementById("verificationDate"));
	applyFormUpdateChecker(document.getElementById("residenceForm"));
	applyDisableOnSubmit(document.getElementById("residenceForm"));
	assignResidenceOnClick();
	createNewLocation();
	assignFormOptions();
	applyVerifiedByUserSearch();
	applyNewCityOnClick();
	applyNewZipCodeOnClick();
	
	var existingPrimaryResidence = document.getElementById("existingPrimaryResidence");
	if (existingPrimaryResidence != null && existingPrimaryResidence.value != "") {
		document.getElementById("existingPrimarySetSecondary").onclick = function() {
			var existingPrimaryEndDate = document.getElementById("existingPrimaryEndDate");
			if (!ui.hasClass(existingPrimaryEndDate, "hidden")) {
				ui.addClass(existingPrimaryEndDate, "hidden");
			}
		};
		document.getElementById("existingPrimarySetHistorical").onclick = function() {
			var existingPrimaryEndDate = document.getElementById("existingPrimaryEndDate");
			if (ui.hasClass(existingPrimaryEndDate, "hidden")) {
				ui.removeClass(existingPrimaryEndDate, "hidden");
			}
			var existingHistoricalEndDate = document.getElementById("existingHistoricalEndDate");
			var startDate = document.getElementById("startDate");
			existingHistoricalEndDate.value = startDate.value;
		};
		document.getElementById("startDate").onchange = function() {
			var existingHistoricalEndDate = document.getElementById("existingHistoricalEndDate");
			var startDate = document.getElementById("startDate");
			existingHistoricalEndDate.value = startDate.value;
		};
		applyDatePicker(document.getElementById("existingHistoricalEndDate"));
	}
};