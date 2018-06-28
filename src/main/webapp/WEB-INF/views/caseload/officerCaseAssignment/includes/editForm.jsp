<?xml version="1.0" encoding="UTF-8"?>
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
 - Author: Josh Divine
 - Version: 0.1.0 (Jun 21, 2018)
 - Since: OMIS 3.0
 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<sec:authorize var="editOfficerCaseAssignment" access="hasRole('OFFICER_CASE_ASSIGNMENT_EDIT') or hasRole('ADMIN') or hasRole('OFFICER_CASE_ASSIGNMENT_CREATE')"/>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.caseload.msgs.officerCaseAssignment">
<form:form commandName="officerCaseAssignmentForm" class="editForm">
	<fieldset>
		<legend><fmt:message key="assignmentDetailsTitle"/></legend>
		<span class ="fieldGroup">
			<form:label path="officer" class="fieldLabel">
				<fmt:message key="assignedToLabel"/></form:label>
			<input id="assignedTo" ${not empty userAccount ? 'type="hidden"' : ''}/>
			<form:input path="officer" type="hidden"/>			
			<a id="currentUserAccountLink" class="currentUserAccountLink" ${not empty userAccount ? 'style="display: none"' : ''}></a>
			<a id="clearUserLink" class="clearLink" ${not empty userAccount ? 'style="display: none"' : ''}></a>
			<span id="userAccountCurrentLabel">
			<c:if test="${not empty officerCaseAssignmentForm.officer}">
				<c:out value="${officerCaseAssignmentForm.officer.user.name.lastName}, ${officerCaseAssignmentForm.officer.user.name.firstName} (${officerCaseAssignmentForm.officer.username})"/>
			</c:if>
			</span>
		<form:errors path="officer" cssClass="error"/>
		</span>
		<span class ="fieldGroup">
			<form:label path="selectedOffender" class="fieldLabel">
				<fmt:message key="offenderLabel"/></form:label>
			<input id="offenderSearch" ${not empty offender or not empty officerCaseAssignmentForm.selectedOffender ? 'type="hidden"' : ''}/>
			<form:input path="selectedOffender" type="hidden"/>			
			<a id="clearOffenderLink" class="clearLink" ${not empty offender or not empty officerCaseAssignmentForm.selectedOffender ? 'style="display: none"' : ''}></a>
			<span id="offenderCurrentLabel">
			<c:if test="${not empty officerCaseAssignmentForm.selectedOffender}">
				<c:out value="${officerCaseAssignmentForm.selectedOffender.name.lastName}, ${officerCaseAssignmentForm.selectedOffender.name.firstName} #${officerCaseAssignmentForm.selectedOffender.offenderNumber}"/>
			</c:if>
			</span>
		<form:errors path="selectedOffender" cssClass="error"/>
		</span>
		<span class="fieldGroup">
			<form:label path="supervisionLevelCategory" class="fieldLabel">
				<fmt:message key="supervisionLevelCategoryLabel"/>
			</form:label>
			<form:select path="supervisionLevelCategory">
				<jsp:include page="/WEB-INF/views/includes/nullOption.jsp"/>
				<c:forEach items="${supervisionLevels}" var="supervisionLevel">
					<option value="${supervisionLevel.id}" ${supervisionLevel == officerCaseAssignmentForm.supervisionLevelCategory ? 'selected="selected"' : ''}>
						<c:out value="${supervisionLevel.description}"/>
					</option>
				</c:forEach>
			</form:select>
			<form:errors path="supervisionLevelCategory" cssClass="error"/>
		</span>
		<span class="fieldGroup">
			<form:label path="location" class="fieldLabel">
				<fmt:message key="regionOfficeLabel"/>
			</form:label>
			<form:select path="location">
				<jsp:include page="/WEB-INF/views/includes/nullOption.jsp"/>
				<c:forEach items="${locations}" var="location">
					<option value="${location.id}" ${location == officerCaseAssignmentForm.location ? 'selected="selected"' : ''}>
						<c:out value="${location.organization.name}"/>
					</option>
				</c:forEach>
			</form:select>
			<form:errors path="location" cssClass="error"/>
		</span>
		<span class="fieldGroup">
			<form:label path="startDate" class="fieldLabel">
				<fmt:message key="startDateLabel"/>
			</form:label>
			<form:input path="startDate" class="date"/>
			<form:errors path="startDate" cssClass="error"/>
		</span>
		<span class="fieldGroup">
			<form:label path="endDate" class="fieldLabel">
				<fmt:message key="endDateLabel"/>
			</form:label>
			<form:input path="endDate" class="date"/>
			<form:errors path="endDate" cssClass="error"/>
		</span>
	</fieldset>
	<c:if test="${not empty officerCaseAssignment}">
		<c:set var="updatable" value="${officerCaseAssignment}" scope="request"/>
		<jsp:include page="/WEB-INF/views/audit/includes/updateSignature.jsp"/>
	</c:if>
	<c:if test="${editOfficerCaseAssignment}">
		<p class="buttons">
			<input type="submit" value="<fmt:message key='saveLabel' bundle='${commonBundle}'/>"/>
		</p>
	</c:if>
</form:form>
</fmt:bundle>