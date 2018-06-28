<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<fmt:setBundle var="commonBundle" basename="omis.msgs.common"/>
<fmt:bundle basename="omis.travelpermit.msgs.form">
<tr id="travelPermitNoteRows[${travelPermitNoteItemIndex}].row" class="${travelPermitNoteItemClass}">	
	<td class="operation">
		<input type="hidden" name="travelPermitNoteItems[${travelPermitNoteItemIndex}].operation" id="travelPermitNoteItems[${travelPermitNoteItemIndex}].operation" value="${travelPermitNoteItem.operation}"/>
		<a class="removeLink"  id="removeNote[${travelPermitNoteItemIndex}].removeLink" href="${pageContext.request.contextPath}/remove.html?itemIndex=${travelPermitNoteItemIndex}&offener=${offender}"></a>
		<input type="hidden" id="travelPermitNoteRow[${travelPermitNoteItemIndex}].travelPermitNote" name="travelPermitNoteItems[${travelPermitNoteItemIndex}].travelPermitNote" value="${travelPermitNoteItem.travelPermitNote.id}"/>
	</td>
	<td>
		<input id="travelPermitNoteRow[${travelPermitNoteItemIndex}].date" name="travelPermitNoteItems[${travelPermitNoteItemIndex}].date" class="date" value="<fmt:formatDate value='${travelPermitNoteItem.date}' pattern='MM/dd/yyyy'/>"/>
		<form:errors path="travelPermitNoteItems[${travelPermitNoteItemIndex}].date" cssClass="error"/>
	</td>
	<td id="travelPermitNoteRow[${travelPermitNoteItemIndex}].updateSignature">
		<c:if test="${!createTravelPermit && not empty travelPermitNoteItem.updateSignature}">
			<c:out value="${travelPermitNoteItem.updateSignature.userAccount.user.name.lastName},"/> <c:out value="${travelPermitNoteItem.updateSignature.userAccount.user.name.firstName}"/> <c:out value="("/><c:out value="${travelPermitNoteItem.updateSignature.userAccount.username}"/><c:out value=")"/>
		</c:if>
	</td>
	<td>
		<textarea id="travelPermitNoteRow[${travelPermitNoteItemIndex}].note" name="travelPermitNoteItems[${travelPermitNoteItemIndex}].note"><c:out value="${travelPermitNoteItem.note}"/></textarea>
		<form:errors path="travelPermitNoteItems[${travelPermitNoteItemIndex}].note" cssClass="error"/>
	</td>
</tr>
</fmt:bundle>