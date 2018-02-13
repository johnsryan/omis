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
  - Action menu for note items on update relationship screen.
  -
  - Author: Stephen Abson
  --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle var="editOffenderRelationshipBundle" basename="omis.offenderrelationship.msgs.editOffenderRelationship"/>
<ul>
	<sec:authorize access="hasRole('OFFENDER_RELATIONSHIP_NOTE_CREATE') or hasRole('ADMIN')">
		<li><a href="${pageContext.request.contextPath}/offenderRelationship/update/createRelationshipNoteItem.html" class="createLink" id="createRelationshipNoteItemLink"><fmt:message key="createOffenderRelationshipNoteLink" bundle="${editOffenderRelationshipBundle}"/></a></li>
	</sec:authorize>
</ul>