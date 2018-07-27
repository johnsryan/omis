/**
 * jQuery implementation behavior for user preference screen.
 *
 * Author: Joel Norris
 * Version: 0.1.1 (July 19, 2018)
 * Since: OMIS 3.0
 */

/**
 * Applies on click functionality for a hue range input.
 * 
 * @param hueEle hue range input
 * @param saturationEle saturation range input
 * @param lightnessEle lightness range input
 * @param opacityEle opacity range input
 */
function applyHueInputOnClick(hueEle, saturationEle, lightnessEle, opacityEle) {
	applyStyleToRelatedRangeInputs(hueEle, saturationEle, lightnessEle, opacityEle);
	hueEle.onchange = function() {
		applyStyleToRelatedRangeInputs(hueEle, saturationEle, lightnessEle, opacityEle);
		adjustPreview(hueEle.value, saturationEle.value, lightnessEle.value, opacityEle.value, hueEle.id.replace("Hue", ""));
	};
}

/**
 * Applies on click interactions for related groups of preference sliders, and interaction with the preview area.
 * 
 * @param hueEle hue input element
 * @param saturationEle saturation input element
 * @param lightnessEle lightness input element
 * @param opacityEle opacity input element
 */
function applyRelatedUserPreferenceRangeInputsOnClick(hueEle, saturationEle, lightnessEle, opacityEle) {
	
	applyStyleToRelatedRangeInputs(hueEle, saturationEle, lightnessEle, opacityEle);
	hueEle.addEventListener("change", function() {
		var saturationValue;
		var lightnessValue;
		var opacityValue;
		if(saturationEle) {
			saturationValue = saturationEle.value;
		}
		if (lightnessEle) {
			lightnessValue = lightnessEle.value;
		}
		if (opacityEle) {
			opacityValue = opacityEle.value;
		}
		applyStyleToRelatedRangeInputs(hueEle, saturationEle, lightnessEle, opacityEle);
		adjustPreview(hueEle.value, saturationValue, lightnessValue, opacityValue, hueEle.id.replace("Hue", ""));
	});
	if (saturationEle && saturationEle instanceof HTMLInputElement) {
		saturationEle.addEventListener("change", function() {
			var saturationValue;
			var lightnessValue;
			var opacityValue;
			if(saturationEle) {
				saturationValue = saturationEle.value;
			}
			if (lightnessEle) {
				lightnessValue = lightnessEle.value;
			}
			if (opacityEle) {
				opacityValue = opacityEle.value;
			}
			applyStyleToRelatedRangeInputs(hueEle, saturationEle, lightnessEle, opacityEle);
			adjustPreview(hueEle.value, saturationValue, lightnessValue, opacityValue, hueEle.id.replace("Hue", ""));
		}); 
	}
	if (lightnessEle && lightnessEle instanceof HTMLInputElement) {
		lightnessEle.addEventListener("change", function() {
			var saturationValue;
			var lightnessValue;
			var opacityValue;
			if(saturationEle) {
				saturationValue = saturationEle.value;
			}
			if (lightnessEle) {
				lightnessValue = lightnessEle.value;
			}
			if (opacityEle) {
				opacityValue = opacityEle.value;
			}
			applyStyleToRelatedRangeInputs(hueEle, saturationEle, lightnessEle, opacityEle);
			adjustPreview(hueEle.value, saturationValue, lightnessValue, opacityValue, hueEle.id.replace("Hue", ""));
		});
	}
	if (opacityEle && opacityEle instanceof HTMLInputElement) {
		opacityEle.addEventListener("change", function() {
			var saturationValue;
			var lightnessValue;
			var opacityValue;
			if(saturationEle) {
				saturationValue = saturationEle.value;
			}
			if (lightnessEle) {
				lightnessValue = lightnessEle.value;
			}
			if (opacityEle) {
				opacityValue = opacityEle.value;
			}
			applyStyleToRelatedRangeInputs(hueEle, saturationEle, lightnessEle, opacityEle);
			adjustPreview(hueEle.value, saturationValue, lightnessValue, opacityValue, hueEle.id.replace("Hue", ""));
		});
	}
}

/**
 * Bonds two elements to update each others values on change.
 * 
 * @param firstEle fire element
 * @param secondEle second element
 */
function bondEleValuesOnChange(firstEle, secondEle) {
	firstEle.addEventListener("change", function(){
		secondEle.value = firstEle.value;
	});
	secondEle.addEventListener("change", function(){
		firstEle.value = secondEle.value;
	});
}

/**
 * Applies saturation control background values. 
 * 
 * @param hueValue hue value
 * @param saturationContol saturation control
 * @param lightnessValue lightness value (optional with default)
 * @param opacityValue opacity value (optional with default)
 * 
 */
