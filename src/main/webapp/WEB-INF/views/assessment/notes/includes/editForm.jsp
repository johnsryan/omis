<?xml version="1.0" encoding="UTF-8"?>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<sec:authorize var="editAssessmentNote" access="hasRole('ASSESSMENT_NOTE_EDIT') or hasRole('ADMIN') or hasRole('ASSESSMENT_NOTE_CREATE')"/>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.assessment.msgs.assessmentNotes">
<form:form commandName="assessmentNoteForm" class="editForm">
	<fieldset>
		<span class="fieldGroup">
			<c:set var="assessmentNoteItems" value="${assessmentNoteForm.assessmentNoteItems}" scope="request"/>
			<jsp:include page="assessmentNoteTable.jsp"/>
		</span>
	</fieldset>
	<c:if test="${editAssessmentNote}">
		<p class="buttons">
			<input type="submit" value="<fmt:message key='saveLabel' bundle='${commonBundle}'/>"/>
		</p>
	</c:if>
</form:form>
</fmt:bundle>