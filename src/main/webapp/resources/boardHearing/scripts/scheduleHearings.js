window.onload = function() {
	applyActionMenu(document.getElementById("actionMenuLink"));
	var dates = document.getElementsByClassName("date");
	for (var i = 0; i < dates.length; i++) {
		applyDatePicker(dates[i]);
	}
	var checkboxes = document.getElementsByClassName("selectEligibility");
	for (var i = 0; i < checkboxes.length; i++) {
		checkboxes[i].onchange = function(event) {
			if (event.target.checked) {
				event.target.parentNode.parentNode.nextSibling.getElementsByClassName("hearingFields")[0].classList.remove("hidden");
			} else {
				event.target.parentNode.parentNode.nextSibling.getElementsByClassName("hearingFields")[0].classList.add("hidden");
			}
		}
		if (checkboxes[i].checked) {
			checkboxes[i].parentNode.parentNode.nextSibling.getElementsByClassName("hearingFields")[0].classList.remove("hidden");
		} else {
			checkboxes[i].parentNode.parentNode.nextSibling.getElementsByClassName("hearingFields")[0].classList.add("hidden");
		}
	}
	

	/*var groupEdit = document.getElementsByName("scheduleAsGroup");
	var items = document.getElementsByClassName("hearingFields");
	for(var i = 0; i < groupEdit.length; i++){
		groupEdit[i].onchange = function(event){
			if (event.target.value == 'true') {
				document.getElementById("groupHearingFields").classList.remove("hidden");
				for(var j = 1; j < items.length; j++){
					items[j].classList.add("hidden");
				}
				
			} else {
				document.getElementById("groupHearingFields").classList.add("hidden");
				for(var j = 1; j < items.length; j++){
					if (items[j].parentNode.getElementsByClassName("selectEligibility")[0].checked) {
						items[j].classList.remove("hidden");
					}
				}
			}
		}
		if(groupEdit[i].checked){
			if(groupEdit[i].value == 'true'){
				document.getElementById("groupHearingFields").classList.remove("hidden");
				for(var j = 1; j < items.length; j++){
					items[j].classList.add("hidden");
				}
			} else {
				document.getElementById("groupHearingFields").classList.add("hidden");
				for(var j = 1; j < items.length; j++){
					if (items[j].parentNode.getElementsByClassName("selectEligibility")[0].checked) {
						items[j].classList.remove("hidden");
					}
				}
			}
		}
	}*/
}