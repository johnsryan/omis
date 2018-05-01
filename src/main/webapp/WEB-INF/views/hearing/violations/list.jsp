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
		<fmt:message key="violationStatusListHeader"/>
		<jsp:include page="/WEB-INF/views/offender/includes/offenderNameSummary.jsp"/>
	</title>
</head>
<body>
	<jsp:include page="/WEB-INF/views/offender/includes/offenderHeader.jsp"/>
	<h1>
		<a class="actionMenuItem" id="actionMenuLink" href="${pageContext.request.contextPath}/hearing/violations/violationStatusActionMenu.html?offender=${offender.id}"></a>
		<fmt:message key="violationStatusListHeader"/><br /><br />
	</h1>
	<div class="violations">
		<h1>
			<a class="actionMenuItem" id="actionMenuLink" href="${pageContext.request.contextPath}/hearing/violations/unresolvedViolationsActionMenu.html?offender=${offender.id}&unresolvedDisciplinaryViolationsExist=${unresolvedDisciplinaryViolationsExist}&unresolvedConditionViolationsExist=${unresolvedConditionViolationsExist}"></a>
			<fmt:message key="unresolvedViolationsListHeader"/>
		</h1>
		<jsp:include page="includes/unresolvedViolationsListTable.jsp"/>
	</div>
	<div class="violations">
		<h1>
			<a class="actionMenuItem" id="actionMenuLink" href="${pageContext.request.contextPath}/hearing/violations/scheduledViolationsActionMenu.html?offender=${offender.id}"></a>
			<fmt:message key="scheduledViolationsListHeader"/>
		</h1>
		<jsp:include page="includes/scheduledViolationsListTable.jsp"/>
	</div>
	<div class="violations">
		<h1>
			<a class="actionMenuItem" id="actionMenuLink" href="${pageContext.request.contextPath}/hearing/violations/resolvedViolationsActionMenu.html?offender=${offender.id}"></a>
			<fmt:message key="resolvedViolationsListHeader"/>
		</h1>
		<jsp:include page="includes/resolvedViolationsListTable.jsp"/>
	</div>
</body>
</fmt:bundle>
</html>