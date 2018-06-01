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
  - Action menu for location terms screen.
  -
  - Author: Stephen Abson
  --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle basename="omis.locationterm.msgs.profile" var="profileBundle"/>
<fmt:bundle basename="omis.locationterm.msgs.locationTerm">
<ul>
	<c:if test="${not empty offender}">
		<sec:authorize access="hasRole('LOCATION_TERM_CREATE') or hasRole('ADMIN')">
			<li><a class="createLink" href="${pageContext.request.contextPath}/locationTerm/create.html?offender=${offender.id}"><span class="visibleLinkLabel"><fmt:message key="createLocationTermTitle"/></span></a></li>
		</sec:authorize>
	</c:if>
	<c:if test="${not empty supervisoryOrganizationLocations}">
		<c:forEach var="supervisoryOrganizationLocation" items="${supervisoryOrganizationLocations}">
			<c:if test="${location ne supervisoryOrganizationLocation}">
				<sec:authorize access="hasRole('LOCATION_TERM_CREATE') or hasRole('ADMIN')">
					<li><a class="createLink" href="${pageContext.request.contextPath}/locationTerm/create.html?offender=${offender.id}&amp;toLocation=${supervisoryOrganizationLocation.id}"><span class="visibleLinkLabel"><fmt:message key="createLocationAtLink"><fmt:param value="${supervisoryOrganizationLocation.organization.name}"/></fmt:message></span></a></li>
				</sec:authorize>
			</c:if>
		</c:forEach>
	</c:if>
	<c:if test="${not empty changeActions}">
		<c:forEach var="changeAction" items="${changeActions}">
			<sec:authorize access="hasRole('LOCATION_TERM_CREATE') or hasRole('ADMIN')">
				<li><a class="createLink" href="${pageContext.request.contextPath}/locationTerm/create.html?offender=${offender.id}&amp;changeAction=${changeAction.id}"><span class="visibleLinkLabel"><c:out value="${changeAction.name}"/></span></a></li>
			</sec:authorize>
		</c:forEach>
	</c:if>
	<c:if test="${not empty offender}">
		<sec:authorize access="hasRole('LOCATION_TERM_PROFILE_VIEW') or hasRole('ADMIN')">
			<li><a class="locationTermProfileLink" href="${pageContext.request.contextPath}/locationTerm/profile.html?offender=${offender.id}"><span class="visibleLinkLabel"><fmt:message key="locationTermProfileHeader" bundle="${profileBundle}"/></span></a></li>
		</sec:authorize>
	</c:if>
	<c:if test="${not empty locationTerm}">
		<sec:authorize access="hasRole('LOCATION_TERM_VIEW') or hasRole('ADMIN')">
			<li><a class="viewEditLink" href="${pageContext.request.contextPath}/locationTerm/edit.html?locationTerm=${locationTerm.id}" title="<fmt:message key='viewEditLocationTermLink'/>"><span class="visibleLinkLabel"><fmt:message key="viewEditLocationTermLink"/></span></a></li>
		</sec:authorize>
		<sec:authorize access="hasRole('LOCATION_REMOVE') or hasRole('ADMIN')">
			<li><a class="removeLink" href="${pageContext.request.contextPath}/locationTerm/remove.html?locationTerm=${locationTerm.id}" title="<fmt:message key='removeLocationTermLink'/>"><span class="visibleLinkLabel"><fmt:message key="removeLocationTermLink"/></span></a></li>
		</sec:authorize>
		<sec:authorize access="hasRole('LOCATION_TERM_VIEW') or hasRole('ADMIN')">
		<li>
			<a href="${pageContext.request.contextPath}/locationTerm/locationTermDetailsReport.html?locationTerm=${locationTerm.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="locationTermDetailsReportLinkLabel"/></a>
		</li>
	</sec:authorize>
	</c:if>
	<c:if test="${not empty offender}">
		<sec:authorize access="hasRole('LOCATION_TERM_LIST') or hasRole('ADMIN')">
		<li>
			<a href="${pageContext.request.contextPath}/locationTerm/locationTermListingReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="locationTermListingReportLinkLabel"/></a>
		</li>
		</sec:authorize>
	</c:if>
</ul>
</fmt:bundle>