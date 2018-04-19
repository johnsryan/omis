<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.assessment.msgs.assessmentQuestionnaire">
<h1>
	<a class="actionMenuItem" id="actionMenuLink" href="${pageContext.request.contextPath}/assessment/questionnaire/assessmentQuestionnaireActionMenu.html?administeredQuestionnaire=${administeredQuestionnaire.id}"></a>
	<fmt:message key="assessmentQuestionnaireHeader"/>
</h1>
<div class="moduleGroupLinkContainer">
	<c:forEach items="${sectionSummaries}" var="sectionSummary">
		<a href="${pageContext.request.contextPath}/assessment/questionnaire/section.html?sectionStatus=${sectionSummary.questionnaireSectionStatusId}">
			<fmt:message key="sectionLink">
				<fmt:param value="${sectionSummary.title}"/>
			</fmt:message>
		</a>
	</c:forEach>
</div>
</fmt:bundle>