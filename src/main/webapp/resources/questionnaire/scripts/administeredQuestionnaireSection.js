window.onload = function(){
	applyActionMenu(document.getElementById("actionMenuLink"));
	var textValues = document.getElementsByClassName('textValue');
	var displayTexts =  document.getElementsByClassName('textValueDisplay');
	for(var i = 0; i < textValues.length; i++){
		applyTextCounter(textValues[i], displayTexts[i]);
	}
	var dateInputs =  document.getElementsByClassName('date');
	for(var i = 0; i < dateInputs.length; i++){
		applyDatePicker(dateInputs[i]);
	}
	
	var addComments =  document.getElementsByClassName('addComments');
	for(var i = 0; i < addComments.length; i++){
		addComments[i].setAttribute('data-param', i);
		addComments[i].onclick = function(){
			showHideComments(this.getAttribute('data-param'));
		};
	}
	
	var numerics =  document.getElementsByClassName('numeric');
	for(var i = 0; i < numerics.length; i++){
		applyInputValidator(numerics[i]);
		validateInput(numerics[i]);
	}
	
	var btn = document.getElementById("saveAsFinal");
	var resolver = new common.MessageResolver("omis.questionnaire.msgs.questionnaire");
	var message = resolver.getMessage("confirmFinalizeSection", null);
	
	btn.onclick = function() {
		if (confirm(message)) {
			return true;
		} else {
			return false;
		}
	};
}

function showHideComments(index){
	var ci = document.getElementById("commentsInput["+index+"]");
	var ac = document.getElementById("addComments["+index+"]");
	if(ci.style.display == "block"){
		ci.style.display = "none";
		ac.value = false;
		document.getElementById("addCommentsImg["+index+"]").classList.remove("hideComments");
		document.getElementById("addCommentsImg["+index+"]").classList.add("showComments");
	}
	else if(ci.style.display == "none"){
		ci.style.display = "block";
		ac.value = true;
		document.getElementById("addCommentsImg["+index+"]").classList.remove("showComments");
		document.getElementById("addCommentsImg["+index+"]").classList.add("hideComments");
	}
}
			