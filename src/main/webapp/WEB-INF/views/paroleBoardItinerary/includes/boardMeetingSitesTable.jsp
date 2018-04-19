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
 - Version: 0.1.1 (Apr 11, 2018)
 - Since: OMIS 3.0
 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:bundle basename="omis.paroleboarditinerary.msgs.paroleBoardItinerary">
<table class="editTable">
	<thead>
		<tr>
			<th class="operations"><a class="actionMenuItem" id="boardMeetingSitesActionMenuLink" href="${pageContext.request.contextPath}/paroleBoardItinerary/boardMeetingSitesActionMenu.html?boardMeetingSiteIndex=${boardMeetingSiteIndex}"></a></th>
			<th><label><fmt:message key="boardMeetingSiteFacilityLabel"/></label></th>
			<th><label><fmt:message key="boardMeetingSiteUnitLabel"/></label></th>
			<th><label><fmt:message key="boardMeetingSiteOrderLabel"/></label>
			<th><label><fmt:message key="boardMeetingSiteDateLabel"/></label></th>
			<th><label><fmt:message key="lastUpdatedByLabel"/></label></th>
		</tr>
	</thead>
	<tbody id="boardMeetingSites">
	<c:forEach var="boardMeetingSite" items="${paroleBoardItineraryForm.boardMeetingSiteItems}" varStatus="status">
		<c:set var="boardMeetingSiteIndex" value="${status.index}" scope="request"/>
		<c:set var="operation" value="${boardMeetingSite.operation}" scope="request"/>
		<c:if test="${not empty boardMeetingSite.operation}">
			<jsp:include page="boardMeetingSiteTableRow.jsp"/>
		</c:if>
	</c:forEach>
	</tbody>
</table>
</fmt:bundle>