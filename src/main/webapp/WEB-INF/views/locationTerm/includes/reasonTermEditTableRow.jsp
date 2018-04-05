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
  - Edit table row for reason terms.
  -
  - Author: Stephen Abson
  --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<fmt:setBundle var="commonBundle" basename="omis.msgs.common"/>
<c:choose>
	<c:when test="${locationReasonTermItem.operation.name eq 'REMOVE'}">
		<c:set var="reasonTermItemRowClass" value="removeRow"/>
	</c:when>
	<c:otherwise>
		<c:set var="reasonTermItemRowClass" value=""/>
	</c:otherwise>
</c:choose>
<tr id="reasonTermItems[${locationReasonTermItemIndex}].row" class="${reasonTermItemRowClass}">
	<td>
		<a href="${pageContext.request.contextPath}/locationTerm/reasonTerm/remove.html?itemIndex=${locationReasonTermItemIndex}" class="removeLink" id="reasonTermItems[${locationReasonTermItemIndex}].removeLink"></a>
		<input type="hidden" id="reasonTermItems[${locationReasonTermItemIndex}].operation" name="reasonTermItems[${locationReasonTermItemIndex}].operation" value="${locationReasonTermItem.operation.name}"/>
		<input type="hidden" id="reasonTermItems[${locationReasonTermItemIndex}].reasonTerm" name="reasonTermItems[${locationReasonTermItemIndex}].reasonTerm" value="${locationReasonTermItem.reasonTerm.id}"/>
	</td>
	<td>
		<select name="reasonTermItems[${locationReasonTermItemIndex}].reason">
			<option value=""><fmt:message key="nullLabel" bundle="${commonBundle}"/></option>
			<c:forEach var="locationReason" items="${reasons}">
				<c:choose>
					<c:when test="${locationReasonTermItem.reason eq locationReason}">
						<option value="${locationReason.id}" selected="selected"><c:out value="${locationReason.name}"/></option>
					</c:when>
					<c:otherwise>
						<option value="${locationReason.id}"><c:out value="${locationReason.name}"/></option>
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</select>
		<form:errors path="reasonTermItems[${locationReasonTermItemIndex}].reason" cssClass="error"/>
	</td>
	<td>
		<input type="text" class="date" id="reasonTermItems[${locationReasonTermItemIndex}].startDate" name="reasonTermItems[${locationReasonTermItemIndex}].startDate" value="<fmt:formatDate pattern='MM/dd/yyyy' value='${locationReasonTermItem.startDate}'/>"/>
		<form:errors path="reasonTermItems[${locationReasonTermItemIndex}].startDate" cssClass="error"/>
	</td>
	<td>
		<input type="text" class="date" id="reasonTermItems[${locationReasonTermItemIndex}].startTime" name="reasonTermItems[${locationReasonTermItemIndex}].startTime" value="<fmt:formatDate pattern='h:mm a' value='${locationReasonTermItem.startTime}'/>"/>
		<form:errors path="reasonTermItems[${locationReasonTermItemIndex}].startTime" cssClass="error"/>
	</td>
	<td>
		<input type="text" class="date" id="reasonTermItems[${locationReasonTermItemIndex}].endDate" name="reasonTermItems[${locationReasonTermItemIndex}].endDate" value="<fmt:formatDate pattern='MM/dd/yyyy' value='${locationReasonTermItem.endDate}'/>"/>
		<form:errors path="reasonTermItems[${locationReasonTermItemIndex}].endDate" cssClass="error"/>
	</td>
	<td>
		<input type="text" class="date" id="reasonTermItems[${locationReasonTermItemIndex}].endTime" name="reasonTermItems[${locationReasonTermItemIndex}].endTime" value="<fmt:formatDate pattern='h:mm a' value='${locationReasonTermItem.endTime}'/>"/>
		<form:errors path="reasonTermItems[${locationReasonTermItemIndex}].endTime" cssClass="error"/>
	</td>
</tr>