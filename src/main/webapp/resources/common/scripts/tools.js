"use strict";
/** Provides UI tools including offender selection.
 * @author Ryan Johns
 * @author Stephen Abson
 * @version 0.1.0 (Feb 27, 2014) 
 * @since OMIS 3.0 */

if (typeof ui === 'undefined') {
	var ui = new Object();
}
/** Perform AJAX calls.
 * @param url url.
 * @param target target element.
 * @param type type default 'GET'.
 * @param dataType data type default 'html'.
 * @param options options. */
function ajax(url, target, onSuccess, type, dataType, options) {
	type = typeof type !== 'undefined' ? type : "GET";
	dataType = typeof dataType !== 'undefined' ? dataType : "html";
	onSuccess = typeof onSuccess !== 'undefined' ? onSuccess : function(html) {
		target.innerHTML = html;
	};
	ui.Tools.AJAX(url, target, onSuccess, type, dataType, options);
}

/** Create scrollable table.
 * @param target target table.
 * @param filter optional param.
 * @param options additional implementation options. */
function scrollTable(target, filter, options) {
	ui.Tools.SCROLL_TABLE(target, filter, options);
}

/** Follows link in target.
 * @param anchor link.
 * @param target target.
 * @param onSuccess event handler.
 * @param additional options. */
function ajaxLink(anchor, target, onSuccess, options) {
	ui.Tools.AJAX_LINK(anchor, target, onSuccess, options);
}

/** Submits a form.
 * @param form form to submit.
 * @param target target of submission.
 * @param onSuccess event handler.
 * @param onFailedValidation event handler.
 * @param precondition boolean condition that must be met inorder to submit.
 * @param options options. */
function ajaxSubmit(form, target, onSuccess, onFailedValidation, precondition, options) {
	ui.Tools.AJAX_SUBMIT(form, target, onSuccess, onFailedValidation, precondition, options);
}

/** Dialogs element.
 * @param options options. */
function dialog(element, options) {
	ui.Tools.DIALOG(element, options);
}

/** Creates action menu.
 * @param srcElement link to menu service. */
function applyActionMenu(srcElement, onload) {
	ui.Tools.ACTION_MENU(srcElement, onload);
}

/** inserts element after another element. 
 * @param element element that newElement is added after.
 * @param newElement new element to add. */
function insertAfter(element, newElement) {
	if (typeof element.insertAfter !== 'function') {
		return ui.Tools.INSERT_AFTER(element, newElement);
	}
}

/** retrieve DOM resource.
 * @param resourcePath path to resources. */
function retrieveDom(resourcePath) {
	return ui.Tools.RETRIEVE_RESOURCE(resourcePath);
}

/** Change content to upper case when it is blurred.
 * @param element to be changed to upper case. */
function applyUppercaseChange(element) {
	element.onkeyup = function() {
		element.value = element.value.toUpperCase();
	}
}

function applyMouseOutHide(element) {
	return ui.Tools.MOUSE_OUT_HIDE(element);
}

/* Defines document getElementsByClassName if not defined. */
if (typeof document.getElementsByClassName !== 'function') {
	document.getElementsByClassName = 
		ui.Tools.GET_ELEMENTS_BY_CLASS_NAME;
}
if(typeof console !== 'object') {
    var console = {
        log : function(){},
        warn : function(){},
        error : function(){},
        time : function(){},
        timeEnd : function(){}
    }
}



/* Caps lock detection on events.
 * e - event. */
function capsDetection(e) 
{
	var ev = e ? e : window.event;
	
	var shift_status = event.shiftKey==1;
	
	if (((ev.keyCode >= 65 && ev.keyCode <= 90) && !shift_status) ||
			((ev.keyCode >= 97 && ev.keyCode <= 122) && shift_status))
	{
			return true;
	}
	 else 
	 {
		return false;
	}
}

/**
 * Returns whether element has class.
 * 
 * @param elt element
 * @param className class
 * @returns whether element has class
 */
ui.hasClass = function(elt, className) {
	var classes = elt.getAttribute("class");
	if (classes != null && classes.indexOf(className) > -1) {
		return true;
	} else {
		return false;
	}
};

/**
 * Adds or replaces class of element.
 *  
 * @param elt element
 * @param oldClassName old class (to replace)
 * @param newClassName new class (with which to replace)
 */
ui.addOrReplaceClass = function(elt, oldClassName, newClassName) {
	var classes = elt.getAttribute("class");
	if (classes != null) {
		if (classes.indexOf(oldClassName) > -1) {
			classes = classes.replace(oldClassName, newClassName);
		} else {
			classes = newClassName;
		}
	} else {
		classes = classes + newClassName;
	}
	elt.setAttribute("class", classes);
};

