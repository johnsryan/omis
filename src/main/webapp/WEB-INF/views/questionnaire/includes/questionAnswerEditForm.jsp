<%@page import="java.util.List"%>
<%@page import="omis.questionnaire.web.form.QuestionAnswerEditItem"%>
<%@page import="java.util.Comparator"%>
<%@page import="java.util.Collections"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<sec:authorize var="editQuestionnaire" access="hasRole('QUESTIONNAIRE_CREATE') or hasRole('ADMIN')"/>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.questionnaire.msgs.questionnaire">
<form:form commandName="questionAnswerEditForm" class="editForm">
	<c:set var="questionAnswerEditItems" value="${questionAnswerEditForm.questionAnswerEditItems}" scope="request"/>
	<div id = "questionAnswerEditItemsBody">
		<c:forEach var="questionAnswerEditItem" items="${questionAnswerEditItems}" varStatus="status">
			<c:set var="questionAnswerEditItem" value="${questionAnswerEditItem}" scope="request"/>
			<c:set var="questionAnswerEditItemIndex" value="${status.index}" scope="request"/>
			<c:if test="${not empty questionAnswerEditItem.operation}">
				<jsp:include page="questionAnswerEditItemContent.jsp"/>
			</c:if>
		</c:forEach>
	</div>
	<span class="fieldGroup">
		<label class="fieldLabel">
			<a id="questionAddItemLink[last]" class="questionAddItemLink">
				<fmt:message key="addQuestionLink"/>
			</a>
		</label>
	</span>
	<c:if test="${not empty questionnaireSection}">
		<c:set var="updatable" value="${questionnaireSection}" scope="request"/>
		<jsp:include page="/WEB-INF/views/audit/includes/updateSignature.jsp"/>
	</c:if>
	<c:if test="${editQuestionnaire}">
		<p class="buttons">
			<input type="submit" value="<fmt:message key='saveLabel' bundle='${commonBundle}'/>"/>
		</p>
	</c:if>
</form:form>
</fmt:bundle>