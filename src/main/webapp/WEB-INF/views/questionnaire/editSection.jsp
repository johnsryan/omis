<?xml version="1.0" encoding="UTF-8"?>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<fmt:bundle basename="omis.questionnaire.msgs.questionnaire">
<head>
	<title><c:out value="${questionnaireSectionStatus.questionnaireSection.title}"/></title>
	<jsp:include page="/WEB-INF/views/common/includes/headerOffenderFormResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/toolsResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/searchResources.jsp"/>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/questionnaire/style/questionnaire.css"/>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/questionnaire/scripts/administeredQuestionnaireSection.js?VERSION=1"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/questionnaire/scripts/inputValidation.js?VERSION=1"></script>
</head>
<body>
	<jsp:include page="/WEB-INF/views/offender/includes/offenderHeader.jsp"/>
	<h1>
		<a class="actionMenuItem" id="actionMenuLink" href="${pageContext.request.contextPath}/questionnaire/administeredQuestionnaireSectionActionMenu.html?offender=${offender.id}&administeredQuestionnaire=${administeredQuestionnaire.id}"></a>
		<c:out value="${questionnaireSectionStatus.questionnaireSection.title}"/>
	</h1>
	<jsp:include page="includes/editSectionForm.jsp"/>
</body>
</fmt:bundle>
</html>