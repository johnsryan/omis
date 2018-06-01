<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:bundle basename="omis.assessment.msgs.assessmentRating">
	<ul>
		<c:if test="${empty assessmentCategoryOverride}">
			<sec:authorize access="hasRole('ASSESSMENT_RATING_CREATE') or hasRole('ADMIN')">
				<li>
					<a class="viewEditLink" href="${pageContext.request.contextPath}/assessment/rating/create.html?assessmentCategoryScore=${assessmentCategoryScore.id}"><span class="visibleLinkLabel"><fmt:message key="createOverrideLink" /></span></a>
				</li>
			</sec:authorize>
		</c:if>
		<c:if test="${not empty assessmentCategoryOverride}">
			<sec:authorize access="hasRole('ASSESSMENT_RATING_VIEW') or hasRole('ADMIN')">
				<li>
					<a class="viewEditLink" href="${pageContext.request.contextPath}/assessment/rating/edit.html?assessmentCategoryOverride=${assessmentCategoryOverride.id}"><span class="visibleLinkLabel"><fmt:message key="viewEditOverrideLink" /></span></a>
				</li>
			</sec:authorize>
			<sec:authorize access="hasRole('ASSESSMENT_RATING_REMOVE') or hasRole('ADMIN')">
				<li>
					<a class="removeLink" href="${pageContext.request.contextPath}/assessment/rating/remove.html?assessmentCategoryOverride=${assessmentCategoryOverride.id}"><span class="visibleLinkLabel"><fmt:message key="removeOverrideLink" /></span></a>
				</li>
			</sec:authorize>
		</c:if>
	</ul>
</fmt:bundle>