<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<fmt:bundle basename="omis.assessment.msgs.assessmentRating">
	<head>
		<jsp:include page="/WEB-INF/views/common/includes/headerOffenderFormResources.jsp"/>
		<jsp:include page="/WEB-INF/views/common/includes/toolsResources.jsp"/>
		<jsp:include page="/WEB-INF/views/common/includes/searchResources.jsp"/>
		<script type="text/javascript" src="${pageContext.request.contextPath}/resources/common/scripts/SessionConfig.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/resources/assessment/scripts/assessmentCategoryOverride.js?VERSION=1"></script>
		<title>
			<c:choose>
				<c:when test="${empty assessmentCategoryOverride}">
					<fmt:message key="createAssessmentCategoryOverrideTitle" />
				</c:when>
				<c:otherwise>
					<fmt:message key="editAssessmentCategoryOverrideTitle" />
				</c:otherwise>
			</c:choose>
		</title>
	</head>
	<body>
		<h1>
			<a class="actionMenuItem" id="actionMenuLink" href="${pageContext.request.contextPath}/assessment/rating/assessmentCategoryOverrideActionMenu.html?assessmentCategoryScore=${assessmentCategoryScore.id}"></a>
			<c:choose>
				<c:when test="${empty assessmentCategoryOverride}">
					<fmt:message key="createAssessmentCategoryOverrideTitle" />
				</c:when>
				<c:otherwise>
					<fmt:message key="editAssessmentCategoryOverrideTitle" />
				</c:otherwise>
			</c:choose>
		</h1>
		<jsp:include page="includes/editForm.jsp"/>
	</body>
</fmt:bundle>
</html>