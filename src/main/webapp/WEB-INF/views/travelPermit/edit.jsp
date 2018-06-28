<?xml version="1.0" encoding="UTF-8"?>
<%--
 - Author: Yidong Li
 - Version: 0.1.0 (May 18, 2018)
 - Since: OMIS 3.0
 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
	<fmt:setBundle basename="omis.travelpermit.msgs.travelPermit" var="travelPermit"/>
	<head>
		<title>
			<c:choose>
				<c:when test="${createTravelPermit}">  
					<fmt:message key="createTravelPermitLabel" bundle="${travelPermit}"></fmt:message>
					<jsp:include page="/WEB-INF/views/offender/includes/offenderNameSummary.jsp"/>
				</c:when>	
				<c:otherwise>
					<fmt:message key="editTravelPermitLabel" bundle="${travelPermit}"></fmt:message>
					<jsp:include page="/WEB-INF/views/offender/includes/offenderNameSummary.jsp"/>
				</c:otherwise>	
			</c:choose>
		</title>
		<jsp:include page="/WEB-INF/views/common/includes/headerOffenderFormResources.jsp"/>
		<jsp:include page="/WEB-INF/views/common/includes/messageResolverResources.jsp"/>
		<jsp:include page="/WEB-INF/views/common/includes/serverConfigResources.jsp"/>
		<jsp:include page="/WEB-INF/views/common/includes/linksResources.jsp"/>
		<jsp:include page="/WEB-INF/views/common/includes/toolsResources.jsp"/>
		<jsp:include page="/WEB-INF/views/common/includes/searchResources.jsp"/>
		<jsp:include page="/WEB-INF/views/address/includes/addressFieldsResources.jsp"/>
		<jsp:include page="/WEB-INF/views/common/includes/contactSummaryResources.jsp"/>
		<script type="text/javascript" src="${pageContext.request.contextPath}/resources/user/scripts/searchUserAccount.js?VERSION=1"> </script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/resources/common/scripts/SessionConfig.js"> </script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/resources/common/scripts/search.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/resources/travelPermit/scripts/travelPermit.js"></script>
		<script type="text/javascript">
  			var travelPermitNoteItemIndex= ${travelPermitNoteItemIndex};
  			var createTravelPermit= ${createTravelPermit};
	  	</script>
	</head>
	<body>
		<c:if test="${not empty offenderSummary}">
			<jsp:include page="/WEB-INF/views/offender/includes/offenderHeader.jsp"/>
		</c:if>
		<h1>
			<a class="actionMenuItem" id="travelPermitEditActionMenuLink" href="${pageContext.request.contextPath}/travelPermit/travelPermitEditActionMenu.html?offender=${offenderSummary.id}"></a><span class="visibleLinkLabel"/>
			<c:choose>
				<c:when test="${createTravelPermit}">  
					<fmt:message key="createTravelPermitLabel" bundle="${travelPermit}"></fmt:message>
				</c:when>	
				<c:otherwise>
					<fmt:message key="editTravelPermitLabel" bundle="${travelPermit}"></fmt:message>
				</c:otherwise>	
			</c:choose>
		</h1>
		<jsp:include page="/WEB-INF/views/travelPermit/includes/editForm.jsp"/>	
	</body>
</html>