<%-- Includes general css if not loaded.
 - Author: Ryan Johns
 - Version: 0.1.0 (Oct 21, 2014)
 - Since: OMIS 3.0
 --%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<jsp:include page="/WEB-INF/views/common/includes/linksCSS.jsp"/>
	<c:if test="${empty generalCSS}">
		<c:set var="generalCSS" value="true" scope="request"/>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/common/style/general.css?VERSION=2"/>
		<c:choose>
			<c:when test="${not empty sessionScope.userAppearance}">
				<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/common/style/colors.css?USER_APPEARANCE=${sessionScope.userAppearance.date.time}&&VERSION=9"/>
				<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/common/style/userPreference.css?USER_APPEARANCE=${sessionScope.userAppearance.date.time}&&VERSION=9"/>
			</c:when>
			<c:otherwise>
				<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/common/style/colors.css?VERSION=9"/>
				<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/common/style/userPreference.css?USER_APPEARANCE=${sessionScope.userAppearance.date.time}&&VERSION=9"/>
			</c:otherwise>
		</c:choose>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/common/style/fonts.css?VERSION=1"/>
	</c:if>