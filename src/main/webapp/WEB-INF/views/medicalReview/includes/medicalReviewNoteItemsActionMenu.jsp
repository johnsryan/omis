<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle" />
<fmt:bundle basename="omis.medicalreview.msgs.medicalReview">
	<ul>
		<sec:authorize access="hasRole('MEDICAL_REVIEW_CREATE') or hasRole('MEDICAL_REVIEW_EDIT') or hasRole('ADMIN')">
			<li>
				<a id="createMedicalReviewNoteItemLink" class="createLink" href="${pageContext.request.contextPath}/medicalReview/createMedicalReviewNoteItem.html?medicalReviewNoteItemIndex=${medicalReviewNoteItemIndex}"><span class="visibleLinkLabel"><fmt:message key="addMedicalReviewNoteLink"/></span></a>
			</li>
		</sec:authorize>
	</ul>
</fmt:bundle>