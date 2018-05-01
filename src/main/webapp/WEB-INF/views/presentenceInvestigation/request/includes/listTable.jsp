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
  - Author: Ryan Johns
  - Author: Annie Wahl
  - Author: Josh Divine
  - Version: 0.1.7 (Apr 24, 2018)
  - Since: OMIS 3.0 
  --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<fmt:bundle basename="omis.presentenceinvestigation.msgs.presentenceInvestigationRequest">
<table id="presentenceInvesigationRequestSummariesTable" class="listTable">
	<thead>
		<tr>
			<th class="operations"></th>
			<c:choose>
				<c:when test="${empty offender}">
					<th><fmt:message key="offenderLabel"/></th>
				</c:when>
				<c:when test="${not empty offender}">
					<th><fmt:message key="assignedUserLabel"/></th>
				</c:when>
			</c:choose>
			<th><fmt:message key="docketLabel"/></th>
			<th><fmt:message key="statusLabel"/></th>
			<th><fmt:message key="requestDateLabel"/></th>
			<th><fmt:message key="sentenceDateLabel"/></th>
			<th><fmt:message key="categoryLabel"/></th>
		</tr>
	</thead>
	<tbody>
		<jsp:include page="listTableBodyContent.jsp"/>
	</tbody>
</table>
</fmt:bundle>