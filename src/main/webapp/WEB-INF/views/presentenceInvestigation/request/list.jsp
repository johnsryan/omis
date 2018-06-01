<?xml version="1.0" encoding="UTF-8"?>
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
  - Author: Ryan Johns
  - Author: Annie Wahl
  - Author: Josh Divine
  - Version: 0.1.6 (May 15, 2018)
  - Since: OMIS 3.0 
  --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<fmt:bundle basename="omis.presentenceinvestigation.msgs.presentenceInvestigationRequest">
<head>
	<jsp:include page="/WEB-INF/views/common/includes/headerOffenderListResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/headerListResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/toolsResources.jsp"/>
	<title>
		<fmt:message key="presentenceInvestigationRequestListHeader"/>
	</title>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/presentenceInvestigation/scripts/presentenceInvestigationRequests.js?VERSION=1"> </script>
</head>
 <body>
 	<c:choose>
	<c:when test="${empty offender}">
		<c:set var="onReturn" value="byUser" />
		<h1>
			<a class="actionMenuItem" id="unsubmittedActionMenuLink" href="${pageContext.request.contextPath}/presentenceInvestigation/request/presentenceInvestigationRequestsActionMenu.html?assignedUser=${assignedUser.id}&offender=${offender.id}&onReturn=${onReturn}"></a>
			<fmt:message key="unsubmittedPresentenceInvestigationRequestListHeader"/>
		</h1>
		<c:set var="summaries" value="${unsubmittedSummaries}" scope="request"/>
		<jsp:include page="includes/listTable.jsp"/>
		<h1>
			<a class="actionMenuItem" id="submittedActionMenuLink" href="${pageContext.request.contextPath}/presentenceInvestigation/request/presentenceInvestigationRequestsActionMenu.html?assignedUser=${assignedUser.id}&offender=${offender.id}&onReturn=${onReturn}"></a>
			<fmt:message key="submittedPresentenceInvestigationRequestListHeader"/>
		</h1>
		<c:set var="summaries" value="${submittedSummaries}" scope="request"/>
		<jsp:include page="includes/listTable.jsp"/>
	</c:when>
	<c:when test="${not empty offender}">
		<c:set var="onReturn" value="byOffender" />
		<c:if test="${not empty offenderSummary}">
			<jsp:include page="/WEB-INF/views/offender/includes/offenderHeader.jsp"/>
		</c:if>
		<h1>
			<a class="actionMenuItem" id="actionMenuLink" href="${pageContext.request.contextPath}/presentenceInvestigation/request/presentenceInvestigationRequestsActionMenu.html?assignedUser=${assignedUser.id}&offender=${offender.id}&onReturn=${onReturn}"></a>
			<fmt:message key="presentenceInvestigationRequestListHeader"/>
		</h1>
		<jsp:include page="includes/listTable.jsp"/>
	</c:when>
	</c:choose>
</body>
</fmt:bundle>
</html>