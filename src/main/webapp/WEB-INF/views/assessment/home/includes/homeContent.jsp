<?xml version="1.0" encoding="UTF-8"?>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<fmt:bundle basename="omis.assessment.msgs.assessmentHome">
	<a class="actionMenuItem assessmentActionMenu" id="actionMenuLink" href="${pageContext.request.contextPath}/assessment/assessmentActionMenu.html?offender=${administeredQuestionnaire.answerer.id}"></a>
	<div id="assessmentModuleGroup" class="moduleGroupLinkContainer">
		<a href="${pageContext.request.contextPath}/assessment/questionnaire/administer.html?administeredQuestionnaire=${administeredQuestionnaire.id}"><fmt:message key="questionnaireLink"/></a>
		<a href="${pageContext.request.contextPath}/offender/profile.html?offender=${offenderSummary.id}"><fmt:message key="scoresRatingsLink"/></a>
		<a href="${pageContext.request.contextPath}/offender/modules.html?offender=${offenderSummary.id}"><fmt:message key="recommendationsLink"/></a>
		<a href="${pageContext.request.contextPath}/assessment/document/list.html?administeredQuestionnaire=${administeredQuestionnaire.id}"><fmt:message key="attachmentsLink"/></a>
		<a href="${pageContext.request.contextPath}/assessment/notes/edit.html?administeredQuestionnaire=${administeredQuestionnaire.id}"><fmt:message key="notesLink"/></a>
	</div>
</fmt:bundle>