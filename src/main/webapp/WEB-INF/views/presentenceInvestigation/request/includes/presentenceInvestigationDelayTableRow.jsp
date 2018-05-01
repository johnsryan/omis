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
  - Table row to edit presentence investigation delays.
  -
  - Author: Josh Divine
  - Version: 0.1.0 (Apr 24, 2018)
  - Since: OMIS 3.0
  --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.presentenceinvestigation.msgs.presentenceInvestigationRequest">
	<tr id="presentenceInvestigationDelayItemRow${presentenceInvestigationDelayItemIndex}" class="presentenceInvestigationDelayItemRow">
		<td>
			<a class="removeLink" id="removePresentenceInvestigationDelayLink${presentenceInvestigationDelayItemIndex}" href="${pageContext.request.contextPath}/presentenceInvestigationRequest/removePresentenceInvestigationDelay.html?presentenceInvestigationRequest=${presentenceInvestigationRequest.id}"><span class="linkLabel"><fmt:message key="removeLink" bundle="${commonBundle}"/></span></a>
			<input type="hidden" id="presentenceInvestigationDelayId${presentenceInvestigationDelayItemIndex}" name="presentenceInvestigationDelayItems[${presentenceInvestigationDelayItemIndex}].presentenceInvestigationDelay" value="${presentenceInvestigationDelayItem.presentenceInvestigationDelay.id}"/>
			<form:errors path="presentenceInvestigationDelayItems[${presentenceInvestigationDelayItemIndex}].presentenceInvestigationDelay" cssClass="error"/>
			<input type="hidden" id="presentenceInvestigationDelayOperation${presentenceInvestigationDelayItemIndex}" name="presentenceInvestigationDelayItems[${presentenceInvestigationDelayItemIndex}].itemOperation" value="${presentenceInvestigationDelayItem.itemOperation}"/>
			<form:errors path="presentenceInvestigationDelayItems[${presentenceInvestigationDelayItemIndex}].itemOperation" cssClass="error"/>
		</td>
		<td>
			<fmt:formatDate var="presentenceInvestigationDelayDate" value="${presentenceInvestigationDelayItem.date}" pattern="MM/dd/yyyy"/>
			<input type="text" class="date" name="presentenceInvestigationDelayItems[${presentenceInvestigationDelayItemIndex}].date" id="presentenceInvestigationDelayItemDate${presentenceInvestigationDelayItemIndex}" value="${presentenceInvestigationDelayDate}"/>
			<form:errors path="presentenceInvestigationDelayItems[${presentenceInvestigationDelayItemIndex}].date" cssClass="error"/>
		</td>
		<td>
			<select name="presentenceInvestigationDelayItems[${presentenceInvestigationDelayItemIndex}].reason" id="presentenceInvestigationDelayItems[${presentenceInvestigationDelayItemIndex}].reason">
				<jsp:include page="/WEB-INF/views/includes/nullOption.jsp"/>
				<c:forEach items="${delayCategories}" var="reason">
					<option value="${reason.id}" ${reason == presentenceInvestigationDelayItems[presentenceInvestigationDelayItemIndex].reason ? 'selected="selected"' : ''}>
						<c:out value="${reason.name}"/>
					</option>
				</c:forEach>
			</select>
			<form:errors path="presentenceInvestigationDelayItems[${presentenceInvestigationDelayItemIndex}].reason" cssClass="error"/>
		</td>
	</tr>
</fmt:bundle> 