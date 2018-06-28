<%--
 - Author: Yidong Li
 - Version: 0.1.1 (May 21, 2018)
 - Since: OMIS 3.0
 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.travelpermit.msgs.travelPermit">
<form:form commandName="travelPermitForm" class="editForm">
	<c:set var="addressFields" value="${travelPermitForm.addressFields}" scope="request"/>
	<c:set var="addressFieldsPropertyName" value="addressFields" scope="request"/>
	<fieldset>
		<legend><fmt:message key="insuranceDetailsHeaderLabel"/></legend>
		<span class="fieldGroup">
			<form:label class="fieldLabel" path="periodicity">
				<fmt:message key="periodicityLabel"/>
			</form:label>
			<form:select path = "periodicity">
				<form:option value=""><fmt:message key="nullLabel" bundle="${commonBundle}"/></form:option>
				<form:options items="${periodicities}" itemValue="id" itemLabel="name"/>
			</form:select>
			<form:errors path="periodicity" cssClass="error"/>
		</span>
		<span class="fieldGroup">
			<form:label path="issueDate" class="fieldLabel">
				<fmt:message key="issueDateLabel"/>
			</form:label>
			<form:input path="issueDate" class="date"/>
			<form:errors path="issueDate" cssClass="error"/>
		</span>
		<span class="fieldGroup">
			<form:label class="fieldLabel" path="issuer">
				<fmt:message key="issuerLabel"/>
			</form:label>
			<c:choose>
				<c:when test="${not empty travelPermitForm.issuerInput}"><c:set var="issuerInput" value="${travelPermitForm.issuerInput}"/></c:when>
				<c:when test="${not empty travelPermitForm.issuer}"><c:set var="issuerInput"><c:set var="userAccount" value="${travelPermitForm.issuer}" scope="request"/><jsp:include page="/WEB-INF/views/user/includes/userAccount.jsp"/></c:set></c:when>
			</c:choose>
			<input type="text" name="issuerInput" id="issuerInput" value="<c:out value='${issuerInput}'/>"/>
			<input type="hidden" name="issuer" id="issuer" value="<c:out value='${travelPermitForm.issuer.id}'/>"/>
			<a id="useCurrentUserAccountForIssuerLink" class="currentUserAccountLink"></a>
			<a id="issuerClear" class="clearLink"></a>
			<form:errors path="issuer" cssClass="error"/>
		</span>
		<span class="fieldGroup">
			<form:label path="startDate" class="fieldLabel">
				<fmt:message key="startDateLabel" bundle="${commonBundle}"/>
			</form:label>
			<form:input path="startDate" id="startDate" class="date"/>
			<form:errors path="startDate" cssClass="error"/>
		</span>
		<span class="fieldGroup">
			<form:label path="endDate" class="fieldLabel">
				<fmt:message key="endDateLabel" bundle="${commonBundle}"/>
			</form:label>
			<form:input path="endDate" id="endDate" class="date"/>
			<form:errors path="endDate" cssClass="error"/>
		</span>
	</fieldset>	
	<fieldset>
		<legend><fmt:message key="destinationDetailsLabel"/></legend>
		<span class="fieldGroup">
			<form:label path="name" class="fieldLabel">
				<fmt:message key="nameLabel"/>
			</form:label>
			<form:textarea path="name" row="4"/> 
			<form:errors path="name" cssClass="error"/>
		</span>
		<span class="fieldGroup">
			<form:label path="phoneNumber" class="fieldLabel">
				<fmt:message key="phoneNumberLabel"/>
			</form:label>
			<form:input path="phoneNumber" />
			<form:errors path="phoneNumber" cssClass="error"/>
		</span>
		<span>
			<form:label path="destinationOption" class="fieldLabel">
				<fmt:message key="destinationOptionsLabel"/>
			</form:label>
			<c:if test="${createTravelPermit}">
			<c:forEach items="${destinationOptions}" var="option" varStatus="status">
				<c:choose>
					<c:when test="${option eq 'USE_PARTIAL_ADDRESS'}">
						<label><form:radiobutton path="destinationOption" id="partialAddress" value="${option.name}"/><fmt:message key="partialAddressLabel"/></label>
					</c:when>
					<c:when test="${option eq 'USE_FULL_ADDRESS'}">
						<label><form:radiobutton path="destinationOption" id="fullAddress" value="${option.name}"/><fmt:message key="fullAddressLabel"/></label>
					</c:when>
				</c:choose>
			</c:forEach>
			</c:if>
			<c:if test="${!createTravelPermit}">
				<c:choose>
					<c:when test="${travelPermitForm.destinationOption.name eq 'USE_FULL_ADDRESS'}">
						<label><form:radiobutton path="destinationOption" checked = "checked" id="fullAddress" value="USE_FULL_ADDRESS"/><fmt:message key="fullAddressLabel"/></label>
						<label><form:radiobutton path="destinationOption" id="partialAddress" value="USE_PARTIAL_ADDRESS"/><fmt:message key="partialAddressLabel"/></label>
					</c:when>
					<c:when test="${travelPermitForm.destinationOption.name eq 'USE_PARTIAL_ADDRESS'}">
						<label><form:radiobutton path="destinationOption" id="fullAddress" value="USE_FULL_ADDRESS"/><fmt:message key="fullAddressLabel"/></label>
						<label><form:radiobutton path="destinationOption" checked = "checked" id="partialAddress" value="USE_PARTIAL_ADDRESS"/><fmt:message key="partialAddressLabel"/></label>
					</c:when>
				</c:choose>
			</c:if>
			<form:errors path="destinationOption" cssClass="error"/>
		</span>
		<br>
		<div id="fullAddressContainer" <c:if test="${travelPermitForm.destinationOption.name ne 'USE_FULL_ADDRESS'}">hidden=true</c:if>>
			<span>
				<form:label path="addressOption" class="fieldLabel">
					<fmt:message key="addressOptionsLabel"/>
				</form:label>
				<c:forEach items="${addressOption}" var="option" varStatus="status">
					<c:choose>
						<c:when test="${option eq 'USE_EXISTING'}">
							<label><form:radiobutton path="addressOption" id="useExistingAddress" value="${option.name}"/><fmt:message key="useExistingLabel"/></label>
						</c:when>
						<c:when test="${option eq 'CREATE_NEW'}">
							<label><form:radiobutton path="addressOption" id="createNewAddress" value="${option.name}"/><fmt:message key="createNewLabel"/></label>
						</c:when>
					</c:choose>
				</c:forEach>
				<form:errors path="addressOption" cssClass="error"/>
			</span>
			<div id="existingAddressContainer" <c:if test="${travelPermitForm.addressOption.name ne 'USE_EXISTING'}">hidden=true</c:if>>
				<form:input path="addressQuery" id="travelPermitAddressQuery" class="large"/>
				<form:errors path="addressQuery" cssClass="error"/>
				<form:hidden path="address" id="searchAddress"/>
			</div>
			<div id="newAddressContainer" <c:if test="${travelPermitForm.addressOption.name ne 'CREATE_NEW'}">hidden=true</c:if>>
				<c:set var="addressFields" value="${travelPermitForm.addressFields}" scope="request"/>
				<jsp:include page="/WEB-INF/views/address/includes/addressFields.jsp"/>
			</div>
		</div>
		<div id="partialAddressContainer" <c:if test="${travelPermitForm.destinationOption.name ne 'USE_PARTIAL_ADDRESS'}">hidden=true</c:if>>
			<span class="fieldGroup">
				<form:label class="fieldLabel" path="partialAddressState">
					<fmt:message key="stateLabel"/>
				</form:label>
				<form:select id="partialAddressStateID"    path = "partialAddressState">
					<form:option value=""><fmt:message key="nullLabel" bundle="${commonBundle}"/></form:option>
					<form:options items="${partialAddressStates}" itemValue="id" itemLabel="name"/>
				</form:select>
				<form:errors path="partialAddressState" cssClass="error"/>
			</span>
			<c:choose>
				<c:when test="${travelPermitForm.newCity}">
					<c:set value="" var="newCityDisplayClass"/>
					<c:set value="hidden" var="existingCityDisplayClass"/>
				</c:when>
				<c:otherwise>
					<c:set value="hidden" var="newCityDisplayClass"/>
					<c:set value="" var="existingCityDisplayClass"/>
				</c:otherwise>
			</c:choose>
			<span class="fieldGroup">
				<span id="newCitySelectionFieldGroup">
					<c:choose>
						<c:when test="${travelPermitForm.newCity}">
							<label class="fieldLabel" for="${travelkPermitForm}.newCityName"><fmt:message key="newCityLabel"/></label>
							<form:radiobutton id="newCityFalse" class="fieldValue" path="newCity" value="false" />
							<label for="newCityFalse" class="fieldValueLabel"><fmt:message key="existingCityLabel"/></label>
							<form:radiobutton id="newCityTrue" class="fieldValue" path="newCity"  value="true" checked ="checked"/>
							<label for="newCityTrue" class="fieldValueLabel"><fmt:message key="newCityLabel"/></label>
						</c:when>
						<c:otherwise>
							<label class="fieldLabel" for="${travelkPermitForm}.partialAddressCity"><fmt:message key="newCityLabel"/></label>
							<form:radiobutton id="newCityFalse" class="fieldValue" path="newCity" value="false" checked ="checked" />
							<label for="newCityFalse" class="fieldValueLabel"><fmt:message key="existingCityLabel"/></label>
							<form:radiobutton id="newCityTrue" class="fieldValue" path="newCity"  value="true"/>
							<label for="newCityTrue" class="fieldValueLabel"><fmt:message key="newCityLabel"/></label>
						</c:otherwise>
					</c:choose>
					<form:errors cssClass="error" path="newCity"/>
				</span>
				<span id="existingCityFieldGroup" class="${existingCityDisplayClass}">
					<form:select path = "partialAddressCity" id="partialAddressCityID">
						<jsp:include page="/WEB-INF/views/travelPermit/includes/partialAddressCityOptions.jsp"/>
					</form:select>
					<form:errors path="partialAddressCity" cssClass="error"/>
				</span>
				<span id="newCityFieldGroup" class="${newCityDisplayClass}">
					<form:input type="text" path="newCityName" id="newCityName" value="${travelPermitForm.newCityName}"/>
					<form:errors cssClass="error" path="newCityName"/>
				</span>
			</span>
			<c:choose>
				<c:when test="${travelPermitForm.newCity or travelPermitForm.newZipCode}">
						<c:set value="" var="newZipCodeDisplayClass"/>
						<c:set value="hidden" var="existingZipCodeDisplayClass"/>
				</c:when>
				<c:otherwise>
					<c:set value="hidden" var="newZipCodeDisplayClass"/>
					<c:set value="" var="existingZipCodeDisplayClass"/>
				</c:otherwise>
			</c:choose>
			<span class="fieldGroup">
				<span id="newZipCodeSelectionFieldGroup">
					<c:choose>
						<c:when test="${travelPermitForm.newZipCode}">
							<form:label class="fieldLabel" path="newZipCodeName"><fmt:message key="zipCodeLabel"/></form:label>
							<c:if test="${travelPermitForm.newCity}">
								<form:radiobutton id="newZipCodeFalse" class="fieldValue" path="newZipCode" value="false" disabled="true"/>
							</c:if>
							<c:if test="${not travelPermitForm.newZipCode}">
								<form:radiobutton id="newZipCodeFalse" class="fieldValue" path="newZipCode" value="false" disabled="false"/>
							</c:if>
							<form:label path="newZipCodeName" class="fieldValueLabel"><fmt:message key="existingZipCodeLabel"/></form:label>
							<form:radiobutton id="newZipCodeTrue" class="fieldValue" path="newZipCode"  value="true" checked ="checked"/>
							<form:label path="newZipCodeName" class="fieldValueLabel"><fmt:message key="newZipCodeLabel"/></form:label>
						</c:when>
						<c:otherwise>
							<form:label class="fieldLabel" path="partialAddressZipCode"><fmt:message key="zipCodeLabel"/></form:label>
							<c:if test="${travelPermitForm.newCity}">
								<form:radiobutton id="newZipCodeFalse" class="fieldValue" path="newZipCode" value="false" disabled="true" checked ="checked"/>
							</c:if>
							<c:if test="${not travelPermitForm.newCity}">
								<form:radiobutton id="newZipCodeFalse" class="fieldValue" path="newZipCode" value="false" disabled="false" checked ="checked"/>
							</c:if>
							<form:label path="partialAddressZipCode" class="fieldValueLabel"><fmt:message key="existingZipCodeLabel"/></form:label>
							<form:radiobutton id="newZipCodeTrue" class="fieldValue" path="newZipCode"  value="true"/>
							<form:label path="newZipCodeName" class="fieldValueLabel"><fmt:message key="newZipCodeLabel"/></form:label>
						</c:otherwise>
					</c:choose>
					<form:errors cssClass="error" path="newZipCode"/>
				</span>
				<span id="existingZipCodeFieldGroup" class="${existingZipCodeDisplayClass}">
					<form:select path = "partialAddressZipCode" id="partialAddressZipCodeID">
					<jsp:include page="/WEB-INF/views/travelPermit/includes/partialAddressZipCodeOptions.jsp"/>
					</form:select>
					<form:errors cssClass="error" path="partialAddressZipCode"/>
				</span>
				<span id="newZipCodeFieldGroup" class="${newZipCodeDisplayClass}">
					<form:input path="newZipCodeName" type="text" name="newZipCodeName" id="newZipCodeName" value="${travelPermitForm.newZipCodeName}"/>
					<form:errors cssClass="error" path="newZipCodeName"/>
					<form:label class="fieldValueLabel" path="newZipCodeExtension"><fmt:message key="newZipCodeExtensionLabel"/></form:label>
					<form:input type="text" path="newZipCodeExtension" id="newZipCodeExtension" value="${travelPermitForm.newZipCodeExtension}"/>
					<form:errors cssClass="error" path="newZipCodeExtension"/>
				</span>
			</span>
		</div>
	</fieldset>				
	<fieldset>
		<legend><fmt:message key="transportationDetailsLabel"/></legend>
		<span class="fieldGroup">
			<form:label path="tripPurpose" class="fieldLabel">
				<fmt:message key="tripPurposeLabel"/>
			</form:label>
			<form:textarea path="tripPurpose" row="4"/> 
			<form:errors path="tripPurpose" cssClass="error"/>
		</span>
		<form:label path="travelMethod" class="fieldLabel">
			<fmt:message key="transportMethodsLabel"/>
		</form:label>
		<form:radiobuttons path="travelMethod" items="${transportMethods}" itemLabel="name" itemValue="id"/>
		<a class="link"  id="transportMethodLink" href="${pageContext.request.contextPath}/travelPermit/transportMethod.html?"></a>
		<form:errors path="travelMethod" cssClass="error"/>
		<div id="travelMethods">
			<c:if test="${not empty travelPermitForm.travelMethod}">
			<c:set var="travelMethod" value="${travelPermitForm.travelMethod}" scope="request"/>
			<jsp:include page="travelMethod.jsp"/>
			</c:if>
		</div>
	</fieldset>
	<fieldset>
		<legend><fmt:message key="accompaniedByLabel"/></legend>
		<span class="fieldGroup">
			<form:label path="persons" class="fieldLabel">
				<fmt:message key="personLabel"/>
			</form:label>
			<form:textarea path="persons" row="4"/> 
			<form:errors path="persons" cssClass="error"/>
		</span>
		<br>
		<span class="fieldGroup">
			<form:label path="relationships" class="fieldLabel">
				<fmt:message key="relationLabel"/>
			</form:label>
			<form:textarea path="relationships" row="4"/> 
			<form:errors path="relationships" cssClass="error"/>
		</span>
	</fieldset>
	<fieldset >
		<legend><fmt:message key="notesLabel"/></legend>
		<jsp:include page="noteTable.jsp"/>
	</fieldset>			
	<p class="buttons">
		<input type="submit" value="<fmt:message key='saveLabel' bundle="${commonBundle}"/>"/>
	</p>
</form:form>
</fmt:bundle>