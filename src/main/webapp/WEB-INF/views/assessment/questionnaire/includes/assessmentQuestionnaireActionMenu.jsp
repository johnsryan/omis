<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:bundle basename="omis.questionnaire.msgs.questionnaire">
	<ul>
		<sec:authorize access="hasRole('QUESTIONNAIRE_VIEW') or hasRole('ADMIN')">
			<li>
				<a class="viewEditLink" href="${pageContext.request.contextPath}/assessment/questionnaire/review.html?administeredQuestionnaire=${administeredQuestionnaire.id}"><span class="visibleLinkLabel"><fmt:message key="completeLink" /></span></a>
			</li>
		</sec:authorize>
	</ul>
</fmt:bundle>