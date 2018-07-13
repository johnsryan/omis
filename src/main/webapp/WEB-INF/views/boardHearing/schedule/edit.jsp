<?xml version="1.0" encoding="UTF-8"?>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<fmt:bundle basename="omis.boardhearing.msgs.scheduleHearings">
<head>
	<jsp:include page="/WEB-INF/views/common/includes/headerOffenderFormResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/headerListResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/toolsResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/searchResources.jsp"/><script type="text/javascript" src="${pageContext.request.contextPath}/resources/boardHearing/scripts/scheduleHearings.js?VERSION=1"></script>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/boardHearing/style/boardHearing.css" />
	<title>
		<fmt:message key="scheduleHearingsHeader" />
	</title>
</head>
<body>
<div id="boardHearingHeader">
	<fieldset class="foregroundUltraLight">
		<span class="fieldGroup">
			<label class="fieldLabel detailsLabel">
				<fmt:message key="itineraryDatesLabel"/>
			</label>
			<span class="detail">
				<fmt:formatDate value="${paroleBoardItinerarySummary.startDate}" var="startDate" pattern="MM/dd/yyyy"/>
				<fmt:formatDate value="${paroleBoardItinerarySummary.endDate}" var="endDate" pattern="MM/dd/yyyy"/>
				<fmt:message key="itineraryDatesDisplayLabel">
					<fmt:param value="${startDate}" />
					<fmt:param value="${startDate}" />
				</fmt:message>
			</span>
		</span>
		<span class="fieldGroup">
			<label class="fieldLabel detailsLabel">
				<fmt:message key="locationLabel"/>
			</label>
			<span class="detail">
				<c:out value="${paroleBoardItinerarySummary.locationOrganizationName}" />
				<%-- <c:out value="${paroleBoardItinerarySummary.facilityName}" /> 
				<c:out value="${paroleBoardItinerarySummary.unitName}" /> --%>
			</span>
		</span>
	</fieldset>
</div>
	<h1>
		<a class="actionMenuItem" id="actionMenuLink" href="${pageContext.request.contextPath}/boardHearing/schedule/scheduleHearingsActionMenu.html?paroleBoardItinerary=${paroleBoardItinerary.id}"></a>
		<fmt:message key="scheduleHearingsHeader" />
	</h1>
	<jsp:include page="includes/editForm.jsp"/>
</body>
</fmt:bundle>
</html>