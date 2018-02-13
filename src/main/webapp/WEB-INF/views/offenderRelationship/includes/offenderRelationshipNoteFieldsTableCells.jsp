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
  - Table cells of offender relationship note fields. Assumes cells are
  - presented in table row (required to be valid HTML).
  -
  - Expects the following attributes:
  -  - offenderRelationshipNoteFieldsPropertyName - property name, if not
  -     supplied default of offenderRelationshipNoteFields is used
  -  - offenderRelationshipNoteFields - fields
  -  - offenderRelationshipNoteCategories - note categories
  -
  - Author: Stephen Abson
  --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<fmt:setBundle var="commonBundle" basename="omis.msgs.common"/>
<c:if test="${empty offenderRelationshipNoteFieldsPropertyName}">
	<c:set var="offenderRelationshipNoteFieldsPropertyName" value="offenderRelationshipNoteFields"/>
</c:if>
<td>
	<select name="${offenderRelationshipNoteFieldsPropertyName}.category" id="${offenderRelationshipNoteFieldsPropertyName}.category">
		<option value=""><fmt:message key="nullLabel" bundle="${commonBundle}"/></option>
		<c:forEach var="offenderRelationshipNoteCategory" items="${offenderRelationshipNoteCategories}">
			<c:choose>
				<c:when test="${offenderRelationshipNoteFields.category eq offenderRelationshipNoteCategory}">
					<option value="${offenderRelationshipNoteCategory.id}" selected="selected"><c:out value="${offenderRelationshipNoteCategory.name}"/></option>
				</c:when>
				<c:otherwise>
					<option value="${offenderRelationshipNoteCategory.id}"><c:out value="${offenderRelationshipNoteCategory.name}"/></option>
				</c:otherwise>
			</c:choose>
		</c:forEach>
	</select>
	<form:errors path="${offenderRelationshipNoteFieldsPropertyName}.category" cssClass="error"/>
</td>
<td>
	<input id="${offenderRelationshipNoteFieldsPropertyName}.date" name="${offenderRelationshipNoteFieldsPropertyName}.date" class="date" value="<fmt:formatDate value='${offenderRelationshipNoteFields.date}' pattern='MM/dd/yyyy'/>"/>
	<form:errors path="${offenderRelationshipNoteFieldsPropertyName}.date" cssClass="error"/>
</td>
<td>
	<textarea id="${offenderRelationshipNoteFieldsPropertyName}.value" name="${offenderRelationshipNoteFieldsPropertyName}.value"><c:out value="${offenderRelationshipNoteFields.value}"/></textarea>
	<form:errors path="${offenderRelationshipNoteFieldsPropertyName}.value" cssClass="error"/>
</td>