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
				<c:when test="${not empty travelPermitForm.issuerInput}"><c:set var="issueInput" value="${travelPermitForm.issuerInput}"/></c:when>
				<c:when test="${not empty travelPermitForm.issuer}"><c:set var="issuer"><c:set var="userAccount" value="${travelPermitForm.issuer}" scope="request"/><jsp:include page="/WEB-INF/views/user/includes/userAccount.jsp"/></c:set></c:when>
			</c:choose>
			<input name="issuerInput" id="issuerInput" value="<c:out value='${issuerInput}'/>"/>
			<form:hidden path="issuer"/>
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
			<form:radiobuttons path="destinationOption" items="${destinationOptions}" />
			<form:errors path="destinationOption" cssClass="error"/>
		</span>
		<br>
		<div id="fullAddressContainer" <c:if test="${travelPermitForm.destinationOption ne 'Full Address'}">hidden=true</c:if>>
			<span>
				<form:label path="addressOption" class="fieldLabel">
					<fmt:message key="addressOptionsLabel"/>
				</form:label>
				<form:radiobuttons path="addressOption" items="${addressOptions}" />
				<form:errors path="addressOption" cssClass="error"/>
			</span>
			<div id="existingAddressContainer" <c:if test="${addressOption eq null}">hidden=true</c:if>>
				<fieldset>
					<legend><fmt:message key="useExistingAddressLabel"/></legend>
					<form:input path="addressQuery" id="travelPermitAddressQuery" class="large"/>
					<form:errors path="addressQuery" cssClass="error"/>
					<form:hidden path="address" id="searchAddress"/>
				</fieldset>
			</div>
			<div id="newAddressContainer" <c:if test="${addressOption eq null}">hidden=true</c:if>>
				<fieldset>
					<legend><fmt:message key="createNewAddressLabel"/></legend>
				<c:set var="addressFields" value="${travelPermitForm.addressFields}" scope="request"/>
				<jsp:include page="/WEB-INF/views/address/includes/addressFields.jsp"/>
				</fieldset>
			</div>
		</div>
		<div id="partialAddressContainer" <c:if test="${travelPermitForm.destinationOption ne 'Partial Address'}">hidden=true</c:if>>
			<fieldset>
				<legend><fmt:message key="partialAddressLabel"/></legend>
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
			<span class="oneline fieldGroup">
				<form:label class="fieldLabel" path="partialAddressCity">
					<fmt:message key="cityLabel"/>
				</form:label>
				<form:select path = "partialAddressCity" id="partialAddressCityID">
					<jsp:include page="/WEB-INF/views/travelPermit/includes/partialAddressCityOptions.jsp"/>
				</form:select>
				<form:errors path="partialAddressCity" cssClass="error"/>
				<form:label path="newCity" class="fieldLabel"><fmt:message key="newCityLabel"/></form:label>
				<form:checkbox id = "newCityName" path="newCity"/>
			</span>
			<div id="newCityContainer" <c:if test="${not travelPermitForm.newCity}">hidden=true</c:if>>
				<span class="fieldGroup">
					<form:label class="fieldLabel" path="newCityName">
						<fmt:message key="newCityLabel"/>
					</form:label>
					<form:input path="newCityName"/> 
					<form:errors path="newCityName" cssClass="error"/>
				</span>
			</div>
			<span class="fieldGroup">
				<form:label class="fieldLabel" path="partialAddressZipCode">
					<fmt:message key="zipCodeLabel"/>
				</form:label>
				<form:select path = "partialAddressZipCode" id="partialAddressZipCodeID">
				<jsp:include page="/WEB-INF/views/travelPermit/includes/partialAddressZipCodeOptions.jsp"/>
				</form:select>
				<form:errors path="partialAddressZipCode" cssClass="error"/>
				<form:label path="newZipCode" class="fieldLabel"><fmt:message key="newZipCodeLabel"/></form:label>
					<form:checkbox id = "newZipCodeName" path="newZipCode"/>
			</span>
			<div id="newZipCodeContainer" <c:if test="${not travelPermitForm.newZipCode}">hidden=true</c:if>>
				<span class="fieldGroup">
					<form:label class="fieldLabel" path="newZipCodeName">
						<fmt:message key="newZipCodeLabel"/>
					</form:label>
					<form:input path="newZipCodeName"/> 
					<form:errors path="newZipCodeName" cssClass="error"/>
				</span>
			</div>
			</fieldset>
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
		<form:label path="transportMethod" class="fieldLabel">
			<fmt:message key="transportMethodsLabel"/>
		</form:label>
		<form:radiobuttons path="transportMethod" items="${transportMethods}" itemLabel="name" itemValue="id"/>
		<a class="link"  id="transportMethodLink" href="${pageContext.request.contextPath}/travelPermit/transportMethod.html?"></a>
		<form:errors path="transportMethod" cssClass="error"/>
		<%-- <div id="PrivateVehicle" <c:if test="${travelPermitForm.transportMethod ne 'PrivateVehicle'}">hidden=true</c:if>>
			<fieldset>
			<legend><fmt:message key="privateVehicleLabel"/></legend>
			<span class="fieldGroup">
				<form:label path="vehicleInfo" class="fieldLabel">
					<fmt:message key="vehicleInfoLabel"/>
				</form:label>
				<form:input path="vehicleInfo"/> 
				<form:errors path="vehicleInfo" cssClass="error"/>
				<form:label path="plateNumber" class="fieldLabel">
					<fmt:message key="plateNumberLabel"/>
				</form:label>
				<form:input path="plateNumber"/> 
				<form:errors path="plateNumber" cssClass="error"/>
			</span>
			</fieldset>
		</div>
		<div id="Airplane" <c:if test="${travelPermitForm.transportMethod ne 'Airplane'}">hidden=true</c:if>>
			<fieldset>
			<legend><fmt:message key="airplaneLabel"/></legend>
			<span class="fieldGroup">
				<form:label path="flightNumber" class="fieldLabel">
					<fmt:message key="flightNumberLabel"/>
				</form:label>
				<form:input path="flightNumber"/> 
				<form:errors path="flightNumber" cssClass="error"/>
			</span>
			</fieldset>
		</div>
		<div id="Bus" <c:if test="${travelPermitForm.transportMethod ne 'Bus'}">hidden=true</c:if>>
			<fieldset>
			<legend><fmt:message key="busLabel"/></legend>
			<span class="fieldGroup">
				<form:label path="busNumber" class="fieldLabel">
					<fmt:message key="busNumberLabel"/>
				</form:label>
				<form:input path="busNumber"/> 
				<form:errors path="busNumber" cssClass="error"/>
			</span>
			</fieldset>
		</div>
		<div id="Train" <c:if test="${travelPermitForm.transportMethod ne 'Train'}">hidden=true</c:if>>
			<fieldset>
			<legend><fmt:message key="trainLabel"/></legend>
			<span class="fieldGroup">
				<form:label path="trainNumber" class="fieldLabel">
					<fmt:message key="trainNumberLabel"/>
				</form:label>
				<form:input path="trainNumber"/> 
				<form:errors path="trainNumber" cssClass="error"/>
			</span>
			</fieldset>
		</div> --%>
		<div id="travelMethod">
		</div>
	</fieldset>
	<fieldset>
		<legend><fmt:message key="accompaniedByLabel"/></legend>
		<span class="fieldGroup">
			<form:label path="person" class="fieldLabel">
				<fmt:message key="personLabel"/>
			</form:label>
			<form:textarea path="person" row="4"/> 
			<form:errors path="person" cssClass="error"/>
		</span>
		<span class="fieldGroup">
			<form:label path="relation" class="fieldLabel">
				<fmt:message key="relationLabel"/>
			</form:label>
			<form:textarea path="relation" row="4"/> 
			<form:errors path="relation" cssClass="error"/>
		</span>
	</fieldset>
	<fieldset >
		<legend><fmt:message key="notesLabel"/></legend>
		<form:errors path="travelPermitNoteItems" cssClass="error"/>
		<c:set var="travelPermitNoteItems" value="${travelPermitNoteItems}" scope="request"/>
		<jsp:include page="noteTable.jsp"/>
	</fieldset>			
	<p class="buttons">
		<input type="submit" value="<fmt:message key='saveLabel' bundle="${commonBundle}"/>"/>
	</p>
</form:form>
</fmt:bundle>