window.onload = function() {
	applyActionMenu(document.getElementById("actionMenuLink"));
	var rows = document.getElementsByClassName('actionMenuItem');
	for(var i = 0; i < rows.length; i++){
		applyActionMenu(rows[i], function() {
			applyRemoveLinkConfirmation();
		});
	}
	
	
	var hideButtons = document.getElementsByClassName("hideOverflow");
	var showButtons = document.getElementsByClassName("showOverflow");
	var items = document.getElementsByClassName("violationItemRow");
	for(var i = 0; i < items.length; i++){
		var descriptionText = items[i].getElementsByClassName('violationDescriptionNoOverflow')[0];
		if(descriptionText){
			if(descriptionText.scrollWidth > descriptionText.offsetWidth){
				showButtons[i].style.display = 'inline-block';
				hideButtons[i].style.display = 'inline-block';
			}
		}
	}
	for(var i = 0; i < showButtons.length; i++){
		showButtons[i].onclick = function() {
			this.parentElement.getElementsByClassName('violationDescriptionNoOverflow')[0].className = "violationDescriptionShow";
			this.style.display = 'none';
		}
		hideButtons[i].onclick = function(){
			this.parentElement.className = "violationDescriptionNoOverflow";
			this.parentElement.parentElement.getElementsByClassName("showOverflow")[0].style.display = 'inline-block';
		}
	}
}