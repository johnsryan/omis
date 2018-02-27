<?xml version="1.0" encoding="UTF-8"?>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<fmt:bundle basename="omis.assessment.msgs.assessmentHome">
	<div id="assessmentModuleGroup" class="moduleGroupLinkContainer">
		<a href="${pageContext.request.contextPath}/offender/profile.html?offender=${offenderSummary.id}"><fmt:message key="scoresRatingsLink"/></a>
		<a href="${pageContext.request.contextPath}/offender/modules.html?offender=${offenderSummary.id}"><fmt:message key="recommendationsLink"/></a>
		<a href="${pageContext.request.contextPath}/offender/personalDetails/edit.html?offender=${offenderSummary.id}"><fmt:message key="attachmentsLink"/></a>
		<a href="${pageContext.request.contextPath}/offender/name/alternative/list.html?offender=${offenderSummary.id}"><fmt:message key="notesLink"/></a>
	</div>
</fmt:bundle>