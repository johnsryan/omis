<%-- Includes offender header resources if not loaded.
 - Author: Ryan Johns
 - Version: 0.1.0 (Oct 23, 2014)
 - Since: OMIS 3.0
--%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<jsp:include page="/WEB-INF/views/common/includes/toolsResources.jsp"/>
	<c:if test="${empty offenderHeaderResources}">
		<c:set var="offenderHeaderResources" value="true" scope="request"/>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/offender/style/offenderHeader.css?VERSION=11"/>
		<script type="text/javascript" src="${pageContext.request.contextPath}/resources/offender/scripts/JQuery/jquery.omis.offenderHeader.js?VERSION=3"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/resources/offender/scripts/offenderHeader.js?VERSION=5"></script>
	</c:if>