<%@ page contentType="text/css" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:choose>
	<c:when test="${boxShadow}">
		<c:set var="boxShadowValue" value="5px 5px, 5px"/>
	</c:when>
	<c:otherwise>
		<c:set var="boxShadowValue" value="none"/>
	</c:otherwise>
</c:choose>

.spectrum {
    background: linear-gradient(to right, hsl(0,100%,50%),hsl(60,100%, 50%), hsl(120,100%, 50%), hsl(180, 100%, 50%), hsl(240,100%,50%),hsl(300,100%,50%), hsl(359, 100%, 50%));
    background-image: linear-gradient(to right, hsl(0,100%,50%),hsl(60,100%, 50%), hsl(120,100%, 50%), hsl(180, 100%, 50%), hsl(240,100%,50%),hsl(300,100%,50%), hsl(359, 1000%, 50%));
}

form.editForm span.fieldGroup > input.preferenceSlider {
	width: 400px;
    height: 15px;
    border-radius: 25px;
    padding: 5px 0 5px 0;
    max-width: 100%
    margin: 0;
}

form.editForm span.fieldGroup > span.preferenceSliderWrapper > input.preferenceSlider {
	width: 100%;
    height: 15px;
    border-radius: 25px;
    padding: 5px 0 5px 0;
	max-width: 100%;
	min-width: 100%;
	margin: 0;
}

form.editForm span.fieldGroup {
	margin: 3px 0 0 0;
}

form.editForm span.fieldGroup input.enhancedRangeNumberInput {
	float: none;
	width: 5em;
	margin: 0 0 0 3px;
	border: none;
}

.preferenceSliderWrapper {
	width: 400px;
   // height: 15px;
	border-radius: 25px;
	//padding: 5px;
	display: inline-block;
	background: /* tint image */
                      linear-gradient(to right, rgba(192, 192, 192, 0.75), rgba(192, 192, 192, 0.75)),
                      /* checkered effect */
                      linear-gradient(to right, black 50%, white 50%),
                      linear-gradient(to bottom, black 50%, white 50%);
	background-image: /* tint image */
                      linear-gradient(to right, rgba(192, 192, 192, 0.75), rgba(192, 192, 192, 0.75)),
                      /* checkered effect */
                      linear-gradient(to right, black 50%, white 50%),
                      linear-gradient(to bottom, black 50%, white 50%);
    background-blend-mode: normal, difference, normal;
    background-size: 2em 2em;
    margin: 0;
 }

#foregroundSaturation {
	background: linear-gradient(to right, hsl(${foregroundHue},0%,50%),hsl(${foregroundHue},100%, 50%));
    background-image: linear-gradient(to right, hsl(${foregroundHue},0%,50%),hsl(${foregroundHue},100%, 50%));	
}

#backgroundSaturation {
	background: linear-gradient(to right, hsl(${backgroundHue},0%,50%),hsl(${backgroundHue},100%, 50%));
    background-image: linear-gradient(to right, hsl(${backgroundHue},0%,50%),hsl(${backgroundHue},100%, 50%));	
}

input[type=range]{
    -webkit-appearance: none;
}

#previewContainer {
	border: solid 1px #000;
}

#previewContainer div#previewBackground p.buttons input,
#previewContainer p.buttons > button[type="submit"],
#previewContainer p.buttons > input[type="submit"] {
    float: right;
    margin-right: 20px;
}

#previewContainer div#previewBackground fieldset#previewFieldSet table#previewTable {
	width: 98%;
}

#previewContainer div#previewBackground fieldset#previewFieldSet table#previewTable tbody tr th#previewTableHeader {
	text-align: left;
	color: white;
}

#previewContainer > fieldset > table > tr {
	width: 100%;
	
}

#previewFieldSet {
	background-color: hsla(${foregroundColorValue.hue}, ${foregroundColorValue.saturation}%, ${foregroundColorValue.lightness}%, ${foregroundColorValue.opacity});
	border-color: hsla(${foregroundColorValue.hue}, ${foregroundColorValue.saturation}%, ${foregroundColorValue.lightness}%, ${foregroundColorValue.opacity});
	width: 90%;
	margin: 0 4% 0 4%;
}

#previewFieldLegend {
	background-color: hsla(${foregroundColorValue.hue}, ${foregroundColorValue.saturation}%, 80%, 1);
}
<c:choose>
	<c:when test="${not empty backgroundPhoto}">
		<c:set var="backgroundImageURL" value="${pageContext.request.contextPath}/userPreference/displayPhoto.html?photo=${backgroundPhoto.id}&contentType=image/jpeg"/>
	</c:when>
	<c:otherwise>
		<c:set var="backgroundImageURL" value=""/>
	</c:otherwise>
</c:choose>
#previewBackground {
	background-color: hsla(${backgroundColorValue.hue},${backgroundColorValue.saturation}%, ${backgroundColorValue.lightness}%, 1);
	background-image: url('${backgroundImageURL}');
	background-attachment: fixed;
	padding: 1%;
}

#previewTableHeader {
	background-color: hsla(${accentColorValue.hue}, ${accentColorValue.saturation}%, 50%, 1);
}
