<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.address.msgs.address">
	<c:if test="${empty addressFieldsPropertyName}">
		<c:set var="addressFieldsPropertyName" value="addressFields" scope="request"/>
	</c:if>
	<div id="${addressFieldsPropertyName}Snippet">
		<span class="fieldGroup">
			<label class="fieldLabel" for="${addressFieldsPropertyName}.value"><fmt:message key="addressFieldsValueLabel"/></label>
			<input type="text" name="${addressFieldsPropertyName}.value" id="${addressFieldsPropertyName}Value" value="${addressFields.value}" class="large"/>
			<form:errors cssClass="error" path="${addressFieldsPropertyName}.value"/>
		</span>
		<span class="fieldGroup">
			<label class="fieldLabel" for="${addressFieldsPropertyName}.country"><fmt:message key="addressFieldsCountryLabel"/></label>
			<select name="${addressFieldsPropertyName}.country" id="${addressFieldsPropertyName}Country">
				<jsp:include page="addressFieldsCountryOptions.jsp"/>
			</select>
			<form:errors cssClass="error" path="${addressFieldsPropertyName}.country"/>
		</span>
		<span class="fieldGroup">
			<label class="fieldLabel" for="${addressFieldsPropertyName}.state"><fmt:message key="addressFieldsStateLabel"/></label>
			<select name="${addressFieldsPropertyName}.state" id="${addressFieldsPropertyName}State">
				<jsp:include page="addressFieldsStateOptions.jsp"/>
			</select>
			<form:errors cssClass="error" path="${addressFieldsPropertyName}.state"/>
		</span>
		<c:choose>
			<c:when test="${addressFields.newCity}">
				<c:set value="" var="newCityDisplayClass"/>
				<c:set value="hidden" var="existingCityDisplayClass"/>
			</c:when>
			<c:otherwise>
				<c:set value="hidden" var="newCityDisplayClass"/>
				<c:set value="" var="existingCityDisplayClass"/>
			</c:otherwise>
		</c:choose>
		<span class="fieldGroup">
			<span id="${addressFieldsPropertyName}NewCitySelectionFieldGroup">
				<c:choose>
					<c:when test="${addressFields.newCity}">
						<label class="fieldLabel" for="${addressFieldsPropertyName}.cityName"><fmt:message key="addressFieldsCityNameLabel"/></label>
						<form:radiobutton id="${addressFieldsPropertyName}FalseNewCity" class="fieldValue" path="${addressFieldsPropertyName}.newCity" value="false" />
						<label for="${addressFieldsPropertyName}FalseNewCity"><fmt:message key="falseNewCityLabel"/></label>
						<form:radiobutton id="${addressFieldsPropertyName}TrueNewCity" class="fieldValue" path="${addressFieldsPropertyName}.newCity"  value="true" checked ="checked"/>
						<label for="${addressFieldsPropertyName}TrueNewCity" class="fieldValueLabel"><fmt:message key="trueNewCityLabel"/></label>
					</c:when>
					<c:otherwise>
						<label class="fieldLabel" for="${addressFieldsPropertyName}.city"><fmt:message key="addressFieldsCityLabel"/></label>
						<form:radiobutton id="${addressFieldsPropertyName}FalseNewCity" class="fieldValue" path="${addressFieldsPropertyName}.newCity" value="false" checked ="checked"/>
						<label for="${addressFieldsPropertyName}FalseNewCity" class="fieldValueLabel"><fmt:message key="falseNewCityLabel"/></label>
						<form:radiobutton id="${addressFieldsPropertyName}TrueNewCity" class="fieldValue" path="${addressFieldsPropertyName}.newCity"  value="true"/>
						<label for="${addressFieldsPropertyName}TrueNewCity" class="fieldValueLabel"><fmt:message key="trueNewCityLabel"/></label>
					</c:otherwise>
				</c:choose>
				<form:errors cssClass="error" path="${addressFieldsPropertyName}.newCity"/>
			</span>
			<span id="${addressFieldsPropertyName}ExistingCityFieldGroup" class="${existingCityDisplayClass}">
				<select name="${addressFieldsPropertyName}.city" id="${addressFieldsPropertyName}City">
					<jsp:include page="addressFieldsCityOptions.jsp"/>
				</select>
				<form:errors cssClass="error" path="${addressFieldsPropertyName}.city"/>
			</span>
			<span id="${addressFieldsPropertyName}NewCityFieldGroup" class="${newCityDisplayClass}">
				<input type="text" name="${addressFieldsPropertyName}.cityName" id="${addressFieldsPropertyName}CityName" value="${addressFields.cityName}"/>
				<form:errors cssClass="error" path="${addressFieldsPropertyName}.cityName"/>
			</span>
		</span>
		<c:choose>
			<c:when test="${addressFields.newCity or addressFields.newZipCode}">
					<c:set value="" var="newZipCodeDisplayClass"/>
					<c:set value="hidden" var="existingZipCodeDisplayClass"/>
			</c:when>
			<c:otherwise>
				<c:set value="hidden" var="newZipCodeDisplayClass"/>
				<c:set value="" var="existingZipCodeDisplayClass"/>
			</c:otherwise>
		</c:choose>
		<span class="fieldGroup">
			<span id="${addressFieldsPropertyName}NewZipCodeSelectionFieldGroup">
				<c:choose>
					<c:when test="${addressFields.newZipCode}">
						<label class="fieldLabel" for="${addressFieldsPropertyName}.zipCodeValue"><fmt:message key="addressFieldsZipCodeValueLabel"/></label>
						<c:if test="${addressFields.newCity}">
							<form:radiobutton id="${addressFieldsPropertyName}FalseNewZipCode" class="fieldValue" path="${addressFieldsPropertyName}.newZipCode" value="false" disabled="true"/>
						</c:if>
						<c:if test="${not addressFields.newCity}">
							<form:radiobutton id="${addressFieldsPropertyName}FalseNewZipCode" class="fieldValue" path="${addressFieldsPropertyName}.newZipCode" value="false" disabled="false"/>
						</c:if>
						<label for="${addressFieldsPropertyName}FalseNewZipCode" class="fieldValueLabel"><fmt:message key="falseNewZipCodeLabel"/></label>
						<form:radiobutton id="${addressFieldsPropertyName}TrueNewZipCode" class="fieldValue" path="${addressFieldsPropertyName}.newZipCode"  value="true" checked ="checked"/>
						<label for="${addressFieldsPropertyName}TrueNewZipCode" class="fieldValueLabel"><fmt:message key="trueNewZipCodeLabel"/></label>
					</c:when>
					<c:otherwise>
						<label class="fieldLabel" for="${addressFieldsPropertyName}.zipCode"><fmt:message key="addressFieldsZipCodeLabel"/></label>
						<c:if test="${addressFields.newCity}">
							<form:radiobutton id="${addressFieldsPropertyName}FalseNewZipCode" class="fieldValue" path="${addressFieldsPropertyName}.newZipCode" value="false" disabled="true" checked ="checked"/>
						</c:if>
						<c:if test="${not addressFields.newCity}">
							<form:radiobutton id="${addressFieldsPropertyName}FalseNewZipCode" class="fieldValue" path="${addressFieldsPropertyName}.newZipCode" value="false" disabled="false" checked ="checked"/>
						</c:if>
						<label for="${addressFieldsPropertyName}FalseNewZipCode" class="fieldValueLabel"><fmt:message key="falseNewZipCodeLabel"/></label>
						<form:radiobutton id="${addressFieldsPropertyName}TrueNewZipCode" class="fieldValue" path="${addressFieldsPropertyName}.newZipCode"  value="true"/>
						<label for="${addressFieldsPropertyName}TrueNewZipCode" class="fieldValueLabel"><fmt:message key="trueNewZipCodeLabel"/></label>
					</c:otherwise>
				</c:choose>
				<form:errors cssClass="error" path="${addressFieldsPropertyName}.newZipCode"/>
			</span>
			<span id="${addressFieldsPropertyName}ExistingZipCodeFieldGroup" class="${existingZipCodeDisplayClass}">
				<select name="${addressFieldsPropertyName}.zipCode" id="${addressFieldsPropertyName}ZipCode">
					<jsp:include page="addressFieldsZipCodeOptions.jsp"/>
				</select>
				<form:errors cssClass="error" path="${addressFieldsPropertyName}.zipCode"/>
			</span>
			<span id="${addressFieldsPropertyName}NewZipCodeFieldGroup" class="${newZipCodeDisplayClass}">
				<input type="text" name="${addressFieldsPropertyName}.zipCodeValue" id="${addressFieldsPropertyName}ZipCodeValue" value="${addressFields.zipCodeValue}"/>
				<form:errors cssClass="error" path="${addressFieldsPropertyName}.zipCodeValue"/>
				<label class="fieldValueLabel" for="${addressFieldsPropertyName}.zipCodeExtension"><fmt:message key="addressFieldsZipCodeExtensionLabel"/></label>
				<input type="text" name="${addressFieldsPropertyName}.zipCodeExtension" id="${addressFieldsPropertyName}ZipCodeExtension" value="${addressFields.zipCodeExtension}"/>
				<form:errors cssClass="error" path="${addressFieldsPropertyName}.zipCodeExtension"/>
			</span>
		</span>
	</div>
</fmt:bundle>