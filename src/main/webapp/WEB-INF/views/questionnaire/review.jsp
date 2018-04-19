<?xml version="1.0" encoding="UTF-8"?>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<fmt:bundle basename="omis.questionnaire.msgs.questionnaire">
<head>
	<title><fmt:message key="reviewQuestionnaireTitle" /></title>
	<jsp:include page="/WEB-INF/views/common/includes/headerOffenderFormResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/toolsResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/listCSS.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/searchResources.jsp"/>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/questionnaire/style/questionnaire.css"/>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/questionnaire/scripts/administeredQuestionnaireReview.js?VERSION=1"></script>
</head>
<body>
	<jsp:include page="/WEB-INF/views/offender/includes/offenderHeader.jsp"/>
	<h1>
		<a class="actionMenuItem" id="actionMenuLink" href="${pageContext.request.contextPath}/questionnaire/administeredQuestionnaireReviewActionMenu.html?offender=${offender.id}&administeredQuestionnaire=${administeredQuestionnaireSummary.questionnaireId}"></a>
		<c:choose>
			<c:when test="${administeredQuestionnaireSummary.draft}">
				<fmt:message key="draftSectionTitleLabel">
					<fmt:param value="${administeredQuestionnaireSummary.questionnaireTitle}"/>
				</fmt:message>
			</c:when>
			<c:otherwise>
				<fmt:message key="completedSectionTitleLabel">
					<fmt:param value="${administeredQuestionnaireSummary.questionnaireTitle}"/>
				</fmt:message>
			</c:otherwise>
		</c:choose>
	</h1>
	<jsp:include page="includes/reviewForm.jsp"/>
</body>
</fmt:bundle>
</html>