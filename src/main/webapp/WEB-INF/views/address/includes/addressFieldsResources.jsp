 <%-- 
 - Includes JS libraries address fields functionality.
 -
 - Usage: See addressFields.js
 - Author: Joel Norris
 - Version: 0.1.0 (June 18, 2015)
 - Since: OMIS 3.0
 --%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<jsp:include page="/WEB-INF/views/common/includes/formCSS.jsp"/>	
	<jsp:include page="/WEB-INF/views/common/includes/jQueryResources.jsp"/>	
	<jsp:include page="/WEB-INF/views/common/includes/serverConfigResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/messageResolverResources.jsp"/>
	<c:if test="${empty addressFieldsLIB}">
		<c:set var="addressFieldsLIB" value="true" scope="request"/>
		<script type="text/javascript" src="${pageContext.request.contextPath}/resources/address/scripts/JQuery/jquery.omis.addressFields.js?VERSION=1.1"> </script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/resources/address/scripts/addressFields.js?VERSION=1.1"> </script>
	</c:if>