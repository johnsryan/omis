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
 - List screen row action menu 
 - Author: Yidong Li
 - Version: 0.1.1 (Dec 18, 2017)
 - Since: OMIS 3.0
 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.trackeddocument.msgs.trackedDocument">
	<ul>
		<sec:authorize access="hasRole('TRACKED_DOCUMENT_VIEW') or hasRole('TRACKED_DOCUMENT_EDIT') or hasRole('ADMIN')">
		<li>
			<a class="viewEditLink" href="${pageContext.request.contextPath}/trackedDocument/edit.html?docket=${docket.id}"><span class="visibleLinkLabel"><fmt:message key="viewEditLink" /></span></a>
		</li>
		</sec:authorize> 
		<sec:authorize access="hasRole('TRACKED_DOCUMENT_REMOVE') or hasRole('ADMIN')">
		<li>
			<a class="removeLink" href="${pageContext.request.contextPath}/trackedDocument/remove.html?docket=${docket.id}"><span class="visibleLinkLabel"><fmt:message key="removeLink" /></span></a>
		</li>
		</sec:authorize>
		<sec:authorize access="hasRole('TRACKED_DOCUMENT_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty docket}">
			<li>
				<a href="${pageContext.request.contextPath}/trackedDocument/trackedDocumentDetailsReport.html?docket=${docket.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="trackedDocumentDetailsReportLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>			
	</ul>
</fmt:bundle>