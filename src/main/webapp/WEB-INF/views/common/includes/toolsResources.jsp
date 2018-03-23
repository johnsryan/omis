<%--Include tools resources if not loaded.
 - Author: Ryan Johns
 - Version: 0.1.0 (Oct 29, 2014)
 - Since: OMIS 3.0
 --%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/WEB-INF/views/common/includes/jQueryResources.jsp"/>
<jsp:include page="/WEB-INF/views/common/includes/serverConfigResources.jsp"/>
<c:if test="${empty toolsResources}">
	<c:set var="toolsResources" value="true" scope="request"/>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/common/style/moduleGroup.css?VERSION=1"/>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/common/style/actionMenu.css?VERSION=3"/>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/common/scripts/JQuery/jquery.omis.tools.js?VERSION=6"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/common/scripts/tools.js?VERSION=7"></script>
</c:if>