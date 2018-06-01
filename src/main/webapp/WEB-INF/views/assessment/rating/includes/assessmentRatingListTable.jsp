<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:bundle basename="omis.assessment.msgs.assessmentRating">
<table id="assessmentRatingSummariesTable" class="listTable">
	<thead>
		<tr>
			<th class="operations"></th>
			<th><fmt:message key="ratingLabel"/></th>
			<th><fmt:message key="descriptionLabel"/></th>
			<th><fmt:message key="rangeLabel"/></th>
			<th><fmt:message key="reasonsLabel"/></th>
		</tr>
	</thead>
	<tbody>
		<jsp:include page="assessmentRatingListTableBodyContent.jsp"/>
	</tbody>
</table>
<br/>
</fmt:bundle>