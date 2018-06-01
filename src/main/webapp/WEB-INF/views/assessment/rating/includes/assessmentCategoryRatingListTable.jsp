<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<fmt:bundle basename="omis.assessment.msgs.assessmentRating">
<div>
	<table id="assessmentCategoryRatingSummariesTable" class="listTable">
		<thead>
			<tr>
				<th class="operations"></th>
				<th><fmt:message key="sectionLabel"/></th>
				<th><fmt:message key="ratingLabel"/></th>
				<th><fmt:message key="rangeLabel"/></th>
				<th><fmt:message key="descriptionLabel"/></th>
			</tr>
		</thead>
		<tbody>
			<jsp:include page="assessmentCategoryRatingListTableBodyContent.jsp"/>
		</tbody>
	</table>
	<div id="assessmentGraphContainer" class="accentDark">
		<c:choose>
			<c:when test="${not empty graphData}">
				<div id="graphError" class="graphMessage hidden">
					<fmt:message key="graphErrorLabel" />
				</div>
				<canvas id="assessmentGraph" class="graphCanvas"></canvas>
				<canvas id="assessmentGraphScaled" class="graphCanvas hiddenGraph"></canvas>
				<div id="toggleGraph">
					<fmt:message key="togglePercentageLabel" />
				</div>
			</c:when>
			<c:otherwise>
				<div class="graphMessage">
					<fmt:message key="noDataLabel" />
				</div>
			</c:otherwise>
		</c:choose>
	</div>
</div>
</fmt:bundle>