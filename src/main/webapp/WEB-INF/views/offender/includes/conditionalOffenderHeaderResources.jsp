<%--
 - Author: Stephen Abson
 - Version: 0.1.0 (Oct 30, 2013)
 - Since: OMIS 3.0
 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:if test="${not empty offenderSummary}">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/offender/style/offenderHeader.css?VERSION=10"/>
		<script type="text/javascript" src="${pageContext.request.contextPath}/resources/offender/scripts/JQuery/jquery.omis.offenderHeader.js?VERSION=3"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/offender/scripts/offenderHeader.js?VERSION=5"></script>
</c:if>