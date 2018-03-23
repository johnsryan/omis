<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle" />
<fmt:bundle basename="omis.assessment.msgs.assessmentNotes">
	<ul>
		<sec:authorize access="hasRole('ASSESSMENT_CREATE') or hasRole('ASSESSMENT_EDIT') or hasRole('ADMIN')">
			<li>
				<a id="createAssessmentNoteItemLink" class="createLink" href="${pageContext.request.contextPath}/assessment/notes/createAssessmentNoteItem.html?assessmentNoteItemIndex=${assessmentNoteItemIndex}"><span class="visibleLinkLabel"><fmt:message key="addAssessmentNoteLink"/></span></a>
			</li>
		</sec:authorize>
	</ul>
</fmt:bundle>