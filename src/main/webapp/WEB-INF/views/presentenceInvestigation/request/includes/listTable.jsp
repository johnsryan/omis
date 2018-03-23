<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<fmt:bundle basename="omis.presentenceinvestigation.msgs.presentenceInvestigationRequest">
<table id="presentenceInvesigationRequestSummariesTable" class="listTable">
	<thead>
		<tr>
			<th class="operations"></th>
			<th><fmt:message key="courtCaseLabel"/></th>
			<th><fmt:message key="requestDateLabel"/></th>
			<th><fmt:message key="expectedCompletionDateLabel"/></th>
			<th><fmt:message key="statusLabel"/></th>
			<th><fmt:message key="submissionDateLabel"/></th>
			<th><fmt:message key="categoryLabel"/></th>
			<c:choose>
				<c:when test="${empty offender}">
					<th><fmt:message key="offenderLabel"/></th>
				</c:when>
				<c:when test="${not empty offender}">
					<th><fmt:message key="assignedUserLabel"/></th>
				</c:when>
			</c:choose>
			<th><fmt:message key="sentenceDateLabel"/></th>
		</tr>
	</thead>
	<tbody>
		<jsp:include page="listTableBodyContent.jsp"/>
	</tbody>
</table>
</fmt:bundle>