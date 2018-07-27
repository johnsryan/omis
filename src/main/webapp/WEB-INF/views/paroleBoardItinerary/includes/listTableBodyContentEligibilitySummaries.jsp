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
 - Version: 0.1.0 (July 17, 2018)
 - Since: OMIS 3.0
 --%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<fmt:setBundle basename="omis.paroleeligibility.msgs.paroleEligibility" var="paroleEligibilityBundle"/>
<fmt:bundle basename="omis.paroleboarditinerary.msgs.paroleBoardItinerary">
<c:forEach var="paroleEligibility" items="${paroleEligibilitySummaries}" varStatus="status">
	<tr>
		<td>
			<a class="actionMenuItem" id="rowActionMenuLink${status.index}" href="${pageContext.request.contextPath}/paroleBoardItinerary/itineraryHearingsRowActionMenu.html?paroleBoardItinerary=${itinerarySummary.id}&eligibility=${paroleEligibility.paroleEligibilityId}"></a>
		</td>
		<td><fmt:formatDate value="${paroleEligibility.hearingEligibilityDate}" pattern="MM/dd/yyyy"/></td>
		<td><c:out value="${paroleEligibility.lastName}, ${paroleEligibility.firstName} ${paroleEligibility.middleName} #${paroleEligibility.offenderNumber}"/></td>
		<td><fmt:formatDate value="${paroleEligibility.statusDate}" pattern="MM/dd/yyyy"/></td>
		<td><c:out value="${paroleEligibility.appearanceCategoryName}"/></td>
		<td><c:if test="${not empty paroleEligibility.statusCategory}"><fmt:message key="paroleEligibilityStatusLabel.${paroleEligibility.statusCategory.name}" bundle="${paroleEligibilityBundle}"/></c:if></td>
		<td><fmt:formatDate value="${paroleEligibility.hearingDate}" pattern="MM/dd/yyyy"/></td>
	</tr>
</c:forEach>
</fmt:bundle>