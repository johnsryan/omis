<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:bundle basename="omis.visitation.msgs.visitation">
	<form:form commandName="visitationAssociationForm" class="editForm">
	<fieldset>
			<legend><fmt:message key="personDetailsLabel"/></legend>
			<span class="fieldGroup">
				<c:choose>
					<c:when test="${visitationAssociationForm.newVisitor}">
						<c:set value="${visitationAssociationForm.personFields}" var="personFields" scope="request"/>
						<jsp:include page="../../person/includes/personFields.jsp"/>
					</c:when>
					<c:otherwise>
						<c:set value="${contactSummary}" var="contactSummary" scope="request"/>
						<jsp:include page="../../contact/includes/contactSummary.jsp"/>
						<form:input type="hidden" path="person"/>
						<form:errors path="person"/>
					</c:otherwise>
				</c:choose>					
			</span>
			<form:input type="hidden" path="newVisitor"/>
		</fieldset>
		<fieldset>
			<legend><fmt:message key="approvalDetailsLabel"/></legend>
				<c:choose>
					<c:when test="${empty offender}">
						<span class="fieldGroup">
							<form:label path="associatedOffender" class="fieldLabel"><fmt:message key="associatedOffenderLabel"/></form:label>
							<form:input type="hidden" path="associatedOffender" id="associatedOffender"/>
							<input type="text" id="associatedOffenderInput"/>
							<a id="associatedOffenderClear" class="clearLink"></a>
							<span id="associatedOffenderDisplay">
								<c:if test="${not empty visitationAssociationForm.associatedOffender}">
									<fmt:message key="offenderNameAndNumber">
										<fmt:param value="${visitationAssociationForm.associatedOffender.name.lastName}"/>
										<fmt:param value="${visitationAssociationForm.associatedOffender.name.firstName}"/>
										<fmt:param value="${visitationAssociationForm.associatedOffender.offenderNumber}"/>
									</fmt:message>
								</c:if>
							</span>
							<form:errors cssClass="error" path="associatedOffender"/>
						</span>
					</c:when>
					<c:otherwise>
						<form:input type="hidden" path="associatedOffender"/>
						<form:errors cssClass="error" path="associatedOffender"/>
					</c:otherwise>
				</c:choose>
				<span class="fieldGroup">
					<form:label path="approved" class="fieldLabel"><fmt:message key="visitationAssociationApprovedLabel"/></form:label>
					<form:checkbox path="approved"/>
					<form:errors cssClass="error" path="approved"/>
				</span>
				<span class="fieldGroup">
					<form:label path="decisionDate" class="fieldLabel"><fmt:message key="visitationAssociationDecisionDateLabel"/></form:label>
					<form:input path="decisionDate" class="date"/>
					<form:errors cssClass="error" path="decisionDate"/>
				</span>
		</fieldset>		
		<fieldset>
			<legend><fmt:message key="VisitationFlagsLabel"/></legend>
			<span class="fieldGroup">
				<form:label path="money" class="fieldLabel"><fmt:message key="visitationAssociationMoneyLabel"/></form:label>
				<form:checkbox path="money"/>
			</span>
			<span class="fieldGroup">
				<form:label path="nonContact" class="fieldLabel"><fmt:message key="visitationAssociationNonContactLabel"/></form:label>
				<form:checkbox path="nonContact"/>
			</span>
			<span class="fieldGroup">
				<form:label path="courtOrder" class="fieldLabel"><fmt:message key="visitationAssociationCourtOrderLabel"/></form:label>
				<form:checkbox path="courtOrder"/>
			</span>
			<span class="fieldGroup">
				<form:label path="specialVisit" class="fieldLabel"><fmt:message key="visitationAssociationSpecialVisitLabel"/></form:label>
				<form:checkbox path="specialVisit"/>
			</span>
		</fieldset>
		<fieldset>
			<legend><fmt:message key="visitorDetailsLabel"/></legend>
			<span class="fieldGroup">
				<form:label path="category" class="fieldLabel"><fmt:message key="visitationAssociationCategoryLabel"/></form:label>
				<form:select path="category">
					<jsp:include page="../../includes/nullOption.jsp"/>
					<form:options items="${visitationAssociationCategories}" itemValue="id" itemLabel="name"/>
				</form:select>
				<form:errors cssClass="error" path="category"/>
			</span>
			<span class="fieldGroup">
				<form:label path="startDate" class="fieldLabel"><fmt:message key="visitationAssociationStartDateLabel"/></form:label>
				<form:input class="date" path="startDate"/>
				<form:errors cssClass="error" path="startDate"/>
			</span>
			<span class="fieldGroup">
				<form:label path="endDate" class="fieldLabel"><fmt:message key="visitationAssociationEndDateLabel"/></form:label>
				<form:input class="date" path="endDate"/>
				<form:errors cssClass="error" path="endDate"/>
			</span>
			<span class="fieldGroup">
				<form:label path="guardianship" class="fieldLabel"><fmt:message key="guardianshipLabel"/></form:label>
				<form:textarea path="guardianship" maxlength="1024" rows="8" placeholder="${guardianshipPlaceholder}"/>
				<span class="characterCounter" id="guardianshipCharacterCounter">
				</span>
			</span>
			<span class="fieldGroup">
				<form:label path="notes" class="fieldLabel"><fmt:message key="visitorCommentsLabel"/></form:label>
				<form:textarea path="notes" maxlength="2048" rows="8" placeholder="${commentsPlaceholder}"/>
				<span class="characterCounter" id="notesCharacterCounter">
				</span>
				<form:errors cssClass="error" path="notes"/>
			</span>
		</fieldset>
		<c:if test="${not empty visitationAssociation}">
			<c:set var="updatable" value="${visitationAssociation}" scope="request"/>
			<jsp:include page="/WEB-INF/views/audit/includes/updateSignature.jsp"/>
		</c:if>
		<p class="buttons">
			<input type="submit" value="<fmt:message key='saveVisitationAssociationLabel'/>"/>
		</p>
	</form:form>
</fmt:bundle>