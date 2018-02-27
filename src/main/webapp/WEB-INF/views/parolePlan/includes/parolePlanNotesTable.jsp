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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:bundle basename="omis.paroleplan.msgs.parolePlan">
<table class="editTable">
	<thead>
		<tr>
			<th class="operations"><a class="actionMenuItem" id="parolePlanNotesActionMenuLink" href="${pageContext.request.contextPath}/parolePlan/parolePlanNotesActionMenu.html?parolePlanNoteIndex=${parolePlanNoteIndex}"></a></th>
			<th><label><fmt:message key="parolePlanNoteDateLabel"/></label></th>
			<th><label><fmt:message key="parolePlanNoteValueLabel"/></label>
			<th><label><fmt:message key="lastUpdatedByLabel"/></label></th>
		</tr>
	</thead>
	<tbody id="parolePlanNotes">
	<c:forEach var="parolePlanNote" items="${parolePlanForm.parolePlanNoteItems}" varStatus="status">
		<c:set var="parolePlanNoteIndex" value="${status.index}" scope="request"/>
		<c:set var="operation" value="${parolePlanNote.operation}" scope="request"/>
		<c:if test="${not empty parolePlanNote.operation}">
			<jsp:include page="parolePlanNoteTableRow.jsp"/>
		</c:if>
	</c:forEach>
	</tbody>
</table>
</fmt:bundle>