 window.addEventListener("load",function(){
	if (document.getElementById("offenderHeader")) {
		var scrollDots = document.getElementsByClassName("scrollDot");
		var anchors = document.getElementsByClassName("scrollItem");
		var parent = document.getElementById("itemWrapper");
		var padding = 4;//parseFloat(window.getComputedStyle(parent).paddingLeft);
		//^ window.getComputedStyle causes this to break if multiple tabs are open, so I'm hard-coding the padding amount in until I find a better solution.
		anchors[0].className += " currentItem";
		scrollDots[0].className += " activeScrollDot";
		
		$("#itemWrapper").scroll(function() {
			for(var i = 0; i < scrollDots.length; i++) {
				if(parent["scrollLeft"] >= anchors[i].offsetLeft - parent.offsetLeft - padding) {
					for(var j = 0, scrollDot; scrollDot = scrollDots[j]; j++) {
						scrollDot.classList.remove("activeScrollDot");
					}
					for(var j = 0, anchor; anchor = anchors[j]; j++) {
						anchor.classList.remove("currentItem");
					}
					scrollDots[i].className += " activeScrollDot";
					anchors[i].className += " currentItem";
				}
			}
		});
		
		for(var i = 0, scrollButton; scrollButton = scrollDots[i]; i++) {
			scrollButton.onclick = function(event) {
				event.preventDefault();
				var trgt = event.target.id + "_contentPane";
				var target = document.getElementById(trgt);
				scrollTo(target);
			};
		}
		document.getElementById('rightArrow').onclick = function(e) {
			arrowScroll("right");
			e.preventDefault();
		};
		document.getElementById('leftArrow').onclick = function(e) {
			arrowScroll("left");
			e.preventDefault();
		};
		function scrollTo(target){
			var from = parent.scrollLeft,
				to = (target.offsetLeft - parent.offsetLeft - padding),
				time = 200,
				start = new Date().getTime(),
				timer = setInterval(function () {
					var step = Math.min(1, (new Date().getTime() - start) / time);
					parent["scrollLeft"] = (from + step * (to - from));
					if (step === 1) {
						clearInterval(timer);
					}
				}, 25);
			parent["scrollLeft"] = from;
		}
		
		function arrowScroll(direction){
			var selected = document.getElementsByClassName("currentItem")[0];
			var pos = [].indexOf.call(anchors, selected);
			var target;
			if(direction == 'left'){
				var prev = anchors[pos - 1];
				if(prev == undefined){
					prev = anchors.item(anchors.length-1);
				}
				target = prev;
			}
			else if(direction == 'right'){
				var next = anchors[pos + 1];
				if(next == undefined){
					next = anchors.item(0);
				}
				target = next;
			}
			else{
				throw new exception("Incompatible Direction Given.")
			}
			scrollTo(target);
		}
		
		var fieldValues = document.getElementsByClassName("offenderHeaderFieldValue");
		for(var i = 0, fieldValue; fieldValue = fieldValues[i]; i++) {
			fieldValue.setAttribute('title', fieldValue.innerHTML.trim());
		}
		
		if(document.getElementById('navBar')){
			var scrollDistance = ((document.getElementsByClassName('navBarItem')[0].offsetWidth) + padding); 
			var navBar = document.getElementById('navBar');
			document.getElementById('navRightArrow').onclick = function(e) {
				navBarScroll("right");
				e.preventDefault();
			}
			document.getElementById('navLeftArrow').onclick = function(e) {
				navBarScroll("left");
				e.preventDefault();
			}
			
			function navBarScroll(direction){
				var to;
				if(direction == 'left'){
					to = (navBar.scrollLeft - scrollDistance);
				}
				else if(direction == 'right'){
					to = (navBar.scrollLeft + scrollDistance);
				}
				else{
					throw new exception("Incompatible Direction Given.")
				}
				var from = navBar.scrollLeft,
				time = 200,
				start = new Date().getTime(),
				timer = setInterval(function () {
					var step = Math.min(1, (new Date().getTime() - start) / time);
					navBar["scrollLeft"] = (from + step * (to - from));
					if (step === 1) {
						clearInterval(timer);
					}
				}, 25);
				navBar["scrollLeft"] = from;
			}
			
			var navItems = document.getElementsByClassName("navBarItem");
			var navContentItems = document.getElementsByClassName("navContent");
			for(var i = 0, navItem; navItem = navItems[i]; i++) {
				navItem.onclick = function(event) {
					event.preventDefault();
					event.target.classList.toggle("activeNavItem");
					var contentId = event.target.id + "Content";
					var navContentTarget = document.getElementById(contentId);
					
					navContentTarget.classList.toggle("navContentShow");
					for(var j = 0, navContentItem; navContentItem = navContentItems[j]; j++) {
						if(navContentItem != navContentTarget){
							navContentItem.classList.remove("navContentShow");
						}
					}
					for(var j = 0, navItem; navItem = navItems[j]; j++) {
						if(navItem != event.target){
							navItem.classList.remove("activeNavItem");
						}
					}
	
					setContentPosition(event.target, navContentTarget);
					
				};
			}
			
			var timeout;
			document.getElementById("offenderNavHeader").onmouseleave = function(event) {
				clearTimeout(timeout);
				timeout = setTimeout(function(){
					for(var j = 0, navContentItem; navContentItem = navContentItems[j]; j++) {
						navContentItem.classList.remove("navContentShow");
					}
					for(var j = 0, navItem; navItem = navItems[j]; j++) {
							navItem.classList.remove("activeNavItem");
					}
				}, 500);
			}
			document.getElementById("offenderNavHeader").onmouseenter = function(event) {
				clearTimeout(timeout);
			}
		}
	}
});