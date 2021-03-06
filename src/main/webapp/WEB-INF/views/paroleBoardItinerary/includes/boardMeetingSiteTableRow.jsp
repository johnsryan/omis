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
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<fmt:bundle basename="omis.paroleboarditinerary.msgs.paroleBoardItinerary">
<tr id="boardMeetingSiteRow${boardMeetingSiteIndex}">
	<td>
		<a href="${pageContext.request.contextPath}/paroleBoardItinerary/removeBoardMeetingSite.html" id="removeLink${boardMeetingSiteIndex}" class="removeLink"><span class="linkLabel"><fmt:message key="deleteBoardMeetingSiteLink"/></span></a>
		<input type="hidden" id="boardMeetingSiteId${boardMeetingSiteIndex}" name="boardMeetingSiteItems[${boardMeetingSiteIndex}].boardMeetingSite" value="${paroleBoardItineraryForm.boardMeetingSiteItems[boardMeetingSiteIndex].boardMeetingSite.id}"/>
		<form:errors path="boardMeetingSiteItems[${boardMeetingSiteIndex}].boardMeetingSite" cssClass="error"/>
		<input type="hidden" id="boardMeetingSiteOperation${boardMeetingSiteIndex}" name="boardMeetingSiteItems[${boardMeetingSiteIndex}].operation" value="${operation}"/>
		<form:errors path="boardMeetingSiteItems[${boardMeetingSiteIndex}].operation" cssClass="error"/>
	</td>
	<td>
		<select name="boardMeetingSiteItems[${boardMeetingSiteIndex}].location" id="boardMeetingSiteItems${boardMeetingSiteIndex}Location">
			<option value="">...</option>
			<c:forEach var="location" items="${boardMeetingSites}">
			<c:choose>
				<c:when test="${not empty paroleBoardItineraryForm.boardMeetingSiteItems[boardMeetingSiteIndex].location and paroleBoardItineraryForm.boardMeetingSiteItems[boardMeetingSiteIndex].location eq location}">
					<option value="${location.id}" selected="selected" class="large"><c:out value="${location.organization.name}"/></option>
				</c:when>
				<c:otherwise>
					<option value="${location.id}" class="large"><c:out value="${location.organization.name}"/></option>
				</c:otherwise>
			</c:choose>
			</c:forEach>
		</select>
		<form:errors path="boardMeetingSiteItems[${boardMeetingSiteIndex}].location" cssClass="error"/>
	</td>
	<td>
		<select name="boardMeetingSiteItems[${boardMeetingSiteIndex}].unit" id="boardMeetingSiteItems${boardMeetingSiteIndex}Unit">
			<c:if test="${not empty boardMeetingSiteUnits}">
				<c:set var="units" value="${boardMeetingSiteUnits[paroleBoardItineraryForm.boardMeetingSiteItems[boardMeetingSiteIndex]]}" scope="request"/>
				<c:set var="boardMeetingSiteUnit" value="${paroleBoardItineraryForm.boardMeetingSiteItems[boardMeetingSiteIndex].unit}" scope="request"/>
				<jsp:include page="/WEB-INF/views/paroleBoardItinerary/includes/unitOptions.jsp"/>
			</c:if>
		</select>
		<form:errors path="boardMeetingSiteItems[${boardMeetingSiteIndex}].unit" cssClass="error"/>
	</td>
	<td>
		<c:choose>
			<c:when test="${not empty paroleBoardItineraryForm.boardMeetingSiteItems[boardMeetingSiteIndex].order}">
				<input name="boardMeetingSiteItems[${boardMeetingSiteIndex}].order" type="text" class="veryShortNumber" value="${paroleBoardItineraryForm.boardMeetingSiteItems[boardMeetingSiteIndex].order}"/>
			</c:when>
			<c:otherwise>
				<input name="boardMeetingSiteItems[${boardMeetingSiteIndex}].order" type="text" class="veryShortNumber" value="${sortOrder}"/>
			</c:otherwise>
		</c:choose>
		
		<form:errors path="boardMeetingSiteItems[${boardMeetingSiteIndex}].order" cssClass="error"/>
	</td>
	<td>
		<input name="boardMeetingSiteItems[${boardMeetingSiteIndex}].date" id="boardMeetingSite${boardMeetingSiteIndex}Date" type="text" class="date" value="<fmt:formatDate pattern='MM/dd/yyyy' value='${paroleBoardItineraryForm.boardMeetingSiteItems[boardMeetingSiteIndex].date}'/>"/>
		<form:errors path="boardMeetingSiteItems[${boardMeetingSiteIndex}].date" cssClass="error"/>
	</td>
	<td>
		<c:if test="${not empty paroleBoardItineraryForm.boardMeetingSiteItems[boardMeetingSiteIndex].boardMeetingSite}">
			<c:set var="updatable" value="${paroleBoardItineraryForm.boardMeetingSiteItems[boardMeetingSiteIndex].boardMeetingSite}" scope="request"/>
			<jsp:include page="/WEB-INF/views/audit/includes/lastUpdatedBy.jsp"/>
		</c:if>
	</td>
</tr>
</fmt:bundle>