<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<fmt:bundle basename="omis.assessment.msgs.assessmentDocument">
	<head>
		<jsp:include page="/WEB-INF/views/common/includes/headerOffenderFormResources.jsp"/>
		<jsp:include page="/WEB-INF/views/common/includes/toolsResources.jsp"/>
		<jsp:include page="/WEB-INF/views/document/includes/documentTagResources.jsp"/>
		<script type="text/javascript" src="${pageContext.request.contextPath}/resources/assessment/scripts/assessmentDocument.js?VERSION=1"></script>
		<title>
			<c:if test="${empty assessmentDocumentAssociation}">
				<fmt:message key="createAssessmentDocumentTitle" />
			</c:if>
			<c:if test="${not empty assessmentDocumentAssociation}">
				<fmt:message key="editAssessmentDocumentTitle" />
			</c:if>
			<jsp:include page="/WEB-INF/views/offender/includes/offenderNameSummary.jsp"/>
		</title>
	</head>
	<body>
		<h1>
			<a href="${pageContext.request.contextPath}/assessment/document/assessmentDocumentActionMenu.html?administeredQuestionnaire=${administeredQuestionnaire.id}" id="actionMenuLink" class="actionMenuItem"></a>
			<c:if test="${empty assessmentDocumentAssociation}">
				<fmt:message key="createAssessmentDocumentTitle" />
			</c:if>
			<c:if test="${not empty assessmentDocumentAssociation}">
				<fmt:message key="editAssessmentDocumentTitle" />
			</c:if>
		</h1>
		<jsp:include page="includes/editForm.jsp"/>
	</body>
</fmt:bundle>
</html>