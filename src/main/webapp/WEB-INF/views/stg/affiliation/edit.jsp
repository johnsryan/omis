<?xml version="1.0" encoding="UTF-8"?>
<%--
 - Author: Stephen Abson
 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<fmt:bundle basename="omis.stg.msgs.stg">
<head>
	<title>
		<c:choose>
			<c:when test="${not empty affiliation}">
				<fmt:message key="editStgAffiliationTitle"/>
				<jsp:include page="/WEB-INF/views/offender/includes/offenderNameSummary.jsp"/>
			</c:when>
			<c:otherwise>
				<fmt:message key="createStgAffiliationTitle"/>
				<jsp:include page="/WEB-INF/views/offender/includes/offenderNameSummary.jsp"/>
			</c:otherwise>
		</c:choose>
	</title>
	<jsp:include page="/WEB-INF/views/common/includes/headerOffenderFormResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/jQueryResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/serverConfigResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/messageResolverResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/toolsResources.jsp"/>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/common/scripts/SessionConfig.js"> </script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/user/scripts/searchUserAccount.js"> </script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/stg/scripts/JQuery/jquery.omis.affiliation.js"> </script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/stg/scripts/affiliation.js"> </script>
	<script type="text/javascript">
		/* <![CDATA[ */
			//affiliation note index used to track affiliation note items on the form 
			var currentAffiliationNoteItemIndex = ${affiliationNoteItemIndex};
		/* ]]> */
	</script>
</head>
<body>
	<jsp:include page="/WEB-INF/views/offender/includes/offenderHeader.jsp"/>
	<h1>
		<a class="actionMenuItem" id="actionMenuLink" href="${pageContext.request.contextPath}/stg/affiliation/affiliationActionMenu.html?offender=${offenderSummary.id}"></a>
		<c:choose>
			<c:when test="${not empty affiliation}">
				<fmt:message key="editStgAffiliationTitle"/>
			</c:when>
			<c:otherwise>
				<fmt:message key="createStgAffiliationTitle"/>
			</c:otherwise>
		</c:choose>
	</h1>
	<jsp:include page="includes/editForm.jsp"/>
</body>
</fmt:bundle>
</html>