<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:bundle basename="omis.assessment.msgs.assessmentNotes">
<table id="assessmentNoteTable" class="editTable">
	<thead>
		<tr>
			<th class="operations"><a class="actionMenuItem" id="assessmentNoteItemsActionMenuLink" href="${pageContext.request.contextPath}/assessment/notes/assessmentNoteItemsActionMenu.html?assessmentNoteItemIndex=${assessmentNoteItemIndex}"></a></th>
			<th><fmt:message key="dateLabel"/></th>
			<th><fmt:message key="descriptionLabel"/></th>
			<th><fmt:message key="updatedByLabel"/></th>
		</tr>
	</thead>
	<tbody id="assessmentNoteTableBody">
		<jsp:include page="assessmentNoteTableBodyContent.jsp"/>
	</tbody>
</table>
</fmt:bundle>