<%--
 - Displays scheduled referrals table body content, regardless of type.
 -
 - Author: Stephen Abson
 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.health.msgs.health">
<c:forEach var="scheduled" items="${scheduledReferrals}">
	<tr>
		<td>
			<c:choose>
				<c:when test="${'INTERNAL_MEDICAL' eq scheduled.type}">
					<a href="${pageContext.request.contextPath}/health/referral/internal/assess.html?internalReferral=${scheduled.id}" class="healthAssessLink" title="<fmt:message key='assessLabel'/>"><span class="linkLabel"><fmt:message key="assessLabel"/></span></a>
					<a href="${pageContext.request.contextPath}/health/referral/internal/reschedule.html?internalReferral=${scheduled.id}&facility=${facility.id}" class="calendarLink" title="<fmt:message key='recheduleReferralLink'/>"><span class="linkLabel"><fmt:message key="recheduleReferralLink"/></span></a>
					<a href="${pageContext.request.contextPath}/health/referral/internal/editSchedule.html?internalReferral=${scheduled.id}" class="viewEditLink" title="<fmt:message key='editReferralScheduleLink'/>"><span class="linkLabel"><fmt:message key="editReferralScheduleLink"/></span></a>
					<a href="${pageContext.request.contextPath}/health/referral/internal/cancel.html?internalReferral=${scheduled.id}" title="<fmt:message key='cancelLabel' bundle='${commonBundle}'/>"class="cancelLink"><span class="linkLabel"><fmt:message key="cancelLabel" bundle="${commonBundle}"/></span></a>
				</c:when>
				<c:when test="${'EXTERNAL_MEDICAL' eq scheduled.type}">
					<a href="${pageContext.request.contextPath}/health/referral/external/assess.html?externalReferral=${scheduled.id}" class="healthAssessLink" title="<fmt:message key='assessLabel'/>"><span class="linkLabel"><fmt:message key="assessLabel"/></span></a>
					<a href="${pageContext.request.contextPath}/health/referral/external/authorized/reschedule.html?externalReferral=${scheduled.id}" class="calendarLink" title="<fmt:message key='recheduleReferralLink'/>"><span class="linkLabel"><fmt:message key="recheduleReferralLink"/></span></a>
					<a href="${pageContext.request.contextPath}/health/referral/external/authorized/editSchedule.html?externalReferral=${scheduled.id}" class="viewEditLink" title="<fmt:message key='editReferralScheduleLink'/>"><span class="linkLabel"><fmt:message key="editReferralScheduleLink"/></span></a>
					<a href="${pageContext.request.contextPath}/health/referral/external/cancel.html?externalReferral=${scheduled.id}" title="<fmt:message key='cancelLabel' bundle='${commonBundle}'/>"class="cancelLink"><span class="linkLabel"><fmt:message key="cancelLabel" bundle="${commonBundle}"/></span></a>
					<a href="${pageContext.request.contextPath}/health/referral/external/clinicalServicesReferralForm.rtf?externalReferral=${scheduled.id}&reportFormat=RTF" title="<fmt:message key='clinicalServicesReferralFormLink'/>"class="msWordReportLink"><span class="linkLabel"><fmt:message key="clinicalServicesReferralFormLink"/></span></a>
					<a href="${pageContext.request.contextPath}/health/referral/external/providerBillingMemo.rtf?externalReferral=${scheduled.id}&reportFormat=RTF" title="<fmt:message key='providerBillingMemoLink'/>"class="msWordReportLink"><span class="linkLabel"><fmt:message key="providerBillingMemoLink"/></span></a>
				</c:when>
				<c:when test="${'LAB' eq scheduled.type}">
					<a href="${pageContext.request.contextPath}/health/labWork/edit.html?labWork=${scheduled.id}&facility=${facility.id}" class="viewEditLink" title="<fmt:message key='editReferralScheduleLink'/>"><span class="linkLabel"><fmt:message key="editReferralScheduleLink"/></span></a>
					<a href="${pageContext.request.contextPath}/health/labWork/remove.html?labWork=${scheduled.id}&facility=${facility.id}" title="<fmt:message key='removeLink' bundle='${commonBundle}'/>"class="removeLink"><span class="linkLabel"><fmt:message key="removeLink" bundle="${commonBundle}"/></span></a>
				</c:when>
			</c:choose>
		</td>
		<c:if test="${empty referralType or 'ALL' eq referralType}">
		<td>
			<fmt:message key="referralTypeLabel.${scheduled.type.name}"/>
		</td>
		</c:if>
		<td>
			<c:out value="${scheduled.offenderLastName}"/>,
			<c:out value="${scheduled.offenderFirstName}"/>
			<c:out value="${scheduled.offenderMiddleName}"/> 
			(<c:out value="${scheduled.offenderNumber}"/>)
		</td>
		<td>
			<fmt:formatDate value="${scheduled.appointmentDate}" pattern="MM/dd/yyyy"/>
			<c:if test="${not empty scheduled.appointmentTime}">
				<fmt:formatDate value="${scheduled.appointmentTime}" pattern="h:mm a"/>
			</c:if>
		</td>
		<td>
		<c:if test="${scheduled.primaryProviderExists}">
			<c:out value="${scheduled.primaryProviderLastName}"/>,
			<c:out value="${scheduled.primaryProviderFirstName}"/>  
			<c:out value="${scheduled.primaryProviderTitleAbbreviation}"/>
		</c:if>
		</td>
		<c:if test="${referralType eq 'EXTERNAL_MEDICAL'}">
		<td>
			<c:out value="${scheduled.facilityName}"/>
		</td>
		</c:if>
		<td>
			<c:out value="${scheduled.reasonName}"/>
		</td>
		<td>
			<c:if test="${not empty scheduled.reasonNotes}">
				<a class="commentLink" title="<c:out value='${scheduled.reasonNotes}'/>"></a>
			</c:if>
		</td>
		<td>
			<c:if test="${not empty scheduled.locationDesignator}">
		    	<fmt:message key="referralLocationDesignatorLabel.${scheduled.locationDesignator.name}"/>
		    </c:if>
		</td>
		<td>
			<c:out value="${scheduled.unitName}"/>
		</td>
		<td>
			<c:if test="${not empty scheduled.schedulingNotes}">
				<a class="commentLink" title="<c:out value="${scheduled.schedulingNotes}"/>"></a>
			</c:if>
		</td>
	</tr>
</c:forEach>
</fmt:bundle>