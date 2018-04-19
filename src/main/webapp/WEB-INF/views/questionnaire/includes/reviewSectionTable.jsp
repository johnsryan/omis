<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<fmt:bundle basename="omis.questionnaire.msgs.questionnaire">
	<table id="questionnaireSectionReviewTable[${sectionIndex}]" class="listTable">
		<thead>
			<tr>
				<th><fmt:message key="questionLabel"/></th>
				<th><fmt:message key="answerLabel"/></th>
				<th><fmt:message key="responseLabel"/></th>
				<th><fmt:message key="questionCommentsLabel"/></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="questionSummary" items="${sectionQuestionSummary.value}" varStatus="currentQuestion">
			<tr>
				<td>
					<span title="${questionSummary.questionHelpText}">
						<c:out value="${questionSummary.questionNumber}. ${questionSummary.questionText}"/>
					</span>
				</td>
				<td>
					<c:out value="${questionAnswerItems[currentQuestion.index].answerValue.description}"/>
					<c:forEach var="answers" items="${questionAnswerItems[currentQuestion.index].administeredQuestionValueItems}" varStatus="currentAnswer">
							<c:set var="commaTest" value="${fn:length(questionAnswerItems[currentQuestion.index].administeredQuestionValueItems) gt (currentAnswer.index+1)}" />
							<c:out value="${answers.answerValue.description}"/><c:if test="${commaTest}"><c:out value=","/></c:if>
					</c:forEach>
				</td>
				<td>
					<c:out value="${questionAnswerItems[currentQuestion.index].answerValueText}" />
				</td>
				<td>
					<c:out value="${questionAnswerItems[currentQuestion.index].comments}" />
				</td>
				</tr>
			</c:forEach>
			<c:if test="${not empty sectionNotes}">
				<tr>
					<td>
						<fmt:message key="sectionCommentsLabel"/>
					</td>
					<td colspan="3">
						<c:out value="${sectionNotes}" />
					</td>
				</tr>
			</c:if>
		</tbody>
	</table>
</fmt:bundle>