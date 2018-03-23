<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<fmt:bundle basename="omis.hearing.msgs.hearing">
<head>
	<jsp:include page="/WEB-INF/views/common/includes/headerOffenderListResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/headerListResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/toolsResources.jsp"/>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/hearing/scripts/violations.js?VERSION=1"> </script>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/hearing/style/hearing.css" />
	<title>
		<fmt:message key="violationsListHeader"/>
		<jsp:include page="/WEB-INF/views/offender/includes/offenderNameSummary.jsp"/>
	</title>
</head>
 <body>
 	<jsp:include page="/WEB-INF/views/offender/includes/offenderHeader.jsp"/>
	<div class="violations">
		<h1>
			<a class="actionMenuItem" id="actionMenuLink" href="${pageContext.request.contextPath}/hearing/violations/unresolvedViolationsActionMenu.html?offender=${offender.id}&unresolvedDisciplinaryViolationsExist=${unresolvedDisciplinaryViolationsExist}&unresolvedConditionViolationsExist=${unresolvedConditionViolationsExist}"></a>
			<fmt:message key="unresolvedViolationsListHeader"/>
		</h1>
		<jsp:include page="includes/unresolvedViolationsListTable.jsp"/>
		<c:if test="${empty unresolvedViolationSummaries}">
			<fmt:message key="noViolationsMessage" />
		</c:if>
	</div>
	<div class="violations">
		<h1>
			<fmt:message key="scheduledViolationsListHeader"/>
		</h1>
		<jsp:include page="includes/scheduledViolationsListTable.jsp"/>
		<c:if test="${empty scheduledHearingViolationSummaries}">
			<table id="scheduledViolationSummariesTable${status.index}" class="listTable">
				<thead>
					<tr>
						<th><fmt:message key="eventCategoryLabel"/></th>
						<th><fmt:message key="eventDateLabel"/></th>
						<th><fmt:message key="eventDetailsLabel"/></th>
						<th><fmt:message key="violationDetailsLabel"/></th>
					</tr>
				</thead>
				<tbody />
			</table>
			<fmt:message key="noViolationsMessage" />
		</c:if>
	</div>
	<div class="violations">
		<h1>
			<fmt:message key="resolvedViolationsListHeader"/>
		</h1>
		<jsp:include page="includes/resolvedViolationsListTable.jsp"/>
		<c:if test="${empty resolvedViolationSummaries}">
			<fmt:message key="noViolationsMessage" />
		</c:if>
	</div>
</body>
</fmt:bundle>
</html>