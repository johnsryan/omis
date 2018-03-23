<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:bundle basename="omis.hearing.msgs.hearing">
<table id="hearingNoteTable" class="editTable">
	<thead class="notes">
		<tr>
			<th class="operations"><a class="actionMenuItem" id="hearingNoteItemsActionMenuLink" href="${pageContext.request.contextPath}/hearing/hearingNoteItemsActionMenu.html?hearingNoteItemIndex=${hearingNoteItemIndex}"></a></th>
			<th class="date"><fmt:message key="dateLabel"/></th>
			<th><fmt:message key="descriptionLabel"/></th>
			<th><fmt:message key="updatedByLabel"/></th>
		</tr>
	</thead>
	<tbody id="hearingNoteTableBody">
		<jsp:include page="hearingNoteTableBodyContent.jsp"/>
	</tbody>
</table>
</fmt:bundle>