/**
 * Removes class from element if element has class.
 * 
 * @param elt element
 * @param className class to remove
 */
ui.removeClass = function(elt, className) {
	var classes = elt.getAttribute("class");
	if (classes != null && classes.indexOf(className) > -1) {
		classes = classes.replace(className, "");
	}
	elt.setAttribute("class", classes);
};

/**
 * Adds class to element if element does not have class.
 * 
 * @param elt element
 * @param className class to add
 */
ui.addClass = function(elt, className) {
	var classes = elt.getAttribute("class");
	if (classes == null || classes.indexOf(className) == -1) {
		classes = (classes != null ? classes : "") + " " + className;
	}
	elt.setAttribute("class", classes);
};

/**
 * Appends HTML to element.
 * 
 * @param elt element to which to append
 * @param newHtml new HTML to append
 */
ui.appendHtml = function(elt, newHtml) {
	ui.appendHtmlImpl(elt, newHtml);
};

/**
 * Applies session extender to the specified element.
 * 
 * @param elt DOM element
 * @param url uniform resource locator
 * @param increment increment to check for session extension (milliseconds)
 */
function applySessionExtender(elt, url, increment) {
	var elementChanged = false;
	elt.onkeypress = function() {
		elementChanged = true;
		elt.onkeypress = null;
	}
	var refresh = setInterval(function(){
					elt.onkeypress = function() {
						elementChanged = true;
						elt.onkeypress = null;
					}
					if(elementChanged) {
						var request = new XMLHttpRequest();
						request.open("get", url , false); 
						request.send(null);
						elementChanged = false;
					}
				}, increment);
	elt.onblur = function() {clearInterval(refresh)};
}

/*
 * Applies a module group iframe displayer.
 * 
 * elt - div or span element with link children
 * nextElt - optional, element in which the links within elt will source into,
 * leave null to have one created.
 *
 * example:
 * <div id="elt" class="linkstuff">
 *	<a href="somewhere.to/link.html">go to link</a>
 *  <a href="somewhere.to/otherLink.html">go to other</a>
 * </div>
 * JS: applyModuleGroup(elt);
 * - in the above, a div will be created dynamically placed after "elt" in which
 * the iframes will be displayed
 *
 * other example:
 * <div id="elt">
 *	<a href="somewhere.to/link.html">go to link</a>
 *  <a href="somewhere.to/otherLink.html">go to other</a>
 * </div>
 * <div id="nextElt"></div>
 * JS: applyModuleGroup(elt, nextElt);
 * - in the above, the iframes will be displayed within "nextElt", this will
 * allow for styling freedom.
 */
function applyModuleGroup(elt, nextElt) {
	if (!elt) {
		console.error("No element provided to apply module group.");
		return false;
	}
	if (!nextElt) {
		nextElt = document.createElement('div');
		nextElt.classList.add("moduleGroupDisplayContainer");
		insertAfter(elt, nextElt);
	}
	var links = elt.getElementsByTagName('a');
	var iframes = [];
	var refresh = document.createElement('span');
	refresh.classList.add("moduleGroupLinkRefresh");
	refresh.setAttribute("title", "Reset Current Page");
	if(links.length) {
		elt.insertBefore(refresh, links[0]);
	} else {
		console.error("No links provided to apply module group.");
		return false;
	}
	
	for (var i = 0; i < links.length; i++) {
		iframes[i] = document.createElement('iframe');
		iframes[i].setAttribute("frameborder", 0);
		iframes[i].classList.add("hidden");
		iframes[i].classList.add("moduleGroupDisplayContent");
		iframes[i].setAttribute("src", links[i].getAttribute("href"));
		nextElt.appendChild(iframes[i]);
		links[i].onclick = function(e) {
			for (var j = 0; j < iframes.length; j++) {
				if (iframes[j].getAttribute("src") == e.target
						.getAttribute("href")) {
					iframes[j].classList.remove("hidden");
				} else {
					iframes[j].classList.add("hidden");
				}
			}
			for (var j = 0; j < links.length; j++) {
				links[j].classList.remove("activeModuleGroupLink");
			}
			e.target.classList.add("activeModuleGroupLink");
			e.preventDefault();
		};
	}
	if(iframes[0]) {
		iframes[0].classList.remove("hidden");
		links[0].classList.add("activeModuleGroupLink");
	}
	refresh.onclick = function(e) {
		var linkHref = elt.getElementsByClassName("activeModuleGroupLink")[0].getAttribute("href");
		for (var j = 0; j < iframes.length; j++) {
			if (iframes[j].getAttribute("src") == linkHref) {
				iframes[j].src = linkHref;
			}
		}
	}
}

