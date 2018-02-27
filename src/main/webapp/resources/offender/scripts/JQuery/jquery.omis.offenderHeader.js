function setContentPosition(navItem, navContentTarget) {
	var offset = $(navItem).offset();
	var height = $(navItem).height();
	var width = $(navItem).width();
	var top = offset.top + height + 5 + "px";
	var left = offset.left - 15 + "px";
	if (offset.left > ($(window).width()/2)+15) {
		left = offset.left + width - $(navContentTarget).width() + 15 + "px";
	}
	
	$(navContentTarget).css( {
		'position': 'absolute',
		'left': left,
		'top': top
	});
}