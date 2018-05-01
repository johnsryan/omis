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
  - Author: Ryan Johns
  - Author: Annie Wahl
  - Author: Josh Divine
  - Version: 0.1.3 (Apr 24, 2018)
  - Since: OMIS 3.0 
  --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:bundle basename="omis.presentenceinvestigation.msgs.presentenceInvestigationRequest">
<c:forEach var="summary" items="${summaries}">
<tr>
	<c:choose>
	<c:when test="${empty offender}">
		<c:set var="onReturn" value="byUser" />
	</c:when>
	<c:when test="${not empty offender}">
		<c:set var="onReturn" value="byOffender" />
	</c:when>
	</c:choose>
	<td><a class="actionMenuItem rowActionMenuItem" href="${pageContext.request.contextPath}/presentenceInvestigation/request/presentenceInvestigationRequestsRowActionMenu.html?presentenceInvestigationRequest=${summary.presentenceInvestigationRequestId}&onReturn=${onReturn}"></a></td>
	<c:choose>
		<c:when test="${empty offender}">
			<td>
			<c:if test="${not empty summary.offenderId}">
				<c:out value="${summary.offenderFirstName} ${summary.offenderLastName} #${summary.offenderNumber}"/>
			</c:if>
			</td>
		</c:when>
		<c:when test="${not empty offender}">
			<td><c:out value="${summary.assignedUserFirstName} ${summary.assignedUserLastName}"/></td>
		</c:when>
	</c:choose>
	<td><c:out value="${summary.docketValue}"/></td>
	<td><fmt:message key="presentenceInvestigationRequestStatusLabel.${summary.status}"/></td>
	<td><fmt:formatDate value="${summary.requestDate}" pattern="MM/dd/yyyy" /></td>
	<td><fmt:formatDate value="${summary.sentenceDate}" pattern="MM/dd/yyyy" /></td>
	<td><c:out value="${summary.category}"/></td>
</tr>
</c:forEach>
</fmt:bundle>