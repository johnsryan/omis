<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="/WEB-INF/tld/omis.tld" prefix="omis" %>
<fmt:bundle basename="omis.detainernotification.msgs.detainerNotification">
	<ul>
		<sec:authorize access="hasRole('DETAINER_NOTIFICATION_VIEW') or hasRole('ADMIN')">
			<li>
				<a class="viewEditLink" href="${pageContext.request.contextPath}/detainerNotification/edit.html?detainer=${detainer.id}&offender=${offender.id}"><span class="visibleLinkLabel"><fmt:message key="editDetainerLink"/></span></a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('DETAINER_NOTIFICATION_REMOVE') or hasRole('ADMIN')">
			<li>
				<a class="removeLink" href="${pageContext.request.contextPath}/detainerNotification/remove.html?detainer=${detainer.id}&offender=${offender.id}"><span class="visibleLinkLabel"><fmt:message key="removeDetainerLink"/></span></a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('DETAINER_NOTIFICATION_LIST') or hasRole('ADMIN')">
			<li>
				<omis:reportPro reportPath="/Legal/DetainersAndNotifications/Agreement_on_Detainer_Cover_Letter&DETAINER_ID=${detainer.id}" decorate="no" title="" className="newTab reportLink"><fmt:message key="agreementOnDetainerCoverLetterReportLinkLabel"/></omis:reportPro>
			</li>
			<li>
				<omis:reportPro reportPath="/Legal/DetainersAndNotifications/Agreement_on_Detainer_Form_I&DETAINER_ID=${detainer.id}" decorate="no" title="" className="newTab reportLink"><fmt:message key="agreementOnDetainerFormIReportLinkLabel"/></omis:reportPro>
			</li>
			<li>
				<omis:reportPro reportPath="/Legal/DetainersAndNotifications/Agreement_on_Detainer_Form_II&DETAINER_ID=${detainer.id}" decorate="no" title="" className="newTab reportLink"><fmt:message key="agreementOnDetainerFormIIReportLinkLabel"/></omis:reportPro>
			</li>
			<li>
				<omis:reportPro reportPath="/Legal/DetainersAndNotifications/Agreement_on_Detainer_Form_III&DETAINER_ID=${detainer.id}" decorate="no" title="" className="newTab reportLink"><fmt:message key="agreementOnDetainerFormIIIReportLinkLabel"/></omis:reportPro>
			</li>
			<li>
				<omis:reportPro reportPath="/Legal/DetainersAndNotifications/Agreement_on_Detainer_Form_IV&DETAINER_ID=${detainer.id}" decorate="no" title="" className="newTab reportLink"><fmt:message key="agreementOnDetainerFormIVReportLinkLabel"/></omis:reportPro>
			</li>
			<li>
				<omis:reportPro reportPath="/Legal/DetainersAndNotifications/Agreement_on_Detainer_Form_VA&DETAINER_ID=${detainer.id}" decorate="no" title="" className="newTab reportLink"><fmt:message key="agreementOnDetainerFormVAReportLinkLabel"/></omis:reportPro>
			</li>
			<li>
				<a href="${pageContext.request.contextPath}/detainerNotification/detainerNotificationDetailsReport.html?detainer=${detainer.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="detainerNotificationDetailsReportLinkLabel"/></a>
			</li>
		</sec:authorize>
	</ul>
</fmt:bundle>