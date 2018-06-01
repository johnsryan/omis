<!-- 
 - Author: Josh Divine
 - Version: 0.1.1 (June 23, 2017)
 - Since: OMIS 3.0
 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="/WEB-INF/tld/omis.tld" prefix="omis" %>
<fmt:bundle basename="omis.offender.msgs.profile">
	<ul>
		<sec:authorize access="hasRole('OFFENDER_PROFILE_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
				<li>
					<a href="${pageContext.request.contextPath}/offender/profileClinicalServicesReferralFormReport.rtf?offender=${offender.id}&reportFormat=RTF" class="msWordReportLink"><fmt:message key="clinicalServicesReferralFormReportLinkLabel"/></a>
				</li>
				<li>
					<%--<a href="${pageContext.request.contextPath}/offender/profileProviderBillingMemoReport.rtf?offender=${offender.id}&reportFormat=RTF" class="reportLink"><fmt:message key="providerBillingMemoReportLinkLabel"/></a> --%>
					<omis:reportPro reportPath="/Health/Provider_Billing_Memo_OMIS_Version&DOC_ID=${offender.offenderNumber}" decorate="no" title="" className="newTab reportLink"><fmt:message key="providerBillingMemoReportLinkLabel"/></omis:reportPro>
				</li>
				<li>
					<a href="${pageContext.request.contextPath}/offender/profileDenialDOCBillingResponsibilityReport.html?offender=${offender.id}&reportFormat=DOCX" class="msWordReportLink"><fmt:message key="denialDOCBillingResponsibilityReportLinkLabel"/></a>
				</li>
				<li>
					<a href="${pageContext.request.contextPath}/offender/profileResponsibilityForMedicalBillReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab adobeReportLink"><fmt:message key="responsibilityForMedicalBillReportLinkLabel"/></a>
				</li>
			</c:if>
		</sec:authorize>
	</ul>
</fmt:bundle>