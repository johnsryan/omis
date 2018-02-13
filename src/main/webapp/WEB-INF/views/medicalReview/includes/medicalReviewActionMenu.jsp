<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle" />
<fmt:bundle basename="omis.medicalreview.msgs.medicalReview">
	<ul>
		<sec:authorize access="hasRole('MEDICAL_REVIEW_LIST') or hasRole('ADMIN')">
			<li>
				<a class="listLink" href="${pageContext.request.contextPath}/medicalReview/list.html?offender=${offender.id}"><span class="visibleLinkLabel"><fmt:message key="viewMedicalReviewsLink"/></span></a>
			</li>
		</sec:authorize>
	</ul>
</fmt:bundle>