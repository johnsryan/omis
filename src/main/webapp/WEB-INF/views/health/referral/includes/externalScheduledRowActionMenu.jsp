<%--
 - Author: Josh Divine
 - Version: 0.1.0 (Mar 21, 2017)
 - Since: OMIS 3.0
 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.health.msgs.health">
	<ul>
		<li><a href="${pageContext.request.contextPath}/health/referral/external/assess.html?externalReferral=${scheduled.id}" class="healthAssessLink" title="<fmt:message key='assessLabel'/>"><span class="visibleLinkLabel"><fmt:message key="assessLabel"/></span></a></li>
		<li><a href="${pageContext.request.contextPath}/health/referral/external/authorized/reschedule.html?externalReferral=${scheduled.id}" class="calendarLink" title="<fmt:message key='recheduleReferralLink'/>"><span class="visibleLinkLabel"><fmt:message key="recheduleReferralLink"/></span></a></li>
		<li><a href="${pageContext.request.contextPath}/health/referral/external/authorized/editSchedule.html?externalReferral=${scheduled.id}" class="viewEditLink" title="<fmt:message key='editReferralScheduleLink'/>"><span class="visibleLinkLabel"><fmt:message key="editReferralScheduleLink"/></span></a></li>
		<li><a href="${pageContext.request.contextPath}/health/referral/external/cancel.html?externalReferral=${scheduled.id}" title="<fmt:message key='cancelLabel' bundle='${commonBundle}'/>"class="cancelLink"><span class="visibleLinkLabel"><fmt:message key="cancelLabel" bundle="${commonBundle}"/></span></a></li>
		<li><a href="${pageContext.request.contextPath}/health/referral/external/clinicalServicesReferralForm.rtf?externalReferral=${scheduled.id}&reportFormat=RTF" title="<fmt:message key='clinicalServicesReferralFormLink'/>"class="msWordReportLink"><span class="visibleLinkLabel"><fmt:message key="clinicalServicesReferralFormLink"/></span></a></li>
		<li><a href="${pageContext.request.contextPath}/health/referral/external/providerBillingMemo.rtf?externalReferral=${scheduled.id}&reportFormat=RTF" title="<fmt:message key='providerBillingMemoLink'/>"class="msWordReportLink"><span class="visibleLinkLabel"><fmt:message key="providerBillingMemoLink"/></span></a></li>
	</ul>
</fmt:bundle>