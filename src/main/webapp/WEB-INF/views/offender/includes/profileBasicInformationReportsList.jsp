<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:bundle basename="omis.offender.msgs.profile">
	<sec:authorize access="(hasRole('OFFENDER_VIEW') and hasRole('OFFENDER_SSN_VIEW')) or hasRole('ADMIN')">
		<c:if test="${not empty offender}">
		<span>
			<a href="${pageContext.request.contextPath}/offender/profileBasicInfoAltSecReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab adobeReportLink"><fmt:message key="basicInfoAltSecReportLinkLabel"/></a>
		</span>
		</c:if>
	</sec:authorize>
	<sec:authorize access="hasRole('OFFENDER_VIEW') or hasRole('ADMIN')">
		<c:if test="${not empty offender}">
		<span>
			<a href="${pageContext.request.contextPath}/offender/profileBasicInfoAltSecRedactedReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab adobeReportLink"><fmt:message key="basicInfoAltSecRedactedReportLinkLabel"/></a>
		</span>
		</c:if>
	</sec:authorize>
	<sec:authorize access="(hasRole('OFFENDER_VIEW') and hasRole('OFFENDER_SSN_VIEW')) or hasRole('ADMIN')">
		<c:if test="${not empty offender}">
		<span>
			<a href="${pageContext.request.contextPath}/offender/profileBasicInfoFacilityReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab adobeReportLink"><fmt:message key="basicInfoFacilityReportLinkLabel"/></a>
		</span>
		</c:if>
	</sec:authorize>
	<sec:authorize access="hasRole('OFFENDER_VIEW') or hasRole('ADMIN')">
		<c:if test="${not empty offender}">
		<span>
			<a href="${pageContext.request.contextPath}/offender/profileBasicInfoFacilityRedactedReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab adobeReportLink"><fmt:message key="basicInfoFacilityRedactedReportLinkLabel"/></a>
		</span>
		</c:if>
	</sec:authorize>
	<sec:authorize access="(hasRole('OFFENDER_VIEW') and hasRole('OFFENDER_SSN_VIEW')) or hasRole('ADMIN')">
		<c:if test="${not empty offender}">
		<span>
			<a href="${pageContext.request.contextPath}/offender/profileBasicInfoProbationParoleReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab adobeReportLink"><fmt:message key="basicInfoProbationParoleReportLinkLabel"/></a>
		</span>
		</c:if>
	</sec:authorize>
	<sec:authorize access="hasRole('OFFENDER_VIEW') or hasRole('ADMIN')">
		<c:if test="${not empty offender}">
		<span>
			<a href="${pageContext.request.contextPath}/offender/profileBasicInfoProbationParoleRedactedReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab adobeReportLink"><fmt:message key="basicInfoProbationParoleRedactedReportLinkLabel"/></a>
		</span>
		</c:if>
	</sec:authorize>
	<sec:authorize access="(hasRole('OFFENDER_VIEW') and hasRole('OFFENDER_SSN_VIEW')) or hasRole('ADMIN')">
		<c:if test="${not empty offender}">
		<span>
			<a href="${pageContext.request.contextPath}/offender/profileBasicInfoSheetReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab adobeReportLink"><fmt:message key="basicInformationSheetReportLinkLabel"/></a>
		</span>
		</c:if>
	</sec:authorize>
	<sec:authorize access="hasRole('OFFENDER_VIEW') or hasRole('ADMIN')">
		<c:if test="${not empty offender}">
		<span>
			<a href="${pageContext.request.contextPath}/offender/profileBasicInfoSheetRedactedReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab adobeReportLink"><fmt:message key="basicInformationSheetRedactedReportLinkLabel"/></a>
		</span>
		</c:if>
	</sec:authorize>
	<sec:authorize access="hasRole('OFFENDER_VIEW') or hasRole('ADMIN')">
		<c:if test="${not empty offender}">
		<span>
			<a href="${pageContext.request.contextPath}/offender/profileBasicInfocjinBackgroundCheckReport.rtf?offender=${offender.id}&reportFormat=RTF" class="msWordReportLink"><fmt:message key="basicInfoCJINBackgroundCheckReportLinkLabel"/></a>
		</span>
		</c:if>
	</sec:authorize>	
	<sec:authorize access="hasRole('OFFENDER_VIEW') or hasRole('ADMIN')">
		<c:if test="${not empty offender}">
		<span>
			<a href="${pageContext.request.contextPath}/offender/profileWantedPosterReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab adobeReportLink"><fmt:message key="wantedPosterReportLinkLabel"/></a>
		</span>
		</c:if>
	</sec:authorize>
</fmt:bundle>