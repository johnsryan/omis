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
  - Table of offender relationship note items.
  -
  - Expects the following attributes:
  -  - offenderRelationshipNoteItems - collection of note items
  -  - offenderRelationshipNoteItemsFieldName - field name of offender
  -    relationship note items
  -  - offenderRelationshipNoteCategories - note categories
  -  - baseUrl - base URL for links
  -
  - Author: Stephen Abson
  --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setBundle var="offenderRelationshipBundle" basename="omis.offenderrelationship.msgs.offenderRelationship"/>
<fmt:setBundle var="auditBundle" basename="omis.audit.msgs.audit"/>
<table class="editTable">
	<thead>
		<tr>
			<th class="operations"><a href="${baseUrl}/noteItemsActionMenu.html" class="actionMenuItem" id="noteItemsActionMenuLink"></a></th>
			<th><fmt:message key="offenderRelationshipNoteCategoryLabel" bundle="${offenderRelationshipBundle}"/></th>
			<th><fmt:message key="offenderRelationshipNoteDateLabel" bundle="${offenderRelationshipBundle}"/></th>
			<th><fmt:message key="offenderRelationshipNoteValueLabel" bundle="${offenderRelationshipBundle}"/></th>
			<th><fmt:message key="updateUserAccountLabel" bundle="${auditBundle}"/></th>
		</tr>
	</thead>
	<tbody id="relationshipNotesTableBody">
		<c:forEach var="offenderRelationshipNoteItem" items="${offenderRelationshipNoteItems}" varStatus="status">
			<c:if test="${not empty offenderRelationshipNoteItem.operation}">
				<c:set var="offenderRelationshipNoteItemIndex" value="${status.index}" scope="request"/>
				<c:set var="offenderRelationshipNoteItem" value="${offenderRelationshipNoteItem}" scope="request"/>
				<jsp:include page="offenderRelationshipNoteItemTableRow.jsp"/>
			</c:if>
		</c:forEach>
	</tbody>
</table>