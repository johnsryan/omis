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
 - Author: Ryan Johns
 - Author: Annie Wahl
 - Author: Josh Divine
 - Version: 0.1.9 (Apr 24, 2018)
 - Since: OMIS 3.0
 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<fmt:bundle basename="omis.presentenceinvestigation.msgs.presentenceInvestigationRequest">
<head>
	<title><fmt:message key="presentenceInvestigationRequestTitle"/></title>
	<jsp:include page="/WEB-INF/views/common/includes/headerOffenderFormResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/toolsResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/searchResources.jsp"/>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/presentenceInvestigation/style/presentenceInvestigation.css" />
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/user/scripts/searchUserAccount.js?VERSION=1"> </script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/common/scripts/SessionConfig.js"> </script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/presentenceInvestigation/scripts/includes/jquery.omis.presentenceInvestigationRequest.js?VERSION=1"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/presentenceInvestigation/scripts/presentenceInvestigationRequest.js?VERSION=2"></script>
	<script type="text/javascript">
		var currentPresentenceInvestigationRequestNoteItemIndex = ${presentenceInvestigationRequestNoteItemIndex};
		var currentPresentenceInvestigationDelayItemIndex = ${presentenceInvestigationDelayItemIndex};
	</script>
	</head>
	<body>
		<c:if test="${not empty offenderSummary}">
			<jsp:include page="/WEB-INF/views/offender/includes/offenderHeader.jsp"/>
		</c:if>
		<h1>
			<c:choose>
				<c:when test="${not empty offender}">
					<c:set var="params" value="offender=${offender.id}" />
				</c:when>
				<c:when test="${empty offender and not empty presentenceInvestigationRequest}">
					<c:set var="params" value="offender=${presentenceInvestigationRequest.docket.person.id}" />
				</c:when>
			</c:choose>
			<c:if test="${not empty presentenceInvestigationRequest}" >
				<c:set var="params" value="${params}${not empty params ? '&' : ''}presentenceInvestigationRequest=${presentenceInvestigationRequest.id}"/>
			</c:if>
			<a class="actionMenuItem" id="actionMenuLink"
				href="${pageContext.request.contextPath}/presentenceInvestigation/request/presentenceInvestigationRequestActionMenu.html?${params}"></a>
			<fmt:message key="presentenceInvestigationRequestTitle"/>
		</h1>
		
		<jsp:include page="includes/editForm.jsp"/>
	</body>
	</fmt:bundle>
	</html>