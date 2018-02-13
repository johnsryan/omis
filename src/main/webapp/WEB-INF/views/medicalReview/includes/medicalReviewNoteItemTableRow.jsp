<%--
 - OMIS - Offender Management Information System
 - Copyright (C) 2011 - 2017 State of Montana
 -
 - This program is free software: you can redistribute it and/or modify
 - it under the terms of the GNU General Public License as published by
 - the Free Software Foundation, either version 3 of the License, or
 - (at your option) any later version.
 -
 - This program is distributed in the hope that it will be useful,
 - but WITHOUT ANY WARRANTY; without even the implied warranty of
 - MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 - GNU General Public License for more details.
 -
 - You should have received a copy of the GNU General Public License
 - along with this program.  If not, see <http://www.gnu.org/licenses/>.
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.medicalreview.msgs.medicalReview">
	<tr id="medicalReviewNoteItemRow${medicalReviewNoteItemIndex}" class="medicalReviewNoteItemRow">
		<td>
			<a class="removeLink" id="removeMedicalReviewNoteLink${medicalReviewNoteItemIndex}" href="${pageContext.request.contextPath}/medicalReview/removeMedicalReviewNote.html?medicalReview=${medicalReview.id}"><span class="linkLabel"><fmt:message key="removeLink" bundle="${commonBundle}"/></span></a>
			<input type="hidden" id="medicalReviewNoteId${medicalReviewNoteItemIndex}" name="medicalReviewNoteItems[${medicalReviewNoteItemIndex}].medicalReviewNote" value="${medicalReviewNoteItem.medicalReviewNote.id}"/>
			<form:errors path="medicalReviewNoteItems[${medicalReviewNoteItemIndex}].medicalReviewNote" cssClass="error"/>
			<input type="hidden" id="medicalReviewNoteOperation${medicalReviewNoteItemIndex}" name="medicalReviewNoteItems[${medicalReviewNoteItemIndex}].itemOperation" value="${medicalReviewNoteItem.itemOperation}"/>
			<form:errors path="medicalReviewNoteItems[${medicalReviewNoteItemIndex}].itemOperation" cssClass="error"/>
		</td>
		<td>
			<fmt:formatDate var="medicalReviewNoteDate" value="${medicalReviewNoteItem.date}" pattern="MM/dd/yyyy"/>
			<input type="text" class="date" name="medicalReviewNoteItems[${medicalReviewNoteItemIndex}].date" id="medicalReviewNoteItemDate${medicalReviewNoteItemIndex}" value="${medicalReviewNoteDate}"/>
			<form:errors path="medicalReviewNoteItems[${medicalReviewNoteItemIndex}].date" cssClass="error"/>
		</td>
		<td>
			<textarea rows="4" name="medicalReviewNoteItems[${medicalReviewNoteItemIndex}].description" id="medicalReviewNoteItems[${medicalReviewNoteItemIndex}].description" style="width: 95%"><c:out value="${medicalReviewNoteItem.description}"/></textarea>
			<form:errors path="medicalReviewNoteItems[${medicalReviewNoteItemIndex}].description" cssClass="error"/>
		</td> 
		<td>
			<c:if test="${not empty medicalReviewNoteItem.medicalReviewNote.updateSignature}">
				<label>
					<fmt:message key="lastUpdatedUserName">
						<fmt:param value="${medicalReviewNoteItem.medicalReviewNote.updateSignature.userAccount.user.name.lastName}"/>
						<fmt:param value="${medicalReviewNoteItem.medicalReviewNote.updateSignature.userAccount.user.name.firstName}"/>
					</fmt:message>
				</label>
			</c:if>
		</td>
	</tr>
</fmt:bundle> 