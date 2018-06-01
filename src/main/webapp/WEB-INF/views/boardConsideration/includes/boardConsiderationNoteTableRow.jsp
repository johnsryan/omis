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
 - Version: 0.1.0 (May 31, 2018)
 - Since: OMIS 3.0
 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.boardconsideration.msgs.boardConsideration">
	<tr id="boardConsiderationNoteItemRow${boardConsiderationNoteIndex}" class="boardConsiderationNoteItemRow">
		<td>
			<a class="removeLink" id="removeBoardConsiderationNoteLink${boardConsiderationNoteIndex}" href="${pageContext.request.contextPath}/boardConsideration/removeBoardConsiderationNote.html"><span class="linkLabel"><fmt:message key="removeLink" bundle="${commonBundle}"/></span></a>
			<input type="hidden" id="boardConsiderationNoteId${boardConsiderationNoteIndex}" name="boardConsiderationNoteItems[${boardConsiderationNoteIndex}].boardConsiderationNote" value="${boardConsiderationNoteItem.boardConsiderationNote.id}"/>
			<form:errors path="boardConsiderationNoteItems[${boardConsiderationNoteIndex}].boardConsiderationNote" cssClass="error"/>
			<input type="hidden" id="boardConsiderationNoteOperation${boardConsiderationNoteIndex}" name="boardConsiderationNoteItems[${boardConsiderationNoteIndex}].operation" value="${operation}"/>
			<form:errors path="boardConsiderationNoteItems[${boardConsiderationNoteIndex}].operation" cssClass="error"/>
		</td>
		<td>
			<fmt:formatDate var="boardConsiderationNoteDate" value="${boardConsiderationNoteItem.date}" pattern="MM/dd/yyyy"/>
			<input type="text" class="date" name="boardConsiderationNoteItems[${boardConsiderationNoteIndex}].date" id="boardConsiderationNoteItemDate${boardConsiderationNoteIndex}" value="${boardConsiderationNoteDate}"/>
			<form:errors path="boardConsiderationNoteItems[${boardConsiderationNoteIndex}].date" cssClass="error"/>
		</td>
		<td>
			<textarea rows="4" name="boardConsiderationNoteItems[${boardConsiderationNoteIndex}].value" id="boardConsiderationNoteItems[${boardConsiderationNoteIndex}].value" style="width: 95%"><c:out value="${boardConsiderationNoteItem.value}"/></textarea>
			<form:errors path="boardConsiderationNoteItems[${boardConsiderationNoteIndex}].value" cssClass="error"/>
		</td> 
		<td>
			<c:if test="${not empty boardConsiderationNoteItem.boardConsiderationNote}">
				<c:set var="updatable" value="${boardConsiderationNoteItem.boardConsiderationNote}" scope="request"/>
				<jsp:include page="/WEB-INF/views/audit/includes/lastUpdatedBy.jsp"/>
			</c:if>
		</td>
	</tr>
</fmt:bundle> 