<?xml version="1.0" encoding="UTF-8"?>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<fmt:bundle basename="omis.assessment.msgs.assessmentQuestionnaire">
<head>
	<title>
		<fmt:message key="assessmentTitle"/>
		<jsp:include page="/WEB-INF/views/offender/includes/offenderNameSummary.jsp"/>
	</title>
	<jsp:include page="/WEB-INF/views/common/includes/toolsResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/headerOffenderFormResources.jsp"/>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/assessment/style/assessmentQuestionnaire.css?VERSION=1"/>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/questionnaire/style/questionnaire.css?VERSION=1"/>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/questionnaire/scripts/inputValidation.js?VERSION=1"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/questionnaire/scripts/administeredQuestionnaireSection.js?VERSION=1" ></script>
</head>
<body>
	<jsp:include page="includes/sectionHeader.jsp"/>
	<h1>
		<c:out value="${questionnaireSectionStatus.questionnaireSection.title}"/>
	</h1>
	<jsp:include page="../../questionnaire/includes/editSectionForm.jsp"/>
</body>
</fmt:bundle>
</html>