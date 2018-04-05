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
 - Author: Yidong Li
 - Version: 0.1.0 (June 19, 2015)
 - Since: OMIS 3.0
 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:setBundle basename="omis.audit.msgs.audit" var="auditBundle"/>
<fmt:bundle basename="omis.family.msgs.family">
<form:form commandName="familyAssociationForm" class="editForm">
	<input type="hidden" id="categoryId" value="${familyAssociation.category.id}"/>
	<c:set var="addressFields" value="${familyAssociationForm.addressFields}" scope="request"/>
	<c:set var="addressFieldsPropertyName" value="addressFields" scope="request"/>
	<c:set var="poBoxFields" value="${familyAssociationForm.poBoxFields}" scope="request"/>
	<c:set var="poBoxFieldsPropertyName" value="poBoxFields" scope="request"/>
	<c:if test="${not empty familyMember}">
		<fieldset><c:set value="${existingContactSummary}" var="contactSummary" scope="request"/>
		<jsp:include page="/WEB-INF/views/contact/includes/contactSummary.jsp"/></fieldset>
	</c:if>
	<c:choose>
		<c:when test = "${familyAssociationForm.category.classification.name eq 'SPOUSE'}">
			<c:set var="marriageFieldsVisibilityClass" value="show"/>
		</c:when>
		<c:otherwise>
			<c:set var="marriageFieldsVisibilityClass" value="hidden"/>
		</c:otherwise> 
	</c:choose>
	<c:if test="${empty familyMember}">
		<fieldset <c:if test="${not createFlag}">disabled</c:if>>
			<legend><fmt:message key="personFieldsLabel"/></legend>
			<sec:authorize access="hasRole('ADMIN') or hasRole('FAMILY_ASSOCIATION_SSN_EDIT')" var="canEditSsn"/>
			<c:set var="readOnlySsn" value="${not canEditSsn}" scope="request"/>
			<form:hidden path="validateSocialSecurityNumber"/>
			<c:set var="personFields" value="${familyAssociationForm.personFields}" scope="request"/>
			<c:set var="createFlag" value="${createFlag}" scope="request"/>
			<jsp:include page="../../person/includes/personFields.jsp"/>
		</fieldset>
		<fieldset <c:if test="${not createFlag}">disabled</c:if>>
			<legend><fmt:message key="familyAddressLabel"/></legend>
			<label class="fieldLabel" for="enterAddress"><fmt:message key="enterAddressLabel"/></label>
			<c:choose>
				<c:when test="${familyAssociationForm.enterAddress}">
					<input type="checkbox" value="true" name="enterAddress" id="familyCreateScreenEnterAddress" checked="checked"/>
				</c:when>
				<c:otherwise>
					<input type="checkbox" value="true" name="enterAddress" id="familyCreateScreenEnterAddress" />
				</c:otherwise>
			</c:choose>
			<div id="familyCreateScreenAddressContainer" <c:if test="${not familyAssociationForm.enterAddress}">hidden=true</c:if>>
					<c:if test="${familyAssociationForm.addressOperation eq 'EXISTING'}">
						<form:radiobutton path="addressOperation" id="newAddressNo" value="EXISTING" checked ="checked"/><label class="fieldValueLabel" for="newAddressNo"><fmt:message key="newAddressNoLabel"/></label>
						<form:input path="addressQuery" id="familySearchAddressQuery" class="large"/>
						<form:errors path="addressQuery" cssClass="error"/>
						<form:hidden path="address" id="searchAddress"/>
						<br>
						<form:radiobutton path="addressOperation" id="newAddressYes" value="NEW" /><label class="fieldValueLabel" for="newAddressYes"><fmt:message key="newAddressYesLabel"/></label>
					</c:if>
					<c:if test="${familyAssociationForm.addressOperation eq 'NEW'}">
						<form:radiobutton path="addressOperation" id="newAddressNo" value="EXISTING"/><label class="fieldValueLabel" for="newAddressNo"><fmt:message key="newAddressNoLabel"/></label>
						<form:input path="addressQuery" id="familySearchAddressQuery" class="large"/>
						<form:errors path="addressQuery" cssClass="error"/>
						<form:hidden path="address" id="searchAddress"/>
						<br>
						<form:radiobutton path="addressOperation" id="newAddressYes" value="NEW" checked ="checked"/><label class="fieldValueLabel" for="newAddressYes"><fmt:message key="newAddressYesLabel"/></label>
					</c:if>
					<c:if test="${familyAssociationForm.addressOperation eq 'CURRENT'}">
						<form:radiobutton path="addressOperation" id="newAddressNo" value="EXISTING"/><label class="fieldValueLabel" for="newAddressNo"><fmt:message key="newAddressNoLabel"/></label>
						<form:input path="addressQuery" id="familySearchAddressQuery" class="large"/>
						<form:errors path="addressQuery" cssClass="error"/>
						<form:hidden path="address" id="searchAddress"/>
						<br>
						<form:radiobutton path="addressOperation" id="newAddressYes" value="NEW" /><label class="fieldValueLabel" for="newAddressYes"><fmt:message key="newAddressYesLabel"/></label>
					</c:if>
					<c:set var="addressFields" value="${familyAssociationForm.addressFields}" scope="request"/>
					<div id="addressContainer">
						<jsp:include page="../../address/includes/addressFields.jsp"/>
						<span class="fieldGroup">
							<form:label path="homeType" class="fieldLabel">
								<fmt:message key="homeTypeLabel" />
							</form:label>					
							<form:select path="homeType" >
								<form:option value=""><fmt:message key="nullLabel" bundle="${commonBundle}"/></form:option>
								<c:forEach var="homeType" items="${homeTypes}">
									<form:option value="${homeType.name}"><fmt:message key="homeType${homeType.name}Label" /></form:option>
								</c:forEach>
							</form:select>
							<form:errors path="homeType" cssClass="error"/>
						</span>
						<span class="fieldGroup">
							<form:label path="secondAddressDesignator" class="fieldLabel">
							 	<fmt:message key="secondAddressDesignatorLabel" />
							</form:label>
							<form:input path="secondAddressDesignator" type = "text" />
							<form:errors path="secondAddressDesignator" cssClass="error"/>
						</span>
					</div>
			</div>
		</fieldset>
		<fieldset <c:if test="${not createFlag}">disabled</c:if>>
			<legend><fmt:message key="postOfficeBoxLabel"/></legend>
			<label class="fieldLabel" for="enterPoBox"><fmt:message key="enterPoBoxLabel"/></label>
			<c:choose>
				<c:when test="${familyAssociationForm.enterPoBox}">
					<input type="checkbox" value="true" name="enterPoBox" id="familyCreateScreenEnterPoBox" checked="checked"/>
				</c:when>
				<c:otherwise>
					<input type="checkbox" value="true" name="enterPoBox" id="familyCreateScreenEnterPoBox" />
				</c:otherwise>
			</c:choose>
			<c:set var="poBoxFields" value="${familyAssociationForm.poBoxFields}" scope="request"/>
			<c:set var="createFlag" value="${createFlag}" scope="request"/>
			<div id="familyPoBoxFieldsContainer" <c:if test="${not familyAssociationForm.enterPoBox}">hidden=true</c:if>>
				<jsp:include page="../../contact/includes/poBoxFields.jsp"/>
			</div>
		</fieldset>
		<fieldset <c:if test="${not createFlag}">disabled</c:if>>
			<legend><fmt:message key="telephoneNumberLabel"/></legend>
			<form:errors path="familyAssociationTelephoneNumberItems" cssClass="error"/>
			<c:set var="familyAssociationTelephoneNumberItems" value="${familyAssociationTelephoneNumberItems}" scope="request"/>
			<jsp:include page="telephoneNumberTable.jsp"/>
		</fieldset>
		<fieldset <c:if test="${not createFlag}">disabled</c:if>>
			<legend><fmt:message key="eMailAddressLabel"/></legend>
			<form:errors path="familyAssociationOnlineAccountItems" cssClass="error"/>
			<c:set var="familyAssociationOnlineAccountItems" value="${familyAssociationOnlineAccountItems}" scope="request"/>
			<jsp:include page="emailTable.jsp"/>
		</fieldset>
	</c:if>
	<fieldset>
		<legend><fmt:message key="familyToOffenderAssociationLabel"/></legend>
		<span class="fieldGroup">
			<form:label path="startDate" class="fieldLabel">
				<fmt:message key="startDateLabel" />
			</form:label>
			<form:input path="startDate" class="date"/>
			<form:errors path="startDate"  cssClass="error"/>
		</span>
		<span class="fieldGroup">
			<form:label path="endDate" class="fieldLabel">
				<fmt:message key="endDateLabel" />
			</form:label>
			<form:input path="endDate" class="date"/>
			<form:errors path="endDate"  cssClass="error"/>
		</span>
		<span class="fieldGroup" >
			<form:label path="category" class="fieldLabel">
				<fmt:message key="relationshipLabel" />
			</form:label>					
			<form:select path="category" id="relationship">
				<form:option value=""><fmt:message key="nullLabel" bundle="${commonBundle}"/></form:option>
				<form:options items="${categories}" itemValue="id" itemLabel="name"/>
			</form:select>
			<form:errors path="category" cssClass="error"/>
		</span>
		<div id = "marriageDateContainer" class = "${marriageFieldsVisibilityClass}"> 
			<span  class="fieldGroup" >	
				<form:label path="marriageDate" class="fieldLabel">
					<fmt:message key="familyMarriageDateLabel" />
				</form:label>
				<form:input path="marriageDate" class="date" id="marriageDateInput"/>
				<form:errors path="marriageDate"  cssClass="error"/>
			</span>
		</div>
		<div id = "divorceDateContainer" class = "${marriageFieldsVisibilityClass}">
			<span  class="fieldGroup" >
				<form:label path="divorceDate" class="fieldLabel">
					<fmt:message key="familyDivorceDateLabel" />
				</form:label>
				<form:input path="divorceDate" class="date" id="divorceDateInput"/>
				<form:errors path="divorceDate"  cssClass="error"/>
			</span>
		</div>
		<span class="fieldGroup">
			<form:label path="emergencyContact" class="fieldLabel">
			<fmt:message key="familyEmergencyContactLabel" /></form:label>
			<form:radiobutton path="emergencyContact" id="emergencyContactYes" value="${true}"/><label class="fieldValueLabel" for="emergencyContactYes"><fmt:message key="yesLabel" bundle="${commonBundle}"/></label>
			<form:radiobutton path="emergencyContact" id="emergencyContactNo" value="${false}"/><label class="fieldValueLabel" for="emergencyContactNo"><fmt:message key="noLabel" bundle="${commonBundle}"/></label>
			<form:radiobutton path="emergencyContact" id="emergencyContactUnknown" value="${null}"/><label class="fieldValueLabel" for="emergencyContactUnknown"><fmt:message key="unknownLabel" bundle="${commonBundle}"/></label>
			<form:errors path="emergencyContact" cssClass="error"/>
		</span>
		<span class="fieldGroup">
			<form:label path="dependent" class="fieldLabel">
			<fmt:message key="familyDependantLabel" /></form:label>
			<form:radiobutton path="dependent" id="dependentYes" value="${true}"/><label class="fieldValueLabel" for="dependentYes"><fmt:message key="yesLabel" bundle="${commonBundle}"/></label>
			<form:radiobutton path="dependent" id="dependentNo" value="${false}"/><label class="fieldValueLabel" for="dependentNo"><fmt:message key="noLabel" bundle="${commonBundle}"/></label>
			<form:radiobutton path="dependent" id="dependentUnknown" value="${null}"/><label class="fieldValueLabel" for="dependentUnknown"><fmt:message key="unknownLabel" bundle="${commonBundle}"/></label>
			<form:errors path="dependent" cssClass="error"/>
		</span>
		<span class="fieldGroup">
			<form:label path="cohabitant" class="fieldLabel">
			<fmt:message key="familyCohabitateLabel" /></form:label>
			<form:radiobutton path="cohabitant" id="cohabitantYes" value="${true}"/><label class="fieldValueLabel" for="cohabitantYes"><fmt:message key="yesLabel" bundle="${commonBundle}"/></label>
			<form:radiobutton path="cohabitant" id="cohabitantNo" value="${false}"/><label class="fieldValueLabel" for="cohabitantNo"><fmt:message key="noLabel" bundle="${commonBundle}"/></label>
			<form:radiobutton path="cohabitant" id="cohabitantUnknown" value="${null}"/><label class="fieldValueLabel" for="cohabitantUnknown"><fmt:message key="unknownLabel" bundle="${commonBundle}"/></label>
			<form:errors path="cohabitant" cssClass="error"/>
		</span>
	</fieldset>
	<%-- <c:if test="${empty familyMember}"> --%>
		<fieldset>
			<legend><fmt:message key="offenderToFamilyAssociationNotesLabel"/></legend>
			<form:errors cssClass="error" path="familyAssociationNoteItems"/>
			<c:set var="offenderRelationshipNoteItems" value="${familyAssociationForm.familyAssociationNoteItems}" scope="request"/>
			<c:set var="offenderRelationshipNoteItemsFieldName" value="familyAssociationNoteItems" scope="request"/>
			<c:set var="baseUrl" value="${pageContext.request.contextPath}/family" scope="request"/>
			<jsp:include page="/WEB-INF/views/offenderRelationship/includes/offenderRelationshipNoteItemsTable.jsp"/>
		</fieldset>
	<%-- </c:if> --%>
	<c:if test="${not empty familyAssociation}">
		<c:set var="updatable" value="${familyAssociation}" scope="request"/>
		<jsp:include page="/WEB-INF/views/audit/includes/updateSignature.jsp"/>
	</c:if>
	<p class="buttons">
		<input type="submit" value="<fmt:message key='saveFamilyLabel' />"/>
	</p>
</form:form>
</fmt:bundle>