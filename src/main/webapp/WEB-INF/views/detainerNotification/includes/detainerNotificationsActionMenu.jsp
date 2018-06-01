<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="/WEB-INF/tld/omis.tld" prefix="omis" %>
<fmt:bundle basename="omis.detainernotification.msgs.detainerNotification">
	<ul>
		<sec:authorize access="hasRole('DETAINER_NOTIFICATION_CREATE') or hasRole('ADMIN')">
			<li>
				<a class="createLink" href="${pageContext.request.contextPath}/detainerNotification/create.html?offender=${offender.id}"><span class="visibleLinkLabel"><fmt:message key="createDetainerNotificationLink"/></span></a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('DETAINER_NOTIFICATION_LIST') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
				<li>
					<omis:reportPro reportPath="/Legal/DetainersAndNotifications/Arson_Admit_Notice&DOC_ID=${offender.offenderNumber}" decorate="no" title="" className="newTab reportLink"><fmt:message key="arsonAdmitNoticeReportLinkLabel"/></omis:reportPro>
				</li>
				<li>
					<omis:reportPro reportPath="/Legal/DetainersAndNotifications/Arson_Release_Notice&DOC_ID=${offender.offenderNumber}" decorate="no" title="" className="newTab reportLink"><fmt:message key="arsonReleaseNoticeReportLinkLabel"/></omis:reportPro>
				</li>
				<li>
					<omis:reportPro reportPath="/Legal/DetainersAndNotifications/Escape_Notification&DOC_ID=${offender.offenderNumber}" decorate="no" title="" className="newTab reportLink"><fmt:message key="escapeNotificationReportLinkLabel"/></omis:reportPro>
				</li>
				<li>
					<omis:reportPro reportPath="/Legal/DetainersAndNotifications/Generic_Notification&DOC_ID=${offender.offenderNumber}" decorate="no" title="" className="newTab reportLink"><fmt:message key="genericNotificationReportLinkLabel"/></omis:reportPro>
				</li>
				<li>
					<omis:reportPro reportPath="/Legal/DetainersAndNotifications/Inmate_Release_Notification&DOC_ID=${offender.offenderNumber}" decorate="no" title="" className="newTab reportLink"><fmt:message key="inmateReleaseNotificationReportLinkLabel"/></omis:reportPro>
				</li>
				<li>
					<omis:reportPro reportPath="/Legal/DetainersAndNotifications/Notification_of_Inmate_Passing&DOC_ID=${offender.offenderNumber}" decorate="no" title="" className="newTab reportLink"><fmt:message key="notificationOfInmatePassingReportLinkLabel"/></omis:reportPro>
				</li>
				<li>
					<a href="${pageContext.request.contextPath}/detainerNotification/detainerNotificationListingReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="detainerNotificationListingReportLinkLabel"/></a>
				</li>				
			</c:if>
		</sec:authorize>
	</ul>
</fmt:bundle>