<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.hearing.msgs.hearing">
<c:forEach var="summary" items="${summaries}" varStatus="status">
<tr>
	<td><a class="actionMenuItem" id="actionMenuLink" href="${pageContext.request.contextPath}/hearing/violations/scheduledViolationsRowActionMenu.html?hearing=${summary.key.hearingId}"></a></td>
	<td>
		<fmt:formatDate value="${summary.key.hearingDate}" pattern="MM/dd/yyyy" />
	</td>
	<td>
		<fmt:message key="${summary.key.category}CategoryLabel"/>
	</td>
	<td>
		<c:out value="${summary.key.locationName}"/>
	</td>
	<td>
		<fmt:message key="${summary.key.hearingStatusCategory}StatusLabel"/>
	</td>
	<td>
		<fmt:formatDate value="${summary.key.hearingStatusDate}" pattern="MM/dd/yyyy" />
	</td>
</tr>
<tr>
	<td></td>
	<td colspan="5">
		<table id="scheduledViolationSummariesTable${status.index}" class="listTable">
			<thead>
				<tr>
					<th><fmt:message key="eventCategoryLabel"/></th>
					<th><fmt:message key="eventDateLabel"/></th>
					<th><fmt:message key="eventDetailsLabel"/></th>
					<th><fmt:message key="violationDetailsLabel"/></th>
				</tr>
			</thead>
			<tbody>
				<c:set var="scheduledViolationSummaries" value="${summary.value}" scope="request" />
				<jsp:include page="scheduledViolationsListTableBodyContent.jsp"/>
			</tbody>
		</table>
	</td>
</tr>
</c:forEach>
</fmt:bundle>	