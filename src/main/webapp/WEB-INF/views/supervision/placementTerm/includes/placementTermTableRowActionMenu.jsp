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
  - Action menu for placement term table row.
  -
  - Author: Stephen Abson
  --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle basename="omis.supervision.msgs.profile" var="profileBundle"/>
<fmt:bundle basename="omis.supervision.msgs.placementTerm">
<ul>
	<c:if test="${not empty placementTerm}">
		<sec:authorize access="hasRole('PLACEMENT_TERM_VIEW') or hasRole('ADMIN')">
			<li><a class="viewEditLink" href="${pageContext.request.contextPath}/supervision/placementTerm/edit.html?placementTerm=${placementTerm.id}"><span class="visibleLinkLabel"><fmt:message key="viewEditPlacementTermLink"/></span></a></li>
		</sec:authorize>
	</c:if>
	<c:if test="${not empty placementTerm}">
		<sec:authorize access="hasRole('PLACEMENT_TERM_REMOVE') or hasRole('ADMIN')">
			<li><a class="removeLink" href="${pageContext.request.contextPath}/supervision/placementTerm/remove.html?placementTerm=${placementTerm.id}"><span class="visibleLinkLabel"><fmt:message key="removePlacementTermLink"/></span></a></li>
		</sec:authorize>
	</c:if>
		<sec:authorize access="hasRole('PLACEMENT_TERM_VIEW') or hasRole('ADMIN')">
		<c:if test="${not empty placementTerm}">
			<li>
				<a href="${pageContext.request.contextPath}/supervision/placementTerm/placementTermDetailReport.html?placementTerm=${placementTerm.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="placementTermDetailsReportLinkLabel"/></a>
			</li>
		</c:if>
	</sec:authorize>
</ul>
</fmt:bundle>