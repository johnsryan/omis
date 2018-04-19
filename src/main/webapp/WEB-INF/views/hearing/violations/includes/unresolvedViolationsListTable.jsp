<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:bundle basename="omis.hearing.msgs.hearing">
<table id="unresolvedViolationSummariesTable" class="listTable">
	<thead>
		<tr>
			<th><fmt:message key="eventCategoryLabel"/></th>
			<th class="date"><fmt:message key="eventDateLabel"/></th>
			<th><fmt:message key="eventDetailsLabel"/></th>
			<th><fmt:message key="violationDetailsLabel"/></th>
		</tr>
	</thead>
	<tbody>
		<jsp:include page="unresolvedViolationsListTableBodyContent.jsp"/>
	</tbody>
</table>
</fmt:bundle>