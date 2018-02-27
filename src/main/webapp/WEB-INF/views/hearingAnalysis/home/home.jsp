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
 - Version: 0.1.1 (Feb 20, 2018)
 - Since: OMIS 3.0
 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<fmt:bundle basename="omis.hearinganalysis.msgs.hearingAnalysisHome">
<head>
	<title>
		<fmt:message key="hearingAnalysisHomeTitle"/>
		<jsp:include page="/WEB-INF/views/offender/includes/offenderNameSummary.jsp"/>
	</title>
	<jsp:include page="/WEB-INF/views/common/includes/toolsResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/headerOffenderFormResources.jsp"/>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/hearingAnalysis/style/hearingAnalysis.css?VERSION=2" />
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/hearingAnalysis/scripts/JQuery/jquery.omis.hearingAnalysisHome.js?VERSION=1" ></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/hearingAnalysis/scripts/JQuery/jquery.omis.hearingAnalysis.js?VERSION=1" ></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/hearingAnalysis/scripts/hearingAnalysisHome.js?VERSION=1" ></script>
	<script type="text/javascript">
		//Track current indexes
		var currentHearingAnalysisNoteIndex = ${hearingAnalysisNoteIndex};
    </script>
</head>
<body>
	<jsp:include page="/WEB-INF/views/offender/includes/offenderHeader.jsp"/>
	<h1>
		<a class="actionMenuItem" id="actionMenuLink"
			href="${pageContext.request.contextPath}/hearingAnalysis/homeActionMenu.html?hearingAnalysis=${hearingAnalysis.id}"></a>
		<fmt:message key="hearingAnalysisHomeTitle"/>
	</h1>
	<jsp:include page="includes/homeContent.jsp"/>
</body>
</fmt:bundle>
</html>