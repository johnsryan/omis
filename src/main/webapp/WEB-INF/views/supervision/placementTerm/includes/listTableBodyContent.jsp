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
 - Table body content of placement terms.
 -
 - Author: Stephen Abson
 --%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setBundle basename="omis.supervision.msgs.placementTerm" var="placementTermBundle"/>
<fmt:bundle basename="omis.msgs.common">
<c:forEach var="placementTermSummary" items="${placementTermSummaries}">
	<tr>
		<td><a class="actionMenuItem" href="${pageContext.request.contextPath}/supervision/placementTerm/placementTermTableRowActionMenu.html?placementTerm=${placementTermSummary.id}"></a></td>
		<td hidden="hidden"><c:out value="${placementTermSummary.supervisoryOrganizationName}"/></td>
		<td><c:out value="${placementTermSummary.correctionalStatusName}"/></td>
		<td><fmt:formatDate value="${placementTermSummary.startDate}" pattern="MM/dd/yyyy h:mm a"/></td>
		<td><fmt:formatDate value="${placementTermSummary.endDate}" pattern="MM/dd/yyyy h:mm a"/></td>
		<td><c:if test="${not empty placementTermSummary.dayCount}"><c:out value="${placementTermSummary.dayCount}"/></c:if></td>
		<td><c:out value="${placementTermSummary.startChangeReasonName}"/></td>
		<td hidden="hidden"><c:out value="${placementTermSummary.endChangeReasonName}"/></td>
		<td hidden="hidden"><fmt:message key="placementStatusLabel.${placementTermSummary.status.name}" bundle="${placementTermBundle}"/></td>
		<td hidden="hidden"><fmt:formatDate value="${placementTermSummary.statusStartDate}" pattern="MM/dd/yyyy h:mm a"/></td>
		<td hidden="hidden"><fmt:formatDate value="${placementTermSummary.statusEndDate}" pattern="MM/dd/yyyy h:mm a"/></td>
		<td hidden="hidden"><c:if test="${not empty placementTermSummary.status and placementTermSummary.status.name ne 'PLACED' and not empty placementTermSummary.statusDayCount}"><c:out value="${placementTermSummary.statusDayCount}"/></c:if></td>
	</tr>
</c:forEach>
</fmt:bundle>