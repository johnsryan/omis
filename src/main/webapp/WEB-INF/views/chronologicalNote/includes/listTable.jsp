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
 - Table of listed chronological notes 
 - Author: Yidong Li
 - Author: Sheronda Vaughn
 - Version: 0.1.1 (Feb 1, 2018)
 - Since: OMIS 3.0
 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:bundle basename="omis.chronologicalnote.msgs.chronologicalNote">
	<table id="chronologicalnote" class="listTable">
		<thead>
			<tr>
				<th class = "operations"> </th>
				<th class="date"><fmt:message key="titleLabel"/></th>
				<th><fmt:message key="startDateLabel"/></th>
				<th><fmt:message key="categoriesLabel"/></th>
				<th><fmt:message key="narrativeLabel"/></th>
				<th><fmt:message key="updatedByLabel"/></th>
			</tr>
		</thead>
		<tbody>
			<jsp:include page="listTableBodyContent.jsp"/>
		</tbody>
	</table>
</fmt:bundle>