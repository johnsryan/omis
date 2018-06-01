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
 - Table body of location terms.
 -
 - Author: Stephen Abson
 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:setBundle basename="omis.locationterm.msgs.locationTerm" var="locationTermBundle"/>
<c:forEach var="locationTermSummary" items="${locationTermSummaries}">
	<tr>
		<td>
			<a href="${pageContext.request.contextPath}/locationTerm/locationTermsActionMenu.html?locationTerm=${locationTermSummary.id}" class="actionMenuItem"></a>
		</td>
		<td><c:out value="${locationTermSummary.locationName}"/></td>
		<td>
			<c:choose>
				<c:when test="${not empty locationTermSummary.singleReasonName}">
					<c:out value="${locationTermSummary.singleReasonName}"/>
				</c:when>
				<c:otherwise>
					<c:if test="${locationTermSummary.reasonCount gt 0}">
						<fmt:message key="reasonCountLabel" bundle="${locationTermBundle}">
							<fmt:param value="${locationTermSummary.reasonCount}"/>
						</fmt:message>
					</c:if>
				</c:otherwise>
			</c:choose>
		</td>
		<td><fmt:formatDate value="${locationTermSummary.startDate}" pattern="MM/dd/yyyy h:mm a"/></td>
		<td><fmt:formatDate value="${locationTermSummary.endDate}" pattern="MM/dd/yyyy h:mm a"/></td>
		<td><c:out value="${locationTermSummary.dayCount}"/></td>
	</tr>
</c:forEach>