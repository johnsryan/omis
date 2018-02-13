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
  - Table row for offender relationship note items.
  -
  - Expects these attributes:
  -  - offenderRelationshipNoteItemIndex - item index
  -  - offenderRelationshipNoteItem - note item
  -  - offenderRelationshipNoteItemsFieldName - field name of offender
  -    relationship note items
  -  - offenderRelationshipNoteCategories - note categories
  -  - baseUrl - base URL for links
  -
  - Author: Stephen Abson
  --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<fmt:setBundle var="auditBundle" basename="omis.audit.msgs.audit"/>
<c:choose>
	<c:when test="${offenderRelationshipnoteItem.operation.name eq 'REMOVE'}">
		<c:set var="offenderRelationshipNoteItemRowClass" value="removeRow"/>
	</c:when>
	<c:otherwise>
		<c:set var="offenderRelationshipNoteItemRowClass" value=""/>
	</c:otherwise>
</c:choose>
<tr id="${offenderRelationshipNoteItemsFieldName}[${offenderRelationshipNoteItemIndex}].row" class="${offenderRelationshipNoteItemRowClass}">
	<td>
		<a id="${offenderRelationshipNoteItemsFieldName}[${offenderRelationshipNoteItemIndex}].removeLink" class="removeLink" href="${baseUrl}/removeNote.html?itemIndex=${offenderRelationshipNoteItemIndex}"></a>
		<input name="${offenderRelationshipNoteItemsFieldName}[${offenderRelationshipNoteItemIndex}].operation" id="${offenderRelationshipNoteItemsFieldName}[${offenderRelationshipNoteItemIndex}].operation" type="hidden" value="${offenderRelationshipNoteItem.operation.name}"/>
		<input name="${offenderRelationshipNoteItemsFieldName}[${offenderRelationshipNoteItemIndex}].note" type="hidden" value="${offenderRelationshipNoteItem.note.id}"/> 
	</td>
	<c:set var="offenderRelationshipNoteFieldsPropertyName" value="${offenderRelationshipNoteItemsFieldName}[${offenderRelationshipNoteItemIndex}].fields" scope="request"/>
	<c:set var="offenderRelationshipNoteFields" value="${offenderRelationshipNoteItem.fields}" scope="request"/>
	<jsp:include page="offenderRelationshipNoteFieldsTableCells.jsp"/>
	<td>
		<form:errors path="${offenderRelationshipNoteItemsFieldName}[${offenderRelationshipNoteItemIndex}].fields" cssClass="error"/>
		<c:if test="${not empty offenderRelationshipNoteItem.note}">
			<fmt:message key="updateUserAccountFormat" bundle="${auditBundle}">
				<fmt:param>${offenderRelationshipNoteItem.note.updateSignature.userAccount.user.name.lastName}</fmt:param>
				<fmt:param>${offenderRelationshipNoteItem.note.updateSignature.userAccount.user.name.firstName}</fmt:param>
				<fmt:param>${offenderRelationshipNoteItem.note.updateSignature.userAccount.username}</fmt:param>
			</fmt:message>
		</c:if>
	</td>
</tr>