<?xml version="1.0" encoding="UTF-8"?>
<%--
 -  OMIS - Offender Management Information System
 -  Copyright (C) 2011 - 2017 State of Montana
 -
 -  This program is free software: you can redistribute it and/or modify
 -  it under the terms of the GNU General Public License as published by
 -  the Free Software Foundation, either version 3 of the License, or
 -  (at your option) any later version.
 -
 -  This program is distributed in the hope that it will be useful,
 -  but WITHOUT ANY WARRANTY; without even the implied warranty of
 -  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 -  GNU General Public License for more details.
 -
 -  You should have received a copy of the GNU General Public License
 -  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 --%>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<fmt:bundle basename="omis.chronologicalnote.msgs.chronologicalNote">
	<head>
		<title>
			<c:choose>
				<c:when test="${not empty chronologicalNote}">
					<fmt:message key="editChronologicalNoteHeader"/>
					<jsp:include page="/WEB-INF/views/offender/includes/offenderNameSummary.jsp"/>
				</c:when>
				<c:otherwise>
					<fmt:message key="createChronologicalNoteHeader"/>
					<jsp:include page="/WEB-INF/views/offender/includes/offenderNameSummary.jsp"/>
				</c:otherwise>
			</c:choose>
		</title>
		<jsp:include page="/WEB-INF/views/common/includes/headerOffenderFormResources.jsp"/>
		<jsp:include page="/WEB-INF/views/common/includes/messageResolverResources.jsp"/>
		<jsp:include page="/WEB-INF/views/common/includes/toolsResources.jsp"/>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/common/style/jquery/ui/jquery.ptTimeSelect.css"/>
		<script type="text/javascript" src="${pageContext.request.contextPath}/resources/3rdparty/JQuery/ui/jquery.ptTimeSelect.js"> </script>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/chronologicalnote/style/chronologicalNote.css?VERSION=1.3"/>
		<script type="text/javascript" src="${pageContext.request.contextPath}/resources/chronologicalnote/scripts/chronologicalNote.js?VERSION=1"> </script>
	</head>
	<body>
		<jsp:include page="/WEB-INF/views/offender/includes/offenderHeader.jsp"/>
		<h1>
			<a class="actionMenuItem" id="actionMenuLink" href="${pageContext.request.contextPath}/chronologicalNote/chronologicalNoteActionMenu.html?offender=${offender.id}"></a>
			<c:choose>
				<c:when test="${not empty chronologicalNote}">
					<fmt:message key="editChronologicalNoteHeader"/>
				</c:when>
				<c:otherwise>
					<fmt:message key="createChronologicalNoteHeader"/>
				</c:otherwise>
			</c:choose>
		</h1>
		<jsp:include page="/WEB-INF/views/chronologicalNote/includes/editForm.jsp"/>
	</body>
</fmt:bundle>
</html>
