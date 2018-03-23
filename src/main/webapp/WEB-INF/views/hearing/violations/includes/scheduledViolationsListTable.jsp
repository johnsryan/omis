<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<fmt:bundle basename="omis.hearing.msgs.hearing">
<c:forEach var="summaryMap" items="${scheduledHearingViolationSummaries}" varStatus="status">
<a class="actionMenuItem" id="actionMenuLink" href="${pageContext.request.contextPath}/hearing/violations/scheduledViolationsActionMenu.html?hearing=${summaryMap.key.hearingId}"></a>
<span class="hearingDetails" >
	<span class="detailsLabel" >
		<fmt:message key="hearingDateLabel"/>
	</span>
	<span class="detail" >
		<fmt:formatDate value="${summaryMap.key.hearingDate}" pattern="MM/dd/yyyy" />
	</span>
</span>
<span class="hearingDetails" >
	<span class="detailsLabel" >
		<fmt:message key="hearingTypeLabel"/>
	</span>
	<span class="detail" >
		<fmt:message key="${summaryMap.key.category}CategoryLabel"/>
	</span>
</span>
<span class="hearingDetails" >
	<span class="detailsLabel" >
		<fmt:message key="locationLabel"/>
	</span>
	<span class="detail" >
		<c:out value="${summaryMap.key.locationName}"/>
	</span>
</span>
<span class="hearingDetails" >
	<span class="detailsLabel" >
		<fmt:message key="hearingStatusLabel"/>
	</span>
	<span class="detail" >
		<fmt:message key="${summaryMap.key.hearingStatusCategory}StatusLabel"/>
	</span>
</span>
<span class="hearingDetails" >
	<span class="detailsLabel" >
		<fmt:message key="statusDateLabel" />
	</span>
	<span class="detail" >
		<fmt:formatDate value="${summaryMap.key.hearingStatusDate}" pattern="MM/dd/yyyy" />
	</span>
</span>
<div class="scheduledViolations">
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
			<c:set var="scheduledViolationSummaries" value="${summaryMap.value}" scope="request" />
			<jsp:include page="scheduledViolationsListTableBodyContent.jsp"/>
		</tbody>
	</table>
</div>
</c:forEach>



</fmt:bundle>