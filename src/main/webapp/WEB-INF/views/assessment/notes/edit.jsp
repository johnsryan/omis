<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<fmt:bundle basename="omis.assessment.msgs.assessmentNotes">
	<head>
		<jsp:include page="/WEB-INF/views/common/includes/headerOffenderFormResources.jsp"/>
		<jsp:include page="/WEB-INF/views/common/includes/toolsResources.jsp"/>
		<script type="text/javascript" src="${pageContext.request.contextPath}/resources/assessment/scripts/includes/jquery.omis.assessmentNotes.js?VERSION=1"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/resources/assessment/scripts/assessmentNotes.js?VERSION=1"></script>
		<script type="text/javascript">
			var currentAssessmentNoteItemIndex = ${assessmentNoteItemIndex};
		</script>
		<title>
			<fmt:message key="editAssessmentNotesTitle" />
		</title>
	</head>
	<body>
		<h1>
			<fmt:message key="editAssessmentNotesTitle" />
		</h1>
		<jsp:include page="includes/editForm.jsp"/>
	</body>
</fmt:bundle>
</html>