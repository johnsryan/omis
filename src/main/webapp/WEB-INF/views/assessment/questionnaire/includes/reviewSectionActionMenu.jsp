<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:bundle basename="omis.questionnaire.msgs.questionnaire">
	<ul>
		<sec:authorize access="hasRole('QUESTIONNAIRE_VIEW') or hasRole('ADMIN')">
			<li>
				<a class="listLink" href="${pageContext.request.contextPath}/assessment/questionnaire/section.html?sectionStatus=${questionnaireSectionStatus.id}"><span class="visibleLinkLabel"><fmt:message key="viewSectionLink"/></span></a>
			</li>
		</sec:authorize>
	</ul>
</fmt:bundle>