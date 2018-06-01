<%-- Author: Stephen Abson --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle basename="omis.person.msgs.person" var="personBundle"/>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle" />
<fmt:bundle basename="omis.offender.msgs.alternativeOffenderIdentity">
<ul>
	<li><sec:authorize access="hasRole('OFFENDER_EDIT') or hasRole('ADMIN')">
		<a class="personalDetailsLink" href="${pageContext.request.contextPath}/offender/personalDetails/edit.html?offender=${offender.id}">
			<fmt:message key="editPersonHeader" bundle="${personBundle}"/></a></sec:authorize></li>
			<li><sec:authorize access="hasRole('OFFENDER_ALT_IDENTITY_CREATE') or hasRole('ADMIN')">
		<a class="createLink" href="${pageContext.request.contextPath}/offender/identity/alternative/create.html?offender=${offender.id}">
			<fmt:message key="createAlterntaiveOffenderIdentityTitle"/></a></sec:authorize></li>	
		<sec:authorize access="(hasRole('OFFENDER_ALT_IDENTITY_VIEW') and hasRole('OFFENDER_SSN_VIEW')) or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
			<li>
				<a href="${pageContext.request.contextPath}/offender/identity/alternative/altIdentityListingFullReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="altIdentityListingFullReportLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>
		<sec:authorize access="hasRole('OFFENDER_ALT_IDENTITY_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
			<li>
				<a href="${pageContext.request.contextPath}/offender/identity/alternative/altIdentityListingRedactedReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="altIdentityListingRedactedReportLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>
</ul>
</fmt:bundle>