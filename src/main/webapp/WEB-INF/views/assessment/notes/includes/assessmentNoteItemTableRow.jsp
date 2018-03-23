<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.assessment.msgs.assessmentNotes">
	<tr id="assessmentNoteItemRow${assessmentNoteItemIndex}" class="assessmentNoteItemRow">
		<td>
			<a class="removeLink" id="removeAssessmentNoteLink${assessmentNoteItemIndex}" href="${pageContext.request.contextPath}/assessment/removeAssessmentNote.html?assessment=${assessment.id}"><span class="linkLabel"><fmt:message key="removeLink" bundle="${commonBundle}"/></span></a>
			<input type="hidden" id="assessmentNoteId${assessmentNoteItemIndex}" name="assessmentNoteItems[${assessmentNoteItemIndex}].assessmentNote" value="${assessmentNoteItem.assessmentNote.id}"/>
			<form:errors path="assessmentNoteItems[${assessmentNoteItemIndex}].assessmentNote" cssClass="error"/>
			<input type="hidden" id="assessmentNoteOperation${assessmentNoteItemIndex}" name="assessmentNoteItems[${assessmentNoteItemIndex}].itemOperation" value="${assessmentNoteItem.itemOperation}"/>
			<form:errors path="assessmentNoteItems[${assessmentNoteItemIndex}].itemOperation" cssClass="error"/>
		</td>
		<td>
			<fmt:formatDate var="assessmentNoteDate" value="${assessmentNoteItem.date}" pattern="MM/dd/yyyy"/>
			<input type="text" class="date" name="assessmentNoteItems[${assessmentNoteItemIndex}].date" id="assessmentNoteItemDate${assessmentNoteItemIndex}" value="${assessmentNoteDate}"/>
			<form:errors path="assessmentNoteItems[${assessmentNoteItemIndex}].date" cssClass="error"/>
		</td>
		<td>
			<textarea rows="4" name="assessmentNoteItems[${assessmentNoteItemIndex}].description" id="assessmentNoteItems[${assessmentNoteItemIndex}].description" style="width: 95%"><c:out value="${assessmentNoteItem.description}"/></textarea>
			<form:errors path="assessmentNoteItems[${assessmentNoteItemIndex}].description" cssClass="error"/>
		</td> 
		<td>
			<c:if test="${not empty assessmentNoteItem.assessmentNote.updateSignature}">
				<label>
					<fmt:message key="lastUpdatedUserName">
						<fmt:param value="${assessmentNoteItem.assessmentNote.updateSignature.userAccount.user.name.lastName}"/>
						<fmt:param value="${assessmentNoteItem.assessmentNote.updateSignature.userAccount.user.name.firstName}"/>
					</fmt:message>
				</label>
			</c:if>
		</td>
	</tr>
</fmt:bundle> 