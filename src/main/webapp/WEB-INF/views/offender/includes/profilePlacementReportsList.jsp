<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="/WEB-INF/tld/omis.tld" prefix="omis" %>
<fmt:bundle basename="omis.offender.msgs.profile">
	<sec:authorize access="hasRole('OFFENDER_PROFILE_VIEW') or hasRole('ADMIN')">
		<c:if test="${not empty offender}">
			<span>
				<a href="${pageContext.request.contextPath}/offender/profileBasicDoorCardReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab adobeReportLink"><fmt:message key="basicDoorCardReportLinkLabel"/></a>
			</span>
			<span>
				<omis:reportPro reportPath="/Placement/BedAssignment/Cell_Check_In_Check_Out_Form&DOC_ID=${offender.offenderNumber}" decorate="no" title="" className="newTab reportLink"><fmt:message key="cellCheckInCheckOutFormReportLinkLabel"/></omis:reportPro>
			</span>
			<span>
				<a href="${pageContext.request.contextPath}/offender/profileDetailedDoorCardReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab adobeReportLink"><fmt:message key="detailedDoorCardReportLinkLabel"/></a>
			</span>
			<span>
				<a href="${pageContext.request.contextPath}/offender/profileInternalExternalMovementReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab adobeReportLink"><fmt:message key="internalExternalMovementReportLinkLabel"/></a>
			</span>	
			<span>
				<a href="${pageContext.request.contextPath}/offender/profileOffenderLocationHistoryReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab adobeReportLink"><fmt:message key="offenderLocationHistoryReportLinkLabel"/></a>
			</span>	
			<span>
				<omis:reportPro reportPath="/Placement/BedAssignment/Locked_Housing_Check_In_Check_Out&DOC_ID=${offender.offenderNumber}" decorate="no" title="" className="newTab reportLink"><fmt:message key="lockedHousingCheckInCheckOutReportLinkLabel"/></omis:reportPro>
			</span>
			<span>
				<a href="${pageContext.request.contextPath}/offender/profilePhttpDoorCardReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab adobeReportLink"><fmt:message key="phttpDoorCardReportLinkLabel"/></a>
			</span>						
		</c:if>	
	</sec:authorize>
	<sec:authorize access="(hasRole('OFFENDER_VIEW') and hasRole('OFFENDER_SSN_VIEW')) or hasRole('ADMIN')">
		<c:if test="${not empty offender}">
			<span>
				<a href="${pageContext.request.contextPath}/offender/profilePrisonIntakeReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab adobeReportLink"><fmt:message key="prisonIntakeReportLinkLabel"/></a>
			</span>
		</c:if>
	</sec:authorize>
</fmt:bundle>