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
 - Version: 0.1.0 (Feb 12, 2018)
 - Since: OMIS 3.0
 --%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<fmt:bundle basename="omis.parolereview.msgs.paroleReview">
<tr id="paroleReviewNoteRow${paroleReviewNoteIndex}">
	<td>
		<a href="${pageContext.request.contextPath}/paroleReview/removeParoleReviewNote.html" id="removeNoteLink${paroleReviewNoteIndex}" class="removeLink"><span class="linkLabel"><fmt:message key="deleteParoleReviewNoteLink"/></span></a>
		<input type="hidden" id="paroleReviewNoteId${paroleReviewNoteIndex}" name="paroleReviewNoteItems[${paroleReviewNoteIndex}].paroleReviewNote" value="${paroleReviewForm.paroleReviewNoteItems[paroleReviewNoteIndex].paroleReviewNote.id}"/>
		<form:errors path="paroleReviewNoteItems[${paroleReviewNoteIndex}].paroleReviewNote" cssClass="error"/>
		<input type="hidden" id="paroleReviewNoteOperation${paroleReviewNoteIndex}" name="paroleReviewNoteItems[${paroleReviewNoteIndex}].operation" value="${operation}"/>
		<form:errors path="paroleReviewNoteItems[${paroleReviewNoteIndex}].operation" cssClass="error"/>
	</td>
	<td>
		<input name="paroleReviewNoteItems[${paroleReviewNoteIndex}].date" id="paroleReviewNoteDate${paroleReviewNoteIndex}" type="text" class="date" value="<fmt:formatDate pattern='MM/dd/yyyy' value='${paroleReviewForm.paroleReviewNoteItems[paroleReviewNoteIndex].date}'/>"/>
		<form:errors path="paroleReviewNoteItems[${paroleReviewNoteIndex}].date" cssClass="error"/>
	</td>
	<td>
		<textarea rows="4" name="paroleReviewNoteItems[${paroleReviewNoteIndex}].value" id="paroleReviewNoteValue${paroleReviewNoteIndex}" style="width: 95%"><c:out value="${paroleReviewForm.paroleReviewNoteItems[paroleReviewNoteIndex].value}"/></textarea>
		<form:errors path="paroleReviewNoteItems[${paroleReviewNoteIndex}].value" cssClass="error"/>
	</td>
	<td>
		<c:if test="${not empty paroleReviewForm.paroleReviewNoteItems[paroleReviewNoteIndex].paroleReviewNote}">
			<c:set var="updatable" value="${paroleReviewForm.paroleReviewNoteItems[paroleReviewNoteIndex].paroleReviewNote}" scope="request"/>
			<jsp:include page="/WEB-INF/views/audit/includes/lastUpdatedBy.jsp"/>
		</c:if>
	</td>
</tr>
</fmt:bundle>