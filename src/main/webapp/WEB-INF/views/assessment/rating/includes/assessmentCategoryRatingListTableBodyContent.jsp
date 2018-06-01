<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:bundle basename="omis.assessment.msgs.assessmentRating">
<c:forEach var="summary" items="${assessmentCategoryScoreSummaries}">
<tr>
	<td><a class="actionMenuItem rowActionMenuItem" href="${pageContext.request.contextPath}/assessment/rating/assessmentCategoryRatingsRowActionMenu.html?assessmentCategoryScore=${summary.assessmentCategoryScoreId}"></a></td>
	<td>
		<c:out value="${summary.ratingCategoryDescription}"/>
	</td>
	<td>
		<c:out value="${summary.ratingRankName}"/>
	</td>
	<td>
		<fmt:message key="rangeOverrideDisplayLabel">
			<fmt:param value="${summary.assessmentRatingMin}"/>
			<fmt:param value="${summary.assessmentRatingMax}"/>
			<fmt:param value="${summary.assessmentCategoryScoreValue}"/>
		</fmt:message>
	</td>
	<td>
		<c:out value="${summary.assessmentRatingDescription}"/>
	</td>
</tr>
</c:forEach>
</fmt:bundle>