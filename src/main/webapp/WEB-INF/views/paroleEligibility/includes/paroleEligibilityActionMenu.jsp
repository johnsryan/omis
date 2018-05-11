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
 - Author: Trevor Isles
 - Date: Jan 2, 2018
 - Since: OMIS 3.0
 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle" />
<fmt:bundle basename="omis.paroleeligibility.msgs.paroleEligibility">
<ul>
	<sec:authorize access="hasRole('PAROLE_ELIGIBILITY_LIST') or hasRole('ADMIN')">
		<li>
			<a class="listLink" href="${pageContext.request.contextPath}/paroleEligibility/list.html?offender=${offender.id}">
			<span class="visibleLinkLabel"><fmt:message key="listParoleEligibilityLink"/></span></a>
		</li>
	</sec:authorize>
	<sec:authorize access="hasRole('HEARING_ANALYSIS_EDIT') or hasRole('ADMIN')">
		<c:choose>
			<c:when test="${not empty boardHearing}">
				<li>
					<a class="viewEditLink" href="${pageContext.request.contextPath}/boardHearing/edit.html?boardHearing=${boardHearing.id}"><span class="visibleLinkLabel"><fmt:message key="editBoardHearingLink"/></span></a>
				</li>
			</c:when>
		<c:otherwise>
			<c:if test="${not empty paroleEligibility}">
				<li>
					<a class="viewEditLink" href="${pageContext.request.contextPath}/boardHearing/create.html?paroleEligibility=${paroleEligibility.id}"><span class="visibleLinkLabel"><fmt:message key="editBoardHearingLink"/></span></a>
				</li>
			</c:if>
		</c:otherwise>
		</c:choose>
	</sec:authorize>
</ul>
</fmt:bundle>