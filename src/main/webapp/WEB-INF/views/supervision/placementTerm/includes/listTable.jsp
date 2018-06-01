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
 - Table to list placement terms.
 -
 - Author: Stephen Abson
 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:bundle basename="omis.supervision.msgs.placementTerm">
<table class="listTable" id="placementTerms">
	<thead>
		<tr>
			<th/>
			<th hidden="hidden"><fmt:message key="supervisoryOrganizationLabel"/></th>
			<th><fmt:message key="correctionalStatusLabel"/></th>
			<th class="datetime"><fmt:message key="startDateLabel"/></th>
			<th class="datetime"><fmt:message key="endDateLabel"/></th>
			<th><fmt:message key="dayCountLabel"/>
			<th><fmt:message key="startChangeReasonLabel"/></th>
			<th hidden="hidden"><fmt:message key="endChangeReasonLabel"/></th>
			<th hidden="hidden"><fmt:message key="statusLabel"/></th>
			<th hidden="hidden" class="datetime"><fmt:message key="statusDateLabel"/></th>
			<th hidden="hidden" class="datetime"><fmt:message key="statusReturnedDateLabel"/></th>
			<th hidden="hidden"><fmt:message key="statusDayCountLabel"/>
		</tr>
	</thead>
	<tbody id="placementTerms">
		<jsp:include page="listTableBodyContent.jsp"/>
	</tbody>
</table>
</fmt:bundle>