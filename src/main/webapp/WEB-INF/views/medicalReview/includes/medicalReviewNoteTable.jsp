<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:bundle basename="omis.medicalreview.msgs.medicalReview">
<table id="medicalReviewNoteTable" class="editTable">
	<thead>
		<tr>
			<th class="operations"><a class="actionMenuItem" id="medicalReviewNoteItemsActionMenuLink" href="${pageContext.request.contextPath}/medicalReview/medicalReviewNoteItemsActionMenu.html?medicalReviewNoteItemIndex=${medicalReviewNoteItemIndex}"></a></th>
			<th><fmt:message key="dateLabel"/></th>
			<th><fmt:message key="descriptionLabel"/></th>
			<th><fmt:message key="updatedByLabel"/></th>
		</tr>
	</thead>
	<tbody id="medicalReviewNoteTableBody">
		<jsp:include page="medicalReviewNoteTableBodyContent.jsp"/>
	</tbody>
</table>
</fmt:bundle>