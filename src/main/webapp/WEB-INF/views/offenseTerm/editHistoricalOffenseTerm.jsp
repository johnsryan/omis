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
  - Screen to edit historical offense terms.
  -
  - Historical offense terms are represented by inactive sentences.
  -
  - Author: Stephen Abson
  --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<fmt:setBundle var="offenseTermBundle" basename="omis.offenseterm.msgs.offenseTerm"/>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>
		<c:choose>
			<c:when test="${not empty sentence}">
				<fmt:message key="editHistoricalOffenseTermTitle" bundle="${offenseTermBundle}"/>
			</c:when>
			<c:otherwise>
				<fmt:message key="createHistoricalOffenseTermTitle" bundle="${offenseTermBundle}"/>
			</c:otherwise>
		</c:choose>
		<c:choose>
			<c:when test="${not empty offenderSummary}">
				<jsp:include page="/WEB-INF/views/offender/includes/offenderNameSummary.jsp"/>
			</c:when>
		</c:choose>
	</title>
	<jsp:include page="/WEB-INF/views/common/includes/headerOffenderFormResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/jQueryResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/serverConfigResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/toolsResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/searchResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/contactSummaryResources.jsp"/>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/offenseTerm/scripts/historicalOffenseTerm.js?VERSION=1"> </script>
</head>
<body>
	<c:choose>
		<c:when test="${not empty offenderSummary}">
			<jsp:include page="/WEB-INF/views/offender/includes/offenderHeader.jsp"/>
		</c:when>
		<c:when test="${not empty contactSummary}">
			<jsp:include page="/WEB-INF/views/contact/includes/contactSummary.jsp"/>
		</c:when>
		<c:otherwise>
			<span>NO OFFENDER OR CONTACT SUMMARY</span>
		</c:otherwise>
	</c:choose>
	<h1>
		<a href="${pageContext.request.contextPath}/offenseTerm/historicalOffenseTermActionMenu.html?conviction=${conviction.id}" class="actionMenuItem" id="actionMenuLink"></a>
		<c:choose>
			<c:when test="${not empty sentence}">
				<fmt:message key="editHistoricalOffenseTermTitle" bundle="${offenseTermBundle}"/>
			</c:when>
			<c:otherwise>
				<fmt:message key="createHistoricalOffenseTermTitle" bundle="${offenseTermBundle}"/>
			</c:otherwise>
		</c:choose>
	</h1>
	<div>
		<c:set var="docket" value="${conviction.courtCase.docket}" scope="request"/>
		<jsp:include page="/WEB-INF/views/docket/includes/docket.jsp"/>
		<jsp:include page="/WEB-INF/views/conviction/includes/conviction.jsp"/>
	</div>
	<jsp:include page="includes/historicalOffenseTermEditForm.jsp"/>
</body>
</html>