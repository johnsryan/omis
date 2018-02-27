<%--Include form css if not loaded.
 - Author: Ryan Johns
 - Version: 0.1.0 (Oct 23, 2014)
 - Since: OMIS 3.0
 --%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<c:if test="${empty formCSS}">
		<c:set var="formCSS" value="true" scope="request"/>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/common/style/form.css?VERSION=3"/>
	</c:if>