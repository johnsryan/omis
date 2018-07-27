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
 - Author: Trevor Isles
 - Date: June 22, 2018
 - Since: OMIS 3.0
 --%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.paroleeligibility.msgs.paroleEligibility">
<c:forEach var="paroleEligibilitySummary" items="${paroleEligibilitySummaries}" varStatus="status">
	<tr>
		<td><a class="actionMenuItem" href="${pageContext.request.contextPath}/paroleEligibility/unscheduledEligibilitiesRowActionMenu.html?eligibility=${paroleEligibilitySummary.paroleEligibilityId}" id="paroleEligibilities${status.index}"></a></td>
		<td><fmt:formatDate value="${paroleEligibilitySummary.hearingEligibilityDate}" pattern="MM/dd/yyyy"/></td>
		<td><c:out value="${paroleEligibilitySummary.lastName}, ${paroleEligibilitySummary.firstName} ${paroleEligibilitySummary.middleName} (${paroleEligibilitySummary.offenderNumber})"/></td>
		<td><fmt:formatDate value="${paroleEligibilitySummary.statusDate}" pattern="MM/dd/yyyy"/></td>
		<td><c:out value="${paroleEligibilitySummary.appearanceCategoryName}"/></td>
		<td><c:if test="${not empty paroleEligibilitySummary.statusCategory}"><fmt:message key="paroleEligibilityStatusLabel.${paroleEligibilitySummary.statusCategory}"/></c:if>
		<td><fmt:formatDate value="${paroleEligibilitySummary.hearingDate}" pattern="MM/dd/yyyy"/></td>
	</tr>
</c:forEach>
</fmt:bundle>