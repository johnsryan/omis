<%--
 - OMIS - Offender Management Information System
 - Copyright (C) 2011 - 2017 State of Montana
 -
 - This program is free software: you can redistribute it and/or modify
 - it under the terms of the GNU General Public License as published by
 - the Free Software Foundation, either version 3 of the License, or
 - (at your option) any later version.
 -
 - This program is distributed in the hope that it will be useful,
 - but WITHOUT ANY WARRANTY; without even the implied warranty of
 - MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 - GNU General Public License for more details.
 -
 - You should have received a copy of the GNU General Public License
 - along with this program.  If not, see <http://www.gnu.org/licenses/>.
 --%>

<%--
  - Form to edit offender relationships.
  -
  - Author: Yidong Li
  - Author: Joel Norris
  - Author: Stephen Abson
  - Author: Sheronda Vaughn
  --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.offenderrelationship.msgs.editOffenderRelationship">
<form:form commandName="editRelationshipsForm" class="editForm">
	<c:set var="addressFields" value="${editRelationshipsForm.addressFields}" scope="request"/>
	<c:set var="addressFieldsPropertyName" value="addressFields" scope="request"/>
	<c:set var="poBoxFields" value="${editRelationshipsForm.poBoxFields}" scope="request"/>
	<c:set var="poBoxFieldsPropertyName" value="poBoxFields" scope="request"/>
	<c:set var="personFields" value="${editRelationshipsForm.personFields}" scope="request"/>
	<c:set var="personFieldsPropertyName" value="personFields" scope="request"/>
	<fieldset id="personFields">
		<legend><fmt:message key="personDetailsLegendLabel"/></legend>
		<sec:authorize access="hasRole('ADMIN') or hasRole('OFFENDER_RELATIONSHIP_SSN_EDIT')" var="canEditSsn"/>
		<c:set var="readOnlySsn" value="${not canEditSsn}" scope="request"/>
		<form:hidden path="validateSocialSecurityNumber"/>
		<jsp:include page="/WEB-INF/views/person/includes/personFields.jsp"/>
	</fieldset>
	<fieldset id="addressFields">
		<legend><fmt:message key="addressDetailsLegendLabel"/></legend>	
		<label class="fieldLabel" for="enterAddress"><fmt:message key="enterAddressLabel"/></label>
		<form:checkbox path="enterAddress" id="enterAddress"/>
		<div id="addressContainer" <c:if test="${addressFields eq null}">hidden="true"</c:if>>				
			<div>
				<c:if test="${not empty existingAddress}">
				<form:radiobutton path="addressOperation" id="currentAddress" value="CURRENT"/><label class="fieldValueLabel" for="currentAddress"><fmt:message key="currentAddressLabel"/></label>
				<c:out value=" ${existingAddress.value}"></c:out><c:out value=" ${existingAddress.zipCode.city.name},"></c:out><c:if test="${not empty existingAddress.zipCode.city.state.abbreviation}"><c:out value=" ${existingAddress.zipCode.city.state.abbreviation}"></c:out></c:if><c:out value=" ${existingAddress.zipCode.value}"></c:out><c:if test="${not empty existingAddress.zipCode.extension}"><c:out value="-${existingAddress.zipCode.extension}"></c:out></c:if><c:out value=","></c:out><c:out value=" ${existingAddress.zipCode.city.country.name}"></c:out>
				</c:if>
			</div>
			<div>
				<form:radiobutton path="addressOperation" id="existingAddress" value="EXISTING"/><label class="fieldValueLabel" for="existingAddress"><fmt:message key="existingAddressLabel"/></label>
				<form:input path="addressQuery" id="offenderRelationshipSearchAddressQuery" disabled="${editRelationshipsForm.addressOperation ne 'EXISTING'}" class="medium"></form:input>
				<form:errors path="addressQuery" cssClass="error"/>
				<form:hidden path="address" id="searchAddress"/>
				<form:errors path="address" cssClass="error"/>
			</div>
			<div>
				<form:radiobutton path="addressOperation" id="newAddress" value="NEW" /><label class="fieldValueLabel" for="newAddress"><fmt:message key="newAddressLabel"/></label>
			</div>
			<div id="addressFieldsContainer" <c:if test="${editRelationshipsForm.addressOperation eq 'CURRENT' or editRelationshipsForm.addressOperation eq 'EXISTING'}">hidden="true"</c:if>>
				<c:set value="${editRelationshipsForm.addressFields}" var="addressFields" scope="request"/>
				<jsp:include page="../../address/includes/addressFields.jsp"/>
			</div>
		</div>
	</fieldset>
	<fieldset id="poBoxFields">
		<legend><fmt:message key="poBoxDetailsLegendLabel"/></legend>	
		<label class="fieldLabel" for="enterPoBox"><fmt:message key="enterPoBoxLabel"/></label>
		<c:choose>
			<c:when test="${editRelationshipsForm.enterPoBox}">
				<input type="checkbox" value="true" name="enterPoBox" id="enterPoBox" checked="checked"/>
			</c:when>
			<c:otherwise>
				<input type="checkbox" value="true" name="enterPoBox" id="enterPoBox"/>
			</c:otherwise>
		</c:choose>
		<div id="poBoxFieldsContainer" <c:if test="${poBoxFields eq null}">hidden="true"</c:if>>
			<jsp:include page="/WEB-INF/views/contact/includes/poBoxFields.jsp"/>
		</div>
	</fieldset>
	<fieldset id="telephoneNumberFields">
		<legend><fmt:message key="telephoneNumberDetailsLegendLabel"/></legend>
		<form:errors cssClass="error" path="telephoneNumberItems"/>
		<c:set var="telephoneNumberItems" value="${editRelationshipsForm.telephoneNumberItems}" scope="request"/>
			<jsp:include page="../../offenderRelationship/includes/update/editTelephoneNumberTableBody.jsp"/>
	</fieldset>
	<fieldset id="onlineAccountFields">
		<legend><fmt:message key="onlineAccountDetailsLegendLabel"/></legend>
		<form:errors cssClass="error" path="onlineAccountContactItems"/>
		<c:set var="onlineAccountContactItems" value="${editRelationshipsForm.onlineAccountContactItems}" scope="request"/>
			<jsp:include page="../../offenderRelationship/includes/update/editOnlineAccountTableBody.jsp"/>
	</fieldset>
	<fieldset id="noteFields">
		<legend><fmt:message key="noteDetailsLegendLabel"/></legend>
			<form:errors cssClass="error" path="noteItems"/>
			<c:set var="offenderRelationshipNoteItems" value="${editRelationshipsForm.noteItems}" scope="request"/>
			<c:set var="offenderRelationshipNoteItemsFieldName" value="noteItems" scope="request"/>
			<c:set var="baseUrl" value="${pageContext.request.contextPath}/offenderRelationship/update" scope="request"/>
		<jsp:include page="offenderRelationshipNoteItemsTable.jsp"/>
	</fieldset>
	<p class="buttons">
		<input type="submit" value="<fmt:message key='saveFamilyLabel' />"/>
	</p>
</form:form>
</fmt:bundle>