<?xml version="1.0" encoding="UTF-8"?>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<fmt:bundle basename="omis.boardhearing.msgs.boardHearing">
<head>
	<jsp:include page="/WEB-INF/views/common/includes/headerOffenderFormResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/toolsResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/searchResources.jsp"/>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/boardHearing/scripts/includes/jquery.omis.boardHearing.js?VERSION=1"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/boardHearing/scripts/boardHearing.js?VERSION=1"></script>
	<script type="text/javascript">
		var currentBoardHearingNoteItemIndex = ${boardHearingNoteItemIndex};
	</script>
	<title>
		<c:if test="${empty boardHearing}">
			<fmt:message key="createBoardHearingHeader" />
		</c:if>
		<c:if test="${not empty boardHearing}">
			<fmt:message key="editBoardHearingHeader" />
		</c:if>
		<jsp:include page="/WEB-INF/views/offender/includes/offenderNameSummary.jsp"/>
	</title>
</head>
<body>
	<jsp:include page="/WEB-INF/views/offender/includes/offenderHeader.jsp"/>
	<h1>
		<a class="actionMenuItem" id="actionMenuLink" href="${pageContext.request.contextPath}/boardHearing/boardHearingActionMenu.html?paroleEligibility=${paroleEligibility.id}&boardHearing=${boardHearing.id}"></a>
		<c:if test="${empty boardHearing}">
			<fmt:message key="createBoardHearingHeader" />
		</c:if>
		<c:if test="${not empty boardHearing}">
			<fmt:message key="editBoardHearingHeader" />
		</c:if>
	</h1>
	<jsp:include page="includes/editForm.jsp"/>
</body>
</fmt:bundle>
</html>