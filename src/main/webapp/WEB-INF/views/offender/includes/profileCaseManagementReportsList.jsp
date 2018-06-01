<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="/WEB-INF/tld/omis.tld" prefix="omis" %>
<fmt:bundle basename="omis.offender.msgs.profile">
	<sec:authorize access="hasRole('OFFENDER_PROFILE_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
				<span>
					<omis:reportPro reportPath="/CaseManagement/Contact_Letter&DOC_ID=${offender.offenderNumber}" decorate="no" title="" className="newTab reportLink"><fmt:message key="contactLetterLinkLabel"/></omis:reportPro>
				</span>
			</c:if>
		</sec:authorize>
		<sec:authorize access="hasRole('OFFENDER_PROFILE_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
				<span>
					<a href="${pageContext.request.contextPath}/offender/profileInstitutionalCasePlanReport.rtf?offender=${offender.id}&reportFormat=RTF" class="newTab msWordReportLink"><fmt:message key="institutionalCasePlanLinkLabel"/></a>
				</span>
			</c:if>
		</sec:authorize>			
		<sec:authorize access="hasRole('OFFENDER_PROFILE_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
				<span>
					<a href="${pageContext.request.contextPath}/offender/profileOffenderContactReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab adobeReportLink"><fmt:message key="offenderContactReportLinkLabel"/></a>
				</span>
			</c:if>
		</sec:authorize>
		<sec:authorize access="hasRole('OFFENDER_PROFILE_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
				<span>
					<a href="${pageContext.request.contextPath}/offender/profileSignUpReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab adobeReportLink"><fmt:message key="signUpReportLinkLabel"/></a>
				</span>
			</c:if>
		</sec:authorize>
		<sec:authorize access="hasRole('OFFENDER_PROFILE_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
				<span>
					<a href="${pageContext.request.contextPath}/offender/profileTransitionalCasePlanReport.rtf?offender=${offender.id}&reportFormat=RTF" class="newTab msWordReportLink"><fmt:message key="transitionalCasePlanLinkLabel"/></a>
				</span>
			</c:if>
		</sec:authorize>
</fmt:bundle>