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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.hearing.msgs.hearing">
	<tr id="userAttendanceItemRow${userAttendanceItemIndex}" class="userAttendanceItemRow">
		<td>
			<a class="removeLink" id="removeUserAttendanceLink${userAttendanceItemIndex}" href="${pageContext.request.contextPath}/hearing/removeUserAttendance.html?hearing=${hearing.id}"><span class="linkLabel"><fmt:message key="removeLink" bundle="${commonBundle}"/></span></a>
			<input type="hidden" id="userAttendanceId${userAttendanceItemIndex}" name="userAttendanceItems[${userAttendanceItemIndex}].userAttended" value="${userAttendanceItem.userAttended.id}"/>
			<form:errors path="userAttendanceItems[${userAttendanceItemIndex}].userAttended" cssClass="error"/>
			<input type="hidden" id="userAttendanceOperation${userAttendanceItemIndex}" name="userAttendanceItems[${userAttendanceItemIndex}].itemOperation" value="${userAttendanceItem.itemOperation}"/>
			<form:errors path="userAttendanceItems[${userAttendanceItemIndex}].itemOperation" cssClass="error"/>
		</td>
		<td>
			<input id="userAttendanceInput${userAttendanceItemIndex}"/>
				<input type="hidden" id="userAttendanceItems[${userAttendanceItemIndex}].userAccount" name="userAttendanceItems[${userAttendanceItemIndex}].userAccount" value="${userAttendanceItems[userAttendanceItemIndex].userAccount.id}"/>
				<a id="clearUserAttendance${userAttendanceItemIndex}" class="clearLink"></a>
			<span id="userAttendanceDisplay${userAttendanceItemIndex}">
				<c:if test="${not empty userAttendanceItems[userAttendanceItemIndex].userAccount}" >
					<c:out value="${userAttendanceItems[userAttendanceItemIndex].userAccount.user.name.lastName}, ${userAttendanceItems[userAttendanceItemIndex].userAccount.user.name.firstName} (${userAttendanceItems[userAttendanceItemIndex].userAccount.username})"/>
				</c:if>
			</span>
			<form:errors path="userAttendanceItems[${userAttendanceItemIndex}].userAccount" cssClass="error"/>
		</td>
	</tr>
</fmt:bundle>