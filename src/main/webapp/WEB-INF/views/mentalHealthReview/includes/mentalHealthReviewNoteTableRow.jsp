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
 - Table row for mental health review notes.
 -
 - Author: Josh Divine
 - Version: 0.1.0 (Feb 5, 2018)
 - Since: OMIS 3.0
 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.mentalhealthreview.msgs.mentalHealthReview">
	<tr id="mentalHealthNoteItemRow${mentalHealthReviewNoteItemIndex}" class="mentalHealthNoteItemRow">
		<td>
			<a class="removeLink" id="removeMentalHealthReviewNoteLink${mentalHealthReviewNoteItemIndex}" href="${pageContext.request.contextPath}/mentalHealthReview/removeMentalHealthReviewNote.html?mentalHealthReview=${mentalHealthReview.id}"><span class="linkLabel"><fmt:message key="removeLink" bundle="${commonBundle}"/></span></a>
			<input type="hidden" id="mentalHealthNoteId${mentalHealthReviewNoteItemIndex}" name="mentalHealthNoteItems[${mentalHealthReviewNoteItemIndex}].mentalHealthNote" value="${mentalHealthNoteItem.mentalHealthNote.id}"/>
			<form:errors path="mentalHealthNoteItems[${mentalHealthReviewNoteItemIndex}].mentalHealthNote" cssClass="error"/>
			<input type="hidden" id="mentalHealthNoteOperation${mentalHealthReviewNoteItemIndex}" name="mentalHealthNoteItems[${mentalHealthReviewNoteItemIndex}].itemOperation" value="${mentalHealthNoteItem.itemOperation}"/>
			<form:errors path="mentalHealthNoteItems[${mentalHealthReviewNoteItemIndex}].itemOperation" cssClass="error"/>
		</td>		
		<td>
			<fmt:formatDate var="mentalHealthNoteDate" value="${mentalHealthNoteItem.date}" pattern="MM/dd/yyyy"/>
			<input type="text" class="noteDate" name="mentalHealthNoteItems[${mentalHealthReviewNoteItemIndex}].date" id="mentalHealthNoteItemDate${mentalHealthReviewNoteItemIndex}" value="${mentalHealthNoteDate}"/>
			<form:errors path="mentalHealthNoteItems[${mentalHealthReviewNoteItemIndex}].date" cssClass="error"/>
		</td>
		<td>
			<textarea rows="4" name="mentalHealthNoteItems[${mentalHealthReviewNoteItemIndex}].description" id="mentalHealthNoteItems[${mentalHealthReviewNoteItemIndex}].description"><c:out value="${mentalHealthNoteItem.description}"/></textarea>
			<form:errors path="mentalHealthNoteItems[${mentalHealthReviewNoteItemIndex}].description" cssClass="error"/>
		</td>
		<td>
			<c:if test="${not empty mentalHealthNoteItem.mentalHealthNote}">
				<c:set var="updatable" value="${mentalHealthNoteItem.mentalHealthNote}" scope="request"/>
				<jsp:include page="/WEB-INF/views/audit/includes/lastUpdatedBy.jsp"/>
			</c:if>
		</td>
	</tr>
</fmt:bundle> 