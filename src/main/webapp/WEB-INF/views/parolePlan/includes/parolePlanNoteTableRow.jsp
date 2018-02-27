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
 - Version: 0.1.0 (Feb 13, 2018)
 - Since: OMIS 3.0
 --%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<fmt:bundle basename="omis.paroleplan.msgs.parolePlan">
<tr id="parolePlanNoteRow${parolePlanNoteIndex}">
	<td>
		<a href="${pageContext.request.contextPath}/parolePlan/removeParolePlanNote.html" id="removeNoteLink${parolePlanNoteIndex}" class="removeLink"><span class="linkLabel"><fmt:message key="deleteParolePlanNoteLink"/></span></a>
		<input type="hidden" id="parolePlanNoteId${parolePlanNoteIndex}" name="parolePlanNoteItems[${parolePlanNoteIndex}].parolePlanNote" value="${parolePlanForm.parolePlanNoteItems[parolePlanNoteIndex].parolePlanNote.id}"/>
		<form:errors path="parolePlanNoteItems[${parolePlanNoteIndex}].parolePlanNote" cssClass="error"/>
		<input type="hidden" id="parolePlanNoteOperation${parolePlanNoteIndex}" name="parolePlanNoteItems[${parolePlanNoteIndex}].operation" value="${operation}"/>
		<form:errors path="parolePlanNoteItems[${parolePlanNoteIndex}].operation" cssClass="error"/>
	</td>
	<td>
		<input name="parolePlanNoteItems[${parolePlanNoteIndex}].date" id="parolePlanNoteDate${parolePlanNoteIndex}" type="text" class="date" value="<fmt:formatDate pattern='MM/dd/yyyy' value='${parolePlanForm.parolePlanNoteItems[parolePlanNoteIndex].date}'/>"/>
		<form:errors path="parolePlanNoteItems[${parolePlanNoteIndex}].date" cssClass="error"/>
	</td>
	<td>
		<textarea rows="4" name="parolePlanNoteItems[${parolePlanNoteIndex}].value" id="parolePlanNoteValue${parolePlanNoteIndex}" style="width: 95%"><c:out value="${parolePlanForm.parolePlanNoteItems[parolePlanNoteIndex].value}"/></textarea>
		<form:errors path="parolePlanNoteItems[${parolePlanNoteIndex}].value" cssClass="error"/>
	</td>
	<td>
		<c:if test="${not empty parolePlanForm.parolePlanNoteItems[parolePlanNoteIndex].parolePlanNote}">
			<c:set var="updatable" value="${parolePlanForm.parolePlanNoteItems[parolePlanNoteIndex].parolePlanNote}" scope="request"/>
			<jsp:include page="/WEB-INF/views/audit/includes/lastUpdatedBy.jsp"/>
		</c:if>
	</td>
</tr>
</fmt:bundle>