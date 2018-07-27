<?xml version="1.0" encoding="UTF-8"?>
<%--
 - Offender profile screen.
 -
 - Author: Stephen Abson
 - Author: Ryan Johns
 - Author: Annie Jacques
 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<fmt:bundle basename="omis.offender.msgs.profile">
<head>
	<title>
		<fmt:message key="offenderProfileHeader"/>
		<jsp:include page="/WEB-INF/views/offender/includes/offenderNameSummary.jsp"/>
	</title>
	<jsp:include page="/WEB-INF/views/common/includes/headerMetas.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/headerGeneralResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/headerOffenderHeaderResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/headerToolbarResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/jQueryResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/toolsResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/interactiveImageResources.jsp"/>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/offender/scripts/offenderProfile.js"> </script>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/offender/style/profile.css?VERSION=14"/>
</head>
<body>
	<div id="offenderHeader">
		<jsp:include page="includes/offenderHeaderSummaryContent.jsp"/>
	</div>
	<jsp:include page="includes/profileContent.jsp"/>
</body>
</fmt:bundle>
</html>