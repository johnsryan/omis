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
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<fmt:bundle basename="omis.hearing.msgs.hearing">
<head>
	<jsp:include page="/WEB-INF/views/common/includes/headerOffenderFormResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/toolsResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/searchResources.jsp"/>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/common/style/jquery/ui/jquery.ptTimeSelect.css"/>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/3rdparty/JQuery/ui/jquery.ptTimeSelect.js"> </script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/hearing/scripts/includes/jquery.omis.hearing.js?VERSION=2"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/hearing/scripts/hearing.js?VERSION=4"></script>
	<script type="text/javascript">
		var currentHearingNoteItemIndex = ${hearingNoteItemIndex};
	</script>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/hearing/style/hearing.css" />
	<title>
		<c:if test="${empty hearing}">
			<fmt:message key="createHearingHeader" />
		</c:if>
		<c:if test="${not empty hearing}">
			<fmt:message key="editHearingHeader" />
		</c:if>
		<jsp:include page="/WEB-INF/views/offender/includes/offenderNameSummary.jsp"/>
	</title>
</head>
<body>
	<jsp:include page="/WEB-INF/views/offender/includes/offenderHeader.jsp"/>
	<h1>
		<a class="actionMenuItem" id="actionMenuLink" href="${pageContext.request.contextPath}/hearing/hearingActionMenu.html?offender=${offender.id}"></a>
		<c:if test="${empty hearing}">
			<fmt:message key="createHearingHeader" />
		</c:if>
		<c:if test="${not empty hearing}">
			<fmt:message key="editHearingHeader" />
		</c:if>
	</h1>
	<jsp:include page="includes/editForm.jsp"/>
</body>
</fmt:bundle>
</html>