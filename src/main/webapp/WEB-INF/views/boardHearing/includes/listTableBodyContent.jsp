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
 - Author: Annie Wahl
 - Author: Josh Divine
 - Version: 0.1.1 (Apr 18, 2018)
 - Since: OMIS 3.0
 --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.hearing.msgs.hearing">
<c:forEach var="summary" items="${boardHearingSummaries}">
<tr>
	<td><a class="actionMenuItem rowActionMenuItem" href="${pageContext.request.contextPath}/boardHearing/boardHearingsRowActionMenu.html?boardHearing=${summary.boardHearingId}"></a></td>
	<td>
		<fmt:formatDate value="${summary.hearingDate}" pattern="MM/dd/yyyy" />
	</td>
	<td>
		<c:out value="${summary.hearingStatusName}"/>
	</td>
	<td>
		<c:out value="${summary.appearanceCategoryName}"/>
	</td>
	<td>
		<c:if test="${not empty summary.hearingAnalystLastName}">
			<c:out value="${summary.hearingAnalystLastName}, ${summary.hearingAnalystFirstName} ${summary.hearingAnalystMiddleName}"/>
		</c:if>
	</td>
	<td>
		<c:out value="${summary.decisionName}"/>
	</td>
</tr>
</c:forEach>
</fmt:bundle>