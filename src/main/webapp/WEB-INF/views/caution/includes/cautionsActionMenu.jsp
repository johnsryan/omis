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
 - Author: Joel Norris
 - Version: 0.1.0 (February 24, 2015)
 - Since: OMIS 3.0
 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle" />
<fmt:bundle basename="omis.caution.msgs.caution">
	<ul>
		<sec:authorize access="hasRole('CAUTION_CREATE') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
			<li>
				<a class="createLink" href="${pageContext.request.contextPath}/caution/create.html?offender=${offender.id}"><span class="visibleLinkLabel"><fmt:message key="createCautionLink"/></span></a>
			</li>
			</c:if>
		</sec:authorize>
		<sec:authorize access="hasRole('CAUTION_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
			<li>
				<a href="${pageContext.request.contextPath}/caution/cautionListingReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="cautionListingReportLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>
		<sec:authorize access="hasRole('CAUTION_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty caution}">
			<li>
				<a class="viewEditLink" href="${pageContext.request.contextPath}/caution/edit.html?caution=${caution.id}"><span class="visibleLinkLabel"><fmt:message key="viewEditCautionLink"/></span></a>
			</li>
			</c:if>
		</sec:authorize>
		<sec:authorize access="hasRole('CAUTION_REMOVE') or hasRole('ADMIN')">
			<c:if test="${not empty caution}">
			<li>
				<a class="removeLink" href="${pageContext.request.contextPath}/caution/remove.html?caution=${caution.id}"><span class="visibleLinkLabel"><fmt:message key="removeCautionLink"/></span></a>
			</li>
			</c:if>
		</sec:authorize>
		<sec:authorize access="hasRole('CAUTION_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty caution}">
			<li>
				<a href="${pageContext.request.contextPath}/caution/cautionDetailsReport.html?caution=${caution.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="cautionDetailsReportLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>
	</ul>
</fmt:bundle>