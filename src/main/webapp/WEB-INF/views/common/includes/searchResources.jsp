<%-- Includes JS libraries for autocomplete functionality and dependencies.
 - Usage: See search.js
 - Author: Ryan Johns
 - Version: 0.1.0 (Oct 21, 2014)
 - Since: OMIS 3.0
 --%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<jsp:include page="/WEB-INF/views/common/includes/jQueryResources.jsp"/>	
	<jsp:include page="/WEB-INF/views/common/includes/serverConfigResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/messageResolverResources.jsp"/>
	<c:if test="${empty searchLIB}">
		<c:set var="searchLIB" value="true" scope="request"/>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/search/style/offenderSearchList.css?VERSION=4"/>
		<script type="text/javascript" src="${pageContext.request.contextPath}/resources/common/scripts/JQuery/jquery.omis.search.js?VERSION=10"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/resources/common/scripts/search.js?VERSION=5"></script>		
		<script type="text/javascript" src="${pageContext.request.contextPath}/resources/caseload/scripts/JQuery/jquery.omis.caseloadSearch.js?VERSION=1"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/resources/caseload/scripts/caseloadSearch.js?VERSION=1"></script>
	</c:if>