<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<sec:authorize var="editQuestionnaire" access="hasRole('QUESTIONNAIRE_CREATE') or hasRole('ADMIN')"/>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.questionnaire.msgs.questionnaire">
<form:form commandName="questionnaireReviewForm" class="editForm">
	<c:forEach var="sectionQuestionSummary" items="${sectionQuestionSummaries}" varStatus="currentSection">
		<c:set var="sectionDraftError"><form:errors path="sectionReviewItems[${currentSection.index}].sectionStatus" /></c:set>
		<c:if test="${not empty sectionDraftError}">
			<br>
			<span class="error">
				<c:out value="${sectionQuestionSummary.key.number}."/>&nbsp;
			</span>
			<form:errors path="sectionReviewItems[${currentSection.index}].sectionStatus" cssClass="error" />
		</c:if>
	</c:forEach>
	
	<c:forEach var="sectionQuestionSummary" items="${sectionQuestionSummaries}" varStatus="currentSection">
		<br/>
		<h1>
			<a class="actionMenuItem" id="actionMenuLink${currentSection.index}" href="${pageContext.request.contextPath}/assessment/questionnaire/reviewSectionActionMenu.html?sectionStatus=${questionnaireReviewForm.sectionReviewItems[currentSection.index].sectionStatus.id}"></a>
			<fmt:message key="sectionTitle">
				<fmt:param value="${sectionQuestionSummary.key.number}" />
				<fmt:param value="${sectionQuestionSummary.key.title}" />
			</fmt:message>
		</h1>
		<input type="hidden" name="sectionReviewItems[${currentSection.index}].sectionStatus" value="${questionnaireReviewForm.sectionReviewItems[currentSection.index].sectionStatus.id}"/>
		
		<form:errors path="sectionReviewItems[${currentSection.index}].sectionStatus" cssClass="error" />
		<br>
		<c:set var="sectionId" value="${sectionQuestionSummary.key.questionnaireSectionId}" scope="request"/>
		<c:set var="questionAnswerItems" value="${questionnaireReviewForm.sectionReviewItems[currentSection.index].questionAnswerItems}" scope="request"/>
		<c:set var="sectionNotes" value="${questionnaireReviewForm.sectionReviewItems[currentSection.index].sectionNotes}" scope="request"/>
		<c:set var="sectionIndex" value="${currentSection.index}" scope="request"/>
		<c:set var="sectionQuestionSummary" value="${sectionQuestionSummary}" scope="request" />
		<jsp:include page="../../../questionnaire/includes/reviewSectionTable.jsp"/>
	</c:forEach>
	
	<c:if test="${editQuestionnaire && administeredQuestionnaireSummary.draft}">
		<p class="buttons">
			<input type="submit" id="saveAsFinal" value="<fmt:message key='finalizeQuestionnaireLabel'/>"/>
		</p>
	</c:if>
</form:form>
</fmt:bundle>