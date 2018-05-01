<?xml version="1.0" encoding="UTF-8"?>
<%--
 - Author: Yidong Li
 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<fmt:bundle basename="omis.incident.msgs.incident">
<head>
	<title><fmt:message key="incidentStatementLabel"/></title>
	<jsp:include page="/WEB-INF/views/common/includes/headerOffenderFormResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/headerOffenderListResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/messageResolverResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/searchResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/toolsResources.jsp"/>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/incident/style/incident.css?VERSION=1"/>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/incident/statement/scripts/jquery/jquery.omis.incidentStatements.js?VERSION=1"> </script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/incident/statement/scripts/incidentStatements.js?VERSION=1"> </script>
	<script type="text/javascript">
		/* <![CDATA[ */
			//Involved person item index 
			var currentInvolvedPersonItemIndex = ${involvedPersonItemIndex};
		/* ]]> */
	</script>
</head>
<body>
<%-- 	<jsp:include page="includes/search.jsp" /> --%>
	<h1><a class="actionMenuItem" id="actionMenuLink" href="${pageContext.request.contextPath}/incident/statement/incidentActionMenu.html"></a><span class="visibleLinkLabel"><fmt:message key="incidentStatementLabel"/></span></h1>
	<jsp:include page="includes/searchResults.jsp"/>
</body>
</fmt:bundle>
</html>