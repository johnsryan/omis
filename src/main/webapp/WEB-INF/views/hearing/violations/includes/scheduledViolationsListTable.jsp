<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<fmt:bundle basename="omis.hearing.msgs.hearing">
	<table id="hearingSummariesTable${status.index}" class="listTable">
		<thead>
			<tr>
				<th class="operations"></th>
				<th class="date"><fmt:message key="hearingDateLabel"/></th>
				<th><fmt:message key="hearingTypeLabel"/></th>
				<th><fmt:message key="locationLabel"/></th>
				<th><fmt:message key="hearingStatusLabel"/></th>
				<th class="date"><fmt:message key="statusDateLabel"/></th>
			</tr>
		</thead>
		<tbody>
			<c:set var="summaries" value="${scheduledHearingViolationSummaries}" scope="request" />
			<jsp:include page="scheduledHearingsListTableBodyContent.jsp"/>
		</tbody>
	</table>
</fmt:bundle>