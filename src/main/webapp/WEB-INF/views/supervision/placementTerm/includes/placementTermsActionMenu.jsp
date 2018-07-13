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
  - Action menu for placement terms.
  -
  - Author: Stephen Abson
  --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle basename="omis.supervision.msgs.profile" var="profileBundle"/>
<fmt:bundle basename="omis.supervision.msgs.placementTerm">
<ul>
	<c:if test="${not empty offender}">
		<sec:authorize access="hasRole('PLACEMENT_TERM_CREATE') or hasRole('ADMIN')">
			<li><a class="createLink" href="${pageContext.request.contextPath}/supervision/placementTerm/create.html?offender=${offender.id}&amp;allowCorrectionalStatusChange=true"><span class="visibleLinkLabel"><c:choose><c:when test="${not empty effectivePlacementTerm}"><fmt:message key="changePlacementLink"/></c:when><c:otherwise><fmt:message key="createPlacementTermLink"/></c:otherwise></c:choose></span></a></li>
		</sec:authorize>
	</c:if>
	<%-- Disable link until supervisory organizations are activated - SA --%>
	<c:if test="${not empty effectivePlacementTerm and effectivePlacementTerm.correctionalStatusTerm.correctionalStatus.locationRequired}">
		<sec:authorize access="hasRole('DISABLED') and (hasRole('PLACEMENT_TERM_CREATE') or hasRole('ADMIN'))">
			<li><a class="createLink" href="${pageContext.request.contextPath}/supervision/placementTerm/create.html?offender=${offender.id}&amp;allowCorrectionalStatusChange=false"><span class="visibleLinkLabel"><fmt:message key="facilityTransferLink"/></span></a></li>
		</sec:authorize>
	</c:if>
	<c:if test="${not empty effectivePlacementTerm and not empty effectivePlacementTerm.supervisoryOrganizationTerm and not effectivePlacementTerm.correctionalStatusTerm.correctionalStatus.locationRequired}">
		<sec:authorize access="hasRole('PLACEMENT_TERM_CREATE') or hasRole('ADMIN')">
			<li><a class="createLink" href="${pageContext.request.contextPath}/supervision/placementTerm/create.html?offender=${offender.id}&amp;allowCorrectionalStatusChange=false"><span class="visibleLinkLabel"><fmt:message key="changeSupervisoryOrganizationLink"/></span></a></li>
		</sec:authorize>
	</c:if>
	<c:if test="${not empty effectivePlacementTerm and empty effectivePlacementTerm.dateRange.endDate}">
		<sec:authorize access="hasRole('PLACEMENT_TERM_EDIT') or hasRole('ADMIN')">
			<li><a class="createLink" href="${pageContext.request.contextPath}/supervision/placementTerm/endPlacementTerm.html?offender=${offender.id}&placementTerm=${effectivePlacementTerm.id}"><span class="visibleLinkLabel"><fmt:message key="endPlacementTermLink"/></span></a></li>
		</sec:authorize>
	</c:if>
	<c:if test="${not empty offender}">
		<sec:authorize access="hasRole('SUPERVISION_PROFILE_VIEW') or hasRole('ADMIN')">
			<li><a class="supervisionProfileLink" href="${pageContext.request.contextPath}/supervision/profile.html?offender=${offender.id}"><span class="visibleLinkLabel"><fmt:message key="supervisionProfileHeader" bundle="${profileBundle}"/></span></a></li>
		</sec:authorize>
	</c:if>
	<sec:authorize access="hasRole('PLACEMENT_TERM_VIEW') or hasRole('ADMIN')">
		<c:if test="${not empty offender}">
			<li>
				<a href="${pageContext.request.contextPath}/supervision/placementTerm/placementListingReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab adobeReportLink"><fmt:message key="placementListingReportLinkLabel"/></a>
			</li>
		</c:if>
	</sec:authorize>
	<sec:authorize access="hasRole('PLACEMENT_TERM_VIEW') or hasRole('ADMIN')">
		<c:if test="${not empty offender}">
			<li>
				<a href="${pageContext.request.contextPath}/supervision/placementTerm//placementSupOrgListingReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="placementSupOrgListingReportLinkLabel"/></a>
			</li>
		</c:if>
	</sec:authorize>	
</ul>
</fmt:bundle>