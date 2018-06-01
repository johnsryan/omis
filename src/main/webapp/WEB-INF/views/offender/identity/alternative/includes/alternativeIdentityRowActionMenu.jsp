<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle" />
<fmt:bundle basename="omis.offender.msgs.alternativeOffenderIdentity">
	<ul>
		<sec:authorize access="hasRole('OFFENDER_ALT_IDENTITY_VIEW') or hasRole('ADMIN')">
			<li>
				<a class="viewEditLink" href="${pageContext.request.contextPath}/offender/identity/alternative/edit.html?alternativeIdentityAssociation=${alternativeIdentityAssociation.id}"><fmt:message key="viewEditLink" bundle="${commonBundle}"/></a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('OFFENDER_ALT_IDENTITY_REMOVE') or hasRole('ADMIN')">
			<li>	
				<a class="removeLink" href="${pageContext.request.contextPath}/offender/identity/alternative/remove.html?alternativeIdentityAssociation=${alternativeIdentityAssociation.id}"><fmt:message key="removeLink" bundle="${commonBundle}"/></a>
			</li>
		</sec:authorize>		
		<sec:authorize access="(hasRole('OFFENDER_ALT_IDENTITY_VIEW') and hasRole('OFFENDER_SSN_VIEW')) or hasRole('ADMIN')">
			<c:if test="${not empty alternativeIdentityAssociation}">
			<li>
				<a href="${pageContext.request.contextPath}/offender/identity/alternative/altIdentityDetailsFullReport.html?alternativeIdentityAssociation=${alternativeIdentityAssociation.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="altIdentityDetailsFullReportLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>
		<sec:authorize access="hasRole('OFFENDER_ALT_IDENTITY_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty alternativeIdentityAssociation}">
			<li>
				<a href="${pageContext.request.contextPath}/offender/identity/alternative/altIdentityDetailsRedactedReport.html?alternativeIdentityAssociation=${alternativeIdentityAssociation.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="altIdentityDetailsRedactedReportLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>
		
	</ul>
</fmt:bundle>