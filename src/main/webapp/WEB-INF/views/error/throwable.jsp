<?xml version="1.0" encoding="UTF-8"?>
<%--
 - Author: Stephen Abson
 - Version: 0.1.0 (Oct 30, 2013)
 - Since: OMIS 3.0
 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<fmt:bundle basename="omis.error.msgs.error">
<head>
	<title><fmt:message key="throwableTitle"/></title>
	<jsp:include page="/WEB-INF/views/common/includes/headerMetas.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/headerGeneralResources.jsp"/>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/error/style/error.css"/>
</head>
<body>
	<h1><fmt:message key="throwableTitle"/></h1>
	<p class="exceptionMessage">
		<fmt:message key="throwableMessage"/>
	</p>
	<p>
		<button type="button" onclick="history.back()"><fmt:message key='backLabel'/></button>
	</p>
	<jsp:include page="includes/stackTrace.jsp"/>
</body>
</fmt:bundle>
</html>