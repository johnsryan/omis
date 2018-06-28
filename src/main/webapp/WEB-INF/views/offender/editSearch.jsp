<?xml version="1.0" encoding="UTF-8"?>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<fmt:setBundle basename="omis.offender.msgs.offenderSearch" var="offenderSearchBundle"/>
<head>
	<title>	
	<fmt:message key="offenderSearchTitle" bundle="${offenderSearchBundle}"/>
	</title>	
	<jsp:include page="/WEB-INF/views/common/includes/headerListResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/searchResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/toolsResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/headerOffenderFormResources.jsp"/>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/common/style/home.css?VERSION=1"/>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/offender/style/advancedOffenderSearch.css?VERSION=1"/>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/offender/scripts/JQuery/jquery.omis.offenderSearch.js?VERSION=1"></script>	
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/offender/scripts/offenderSearch.js?VERSION=1.1"></script>
</head>		
<body>	
	<h1>			
		<a class="actionMenuItem" id="searchCriteriaActionMenu" href="${pageContext.request.contextPath}/offender/searchCriteriaActionMenu.html"></a>
		<fmt:message key="offenderSearchTitle" bundle="${offenderSearchBundle}"/>
	</h1>	
	<jsp:include page="includes/editFormSearch.jsp"/>	
</body>
</html>