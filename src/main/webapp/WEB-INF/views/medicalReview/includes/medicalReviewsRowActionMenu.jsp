<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<%@ taglib uri="/WEB-INF/tld/omis.tld" prefix="omis" %>
<fmt:bundle basename="omis.medicalreview.msgs.medicalReview">
	<ul>
		<sec:authorize access="hasRole('MEDICAL_REVIEW_VIEW') or hasRole('ADMIN')">
			<li>
				<a class="viewEditLink" href="${pageContext.request.contextPath}/medicalReview/edit.html?medicalReview=${medicalReview.id}"><span class="visibleLinkLabel"><fmt:message key="viewEditLink" bundle="${commonBundle}"/></span></a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('MEDICAL_REVIEW_REMOVE') or hasRole('ADMIN')">
			<li>
				<a class="removeLink" href="${pageContext.request.contextPath}/medicalReview/remove.html?medicalReview=${medicalReview.id}"><span class="visibleLinkLabel"><fmt:message key="removeLink" bundle="${commonBundle}"/></span></a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('MEDICAL_REVIEW_VIEW') or hasRole('ADMIN')">			
		<c:if test="${not empty medicalReview}">
			<li>
				<a href="${pageContext.request.contextPath}/medicalReview/medicalReviewDetailsReport.html?medicalReview=${medicalReview.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="medicalReviewDetailsReportLinkLabel"/></a>
			</li>
		</c:if>	
		</sec:authorize>	
	</ul>
</fmt:bundle>