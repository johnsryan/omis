<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<fmt:setBundle var="commonBundle" basename="omis.msgs.common"/>
<fmt:bundle basename="omis.travelpermit.msgs.form">
<tr id="travelPermitNoteRows[${travelPermitNoteItemIndex}].row" class="${travelPermitNoteItemClass}">	
	<td>
		<a class="removeLink"  id="removeNote[${travelPermitNoteItemIndex}].removeLink" href="${pageContext.request.contextPath}/remove.html?itemIndex=${travelPermitNoteItemIndex}&offener=${offender}">
		<span class="linkLabel"><fmt:message key="removeTravelPermitNoteLink"/></span></a>
		<!-- <input type="hidden" name="travelPermitNoteItems[${travelPermitNoteItemIndex}].id" id="travelPermitNoteItems${travelPermitNoteItemIndex}Id" value="${travelPermitNoteItem.id}"/>  -->
		<input type="hidden" name="travelPermitNoteItems[${travelPermitNoteItemIndex}].operation" id="travelPermitNoteItems[${travelPermitNoteItemIndex}].operation" value="${travelPermitNoteItem.operation}"/>
		<!--<input type="hidden" name="travelPermitNoteItems[${travelPermitNoteItemIndex}].userAccount" id="travelPermitNoteItemsUserAccount${travelPermitNoteItemIndex}UserAccount" value="${travelPermitNoteItem.useraccount}"/>
		<input type="hidden" name="travelPermitNoteItems[${travelPermitNoteItemIndex}].note" id="travelPermitNoteItemsUserAccount${travelPermitNoteItemIndex}Note" value="${travelPermitNoteItem.note}"/>
		<input type="hidden" name="travelPermitNoteItems[${travelPermitNoteItemIndex}].date" id="travelPermitNoteItemsUserAccount${travelPermitNoteItemIndex}Date" value="${travelPermitNoteItem.date}"/>-->
		<c:set var="note" value="travelPermitNoteItems[${travelPermitNoteItemIndex}].note" scope="request"/>
		<c:set var="userAccount" value="travelPermitNoteItems[${travelPermitNoteItemIndex}].userAccount" scope="request"/>
		<c:set var="date" value="travelPermitNoteItems[${travelPermitNoteItemIndex}].note" scope="request"/>
	</td>
	<td>
		<input id="travelPermitNoteRow[${travelPermitNoteItemIndex}].date" name="travelPermitNoteRow[${travelPermitNoteItemIndex}].date" class="date" value="<fmt:formatDate value='${travelPermitNoteItem.note.date}' pattern='MM/dd/yyyy'/>"/>
		<form:errors path="travelPermitNoteRow[${travelPermitNoteItemIndex}].date" cssClass="error"/>
	</td>
	<td>
		<c:if test="${prepareEditMav}">
			<c:out value="${travelPermitNoteItem.note.updateSignature.userAccount.user.name.lastName},"/> <c:out value="${travelPermitNoteItem.note.updateSignature.userAccount.user.name.firstName}"/> <c:out value="("/><c:out value="${travelPermitNoteItem.note.updateSignature.userAccount.username}"/><c:out value=")"/>
		</c:if>
	</td>
	<td>
		<textarea id="travelPermitNoteRow[${travelPermitNoteItemIndex}].value" name="travelPermitNoteRow[${travelPermitNoteItemIndex}].value"><c:out value="${travelPermitNoteItem.note.value}"/></textarea>
		<form:errors path="travelPermitNoteRow[${travelPermitNoteItemIndex}].value" cssClass="error"/>
	</td>
</tr>
</fmt:bundle>