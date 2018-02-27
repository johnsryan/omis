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
 - Version: 0.1.1 (Feb 20, 2018)
 - Since: OMIS 3.0
 --%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<fmt:bundle basename="omis.hearinganalysis.msgs.hearingAnalysis">
<tr id="hearingAnalysisNoteRow${hearingAnalysisNoteIndex}">
	<td>
		<a href="${pageContext.request.contextPath}/hearingAnalysis/removeHearingAnalysisNote.html" id="removeNoteLink${hearingAnalysisNoteIndex}" class="removeLink"><span class="linkLabel"><fmt:message key="deleteHearingAnalysisNoteLink"/></span></a>
		<input type="hidden" id="hearingAnalysisNoteId${hearingAnalysisNoteIndex}" name="hearingAnalysisNoteItems[${hearingAnalysisNoteIndex}].hearingAnalysisNote" value="${hearingAnalysisNote.hearingAnalysisNote.id}"/>
		<form:errors path="hearingAnalysisNoteItems[${hearingAnalysisNoteIndex}].hearingAnalysisNote" cssClass="error"/>
		<input type="hidden" id="hearingAnalysisNoteOperation${hearingAnalysisNoteIndex}" name="hearingAnalysisNoteItems[${hearingAnalysisNoteIndex}].operation" value="${operation}"/>
		<form:errors path="hearingAnalysisNoteItems[${hearingAnalysisNoteIndex}].operation" cssClass="error"/>
	</td>
	<td>
		<input name="hearingAnalysisNoteItems[${hearingAnalysisNoteIndex}].date" id="hearingAnalysisNoteDate${hearingAnalysisNoteIndex}" type="text" class="date" value="<fmt:formatDate pattern='MM/dd/yyyy' value='${hearingAnalysisNote.date}'/>"/>
		<form:errors path="hearingAnalysisNoteItems[${hearingAnalysisNoteIndex}].date" cssClass="error"/>
	</td>
	<td>
		<textarea rows="4" name="hearingAnalysisNoteItems[${hearingAnalysisNoteIndex}].value" id="hearingAnalysisNoteValue${hearingAnalysisNoteIndex}" style="width: 95%"><c:out value="${hearingAnalysisNote.value}"/></textarea>
		<form:errors path="hearingAnalysisNoteItems[${hearingAnalysisNoteIndex}].value" cssClass="error"/>
	</td>
	<td>
		<c:if test="${not empty hearingAnalysisNote.hearingAnalysisNote}">
			<c:set var="updatable" value="${hearingAnalysisNote.hearingAnalysisNote}" scope="request"/>
			<jsp:include page="/WEB-INF/views/audit/includes/lastUpdatedBy.jsp"/>
		</c:if>
	</td>
</tr>
</fmt:bundle>