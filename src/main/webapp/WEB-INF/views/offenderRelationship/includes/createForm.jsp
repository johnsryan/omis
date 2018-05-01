<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:setBundle basename="omis.person.msgs.person" var="personBundle"/>
<fmt:bundle basename="omis.offenderrelationship.msgs.createOffenderRelationship">
	<form:form commandName="createRelationshipsForm" class="editForm">	
	<c:set var="addressFields" value="${createRelationshipsForm.addressFields}" scope="request"/>
	<c:set var="addressFieldsPropertyName" value="addressFields" scope="request"/>
	<c:set var="poBoxFields" value="${createRelationshipsForm.poBoxFields}" scope="request"/>
	<c:set var="poBoxFieldsPropertyName" value="poBoxFields" scope="request"/>
	<c:set var="personFields" value="${createRelationshipsForm.personFields}" scope="request"/>
	<c:set var="personFieldsPropertyName" value="personFields" scope="request"/>
	<c:set var="visitationAssociationFields" value="${createRelationshipsForm.visitationAssociationFields}" scope="request"/>
	<c:set var="visitationAssociationFieldsPropertyName" value="visitationAssociationFields" scope="request"/>
	<c:if test="${empty relation}">		
		<fieldset id="personFields">
			<legend><fmt:message key="personFieldsLabel"/></legend>
			<jsp:include page="/WEB-INF/views/person/includes/personFields.jsp"/>
		</fieldset>		
		<fieldset id="addressFields">
			<legend><fmt:message key="addressFieldsLabel"/></legend>	
			<label class="fieldLabel" for="enterAddress"><fmt:message key="enterAddressLabel"/></label>
			<c:choose>
				<c:when test="${createRelationshipsForm.enterAddress}">
					<input type="checkbox" value="true" name="enterAddress" id="createScreenEnterAddress" checked="checked"/>
				</c:when>
				<c:otherwise>
					<input type="checkbox" value="true" name="enterAddress" id="createScreenEnterAddress" />
				</c:otherwise>
			</c:choose>
			<div id="createScreenAddressContainer" <c:if test="${createRelationshipsForm.enterAddress ne true}">hidden</c:if>>
				<c:if test="${createRelationshipsForm.addressOperation eq 'EXISTING'}">
					<form:radiobutton path="addressOperation" id="existingAddress" value="EXISTING" checked ="checked"/><label class="fieldValueLabel" for="existingAddress"><fmt:message key="existingAddressLabel"/></label>
					<form:input path="addressQuery" id="offenderRelationshipSearchAddressQuery" />
					<form:errors path="addressQuery" cssClass="error"/>
					<form:hidden path="address" id="searchAddress"/>
					<form:errors path="address" cssClass="error"/>
					<br>
					<form:radiobutton path="addressOperation" id="newAddress" value="NEW" /><label class="fieldValueLabel" for="newAddress"><fmt:message key="newAddressLabel"/></label>
				</c:if>
				<c:if test="${createRelationshipsForm.addressOperation eq 'NEW'}">
					<form:radiobutton path="addressOperation" id="existingAddress" value="EXISTING"/><label class="fieldValueLabel" for="existingAddress"><fmt:message key="existingAddressLabel"/></label>
					<form:input path="addressQuery" id="offenderRelationshipSearchAddressQuery" />
					<form:errors path="addressQuery" cssClass="error"/>
					<form:hidden path="address" id="searchAddress"/>
					<form:errors path="address" cssClass="error"/>
					<br>
					<form:radiobutton path="addressOperation" id="newAddress" value="NEW" checked ="checked"/><label class="fieldValueLabel" for="newAddress"><fmt:message key="newAddressLabel"/></label>
				</c:if>
				<c:set var="addressFields" value="${createRelationshipsForm.addressFields}" scope="request"/>
				<div id="createScreenAddressFieldContainer"	<c:if test="${createRelationshipsForm.addressOperation eq 'EXISTING'}">hidden</c:if>>
					<jsp:include page="../../address/includes/addressFields.jsp"/>
				</div>
			</div>
			
		</fieldset>
		<fieldset id="poBoxFields">
			<legend><fmt:message key="poBoxFieldsLabel"/></legend>	
			<label class="fieldLabel" for="enterPoBox"><fmt:message key="enterPoBoxLabel"/></label>
			<c:choose>
				<c:when test="${createRelationshipsForm.enterPoBox}">
					<input type="checkbox" value="true" name="enterPoBox" id="createScreenEnterPoBox" checked="checked"/>
				</c:when>
				<c:otherwise>
					<input type="checkbox" value="true" name="enterPoBox" id="createScreenEnterPoBox"/>
				</c:otherwise>
			</c:choose>
			<div id="poBoxFieldsContainer" <c:if test="${createRelationshipsForm.enterPoBox ne true}">hidden</c:if>>
				<jsp:include page="/WEB-INF/views/contact/includes/poBoxFields.jsp"/>
			</div>
		</fieldset>
		<fieldset id="telephoneNumberFields">
			<legend><fmt:message key="telephoneNumberItemsLabel"/></legend>
			<form:errors cssClass="error" path="telephoneNumberItems"/>
			<c:set var="telephoneNumberItems" value="${createRelationshipsForm.telephoneNumberItems}" scope="request"/>
				<jsp:include page="../../offenderRelationship/includes/create/createTelephoneNumberTableBody.jsp"/>
		</fieldset>
		<fieldset id="onlineAccountFields">
			<legend><fmt:message key="onlineAccountItemsLabel"/></legend>
			<form:errors cssClass="error" path="onlineAccountContactItems"/>
			<c:set var="onlineAccountContactItems" value="${createRelationshipsForm.onlineAccountContactItems}" scope="request"/>
				<jsp:include page="../../offenderRelationship/includes/create/createOnlineAccountTableBody.jsp"/>
		</fieldset>
		<fieldset id="offenderRelationshipNoteFields">
			<legend><fmt:message key="offenderRelationshipNotesLabel"/></legend>
			<form:errors cssClass="error" path="noteItems"/>
			<c:set var="offenderRelationshipNoteItems" value="${createRelationshipsForm.noteItems}" scope="request"/>
			<c:set var="offenderRelationshipNoteItemsFieldName" value="noteItems" scope="request"/>
			<c:set var="baseUrl" value="${pageContext.request.contextPath}/offenderRelationship/create" scope="request"/>
			<jsp:include page="offenderRelationshipNoteItemsTable.jsp"/>
		</fieldset>
	</c:if>
	<c:if test="${not empty relation}">
			<fieldset>
				<c:set value="${existingContactSummary}" var="contactSummary" scope="request"/>
				<jsp:include page="/WEB-INF/views/contact/includes/contactSummary.jsp"/>
			</fieldset>
	</c:if>
	<sec:authorize access="hasRole('FAMILY_ASSOCIATION_CREATE') or hasRole('ADMIN')">
		<fieldset id="familyAssociationFields">
			<legend><fmt:message key="familyAssociationLabel"/></legend>	
			<label class="fieldLabel" for="createFamilyMember"><fmt:message key="createFamilyMemberLabel"/></label>
			<c:choose>
				<c:when test="${createRelationshipsForm.createFamilyMember}">
					<form:checkbox name="createFamilyMember" id="createFamilyMember" path="createFamilyMember" checked="checked" />
					<form:errors path="createFamilyMember" cssClass="error"/>
				</c:when>
				<c:otherwise>
					<form:checkbox name="createFamilyMember" id="createFamilyMember" path="createFamilyMember"/>
					<form:errors path="createFamilyMember" cssClass="error"/>
				</c:otherwise>
			</c:choose>	
			<div id = "familyMemberContainer" <c:if test="${not createRelationshipsForm.createFamilyMember}">hidden="true"</c:if>>
				<jsp:include page="/WEB-INF/views/family/includes/familyAssociationFields.jsp"/>
			</div>
		</fieldset>
	</sec:authorize>
	<sec:authorize access="hasRole('VICTIM_ASSOCIATION_CREATE') or hasRole('ADMIN')">
		<fieldset id="victimAssociationFields">
			<legend><fmt:message key="victimAssociationLabel"/></legend>
			<label class="fieldLabel" for="createVictim"><fmt:message key="createVictimLabel"/></label>
			<c:choose>
				<c:when test="${createRelationshipsForm.createVictim}">
					<form:checkbox  name="createVictim" id="createVictim" path="createVictim" checked="checked"/>
					<form:errors path="createVictim" cssClass="error"/>
				</c:when>
				<c:otherwise>
					<form:checkbox value="true" name="createVictim" id="createVictim" path="createVictim"/>
					<form:errors path="createVictim" cssClass="error"/>
				</c:otherwise>
			</c:choose>		
			<div id="victimContainer" <c:if test="${not createRelationshipsForm.createVictim}">hidden="true"</c:if>>
				<jsp:include page="/WEB-INF/views/victim/includes/victimAssociationFields.jsp"/>
			</div>
		</fieldset>
	</sec:authorize>
	<sec:authorize access="hasRole('VISITATION_ASSOCIATION_CREATE') or hasRole('ADMIN')">
		<fieldset id="visitationAssociationFields">
			<legend><fmt:message key="visitationAssociationLabel"/></legend>	
			<label class="fieldLabel" for="createVisitor"><fmt:message key="createVisitorLabel"/></label>
			<c:choose>
				<c:when test="${createRelationshipsForm.createVisitor}">
					<form:checkbox value="true" name="createVisitor" id="createVisitor" path="createVisitor" checked="checked"/>
					<form:errors path="createVisitor" cssClass="error"/>
				</c:when>
				<c:otherwise>
					<form:checkbox value="true" name="createVisitor" path="createVisitor" id="createVisitor"/>
					<form:errors path="createVisitor" cssClass="error"/>
				</c:otherwise>
			</c:choose>
			<div id="visitorContainer" <c:if test="${not createRelationshipsForm.createVisitor}">hidden="true"</c:if>>
				<jsp:include page="/WEB-INF/views/visitation/includes/visitationAssociationFields.jsp"/>
			</div>
		</fieldset>
	</sec:authorize>
	<p class="buttons">
		<input type="submit" value="<fmt:message key='saveFamilyLabel' />"/>
	</p>
	</form:form>
</fmt:bundle>