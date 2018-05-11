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
 - Table body content of STG affiliations.
 -
 - Author: Stephen Abson
 - Author: Trevor Isles
 - Author: Annie Wahl
 - Author: Josh Divine
 --%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<c:forEach var="affiliationSummary" items="${affiliationSummaries}"
	varStatus="status">
	<tr class="<c:if test='${affiliationSummary.active}'>activeRecord</c:if>">
		<td>
			<a class="actionMenuItem" href="${pageContext.request.contextPath}/stg/affiliationsActionMenu.html?affiliation=${affiliationSummary.id}"
			id="affiliations${status.index}"></a>
		</td>
		<td><c:out value="${affiliationSummary.groupName}"/></td>
		<td><c:out value="${affiliationSummary.chapterName}"/></td>
		<td><c:out value="${affiliationSummary.moniker}"/></td>
		<td><c:out value="${affiliationSummary.rankName}"/></td>
		<td><c:out value="${affiliationSummary.activityLevelName}"/></td>
		<td><c:out value="${affiliationSummary.cityName}"/></td>
		<td><c:out value="${affiliationSummary.stateName}"/></td>
		<td><fmt:formatDate value="${affiliationSummary.startDate}" pattern="MM/dd/yyyy"/></td>
		<td><fmt:formatDate value="${affiliationSummary.endDate}" pattern="MM/dd/yyyy"/></td>
	</tr>
</c:forEach>
