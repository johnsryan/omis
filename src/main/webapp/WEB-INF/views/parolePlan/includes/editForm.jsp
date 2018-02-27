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
 - Edit form screen for parole plans.
 -
 - Author: Josh Divine
 - Version: 0.1.0 (Feb 13, 2018)
 - Since: OMIS 3.0
 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.paroleplan.msgs.parolePlan">
<form:form commandName="parolePlanForm" class="editForm" enctype="multipart/form-data">
<fieldset>
	<legend><fmt:message key="eligibilityDetailsTitle"/></legend>
	<span class="fieldGroup">
		<label class="fieldLabel"><fmt:message key="hearingEligibleDateLabel"/></label>
		<span id="hearingEligibilityDate">
			<fmt:formatDate value="${paroleEligibility.hearingEligibilityDate}" pattern="MM/dd/yyyy"/>
		</span>
	</span>
	<span class="fieldGroup">
		<label class="fieldLabel"><fmt:message key="nextReviewDateLabel"/></label>
		<span id="nextReviewDate">
			<fmt:formatDate value="${paroleEligibility.reviewDate}" pattern="MM/dd/yyyy"/>
		</span>
	</span>
</fieldset>
<fieldset>
	<legend><fmt:message key="parolePlanDetailsTitle"/></legend>
	<span class="fieldGroup">
		<form:label path="vocationalPlan" class="fieldLabel">
			<fmt:message key="vocationalPlanLabel"/></form:label>
		<form:textarea path="vocationalPlan"/> 
		<form:errors path="vocationalPlan" cssClass="error"/>
	</span>
	<span class="fieldGroup">
		<form:label path="residencePlan" class="fieldLabel">
			<fmt:message key="residencePlanLabel"/></form:label>
		<form:textarea path="residencePlan"/> 
		<form:errors path="residencePlan" cssClass="error"/>
	</span>
	<span class="fieldGroup">
		<form:label path="treatmentPlan" class="fieldLabel">
			<fmt:message key="treatmentPlanLabel"/></form:label>
		<form:textarea path="treatmentPlan"/> 
		<form:errors path="treatmentPlan" cssClass="error"/>
	</span>
	<span class="fieldGroup">
		<form:label path="evaluator" class="fieldLabel">
			<fmt:message key="evaluatorLabel"/></form:label>
		<form:hidden path="evaluator"/>
		<input type="text" id="evaluatorInput"/>
		<a id="evaluatorCurrent" class="currentUserAccountLink"></a>
		<a id="evaluatorClear" class="clearLink"></a>
		<span id="evaluatorDisplay">
			<c:if test="${not empty parolePlanForm.evaluator}">
				<c:set var="name" value="${parolePlanForm.evaluator.staffMember.name}" scope="request"/>
				<c:out value="${name.lastName}, ${name.firstName}"/>
				<c:if test="${not empty name.middleName}">
					<c:out value=" ${name.middleName}"/>
				</c:if>
				<c:if test="${not empty parolePlanForm.evaluator.title}">
					<c:out value=" ${parolePlanForm.evaluator.title.name}"/>
				</c:if>
			</c:if>
		</span>
		<form:errors path="evaluator" cssClass="error"/>
	</span>
	<span class="fieldGroup">
		<form:label path="evaluationDescription" class="fieldLabel">
			<fmt:message key="evaluationDescriptionLabel"/></form:label>
		<form:textarea path="evaluationDescription"/> 
		<form:errors path="evaluationDescription" cssClass="error"/>
	</span>
</fieldset>
<fieldset id="parolePlanNotesHolder">
	<legend><fmt:message key="parolePlanNotesTitle"/></legend>
	<jsp:include page="parolePlanNotesTable.jsp"/>
	<form:errors path="parolePlanNoteItems" cssClass="error"/>
</fieldset>
<fieldset>
	<legend>
		<fmt:message key="parolePlanDocumentAssociationsTitle" />
	</legend>
	<span class="fieldGroup">
		<c:set var="parolePlanDocumentAssociationItems" value="${parolePlanForm.parolePlanDocumentAssociationItems}" scope="request"/>
		<jsp:include page="documentAssociationItems.jsp"/>
	</span>
</fieldset>
<c:if test="${not empty parolePlan}">
<c:set var="updatable" value="${parolePlan}" scope="request"/>
<jsp:include page="/WEB-INF/views/audit/includes/updateSignature.jsp"/>
</c:if>
<p class="buttons">
	<input type="submit" value="<fmt:message key='saveLabel' bundle='${commonBundle}'/>"/>
</p>
</form:form>
</fmt:bundle>