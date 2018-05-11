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
 - Table for STG affiliations.
 -
 - Author: Stephen Abson
 - Author: Annie Wahl
 - Author: Josh Divine
 --%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setBundle basename="omis.region.msgs.region" var="regionBundle"/>
<fmt:setBundle basename="omis.region.msgs.state" var="stateBundle"/>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.stg.msgs.stg">
<table class="listTable">
	<thead>
		<tr>
			<th class="operations"/>
			<th><fmt:message key="securityThreatGroupLabel"/></th>
			<th><fmt:message key="stgChapterLabel"/></th>
			<th><fmt:message key="stgMonikerLabel"/></th>
			<th><fmt:message key="stgRankLabel"/></th>
			<th><fmt:message key="stgActivityLevelLabel"/></th>
			<th><fmt:message key="cityLabel" bundle="${regionBundle}"/></th>
			<th><fmt:message key="stateLabel" bundle="${stateBundle}"/></th>
			<th class="date"><fmt:message key="startDateLabel" bundle="${commonBundle}"/></th>
			<th class="date"><fmt:message key="endDateLabel" bundle="${commonBundle}"/></th>
		</tr>
	</thead>
	<tbody id="affiliations">
		<jsp:include page="listTableBodyContent.jsp"/>
	</tbody>
</table>
</fmt:bundle>