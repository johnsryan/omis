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
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:bundle basename="omis.boardconsideration.msgs.boardConsideration">
<table id="boardConsiderationTable" class="editTable">
	<thead>
		<tr>
			<th class="operations"><a class="actionMenuItem" id="boardConsiderationItemsActionMenuLink" href="${pageContext.request.contextPath}/boardConsideration/boardConsiderationsActionMenu.html?boardConsiderationIndex=${boardConsiderationIndex}&hearingAnalysis=${hearingAnalysis.id}"></a></th>
			<th><fmt:message key="acceptedLabel"/></th>
			<th><fmt:message key="considerationLabel"/></th>
			<th><fmt:message key="riskStrengthLabel"/></th>
			<th><fmt:message key="descriptionLabel"/></th>
		</tr>
	</thead>
	<tbody id="boardConsiderationTableBody">
		<jsp:include page="boardConsiderationTableBodyContent.jsp"/>
	</tbody>
</table>
</fmt:bundle>