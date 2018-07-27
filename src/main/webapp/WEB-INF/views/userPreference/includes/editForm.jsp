<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:setBundle basename="omis.media.msgs.media" var="mediaBundle"/>
<fmt:bundle basename="omis.userpreference.msgs.userPreference">
	<form:form commandName="userPreferenceForm" enctype="multipart/form-data" class="editForm">
		<fieldset>
			<legend><fmt:message key="generalOptionsLegendLabel"/></legend>
			<input type="button" id="defaultValuesButton" value="<fmt:message key='setDefaultButtonLabel'/>"/>
		</fieldset>
		<fieldset class="hidden">
			<legend><fmt:message key="displayThemeLegendLabel"/></legend>
			<span class="fieldGroup">
				<form:label class="fieldLabel" path="displayTheme"><fmt:message key="displayThemeLabel"/></form:label>
				<form:select path="displayTheme">
					<form:options items="${displayThemes}" itemValue="name"/>
				</form:select>
			</span>
		</fieldset>
		<fieldset>
			<legend><fmt:message key="backgroundPreferencesLabel"/></legend>
			<span class="fieldGroup">
				<form:label class="fieldLabel" path="backgroundHue"><fmt:message key="hueLabel"/></form:label>
				<form:input class="preferenceSlider spectrum enhancedRangeInput" type="range" path="backgroundHue" min="0" max="359"/>
<%-- 				<input type="number" id="backgroundHueText" class="rangeInputBox" value="${userPreferenceForm.backgroundHue}"/> --%>
				<form:errors path="backgroundHue"/>
			</span>
			<span class="fieldGroup">
				<form:label class="fieldLabel" path="backgroundSaturation"><fmt:message key="saturationLabel"/></form:label>
				<form:input class="preferenceSlider enhancedRangeInput" type="range" path="backgroundSaturation" min="0" max="100"/>
				<form:errors path="backgroundSaturation"/>
			</span>
			<span class="fieldGroup">
				<form:label class="fieldLabel" path="backgroundLightness"><fmt:message key="lightnessLabel"/></form:label>
				<form:input class="preferenceSlider enhancedRangeInput" type="range" path="backgroundLightness" min="0" max="100"/>
				<form:errors path="backgroundLightness"/>
			</span>
			<span class="fieldGroup">
				<form:label path="changeBackgroundPhoto" class="fieldLabel"><fmt:message key="changeBackgroundPhotoLabel"/></form:label>
				<form:checkbox path="changeBackgroundPhoto" id="changeBackgroundPhoto" value="true"/>
			</span>
			<c:choose>
				<c:when test="${userPreferenceForm.changeBackgroundPhoto}">
					<c:set var="photoFieldGroupDisplayClass" value="fieldGroup"/>
				</c:when>
				<c:otherwise>
					<c:set var="photoFieldGroupDisplayClass" value="fieldGroup hidden"/>
				</c:otherwise>
			</c:choose>
			<span class="${photoFieldGroupDisplayClass}" id="backgroundPhotoFieldGroup">
				<label for="photoData" class="fieldLabel">
					<fmt:message key="photoFileLabel" bundle="${mediaBundle}"/>
				</label>
				<input id="backgroundPhotoData" type="file" name="backgroundPhotoData" accept="image/jpeg"/>
				<form:errors path="backgroundPhotoData" cssClass="error"/>
			</span>
		</fieldset>
		<fieldset>
			<legend><fmt:message key="foregroundPreferencesLabel"/></legend>
			<span class="fieldGroup">
				<form:label class="fieldLabel" path="foregroundHue"><fmt:message key="hueLabel"/></form:label>
				<span class="preferenceSliderWrapper">
				<form:input class="preferenceSlider spectrum enhancedRangeInput" type="range" path="foregroundHue" min="0" max="359"/>
				</span>
				<form:errors path="foregroundHue"/>
			</span>
			<span class="fieldGroup">
				<form:label class="fieldLabel" path="foregroundSaturation"><fmt:message key="saturationLabel"/></form:label>
				<span class="preferenceSliderWrapper">
				<form:input class="preferenceSlider enhancedRangeInput" type="range" path="foregroundSaturation" min="0" max="100"/>
				</span>
				<form:errors path="foregroundSaturation"/>
			</span>
			<span class="fieldGroup">
				<form:label class="fieldLabel" path="foregroundLightness"><fmt:message key="lightnessLabel"/></form:label>
				<span class="preferenceSliderWrapper">
				<form:input class="preferenceSlider enhancedRangeInput" type="range" path="foregroundLightness" min="30" max="100" step="1"/>
				</span>
				<form:errors path="foregroundOpacity"/>
			</span>
			<span class="fieldGroup">
				<form:label class="fieldLabel" path="foregroundOpacity"><fmt:message key="opacityLabel"/></form:label>
				<span class="preferenceSliderWrapper">
					<form:input class="preferenceSlider enhancedRangeInput" type="range" path="foregroundOpacity" min=".3" max="1" step="0.05"/>
				</span>
				<form:errors path="foregroundOpacity"/>
			</span>
			<span class="fieldGroup">
				<form:label class="fieldLabel" path="borderRadius"><fmt:message key="borderRadiusLabel"/></form:label>
				<form:input class="preferenceSlider enhancedRangeInput" type="range" path="borderRadius" min="0" max="8" step="1"/>
				<form:errors path="borderRadius"/>
			</span>
			<span class="fieldGroup">
				<form:label class="fieldLabel" path="shadows"><fmt:message key="shadowsLabel"/></form:label>
				<form:checkbox path="shadows" id="shadows"/>
				<form:errors path="shadows"/>
			</span>
		</fieldset>
		<fieldset>
			<legend><fmt:message key="accentPreferencesLabel"/></legend>
			<span class="fieldGroup">
				<form:label class="fieldLabel" path="accentHue"><fmt:message key="hueLabel"/></form:label>
				<form:input class="preferenceSlider spectrum enhancedRangeInput" type="range" path="accentHue" min="0" max="359"/>
				<form:errors path="accentHue"/>
			</span>
			<span class="fieldGroup">
				<form:label class="fieldLabel" path="accentSaturation"><fmt:message key="saturationLabel"/></form:label>
				<form:input class="preferenceSlider enhancedRangeInput" type="range" path="accentSaturation" min="0" max="100"/>
				<form:errors path="accentSaturation"/>
			</span>
		</fieldset>
		<fieldset>
			<legend><fmt:message key="previewLabel"/></legend>
			<div id="previewContainer">
				<div id="previewBackground">
					<h1 id="previewHeader"><fmt:message key="previewHeaderLabel"/></h1>
					<fieldset id="previewFieldSet">
						<legend id="previewFieldLegend"><fmt:message key="previewFieldSetLegendLabel"/></legend>
						<label><fmt:message key="previewInputLabel"/></label>
						<input id="previewInput" type="text"/>
						<table id="previewTable">
							<tr>
							<th id="previewTableHeader"><fmt:message key="previewTableHeaderLabel"/></th>
							</tr>
						</table>						
					</fieldset>
				</div>
			</div>
		</fieldset>
		
		<p class="buttons">
			<input type="submit" value="<fmt:message key='saveLabel' bundle='${commonBundle}'/>"/>
		</p>
	</form:form>
</fmt:bundle>