function applySaturationControlBackground(hueValue, saturationControl, lightnessValue, opacityValue) {
	if (!lightnessValue) {
		lightnessValue = 50;
	}
	if (!opacityValue) {
		opacityValue = 1;
	}
	saturationControl.style.background = "linear-gradient(to right, hsla("+ hueValue +","+saturationControl.min+"%,"+ lightnessValue +"%,"+ opacityValue +"),hsla("+ hueValue +"," + saturationControl.max + "%, "+ lightnessValue +"%,"+ opacityValue +"))";
	saturationControl.style.backgroundImage = "linear-gradient(to right, hsla("+ hueValue +","+saturationControl.min+"%,"+ lightnessValue +"%,"+ opacityValue +"),hsla("+ hueValue +"," + saturationControl.max + "%, "+ lightnessValue +"%,"+ opacityValue +"))";
}

/**
 * Applies lightness control background values.
 * 
 * @param hueValue hue value
 * @param saturationValue saturation value (optional with default)
 * @param lightnessControl lightness input control
 * @param opacityValue opacity value (optional with default)
 */
function applyLightnessControlBackground(hueValue, saturationValue, lightnessControl, opacityValue) {
	if (!saturationValue) {
		saturationValue = 100;
	}
	if (!opacityValue) {
		opacityValue = 1;
	}
	lightnessControl.style.background = "linear-gradient(to right, hsla("+ hueValue +","+saturationValue+"%,"+lightnessControl.min+"%,"+ opacityValue +"),hsla("+ hueValue +","+saturationValue+"%, 50%,"+ opacityValue +"),hsla("+ hueValue +","+saturationValue+"%, "+lightnessControl.max+"%,"+ opacityValue +"))";
	lightnessControl.style.backgroundImage = "linear-gradient(to right, hsla("+ hueValue +","+saturationValue+"%,"+lightnessControl.min+"%,"+ opacityValue +"),hsla("+ hueValue +","+saturationValue+"%, 50%,"+ opacityValue +"),hsla("+ hueValue +","+saturationValue+"%, "+lightnessControl.max+"%,"+ opacityValue +"))";
}

/**
 * Applies opacity control background values.
 * 
 * @param hueValue hue value
 * @param saturationValue saturation value (optional with default)
 * @param lightnessValue lightness value (optional with default)
 * @param opacityEle opacity input control
 */
function applyOpacityControlBackground(hueValue, saturationValue, lightnessValue, opacityEle) {
	if (!saturationValue) {
		saturationValue = 100;
	}
	if (!lightnessValue) {
		lightnessValue = 50;
	}
	opacityEle.style.background = "linear-gradient(to right, hsla("+ hueValue +","+saturationValue+"%,"+lightnessValue+"%,"+ opacityEle.min +"),hsla("+ hueValue +","+saturationValue+"%, "+lightnessValue+"%,"+ opacityEle.max +"))";
	opacityEle.style.backgroundImage = "linear-gradient(to right, hsla("+ hueValue +","+saturationValue+"%,"+lightnessValue+"%,"+ opacityEle.min +"),hsla("+ hueValue +","+saturationValue+"%, "+lightnessValue+"%,"+ opacityEle.max +"))";
}

/**
 * Applies hue control background values.
 * 
 * @param hueEle hue input control
 * @param saturationValue saturation value (optional with default)
 * @param lightnessValue lightness value (optional with default)
 * @param opacityValue opacity value (optional with default)
 */
function applyHueControlBackground(hueEle, saturationValue, lightnessValue, opacityValue) {
	if (!saturationValue) {
		saturationValue = 100;
	}
	if (!lightnessValue) {
		lightnessValue = 50;
	}
	if (!opacityValue) {
		opacityValue = 1;
	}
	hueEle.style.background = "linear-gradient(to right, hsla(0,"+saturationValue+"%,"+lightnessValue+"%, "+opacityValue+"),hsla(60,"+saturationValue+"%, "+lightnessValue+"%, "+opacityValue+"), hsla(120,"+saturationValue+"%, "+lightnessValue+"%, "+opacityValue+"), hsla(180, "+saturationValue+"%, "+lightnessValue+"%, "+opacityValue+"), hsla(240,"+saturationValue+"%,"+lightnessValue+"%, "+opacityValue+"),hsla(300,"+saturationValue+"%,"+lightnessValue+"%, "+opacityValue+"), hsla(359, "+saturationValue+"%, "+lightnessValue+"%, "+opacityValue+"))";
	hueEle.style.backgroundImage = "linear-gradient(to right, hsla(0,"+saturationValue+"%,"+lightnessValue+"%, "+opacityValue+"),hsla(60,"+saturationValue+"%, "+lightnessValue+"%, "+opacityValue+"), hsla(120,"+saturationValue+"%, "+lightnessValue+"%, "+opacityValue+"), hsla(180, "+saturationValue+"%, "+lightnessValue+"%, "+opacityValue+"), hsla(240,"+saturationValue+"%,"+lightnessValue+"%, "+opacityValue+"),hsla(300,"+saturationValue+"%,"+lightnessValue+"%, "+opacityValue+"), hsla(359, "+saturationValue+"%, "+lightnessValue+"%, "+opacityValue+"))";
}

