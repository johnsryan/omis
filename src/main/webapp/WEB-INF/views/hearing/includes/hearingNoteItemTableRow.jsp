<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.hearing.msgs.hearing">
	<tr id="hearingNoteItemRow${hearingNoteItemIndex}" class="hearingNoteItemRow">
		<td>
			<a class="removeLink" id="removeHearingNoteLink${hearingNoteItemIndex}" href="${pageContext.request.contextPath}/hearing/removeHearingNote.html?hearing=${hearing.id}"><span class="linkLabel"><fmt:message key="removeLink" bundle="${commonBundle}"/></span></a>
			<input type="hidden" id="hearingNoteId${hearingNoteItemIndex}" name="hearingNoteItems[${hearingNoteItemIndex}].hearingNote" value="${hearingNoteItem.hearingNote.id}"/>
			<form:errors path="hearingNoteItems[${hearingNoteItemIndex}].hearingNote" cssClass="error"/>
			<input type="hidden" id="hearingNoteOperation${hearingNoteItemIndex}" name="hearingNoteItems[${hearingNoteItemIndex}].itemOperation" value="${hearingNoteItem.itemOperation}"/>
			<form:errors path="hearingNoteItems[${hearingNoteItemIndex}].itemOperation" cssClass="error"/>
		</td>
		<td>
			<fmt:formatDate var="hearingNoteDate" value="${hearingNoteItem.date}" pattern="MM/dd/yyyy"/>
			<input type="text" class="date" name="hearingNoteItems[${hearingNoteItemIndex}].date" id="hearingNoteItemDate${hearingNoteItemIndex}" value="${hearingNoteDate}"/>
			<form:errors path="hearingNoteItems[${hearingNoteItemIndex}].date" cssClass="error"/>
		</td>
		<td>
			<textarea rows="4" name="hearingNoteItems[${hearingNoteItemIndex}].description" id="hearingNoteItems[${hearingNoteItemIndex}].description" style="width: 95%"><c:out value="${hearingNoteItem.description}"/></textarea>
			<form:errors path="hearingNoteItems[${hearingNoteItemIndex}].description" cssClass="error"/>
		</td>
		<td>
			<c:if test="${not empty hearingNoteItem.hearingNote.updateSignature}">
				<label>
					<fmt:message key="lastUpdatedUserName">
						<fmt:param value="${hearingNoteItem.hearingNote.updateSignature.userAccount.user.name.lastName}"/>
						<fmt:param value="${hearingNoteItem.hearingNote.updateSignature.userAccount.user.name.firstName}"/>
					</fmt:message>
				</label>
			</c:if>
		</td>
	</tr>
</fmt:bundle> 