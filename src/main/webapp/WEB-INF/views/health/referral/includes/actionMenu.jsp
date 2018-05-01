<%--
 - Author: Ryan Johns
 - Author: Stephen Abson
 - Author: Sheronda Vaughn
 - Version: 0.1.0 (Mar 31, 2014)
 - Since: OMIS 3.0
 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="/WEB-INF/tld/omis.tld" prefix="omis" %>
<fmt:bundle basename="omis.health.msgs.health">
<ul>
	<li><a href="${pageContext.request.contextPath}/health/referral/referralCenter.html?facility=${facility.id}&amp;referralType=${referralType.name}" id="refreshListLink"><fmt:message key="refreshLink" bundle="${commonBundle}"/></a></li>
	<sec:authorize access="hasRole('ADMIN') or hasRole('HEALTH_ADMIN') or hasRole('PROVIDER_VIEW')">
		<li><a href="${pageContext.request.contextPath}/health/provider/list.html?facility=${facility.id}" id="providerListLink"><fmt:message key="listProvidersLabel"/></a></li>
	</sec:authorize>
	<li><a href="${pageContext.request.contextPath}/health/referral/changeFacilityMenu.html?facility=${facility.id}&amp;referralType=${referralType.name}" id="facilityListLink" class="actionItem"><fmt:message key="changeFacilityLabel"/></a></li>

	<li><omis:reportPro reportPath="/Medical/Call_Out_List" decorate="no" title="" className="newTab reportLink"><fmt:message key="callOutSheetReportLabel"/></omis:reportPro></li>
	<li><omis:reportPro reportPath="/Medical/Provider_Daily_Schedule" decorate="no" title="" className="newTab reportLink"><fmt:message key="dailyProviderListReport"/></omis:reportPro></li>
	<li><omis:reportPro reportPath="/Medical/Unit_Call_List" decorate="no" title="" className="newTab reportLink"><fmt:message key="unitCallListReportLabel"/></omis:reportPro></li>
	<li><omis:reportPro reportPath="/Medical/Health_External_Referral_Status_for_Offender" decorate="no" title="" className="newTab reportLink"><fmt:message key="externalReferralHistoryReportLabel"/></omis:reportPro></li>
	<li><omis:reportPro reportPath="/Medical/Lab_Notification_Report" decorate="no" title="" className="newTab reportLink"><fmt:message key="labNotificationReportLabel"/></omis:reportPro></li>
	<li><omis:reportPro reportPath="/Medical/Pending_Labs_Report" decorate="no" title="" className="newTab reportLink"><fmt:message key="pendingLabsReportLabel"/></omis:reportPro></li>
	<li><omis:reportPro reportPath="/Medical/External_Referral_Call_Out" decorate="no" title="" className="newTab reportLink"><fmt:message key="externalCallOutReportLabel"/></omis:reportPro></li>
	<li><omis:reportPro reportPath="/Medical/Call_Out_List___Labs" decorate="no" title="" className="newTab reportLink"><fmt:message key="labCallOutReportLabel"/></omis:reportPro></li>
    <li><omis:reportPro reportPath="/Health/Nursing_and_Provider_Progress_Notes" decorate="no" title="" className="newTab reportLink"><fmt:message key="NurseProviderProgressNotesReportLabel"/></omis:reportPro></li>
    <li><omis:reportPro reportPath="/Health/Inmate_Appointment_Slips" decorate="no" title="" className="newTab reportLink"><fmt:message key="InmateAppointmentSlipsReportLabel"/></omis:reportPro></li>
</ul>
</fmt:bundle>