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
 - Version: 0.1.0 (Feb 20, 2018)
 - Since: OMIS 3.0
 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:bundle basename="omis.hearinganalysis.msgs.hearingAnalysis">
<table class="editTable">
	<thead>
		<tr>
			<th class="operations"><a class="actionMenuItem" id="hearingAnalysisNotesActionMenuLink" href="${pageContext.request.contextPath}/hearingAnalysis/hearingAnalysisNotesActionMenu.html?hearingAnalysisNoteIndex=${hearingAnalysisNoteIndex}"></a></th>
			<th><label><fmt:message key="hearingAnalysisNoteDateLabel"/></label></th>
			<th><label><fmt:message key="hearingAnalysisNoteValueLabel"/></label>
			<th><label><fmt:message key="lastUpdatedByLabel"/></label></th>
		</tr>
	</thead>
	<tbody id="hearingAnalysisNotes">
	<c:forEach var="hearingAnalysisNote" items="${hearingAnalysisNoteItems}" varStatus="status">
		<c:set var="hearingAnalysisNote" value="${hearingAnalysisNote}" scope="request"/>
		<c:set var="hearingAnalysisNoteIndex" value="${status.index}" scope="request"/>
		<c:set var="operation" value="${hearingAnalysisNote.operation}" scope="request"/>
		<c:if test="${not empty hearingAnalysisNote.operation}">
			<jsp:include page="hearingAnalysisNoteTableRow.jsp"/>
		</c:if>
	</c:forEach>
	</tbody>
</table>
</fmt:bundle>