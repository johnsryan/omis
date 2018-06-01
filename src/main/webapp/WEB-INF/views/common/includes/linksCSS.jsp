<%--Include link css if not loaded.
 - Author: Ryan Johns
 - Version: 0.1.0 (Oct 23, 2014)
 - Since: OMIS 3.0
 --%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<c:if test="${empty linksCSS}">
		<c:set var="linksCSS" value="true" scope="request"/>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/common/style/links.css?VERSION=5"/>
	</c:if>