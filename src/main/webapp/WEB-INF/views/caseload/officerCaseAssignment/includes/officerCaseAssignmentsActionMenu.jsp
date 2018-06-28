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
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.caseload.msgs.officerCaseAssignment">
	<ul>
		<c:if test="${empty officerCaseAssignment}">
			<sec:authorize access="hasRole('OFFICER_CASE_ASSIGNMENT_CREATE') or hasRole('ADMIN')">
				<li>
					<a class="createLink" href="${pageContext.request.contextPath}/caseload/officerCaseAssignment/create.html?offender=${offender.id}&userAccount=${userAccount.id}"><span class="visibleLinkLabel"><fmt:message key="createOfficerCaseAssignmentsLink"/></span></a>
				</li>
			</sec:authorize>
		</c:if>
		<c:if test="${not empty officerCaseAssignment}">
			<sec:authorize access="hasRole('OFFICER_CASE_ASSIGNMENT_VIEW') or hasRole('ADMIN')">
				<li>
					<a class="viewEditLink" href="${pageContext.request.contextPath}/caseload/officerCaseAssignment/edit.html?officerCaseAssignment=${officerCaseAssignment.id}&offender=${offender.id}&userAccount=${userAccount.id}"><span class="visibleLinkLabel"><fmt:message key="viewEditLink" bundle="${commonBundle}"/></span></a>
				</li>
			</sec:authorize>
			<sec:authorize access="hasRole('OFFICER_CASE_ASSIGNMENT_REMOVE') or hasRole('ADMIN')">
				<li>
					<a class="removeLink" href="${pageContext.request.contextPath}/caseload/officerCaseAssignment/remove.html?officerCaseAssignment=${officerCaseAssignment.id}"><span class="visibleLinkLabel"><fmt:message key="removeLink" bundle="${commonBundle}"/></span></a>
				</li>
			</sec:authorize>
		</c:if>
	</ul>
</fmt:bundle>