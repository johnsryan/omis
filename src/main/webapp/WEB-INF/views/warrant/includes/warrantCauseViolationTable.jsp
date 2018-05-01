<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:bundle basename="omis.warrant.msgs.warrant">
<table id="warrantNoteTable" class="editTable">
	<thead>
		<tr>
			<th class="operations"><a class="actionMenuItem" id="warrantCauseViolationItemsActionMenuLink" href="${pageContext.request.contextPath}/warrant/warrantCauseViolationItemsActionMenu.html?violationToWitItemIndex=${violationToWitItemIndex}&offender=${offender.id}"></a></th>
			<th><fmt:message key="conditionClauseTableHeader"/></th>
			<th><fmt:message key="courtCasesHeader"/></th>
			<th><fmt:message key="descriptionTableHeader"/></th>
		</tr>
	</thead>
	<tbody id="warrantCauseViolationTableBody">
		<jsp:include page="warrantCauseViolationTableBodyContent.jsp"/>
	</tbody>
</table>
</fmt:bundle>