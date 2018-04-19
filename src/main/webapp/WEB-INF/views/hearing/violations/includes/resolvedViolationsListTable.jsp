<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:bundle basename="omis.hearing.msgs.hearing">
<table id="resolvedViolationSummariesTable" class="listTable">
	<thead>
		<tr>
			<th class="operations" />
			<th><fmt:message key="eventCategoryLabel"/></th>
			<th class="date"><fmt:message key="eventDateLabel"/></th>
			<th><fmt:message key="violationDetailsLabel"/></th>
			<th><fmt:message key="reasonForDecisionLabel"/></th>
			<th><fmt:message key="dispositionLabel"/></th>
			<th><fmt:message key="resolutionLabel"/></th>
			<th class="date"><fmt:message key="appealDateLabel"/></th>
			<th><fmt:message key="sanctionLabel"/></th>
		</tr>
	</thead>
	<tbody>
		<jsp:include page="resolvedViolationsListTableBodyContent.jsp"/>
	</tbody>
</table>
</fmt:bundle>