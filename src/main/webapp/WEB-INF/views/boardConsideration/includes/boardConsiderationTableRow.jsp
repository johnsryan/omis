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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.boardconsideration.msgs.boardConsideration">
	<tr id="boardConsiderationItemRow${boardConsiderationIndex}" class="boardConsiderationItemRow">
		<td>
			<a class="removeLink" id="removeBoardConsiderationLink${boardConsiderationIndex}" href="${pageContext.request.contextPath}/boardConsideration/removeBoardConsideration.html"><span class="linkLabel"><fmt:message key="removeLink" bundle="${commonBundle}"/></span></a>
			<input type="hidden" id="boardConsiderationId${boardConsiderationIndex}" name="boardConsiderationItems[${boardConsiderationIndex}].boardConsideration" value="${boardConsiderationItem.boardConsideration.id}"/>
			<form:errors path="boardConsiderationItems[${boardConsiderationIndex}].boardConsideration" cssClass="error"/>
			<input type="hidden" id="boardConsiderationOperation${boardConsiderationIndex}" name="boardConsiderationItems[${boardConsiderationIndex}].operation" value="${operation}"/>
			<form:errors path="boardConsiderationItems[${boardConsiderationIndex}].operation" cssClass="error"/>
		</td>
		<td>
			<input type="checkbox" name="boardConsiderationItems[${boardConsiderationIndex}].accepted" id="boardConsiderationItems[${boardConsiderationIndex}].accepted" ${boardConsiderationItem.accepted ? 'checked="checked"' : ''} />
			<form:errors path="boardConsiderationItems[${boardConsiderationIndex}].accepted" cssClass="error"/>
		</td>
		<td>
			<input type="text" name="boardConsiderationItems[${boardConsiderationIndex}].title" id="boardConsiderationItems[${boardConsiderationIndex}].title" value="${boardConsiderationItem.title}"/>
			<form:errors path="boardConsiderationItems[${boardConsiderationIndex}].title" cssClass="error"/>
		</td>
		<td>
			<select name="boardConsiderationItems[${boardConsiderationIndex}].category" id="boardConsiderationItems[${boardConsiderationIndex}].category">
				<option value=""><fmt:message key="nullLabel" bundle="${commonBundle}"/></option>
				<c:forEach var="category" items="${boardConsiderationCategories}">
					<option value="${category}" ${category == boardConsiderationItem.category ? 'selected="selected"' : ''}>
						<fmt:message key="boardConsiderationCategoryLabel.${category}"/>
					</option>
				</c:forEach>
			</select>
			<form:errors path="boardConsiderationItems[${boardConsiderationIndex}].category" cssClass="error"/>
		</td>
		<td>
			<textarea rows="4" name="boardConsiderationItems[${boardConsiderationIndex}].description" id="boardConsiderationItems[${boardConsiderationIndex}].description" style="width: 95%"><c:out value="${boardConsiderationItem.description}"/></textarea>
			<form:errors path="boardConsiderationItems[${boardConsiderationIndex}].description" cssClass="error"/>
		</td> 
	</tr>
</fmt:bundle> 