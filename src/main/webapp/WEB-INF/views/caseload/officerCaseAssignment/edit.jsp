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
 - Author: Josh Divine
 - Version: 0.1.0 (Jun 21, 2018)
 - Since: OMIS 3.0
 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<fmt:bundle basename="omis.caseload.msgs.officerCaseAssignment">
<head>
	<jsp:include page="/WEB-INF/views/common/includes/headerOffenderFormResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/toolsResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/searchResources.jsp"/>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/caseload/officerCaseAssignment/scripts/JQuery/jquery.omis.officerCaseAssignment.js?VERSION=1"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/caseload/officerCaseAssignment/scripts/officerCaseAssignment.js?VERSION=1"></script>
	<title>
		<c:if test="${empty officerCaseAssignment}">
			<fmt:message key="createOfficerCaseAssignmentTitle" />
		</c:if>
		<c:if test="${not empty officerCaseAssignment}">
			<fmt:message key="editOfficerCaseAssignmentTitle" />
		</c:if>
		<c:if test="${not empty offender}"><jsp:include page="/WEB-INF/views/offender/includes/offenderNameSummary.jsp"/></c:if>
	</title>
</head>
<body>
	<c:if test="${not empty offender}">
		<jsp:include page="/WEB-INF/views/offender/includes/offenderHeader.jsp"/>
	</c:if>
	<h1>
		<a class="actionMenuItem" id="actionMenuLink" href="${pageContext.request.contextPath}/caseload/officerCaseAssignment/officerCaseAssignmentActionMenu.html?offender=${offender.id}&userAccount=${userAccount.id}"></a>
		<c:if test="${empty officerCaseAssignment}">
			<fmt:message key="createOfficerCaseAssignmentTitle" />
		</c:if>
		<c:if test="${not empty officerCaseAssignment}">
			<fmt:message key="editOfficerCaseAssignmentTitle" />
		</c:if>
	</h1>
	<jsp:include page="includes/editForm.jsp"/>
</body>
</fmt:bundle>
</html>