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
<fmt:bundle basename="omis.employment.msgs.employment">
<head>
	<title><fmt:message key="employerSearchTitle"/></title>
	<jsp:include page="/WEB-INF/views/common/includes/headerOffenderListResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/headerOffenderFormResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/serverConfigResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/messageResolverResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/linksResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/searchResources.jsp"/>
<%-- 	<jsp:include page="/WEB-INF/views/common/includes/searchCSS.jsp"/> --%>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/employment/scripts/employer.js"></script>
</head>
<body>
	<h1>
		<a class="actionMenuItem" id="actionMenuLink" href="${pageContext.request.contextPath}/employment/employerSearchActionMenu.html?offender=${employerSearchForm.offender.id}"></a><span class="visibleLinkLabel"/>
		<fmt:message key="employerSearchTitle"/>
	</h1>
	<jsp:include page="includes/searchForm.jsp" />
	<c:if test="${not empty employerSummaries}">
		<jsp:include page="includes/searchResults.jsp"/>
	</c:if>
</body>
	<span class="linkContainer">
		<a class="createLink" href="${pageContext.request.contextPath}/employment/create.html?offender=${employerSearchForm.offender.id}&exist=false"><span class="visibleLinkLabel"><fmt:message key="createNewEmployerLabel"/></span></a>
	</span>
</fmt:bundle>
</html>