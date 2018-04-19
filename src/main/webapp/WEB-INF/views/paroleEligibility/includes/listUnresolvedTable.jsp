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
 - Date: Apr 17, 2018
 - Since: OMIS 3.0
 --%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.paroleeligibility.msgs.paroleEligibility">
<table class="listTable">
	<thead>
		<tr>
			<th class="operations"/>
			<th><fmt:message key="paroleHearingEligibilityDateLabel"/></th>
			<th><fmt:message key="offenderLabel"/></th>
			<th><fmt:message key="statusDateLabel"/></th>
			<th><fmt:message key="appearanceCategoryLabel"/></th>
			<th><fmt:message key="eligibilityStatusLabel"/></th>
			<th><fmt:message key="hearingDateLabel"/></th>
		</tr>
	</thead>
	<tbody id="paroleEligibilities">
		<jsp:include page="listUnresolvedTableBodyContent.jsp"/>
	</tbody>
</table>
</fmt:bundle>