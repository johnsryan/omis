/*
 * Link behavior.
 *
 * Author: Stephen Abson
 */

/**
 * Applies remove link behavior to elements by class name.
 * 
 * @param className class name
 */
function applyRemoveLinkConfirmation(className) {
	if (typeof(className) == "undefined") {
		className = "removeLink";
	}
	applyLinkConfirmation(className, "removeConfirmMessage", "omis.msgs.common");
}

/**
 * Applies cancel link behavior to elements by class name.
 * 
 * @param className class name
 * @param param parameters
 */
function applyCancelLinkConfirmation(className, param) {
	if (typeof(className) == "undefined") {
		className = "cancelLink";
	}
	applyLinkConfirmation(className, "cancelConfirmMessage", "omis.msgs.common", [param]);
}

/**
 * Applies link confirmation behavior.
 * 
 * @param className class of link to which to apply behavior 
 * @param messageKey confirmation message message key
 * @param bundleName confirmation message bundle name
 * @param params parameters
 */
function applyLinkConfirmation(className, messageKey, bundleName, params) {
	var as = document.getElementsByTagName("a");
	var message = null;
	for (var i = 0; i < as.length; i++) {
		var a = as[i];
		var classAttr = a.getAttribute("class");
		if (classAttr != null) {
			if (classAttr.indexOf(className) != -1) {
				if (message == null) {
					var resolver = new common.MessageResolver(bundleName);
					message = resolver.getMessage(messageKey, params);
				}
				a.onclick = function() {
					if (confirm(message)) {
						return true;
					} else {
						return false;
					}
				};
			}
		}
	}
}