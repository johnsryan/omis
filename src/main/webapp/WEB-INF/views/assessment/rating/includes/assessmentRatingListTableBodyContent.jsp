<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:bundle basename="omis.assessment.msgs.assessmentRating">
<c:forEach var="summary" items="${assessmentRatingSummaries}">
<c:choose>
	<c:when test="${summary.assessmentCategoryScorePertinent eq 'TRUE'}">
		<c:set value="${summary.assessmentCategoryScorePertinent}" var="pertinent"/>
	</c:when>
	<c:otherwise>
		<c:set value="" var="pertinent"/>
	</c:otherwise>
</c:choose>
<tr ${pertinent ? 'class=\"pertinent accentRegular\"' : ''}>
	<td><c:choose>
			<c:when test="${pertinent}"><a class="actionMenuItem rowActionMenuItem" href="${pageContext.request.contextPath}/assessment/rating/assessmentRatingsRowActionMenu.html?assessmentCategoryScore=${summary.assessmentCategoryScoreId}"></a></c:when>
		</c:choose>
	</td>
	<td>
		<c:out value="${summary.rankName}"/>
	</td>
	<td>
		<c:out value="${summary.assessmentRatingDescription}"/>
	</td>
	<td>
		<c:choose>
			<c:when test="${pertinent}">
				<fmt:message key="rangeOverrideDisplayLabel">
					<fmt:param value="${summary.assessmentRatingMin}"/>
					<fmt:param value="${summary.assessmentRatingMax}"/>
					<fmt:param value="${summary.assessmentCategoryScoreValue}"/>
				</fmt:message>
			</c:when>
			<c:otherwise>
				<fmt:message key="rangeDisplayLabel">
					<fmt:param value="${summary.assessmentRatingMin}"/>
					<fmt:param value="${summary.assessmentRatingMax}"/>
				</fmt:message>
			</c:otherwise>
		</c:choose>
	</td>
	<td>
		<c:choose>
			<c:when test="${summary.override eq 'TRUE'}">
				<c:out value="foreach categoryOverrideReasonNames"/>
			</c:when>
		</c:choose>
	</td>
</tr>
</c:forEach>
</fmt:bundle>