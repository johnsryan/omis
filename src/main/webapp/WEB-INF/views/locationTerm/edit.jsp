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
 - Screen to edit location terms.
 -
 - Author: Stephen Abson
 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<fmt:bundle basename="omis.locationterm.msgs.locationTerm">
<head>
	<title>
		<c:choose>
			<c:when test="${not empty locationTerm}">
				<fmt:message key="editLocationTermTitle"/>
			</c:when>
			<c:when test="${not empty toLocation}">
				<fmt:message key="createLocationAtLink">
					<fmt:param>${toLocation.organization.name}</fmt:param>
				</fmt:message>
			</c:when>
			<c:when test="${not empty changeAction}">
				<c:out value="${changeAction.name}"/>
			</c:when>
			<c:otherwise>
				<fmt:message key="createLocationTermTitle"/>
			</c:otherwise>
		</c:choose>
		<jsp:include page="/WEB-INF/views/offender/includes/offenderNameSummary.jsp"/>
	</title>
	<jsp:include page="/WEB-INF/views/common/includes/headerOffenderFormResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/jQueryResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/toolsResources.jsp"/>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/locationTerm/style/links.css"/>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/common/style/jquery/ui/jquery.ptTimeSelect.css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/3rdparty/JQuery/ui/jquery.ptTimeSelect.js"> </script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/locationTerm/scripts/locationTerm.js"> </script>
    <script type="text/javascript">/* <[CDATA[ */
		var locationReasonTermItemIndex = <c:choose><c:when test="${not empty locationReasonTermItemIndex}">${locationReasonTermItemIndex}</c:when><c:otherwise>0</c:otherwise></c:choose>;
		var offender = <c:choose><c:when test="${not empty offenderSummary}">${offenderSummary.id}</c:when><c:otherwise>null</c:otherwise></c:choose>;
		var changeAction = <c:choose><c:when test="${not empty changeAction}">${changeAction.id}</c:when><c:otherwise>null</c:otherwise></c:choose>;
		var locationTerm = <c:choose><c:when test="${not empty locationTerm}">${locationTerm.id}</c:when><c:otherwise>null</c:otherwise></c:choose>;
		var locationTermLocation = <c:choose><c:when test="${not empty locationTerm}">${locationTerm.location.id}</c:when><c:otherwise>null</c:otherwise></c:choose>;
    /* ]]> */</script>
</head>
<body>
	<jsp:include page="/WEB-INF/views/offender/includes/offenderHeader.jsp"/>
	<h1>
		<a href="${pageContext.request.contextPath}/locationTerm/locationTermActionMenu.html?offender=${offenderSummary.id}" class="actionMenuItem" id="actionMenuLink"></a>
		<c:choose>
			<c:when test="${not empty locationTerm}">
				<fmt:message key="editLocationTermTitle"/>
			</c:when>
			<c:when test="${not empty toLocation}">
				<fmt:message key="createLocationAtLink">
					<fmt:param>${toLocation.organization.name}</fmt:param>
				</fmt:message>
			</c:when>
			<c:when test="${not empty changeAction}">
				<c:out value="${changeAction.name}"/>
			</c:when>
			<c:otherwise>
				<fmt:message key="createLocationTermTitle"/>
			</c:otherwise>
		</c:choose>
	</h1>
	<jsp:include page="includes/editForm.jsp"/>
</body>
</fmt:bundle>
</html>