<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<sec:authorize var="editQuestionnaire" access="hasRole('QUESTIONNAIRE_CREATE') or hasRole('ADMIN')"/>
<c:set var="draft" value="${questionnaireSectionStatus.draft}" />
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.questionnaire.msgs.questionnaire">
<form:form commandName="administeredQuestionnaireSectionForm" class="editForm">
<fieldset>
	<span class="fieldGroup">
		<c:forEach var="questionAnswerSummary" items="${questionAnswerSummaries}" varStatus="currentQuestion">
			<c:set var="questionErrorsItems"><form:errors path="questionAnswerItems[${currentQuestion.index}].administeredQuestionValueItems" /></c:set>
			<c:set var="questionErrorsSingle"><form:errors path="questionAnswerItems[${currentQuestion.index}].answerValue" /></c:set>
			<c:set var="questionErrorsText"><form:errors path="questionAnswerItems[${currentQuestion.index}].answerValueText" /></c:set>
			<c:choose>
				<c:when test="${not empty questionErrorsText && not empty questionErrorsItems}">
					<span class="error">
						<c:out value="${questionAnswerSummary.key.questionNumber}."/>&nbsp;
					</span>
					<form:errors path="questionAnswerItems[${currentQuestion.index}].administeredQuestionValueItems" cssClass="error" />
					<span class="error"><c:out value="," />&nbsp;</span>
					<form:errors path="questionAnswerItems[${currentQuestion.index}].answerValueText" cssClass="error" />
					<br>
				</c:when>
				<c:when test="${not empty questionErrorsText && not empty questionErrorsSingle}">
					<span class="error">
						<c:out value="${questionAnswerSummary.key.questionNumber}."/>&nbsp;
					</span>
					<form:errors path="questionAnswerItems[${currentQuestion.index}].answerValue" cssClass="error" />
					<span class="error"><c:out value="," />&nbsp;</span>
					<form:errors path="questionAnswerItems[${currentQuestion.index}].answerValueText" cssClass="error" />
					<br>
				</c:when>
				<c:when test="${not empty questionErrorsItems}">
					<span class="error">
						<c:out value="${questionAnswerSummary.key.questionNumber}."/>&nbsp;
					</span>
					<form:errors path="questionAnswerItems[${currentQuestion.index}].administeredQuestionValueItems" cssClass="error" />
					<br>
				</c:when>
				<c:when test="${not empty questionErrorsSingle}">
					<span class="error">
						<c:out value="${questionAnswerSummary.key.questionNumber}."/>&nbsp;
					</span>
					<form:errors path="questionAnswerItems[${currentQuestion.index}].answerValue" cssClass="error" />
					<br>
				</c:when>
				<c:when test="${not empty questionErrorsText}">
					<span class="error">
						<c:out value="${questionAnswerSummary.key.questionNumber}."/>&nbsp;
					</span>
					<form:errors path="questionAnswerItems[${currentQuestion.index}].answerValueText" cssClass="error" />
					<br>
				</c:when>
			</c:choose>
		</c:forEach>
	</span>
	<c:forEach var="questionAnswerSummary" items="${questionAnswerSummaries}" varStatus="currentQuestion">
			<form:errors path="questionAnswerItems[${currentQuestion.index}].answerValue" cssClass="error" />
			<form:errors path="questionAnswerItems[${currentQuestion.index}].administeredQuestionValueItems" cssClass="error" />	
			<span class="fieldGroup">
				<span title="${questionAnswerSummary.key.questionHelpText}">
					<label class="fieldLabel">
						<c:out value="${questionAnswerSummary.key.questionNumber}. ${questionAnswerSummary.key.questionText}"/>
					</label>
				</span>
				<c:choose>
					<c:when test="${not empty administeredQuestionnaireSectionForm.questionAnswerItems[currentQuestion.index].comments}">
						<c:set var="hiddenOrBlock" value="block" />
						<c:set var="imgClass" value="hideComments" />
					</c:when>
					<c:otherwise>
						<c:set var="hiddenOrBlock" value="none" />
						<c:set var="imgClass" value="showComments" />
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${questionAnswerItems[currentQuestion.index].addComments}">
						<c:set var="addComments" value="true" />
					</c:when>
					<c:otherwise>
						<c:set var="addComments" value="false" />
					</c:otherwise>
				</c:choose>
				<span class="addComments">
					<form:input type="hidden" path="questionAnswerItems[${currentQuestion.index}].addComments" 
						name="addComments[${currentQuestion.index}]" 
						id="addComments[${currentQuestion.index}]" value="${addComments}" />
					<label for="addComments[${currentQuestion.index}]" 
						id="addCommentsLbl[${currentQuestion.index}]">
						<span id="addCommentsImg[${currentQuestion.index}]" class="${imgClass}"></span>
						<fmt:message key="addCommentsLabel" />
					</label>
				</span>
			</span>
			<form:input type="hidden" path="questionAnswerItems[${currentQuestion.index}].allowedQuestion" value="${questionAnswerSummary.key.allowedQuestionId}"/>
			<form:input type="hidden" path="questionAnswerItems[${currentQuestion.index}].answerCardinality" value="${questionAnswerSummary.key.answerCardinality}" />
			<form:input type="hidden" path="questionAnswerItems[${currentQuestion.index}].administeredQuestionValue"/>
			<c:choose>
				<c:when test="${questionAnswerSummary.key.questionCategory eq 'TRUE_FALSE'}">
					<c:forEach var="answerSummary" items="${questionAnswerSummary.value}"> 
						<span class="fieldGroup">
							<form:radiobutton class="fieldOption" path="questionAnswerItems[${currentQuestion.index}].answerValue"
							name="answerValue"  value="${answerSummary.answerValueId}"
								checked="${answerSummary.answerValueId == 
									administeredQuestionnaireSectionForm.questionAnswerItems[currentQuestion.index].answerValue.id 
										? 'checked' : ''}" />
							<form:label path="questionAnswerItems[${currentQuestion.index}].answerValue">
								<c:out value="${answerSummary.description}" />
							</form:label>
						</span>
					</c:forEach>
				</c:when>
				<c:when test="${questionAnswerSummary.key.questionCategory eq 'MULTIPLE_CHOICE'}">
					<c:choose>
						<c:when test="${questionAnswerSummary.key.answerCardinality eq 'SINGLE'}">
							<c:forEach var="answerSummary" items="${questionAnswerSummary.value}"> 
								<span class="fieldGroup">
									<form:radiobutton class="fieldOption" path="questionAnswerItems[${currentQuestion.index}].answerValue"
									name="answerValue"  value="${answerSummary.answerValueId}" 
									checked="${answerSummary.answerValueId == 
										administeredQuestionnaireSectionForm.questionAnswerItems[currentQuestion.index].answerValue.id 
											? 'checked' : ''}" />
									<form:label path="questionAnswerItems[${currentQuestion.index}].answerValue">
										<c:out value="${answerSummary.description}" />
									</form:label>
								</span>
							</c:forEach>
						</c:when>
						<c:when test="${questionAnswerSummary.key.answerCardinality eq 'MULTIPLE'}">
							<c:forEach var="answerSummary" items="${questionAnswerSummary.value}" varStatus="answerNumber"> 
								<c:set var="useChecked" value="" />
								<c:forEach var="answerValueItem" items="${administeredQuestionnaireSectionForm.questionAnswerItems[currentQuestion.index].administeredQuestionValueItems}">
									<c:if test="${answerSummary.answerValueId == answerValueItem.answerValue.id}">
										<c:set var="useChecked" value="checked" />
									</c:if>
								</c:forEach>
								<form:input type="hidden" path="questionAnswerItems[${currentQuestion.index}].administeredQuestionValueItems[${answerNumber.index}].administeredQuestionValue" />
								<span class="fieldGroup">
									<form:checkbox class="fieldOption" path="questionAnswerItems[${currentQuestion.index}].administeredQuestionValueItems[${answerNumber.index}].answerValue"
									name="answerValues" value="${answerSummary.answerValueId}" checked="${useChecked}" />
									<form:label path="questionAnswerItems[${currentQuestion.index}].administeredQuestionValueItems[${answerNumber.index}].answerValue">
										<c:out value="${answerSummary.description}" />
									</form:label>
								</span>
							</c:forEach>
						</c:when>
					</c:choose>
				</c:when>
				<c:when test="${questionAnswerSummary.key.questionCategory eq 'MULTIPLE_CHOICE_ESSAY'}">
					<c:choose>
						<c:when test="${questionAnswerSummary.key.answerCardinality eq 'SINGLE'}">
							<c:forEach var="answerSummary" items="${questionAnswerSummary.value}"> 
								<span class="fieldGroup">
									<form:radiobutton class="fieldOption" path="questionAnswerItems[${currentQuestion.index}].answerValue"
									name="answerValue"  value="${answerSummary.answerValueId}"
									checked="${answerSummary.answerValueId == 
										administeredQuestionnaireSectionForm.questionAnswerItems[currentQuestion.index].answerValue.id 
											? 'checked' : ''}" />
									<form:label path="questionAnswerItems[${currentQuestion.index}].answerValue">
										<c:out value="${answerSummary.description}" />
									</form:label>
								</span>
							</c:forEach>
						</c:when>
						<c:when test="${questionAnswerSummary.key.answerCardinality eq 'MULTIPLE'}">
							<c:forEach var="answerSummary" items="${questionAnswerSummary.value}" varStatus="answerNumber"> 
								<c:set var="useChecked" value="" />
								<c:forEach var="answerValueItem" items="${administeredQuestionnaireSectionForm.questionAnswerItems[currentQuestion.index].administeredQuestionValueItems}">
									<c:if test="${answerSummary.answerValueId == answerValueItem.answerValue.id}">
										<c:set var="useChecked" value="checked" />
									</c:if>
								</c:forEach>
								<form:input type="hidden" path="questionAnswerItems[${currentQuestion.index}].administeredQuestionValueItems[${answerNumber.index}].administeredQuestionValue" />
								<span class="fieldGroup">
									<form:checkbox class="fieldOption" path="questionAnswerItems[${currentQuestion.index}].administeredQuestionValueItems[${answerNumber.index}].answerValue"
									name="answerValue" value="${answerSummary.answerValueId}" checked="${useChecked}" />
									<form:label path="questionAnswerItems[${currentQuestion.index}].administeredQuestionValueItems[${answerNumber.index}].answerValue">
										<c:out value="${answerSummary.description}" />
									</form:label>
								</span>
							</c:forEach>
						</c:when>
					</c:choose>
					<span class="fieldGroup">
						<form:errors path="questionAnswerItems[${currentQuestion.index}].answerValueText" cssClass="error" />
						<br>
						<form:textarea path="questionAnswerItems[${currentQuestion.index}].answerValueText" maxLength="1024" class="textValue"/>
					</span>
					<span class="textValueDisplay"></span>
				</c:when>
				<c:when test="${questionAnswerSummary.key.questionCategory eq 'ESSAY'}">
					<span class="fieldGroup">
						<form:errors path="questionAnswerItems[${currentQuestion.index}].answerValueText" cssClass="error" />
						<br>
						<form:textarea path="questionAnswerItems[${currentQuestion.index}].answerValueText" maxLength="1024" class="textValue"/>
					</span>
					<span class="textValueDisplay"></span>
				</c:when>
				<c:when test="${questionAnswerSummary.key.questionCategory eq 'SELECT_FROM_LIST'}">
					<span class="shortInput">
						<form:select path="questionAnswerItems[${currentQuestion.index}].answerValue">
							<jsp:include page="../../includes/nullOption.jsp"/>
							<c:forEach var="answerSummary" items="${questionAnswerSummary.value}"> 
								<option value="${answerSummary.answerValueId}"
								 ${answerSummary.answerValueId == administeredQuestionnaireSectionForm.questionAnswerItems[currentQuestion.index].answerValue.id ? 'selected="selected"' : ''}>
									<c:out value="${answerSummary.description}"/>
								</option>
							</c:forEach>
						</form:select>
					</span>
				</c:when>
				<c:when test="${questionAnswerSummary.key.questionCategory eq 'DATE'}">
					<span class="shortInput">
						<form:input path="questionAnswerItems[${currentQuestion.index}].answerValueText" class="date"/>
						<form:errors path="questionAnswerItems[${currentQuestion.index}].answerValueText" cssClass="error"/>
					</span>
				</c:when>
				<c:when test="${questionAnswerSummary.key.questionCategory eq 'PHONE_NUMBER'}" >
					<span class="shortInput">
						<form:input path="questionAnswerItems[${currentQuestion.index}].answerValueText" class="shortNumber numeric phoneNumber" />
						<form:errors path="questionAnswerItems[${currentQuestion.index}].answerValueText" cssClass="error"/>
					</span>
				</c:when>
				<c:when test="${questionAnswerSummary.key.questionCategory eq 'CURRENCY'}" >
					<span class="shortInput">
						<c:out value="$"/><form:input path="questionAnswerItems[${currentQuestion.index}].answerValueText" class="shortNumber numeric currency" />
						<form:errors path="questionAnswerItems[${currentQuestion.index}].answerValueText" cssClass="error"/>
					</span>
				</c:when>
				<c:when test="${questionAnswerSummary.key.questionCategory eq 'WHOLE_NUMBER'}" >
					<span class="shortInput">
						<form:input path="questionAnswerItems[${currentQuestion.index}].answerValueText" class="shortNumber numeric wholeNumber" />
						<form:errors path="questionAnswerItems[${currentQuestion.index}].answerValueText" cssClass="error"/>
					</span>
				</c:when>
				<c:when test="${questionAnswerSummary.key.questionCategory eq 'DECIMAL_NUMBER'}" >
					<span class="shortInput">
						<form:input path="questionAnswerItems[${currentQuestion.index}].answerValueText" class="shortNumber numeric decimalNumber" />
						<form:errors path="questionAnswerItems[${currentQuestion.index}].answerValueText" cssClass="error"/>
					</span>
				</c:when>
				<c:when test="${questionAnswerSummary.key.questionCategory eq 'SHORT_ANSWER'}">
					<span class="shortInput">
						<form:input path="questionAnswerItems[${currentQuestion.index}].answerValueText" class="textValue" maxLength="25"/>
						<form:errors path="questionAnswerItems[${currentQuestion.index}].answerValueText" cssClass="error"/>
					</span>
					<span class="textValueDisplay"></span>
				</c:when>
			</c:choose>
			<span id="commentsInput[${currentQuestion.index}]" style="display: ${hiddenOrBlock};">
				<br>
				<span class="fieldGroup">
					<form:label path="questionAnswerItems[${currentQuestion.index}].comments">
						<fmt:message key="questionCommentsLabel"/>
					</form:label>
				</span>
				<span class="fieldGroup">
					<form:textarea path="questionAnswerItems[${currentQuestion.index}].comments" maxLength="1024" class="textValue"/>
				</span>
				<span class="textValueDisplay" ></span>
			</span>
			<span class="whiteSpace"></span>	
	</c:forEach>
	<span class="fieldGroup">
		<form:label path="sectionComments">
			<fmt:message key="sectionCommentsLabel"/>
		</form:label>
	</span>
	<span class="fieldGroup">
		<form:textarea path="sectionComments" class="textValue" maxLength="2048" />
	</span>
	<span class="textValueDisplay"></span>
	<c:if test="${editQuestionnaire && draft}">
		<p class="buttons">
			<input id="saveAsFinal" type="submit" value="<fmt:message key='saveAsFinalLabel'/>" name="final"/>
			<input id="saveAsDraft" type="submit" value="<fmt:message key='saveAsDraftLabel' />" name="draft" />
		</p>
	</c:if>
</fieldset>
</form:form>
</fmt:bundle>