/**
 * Applies styling to specified inputs with values from the specified inputs.
 *  
 * @param hueEle hue control input
 * @param saturationEle saturation control input (optional)
 * @param lightnessEle lightness control input (optional)
 * @param opacityEle opacity control input (optional)
 */
function applyStyleToRelatedRangeInputs(hueEle, saturationEle, lightnessEle, opacityEle) {
	var saturationValue;
	var lightnessValue;
	var opacityValue;
	if(saturationEle) {
		saturationValue = saturationEle.value;
	}
	if (lightnessEle) {
		lightnessValue = lightnessEle.value;
	}
	if (opacityEle) {
		opacityValue = opacityEle.value;
	}
	applyHueControlBackground(hueEle, saturationValue, lightnessValue, opacityValue);
	if (saturationEle && saturationEle instanceof HTMLInputElement) {
		applySaturationControlBackground(hueEle.value, saturationEle, lightnessValue, opacityValue);
	}
	if (lightnessEle && lightnessEle instanceof HTMLInputElement) {
		applyLightnessControlBackground(hueEle.value, saturationValue, lightnessEle, opacityValue);
	}
	if (opacityEle && opacityEle instanceof HTMLInputElement) {
		applyOpacityControlBackground(hueEle.value, saturationValue, lightnessValue, opacityEle);
	}
}

function adjustForegroundPreview(hue, saturation, lightness, opacity) {
	document.getElementById("previewFieldSet").style.backgroundColor = "hsla(" + hue  + ", " + saturation + "%, "+lightness+"%, "+opacity+")";
	document.getElementById("previewFieldSet").style.borderColor = "hsla(" + hue  + ", " + saturation + "%, "+lightness+"%, "+opacity+")";
	document.getElementById("previewFieldLegend").style.backgroundColor = "hsla(" + hue  + ", " + saturation + "%, 80%, 1)";
	document.getElementById("previewHeader").style.backgroundColor = "hsla(" + hue  + ", " + saturation + "%, "+lightness+"%, "+opacity+")";
	document.getElementById("previewHeader").style.borderColor = "hsla(" + hue  + ", " + saturation + "%, "+lightness+"%, "+opacity+")";
	if (document.getElementById("shadows").checked) {
		document.getElementById("previewHeader").style.boxShadow = "5px 5px 5px hsla(0, 0%, 0%, "+opacity+")";
		document.getElementById("previewFieldSet").style.boxShadow = "5px 5px 5px hsla(0, 0%, 0%, "+opacity+")";
	} else {
		document.getElementById("previewHeader").style.boxShadow = "none";
		document.getElementById("previewFieldSet").style.boxShadow = "none";
	}
	document.getElementById("previewHeader").style.borderRadius = document.getElementById("borderRadius").value + "px";
	document.getElementById("previewFieldSet").style.borderRadius = document.getElementById("borderRadius").value + "px";
}

function adjustBackgroundPreview(hue, saturation, lightness) {
	document.getElementById("previewBackground").style.backgroundColor = "hsla(" + hue  + ", " + saturation + "%, "+lightness+"%, 1)";
}


/**
 * Adjusts the preview to match the specified hue and saturation values.
 * 
 * @param hue hue value
 * @param saturation saturation value
 * @param lightness lightness
 * @param opacity opacity
 * @param previewArea are of preview to adjust
 */
function adjustPreview(hue, saturation, lightness, opacity, previewArea) {
	if (!lightness) {
		lightness = 50;
	}
	if (!opacity) {
		opacity = 1;
	}
	if (previewArea === "foreground") {
		adjustForegroundPreview(hue, saturation, lightness, opacity);
	}
	if (previewArea === "accent") {
		document.getElementById("previewTableHeader").style.backgroundColor = "hsla(" + hue  + ", " + saturation + "%, 40%, 1)";
	}
	if (previewArea === "background") {
		adjustBackgroundPreview(hue, saturation, lightness);
	}
}
