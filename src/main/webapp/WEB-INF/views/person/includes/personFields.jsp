<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.person.msgs.person">
	<c:if test="${empty personFieldsPropertyName}">
		<c:set var="personFieldsPropertyName" value="personFields" scope="request"/>
	</c:if>
	<span class="fieldGroup">
		<label class="fieldLabel" for="${personFieldsPropertyName}.firstName"><fmt:message key="personFieldsFirstNameLabel"/></label>
		<input type="text" name="${personFieldsPropertyName}.firstName" id="${personFieldsPropertyName}FirstName" value="${personFields.firstName}"/>
		<form:errors cssClass="error" path="${personFieldsPropertyName}.firstName"/>
	</span>
	<span class="fieldGroup">
		<label class="fieldLabel" for="${personFieldsPropertyName}.lastName"><fmt:message key="personFieldsLastNameLabel"/></label>
		<input type="text" name="${personFieldsPropertyName}.lastName" id="${personFieldsPropertyName}LastName" value="${personFields.lastName}"/>
		<form:errors cssClass="error" path="${personFieldsPropertyName}.lastName"/>
	</span>
	<span class="fieldGroup">
		<label class="fieldLabel" for="${personFieldsPropertyName}.middleName"><fmt:message key="personFieldsMiddleNameLabel"/></label>
		<input type="text" name="${personFieldsPropertyName}.middleName" id="${personFieldsPropertyName}MiddleName" value="${personFields.middleName}"/>
		<form:errors cssClass="error" path="${personFieldsPropertyName}.middleName"/>
	</span>
	<span class="fieldGroup">
		<label class="fieldLabel" for="personFields.suffix"><fmt:message key="personFieldsSuffixLabel"/></label>
		<select name="${personFieldsPropertyName}.suffix" id="${personFieldsPropertyName}Suffix">
			<jsp:include page="../../includes/nullOption.jsp"/>
			<c:forEach items="${suffixes}" varStatus="status" var="suffix">
				<c:choose>
					<c:when test="${suffix eq personFields.suffix}">
						<option value="${suffix}" selected="selected"><c:out value="${suffix}"/></option>
					</c:when>
					<c:otherwise>
						<option value="${suffix}"><c:out value="${suffix}"/></option>
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</select>
		<form:errors cssClass="error" path="${personFieldsPropertyName}.suffix"/>
	</span>
	<span class="fieldGroup">
		<label class="fieldLabel" for="${personFieldsPropertyName}.sex"><fmt:message key="personFieldsSexLabel"/></label>
		<select name="${personFieldsPropertyName}.sex" id="${personFieldsPropertyName}Sex">
			<jsp:include page="../../includes/nullOption.jsp"/>
			<c:forEach items="${sexes}" varStatus="status" var="sex">
				<c:choose>
					<c:when test="${sex eq personFields.sex}">
						<option value="${sex}" selected="selected"><fmt:message key="sexOptionLabel.${sex.name}"/></option>
					</c:when>
					<c:otherwise>
						<option value="${sex}"><fmt:message key="sexOptionLabel.${sex.name}"/></option>
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</select>
		<form:errors cssClass="error" path="${personFieldsPropertyName}.sex"/>
	</span>
	<span class="fieldGroup">
		<label class="fieldLabel" for="${personFieldsPropertyName}.birthDate"><fmt:message key="personFieldsBirthDateLabel"/></label>
		<fmt:formatDate value="${personFields.birthDate}" pattern="MM/dd/yyyy" var="birthDate"/>
		<input type="text" class="date" name="${personFieldsPropertyName}.birthDate" id="${personFieldsPropertyName}BirthDate" value="${birthDate}"/>
		<form:errors cssClass="error" path="${personFieldsPropertyName}.birthDate"/>
	</span>
	<span class="fieldGroup">
		<label class="fieldLabel" for="${personFieldsPropertyName}.birthCountry"><fmt:message key="personFieldsBirthCountryLabel"/></label>
		<select name="${personFieldsPropertyName}.birthCountry" id="${personFieldsPropertyName}BirthCountry">
			<jsp:include page="birthCountryOptions.jsp"/>
		</select>
		<form:errors cssClass="error" path="${personFieldsPropertyName}.birthCountry"/>
	</span>
	<span class="fieldGroup">
		<label class="fieldLabel" for="${personFieldsPropertyName}.birthState"><fmt:message key="personFieldsBirthStateLabel"/></label>
		<select name="${personFieldsPropertyName}.birthState" id="${personFieldsPropertyName}BirthState">
			<jsp:include page="birthStateOptions.jsp"/>
		</select>
		<form:errors cssClass="error" path="${personFieldsPropertyName}.birthState"/>
	</span>
	<c:choose>
		<c:when test="${personFields.newCity}">
			<c:set value="" var="newBirthCityDisplayClass"/>
			<c:set value="hidden" var="existingBirthCityDisplayClass"/>
		</c:when>
		<c:otherwise>
			<c:set value="hidden" var="newBirthCityDisplayClass"/>
			<c:set value="" var="existingBirthCityDisplayClass"/>
		</c:otherwise>
	</c:choose>
	<span class="fieldGroup">
		<span id="${personFieldsPropertyName}NewCitySelectionFieldGroup">
			<c:choose>
				<c:when test="${personFields.newCity}">
					<label class="fieldLabel" for="${personFieldsPropertyName}.cityName"><fmt:message key="personFieldsBirthCityLabel"/></label>
					<form:radiobutton id="${personFieldsPropertyName}FalseNewCity" class="fieldValue" path="${personFieldsPropertyName}.newCity" value="false" />
					<label for="${personFieldsPropertyName}FalseNewCity"><fmt:message key="falseNewCityLabel"/></label>
					<form:radiobutton id="${personFieldsPropertyName}TrueNewCity" class="fieldValue" path="${personFieldsPropertyName}.newCity"  value="true" checked ="checked"/>
					<label for="${personFieldsPropertyName}TrueNewCity" class="fieldValueLabel"><fmt:message key="trueNewCityLabel"/></label>
				</c:when>
				<c:otherwise>
					<label class="fieldLabel" for="${personFieldsPropertyName}.birthCity"><fmt:message key="personFieldsBirthCityLabel"/></label>
					<form:radiobutton id="${personFieldsPropertyName}FalseNewCity" class="fieldValue" path="${personFieldsPropertyName}.newCity" value="false" checked ="checked"/>
					<label for="${personFieldsPropertyName}FalseNewCity" class="fieldValueLabel"><fmt:message key="falseNewCityLabel"/></label>
					<form:radiobutton id="${personFieldsPropertyName}TrueNewCity" class="fieldValue" path="${personFieldsPropertyName}.newCity"  value="true"/>
					<label for="${personFieldsPropertyName}TrueNewCity" class="fieldValueLabel"><fmt:message key="trueNewCityLabel"/></label>
				</c:otherwise>
			</c:choose>
			<form:errors cssClass="error" path="${personFieldsPropertyName}.newCity"/>
		</span>
		<span id="${personFieldsPropertyName}ExistingCityFieldGroup" class="${existingBirthCityDisplayClass}">
			<select name="${personFieldsPropertyName}.birthCity" id="${personFieldsPropertyName}BirthCity">
				<jsp:include page="birthCityOptions.jsp"/>
			</select>
			<form:errors cssClass="error" path="${personFieldsPropertyName}.birthCity"/>
		</span>
		<span id="${personFieldsPropertyName}NewCityFieldGroup" class="${newBirthCityDisplayClass}">
			<input type="text" name="${personFieldsPropertyName}.cityName" id="${personFieldsPropertyName}BirthCityName" value="${personFields.cityName}"/>
			<form:errors cssClass="error" path="${personFieldsPropertyName}.cityName"/>
		</span>
	</span>
	<span class="fieldGroup">
		<label class="fieldLabel" for="${personFieldsPropertyName}.socialSecurityNumber"><fmt:message key="personFieldsSocialSecurityNumberLabel"/></label>
		<c:choose>
			<c:when test="${readOnlySsn}">
				<input type="text" name="${personFieldsPropertyName}.socialSecurityNumber" id="${personFieldsPropertyName}SocialSecurityNumber" value="${personFields.socialSecurityNumber}" disabled="true"/>
			</c:when>
			<c:otherwise>
				<input type="text" name="${personFieldsPropertyName}.socialSecurityNumber" id="${personFieldsPropertyName}SocialSecurityNumber" value="${personFields.socialSecurityNumber}"/>
			</c:otherwise>
		</c:choose>
		<form:errors cssClass="error" path="${personFieldsPropertyName}.socialSecurityNumber"/>
	</span>
	<span class="fieldGroup">
		<label class="fieldLabel" for="${personFieldsPropertyName}.stateIdNumber"><fmt:message key="personFieldsStateIdLabel"/></label>
		<input type="text" name="${personFieldsPropertyName}.stateIdNumber" id="${personFieldsPropertyName}StateIdNumber" value="${personFields.stateIdNumber}"/>
		<form:errors cssClass="error" path="${personFieldsPropertyName}.stateIdNumber"/>
	</span>
	<span class="fieldGroup">
		<label class="fieldLabel" for="${personFieldsPropertyName}.deceased"><fmt:message key="personFieldsDeceasedLabel"/></label>
		<c:choose>
			<c:when test="${personFields.deceased eq true}">
				<input type="checkbox" value="true" name="${personFieldsPropertyName}.deceased" id="${personFieldsPropertyName}Deceased" checked="checked"/>
			</c:when>
			<c:otherwise>
				<input type="checkbox" value="true" name="${personFieldsPropertyName}.deceased" id="${personFieldsPropertyName}Deceased"/>
			</c:otherwise>
		</c:choose>	
	</span>
	<span class="fieldGroup">
		<label class="fieldLabel" for="${personFieldsPropertyName}.deathDate"><fmt:message key="personFieldsDeathDateLabel"/></label>
		<fmt:formatDate value="${personFields.deathDate}" pattern="MM/dd/yyyy" var="deathDate"/>
		<c:choose>
			<c:when test="${personFields.deceased eq true}">
				<input type="text" class="date" name="${personFieldsPropertyName}.deathDate" id="${personFieldsPropertyName}DeathDate" value="${deathDate}"/>
			</c:when>
			<c:otherwise>
				<input type="text" class="date" name="${personFieldsPropertyName}.deathDate" id="${personFieldsPropertyName}DeathDate" value="${deathDate}" disabled="disabled"/>
			</c:otherwise>
		</c:choose>
		<form:errors cssClass="error" path="${personFieldsPropertyName}.deathDate"/>
	</span>
</fmt:bundle>