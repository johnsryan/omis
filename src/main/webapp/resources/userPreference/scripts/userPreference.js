/**
 * Behavior for user preference screen.
 *
 * Author: Joel Norris
 * Version: 0.1.1 (July 19, 2018)
 * Since: OMIS 3.0
 */
window.onload = function() {
	applyRelatedUserPreferenceRangeInputsOnClick(document.getElementById("backgroundHue"), document.getElementById("backgroundSaturation"), document.getElementById("backgroundLightness"));
	applyRelatedUserPreferenceRangeInputsOnClick(document.getElementById("foregroundHue"), document.getElementById("foregroundSaturation"), document.getElementById("foregroundLightness"), document.getElementById("foregroundOpacity"));
	applyRelatedUserPreferenceRangeInputsOnClick(document.getElementById("accentHue"), document.getElementById("accentSaturation"));
	document.getElementById("changeBackgroundPhoto").onclick = function() {
		if (document.getElementById("changeBackgroundPhoto").checked) {
			document.getElementById("backgroundPhotoFieldGroup").classList.remove("hidden");
		} else {
			document.getElementById("backgroundPhotoFieldGroup").classList.add("hidden");
		}
	}
	document.getElementById('backgroundPhotoData').addEventListener('change', readURL, true);
	document.getElementById('defaultValuesButton').addEventListener('click', applyDefaultUserPreferenceValues);
	document.getElementById('shadows').addEventListener("click", function() {adjustForegroundPreview(document.getElementById("foregroundHue").value, document.getElementById("foregroundSaturation").value, document.getElementById("foregroundLightness").value, document.getElementById("foregroundOpacity").value)});
	document.getElementById('borderRadius').addEventListener("change", function() {adjustForegroundPreview(document.getElementById("foregroundHue").value, document.getElementById("foregroundSaturation").value, document.getElementById("foregroundLightness").value, document.getElementById("foregroundOpacity").value)});
}


//Assign event listener to on load event of the window.
window.addEventListener("load", assignEnhancedRangeInputControls);

/**
 * Checks for range inputs that have the "enhancedRangeInput" class
 * and ...enhances those inputs.
 */
function assignEnhancedRangeInputControls() {
	var rangeInputsToEnhance = document.getElementsByClassName("enhancedRangeInput");
	if (rangeInputsToEnhance.length > 0) {
		for(var i=0, len=rangeInputsToEnhance.length; i<len; i++) {
			//generate text input box to partner with range input and assign element value bonding
			applyEnhanceRangeInput(rangeInputsToEnhance[i], findAncestor(rangeInputsToEnhance[i], "fieldGroup"));
		}
	}
}

function applyEnhanceRangeInput(rangeInputEle, containerEle) {
	var node = document.createElement("input");
	node.setAttribute("type", "number");
	node.setAttribute("id", rangeInputEle.id + "Number");
	node.setAttribute("min", rangeInputEle.min);
	node.setAttribute("max", rangeInputEle.max);
	node.setAttribute("step", rangeInputEle.step);
	node.classList.add("enhancedRangeNumberInput");
	//node.setAttribute();
	containerEle.appendChild(node);
	node.value = rangeInputEle.value;
	bondEleValuesOnChange(rangeInputEle, node);
}

function applyDefaultUserPreferenceValues() {
	//document.getElementById("foregroundHue").value = 0;
	setInputValueAndTriggerEvent(document.getElementById("foregroundHue"), 0, "change");
	setInputValueAndTriggerEvent(document.getElementById("foregroundSaturation"), 0, "change");
	setInputValueAndTriggerEvent(document.getElementById("foregroundLightness"), 90, "change");
	setInputValueAndTriggerEvent(document.getElementById("foregroundOpacity"), 1, "change");
	setInputValueAndTriggerEvent(document.getElementById("backgroundHue"), 0, "change");
	setInputValueAndTriggerEvent(document.getElementById("backgroundSaturation"), 0, "change");
	setInputValueAndTriggerEvent(document.getElementById("backgroundLightness"), 100, "change");
	setInputValueAndTriggerEvent(document.getElementById("borderRadius"), 0, "change");
	setInputValueAndTriggerEvent(document.getElementById("backgroundPhotoData"), null, "change");
	setInputValueAndTriggerEvent(document.getElementById("changeBackgroundPhoto"), true, "click");
	setInputValueAndTriggerEvent(document.getElementById("shadows"), false, "click");
	setInputValueAndTriggerEvent(document.getElementById("accentHue"), 200, "change");
	setInputValueAndTriggerEvent(document.getElementById("accentSaturation"), 100, "change");
	document.getElementById("previewBackground").style.backgroundImage = 'none';
}

/**
 * Sets a new value for the specified input element, and triggers the specified event name.
 * 
 * @param inputEle input element
 * @param newValue new value
 * @param eventName event name to trigger
 */
function setInputValueAndTriggerEvent(inputEle, newValue, eventName) {
	if (inputEle instanceof HTMLInputElement) {
		if(inputEle.getAttribute('type').toLowerCase() === 'checkbox') {
			inputEle.checked = newValue;
		} else {
			inputEle.value = newValue;
		}
		//if IE, create an event using deprecated method and dispatch, otherwise fire event using new method
		 if(/MSIE \d|Trident.*rv:/.test(navigator.userAgent)) {
			 var event = document.createEvent("Event");
			 event.initEvent(eventName, false, true);
			 inputEle.dispatchEvent(event);
		 } else {
			 inputEle.dispatchEvent(new Event(eventName));
		 }
	}
}

/**
 * Finds the ancestor of the specified element with the specified class name.
 * 
 * @param el target element
 * @param cls class to target for ancestor
 * @returns ancestor element
 */
function findAncestor(el, cls) {
    while ((el = el.parentElement) && !el.classList.contains(cls));
    return el;
}

/**
 * Reads the URL for a photo to be displayed in the preview background area.
 */
function readURL(){
    var file = document.getElementById("backgroundPhotoData").files[0];
    var reader = new FileReader();
    reader.onloadend = function(){
        document.getElementById('previewBackground').style.backgroundImage = "url(" + reader.result + ")";        
    }
    if(file){
        reader.readAsDataURL(file);
    }else{
    }
}