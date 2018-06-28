/**
 * Applies a jQuery date picker to the element with the specified id.
 * 
 * @param inputIdToAssign input ID to assign
 */
function applyDatePicker(inputIdToAssign) {
	$("#" + inputIdToAssign).datepicker({
		changeMonth: true,
		changeYear: true
	});
}