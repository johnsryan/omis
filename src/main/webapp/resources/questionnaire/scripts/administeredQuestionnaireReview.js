window.onload = function(){
	var rows = document.getElementsByClassName('actionMenuItem');
	for(var i = 0; i < rows.length; i++){
		applyActionMenu(rows[i], function() {
			applyRemoveLinkConfirmation();
		});
	}
	var btn = document.getElementById("saveAsFinal");
	var resolver = new common.MessageResolver("omis.questionnaire.msgs.questionnaire");
	var message = resolver.getMessage("confirmFinalizeQuestionnaire", null);
	
	btn.onclick = function() {
		if (confirm(message)) {
			return true;
		} else {
			return false;
		}
	};
}