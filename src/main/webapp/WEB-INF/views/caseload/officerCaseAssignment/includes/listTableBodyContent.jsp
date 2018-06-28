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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:bundle basename="omis.caseload.msgs.officerCaseAssignment">
<c:forEach var="officerCaseAssignmentSummary" items="${officerCaseAssignmentSummaries}" varStatus="status">
<c:set var="officerCaseAssignmentSummary" value="${officerCaseAssignmentSummary}" scope="request"/>
<tr id="officerCaseAssignmentRow${status.index}">
<td>
	<a class="actionMenuItem rowActionMenuLinks"
				id="summaryActionMenuLink${status.index}"
				href="${pageContext.request.contextPath}/caseload/officerCaseAssignment/officerCaseAssignmentsActionMenu.html?officerCaseAssignment=${officerCaseAssignmentSummary.officerCaseAssignmentId}&offender=${offender.id}&userAccount=${userAccount.id}"></a>
</td>
		<td>
			<fmt:formatDate value="${officerCaseAssignmentSummary.startDate}" pattern="MM/dd/yyyy"/>
		</td>
		<td>
			<fmt:formatDate value="${officerCaseAssignmentSummary.endDate}" pattern="MM/dd/yyyy"/>
		</td>
		<td>			
			<c:choose>
			<c:when test="${empty offender}">
				<c:out value="${officerCaseAssignmentSummary.offenderLastName}, ${officerCaseAssignmentSummary.offenderFirstName} ${officerCaseAssignmentSummary.offenderMiddleName} #${officerCaseAssignmentSummary.offenderNumber}"/>
			</c:when>
			<c:otherwise>
				<c:out value="${officerCaseAssignmentSummary.officerLastName}, ${officerCaseAssignmentSummary.officerFirstName} ${officerCaseAssignmentSummary.officerMiddleName} (${officerCaseAssignmentSummary.officerUsername})"/>
			</c:otherwise>
			</c:choose>
		</td>
		<td>
			<c:out value="${officerCaseAssignmentSummary.supervisionLevelCategoryDescription}"/>
		</td>
</tr>
</c:forEach>	
</fmt:bundle>