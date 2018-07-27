<%-- Includes libraries for tabs if not loaded.
 - Author: Ryan Johns
 - Version: 0.1.0 (Oct 21, 2014)
 - Since: OMIS 3.0
 --%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<jsp:include page="/WEB-INF/views/common/includes/jQueryResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/scrollContainerResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/messageEventResources.jsp"/>
	<c:if test="${empty tabsLIB}">
		<c:set var="tabsLIB" value="true" scope="request"/>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/common/style/tabs.css?VERSION=12"/>
		<script type="text/javascript" src="${pageContext.request.contextPath}/resources/common/scripts/tabs.js?VERSION=12"></script>
	</c:if>