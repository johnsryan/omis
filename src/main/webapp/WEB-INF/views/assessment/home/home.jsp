<?xml version="1.0" encoding="UTF-8"?>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<fmt:bundle basename="omis.assessment.msgs.assessmentHome">
<head>
	<title>
		<fmt:message key="assessmentTitle"/>
		<jsp:include page="/WEB-INF/views/offender/includes/offenderNameSummary.jsp"/>
	</title>
	<jsp:include page="/WEB-INF/views/common/includes/toolsResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/headerOffenderFormResources.jsp"/>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/assessment/style/home.css?VERSION=1" />
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/assessment/scripts/home.js?VERSION=1" ></script>
</head>
<body>
		<jsp:include page="/WEB-INF/views/offender/includes/offenderHeader.jsp"/>
		<jsp:include page="/WEB-INF/views/assessment/includes/assessmentHeader.jsp"/>
		<jsp:include page="includes/homeContent.jsp"/>
</body>
</fmt:bundle>
</html>