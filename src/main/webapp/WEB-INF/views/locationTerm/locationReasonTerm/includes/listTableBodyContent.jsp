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
 - List table body contents of location reason terms.
 -
 - Author: Stephen Abson
 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<c:forEach var="locationReasonTerm" items="${locationReasonTerms}">
	<tr>
		<td>
			<a href="${pageContext.request.contextPath}/locationTerm/locationReasonTerm/locationReasonTermsActionMenu.html?locationReasonTerm=${locationReasonTerm.id}" class="actionMenuItem"></a>
		</td>
		<td>
			<c:out value="${locationReasonTerm.locationTerm.location.organization.name}"/>
			<fmt:formatDate value="${locationReasonTerm.locationTerm.dateRange.startDate}" pattern="MM/dd/yyyy h:mm a"/>
			<c:if test="${not empty locationReasonTerm.locationTerm.dateRange.endDate}">
				- <fmt:formatDate value="${locationReasonTerm.locationTerm.dateRange.endDate}" pattern="MM/dd/yyyy h:mm a"/>
			</c:if>
		</td>
		<td><c:out value="${locationReasonTerm.reason.name}"/></td>
		<td><fmt:formatDate value="${locationReasonTerm.dateRange.startDate}" pattern="MM/dd/yyyy h:mm a"/></td>
		<td><fmt:formatDate value="${locationReasonTerm.dateRange.endDate}" pattern="MM/dd/yyyy h:mm a"/></td>
	</tr>
</c:forEach>